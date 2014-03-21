package org.minnie.utility.module.lecaiwang;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.minnie.utility.parser.HttpSimulation;
import org.minnie.utility.parser.JsoupHtmlParser;

/**
 * @author neiplzer@gmail.com
 * @version 2014-3-21
 * 类说明
 */
public class App {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		HttpSimulation hs = new HttpSimulation();
		
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
		
		JsoupHtmlParser.getLCW2(hs.getResponseBodyByPost(action, paramPair, "gbk"), "font[size='4']");
		
//		getLCW2(hs.getResponseBodyByPost(action, paramPair, charset));
		
		
		String url = "http://www.17500.cn/ssq/newhot.php";
		
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("all", "1");
//		params.put("hz1", "60");
//		params.put("hz2", "140");
//		params.put("jo[]", "2");
//		params.put("jo[]", "3");
//		params.put("jo[]", "4");
//		params.put("mid[]", "01");
//		params.put("mid[]", "02");
//		params.put("mid[]", "03");
//		params.put("mid[]", "04");
//		params.put("mid[]", "05");
//		params.put("mid[]", "06");
//		params.put("mid[]", "07");
//		params.put("mid[]", "08");
//		params.put("mid[]", "09");
//		params.put("mid[]", "10");
//		params.put("mid[]", "11");
//		params.put("mid[]", "12");
//		params.put("mid[]", "13");
//		params.put("mid[]", "14");
//		params.put("mid[]", "15");
//		params.put("mid[]", "16");
//		params.put("mid[]", "17");
//		params.put("mid[]", "18");
//		params.put("mid[]", "19");
//		params.put("mid[]", "20");
//		params.put("mid[]", "21");
//		params.put("mid[]", "22");
//		params.put("mid[]", "23");
//		params.put("mid[]", "24");
//		params.put("mid[]", "25");
//		params.put("mid[]", "26");
//		params.put("mid[]", "27");
//		params.put("mid[]", "28");
//		params.put("mid[]", "29");
//		params.put("mid[]", "30");
//		params.put("mid[]", "31");
//		params.put("mid[]", "32");
//		params.put("mid[]", "33");
//		params.put("th[]", "0");
//		params.put("th[]", "1");
//		params.put("th[]", "2");
//		params.put("zhushu", "1000");
//
//		
//		JsoupHtmlParser.getLCW(url, params);
		
	}

}
