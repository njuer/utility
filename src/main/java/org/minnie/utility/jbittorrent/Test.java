package org.minnie.utility.jbittorrent;

import java.util.Map;

/**
 * 代码来源：jbittorrentapi-v1.0
 * 		https://github.com/athy/jtorrentAPI
 * @author admin
 *
 */
public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TorrentProcessor processor = new TorrentProcessor();
		Map map = processor.parseTorrent("C:/Users/admin/Downloads/1024gc.com_253.(E-BODY)(EBOD-411)感度200倍_ゆっくりねっとりスローセックス6時間_周防ゆきこ_1.torrent");
//		Map map = processor.parseTorrent("C:/Users/admin/Downloads/1024gc.com_TPimage 系列套圖NO.0236--NO.0242.torrent");
//		Map map = processor.parseTorrent("C:/Users/admin/Downloads/MICROSOFT.WINDOWS.8.ENTERPRISE.N.RTM.X64.VOLUME.ENGLISH.BOOTABLE_ISO-CTRLSOFT.torrent");
		TorrentFile torrent = processor.getTorrentFile(map);
//		System.out.println(torrent.toString());
		torrent.printData(true);
		
//		long t = 2152145089l;
//		
//		System.out.println(t/1024/1024/1024/1024);
	}

}
