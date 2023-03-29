package com.samuel.healthmonitor.resources;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;

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

    public static void load() {
        while(true) {
            String[] command = {"/bin/bash", "-c", "sudo bonnie++ -d bonnie/ -s 2048 -n 1000 -r 100 -u root"};
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
