package com.autentia.tutoriales;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.autentia.tutoriales.redis.StringRedisRepository;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application {

	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	@Bean
	JedisConnectionFactory connectionFactory() {
		return new JedisConnectionFactory();
	}
	
	@Bean
	StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
		final StringRedisTemplate template = new StringRedisTemplate(connectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		  
		return template;
	}

	@Bean
	StringRedisRepository stringRedisRepository(StringRedisTemplate template) {
		return new StringRedisRepository(template);
	}
	
	public static void main(String[] args) throws InterruptedException {
		LOGGER.debug("Initializating app...");
		
		SpringApplication.run(Application.class, args);
	}
}