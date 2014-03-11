package org.minnie.utility.module.yangshengsuo;
/**
 * @author neiplzer@gmail.com
 * @version 2014-3-10
 * 39养生所实体类
 */
public class Regimen {

	private String title;//标题
	private String pageUrl;//页面地址
	private String pictureUrl;//图片地址
	private int total;//图片总数
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getPageUrl() {
		return pageUrl;
	}
	
	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}
	
	public String getPictureUrl() {
		return pictureUrl;
	}
	
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	
	public int getTotal() {
		return total;
	}
	
	public void setTotal(int total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "Regimen [title=" + title + ", pageUrl=" + pageUrl
				+ ", pictureUrl=" + pictureUrl + ", total=" + total + "]";
	}
	
}