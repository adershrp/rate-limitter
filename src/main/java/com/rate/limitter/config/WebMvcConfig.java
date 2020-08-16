package com.rate.limitter.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.rate.limitter.interceptor.RequestInterceptor;
import com.rate.limitter.service.UserTokenStoreService;

@Configuration
@ConditionalOnBean(UserTokenStoreService.class)
public class WebMvcConfig implements WebMvcConfigurer {
	/**
	 * Adding RequestInterceptor.
	 */
	@Autowired
	private RequestInterceptor requestInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(requestInterceptor);
	}
}
