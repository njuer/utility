package org.minnie.utility.module.netease;

/**
 * 精彩足球实体类
 * 
 * @author eazytec
 * 
 */
public class SmgFootball {
	
	private String gameDate;// 比赛日期<-gameDate

	private String matchId = "989613";// 比赛ID<-matchid
	private String matchCode = "201410083011";// 比赛代码：年(4)月(2)日(2)轮?(1)编号(3)<-matchcode
	private String matchNumCn = "周三011";// 周几第几场<-matchnumcn
	private String startTime = "1412821800000";// 开赛时间,时间戳<-starttime
	private String endTime = "1412821620000";// 代购截止时间,时间戳<-endtime
	private String isAttention = "0";// 是否关注 0:未关注 1:已关注<-isattention
	private String homeTeamId = "27972";// 主队ID<-hostteamid
	private String homeTeamName = "波特兰";// 主队名称<-hostname
	private Integer homeTeamRank;// 主队排名
	
	private String awayTeamId = "41560";// 客队ID<-visitteamid
	private String awayTeamName = "圣何塞";// 客队名称<-guestname
	private Integer awayTeamRank;// 客队排名

	private String leagueId = "38";// 联赛id<-leagueid
	private String leagueName = "美大联盟";// 联赛名称<-leagueName
	
	private String isStop = "0";// 已出赛果 1:是 0:否 <-isStop
	private String isHot = "0";// 是否特点赛事 1:是 0:否<-ishot
	private String score = "-1";// 比分,球赛结束前默认值为-1<-score
	
	private Float winOdds;//主胜赔率
	private Float drawOdds;//主平赔率
	private Float loseOdds;//主负赔率

	private Float concedeWinOdds;//让球主胜赔率
	private Float concedeDrawOdds;//让球主平赔率
	private Float concedeLoseOdds;//让球主负赔率
	
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
	public String getMatchNumCn() {
		return matchNumCn;
	}
	public void setMatchNumCn(String matchNumCn) {
		this.matchNumCn = matchNumCn;
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
	public String getIsAttention() {
		return isAttention;
	}
	public void setIsAttention(String isAttention) {
		this.isAttention = isAttention;
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
	public Integer getHomeTeamRank() {
		return homeTeamRank;
	}
	public void setHomeTeamRank(Integer homeTeamRank) {
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
	public Integer getAwayTeamRank() {
		return awayTeamRank;
	}
	public void setAwayTeamRank(Integer awayTeamRank) {
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
	public Float getWinOdds() {
		return winOdds;
	}
	public void setWinOdds(Float winOdds) {
		this.winOdds = winOdds;
	}
	public Float getDrawOdds() {
		return drawOdds;
	}
	public void setDrawOdds(Float drawOdds) {
		this.drawOdds = drawOdds;
	}
	public Float getLoseOdds() {
		return loseOdds;
	}
	public void setLoseOdds(Float loseOdds) {
		this.loseOdds = loseOdds;
	}
	public Float getConcedeWinOdds() {
		return concedeWinOdds;
	}
	public void setConcedeWinOdds(Float concedeWinOdds) {
		this.concedeWinOdds = concedeWinOdds;
	}
	public Float getConcedeDrawOdds() {
		return concedeDrawOdds;
	}
	public void setConcedeDrawOdds(Float concedeDrawOdds) {
		this.concedeDrawOdds = concedeDrawOdds;
	}
	public Float getConcedeLoseOdds() {
		return concedeLoseOdds;
	}
	public void setConcedeLoseOdds(Float concedeLoseOdds) {
		this.concedeLoseOdds = concedeLoseOdds;
	}
	
	@Override
	public String toString() {
		return "SmgFootball [gameDate=" + gameDate + ", matchId=" + matchId
				+ ", matchCode=" + matchCode + ", matchNumCn=" + matchNumCn
				+ ", startTime=" + startTime + ", endTime=" + endTime
				+ ", isAttention=" + isAttention + ", homeTeamId=" + homeTeamId
				+ ", homeTeamName=" + homeTeamName + ", homeTeamRank="
				+ homeTeamRank + ", awayTeamId=" + awayTeamId
				+ ", awayTeamName=" + awayTeamName + ", awayTeamRank="
				+ awayTeamRank + ", leagueId=" + leagueId + ", leagueName="
				+ leagueName + ", isStop=" + isStop + ", isHot=" + isHot
				+ ", score=" + score + ", winOdds=" + winOdds + ", drawOdds="
				+ drawOdds + ", loseOdds=" + loseOdds + ", concedeWinOdds="
				+ concedeWinOdds + ", concedeDrawOdds=" + concedeDrawOdds
				+ ", concedeLoseOdds=" + concedeLoseOdds + "]";
	}

}