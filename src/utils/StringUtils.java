/**
Copyright 2016 ZeusAFK

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;

import org.apache.commons.codec.digest.DigestUtils;

public class StringUtils {
	private static String log_file = "log";
	private static final int INDEX_NOT_FOUND = -1;

	public static String getLog_file() {
		return log_file;
	}

	public static void setLog_file(String log_file) {
		StringUtils.log_file = log_file;
	}

	private static void printLine(String data) {
		System.out.println(data);
	}

	public static void printWarning(String data) {
		data = "[WARNG]\t" + data;
		printLine(data);
		writeLog(data);
	}

	public static void printFatalError(String data) {
		data = "[FATAL]\t" + data;
		printLine(data);
		writeLog(data);
	}

	public static void printInfo(String data) {
		data = "[INFO]\t" + data;
		printLine(data);
		writeLog(data);
	}

	public static void writeLog(String data) {
		try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(log_file, true)))) {
			out.println(data);
		} catch (IOException e) {
			System.out.println("[FATAL] Error writting to log file: " + e.getMessage());
			System.exit(0);
		}
	}

	public static int countMatches(String str, String sub) {
		if (isEmpty(str) || isEmpty(sub)) {
			return 0;
		}
		int count = 0;
		int idx = 0;
		while ((idx = str.indexOf(sub, idx)) != INDEX_NOT_FOUND) {
			count++;
			idx += sub.length();
		}
		return count;
	}

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static String getSha1(String value) {
		return DigestUtils.sha1Hex(value);
	}

	public static String getContentFromFile(String location) {
		try {
			String response = "";
			String line;
			BufferedReader br;
			br = new BufferedReader(new FileReader(location));
			boolean skip = false;
			while ((line = br.readLine()) != null) {
				if (line.contains("<wb-skip>"))
					skip = true;

				if (!skip && !line.contains("<wb-skip/>")) {
					response += line;
				}

				if (line.contains("</wb-skip>"))
					skip = false;
			}
			br.close();
			return response;
		} catch (IOException e) {
			return null;
		}
	}

	public static String byteArrayToHexString(byte[] bytes) {
		String result = "";
		for (int i = 0; i < bytes.length; i++) {
			result += Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}

	public static String getFileSha1(String location) {
		try {
			DigestInputStream dis = new DigestInputStream(Files.newInputStream(Paths.get(location)), MessageDigest.getInstance("SHA-1"));
			byte[] digest = dis.getMessageDigest().digest();
			return byteArrayToHexString(digest);
		} catch (Exception e) {
			return null;
		}
	}
}