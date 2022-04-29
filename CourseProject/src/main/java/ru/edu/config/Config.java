package ru.edu.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
@PropertySource("classpath:application.yml")
public class Config {

    @Value("${db.url}")
    String url;

    @Bean
    public Connection connection() throws SQLException {
        return DriverManager.getConnection(url);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
