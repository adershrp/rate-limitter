package com.rate.limitter.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

@Configuration
@EnableWebSecurity
class SecurityConfig extends WebSecurityConfigurerAdapter {
	/**
	 * Basic Authentication.
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic();
		http.authorizeRequests().anyRequest().authenticated();
	}

	@Override
	@Bean
	protected UserDetailsService userDetailsService() {
		return new InMemoryUserDetailsManager();
	}

	/**
	 * Configuring In-Memory store with 3 users.
	 * 
	 * @param manager
	 * @return
	 */
	@Bean(name = "initializeUserDetails")
	InitializingBean initializer(UserDetailsManager manager) {
		return () -> {
			UserDetails user1 = User.withDefaultPasswordEncoder().username("user1").password("password")
					.roles("USER", "ADMIN").build();
			manager.createUser(user1);
			UserDetails user2 = User.withDefaultPasswordEncoder().username("user2").password("password").roles("USER")
					.build();
			manager.createUser(user2);
			UserDetails system = User.withDefaultPasswordEncoder().username("system").password("password").roles("USER")
					.build();
			manager.createUser(system);
		};
	}
}
