package com.rate.limitter.vo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rate-limiter")
public class UserApiThresholdConfiguration {

	private Map<String, List<ApiAllowedThershold>> userApiMap = new HashMap<>();

	public Map<String, List<ApiAllowedThershold>> getUserApiMap() {
		return userApiMap;
	}

	public void setUserApiMap(Map<String, List<ApiAllowedThershold>> userApiMap) {
		this.userApiMap = userApiMap;
	}

}
