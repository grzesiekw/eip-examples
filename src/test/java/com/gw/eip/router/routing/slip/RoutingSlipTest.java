package com.gw.eip.router.routing.slip;

import com.gw.eip.router.aggregator.*;
import org.apache.camel.*;
import org.apache.camel.builder.*;
import org.apache.camel.impl.*;
import org.junit.*;

import java.util.*;
import java.util.concurrent.*;

import static com.jayway.awaitility.Awaitility.*;
import static com.jayway.awaitility.Duration.*;
import static org.hamcrest.Matchers.*;

/**
 * User: grzesiek
 * Date: 21.04.13
 * Time: 13:26
 */
public class RoutingSlipTest {

	private final CamelContext context = new DefaultCamelContext();

	private final RoutingService routingService = new RoutingService();
	private final AggregationConsumer aggregationConsumer = new AggregationConsumer();

	@Before
	public void setUpContext() throws Exception {
		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("direct:input").routingSlip().method(routingService, "route").to("direct:output");

				from("direct:a").bean(new ProcessorBean("a"));
				from("direct:b").bean(new ProcessorBean("b"));
				from("direct:c").bean(new ProcessorBean("c"));

				from("direct:output").bean(aggregationConsumer);
			}
		});

		context.start();
	}

	@Test
	public void shouldProcessMessage() throws Exception {
		final ProducerTemplate producerTemplate = context.createProducerTemplate();
		routingService.control("revertRouting", "direct:c,direct:b,direct:a");

		producerTemplate.sendBodyAndHeader("direct:input", "a,b,c", "routing-type", "revertRouting");

		waitAtMost(FIVE_SECONDS).until(aggregatedMessage(), contains("a,b,c c b a"));
	}

	private Callable<List<String>> aggregatedMessage() {
		return new Callable<List<String>>() {
			@Override
			public List<String> call() throws Exception {
				return aggregationConsumer.getMessages();
			}
		};
	}

}
