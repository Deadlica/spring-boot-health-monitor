package com.samuel.healthmonitor.resources;

import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpHeaders;

public class Network {
    public static void load() {
        while(true) {
            request.getForObject(url, String.class);
            /*request.postForObject(url, new HttpEntity<>
                            ("{'title':'test','body':'test','userId':1}")
                            , String.class);
        */}
    }

    private static final RestTemplate request = new RestTemplate();
    private static final String url = "https://jsonplaceholder.typicode.com/posts";
}
