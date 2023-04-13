package com.samuel.healthmonitor.resources;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RAM {
    public static String load() {
        memoryList.clear();
        long allocatedMemory = 0;
        long freeMemory = 0;
        long totalMemory = 0;

        while(true) {
            try {
                byte[] memoryBlock = new byte[MiB];
                memoryList.add(memoryBlock);
                allocatedMemory += memoryBlock.length;
                freeMemory = Runtime.getRuntime().freeMemory();
                totalMemory = Runtime.getRuntime().totalMemory();
            }
            catch(OutOfMemoryError e) {
                break;
            }
        }

        

        return String.format(
                        "Allocated %d MB of memory. " +
                        "Free memory: %d MB, " +
                        "Total memory: %d MB, " +
                        "Max memory: %d MB",
                        allocatedMemory / MiB,
                        freeMemory / MiB,
                        totalMemory / MiB,
                        maxMemory / MiB);
    }

    private static List<byte[]> memoryList = new ArrayList<>();
    private static long maxMemory = Runtime.getRuntime().maxMemory();
    private static final int MiB = (int) Math.pow(2, 20);
}

