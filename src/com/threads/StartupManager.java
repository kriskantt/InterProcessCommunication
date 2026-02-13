package com.threads;

import java.util.concurrent.CountDownLatch;

public class StartupManager {
    public static void main(String[] args) throws InterruptedException {
        // 1. Initialize latch with the number of services to wait for
        CountDownLatch latch = new CountDownLatch(3);

        // 2. Start services in separate threads
        new Thread(new Service("Database", 2000, latch)).start();
        new Thread(new Service("Cache", 1000, latch)).start();
        new Thread(new Service("Messaging", 1500, latch)).start();

        System.out.println("Main: Waiting for services to initialize...");

        // 3. Main thread blocks here until count reaches 0
        latch.await();

        System.out.println("Main: All services are UP. Starting application!");
    }
}

class Service implements Runnable {
    private final String name;
    private final int startupTime;
    private final CountDownLatch latch;

    public Service(String name, int startupTime, CountDownLatch latch) {
        this.name = name;
        this.startupTime = startupTime;
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(startupTime); // Simulate startup work
            System.out.println(name + " is ready.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            // 4. Decrement the latch count when finished
            latch.countDown();
        }
    }
}
