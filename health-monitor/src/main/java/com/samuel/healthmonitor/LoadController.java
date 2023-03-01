package com.samuel.healthmonitor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Random;

@RestController
public class LoadController {
    @GetMapping("/load/cpu")
    public String loadCPU() {
        int i = 0;
        while(i < 1000000000) {
            i++;
        }
        return "Loaded the CPU successfully!";
    }

    @GetMapping("/load/ram")
    public String loadRAM() {
        int arraySize = 100000000;
        int[] array = new int[arraySize];
        for(int i = 0; i < arraySize; i++) {
            array[i] = new Random().nextInt();
        }
        return "Loaded the RAM successfully!";
    }

    @GetMapping("/load/disk")
    public String loadDisk() throws IOException {
        String filename = "load_test.txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        for(int i = 0; i < 1000000; i++) {
            writer.write("Load test\n");
        }
        writer.close();
        return "Loaded the disk successfully!";
    }

    @GetMapping("load/network")
    public String loadNetwork() throws IOException {
        InetAddress.getByName("www.google.com").isReachable(5000);
        return "Loaded the network successfully";
    }

    @GetMapping("load/all")
    public String loadAll() throws IOException {
        loadCPU();
        loadRAM();
        loadDisk();
        loadNetwork();
        return "Loaded all hardware successfully";
    }

}
