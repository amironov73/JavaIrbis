// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna.infrastructure;

import java.util.Hashtable;

/**
 * Словарь ресурсов сервера.
 */
@SuppressWarnings({"unused"})
public final class ResourceDictionary<T> {
    private final Hashtable<String, IrbisResource<T>> _dictionary;

    public ResourceDictionary() {
        _dictionary = new Hashtable<>();
    }

    public ResourceDictionary<T> add(String name, T content) {
        _dictionary.put(name, new IrbisResource<>(name, content));
        return this;
    }

    public IrbisResource[] all() {
        return _dictionary.values().toArray(new IrbisResource[0]);
    }

    public ResourceDictionary<T> clear() {
        _dictionary.clear();
        return this;
    }

    public int count() {
        return _dictionary.size();
    }

    public T get(String name) {
        IrbisResource<T> resource = _dictionary.getOrDefault(name, null);
        if (resource == null) {
            return null;
        }
        return resource.content;
    }

    public boolean have(String name) {
        return _dictionary.containsKey(name);
    }

    public ResourceDictionary<T> put(String name, T content) {
        _dictionary.put(name, new IrbisResource<>(name, content));
        return this;
    }

    public ResourceDictionary<T> remove(String name) {
        _dictionary.remove(name);
        return this;
    }
}
