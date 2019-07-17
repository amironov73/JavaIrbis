// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna;

/**
 * Файл не найден на сервере.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class IrbisFileNotFoundException extends IrbisException {
    public String fileName;

    public IrbisFileNotFoundException(String fileName) {
        this.fileName = fileName;
    }

    public IrbisFileNotFoundException(FileSpecification specification) {
        this.fileName = specification.toString();
    }
}
