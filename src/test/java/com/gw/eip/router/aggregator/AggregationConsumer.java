package com.gw.eip.router.aggregator;

import java.util.*;

/**
 * User: grzesiek
 * Date: 20.04.13
 * Time: 18:51
 */
public class AggregationConsumer {

	private final List<String> messages = new ArrayList<String>();

	public void consume(String message) {
		this.messages.add(message);
	}

	public List<String> getMessages() {
		return messages;
	}
}
