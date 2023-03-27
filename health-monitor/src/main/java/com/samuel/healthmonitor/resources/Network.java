package com.samuel.healthmonitor.resources;

import org.springframework.web.client.RestTemplate;

public class Network {
    public static void load() {
        while(true) {
            request.getForObject(url, String.class);
        }
    }

    private static final RestTemplate request = new RestTemplate();
    private static final String url = "https://jsonplaceholder.typicode.com/posts";
}
