package org.minnie.utility.module.lecaiwang;

/**
 * @author neiplzer@gmail.com
 * @version 2014-3-21
 * 类说明
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class TestPost {
	public static void testPost() throws IOException {
		/**
		 * 首先要和URL下的URLConnection对话。 URLConnection可以很容易的从URL得到。比如： // Using
		 * java.net.URL and //java.net.URLConnection
		 * 
		 * 使用页面发送请求的正常流程：在页面http://www.faircanton.com/message/loginlytebox.
		 * asp中输入用户名和密码，然后按登录，
		 * 跳转到页面http://www.faircanton.com/message/check.asp进行验证 验证的的结果返回到另一个页面
		 * 
		 * 使用java程序发送请求的流程：使用URLConnection向http://www.faircanton.com/message/
		 * check.asp发送请求 并传递两个参数：用户名和密码 然后用程序获取验证结果
		 */
		URL url = new URL("http://www.17500.cn/ssq/newhot.php");
		URLConnection connection = url.openConnection();
		/**
		 * 然后把连接设为输出模式。URLConnection通常作为输入来使用，比如下载一个Web页。
		 * 通过把URLConnection设为输出，你可以把数据向你个Web页传送。下面是如何做：
		 */
		connection.setDoOutput(true);
		/**
		 * 最后，为了得到OutputStream，简单起见，把它约束在Writer并且放入POST信息中，例如： ...
		 */
		OutputStreamWriter out = new OutputStreamWriter(
				connection.getOutputStream(), "UTF-8");
//		out.write("username=kevin&password=*********"); // 向页面传递数据。post的关键所在！
		StringBuilder sb = new StringBuilder();
		sb.append("all=1");
		sb.append("&bh[]=");
		sb.append("&bh[]=");
		sb.append("&hz1=60");
		sb.append("&hz2=140");
		sb.append("&jo[]=2");
		sb.append("&jo[]=3");
		sb.append("&jo[]=4");
		sb.append("&mid[]=01");
		sb.append("&mid[]=02");
		sb.append("&mid[]=03");
		sb.append("&mid[]=04");
		sb.append("&mid[]=05");
		sb.append("&mid[]=06");
		sb.append("&mid[]=07");
		sb.append("&mid[]=08");
		sb.append("&mid[]=09");
		sb.append("&mid[]=10");
		sb.append("&mid[]=11");
		sb.append("&mid[]=12");
		sb.append("&mid[]=13");
		sb.append("&mid[]=14");
		sb.append("&mid[]=15");
		sb.append("&mid[]=16");
		sb.append("&mid[]=17");
		sb.append("&mid[]=18");
		sb.append("&mid[]=19");
		sb.append("&mid[]=20");
		sb.append("&mid[]=21");
		sb.append("&mid[]=22");
		sb.append("&mid[]=23");
		sb.append("&mid[]=24");
		sb.append("&mid[]=25");
		sb.append("&mid[]=26");
		sb.append("&mid[]=27");
		sb.append("&mid[]=28");
		sb.append("&mid[]=29");
		sb.append("&mid[]=30");
		sb.append("&mid[]=31");
		sb.append("&mid[]=32");
		sb.append("&mid[]=33");
		sb.append("&pastresult=12,18,19,23,24,30");
		sb.append("&th[]=0");
		sb.append("&th[]=1");
		sb.append("&th[]=2");
		sb.append("&zhushu=1000");
		
		out.write(sb.toString()); 
		
		// remember to clean up
		out.flush();
		out.close();
		/**
		 * 这样就可以发送一个看起来象这样的POST： POST /jobsearch/jobsearch.cgi HTTP 1.0 ACCEPT:
		 * text/plain Content-type: application/x-www-form-urlencoded
		 * Content-length: 99 username=bob password=someword
		 */
		// 一旦发送成功，用以下方法就可以得到服务器的回应：
		String sCurrentLine;
		String sTotalString;
		sCurrentLine = "";
		sTotalString = "";
		InputStream l_urlStream;
		l_urlStream = connection.getInputStream();
		// 传说中的三层包装阿！
		BufferedReader l_reader = new BufferedReader(new InputStreamReader(
				l_urlStream));
		while ((sCurrentLine = l_reader.readLine()) != null) {
			sTotalString += sCurrentLine;

		}
		System.out.println(sTotalString);
	}

	public static void main(String[] args) throws IOException {
		testPost();
	}
}