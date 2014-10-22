package org.minnie.utility.module.netease;

public class FootballLeague {

	private Long id; 		// 编号
	private String name; 	// 名称
    private String category;	// 赛事类型：联赛、杯赛
    private String country;	// 国家
    private String continental;	// 洲
    private String link;	// 赛事链接
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getContinental() {
		return continental;
	}
	public void setContinental(String continental) {
		this.continental = continental;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
	@Override
	public String toString() {
		return "FootballLeague [id=" + id + ", name=" + name + ", category="
				+ category + ", country=" + country + ", continental="
				+ continental + ", link=" + link + "]";
	}
    
    
}