package org.minnie.utility.parser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.minnie.utility.module.netease.NeteasePage;
import org.minnie.utility.module.netease.Picture;
import org.minnie.utility.module.yangshengsuo.Regimen;
import org.minnie.utility.util.StringUtil;

/**
 * @author neiplzer@gmail.com
 * @version 2014-3-10 基于Jsoup的Html解析工具类
 */
public class JsoupHtmlParser {

	private static Logger logger = Logger.getLogger(JsoupHtmlParser.class
			.getName());

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// Document doc =
			// Jsoup.connect("http://39yss.com/yinshi/yingyang/2014030725664.html").get();
			Document doc = Jsoup.connect(
					"http://39yss.com/mysn/yingyouerqi/20140226/25276.html")
					.get();
			System.out.println(doc.title());

			Elements pageBody = doc.select("div.c100Pages");
			// System.out.println(doc.select("span.cptotal").html());

			if (null != pageBody) {
				Element firstPageBody = pageBody.first();
				if (!StringUtils.isBlank(firstPageBody.html())) {
					// System.out.println(firstPageBody.select("span.cptotal").html().replace("共",
					// "").replace("页", ""));
					int total = Integer.valueOf(
							firstPageBody.select("span.cptotal").html()
									.replace("共", "").replace("页", ""))
							.intValue();
					System.out.println(total);
					if (null != firstPageBody) {
						Elements linkTag = firstPageBody.select("a");
						for (Element e : linkTag) {
							if (e.hasAttr("class")) {
								System.out.println(e + "---跳过---");
							} else {
								System.out.println(e.attr("href"));
							}
						}
					}
				}

			}

			// Elements arcBody = doc.select("div.arcBody");
			// if(null != arcBody){
			// // System.out.println(arcBody.first());
			// Element firstArcBody = arcBody.first();
			//
			// if(null != firstArcBody){
			// Elements imgList = firstArcBody.select("img");
			// if(null != imgList){
			// for(Element e:imgList){
			// String imgSrc = e.attr("src");
			// }
			// }
			// // System.out.println(imgList.first().attr("src"));
			// }
			// }
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static NeteasePage getNeteasePage(String galleryUrl) {

		return getNeteasePage(galleryUrl, "textarea[name=gallery-data]");

	}

	/**
	 * 获取网易图集所有图片的地址
	 * 
	 * @param galleryUrl
	 *            图集地址
	 * @param cssQuery
	 *            CSS selector
	 * @return
	 */
	public static NeteasePage getNeteasePage(String galleryUrl, String cssQuery) {

		Document doc;
		try {
			doc = Jsoup.connect(galleryUrl).get();
			Elements eles = doc.select(cssQuery);
			String json = eles.text();

			/**
			 * 如果要转换到的Bean类中含有类似ArrayList、Map、List这样的集合时，
			 * 要对集合进行处理，否则会报“net.sf.ezmorph.bean.MorphDynaBean cannot be cast to
			 * XXX”这样的异常。
			 */
			Map<String, Class> listParamMap = new HashMap<String, Class>();
			listParamMap.put("list", Picture.class); // 我这里的成员名为content
														// 对应的类为ClientInfo.class，具体值要跟据你自己的来定。

			JSONObject jObj = new JSONObject().fromObject(json);
			return (NeteasePage) JSONObject.toBean(jObj, NeteasePage.class,
					listParamMap);

		} catch (IOException e) {
			logger.error("IOException[JsoupHtmlParser->getNeteasePage(String galleryUrl, String cssQuery)]: "
					+ e.getMessage());
		}

		return null;
	}

	public static void extractPagesBy39YangShengsuo(String pageUrl,
			LinkedBlockingQueue<Regimen> pageUrlQueue) {

		int total = 1;
		try {
			Document doc = Jsoup.connect(pageUrl).get();
			String title = doc.title();
			/**
			 * 处理非法字符
			 */
			int index = title.indexOf("_");
			if (index > 0) {
				title = title.substring(0, index);
			}
			title = StringUtil.StringFilter(title);

			Elements pageBody = doc.select("div.c100Pages");

			if (null != pageBody) {
				Element firstPageBody = pageBody.first();
				if (!StringUtils.isBlank(firstPageBody.html())) {
					total = Integer.valueOf(
							firstPageBody.select("span.cptotal").html()
									.replace("共", "").replace("页", ""))
							.intValue();
					// System.out.println(total);
					if (null != firstPageBody) {
						Elements linkTag = firstPageBody.select("a");
						for (Element e : linkTag) {
							if (!e.hasAttr("class")) {
								Regimen regimen = new Regimen();
								regimen.setTitle(title);
								regimen.setPageUrl(pageUrl.substring(0,
										pageUrl.lastIndexOf("/") + 1)
										+ e.attr("href"));
								regimen.setTotal(total);
								// logger.info(regimen.toString());
								pageUrlQueue.put(regimen);
							}
						}
					}
				}
			}

			Regimen regimen = new Regimen();
			regimen.setTitle(title);
			regimen.setPageUrl(pageUrl);
			regimen.setTotal(total);
			pageUrlQueue.put(regimen);
		} catch (IOException e) {
			logger.error("IOException[JsoupHtmlParser->extractPagesBy39YangShengsuo(String pageUrl,LinkedBlockingQueue<Regimen> pageUrlQueue)]: "
					+ e.getMessage());
		} catch (InterruptedException e) {
			logger.error("InterruptedException[JsoupHtmlParser->extractPagesBy39YangShengsuo(String pageUrl,LinkedBlockingQueue<Regimen> pageUrlQueue)]: "
					+ e.getMessage());
		}
	}

	public static String getRegimenPictureUrl(Regimen regimen) {

		String pageUrl = regimen.getPageUrl();

		if (null == pageUrl || StringUtils.isBlank(pageUrl)) {
			return null;
		}

		try {
			Document doc = Jsoup.connect(pageUrl).get();

			Elements arcBody = doc.select("div.arcBody");
			if (null != arcBody) {
				Element firstArcBody = arcBody.first();
				if (null != firstArcBody) {
					Elements imgList = firstArcBody.select("img");
					if (null != imgList) {
						for (Element e : imgList) {
							return e.attr("src");
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}

}
