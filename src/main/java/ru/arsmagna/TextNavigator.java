// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Навигатор по тексту.
 */
@SuppressWarnings({"unused", "WeakerAccess", "UnusedReturnValue"})
public class TextNavigator implements Cloneable {

    /**
     * Признак конца текста.
     */
    public static final char EOT = '\0';

    //=========================================================================

    /**
     * Конструктор.
     *
     * @param text Текст.
     */
    public TextNavigator(@NotNull String text) {
        _text = text;
        _position = 0;
        _length = _text.length();
        _line = 1;
        _column = 1;
    }

    /**
     * Конструктор.
     *
     * @param path    Путь к файлу.
     * @param charset Кодировка.
     * @throws IOException Ошибка ввода-вывода.
     */
    public TextNavigator(@NotNull Path path, @NotNull Charset charset)
            throws IOException {
        byte[] buffer = Files.readAllBytes(path);
        _text = new String(buffer, charset);
        _position = 0;
        _length = _text.length();
        _line = 1;
        _column = 1;
    }

    //=========================================================================

    private int _column, _length, _line, _position;

    private String _text;

    //=========================================================================

    /**
     * Символ в указанной позиции.
     *
     * @param position Смещение от начала текста в символах.
     * @return Символ или EOF.
     */
    public char charAt(int position) {
        return position >= 0 && position < _length ? _text.charAt(position) : EOT;
    }

    /**
     * Создание копии (в текущем состоянии).
     *
     * @return Копия.
     */
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public TextNavigator clone() {
        TextNavigator result = new TextNavigator(_text);
        result._line = _line;
        result._column = _column;
        result._position = _position;

        return result;
    }

    /**
     * Номер текущей колонки (отсчитывается от 1).
     *
     * @return Номер колонки.
     */
    public int column() {
        return _column;
    }

    /**
     * Признак конца текста.
     *
     * @return Конец текста?
     */
    public boolean eot() {
        return _position >= _length;
    }

    /**
     * Управляющий символ?
     *
     * @return Управляющий символ?
     */
    public boolean isControl() {
        return Character.isISOControl(peekChar());
    }

    /**
     * Цифра?
     *
     * @return Цифра?
     */
    public boolean isDigit() {
        return Character.isDigit(peekChar());
    }

    /**
     * Буква?
     *
     * @return Буква?
     */
    public boolean isLetter() {
        return Character.isLetter(peekChar());
    }

    /**
     * Пробел?
     *
     * @return Пробел?
     */
    public boolean isWhitespace() {
        return Character.isWhitespace(peekChar());
    }

    /**
     * Общая длина текста в символах, включая служебные.
     *
     * @return Длина текста.
     */
    public int length() {
        return _length;
    }

    /**
     * Номер текущей строки (отсчет от 1).
     *
     * @return Номер текущей строки.
     */
    public int line() {
        return _line;
    }

    /**
     * Подглядывание текущего символа. Не смещает позицию.
     *
     * @return Текущий символ или EOT.
     */
    public char peekChar() {
        return _position >= _length ? EOT : _text.charAt(_position);
    }

    /**
     * Текущая позиция (отсчет от 0, учитываются все символы,
     * в т. ч. служебные). Может быть за пределами текста.
     *
     * @return Текущая позиция.
     */
    public int position() {
        return _position;
    }

    /**
     * Чтение текущего символа. Позиция смещается на один символ.
     *
     * @return Текущий символ или EOT.
     */
    public char readChar() {
        if (_position >= _length) {
            return EOT;
        }
        char result = _text.charAt(_position++);
        if (result == '\n') {
            _line++;
            _column = 1;
        } else {
            _column++;
        }

        return result;
    }

    /**
     * Чтение строки.
     *
     * @return Строка или null.
     */
    @Nullable
    public String readLine() {
        if (_position >= _length) {
            return null;
        }

        int start = _position;
        while (_position < _length) {
            char c = _text.charAt(_position);
            if (c == '\r' || c == '\n') {
                break;
            }
            readChar();
        }

        String result;
        result = _text.substring(start, _position);
        if (_position < _length) {
            char c = _text.charAt(_position);
            if (c == '\r') {
                readChar();
                c = peekChar();
            }
            if (c == '\n') {
                readChar();
            }
        }

        return result;
    }

    /**
     * Считывание вплоть до указанного символа (включая его).
     *
     * @param stopChar Стоп-символ.
     * @return Считанная строка или null.
     */
    @Nullable
    public String readTo(char stopChar) {
        if (eot()) {
            return null;
        }

        int savePosition = _position;
        while (true) {
            char c = readChar();
            if (c == EOT || c == stopChar) {
                break;
            }
        }

        String result = _text.substring(savePosition, _position);

        return result;
    }

    /**
     * Считывание вплоть до указанного символа
     * (сам символ остается несчитанным).
     *
     * @param stopChar Стоп-символ.
     * @return Считанная строка или null.
     */
    @Nullable
    public String readUntil(char stopChar) {
        if (eot()) {
            return null;
        }

        int savePosition = _position;
        while (true) {
            char c = peekChar();
            if (c == EOT || c == stopChar) {
                break;
            }

            readChar();
        }

        String result = _text.substring(savePosition, _position);

        return result;
    }

    /**
     * Отдать остаток текста.
     *
     * @return Остаток текста или null.
     */
    @Nullable
    public String remainingText() {
        if (eot()) {
            return null;
        }

        return _text.substring(_position);
    }
}
