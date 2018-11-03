package org.equinox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement(proxyTargetClass = true)
public class EquinoxApp {
    public static void main(String[] args) {
        SpringApplication.run(EquinoxApp.class, args);
    }
}
