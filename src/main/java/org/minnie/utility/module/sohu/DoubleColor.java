package org.minnie.utility.module.sohu;

import java.util.ArrayList;
import java.util.List;

/**
 * @author neiplzer@gmail.com
 * @version 2014-3-11
 * 双色球实体类
 */
public class DoubleColor implements Comparable {

	private Integer phase;
	private List<String> red = new ArrayList<String>(6);
	private String blue;
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
	
	public String getBlue() {
		return blue;
	}
	
	public void setBlue(String blue) {
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
		 return this.phase - ((DoubleColor) o).getPhase();
	}
	
	@Override
	public String toString() {
		return "DoubleColor [phase=" + phase + ", red=" + red + ", blue="
				+ blue + ", year=" + year + "]";
	}

}