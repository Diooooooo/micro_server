package com.e9w.skywalker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Created by fc on 2016-11-25.
 */
@EnableEurekaServer
@SpringBootApplication
public class ServiceCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceCenterApplication.class, args);
    }
}
