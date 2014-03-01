package org.minnie.utility;

import java.net.HttpURLConnection;
import java.net.URL;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.util.NodeList;

/**
 * Hello world!
 * 
 */
public class App {
	
	public static void main(String[] args) {
		try {
			Parser parser = new Parser(
					(HttpURLConnection) (new URL(
							"http://www.xinyingba.com/dianying/2014-nian.htm"))
							.openConnection());

			// 找到class=video-list gclearfix的div
			NodeFilter filter = new HasAttributeFilter( "class", "video-list gclearfix" );
			NodeList nodes = parser.extractAllNodesThatMatch(filter);


			if (nodes != null) {
				for (int i = 0; i < nodes.size(); i++) {
					Node textnode = (Node) nodes.elementAt(i);

					// 找到<div class="video-list gclearfix">下的子节点<dl></dl>
//					System.out.println("getText["+i+"]:" + textnode.getText());
					NodeList mvList = textnode.getChildren();
					int size = mvList.size();
					for(int j = 0; j < size; j++){
						Node nd = mvList.elementAt(j);
						//去空
						if(nd.getText().trim().equals("")){
							continue;
						}
						System.out.println("getText"+j+":" + nd.getText());
					}

					System.out.println("=================================================");
				}
			}
//			NodeFilter filter = new HasAttributeFilter( "class", "blue" );
//			NodeList nodes = parser.extractAllNodesThatMatch(filter);
//
//
//			if (nodes != null) {
//				for (int i = 0; i < nodes.size(); i++) {
//					Node textnode = (Node) nodes.elementAt(i);
//
//					System.out.println("getText:" + textnode.getText());
//					Node textnode0 = textnode.getNextSibling();
//					if(null != textnode0){
//						System.out.println("tesxt:" + textnode0.getText());
//					}
//
//					System.out.println("=================================================");
//				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
