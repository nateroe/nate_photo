package com.nateroe.photo.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientHelper {
	private final static Logger log = LoggerFactory.getLogger(HttpClientHelper.class);

	public static final int TIMEOUT_MILLIS = 60000; // 60 seconds
	public static final int MAX_RETRY = 3;

	private static CookieStore cookieStore = new BasicCookieStore();

	private static HttpResponse getUrl(String url) throws ClientProtocolException, IOException {
		// HttpHost proxy = new HttpHost("localhost", 8888);
		// DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);

		RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.DEFAULT)
				.setSocketTimeout(TIMEOUT_MILLIS).setConnectTimeout(TIMEOUT_MILLIS).build();

		HttpClient client = HttpClientBuilder.create().setDefaultCookieStore(cookieStore)
				.setDefaultRequestConfig(globalConfig).build();

		RequestConfig localConfig = RequestConfig.copy(globalConfig)
				.setCookieSpec(CookieSpecs.DEFAULT).build();
		HttpGet get = new HttpGet(url);
		get.setConfig(localConfig);
		get.setHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
		return client.execute(get);
	}

	public static String downloadFileToString(String url) throws Exception {
		String returnVal = null;
		int retry = 0;
		boolean failed = false;
		do {
			HttpResponse response = getUrl(url);

			int statusCode = response.getStatusLine().getStatusCode();

			log.trace("response: " + response.getStatusLine());

			HttpEntity entity = response.getEntity();
			if (statusCode == 200 && entity != null) {
				InputStream inputStream = entity.getContent();
				try {
					log.trace("Content length: " + entity.getContentLength());
					log.trace("Content type: " + entity.getContentType().getValue());
					log.debug("Download content: " + url);

					@SuppressWarnings("unused")
					String mimeType = entity.getContentType().getValue();
					BufferedInputStream bis = new BufferedInputStream(inputStream);

					// XXX could validate mimeType
					ByteArrayOutputStream baos;
					if (entity.getContentLength() > 0) {
						baos = new ByteArrayOutputStream((int) (entity.getContentLength()));
					} else {
						baos = new ByteArrayOutputStream();
					}
					OutputStream out = new BufferedOutputStream(baos);

					int byteCount = 0;

					MessageDigest digest = MessageDigest.getInstance("MD5");

					byte[] buf = new byte[4096];
					int bytesRead = 0;
					while ((bytesRead = bis.read(buf)) > 0) {
						byteCount += bytesRead;
						out.write(buf, 0, bytesRead);
						digest.update(buf, 0, bytesRead);
					}

					out.close();

					log.debug("Downloaded " + byteCount + " bytes (of " + entity.getContentLength()
							+ ")");

					byte[] imageBytes = baos.toByteArray();

					// If a content length was specified, verify it
					if (byteCount > 0 && entity.getContentLength() > 0
							&& byteCount != entity.getContentLength()) {
						retry++;
						failed = true;
						log.warn("        Warning: premature exit. Retry " + retry
								+ " initiated. Downloaded " + byteCount + " of "
								+ entity.getContentLength() + " bytes.");
					} else {
						returnVal = new String(imageBytes, "UTF-8");

						log.info("result: " + returnVal);
					}

				} catch (Exception e) {
					log.info("failure (url: " + url + ")", e);
				} finally {
					inputStream.close();
				}
			} else {
				log.warn("    abort download, http status: " + statusCode + " url: " + url);
			}
		} while (failed == true && retry < MAX_RETRY);

		if (failed) {
			throw new Exception("Data download failed " + retry + " retries.");
		}

		return returnVal;
	}

	public static void main(String[] args) {
		// example cookie
		BasicClientCookie cookie = new BasicClientCookie("nateWasHere", "true");
		cookie.setVersion(0);
		cookie.setDomain(".phlake.org");
		cookie.setPath("/");
		cookieStore.addCookie(cookie);
	}
}
