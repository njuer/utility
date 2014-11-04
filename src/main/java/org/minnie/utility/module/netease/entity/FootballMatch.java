package org.minnie.utility.module.netease.entity;

import java.math.BigDecimal;


/**
 * 每日赛事信息Entity
 * @author neiplz
 * @version 2014-11-01
 */
public class FootballMatch{
	
	private static final long serialVersionUID = 1L;
	private Long id; 		// 编号
//	private String name; 	// 名称
//    private String matchId;	// 比赛ID
    private String matchCode;	// 比赛代码
    private String matchNumCn;	// 周几第几场
    private String gameDate;	// 比赛日期
    private String matchLink;	// 比赛链接
    private String startTime;	// 开赛时间
    private String endTime;	// 代购截止时间
    private String homeTeamId;	// 主队ID
    private String homeTeamName;	// 主队名称
    private String homeTeamRank;	// 主队排名
    private String awayTeamId;	// 客队ID
    private String awayTeamName;	// 客队名称
    private String awayTeamRank;	// 客队排名
    private String leagueId;	// 联赛id
    private String leagueName;	// 联赛名称
    private String leagueLink;	// 联赛链接
    private String isAttention;	// 是否关注 0:未关注 1:已关注
    private String isStop;	// 已出赛果 1:是 0:否
    private String isHot;	// 是否特点赛事 1:是 0:否
    private String score;	// 比分,球赛结束前默认值为让球数
    private BigDecimal winOdds;	// 主胜赔率
    private BigDecimal drawOdds;	// 主平赔率
    private BigDecimal loseOdds;	// 主负赔率
    private BigDecimal concedePoints;	// 让球数
    private BigDecimal concedeWinOdds;	// 让球主胜赔率
    private BigDecimal concedeDrawOdds;	// 让球主平赔率
    private BigDecimal concedeLoseOdds;	// 让球主负赔率
	private String remarks;	// 备注
	private String createBy;	// 创建者
	private String createDate;// 创建日期
	private String updateBy;	// 更新者
	private String updateDate;// 更新日期
	private String delFlag; // 删除标记（0：正常；1：删除；2：审核）

	public FootballMatch() {
		super();
	}

	public FootballMatch(Long id){
		this();
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//	
//    public String getMatchId() {
//        return matchId;
//    }
//
//    public void setMatchId(String matchId) {
//        this.matchId = matchId;
//    }
    
    public String getMatchCode() {
        return matchCode;
    }

    public void setMatchCode(String matchCode) {
        this.matchCode = matchCode;
    }
    
    public String getMatchNumCn() {
        return matchNumCn;
    }

    public void setMatchNumCn(String matchNumCn) {
        this.matchNumCn = matchNumCn;
    }
    
    public String getGameDate() {
        return gameDate;
    }

    public void setGameDate(String gameDate) {
        this.gameDate = gameDate;
    }
    
    public String getMatchLink() {
        return matchLink;
    }

    public void setMatchLink(String matchLink) {
        this.matchLink = matchLink;
    }
    
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    
    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    
    public String getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(String homeTeamId) {
        this.homeTeamId = homeTeamId;
    }
    
    public String getHomeTeamName() {
        return homeTeamName;
    }

    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }
    
    public String getHomeTeamRank() {
        return homeTeamRank;
    }

    public void setHomeTeamRank(String homeTeamRank) {
        this.homeTeamRank = homeTeamRank;
    }
    
    public String getAwayTeamId() {
        return awayTeamId;
    }

    public void setAwayTeamId(String awayTeamId) {
        this.awayTeamId = awayTeamId;
    }
    
    public String getAwayTeamName() {
        return awayTeamName;
    }

    public void setAwayTeamName(String awayTeamName) {
        this.awayTeamName = awayTeamName;
    }
    
    public String getAwayTeamRank() {
        return awayTeamRank;
    }

    public void setAwayTeamRank(String awayTeamRank) {
        this.awayTeamRank = awayTeamRank;
    }
    
    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }
    
    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }
    
    public String getLeagueLink() {
        return leagueLink;
    }

    public void setLeagueLink(String leagueLink) {
        this.leagueLink = leagueLink;
    }
    
    public String getIsAttention() {
        return isAttention;
    }

    public void setIsAttention(String isAttention) {
        this.isAttention = isAttention;
    }
    
    public String getIsStop() {
        return isStop;
    }

    public void setIsStop(String isStop) {
        this.isStop = isStop;
    }
    
    public String getIsHot() {
        return isHot;
    }

    public void setIsHot(String isHot) {
        this.isHot = isHot;
    }
    
    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
    
    public BigDecimal getWinOdds() {
        return winOdds;
    }

    public void setWinOdds(BigDecimal winOdds) {
        this.winOdds = winOdds;
    }
    
    public BigDecimal getDrawOdds() {
        return drawOdds;
    }

    public void setDrawOdds(BigDecimal drawOdds) {
        this.drawOdds = drawOdds;
    }
    
    public BigDecimal getLoseOdds() {
        return loseOdds;
    }

    public void setLoseOdds(BigDecimal loseOdds) {
        this.loseOdds = loseOdds;
    }
    
    public BigDecimal getConcedePoints() {
        return concedePoints;
    }

    public void setConcedePoints(BigDecimal concedePoints) {
        this.concedePoints = concedePoints;
    }
    
    public BigDecimal getConcedeWinOdds() {
        return concedeWinOdds;
    }

    public void setConcedeWinOdds(BigDecimal concedeWinOdds) {
        this.concedeWinOdds = concedeWinOdds;
    }
    
    public BigDecimal getConcedeDrawOdds() {
        return concedeDrawOdds;
    }

    public void setConcedeDrawOdds(BigDecimal concedeDrawOdds) {
        this.concedeDrawOdds = concedeDrawOdds;
    }
    
    public BigDecimal getConcedeLoseOdds() {
        return concedeLoseOdds;
    }

    public void setConcedeLoseOdds(BigDecimal concedeLoseOdds) {
        this.concedeLoseOdds = concedeLoseOdds;
    }
    
    
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	@Override
	public String toString() {
		return "FootballMatch [id=" + id + ", matchCode=" + matchCode
				+ ", matchNumCn=" + matchNumCn + ", gameDate=" + gameDate
				+ ", matchLink=" + matchLink + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", homeTeamId=" + homeTeamId
				+ ", homeTeamName=" + homeTeamName + ", homeTeamRank="
				+ homeTeamRank + ", awayTeamId=" + awayTeamId
				+ ", awayTeamName=" + awayTeamName + ", awayTeamRank="
				+ awayTeamRank + ", leagueId=" + leagueId + ", leagueName="
				+ leagueName + ", leagueLink=" + leagueLink + ", isAttention="
				+ isAttention + ", isStop=" + isStop + ", isHot=" + isHot
				+ ", score=" + score + ", winOdds=" + winOdds + ", drawOdds="
				+ drawOdds + ", loseOdds=" + loseOdds + ", concedePoints="
				+ concedePoints + ", concedeWinOdds=" + concedeWinOdds
				+ ", concedeDrawOdds=" + concedeDrawOdds + ", concedeLoseOdds="
				+ concedeLoseOdds + ", remarks=" + remarks + ", createBy="
				+ createBy + ", createDate=" + createDate + ", updateBy="
				+ updateBy + ", updateDate=" + updateDate + ", delFlag="
				+ delFlag + "]";
	}
	
}