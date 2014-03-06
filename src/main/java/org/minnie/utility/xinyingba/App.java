package org.minnie.utility.xinyingba;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.PrototypicalNodeFactory;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.minnie.utility.persistence.MysqlDatabseHelper;
import org.minnie.utility.tags.DdTag;
import org.minnie.utility.tags.DlTag;
import org.minnie.utility.tags.DtTag;
import org.minnie.utility.tags.EmTag;
import org.minnie.utility.util.Constant;
import org.minnie.utility.util.DateUtil;

public class App {

	private static Logger logger = Logger.getLogger(App.class.getName());

	private static ResourceBundle rb;
	private static BufferedInputStream inputStream;

	public static List<Video> movieList = new ArrayList<Video>();
	
	private static final ExecutorService DEFAULT_TASK_EXECUTOR;
	
	static {
		DEFAULT_TASK_EXECUTOR = (ExecutorService) Executors
				.newFixedThreadPool(50);
	};

	private static Object lock = new Object();
	private static int size = 0;

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// 获取加载系统配置时间
		long configurationStartTime = System.currentTimeMillis();

		/**
		 * 读取log4j配置
		 */
		// BasicConfigurator.configure();// 默认配置
		PropertyConfigurator.configure(System.getProperty("user.dir")
				+ Constant.LOG_LOG4J_PARAM_FILE);

		logger.info("程序开始执行时间：" + DateUtil.getTime(configurationStartTime));

		//每页影片数量
		int videoPerPage = 14;
		//影片分类
		String videoCategory = "";
		//批量导入影片信息[不包含缺二进制海报图片]SQL
		String sqlBatchAddVideoWithoutImage = null;
		//批量导入影片信息[包含缺二进制海报图片]SQL
		String sqlBatchAddVideoWithImage = null;
		//获取影片列表
		String sqlVideoList = null;
		//批量更新图片SQL
		String sqlBatchUpdateImage = null;
		//批量更新图片SQL[带标记]
		String sqlBatchUpdateImageWithFLag = null;
		//图片保存路径
		String pictureStoreDirectory = null;

		/**
		 * 加载系统参数
		 */
		String confFilePath = System.getProperty("user.dir")
				+ Constant.SYS_PARAM_FILE;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(
					confFilePath));
			rb = new PropertyResourceBundle(inputStream);

			logger.info("加载系统参数......");
			videoPerPage = Integer.valueOf(rb.getString("video.per.page"))
					.intValue();
			if (videoPerPage == 0) {
				videoPerPage = 14;
			}
			logger.info("\t video.per.page = " + videoPerPage);

			videoCategory = rb.getString("video.category");
			logger.info("\t video.category = " + videoCategory);

			sqlBatchAddVideoWithoutImage = rb.getString("sql.batchAddVideo.withoutImage");
			logger.info("\t sql.batchAddVideo.withoutImage = " + sqlBatchAddVideoWithoutImage);
			
			sqlBatchAddVideoWithImage = rb.getString("sql.batchAddVideo.withImage");
			logger.info("\t sql.batchAddVideo.withImage = " + sqlBatchAddVideoWithImage);
			
			sqlVideoList = rb.getString("sql.video.list");
			logger.info("\t sql.video.list = " + sqlVideoList);
			
			sqlBatchUpdateImage = rb.getString("sql.batchUpdateImage");
			logger.info("\t sql.batchUpdateImage = " + sqlBatchUpdateImage);
			
			sqlBatchUpdateImageWithFLag = rb.getString("sql.batchUpdateImage.withFlag");
			logger.info("\t sql.batchUpdateImage.withFlag = " + sqlBatchUpdateImageWithFLag);
			
			pictureStoreDirectory = rb.getString("picture.store.directory");
			logger.info("\t picture.store.directory = " + pictureStoreDirectory);
			
			if(null == pictureStoreDirectory || Constant.BLANK.equals(pictureStoreDirectory)){
				pictureStoreDirectory = "C:/xinyingba";
			}

			// 关闭inputStream
			if (null != inputStream) {
				inputStream.close();
			}
		} catch (FileNotFoundException fnfe) {
			logger.error(fnfe.getMessage());
		} catch (IOException ioe) {
			logger.error(ioe.getMessage());
		}

		// 获取加载系统配置结束时间
		long configurationEndTime = System.currentTimeMillis();

		logger.info("加载系统配置耗时 "
				+ (configurationEndTime - configurationStartTime) + "ms");
		
		MysqlDatabseHelper.batchUpdateImageWithFlag(sqlBatchUpdateImageWithFLag, pictureStoreDirectory);

//		long countStartTime = System.currentTimeMillis();
//		// 获取电影数目
//		int total = getTotal(videoCategory, 0);
//		long countEndTime = System.currentTimeMillis();
//		logger.info("获取影片数量[" + total + "]耗时 "
//				+ (countEndTime - countStartTime) + "ms");
//
//		int pages = Double.valueOf(Math.ceil(total / (double) videoPerPage))
//				.intValue();
//		for (int j = 1; j <= pages; j++) {
//			long startTime = System.currentTimeMillis();
//			getVideoListByCategory(j, videoCategory, 0);
//			long endTime = System.currentTimeMillis();
//			logger.info("获取第" + j + "页影片信息耗时 " + (endTime - startTime) + "ms");
//		}
//
//		long dbStartTime = System.currentTimeMillis();
//		// MysqlDatabseHelper.batchAddVideo(movieList, sqlBatchAddVideoWithImage);
//		MysqlDatabseHelper.batchAddVideoWithoutImage(movieList, sqlBatchAddVideoWithoutImage);
//		long dbEndTime = System.currentTimeMillis();
//		logger.info("信息写入数据库耗时 " + (dbEndTime - dbStartTime) + "ms");
//
//		logger.info("程序结束执行时间：" + DateUtil.getTime(dbEndTime));
//		logger.info("程序耗时 " + (dbEndTime - configurationStartTime) + "ms");
		
//		long dbStartTime = System.currentTimeMillis();
//		List<Video> list = MysqlDatabseHelper.getVideoList(sqlVideoList);
//		long dbEndTime = System.currentTimeMillis();
//		logger.info("信息从数据库加载耗时 " + (dbEndTime - dbStartTime) + "ms");
		
//		new DownloadService("C:/xinyingba", list,
//				new DownloadStateListener() {
//
//					@Override
//					public void onFinish() {
//						// 图片下载成功后，实现您的代码
//						System.out.println("图片下载完成！");
//					}
//
//					@Override
//					public void onFailed() {
//						// 图片下载成功后，实现您的代码
//						System.out.println("图片下载失败！");
//					}
//				}).startDownload();

//		long queueStartTime = System.currentTimeMillis();
//		// 队列
//		LinkedBlockingQueue<Video> queue = new LinkedBlockingQueue<Video>(
//				list.size());
////		Set<Integer> videoSet = new HashSet<Integer>();
//		for (Video video : list) {
////			videoSet.add(video.getNumber());
//			try {
//				queue.put(video);
//			} catch (InterruptedException e) {
//				logger.info("InterruptedException[App->main]: " + e.getMessage());
//			}
//		}
//		long queueEndTime = System.currentTimeMillis();
//		logger.info("载入LinkedBlockingQueue耗时 "
//				+ (queueEndTime - queueStartTime) + "ms");
//		logger.info("list.size() =  " + list.size());
//		logger.info("queue.size() =  " + queue.size());
//		logger.info("videoSet.size() =  " + videoSet.size());

//		Set<Integer> resultSet = FileUtil.getOmittedNumber(pictureStoreDirectory, videoSet);
//		StringBuilder sb = new StringBuilder();
//		sb.append("select * from ent_movie where number in (");
//		for(Integer integer : resultSet){
//			sb.append(integer);
//			sb.append(",");
//		}
//		sb.append(")");
//		logger.info(sb.toString());
//		   
//		// 消费者
//		Consumer consumer = new Consumer(queue, DEFAULT_TASK_EXECUTOR, pictureStoreDirectory);
//		consumer.setSize(size);
//		consumer.setLock(lock);
//		for (int i = 0; i < 50; i++) {
//			// new Thread(consumer).start();
//			DEFAULT_TASK_EXECUTOR.execute(consumer);
//		}

	}

	/**
	 * 获取某类电影的数量
	 * 
	 * @param category
	 *            影片分类，包括：
	 *            电影(dianying)、电视(dianshi)、综艺(zongyi)、动漫(dongman)、更新(jinri
	 *            )、预告(yugao)等
	 * @param year
	 *            只有电影(dianying)、电视(dianshi)需要填写年份
	 * @return
	 */
	public static int getTotal(String category, int year) {

		int total = -1;
		StringBuilder sb = new StringBuilder();
		sb.append(Constant.URL_XINYINGBA);

		if (Constant.CATEGORY_XINYINGBA_MOVIE.equals(category)
				|| Constant.CATEGORY_XINYINGBA_TV.equals(category)) {
			sb.append("/");
			sb.append(category);
			sb.append("/");
			if (year > 0) {
				sb.append(year);
				sb.append("-nian.htm");
			}
		} else if (Constant.CATEGORY_XINYINGBA_VARIETY_SHOW.equals(category)
				|| Constant.CATEGORY_XINYINGBA_ANIMATION.equals(category)
				|| Constant.CATEGORY_XINYINGBA_UPDATE_TODAY.equals(category)
				|| Constant.CATEGORY_XINYINGBA_UPDATE_TODAY.equals(category)) {
			sb.append("/");
			sb.append(category);
			sb.append("/");
		} else {
			return 0;
		}

		Parser parser;
		try {
			parser = new Parser(
					(HttpURLConnection) (new URL(sb.toString()))
							.openConnection());
			// 找到class="blue"的em
			NodeFilter filter = new HasAttributeFilter("class", "blue");
			NodeList nodes = parser.extractAllNodesThatMatch(filter);

			if (nodes != null && nodes.size() > 0) {
				Node textnode = (Node) nodes.elementAt(0);
				total = Integer.valueOf(textnode.getNextSibling().getText())
						.intValue();
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

	/**
	 * 根据地区获取电影清单
	 * 
	 * @param page
	 *            第几页
	 * @param region
	 *            地区，包括：
	 *            欧美(oumei)、大陆(dalu)、香港(xianggang)、台湾(taiwan)、韩国(hanguo)、日本
	 *            (riben)
	 */
	public static void getMovieListByRegion(int page, String region) {
		getVideoListByRegion(page, region);
	}

	/**
	 * 根据地区获取电视剧清单
	 * 
	 * @param page
	 *            第几页
	 * @param region
	 *            地区，包括：
	 *            国产剧(guochan)、港台剧(gangtai)、日韩剧(rihan)、欧美剧(oumeiju)、新马泰剧(xinmatai
	 *            )、其他片(qitapian)
	 */
	public static void getTvListByRegion(int page, String region) {
		getVideoListByRegion(page, region);
	}

	/**
	 * 根据地区获取影片清单
	 * 
	 * @param page
	 *            第几页
	 * @param region
	 *            地区
	 */
	public static void getVideoListByRegion(int page, String region) {

		if (page <= 0) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		sb.append(Constant.URL_XINYINGBA);
		sb.append("/");
		sb.append(region);
		sb.append("/");
		sb.append(page);
		sb.append(".htm");

		getVideoList(sb.toString());
	}

	/**
	 * 根据影片分类获取影片清单
	 * 
	 * @param page
	 *            第几页
	 * @param category
	 *            影片分类，包括：
	 *            电影(dianying)、电视(dianshi)、综艺(zongyi)、动漫(dongman)、更新(jinri
	 *            )、预告(yugao)等
	 * @param year
	 *            只有电影(dianying)、电视(dianshi)需要填写年份
	 */
	public static void getVideoListByCategory(int page, String category,
			int year) {

		if (page <= 0) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		sb.append(Constant.URL_XINYINGBA);
		if (Constant.CATEGORY_XINYINGBA_MOVIE.equals(category)
				|| Constant.CATEGORY_XINYINGBA_TV.equals(category)) {
			sb.append("/");
			sb.append(category);
			sb.append("/");
			if (year > 0) {
				sb.append(year);
				sb.append("-nian-");
			}
			sb.append(page);
			sb.append(".htm");
		} else if (Constant.CATEGORY_XINYINGBA_VARIETY_SHOW.equals(category)
				|| Constant.CATEGORY_XINYINGBA_ANIMATION.equals(category)
				|| Constant.CATEGORY_XINYINGBA_UPDATE_TODAY.equals(category)
				|| Constant.CATEGORY_XINYINGBA_UPDATE_TODAY.equals(category)) {
			sb.append("/");
			sb.append(category);
			sb.append("/");
			sb.append(page);
			sb.append(".htm");
		} else {
			return;
		}

		getVideoList(sb.toString());
	}

	/**
	 * 获取影片信息
	 * 
	 * @param url
	 *            分类URL地址
	 */
	public static void getVideoList(String url) {

		// 注册使用DlTag
		PrototypicalNodeFactory factory = new PrototypicalNodeFactory();
		factory.registerTag(new DlTag());
		factory.registerTag(new DtTag());
		factory.registerTag(new DdTag());
		factory.registerTag(new EmTag());

		Pattern pattern = Pattern.compile(Constant.REG_NUMBER);

		Parser parser;
		try {
			parser = new Parser(
					(HttpURLConnection) (new URL(url)).openConnection());
			parser.setNodeFactory(factory);
			// 找到class=video-list gclearfix的div
			NodeFilter filter = new HasAttributeFilter("class", "section");
			NodeList nodes = parser.extractAllNodesThatMatch(filter);

			if (nodes != null) {
				for (int i = 0; i < nodes.size(); i++) {
					Video video = new Video();

					Node node = (Node) nodes.elementAt(i);
					if (node instanceof DlTag) {
						DlTag dlTag = (DlTag) node;
						Matcher matcher = pattern.matcher(dlTag
								.getAttribute("id"));
						if (matcher.find()) {
							video.setNumber(Integer.valueOf(matcher.group())
									.intValue());
						}
					}
					traversal(node, video, 1);
					// System.out.println(video.toString());
					logger.info(video.toString());
					movieList.add(video);
				}
			}
		} catch (ParserException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void getMovieListByFile(String filePath) {
		Parser parser;
		try {
			parser = new Parser();
			// parser.setEncoding("UTF-8");
			parser.setResource(filePath);
			// System.out.println("--------"+parser.getEncoding());

			// 找到class=video-list gclearfix的div
			NodeFilter filter = new HasAttributeFilter("class", "section");
			NodeList nodes = parser.extractAllNodesThatMatch(filter);

			if (nodes != null) {
				for (int i = 0; i < nodes.size(); i++) {
					Node node = (Node) nodes.elementAt(i);
					// bianli(node);
				}
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}

	}

	public static void traversal(Node node, Video video, int level) {
		// System.out.println("level : " + level);
		NodeList nodeList = node.getChildren();
		if (nodeList == null) {
			return;
		}
		int size = nodeList.size();
		for (int j = 0; j < size; j++) {
			Node childNode = nodeList.elementAt(j);
			// 去空
			if (childNode.getText().trim().equals("")) {
				continue;
			}
			// System.out.println("getText" + j + ":" + childNode.getText());
			if (childNode instanceof DtTag) {
				Node cn = childNode.getFirstChild();
				if (null != cn) {
					if (cn instanceof LinkTag) {
						LinkTag linkTag = (LinkTag) cn;
						// 获取影片地址
						video.setUrl(linkTag.getLink());
						// 获取影片标题
						video.setTitle(linkTag.getAttribute("title"));
					}
				}
				continue;
			} else if (childNode instanceof DdTag) {
				DdTag ddTag = (DdTag) childNode;
				if (Constant.TAG_DD_ATTRIBUTE_CLASS_VIDEO_COVER.equals(ddTag
						.getAttribute("class"))) {
					NodeList childNodeList = childNode.getChildren();
					if (null != childNodeList) {
						int childNodeListSize = childNodeList.size();
						for (int p = 0; p < childNodeListSize; p++) {
							Node ddNode = childNodeList.elementAt(p);
							// 去空
							if (ddNode.getText().trim().equals("")) {
								continue;
							}
							if (ddNode instanceof LinkTag) {
								NodeList ddANodeList = ddNode.getChildren();
								if (null != ddANodeList) {
									int ddANodeListSize = ddANodeList.size();
									for (int q = 0; q < ddANodeListSize; q++) {
										Node ddANode = ddANodeList.elementAt(q);
										if (ddANode instanceof ImageTag) {
											ImageTag imgTag = (ImageTag) ddANode;
											// 获取影片海报地址
											video.setImageSource(imgTag
													.getAttribute("data-original"));
											break;
										}
									}
								}
								break;
							}
						}
					}
				} else if (Constant.TAG_DD_ATTRIBUTE_CLASS_VIDEO_INTRO
						.equals(ddTag.getAttribute("class"))) {
					// System.out.println("TAG_ATTRIBUTE_CLASS_VIDEO_INTRO :" +
					// childNode.getText());
					// 获取影片类型地址
					Node cnf = childNode.getFirstChild();
					if (null != cnf) {
						video.setIntroduction(cnf.getText());
					}

				} else if (Constant.TAG_DD_ATTRIBUTE_CLASS_VIDEO_GRADE
						.equals(ddTag.getAttribute("class"))) {
					NodeList childNodeList = childNode.getChildren();
					if (null != childNodeList) {
						int childNodeListSize = childNodeList.size();
						for (int p = 0; p < childNodeListSize; p++) {
							Node ddNode = childNodeList.elementAt(p);
							// 去空
							if (ddNode.getText().trim().equals("")) {
								continue;
							}
							if (ddNode instanceof LinkTag) {
								LinkTag linkTag = (LinkTag) ddNode;
								if (Constant.TAG_LINK_ATTRIBUTE_CLASS_GRADE_SCORE
										.equals(linkTag.getAttribute("class"))) {
									Node ltfc = linkTag.getFirstChild();
									if (null != ltfc) {
										video.setRate(Float.valueOf(
												ltfc.getText()).floatValue());
									}
									break;
								}
							}
						}
					}
				} else if (Constant.TAG_DD_ATTRIBUTE_CLASS_VIDEO_INFORMATION
						.equals(ddTag.getAttribute("class"))) {
					NodeList childNodeList = childNode.getChildren();
					if (null != childNodeList) {
						int childNodeListSize = childNodeList.size();
						int flag = 0;
						List<String> list = new ArrayList<String>();
						for (int p = 0; p < childNodeListSize; p++) {
							Node ddNode = childNodeList.elementAt(p);
							// 去空
							if (ddNode.getText().trim().equals("")) {
								continue;
							}
							if (ddNode instanceof EmTag) {
								Node emfc = ddNode.getFirstChild();
								if (null != emfc) {
									if (Constant.TAG_EM_VALUE_CATEGORY
											.equals(emfc.getText())) {
										flag = 1;
									} else if (Constant.TAG_EM_VALUE_STARRING
											.equals(emfc.getText())) {
										flag = 2;
									} else if (Constant.TAG_EM_VALUE_YEAR
											.equals(emfc.getText())) {
										flag = 3;
									}
								}
							} else if (ddNode instanceof LinkTag) {
								Node lnkfc = ddNode.getFirstChild();
								if (null != lnkfc) {
									if (flag == 1) {
										video.setCategory(lnkfc.getText());
									} else if (flag == 2) {
										// String tp = lnkfc.getText();
										list.add(lnkfc.getText());
									} else if (flag == 3) {
										video.setYear(Integer.valueOf(
												lnkfc.getText()).intValue());
									}
								}
							}
						}
						if (flag == 2) {
							video.setStarring(StringUtils.join(list.toArray(),
									","));
						}

					}
				}
			}
			// traversal(childNode, video, level + 1);
		}
	}

	/**
	 * 从html文件获取电影的数量
	 * 
	 * @param filePath
	 * @return
	 */
	public static int getTotalByFile(String filePath) {
		int total = -1;
		Parser parser;
		try {
			parser = new Parser();
			parser.setResource(filePath);
			// 找到class="blue"的em
			NodeFilter filter = new HasAttributeFilter("class", "blue");
			NodeList nodes = parser.extractAllNodesThatMatch(filter);

			if (nodes != null && nodes.size() > 0) {
				Node textnode = (Node) nodes.elementAt(0);
				total = Integer.valueOf(textnode.getNextSibling().getText())
						.intValue();
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}

		return total;
	}

	// public static void traversal(Node node, Video video, int level) {
	// System.out.println("level : " + level);
	// NodeList nodeList = node.getChildren();
	// if (nodeList == null) {
	// return;
	// }
	// int size = nodeList.size();
	// for (int j = 0; j < size; j++) {
	// Node childNode = nodeList.elementAt(j);
	// // 去空
	// if (childNode.getText().trim().equals("")) {
	// continue;
	// }
	// System.out.println("getText" + j + ":" + childNode.getText());
	// // if (childNode instanceof DtTag) {
	// //// DtTag dtTag = (DtTag) childNode;
	// // Node cn = childNode.getFirstChild();
	// // if(null != cn){
	// // if (childNode instanceof DtTag) {
	// //
	// // }
	// // }
	// // }
	//
	//
	// if (childNode instanceof ImageTag) {
	// ImageTag imgTag = (ImageTag) childNode;
	// // System.out.println("image : " +
	// //
	// imgTagSystem.out.println("=================================================");.getImageURL());
	// // 获取data-original的属性值，从而获得影片封面
	// System.out.println("image-data-original : "
	// + imgTag.getAttribute("data-original"));
	// video.setImageSource(imgTag.getAttribute("data-original"));
	// }
	// if (childNode instanceof LinkTag) {
	// LinkTag linkTag = (LinkTag) childNode;
	// if (!Constant.URL_STRING_JAVASCRIPT.equals(linkTag
	// .getAttribute("href"))) {
	// System.out.println("link : " + linkTag.getLink());
	// // video.setUrl(Constant.URL_XINYINGBA + linkTag.getLink());
	// // video.setTitle(linkTag.getAttribute("title"));
	//
	// }
	// // if(!Constant.URL_STRING_JAVASCRIPT.equals(linkTag.getLink())){
	// // System.out.println("link : " + linkTag.getLink());
	// // }
	//
	// }
	// traversal(childNode,video,level+1);
	// }
	// System.out.println("=================================================");
	//
	// }

}
