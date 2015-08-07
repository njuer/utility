/*
 * Java Bittorrent API as its name indicates is a JAVA API that implements the Bittorrent Protocol
 * This project contains two packages:
 * 1. jBittorrentAPI is the "client" part, i.e. it implements all classes needed to publish
 *    files, share them and download them.
 *    This package also contains example classes on how a developer could create new applications.
 * 2. trackerBT is the "tracker" part, i.e. it implements a all classes needed to run
 *    a Bittorrent tracker that coordinates peers exchanges. *
 *
 * Copyright (C) 2007 Baptiste Dubuis, Artificial Intelligence Laboratory, EPFL
 *
 * This file is part of jbittorrentapi-v1.0.zip
 *
 * Java Bittorrent API is free software and a free user study set-up;
 * you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Java Bittorrent API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Java Bittorrent API; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * @version 1.0
 * @author Baptiste Dubuis
 * To contact the author:
 * email: baptiste.dubuis@gmail.com
 *
 * More information about Java Bittorrent API:
 *    http://sourceforge.net/projects/bitext/
 */

package org.minnie.utility.jbittorrent;

import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Representation of a torrent file
 *
 * @author Baptiste Dubuis
 * @version 0.1
 */
public class TorrentFile {

	public String announce; // Tracker的主服务器
	public String comment;// 种子文件的注释
	public String createdBy;// 创建此torrent文件的程序信息
	public long creationDate;// 种子文件建立的时间，UNIX时间戳，是从1970年1月1日00:00:00到现在的秒数。
	public String encoding;// 种子文件的默认编码，比如GB2312，Big5，utf-8等
	 public String saveAs;
	public long pieceLength;// 每个文件块的大小，用Byte计算(分片长度)
    public List<String> announceList;//备选的Tracker服务器列表
    public List<InetSocketAddress> nodes;//一系列ip:端口的列表，是用于连接DHT初始node

	public String name;// 要下载文件的根目录
	public String publisherUrl;// 文件发布者的网址
	public String publisher;// 文件发布者的名字

    /* In case of multiple files torrent, saveAs is the name of a directory
     * and name contains the path of the file to be saved in this directory
     */
    public List<String> path;//目标文件存储相对于name字段表示的文件夹的位置。
    public List<Long> length;//目标文件的大小[单位为byte]

    public byte[] hashAsBinary;
    public String hashAsHex;//哈希值
    public String hashAsUrl;
    public long total_length;

    public List pieceHashAsBinary;
    public List pieceHashAsHex;
    public List pieceHashAsUrl;

    /**
     * Create the TorrentFile object and initiate its instance variables
     */
    public TorrentFile() {
        super();

        announceList = new ArrayList<String>();
        nodes = new ArrayList<InetSocketAddress>();
        path = new ArrayList<String>();
        length = new ArrayList<Long>();

        pieceHashAsBinary = new ArrayList();
        pieceHashAsUrl = new ArrayList();
        pieceHashAsHex = new ArrayList();
        hashAsBinary = new byte[20];
        hashAsUrl = new String();
        hashAsHex = new String();
    }

    /**
     * Print the torrent information in a readable manner.
     * @param detailed Choose if we want a detailed output or not. Detailed
     * output prints the comment, the files list and the pieces hashes while the
     * standard output simply prints tracker url, creator, creation date and
     * info hash
     */
    public void printData(boolean detailed) {
    	//基本信息
    	System.out.println("种子文件编码 : " + this.encoding);
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	//把unix时间戳转换成java时间戳:(Long)creationDate*1000
    	String sd = sdf.format(new Date((Long)this.creationDate*1000));
        System.out.println("种子文件创建时间 : " + sd);
        System.out.println("制作种子软件 : " + this.createdBy);
        System.out.println("Tracker的主服务器 : " + this.announce);
        System.out.println("信息Hash :"+ this.hashAsHex);
        System.out.println("备注信息 :" + this.comment);
//        System.out.println("\t\t" + new String(this.info_hash_as_binary));
//        System.out.println("\t\t" + this.hashAsHex);
//        System.out.println("\t\t" + this.info_hash_as_url);
        if(detailed){
            //分片信息
            System.out.println("分片长度 :" + this.pieceLength);
            System.out.println("要下载文件的根目录 : " + this.name);
            System.out.println("文件发布者 : " + this.publisher);
            System.out.println("文件发布者的网址 : " + this.publisherUrl);

            //包含文件清单
            System.out.println("\n包含文件清单 :");
            for (int i = 0; i < this.length.size(); i++)
                System.out.println("\t- " + this.path.get(i) + " ( " +
                		getFileSize((Long)this.length.get(i)) + " )");
            
            //发布地址
            System.out.println("\n发布地址 :");
            for (int j = 0; j < this.announceList.size(); j++){
            	if( j == 0){
            		System.out.println("\t主要地址- " + this.announceList.get(j));
            	} else {
            		System.out.println("\t其它地址"+j+"- " + this.announceList.get(j));
            	}
            }
            
          //节点信息
            System.out.println("\nNodes :");
            for (int k = 0; k < this.nodes.size(); k++){
            	System.out.println("\t- " + this.nodes.get(k));
            }
            System.out.println("\n");
        }

    }
    
    public String getFileSize(long size){
    	
		int level = 0;
		long temp = size;
		long val = 1;
		while ((temp = temp / 1024) > 0 && level < 5) {
			level++;
			val *= 1024;
		}

		BigDecimal b1 = new BigDecimal(size);
		BigDecimal b2 = new BigDecimal(val);
		
		String unit = "B";
		if(level == 1){
			unit = "KB";
		} else if(level == 2){
			unit = "MB";
		} else if(level == 3){
			unit = "GB";
		} else if(level == 4){
			unit = "TB";
		}
		return b1.divide(b2, 2, BigDecimal.ROUND_HALF_EVEN).toString() + unit;
    }

}
