package org.minnie.utility.module.hoopchina;
/**
 * @author 作者名 E-mail:neiplzer@gmail.com
 * @version 创建时间：2014-3-6 上午12:11:30
 * 类说明
 */
public class HoopChina {
	
	private String title;//标题
	private int total;//总数
	
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

	@Override
	public String toString() {
		return "HoopChina [title=" + title + ", total=" + total + "]";
	}
	
	
}