package com.jfsd.ocms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OcmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(OcmsApplication.class, args);
    }
}
