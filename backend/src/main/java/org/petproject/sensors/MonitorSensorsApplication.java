package org.petproject.sensors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class MonitorSensorsApplication {
  public static void main(final String[] args) {
    SpringApplication.run(MonitorSensorsApplication.class, args);
  }
}