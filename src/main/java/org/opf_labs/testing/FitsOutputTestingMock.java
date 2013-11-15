/**
 * 
 */
package org.opf_labs.testing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author carl
 *
 */
public final class FitsOutputTestingMock {

	/**
	 * So a test mock for the tester 
	 * @param args
	 */
	public static void main(final String[] args) {
		// If there's insufficient args throw a bad error 
		if (args.length < 3) {
			System.err.println("Insufficient args");
			System.exit(-1);
		}
		File comparisonOne = new File(args[0]);
		File comparisonTwo = new File(args[1]);
		String shaTested = args[2];
		if (!comparisonOne.isDirectory() || !comparisonTwo.isDirectory()) {
			System.err.println("Expecting two directories to compare");
			System.exit(-1);
		}
		System.out.println("Testing commit " + shaTested);
		// Execute with OK for now
		System.exit(testOutputDirs(comparisonOne, comparisonTwo));
	}
	
	private static int testOutputDirs(File masterOutput, File testOutput) {
		File[] masterFiles = masterOutput.listFiles();
		File[] testFiles = testOutput.listFiles();
		if ((masterFiles.length < 1) || (testFiles.length < 1)) {
			return 125;
		}
		if (!(masterFiles.length == testFiles.length)) {
			System.err.println("Test failed masterFiles:" + masterFiles.length + ", testFiles:" + testFiles.length);
			return 1;
		}
		
		Map<String, File> testMap = new HashMap<>();
		for (File test : testFiles) {
			if (test.isFile()) {
				String name = test.getAbsolutePath().replace(testOutput.getAbsolutePath(), ".");
				testMap.put(name, test);
			}
		}
		for (File master : masterFiles) {
			if (master.isFile()) {
				String name = master.getAbsolutePath().replace(masterOutput.getAbsolutePath(), ".");
				if (! testMap.containsKey(name)) {
					System.err.println("Test failed, test set didn't contain file " + name);
					return 1;
				}
				File test = testMap.get(name);
				if (!(master.length() == test.length())) {
					System.err.println("Test failed, test set and master lengths differ for " + name);
					return 1;
				}
			}
		}
		return 0;
	}

}
