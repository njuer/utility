package org.minnie.utility.module.lecaiwang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import sun.misc.IOUtils;

/**
 * @author neiplzer@gmail.com
 * @version 2014-3-21 类说明
 */
public class HttpClientTest {

	private static final HttpClient client = new DefaultHttpClient();
	private static final String username = "账号";
	private static final String password = "密码";
	private static final String portal = "要抓取的网页";
	private static final String url = "http://www.17500.cn/ssq/newhot.php";

	public static void main(String[] args) throws IOException {
		login(url);
//		get(url);
	}

	/**
	 * 抓取网页
	 * 
	 * @param url
	 * @throws IOException
	 */
	static void get(String url) throws IOException {
		HttpGet get = new HttpGet(url);
		HttpResponse response = client.execute(get);
		System.out.println(response.getStatusLine());
		HttpEntity entity = response.getEntity();
		dump(entity);
	}

	/**
	 * 执行登录过程
	 * 
	 * @param user
	 * @param pwd
	 * @param debug
	 * @throws IOException
	 */
	static void login(String action) throws IOException {
		HttpPost post = new HttpPost(action);

		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("all", "1"));
		qparams.add(new BasicNameValuePair("bh[]", ""));
		qparams.add(new BasicNameValuePair("bh[]", ""));
		qparams.add(new BasicNameValuePair("bh[]", ""));
		qparams.add(new BasicNameValuePair("hz1", "60"));
		qparams.add(new BasicNameValuePair("hz2", "140"));
		qparams.add(new BasicNameValuePair("jo[]", "2"));
		qparams.add(new BasicNameValuePair("jo[]", "3"));
		qparams.add(new BasicNameValuePair("jo[]", "4"));
		qparams.add(new BasicNameValuePair("mid[]", "01"));
		qparams.add(new BasicNameValuePair("mid[]", "02"));
		qparams.add(new BasicNameValuePair("mid[]", "03"));
		qparams.add(new BasicNameValuePair("mid[]", "04"));
		qparams.add(new BasicNameValuePair("mid[]", "05"));
		qparams.add(new BasicNameValuePair("mid[]", "06"));
		qparams.add(new BasicNameValuePair("mid[]", "07"));
		qparams.add(new BasicNameValuePair("mid[]", "08"));
		qparams.add(new BasicNameValuePair("mid[]", "09"));
		qparams.add(new BasicNameValuePair("mid[]", "10"));
		qparams.add(new BasicNameValuePair("mid[]", "11"));
		qparams.add(new BasicNameValuePair("mid[]", "12"));
		qparams.add(new BasicNameValuePair("mid[]", "13"));
		qparams.add(new BasicNameValuePair("mid[]", "14"));
		qparams.add(new BasicNameValuePair("mid[]", "15"));
		qparams.add(new BasicNameValuePair("mid[]", "16"));
		qparams.add(new BasicNameValuePair("mid[]", "17"));
		qparams.add(new BasicNameValuePair("mid[]", "18"));
		qparams.add(new BasicNameValuePair("mid[]", "19"));
		qparams.add(new BasicNameValuePair("mid[]", "20"));
		qparams.add(new BasicNameValuePair("mid[]", "21"));
		qparams.add(new BasicNameValuePair("mid[]", "22"));
		qparams.add(new BasicNameValuePair("mid[]", "23"));
		qparams.add(new BasicNameValuePair("mid[]", "24"));
		qparams.add(new BasicNameValuePair("mid[]", "25"));
		qparams.add(new BasicNameValuePair("mid[]", "26"));
		qparams.add(new BasicNameValuePair("mid[]", "27"));
		qparams.add(new BasicNameValuePair("mid[]", "28"));
		qparams.add(new BasicNameValuePair("mid[]", "29"));
		qparams.add(new BasicNameValuePair("mid[]", "30"));
		qparams.add(new BasicNameValuePair("mid[]", "31"));
		qparams.add(new BasicNameValuePair("mid[]", "32"));
		qparams.add(new BasicNameValuePair("mid[]", "33"));
		qparams.add(new BasicNameValuePair("pastresult", "12,18,19,23,24,30"));
		qparams.add(new BasicNameValuePair("th[]", "0"));
		qparams.add(new BasicNameValuePair("th[]", "1"));
		qparams.add(new BasicNameValuePair("th[]", "2"));
		qparams.add(new BasicNameValuePair("zhushu", "100"));
		
		UrlEncodedFormEntity params = new UrlEncodedFormEntity(qparams, "gbk"); // 网页编码
		post.setEntity(params);

		// Execute the request
		HttpResponse httpResponse = client.execute(post);
		HttpEntity entity = httpResponse.getEntity();
		System.out.println(EntityUtils.toString(entity));
//		post.abort();
	}

	/**
	 * 打印页面
	 * 
	 * @param entity
	 * @throws IOException
	 */
	private static void dump(HttpEntity entity) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				entity.getContent(), "gbk"));

//		 System.out.println(br.);
	}

}
