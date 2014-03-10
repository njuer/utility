package org.minnie.utility.module.netease;
/**
 * @author neiplzer@gmail.com
 * @version 2014-3-10
 * 类说明
 */
public class PictureSize {
	
	private int w;//图片宽度
	private int h;//图片高度
	
	public int getW() {
		return w;
	}
	
	public void setW(int w) {
		this.w = w;
	}
	
	public int getH() {
		return h;
	}
	
	public void setH(int h) {
		this.h = h;
	}

	@Override
	public String toString() {
		return "PictureSize [w=" + w + ", h=" + h + "]";
	}
	
}