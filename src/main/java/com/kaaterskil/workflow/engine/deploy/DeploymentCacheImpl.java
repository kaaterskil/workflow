package com.kaaterskil.workflow.engine.deploy;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DeploymentCacheImpl<T> implements DeploymentCache<T> {

    private final Map<Serializable, T> cache = new HashMap<>();

    @Override
    public int size() {
        return cache.size();
    }

    @Override
    public boolean isEmpty() {
        return cache.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return cache.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return cache.containsValue(value);
    }

    @Override
    public T get(Object key) {
        return cache.get(key);
    }

    @Override
    public T put(Serializable key, T value) {
        return cache.put(key, value);
    }

    @Override
    public T remove(Object key) {
        return cache.remove(key);
    }

    @Override
    public void putAll(Map<? extends Serializable, ? extends T> m) {
        cache.putAll(m);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public Set<Serializable> keySet() {
        return cache.keySet();
    }

    @Override
    public Collection<T> values() {
        return cache.values();
    }

    @Override
    public Set<java.util.Map.Entry<Serializable, T>> entrySet() {
        return cache.entrySet();
    }

}
