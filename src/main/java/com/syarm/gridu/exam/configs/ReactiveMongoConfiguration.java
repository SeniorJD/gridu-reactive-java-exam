package com.syarm.gridu.exam.configs;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.syarm.gridu.exam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableReactiveMongoRepositories(basePackageClasses = {UserRepository.class})
@Configuration
public class ReactiveMongoConfiguration extends AbstractReactiveMongoConfiguration {

    private final String dbUsername;
    private final String dbName;
    private final String dbPassword;

    @Autowired
    public ReactiveMongoConfiguration(@Qualifier("dbUsername") String dbUsername,
                                      @Qualifier("dbName") String dbName,
                                      @Qualifier("dbPassword") String dbPassword
    ) {
        this.dbUsername = dbUsername;
        this.dbName = dbName;
        this.dbPassword = dbPassword;
    }

    @Override
    protected MongoClientSettings mongoClientSettings() {
        return MongoClientSettings.builder()
                .credential(MongoCredential.createCredential(
                                dbUsername,
                                dbName,
                                dbPassword.toCharArray()))
                .build();
    }

    @Override
    protected String getDatabaseName() {
        return dbName;
    }
}