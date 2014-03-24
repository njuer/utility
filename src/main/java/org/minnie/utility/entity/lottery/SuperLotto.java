package org.minnie.utility.entity.lottery;

import java.util.ArrayList;
import java.util.List;

/**
 * @author neiplzer@gmail.com
 * @version 2014-3-11
 * 超级大乐透实体类
 */
public class SuperLotto implements Comparable<Object> {

	private Integer phase;
	private List<String> red = new ArrayList<String>(5);
	private List<String> blue = new ArrayList<String>(2);
	private Integer year;
	
	public Integer getPhase() {
		return phase;
	}
	
	public void setPhase(Integer phase) {
		this.phase = phase;
	}
	
	public List<String> getRed() {
		return red;
	}
	
	public void setRed(List<String> red) {
		this.red = red;
	}
	
	public List<String> getBlue() {
		return blue;
	}

	public void setBlue(List<String> blue) {
		this.blue = blue;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	@Override
	public int compareTo(Object o) {
		 return this.phase - ((SuperLotto) o).getPhase();
	}

	@Override
	public String toString() {
		return "SuperLotto [phase=" + phase + ", red=" + red + ", blue=" + blue
				+ ", year=" + year + "]";
	}
	
}