package org.minnie.utility.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author neiplzer@gmail.com
 * @version 2014-3-11 类说明
 */
public class StringUtil {

	public static Pattern urlPattern = Pattern.compile(Constant.REG_URL);
	public static Pattern domainPattern = Pattern.compile(Constant.REG_DOMAIN,
			Pattern.CASE_INSENSITIVE);
	public static Pattern periodPattern = Pattern.compile(Constant.REG_GD_FIVE_IN_ELEVEN,
			Pattern.CASE_INSENSITIVE);
	public static Pattern ballPattern = Pattern.compile(Constant.REG_BALL,
			Pattern.CASE_INSENSITIVE);
	public static Pattern neteaseBbsDatetimePattern = Pattern.compile(Constant.REG_NETEASE_BBS_DATETIME,
			Pattern.CASE_INSENSITIVE);
	

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

	public static String getDomainName(String url) {
		Matcher matcher = domainPattern.matcher(url);
		if (matcher.find()) {
			return matcher.group();
		}
		return null;
	}

	public static String correctRate(int correct, int total) {
//    	//注释掉的也是一种方法
//        NumberFormat nf = NumberFormat.getPercentInstance(); 
//        nf.setMinimumFractionDigits( 2 ); 保留到小数点后几位
    	
    	// 百分比格式，后面不足2位的用0补齐
        DecimalFormat df = new DecimalFormat(Constant.PATTERN_PERCENTAGE); // ##.00%
                                                            
        return df.format((correct * 1.0)/(total * 1.0));
    }
	
	/**
	 * int转换为两位String，不足(左侧)补零
	 * @param value
	 * @return
	 */
	public static String getTwoBitValue(int value){
		return String.format(Constant.FORMAT_BALL_VALUE, value);
	}
	
	/**
	 * 判断是否为合法的11选5期号
	 * @param period
	 * @return
	 */
	public static boolean isLegalPeriod(String period) {
		Matcher matcher = periodPattern.matcher(period);
		if (matcher.find()) {
			return true;
		}
		return false;
	}
	
	/**
	 * 获取广东11选5红球清单
	 * @param balls
	 * @return
	 */
	public static List<String> getBalls(String balls) {
		List<String> list = new ArrayList<String>(5);
		Matcher matcher = ballPattern.matcher(balls);
		while (matcher.find()) {
			list.add(matcher.group());
		}
		return list;
	}
	

	public static String getNumber(String str) {
		Pattern p = Pattern.compile(Constant.REG_NUMBER);
		Matcher matcher = p.matcher(str);
//		return m.replaceAll("").trim();
//		Matcher matcher = ballPattern.matcher(balls);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
//			list.add(matcher.group());
			sb.append(matcher.group());
		}
		return sb.toString();
	}
	
    /**
     * @param 待验证的字符串
     * @return 如果是符合网址格式的字符串,返回<b>true</b>,否则为<b>false</b>
     */
    public static boolean isEmail( String str ){
        Pattern pattern = Pattern.compile(Constant.REG_MAIL);
        Matcher  matcher = pattern.matcher( str );
        return matcher.matches();
    }
    
	public static String getNeteaseBbsDateTime(String src) {
		Matcher matcher = neteaseBbsDatetimePattern.matcher(src);
		if (matcher.find()) {
			return matcher.group();
		}
		return null;
	}
	
}