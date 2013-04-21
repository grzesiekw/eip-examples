package com.gw.eip.router.recipient.list;

import org.apache.camel.*;
import org.apache.camel.builder.*;
import org.apache.camel.impl.*;
import org.junit.*;
import org.junit.rules.*;

import java.io.*;

import static com.gw.eip.FileUtils.*;

/**
 * User: grzesiek
 * Date: 20.04.13
 * Time: 13:10
 */
public class RecipientListTest {

	private RecipientListBean recipientList;
	private final CamelContext camelContext = new DefaultCamelContext();

	@Rule
	public final TemporaryFolder testFolder = new TemporaryFolder();

	private File sourceFolder;
	private File aFilesTargetFolder;
	private File bFilesTargetFolder;

	@Before
	public void setUpContext() throws Exception {
		sourceFolder = testFolder.newFolder();
		aFilesTargetFolder = testFolder.newFolder();
		bFilesTargetFolder = testFolder.newFolder();

		recipientList = new RecipientListBean("file://" + aFilesTargetFolder.getPath(), "file://" + bFilesTargetFolder.getPath());

		camelContext.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("file://" + sourceFolder.getPath()).recipientList(method(recipientList, "route"));
			}
		});

		camelContext.start();
	}

	@Test
	public void shouldCopyFileToRecipientList() throws Exception {
		createNewFile(sourceFolder, "file.txt");

		assertThatFileExistsInFolder(aFilesTargetFolder);
		assertThatFileExistsInFolder(bFilesTargetFolder);
	}

}
