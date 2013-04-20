package com.gw.eip.router.aggregator;

import org.apache.camel.*;
import org.apache.camel.processor.aggregate.*;

/**
 * User: grzesiek
 * Date: 20.04.13
 * Time: 18:18
 */
public class AppendAggregationStrategy implements AggregationStrategy {

	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		final String body = newExchange.getIn().getBody(String.class);

		if (oldExchange != null) {
			final String oldBody = oldExchange.getIn().getBody(String.class);
			oldExchange.getIn().setBody(oldBody + ", " + body);
			return oldExchange;
		} else {
			return newExchange;
		}
	}
}
