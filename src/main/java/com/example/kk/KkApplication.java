package com.example.kk;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@SpringBootApplication
public class KkApplication {

    public static void main(String[] args) {
        SpringApplication.run(KkApplication.class, args);
    }

    @Value("${node}")
    private String node;

    @GetMapping(value = "/health")
    public String index() {
        return Instant.now().toString() + ":node->" + node;
    }
}
