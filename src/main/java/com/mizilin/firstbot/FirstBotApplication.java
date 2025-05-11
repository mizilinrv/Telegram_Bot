package com.mizilin.firstbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
public class FirstBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(FirstBotApplication.class, args);
    }

}
