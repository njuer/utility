package org.minnie.utility.module.netease;

import java.util.List;

/**
 * @author neiplzer@gmail.com
 * @version 2014-3-10
 * 类说明
 */
public class NeteasePage {

	private Gallery info;
	private List<Picture> list;
	
	public Gallery getInfo() {
		return info;
	}
	
	public void setInfo(Gallery info) {
		this.info = info;
	}

	public List<Picture> getList() {
		return list;
	}

	public void setList(List<Picture> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "NeteasePage [info=" + info + ", list=" + list + "]";
	}
	
}