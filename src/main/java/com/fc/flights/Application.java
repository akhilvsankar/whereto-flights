package com.fc.flights;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * SpringBoot main application class responsible for starting the server.
 */
@SpringBootApplication
public class Application {

    /**
     * main method for running the application.
     *
     * @param args
     */
    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);

    }

    @Bean
    public RestTemplate getRestTemplate()
    {
        return new RestTemplate();
    }
}
