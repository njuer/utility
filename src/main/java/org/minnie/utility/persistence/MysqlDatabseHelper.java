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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.minnie.utility.module.sohu.DoubleColor;
import org.minnie.utility.module.xinyingba.Video;
import org.minnie.utility.util.Constant;

public class MysqlDatabseHelper {

	private static Logger logger = Logger.getLogger(MysqlDatabseHelper.class
			.getName());

	public static Pattern urlPattern = Pattern.compile(Constant.REG_URL);

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

	public static void batchUpdateImageWithFlag(String sql, String sourceFileDirectory) {
		
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
							String number = name.substring(0, name.indexOf("."));
//							if (StringUtils.isNumeric(number)) {
//								fileSet.add(Integer.valueOf(number));
//							}
							pst.setInt(3, Integer.valueOf(number));
							// 把一个SQL命令加入命令列表
							pst.addBatch();
//							is.close();
//							fis.close();
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

	public static void batchAddLotteryDoubleColor(List<DoubleColor> list){
		batchAddLotteryDoubleColor(list, null); 
	}
	
	public static void batchAddLotteryDoubleColor(List<DoubleColor> list, String sql) {

		Connection conn = null;
		PreparedStatement pst = null;

		try {
			conn = MysqlConnectionManager.getConnection();
			// 关闭事务自动提交
			conn.setAutoCommit(false);
			if (conn != null) {
				if (null == sql || StringUtils.isBlank(sql)) {
					sql = "insert into lottery_double_color (phase, red_1, red_2, red_3, red_4, red_5, red_6, blue, year) VALUES (?,?,?,?,?,?,?,?,?)";
				}
				pst = (PreparedStatement) conn.prepareStatement(sql);
				for (DoubleColor ssq : list) {
					pst.setInt(1, ssq.getPhase());
					List<String> red = ssq.getRed();
					for(int i = 0; i < 6; i++){
						pst.setString(i + 2, red.get(i));
					}
					pst.setString(8, ssq.getBlue());
					pst.setInt(9, ssq.getYear());
					
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
		
		if(null == tableName || StringUtils.isBlank(tableName)){
			logger.error("清空表失败，参数tableName为空!");
			return result;
		}
		
		sql += tableName;

		try {
			conn = MysqlConnectionManager.getConnection();
			if (conn != null) {
				stmt = conn.createStatement();
				result = stmt.executeUpdate(sql);
				if(result > 0){
					logger.info("清空表["+tableName+"]成功！");
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
	
}