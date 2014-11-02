package com.autentia.tutoriales.tweets;

import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.autentia.tutoriales.redis.StringRedisRepository;

@Component
public class TweetsRetriever {

	private static final Logger logger = LoggerFactory.getLogger(TweetsRetriever.class);
	
	@Autowired
	private StringRedisRepository redisRepository;
	
	@PostConstruct
	public void retrieve() {
		final Set<String> tweets = redisRepository.getAllValuesBy(TweetIngestor.TWEET_KEY + "*");
		
		for (String tweet : tweets) {
			logger.info(tweet);
		}
	}
}
