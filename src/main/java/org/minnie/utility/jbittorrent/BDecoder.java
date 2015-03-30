/*
 * BeDecoder.java
 *
 * Created on May 30, 2003, 2:44 PM
 * Copyright (C) 2003, 2004, 2005, 2006 Aelitis, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * AELITIS, SAS au capital de 46,603.30 euros
 * 8 Allee Lenotre, La Grille Royale, 78600 Le Mesnil le Roi, France.
 */

package org.minnie.utility.jbittorrent;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.minnie.utility.util.Constant;

/**
 * A set of utility methods to decode a bencoded array of byte into a Map.
 * integer are represented as Long, String as byte[], dictionnaries as Map, and
 * list as List.
 * 
 * @author TdC_VgA
 * 
 */
public class BDecoder {

	private boolean recovery_mode;

	public static Map decode(byte[] data) throws IOException {
		return (new BDecoder().decodeByteArray(data));
	}

	public static Map decode(BufferedInputStream is) throws IOException {
		return (new BDecoder().decodeStream(is));
	}

	public BDecoder() {
		
	}

	public Map decodeByteArray(byte[] data) throws IOException {
		return (decode(new ByteArrayInputStream(data)));
	}

	public Map decodeStream(BufferedInputStream data) throws IOException {
		Object res = decodeInputStream(data, 0);

		if (res == null) {
			throw (new IOException("BDecoder: zero length file"));
		} else if (!(res instanceof Map)) {
			throw (new IOException("BDecoder: top level isn't a Map"));
		}

		return ((Map) res);
	}

	private Map decode(ByteArrayInputStream data) throws IOException {
		
		Object res = decodeInputStream(data, 0);

		if (res == null) {
			throw (new IOException("BDecoder: zero length file"));
		} else if (!(res instanceof Map)) {
			throw (new IOException("BDecoder: top level isn't a Map"));
		}

		return ((Map) res);
	}

	private Object decodeInputStream(InputStream bais, int nesting) throws IOException {
		
		if (nesting == 0 && !bais.markSupported()) {
			throw new IOException("InputStream must support the mark() method");
		}

		// set a mark
		bais.mark(Integer.MAX_VALUE);

		// read a byte
		int tempByte = bais.read();

		// decide what to do
		switch (tempByte) {
		case 'd':
			// create a new dictionary object
			Map tempMap = new HashMap();

			try {
				// get the key
				byte[] tempByteArray = null;

				while ((tempByteArray = (byte[]) decodeInputStream(bais, nesting + 1)) != null) {

					// decode some more
					Object value = decodeInputStream(bais, nesting + 1);

					// add the value to the map
					CharBuffer cb = Constant.BYTE_CHARSET.decode(ByteBuffer.wrap(tempByteArray));

					String key = new String(cb.array(), 0, cb.limit());
//					String tempKey = new
					tempMap.put(key, value);
				}

				bais.mark(Integer.MAX_VALUE);
				tempByte = bais.read();
				bais.reset();
				if (nesting > 0 && tempByte == -1) {
					throw (new IOException("BDecoder: invalid input data, 'e' missing from end of dictionary"));
				}
			} catch (Throwable e) {

				if (!recovery_mode) {
					if (e instanceof IOException) {
						throw ((IOException) e);
					}

					throw (new IOException(e.toString()));
				}
			}

			// return the map
			return tempMap;

		case 'l':

			// create the list
			List tempList = new ArrayList();

			try {
				// create the key
				Object tempElement = null;
				while ((tempElement = decodeInputStream(bais, nesting + 1)) != null) {
					// add the element
					tempList.add(tempElement);
				}

				bais.mark(Integer.MAX_VALUE);
				tempByte = bais.read();
				bais.reset();
				if (nesting > 0 && tempByte == -1) {

					throw (new IOException("BDecoder: invalid input data, 'e' missing from end of list"));
				}
			} catch (Throwable e) {

				if (!recovery_mode) {
					if (e instanceof IOException) {
						throw ((IOException) e);
					}
					throw (new IOException(e.toString()));
				}
			}

			// return the list
			return tempList;

		case 'e':
		case -1:
			return null;
		case 'i':
			return new Long(getNumberFromStream(bais, 'e'));
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			// move back one
			bais.reset();

			// get the string
			return getByteArrayFromStream(bais);

		default: {

			int rem_len = bais.available();

			if (rem_len > 256) {
				rem_len = 256;
			}

			byte[] rem_data = new byte[rem_len];

			bais.read(rem_data);

			throw (new IOException("BDecoder: unknown command '" + tempByte + ", remainder = " + new String(rem_data)));
		}
		}
	}

	private long getNumberFromStream(InputStream bais, char parseChar) throws IOException {
		StringBuffer sb = new StringBuffer(3);

		int tempByte = bais.read();
		while ((tempByte != parseChar) && (tempByte >= 0)) {
			sb.append((char) tempByte);
			tempByte = bais.read();
		}

		// are we at the end of the stream?
		if (tempByte < 0) {
			return -1;
		}

		return Long.parseLong(sb.toString());
	}

	// This one causes lots of "Query Information" calls to the filesystem
	private long getNumberFromStreamOld(InputStream bais, char parseChar) throws IOException {
		int length = 0;

		// place a mark
		bais.mark(Integer.MAX_VALUE);

		int tempByte = bais.read();
		while ((tempByte != parseChar) && (tempByte >= 0)) {
			tempByte = bais.read();
			length++;
		}

		// are we at the end of the stream?
		if (tempByte < 0) {
			return -1;
		}

		// reset the mark
		bais.reset();

		// get the length
		byte[] tempArray = new byte[length];
		int count = 0;
		int len = 0;

		// get the string
		while (count != length && (len = bais.read(tempArray, count, length - count)) > 0) {
			count += len;
		}

		// jump ahead in the stream to compensate for the :
		bais.skip(1);

		// return the value

		CharBuffer cb = Constant.DEFAULT_CHARSET.decode(ByteBuffer.wrap(tempArray));

		String str_value = new String(cb.array(), 0, cb.limit());

		return Long.parseLong(str_value);
	}

	private byte[] getByteArrayFromStream(InputStream bais) throws IOException {
		int length = (int) getNumberFromStream(bais, ':');

		if (length < 0) {
			return null;
		}

		// note that torrent hashes can be big (consider a 55GB file with 2MB
		// pieces
		// this generates a pieces hash of 1/2 meg

		if (length > 8 * 1024 * 1024) {
			throw (new IOException("Byte array length too large (" + length + ")"));
		}

		byte[] tempArray = new byte[length];
		int count = 0;
		int len = 0;
		// get the string
		while (count != length && (len = bais.read(tempArray, count, length - count)) > 0) {
			count += len;
		}

		if (count != tempArray.length) {
			throw (new IOException("BDecoder::getByteArrayFromStream: truncated"));
		}

		return tempArray;
	}

	public void setRecoveryMode(boolean r) {
		recovery_mode = r;
	}

	private void print(PrintWriter writer, Object obj) {
		print(writer, obj, "", false);
	}

	private void print(PrintWriter writer, Object obj, String indent, boolean skip_indent) {
		String use_indent = skip_indent ? "" : indent;

		if (obj instanceof Long) {
			writer.println(use_indent + obj);
		} else if (obj instanceof byte[]) {

			byte[] b = (byte[]) obj;
			int len = b.length;
			String temp = new String(b);
//			System.out.println(temp);

			if (len == 20) {
				writer.println(use_indent + " { " + b + " }");
			} else if (len < 64) {
				writer.println(new String(b));
			} else {
				writer.println("[byte array length " + b.length);
			}

		} else if (obj instanceof String) {
			writer.println(use_indent + obj);
		} else if (obj instanceof List) {
			List l = (List) obj;
			writer.println(use_indent + "[");

			for (int i = 0; i < l.size(); i++) {
				writer.print(indent + "  (" + i + ") ");
				print(writer, l.get(i), indent + "    ", true);
			}
			writer.println(indent + "]");
		} else {
			Map m = (Map) obj;
			
			Iterator it = m.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				if (key.length() > 256) {
					writer.print(indent + key.substring(0, 256) + "... = ");
				} else {
					writer.print(indent + key + " = ");
				}
				print(writer, m.get(key), indent + "  ", true);
			}
		}
	}
	

	private void print2(Object obj) {
//		String use_indent = skip_indent ? "" : indent;

		if (obj instanceof Long) {
//			writer.println(use_indent + obj);
			System.out.println("Long : " + obj);
		} else if (obj instanceof byte[]) {

			byte[] b = (byte[]) obj;
			int len = b.length;
			String temp = new String(b);
//			System.out.println(temp);
			System.out.println("byte[] : " + temp);
			if (len == 20) {
//				writer.println(use_indent + " { " + b + " }");
			} else if (len < 64) {
//				writer.println(new String(b));
			} else {
//				writer.println("[byte array length " + b.length);
			}

		} else if (obj instanceof String) {
//			writer.println(use_indent + obj);
			System.out.println("String : " + obj);
		} else if (obj instanceof List) {
			List l = (List) obj;
//			writer.println(use_indent + "[");

			for (int i = 0; i < l.size(); i++) {
//				writer.print(indent + "  (" + i + ") ");
//				print(writer, l.get(i), indent + "    ", true);
				print2(l.get(i));
			}
//			writer.println(indent + "]");
		} else {
			Map m = (Map) obj;
			
			Iterator it = m.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				System.out.println("else : " + key);
				if (key.length() > 256) {
//					writer.print(indent + key.substring(0, 256) + "... = ");
				} else {
//					writer.print(indent + key + " = ");
				}
//				print(writer, m.get(key), indent + "  ", true);
				print2(m.get(key));
			}
		}
	}

	private static void print(File f, File output) {
		try {
			BDecoder decoder = new BDecoder();

			decoder.setRecoveryMode(false);
			
			Object obj = decoder.decodeStream(new BufferedInputStream(new FileInputStream(f)));
		    Map<String, Object> map = (Map<String, Object>) obj;
		    
		    Object createdBy = map.get("created by");
		    if (createdBy instanceof byte[]){
		    	System.out.println("created by = " + new String((byte[])createdBy));
		    }
		    
		    Object announce = map.get("announce");
		    if (announce instanceof byte[]){
		    	System.out.println("announce = " + new String((byte[])announce));
		    }
		    
		    Object encoding = map.get("encoding");
		    if (encoding instanceof byte[]){
		    	System.out.println("encoding = " + new String((byte[])encoding));
		    }
		    
		    Object comment = map.get("comment");
		    if (comment instanceof byte[]){
		    	System.out.println("comment = " + new String((byte[])comment));
		    }
		    
		    Object creationDate = map.get("creation date");
		    if (creationDate instanceof Long){
		    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    	//把unix时间戳转换成java时间戳:(Long)creationDate*1000
		    	String sd = sdf.format(new Date((Long)creationDate*1000));
		    	System.out.println("creation date = " + sd);
		    }
		    
		    Object info = map.get("info");
		    if (info instanceof List) {
				List list = (List) info;
				for (int i = 0; i < list.size(); i++) {
					System.out.println("list = " + list.get(i));
				}
			} else if (info instanceof Map) {
				Map infoMap = (Map) info;
				
//				Iterator it = infoMap.keySet().iterator();
//				while (it.hasNext()) {
//					String key = (String) it.next();
//////					System.out.println("else : " + key);
////					if (key.length() > 256) {
//////						writer.print(indent + key.substring(0, 256) + "... = ");
////					} else {
//////						writer.print(indent + key + " = ");
////					}
//					System.out.println("key = " + key + " , value = " + infoMap.get(key));
//				}
			    Object name = infoMap.get("name");
			    if (name instanceof byte[]){
			    	System.out.println("name = " + new String((byte[])name));
			    }
			    
			    Object publisher = infoMap.get("publisher");
			    if (publisher instanceof byte[]){
			    	System.out.println("publisher = " + new String((byte[])publisher));
			    }
			    
			    Object publisherUrl = infoMap.get("publisher-url");
			    if (publisherUrl instanceof byte[]){
			    	System.out.println("publisher-url = " + new String((byte[])publisherUrl));
			    }
			    
//				// 乱码,不使用
//			    Object pieces = infoMap.get("pieces");
//			    if (pieces instanceof byte[]){
//			    	System.out.println("pieces = " + new String((byte[])pieces));
//			    }
			    Object pieceLength = infoMap.get("piece length");
			    if (pieceLength instanceof Long){
			    	System.out.println("piece length = " + (Long)pieceLength);
			    }
			    Object files = infoMap.get("files");
			    if (files instanceof List){
					List list = (List) files;
					for (int i = 0; i < list.size(); i++) {
//						System.out.println("list = " + list.get(i));
						Object file = list.get(i);
						if(file instanceof Map){
							Map fileMap = (Map)file;
							Object path = fileMap.get("path");
							if(path instanceof String){
//								System.out.println("path = " + (String)path);
								System.out.println("String");
							} else if(path instanceof byte[]){
								System.out.println("byte[]");
							} else if(path instanceof Map){
								System.out.println("Map");
							} else if(path instanceof List){
//								System.out.println("List");
								List pathList = (List) path;
//								System.out.println("size = " + pathList.size());
								for(Object o : pathList){
									if(o instanceof byte[]){
										System.out.println(new String((byte[])o));
									}
								}
							}
							System.out.println("length = " + fileMap.get("length"));
						}
					}			    
				}
			}
//		    if (info instanceof byte[]){
//		    	System.out.println("info = " + new String((byte[])publisher));
//		    }
//		    System.out.println("info = " + map.get("info"));
//		    System.out.println("nodes = " + map.get("nodes"));
//		    System.out.println("announce-list = " + map.get("announce-list"));

		    
//			Key = created by, Value = [B@26966110
//			                           Key = nodes, Value = [[[B@42f66abc, 59290], [[B@3fd97efc, 59420], [[B@601ff323, 59821], [[B@5193b022, 46322], [[B@3710b205, 25641], [[B@29178281, 61245]]
//			                           Key = announce, Value = [B@4678f83a
//			                           Key = encoding, Value = [B@2ff0cbfb
//			                           Key = announce-list, Value = [[[B@69dfe453], [[B@6a073b72], [[B@cfefc0], [[B@538526aa], [[B@4aad8dbc], [[B@483457f1], [[B@6479b43f], [[B@7228c7a1], [[B@534a5594], [[B@35389244], [[B@150ac9a8], [[B@773d3f62], [[B@560c7816], [[B@4ca0187c], [[B@22a79c31], [[B@29ff66bd], [[B@3eb217d5], [[B@1e3ac11b], [[B@235be31e], [[B@659bd8b4], [[B@5ab04589], [[B@6aa553e2], [[B@c265121], [[B@279853c2], [[B@1a2f9dd], [[B@72c21d01], [[B@25fe6783], [[B@613ddcf1], [[B@24748417], [[B@5d8c14b3], [[B@17a323c0], [[B@57801e5f], [[B@1c4a1bda], [[B@456a93b8], [[B@580838a2], [[B@3be40d5], [[B@3f9261de], [[B@2357566d], [[B@3aeb203b], [[B@22911fb5], [[B@65b8b5cd], [[B@41a7d9e7], [[B@72d2ee5d], [[B@761f568f], [[B@36c02df], [[B@2830803a], [[B@8327473], [[B@287b2e39], [[B@2f17b4f2], [[B@3d689405], [[B@1ccdf3c2], [[B@153e5454], [[B@2f1261b1], [[B@5fcbc39b], [[B@3a97263f], [[B@19501026], [[B@57543bc5], [[B@5b202f4d], [[B@1e0196f8], [[B@7a5e832b], [[B@52f5bad0], [[B@2bbd83d], [[B@7a718e31], [[B@352e71c4], [[B@7c2f1622], [[B@775651df], [[B@441944ae], [[B@56ec1e6f], [[B@27c235fe], [[B@3f66cb16], [[B@759f31de], [[B@26832226], [[B@6b37008a], [[B@68049b03], [[B@65d174f], [[B@49a546cc], [[B@1cb20da], [[B@3ed9c921], [[B@4c23b5c3], [[B@3c723c42], [[B@6fd2300e], [[B@5051207c], [[B@7e2bd615], [[B@6ff43d69], [[B@bebf1eb], [[B@2f57816a], [[B@19f16e6e], [[B@39890510], [[B@52ab7af2], [[B@7814d044], [[B@1e755df3], [[B@2b6b0c24], [[B@ea25c1], [[B@c54a25f], [[B@870114c], [[B@757f98e7], [[B@c0fa1f5], [[B@5651e202], [[B@19cd1d94], [[B@6950ecc7], [[B@3fe932d5], [[B@67085eba], [[B@56833a2e], [[B@4160ce61], [[B@3a8d63cf], [[B@1893c911], [[B@e7587b2], [[B@461fbe88], [[B@4e15f6af], [[B@3f68336], [[B@2d205042], [[B@24753433], [[B@ab612f8], [[B@37975d46], [[B@326cbb2d], [[B@74a138], [[B@690463c3], [[B@1f31cd12], [[B@7e8e68bd], [[B@377e9134], [[B@566f0962], [[B@13d12d43], [[B@9f293df], [[B@14ca1a93], [[B@6096b38e], [[B@45e6612c], [[B@17f5b38e], [[B@3df3bec], [[B@34d704f0], [[B@108543aa], [[B@1d1d565f], [[B@711185e7], [[B@356e3aaf], [[B@57ced290], [[B@4b6218f9], [[B@1555aa19], [[B@2b125a40], [[B@41e335d7], [[B@2be3d80c], [[B@7b7d1256]]
//			                           Key = comment, Value = [B@503f0b70
//			                           Key = creation date, Value = 1422979337
//			                           Key = info, Value = {pieces=[B@5b080f38, files=[{path=[[B@6e1f5438], length=130474}, {path=[[B@4ad26103], length=2152145089}, {path=[[B@39df3255], length=1067284411}, {path=[[B@6c618821], length=483}, {path=[[B@7730661d], length=493}, {path=[[B@a80370d], length=180885}, {path=[[B@679e3bdd], length=496}, {path=[[B@456c5f50], length=480}], name=[B@4de5a0cb, publisher-url=[B@1e9f9761, piece length=2097152, publisher=[B@7f8b70fb}

			
//			Map<String, Object> map = new HashMap<String, Object>();

//			for (Map.Entry<String, Object> entry : ((Map<String, Object>) obj).entrySet()) {
//
//			    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
//
//			}
			
			
			
//			for (Map.Entry entry : ((Map) obj).entrySet()) {
//
//			    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
////			    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
//
//			}
			
			//			decoder.print2(obj);
//			PrintWriter pw = new PrintWriter(new FileWriter(output));

//			decoder.print(pw, decoder.decodeStream(new BufferedInputStream(new FileInputStream(f))));

//			pw.flush();

		} catch (Throwable e) {

			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
//		print(new File("C:/Users/admin/Downloads/MICROSOFT.WINDOWS.8.ENTERPRISE.N.RTM.X64.VOLUME.ENGLISH.BOOTABLE_ISO-CTRLSOFT.torrent"), new File("D:/logs/bdecoder1.log"));
//		print(new File("C:/Users/admin/Downloads/1024gc.com_253.(E-BODY)(EBOD-411)感度200倍_ゆっくりねっとりスローセックス6時間_周防ゆきこ_1.torrent"), new File("D:/logs/bdecoder.log"));
		print(new File("C:/Users/admin/Downloads/1024gc.com_9E10D34BC03E7E6A742632C55F257007B8746D16.torrent"), new File("D:/logs/bdecoder.log"));
	}
}
