package org.minnie.utility.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.HeadingTag;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.Span;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.minnie.utility.module.hoopchina.HoopChina;

/**
 * @author neiplzer@gmail.com
 * @version 2014-3-6 基于htmlparser的工具类
 */
public class HtmlUtil {

	private static Logger logger = Logger.getLogger(HtmlUtil.class.getName());

	public static final Pattern patternTotalCountString = Pattern.compile(Constant.REG_HOOPCHINA_COUNT_STRING);
	public static final Pattern patternTotalCount = Pattern.compile(Constant.REG_NUMBER);
	
	
	public static HoopChina getHoopChina(String urlAddress) {
		return getHoopChina(urlAddress, "class", "sedtop");
	}
	
	public static HoopChina getHoopChina(String urlAddress, String attribute,String value) {

		HoopChina hoopChina = new HoopChina();

		Parser parser;
		try {
			parser = new Parser(
					(HttpURLConnection) (new URL(urlAddress)).openConnection());
			// 找到属性为attribute，值为value的节点(Node)列表
			NodeFilter filter = new HasAttributeFilter(attribute, value);
			NodeList nodes = parser.extractAllNodesThatMatch(filter);

			if (nodes != null && nodes.size() > 0) {
				Node root = (Node) nodes.elementAt(0);
				NodeList nodeList = root.getChildren();
				if (null != nodeList) {
					int size = nodeList.size();
					for (int i = 0; i < size; i++) {
						Node childNode = nodeList.elementAt(i);
						// 去空
						if (childNode.getText().trim().equals("")) {
							continue;
						}

						if (childNode instanceof HeadingTag) {
							hoopChina.setTitle(childNode.getFirstChild().getText());
						} else if (childNode instanceof Span) {
							NodeList spanNodeList = childNode.getChildren();
							if (null != spanNodeList) {
								int spanSize = spanNodeList.size();
								for (int j = 0; j < spanSize; j++) {
									Node spanChildNode = spanNodeList.elementAt(j);
									// 去空
									if (spanChildNode.getText().trim().equals("")) {
										continue;
									}
									if (!(spanChildNode instanceof LinkTag)) {
										String text = spanChildNode.getText();
										Matcher matcher = patternTotalCountString.matcher(text);
										if (matcher.find()) {
											text = matcher.group();
											matcher = patternTotalCount.matcher(text);
											if (matcher.find()) {
												hoopChina.setTotal(Integer.valueOf(matcher.group()).intValue());
											}
										} 
									}
								}
							}
						}
					}
				}
			}
		} catch (ParserException e) {
			logger.error("ParserException[HtmlUtil->getHoopChina]: "+ e.getMessage());
		} catch (MalformedURLException e) {
			logger.error("MalformedURLException[HtmlUtil->getHoopChina]: "+ e.getMessage());
		} catch (IOException e) {
			logger.error("IOException[HtmlUtil->getHoopChina]: "+ e.getMessage());
		}

		return hoopChina;
	}
	
	
	public static String getHoopChinaPictureUrl(String urlAddress) {
		return getPictureUrl(urlAddress, "id", "bigpicpic");
	}
	
	/**
	 * 获取图片地址
	 * @param urlAddress	网址
	 * @param attribute		html标签的属性名
	 * @param value			html标签的attribute属性的值
	 * @return
	 */
	public static String getPictureUrl(String urlAddress, String attribute,
			String value) {
		Parser parser;
		try {
			parser = new Parser(
					(HttpURLConnection) (new URL(urlAddress)).openConnection());
			// 找到属性为attribute，值为value的节点(Node)列表
			NodeFilter filter = new HasAttributeFilter(attribute, value);
			NodeList nodes = parser.extractAllNodesThatMatch(filter);

			if (nodes != null && nodes.size() > 0) {
				Node root = (Node) nodes.elementAt(0);
				if (root instanceof ImageTag) {
					ImageTag imgTag = (ImageTag) root;
					return imgTag.getImageURL();
				}
			}
		} catch (ParserException e) {
			logger.error("ParserException[HtmlUtil->getPictureUrl]: "+ e.getMessage());
		} catch (MalformedURLException e) {
			logger.error("MalformedURLException[HtmlUtil->getPictureUrl]: "+ e.getMessage());
		} catch (IOException e) {
			logger.error("IOException[HtmlUtil->getPictureUrl]: "+ e.getMessage());
		}
		
		return null;
	}
	
	public static int getTotal39YangShengsuo(String pageUrl){

		int total = -1;

		Parser parser;
		try {
			parser = new Parser((HttpURLConnection) (new URL(pageUrl)).openConnection());
			parser.setEncoding(parser.getEncoding().equals("ISO-8859-1")?"gb2312":parser.getEncoding());   
			// 找到class="blue"的em
			NodeFilter filter = new HasAttributeFilter("class", "cptotal");
			NodeList nodes = parser.extractAllNodesThatMatch(filter);

			if (nodes != null && nodes.size() > 0) {
				Node textnode = (Node) nodes.elementAt(0);
				System.out.println(textnode.getFirstChild().getText());
				String totalString = textnode.getNextSibling().getText();
//				totalString.replace("共", "").replace("页", "");
				total = Integer.valueOf(totalString.replace("共", "").replace("页", "")).intValue();
			}
		} catch (ParserException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return total;
		
	}

}