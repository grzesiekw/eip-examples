package com.gw.eip.simple;

import com.gw.eip.*;
import org.apache.camel.*;
import org.apache.camel.builder.*;
import org.apache.camel.impl.*;
import org.junit.*;
import org.junit.rules.*;

import java.io.*;

import static com.gw.eip.FileUtils.*;

/**
 * User: grzesiek
 * Date: 15.04.13
 * Time: 22:53
 */
public class SimpleFileTest {

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
		createNewFile(sourceFolder, "file.txt");

		FileUtils.assertThatFileExistsInFolder(targetFolder);
	}


}