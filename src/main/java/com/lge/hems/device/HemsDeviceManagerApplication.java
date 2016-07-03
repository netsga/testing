package com.lge.hems.device;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@SpringBootApplication
@EnableAutoConfiguration
@PropertySource("config.properties")
public class HemsDeviceManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(HemsDeviceManagerApplication.class, args);
	}
}
