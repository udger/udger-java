package org.udger.parser;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The Class LRUCache. Simple LRU cache for UA Parser
 */
public class LRUCache<K, V> implements Serializable {

    private static final long serialVersionUID = 275929298283639982L;

    private static class Node<K, V> implements Serializable {
        private static final long serialVersionUID = -2815264316130381309L;
        private Node<K, V> prev;
        private Node<K, V> next;
        private K key;
        private V value;
    }

    private Node<K, V> head;
    private Node<K, V> tail;
    private int capacity;

    private final Map<K, Node<K, V>> map = new ConcurrentHashMap<>();

    public LRUCache(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        if (this.capacity > capacity) {
            while (map.size() > capacity) {
                assert (tail != null);
                map.remove(tail.key);
                tail = tail.prev;
                tail.next = null;
            }
        }
        this.capacity = capacity;
    }

    public void clear() {
        this.map.clear();
    }

    public V get(K uaString) {
        Node<K, V> node = map.get(uaString);
        if (node != null) {
            if (head != node) {
                if (node.next != null) {
                    node.next.prev = node.prev;
                } else {
                    tail = node.prev;
                }
                node.prev.next = node.next;
                head.prev = node;
                node.next = head;
                node.prev = null;
                head = node;
            }
            return node.value;
        }
        return null;
    }

    public void put(K key, V value) {
        Node<K, V> node = map.get(key);
        if (node == null) {
            node = new Node<>();
            node.value = value;
            node.key = key;
            node.next = head;
            node.prev = null;
            if (head != null) {
                head.prev = node;
            }
            if (tail == null) {
                tail = head;
            }
            head = node;
            map.put(key, node);
            if (map.size() > capacity) {
                assert (tail != null);
                map.remove(tail.key);
                tail = tail.prev;
                tail.next = null;
            }
        }
        node.value = value;
    }

}

