package org.minnie.utility.entity.lottery;

import java.util.Arrays;
import java.util.Set;

public class FiveInElevenPredict2 {

	private Integer period;
	private Set<String> result;//开奖号码集合
	private int [] kill;//杀号命中数
	private Integer hit;//命中数
	private String category;//杀号类别
	
	public Integer getPeriod() {
		return period;
	}
	public void setPeriod(Integer period) {
		this.period = period;
	}
	public Set<String> getResult() {
		return result;
	}
	public void setResult(Set<String> result) {
		this.result = result;
	}
	public Integer getHit() {
		return hit;
	}
	public void setHit(Integer hit) {
		this.hit = hit;
	}
	
	public int[] getKill() {
		return kill;
	}
	public void setKill(int[] kill) {
		this.kill = kill;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	@Override
	public String toString() {
		return "FiveInElevenAnalysis [period=" + period + ", result=" + result
				+ ", kill=" + Arrays.toString(kill) + ", hit=" + hit
				+ ", category=" + category + "]";
	}
	
}