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
    public ResponseEntity<String> loadCPU() {
        for(int i = 0; i < CPU.cores(); i++) {
            Thread thread = new Thread(CPU::load);
            thread.start();
        }
        return ResponseEntity.ok("Loaded the CPU successfully!");
    }

    @GetMapping("/load/ram")
    public ResponseEntity<String> loadRAM() {
        Thread t = new Thread(RAM::load2);
        t.start();
        return ResponseEntity.ok("Loading the RAM!");
    }

    @GetMapping("/load/disk")
    public ResponseEntity<String> loadDisk() {
        Thread t = new Thread(Disk::load);
        t.start();
        return ResponseEntity.ok("Loading the disk!");
    }

    @GetMapping("load/network")
    public ResponseEntity<String> loadNetwork() {
        Thread t = new Thread(Network::load);
        t.start();
        return ResponseEntity.ok("Loading the network!");
    }

    @GetMapping("load/all")
    public ResponseEntity<String> loadAll() {
        loadCPU();
        loadRAM();
        loadDisk();
        loadNetwork();
        return ResponseEntity.ok("Loading all hardware!");
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
