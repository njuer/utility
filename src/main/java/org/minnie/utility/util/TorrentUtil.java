package org.minnie.utility.util;

import java.io.File;
import java.io.IOException;

import org.eclipse.bittorrent.TorrentFile;


public class TorrentUtil {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String path = "‪D:\\172835bbl8lyxyghqrztrs.torrent";
		String path = "‪D:\\aaa.torrent";

		TorrentFile file;
		try {
			file = new TorrentFile(new File(path));
			
			String[] strs = file.getFilenames();
			long[] longs = file.getLengths();
			System.out.println(strs.length + " " + longs.length);

			for (int i = 0; i < strs.length; i++) {
				System.err.println(strs[i] + " --->  " + longs[i]);
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}
