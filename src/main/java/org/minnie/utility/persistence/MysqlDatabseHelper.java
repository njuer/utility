package org.minnie.utility.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.minnie.autocode.NamingRuleConvert;
import org.minnie.utility.entity.lottery.FiveInEleven;
import org.minnie.utility.entity.lottery.SuperLotto;
import org.minnie.utility.module.netease.Article;
import org.minnie.utility.module.netease.FootballLeague;
import org.minnie.utility.module.netease.FootballTeam;
import org.minnie.utility.module.sohu.DoubleColor;
import org.minnie.utility.module.xinyingba.Video;
import org.minnie.utility.util.Constant;
import org.minnie.utility.util.DateUtil;

public class MysqlDatabseHelper {

	private static Logger logger = Logger.getLogger(MysqlDatabseHelper.class
			.getName());

	public static Pattern urlPattern = Pattern.compile(Constant.REG_URL);
	
	public static List<String[]> getTableInfo(String jdbcTable) {
		
		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSetMetaData rsmd = null;
		DatabaseMetaData dmd = null;
		
		List<String[]> list = new ArrayList<String[]>();
		Set<String> ignoreColumnSet = new HashSet<String>();
		ignoreColumnSet.add("id");//主键
		ignoreColumnSet.add("name");//名称
		ignoreColumnSet.add("remarks");//备注
		ignoreColumnSet.add("createBy");//创建者
		ignoreColumnSet.add("createDate");//创建日期
		ignoreColumnSet.add("updateBy");//更新者
		ignoreColumnSet.add("updateDate");//更新日期
		ignoreColumnSet.add("delFlag");//删除标记（0：正常；1：删除；2：审核）
		
		try {
			conn = MysqlConnectionManager.getConnection();
			pstmt = conn.prepareStatement("SELECT * FROM " + jdbcTable);
			rs = pstmt.executeQuery();
			rsmd = rs.getMetaData(); // 获取字段名
			dmd = conn.getMetaData();
			
			// 获取字段注释
			ResultSet rsColumns = dmd.getColumns(null, "%", jdbcTable, "%");
			List<String> comments = new ArrayList<String>();
			while (rsColumns.next()) {
//				System.out.println(rsColumns.getString("COLUMN_NAME") + "----" + rsColumns.getString("REMARKS"));
				comments.add(rsColumns.getString("REMARKS"));
			}

			
			if (rsmd != null) {
				int count = rsmd.getColumnCount();
				for (int i = 1; i <= count; i++) {
					String javaType = rsmd.getColumnClassName(i);
					int columnDisplaySize = rsmd.getColumnDisplaySize(i);
					String columnName = rsmd.getColumnName(i);
					String propertyName = NamingRuleConvert.replaceUnderlineAndfirstToUpper(columnName, "_", "");
					if(ignoreColumnSet.contains(propertyName)){
						continue;
					}
//					String len = null;
					javaType = javaType.substring(javaType.lastIndexOf(".") + 1);
					if ("Timestamp".equals(javaType)) {
						javaType = "Date";
					}
//					if ("String".equals(javaType)) {
//						len = String.valueOf(columnDisplaySize);
//					}
					String[] str = new String[] {javaType, propertyName, columnName, comments.get(i - 1), String.valueOf(columnDisplaySize)};
					list.add(str);
				}
			}
		} catch (SQLException ex2) {
			ex2.printStackTrace();
		} catch (Exception ex2) {
			ex2.printStackTrace();
		} finally {
			MysqlConnectionManager.closeStatement(stmt);
			MysqlConnectionManager.closePreparedStatement(pstmt);
			MysqlConnectionManager.closeConnection(conn);
		}
		
		return list;
	}

	/**
	 * 批量导入影片信息[不包含缺二进制海报图片]
	 * 
	 * @param list
	 *            影片列表
	 */
	public static void batchAddVideoWithoutImage(List<Video> list, String sql) {

		Connection conn = null;
		PreparedStatement pst = null;

		try {
			conn = MysqlConnectionManager.getConnection();
			// 关闭事务自动提交
			conn.setAutoCommit(false);

			if (conn != null) {
				if (null == sql || StringUtils.isBlank(sql)) {
					sql = "insert into ent_movie (uuid, number, title, url, imageSource, category, year, starring, rate, introduction) VALUES (?,?,?,?,?,?,?,?,?,?)";
				}
				pst = (PreparedStatement) conn.prepareStatement(sql);
				for (Video video : list) {
					pst.setString(1, UUID.randomUUID().toString());
					pst.setInt(2, video.getNumber());
					pst.setString(3, video.getTitle());
					pst.setString(4, video.getUrl());
					pst.setString(5, video.getImageSource());
					pst.setString(6, video.getCategory());
					pst.setInt(7, video.getYear());
					pst.setString(8, video.getStarring());
					pst.setFloat(9, video.getRate());
					pst.setString(10, video.getIntroduction());
					// 把一个SQL命令加入命令列表
					pst.addBatch();
				}

				// 执行批量更新
				pst.executeBatch();
				// 语句执行完毕，提交本事务
				conn.commit();
				logger.info("批量导入影片信息[缺图片]成功！");
			}
		} catch (SQLException e) {
			logger.error("SQLException[MysqlDatabseHelper->batchAddVideo]: "
					+ e.getMessage());
		} finally {
			MysqlConnectionManager.closePreparedStatement(pst);
			MysqlConnectionManager.closeConnection(conn);
		}
	}

	/**
	 * 批量导入影片信息[包含缺二进制海报图片]
	 * 
	 * @param list
	 *            影片列表
	 */
	public static void batchAddVideo(List<Video> list, String sql) {

		Connection conn = null;
		PreparedStatement pst = null;

		try {
			conn = MysqlConnectionManager.getConnection();
			// 关闭事务自动提交
			conn.setAutoCommit(false);

			if (conn != null) {

				if (null == sql || StringUtils.isBlank(sql)) {
					sql = "insert into ent_movie (uuid, number, title, url, imageSource, image, category, year, starring, rate, introduction) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
				}

				pst = (PreparedStatement) conn.prepareStatement(sql);
				for (Video video : list) {
					pst.setString(1, UUID.randomUUID().toString());
					pst.setInt(2, video.getNumber());
					pst.setString(3, video.getTitle());
					pst.setString(4, video.getUrl());
					String imgSrcUrl = video.getImageSource();
					pst.setString(5, imgSrcUrl);
					// InputStream is = ImageUtil.getInputStream(imgSrcUrl);
					// if(null != is){
					// pst.setBinaryStream(6, is, is.available());
					// } else {
					// pst.setNull(6, java.sql.Types.BLOB);
					// }
					pst.setString(7, video.getCategory());
					pst.setInt(8, video.getYear());
					pst.setString(9, video.getStarring());
					pst.setFloat(10, video.getRate());
					pst.setString(11, video.getIntroduction());
					// 把一个SQL命令加入命令列表
					pst.addBatch();
				}

				// 执行批量更新
				pst.executeBatch();
				// 语句执行完毕，提交本事务
				conn.commit();
				logger.info("批量导入影片信息成功！");
			}
		} catch (SQLException e) {
			// e.printStackTrace();
			logger.error("SQLException[MysqlDatabseHelper->batchAddVideo]: "
					+ e.getMessage());
		}
		// catch (IOException e) {
		// // e.printStackTrace();
		// logger.error("IOException[MysqlDatabseHelper->batchAddVideo]: "
		// + e.getMessage());
		// }
		finally {
			MysqlConnectionManager.closePreparedStatement(pst);
			MysqlConnectionManager.closeConnection(conn);
		}
	}

	/**
	 * 获取影片列表
	 * 
	 * @param sql
	 *            执行SQL
	 * @return
	 */
	public static List<Video> getVideoList(String sql) {

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<Video> list = new ArrayList<Video>();

		try {
			conn = MysqlConnectionManager.getConnection();
			if (conn != null) {
				stmt = conn.createStatement();
				if (null == sql || StringUtils.isBlank(sql)) {
					sql = "SELECT uuid,number,imageSource FROM ent_movie WHERE number != 1";
				}
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					Video video = new Video();
					video.setUuid(rs.getString(1));
					video.setNumber(rs.getInt(2));
					video.setImageSource(rs.getString(3));
					// logger.debug(video.toString());
					list.add(video);
				}
				logger.info("已获取影片海报地址信息");
			}

		} catch (SQLException e) {
			logger.error("SQLException[MysqlDatabseHelper->getVideoList]: "
					+ e.getMessage());
			// e.printStackTrace();
		} finally {
			MysqlConnectionManager.closeResultSet(rs);
			MysqlConnectionManager.closeStatement(stmt);
			MysqlConnectionManager.closeConnection(conn);
		}
		return list;
	}

	/**
	 * 批量更新图片
	 * 
	 * @param list
	 *            影片列表
	 * @param sql
	 *            执行SQL
	 */
	public static void batchUpdateImage(List<Video> list, String sql) {

		Connection conn = null;
		PreparedStatement pst = null;

		try {
			conn = MysqlConnectionManager.getConnection();
			// 关闭事务自动提交
			conn.setAutoCommit(false);

			if (conn != null) {

				if (null == sql || StringUtils.isBlank(sql)) {
					sql = "UPDATE ent_movie SET image = ? WHERE uuid = ?";
				}
				pst = (PreparedStatement) conn.prepareStatement(sql);

				int size = list.size();
				int batchSize = MysqlConnectionManager.batchSize;
				logger.info("batchSize = " + batchSize);
				for (int i = 0; i < size; i++) {
					Video video = list.get(i);

					URL url;
					InputStream is = null;
					String imageSource = video.getImageSource();
					Matcher matcher = urlPattern.matcher(imageSource);
					if (matcher.find()) {
						imageSource = matcher.group();
					} else if (Constant.IMG_NO_PIC.equals(imageSource)) {
						imageSource = Constant.URL_XINYINGBA + imageSource;
					} else {
						continue;
					}

					try {
						url = new URL(imageSource);

						HttpURLConnection httpUrlConnection = (HttpURLConnection) url
								.openConnection();

						// 设定timeout时间
						httpUrlConnection.setConnectTimeout(15 * 1000);
						httpUrlConnection.setReadTimeout(15 * 1000);
						httpUrlConnection.setUseCaches(false);

						httpUrlConnection.connect();// 连接

						int status = httpUrlConnection.getResponseCode();
						int length = httpUrlConnection.getContentLength();

						if (HttpURLConnection.HTTP_OK == status) {
							is = httpUrlConnection.getInputStream();
							if (null != is) {
								pst.setBinaryStream(1, is, length);
								// is.close();
							}

							logger.info("下载图片:" + imageSource);
						} else {
							switch (status) {
							case HttpURLConnection.HTTP_FORBIDDEN:// 403
								logger.error("[403]:连接网址[" + imageSource
										+ "]禁止!");
								break;
							case HttpURLConnection.HTTP_NOT_FOUND:// 404
								logger.error("[404]:连接网址[" + imageSource
										+ "]不存在!");
								break;
							case HttpURLConnection.HTTP_INTERNAL_ERROR:// 500
								logger.error("[500]:连接网址[" + imageSource
										+ "]错误或不存在!");
								break;
							case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:// 504
								logger.error("[504]:连接网址[" + imageSource
										+ "]超时!");
								break;
							default:
								logger.error("下载失败: " + imageSource
										+ "，status code = " + status);
							}
							pst.setNull(1, java.sql.Types.BLOB);
						}

						pst.setString(2, video.getUuid());
						// 把一个SQL命令加入命令列表
						pst.addBatch();

					} catch (MalformedURLException mue) {
						logger.error("MalformedURLException[ImageUtil->getInputStream]-网址["
								+ imageSource + "]格式错误: " + mue.getMessage());
					} catch (IOException e) {
						logger.error("IOException[ImageUtil->getInputStream]-网络连接有问题: "
								+ e.getMessage());
					} finally {

					}
					if ((i + 1) % batchSize == 0) {

						// 执行批量更新
						pst.executeBatch();
						// 语句执行完毕，提交本事务
						conn.commit();
						logger.info("i=" + i + "->提交");
						pst.clearBatch();
					}
				}
				pst.executeBatch();
				// 语句执行完毕，提交本事务
				conn.commit();
				logger.info("批量更新影片海报信息成功！");
			}
		} catch (SQLException e) {
			logger.error("SQLException[MysqlDatabseHelper->batchUpdateImage]: "
					+ e.getMessage());
		} finally {
			MysqlConnectionManager.closePreparedStatement(pst);
			MysqlConnectionManager.closeConnection(conn);
		}
	}

	public static void batchUpdateImageWithFlag(String sql,
			String sourceFileDirectory) {

		Connection conn = null;
		PreparedStatement pst = null;

		try {
			conn = MysqlConnectionManager.getConnection();
			// 关闭事务自动提交
			conn.setAutoCommit(false);

			if (conn != null) {

				if (null == sql || StringUtils.isBlank(sql)) {
					sql = "UPDATE ent_movie SET image = ?, image_flag = ? WHERE number = ?";
				}
				pst = (PreparedStatement) conn.prepareStatement(sql);

				int batchSize = MysqlConnectionManager.batchSize;
				logger.info("batchSize = " + batchSize);

				File sourceDir = new File(sourceFileDirectory);
				if (sourceDir.exists()) {
					File[] sourceFiles = sourceDir.listFiles();
					int sourceFileLength = sourceFiles.length;
					for (int i = 0; i < sourceFileLength; i++) {
						String picturePath = sourceFiles[i].getAbsolutePath();
						File picture = new File(picturePath);
						InputStream fis = new FileInputStream(picturePath);
						if (null != fis) {
							pst.setBinaryStream(1, fis, picture.length());
							pst.setInt(2, 1);
							String name = sourceFiles[i].getName();
							String number = name
									.substring(0, name.indexOf("."));
							// if (StringUtils.isNumeric(number)) {
							// fileSet.add(Integer.valueOf(number));
							// }
							pst.setInt(3, Integer.valueOf(number));
							// 把一个SQL命令加入命令列表
							pst.addBatch();
							// is.close();
							// fis.close();
							logger.info(picturePath);
						}
						if ((i + 1) % batchSize == 0) {

							// 执行批量更新
							pst.executeBatch();
							// 语句执行完毕，提交本事务
							conn.commit();
							logger.info("i=" + i + "->提交");
							pst.clearBatch();
						}
					}
					pst.executeBatch();
					// 语句执行完毕，提交本事务
					conn.commit();
					logger.info("批量更新影片海报信息成功！");
				}
			}
		} catch (SQLException e) {
			logger.error("SQLException[MysqlDatabseHelper->batchUpdateImage]: "
					+ e.getMessage());
		} catch (FileNotFoundException e) {
			logger.error("FileNotFoundException[MysqlDatabseHelper->batchUpdateImage]: "
					+ e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			MysqlConnectionManager.closePreparedStatement(pst);
			MysqlConnectionManager.closeConnection(conn);
		}
	}

	public static void batchAddLotteryDoubleColor(List<DoubleColor> list) {
		batchAddLotteryDoubleColor(list, null);
	}

	public static void batchAddLotteryDoubleColor(List<DoubleColor> list,
			String sql) {

		Connection conn = null;
		PreparedStatement pst = null;

		try {
			conn = MysqlConnectionManager.getConnection();
			// 关闭事务自动提交
			conn.setAutoCommit(false);
			if (conn != null) {
				if (null == sql || StringUtils.isBlank(sql)) {
					sql = "insert into lottery_double_color (phase, red_1, red_2, red_3, red_4, red_5, red_6, blue, year, create_date, update_date) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
				}
				pst = (PreparedStatement) conn.prepareStatement(sql);

				Date myDate = new Date();
				for (DoubleColor ssq : list) {
					pst.setInt(1, ssq.getPhase());
					List<String> red = ssq.getRed();
					for (int i = 0; i < 6; i++) {
						pst.setString(i + 2, red.get(i));
					}
					pst.setString(8, ssq.getBlue());
					pst.setInt(9, ssq.getYear());
					pst.setTimestamp(10, new Timestamp(myDate.getTime()));
					pst.setTimestamp(11, new Timestamp(myDate.getTime()));

					logger.info(ssq.toString());

					// 把一个SQL命令加入命令列表
					pst.addBatch();
				}

				// 执行批量更新
				pst.executeBatch();
				// 语句执行完毕，提交本事务
				conn.commit();
				logger.info("批量导入双色球信息成功！");
			}
		} catch (SQLException e) {
			logger.error("SQLException[MysqlDatabseHelper->batchAddLotteryDoubleColor]: "
					+ e.getMessage());
		} finally {
			MysqlConnectionManager.closePreparedStatement(pst);
			MysqlConnectionManager.closeConnection(conn);
		}
	}

	public static int truncate(String tableName) {

		Connection conn = null;
		Statement stmt = null;
		String sql = "TRUNCATE TABLE ";
		int result = -1;

		if (null == tableName || StringUtils.isBlank(tableName)) {
			logger.error("清空表失败，参数tableName为空!");
			return result;
		}

		sql += tableName;

		try {
			conn = MysqlConnectionManager.getConnection();
			if (conn != null) {
				stmt = conn.createStatement();
				result = stmt.executeUpdate(sql);
				if (result > 0) {
					logger.info("清空表[" + tableName + "]成功！");
				}
			}

		} catch (SQLException e) {
			logger.error("SQLException[MysqlDatabseHelper->truncate]: "
					+ e.getMessage());
		} finally {
			MysqlConnectionManager.closeStatement(stmt);
			MysqlConnectionManager.closeConnection(conn);
		}
		return result;
	}

	public static List<DoubleColor> getLastPhase(Integer phase, Integer top) {
		StringBuilder sb = new StringBuilder();

		sb.append("SELECT");
		sb.append(" * ");
		// sb.append(" phase,red_1,red_2,red_3,red_4,red_5,red_6,blue ");
		sb.append("FROM ");
		sb.append("	lottery_double_color ");
		sb.append("WHERE");
		sb.append("	1 = 1");
		if (null != phase) {
			sb.append(" AND phase < ").append(phase);
		}
		sb.append(" ORDER BY ");
		sb.append("  phase DESC ");
		if (null != top) {
			sb.append(" LIMIT ").append(top);
		}

		return getDoubleColorLotteryList(sb.toString());
	}

	public static List<DoubleColor> getDoubleColorLotteryList(String sql) {

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<DoubleColor> list = new ArrayList<DoubleColor>();

		try {
			conn = MysqlConnectionManager.getConnection();
			if (conn != null) {
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					DoubleColor ssq = new DoubleColor();
					ssq.setPhase(rs.getInt("phase"));
					List<String> red = new ArrayList<String>(6);
					red.add(rs.getString("red_1"));
					red.add(rs.getString("red_2"));
					red.add(rs.getString("red_3"));
					red.add(rs.getString("red_4"));
					red.add(rs.getString("red_5"));
					red.add(rs.getString("red_6"));
					ssq.setRed(red);
					ssq.setBlue(rs.getString("blue"));
					list.add(ssq);
				}
				// logger.info("已获取双色球结果列表！");
			}

		} catch (SQLException e) {
			logger.error("SQLException[MysqlDatabseHelper->getDoubleColorLotteryList(String sql)]: "
					+ e.getMessage());
		} finally {
			MysqlConnectionManager.closeResultSet(rs);
			MysqlConnectionManager.closeStatement(stmt);
			MysqlConnectionManager.closeConnection(conn);
		}
		return list;
	}

	public static Map<Integer, DoubleColor> getDoubleColorLotteryMap(String sql) {

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Map<Integer, DoubleColor> map = new HashMap<Integer, DoubleColor>();

		try {
			conn = MysqlConnectionManager.getConnection();
			if (conn != null) {
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					DoubleColor ssq = new DoubleColor();
					Integer phase = rs.getInt("phase");
					ssq.setPhase(phase);
					List<String> red = new ArrayList<String>(6);
					red.add(rs.getString("red_1"));
					red.add(rs.getString("red_2"));
					red.add(rs.getString("red_3"));
					red.add(rs.getString("red_4"));
					red.add(rs.getString("red_5"));
					red.add(rs.getString("red_6"));
					ssq.setRed(red);
					ssq.setBlue(rs.getString("blue"));
					map.put(phase, ssq);
				}
				// logger.info("已获取双色球结果列表！");
			}

		} catch (SQLException e) {
			logger.error("SQLException[MysqlDatabseHelper->getDoubleColorLotteryList(String sql)]: "
					+ e.getMessage());
		} finally {
			MysqlConnectionManager.closeResultSet(rs);
			MysqlConnectionManager.closeStatement(stmt);
			MysqlConnectionManager.closeConnection(conn);
		}
		return map;
	}

	/**
	 * 获取某期双色球信息
	 * 
	 * @param phase
	 *            期号
	 * @return
	 */
	public static DoubleColor getDoubleColorByPhase(Integer phase) {

		if (null == phase) {
			return null;
		}

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM lottery_double_color WHERE phase  = ?";
		DoubleColor ssq = new DoubleColor();

		try {
			conn = MysqlConnectionManager.getConnection();
			if (conn != null) {
				stmt = conn.createStatement();

				rs = stmt.executeQuery(sql);
				if (rs.next()) {
					ssq.setPhase(rs.getInt("phase"));
					List<String> red = new ArrayList<String>(6);
					red.add(rs.getString("red_1"));
					red.add(rs.getString("red_2"));
					red.add(rs.getString("red_3"));
					red.add(rs.getString("red_4"));
					red.add(rs.getString("red_5"));
					red.add(rs.getString("red_6"));
					ssq.setRed(red);
					ssq.setBlue(rs.getString("blue"));
				}
				logger.info("已获取" + phase + "期双色球信息！");
			}

		} catch (SQLException e) {
			logger.error("SQLException[MysqlDatabseHelper->getVideoList]: "
					+ e.getMessage());
		} finally {
			MysqlConnectionManager.closeResultSet(rs);
			MysqlConnectionManager.closeStatement(stmt);
			MysqlConnectionManager.closeConnection(conn);
		}
		return ssq;
	}

	public static void batchAddLotterySuperLotto(List<SuperLotto> list) {
		batchAddLotterySuperLotto(list, null);
	}

	public static void batchAddLotterySuperLotto(List<SuperLotto> list,
			String sql) {

		Connection conn = null;
		PreparedStatement pst = null;

		try {
			conn = MysqlConnectionManager.getConnection();
			// 关闭事务自动提交
			conn.setAutoCommit(false);
			if (conn != null) {
				if (null == sql || StringUtils.isBlank(sql)) {
					sql = "insert into lottery_super_lotto (phase, red_1, red_2, red_3, red_4, red_5, blue_1, blue_2, year) VALUES (?,?,?,?,?,?,?,?,?)";
				}
				pst = (PreparedStatement) conn.prepareStatement(sql);
				for (SuperLotto sl : list) {
					pst.setInt(1, sl.getPhase());
					List<String> red = sl.getRed();
					for (int i = 0; i < 5; i++) {
						pst.setString(i + 2, red.get(i));
					}
					List<String> blue = sl.getBlue();
					for (int j = 0; j < 2; j++) {
						pst.setString(j + 7, blue.get(j));
					}
					pst.setInt(9, sl.getYear());

					logger.info(sl.toString());

					// 把一个SQL命令加入命令列表
					pst.addBatch();
				}

				// 执行批量更新
				pst.executeBatch();
				// 语句执行完毕，提交本事务
				conn.commit();
				logger.info("批量导入大乐透信息成功！");
			}
		} catch (SQLException e) {
			logger.error("SQLException[MysqlDatabseHelper->batchAddLotterySuperLotto]: "
					+ e.getMessage());
		} finally {
			MysqlConnectionManager.closePreparedStatement(pst);
			MysqlConnectionManager.closeConnection(conn);
		}
	}

	public static void batchAddLotteryFiveInEleven(List<FiveInEleven> list) {
		batchAddLotteryFiveInEleven(list, null);
	}

	public static void batchAddLotteryFiveInEleven(List<FiveInEleven> list,
			String sql) {

		Connection conn = null;
		PreparedStatement pst = null;

		try {
			conn = MysqlConnectionManager.getConnection();
			// 关闭事务自动提交
			conn.setAutoCommit(false);
			if (conn != null) {
				if (null == sql || StringUtils.isBlank(sql)) {
					sql = "insert into lottery_five_in_eleven (period, red_1, red_2, red_3, red_4, red_5, lottery_number, category, period_date, create_date, update_date) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
				}
				pst = (PreparedStatement) conn.prepareStatement(sql);
				Date myDate = new Date();

				for (FiveInEleven fie : list) {
					pst.setInt(1, fie.getPeriod());
					List<String> red = fie.getRed();
					for (int i = 0; i < 5; i++) {
						pst.setString(i + 2, red.get(i));
					}
					pst.setString(7, fie.getLotteryNumber());
					pst.setString(8, fie.getCategory());
					pst.setString(9, fie.getDate());
					pst.setTimestamp(10, new Timestamp(myDate.getTime()));
					pst.setTimestamp(11, new Timestamp(myDate.getTime()));

					// logger.info(fie.toString());

					// 把一个SQL命令加入命令列表
					pst.addBatch();
				}

				// 执行批量更新
				pst.executeBatch();
				// 语句执行完毕，提交本事务
				conn.commit();
				logger.info("批量导入11选5信息成功！");
			}
		} catch (SQLException e) {
			logger.error("SQLException[MysqlDatabseHelper->batchAddLotteryFiveInEleven]: "
					+ e.getMessage());
		} finally {
			MysqlConnectionManager.closePreparedStatement(pst);
			MysqlConnectionManager.closeConnection(conn);
		}
	}

	public static Map<String, Integer> statsMaxPeriod(String sql) {

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Map<String, Integer> map = new HashMap<String, Integer>();

		try {
			conn = MysqlConnectionManager.getConnection();
			if (conn != null) {
				stmt = conn.createStatement();
				if (null == sql || StringUtils.isBlank(sql)) {
					sql = "SELECT category,MAX(period) FROM lottery_five_in_eleven GROUP BY category";
				}
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					map.put(rs.getString(1), rs.getInt(2));
				}
				logger.info("已获取各类11选5最大期数信息");
			}

		} catch (SQLException e) {
			logger.error("SQLException[MysqlDatabseHelper->getVideoList]: "
					+ e.getMessage());
			// e.printStackTrace();
		} finally {
			MysqlConnectionManager.closeResultSet(rs);
			MysqlConnectionManager.closeStatement(stmt);
			MysqlConnectionManager.closeConnection(conn);
		}
		return map;
	}

	/**
	 * 按日获取11选5数据
	 * 
	 * @param category
	 * @param date
	 * @return
	 */
	public static List<FiveInEleven> getFiveInElevenList(String category,
			String date) {

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM lottery_five_in_eleven WHERE 1 = 1 ");
		if (null != category) {
			sb.append(" AND category ='").append(category).append("'");
		}
		if (null != category && !StringUtils.isBlank(date)) {
			sb.append(" AND period_date ='").append(date).append("'");
		}
		sb.append(" ORDER BY period");

		return getFiveInElevenList(sb.toString());
	}

	/**
	 * 获取11选5数据
	 * 
	 * @param sql
	 * @return
	 */
	public static List<FiveInEleven> getFiveInElevenList(String sql) {

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<FiveInEleven> list = new ArrayList<FiveInEleven>();

		try {
			conn = MysqlConnectionManager.getConnection();
			if (conn != null) {
				stmt = conn.createStatement();
				if (null == sql || StringUtils.isBlank(sql)) {
					sql = "SELECT * FROM lottery_five_in_eleven ORDER BY period";
				}
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					FiveInEleven fie = new FiveInEleven();
					fie.setPeriod(rs.getInt("period"));
					List<String> red = new ArrayList<String>(5);
					red.add(rs.getString("red_1"));
					red.add(rs.getString("red_2"));
					red.add(rs.getString("red_3"));
					red.add(rs.getString("red_4"));
					red.add(rs.getString("red_5"));
					fie.setRed(red);
					fie.setLotteryNumber(rs.getString("lottery_number"));
					fie.setCategory(rs.getString("category"));
					fie.setDate(rs.getString("period_date"));
					// logger.info(fie.toString());
					list.add(fie);
				}
				logger.info("已获取11选5结果列表！");
			}

		} catch (SQLException e) {
			logger.error("SQLException[MysqlDatabseHelper->getDoubleColorLotteryList(String sql)]: "
					+ e.getMessage());
		} finally {
			MysqlConnectionManager.closeResultSet(rs);
			MysqlConnectionManager.closeStatement(stmt);
			MysqlConnectionManager.closeConnection(conn);
		}
		return list;
	}

	public static void batchAddFiveInElevenAdjacent(
			List<FiveInEleven> adjacentList, String category, String date,
			String sql) {

		Connection conn = null;
		PreparedStatement pst = null;

		try {
			conn = MysqlConnectionManager.getConnection();
			// 关闭事务自动提交
			conn.setAutoCommit(false);
			if (conn != null) {
				if (null == sql || StringUtils.isBlank(sql)) {
					sql = "INSERT INTO analysis_fie_adjacent(period, lottery_number, category, period_date, create_date, update_date) VALUES (?,?,?,?,?,?)";
				}
				pst = (PreparedStatement) conn.prepareStatement(sql);
				Date myDate = new Date();
				for (FiveInEleven fie : adjacentList) {
					pst.setInt(1, fie.getPeriod());
					List<String> list = fie.getRed();
					Collections.sort(list);
					// int count = 0;
					// String ballStr = "";
					// for(String ball : list){
					// if(count++ > 0){
					// ballStr += ",";
					// }
					// ballStr += ball;
					// }
					// pst.setString(2, ballStr);
					pst.setString(2, list.toString());
					pst.setString(3, category);
					pst.setString(4, date);
					pst.setTimestamp(5, new Timestamp(myDate.getTime()));
					pst.setTimestamp(6, new Timestamp(myDate.getTime()));
					// logger.info(fie.toString());
					// 把一个SQL命令加入命令列表
					pst.addBatch();
				}

				// 执行批量更新
				pst.executeBatch();
				// 语句执行完毕，提交本事务
				conn.commit();
				logger.info("批量导入11选5[相邻期相同]信息成功！");
			}
		} catch (SQLException e) {
			logger.error("SQLException[MysqlDatabseHelper->batchAddLotteryFiveInEleven]: "
					+ e.getMessage());
		} finally {
			MysqlConnectionManager.closePreparedStatement(pst);
			MysqlConnectionManager.closeConnection(conn);
		}
	}

	public static void batchAddFiveInElevenConsecutive(
			List<FiveInEleven> consecutiveList, String category, String date,
			String sql) {

		Connection conn = null;
		PreparedStatement pst = null;

		try {
			conn = MysqlConnectionManager.getConnection();
			// 关闭事务自动提交
			conn.setAutoCommit(false);
			if (conn != null) {
				if (null == sql || StringUtils.isBlank(sql)) {
					sql = "INSERT INTO analysis_fie_consecutive(period, lottery_number, consecutive, amount, category, period_date, create_date, update_date) VALUES (?,?,?,?,?,?,?,?)";
				}
				pst = (PreparedStatement) conn.prepareStatement(sql);
				Date myDate = new Date();
				for (FiveInEleven fie : consecutiveList) {
					pst.setInt(1, fie.getPeriod());
					List<String> list = fie.getRed();
					Collections.sort(list);
					pst.setString(2, list.toString());
					pst.setString(3, fie.getConsecutive());
					pst.setInt(4, fie.getAmount());
					pst.setString(5, category);
					pst.setString(6, date);
					pst.setTimestamp(7, new Timestamp(myDate.getTime()));
					pst.setTimestamp(8, new Timestamp(myDate.getTime()));
					// logger.info(fie.toString());
					// 把一个SQL命令加入命令列表
					pst.addBatch();
				}

				// 执行批量更新
				pst.executeBatch();
				// 语句执行完毕，提交本事务
				conn.commit();
				logger.info("批量导入11选5[连号]信息成功！");
			}
		} catch (SQLException e) {
			logger.error("SQLException[MysqlDatabseHelper->batchAddLotteryFiveInEleven]: "
					+ e.getMessage());
		} finally {
			MysqlConnectionManager.closePreparedStatement(pst);
			MysqlConnectionManager.closeConnection(conn);
		}
	}

	/**
	 * 删除某日某种分析结果
	 * 
	 * @param table
	 *            分析表名
	 * @param date
	 *            日期
	 * @return
	 */
	public static int deleteFiveInElevenAnalysis(String table, String date) {

		if (null == table || StringUtils.isBlank(table)) {
			return -1;
		}

		Connection conn = null;
		Statement stmt = null;
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ").append(table);
		if (null != date) {
			sb.append(" WHERE period_date = '").append(date).append("'");
		}
		int result = -1;

		try {
			conn = MysqlConnectionManager.getConnection();
			if (conn != null) {
				stmt = conn.createStatement();
				result = stmt.executeUpdate(sb.toString());
				if (result >= 0) {
					if (null != date) {
						logger.info("删除[" + date + "]分析记录！");
					} else {
						logger.info("删除分析记录！");
					}
				}
			}

		} catch (SQLException e) {
			logger.error("SQLException[MysqlDatabseHelper->deleteFiveInElevenConsecutiveByDate(String date)]: "
					+ e.getMessage());
		} finally {
			MysqlConnectionManager.closeStatement(stmt);
			MysqlConnectionManager.closeConnection(conn);
		}
		return result;
	}

	/**
	 * 删除某日[连号]分析结果
	 * 
	 * @param date
	 *            日期
	 * @return
	 */
	public static int deleteFiveInElevenAnalysisByConsecutive(String date) {
		return deleteFiveInElevenAnalysis("analysis_fie_consecutive", date);
	}

	/**
	 * 删除某日[与上期相同]分析结果
	 * 
	 * @param date
	 *            日期
	 * @return
	 */
	public static int deleteFiveInElevenAnalysisByAdjacent(String date) {
		return deleteFiveInElevenAnalysis("analysis_fie_adjacent", date);
	}

	/**
	 * 删除某日[同号]分析结果
	 * 
	 * @param date
	 *            日期
	 * @return
	 */
	public static int deleteFiveInElevenAnalysisBySame(String date) {
		return deleteFiveInElevenAnalysis("analysis_fie_same", date);
	}

	public static void batchAddFiveInElevenSame(
			List<FiveInEleven> samePeriodList, String category, String date,
			String sql) {

		Connection conn = null;
		PreparedStatement pst = null;

		try {
			conn = MysqlConnectionManager.getConnection();
			// 关闭事务自动提交
			conn.setAutoCommit(false);
			if (conn != null) {
				if (null == sql || StringUtils.isBlank(sql)) {
					sql = "INSERT INTO analysis_fie_same(period, lottery_number, category, period_date, create_date, update_date) VALUES (?,?,?,?,?,?)";
				}
				pst = (PreparedStatement) conn.prepareStatement(sql);
				Date myDate = new Date();

				for (FiveInEleven fie : samePeriodList) {
					pst.setInt(1, fie.getPeriod());
					List<String> list = fie.getRed();
					Collections.sort(list);
					pst.setString(2, list.toString());
					pst.setString(3, category);
					pst.setString(4, date);
					pst.setTimestamp(5, new Timestamp(myDate.getTime()));
					pst.setTimestamp(6, new Timestamp(myDate.getTime()));
					// logger.info(fie.toString());
					// 把一个SQL命令加入命令列表
					pst.addBatch();
				}

				// 执行批量更新
				pst.executeBatch();
				// 语句执行完毕，提交本事务
				conn.commit();
				logger.info("批量导入11选5[同号]信息成功！");
			}
		} catch (SQLException e) {
			logger.error("SQLException[MysqlDatabseHelper->batchAddFiveInElevenSame(List<FiveInEleven> samePeriodList, String category, String date, String sql)]: "
					+ e.getMessage());
		} finally {
			MysqlConnectionManager.closePreparedStatement(pst);
			MysqlConnectionManager.closeConnection(conn);
		}
	}

	public static Set<String> statsPeriodPerDay(String sql) {

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Set<String> set = new HashSet<String>();

		try {
			conn = MysqlConnectionManager.getConnection();
			if (conn != null) {
				stmt = conn.createStatement();
				if (null == sql || StringUtils.isBlank(sql)) {
					sql = "SELECT period_date,COUNT(id) FROM lottery_five_in_eleven GROUP BY period_date ORDER BY period_date";
				}
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					set.add(rs.getString(1));
				}
				logger.info("已获取各类11选5每天期数");
			}

		} catch (SQLException e) {
			logger.error("SQLException[MysqlDatabseHelper->getVideoList]: "
					+ e.getMessage());
		} finally {
			MysqlConnectionManager.closeResultSet(rs);
			MysqlConnectionManager.closeStatement(stmt);
			MysqlConnectionManager.closeConnection(conn);
		}
		return set;
	}

	public static Set<String> getFiveInElevenAnalysisBySame(String sql) {

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Set<String> set = new HashSet<String>();

		try {
			conn = MysqlConnectionManager.getConnection();
			if (conn != null) {
				stmt = conn.createStatement();
				if (null == sql || StringUtils.isBlank(sql)) {
					sql = "SELECT DISTINCT lottery_number FROM analysis_fie_same";
				}
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					set.add(rs.getString(1));
				}
				logger.info("已获取11选5结果列表！");
			}

		} catch (SQLException e) {
			logger.error("SQLException[MysqlDatabseHelper->getDoubleColorLotteryList(String sql)]: "
					+ e.getMessage());
		} finally {
			MysqlConnectionManager.closeResultSet(rs);
			MysqlConnectionManager.closeStatement(stmt);
			MysqlConnectionManager.closeConnection(conn);
		}
		return set;
	}

	public static void batchAddLuckFiveInEleven(List<Video> list, String sql) {

		Connection conn = null;
		PreparedStatement pst = null;

		try {
			conn = MysqlConnectionManager.getConnection();
			// 关闭事务自动提交
			conn.setAutoCommit(false);

			if (conn != null) {
				if (null == sql || StringUtils.isBlank(sql)) {
					sql = "insert into ent_movie (uuid, number, title, url, imageSource, category, year, starring, rate, introduction) VALUES (?,?,?,?,?,?,?,?,?,?)";
				}
				pst = (PreparedStatement) conn.prepareStatement(sql);
				for (Video video : list) {
					pst.setString(1, UUID.randomUUID().toString());
					pst.setInt(2, video.getNumber());
					pst.setString(3, video.getTitle());
					pst.setString(4, video.getUrl());
					pst.setString(5, video.getImageSource());
					pst.setString(6, video.getCategory());
					pst.setInt(7, video.getYear());
					pst.setString(8, video.getStarring());
					pst.setFloat(9, video.getRate());
					pst.setString(10, video.getIntroduction());
					// 把一个SQL命令加入命令列表
					pst.addBatch();
				}

				// 执行批量更新
				pst.executeBatch();
				// 语句执行完毕，提交本事务
				conn.commit();
				logger.info("批量导入影片信息[缺图片]成功！");
			}
		} catch (SQLException e) {
			logger.error("SQLException[MysqlDatabseHelper->batchAddVideo]: "
					+ e.getMessage());
		} finally {
			MysqlConnectionManager.closePreparedStatement(pst);
			MysqlConnectionManager.closeConnection(conn);
		}
	}

	/**
	 * 获取文章ID集合
	 * 
	 * @param authorIdArray
	 *            作者ID数组
	 * @return
	 */
	public static Set<Integer> getNeteaseArticleIdSet(Integer[] authorIdArray) {

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Set<Integer> threadIdSet = new HashSet<Integer>();

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT id FROM lms_bbs_netease ");
		if (null != authorIdArray) {
			sb.append("WHERE ");
			int len = authorIdArray.length;
			if (len == 1) {
				sb.append(" authorId = ").append(authorIdArray[0]);
			} else {
				sb.append(" authorId IN (");
				for (int i = 0; i < len; i++) {
					if (i > 0) {
						sb.append(",");
					}
					sb.append(authorIdArray[i]);
				}
				sb.append(")");
			}
		}

		try {
			conn = MysqlConnectionManager.getConnection();
			if (conn != null) {
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sb.toString());
				while (rs.next()) {
					threadIdSet.add(rs.getInt(1));
				}
				logger.info("已获取" + Arrays.toString(authorIdArray) + "发表文章ID集合");
			}

		} catch (SQLException e) {
			logger.error("SQLException[MysqlDatabseHelper->getNeteaseArticleIdSet(int [] authorIdArray)]: "
					+ e.getMessage());
		} finally {
			MysqlConnectionManager.closeResultSet(rs);
			MysqlConnectionManager.closeStatement(stmt);
			MysqlConnectionManager.closeConnection(conn);
		}
		return threadIdSet;
	}

	public static Map<Integer, String> getNeteaseThreadTimeMap(
			Integer[] authorIdArray) {

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Map<Integer, String> map = new HashMap<Integer, String>();

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT id,modify_time FROM lms_bbs_netease ");
		if (null != authorIdArray) {
			sb.append("WHERE ");
			int len = authorIdArray.length;
			if (len == 1) {
				sb.append(" authorId = ").append(authorIdArray[0]);
			} else {
				sb.append(" authorId IN (");
				for (int i = 0; i < len; i++) {
					if (i > 0) {
						sb.append(",");
					}
					sb.append(authorIdArray[i]);
				}
				sb.append(")");
			}
		}

		try {
			conn = MysqlConnectionManager.getConnection();
			if (conn != null) {
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sb.toString());
				while (rs.next()) {
					map.put(rs.getInt(1), rs.getString(2));
				}
				logger.info("已获取" + Arrays.toString(authorIdArray)
						+ "发表文章<ID,编辑时间>集合");
			}

		} catch (SQLException e) {
			logger.error("SQLException[MysqlDatabseHelper->getNeteaseThreadTimeMap(int [] authorIdArray): "
					+ e.getMessage());
		} finally {
			MysqlConnectionManager.closeResultSet(rs);
			MysqlConnectionManager.closeStatement(stmt);
			MysqlConnectionManager.closeConnection(conn);
		}
		return map;
	}

	/**
	 * 批量导入彩民社区文章
	 * 
	 * @param articleList
	 */
	public static void batchAddNeteaseArticles(List<Article> articleList) {

		Connection conn = null;
		PreparedStatement pst = null;

		StringBuffer sb = new StringBuffer();
		sb.append("insert into  )");
		sb.append(" lms_bbs_netease ");
		sb.append(" ( ");
		sb.append("id,");
		sb.append("module,");
		sb.append("subject,");
		sb.append("content,");
		sb.append("link,");
		sb.append("category,");
		sb.append("author,");
		sb.append("authorId,");
		sb.append("post_time,");
		sb.append("del_flag,");
		sb.append("create_date,");
		sb.append("update_by,");
		sb.append("update_date,");
		sb.append("create_by,");
		sb.append("remarks");
		sb.append(" ) ");
		sb.append(" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

		try {
			conn = MysqlConnectionManager.getConnection();
			// 关闭事务自动提交
			conn.setAutoCommit(false);

			if (conn != null) {
				pst = (PreparedStatement) conn.prepareStatement(sb.toString());
				for (Article article : articleList) {
					pst.setInt(1, article.getThreadId());
					pst.setString(2, article.getModule());
					pst.setString(3, article.getSubject());
					pst.setString(4, article.getContent());
					pst.setString(5, article.getLink());
					pst.setString(6, article.getCategory());
					pst.setString(7, article.getAuthor());
					pst.setInt(8, article.getAuthorId());
					pst.setString(9, article.getPostTime());
					pst.setInt(10, 1);
					String dt = DateUtil.getTime(System.currentTimeMillis());
					pst.setString(11, dt);
					pst.setString(12, "admin");
					pst.setString(13, dt);
					pst.setString(14, "admin");
					pst.setNull(15, Types.VARCHAR);
					// 把一个SQL命令加入命令列表
					pst.addBatch();
				}

				// 执行批量更新
				pst.executeBatch();
				// 语句执行完毕，提交本事务
				conn.commit();
				logger.info("批量导入彩民社区文章成功！");
			}
		} catch (SQLException e) {
			logger.error("SQLException[MysqlDatabseHelper->batchAddNeteaseArticles(List<Article> articleList)]: "
					+ e.getMessage());
		} finally {
			MysqlConnectionManager.closePreparedStatement(pst);
			MysqlConnectionManager.closeConnection(conn);
		}
	}

	/**
	 * 批量导入彩民社区文章
	 * 
	 * @param articleList
	 */
	public static void batchAddNeteaseArticles(List<Article> articleList,
			Set<Integer> existThreadSet) {

		Connection conn = null;
		PreparedStatement pstInsert = null;
		PreparedStatement pstUpdate = null;

		// 新增
		StringBuffer sqlInsert = new StringBuffer();
		sqlInsert.append("insert into ");
		sqlInsert.append(" lms_bbs_netease ");
		sqlInsert.append(" ( ");
		sqlInsert.append("id,");
		sqlInsert.append("module,");
		sqlInsert.append("subject,");
		sqlInsert.append("content,");
		sqlInsert.append("link,");
		sqlInsert.append("category,");
		sqlInsert.append("author,");
		sqlInsert.append("authorId,");
		sqlInsert.append("post_time,");
		sqlInsert.append("modify_time,");
		sqlInsert.append("del_flag,");
		sqlInsert.append("create_date,");
		sqlInsert.append("create_by,");
		sqlInsert.append("update_date,");
		sqlInsert.append("update_by,");
		sqlInsert.append("remarks");
		sqlInsert.append(" ) ");
		sqlInsert.append(" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

		// 更新
		StringBuffer sqlUpdate = new StringBuffer();
		sqlUpdate.append("update ");
		sqlUpdate.append(" lms_bbs_netease ");
		sqlUpdate.append("set ");
		sqlUpdate.append(" module = ?,");
		sqlUpdate.append(" subject = ?,");
		sqlUpdate.append(" content = ?,");
		sqlUpdate.append(" category = ?,");
		sqlUpdate.append(" post_time = ?,");
		sqlUpdate.append(" modify_time = ?,");
		sqlUpdate.append(" update_date = ?,");
		sqlUpdate.append(" update_by = ? ");
		sqlUpdate.append("where id = ? ");

		try {
			conn = MysqlConnectionManager.getConnection();
			// 关闭事务自动提交
			conn.setAutoCommit(false);

			if (conn != null) {
				pstInsert = (PreparedStatement) conn.prepareStatement(sqlInsert
						.toString());
				pstUpdate = (PreparedStatement) conn.prepareStatement(sqlUpdate
						.toString());

				for (Article article : articleList) {
					Integer threadId = article.getThreadId();
					String content = article.getContent();

					// logger.info(article);
					// logger.info(content.length());

					if (existThreadSet.contains(threadId)) {
						// 更新
						pstUpdate.setString(1, article.getModule());
						pstUpdate.setString(2, article.getSubject());
						pstUpdate.setString(3, article.getContent());
						pstUpdate.setString(4, article.getCategory());
						pstUpdate.setString(5, article.getPostTime());
						pstUpdate.setString(6, article.getModifyTime());
						pstUpdate.setString(7,
								DateUtil.getTime(System.currentTimeMillis()));
						pstUpdate.setString(8, "admin");
						pstUpdate.setInt(9, threadId);
						// 把一个SQL命令加入命令列表
						pstUpdate.addBatch();
					} else {
						// 新增
						pstInsert.setInt(1, threadId);
						pstInsert.setString(2, article.getModule());
						pstInsert.setString(3, article.getSubject());
						pstInsert.setString(4, article.getContent());
						pstInsert.setString(5, article.getLink());
						pstInsert.setString(6, article.getCategory());
						pstInsert.setString(7, article.getAuthor());
						pstInsert.setInt(8, article.getAuthorId());
						pstInsert.setString(9, article.getPostTime());
						pstInsert.setString(10, article.getModifyTime());
						pstInsert.setInt(11, 1);
						String dt = DateUtil
								.getTime(System.currentTimeMillis());
						pstInsert.setString(12, dt);
						pstInsert.setString(13, "admin");
						pstInsert.setString(14, dt);
						pstInsert.setString(15, "admin");
						pstInsert.setNull(16, Types.VARCHAR);
						// 把一个SQL命令加入命令列表
						pstInsert.addBatch();
					}
				}

				// 执行批量更新
				pstUpdate.executeBatch();
				pstInsert.executeBatch();
				// 语句执行完毕，提交本事务
				conn.commit();
				logger.info("批量导入彩民社区文章成功！");
			}
		} catch (SQLException e) {
			logger.error("SQLException[MysqlDatabseHelper->batchAddNeteaseArticles(List<Article> articleList)]: "
					+ e.getMessage());
		} finally {
			MysqlConnectionManager.closePreparedStatement(pstInsert);
			MysqlConnectionManager.closeConnection(conn);
		}
	}

	public static void batchAddNeteaseArticles(List<Article> articleList,
			Map<Integer, String> existThreadMap) {

		Connection conn = null;
		PreparedStatement pstInsert = null;
		PreparedStatement pstUpdate = null;

		boolean existFlag = true;
		if (null == existThreadMap) {
			existFlag = false;
		}

		Set<Integer> existThreadSet = existThreadMap.keySet();

		// 新增
		StringBuffer sqlInsert = new StringBuffer();
		sqlInsert.append("insert into ");
		sqlInsert.append(" lms_bbs_netease ");
		sqlInsert.append(" ( ");
		sqlInsert.append("id,");
		sqlInsert.append("module,");
		sqlInsert.append("subject,");
		sqlInsert.append("content,");
		sqlInsert.append("link,");
		sqlInsert.append("category,");
		sqlInsert.append("author,");
		sqlInsert.append("authorId,");
		sqlInsert.append("post_time,");
		sqlInsert.append("modify_time,");
		sqlInsert.append("del_flag,");
		sqlInsert.append("create_date,");
		sqlInsert.append("create_by,");
		sqlInsert.append("update_date,");
		sqlInsert.append("update_by,");
		sqlInsert.append("remarks");
		sqlInsert.append(" ) ");
		sqlInsert.append(" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

		// 更新
		StringBuffer sqlUpdate = new StringBuffer();
		sqlUpdate.append("update ");
		sqlUpdate.append(" lms_bbs_netease ");
		sqlUpdate.append("set ");
		sqlUpdate.append(" module = ?,");
		sqlUpdate.append(" subject = ?,");
		sqlUpdate.append(" content = ?,");
		sqlUpdate.append(" category = ?,");
		sqlUpdate.append(" post_time = ?,");
		sqlUpdate.append(" modify_time = ?,");
		sqlUpdate.append(" update_date = ?,");
		sqlUpdate.append(" update_by = ? ");
		sqlUpdate.append("where id = ? ");

		try {
			conn = MysqlConnectionManager.getConnection();
			// 关闭事务自动提交
			conn.setAutoCommit(false);

			if (conn != null) {
				pstInsert = (PreparedStatement) conn.prepareStatement(sqlInsert
						.toString());
				pstUpdate = (PreparedStatement) conn.prepareStatement(sqlUpdate
						.toString());

				for (Article article : articleList) {
					Integer threadId = article.getThreadId();
					String content = article.getContent();

					if (existFlag && existThreadSet.contains(threadId)) {
						String currentModifyTime = article.getModifyTime();
						String lastModifyTime = existThreadMap.get(threadId);

						if (null != lastModifyTime
								&& null != currentModifyTime
								&& currentModifyTime.compareTo(lastModifyTime) > 0) {
							// 更新
							pstUpdate.setString(1, article.getModule());
							pstUpdate.setString(2, article.getSubject());
							pstUpdate.setString(3, content);
							pstUpdate.setString(4, article.getCategory());
							pstUpdate.setString(5, article.getPostTime());
							pstUpdate.setString(6, currentModifyTime);
							pstUpdate.setString(7, DateUtil.getTime(System
									.currentTimeMillis()));
							pstUpdate.setString(8, "admin");
							pstUpdate.setInt(9, threadId);
							// 把一个SQL命令加入命令列表
							pstUpdate.addBatch();
						}
					} else {
						// 新增
						pstInsert.setInt(1, threadId);
						pstInsert.setString(2, article.getModule());
						pstInsert.setString(3, article.getSubject());
						pstInsert.setString(4, content);
						pstInsert.setString(5, article.getLink());
						pstInsert.setString(6, article.getCategory());
						pstInsert.setString(7, article.getAuthor());
						pstInsert.setInt(8, article.getAuthorId());
						pstInsert.setString(9, article.getPostTime());
						pstInsert.setString(10, article.getModifyTime());
						pstInsert.setInt(11, 1);
						String dt = DateUtil
								.getTime(System.currentTimeMillis());
						pstInsert.setString(12, dt);
						pstInsert.setString(13, "admin");
						pstInsert.setString(14, dt);
						pstInsert.setString(15, "admin");
						pstInsert.setNull(16, Types.VARCHAR);
						// 把一个SQL命令加入命令列表
						pstInsert.addBatch();
					}
				}

				// 执行批量更新
				pstUpdate.executeBatch();
				pstInsert.executeBatch();
				// 语句执行完毕，提交本事务
				conn.commit();
				logger.info("批量导入彩民社区文章成功！");
			}
		} catch (SQLException e) {
			logger.error("SQLException[MysqlDatabseHelper->batchAddNeteaseArticles(List<Article> articleList)]: "
					+ e.getMessage());
		} finally {
			MysqlConnectionManager.closePreparedStatement(pstInsert);
			MysqlConnectionManager.closeConnection(conn);
		}
	}

	public static List<Article> persistAndToMail(List<Article> articleList,
			Map<Integer, String> existThreadMap) {

		Connection conn = null;
		PreparedStatement pstInsert = null;
		PreparedStatement pstUpdate = null;

		boolean existFlag = true;
		if (null == existThreadMap) {
			existFlag = false;
		}

		List<Article> mailList = new ArrayList<Article>();
		Set<Integer> existThreadSet = existThreadMap.keySet();

		// 新增
		StringBuffer sqlInsert = new StringBuffer();
		sqlInsert.append("insert into ");
		sqlInsert.append(" lms_bbs_netease ");
		sqlInsert.append(" ( ");
		sqlInsert.append("id,");
		sqlInsert.append("module,");
		sqlInsert.append("subject,");
		sqlInsert.append("content,");
		sqlInsert.append("link,");
		sqlInsert.append("category,");
		sqlInsert.append("author,");
		sqlInsert.append("authorId,");
		sqlInsert.append("post_time,");
		sqlInsert.append("modify_time,");
		sqlInsert.append("del_flag,");
		sqlInsert.append("create_date,");
		sqlInsert.append("create_by,");
		sqlInsert.append("update_date,");
		sqlInsert.append("update_by,");
		sqlInsert.append("remarks");
		sqlInsert.append(" ) ");
		sqlInsert.append(" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

		// 更新
		StringBuffer sqlUpdate = new StringBuffer();
		sqlUpdate.append("update ");
		sqlUpdate.append(" lms_bbs_netease ");
		sqlUpdate.append("set ");
		sqlUpdate.append(" module = ?,");
		sqlUpdate.append(" subject = ?,");
		sqlUpdate.append(" content = ?,");
		sqlUpdate.append(" category = ?,");
		sqlUpdate.append(" post_time = ?,");
		sqlUpdate.append(" modify_time = ?,");
		sqlUpdate.append(" update_date = ?,");
		sqlUpdate.append(" update_by = ? ");
		sqlUpdate.append("where id = ? ");

		try {
			conn = MysqlConnectionManager.getConnection();
			// 关闭事务自动提交
			conn.setAutoCommit(false);

			if (conn != null) {
				pstInsert = (PreparedStatement) conn.prepareStatement(sqlInsert
						.toString());
				pstUpdate = (PreparedStatement) conn.prepareStatement(sqlUpdate
						.toString());

				for (Article article : articleList) {
					Integer threadId = article.getThreadId();
					String content = article.getContent();

					if (existFlag && existThreadSet.contains(threadId)) {
						String currentModifyTime = article.getModifyTime();
						String lastModifyTime = existThreadMap.get(threadId);

						if (null != lastModifyTime
								&& null != currentModifyTime
								&& currentModifyTime.compareTo(lastModifyTime) > 0) {
							// 更新
							pstUpdate.setString(1, article.getModule());
							pstUpdate.setString(2, article.getSubject());
							pstUpdate.setString(3, content);
							pstUpdate.setString(4, article.getCategory());
							pstUpdate.setString(5, article.getPostTime());
							pstUpdate.setString(6, currentModifyTime);
							pstUpdate.setString(7, DateUtil.getTime(System
									.currentTimeMillis()));
							pstUpdate.setString(8, "admin");
							pstUpdate.setInt(9, threadId);
							// 把一个SQL命令加入命令列表
							pstUpdate.addBatch();
							// 加入待发邮件列表
							mailList.add(article);
						}
					} else {
						// 新增
						pstInsert.setInt(1, threadId);
						pstInsert.setString(2, article.getModule());
						pstInsert.setString(3, article.getSubject());
						pstInsert.setString(4, content);
						pstInsert.setString(5, article.getLink());
						pstInsert.setString(6, article.getCategory());
						pstInsert.setString(7, article.getAuthor());
						pstInsert.setInt(8, article.getAuthorId());
						pstInsert.setString(9, article.getPostTime());
						pstInsert.setString(10, article.getModifyTime());
						pstInsert.setInt(11, 1);
						String dt = DateUtil
								.getTime(System.currentTimeMillis());
						pstInsert.setString(12, dt);
						pstInsert.setString(13, "admin");
						pstInsert.setString(14, dt);
						pstInsert.setString(15, "admin");
						pstInsert.setNull(16, Types.VARCHAR);
						// 把一个SQL命令加入命令列表
						pstInsert.addBatch();
						// 加入待发邮件列表
						mailList.add(article);
					}
				}

				// 执行批量更新
				pstUpdate.executeBatch();
				pstInsert.executeBatch();
				// 语句执行完毕，提交本事务
				conn.commit();
				logger.info("批量导入彩民社区文章成功！");
			}
		} catch (SQLException e) {
			logger.error("SQLException[MysqlDatabseHelper->persistAndToMail(List<Article> articleList)]: "
					+ e.getMessage());
		} finally {
			MysqlConnectionManager.closePreparedStatement(pstInsert);
			MysqlConnectionManager.closeConnection(conn);
		}
		return mailList;
	}

	/**
	 * 批量导入足球赛事
	 * 
	 * @param footballLeagueList
	 *            赛事列表
	 */
	public static void batchAddFootballLeague(
			List<FootballLeague> footballLeagueList) {

		Connection conn = null;
		PreparedStatement pst = null;

		StringBuffer sb = new StringBuffer();
		sb.append("insert into ");
		sb.append(" lms_football_league ");
		sb.append(" ( ");
		sb.append("id,");
		sb.append("name,");
		sb.append("category,");
		sb.append("country,");
		sb.append("continental,");
		sb.append("link,");
		sb.append("del_flag,");
		sb.append("create_date,");
		sb.append("update_by,");
		sb.append("update_date,");
		sb.append("create_by,");
		sb.append("remarks");
		sb.append(" ) ");
		sb.append(" values (?,?,?,?,?,?,?,?,?,?,?,?)");

		try {
			conn = MysqlConnectionManager.getConnection();
			// 关闭事务自动提交
			conn.setAutoCommit(false);

			if (conn != null) {
				pst = (PreparedStatement) conn.prepareStatement(sb.toString());
				for (FootballLeague league : footballLeagueList) {
					pst.setLong(1, league.getId());
					pst.setString(2, league.getName());
					pst.setString(3, league.getCategory());
					pst.setString(4, league.getCountry());
					pst.setString(5, league.getContinental());
					pst.setString(6, league.getLink());
					pst.setInt(7, 1);
					String dt = DateUtil.getTime(System.currentTimeMillis());
					pst.setString(8, dt);
					pst.setString(9, "admin");
					pst.setString(10, dt);
					pst.setString(11, "admin");
					pst.setNull(12, Types.VARCHAR);
					// 把一个SQL命令加入命令列表
					pst.addBatch();
					// logger.info(league);
				}

				// 执行批量更新
				pst.executeBatch();
				// 语句执行完毕，提交本事务
				conn.commit();
				logger.info("批量导入足球赛事成功！");
			}
		} catch (SQLException e) {
			logger.error("SQLException[MysqlDatabseHelper->batchAddFootballLeague(List<FootballLeague> footballLeagueList)]: "
					+ e.getMessage());
		} finally {
			MysqlConnectionManager.closePreparedStatement(pst);
			MysqlConnectionManager.closeConnection(conn);
		}
	}

	/**
	 * 获取足球赛事列表
	 * 
	 * @param sql
	 * @return
	 */
	public static List<FootballLeague> getFootballLeagueList(String sql) {

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<FootballLeague> list = new ArrayList<FootballLeague>();

		try {
			conn = MysqlConnectionManager.getConnection();
			if (conn != null) {
				stmt = conn.createStatement();
				if (null == sql || StringUtils.isBlank(sql)) {
					sql = "SELECT id,name,category,country,continental,link FROM lms_football_league";
				}
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					FootballLeague league = new FootballLeague();
					league.setId(rs.getLong(1));
					league.setName(rs.getString(2));
					league.setCategory(rs.getString(3));
					league.setCountry(rs.getString(4));
					league.setContinental(rs.getString(5));
					league.setLink(rs.getString(6));

					list.add(league);
				}
				logger.info("已获取足球赛事信息");
			}

		} catch (SQLException e) {
			logger.error("SQLException[MysqlDatabseHelper->getFootballLeagueList(String sql)]: "
					+ e.getMessage());
			// e.printStackTrace();
		} finally {
			MysqlConnectionManager.closeResultSet(rs);
			MysqlConnectionManager.closeStatement(stmt);
			MysqlConnectionManager.closeConnection(conn);
		}
		return list;
	}

	/**
	 * 批量导入球队信息
	 * 
	 * @param teamList
	 *            球队列表
	 * @param existIdSet
	 */
	public static void batchAddFootballTeam(List<FootballTeam> teamList,
			Set<Long> existIdSet) {

		Connection conn = null;
		PreparedStatement pstInsert = null;
		PreparedStatement pstUpdate = null;

		boolean existFlag = true;
		if (null == existIdSet || existIdSet.size() == 0) {
			existFlag = false;
		}

		// 新增
		StringBuffer sqlInsert = new StringBuffer();
		sqlInsert.append("insert into ");
		sqlInsert.append(" lms_football_team ");
		sqlInsert.append(" ( ");
		sqlInsert.append("id,");
		sqlInsert.append("name,");
		sqlInsert.append("full_name,");
		sqlInsert.append("league_id,");
		sqlInsert.append("rank,");
		sqlInsert.append("city,");
		sqlInsert.append("home_court,");
		sqlInsert.append("head_coach,");
		sqlInsert.append("link,");
		sqlInsert.append("brief_introduction,");
		sqlInsert.append("del_flag,");
		sqlInsert.append("create_date,");
		sqlInsert.append("create_by,");
		sqlInsert.append("update_date,");
		sqlInsert.append("update_by,");
		sqlInsert.append("remarks");
		sqlInsert.append(" ) ");
		sqlInsert.append(" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

		// 更新
		StringBuffer sqlUpdate = new StringBuffer();
		sqlUpdate.append("update ");
		sqlUpdate.append(" lms_football_team ");
		sqlUpdate.append("set ");
		sqlUpdate.append(" name = ?,");
		sqlUpdate.append(" full_name = ?,");
		sqlUpdate.append(" league_id = ?,");
		sqlUpdate.append(" rank = ?,");
		sqlUpdate.append(" city = ?,");
		sqlUpdate.append(" home_court = ?,");
		sqlUpdate.append(" head_coach = ?,");
		sqlUpdate.append(" link = ?, ");
		sqlUpdate.append(" brief_introduction = ?, ");
		sqlUpdate.append(" update_date = ?,");
		sqlUpdate.append(" update_by = ? ");
		sqlUpdate.append("where id = ? ");

		try {
			conn = MysqlConnectionManager.getConnection();
			// 关闭事务自动提交
			conn.setAutoCommit(false);

			if (conn != null) {
				pstInsert = (PreparedStatement) conn.prepareStatement(sqlInsert
						.toString());
				pstUpdate = (PreparedStatement) conn.prepareStatement(sqlUpdate
						.toString());

				for (FootballTeam team : teamList) {
					Long id = team.getId();

					if (existFlag && existIdSet.contains(id)) {
						// 更新
						pstUpdate.setString(1, team.getName());
						pstUpdate.setString(2, team.getFullName());
						pstUpdate.setLong(3, team.getLeagueId());
						pstUpdate.setString(4, team.getRank());
						pstUpdate.setString(5, team.getCity());
						pstUpdate.setString(6, team.getHomeCourt());
						pstUpdate.setString(7, team.getHeadCoach());
						pstUpdate.setString(8, team.getLink());
						pstUpdate.setString(9, team.getBriefIntroduction());
						pstUpdate.setString(10,
								DateUtil.getTime(System.currentTimeMillis()));
						pstUpdate.setString(11, "admin");
						pstUpdate.setLong(12, team.getId());
						// 把一个SQL命令加入命令列表
						pstUpdate.addBatch();
					} else {
						// 新增
						pstInsert.setLong(1, team.getId());
						pstInsert.setString(2, team.getName());
						pstInsert.setString(3, team.getFullName());
						pstInsert.setLong(4, team.getLeagueId());
						pstInsert.setString(5, team.getRank());
						pstInsert.setString(6, team.getCity());
						pstInsert.setString(7, team.getHomeCourt());
						pstInsert.setString(8, team.getHeadCoach());
						pstInsert.setString(9, team.getLink());
						pstInsert.setString(10, team.getBriefIntroduction());
						pstInsert.setInt(11, 1);
						String dt = DateUtil
								.getTime(System.currentTimeMillis());
						pstInsert.setString(12, dt);
						pstInsert.setString(13, "admin");
						pstInsert.setString(14, dt);
						pstInsert.setString(15, "admin");
						pstInsert.setNull(16, Types.VARCHAR);
						// 把一个SQL命令加入命令列表
						pstInsert.addBatch();
					}
				}

				// 执行批量更新
				pstUpdate.executeBatch();
				pstInsert.executeBatch();
				// 语句执行完毕，提交本事务
				conn.commit();
				logger.info("批量导入球队信息成功！");
			}
		} catch (SQLException e) {
			logger.error("SQLException[MysqlDatabseHelper->batchAddFootballTeam(List<FootballTeam> teamList, Set<Long> existIdSet)]: "
					+ e.getMessage());
		} finally {
			MysqlConnectionManager.closePreparedStatement(pstInsert);
			MysqlConnectionManager.closeConnection(conn);
		}
	}

	public static List<FootballTeam> getFootballTeamList(String sql) {

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<FootballTeam> list = new ArrayList<FootballTeam>();

		try {
			conn = MysqlConnectionManager.getConnection();
			if (conn != null) {
				stmt = conn.createStatement();
				if (StringUtils.isBlank(sql)) {
					sql = "SELECT id,name,full_name,league_id,rank,city,home_court,head_coach,link,brief_introduction FROM lms_football_team";
				}
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					FootballTeam team = new FootballTeam();
					team.setId(rs.getLong(1));
					team.setName(rs.getString(2));
					team.setFullName(rs.getString(3));
					team.setLeagueId(rs.getLong(4));
					team.setRank(rs.getString(5));
					team.setCity(rs.getString(6));
					team.setHomeCourt(rs.getString(7));
					team.setHomeCourt(rs.getString(8));
					team.setLink(rs.getString(9));
					team.setBriefIntroduction(rs.getString(10));
					list.add(team);
				}
				logger.info("已获取球队信息");
			}

		} catch (SQLException e) {
			logger.error("SQLException[MysqlDatabseHelper->getFootballTeamList(String sql)]: "
					+ e.getMessage());
			// e.printStackTrace();
		} finally {
			MysqlConnectionManager.closeResultSet(rs);
			MysqlConnectionManager.closeStatement(stmt);
			MysqlConnectionManager.closeConnection(conn);
		}
		return list;
	}

	/**
	 * 获取球队ID集合
	 * 
	 * @param sql
	 * @return
	 */
	public static Set<Long> getExistFootballTeamIdSet(String sql) {

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Set<Long> set = new HashSet<Long>();

		try {
			conn = MysqlConnectionManager.getConnection();
			if (conn != null) {
				stmt = conn.createStatement();
				if (StringUtils.isBlank(sql)) {
					sql = "SELECT id FROM lms_football_team";
				}
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					set.add(rs.getLong(1));
				}
				logger.info("已获取球队ID集合信息");
			}

		} catch (SQLException e) {
			logger.error("SQLException[MysqlDatabseHelper->getExistIdSet(String sql)]: "
					+ e.getMessage());
			// e.printStackTrace();
		} finally {
			MysqlConnectionManager.closeResultSet(rs);
			MysqlConnectionManager.closeStatement(stmt);
			MysqlConnectionManager.closeConnection(conn);
		}
		return set;
	}

	public static void incrementalAddFootballTeam(List<FootballTeam> teamList) {

		Connection conn = null;
		PreparedStatement pstInsert = null;

		// 新增
		StringBuffer sqlInsert = new StringBuffer();
		sqlInsert.append("insert into ");
		sqlInsert.append(" lms_football_team ");
		sqlInsert.append(" ( ");
		sqlInsert.append("id,");
		sqlInsert.append("name,");
		sqlInsert.append("full_name,");
		sqlInsert.append("league_id,");
		sqlInsert.append("rank,");
		sqlInsert.append("city,");
		sqlInsert.append("home_court,");
		sqlInsert.append("head_coach,");
		sqlInsert.append("link,");
		sqlInsert.append("brief_introduction,");
		sqlInsert.append("del_flag,");
		sqlInsert.append("create_date,");
		sqlInsert.append("create_by,");
		sqlInsert.append("update_date,");
		sqlInsert.append("update_by,");
		sqlInsert.append("remarks");
		sqlInsert.append(" ) ");
		sqlInsert.append(" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

		try {
			conn = MysqlConnectionManager.getConnection();
			// 关闭事务自动提交
			conn.setAutoCommit(false);

			if (conn != null) {
				pstInsert = (PreparedStatement) conn.prepareStatement(sqlInsert
						.toString());

				for (FootballTeam team : teamList) {
					Long id = team.getId();

					// 新增
					pstInsert.setLong(1, team.getId());
					pstInsert.setString(2, team.getName());
					pstInsert.setString(3, team.getFullName());
					pstInsert.setLong(4, team.getLeagueId());
					pstInsert.setString(5, team.getRank());
					pstInsert.setString(6, team.getCity());
					pstInsert.setString(7, team.getHomeCourt());
					pstInsert.setString(8, team.getHeadCoach());
					pstInsert.setString(9, team.getLink());
					pstInsert.setString(10, team.getBriefIntroduction());
					pstInsert.setInt(11, 1);
					String dt = DateUtil.getTime(System.currentTimeMillis());
					pstInsert.setString(12, dt);
					pstInsert.setString(13, "admin");
					pstInsert.setString(14, dt);
					pstInsert.setString(15, "admin");
					pstInsert.setNull(16, Types.VARCHAR);
					// 把一个SQL命令加入命令列表
					pstInsert.addBatch();
				}

				// 执行批量更新
				pstInsert.executeBatch();
				// 语句执行完毕，提交本事务
				conn.commit();
				logger.info("增量导入球队信息成功！");
			}
		} catch (SQLException e) {
			logger.error("SQLException[MysqlDatabseHelper->batchAddFootballTeam(List<FootballTeam> teamList, Set<Long> existIdSet)]: "
					+ e.getMessage());
		} finally {
			MysqlConnectionManager.closePreparedStatement(pstInsert);
			MysqlConnectionManager.closeConnection(conn);
		}
	}
	
	/**
	 * 删除某表数据
	 * @param table
	 * @param whereClause
	 * @return
	 */
	public static int deleteFromTable(String table, String whereClause) {

		if (null == table || StringUtils.isBlank(table)) {
			return -1;
		}

		Connection conn = null;
		Statement stmt = null;
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ").append(table);
		if (StringUtils.isNotBlank(whereClause)) {
			sb.append(" WHERE ").append(whereClause);
		}
		int result = -1;

		try {
			conn = MysqlConnectionManager.getConnection();
			if (conn != null) {
				stmt = conn.createStatement();
				result = stmt.executeUpdate(sb.toString());
				if (result >= 0) {
					logger.info("删除记录成功！");
				} else {
					logger.info("删除记录失败！");
				}
			}

		} catch (SQLException e) {
			logger.error(e.getMessage());
		} finally {
			MysqlConnectionManager.closeStatement(stmt);
			MysqlConnectionManager.closeConnection(conn);
		}
		return result;
	}

}