package org.minnie.utility.module.xinyingba;

public class Video {
	
	public String uuid;//uuid
	public int number;//影片编号
	public String title;//影片名称
	public String url;//影片地址
	public String imageSource;//海报地址
	public String category;//影片类型
	public int year;//年份
	public String starring;//主演
	public float rate;//影片评分
	public String introduction;//影片简介
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int getNumber() {
		return number;
	}
	
	public void setNumber(int number) {
		this.number = number;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getImageSource() {
		return imageSource;
	}
	
	public void setImageSource(String imageSource) {
		this.imageSource = imageSource;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public String getStarring() {
		return starring;
	}
	
	public void setStarring(String starring) {
		this.starring = starring;
	}
	
	public float getRate() {
		return rate;
	}
	
	public void setRate(float rate) {
		this.rate = rate;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	@Override
	public String toString() {
		return "Video [uuid=" + uuid + ", number=" + number + ", title="
				+ title + ", url=" + url + ", imageSource=" + imageSource
				+ ", category=" + category + ", year=" + year + ", starring="
				+ starring + ", rate=" + rate + ", introduction="
				+ introduction + "]";
	}

}