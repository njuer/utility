package org.minnie.utility.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * @author neiplzer@gmail.com
 * @version 2014-3-21
 * 类说明
 */
public class HttpSimulation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String action = "http://www.17500.cn/ssq/newhot.php";
		
		List<NameValuePair> paramPair = new ArrayList<NameValuePair>();
		paramPair.add(new BasicNameValuePair("all", "1"));
		paramPair.add(new BasicNameValuePair("bh[]", ""));
		paramPair.add(new BasicNameValuePair("bh[]", ""));
		paramPair.add(new BasicNameValuePair("bh[]", ""));
		paramPair.add(new BasicNameValuePair("hz1", "60"));
		paramPair.add(new BasicNameValuePair("hz2", "140"));
		paramPair.add(new BasicNameValuePair("jo[]", "2"));
		paramPair.add(new BasicNameValuePair("jo[]", "3"));
		paramPair.add(new BasicNameValuePair("jo[]", "4"));
		paramPair.add(new BasicNameValuePair("mid[]", "01"));
		paramPair.add(new BasicNameValuePair("mid[]", "02"));
		paramPair.add(new BasicNameValuePair("mid[]", "03"));
		paramPair.add(new BasicNameValuePair("mid[]", "04"));
		paramPair.add(new BasicNameValuePair("mid[]", "05"));
		paramPair.add(new BasicNameValuePair("mid[]", "06"));
		paramPair.add(new BasicNameValuePair("mid[]", "07"));
		paramPair.add(new BasicNameValuePair("mid[]", "08"));
		paramPair.add(new BasicNameValuePair("mid[]", "09"));
		paramPair.add(new BasicNameValuePair("mid[]", "10"));
		paramPair.add(new BasicNameValuePair("mid[]", "11"));
		paramPair.add(new BasicNameValuePair("mid[]", "12"));
		paramPair.add(new BasicNameValuePair("mid[]", "13"));
		paramPair.add(new BasicNameValuePair("mid[]", "14"));
		paramPair.add(new BasicNameValuePair("mid[]", "15"));
		paramPair.add(new BasicNameValuePair("mid[]", "16"));
		paramPair.add(new BasicNameValuePair("mid[]", "17"));
		paramPair.add(new BasicNameValuePair("mid[]", "18"));
		paramPair.add(new BasicNameValuePair("mid[]", "19"));
		paramPair.add(new BasicNameValuePair("mid[]", "20"));
		paramPair.add(new BasicNameValuePair("mid[]", "21"));
		paramPair.add(new BasicNameValuePair("mid[]", "22"));
		paramPair.add(new BasicNameValuePair("mid[]", "23"));
		paramPair.add(new BasicNameValuePair("mid[]", "24"));
		paramPair.add(new BasicNameValuePair("mid[]", "25"));
		paramPair.add(new BasicNameValuePair("mid[]", "26"));
		paramPair.add(new BasicNameValuePair("mid[]", "27"));
		paramPair.add(new BasicNameValuePair("mid[]", "28"));
		paramPair.add(new BasicNameValuePair("mid[]", "29"));
		paramPair.add(new BasicNameValuePair("mid[]", "30"));
		paramPair.add(new BasicNameValuePair("mid[]", "31"));
		paramPair.add(new BasicNameValuePair("mid[]", "32"));
		paramPair.add(new BasicNameValuePair("mid[]", "33"));
		paramPair.add(new BasicNameValuePair("pastresult", "12,18,19,23,24,30"));
		paramPair.add(new BasicNameValuePair("th[]", "0"));
		paramPair.add(new BasicNameValuePair("th[]", "1"));
		paramPair.add(new BasicNameValuePair("th[]", "2"));
		paramPair.add(new BasicNameValuePair("zhushu", "100"));
		
		System.out.println(getResponseBodyByPost(action, paramPair, "gbk"));
		
//		CloseableHttpClient httpclient = HttpClients.createDefault();
//		try {
////			HttpGet httpget = new HttpGet("http://hc.apache.org/httpcomponents-client-4.3.x/examples.html");
//			HttpPost post = new HttpPost("http://www.17500.cn/ssq/newhot.php");
//			
//			List<NameValuePair> paramPair = new ArrayList<NameValuePair>();
//			paramPair.add(new BasicNameValuePair("all", "1"));
//			paramPair.add(new BasicNameValuePair("bh[]", ""));
//			paramPair.add(new BasicNameValuePair("bh[]", ""));
//			paramPair.add(new BasicNameValuePair("bh[]", ""));
//			paramPair.add(new BasicNameValuePair("hz1", "60"));
//			paramPair.add(new BasicNameValuePair("hz2", "140"));
//			paramPair.add(new BasicNameValuePair("jo[]", "2"));
//			paramPair.add(new BasicNameValuePair("jo[]", "3"));
//			paramPair.add(new BasicNameValuePair("jo[]", "4"));
//			paramPair.add(new BasicNameValuePair("mid[]", "01"));
//			paramPair.add(new BasicNameValuePair("mid[]", "02"));
//			paramPair.add(new BasicNameValuePair("mid[]", "03"));
//			paramPair.add(new BasicNameValuePair("mid[]", "04"));
//			paramPair.add(new BasicNameValuePair("mid[]", "05"));
//			paramPair.add(new BasicNameValuePair("mid[]", "06"));
//			paramPair.add(new BasicNameValuePair("mid[]", "07"));
//			paramPair.add(new BasicNameValuePair("mid[]", "08"));
//			paramPair.add(new BasicNameValuePair("mid[]", "09"));
//			paramPair.add(new BasicNameValuePair("mid[]", "10"));
//			paramPair.add(new BasicNameValuePair("mid[]", "11"));
//			paramPair.add(new BasicNameValuePair("mid[]", "12"));
//			paramPair.add(new BasicNameValuePair("mid[]", "13"));
//			paramPair.add(new BasicNameValuePair("mid[]", "14"));
//			paramPair.add(new BasicNameValuePair("mid[]", "15"));
//			paramPair.add(new BasicNameValuePair("mid[]", "16"));
//			paramPair.add(new BasicNameValuePair("mid[]", "17"));
//			paramPair.add(new BasicNameValuePair("mid[]", "18"));
//			paramPair.add(new BasicNameValuePair("mid[]", "19"));
//			paramPair.add(new BasicNameValuePair("mid[]", "20"));
//			paramPair.add(new BasicNameValuePair("mid[]", "21"));
//			paramPair.add(new BasicNameValuePair("mid[]", "22"));
//			paramPair.add(new BasicNameValuePair("mid[]", "23"));
//			paramPair.add(new BasicNameValuePair("mid[]", "24"));
//			paramPair.add(new BasicNameValuePair("mid[]", "25"));
//			paramPair.add(new BasicNameValuePair("mid[]", "26"));
//			paramPair.add(new BasicNameValuePair("mid[]", "27"));
//			paramPair.add(new BasicNameValuePair("mid[]", "28"));
//			paramPair.add(new BasicNameValuePair("mid[]", "29"));
//			paramPair.add(new BasicNameValuePair("mid[]", "30"));
//			paramPair.add(new BasicNameValuePair("mid[]", "31"));
//			paramPair.add(new BasicNameValuePair("mid[]", "32"));
//			paramPair.add(new BasicNameValuePair("mid[]", "33"));
//			paramPair.add(new BasicNameValuePair("pastresult", "12,18,19,23,24,30"));
//			paramPair.add(new BasicNameValuePair("th[]", "0"));
//			paramPair.add(new BasicNameValuePair("th[]", "1"));
//			paramPair.add(new BasicNameValuePair("th[]", "2"));
//			paramPair.add(new BasicNameValuePair("zhushu", "100"));
//			
//			System.out.println("Executing request " + post.getRequestLine());
//			
//			UrlEncodedFormEntity params = new UrlEncodedFormEntity(paramPair, "gbk"); // 网页编码
//			post.setEntity(params);
//
//			
//
//			// Create a custom response handler
//			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
//
//				public String handleResponse(final HttpResponse response)
//						throws ClientProtocolException, IOException {
//					int status = response.getStatusLine().getStatusCode();
//					if (status >= 200 && status < 300) {
//						HttpEntity entity = response.getEntity();
//						return entity != null ? EntityUtils.toString(entity)
//								: null;
//					} else {
//						throw new ClientProtocolException(
//								"Unexpected response status: " + status);
//					}
//				}
//
//			};
//			
//			//
////			// Execute the request
//			String responseBody = httpclient.execute(post, responseHandler);
////			HttpEntity entity = httpResponse.getEntity();
//			
////			String responseBody = httpclient.execute(post, responseHandler);
//			System.out.println("----------------------------------------");
//			System.out.println(responseBody);
//		} catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			try {
//				httpclient.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	}
	
	public static String getResponseBodyByPost(String action, List<NameValuePair> paramPair, String charset){
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String responseBody = null;
		
		try {
			HttpPost post = new HttpPost(action);
			
//			System.out.println("Executing request " + post.getRequestLine());
			
			UrlEncodedFormEntity params = new UrlEncodedFormEntity(paramPair, charset);
			post.setEntity(params);
			
			// Create a custom response handler
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

				public String handleResponse(final HttpResponse response)
						throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity)
								: null;
					} else {
						throw new ClientProtocolException(
								"Unexpected response status: " + status);
					}
				}

			};
			
			// Execute the request And Get Response Body
			responseBody = httpclient.execute(post, responseHandler);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responseBody;
	}

}
