// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna.infrastructure;

/**
 * Файл на сервере.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public final class IrbisResource<T> {
    public String name;
    public T content;

    public IrbisResource(String name, T content) {
        this.name = name;
        this.content = content;
    }

    @Override
    public String toString() {
        return "IrbisResource{" +
                "name='" + name + '\'' +
                ", content=" + content +
                '}';
    }
}
