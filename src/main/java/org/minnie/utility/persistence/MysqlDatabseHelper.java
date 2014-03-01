package org.minnie.utility.persistence;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.minnie.utility.util.Constant;
import org.minnie.utility.util.ImageUtil;
import org.minnie.utility.xinyingba.Video;

public class MysqlDatabseHelper {

	private static Logger logger = Logger.getLogger(MysqlDatabseHelper.class
			.getName());

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
					String imgSrcUrl = video.getImageSource();
					pst.setString(5, imgSrcUrl);
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
					if (Constant.IMG_NO_PIC.equals(imgSrcUrl)) {
						imgSrcUrl = Constant.URL_XINYINGBA + imgSrcUrl;
					}
					InputStream is = ImageUtil.getInputStream(imgSrcUrl);
					pst.setBinaryStream(6, is, is.available());
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

	public static void batchAddVideo1() {

		Connection conn = null;
		PreparedStatement pst = null;

		// File file = new
		// File("http://yezi-img.stor.sinaapp.com/dy/201402/17775.jpg");
		// 构造URL
		URL url;

		try {

			url = new URL(
					"http://yezi-img.stor.sinaapp.com/dy/201402/17775.jpg");

			// 打开连接
			URLConnection con = url.openConnection();
			// 输入流
			InputStream is = con.getInputStream();

			conn = MysqlConnectionManager.getConnection();
			// 关闭事务自动提交
			conn.setAutoCommit(false);

			if (conn != null) {
				pst = (PreparedStatement) conn.prepareStatement("insert into "
						+ " test " + "(image) " + " VALUES (?)");
				pst.setBinaryStream(1, is, is.available());
				pst.addBatch();
				// for(Video video : list){
				// pst.setString(1, UUID.randomUUID().toString());
				// pst.setInt(2, video.getNumber());
				// pst.setString(3, video.getTitle());
				// pst.setString(4, video.getUrl());
				// pst.setString(5, video.getImageSource());
				// pst.setString(6, video.getCategory());
				// pst.setInt(7, video.getYear());
				// pst.setString(8, video.getStarring());
				// pst.setFloat(9, video.getRate());
				// pst.setString(10, video.getIntroduction());
				// // 把一个SQL命令加入命令列表
				// pst.addBatch();
				// }

				// 执行批量更新
				pst.executeBatch();
				// 语句执行完毕，提交本事务
				conn.commit();
				logger.info("批量导入影片信息成功！");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			MysqlConnectionManager.closePreparedStatement(pst);
			MysqlConnectionManager.closeConnection(conn);
		}
	}

}