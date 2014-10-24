package org.minnie.utility.module.netease;

/**
 * 球队信息Entity
 * @author neiplz
 * @version 2014-10-23
 */
public class FootballTeam {
	
	private Long id; 		// 编号
	private String name; 	// 名称
    private String fullName;	// 球队全名
    private Long leagueId;	// 赛事id
    private String rank;	// 联赛排名
    private String city;	// 所在城市
    private String homeCourt;	// 球队主场
    private String headCoach;	// 主教练
    private String link;	// 球队链接
    private String briefIntroduction;	// 球队简介

	public FootballTeam() {
		super();
	}

	public FootballTeam(Long id){
		this();
		this.id = id;
	}
	
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
	
	
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
	
    public Long getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(Long leagueId) {
        this.leagueId = leagueId;
    }
	
    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
	
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
	
    public String getHomeCourt() {
        return homeCourt;
    }

    public void setHomeCourt(String homeCourt) {
        this.homeCourt = homeCourt;
    }
	
    public String getHeadCoach() {
        return headCoach;
    }

    public void setHeadCoach(String headCoach) {
        this.headCoach = headCoach;
    }
	
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
	
    public String getBriefIntroduction() {
        return briefIntroduction;
    }

    public void setBriefIntroduction(String briefIntroduction) {
        this.briefIntroduction = briefIntroduction;
    }

	@Override
	public String toString() {
		return "FootballTeam [id=" + id + ", name=" + name + ", fullName="
				+ fullName + ", leagueId=" + leagueId + ", rank=" + rank
				+ ", city=" + city + ", homeCourt=" + homeCourt
				+ ", headCoach=" + headCoach + ", link=" + link
				+ ", briefIntroduction=" + briefIntroduction + "]";
	}
    
}