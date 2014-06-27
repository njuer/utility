package org.minnie.utility.entity.lottery;

import java.util.Arrays;

public class FiveInElevenCandidate {
	
	private Integer period;
	private int [] candidate;//候选号统计
	private int hint;//候选号码数
	private String category;//杀号类别
	
	public Integer getPeriod() {
		return period;
	}
	public void setPeriod(Integer period) {
		this.period = period;
	}
	public int[] getCandidate() {
		return candidate;
	}
	public void setCandidate(int[] candidate) {
		this.candidate = candidate;
	}
	public int getHint() {
		return hint;
	}
	public void setHint(int hint) {
		this.hint = hint;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	@Override
	public String toString() {
		return "FiveInElevenCandidate [period=" + period + ", candidate="
				+ Arrays.toString(candidate) + ", hint=" + hint + ", category="
				+ category + "]";
	}
	
}