package com.gw.eip.endpoint.selective.consumer;

import com.gw.eip.router.splitter.*;
import org.apache.camel.*;
import org.apache.camel.builder.*;
import org.apache.camel.impl.*;
import org.junit.*;

import static org.fest.assertions.Assertions.*;

/**
 * User: grzesiek
 * Date: 26.05.13
 * Time: 12:22
 */
public class SelectiveConsumerTest {

	private final CamelContext context = new DefaultCamelContext();

	private final MessageConsumer consumer = new MessageConsumer();

	@Before
	public void setUpContext() throws Exception {
		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("direct:a").filter(header("header").isEqualTo("a")).bean(consumer);
			}
		});

		context.start();
	}

	@Test
	public void shouldConsumeMessagesWithSpecifiedHeader() {
		final ProducerTemplate producerTemplate = context.createProducerTemplate();

		producerTemplate.sendBodyAndHeader("direct:a", "A", "header", "a");
		producerTemplate.sendBodyAndHeader("direct:a", "A", "header", "b");

		assertThat(consumer.getMessages()).hasSize(1);
	}

}