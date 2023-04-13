package com.samuel.healthmonitor.rest;

import com.samuel.healthmonitor.resources.*;
import org.springframework.http.ResponseEntity;
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
    public String loadDisk() {
        Thread t = new Thread(Disk::load);
        t.start();
        return "Loaded the disk successfully!";
    }

    @GetMapping("load/network")
    public ResponseEntity<String> loadNetwork() {
        for(int i = 0; i < CPU.cores(); i++) {
            Thread t = new Thread(Network::load);
            t.start();
        }
        return ResponseEntity.ok("Loading the network!");
    }

    @GetMapping("load/all")
    public String loadAll() {
        loadCPU();
        loadDisk();
        loadNetwork();
        loadRAM();
        return "Loaded all hardware successfully";
    }

    @GetMapping("web-shop")
    public String store() {
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "Welcome to the webpage";
    }
}
