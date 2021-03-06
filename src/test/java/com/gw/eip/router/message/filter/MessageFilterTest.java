package com.gw.eip.router.message.filter;

import org.apache.camel.*;
import org.apache.camel.builder.*;
import org.apache.camel.impl.*;
import org.junit.*;
import org.junit.rules.*;

import java.io.*;

import static com.gw.eip.FileUtils.*;

/**
 * User: grzesiek
 * Date: 16.04.13
 * Time: 21:02
 */
public class MessageFilterTest {

	@Rule
	public final TemporaryFolder testFolder = new TemporaryFolder();

	private final CamelContext context = new DefaultCamelContext();

	private File sourceFolder;
	private File aFilesTargetFolder;
	private File bFilesTargetFolder;

	@Before
	public void setUpContext() throws Exception {
		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("file://" + sourceFolder.getPath()).multicast().parallelProcessing().to("direct:a", "direct:b");

				from("direct:b").filter(header("CamelFileName").regex("^b-.*txt$")).to("file://" + bFilesTargetFolder.getPath());
				from("direct:a").filter(header("CamelFileName").regex("^a-.*txt$")).to("file://" + aFilesTargetFolder.getPath());
			}
		});

		context.start();
	}

	@Before
	public void createFolders() throws IOException {
		sourceFolder = testFolder.newFolder();
		aFilesTargetFolder = testFolder.newFolder();
		bFilesTargetFolder = testFolder.newFolder();
	}

	@After
	public void tearDownContext() throws Exception {
		context.stop();
	}

	@Test
	public void copyAFileFromSourceToATargetFolder() throws Exception {
		createNewFile(sourceFolder, "a-file.txt");

		assertThatFileExistsInFolder(aFilesTargetFolder);
	}

	@Test
	public void copyBFileFromSourceToBTargetFolder() throws Exception {
		createNewFile(sourceFolder, "b-file.txt");

		assertThatFileExistsInFolder(bFilesTargetFolder);
	}

}