package com.rate.limitter.vo;

import java.util.Objects;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TokenBucket {

	private static final Logger log = LoggerFactory.getLogger(TokenBucket.class);

	private final UUID identity = UUID.randomUUID();
	private final int maxRequestPerSec;
	private int tokens;

	public TokenBucket(int maxRequestPerSec) {
		this.maxRequestPerSec = maxRequestPerSec;
		tokens = maxRequestPerSec;
		log.info("Created new Token. {}", this);
	}

	/**
	 * Runnable helps to refresh the token count.
	 * 
	 * @return
	 */
	public Runnable tokenRefresh() {
		return new Runnable() {
			@Override
			public void run() {
				synchronized (this) {
					if (tokens < maxRequestPerSec) {
						log.info("Token Refreshing for [identity={}, maxRequestPerSec={}, tokens={}]", identity,
								maxRequestPerSec, tokens);
						tokens = Math.min(tokens + maxRequestPerSec, maxRequestPerSec);
					}
				}
			}
		};
	}

	public boolean allow() {
		synchronized (this) {
			if (tokens == 0) {
				return false;
			}
			tokens--;
			return true;
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(identity, maxRequestPerSec, tokens);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TokenBucket other = (TokenBucket) obj;
		return Objects.equals(identity, other.identity) && (maxRequestPerSec == other.maxRequestPerSec)
				&& (tokens == other.tokens);
	}

	@Override
	public String toString() {
		return String.format("TokenBucket [identity=%s, maxRequestPerSec=%s, tokens=%s]", identity, maxRequestPerSec,
				tokens);
	}

}