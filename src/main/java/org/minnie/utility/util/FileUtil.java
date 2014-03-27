package org.minnie.utility.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

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
	 * 删除此路径名表示的文件或目录。
	 * 如果此路径名表示一个目录，则会先删除目录下的内容再将目录删除，所以该操作不是原子性的。
	 * 如果目录中还有目录，则会引发递归动作。
	 * @param filePath
	 *            要删除文件或目录的路径。
	 * @return 当且仅当成功删除文件或目录时，返回 true；否则返回 false。
	 */
	public static boolean deleteFile(String filePath) {
		File file = new File(filePath);
		return deleteFile(file);
	}
	
	private static boolean deleteFile(File file){
		File[] files = file.listFiles();
		for(File deleteFile : files){
			if(deleteFile.isDirectory()){
				//如果是文件夹，则递归删除下面的文件后再删除该文件夹
				if(!deleteFile(deleteFile)){
					//如果失败则返回
					return false;
				}
			} else {
				if(!deleteFile.delete()){
					//如果失败则返回
					return false;
				}
			}
		}
		return file.delete();
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

	public static Set<String> getHoopChinaUrlSet(String filePath) {

		Set<String> set = new HashSet<String>();
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					filePath)));

			for (String line = br.readLine(); line != null; line = br
					.readLine()) {
				set.add(line);
			}
			br.close();
		} catch (FileNotFoundException e) {
			logger.error("FileNotFoundException[FileUtil->getHoopChinaUrlList]: "
					+ e.getMessage());
		} catch (IOException e) {
			logger.error("IOException[FileUtil->getHoopChinaUrlList]: "
					+ e.getMessage());
		}

		return set;
	}

	public static Set<String> getNeteaseUrlSet(String filePath) {

		Set<String> set = new HashSet<String>();
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					filePath)));

			for (String line = br.readLine(); line != null; line = br
					.readLine()) {
				set.add(line);
			}
			br.close();
		} catch (FileNotFoundException e) {
			logger.error("FileNotFoundException[FileUtil->getNeteaseUrlList]: "
					+ e.getMessage());
		} catch (IOException e) {
			logger.error("IOException[FileUtil->getNeteaseUrlList]: "
					+ e.getMessage());
		}

		return set;
	}

	public static Set<String> getYangShengSuoUrlSet(String filePath) {

		Set<String> set = new HashSet<String>();
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					filePath)));

			for (String line = br.readLine(); line != null; line = br
					.readLine()) {
				set.add(line);
			}
			br.close();
		} catch (FileNotFoundException e) {
			logger.error("FileNotFoundException[FileUtil->getYangShengSuoUrlList]: "
					+ e.getMessage());
		} catch (IOException e) {
			logger.error("IOException[FileUtil->getYangShengSuoUrlList]: "
					+ e.getMessage());
		}

		return set;
	}

	public static Set<String> getUrlSet(String filePath) {

		Set<String> set = new HashSet<String>();
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					filePath)));

			for (String line = br.readLine(); line != null; line = br
					.readLine()) {
				set.add(line);
			}
			br.close();
		} catch (FileNotFoundException e) {
			logger.error("FileNotFoundException[FileUtil->getNeteaseUrlList]: "
					+ e.getMessage());
		} catch (IOException e) {
			logger.error("IOException[FileUtil->getNeteaseUrlList]: "
					+ e.getMessage());
		}

		return set;
	}

	/**
	 * 获取directory目录下一级子文件的清单
	 * @param directory
	 * @return
	 */
	public static List<String> getDirectory(File directory) {
		List<String> list = new ArrayList<String>();
		File files[] = directory.listFiles();
		if (files == null || files.length == 0) {
			return list;
		}

		for (File file : files) {
			if (!file.isDirectory()) {
				// 这里将列出所有的文件
//				System.out.println("file==>" + file.getAbsolutePath());
				list.add(file.getAbsolutePath());
			}
		}

		return list;
	}

	public void getSubFile(String FileName) {
		String dirName = "D:/lottery/gdd11";

		ArrayList nameList = new ArrayList();
		File parentF = new File(FileName);
		if (!parentF.exists()) {
			System.out.println("文件或目录不存在");
			return;
		}
		if (parentF.isFile()) {
			nameList.add(parentF.getAbsoluteFile());
			return;
		}
		String[] subFiles = parentF.list();
		for (int i = 0; i < subFiles.length; i++) {
			getSubFile(dirName + "/" + subFiles[i]);
		}

	}

}