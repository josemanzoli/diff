package com.manzoli.diff.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

/**
 * 
 * @author jmanzol
 * @since 0.0.1
 * Class that holds all the configuration for the connection with MongoDB database. 
 * The fields are in a .env file handled by docker container as environment variables.
 */
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@EnableMongoRepositories("com.manzoli.diff.repository")
public class MongoDbConfiguration {

	/** Hosts that will be used to connect to a cluster of MongoDb. The pattern is [HOST]:[PORT],[HOST]:[PORT].... */
    @Value("${MONGODB_HOSTS}")
    private String hosts;

    /** The name of the database itself */
    @Value("${MONGODB_DBNAME}")
    private String dbName;

    /** Maximum amount of connections per host */
    @Value("${MONGODB_CONN_PER_HOST}")
    private String connPerHost;

    /** Maximum amount of threads allowed */
    @Value("${MONGODB_THREADS_ALLOWED}")
    private String threadsAllowed;

    /** Connection timeout */
    @Value("${MONGODB_CONN_TIMEOUT}")
    private String connTimeout;

    /** Max wait time */
    @Value("${MONGODB_MAX_WAIT_TIME}")
    private String maxWaitTime;

    /** Socket Timeout */
    @Value("${MONGODB_SOCKET_TIMEOUT}")
    private String socketTimeout;

    @Bean
    public MongoTemplate mongoTemplate() {
        //removing _class collumn from spring data mongodb
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
        ((MappingMongoConverter) mongoTemplate.getConverter()).setTypeMapper(new DefaultMongoTypeMapper(null));

        return mongoTemplate;
    }

    @Bean
    public MongoDbFactory mongoDbFactory(){
        return new SimpleMongoDbFactory(mongo(), dbName);
    }

    @Bean
    public MongoClient mongo() {
        return new MongoClient(seeds(), mongoClientOptions());
    }

    private MongoClientOptions mongoClientOptions() {
        MongoClientOptions mongoClientOptions = new MongoClientOptions.Builder()
                .connectionsPerHost(Integer.parseInt(connPerHost))
                .threadsAllowedToBlockForConnectionMultiplier(Integer.parseInt(threadsAllowed))
                .connectTimeout(Integer.parseInt(connTimeout))
                .maxWaitTime(Integer.parseInt(maxWaitTime))
                .socketKeepAlive(true)
                .socketTimeout(Integer.parseInt(socketTimeout))
                .writeConcern(WriteConcern.JOURNALED)
                .build();

        return mongoClientOptions;
    }

    private List<ServerAddress> seeds() {
        List<ServerAddress> seedsList = new ArrayList<ServerAddress>();

        for (String host : hosts.split(",")) {
            String hostnameAndPort[] = host.split(":");
            seedsList.add(new ServerAddress(hostnameAndPort[0], Integer.parseInt(hostnameAndPort[1])));
        }

        return seedsList;
    }
}
