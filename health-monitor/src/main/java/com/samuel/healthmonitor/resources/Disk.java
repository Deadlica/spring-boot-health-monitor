package com.samuel.healthmonitor.resources;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Disk {
    public static void readLoad() throws IOException {
        while(true) {
            FileReader file = new FileReader(fileName);
            for(int i = 0; i < 1000000; i++) {
                String line = new String();
                file.read(line.toCharArray());
            }
            file.close();
        }
    }

    public static void writeLoad() throws IOException {
        while(true) {
            FileWriter file = new FileWriter(fileName);
            for(int i = 0; i < 1000000; i++) {
                file.write("Dummy text.\n");
            }
            file.close();
        }
    }

    private static String fileName = "load_test.txt";
}
