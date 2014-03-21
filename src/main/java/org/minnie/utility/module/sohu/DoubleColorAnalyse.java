package org.minnie.utility.module.sohu;

import java.util.Set;

/**
 * @author neiplzer@gmail.com
 * @version 2014-3-18
 * 双色球结果分析
 */
public class DoubleColorAnalyse {

	private DoubleColor doubleColor;
	private int lastFivePhaseForRed;
	private int downForRed;
	private int lastBlueKillRed;
	private int lastMaxMinusMinKillRed;
	private int phaseNumberKillBlue;
	private int addSubtractionKillBlue;
	private int awards;
	private Set<String> redCandidate;
	private Set<String> blueCandidate;
	
	public DoubleColor getDoubleColor() {
		return doubleColor;
	}
	
	public void setDoubleColor(DoubleColor doubleColor) {
		this.doubleColor = doubleColor;
	}
	
	public int getLastFivePhaseForRed() {
		return lastFivePhaseForRed;
	}
	
	public void setLastFivePhaseForRed(int lastFivePhaseForRed) {
		this.lastFivePhaseForRed = lastFivePhaseForRed;
	}
	
	public int getDownForRed() {
		return downForRed;
	}
	
	public void setDownForRed(int downForRed) {
		this.downForRed = downForRed;
	}
	
	public int getLastBlueKillRed() {
		return lastBlueKillRed;
	}
	
	public void setLastBlueKillRed(int lastBlueKillRed) {
		this.lastBlueKillRed = lastBlueKillRed;
	}
	
	public int getLastMaxMinusMinKillRed() {
		return lastMaxMinusMinKillRed;
	}
	
	public void setLastMaxMinusMinKillRed(int lastMaxMinusMinKillRed) {
		this.lastMaxMinusMinKillRed = lastMaxMinusMinKillRed;
	}
	
	public int getPhaseNumberKillBlue() {
		return phaseNumberKillBlue;
	}
	
	public void setPhaseNumberKillBlue(int phaseNumberKillBlue) {
		this.phaseNumberKillBlue = phaseNumberKillBlue;
	}
	
	public int getAddSubtractionKillBlue() {
		return addSubtractionKillBlue;
	}
	
	public void setAddSubtractionKillBlue(int addSubtractionKillBlue) {
		this.addSubtractionKillBlue = addSubtractionKillBlue;
	}

	public int getAwards() {
		return awards;
	}

	public void setAwards(int awards) {
		this.awards = awards;
	}

	public Set<String> getRedCandidate() {
		return redCandidate;
	}

	public void setRedCandidate(Set<String> redCandidate) {
		this.redCandidate = redCandidate;
	}

	public Set<String> getBlueCandidate() {
		return blueCandidate;
	}

	public void setBlueCandidate(Set<String> blueCandidate) {
		this.blueCandidate = blueCandidate;
	}
	
}