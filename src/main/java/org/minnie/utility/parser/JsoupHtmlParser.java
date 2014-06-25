package org.minnie.utility.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.minnie.utility.entity.lottery.FiveInEleven;
import org.minnie.utility.entity.lottery.ShiShiCaiAnalysis;
import org.minnie.utility.entity.lottery.SuperLotto;
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
	public static List<ShiShiCaiAnalysis> getNeteaseShiShiCai(String html) {

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
	public static void analyzeXinShiShiCai(String wei, String html,
			Boolean onAccuracy) {

		int[] candidate = new int[10];
		int[] accuracy = new int[11];
		int current = 0;

		Document doc = Jsoup.parse(html);
		Elements predicts = doc.select("tr.current");
		if (null != predicts) {
			Element predict = predicts.first();
			if (null != predict) {
				current = Integer.valueOf(predict.child(0).html());
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

		// logger.info(Arrays.toString(accuracy));
		// logger.info(Arrays.toString(candidate));
		// Set<Integer> set = new HashSet<Integer>();
		// for (int i = 0; i < 10; i++) {
		// set.add(i);
		// }
		//
		// Set<Integer> excludeSet = new HashSet<Integer>();
		// if (onAccuracy) {
		// for (int j = 0; j < 10; j++) {
		// if (accuracy[j] >= 90) {
		// set.remove(candidate[j]);
		// }
		// }
		// } else {
		// for (int j = 0; j < 10; j++) {
		// set.remove(candidate[j]);
		// }
		// }

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
		if (Constant.JXSSC_GEWEI.equals(wei)) {
			weiCn = "个位";
			int odd = 0;
			int even = 0;
			int big = 0;
			int small = 0;
			int oddHit = 0;
			int evenHit = 0;
			int bigHit = 0;
			int smallHit = 0;
			// List<Integer> odd = new ArrayList<Integer>();
			// List<Integer> even = new ArrayList<Integer>();
			// List<Integer> big = new ArrayList<Integer>();
			// List<Integer> small = new ArrayList<Integer>();
			for (int i = 0; i < 10; i++) {
				if(excluded[i] == 0){
					list.add(i);
					
					if(i % 2 == 0){
						even++;
					} else {
						odd++;
					}
					
					if(i < 5){
						small++;
					} else {
						big++;
					}
				}
				
				
			}
			
			
			logger.info("[" + weiCn + "]单[" + current + "]：" + odd);
			logger.info("[" + weiCn + "]双[" + current + "]：" + even);
			logger.info("[" + weiCn + "]小[" + current + "]：" + small);
			logger.info("[" + weiCn + "]大[" + current + "]：" + big);
		} else if (Constant.JXSSC_SHIWEI.equals(wei)) {
			weiCn = "十位";
			int odd = 0;
			int even = 0;
			int big = 0;
			int small = 0;
			for (int i = 0; i < 10; i++) {
				if (candidate[i] % 2 == 0) {
					even++;
				} else {
					odd++;
				}

				if (candidate[i] > 4) {
					big++;
				} else {
					small++;
				}
			}
			logger.info("[" + weiCn + "]单[" + current + "]：" + odd);
			logger.info("[" + weiCn + "]双[" + current + "]：" + even);
			logger.info("[" + weiCn + "]小[" + current + "]：" + small);
			logger.info("[" + weiCn + "]大[" + current + "]：" + big);
		} else if (Constant.JXSSC_BAIWEI.equals(wei)) {
			weiCn = "百位";
		} else if (Constant.JXSSC_QIANWEI.equals(wei)) {
			weiCn = "千位";
		} else if (Constant.JXSSC_WANWEI.equals(wei)) {
			weiCn = "万位";
		}

		logger.info("[" + weiCn + "]预测结果[" + current + "]："
				+ Arrays.toString(list.toArray()));

	}

}
