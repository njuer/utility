package org.minnie.utility.entity.lottery;

import java.util.ArrayList;
import java.util.List;

/**
 * @author neiplzer@gmail.com
 * @version 2014-3-23
 * 11选5类
 */
public class FiveInEleven implements Comparable<Object>  {

	
	private Integer period;
	private List<String> red = new ArrayList<String>(5);
	private String lotteryNumber;//排序后的号码
	private String date;
	private String category;//11选5类别
	private String consecutive;//连号
	private Integer amount;//连号数目

	public Integer getPeriod() {
		return period;
	}
	
	public void setPeriod(Integer period) {
		this.period = period;
	}
	
	public List<String> getRed() {
		return red;
	}
	
	public void setRed(List<String> red) {
		this.red = red;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getConsecutive() {
		return consecutive;
	}

	public void setConsecutive(String consecutive) {
		this.consecutive = consecutive;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getLotteryNumber() {
		return lotteryNumber;
	}

	public void setLotteryNumber(String lotteryNumber) {
		this.lotteryNumber = lotteryNumber;
	}

	@Override
	public int compareTo(Object o) {
		 return this.period - ((FiveInEleven) o).getPeriod();
	}

	@Override
	public String toString() {
		return "FiveInEleven [period=" + period + ", red=" + red
				+ ", lotteryNumber=" + lotteryNumber + ", date=" + date
				+ ", category=" + category + ", consecutive=" + consecutive
				+ ", amount=" + amount + "]";
	}

}