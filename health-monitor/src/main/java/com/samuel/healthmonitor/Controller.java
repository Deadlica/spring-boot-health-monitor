package com.samuel.healthmonitor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/monitor")
    public String monitor() {
        return "Hello World";
    }
}
