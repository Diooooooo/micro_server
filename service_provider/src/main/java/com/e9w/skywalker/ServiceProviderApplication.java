package com.e9w.skywalker;

import com.e9w.skywalker.config.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Created by fc on 2016-11-25.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ServiceProviderApplication {

    public static void main(String[] args) {
        Configuration.init();
        SpringApplication.run(ServiceProviderApplication.class, args);
    }
}
