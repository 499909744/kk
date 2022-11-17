package com.example.kk;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.Instant;
import java.util.Objects;

@RestController
@SpringBootApplication
public class KkApplication {

    public static void main(String[] args) {
        SpringApplication.run(KkApplication.class, args);
    }

    @Value("${node}")
    private String node;

    @Value("${serviceAddr}")
    private String serviceAddr;

    @GetMapping(value = "/health")
    public String index() {
        return Instant.now().toString() + ":node->" + node;
    }

    @GetMapping(value = "invoke")
    public String invoke() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(serviceAddr + "/health")
                .get()
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "138c2f3c-457b-a4a9-9d3b-cf5cb00d17ce")
                .build();

        Response response = client.newCall(request).execute();
        return Objects.requireNonNull(response.body()).string();
    }
}
