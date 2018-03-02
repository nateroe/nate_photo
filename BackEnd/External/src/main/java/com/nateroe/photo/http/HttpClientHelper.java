/**
 * NatePhoto - A photo catalog and presentation application.
 * Copyright (C) 2018 Nathaniel Roe
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact nate [at] nateroe [dot] com
 */

package com.nateroe.photo.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientHelper {
	private final static Logger LOGGER = LoggerFactory.getLogger(HttpClientHelper.class);

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

	/**
	 * Request a given URL and return the result as a String
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String downloadFileToString(String url) throws Exception {
		String returnVal = null;
		int retry = 0;
		boolean failed = false;
		do {
			HttpResponse response = getUrl(url);

			int statusCode = response.getStatusLine().getStatusCode();

			LOGGER.trace("response: " + response.getStatusLine());

			HttpEntity entity = response.getEntity();
			if (statusCode == 200 && entity != null) {
				InputStream inputStream = entity.getContent();
				try {
					LOGGER.trace("Content length: " + entity.getContentLength());
					LOGGER.trace("Content type: " + entity.getContentType().getValue());
					LOGGER.debug("Download content: " + url);

					BufferedInputStream bis = new BufferedInputStream(inputStream);

					ByteArrayOutputStream baos;
					if (entity.getContentLength() > 0) {
						baos = new ByteArrayOutputStream((int) (entity.getContentLength()));
					} else {
						baos = new ByteArrayOutputStream();
					}

					int byteCount = 0;
					try (OutputStream out = new BufferedOutputStream(baos)) {
						byte[] buf = new byte[4096];
						int bytesRead = 0;
						while ((bytesRead = bis.read(buf)) > 0) {
							byteCount += bytesRead;
							out.write(buf, 0, bytesRead);
						}
					}

					LOGGER.debug("Downloaded " + byteCount + " bytes (of "
							+ entity.getContentLength() + ")");

					byte[] contentBytes = baos.toByteArray();

					// If a content length was specified, verify it
					if (byteCount > 0 && entity.getContentLength() > 0
							&& byteCount != entity.getContentLength()) {
						retry++;
						failed = true;
						LOGGER.warn("        Warning: premature exit. Retry " + retry
								+ " initiated. Downloaded " + byteCount + " of "
								+ entity.getContentLength() + " bytes.");
					} else {
						returnVal = new String(contentBytes, "UTF-8");

						LOGGER.trace("result: {}", returnVal);
					}

				} catch (Exception e) {
					LOGGER.info("failure (url: " + url + ")", e);
				} finally {
					inputStream.close();
				}
			} else {
				LOGGER.warn("    abort download, http status: " + statusCode + " url: " + url);
			}
		} while (failed == true && retry < MAX_RETRY);

		if (failed) {
			throw new Exception("Data download failed " + retry + " retries.");
		}

		return returnVal;
	}
}
