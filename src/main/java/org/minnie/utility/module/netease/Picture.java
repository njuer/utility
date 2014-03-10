package org.minnie.utility.module.netease;

/**
 * @author neiplzer@gmail.com
 * @version 2014-3-10
 * 类说明
 */
public class Picture {

//    "id": "9MV87AUB4TM10005",
//    "img": "http://img3.cache.netease.com/photo/0005/2014-03-10/9MV87AUB4TM10005.jpg",
//    "timg": "http://img3.cache.netease.com/photo/0005/2014-03-10/t_9MV87AUB4TM10005.jpg",
//    "simg": "http://img3.cache.netease.com/photo/0005/2014-03-10/s_9MV87AUB4TM10005.jpg",
//    "oimg": "http://img3.cache.netease.com/photo/0005/2014-03-10/9MV87AUB4TM10005.jpg",
//    "osize": {"w":1280,"h":846},
//    "title": "杜兰特很无奈",
//    "note": "",
//    "newsurl": "#"
	private String id;//图片编号
	private String img;//图片地址
	private String timg;//tiny图片地址
	private String simg;//small图片地址
	private String oimg;//o图片地址 (同img)
	private PictureSize osize;//图片基本属性(宽、高)
	private String title;//标题
	private String note;//备注
	private String newsurl;//新闻地址
	
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
	
	public String getTimg() {
		return timg;
	}
	
	public void setTimg(String timg) {
		this.timg = timg;
	}
	
	public String getSimg() {
		return simg;
	}
	
	public void setSimg(String simg) {
		this.simg = simg;
	}
	
	public String getOimg() {
		return oimg;
	}
	
	public void setOimg(String oimg) {
		this.oimg = oimg;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getNote() {
		return note;
	}
	
	public void setNote(String note) {
		this.note = note;
	}
	
	public String getNewsurl() {
		return newsurl;
	}
	
	public void setNewsurl(String newsurl) {
		this.newsurl = newsurl;
	}

	public PictureSize getOsize() {
		return osize;
	}

	public void setOsize(PictureSize osize) {
		this.osize = osize;
	}

	@Override
	public String toString() {
		return "NeteasePicture [id=" + id + ", img=" + img + ", timg=" + timg
				+ ", simg=" + simg + ", oimg=" + oimg + ", picture=" + osize
				+ ", title=" + title + ", note=" + note + ", newsurl="
				+ newsurl + "]";
	}

}