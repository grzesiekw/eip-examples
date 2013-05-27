package com.gw.eip.system.management.smart.proxy;

import org.apache.camel.*;

import java.util.*;

/**
 * User: grzesiek
 * Date: 27.05.13
 * Time: 22:35
 */
public class SmartProxy {

	private final Map<String, String> map = new HashMap<String, String>();

	private int id = 1;

	public void request(Exchange exchange) {
		String returnAddress = exchange.getIn().getHeader("ReturnAddress", String.class);

		String smartId = nextId();
		map.put(smartId, returnAddress);

		exchange.getOut().setHeader("SmartId", smartId);
		exchange.getOut().setBody(exchange.getIn().getBody());
	}

	private String nextId() {
		return "smart-" + id++;
	}

	public void reply(Exchange exchange) {
		final String smartId = exchange.getIn().getHeader("SmartId", String.class);

		exchange.getOut().setHeader("ReturnAddress", map.get(smartId));

		exchange.getOut().setBody(exchange.getIn().getBody());
	}

}
