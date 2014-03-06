package org.minnie.utility.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.minnie.utility.xinyingba.Video;

/**
 * 将文件打包成ZIP压缩文件
 * 
 * @author wuf
 * 
 */
public class FileUtil {

	private static Logger logger = Logger.getLogger(FileUtil.class.getName());
	public static final int IO_BUFFER_SIZE = 8 * 1024;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String sourceFilePath = "C:\\home\\pic";
		String zipFilePath = "C:\\home";
		String fileName = "lp20120301.zip";
		// boolean flag = compressAsZip(sourceFilePath, zipFilePath, fileName);
		// if (flag) {
		// logger.info(">>>>>> 文件打包成功. <<<<<<");
		// } else {
		// logger.info(">>>>>> 文件打包失败. <<<<<<");
		// }

		File sourceDir = new File(sourceFilePath);
		File[] sourceFiles = sourceDir.listFiles();
		for (int i = 0; i < sourceFiles.length; i++) {
			String name = sourceFiles[i].getName();
			String number = name.substring(0, name.indexOf("."));
			if (StringUtils.isNumeric(number)) {
				System.out.println(Integer.valueOf(number));
			}

		}

	}

	/**
	 * 将存放在sourceFilePath目录下的源文件,打包成fileName名称的ZIP文件,并存放到zipFilePath。
	 * 
	 * @param sourceFileDirectory
	 *            待压缩的文件路径
	 * @param zipFileDirectory
	 *            压缩后存放路径
	 * @param zipFileName
	 *            压缩后文件的名称
	 * @return flag
	 */
	public static boolean compressAsZip(String sourceFileDirectory,
			String zipFileDirectory, String zipFileName) {

		boolean flag = false;
		File sourceDir = new File(sourceFileDirectory);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		ZipOutputStream zos = null;

		if (sourceDir.exists()) {
			try {
				File targetZipFile = new File(zipFileDirectory + File.separator
						+ zipFileName);
				if (!targetZipFile.exists()) {
					File[] sourceFiles = sourceDir.listFiles();
					int sourceFileLength = sourceFiles.length;
					if (null == sourceFiles || sourceFileLength < 1) {
						logger.info(">>>>>> 待压缩的文件目录：" + sourceFileDirectory
								+ " 里面不存在文件,无需压缩. <<<<<<");
					} else {
						fos = new FileOutputStream(targetZipFile);
						zos = new ZipOutputStream(new BufferedOutputStream(fos));
						byte[] bufs = new byte[IO_BUFFER_SIZE];
						for (int i = 0; i < sourceFileLength; i++) {
							// 创建ZIP实体,并添加进压缩包
							ZipEntry zipEntry = new ZipEntry(
									sourceFiles[i].getName());
							zos.putNextEntry(zipEntry);
							// 读取待压缩的文件并写进压缩包里
							fis = new FileInputStream(sourceFiles[i]);
							bis = new BufferedInputStream(fis, IO_BUFFER_SIZE);
							int read = 0;
							while ((read = bis.read(bufs, 0, IO_BUFFER_SIZE)) != -1) {
								zos.write(bufs, 0, read);
							}
						}
						flag = true;
					}
				} else {
					logger.info(">>>>>> " + zipFileDirectory + " 目录下存在名字为："
							+ zipFileName + " 打包文件. <<<<<<");
				}
			} catch (FileNotFoundException e) {
				logger.error("FileNotFoundException[FileUtil->compressAsZip]: "
						+ e.getMessage());
			} catch (IOException e) {
				logger.error("IOException[FileUtil->compressAsZip]: "
						+ e.getMessage());
			} finally {
				// 关闭流
				try {
					if (null != bis)
						bis.close();
					if (null != zos)
						zos.close();
				} catch (IOException e) {
					logger.error("IOException[FileUtil->compressAsZip]: "
							+ e.getMessage());
				}
			}
		} else {
			logger.info(">>>>>> 待压缩的文件目录：" + sourceFileDirectory
					+ " 不存在. <<<<<<");
		}

		return flag;
	}

	public static Set<Integer> getOmittedNumber(String sourceFileDirectory,
			Set<Integer> videoSet) {
		
		Set<Integer> fileSet = new HashSet<Integer>();
		Set<Integer> resultSet = new HashSet<Integer>();

		File sourceDir = new File(sourceFileDirectory);
		
		if (sourceDir.exists()) {
			File[] sourceFiles = sourceDir.listFiles();
			int sourceFileLength = sourceFiles.length;
			for (int i = 0; i < sourceFileLength; i++) {
				String name = sourceFiles[i].getName();
				String number = name.substring(0, name.indexOf("."));
				if (StringUtils.isNumeric(number)) {
					fileSet.add(Integer.valueOf(number));
				}
			}
		}
		
		resultSet.addAll(videoSet);
		resultSet.removeAll(fileSet);

		return resultSet;

	}

}