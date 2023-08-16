package cn.qssq666.robot.utils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
public class MaxSizeContainer<T> {
    private final int maxSize;
    private final LinkedHashMap<T, Long> elements; // To track insertion order and timestamps
    public MaxSizeContainer(int maxSize) {
        this.maxSize = maxSize;
        this.elements = new LinkedHashMap<T, Long>(maxSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<T, Long> eldest) {
                int size = size();
                boolean result= size > maxSize;
                return result;
            }
        };
    }
    public synchronized boolean add(T element) {
        elements.put(element, System.currentTimeMillis());
        return true;
    }

    public synchronized List<T> getAll() {
        return new ArrayList<>(elements.keySet());
    }

    public int getMaxSize() {
        return maxSize;
    }

    public int getCurrentSize() {
        return elements.size();
    }
    public static void main(String[] args) {
        MaxSizeContainer<String> container = new MaxSizeContainer<>(3);
        container.add("Item 1");
        container.add("Item 2");
        container.add("Item 3");

        System.out.println("Current size: " + container.getCurrentSize());
        System.out.println("Max size: " + container.getMaxSize());

        container.add("Item 4");

        List<String> allItems = container.getAll();
        System.out.println("All items: " + allItems);
    }
}