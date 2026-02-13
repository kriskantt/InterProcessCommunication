package com.ipc.stream;

import java.io.*;
import java.util.Scanner;

public class Parent {
    static Process process;
    // Keep these static so they stay open for the life of the process
    static BufferedWriter writer;
    static BufferedReader reader;

    public static void main(String[] args) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("java", "/Users/kriskant/Documents/Personal_Projects/Java/InterProcessCommunication/src/com/ipc/stream/Child.java");
        pb.redirectErrorStream(true);
        process = pb.start();

        // Initialize streams ONCE
        writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        // 1. Talk
        sendMessage("Data sent by Parent");

        // 2. Listen (We read just one line to avoid the infinite loop hang)
        System.out.println("Parent is listening...");
        String response = reader.readLine();
        System.out.println("Inside Parent Process: " + response);

        process.destroy(); // Manually kill the child since it's in a while(true)
    }

    static void sendMessage(String str) throws IOException {
        writer.write(str);
        writer.newLine();
        writer.flush(); // Forces the string out of the buffer and into the Child's ear
    }
}