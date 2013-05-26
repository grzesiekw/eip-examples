package com.gw.eip.endpoint.idempotent.receiver;

import com.gw.eip.router.splitter.*;
import org.apache.camel.*;
import org.apache.camel.builder.*;
import org.apache.camel.impl.*;
import org.apache.camel.processor.idempotent.*;
import org.junit.*;

import static org.fest.assertions.Assertions.*;

/**
 * User: grzesiek
 * Date: 26.05.13
 * Time: 13:24
 */
public class IdempotentReceiverTest {

	private final CamelContext context = new DefaultCamelContext();

	private final MessageConsumer consumer = new MessageConsumer();

	@Before
	public void setUpContext() throws Exception {
		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("direct:a").idempotentConsumer(header("messageId"), MemoryIdempotentRepository.memoryIdempotentRepository(100)).bean(consumer);
			}
		});

		context.start();
	}

	@Test
	public void shouldReceiveMessageOnlyOnce() {
		final ProducerTemplate producerTemplate = context.createProducerTemplate();

		producerTemplate.sendBodyAndHeader("direct:a", "A", "messageId", "1");
		producerTemplate.sendBodyAndHeader("direct:a", "A", "messageId", "1");

		assertThat(consumer.getMessages()).hasSize(1);
	}
}
