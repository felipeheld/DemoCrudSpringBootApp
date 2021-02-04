package com.felipeheld.grades.config;

import java.util.Arrays;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableMongoRepositories(basePackages = "com.felipeheld.grades.repository")
public class MongoConfig {

    @Autowired
    private Environment env;
   
    @Bean
    public MongoClient mongo(@Value("${mongo-cluster-url}") String clusterUrl) {        
        var database = Arrays.asList(env.getActiveProfiles()).contains("Integration")
          ? "test" : "ensino";

        log.info("DATABASE selected={}", database);
  
        ConnectionString connectionString = new ConnectionString(clusterUrl + database + "?retryWrites=true&w=majority");
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
          .applyConnectionString(connectionString)
          .build();
        
        return MongoClients.create(mongoClientSettings);
    }
}
