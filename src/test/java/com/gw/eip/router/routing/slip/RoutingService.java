package com.gw.eip.router.routing.slip;

import org.apache.camel.*;

import java.util.*;

/**
 * User: grzesiek
 * Date: 21.04.13
 * Time: 13:36
 */
public class RoutingService {

	private final Map<String, String> routingTable = new HashMap<String, String>();

	public String route(Exchange exchange) {
		final String routingHeader = exchange.getIn().getHeader("routing-type", String.class);
		return routingTable.get(routingHeader);
	}

	public void control(String routingHeader, String routing) {
		routingTable.put(routingHeader, routing);
	}
}
