package org.minnie.utility.module.taobao;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.minnie.utility.parser.HttpSimulation;
import org.minnie.utility.parser.JsoupHtmlParser;
import org.minnie.utility.util.Constant;

public class SaleApp {

//	private static Logger logger = Logger.getLogger(SaleApp.class);
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		PropertyConfigurator.configure(System.getProperty("user.dir") + Constant.SYS_PARAM_FILE);
		
//		HttpSimulation hs = new HttpSimulation();
		
		String action = "http://s.taobao.com/search";
//		String action = "http://s.taobao.com/search?q=ddr3&commend=all&ssid=s5-e&search_type=item&sourceId=tb.index&spm=1.7274553.1997520841.1&initiative_id=tbindexz_20140709";
//		String action = "http://s.taobao.com/search?initiative_id=staobaoz_20140709&js=1&stats_click=search_radio_all%253A1&q=crucial+ddr3+1600+4g";


//		List<NameValuePair> paramPair = new ArrayList<NameValuePair>();
//		paramPair.add(new BasicNameValuePair("initiative_id", "staobaoz_20140709"));
//		paramPair.add(new BasicNameValuePair("js", "1"));
//		paramPair.add(new BasicNameValuePair("stats_click", "search_radio_all%3A1"));
//		paramPair.add(new BasicNameValuePair("q", "crucial+ddr3+1600+4g"));
		
		
//		paramPair.add(new BasicNameValuePair("offlineShop", "false"));
//		paramPair.add(new BasicNameValuePair("sellerUserTag2", "18020085046183936"));
//		paramPair.add(new BasicNameValuePair("isForbidBuyItem", "false"));
//		paramPair.add(new BasicNameValuePair("isUseInventoryCenter", "false"));
//		paramPair.add(new BasicNameValuePair("tmallBuySupport", "true"));
//		paramPair.add(new BasicNameValuePair("itemTags", "450,775,1163,1291,1478,1483,1547,1611,1803,1867,2049,2059,2251,2507,2635,3974,4166,4422,7298,7809,9153,12353,12609,13697,28866"));
//		paramPair.add(new BasicNameValuePair("itemId", "21841507547"));
//		paramPair.add(new BasicNameValuePair("isSecKill", "false"));
//		paramPair.add(new BasicNameValuePair("tgTag", "false"));
//		paramPair.add(new BasicNameValuePair("sellerUserTag", "38866976"));
//		paramPair.add(new BasicNameValuePair("showShopProm", "false"));
//		paramPair.add(new BasicNameValuePair("cartEnable", "true"));
//		paramPair.add(new BasicNameValuePair("queryMemberRight", "true"));
//		paramPair.add(new BasicNameValuePair("addressLevel", "3"));
//		paramPair.add(new BasicNameValuePair("household", "false"));
//		paramPair.add(new BasicNameValuePair("sellerPreview", "false"));
//		paramPair.add(new BasicNameValuePair("isApparel", "false"));
//		paramPair.add(new BasicNameValuePair("notAllowOriginPrice", "false"));
//		paramPair.add(new BasicNameValuePair("isRegionLevel", "true"));
//		paramPair.add(new BasicNameValuePair("isIFC", "false"));
//		paramPair.add(new BasicNameValuePair("service3C", "true"));
//		paramPair.add(new BasicNameValuePair("isAreaSell", "false"));
//		paramPair.add(new BasicNameValuePair("sellerUserTag3", "281474976743552"));
//		paramPair.add(new BasicNameValuePair("sellerUserTag4", "1152930305030504835"));
//		paramPair.add(new BasicNameValuePair("callback", "setMdskip"));

//		String resp = hs.getResponseBodyByPost(action, paramPair, "gbk");
		JsoupHtmlParser.getTaobaoData();
		
//		logger.info(hs.getResponseBodyByGet("www.163.com", "", paramPair));
//		System.out.println(hs.getResponseBodyByPost("http://www.163.com", paramPair, "gbk"));
	}

}
