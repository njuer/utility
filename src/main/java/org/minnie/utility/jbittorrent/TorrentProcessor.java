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

import java.io.*;
import java.util.*;
import java.nio.ByteBuffer;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.MalformedURLException;

import org.minnie.utility.util.Constant;
import org.minnie.utility.util.FileUtil;
import org.minnie.utility.util.StringUtil;

/**
 * 
 * Class enabling to process a torrent file
 * 
 * @author Baptiste Dubuis
 * @version 0.1
 */
public class TorrentProcessor {

	private TorrentFile torrent;

	public TorrentProcessor(TorrentFile torrent) {
		this.torrent = torrent;
	}

	public TorrentProcessor() {
		this.torrent = new TorrentFile();
	}

	/**
	 * Given the path of a torrent, parse the file and represent it as a Map
	 * 
	 * @param filename
	 *            String
	 * @return Map
	 */
	public Map parseTorrent(String filename) {
		return this.parseTorrent(new File(filename));
	}

	/**
	 * Given a File (supposed to be a torrent), parse it and represent it as a
	 * Map
	 * 
	 * @param file
	 *            File
	 * @return Map
	 */
	public Map parseTorrent(File file) {
		try {
			return BDecoder.decode(FileUtil.readBytesFromFile(file));
		} catch (IOException ioe) {
		}
		return null;
	}

	/**
	 * Given a Map, retrieve all useful information and represent it as a
	 * TorrentFile object
	 * 
	 * @param map
	 *            Map
	 * @return TorrentFile
	 */
	public TorrentFile getTorrentFile(Map map) {
		if (map == null)
			return null;
		
		// mandatory key
		if (map.containsKey("announce")){
			this.torrent.announce = new String((byte[]) map.get("announce"));
		} else {
			return null;
		}
			
		// optional key
		if (map.containsKey("comment")) 
			this.torrent.comment = new String((byte[]) map.get("comment"));
		// optional key
		if (map.containsKey("created by"))
			this.torrent.createdBy = new String((byte[]) map.get("created by"));
		// optional key
		if (map.containsKey("creation date"))
			this.torrent.creationDate = (Long) map.get("creation date");
		// optional key
		if (map.containsKey("encoding"))
			this.torrent.encoding = new String((byte[]) map.get("encoding"));

		// Store the info field data
		if (map.containsKey("info")) {
			Map info = (Map) map.get("info");
			try {
				this.torrent.hashAsBinary = StringUtil.hash(BEncoder.encode(info));
				this.torrent.hashAsHex = StringUtil.byteArrayToByteString(this.torrent.hashAsBinary);
				this.torrent.hashAsUrl = StringUtil.byteArrayToURLString(this.torrent.hashAsBinary);
			} catch (IOException ioe) {
				return null;
			}
			if (info.containsKey("name"))
				this.torrent.name = new String((byte[]) info.get("name"));
			if (info.containsKey("publisher"))
				this.torrent.publisher = new String((byte[]) info.get("publisher"));
			if (info.containsKey("publisher-url"))
				this.torrent.publisherUrl = new String((byte[]) info.get("publisher-url"));
			if (info.containsKey("piece length")){
				this.torrent.pieceLength = ((Long) info.get("piece length")).intValue();	
			}else{
				return null;
			}

			if (info.containsKey("pieces")) {
				byte[] piecesHash2 = (byte[]) info.get("pieces");
				if (piecesHash2.length % 20 != 0)
					return null;

				for (int i = 0; i < piecesHash2.length / 20; i++) {
					byte[] temp = StringUtil.subArray(piecesHash2, i * 20, 20);
					this.torrent.pieceHashAsBinary.add(temp);
					this.torrent.pieceHashAsHex.add(StringUtil.byteArrayToByteString(temp));
					this.torrent.pieceHashAsUrl.add(StringUtil.byteArrayToURLString(temp));
				}
			} else
				return null;

			if (info.containsKey("files")) {
				List multFiles = (List) info.get("files");
				this.torrent.total_length = 0;
				int multFileSize = multFiles.size();
				for (int i = 0; i < multFileSize; i++) {
					Map fileMap = (Map)multFiles.get(i);
					Object path = fileMap.get("path");
					if (path instanceof List) {
						List pathList = (List) path;
						StringBuffer ph = new StringBuffer();
						int plSize = pathList.size();
						for(int j = 0; j < plSize; j++){
							if(j > 0){
								ph.append("/");
							}
							Object p = pathList.get(j);
							if (p instanceof byte[]) {
								ph.append(new String((byte[]) p));
							}
						}
						this.torrent.path.add(ph.toString());
					}
					this.torrent.length.add((Long)fileMap.get("length"));
				}
			} else {
				this.torrent.length.add(((Long) info.get("length")));
				this.torrent.total_length = ((Long) info.get("length")).intValue();
				this.torrent.path.add(new String((byte[]) info.get("name")));
			}
		} else{
			return null;
		}
		
		if (map.containsKey("announce-list")) {
			List announceList = (List) map.get("announce-list");
			for(Object announce : announceList){
				if(announce instanceof List){
					List ann = (List) announce;
					for(Object o : ann){
						this.torrent.announceList.add(new String((byte[])o));
					}
				}
			}
		}
		
		if (map.containsKey("nodes")) {
			List announceList = (List) map.get("nodes");
			for(Object announce : announceList){
				if(announce instanceof List){
					List al = (List) announce;
					if(2 == al.size()){
						InetSocketAddress isa = new InetSocketAddress(new String((byte[])al.get(0)),((Long)al.get(1)).intValue());
						this.torrent.nodes.add(isa);
					}
				}
			}
		}
			
		return this.torrent;
	}

	/**
	 * Sets the TorrentFile object of the Publisher equals to the given one
	 * 
	 * @param torrent
	 *            TorrentFile
	 */
	public void setTorrent(TorrentFile torrent) {
		this.torrent = torrent;
	}

	/**
	 * Updates the TorrentFile object according to the given parameters
	 * 
	 * @param url
	 *            The announce url
	 * @param pLength
	 *            The length of the pieces of the torrent
	 * @param comment
	 *            The comments for the torrent
	 * @param encoding
	 *            The encoding of the torrent
	 * @param fileName
	 *            The path of the file to be added to the torrent
	 */
	public void setTorrentData(String url, int pLength, String comment, String encoding, String fileName) {
		this.torrent.announce = url;
		this.torrent.pieceLength = pLength * 1024;
		this.torrent.createdBy = Constant.CLIENT;
		this.torrent.comment = comment;
		this.torrent.creationDate = System.currentTimeMillis();
		this.torrent.encoding = encoding;
		this.addFile(fileName);
	}

	/**
	 * Updates the TorrentFile object according to the given parameters
	 * 
	 * @param url
	 *            The announce url
	 * @param pLength
	 *            The length of the pieces of the torrent
	 * @param comment
	 *            The comments for the torrent
	 * @param encoding
	 *            The encoding of the torrent
	 * @param name
	 *            The name of the directory to save the files in
	 * @param fileNames
	 *            The path of the file to be added to the torrent
	 * @throws java.lang.Exception
	 */
	public void setTorrentData(String url, int pLength, String comment, String encoding, String name, List fileNames) throws Exception {
		this.torrent.announce = url;
		this.torrent.pieceLength = pLength * 1024;
		this.torrent.comment = comment;
		this.torrent.createdBy = Constant.CLIENT;
		this.torrent.creationDate = System.currentTimeMillis();
		this.torrent.encoding = encoding;
//		this.torrent.saveAs = name;
		this.addFiles(fileNames);
	}

	/**
	 * Sets the announce url of the torrent
	 * 
	 * @param url
	 *            String
	 */
	public void setAnnounceURL(String url) {
		this.torrent.announce = url;
	}

	/**
	 * Sets the pieceLength
	 * 
	 * @param length
	 *            int
	 */
	public void setPieceLength(int length) {
		this.torrent.pieceLength = length * 1024;
	}

//	/**
//	 * Sets the directory the files have to be saved in (in case of multiple
//	 * files torrent)
//	 * 
//	 * @param name
//	 *            String
//	 */
//	public void setName(String name) {
//		this.torrent.saveAs = name;
//	}

	/**
	 * Sets the comment about this torrent
	 * 
	 * @param comment
	 *            String
	 */
	public void setComment(String comment) {
		this.torrent.comment = comment;
	}

	/**
	 * Sets the creator of the torrent. This should be the client name and
	 * version
	 * 
	 * @param creator
	 *            String
	 */
	public void setCreator(String creator) {
		this.torrent.createdBy = creator;
	}

	/**
	 * Sets the time the torrent was created
	 * 
	 * @param date
	 *            long
	 */
	public void setCreationDate(long date) {
		this.torrent.creationDate = date;
	}

	/**
	 * Sets the encoding of the torrent
	 * 
	 * @param encoding
	 *            String
	 */
	public void setEncoding(String encoding) {
		this.torrent.encoding = encoding;
	}

	/**
	 * Add the files in the list to the torrent
	 * 
	 * @param list
	 *            A list containing the File or String object representing the
	 *            files to be added
	 * @return int The number of files that have been added
	 * @throws Exception
	 */
	public int addFiles(List list) throws Exception {
		return this.addFiles(list.toArray());
	}

	/**
	 * Add the files in the list to the torrent
	 * 
	 * @param file
	 *            The file to be added
	 * @return int The number of file that have been added
	 * @throws Exception
	 */
	public int addFile(File file) {
		return this.addFiles(new File[] { file });
	}

	/**
	 * Add the files in the list to the torrent
	 * 
	 * @param fileName
	 *            The path of the file to be added
	 * @return int The number of file that have been added
	 * @throws Exception
	 */
	public int addFile(String fileName) {
		return this.addFiles(new String[] { fileName });
	}

	/**
	 * Add the files in the list to the torrent
	 * 
	 * @param fileNames
	 *            An array containing the files to be added
	 * @return int The number of files that have been added
	 * @throws Exception
	 */
	public int addFiles(Object[] fileNames) {
		int nbFileAdded = 0;

		if (this.torrent.total_length == -1)
			this.torrent.total_length = 0;

		for (int i = 0; i < fileNames.length; i++) {
			File f = null;
			if (fileNames[i] instanceof String)
				f = new File((String) fileNames[i]);
			else if (fileNames[i] instanceof File)
				f = (File) fileNames[i];
			if (f != null)
				if (f.exists()) {
					this.torrent.total_length += f.length();
					this.torrent.path.add(f.getPath());
					this.torrent.length.add(new Long(f.length()));
					nbFileAdded++;
				}
		}
		return nbFileAdded;
	}

	/**
	 * Generate the SHA-1 hashes for the file in the torrent in parameter
	 * 
	 * @param torrent
	 *            TorrentFile
	 */
	public void generatePieceHashes(TorrentFile torrent) {
		ByteBuffer bb = ByteBuffer.allocate((int)torrent.pieceLength);
//		int index = 0;
		long total = 0;
		torrent.pieceHashAsBinary.clear();
		for (int i = 0; i < torrent.path.size(); i++) {
			total += torrent.length.get(i);
			File f = new File((String) torrent.path.get(i));
			if (f.exists()) {
				try {
					FileInputStream fis = new FileInputStream(f);
					int read = 0;
					byte[] data = new byte[(int)torrent.pieceLength];
					while ((read = fis.read(data, 0, bb.remaining())) != -1) {
						bb.put(data, 0, read);
						if (bb.remaining() == 0) {
							torrent.pieceHashAsBinary.add(StringUtil.hash(bb.array()));
							bb.clear();
						}
					}
				} catch (FileNotFoundException fnfe) {
				} catch (IOException ioe) {
				}
			}
		}
		if (bb.remaining() != bb.capacity())
			torrent.pieceHashAsBinary.add(StringUtil.hash(StringUtil.subArray(bb.array(), 0, bb.capacity() - bb.remaining())));
	}

	/**
	 * Generate the SHA-1 hashes for the files in the current object TorrentFile
	 */
	public void generatePieceHashes() {
		this.generatePieceHashes(this.torrent);
	}

	/**
	 * Generate the bytes of the bencoded TorrentFile data
	 * 
	 * @param torrent
	 *            TorrentFile
	 * @return byte[]
	 */
	public byte[] generateTorrent(TorrentFile torrent) {
		SortedMap map = new TreeMap();
		map.put("announce", torrent.announce);
		if (torrent.comment.length() > 0)
			map.put("comment", torrent.comment);
		if (torrent.creationDate >= 0)
			map.put("creation date", torrent.creationDate);
		if (torrent.createdBy.length() > 0)
			map.put("created by", torrent.createdBy);

		SortedMap info = new TreeMap();
		if (torrent.path.size() == 1) {
			info.put("length", torrent.length.get(0));
			info.put("name", new File((String) torrent.path.get(0)).getName());
		} else {
			if (!torrent.saveAs.matches(""))
				info.put("name", torrent.saveAs);
			else
				info.put("name", "noDirSpec");
			ArrayList files = new ArrayList();
			for (int i = 0; i < torrent.path.size(); i++) {
				SortedMap file = new TreeMap();
				file.put("length", torrent.length.get(i));
				String[] path = ((String) torrent.path.get(i)).split("\\\\");
				File f = new File((String) (torrent.path.get(i)));

				ArrayList pathList = new ArrayList(path.length);
				for (int j = (path.length > 1) ? 1 : 0; j < path.length; j++)
					pathList.add(path[j]);
				file.put("path", pathList);
				files.add(file);
			}
			info.put("files", files);
		}
		info.put("piece length", torrent.pieceLength);
		byte[] pieces = new byte[0];
		for (int i = 0; i < torrent.pieceHashAsBinary.size(); i++)
			pieces = StringUtil.concat(pieces, (byte[]) torrent.pieceHashAsBinary.get(i));
		info.put("pieces", pieces);
		map.put("info", info);
		try {
			byte[] data = BEncoder.encode(map);
			return data;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Generate the bytes for the current object TorrentFile
	 * 
	 * @return byte[]
	 */
	public byte[] generateTorrent() {
		return this.generateTorrent(this.torrent);
	}

	/**
	 * Returns the local TorrentFile in its current state
	 * 
	 * @return TorrentFile
	 */
	public TorrentFile getTorrent() {
		return this.torrent;
	}

}
