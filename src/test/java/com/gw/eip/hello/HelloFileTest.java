package com.gw.eip.hello;

import org.apache.camel.*;
import org.apache.camel.builder.*;
import org.apache.camel.impl.*;
import org.junit.*;
import org.junit.rules.*;

import java.io.*;
import java.util.concurrent.*;

import static com.jayway.awaitility.Awaitility.*;
import static java.util.concurrent.TimeUnit.*;
import static org.hamcrest.Matchers.*;

/**
 * User: grzesiek
 * Date: 15.04.13
 * Time: 22:53
 */
public class HelloFileTest {

	private final CamelContext context = new DefaultCamelContext();

	@Rule
	public final TemporaryFolder testFolder = new TemporaryFolder();

	private File sourceFolder;
	private File targetFolder;

	@Before
	public void setUpContext() throws Exception {
		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("file://" + sourceFolder.getPath()).to("file://" + targetFolder.getPath());
			}
		});

		context.start();
	}

	@Before
	public void createFolders() throws IOException {
		sourceFolder = testFolder.newFolder();
		targetFolder = testFolder.newFolder();
	}

	@After
	public void tearDownContext() throws Exception {
		context.stop();
	}

	@After
	public void deleteFolders() {
		sourceFolder.delete();
		targetFolder.delete();
	}

	@Test
	public void copyFileFromSourceToTarget() throws Exception {
		createNewFile();

		assertThatFileExistsInTargetFolder();
	}

	private void assertThatFileExistsInTargetFolder() throws Exception {
		waitAtMost(5, SECONDS).until(targetFolderSize(), is(equalTo(1)));
	}

	private Callable<Integer> targetFolderSize() {
		return new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				return targetFolder.list().length;
			}
		};
	}

	private void createNewFile() throws IOException {
		new File(sourceFolder, "hello.txt").createNewFile();
	}

}