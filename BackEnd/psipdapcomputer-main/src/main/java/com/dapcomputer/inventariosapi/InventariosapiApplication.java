package com.dapcomputer.inventariosapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class InventariosapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventariosapiApplication.class, args);
    }
}

