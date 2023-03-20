package com.samuel.healthmonitor.rest;

import com.samuel.healthmonitor.resources.CPU;
import com.samuel.healthmonitor.resources.Disk;
import com.samuel.healthmonitor.resources.RAM;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Random;

@RestController
public class LoadController {
    @GetMapping("/load/cpu")
    public String loadCPU() {
        for(int i = 0; i < CPU.cores(); i++) {
            Thread thread = new Thread(CPU::load);
            thread.start();
        }
        return "Loaded the CPU successfully!";
    }

    @GetMapping("/load/ram")
    public String loadRAM() {
        return RAM.load();
    }

    @GetMapping("/load/disk")
    public String loadDisk() throws IOException {
        Thread t1 = new Thread(() -> {
            try {
                Disk.writeLoad();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                Disk.readLoad();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        t1.start();
        t2.start();
        return "Loaded the disk successfully!";
    }

    @GetMapping("load/network")
    public String loadNetwork() throws IOException {
        InetAddress.getByName("www.google.com").isReachable(5000);
        return "Loaded the network successfully";
    }

    @GetMapping("load/all")
    public String loadAll() throws IOException, InterruptedException, RuntimeException {
        /*loadCPU();
        loadRAM();
        loadDisk();
        loadNetwork();*/
        for(int i = 0; i < 5; i++) {
                ArrayList<Thread> resources = new ArrayList<Thread>();
                resources.add(new Thread(loadCPU()));
                resources.add(new Thread(loadRAM()));
                resources.add(new Thread(loadDisk()));
                resources.add(new Thread(loadNetwork()));
                resources.forEach((thread -> {
                    thread.start();
                }));

                /*resources.forEach((thread -> {
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }));*/
        }
        return "Loaded all hardware successfully";
    }
}
