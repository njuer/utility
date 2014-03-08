package org.minnie.utility.module.hoopchina;

/**
 * @author 作者名 E-mail:neiplzer@gmail.com
 * @version 创建时间：2014-3-6 上午12:11:30 类说明
 */
public class HoopChina implements Cloneable {

	private String title;// 标题
	private int total;// 总数
	private String pirctureUrl;// 图片URL
	private String pageUrl;// 页面URL

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getPirctureUrl() {
		return pirctureUrl;
	}

	public void setPirctureUrl(String pirctureUrl) {
		this.pirctureUrl = pirctureUrl;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		HoopChina hoop = null;
		try {
			hoop = (HoopChina) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return hoop;
	}

	@Override
	public String toString() {
		return "HoopChina [title=" + title + ", total=" + total
				+ ", pirctureUrl=" + pirctureUrl + ", pageUrl=" + pageUrl + "]";
	}

}