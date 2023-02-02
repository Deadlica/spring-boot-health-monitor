package com.samuel.healthmonitor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/monitor")
    public String monitor() {
        return "Hello World";
    }

    @GetMapping("/monitor/cpu-overload")
    public String cpuOverload() {
        try {
            while(true) {
                Runnable runner = () -> {    while(true) {}    };
                new Thread(runner).start();
                Thread.sleep(5000);
            }
        } catch(Exception e) {}
        return "CPU fried :(";
    }
}
