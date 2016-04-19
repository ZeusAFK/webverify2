package network.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.channels.Channels;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import utils.StringUtils;
import utils.ThreadUtils;

public class HttpRequest {

	private String requestURL;
	private HttpURLConnection connection;
	private int attempts;

	public HttpRequest(String requestURL) {
		this.requestURL = requestURL;
		attempts = 0;
	}

	public String sendPost(HashMap<String, String> params) throws IOException {
		URL url = new URL(requestURL);
		connection = (HttpURLConnection) url.openConnection();
		connection.setReadTimeout(15000);
		connection.setConnectTimeout(15000);

		connection.setRequestMethod("POST");
		connection.setDoInput(true);
		connection.setDoOutput(true);

		OutputStream os = connection.getOutputStream();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
		writer.write(UrlDataString(params));

		writer.flush();
		writer.close();
		os.close();

		String response = getResponse();
		return response;
	}

	public String sendGet(HashMap<String, String> params) throws IOException {
		String formattedParams = UrlDataString(params);
		URL url = new URL(requestURL + "?" + formattedParams);
		connection = (HttpURLConnection) url.openConnection();
		connection.setReadTimeout(15000);
		connection.setConnectTimeout(15000);
		connection.setRequestMethod("GET");
		connection.setDoInput(true);
		connection.setDoOutput(true);
		String response = getResponse();
		return response;
	}

	private String getResponse() throws IOException {
		String response = "";

		int responseCode = connection.getResponseCode();

		if (responseCode == HttpsURLConnection.HTTP_OK) {
			String line;
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while ((line = br.readLine()) != null) {
				response += line;
			}
		} else {
			response = "";
		}

		return response;
	}

	public String getHeaderField(String field) {
		try {
			connection = (HttpURLConnection) new URL(requestURL).openConnection();
			connection.setReadTimeout(15000);
			connection.setConnectTimeout(15000);
			connection.setRequestMethod("HEAD");
			return connection.getHeaderField(field);
		} catch (IOException e) {
			if (attempt()) {
				return getHeaderField(field);
			} else {
				return null;
			}
		}
	}

	public int getContentLength() {
		if (Exists()) {
			return connection.getContentLength();
		} else {
			return 0;
		}
	}

	public String getContentType() {
		if (Exists()) {
			return connection.getContentType();
		} else {
			return null;
		}
	}

	public boolean Exists() {
		try {
			connection = (HttpURLConnection) new URL(requestURL).openConnection();
			connection.setReadTimeout(15000);
			connection.setConnectTimeout(15000);
			connection.setRequestMethod("HEAD");
			return (connection.getResponseCode() == HttpURLConnection.HTTP_OK);
		} catch (IOException e) {
			if (attempt()) {
				return Exists();
			} else {
				return false;
			}
		}
	}

	public String getContent() {
		if (Exists()) {
			try {
				connection = (HttpURLConnection) new URL(requestURL).openConnection();
				connection.setReadTimeout(15000);
				connection.setConnectTimeout(15000);
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setRequestMethod("GET");

				String response = "";
				String line;
				BufferedReader br;
				br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				while ((line = br.readLine()) != null) {
					response += line;
				}
				br.close();
				return response;
			} catch (IOException e) {
				if (attempt()) {
					return getContent();
				} else {
					return null;
				}
			}
		} else {
			return null;
		}
	}

	public String getBinaryContentSha1() {
		if (Exists()) {
			try {
				connection = (HttpURLConnection) new URL(requestURL).openConnection();
				connection.setReadTimeout(15000);
				connection.setConnectTimeout(15000);
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setRequestMethod("GET");

				DigestInputStream dis = new DigestInputStream(connection.getInputStream(), MessageDigest.getInstance("SHA-1"));
				return StringUtils.byteArrayToHexString(dis.getMessageDigest().digest());
			} catch (Exception e) {
				if (attempt()) {
					return getBinaryContentSha1();
				} else {
					return null;
				}
			}
		} else {
			return null;
		}
	}

	public String getIpAddress() {
		try {
			return InetAddress.getByName(new URL(requestURL).getHost()).getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean saveToFile(String file) {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.getChannel().transferFrom(Channels.newChannel(new URL(requestURL).openStream()), 0, Long.MAX_VALUE);
			fos.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	private String UrlDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for (Map.Entry<String, String> entry : params.entrySet()) {
			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
		}

		return result.toString();
	}

	private boolean attempt() {
		if (attempts < 10) {
			attempts++;
			StringUtils.printWarning("Net timeout: " + requestURL + " waiting " + attempts + " seconds");
			ThreadUtils.sleepSeconds(attempts);
			return true;
		} else {
			StringUtils.printWarning("Net timeout: " + requestURL + " no more attempts allowed");
			return false;
		}
	}
}