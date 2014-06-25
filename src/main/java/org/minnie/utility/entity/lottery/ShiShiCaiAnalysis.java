package org.minnie.utility.entity.lottery;

import java.util.Map;

/**
 * @author neiplzer@gmail.com
 * @version 2014-6-24
 * 类说明
 */
public class ShiShiCaiAnalysis {

	private Integer period;
	private String result;//开奖号码
	private Map<Integer,Boolean> xuanYuan;//轩辕杀号 			
	private Map<Integer,Boolean> zhanLu;//湛泸杀号 
	private Map<Integer,Boolean> chiXiao;//赤霄杀号 
	private Map<Integer,Boolean> taiE;//泰阿杀号
	private Map<Integer,Boolean> qiXing;//七星杀号
	private Map<Integer,Boolean> moYe;//莫邪杀号
	private Map<Integer,Boolean> ganJiang;//干将杀号
	private Map<Integer,Boolean> yuChang;//鱼肠杀号
	private Map<Integer,Boolean> chunJian;//纯剑杀号
	private Map<Integer,Boolean> chengYing;//承影杀号
	private Integer hit;//命中数
	
	public Integer getPeriod() {
		return period;
	}
	public void setPeriod(Integer period) {
		this.period = period;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public Map<Integer, Boolean> getXuanYuan() {
		return xuanYuan;
	}
	public void setXuanYuan(Map<Integer, Boolean> xuanYuan) {
		this.xuanYuan = xuanYuan;
	}
	public Map<Integer, Boolean> getZhanLu() {
		return zhanLu;
	}
	public void setZhanLu(Map<Integer, Boolean> zhanLu) {
		this.zhanLu = zhanLu;
	}
	public Map<Integer, Boolean> getChiXiao() {
		return chiXiao;
	}
	public void setChiXiao(Map<Integer, Boolean> chiXiao) {
		this.chiXiao = chiXiao;
	}
	public Map<Integer, Boolean> getTaiE() {
		return taiE;
	}
	public void setTaiE(Map<Integer, Boolean> taiE) {
		this.taiE = taiE;
	}
	public Map<Integer, Boolean> getQiXing() {
		return qiXing;
	}
	public void setQiXing(Map<Integer, Boolean> qiXing) {
		this.qiXing = qiXing;
	}
	public Map<Integer, Boolean> getMoYe() {
		return moYe;
	}
	public void setMoYe(Map<Integer, Boolean> moYe) {
		this.moYe = moYe;
	}
	public Map<Integer, Boolean> getGanJiang() {
		return ganJiang;
	}
	public void setGanJiang(Map<Integer, Boolean> ganJiang) {
		this.ganJiang = ganJiang;
	}
	public Map<Integer, Boolean> getYuChang() {
		return yuChang;
	}
	public void setYuChang(Map<Integer, Boolean> yuChang) {
		this.yuChang = yuChang;
	}
	public Map<Integer, Boolean> getChunJian() {
		return chunJian;
	}
	public void setChunJian(Map<Integer, Boolean> chunJian) {
		this.chunJian = chunJian;
	}
	public Map<Integer, Boolean> getChengYing() {
		return chengYing;
	}
	public void setChengYing(Map<Integer, Boolean> chengYing) {
		this.chengYing = chengYing;
	}
	public Integer getHit() {
		return hit;
	}
	public void setHit(Integer hit) {
		this.hit = hit;
	}
	
	@Override
	public String toString() {
		return "ShiShiCaiAnalysis [period=" + period + ", result=" + result
				+ ", xuanYuan=" + xuanYuan + ", zhanLu=" + zhanLu
				+ ", chiXiao=" + chiXiao + ", taiE=" + taiE + ", qiXing="
				+ qiXing + ", moYe=" + moYe + ", ganJiang=" + ganJiang
				+ ", yuChang=" + yuChang + ", chunJian=" + chunJian
				+ ", chengYing=" + chengYing + ", hit=" + hit + "]";
	}
	
}