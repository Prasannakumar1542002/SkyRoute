package com.skyroute.flightpath.database;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class APIInvoker {

	private static String sendApiRequest(String apiUrl, Map<String, String> headers, Map<String, Object> payload,
			String method) {
		try {
			CloseableHttpClient  client = HttpClients.createDefault();
			// Handle GET and POST
			if ("POST".equalsIgnoreCase(method)) {
				HttpPost post = new HttpPost(apiUrl);
				headers.forEach(post::setHeader);
				post.addHeader("content-type", "application/json");
				post.setEntity(new StringEntity(new JSONObject(payload).toString()));
				return EntityUtils.toString(client.execute(post).getEntity());
			} else if ("GET".equalsIgnoreCase(method)) {
				if (payload != null && !payload.isEmpty()) {
	                StringBuilder queryParams = new StringBuilder("?");
	                for (Map.Entry<String, Object> entry : payload.entrySet()) {
	                    queryParams.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8))
	                               .append("=")
	                               .append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8))
	                               .append("&");
	                }
	                queryParams.deleteCharAt(queryParams.length() - 1);
	                apiUrl += queryParams.toString();
	            }
				HttpGet get = new HttpGet(apiUrl);
				headers.forEach(get::setHeader);
				return EntityUtils.toString(client.execute(get).getEntity());
			} else {
				throw new IllegalArgumentException("Unsupported HTTP method: " + method);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "{\"error\": \"" + e.getMessage() + "\"}";
		}
	}

	public static JSONObject makePostRequest(String apiUrl, Map<String, String> headers, Map<String, Object> payload)
			throws JSONException {
		return new JSONObject(sendApiRequest(apiUrl, headers, payload, "POST"));
	}
	
	public static String makePostPhpRequest(String apiUrl, Map<String, String> headers, Map<String, Object> payload)
			throws JSONException {
		return sendApiRequest(apiUrl, headers, payload, "POST");
	}

	public static JSONObject makeGetRequest(String apiUrl, Map<String, String> headers, Map<String, Object> payload)
			throws JSONException {
		return new JSONObject(sendApiRequest(apiUrl, headers, payload, "GET"));
	}

}
