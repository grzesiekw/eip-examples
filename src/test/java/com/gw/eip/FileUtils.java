package com.gw.eip;

import java.io.*;
import java.util.concurrent.*;

import static com.jayway.awaitility.Awaitility.*;
import static java.util.concurrent.TimeUnit.*;
import static org.hamcrest.Matchers.*;

/**
 * User: grzesiek
 * Date: 16.04.13
 * Time: 20:51
 */
public class FileUtils {

	public static void createNewFile(File sourceFolder, String fileName) throws IOException {
		new File(sourceFolder, fileName).createNewFile();
	}

	public static void assertThatFileExistsInFolder(File targetFolder) throws Exception {
		waitAtMost(5, SECONDS).until(targetFolderSize(targetFolder), is(equalTo(1)));
	}

	private static Callable<Integer> targetFolderSize(final File folder) {
		return new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				return folder.list().length;
			}
		};
	}

}
