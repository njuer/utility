package org.minnie.utility.persistence;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.minnie.utility.util.Constant;
import org.minnie.utility.util.ImageUtil;
import org.minnie.utility.xinyingba.Video;

public class MysqlDatabseHelper {

	private static Logger logger = Logger.getLogger(MysqlDatabseHelper.class
			.getName());

	/**
	 * 批量导入影片信息[不包含缺二进制海报图片]
	 * @param list	影片列表
	 */
	public static void batchAddVideoWithoutImage(List<Video> list) {

		Connection conn = null;
		PreparedStatement pst = null;

		try {
			conn = MysqlConnectionManager.getConnection();
			// 关闭事务自动提交
			conn.setAutoCommit(false);

			if (conn != null) {
				pst = (PreparedStatement) conn
						.prepareStatement("insert into "
								+ " ent_movie "
								+ "(uuid, number, title, url, imageSource, category, year, starring, rate, introduction) "
								+ " VALUES (?,?,?,?,?,?,?,?,?,?)");
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
	 * @param list	影片列表
	 */
	public static void batchAddVideo(List<Video> list) {

		Connection conn = null;
		PreparedStatement pst = null;

		try {
			conn = MysqlConnectionManager.getConnection();
			// 关闭事务自动提交
			conn.setAutoCommit(false);

			if (conn != null) {
				pst = (PreparedStatement) conn
						.prepareStatement("insert into "
								+ " ent_movie "
								+ "(uuid, number, title, url, imageSource, image, category, year, starring, rate, introduction) "
								+ " VALUES (?,?,?,?,?,?,?,?,?,?,?)");
				for (Video video : list) {
					pst.setString(1, UUID.randomUUID().toString());
					pst.setInt(2, video.getNumber());
					pst.setString(3, video.getTitle());
					pst.setString(4, video.getUrl());
					String imgSrcUrl = video.getImageSource();
					pst.setString(5, imgSrcUrl);
//					if (Constant.IMG_NO_PIC.equals(imgSrcUrl)) {
//						imgSrcUrl = Constant.URL_XINYINGBA + imgSrcUrl;
//					}
					InputStream is = ImageUtil.getInputStream(imgSrcUrl);
					if(null != is){
						pst.setBinaryStream(6, is, is.available());
					} else {
						pst.setNull(6, java.sql.Types.BLOB);
					}
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
		} catch (IOException e) {
			// e.printStackTrace();
			logger.error("IOException[MysqlDatabseHelper->batchAddVideo]: "
					+ e.getMessage());
		} finally {
			MysqlConnectionManager.closePreparedStatement(pst);
			MysqlConnectionManager.closeConnection(conn);
		}
	}
	
	public static List<Video> getVideoList() {
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<Video> list = new ArrayList<Video>();
		
		try {
			conn = MysqlConnectionManager.getConnection();
			if (conn != null) {
				stmt = conn.createStatement();
				rs = stmt.executeQuery("SELECT uuid,number,imageSource FROM ent_movie");
				while (rs.next()) {
					Video video = new Video();
					video.setUuid(rs.getString(1));
					video.setNumber(rs.getInt(2));
					video.setImageSource(rs.getString(3));
//					logger.debug(video.toString());
					list.add(video);
				}
				logger.info("已获取影片海报地址信息");
			}
			
		} catch (SQLException e) {
			logger.error("SQLException[MysqlDatabseHelper->getVideoList]: " + e.getMessage());
			// e.printStackTrace();
		} finally {
			MysqlConnectionManager.closeResultSet(rs);
			MysqlConnectionManager.closeStatement(stmt);
			MysqlConnectionManager.closeConnection(conn);
		}
		return list;
	}
	
	public static void batchUpdateImage(List<Video> list) {

		Connection conn = null;
		PreparedStatement pst = null;
		
		try {
			conn = MysqlConnectionManager.getConnection();
			// 关闭事务自动提交
			conn.setAutoCommit(false);

			if (conn != null) {
				pst = (PreparedStatement) conn
						.prepareStatement("UPDATE ent_movie SET image = ? WHERE uuid = ?");
//				int i = 0;
				for (Video video : list) {
//					stmt.addBatch("UPDATE ent_movie SET image = ? WHERE uuid = '" + video.getUuid() + "'");
//					String imgSrcUrl = video.getImageSource();
//					if (Constant.IMG_NO_PIC.equals(imgSrcUrl)) {
//						imgSrcUrl = Constant.URL_XINYINGBA + imgSrcUrl;
//					}
					InputStream is = ImageUtil.getInputStream(video.getImageSource());
					if(null != is){
						pst.setBinaryStream(1, is, is.available());
					} else {
						pst.setNull(1, java.sql.Types.BLOB);
					}
					pst.setString(2, video.getUuid());
					// 把一个SQL命令加入命令列表
					pst.addBatch();
//					i++;
//					if(i == 2){
//						break;
//					}
				}

				// 执行批量更新
				pst.executeBatch();
				// 语句执行完毕，提交本事务
				conn.commit();
				logger.info("批量更新影片海报信息成功！");
			}
		} catch (SQLException e) {
			logger.error("SQLException[MysqlDatabseHelper->batchUpdateImage]: "
					+ e.getMessage());
		} catch (IOException e) {
			logger.error("IOException[MysqlDatabseHelper->batchUpdateImage]: "
					+ e.getMessage());
		} finally {
			MysqlConnectionManager.closePreparedStatement(pst);
			MysqlConnectionManager.closeConnection(conn);
		}
	}
	
}