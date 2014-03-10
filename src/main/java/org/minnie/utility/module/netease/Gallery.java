package org.minnie.utility.module.netease;
/**
 * @author neiplzer@gmail.com
 * @version 2014-3-10
 * 类说明
 */
public class Gallery {
//    "info": {
//    "setname": "NBA常规赛：湖人114-110雷霆",
//    "imgsum": 20,
//    "lmodify": "2014-03-10 07:46:31",
//    "prevue": "洛杉矶湖人队（22胜42负）成功在主场止血。尽管杜兰特得到27分、12次助攻和10个篮板的三双表现，维斯布鲁克也有20分、8次助攻和7个篮板，但米克斯得到生涯新高42分，加索尔也有20分和11个篮板，他们率队在第三节打出反扑高潮逆转最多18分落后，湖人队第四节成功保住优势，他们在主场以114-110战胜俄克拉荷马城雷霆队（46胜17负）。湖人队结束三连败，雷霆队遭遇两连败。",
//    "channelid": "0005",
//    "prev": {
//        "setname": "NBA常规赛：公牛95-88热火",
//        "simg": "http://img4.cache.netease.com/photo/0005/2014-03-10/s_9MUSSCFH4TM10005.jpg",
//        "seturl": "http://sports.163.com/photoview/4TM10005/111570.html"
//    },
//    "next": {
//        "setname": "NBA常规赛：快船 - 老鹰",
//        "simg": "http://img2.cache.netease.com/photo/0005/2014-03-09/s_9MT8BI0U4TM10005.jpg",
//        "seturl": "http://sports.163.com/photoview/4TM10005/111543.html"
//    }
//},
	
	private String setname;//图集标题
	private int imgsum;//图片数量
	private String lmodify;//最后修改时间
	private String prevue;//图集文字预告
	private String channelid;//频道id
	private String seturl;//图集地址
	private String simg;//缩略图地址
	private Gallery prev;//上一图集
	private Gallery next;//下一图集
	
	public String getSetname() {
		return setname;
	}
	
	public void setSetname(String setname) {
		this.setname = setname;
	}
	
	public int getImgsum() {
		return imgsum;
	}
	
	public void setImgsum(int imgsum) {
		this.imgsum = imgsum;
	}
	
	public String getLmodify() {
		return lmodify;
	}
	
	public void setLmodify(String lmodify) {
		this.lmodify = lmodify;
	}
	
	public String getPrevue() {
		return prevue;
	}
	
	public void setPrevue(String prevue) {
		this.prevue = prevue;
	}
	
	public String getChannelid() {
		return channelid;
	}
	
	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}
	
	public String getSeturl() {
		return seturl;
	}
	
	public void setSeturl(String seturl) {
		this.seturl = seturl;
	}
	
	public String getSimg() {
		return simg;
	}

	public void setSimg(String simg) {
		this.simg = simg;
	}

	public Gallery getPrev() {
		return prev;
	}
	
	public void setPrev(Gallery prev) {
		this.prev = prev;
	}
	
	public Gallery getNext() {
		return next;
	}
	
	public void setNext(Gallery next) {
		this.next = next;
	}

	@Override
	public String toString() {
		return "Gallery [setname=" + setname + ", imgsum=" + imgsum
				+ ", lmodify=" + lmodify + ", prevue=" + prevue
				+ ", channelid=" + channelid + ", seturl=" + seturl + ", simg="
				+ simg + ", prev=" + prev + ", next=" + next + "]";
	}

}