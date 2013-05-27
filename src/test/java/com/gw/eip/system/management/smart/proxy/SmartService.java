package com.gw.eip.system.management.smart.proxy;

import org.apache.camel.*;

import java.util.*;

/**
 * User: grzesiek
 * Date: 27.05.13
 * Time: 22:37
 */
public class SmartService {

	private final List<String> messages = new ArrayList<String>();

	public void process(Exchange exchange) {
		messages.add(exchange.getIn().getBody(String.class));
	}

	public List<String> getMessages() {
		return messages;
	}
}
