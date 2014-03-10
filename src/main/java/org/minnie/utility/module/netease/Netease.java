package org.minnie.utility.module.netease;
/**
 * @author neiplzer@gmail.com
 * @version 2014-3-10
 * 网易图集下载用信息
 */
public class Netease {

	private String id;//图片编号
	private String img;//图片地址
	private String subject;//图集主题
	private String title;//标题
	private int size;//图集大小
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getImg() {
		return img;
	}
	
	public void setImg(String img) {
		this.img = img;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public int getSize() {
		return size;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Override
	public String toString() {
		return "Netease [id=" + id + ", img=" + img + ", subject=" + subject
				+ ", title=" + title + ", size=" + size + "]";
	}

}