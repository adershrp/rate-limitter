package com.rate.limitter.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.rate.limitter.exception.TooManyRequestException;
import com.rate.limitter.service.UserTokenStoreService;

/**
 * 
 * Intercept the requests and limit the access.
 *
 */
@Component
@ConditionalOnBean(UserTokenStoreService.class)
public class RequestInterceptor extends HandlerInterceptorAdapter {

	private static final Logger log = LoggerFactory.getLogger(RequestInterceptor.class);

	@Autowired
	private UserTokenStoreService userTokenStoreService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String requestURI = request.getRequestURI();
		if (requestURI.equalsIgnoreCase("/error")) {
			// Skip /error pages.
			return true;
		}
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		// Validate user and requested pages is under configured limit.
		boolean allowUser = userTokenStoreService.isLimitResourceAccess(authentication.getName(), requestURI);
		if (!allowUser) {
			log.error("Too Many requests from {} for URI {}.", authentication.getName(), requestURI);
			throw new TooManyRequestException();
		}
		log.info("Prinicipal {} and request URI {} allowed {}", authentication.getName(), requestURI, allowUser);
		return allowUser;
	}

}
