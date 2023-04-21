package com.samuel.healthmonitor.resources;

import java.io.*;

public class Disk {
    public static void load() {
        while(true) {
            String[] command = {"/bin/bash", "-c", "sudo bonnie++ -d bonnie/ -s 8192 -n 1000 -r 1000 -u root"};
            try {
                ProcessBuilder pb = new ProcessBuilder(command);
                pb.redirectErrorStream(true);
                pb.directory(new File(System.getProperty("user.home")));
                Process bonnie = pb.start();

                BufferedReader reader = new BufferedReader(new InputStreamReader(bonnie.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                bonnie.waitFor();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
