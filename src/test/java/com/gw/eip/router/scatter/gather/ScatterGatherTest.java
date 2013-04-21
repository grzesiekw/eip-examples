package com.gw.eip.router.scatter.gather;

import com.gw.eip.router.aggregator.*;
import com.gw.eip.router.recipient.list.*;
import org.apache.camel.*;
import org.apache.camel.builder.*;
import org.apache.camel.impl.*;
import org.junit.*;

import static org.fest.assertions.Assertions.*;

/**
 * User: grzesiek
 * Date: 21.04.13
 * Time: 10:59
 */
public class ScatterGatherTest {

	private final CamelContext context = new DefaultCamelContext();

	private final AggregationConsumer aggregationConsumer = new AggregationConsumer();
	private final RecipientListBean recipientList = new RecipientListBean("direct:a", "direct:b", "direct:c");

	@Before
	public void setUpContext() throws Exception {
		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("direct:input").recipientList().method(recipientList, "route");

				from("direct:a").to("direct:output");
				from("direct:b").to("direct:output");
				from("direct:c").to("direct:output");

				from("direct:output").aggregate(header("requestId"), new AppendAggregationStrategy()).completionSize(3).bean(aggregationConsumer, "consume");
			}
		});

		context.start();
	}

	@Test
	public void shouldScatterAndGather() {
		final ProducerTemplate producerTemplate = context.createProducerTemplate();

		producerTemplate.sendBodyAndHeader("direct:input", "a", "requestId", "REQ-1");

		assertThat(aggregationConsumer.getMessages()).containsExactly("a, a, a");
	}

}
