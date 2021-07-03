package com.mdc.medic.apimedic.services.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


@Service
@PropertySource(value = {"${com.mdc.medic.cache.config}", "cache.properties"}, ignoreResourceNotFound = true)
public class RedisServer {
    private Logger logger = LogManager.getLogger(getClass());

    private redis.embedded.RedisServer redisServer;

    @Autowired
    private CacheConfig config;

    @Value("${spring.redis.port}")
    private int redisPort;


    @PostConstruct
    public void start() {
        if (!config.isEmbeddedRedisActive()) {
            logger.info("Using provided Redis-server  ...");
            return;
        }
        logger.info("Starting local redis ...");

        try {
            redisServer = new redis.embedded.RedisServer(redisPort);
            redisServer.start();
        } catch (Exception e) {
            logger.info("Could not start embedded redis ...",e);
        }
    }

    @PreDestroy
    public void stop() {
        if (!config.isEmbeddedRedisActive()) {
            return;
        }

        redisServer.stop();
    }
}
