package com.gw.eip.router.aggregator;

import org.apache.camel.*;
import org.apache.camel.builder.*;
import org.apache.camel.impl.*;
import org.fest.assertions.*;
import org.junit.*;

/**
 * User: grzesiek
 * Date: 20.04.13
 * Time: 17:57
 */
public class AggregatorTest {

	private final CamelContext context = new DefaultCamelContext();
	private final AggregationConsumer aggregationConsumer = new AggregationConsumer();

	@Before
	public void setUpContext() throws Exception {
		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("direct:input").split().tokenize(",").to("direct:split");

				from("direct:split").aggregate(header(Exchange.CORRELATION_ID), new AppendAggregationStrategy()).completionSize(3).bean(aggregationConsumer, "consume");
			}
		});

		context.start();
	}

	@Test
	public void shouldAggregateSplittedMessage() {
		final ProducerTemplate producerTemplate = context.createProducerTemplate();
		final String message = "a,b,c";

		producerTemplate.sendBody("direct:input", message);

		Assertions.assertThat(aggregationConsumer.getMessages()).containsExactly("a, b, c");
	}

}