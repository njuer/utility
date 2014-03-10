package org.minnie.utility.parser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.minnie.utility.module.netease.NeteasePage;
import org.minnie.utility.module.netease.Picture;

/**
 * @author neiplzer@gmail.com
 * @version 2014-3-10 基于Jsoup的Html解析工具类
 */
public class JsoupHtmlParser {

	private static Logger logger = Logger.getLogger(JsoupHtmlParser.class.getName());

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	       try { 
	            Document doc = Jsoup.connect("http://39yss.com/yinshi/yingyang/2014030725664.html").get(); 
//	            System.out.println(doc.title()); 
	            Elements eles = doc.select("div.arcBody"); 
	            for (Element element : eles) {  
//	            	element.select("a").attr("href");
//	                System.out.println(element.select("a").attr("href"));  
	                Elements el = element.select("a");
	                if(null != el){
//	                	Elements e = el.
	                	 System.out.println(el.select("img").attr("src"));
//	                	 System.out.println(el.first().attr("src"));
	                	 break;
	                }
	            }  
	        } catch (IOException e) { 
	            e.printStackTrace(); 
	        } 

	}
	
	public static NeteasePage getNeteasePage(String galleryUrl){
		
		return getNeteasePage(galleryUrl, "textarea[name=gallery-data]");
		
	}
	
	/**
	 * 获取网易图集所有图片的地址
	 * @param galleryUrl	图集地址
	 * @param cssQuery		CSS selector
	 * @return
	 */
	public static NeteasePage getNeteasePage(String galleryUrl, String cssQuery){
		
		Document doc;
		try {
			doc = Jsoup.connect(galleryUrl).get();
			Elements eles = doc.select(cssQuery);
			String json = eles.text();
			
			/**
			 * 如果要转换到的Bean类中含有类似ArrayList、Map、List这样的集合时，
			 * 要对集合进行处理，否则会报“net.sf.ezmorph.bean.MorphDynaBean cannot be cast to XXX”这样的异常。
			 */
			Map <String,Class> listParamMap = new HashMap<String,Class>(); 
			listParamMap.put("list",Picture.class); //我这里的成员名为content 对应的类为ClientInfo.class，具体值要跟据你自己的来定。  
			
			JSONObject jObj = new JSONObject().fromObject(json);
			return (NeteasePage) JSONObject.toBean(jObj, NeteasePage.class, listParamMap);
			
		} catch (IOException e) {
			logger.error("IOException[JsoupHtmlParser->getNeteasePage(String galleryUrl, String cssQuery)]: "+ e.getMessage());
		}
		
		return null;
	}
	
	public static void getTotal39YangShengsuo(String pageUrl){
		
	}

}
