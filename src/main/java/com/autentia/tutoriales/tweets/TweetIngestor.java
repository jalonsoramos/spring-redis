package com.autentia.tutoriales.tweets;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterObjectFactory;

import com.autentia.tutoriales.redis.StringRedisRepository;

@Component
public class TweetIngestor {

	private static final Logger logger = LoggerFactory.getLogger(TweetIngestor.class);

	private final Twitter twitter; 

	public static final String TWEET_KEY = "tweet_";
	
	@Autowired
	private StringRedisRepository redisRepository;
	
	public TweetIngestor() {
		this.twitter = new TwitterFactory().getInstance();
	}
	
	@PostConstruct
	public void searchByHashtag() {
		new Thread() {
			@Override
			public void run() {
				try {
					Query query = new Query("#ebola");

					int numTweets = 0;
					long init = System.currentTimeMillis();
					try {
						QueryResult result;

						do {
							result = twitter.search(query);
							for (Status status : result.getTweets()) {
								if (!status.isRetweet()) {
									redisRepository.add(TWEET_KEY + String.valueOf(status.getId()), TwitterObjectFactory.getRawJSON(status));
									numTweets++;
								}
							}

						} while ((query = result.nextQuery()) != null);
					
						logger.info(String.format("%s tweets received in %s millis", numTweets, System.currentTimeMillis() - init));
					} catch (TwitterException e) {
						throw new RuntimeException("Something was wrong retrieving tweets:", e);
					}
					
				} catch (Exception e) {
					logger.error("Error", e);
				}
			}
		}.start();
	}
}
