package com.example.moviewbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MoviewBackendApplication {

    public static void main(String[] args) {

        SpringApplication.run(MoviewBackendApplication.class, args);
    }

}
