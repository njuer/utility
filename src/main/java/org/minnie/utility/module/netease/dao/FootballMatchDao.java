package org.minnie.utility.module.netease.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.minnie.utility.module.netease.entity.FootballMatch;
import org.minnie.utility.persistence.MysqlConnectionPoolManager;
import org.minnie.utility.util.DateUtil;

/**
 * 每日赛事信息DAO
 * 
 * @author neiplz
 * @version 2014-11-01
 */
public class FootballMatchDao {

	private static Logger logger = Logger.getLogger(FootballMatchDao.class
			.getName());

	/**
	 * 批量导入每日赛事信息
	 * 
	 * @param footballMatchList
	 *            每日赛事信息列表
	 * @param existIdSet
	 */
	public static void batchAddFootballMatch(
			List<FootballMatch> footballMatchList, Set<Long> existIdSet) {

		Connection conn = null;
		PreparedStatement pstInsert = null;
		PreparedStatement pstUpdate = null;
		PreparedStatement pstUpdateScore = null;

		boolean existFlag = true;
		if (null == existIdSet || 0 == existIdSet.size()) {
			existFlag = false;
		}

		// 新增
		StringBuffer sqlInsert = new StringBuffer();
		sqlInsert.append("insert into ");
		sqlInsert.append(" lms_football_match ");
		sqlInsert.append(" ( ");
		sqlInsert.append("id,"); // 比赛ID
		// sqlInsert.append("name,");// 名称
		// sqlInsert.append("match_id,"); // 比赛ID
		sqlInsert.append("match_code,"); // 比赛代码
		sqlInsert.append("match_num_cn,"); // 周几第几场
		sqlInsert.append("game_date,"); // 比赛日期
		sqlInsert.append("match_link,"); // 比赛链接
		sqlInsert.append("start_time,"); // 开赛时间
		sqlInsert.append("end_time,"); // 代购截止时间
		sqlInsert.append("home_team_id,"); // 主队ID
		sqlInsert.append("home_team_name,"); // 主队名称
		sqlInsert.append("home_team_rank,"); // 主队排名
		sqlInsert.append("away_team_id,"); // 客队ID
		sqlInsert.append("away_team_name,"); // 客队名称
		sqlInsert.append("away_team_rank,"); // 客队排名
		sqlInsert.append("league_id,"); // 联赛id
		sqlInsert.append("league_name,"); // 联赛名称
		sqlInsert.append("league_link,"); // 联赛链接
		sqlInsert.append("is_attention,"); // 是否关注 0:未关注 1:已关注
		sqlInsert.append("is_stop,"); // 已出赛果 1:是 0:否
		sqlInsert.append("is_hot,"); // 是否特点赛事 1:是 0:否
		sqlInsert.append("score,"); // 比分,球赛结束前默认值为让球数
		sqlInsert.append("win_odds,"); // 主胜赔率
		sqlInsert.append("draw_odds,"); // 主平赔率
		sqlInsert.append("lose_odds,"); // 主负赔率
		sqlInsert.append("concede_points,"); // 让球数
		sqlInsert.append("concede_win_odds,"); // 让球主胜赔率
		sqlInsert.append("concede_draw_odds,"); // 让球主平赔率
		sqlInsert.append("concede_lose_odds,"); // 让球主负赔率

		// sqlInsert.append("fisrt_half_score,"); // 上半场比分
		// sqlInsert.append("match_result,"); // 胜平负赛果
		// sqlInsert.append("concede_match_result,"); // 让球胜平负赛果
		// sqlInsert.append("goals,"); // 总进球数
		// sqlInsert.append("goals_result,"); // 总进球数赛果
		// sqlInsert.append("half_full_result,"); // 半全场赛果
		// sqlInsert.append("score_result,"); // 比分赛果

		sqlInsert.append("del_flag,"); // 删除标记（0：正常；1：删除；2：审核）
		sqlInsert.append("create_date,"); // 创建日期
		sqlInsert.append("create_by,"); // 创建者
		sqlInsert.append("update_date,"); // 更新日期
		sqlInsert.append("update_by,"); // 更新者
		sqlInsert.append("remarks"); // 备注
		sqlInsert.append(" ) ");
		sqlInsert.append(" values ");
		sqlInsert.append(" ( ");
		sqlInsert.append(" ?, "); // 比赛代码
		// sqlInsert.append(" ?, "); // 名称
		// sqlInsert.append(" ?, "); // 比赛ID
		sqlInsert.append(" ?, "); // 比赛代码
		sqlInsert.append(" ?, "); // 周几第几场
		sqlInsert.append(" ?, "); // 比赛日期
		sqlInsert.append(" ?, "); // 比赛链接
		sqlInsert.append(" ?, "); // 开赛时间
		sqlInsert.append(" ?, "); // 代购截止时间
		sqlInsert.append(" ?, "); // 主队ID
		sqlInsert.append(" ?, "); // 主队名称
		sqlInsert.append(" ?, "); // 主队排名
		sqlInsert.append(" ?, "); // 客队ID
		sqlInsert.append(" ?, "); // 客队名称
		sqlInsert.append(" ?, "); // 客队排名
		sqlInsert.append(" ?, "); // 联赛id
		sqlInsert.append(" ?, "); // 联赛名称
		sqlInsert.append(" ?, "); // 联赛链接
		sqlInsert.append(" ?, "); // 是否关注 0:未关注 1:已关注
		sqlInsert.append(" ?, "); // 已出赛果 1:是 0:否
		sqlInsert.append(" ?, "); // 是否特点赛事 1:是 0:否
		sqlInsert.append(" ?, "); // 比分,球赛结束前默认值为让球数
		sqlInsert.append(" ?, "); // 主胜赔率
		sqlInsert.append(" ?, "); // 主平赔率
		sqlInsert.append(" ?, "); // 主负赔率
		sqlInsert.append(" ?, "); // 让球数
		sqlInsert.append(" ?, "); // 让球主胜赔率
		sqlInsert.append(" ?, "); // 让球主平赔率
		sqlInsert.append(" ?, "); // 让球主负赔率

		// sqlInsert.append(" ?, "); // 上半场比分
		// sqlInsert.append(" ?, "); // 胜平负赛果
		// sqlInsert.append(" ?, "); // 让球胜平负赛果
		// sqlInsert.append(" ?, "); // 总进球数
		// sqlInsert.append(" ?, "); // 总进球数赛果
		// sqlInsert.append(" ?, "); // 半全场赛果
		// sqlInsert.append(" ?, "); // 比分赛果

		sqlInsert.append(" ?, "); // 删除标记（0：正常；1：删除；2：审核）
		sqlInsert.append(" ?, "); // 创建日期
		sqlInsert.append(" ?, "); // 创建者
		sqlInsert.append(" ?, "); // 更新日期
		sqlInsert.append(" ?, "); // 更新者
		sqlInsert.append(" ? "); // 备注
		sqlInsert.append(" ) ");

		// 更新
		StringBuffer sqlUpdate = new StringBuffer();
		sqlUpdate.append("update ");
		sqlUpdate.append(" lms_football_match ");
		sqlUpdate.append("set ");
		// sqlUpdate.append(" name = ?,"); // 名称
		// sqlUpdate.append("match_id = ?,"); // 比赛ID
		sqlUpdate.append("match_code = ?,"); // 比赛代码
		sqlUpdate.append("match_num_cn = ?,"); // 周几第几场
		sqlUpdate.append("game_date = ?,"); // 比赛日期
		sqlUpdate.append("match_link = ?,"); // 比赛链接
		sqlUpdate.append("start_time = ?,"); // 开赛时间
		sqlUpdate.append("end_time = ?,"); // 代购截止时间
		sqlUpdate.append("home_team_id = ?,"); // 主队ID
		sqlUpdate.append("home_team_name = ?,"); // 主队名称
		sqlUpdate.append("home_team_rank = ?,"); // 主队排名
		sqlUpdate.append("away_team_id = ?,"); // 客队ID
		sqlUpdate.append("away_team_name = ?,"); // 客队名称
		sqlUpdate.append("away_team_rank = ?,"); // 客队排名
		sqlUpdate.append("league_id = ?,"); // 联赛id
		sqlUpdate.append("league_name = ?,"); // 联赛名称
		sqlUpdate.append("league_link = ?,"); // 联赛链接
		sqlUpdate.append("is_attention = ?,"); // 是否关注 0:未关注 1:已关注
		sqlUpdate.append("is_stop = ?,"); // 已出赛果 1:是 0:否
		sqlUpdate.append("is_hot = ?,"); // 是否特点赛事 1:是 0:否
		sqlUpdate.append("score = ?,"); // 比分,球赛结束前默认值为让球数
		sqlUpdate.append("win_odds = ?,"); // 主胜赔率
		sqlUpdate.append("draw_odds = ?,"); // 主平赔率
		sqlUpdate.append("lose_odds = ?,"); // 主负赔率
		sqlUpdate.append("concede_points = ?,"); // 让球数
		sqlUpdate.append("concede_win_odds = ?,"); // 让球主胜赔率
		sqlUpdate.append("concede_draw_odds = ?,"); // 让球主平赔率
		sqlUpdate.append("concede_lose_odds = ?,"); // 让球主负赔率

		// sqlUpdate.append("fisrt_half_score = ?, "); // 上半场比分
		// sqlUpdate.append("match_result = ?, "); // 胜平负赛果
		// sqlUpdate.append("concede_match_result = ?, "); // 让球胜平负赛果
		// sqlUpdate.append("goals = ?, "); // 总进球数
		// sqlUpdate.append("goals_result = ?, "); // 总进球数赛果
		// sqlUpdate.append("half_full_result = ?, "); // 半全场赛果
		// sqlUpdate.append("score_result = ?, "); // 比分赛果

		sqlUpdate.append(" update_date = ?,"); // 更新日期
		sqlUpdate.append(" update_by = ? "); // 更新者
		sqlUpdate.append("where id = ? "); // 比赛ID

		// 更新比分
		StringBuffer sqlUpdateScore = new StringBuffer();
		sqlUpdateScore.append("update ");
		sqlUpdateScore.append(" lms_football_match ");
		sqlUpdateScore.append("set ");
		sqlUpdateScore.append("is_stop = ?,"); // 已出赛果 1:是 0:否
		sqlUpdateScore.append("score = ?,"); // 比分,球赛结束前默认值为让球数

//		sqlUpdateScore.append("fisrt_half_score = ?, "); // 上半场比分
//		sqlUpdateScore.append("match_result = ?, "); // 胜平负赛果
//		sqlUpdateScore.append("concede_match_result = ?, "); // 让球胜平负赛果
//		sqlUpdateScore.append("goals = ?, "); // 总进球数
//		sqlUpdateScore.append("goals_result = ?, "); // 总进球数赛果
//		sqlUpdateScore.append("half_full_result = ?, "); // 半全场赛果
//		sqlUpdateScore.append("score_result = ?, "); // 比分赛果

		sqlUpdateScore.append(" update_date = ?,"); // 更新日期
		sqlUpdateScore.append(" update_by = ? "); // 更新者
		sqlUpdateScore.append("where id = ? "); // 比赛ID

		try {
			conn = MysqlConnectionPoolManager.getConnection();
			// 关闭事务自动提交
			conn.setAutoCommit(false);

			if (conn != null) {
				pstInsert = (PreparedStatement) conn.prepareStatement(sqlInsert
						.toString());
				pstUpdate = (PreparedStatement) conn.prepareStatement(sqlUpdate
						.toString());
				pstUpdateScore = (PreparedStatement) conn
						.prepareStatement(sqlUpdateScore.toString());

				for (FootballMatch footballMatch : footballMatchList) {
					Long id = footballMatch.getId();

					if (existFlag && existIdSet.contains(id)) {
						String isStop = footballMatch.getIsStop();
						if ("1".equals(isStop)) {
							pstUpdateScore.setString(1, "1");// 已出赛果 1:是 0:否
							pstUpdateScore.setString(2,
									footballMatch.getScore());// 比分,球赛结束前默认值为让球数
							// pstUpdateScore.setString(3,
							// footballMatch.getFisrtHalfScore());// 上半场比分
							// pstUpdateScore.setString(4,
							// footballMatch.getMatchResult());// 胜平负赛果
							// pstUpdateScore.setString(5,
							// footballMatch.getConcedeMatchResult());// 让球胜平负赛果
							// pstUpdateScore.setInt(6,
							// footballMatch.getGoals());// 总进球数
							// pstUpdateScore.setString(7,
							// footballMatch.getGoalsResult());// 总进球数赛果
							// pstUpdateScore.setString(8,
							// footballMatch.getHalfFullResult());// 半全场赛果
							// pstUpdateScore.setString(9,
							// footballMatch.getScoreResult());// 比分赛果
							pstUpdateScore.setString(3, DateUtil.getTime(System
									.currentTimeMillis()));
							pstUpdateScore.setString(4, "admin");
							pstUpdateScore.setLong(5, footballMatch.getId());
							// 把一个SQL命令加入命令列表
							pstUpdateScore.addBatch();
						} else {
							// 更新
							pstUpdate
									.setString(1, footballMatch.getMatchCode());// 比赛代码
							pstUpdate.setString(2,
									footballMatch.getMatchNumCn());// 周几第几场
							pstUpdate.setString(3, footballMatch.getGameDate());// 比赛日期
							pstUpdate
									.setString(4, footballMatch.getMatchLink());// 比赛链接
							pstUpdate
									.setString(5, footballMatch.getStartTime());// 开赛时间
							pstUpdate.setString(6, footballMatch.getEndTime());// 代购截止时间
							pstUpdate.setString(7,
									footballMatch.getHomeTeamId());// 主队ID
							pstUpdate.setString(8,
									footballMatch.getHomeTeamName());// 主队名称
							pstUpdate.setString(9,
									footballMatch.getHomeTeamRank());// 主队排名
							pstUpdate.setString(10,
									footballMatch.getAwayTeamId());// 客队ID
							pstUpdate.setString(11,
									footballMatch.getAwayTeamName());// 客队名称
							pstUpdate.setString(12,
									footballMatch.getAwayTeamRank());// 客队排名
							pstUpdate
									.setString(13, footballMatch.getLeagueId());// 联赛id
							pstUpdate.setString(14,
									footballMatch.getLeagueName());// 联赛名称
							pstUpdate.setString(15,
									footballMatch.getLeagueLink());// 联赛链接
							pstUpdate.setString(16,
									footballMatch.getIsAttention());// 是否关注
																	// 0:未关注
																	// 1:已关注
							pstUpdate.setString(17, "0");// 已出赛果 1:是 0:否
							pstUpdate.setString(18, footballMatch.getIsHot());// 是否特点赛事
																				// 1:是
																				// 0:否
							pstUpdate.setString(19, footballMatch.getScore());// 比分,球赛结束前默认值为让球数
							pstUpdate.setBigDecimal(20,
									footballMatch.getWinOdds());// 主胜赔率
							pstUpdate.setBigDecimal(21,
									footballMatch.getDrawOdds());// 主平赔率
							pstUpdate.setBigDecimal(22,
									footballMatch.getLoseOdds());// 主负赔率
							pstUpdate.setBigDecimal(23,
									footballMatch.getConcedePoints());// 让球数
							pstUpdate.setBigDecimal(24,
									footballMatch.getConcedeWinOdds());// 让球主胜赔率
							pstUpdate.setBigDecimal(25,
									footballMatch.getConcedeDrawOdds());// 让球主平赔率
							pstUpdate.setBigDecimal(26,
									footballMatch.getConcedeLoseOdds());// 让球主负赔率
							// pstUpdate.setString(27,
							// footballMatch.getFisrtHalfScore());// 上半场比分
							// pstUpdate.setString(28,
							// footballMatch.getMatchResult());// 胜平负赛果
							// pstUpdate.setString(29,
							// footballMatch.getConcedeMatchResult());// 让球胜平负赛果
							// pstUpdate.setInt(30, footballMatch.getGoals());//
							// 总进球数
							// pstUpdate.setString(31,
							// footballMatch.getGoalsResult());// 总进球数赛果
							// pstUpdate.setString(32,
							// footballMatch.getHalfFullResult());// 半全场赛果
							// pstUpdate.setString(33,
							// footballMatch.getScoreResult());// 比分赛果
							pstUpdate.setString(27, DateUtil.getTime(System
									.currentTimeMillis()));
							pstUpdate.setString(28, "admin");
							pstUpdate.setLong(29, id);
							// 把一个SQL命令加入命令列表
							pstUpdate.addBatch();
						}
					} else {
						// 新增
						pstInsert.setLong(1, footballMatch.getId());
						pstInsert.setString(2, footballMatch.getMatchCode());// 比赛代码
						pstInsert.setString(3, footballMatch.getMatchNumCn());// 周几第几场
						pstInsert.setString(4, footballMatch.getGameDate());// 比赛日期
						pstInsert.setString(5, footballMatch.getMatchLink());// 比赛链接
						pstInsert.setString(6, footballMatch.getStartTime());// 开赛时间
						pstInsert.setString(7, footballMatch.getEndTime());// 代购截止时间
						pstInsert.setString(8, footballMatch.getHomeTeamId());// 主队ID
						pstInsert.setString(9, footballMatch.getHomeTeamName());// 主队名称
						pstInsert
								.setString(10, footballMatch.getHomeTeamRank());// 主队排名
						pstInsert.setString(11, footballMatch.getAwayTeamId());// 客队ID
						pstInsert
								.setString(12, footballMatch.getAwayTeamName());// 客队名称
						pstInsert
								.setString(13, footballMatch.getAwayTeamRank());// 客队排名
						pstInsert.setString(14, footballMatch.getLeagueId());// 联赛id
						pstInsert.setString(15, footballMatch.getLeagueName());// 联赛名称
						pstInsert.setString(16, footballMatch.getLeagueLink());// 联赛链接
						pstInsert.setString(17, footballMatch.getIsAttention());// 是否关注
																				// 0:未关注
																				// 1:已关注
						pstInsert.setString(18, footballMatch.getIsStop());// 已出赛果
																			// 1:是
																			// 0:否
						pstInsert.setString(19, footballMatch.getIsHot());// 是否特点赛事
																			// 1:是
																			// 0:否
						pstInsert.setString(20, footballMatch.getScore());// 比分,球赛结束前默认值为让球数
						pstInsert.setBigDecimal(21, footballMatch.getWinOdds());// 主胜赔率
						pstInsert
								.setBigDecimal(22, footballMatch.getDrawOdds());// 主平赔率
						pstInsert
								.setBigDecimal(23, footballMatch.getLoseOdds());// 主负赔率
						pstInsert.setBigDecimal(24,
								footballMatch.getConcedePoints());// 让球数
						pstInsert.setBigDecimal(25,
								footballMatch.getConcedeWinOdds());// 让球主胜赔率
						pstInsert.setBigDecimal(26,
								footballMatch.getConcedeDrawOdds());// 让球主平赔率
						pstInsert.setBigDecimal(27,
								footballMatch.getConcedeLoseOdds());// 让球主负赔率

						// pstInsert.setString(28,
						// footballMatch.getFisrtHalfScore());// 上半场比分
						// pstInsert.setString(29,
						// footballMatch.getMatchResult());// 胜平负赛果
						// pstInsert.setString(30,
						// footballMatch.getConcedeMatchResult());// 让球胜平负赛果
						// pstInsert.setInt(31, footballMatch.getGoals());//
						// 总进球数
						// pstInsert.setString(32,
						// footballMatch.getGoalsResult());// 总进球数赛果
						// pstInsert.setString(33,
						// footballMatch.getHalfFullResult());// 半全场赛果
						// pstInsert.setString(34,
						// footballMatch.getScoreResult());// 比分赛果

						pstInsert.setString(28, "0");// 删除标记（0：正常；1：删除；2：审核）
						String dt = DateUtil
								.getTime(System.currentTimeMillis());
						pstInsert.setString(29, dt);// 创建日期
						pstInsert.setString(30, "admin");// 创建者
						pstInsert.setString(31, dt);// 更新日期
						pstInsert.setString(32, "admin");// 更新者
						pstInsert.setNull(33, Types.VARCHAR);// 备注
						// 把一个SQL命令加入命令列表
						pstInsert.addBatch();
					}
				}

				// 执行批量更新
				pstUpdate.executeBatch();
				pstUpdateScore.executeBatch();
				pstInsert.executeBatch();
				// 语句执行完毕，提交本事务
				conn.commit();
				logger.info("批量导入每日赛事信息成功！");
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		} finally {
			MysqlConnectionPoolManager.closePreparedStatement(pstInsert);
			MysqlConnectionPoolManager.closePreparedStatement(pstUpdate);
			MysqlConnectionPoolManager.closePreparedStatement(pstUpdateScore);
			MysqlConnectionPoolManager.closeConnection(conn);
		}
	}

	/**
	 * 批量更新赛事结果
	 * @param footballMatchList
	 */
	public static void batchUpdateFootballMatchResult(List<FootballMatch> footballMatchList) {
		
		Connection conn = null;
		PreparedStatement pstUpdateScore = null;
		
		// 更新比分
		StringBuffer sqlUpdateScore = new StringBuffer();
		sqlUpdateScore.append("update ");
		sqlUpdateScore.append(" lms_football_match ");
		sqlUpdateScore.append("set ");
		sqlUpdateScore.append("score = ?,");	// 比分,球赛结束前默认值为让球数
		sqlUpdateScore.append("fisrt_half_score = ?, ");		// 上半场比分
		sqlUpdateScore.append("match_result = ?, ");		// 胜平负赛果
		sqlUpdateScore.append("concede_match_result = ?, ");		// 让球胜平负赛果
		sqlUpdateScore.append("goals = ?, ");		// 总进球数
		sqlUpdateScore.append("goals_result = ?, ");		// 总进球数赛果
		sqlUpdateScore.append("half_full_result = ?, ");		// 半全场赛果
		sqlUpdateScore.append("score_result = ?, ");		// 比分赛果
		sqlUpdateScore.append(" update_date = ?,");	// 更新日期
		sqlUpdateScore.append(" update_by = ? ");		// 更新者
		sqlUpdateScore.append("where id = ? ");		//比赛ID
		
		try {
			conn = MysqlConnectionPoolManager.getConnection();
			// 关闭事务自动提交
			conn.setAutoCommit(false);
			
			if (conn != null) {
				pstUpdateScore = (PreparedStatement) conn.prepareStatement(sqlUpdateScore.toString());
				
				for (FootballMatch footballMatch : footballMatchList) {
					pstUpdateScore.setString(1, footballMatch.getScore());// 比分,球赛结束前默认值为让球数
					pstUpdateScore.setString(2, footballMatch.getFisrtHalfScore());// 上半场比分
					pstUpdateScore.setString(3, footballMatch.getMatchResult());// 胜平负赛果
					pstUpdateScore.setString(4, footballMatch.getConcedeMatchResult());// 让球胜平负赛果
					pstUpdateScore.setInt(5, footballMatch.getGoals());// 总进球数
					pstUpdateScore.setString(6, footballMatch.getGoalsResult());// 总进球数赛果
					pstUpdateScore.setString(7, footballMatch.getHalfFullResult());// 半全场赛果
					pstUpdateScore.setString(8, footballMatch.getScoreResult());// 比分赛果
					pstUpdateScore.setString(9, DateUtil.getTime(System.currentTimeMillis()));
					pstUpdateScore.setString(10, "admin");
					pstUpdateScore.setLong(11, footballMatch.getId());
					// 把一个SQL命令加入命令列表
					pstUpdateScore.addBatch();
				}
				
				// 执行批量更新
				pstUpdateScore.executeBatch();
				// 语句执行完毕，提交本事务
				conn.commit();
				logger.info("批量更新赛事结果信息成功！");
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		} finally {
			MysqlConnectionPoolManager.closePreparedStatement(pstUpdateScore);
			MysqlConnectionPoolManager.closeConnection(conn);
		}
	}

	/**
	 * 获取每日赛事信息列表
	 * 
	 * @param sql
	 * @return
	 */
	public static List<FootballMatch> getFootballMatchList(String sql) {

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<FootballMatch> list = new ArrayList<FootballMatch>();

		try {
			conn = MysqlConnectionPoolManager.getConnection();
			if (conn != null) {
				stmt = conn.createStatement();
				if (null == sql || StringUtils.isBlank(sql)) {
					StringBuffer sb = new StringBuffer();
					sb.append("SELECT ");
					sb.append("id,"); // 比赛ID
					sb.append("match_code,"); // 比赛代码
					sb.append("match_num_cn,"); // 周几第几场
					sb.append("game_date,"); // 比赛日期
					sb.append("match_link,"); // 比赛链接
					sb.append("start_time,"); // 开赛时间
					sb.append("end_time,"); // 代购截止时间
					sb.append("home_team_id,"); // 主队ID
					sb.append("home_team_name,"); // 主队名称
					sb.append("home_team_rank,"); // 主队排名
					sb.append("away_team_id,"); // 客队ID
					sb.append("away_team_name,"); // 客队名称
					sb.append("away_team_rank,"); // 客队排名
					sb.append("league_id,"); // 联赛id
					sb.append("league_name,"); // 联赛名称
					sb.append("league_link,"); // 联赛链接
					sb.append("is_attention,"); // 是否关注 0:未关注 1:已关注
					sb.append("is_stop,"); // 已出赛果 1:是 0:否
					sb.append("is_hot,"); // 是否特点赛事 1:是 0:否
					sb.append("score,"); // 比分,球赛结束前默认值为让球数
					sb.append("win_odds,"); // 主胜赔率
					sb.append("draw_odds,"); // 主平赔率
					sb.append("lose_odds,"); // 主负赔率
					sb.append("concede_points,"); // 让球数
					sb.append("concede_win_odds,"); // 让球主胜赔率
					sb.append("concede_draw_odds,"); // 让球主平赔率
					sb.append("concede_lose_odds,"); // 让球主负赔率
					sb.append("del_flag,"); // 删除标记（0：正常；1：删除；2：审核）
					sb.append("create_date,"); // 创建日期
					sb.append("create_by,"); // 创建者
					sb.append("update_date,"); // 更新日期
					sb.append("update_by,"); // 更新者
					sb.append("remarks"); // 备注
					sb.append(" FROM ");
					sb.append(" lms_football_match "); // 表名
					sql = sb.toString();
				}
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					FootballMatch footballMatch = new FootballMatch();
					footballMatch.setId(rs.getLong(1));
					footballMatch.setMatchCode(rs.getString(2));
					footballMatch.setMatchNumCn(rs.getString(3));
					footballMatch.setGameDate(rs.getString(4));
					footballMatch.setMatchLink(rs.getString(5));
					footballMatch.setStartTime(rs.getString(6));
					footballMatch.setEndTime(rs.getString(7));
					footballMatch.setHomeTeamId(rs.getString(8));
					footballMatch.setHomeTeamName(rs.getString(9));
					footballMatch.setHomeTeamRank(rs.getString(10));
					footballMatch.setAwayTeamId(rs.getString(11));
					footballMatch.setAwayTeamName(rs.getString(12));
					footballMatch.setAwayTeamRank(rs.getString(13));
					footballMatch.setLeagueId(rs.getString(14));
					footballMatch.setLeagueName(rs.getString(15));
					footballMatch.setLeagueLink(rs.getString(16));
					footballMatch.setIsAttention(rs.getString(17));
					footballMatch.setIsStop(rs.getString(18));
					footballMatch.setIsHot(rs.getString(19));
					footballMatch.setScore(rs.getString(20));
					footballMatch.setWinOdds(rs.getBigDecimal(21));
					footballMatch.setDrawOdds(rs.getBigDecimal(22));
					footballMatch.setLoseOdds(rs.getBigDecimal(23));
					footballMatch.setConcedePoints(rs.getBigDecimal(24));
					footballMatch.setConcedeWinOdds(rs.getBigDecimal(25));
					footballMatch.setConcedeDrawOdds(rs.getBigDecimal(26));
					footballMatch.setConcedeLoseOdds(rs.getBigDecimal(27));

					list.add(footballMatch);
				}
				logger.info("已获取每日赛事信息");
			}

		} catch (SQLException e) {
			logger.error(e.getMessage());
		} finally {
			MysqlConnectionPoolManager.closeResultSet(rs);
			MysqlConnectionPoolManager.closeStatement(stmt);
			MysqlConnectionPoolManager.closeConnection(conn);
		}
		return list;
	}

	public static List<FootballMatch> getFootballMatchListByDate(String date) {

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<FootballMatch> list = new ArrayList<FootballMatch>();

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ");
		sb.append("id,"); // 比赛ID
		sb.append("match_code,"); // 比赛代码
		sb.append("match_num_cn,"); // 周几第几场
		sb.append("game_date,"); // 比赛日期
		sb.append("match_link,"); // 比赛链接
		sb.append("start_time,"); // 开赛时间
		sb.append("end_time,"); // 代购截止时间
		sb.append("home_team_id,"); // 主队ID
		sb.append("home_team_name,"); // 主队名称
		sb.append("home_team_rank,"); // 主队排名
		sb.append("away_team_id,"); // 客队ID
		sb.append("away_team_name,"); // 客队名称
		sb.append("away_team_rank,"); // 客队排名
		sb.append("league_id,"); // 联赛id
		sb.append("league_name,"); // 联赛名称
		sb.append("league_link,"); // 联赛链接
		sb.append("is_attention,"); // 是否关注 0:未关注 1:已关注
		sb.append("is_stop,"); // 已出赛果 1:是 0:否
		sb.append("is_hot,"); // 是否特点赛事 1:是 0:否
		sb.append("score,"); // 比分,球赛结束前默认值为让球数
		sb.append("win_odds,"); // 主胜赔率
		sb.append("draw_odds,"); // 主平赔率
		sb.append("lose_odds,"); // 主负赔率
		sb.append("concede_points,"); // 让球数
		sb.append("concede_win_odds,"); // 让球主胜赔率
		sb.append("concede_draw_odds,"); // 让球主平赔率
		sb.append("concede_lose_odds,"); // 让球主负赔率
		sb.append("fisrt_half_score,"); // 上半场比分
		sb.append("match_result,"); // 胜平负赛果
		sb.append("concede_match_result,"); // 让球胜平负赛果
		sb.append("goals,"); // 总进球数
		sb.append("goals_result,"); // 总进球数赛果
		sb.append("half_full_result,"); // 半全场赛果
		sb.append("score_result,"); // 比分赛果
		sb.append("del_flag,"); // 删除标记（0：正常；1：删除；2：审核）
		sb.append("create_date,"); // 创建日期
		sb.append("create_by,"); // 创建者
		sb.append("update_date,"); // 更新日期
		sb.append("update_by,"); // 更新者
		sb.append("remarks"); // 备注
		sb.append(" FROM ");
		sb.append(" lms_football_match "); // 表名
		sb.append(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(date)) {
			sb.append(" AND start_time BETWEEN '").append(date).append(" 00:00:00' AND '").append(date).append(" 23:59:59' ");
		}

		try {
			conn = MysqlConnectionPoolManager.getConnection();
			if (conn != null) {
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sb.toString());
				while (rs.next()) {
					FootballMatch footballMatch = new FootballMatch();
					footballMatch.setId(rs.getLong(1));
					footballMatch.setMatchCode(rs.getString(2));
					footballMatch.setMatchNumCn(rs.getString(3));
					footballMatch.setGameDate(rs.getString(4));
					footballMatch.setMatchLink(rs.getString(5));
					footballMatch.setStartTime(rs.getString(6));
					footballMatch.setEndTime(rs.getString(7));
					footballMatch.setHomeTeamId(rs.getString(8));
					footballMatch.setHomeTeamName(rs.getString(9));
					footballMatch.setHomeTeamRank(rs.getString(10));
					footballMatch.setAwayTeamId(rs.getString(11));
					footballMatch.setAwayTeamName(rs.getString(12));
					footballMatch.setAwayTeamRank(rs.getString(13));
					footballMatch.setLeagueId(rs.getString(14));
					footballMatch.setLeagueName(rs.getString(15));
					footballMatch.setLeagueLink(rs.getString(16));
					footballMatch.setIsAttention(rs.getString(17));
					footballMatch.setIsStop(rs.getString(18));
					footballMatch.setIsHot(rs.getString(19));
					footballMatch.setScore(rs.getString(20));
					footballMatch.setWinOdds(rs.getBigDecimal(21));
					footballMatch.setDrawOdds(rs.getBigDecimal(22));
					footballMatch.setLoseOdds(rs.getBigDecimal(23));
					footballMatch.setConcedePoints(rs.getBigDecimal(24));
					footballMatch.setConcedeWinOdds(rs.getBigDecimal(25));
					footballMatch.setConcedeDrawOdds(rs.getBigDecimal(26));
					footballMatch.setConcedeLoseOdds(rs.getBigDecimal(27));
					footballMatch.setFisrtHalfScore(rs.getString(28));
					footballMatch.setMatchResult(rs.getString(29));
					footballMatch.setConcedeMatchResult(rs.getString(30));
					footballMatch.setGoals(rs.getInt(31));
					footballMatch.setGoalsResult(rs.getString(32));
					footballMatch.setHalfFullResult(rs.getString(33));
					footballMatch.setScoreResult(rs.getString(34));
					list.add(footballMatch);
				}
				logger.info("已获取" + date + "赛事信息");
			}

		} catch (SQLException e) {
			logger.error(e.getMessage());
		} finally {
			MysqlConnectionPoolManager.closeResultSet(rs);
			MysqlConnectionPoolManager.closeStatement(stmt);
			MysqlConnectionPoolManager.closeConnection(conn);
		}
		return list;
	}

	public static Set<String> getDateToUpdate() {

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Set<String> set = new HashSet<String>();

		try {
			conn = MysqlConnectionPoolManager.getConnection();
			if (conn != null) {
				stmt = conn.createStatement();
				rs = stmt
						.executeQuery("SELECT DISTINCT game_date FROM lms_football_match WHERE score IS NOT NULL");
				while (rs.next()) {
					set.add(rs.getString(1));
				}
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		} finally {
			MysqlConnectionPoolManager.closeResultSet(rs);
			MysqlConnectionPoolManager.closeStatement(stmt);
			MysqlConnectionPoolManager.closeConnection(conn);
		}
		return set;
	}

	public static Set<Long> getExistFootballMatchIdSet(String sql) {

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Set<Long> set = new HashSet<Long>();

		try {
			conn = MysqlConnectionPoolManager.getConnection();
			if (conn != null) {
				stmt = conn.createStatement();
				if (StringUtils.isBlank(sql)) {
					sql = "SELECT id FROM lms_football_match";
				}
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					set.add(rs.getLong(1));
				}
				logger.info("已获取比赛ID集合信息");
			}

		} catch (SQLException e) {
			logger.error(e.getMessage());
		} finally {
			MysqlConnectionPoolManager.closeResultSet(rs);
			MysqlConnectionPoolManager.closeStatement(stmt);
			MysqlConnectionPoolManager.closeConnection(conn);
		}
		return set;
	}

	/**
	 * 获取已有赛事日期
	 * 
	 * @param sql
	 * @return
	 */
	public static List<String> getExistMatchDateList(String dateBegin,
			String dateEnd) {

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<String> list = new ArrayList<String>();

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT DISTINCT game_date FROM lms_football_match WHERE 1=1 ");
		if (StringUtils.isNotBlank(dateBegin)) {
			sb.append(" AND game_date >= '").append(dateBegin).append("'");
		}
		if (StringUtils.isNotBlank(dateEnd)) {
			sb.append(" AND dateEnd <= '").append(dateEnd).append("'");
		}
		sb.append(" ORDER BY game_date");

		try {
			conn = MysqlConnectionPoolManager.getConnection();
			if (conn != null) {
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sb.toString());
				while (rs.next()) {
					list.add(rs.getString(1));
				}
				logger.info("已获取赛事信息");
			}

		} catch (SQLException e) {
			logger.error(e.getMessage());
		} finally {
			MysqlConnectionPoolManager.closeResultSet(rs);
			MysqlConnectionPoolManager.closeStatement(stmt);
			MysqlConnectionPoolManager.closeConnection(conn);
		}
		return list;
	}

}