package org.minnie.utility.xinyingba;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
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

public class App {

	public static List<Video> movieList = new ArrayList<Video>();
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// System.out.println(getTotalByFile(Constant.URL_XINYINGBA_MOVIE_2014_FILE));
		// getMovieListByFile(Constant.URL_XINYINGBA_MOVIE_2014_FILE);
		// System.out.println(getTotal(null, 2014,
		// Constant.URL_XINYINGBA_MOVIE_2014));
		
//		getMovieList(null, 2014, Constant.URL_XINYINGBA_MOVIE_2014);
//		MysqlDatabseHelper.batchAddVideo(movieList, "ent_movie");
		MysqlDatabseHelper.batchAddVideo1();
	}

	/**
	 * 获取某类电影的数量
	 * 
	 * @param category
	 * @param year
	 * @param url
	 * @return
	 */
	public static int getTotal(String category, int year, String url) {
		int total = -1;
		Parser parser;
		try {
			parser = new Parser(
					(HttpURLConnection) (new URL(url)).openConnection());
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

	public static void getMovieList(String category, int year, String url) {

		// 注册使用DlTag
		PrototypicalNodeFactory factory = new PrototypicalNodeFactory();
		factory.registerTag(new DlTag());
		factory.registerTag(new DtTag());
		factory.registerTag(new DdTag());
		factory.registerTag(new EmTag());

		Pattern pattern = Pattern.compile(Constant.REG_DL_ID);

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
							video.setNumber(Integer.valueOf(matcher.group()).intValue());
						}
					}
					traversal(node, video, 1);
//					System.out.println(video.toString());
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
//		System.out.println("level : " + level);
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
//			System.out.println("getText" + j + ":" + childNode.getText());
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
					video.setIntroduction(childNode.getFirstChild().getText());
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
								if(Constant.TAG_LINK_ATTRIBUTE_CLASS_GRADE_SCORE.equals(linkTag.getAttribute("class"))){
									 video.setRate(Float.valueOf(linkTag.getFirstChild().getText()).floatValue());
									break;
								}
							}
						}
					}
				} else if(Constant.TAG_DD_ATTRIBUTE_CLASS_VIDEO_INFORMATION
						.equals(ddTag.getAttribute("class"))){
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
								if(Constant.TAG_EM_VALUE_CATEGORY.equals(ddNode.getFirstChild().getText())){
									flag = 1;
								} else if (Constant.TAG_EM_VALUE_STARRING.equals(ddNode.getFirstChild().getText())){
									flag = 2;
								} else if (Constant.TAG_EM_VALUE_YEAR.equals(ddNode.getFirstChild().getText())){
									flag = 3;
								}
							} else if (ddNode instanceof LinkTag) {
								if(flag == 1){
									video.setCategory(ddNode.getFirstChild().getText());
								} else if(flag == 2){
									String tp = ddNode.getFirstChild().getText();
									list.add(tp);
//									System.out.println("<a>: " + tp);
								} else if(flag == 3){
									video.setYear(Integer.valueOf(ddNode.getFirstChild().getText()).intValue());
								}
								
							}
						}
						if(flag == 2){
							video.setStarring(StringUtils.join(list.toArray(),","));
						}

					}
				}
			}
//			traversal(childNode, video, level + 1);
		}
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
