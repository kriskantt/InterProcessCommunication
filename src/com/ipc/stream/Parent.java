package com.ipc.stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Parent {
    public static void main(String[] args) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("java",  "/Users/kriskant/Documents/Personal_Projects/Java/InterProcessCommunication/src/com/ipc/stream/Child.java");
        pb.redirectErrorStream(true);


        Process process = pb.start();
        BufferedReader reader  = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine())!= null) {
            System.out.println("Inside Parent Process :\n" + line);
        }
        process.waitFor();
    }
}
