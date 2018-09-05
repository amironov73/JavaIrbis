## Пакет ru.arsmagna

### Введение

Пакет `ru.arsmagna` представляет собой фреймворк для создания клиентских приложений для системы автоматизации библиотек ИРБИС64 на языке Java.

Пакет не содержит неуправляемого кода и не требует irbis64_client.dll. Успешно работает на 32-битных и 64-битных версиях операционных систем Windows и Linux.

Основные возможности пакета:

* Поиск и расформатирование записей.
* Создание и модификация записей, сохранение записей в базе данных на сервере.
* Работа с поисковым словарем: просмотр термов и постингов.
* Администраторские функции: получение списка пользователей, его модификация, передача списка на сервер, создание и удаление баз данных.
* Импорт и экспорт записей в формате ISO 2709 и в обменном формате ИРБИС.

Пакет состоит из нескольких частей:

* **ru.arsmagna** - содержит основную функциональность, в т. ч. для работы с записями.
* **ru.arsmagna.infrastructure** - инфраструктурные классы.
* **ru.arsmagna.direct** - прямое чтение баз данных.

Поддерживаются JDK версии 8 и сервер ИРБИС64, начиная с 2014. Более ранние версии Java будут выдавать ошибки при компиляции, т. к. пакет использует языковые возможности, появившиеся в Java 8. Аналогично обстоит дело и с более ранними версиями сервера ИРБИС64.

### Установка

Пакет можно загрузить с AppVeyor: [https://ci.appveyor.com/project/AlexeyMironov/javairbis/build/artifacts](https://ci.appveyor.com/project/AlexeyMironov/javairbis/build/artifacts).

### Примеры программ

Ниже прилагается пример простой программы. Сначала находятся и загружаются 10 первых библиографических записей, в которых автором является А. С. Пушкин. Показано нахождение значения поля с заданным тегом и подполя с заданным кодом. Также показано расформатирование записи в формат brief.

**ВАЖНО!** Обратите внимание, что компилятор Java по умолчанию считает, что исходные тексты программ записаны в системной кодировке по умолчанию (для русскоязычной Windows это кодировка CP1251). Поэтому, если кодировка исходных текстов будет отличаться от ожидаемой, литералы будут искажены и программа может перестать работать.

Лучший вариант - избегать употребления в тексте программы литералов с национальными символами. В наших примерах литералы употребляются исключительно для простоты изложения.

```java
import ru.arsmagna.DatabaseInfo;
import ru.arsmagna.IrbisConnection;
import ru.arsmagna.MarcRecord;
import ru.arsmagna.infrastructure.IniFile;

import java.util.Arrays;

class Example {
    public static void main(String[] args) {
        try {
            // Подключаемся к серверу
            IrbisConnection connection = new IrbisConnection();
            connection.parseConnectionString("host=127.0.0.1;port=6666;database=IBIS;user=librarian;password=secret;");

            // Из INI-файла можно получить настройки для клиента
            IniFile iniFile = connection.connect();
            DatabaseInfo[] databases = connection.listDatabases(iniFile, "dbnam2.mnu");
            System.out.println("Доступные базы данных");
            for (DatabaseInfo db: databases) {
                System.out.println(db.name);
            }

            int[] found = connection.search("\"A=ПУШКИН$\"");
            System.out.printf("Найдено записей: %d%n", found.length);
            if (found.length > 10) {
                // Ограничиваемся 10 первыми записями
                found = Arrays.copyOf(found, 10);
            }

            for (int i = 0; i < found.length; i++) {
                int mfn = found[i];
                MarcRecord record = connection.readRecord(mfn);

                String title = record.fm(200, 'a');
                System.out.printf("Заглавие: %s%n", title);

                String description = connection.formatRecord("@brief", mfn);
                System.out.printf("Биб. описание: %s%n", description);

                System.out.println();
            }

            // Отключаемся от сервера
            connection.disconnect();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
```

В следующей программе создается и отправляется на сервер 10 записей. Показано добавление в запись полей с подполями.

```java
import ru.arsmagna.IrbisConnection;
import ru.arsmagna.MarcRecord;
import ru.arsmagna.SubField;

class Example {
    public static void main(String[] args) {
        try {
            // Подключаемся к серверу
            IrbisConnection connection = new IrbisConnection();
            connection.parseConnectionString("host=127.0.0.1;port=6666;database=IBIS;user=librarian;password=secret;");
            connection.connect();

            for (int i = 0; i < 10; i++) {
                // Создаем запись
                MarcRecord record = new MarcRecord();

                // Наполняем ее полями: первый автор (поле с подолями),
                record.addField(700,
                        new SubField('a', "Миронов"),
                        new SubField('b', "А. В."),
                        new SubField('g', "Алексей Владимирович"));

                // заглавие (поле с подполями),
                record.addField(200,
                        new SubField('a', String.format("Работа с ИРБИС64: версия %d.0", i)),
                        new SubField('e', "руководство пользователя"));

                // выходные данные (поле с подполями),
                record.addField(210,
                        new SubField('a', "Иркутск"),
                        new SubField('c', "ИРНИТУ"),
                        new SubField('d', "2018"));

                // рабочий лист (поле без подполей).
                record.addField(920, "PAZK");

                // Отсылаем запись на сервер.
                // Обратно приходит запись,
                // обработанная AUTOIN.GBL.
                connection.writeRecord(record);

                // Распечатываем обработанную запись
                System.out.println(record);
                System.out.println();
            }

            // Отключаемся от сервера
            connection.disconnect();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
```