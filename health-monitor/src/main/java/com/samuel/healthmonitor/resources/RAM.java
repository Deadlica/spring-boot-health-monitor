package com.samuel.healthmonitor.resources;

import java.util.ArrayList;
import java.util.List;

public class RAM {
    public static void load() { //Mainly loads ram
        List<byte[]> list = new ArrayList<>();
        while(true) {
            byte[] bytes = new byte[MiB];
            list.add(bytes);
            /*for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) (i % 256);
            }*/
        }
    }
    private static final int MiB = (int) Math.pow(2, 20);
}
