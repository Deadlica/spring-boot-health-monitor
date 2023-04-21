package com.samuel.healthmonitor.resources;

import java.io.IOException;

public class RAM {
    public static void load() {
        while(true) {
            String[] command = {"stress-ng", "--vm", "2", "--vm-bytes", "2G", "--vm-method", "all", "--vm-hang", "60s", "-t", "120s"};
            try {
                ProcessBuilder pb = new ProcessBuilder(command);
                Process stress_ng = pb.start();
                stress_ng.waitFor();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

