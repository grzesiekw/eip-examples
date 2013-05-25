package com.gw.eip.endpoint;

import org.apache.camel.*;
import org.apache.camel.builder.*;
import org.apache.camel.impl.*;
import org.junit.*;
import org.junit.rules.*;

import java.io.*;

import static com.gw.eip.FileUtils.*;
import static org.apache.camel.Exchange.*;
import static org.fest.assertions.Assertions.*;

/**
 * User: grzesiek
 * Date: 25.05.13
 * Time: 13:28
 */
public class PollingConsumerTest {

	private final CamelContext context = new DefaultCamelContext();

	@Rule
	public final TemporaryFolder testFolder = new TemporaryFolder();

	private File sourceFolder;
	private File targetFolder;

	@Before
	public void setUpContext() throws Exception {
		sourceFolder = testFolder.newFolder();
		targetFolder = testFolder.newFolder();

		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("file://" + sourceFolder.getPath() + "?initialDelay=500").to("file://" + targetFolder.getPath());
			}
		});

		context.start();
	}

	@Test
	public void shouldCopyFile() throws Exception {
		createNewFile(sourceFolder, "a.txt");

		assertThatFileExistsInFolder(targetFolder);
	}

	@Test
	public void shouldPollFileName() throws IOException {
		final ConsumerTemplate consumerTemplate = context.createConsumerTemplate();
		final String fileName = "b.txt";
		createNewFile(targetFolder, fileName);

		final Exchange exchange = consumerTemplate.receive("file://" + targetFolder.getPath());

		assertThat(exchange.getIn().getHeader(FILE_NAME)).isEqualTo(fileName);
	}
}
