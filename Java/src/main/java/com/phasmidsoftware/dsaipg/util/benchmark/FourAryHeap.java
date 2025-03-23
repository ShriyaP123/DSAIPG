package com.phasmidsoftware.dsaipg.util;

import java.util.Arrays;

public class FourAryHeap {
    private int[] heap;
    private int size;
    private final int d = 4;

    public FourAryHeap(int capacity) {
        heap = new int[capacity];
        size = 0;
    }

    public int size() {
        return size;
    }

    private int parent(int i) {
        return (i - 1) / d;
    }

    private int child(int i, int k) {
        return d * i + k;
    }

    public void insert(int key) {
        if (size == heap.length) {
            heap = Arrays.copyOf(heap, heap.length * 2);
        }
        heap[size] = key;
        heapifyUp(size);
        size++;
    }

    private void heapifyUp(int i) {
        while (i > 0 && heap[i] < heap[parent(i)]) {
            swap(i, parent(i));
            i = parent(i);
        }
    }

    public int removeMin() {
        if (size == 0) return Integer.MIN_VALUE;
        int min = heap[0];
        heap[0] = heap[--size];
        heapifyDown(0);
        return min;
    }

    private void heapifyDown(int i) {
        while (true) {
            int min = i;
            for (int k = 1; k <= d; k++) {
                int c = child(i, k);
                if (c < size && heap[c] < heap[min]) {
                    min = c;
                }
            }
            if (min == i) break;
            swap(i, min);
            i = min;
        }
    }

    private void swap(int i, int j) {
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

}
