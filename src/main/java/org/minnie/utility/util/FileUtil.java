package org.minnie.utility.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
		
		if(!file.exists()){
			return true;
		}
		
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
	
	/**
	 * 创建单个文件
	 * @param descFileName 文件名，包含路径
	 * @return 如果创建成功，则返回true，否则返回false
	 */
	public static boolean createFile(String descFileName) {
		File file = new File(descFileName);
		if (file.exists()) {
			logger.debug("文件 " + descFileName + " 已存在!");
			return false;
		}
		if (descFileName.endsWith(File.separator)) {
			logger.debug(descFileName + " 为目录，不能创建目录!");
			return false;
		}
		if (!file.getParentFile().exists()) {
			// 如果文件所在的目录不存在，则创建目录
			if (!file.getParentFile().mkdirs()) {
				logger.debug("创建文件所在的目录失败!");
				return false;
			}
		}

		// 创建文件
		try {
			if (file.createNewFile()) {
				logger.debug(descFileName + " 文件创建成功!");
				return true;
			} else {
				logger.debug(descFileName + " 文件创建失败!");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(descFileName + " 文件创建失败!");
			return false;
		}

	}
	
	/**
	 * 复制单个文件，如果目标文件存在，则不覆盖
	 * @param srcFileName 待复制的文件名
	 * @param descFileName 目标文件名
	 * @return 如果复制成功，则返回true，否则返回false
	 */
	public static boolean copyFile(String srcFileName, String descFileName) {
		return copyFileCover(srcFileName, descFileName, false);
	}

	/**
	 * 复制单个文件
	 * @param srcFileName 待复制的文件名
	 * @param descFileName 目标文件名
	 * @param coverlay 如果目标文件已存在，是否覆盖
	 * @return 如果复制成功，则返回true，否则返回false
	 */
	public static boolean copyFileCover(String srcFileName,
			String descFileName, boolean coverlay) {
		File srcFile = new File(srcFileName);
		// 判断源文件是否存在
		if (!srcFile.exists()) {
			logger.debug("复制文件失败，源文件 " + srcFileName + " 不存在!");
			return false;
		}
		// 判断源文件是否是合法的文件
		else if (!srcFile.isFile()) {
			logger.debug("复制文件失败，" + srcFileName + " 不是一个文件!");
			return false;
		}
		File descFile = new File(descFileName);
		// 判断目标文件是否存在
		if (descFile.exists()) {
			// 如果目标文件存在，并且允许覆盖
			if (coverlay) {
				logger.debug("目标文件已存在，准备删除!");
				if (!delFile(descFileName)) {
					logger.debug("删除目标文件 " + descFileName + " 失败!");
					return false;
				}
			} else {
				logger.debug("复制文件失败，目标文件 " + descFileName + " 已存在!");
				return false;
			}
		} else {
			if (!descFile.getParentFile().exists()) {
				// 如果目标文件所在的目录不存在，则创建目录
				logger.debug("目标文件所在的目录不存在，创建目录!");
				// 创建目标文件所在的目录
				if (!descFile.getParentFile().mkdirs()) {
					logger.debug("创建目标文件所在的目录失败!");
					return false;
				}
			}
		}

		// 准备复制文件
		// 读取的位数
		int readByte = 0;
		InputStream ins = null;
		OutputStream outs = null;
		try {
			// 打开源文件
			ins = new FileInputStream(srcFile);
			// 打开目标文件的输出流
			outs = new FileOutputStream(descFile);
			byte[] buf = new byte[1024];
			// 一次读取1024个字节，当readByte为-1时表示文件已经读取完毕
			while ((readByte = ins.read(buf)) != -1) {
				// 将读取的字节流写入到输出流
				outs.write(buf, 0, readByte);
			}
			logger.debug("复制单个文件 " + srcFileName + " 到" + descFileName
					+ "成功!");
			return true;
		} catch (Exception e) {
			logger.debug("复制文件失败：" + e.getMessage());
			return false;
		} finally {
			// 关闭输入输出流，首先关闭输出流，然后再关闭输入流
			if (outs != null) {
				try {
					outs.close();
				} catch (IOException oute) {
					oute.printStackTrace();
				}
			}
			if (ins != null) {
				try {
					ins.close();
				} catch (IOException ine) {
					ine.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 
	 * 删除文件，可以删除单个文件或文件夹
	 * 
	 * @param fileName 被删除的文件名
	 * @return 如果删除成功，则返回true，否是返回false
	 */
	public static boolean delFile(String fileName) {
 		File file = new File(fileName);
		if (!file.exists()) {
			logger.debug(fileName + " 文件不存在!");
			return true;
		} else {
			if (file.isFile()) {
				return deleteFile(fileName);
			} else {
				return deleteDirectory(fileName);
			}
		}
	}
	
	/**
	 * 
	 * 删除目录及目录下的文件
	 * 
	 * @param dirName 被删除的目录所在的文件路径
	 * @return 如果目录删除成功，则返回true，否则返回false
	 */
	public static boolean deleteDirectory(String dirName) {
		String dirNames = dirName;
		if (!dirNames.endsWith(File.separator)) {
			dirNames = dirNames + File.separator;
		}
		File dirFile = new File(dirNames);
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			logger.debug(dirNames + " 目录不存在!");
			return true;
		}
		boolean flag = true;
		// 列出全部文件及子目录
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				// 如果删除文件失败，则退出循环
				if (!flag) {
					break;
				}
			}
			// 删除子目录
			else if (files[i].isDirectory()) {
				flag = deleteDirectory(files[i].getAbsolutePath());
				// 如果删除子目录失败，则退出循环
				if (!flag) {
					break;
				}
			}
		}

		if (!flag) {
			logger.debug("删除目录失败!");
			return false;
		}
		// 删除当前目录
		if (dirFile.delete()) {
			logger.debug("删除目录 " + dirName + " 成功!");
			return true;
		} else {
			logger.debug("删除目录 " + dirName + " 失败!");
			return false;
		}

	}

}