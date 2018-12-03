/********************************************************* 
  @프로그램명   : HttpUtil.java
  @TRIN ID      : 
  @프로그램목적 : HTTP 통신 처리
  @적용일시     : 2014. 05. 26
  @히스토리관리 :
      수정일         변경자       내용
   ------------------------------------------------------
      2014-05-26     hskwon       최초생성
**********************************************************/
package com.echoss.svc.common.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class HttpUtil {

	private static final Log logger = LogFactory.getLog(HttpUtil.class);
	
	/**
	 * HTTP 통신처리
	 * @param method
	 * @param url
	 * @param characterSet
	 * @param requestData
	 * @return
	 * @throws Exception
	 */
	public static String handle(String method, String url, String characterSet, String requestData) throws Exception {
		return handle(method, url, characterSet, requestData, null);
	}

	/**
	 * HTTP 통신처리
	 * @param method
	 * @param url
	 * @param characterSet
	 * @param requestData
	 * @param properties
	 * @return
	 * @throws Exception
	 */
	public static String handle(String method, String url, String characterSet, String requestData, Map<String, String> properties) throws Exception {

		String resultStr = "";

		try {
			if(StringUtils.isEmpty(url)) {
				logger.error("URL 오류입니다. ["+url+"]");
				throw new Exception("잘못된 URL 입니다.");
			}

			resultStr = sendHttp(method, url, characterSet, requestData, properties);
			resultStr = StringUtils.trimToEmpty(resultStr);

			if(StringUtils.isEmpty(resultStr)) {
				logger.error("응답 데이터를 수신하지 못하였습니다.");
				return null;
			}
		}
		catch (Exception e) {
			logger.error("메시지를 처리하던 중 에러가 발생하였습니다.", e);
			throw e;
		}

		return resultStr;
	}

	/**
	 * HTTP 통신 처리
	 * @param method 방식
	 * @param url 서버URL
	 * @param characterSet
	 * @param params 파라미터
	 * @param properties Request Header
	 * @return
	 * @throws Exception
	 */
	private static String sendHttp(String method, String url, String characterSet, String params, Map<String, String> properties) throws Exception {

		String lastUrl = url;
		if(StringUtils.equals(method, "GET")) {
			if(StringUtils.isNotEmpty(StringUtils.trimToEmpty(params))) {
				
				String sendParams = URLEncoder.encode(StringUtils.trimToEmpty(params), characterSet);

				lastUrl = lastUrl + "?" + sendParams;
			}
		}

		BufferedReader in = null;
		try {
			URL obj = new URL(lastUrl);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod(method);

			if(properties != null) {
				Iterator<String> iter2 = properties.keySet().iterator();
				while(iter2.hasNext()){
					String key = iter2.next();
					String value = properties.get(key);

					con.setRequestProperty(key, value);
				}
			}

			// Send request
			if(!StringUtils.equals(method, "GET") && params != null && !params.trim().equals("")) {
				System.out.println("parameters : " + params);

				String sendParams = URLEncoder.encode(StringUtils.trimToEmpty(params), characterSet);

				con.setDoOutput(true);
				DataOutputStream wr = null;
				try {
					wr = new DataOutputStream(con.getOutputStream());
					wr.writeBytes(sendParams);
					wr.flush();
				} finally {
					wr.close();
				}
			}

			int responseCode = con.getResponseCode();

			if(responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_ACCEPTED) {
				in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			}
			else {
				logger.error("Response error : " + responseCode + "-" + con.getResponseMessage());
				in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}

			StringBuffer sb = new StringBuffer();
			String inputLine = null;

			while ((inputLine = in.readLine()) != null) {
				sb.append(inputLine);
			}

			if(logger.isDebugEnabled()) {
				logger.debug("response message : [" + sb.toString() + "]");
			}

			return sb.toString();
		} catch(Exception e) {
			logger.error("getPaserHtml " + "=>" + e.getMessage(), e);
        	throw e;
		} finally {
			if(in != null) in.close();
		}
	}

	public static void makeHttpParameter(StringBuffer sb, String key, String value, String characterSet) {

		if(sb.length() > 0) sb.append("&");

		String encodedValue = "";
		try {
			encodedValue = URLEncoder.encode(value, characterSet);
		} catch (UnsupportedEncodingException e) {
		}

		sb.append(key).append("=").append(encodedValue);
	}
	
	public static void convertMapToHttpParameter(StringBuffer sb, Map<String, String> params, String characterSet) {
		for(String key : params.keySet()) {
			String value = params.get(key);
			HttpUtil.makeHttpParameter(sb, key, value, characterSet);
		}
	}
	
	
	/*
	 * // 把从输入流InputStream按指定编码格式encode变成字符串String
	*/
	public static String changeInputStream(InputStream inputStream, String encode) {

		// ByteArrayOutputStream 一般叫做内存流
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] data = new byte[1024];
		int len = 0;
		String result = "";
		if (inputStream != null) {
			try {
				while ((len = inputStream.read(data)) != -1) {
					byteArrayOutputStream.write(data, 0, len);

				}
				result = new String(byteArrayOutputStream.toByteArray(), encode);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return result;
	}
	
	//用apache接口实现http的post提交数据
	public static String sendHttpClientPost(String path, Map<String, String> params, String encode) {

		List<NameValuePair> list = new ArrayList<NameValuePair>();
		if (params != null && !params.isEmpty()) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
					list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}

		try {
			// 实现将请求的参数封装到表单中，即请求体中
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, encode);
			// 使用post方式提交数据
			HttpPost httpPost = new HttpPost(path);
			httpPost.setEntity(entity);
			// 执行post请求，并获取服务器端的响应HttpResponse
			DefaultHttpClient client = new DefaultHttpClient();
			HttpResponse httpResponse = client.execute(httpPost);
      
			//获取服务器端返回的状态码和输入流，将输入流转换成字符串
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				InputStream inputStream = httpResponse.getEntity().getContent();
				return changeInputStream(inputStream, encode);
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";

	}	
}
