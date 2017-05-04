package org.udger.parser;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The Class LRUCache. Simple thread-safe LRU cache for UA Parser.
 */
class LRUCache<K, V> implements Serializable {

    private static final long serialVersionUID = 275929298283639982L;

    private final ConcurrentHashMap<K, WeakReference<V>> map;
    private final ConcurrentLinkedQueue<K> queue;
    private final int capacity;

    LRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new ConcurrentHashMap<>(capacity);
        this.queue = new ConcurrentLinkedQueue<>();
    }

    V get(K key) {
        final WeakReference<V> valueReference = map.get(key);
        if (valueReference != null) {
            final V value = valueReference.get();
            queue.remove(key);
            if (value != null) {
                //Recently accessed, hence move it to the tail
                queue.add(key);
            } else {
                map.remove(key);
            }
            return value;
        }
        return null;
    }

    void put(K key, V value) {
        if (key == null || value == null) {
            //ConcurrentHashMap doesn't allow null key or values
            throw new NullPointerException("Key or value must not be NULL");
        }
        if (map.containsKey(key)) {
            queue.remove(key);
            queue.add(key);
        } else {
            while (map.size() >= capacity) {
                K lruKey = queue.poll();
                if (lruKey != null) {
                    map.remove(lruKey);
                }
            }
            queue.add(key);
            map.put(key, new WeakReference<>(value));
        }
    }

    int size(){
        return map.size();
    }

    int capacity(){
        return capacity;
    }

    void clear() {
        map.clear();
        queue.clear();
    }

}
