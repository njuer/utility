package org.minnie.utility.module.torrent;

import org.minnie.utility.parser.HttpSimulation;
import org.minnie.utility.parser.JsoupHtmlParser;

public class BitTorrentFactoryApp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		 HttpSimulation hs = new HttpSimulation();
		
		 JsoupHtmlParser.downTorrentsFromBitTorrentFactory(hs, "http://www3.97down.info/qb/file.php/N2SL93G.html","D:/ui");
		 
	}

}
