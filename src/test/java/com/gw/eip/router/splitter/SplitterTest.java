package com.gw.eip.router.splitter;

import org.apache.camel.*;
import org.apache.camel.builder.*;
import org.apache.camel.impl.*;
import org.junit.*;

import static org.fest.assertions.Assertions.*;

/**
 * User: grzesiek
 * Date: 20.04.13
 * Time: 16:32
 */
public class SplitterTest {

	private final CamelContext context = new DefaultCamelContext();

	private MessageConsumer messageConsumer = new MessageConsumer();

	@Before
	public void setUpContext() throws Exception {
		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("direct:input").split(body().tokenize(",")).bean(messageConsumer, "consume");

			}
		});

		context.start();
	}

	@After
	public void tearDownContext() throws Exception {
		context.stop();
	}

	@Test
	public void shouldSplitMessage() {
		// given
		final ProducerTemplate producerTemplate = context.createProducerTemplate();
		final String message = "a,b,c";

		// when
		producerTemplate.sendBody("direct:input", message);

		// then
		assertThat(messageConsumer.getMessages()).contains("a", "b", "c");
	}

}
