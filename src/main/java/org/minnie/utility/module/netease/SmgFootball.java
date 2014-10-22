package org.minnie.utility.module.netease;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 精彩足球实体类
 * 
 * @author eazytec
 * 
 */
public class SmgFootball {
	
	private String gameDate;// 比赛日期<-gameDate

	private String matchId;// 比赛ID<-matchid
	private String matchCode;// 比赛代码：年(4)月(2)日(2)轮?(1)编号(3)<-matchcode
	private String matchLink;// 比赛分析链接
	private String matchNumCn;// 周几第几场<-matchnumcn
//	private String startTime = "1412821800000";// 开赛时间,时间戳<-starttime
//	private String endTime = "1412821620000";// 代购截止时间,时间戳<-endtime

	private Date startTime;	//开赛时间(e.g. 1412821800000),时间戳<-starttime
	private Date endTime;	//代购截止时间(e.g. 1412821620000),时间戳<-endtime
	
	private String homeTeamId;// 主队ID<-hostteamid
	private String homeTeamName;// 主队名称<-hostname
	private String homeTeamRank;// 主队排名
	
	private String awayTeamId;// 客队ID<-visitteamid
	private String awayTeamName;// 客队名称<-guestname
	private String awayTeamRank;// 客队排名

	private String leagueId;// 联赛id<-leagueid
	private String leagueName;// 联赛名称<-leagueName
	private String leagueLink;// 联赛链接
	
	private String isAttention;// 是否关注 0:未关注 1:已关注<-isattention
	private String isStop;// 已出赛果 1:是 0:否 <-isStop
	private String isHot;// 是否特点赛事 1:是 0:否<-ishot
	private String score;// 比分,球赛结束前默认值为-1<-score
	
	private BigDecimal winOdds;//主胜赔率
	private BigDecimal drawOdds;//主平赔率
	private BigDecimal loseOdds;//主负赔率

	private BigDecimal concedePoints;//让球数 
	private BigDecimal concedeWinOdds;//让球主胜赔率
	private BigDecimal concedeDrawOdds;//让球主平赔率
	private BigDecimal concedeLoseOdds;//让球主负赔率
	
	public String getGameDate() {
		return gameDate;
	}
	public void setGameDate(String gameDate) {
		this.gameDate = gameDate;
	}
	public String getMatchId() {
		return matchId;
	}
	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}
	public String getMatchCode() {
		return matchCode;
	}
	public void setMatchCode(String matchCode) {
		this.matchCode = matchCode;
	}
	public String getMatchLink() {
		return matchLink;
	}
	public void setMatchLink(String matchLink) {
		this.matchLink = matchLink;
	}
	public String getMatchNumCn() {
		return matchNumCn;
	}
	public void setMatchNumCn(String matchNumCn) {
		this.matchNumCn = matchNumCn;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
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
	
	@Override
	public String toString() {
		return "SmgFootball [gameDate=" + gameDate + ", matchId=" + matchId
				+ ", matchCode=" + matchCode + ", matchLink=" + matchLink
				+ ", matchNumCn=" + matchNumCn + ", startTime=" + startTime
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
				+ concedeLoseOdds + "]";
	}
	
	
}