package com.samuel.healthmonitor.resources;

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
}
