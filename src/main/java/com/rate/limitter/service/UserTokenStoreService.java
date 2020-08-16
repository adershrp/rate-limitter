package com.rate.limitter.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import com.rate.limitter.vo.ApiAllowedThershold;
import com.rate.limitter.vo.TokenBucket;
import com.rate.limitter.vo.UserApiThresholdConfiguration;

/**
 * 
 * Only if properties under "default.user-api" configured this service gets
 * created.
 *
 */
@Service
@ConditionalOnProperty(prefix = "default.user-api", name = { "duration", "threshold" })
@EnableConfigurationProperties(value = { UserApiThresholdConfiguration.class, ApiAllowedThershold.class })
public class UserTokenStoreService {

	private static final Logger log = LoggerFactory.getLogger(UserTokenStoreService.class);
	/**
	 * Token Store. In-Memory
	 */
	public Map<String, Map<String, TokenBucket>> userTokenStore = new ConcurrentHashMap<>();

	@Autowired
	@Qualifier("rateLimitThreadPoolExecutor")
	private ThreadPoolTaskScheduler rateLimitThreadPoolExecutor;
	/**
	 * Default API threshold values.
	 */
	@Autowired
	private ApiAllowedThershold defaultApiThreshold;

	/**
	 * @param user
	 * @param thershold
	 */
	private void addUserTokenStore(String user, ApiAllowedThershold thershold) {
		if (userTokenStore.containsKey(user)) {
			userTokenStore.get(user).put(thershold.getApi(), createTokens(thershold));
		} else {
			Map<String, TokenBucket> map = new HashMap<>();
			map.put(thershold.getApi(), createTokens(thershold));
			userTokenStore.put(user, map);
		}
	}

	/**
	 * 
	 * @param thershold
	 * @return
	 */
	private TokenBucket createTokens(ApiAllowedThershold thershold) {
		TokenBucket tokenBucket = new TokenBucket(thershold.getThreshold());
		// scheduling runnable at fixed rate.
		rateLimitThreadPoolExecutor.scheduleAtFixedRate(tokenBucket.tokenRefresh(), thershold.getDuration());
		return tokenBucket;
	}

	public boolean isLimitResourceAccess(String username, String requestURI) {
		if (!Optional.ofNullable(userTokenStore.get(username)).isPresent()
				|| !Optional.ofNullable(userTokenStore.get(username).get(requestURI)).isPresent()) {
			ApiAllowedThershold thershold = createThershold(requestURI);
			addUserTokenStore(username, thershold);
			log.info("Token for User {} and URI {} not present.", username, requestURI);
		}
		TokenBucket tokenBucket = userTokenStore.get(username).get(requestURI);
		return tokenBucket.allow();
	}

	private ApiAllowedThershold createThershold(String requestURI) {
		ApiAllowedThershold thershold = new ApiAllowedThershold();
		thershold.setApi(requestURI);
		thershold.setDuration(defaultApiThreshold.getDuration());
		thershold.setThreshold(defaultApiThreshold.getThreshold());
		return thershold;
	}

	/**
	 * Initializing token store from values configured in application.yaml
	 * properties.
	 * 
	 * @param limiterConfiguration
	 * @return
	 */
	@Bean(name = "initializeTokenStore")
	InitializingBean initializer(UserApiThresholdConfiguration limiterConfiguration) {
		return () -> {
			limiterConfiguration.getUserApiMap().entrySet().stream().forEach(t -> {
				String key = t.getKey();
				for (ApiAllowedThershold thershold : t.getValue()) {
					addUserTokenStore(key, thershold);
				}
				log.info("Initializing Token Store for User {}.", key);
			});
		};
	}
}
