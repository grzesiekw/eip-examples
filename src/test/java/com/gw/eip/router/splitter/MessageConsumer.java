package com.gw.eip.router.splitter;

import java.util.*;

/**
 * User: grzesiek
 * Date: 20.04.13
 * Time: 16:40
 */
public class MessageConsumer {
	private List<String> messages = new ArrayList<String>();

	public void consume(String message) {
		messages.add(message);
	}

	public List<String> getMessages() {
		return messages;
	}
}
