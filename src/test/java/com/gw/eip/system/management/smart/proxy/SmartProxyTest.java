package com.gw.eip.system.management.smart.proxy;

import com.gw.eip.router.splitter.*;
import org.apache.camel.*;
import org.apache.camel.builder.*;
import org.apache.camel.impl.*;
import org.junit.*;

import java.util.*;

import static org.fest.assertions.Assertions.*;

/**
 * User: grzesiek
 * Date: 27.05.13
 * Time: 22:33
 */
public class SmartProxyTest {

	private final CamelContext context = new DefaultCamelContext();

	private final SmartProxy smartProxy = new SmartProxy();
	private final SmartService smartService = new SmartService();

	private final MessageConsumer firstMessageConsumer = new MessageConsumer();
	private final MessageConsumer secondMessageConsumer = new MessageConsumer();

	@Before
	public void setUpContext() throws Exception {
		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("direct:a").bean(smartProxy, "request").to("direct:b");
				from("direct:b").bean(smartService).to("direct:c");
				from("direct:c").bean(smartProxy, "reply").recipientList(header("ReturnAddress"));

				from("direct:d").bean(firstMessageConsumer);
				from("direct:e").bean(secondMessageConsumer);
			}
		});

		context.start();
	}

	@Test
	public void shouldReplyToSecondMessageConsumer() {
		final ProducerTemplate producerTemplate = context.createProducerTemplate();
		final Map<String, Object> headers = new HashMap<String, Object>();
		headers.put("ReturnAddress", "direct:e");

		producerTemplate.sendBodyAndHeaders("direct:a", "A", headers);

		assertThat(secondMessageConsumer.getMessages()).hasSize(1);
		assertThat(smartService.getMessages()).hasSize(1);
	}

}
