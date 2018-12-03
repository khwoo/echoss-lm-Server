package com.echoss.biz.test;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;

import com.amazonaws.util.Base64;

import net.sf.json.JSONObject;

public class APITest4Service {

	private final String SERVER_URL = "http://127.0.0.1:8080/csc";

	private final String CONTENT_TYPE = "application/json";
	private final String ACCEPT = "application/json";

	private final String CLIENT_ID = "8009871b-ed4b-4801-874b-30d4e64b1857";
	private final String CLIENT_SECRET = "65356233613366372D666435352D343566382D393638362D323264656264303530336561";
	
	public static void main(String[] args) {
		APITest4Service t = new APITest4Service();

//		t.checkService("S0001");

//		t.checkCustomer("S0001", "91d30684-ee4b-4ae0-a581-c45ca278b901");

//		t.registMember();
		
		t.getMember("S0001", "91d30684-ee4b-4ae0-a581-c45ca278b901");
	}

	public void checkService(String svcCd) {
		
		try {
			String accessKey = Base64.encodeAsString((CLIENT_ID + ":" + CLIENT_SECRET).getBytes());

			JSONObject rootJson = new JSONObject();
			rootJson.put("service", svcCd);

			String urlParameters = rootJson.toString();

			String res = communicate("PUT", SERVER_URL + "/csc/services/check", urlParameters, accessKey);
			System.out.println(getPrettyJson(res));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void checkCustomer(String svcCd, String custId) {
		
		try {
			String accessKey = Base64.encodeAsString((CLIENT_ID + ":" + CLIENT_SECRET).getBytes());

			JSONObject rootJson = new JSONObject();
			rootJson.put("service", svcCd);
			rootJson.put("id", custId);

			String urlParameters = rootJson.toString();

			String res = communicate("PUT", SERVER_URL + "/csc/customers/check", urlParameters, accessKey);
			System.out.println(getPrettyJson(res));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void registMember() {
		
		try {
			String accessKey = Base64.encodeAsString((CLIENT_ID + ":" + CLIENT_SECRET).getBytes());

			JSONObject rootJson = new JSONObject();
			rootJson.put("service", "S0001");
			rootJson.put("id", "hosking");
			rootJson.put("name", "권호성");
			rootJson.put("email", "hskwon@12cm.co.kr");
			rootJson.put("ctn", "01053664784");
			rootJson.put("os", "I");
			rootJson.put("equip", "111111111");
			rootJson.put("pushid", "22222222");
			rootJson.put("pwd", "1112");
			rootJson.put("type", "1");

			String urlParameters = rootJson.toString();

			String res = communicate("POST", SERVER_URL + "/csc/customers", urlParameters, accessKey);
			System.out.println(getPrettyJson(res));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getMember(String svcCd, String custId) {
		
		try {
			String accessKey = Base64.encodeAsString((CLIENT_ID + ":" + CLIENT_SECRET).getBytes());

			JSONObject rootJson = new JSONObject();
			rootJson.put("service", svcCd);
			rootJson.put("id", custId);

			String urlParameters = rootJson.toString();

			String res = communicate("PUT", SERVER_URL + "/csc/customers", urlParameters, accessKey);
			System.out.println(getPrettyJson(res));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String communicate(String method, String url, String urlParameters, String accessKey) throws Exception {

		String lastUrl = url;
		if(StringUtils.equals(method, "GET")) {
			if(StringUtils.isNotEmpty(StringUtils.trimToEmpty(urlParameters))) {
				
				String sendParams = URLEncoder.encode(StringUtils.trimToEmpty(urlParameters), "UTF-8");
				
				lastUrl = lastUrl + "?" + sendParams;
			}
		}

		try {
			System.out.println("Sending '"+method+"' request to URL : " + lastUrl);

			URL obj = new URL(lastUrl);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod(method);
			con.setRequestProperty("Authorization", "Basic " + accessKey);
			con.setRequestProperty("Content-Type",CONTENT_TYPE);
			con.setRequestProperty("Accept",ACCEPT);
			con.setDoOutput(true);

			// Send post request
			if(!StringUtils.equals(method, "GET") && urlParameters != null && !urlParameters.trim().equals("")) {
				System.out.println("POST parameters : " + urlParameters);

				String sendParams = URLEncoder.encode(StringUtils.trimToEmpty(urlParameters), "UTF-8");

				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				wr.writeBytes(sendParams);
				wr.flush();
				wr.close();
			}

			int responseCode = con.getResponseCode();
			System.out.println("Response Code : " + responseCode);
			String responseMsg = con.getResponseMessage();
			System.out.println("Response Msg : " + responseMsg);

			if(responseCode != HttpURLConnection.HTTP_NO_CONTENT) {
				
				BufferedReader in = null;

				if(responseCode != HttpURLConnection.HTTP_OK &&
						responseCode != HttpURLConnection.HTTP_CREATED &&
						responseCode != HttpURLConnection.HTTP_ACCEPTED) {
					in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
				}
				else {
					in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				}

				String inputLine;
				StringBuffer responseBuf = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					responseBuf.append(inputLine);
				}
				in.close();

				return responseBuf.toString();
			}
			else {
				return null;
			}

		} catch (Exception e) {
			throw e;
		}
	}

	public String getPrettyJson(String jsonString) {

		final String INDENT = "    ";
		final String CRLF = "\n";

		StringBuffer prettyJsonSb = new StringBuffer();
		int indentDepth = 0;
		String targetString = null;

		for (int i = 0; i < jsonString.length(); i++) {
			targetString = jsonString.substring(i, i + 1);
			if (targetString.equals("{") || targetString.equals("[")) {
				prettyJsonSb.append(targetString).append(CRLF);
				indentDepth++;
				for (int j = 0; j < indentDepth; j++) {
					prettyJsonSb.append(INDENT);
				}
			}
			else if (targetString.equals("}") || targetString.equals("]")) {
				prettyJsonSb.append(CRLF);
				indentDepth--;
				for (int j = 0; j < indentDepth; j++) {
					prettyJsonSb.append(INDENT);
				}
				prettyJsonSb.append(targetString);
			}
			else if (targetString.equals(",")) {
				prettyJsonSb.append(targetString);
				prettyJsonSb.append(CRLF);
				for (int j = 0; j < indentDepth; j++) {
					prettyJsonSb.append(INDENT);
				}
			}
			else {
				prettyJsonSb.append(targetString);
			}
		}

		return prettyJsonSb.toString();
	}
}
