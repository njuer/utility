package org.minnie.utility.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.minnie.utility.module.sohu.DoubleColor;
import org.minnie.utility.module.yangshengsuo.Regimen;
import org.minnie.utility.util.Constant;
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
			logger.error("IOException[JsoupHtmlParser->getRegimenPictureUrl(Regimen regimen)]: "
					+ e.getMessage());
		}

		return null;

	}

	public static List<DoubleColor> getSohuSSQ(String action,
			Map<String, String> params, Integer year) {
		return getSohuSSQ(action, params, year, "#chartTable");
	}

	/**
	 * 
	 * @param url			action地址
	 * @param params		参数Map，用此法提交有时候参数带不过去，难道是Jsoup的BUG？
	 * @param year			年份
	 * @param cssQuery		
	 * @return
	 */
	public static List<DoubleColor> getSohuSSQ(String action,
			Map<String, String> params, Integer year, String cssQuery) {

		List<DoubleColor> list = new ArrayList<DoubleColor>();

		try {
			Document doc = Jsoup
					.connect(action)
					.data(params)
					.userAgent(Constant.USER_AGENT)
					.timeout(30000) // 设置连接超时时间
					.post(); // 使用POST方法访问URL

			// logger.info(doc.html());
			Elements chartTable = doc.select(cssQuery);

			// startPhase
			Elements startPhase = doc.select("#startPhase");
			Elements endPhase = doc.select("#endPhase");

			if (startPhase.val().equals(params.get("startPhase"))
					&& endPhase.val().equals(params.get("endPhase"))) {

				if (null != chartTable) {
					Element tbody = chartTable.select("tbody").first();
					if (null != tbody) {
						Elements trs = tbody.select("tr");
						for (Element tr : trs) {
							if (!tr.hasAttr("class")) {
								DoubleColor ssq = new DoubleColor();
								Element phase = tr.select("td.chart_table_td")
										.first();
								// logger.info("phase = " + phase.html());
								ssq.setPhase(Integer.valueOf(phase.html()));
								Elements tds = tr.select("td.red_ball");
								List<String> red = new ArrayList<String>(6);
								for (Element td : tds) {
									red.add(td.html());
								}
								ssq.setRed(red);
								Element blue = tr.select("td.blue_ball").first();
								ssq.setBlue(blue.html());
								ssq.setYear(year);
								
								list.add(ssq);
							}
						}
					}
				}
			} else {
//				logger.info("startPhase = " + startPhase.val());
//				logger.info("endPhase = " + endPhase.val());
				logger.info(params.get("startPhase") + "期 -> " + params.get("endPhase") + "期 下载失败！");
			}
		} catch (IOException e) {
			logger.error("IOException[JsoupHtmlParser->getSohuSSQ(String action, Map<String, String> params, String cssQuery)]: "
					+ e.getMessage());
		}
		return list;
	}

	public static List<DoubleColor> getSohuSSQ(String url, Integer year) {
		return getSohuSSQ(url, year, "#chartTable");
	}

	public static List<DoubleColor> getSohuSSQ(Integer startPhase, Integer endPhase, Integer year) {
		return  getSohuSSQ(Constant.URL_SOHU_LOTTERY_DOUBLE_COLOR, startPhase, endPhase, "up", "number", true, year);
	}

	/**
	 * 
	 * @param url			action地址
	 * @param startPhase	开始期号
	 * @param endPhase		结束期号
	 * @param phaseOrder	期号排序: up or down?
	 * @param coldHotOrder	冷热号顺序？number,还有其他值吗？
	 * @param onlyBody		只显示表格主体(不显示其他菜单)：true or false
	 * @param year			年份
	 * @return
	 */
	public static List<DoubleColor> getSohuSSQ(String url, Integer startPhase, Integer endPhase, String phaseOrder, String coldHotOrder, boolean onlyBody, Integer year) {
		
		StringBuffer sb = new StringBuffer();
		sb.append(url);
		sb.append("?");
		sb.append("startPhase=");
		sb.append(startPhase);
		sb.append("&");
		sb.append("endPhase=");
		sb.append(endPhase);
		sb.append("&");
		sb.append("phaseOrder=");
		sb.append(phaseOrder);
		sb.append("&");
		sb.append("coldHotOrder=");
		sb.append(coldHotOrder);
		sb.append("&");
		sb.append("onlyBody=");
		sb.append(onlyBody);
		
		return getSohuSSQ(sb.toString(), year, "#chartTable");
	}

	public static List<DoubleColor> getSohuSSQ(String url, Integer year,
			String cssQuery) {

		List<DoubleColor> list = new ArrayList<DoubleColor>();

		try {
			Document doc = Jsoup
					.connect(url)
					.userAgent(Constant.USER_AGENT)
					.timeout(30000) // 设置连接超时时间
					.post(); // 使用POST方法访问URL

			// logger.info(doc.html());
			Elements chartTable = doc.select(cssQuery);

			if (null != chartTable) {
				Element tbody = chartTable.select("tbody").first();
				if (null != tbody) {
					Elements trs = tbody.select("tr");
					for (Element tr : trs) {
						if (!tr.hasAttr("class")) {
							DoubleColor ssq = new DoubleColor();
							Element phase = tr.select("td.chart_table_td")
									.first();
							ssq.setPhase(Integer.valueOf(phase.html()));
							Elements tds = tr.select("td.red_ball");
							List<String> red = new ArrayList<String>(6);
							for (Element td : tds) {
								red.add(td.html());
							}
							ssq.setRed(red);
							Element blue = tr.select("td.blue_ball").first();
							ssq.setBlue(blue.html());
							ssq.setYear(year);

							list.add(ssq);
							// logger.info(ssq.toString());
						}
					}
				}

			}

		} catch (IOException e) {
			logger.error("IOException[JsoupHtmlParser->getSohuSSQ(String action, Map<String, String> params, String cssQuery)]: "
					+ e.getMessage());
		}
		return list;
	}
		

	/**
	 * 获取乐彩网双色球红球结果
	 * @param html	乐彩网双色球随机选号结果html字符串
	 * @param cssQuery	Jquery CSS
	 * @return
	 */
	public static String getLeCaiDoubleColor(String html, String cssQuery) {
		
		Document doc = Jsoup.parse(html);
		
		Elements redBalls = doc.select(cssQuery);
		
		return redBalls.html();
	}


}
