package org.udger.parser;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class LRUCache. Simple LRU cache for UA Parser
 */
public class LRUCache implements Serializable {

    private static final long serialVersionUID = 275929298283639982L;

    private static class Node implements Serializable {
        private static final long serialVersionUID = -2815264316130381309L;
        private Node prev;
        private Node next;
        private String uaString;
        private UdgerUaResult uaResult;
    }

    private Node head;
    private Node tail;
    private int capacity;

    private final Map<String, Node> map = new HashMap<>();

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
                map.remove(tail.uaString);
                tail = tail.prev;
                tail.next = null;
            }
        }
        this.capacity = capacity;
    }

    public void clear(){
        this.map.clear();
    }

    public UdgerUaResult get(String uaString) {
        Node node = map.get(uaString);
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
            return node.uaResult;
        }
        return null;
    }

    public void put(String uaString, UdgerUaResult uaResult) {
        Node node = map.get(uaString);
        if (node == null) {
            node = new Node();
            node.uaResult = uaResult;
            node.uaString = uaString;
            node.next = head;
            node.prev = null;
            if (head != null) {
                head.prev = node;
            }
            if (tail == null) {
                tail = head;
            }
            head = node;
            map.put(uaString, node);
            if (map.size() > capacity) {
                assert(tail != null);
                map.remove(tail.uaString);
                tail = tail.prev;
                tail.next = null;
            }
        }
        node.uaResult = uaResult;
    }

}
