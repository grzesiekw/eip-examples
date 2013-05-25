package com.gw.eip.endpoint.event.consumer;

import org.apache.camel.*;
import org.apache.camel.impl.*;
import org.junit.*;

import static org.fest.assertions.Assertions.*;

/**
 * User: grzesiek
 * Date: 25.05.13
 * Time: 14:26
 */
public class EventDrivenConsumer {

	private final CamelContext context = new DefaultCamelContext();

	private final ProducerTemplate producerTemplate = context.createProducerTemplate();
	private final ConsumerTemplate consumerTemplate = context.createConsumerTemplate();

	@Before
	public void setUpContext() throws Exception {
		context.start();
		consumerTemplate.start();
	}

	@After
	public void tearDownContext() throws Exception {
		consumerTemplate.stop();
		context.stop();
	}

	@Test
	public void shouldReceiveMessage() throws Exception {
		final String message = "msg";
		final String endpointUri = "seda:a";
		producerTemplate.sendBody(endpointUri, message);

		final Exchange exchange = consumerTemplate.receive(endpointUri);

		assertThat(exchange.getIn().getBody()).isEqualTo(message);
	}
}
