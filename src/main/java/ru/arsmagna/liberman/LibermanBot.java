package ru.arsmagna.liberman;

import chat.tamtam.botapi.*;
import chat.tamtam.botapi.model.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import ru.arsmagna.IrbisConnection;
import ru.arsmagna.IrbisException;
import ru.arsmagna.search.SearchParameters;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.*;
import java.util.List;

public class LibermanBot {
    private static TamTamBotAPI api;
    private static BotInfo me;
    private static Thread poller;
    private static Thread consumerThread;
    private static final AtomicReference<Long> marker = new AtomicReference<>();
    private static final BlockingQueue<Update> updates = new ArrayBlockingQueue<>(100);
    private static final AtomicBoolean isStopped = new AtomicBoolean();
    private static final Update POISON_PILL = new Update(System.currentTimeMillis());

    private static void poll() {
        do {
            marker.set(pollOnce(marker.get()));
        } while (!Thread.currentThread().isInterrupted() && !isStopped.get());
    }

    private static Long pollOnce(Long marker) {
        int error = 0;
        try {
            UpdateList updateList = api.getUpdates().marker(marker).timeout(5).execute();
            for (Update update : updateList.getUpdates()) {
                updates.offer(update);
                System.out.println(update);
            }
            error = 0;
            return updateList.getMarker();
        } catch (Exception e) {
            if (e.getCause() instanceof InterruptedException) {
                return marker;
            }

            error++;
            System.out.println(e.getMessage());
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(Math.min(error, 5)));
            } catch (InterruptedException e1) {
                Thread.currentThread().interrupt();
            }
        }

        return marker;
    }

    private static IrbisConnection getConnection() throws IOException, IrbisException {
        IrbisConnection result = new IrbisConnection();
        result.host = "192.168.3.2";
        result.port = 6666;
        result.username = "miron";
        result.password = "miron";
        result.database = "IBIS";
        result.workstation = 'R';
        result.connect();
        return result;
    }

    private static void flush() {
        marker.set(pollOnce(null));
    }

    private static void start() {
        flush();
        poller.start();
        consumerThread.start();
        System.out.println("Bot started");
    }

    private static void stop() {
        System.out.println("Stopping bot");
        isStopped.set(true);
        try {
            poller.interrupt();
            poller.join();
            updates.offer(POISON_PILL);
            consumerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Bot stopped");
    }

    private static void sendAnswer(Message message, String answerText) {
        NewMessageBody answer = new NewMessageBody(answerText, null, null);
        try {
            api.sendMessage(answer).chatId(message.getRecipient().getChatId()).execute();
        }
        catch (Exception exception) {
            System.out.println(exception.toString());
        }
    }

    private static void sendAnswer(Message message, String answerText, String url) {
        List<AttachmentRequest> attachments = new ArrayList<AttachmentRequest>();
        try {
            if (url != null && url.length() != 0) {
                PhotoAttachmentRequestPayload payload = new PhotoAttachmentRequestPayload();
                payload.setUrl("http://irklib.ru" + url);
                attachments.add(new PhotoAttachmentRequest(payload));
            }
        }
        catch (Exception exception) {
            System.out.println(exception.toString());
        }
        NewMessageBody answer = new NewMessageBody(answerText, attachments, null);
        try {
            api.sendMessage(answer).chatId(message.getRecipient().getChatId()).execute();
        }
        catch (Exception exception) {
            System.out.println(exception.toString());
        }
    }

    private static Announcement[] getAnnounces() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            URL url = new URL("https://www.irklib.ru/api/");
            Announcement[] announcements = mapper.readValue(url, Announcement[].class);
            System.out.println(announcements.toString());
            return announcements;
        }
        catch (Exception exception) {
            System.out.println(exception.toString());
        }
        return new Announcement[0];
    }

    private static String fixHtml(String text) {
        text = text
                .replace("\r", "")
                .replace("\n", "")
                .replace("\t", " ")
                .replace("&nbsp;", " ");
        text = text.trim();
        return text;
    }

    private static void sendAnnounces(Message message) {
        Announcement[] announcements = getAnnounces();
        if (announcements == null || announcements.length == 0) {
            sendAnswer(message, "К сожалению, аннонсы недоступны");
            return;
        }

        for (Announcement announcement : announcements) {
            String text = announcement.PREVIEW_TEXT;
            if (text == null || text.length() == 0) {
                continue;
            }
            text = fixHtml(text);
            sendAnswer(message, text, announcement.DETAIL_PICTURE);
        }
    }

    private static void handleMessage(Message message) throws IOException, IrbisException {
        MessageBody body = message.getBody();
        String text = body.getText();
        if (text == null || text.length() == 0) {
            return;
        }

        /*
            /announces или анонсы - анонсы мероприятий
            /contacts или контакты - контактная информация
            /hours или режим работы - режим работы
            /help помощь - краткая подсказка
            Любая другая строчка - поиск
         */

        if (text.compareToIgnoreCase("помощь") == 0
                ||text.compareToIgnoreCase("/help") == 0) {
            String answerText
                    = "Бот показывает анонсы мероприятий библиотеки, её контакты и режим работы.\n"
                    + "Кроме того, бот ищет книги или статьи в электронном каталоге. \n\n"
                    + "Для поиска введите ключевое слово (например, черемша), "
                    + "заглавие книги (например, Голодные игры) "
                    + "или фамилию автора (например, Акунин)\n"
                    + "Для расширения поиска используйте усечение окончаний слов "
                    + "(черемша -> черемш).";
            sendAnswer(message, answerText);
            return;
        }

        if (text.compareToIgnoreCase("контакты") == 0
                ||text.compareToIgnoreCase("/contacts") == 0) {
            String answerText
                    = "Почтовый адрес: 664033, г. Иркутск, ул. Лермонтова, 253\n"
                    + "Электронная почта: library@irklib.ru\n"
                    + "Многоканальный телефон: (3952) 48-66-80\n"
                    + "Добавочный номер приемной: 705\n\n";
            // + "<a href=\"https://2gis.ru/irkutsk/firm/1548640653569394\">На карте</a>";
            sendAnswer(message, answerText);
            return;
        }

        if (text.compareToIgnoreCase("режим работы") == 0
                || text.compareToIgnoreCase("/hours") == 0) {
            String answerText = "Режим работы:\n\n"
                    + "ВТ-ВС 11.00-20.00 (до 22.00 в режиме читального зала)\n"
                    + "ПН - выходной,\n"
                    + "последняя пятница месяца - санитарный день";
            sendAnswer(message, answerText);
            return;
        }

        if (text.compareToIgnoreCase("анонсы") == 0
                ||text.compareToIgnoreCase("/announces") == 0) {
            sendAnnounces(message);
            return;
        }

        System.out.println("ПОИСК: " + text);
        IrbisConnection connection = null;

        try{
            connection = getConnection();
            String expression = "\"K=" + text + "\"";
            SearchParameters parameters = new SearchParameters();
            parameters.searchExpression = expression;
            parameters.database = connection.database;
            parameters.numberOfRecords = 5;
            int[] found = connection.search(parameters);
            if (found.length == 0) {
                sendAnswer(message, "По Вашему запросу ничего не найдено");
                return;
            }

            for (int mfn: found) {
                String formatted = connection.formatRecord("@brief", mfn);
                sendAnswer(message, formatted);
            }
        }
        finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

    }

    private static void consumeUpdates() {
        while (true) {
            Update update;
            try {
                update = updates.take();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            if (update == POISON_PILL) {
                return;
            }

            if (update instanceof MessageCreatedUpdate)
            {
                MessageCreatedUpdate mcu = (MessageCreatedUpdate)update;
                Message message = mcu.getMessage();
                try {
                    handleMessage(message);
                }
                catch (Exception exception) {
                    System.out.println(exception.toString());
                }
            }
        }
    }

    public static void entryPoint(String[] args) {
        try {
            api = TamTamBotAPI.create("Какие-то цифры");
            me = api.getMyInfo().execute();
            System.out.println(me.toString());

            poller = new Thread(LibermanBot::poll, "updates-poller");
            consumerThread = new Thread(LibermanBot::consumeUpdates, "updates-consumer");

            start();
        }
        catch (Exception exception) {
            System.out.println(exception.toString());
        }
    }
}
