package com.gw.eip.router.dynamic;

import org.apache.camel.*;

import java.util.*;

/**
 * User: grzesiek
 * Date: 17.04.13
 * Time: 23:01
 */
public class DynamicRouterBean {

	private final Set<String> processedMessages = new HashSet<String>();

	private final Map<String, String> fileBasedRoutingTable = new HashMap<String, String>();

	public String route(Exchange exchange, @Header("CamelFileName") String fileName) {
		if (isNew(exchange)) {
			String fileNameKey = fileName.substring(0, 1);

			processedMessages.add(exchange.getIn().getMessageId());

			return fileBasedRoutingTable.get(fileNameKey);
		} else {
			return null;
		}
	}

	private boolean isNew(Exchange exchange) {
		return !processedMessages.contains(exchange.getIn().getMessageId());
	}

	public void control(String fileNameKey, String fileDestination) {
		fileBasedRoutingTable.put(fileNameKey, fileDestination);
	}
}