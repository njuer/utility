package org.minnie.utility.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author neiplzer@gmail.com
 * @version 2014-3-11 类说明
 */
public class StringUtil {

	public static Pattern urlPattern = Pattern.compile(Constant.REG_URL);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static String getUrl(String imageSource) {
		Matcher matcher = urlPattern.matcher(imageSource);
		if (matcher.find()) {
			return matcher.group();
		}
		return null;
	}

	public static String StringFilter(String str) {
		Pattern p = Pattern.compile(Constant.REG_ILLEGAL_CHARACTERS);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

}
