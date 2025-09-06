package com.BankProject.BankApplication.Auth;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
public class RedisConfig {

     // CREATED A BEAN FOR REDIS CACHE MANAGER..
     // IT WILL BE USED IN THE SERVICES CLASS FOR STORING CACHE IN REDIS CACHE MANAGER.
     @Bean
     public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
          return RedisCacheManager.builder(redisConnectionFactory).build();
     }
}
