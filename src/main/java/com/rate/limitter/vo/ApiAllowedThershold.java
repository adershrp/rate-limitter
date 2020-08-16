package com.rate.limitter.vo;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;

@ConfigurationProperties(prefix = "default.user-api")
public class ApiAllowedThershold {

	private String api;

	private int threshold;

	@DurationUnit(ChronoUnit.SECONDS)
	private Duration duration;

	public String getApi() {
		return api;
	}

	public void setApi(String api) {
		this.api = api;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}
}