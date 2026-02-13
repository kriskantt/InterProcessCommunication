package com.threads;


import java.time.Instant;
import java.time.LocalDateTime;

public class Volatile {
    // Try removing 'volatile' to see if the loop ever stops!
    static volatile boolean keepRunning = true;
    public static void main(String[] args) throws InterruptedException{
        Thread th1 = new Thread(() -> {
            while (keepRunning) {
            }
            System.out.println("Thread-1 end");
        });
        th1.start();
        Thread.sleep(4000);
        keepRunning = false;
        th1.join();
        System.out.println("Program ended");
    }
}
