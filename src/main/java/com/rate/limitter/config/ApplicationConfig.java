package com.rate.limitter.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class ApplicationConfig {
	/**
	 * Configuring ThreadPoolTaskScheduler to limit number of threads running to
	 * refresh token.
	 * 
	 * @return
	 */
	@ConditionalOnProperty(prefix = "default.user-api", name = { "duration", "threshold" })
	@Bean(name = "rateLimitThreadPoolExecutor")
	public ThreadPoolTaskScheduler rateLimitThreadPoolExecutor() {
		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setPoolSize(3);
		threadPoolTaskScheduler.setThreadNamePrefix("RateLimitTaskScheduler-");
		return threadPoolTaskScheduler;
	}

}
