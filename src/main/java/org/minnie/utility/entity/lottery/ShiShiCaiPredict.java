package org.minnie.utility.entity.lottery;

import java.util.ArrayList;
import java.util.List;

public class ShiShiCaiPredict {
	
	private Integer period;//期号
	private String wei;//位：个十百千万
	private List<Integer> predict = new ArrayList<Integer>();//预测结果
	private Integer odd = 0;//单
	private Integer even = 0;//双
	private Integer big = 0;//大
	private Integer small = 0;//小

	
	public Integer getPeriod() {
		return period;
	}
	public void setPeriod(Integer period) {
		this.period = period;
	}
	public String getWei() {
		return wei;
	}
	public void setWei(String wei) {
		this.wei = wei;
	}
	public List<Integer> getPredict() {
		return predict;
	}
	public void setPredict(List<Integer> predict) {
		this.predict = predict;
	}
	public Integer getOdd() {
		return odd;
	}
	public void setOdd(Integer odd) {
		this.odd = odd;
	}
	public Integer getEven() {
		return even;
	}
	public void setEven(Integer even) {
		this.even = even;
	}
	public Integer getBig() {
		return big;
	}
	public void setBig(Integer big) {
		this.big = big;
	}
	public Integer getSmall() {
		return small;
	}
	public void setSmall(Integer small) {
		this.small = small;
	}


	
	
}