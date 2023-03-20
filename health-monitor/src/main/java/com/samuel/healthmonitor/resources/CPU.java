package com.samuel.healthmonitor.resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CPU {
    public static void load() {
        while(true) {
            double result = 1.23456789; //These are just random number with some extra precision
            double num2 = 9.87654321;
            for (int i = 0; i < 100000; i++) {
                result += Math.sqrt(result * num2);
            }
        }
    }

    public static int cores() {
        int coresCount = 0;
        try {
            Process process = Runtime.getRuntime().exec("nproc");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();

            if (line != null) {
                coresCount = Integer.parseInt(line.trim());
            }
            reader.close();
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
        return coresCount;
    }
}
