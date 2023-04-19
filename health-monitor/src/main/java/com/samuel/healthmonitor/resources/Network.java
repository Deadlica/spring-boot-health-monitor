package com.samuel.healthmonitor.resources;

import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.http.HttpHeaders;

public class Network {
    public static void load() {
        String[] client_command = {"iperf", "-c", "192.168.1.10", "-p", "5001", "-P", "2", "-t", "3600"};
        String[] server_command = {"iperf", "-s"};
        while(true) {
            try {
                ProcessBuilder client_pb = new ProcessBuilder(client_command);
                ProcessBuilder server_pb = new ProcessBuilder(server_command);
                Process client_iperf = client_pb.start();
                server_pb.start();
                client_iperf.waitFor();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static final RestTemplate request = new RestTemplate();
    private static final String url = "https://jsonplaceholder.typicode.com/posts";
}
