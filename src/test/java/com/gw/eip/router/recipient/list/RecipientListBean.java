package com.gw.eip.router.recipient.list;

import org.apache.camel.*;

import java.util.*;

/**
 * User: grzesiek
 * Date: 20.04.13
 * Time: 13:12
 */
public class RecipientListBean {

	private final List<String> uris;

	public RecipientListBean(String... uris) {
		this.uris = Arrays.asList(uris);
	}

	public List<String> route(Exchange exchange) {
		return uris;
	}

}
