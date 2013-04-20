package com.gw.eip.router.dynamic;

import org.apache.camel.*;
import org.apache.camel.builder.*;
import org.apache.camel.impl.*;
import org.junit.*;
import org.junit.rules.*;

import java.io.*;

import static com.gw.eip.FileUtils.*;

/**
 * User: grzesiek
 * Date: 17.04.13
 * Time: 19:50
 */
public class DynamicRouterTest {

	@Rule
	public final TemporaryFolder testFolder = new TemporaryFolder();

	private File sourceFolder;
	private File aFilesTargetFolder;
	private File bFilesTargetFolder;

	private final CamelContext context = new DefaultCamelContext();
	private final DynamicRouterBean dynamicRouter = new DynamicRouterBean();

	@Before
	public void setUpContext() throws Exception {
		sourceFolder = testFolder.newFolder();
		aFilesTargetFolder = testFolder.newFolder();
		bFilesTargetFolder = testFolder.newFolder();

		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("file://" + sourceFolder.getPath()).dynamicRouter().method(dynamicRouter, "route");
			}
		});

		context.start();
	}

	@After
	public void tearDownContext() throws Exception {
		context.stop();

		sourceFolder.delete();
		aFilesTargetFolder.delete();
		bFilesTargetFolder.delete();
	}

	@Test
	public void copyAFileFromSourceToATargetFolder() throws Exception {
		dynamicRouter.control("a", "file://" + aFilesTargetFolder.getPath());

		createNewFile(sourceFolder, "a-file.txt");

		assertThatFileExistsInFolder(aFilesTargetFolder);
	}

	@Test
	public void copyBFileFromSourceToBTargetFolder() throws Exception {
		dynamicRouter.control("b", "file://" + bFilesTargetFolder.getPath());

		createNewFile(sourceFolder, "b-file.txt");

		assertThatFileExistsInFolder(bFilesTargetFolder);
	}
}
