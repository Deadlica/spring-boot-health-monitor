package com.samuel.healthmonitor.rest;

import com.samuel.healthmonitor.resources.CPU;
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
    public String loadCPU() { //VM has 2 cores, hence the 2 threads.
        Thread t1 = new Thread(CPU::load);
        Thread t2 = new Thread(CPU::load);
        t1.start();
        t2.start();
        return "Loaded the CPU successfully!";
    }

    @GetMapping("/load/ram")
    public String loadRAM() {
        Thread t = new Thread(RAM::load);
        t.start();
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
