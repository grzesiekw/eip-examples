package com.gw.eip.router.routing.slip;

import org.apache.camel.*;

/**
 * User: grzesiek
 * Date: 21.04.13
 * Time: 15:59
 */
public class ProcessorBean {

	private final String name;

	public ProcessorBean(String name) {
		this.name = name;
	}

	public void consume(Exchange exchange) {
		final Message in = exchange.getIn();
		in.setBody(in.getBody(String.class) + " " + name + "");
	}
}
