package com.practice;

import java.util.HashMap;
import java.util.Map;

/**
 * LRU cache with sliding TTL (fixed TTL for all keys).
 *
 * Semantics:
 * - get(key): if present and not expired -> returns value, refreshes expiry (sliding TTL),
 *            moves to MRU (head).
 * - put(key,value): inserts/overwrites, sets expiry = now + ttl, moves to MRU.
 * - Capacity eviction: before evicting live entries, optionally cleans expired entries
 *                      from the LRU tail (expired-first cleanup).
 *
 * Time unit: ttlSeconds passed in constructor; internally uses milliseconds.
 *
 * Complexity:
 * - O(1) per get/put in the typical (interview) sense; cleanup is O(#expired removed)
 *   which is amortized O(1) per operation.
 *
 * NOTE: With fixed TTL + sliding TTL, expiry order == LRU order (oldest access expires first),
 * so we don't need a second expiry list or a heap.
 */
public class LRUwithTTL {
    private static final class Node {
        int key;
        int value;
        long expiryMs; // absolute timestamp in ms
        Node prev;
        Node next;

        Node(int key, int value, long expiryMs) {
            this.key = key;
            this.value = value;
            this.expiryMs = expiryMs;
        }
    }

    private final Map<Integer, Node> map = new HashMap<>();
    private final Node head = new Node(-1, -1, -1); // MRU side sentinel
    private final Node tail = new Node(-1, -1, -1); // LRU side sentinel

    private final int capacity;
    private final long ttlMs;
    private final boolean expiredFirstCleanup;

    public LRUwithTTL(int capacity, int ttlSeconds) {
        this(capacity, ttlSeconds, true);
    }

    public LRUwithTTL(int capacity, int ttlSeconds, boolean expiredFirstCleanup) {
        if (capacity <= 0) throw new IllegalArgumentException("capacity must be > 0");
        if (ttlSeconds <= 0) throw new IllegalArgumentException("ttlSeconds must be > 0");

        this.capacity = capacity;
        this.ttlMs = ttlSeconds * 1000L;
        this.expiredFirstCleanup = expiredFirstCleanup;

        // Wire sentinels
        head.next = tail;
        tail.prev = head;
    }

    public int get(int key) {
        Node node = map.get(key);
        if (node == null) return -1;

        long now = System.currentTimeMillis();
        if (isExpired(node, now)) {
            removeNode(node);
            map.remove(key);
            return -1;
        }

        // Sliding TTL: refresh expiry on access
        node.expiryMs = now + ttlMs;

        // Update LRU order
        moveToFront(node);

        return node.value;
    }

    public void put(int key, int value) {
        long now = System.currentTimeMillis();

        // Overwrite-safe: if key exists, update in-place
        Node existing = map.get(key);
        if (existing != null) {
            if (isExpired(existing, now)) {
                // Treat as new insert
                removeNode(existing);
                map.remove(key);
            } else {
                existing.value = value;
                existing.expiryMs = now + ttlMs; // sliding TTL on write
                moveToFront(existing);
                return;
            }
        }

        // Optional: proactively remove expired keys before considering eviction
        if (expiredFirstCleanup) {
            cleanupExpiredFromTail(now);
        }

        // If still at capacity, evict LRU
        if (map.size() >= capacity) {
            Node lru = tail.prev;
            if (lru != head) {
                removeNode(lru);
                map.remove(lru.key);
            }
        }

        Node node = new Node(key, value, now + ttlMs);
        addToFront(node);
        map.put(key, node);
    }

    // ---------- helpers ----------

    private boolean isExpired(Node node, long nowMs) {
        return node.expiryMs <= nowMs;
    }

    private void cleanupExpiredFromTail(long nowMs) {
        // Because fixed TTL + sliding TTL => oldest access has earliest expiry,
        // expired entries (if any) will appear at the LRU side.
        Node cur = tail.prev;
        while (cur != head && isExpired(cur, nowMs)) {
            Node prev = cur.prev;
            removeNode(cur);
            map.remove(cur.key);
            cur = prev;
        }
    }

    private void addToFront(Node node) {
        Node first = head.next;
        head.next = node;
        node.prev = head;
        node.next = first;
        first.prev = node;
    }

    private void removeNode(Node node) {
        Node p = node.prev;
        Node n = node.next;
        if (p != null) p.next = n;
        if (n != null) n.prev = p;
        node.prev = null;
        node.next = null;
    }

    private void moveToFront(Node node) {
        removeNode(node);
        addToFront(node);
    }
}
