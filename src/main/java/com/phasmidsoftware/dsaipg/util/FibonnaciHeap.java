package com.phasmidsoftware.dsaipg.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FibonnaciHeap {
    private Node minNode = null;
    private int n = 0;

    public int size() {
        return n;
    }

    // Insert a new element into the Fibonacci heap
    public void insert(int key) {
        Node node = new Node(key);
        if (minNode == null) {
            minNode = node;
        } else {
            // Insert node into the root list
            node.prev = minNode.prev;
            node.next = minNode;
            minNode.prev.next = node;
            minNode.prev = node;

            if (key < minNode.key) {
                minNode = node;
            }
        }
        n++;
    }

    // Remove the minimum node from the Fibonacci heap
    public int removeMin() {
        Node z = minNode;
        if (z != null) {
            // If z has children, delay adding them to the root list until consolidation
            if (z.child != null) {
                Node child = z.child;
                do {
                    Node nextChild = child.next;
                    child.prev.next = child.next;
                    child.next.prev = child.prev;

                    // Add child to the root list
                    child.prev = minNode.prev;
                    child.next = minNode;
                    minNode.prev.next = child;
                    minNode.prev = child;

                    child.parent = null;
                    child = nextChild;
                } while (child != z.child);
            }

            // Remove z from the root list
            z.prev.next = z.next;
            z.next.prev = z.prev;

            if (z == z.next) {
                minNode = null; // If z was the only node
            } else {
                minNode = z.next;
                consolidate();  // Consolidate after removal
            }

            n--;
            return z.key;
        }
        return Integer.MIN_VALUE; // Error condition (heap is empty)
    }

    // Consolidate the heap to reduce the number of nodes in the root list
    private void consolidate() {
        if (minNode == null) return;

        Map<Integer, Node> degrees = new HashMap<>(); // Use a Map instead of an ArrayList

        Node current = minNode;
        do {
            Node x = current;
            int d = x.degree;

            // If the degree already exists, perform consolidation
            while (degrees.containsKey(d)) {
                Node y = degrees.get(d);
                if (x.key > y.key) {
                    Node temp = x;
                    x = y;
                    y = temp;
                }

                // Consolidation logic remains the same...
                degrees.remove(d); // Remove the degree to ensure it's only counted once
            }

            // Store node by degree
            degrees.put(d, x);
            current = current.next;
        } while (current != minNode);

        // After consolidation, find the new minimum node
        minNode = null;
        for (Node node : degrees.values()) {
            if (minNode == null || node.key < minNode.key) {
                minNode = node;
            }
        }

        // Clear the degree map to avoid unnecessary memory consumption
        degrees.clear();
    }



    // Node class for the Fibonacci heap
    private static class Node {
        int key;
        Node prev;
        Node next;
        Node child;
        Node parent;
        int degree;
        boolean mark;

        public Node(int key) {
            this.key = key;
            this.prev = this;
            this.next = this;
            this.child = null;
            this.parent = null;
            this.degree = 0;
            this.mark = false;
        }
    }
}

