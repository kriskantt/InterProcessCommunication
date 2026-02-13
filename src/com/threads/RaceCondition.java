package com.threads;


public class RaceCondition {
    static int count = 0;
    static final Object lock = new Object();
    public static void main(String[] args) throws InterruptedException{
        Runnable counter = ()-> {
            synchronized (lock) {
                for (int i = 0; i < 1_000_000; i++) {
                    count++;
                }
            }};
        Thread th1 = new Thread(counter);
        Thread th2 = new Thread(counter);
        th1.start();
        th2.start();

        th1.join();
        th2.join();
        System.out.println(count);
    }
}
