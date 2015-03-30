package org.minnie.utility.parser;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.minnie.utility.entity.lottery.FiveInEleven;
import org.minnie.utility.entity.lottery.FiveInElevenCandidate;
import org.minnie.utility.entity.lottery.FiveInElevenPredict2;
import org.minnie.utility.entity.lottery.ShiShiCaiAnalysis;
import org.minnie.utility.entity.lottery.ShiShiCaiPredict;
import org.minnie.utility.entity.lottery.SuperLotto;
import org.minnie.utility.module.netease.Article;
import org.minnie.utility.module.netease.FootballLeague;
import org.minnie.utility.module.netease.FootballTeam;
import org.minnie.utility.module.netease.NeteasePage;
import org.minnie.utility.module.netease.Picture;
import org.minnie.utility.module.netease.SmgFootball;
import org.minnie.utility.module.netease.entity.FootballMatch;
import org.minnie.utility.module.sohu.DoubleColor;
import org.minnie.utility.module.yangshengsuo.Regimen;
import org.minnie.utility.util.Constant;
import org.minnie.utility.util.DateUtil;
import org.minnie.utility.util.ImageUtil;
import org.minnie.utility.util.StringUtil;

/**
 * @author neiplzer@gmail.com
 * @version 2014-3-10 基于Jsoup的Html解析工具类
 */
public class JsoupHtmlParser {

	private static Logger logger = Logger.getLogger(JsoupHtmlParser.class
			.getName());
	
	private static  Map<String,String> map = new HashMap<String,String>();
	static{
	map.put("英格兰","England");
	map.put("意大利","Italy");
	map.put("西班牙","Spain");
	map.put("德国","Germany");
	map.put("法国","France");
	map.put("葡萄牙","Portugal");
	map.put("荷兰","Netherlands");
	map.put("丹麦","Denmark");
	map.put("挪威","Norway");
	map.put("瑞典","Sweden");
	map.put("芬兰","Finland");
	map.put("苏格兰","Scotland");
	map.put("乌克兰","Ukraine");
	map.put("比利时","Belgium");
	map.put("捷克","Czech");
	map.put("土耳其","Turkey");
	map.put("希腊","Greece");
	map.put("保加利亚","Bulgaria");
	map.put("瑞士","Switzerland");
	map.put("以色列","Israel");
	map.put("塞尔维亚","Serbia");
	map.put("奥地利","Austria");
	map.put("波兰","Poland");
	map.put("爱尔兰","Ireland");
	map.put("匈牙利","Hungary");
	map.put("斯洛伐克","Slovakia");
	map.put("斯洛文尼亚","Slovenia");
	map.put("美国","USA");
	map.put("加拿大","Canada");
	map.put("墨西哥","Mexico");
	map.put("巴西","Brazil");
	map.put("阿根廷","Argentina");
	map.put("乌拉圭","Uruguay");
	map.put("巴拉圭","Paraguay");
	map.put("智利","Chile");
	map.put("秘鲁","Peru");
	map.put("哥伦比亚","Columbia");
	map.put("厄瓜多尔","Ecuador");
	map.put("玻利维亚","Bolivia");
	map.put("委内瑞拉","Venezuela");
	map.put("中国","China");
	map.put("韩国","Korea");
	map.put("日本","Japan");
	map.put("澳大利亚","Australia");
	map.put("新加坡","Singapore");
	map.put("沙特","SaudiArabia");
	map.put("摩洛哥","Morocco");
	map.put("喀麦隆","Cameroon");
	map.put("南非","SouthAfrica");
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// Document doc =
			// Jsoup.connect("http://39yss.com/yinshi/yingyang/2014030725664.html").get();
			Document doc = Jsoup
					.connect(
							"http://s.taobao.com/search?initiative_id=staobaoz_20140709&js=1&stats_click=search_radio_all%253A1&q=crucial+ddr3+1600+4g")
					.get();
			System.out.println(doc.title());

			Elements pageBody = doc.select("div.list-view");
			System.out.println(pageBody.html());

			// if (null != pageBody) {
			// Element firstPageBody = pageBody.first();
			// if (!StringUtils.isBlank(firstPageBody.html())) {
			// //
			// System.out.println(firstPageBody.select("span.cptotal").html().replace("共",
			// // "").replace("页", ""));
			// int total = Integer.valueOf(
			// firstPageBody.select("span.cptotal").html()
			// .replace("共", "").replace("页", ""))
			// .intValue();
			// System.out.println(total);
			// if (null != firstPageBody) {
			// Elements linkTag = firstPageBody.select("a");
			// for (Element e : linkTag) {
			// if (e.hasAttr("class")) {
			// System.out.println(e + "---跳过---");
			// } else {
			// System.out.println(e.attr("href"));
			// }
			// }
			// }
			// }
			//
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
	 * @param url
	 *            action地址
	 * @param params
	 *            参数Map，用此法提交有时候参数带不过去，难道是Jsoup的BUG？
	 * @param year
	 *            年份
	 * @param cssQuery
	 * @return
	 */
	public static List<DoubleColor> getSohuSSQ(String action,
			Map<String, String> params, Integer year, String cssQuery) {

		List<DoubleColor> list = new ArrayList<DoubleColor>();

		try {
			Document doc = Jsoup.connect(action).data(params)
					.userAgent(Constant.USER_AGENT).timeout(30000) // 设置连接超时时间
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
								Element blue = tr.select("td.blue_ball")
										.first();
								ssq.setBlue(blue.html());
								ssq.setYear(year);

								list.add(ssq);
							}
						}
					}
				}
			} else {
				// logger.info("startPhase = " + startPhase.val());
				// logger.info("endPhase = " + endPhase.val());
				logger.info(params.get("startPhase") + "期 -> "
						+ params.get("endPhase") + "期 下载失败！");
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

	public static List<DoubleColor> getSohuSSQ(Integer startPhase,
			Integer endPhase, Integer year) {
		return getSohuSSQ(Constant.URL_SOHU_LOTTERY_DOUBLE_COLOR, startPhase,
				endPhase, "up", "number", true, year);
	}

	/**
	 * 
	 * @param url
	 *            action地址
	 * @param startPhase
	 *            开始期号
	 * @param endPhase
	 *            结束期号
	 * @param phaseOrder
	 *            期号排序: up or down?
	 * @param coldHotOrder
	 *            冷热号顺序？number,还有其他值吗？
	 * @param onlyBody
	 *            只显示表格主体(不显示其他菜单)：true or false
	 * @param year
	 *            年份
	 * @return
	 */
	public static List<DoubleColor> getSohuSSQ(String url, Integer startPhase,
			Integer endPhase, String phaseOrder, String coldHotOrder,
			boolean onlyBody, Integer year) {

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
			Document doc = Jsoup.connect(url).userAgent(Constant.USER_AGENT)
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
	 * 
	 * @param html
	 *            乐彩网双色球随机选号结果html字符串
	 * @param cssQuery
	 *            Jquery CSS
	 * @return
	 */
	public static String getLeCaiDoubleColor(String html, String cssQuery) {

		Document doc = Jsoup.parse(html);

		Elements redBalls = doc.select(cssQuery);

		return redBalls.html();
	}

	public static List<SuperLotto> getSohuLotto(String html, String cssQuery,
			Integer year) {

		List<SuperLotto> list = new ArrayList<SuperLotto>();

		Document doc = Jsoup.parse(html);

		Elements chartTable = doc.select(cssQuery);

		if (null != chartTable) {
			Element tbody = chartTable.select("tbody").first();
			if (null != tbody) {
				Elements trs = tbody.select("tr");
				for (Element tr : trs) {
					if (!tr.hasAttr("class")) {
						SuperLotto sl = new SuperLotto();
						Element phase = tr.select("td.chart_table_td").first();
						sl.setPhase(Integer.valueOf(phase.html()));
						Elements rtds = tr.select("td.red_ball");
						List<String> red = new ArrayList<String>(5);
						for (Element td : rtds) {
							red.add(td.html());
						}
						sl.setRed(red);
						Elements btds = tr.select("td.blue_ball");
						List<String> blue = new ArrayList<String>(2);
						for (Element td : btds) {
							blue.add(td.html());
						}
						sl.setBlue(blue);
						sl.setYear(year);

						logger.info(sl.toString());
						list.add(sl);
					}
				}
			}

		}

		return list;
	}

	/**
	 * 获取某日广东11选5数据清单
	 * 
	 * @param html
	 * @param cssQuery
	 * @param date
	 * @return
	 */
	public static List<FiveInEleven> getGDLotteryFiveInEleven(String html,
			String cssQuery, String date) {

		List<FiveInEleven> list = new ArrayList<FiveInEleven>();

		Document doc = Jsoup.parse(html);
		Elements chartTables = doc.select(cssQuery);

		if (null != chartTables && !chartTables.isEmpty()) {
			for (Element table : chartTables) {
				if ("1".equals(table.attr("cellspacing"))) {
					Element tbody = table.select("tbody").first();
					if (null != tbody) {
						Elements trs = tbody.select("tr");
						for (Element tr : trs) {
							if (!tr.hasAttr("bgcolor")) {
								Elements tds = tr.select("td");
								if (null != tds && !tds.isEmpty()) {
									FiveInEleven fie = new FiveInEleven();
									for (Element td : tds) {
										if (!td.hasAttr("width")) {
											String period = td.html();
											if (StringUtil
													.isLegalPeriod(period)) {
												fie.setPeriod(Integer
														.valueOf(period));
											} else {
												break;
											}
										} else if ("154".equals(td
												.attr("width"))) {
											Element strong = td
													.select("strong").first();
											if (null != strong) {
												List<String> red = StringUtil
														.getBalls(strong.html());
												fie.setRed(red);
												Collections.sort(red);
												fie.setLotteryNumber(red
														.toString());
											} else {
												break;
											}
										} else {
											break;
										}
									}// end of for (Element td : tds)
									fie.setCategory("gdd11");
									fie.setDate(date);
									if (null != fie.getPeriod()) {
										logger.info(fie.toString());
										list.add(fie);
									}
								} // end of if(null != tds && !tds.isEmpty())
							} // end of if (!tr.hasAttr("bgcolor"))
						} // end of for (Element tr : trs)
					} // end of if (null != tbody)
				} // end of if ("1".equals(table.attr("cellspacing")))
			} // end of for (Element table : chartTables)
		} // end of if (null != chartTables && !chartTables.isEmpty())

		return list;
	}

	/**
	 * 获取广东体彩网11选5日期清单
	 * 
	 * @param html
	 * @param cssQuery
	 * @param date
	 * @return
	 */
	public static List<String> getGDLotteryFiveInElevenDateList(String html,
			String cssQuery, String date) {

		List<String> list = new ArrayList<String>();

		Document doc = Jsoup.parse(html);
		Elements dateSelects = doc.select(cssQuery);

		if (null != dateSelects) {
			Element dateSelect = dateSelects.first();
			if (null != dateSelect) {
				Elements options = dateSelect.select("option");
				for (Element option : options) {
					list.add(option.html());
				} // end of for (Element option : options)
			} // end of if (null != dateSelect)
		} // end of if (null != dateSelects)

		return list;
	}

	/**
	 * 从BT工厂下载种子
	 * 
	 * @param hs
	 *            HttpClient对象实例
	 * @param url
	 *            下载页面地址
	 * @param directory
	 *            种子存放目录
	 */
	public static void downTorrentsFromBitTorrentFactory(HttpSimulation hs,
			String url, String directory) {
		Document doc;
		String action = null;
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		try {
			doc = Jsoup.connect(url).get();

			Elements forms = doc.select("form");
			if (null != forms && !forms.isEmpty()) {
				Element form = forms.first();
				/**
				 * 将action属性中的相对URI转换成绝对URI
				 */
				// 方法一
				action = form.attr("abs:action");
				// 方法二
				// action = form.absUrl("action");

				Elements elements = form.select("#type");
				if (null != elements && !elements.isEmpty()) {
					nvps.add(new BasicNameValuePair("type", elements.first()
							.val()));
				}

				elements = form.select("#id");
				if (null != elements && !elements.isEmpty()) {
					nvps.add(new BasicNameValuePair("id", elements.first()
							.val()));
				}

				elements = form.select("#name");
				if (null != elements && !elements.isEmpty()) {
					nvps.add(new BasicNameValuePair("name", elements.first()
							.val()));
				}

				hs.downloadFileByPost(action, nvps, directory);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获取http://zx.caipiao.163.com/shahao/jxssc/gewei_20.html杀号结果集
	 * 
	 * @param html
	 * @return
	 */
	public static List<ShiShiCaiAnalysis> getNeteaseXinShiShiCai(String html) {

		List<ShiShiCaiAnalysis> list = new ArrayList<ShiShiCaiAnalysis>();
		int[] candidate = new int[10];
		int[] accuracy = new int[11];
		int current = 0;

		Document doc = Jsoup.parse(html);
		// Elements currentPeriod = doc.select("#currentPeriod");
		//
		// if (null != currentPeriod) {
		// logger.info("当前期号：" + currentPeriod.first().html());
		// }

		Elements tbodies = doc.select("tbody");
		if (null != tbodies) {
			for (Element tbody : tbodies) {
				if (!tbody.hasAttr("class")) {
					Elements trs = tbody.select("tr");
					for (Element tr : trs) {
						if (tr.hasClass("splitBottom")) {
							continue;
						} else if (tr.hasClass("current")) {
							// logger.info(tr.html());
							current = Integer.valueOf(tr.child(0).html());
							for (int i = 2; i <= 11; i++) {
								candidate[i - 2] = Integer.valueOf(tr.child(i)
										.child(0).html());
							}
							break;
						}
						ShiShiCaiAnalysis ssca = new ShiShiCaiAnalysis();
						Element elem = tr.child(0);
						String period = elem.html();
						if (StringUtils.isNumeric(period)) {
							ssca.setPeriod(Integer.valueOf(period));
						}

						elem = tr.child(1);
						Elements spans = elem.select("span");
						StringBuffer sb = new StringBuffer();
						for (Element span : spans) {
							sb.append(span.html());
						}
						String result = sb.toString();
						if (StringUtils.isBlank(result)) {
							continue;
						}
						ssca.setResult(result);

						Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
						elem = tr.child(2);
						map.put(Integer.valueOf(StringUtil.getNumber(elem
								.ownText())),
								elem.select("em").hasClass("iconRight"));
						ssca.setXuanYuan(map);

						elem = tr.child(3);
						map.clear();
						map.put(Integer.valueOf(StringUtil.getNumber(elem
								.ownText())),
								elem.select("em").hasClass("iconRight"));
						ssca.setZhanLu(map);

						elem = tr.child(4);
						map.clear();
						map.put(Integer.valueOf(StringUtil.getNumber(elem
								.ownText())),
								elem.select("em").hasClass("iconRight"));
						ssca.setChiXiao(map);

						elem = tr.child(5);
						map.clear();
						map.put(Integer.valueOf(StringUtil.getNumber(elem
								.ownText())),
								elem.select("em").hasClass("iconRight"));
						ssca.setTaiE(map);

						elem = tr.child(6);
						map.clear();
						map.put(Integer.valueOf(StringUtil.getNumber(elem
								.ownText())),
								elem.select("em").hasClass("iconRight"));
						ssca.setQiXing(map);

						elem = tr.child(7);
						map.clear();
						map.put(Integer.valueOf(StringUtil.getNumber(elem
								.ownText())),
								elem.select("em").hasClass("iconRight"));
						ssca.setMoYe(map);

						elem = tr.child(8);
						map.clear();
						map.put(Integer.valueOf(StringUtil.getNumber(elem
								.ownText())),
								elem.select("em").hasClass("iconRight"));
						ssca.setGanJiang(map);

						elem = tr.child(9);
						map.clear();
						map.put(Integer.valueOf(StringUtil.getNumber(elem
								.ownText())),
								elem.select("em").hasClass("iconRight"));
						ssca.setYuChang(map);

						elem = tr.child(10);
						map.clear();
						map.put(Integer.valueOf(StringUtil.getNumber(elem
								.ownText())),
								elem.select("em").hasClass("iconRight"));
						ssca.setChunJian(map);

						elem = tr.child(11);
						map.clear();
						map.put(Integer.valueOf(StringUtil.getNumber(elem
								.ownText())),
								elem.select("em").hasClass("iconRight"));
						ssca.setChengYing(map);

						elem = tr.child(12);
						if (elem.hasClass("allRight")) {
							ssca.setHit(10);
						} else {
							ssca.setHit(Integer.valueOf(elem.html()));
						}

						list.add(ssca);
						// logger.info(ssca);

					}
				} else {
					Elements trs = tbody.select("tr");
					for (Element tr : trs) {
						if ("准确率".equals(tr.child(0).html())) {
							for (int i = 1; i <= 11; i++) {
								String accu = tr.child(i).html();
								if ("全对".equals(accu)) {
									accuracy[i - 1] = 100;
								} else {
									accu = accu.replaceAll("%", "");
									if (StringUtils.isNumeric(accu)) {
										accuracy[i - 1] = Integer.valueOf(accu);
									}
								}
							}
						}
					}// end of for (Element tr : trs)
				}// end of else
			}
		}

		logger.info(Arrays.toString(accuracy));
		logger.info(Arrays.toString(candidate));
		Set<Integer> set = new HashSet<Integer>();
		for (int i = 0; i < 10; i++) {
			set.add(i);
		}

		for (int j = 0; j < 10; j++) {
			if (accuracy[j] >= 90) {
				set.remove(candidate[j]);
			}
		}

		logger.info("预测结果[" + current + "]：" + Arrays.toString(set.toArray()));

		return list;

	}

	/**
	 * 预测某位结果
	 * 
	 * @param wei
	 *            位：个(gewei)、十(shiwei)、百(baiwei)、千(qianwei)、万(wanwei)
	 * @param html
	 *            抓取的html内容
	 * @param onAccuracy
	 *            是否参照“准确率”
	 */
	public static ShiShiCaiPredict analyzeXinShiShiCai(String wei, String html,
			Boolean onAccuracy) {

		int[] candidate = new int[10];
		int[] accuracy = new int[11];
		ShiShiCaiPredict result = new ShiShiCaiPredict();

		Document doc = Jsoup.parse(html);
		Elements predicts = doc.select("tr.current");
		if (null != predicts) {
			Element predict = predicts.first();
			if (null != predict) {
				result.setPeriod(Integer.valueOf(predict.child(0).html()));
				for (int i = 2; i <= 11; i++) {
					candidate[i - 2] = Integer.valueOf(predict.child(i)
							.child(0).html());
				}
			}
		}

		// <tbody class="statistics">
		Elements statistics = doc.select("tbody.statistics");
		if (null != statistics) {
			Element statistic = statistics.first();
			if (null != statistic) {
				Elements trs = statistic.select("tr");
				for (Element tr : trs) {
					if ("准确率".equals(tr.child(0).html())) {
						for (int i = 1; i <= 11; i++) {
							String accu = tr.child(i).html();
							if ("全对".equals(accu)) {
								accuracy[i - 1] = 100;
							} else {
								accu = accu.replaceAll("%", "");
								if (StringUtils.isNumeric(accu)) {
									accuracy[i - 1] = Integer.valueOf(accu);
								}
							}
						}
					}
				}// end of for (Element tr : trs)
			}
		}

		int[] excluded = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

		if (onAccuracy) {
			for (int j = 0; j < 10; j++) {
				if (accuracy[j] >= 90) {
					excluded[candidate[j]]++;
				}
			}
		} else {
			for (int j = 0; j < 10; j++) {
				excluded[candidate[j]]++;
			}
		}

		List<Integer> list = new ArrayList<Integer>();

		String weiCn = "";
		if (Constant.JXSSC_GEWEI.equals(wei)
				|| Constant.JXSSC_SHIWEI.equals(wei)) {
			if (Constant.JXSSC_GEWEI.equals(wei)) {
				weiCn = "个位";
			} else {
				weiCn = "十位";
			}

			int oddKill = 0;
			int evenKill = 0;
			int bigKill = 0;
			int smallKill = 0;
			for (int i = 0; i < 10; i++) {
				if (excluded[i] == 0) {
					list.add(i);
				} else {
					if (i % 2 == 0) {
						evenKill++;
					} else {
						oddKill++;
					}
					if (i < 5) {
						smallKill++;
					} else {
						bigKill++;
					}
				}
			}

			int count = 0;
			// 除单
			if (oddKill == 5) {
				for (int i = 1; i < 10; i += 2) {
					if (excluded[i] > 0) {
						count += excluded[i];
					}
				}
				result.setEven(count);
			}
			// 除双
			count = 0;
			if (evenKill == 5) {
				for (int i = 0; i < 10; i += 2) {
					if (excluded[i] > 0) {
						count += excluded[i];
					}
				}
				result.setOdd(count);
			}
			// 除小
			count = 0;
			if (smallKill == 5) {
				for (int i = 0; i < 5; i++) {
					if (excluded[i] > 0) {
						count += excluded[i];
					}
				}
				result.setBig(count);
			}
			// 除大
			count = 0;
			if (bigKill == 5) {
				for (int i = 5; i < 10; i++) {
					if (excluded[i] > 0) {
						count += excluded[i];
					}
				}
				result.setSmall(count);
			}
		} else {
			if (Constant.JXSSC_BAIWEI.equals(wei)) {
				weiCn = "百位";
			} else if (Constant.JXSSC_QIANWEI.equals(wei)) {
				weiCn = "千位";
			} else if (Constant.JXSSC_WANWEI.equals(wei)) {
				weiCn = "万位";
			}
			for (int i = 0; i < 10; i++) {
				if (excluded[i] == 0) {
					list.add(i);
				}
			}
		}

		result.setWei(weiCn);
		result.setPredict(list);

		return result;

	}

	/**
	 * "好运11选5"预测结果
	 * 
	 * @param wei
	 *            位：个(gewei)、十(shiwei)、百(baiwei)、千(qianwei)、万(wanwei)
	 * @param html
	 *            抓取的html内容
	 * @param onAccuracy
	 *            是否参照“准确率”
	 */
	public static FiveInElevenCandidate getLuckFiveInElevenCandidate(
			String category, String html, Boolean onAccuracy) {

		int[] kill = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		FiveInElevenCandidate fiec = new FiveInElevenCandidate();

		Document doc = Jsoup.parse(html);
		Elements predicts = doc.select("tr.current");
		if (null != predicts) {
			Element predict = predicts.first();
			if (null != predict) {
				fiec.setPeriod(Integer.valueOf(predict.child(0).html()));
				for (int i = 2; i <= 11; i++) {
					kill[Integer.valueOf(predict.child(i).child(0).html())]++;
				}
			}
		}
		fiec.setCandidate(kill);

		return fiec;

	}

	public static List<FiveInElevenPredict2> getNeteaseLuckFiveInEleven(
			String html) {

		List<FiveInElevenPredict2> list = new ArrayList<FiveInElevenPredict2>();
		String[] candidate = new String[10];
		int[] accuracy = new int[11];
		int current = 0;

		Document doc = Jsoup.parse(html);
		// Elements currentPeriod = doc.select("#currentPeriod");
		//
		// if (null != currentPeriod) {
		// logger.info("当前期号：" + currentPeriod.first().html());
		// }

		Elements tbodies = doc.select("tbody");
		if (null != tbodies) {
			for (Element tbody : tbodies) {
				if (!tbody.hasAttr("class")) {
					Elements trs = tbody.select("tr");
					for (Element tr : trs) {
						if (tr.hasClass("splitBottom")) {
							continue;
						} else if (tr.hasClass("current")) {
							// logger.info(tr.html());
							current = Integer.valueOf(tr.child(0).html());
							for (int i = 2; i <= 11; i++) {
								candidate[i - 2] = tr.child(i).child(0).html();
							}
							break;
						}
						FiveInElevenPredict2 fiea = new FiveInElevenPredict2();
						Element elem = tr.child(0);
						String period = elem.html();
						if (StringUtils.isNumeric(period)) {
							fiea.setPeriod(Integer.valueOf(period));
						}

						elem = tr.child(1);
						Elements spans = elem.select("span");
						// StringBuffer sb = new StringBuffer();
						Set<String> set = new HashSet<String>();
						for (Element span : spans) {
							// sb.append(span.html());
							set.add(span.html());
						}
						// String result = sb.toString();
						if (set.size() == 0) {
							continue;
						}
						fiea.setResult(set);

						// int [] rightKill = {0,0,0,0,0,0,0,0,0,0,0,0};
						// int [] wrongKill = {0,0,0,0,0,0,0,0,0,0,0,0};
						int[] kill = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

						for (int j = 2; j < 12; j++) {
							elem = tr.child(j);
							if (elem.select("em").hasClass("iconRight")) {
								kill[Integer.valueOf(StringUtil.getNumber(elem
										.ownText()))]++;
							} else {
								kill[Integer.valueOf(StringUtil.getNumber(elem
										.ownText()))]--;
							}
						}
						fiea.setKill(kill);

						elem = tr.child(12);
						if (elem.hasClass("allRight")) {
							fiea.setHit(10);
						} else {
							fiea.setHit(Integer.valueOf(elem.html()));
						}

						list.add(fiea);
						// logger.info(ssca);

					}
				} else {
					Elements trs = tbody.select("tr");
					for (Element tr : trs) {
						if ("准确率".equals(tr.child(0).html())) {
							for (int i = 1; i <= 11; i++) {
								String accu = tr.child(i).html();
								if ("全对".equals(accu)) {
									accuracy[i - 1] = 100;
								} else {
									accu = accu.replaceAll("%", "");
									if (StringUtils.isNumeric(accu)) {
										accuracy[i - 1] = Integer.valueOf(accu);
									}
								}
							}
						}
					}// end of for (Element tr : trs)
				}// end of else
			}
		}

		// logger.info(Arrays.toString(accuracy));
		// logger.info(Arrays.toString(candidate));
		// Set<Integer> set = new HashSet<Integer>();
		// for (int i = 0; i < 10; i++) {
		// set.add(i);
		// }
		//
		// for (int j = 0; j < 10; j++) {
		// if (accuracy[j] >= 90) {
		// set.remove(candidate[j]);
		// }
		// }
		//
		// logger.info("预测结果[" + current + "]：" +
		// Arrays.toString(set.toArray()));

		return list;

	}

	public static void getTaobaoData() {
		// String action =
		// "http://s.taobao.com/search?initiative_id=staobaoz_20140709&js=1&style=list&stats_click=search_radio_all%253A1&q=hynix+ddr3l+1600+4g+%B1%CA%BC%C7%B1%BE";

		String action = "http://s.taobao.com/search?spm=a230r.1.8.1.VhjZqz&promote=0&filter=reserve_price%5B150%2C250%5D&sort=default&fs=0&initiative_id=staobaoz_20140709&tab=all&q=crucial+ddr3+1600+4g+%B1%CA%BC%C7%B1%BE&style=list&stats_click=search_radio_all%253A1#J_relative";
		try {
			Document doc = Jsoup
					.connect(action)
					// .data("query", "Java")
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36")
					// .cookie("auth", "token")
					.timeout(3000).post();

			Elements lists = doc.select("div.list-view");
			if (null != lists) {
				// System.out.println(lists.html());
				Element listView = lists.first();
				if (null != listView) {
					Elements rows = listView.select("div.icon-datalink");

					StringBuffer sb = new StringBuffer();

					for (Element row : rows) {
						sb.append(row.attr("nid"));
						sb.append("\t");
						Element tagA = row.select("h3.summary").first()
								.child(0);
						sb.append(tagA.attr("title"));
						sb.append("\t");
						sb.append(tagA.attr("href"));
						sb.append("\t");
						sb.append(row.select("div.price").first().ownText());
						sb.append("\t");
						sb.append(row.select("div.shipping").first().html());
						sb.append("\t");
						sb.append(row.select("span.seller-loc").first().html());
						sb.append("\t");
						Element tagLink = row.select("a.feature-dsc-tgr")
								.first();
						sb.append("\t");
						sb.append(tagLink.ownText());
						sb.append("\t");
						sb.append(tagLink.absUrl("href"));
						sb.append("\n");
					}
					System.out.println(sb.toString());
				}

				//
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Document doc = Jsoup.parse(html);
		// // System.out.println(doc.html());
		// Elements lists = doc.select("div.list-view");
		// if (null != lists) {
		// System.out.println(lists.html());
		// }
	}

	/**
	 * 抓取下一页参数集合
	 * 
	 * @param html
	 * @return
	 */
	public static List<NameValuePair> getParams(String html) {

		Document doc = Jsoup.parse(html);
		Elements link = doc.select("a.nxt");
		if (null != link && link.size() > 0) {
			Element nextPage = link.first();
			// logger.error(nextPage.absUrl("href"));
			try {
				return URLEncodedUtils.parse(new URI(nextPage.absUrl("href")),
						"UTF-8");

			} catch (URISyntaxException e) {
				e.printStackTrace();
				logger.error("URL参数解析失败： " + e.getMessage());
			}
		}

		return Collections.emptyList();
	}

	/**
	 * 抓取文章列表
	 * 
	 * @param html
	 * @param authorId
	 * @param authorMap
	 * @return
	 */
	public static List<Article> getArticleList(String html, Integer authorId,
			Map<Integer, String> authorMap) {

		List<Article> list = new ArrayList<Article>();
		Document doc = Jsoup.parse(html);
		Elements table = doc.select("#delform>table");
		if (null != table && table.size() > 0) {
			Element tbody = table.first().child(0);
			Elements trs = tbody.children();
			for (Element tr : trs) {
				if (tr.hasClass("th")) {
					continue;
				}
				Article article = new Article();
				// logger.info(tr.childNodeSize());

				if (tr.childNodeSize() < 11) {
					continue;
				}

				Element elem = tr.child(1);
				if (null != elem && elem.childNodeSize() > 0) {
					Element link = elem.child(0);
					// 主题
					String subject = link.html();
					// if (subject.indexOf("易眼金睛") > -1) {
					// article.setSubject(subject);
					// } else {
					// continue;
					// }

					// thread ID
					String threadId = link.attr("href");
					if (threadId.endsWith(".html")) {
						threadId = threadId.substring(0,
								threadId.indexOf(".html"));
						article.setThreadId(Integer.valueOf(threadId));
						article.setLink("http://bbs.caipiao.163.com/thread-"
								+ threadId + "-1-1.html");
					}
				}

				elem = tr.child(2);
				if (null != elem && elem.childNodeSize() > 0) {
					article.setModule(elem.child(0).attr("href"));
				}

				article.setCategory("recommendation");
				article.setAuthorId(authorId);
				article.setAuthor(authorMap.get(authorId));

				list.add(article);
			}
		}

		return list;
	}

	/**
	 * 获取帖子详细信息
	 * 
	 * @param html
	 * @param article
	 * @return
	 */
	public static Article getArticleDetail(String html, Article article) {

		Document doc = Jsoup.parse(html);
		Elements postlist = doc.select("#postlist");

		if (null != postlist) {
			Element firstPost = postlist.first();
			if (null != firstPost) {
				// 帖子首次发表时间
				Elements elems = firstPost.select("em[id]");
				if (null != elems && elems.size() > 0) {
					String postTime = StringUtil
							.convertToStandardDateTime(elems.first().html());
					article.setPostTime(postTime);
					// logger.info(postTime);
				}

				// 帖子内容
				elems = firstPost.select("div.t_fsz");
				if (null != elems && elems.size() > 0) {
					String content = elems.first().text();
					article.setContent(content);
					// logger.info(content);
				}

				// 最后编辑时间
				elems = firstPost.select("i.pstatus");
				if (null != elems && elems.size() > 0) {
					String modifyTime = StringUtil
							.convertToStandardDateTime(elems.first().html());
					article.setModifyTime(modifyTime);
					// logger.info(modifyTime);
				}
			}
		}
		return article;
	}

	public static Article getArticleDetail(String html, Article article,
			Map<Integer, String> existThreadMap) {

		Document doc = Jsoup.parse(html);
		Elements postlist = doc.select("#postlist");

		if (null != postlist) {
			Element firstPost = postlist.first();
			if (null != firstPost) {
				// 帖子首次发表时间
				Elements elems = firstPost.select("em[id]");
				if (null != elems && elems.size() > 0) {
					String postTime = StringUtil
							.convertToStandardDateTime(elems.first().html());
					article.setPostTime(postTime);
					// logger.info(postTime);
				}

				// 帖子内容
				elems = firstPost.select("div.t_fsz");
				if (null != elems && elems.size() > 0) {
					String content = elems.first().text();
					article.setContent(content);
					// logger.info(content);
				}

				// 最后编辑时间
				elems = firstPost.select("i.pstatus");
				if (null != elems && elems.size() > 0) {
					String modifyTime = StringUtil
							.convertToStandardDateTime(elems.first().html());
					article.setModifyTime(modifyTime);
					// logger.info(modifyTime);
				}
			}
		}
		return article;
	}

	/**
	 * 获取网易竞彩足球比赛信息
	 * @param html
	 */
	public static List<SmgFootball> getMatchList(String html) {

		Document doc = Jsoup.parse(html);
		Elements matchList = doc.select("div.dataBody.unAttention");
		List<SmgFootball> list = new ArrayList<SmgFootball>();

		for (Element elem : matchList) {
			Elements children = elem.children();
			for (Element child : children) {
				Elements dls = child.select("dl");
				for (Element dl : dls) {
					String gameDate = dl.attr("gameDate");
					gameDate = gameDate.substring(0, 4) + "-" + gameDate.substring(4, 6) + "-" + gameDate.substring(6, 8);
					Elements dds = dl.select("dd");
					for (Element dd : dds) {
						if(!dd.hasAttr("matchcode")){
							continue;
						}
						SmgFootball sf = new SmgFootball();
						Attributes attrs = dd.attributes();

						sf.setGameDate(gameDate);
						sf.setIsStop(attrs.get("isstop"));
						sf.setMatchCode(attrs.get("matchcode"));
						sf.setMatchNumCn(attrs.get("matchnumcn"));
						sf.setStartTime(DateUtil.TimeStamp2Date(attrs
								.get("starttime")));
						sf.setEndTime(DateUtil.TimeStamp2Date(attrs.get("endtime")));
						sf.setIsAttention(attrs.get("isattention"));
						sf.setHomeTeamName(attrs.get("hostname"));
						sf.setAwayTeamName(attrs.get("guestname"));
						sf.setLeagueId(attrs.get("leagueid"));
						sf.setHomeTeamId(attrs.get("hostteamid"));
						sf.setAwayTeamId(attrs.get("visitteamid"));
						sf.setMatchId(attrs.get("matchid"));
						sf.setLeagueName(attrs.get("leaguename"));
						sf.setIsHot(attrs.get("ishot"));
						sf.setScore(attrs.get("score"));

						Elements spans = dd.select("span");
						for (Element span : spans) {
							// 联赛链接
							if (span.hasClass("co2")) {
								sf.setLeagueLink(span.child(0).attr("href"));
							}

							// 主客队排名
							if (span.hasClass("co4")) {
								Element a = span.child(0);
								sf.setMatchLink(a.attr("href"));
								Elements is = a.select("i");
								if(is.size() == 2){
									sf.setHomeTeamRank(is.get(0).html().replaceAll("\\[", StringUtils.EMPTY).replaceAll("]", StringUtils.EMPTY));
									sf.setAwayTeamRank(is.get(1).html().replaceAll("\\[", StringUtils.EMPTY).replaceAll("]", StringUtils.EMPTY));
								}
								Elements elemFinalScore = span.select("div.finalScore");
								if(elemFinalScore.size() == 1){
									sf.setScore(elemFinalScore.first().text());
								}
							}

							// 让球数
							if (span.hasClass("co5")) {
								Elements ems = span.children();
								for (Element em : ems) {
									if (em.hasClass("line2")) {
										BigDecimal bd = new BigDecimal(em.text());
										sf.setConcedePoints(bd);
									}
								}
							}

							// 赔率
							if (span.hasClass("co6_1")) {
								/**
								 * 非让球
								 */
								Elements line1s = span.select("div.line1");
								if(line1s.size() == 1){
									Elements ems = line1s.first().select("em");
									if (ems.size() == 3) {
										String winSp = ems.get(0).attr("sp");
										if (StringUtil.isValidSp(winSp)) {
											// 构造以字符串内容为值的BigDecimal类型的变量bd
											BigDecimal bd = new BigDecimal(
													winSp);
											// 设置小数位数，第一个变量是小数位数，第二个变量是取舍方法(四舍五入)
											// bd=bd.setScale(2,
											// BigDecimal.ROUND_HALF_UP);
											sf.setWinOdds(bd);
										}
										String drawSp = ems.get(1).attr("sp");
										if (StringUtil.isValidSp(drawSp)) {
											sf.setDrawOdds(new BigDecimal(drawSp));
										}
										String loseSp = ems.get(2).attr("sp");
										if (StringUtil.isValidSp(loseSp)) {
											sf.setLoseOdds(new BigDecimal(loseSp));
										}
									}
								}
								/**
								 * 让球
								 */
								Elements line2s = span.select("div.line2");
								if(line2s.size() == 1){
									Elements ems = line2s.first().select("em");
									if (ems.size() == 3) {
										String concedeWinSp = ems.get(0).attr("sp");
										if (StringUtil.isValidSp(concedeWinSp)) {
											sf.setConcedeWinOdds(new BigDecimal(concedeWinSp));
										}
										String concedeDrawSp = ems.get(1).attr("sp");
										if (StringUtil.isValidSp(concedeDrawSp)) {
											sf.setConcedeDrawOdds(new BigDecimal(concedeDrawSp));
										}
										String concedeLoseSp = ems.get(2).attr("sp");
										if (StringUtil.isValidSp(concedeLoseSp)) {
											sf.setConcedeLoseOdds(new BigDecimal(concedeLoseSp));
										}
									}
								}
							}// end of if (span.hasClass("co6_1"))
						}// end of for (Element span : spans)
						logger.info(sf);
						list.add(sf);
					}// end of for (Element dd : dds)
				}

			}// end of for (Element child : children)
		}// end of for (Element elem : matchList)
		logger.info(list.size());
		return list;
	}

	/**
	 * 
	 * @param html
	 * @return
	 */
	public static List<FootballMatch> getFootballMatchList(String html) {
		
		Document doc = Jsoup.parse(html);
//		Elements matchList = doc.select("div.dataBody.unAttention");
		Elements matchList = doc.select("div.dataBody");
		List<FootballMatch> list = new ArrayList<FootballMatch>();
		
		for (Element elem : matchList) {
			Elements children = elem.children();
			for (Element child : children) {
				Elements dls = child.select("dl");
				for (Element dl : dls) {
					String gameDate = dl.attr("gameDate");
					gameDate = gameDate.substring(0, 4) + "-" + gameDate.substring(4, 6) + "-" + gameDate.substring(6, 8);
					Elements dds = dl.select("dd");
					for (Element dd : dds) {
						if(!dd.hasAttr("matchcode")){
							continue;
						}
						
						Attributes attrs = dd.attributes();
						String matchId = attrs.get("matchid");
						FootballMatch match = new FootballMatch();
						
						if(!StringUtils.isNumeric(matchId)){
							continue;
						}
						match.setId(Long.valueOf(matchId));
						match.setGameDate(gameDate);
						match.setIsStop(attrs.get("isstop"));
						match.setMatchCode(attrs.get("matchcode"));
						match.setMatchNumCn(attrs.get("matchnumcn"));
						match.setStartTime(DateUtil.TimeStamp2DateString(attrs.get("starttime")));
						match.setEndTime(DateUtil.TimeStamp2DateString(attrs.get("endtime")));
						match.setIsAttention(attrs.get("isattention"));
						match.setHomeTeamName(attrs.get("hostname"));
						match.setAwayTeamName(attrs.get("guestname"));
						match.setLeagueId(attrs.get("leagueid"));
						match.setHomeTeamId(attrs.get("hostteamid"));
						match.setAwayTeamId(attrs.get("visitteamid"));
//						match.setMatchId(matchId);
						match.setId(Long.valueOf(matchId));
						match.setLeagueName(attrs.get("leaguename"));
						match.setIsHot(attrs.get("ishot"));
						String score = attrs.get("score");
						match.setScore(StringUtil.getScore(score));
						
						Elements spans = dd.select("span");
						for (Element span : spans) {
							// 联赛链接
							if (span.hasClass("co2")) {
								match.setLeagueLink(span.child(0).attr("href"));
							}
							
							// 主客队排名
							if (span.hasClass("co4")) {
								Element a = span.child(0);
								match.setMatchLink(a.attr("href"));
								Elements is = a.select("i");
								if(is.size() == 2){
									match.setHomeTeamRank(is.get(0).html().replaceAll("\\[", StringUtils.EMPTY).replaceAll("]", StringUtils.EMPTY));
									match.setAwayTeamRank(is.get(1).html().replaceAll("\\[", StringUtils.EMPTY).replaceAll("]", StringUtils.EMPTY));
								}
								Elements elemFinalScore = span.select("div.finalScore");
								if(elemFinalScore.size() == 1){
									match.setScore(elemFinalScore.first().text());
								}
							}
							
							// 让球数
//							if (span.hasClass("co5")) {
//								Elements ems = span.children();
//								for (Element em : ems) {
//									if (em.hasClass("line2")) {
//										BigDecimal bd = new BigDecimal(em.text());
//										match.setConcedePoints(bd);
//									}
//								}
//							}
							
							// 赔率
							if (span.hasClass("co6_1")) {
								/**
								 * 非让球
								 */
								Elements line1s = span.select("div.line1");
								if(line1s.size() == 1){
									Elements ems = line1s.first().select("em");
									if (ems.size() == 4) {
										String winSp = ems.get(1).attr("sp");
										if (StringUtil.isValidSp(winSp)) {
											// 构造以字符串内容为值的BigDecimal类型的变量bd
											BigDecimal bd = new BigDecimal(
													winSp);
											// 设置小数位数，第一个变量是小数位数，第二个变量是取舍方法(四舍五入)
											// bd=bd.setScale(2,
											// BigDecimal.ROUND_HALF_UP);
											match.setWinOdds(bd);
										}
										String drawSp = ems.get(2).attr("sp");
										if (StringUtil.isValidSp(drawSp)) {
											match.setDrawOdds(new BigDecimal(drawSp));
										}
										String loseSp = ems.get(3).attr("sp");
										if (StringUtil.isValidSp(loseSp)) {
											match.setLoseOdds(new BigDecimal(loseSp));
										}
									}
								}
								/**
								 * 让球
								 */
								Elements line2s = span.select("div.line2");
								if(line2s.size() == 1){
									Elements ems = line2s.first().select("em");
									if (ems.size() == 4) {
										String concedePoints = ems.get(0).text();
										match.setConcedePoints(new BigDecimal(concedePoints));
										String concedeWinSp = ems.get(1).attr("sp");
										if (StringUtil.isValidSp(concedeWinSp)) {
											match.setConcedeWinOdds(new BigDecimal(concedeWinSp));
										}
										String concedeDrawSp = ems.get(2).attr("sp");
										if (StringUtil.isValidSp(concedeDrawSp)) {
											match.setConcedeDrawOdds(new BigDecimal(concedeDrawSp));
										}
										String concedeLoseSp = ems.get(3).attr("sp");
										if (StringUtil.isValidSp(concedeLoseSp)) {
											match.setConcedeLoseOdds(new BigDecimal(concedeLoseSp));
										}
									}
								}
							}// end of if (span.hasClass("co6_1"))
						}// end of for (Element span : spans)
						logger.info(match);
						list.add(match);
					}// end of for (Element dd : dds)
				}
				
			}// end of for (Element child : children)
		}// end of for (Element elem : matchList)
		logger.info(list.size());
		return list;
	}
	
	private static final String LEAGUE_EUROPE = "euroLeague";
	private static final String LEAGUE_CENTRAL_NORTH_AMERICA = "cnCupAmericaLeague";
	private static final String LEAGUE_SOUTH_AMERICA = "sCupAmericaLeague";
	private static final String LEAGUE_ASIA = "asiaLeague";
	private static final String LEAGUE_AFRICA = "africaLeague";
	
	private static final String CUP_EUROPE = "euroCup";
	private static final String CUP_CENTRAL_NORTH_AMERICA = "cnCupAmericaCup";
	private static final String CUP_SOUTH_AMERICA = "sCupAmericaCup";
	private static final String CUP_ASIA = "asiaCup";
	private static final String CUP_AFRICA = "africaCup";
	private static final String CUP_WORLD = "worldCup";

	
	private static final String IMAGE_PATH = System.getProperty("user.dir") + File.separator + "image";
	/**
	 * 获取赛事列表
	 * @param html
	 * @return
	 */
	public static List<FootballLeague> getLeagueList(String html) {
		
		Set<String> picUrlSet = new HashSet<String>();
		
		Document doc = Jsoup.parse(html);
		Elements leagueList = doc.select("div.matchList");
		List<FootballLeague> list = new ArrayList<FootballLeague>();
		for (Element elem : leagueList) {
			/**
			 * 联赛
			 */
			Elements leagueBoxes = elem.select("div.leagueBox");
			for(Element leagueBox : leagueBoxes){
				Elements leagues = leagueBox.select("div.league_con.stip_lib");
				for (Element league : leagues) {
					Element elemCountry = league.child(0);
					String continentalCn = null;
					String country = null;
					
					String continental = league.attr("tip");
					continental = continental.replaceAll("[\\#\\d]", StringUtils.EMPTY);
					if(LEAGUE_EUROPE.equals(continental)){
						continentalCn = "欧洲";
					} else if(LEAGUE_CENTRAL_NORTH_AMERICA.equals(continental)){
						continentalCn = "中北美洲";
					} else if(LEAGUE_SOUTH_AMERICA.equals(continental)){
						continentalCn = "南美洲";
					} else if(LEAGUE_ASIA.equals(continental)){
						continentalCn = "亚洲";
					} else if(LEAGUE_AFRICA.equals(continental)){
						continentalCn = "非洲";
					} 
					if(elemCountry.hasAttr("href")){
						country = elemCountry.select("span.leagueName").first().text();
						String leagueLogoLink = elemCountry.select("span.leagueLogo>img").first().attr("data-src");
						int symbolQuestion = leagueLogoLink.indexOf("?");
						if(symbolQuestion >= 0){
							leagueLogoLink = leagueLogoLink.substring(0, symbolQuestion);
						}
						if(!picUrlSet.contains(leagueLogoLink)){
							ImageUtil.save2File(IMAGE_PATH, "league", leagueLogoLink);
							picUrlSet.add(leagueLogoLink);
						}
					}
					
					Elements leagueCons = league.child(1).select("div.league_con");
					for (Element con : leagueCons) {
						Element a = con.child(0);
						String link = a.attr("href");
						String id = link.substring(link.lastIndexOf("/") + 1, link.lastIndexOf(".html"));
						if(!StringUtils.isNumeric(id)){
							continue;
						}
						FootballLeague fl = new FootballLeague();
						fl.setId(Long.valueOf(id));
						fl.setLink(link);
						fl.setName(a.select("span.leagueName").first().text());
						fl.setCategory("league");
						fl.setContinental(continentalCn);
						fl.setCountry(country);
						String leagueLogoLink = a.select("span.leagueLogo>img").first().attr("data-src");
						int symbolQuestion = leagueLogoLink.indexOf("?");
						if(symbolQuestion >= 0){
							leagueLogoLink = leagueLogoLink.substring(0, symbolQuestion);
						}
						if(!picUrlSet.contains(leagueLogoLink)){
							ImageUtil.save2File(IMAGE_PATH, "league", leagueLogoLink);
							picUrlSet.add(leagueLogoLink);
						}
						list.add(fl);
					}

				}
			}// end of 联赛
			
			/**
			 * 杯赛
			 */
			Elements cupBoxes = elem.select("div.cupBox");
			for(Element cupBox : cupBoxes){
				Elements cups = cupBox.select("div.league_con.stip_lib");
				for (Element cup : cups) {
					String continentalCn = null;
					String continental = cup.attr("tip");
					continental = continental.replaceAll("[\\#\\d]", StringUtils.EMPTY);
					
					if(CUP_EUROPE.equals(continental)){
						continentalCn = "欧洲";
					} else if(CUP_CENTRAL_NORTH_AMERICA.equals(continental)){
						continentalCn = "中北美洲";
					} else if(CUP_SOUTH_AMERICA.equals(continental)){
						continentalCn = "南美洲";
					} else if(CUP_ASIA.equals(continental)){
						continentalCn = "亚洲";
					} else if(CUP_AFRICA.equals(continental)){
						continentalCn = "非洲";
					}
					
					Elements leagueCons = cup.select("div.league_con");
					for (Element con : leagueCons) {
						Element a = con.child(0);
						String link = a.attr("href");
						String id = link.substring(link.lastIndexOf("/") + 1, link.lastIndexOf(".html"));
						if(!StringUtils.isNumeric(id)){
							continue;
						}
						FootballLeague fl = new FootballLeague();
						fl.setId(Long.valueOf(id));
						fl.setCategory("cup");
						fl.setContinental(continentalCn);
						fl.setLink(link);
						fl.setName(a.select("span.leagueName.noSub").first().text());
						String leagueLogoLink = a.select("span.leagueLogo>img").first().attr("data-src");
						int symbolQuestion = leagueLogoLink.indexOf("?");
						if(symbolQuestion >= 0){
							leagueLogoLink = leagueLogoLink.substring(0, symbolQuestion);
						}
						if(!picUrlSet.contains(leagueLogoLink)){
							ImageUtil.save2File(IMAGE_PATH, "league", leagueLogoLink);
							picUrlSet.add(leagueLogoLink);
						}
						list.add(fl);
					}
				}
			}// end of 杯赛
			
			/**
			 * 国际赛事
			 */
			Elements worldBoxes = elem.select("div.worldBox");
			for(Element worldBox : worldBoxes){
				Elements cups = worldBox.select("div.league_con.stip_lib");
				for (Element cup : cups) {
//					String continental = cup.attr("tip");
//					continental = continental.replaceAll("[\\#\\d]", StringUtils.EMPTY);
//					if(CUP_WORLD.equals(continental)){
//						fl.setContinental("国际");
//					}
					
					Elements leagueCons = cup.select("div.league_con");
					for (Element con : leagueCons) {
						Element a = con.child(0);
						String link = a.attr("href");
						String id = link.substring(link.lastIndexOf("/") + 1, link.lastIndexOf(".html"));
						if(!StringUtils.isNumeric(id)){
							continue;
						}
						FootballLeague fl = new FootballLeague();
						fl.setCategory("cup");
						fl.setId(Long.valueOf(id));
						fl.setLink(link);
						fl.setName(a.select("span.leagueName.noSub").first().text());
						String leagueLogoLink = a.select("span.leagueLogo>img").first().attr("data-src");
						int symbolQuestion = leagueLogoLink.indexOf("?");
						if(symbolQuestion >= 0){
							leagueLogoLink = leagueLogoLink.substring(0, symbolQuestion);
						}
						if(!picUrlSet.contains(leagueLogoLink)){
							ImageUtil.save2File(IMAGE_PATH, "league", leagueLogoLink);
							picUrlSet.add(leagueLogoLink);
						}
						list.add(fl);
					}
				}
			}// end of 国际赛事
		}
		return list;
	}
	

	/**
	 * 获取球队链接列表
	 * @param html
	 * @return
	 */
	public static List<String> getTeamList(String html) {
		
		List<String> list = new ArrayList<String>();
		Document doc = Jsoup.parse(html);
		Elements div = doc.select("div.teameInfo.sign");
		if(div.size() > 0){
			Elements teams = div.first().select("a");
			for(Element team : teams){
				logger.info(team.attr("href") + "->" + team.text());
				list.add(team.attr("href"));
			}
		}
		
		return list;
	}

	public static FootballTeam getTeamDetail(String html, Long teamId, Long leagueId, String link) {
		
		FootballTeam team = null;
		Document doc = Jsoup.parse(html);
		Elements div = doc.select("div.teamheader.clearfix");
		if(div.size() > 0){
			Element teamheader = div.first();
			team = new FootballTeam();
			team.setId(teamId);
			team.setLeagueId(leagueId);
			team.setLink(link);
			/**
			 * logo
			 */
			Elements teamLogo = teamheader.select("span.teamLogo>img");
			if(teamLogo.size() > 0){
				String logoLink = teamLogo.first().attr("src");
				
				int symbolQuestion = logoLink.indexOf("?");
				if(symbolQuestion >= 0){
					logoLink = logoLink.substring(0, symbolQuestion);
				}
				ImageUtil.save2File(IMAGE_PATH, "team", logoLink);
			}
			/**
			 * 详情
			 */
			Elements teamDetail = teamheader.select("div.teamDetail>p");
			if(teamDetail.size() == 4){
				//球队名称
				team.setName(teamDetail.get(0).child(0).html());
				//球队联赛排名
				String rank = teamDetail.get(1).text();
				rank = rank.substring(rank.indexOf("排名：") + 3);
				team.setRank(rank);
				//全称、所在城市
				Elements em2 = teamDetail.get(2).children();
				if(em2.size() == 2){
					String fullName = em2.get(0).text();
					fullName = fullName.substring(fullName.indexOf("全称：") + 3);
					team.setFullName(fullName);
					
					String city = em2.get(1).text();
					city = city.substring(city.indexOf("所在城市：") + 5);
					team.setCity(city);
				}
				//球队主场、主教练
				Elements em3 = teamDetail.get(3).children();
				if(em3.size() == 2){
					String homeCourt = em3.get(0).text();
					homeCourt = homeCourt.substring(homeCourt.indexOf("球队主场：") + 5);
					team.setHomeCourt(homeCourt);
					
					String headCoach = em3.get(1).text();
					headCoach = headCoach.substring(headCoach.indexOf("主教练：") + 4);
					team.setHeadCoach(headCoach);
				}
			}
			
			Elements brief = teamheader.select("div.teamDetail>div.summary.clearfix");
			String briefContent = brief.first().child(1).text();
			if(!"暂无[查看详情]".equals(briefContent)){
				team.setBriefIntroduction("intro");
			}
//			logger.info(brief.first().child(1).text());

		}
		logger.info(team);
		return team;
	}
	
	public static Set<String> getLeagueNameCnByCaipiao365(String html) {
	
		Set<String> set = new HashSet<String>();
		Document doc = Jsoup.parse(html);
		Elements divs = doc.select("div.jczq-kjing");
		if(divs.size() > 0){
			Elements options = divs.first().select("select option");
			for (Element option : options) {
				String val = option.val();
				if(!StringUtils.isBlank(val)){
					set.add(val);
				}
			}
		}
		return set;
	}
	
	/**
	 * 获取结果页数
	 * @param html
	 * @return
	 */
	public static int getTotalPagesByCaipiao365(String html) {
		
		int pages = 1;
		Document doc = Jsoup.parse(html);
		Elements divs = doc.select("div.page-left");
		if(divs.size() > 0){
			Elements hrefs = divs.first().select("[href]");
			for (Element href : hrefs) {
				if(href.hasClass("pre")){
					continue;
				}
				String val = href.text();
				if(StringUtils.isNumeric(val)){
					int page = Integer.valueOf(val);
					if(page > pages){
						pages = page;
					}
				}
			}
		}
		return pages;
	}
	
	public static List<FootballMatch> getFootballMatchResult(String html, List<FootballMatch> matchList) {
		
		List<FootballMatch> matchResultList = new ArrayList<FootballMatch>();
		if(null == matchList){
			return matchResultList;
		}
		
		Map<String, FootballMatch> map = new HashMap<String, FootballMatch>();
		for(FootballMatch fm : matchList){
			map.put(fm.getMatchNumCn(), fm);
		}
		
		Document doc = Jsoup.parse(html);
		Elements divs = doc.select("div.jczq-kjing");
		if(divs.size() > 0){
			Elements tbodies = divs.first().select("table>tbody");
			if(tbodies.size() > 0){
				Elements trs = tbodies.first().children();
				for (Element tr : trs) {
					FootballMatch match = map.get(tr.child(0).text());
					if(null != match){
						Matcher matcher = StringUtil.scorePattern.matcher(tr.child(4).text());
						int count = 0;
						while (matcher.find()) {
							switch(count){
								case 0:
									String score = matcher.group();//比分
									match.setScore(score);
									String[] scores = score.split(":");
									int total = 0;
									for(String sc : scores){
										if(StringUtils.isNumeric(sc)){
											total += Integer.valueOf(sc);
										}
									}
									match.setGoals(total);//总进球数
									count++;
									break;
								case 1:
									match.setFisrtHalfScore(matcher.group());// 上半场比分
									break;
							}
						}
						
						//胜平负赛果
						match.setMatchResult(tr.child(6).text());
						
						//胜平负赛果
						match.setConcedeMatchResult(tr.child(8).text());
						
						//比分赛果
						match.setScoreResult(tr.child(9).text());
						
						//总进球数赛果
						match.setGoalsResult(tr.child(10).text());
						
						//半全场赛果
						match.setHalfFullResult(tr.child(11).text());
						
						matchResultList.add(match);
						logger.info(match);
					}

				}
			}
		}
		return matchResultList;
	}
	
}