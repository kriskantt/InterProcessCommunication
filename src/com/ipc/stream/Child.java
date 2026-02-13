package com.ipc.stream;

import java.util.Scanner;

public class Child {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // Remove the while(true) for testing, or use a break condition
        if (sc.hasNextLine()) {
            String input = sc.nextLine();
            // This goes back to the Parent's reader.readLine()
            System.out.println("Child received: " + input);
        }
    }
}
