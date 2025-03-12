package com.dinesh.urlshortnerbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UrlShortnerBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(UrlShortnerBackendApplication.class, args);
    }

}
