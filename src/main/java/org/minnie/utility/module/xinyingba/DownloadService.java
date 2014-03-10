package org.minnie.utility.module.xinyingba;

/**
 * @author 作者名 E-mail:neiplzer@gmail.com
 * @version 创建时间：2014-3-5 下午3:37:20
 * 类说明
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

/**
 * 批量图片下载类 无需与界面交互的下载类
 * 
 * @Description:
 * 
 * @author- liubing
 * @create- 2013 -5 - 6
 * @modify by:
 * @modify- time
 */
public class DownloadService {
	
	public static final int IO_BUFFER_SIZE = 8 * 1024;
	private static ExecutorService SINGLE_TASK_EXECUTOR = null;
	private static ExecutorService LIMITED_TASK_EXECUTOR = null;
	private static final ExecutorService FULL_TASK_EXECUTOR = null;
	private static final ExecutorService DEFAULT_TASK_EXECUTOR;
	private static Object lock = new Object();
	
	static {
		// SINGLE_TASK_EXECUTOR = (ExecutorService)
		// Executors.newSingleThreadExecutor();
		LIMITED_TASK_EXECUTOR = (ExecutorService) Executors
				.newFixedThreadPool(40);
		// FULL_TASK_EXECUTOR = (ExecutorService)
		// Executors.newCachedThreadPool();
		DEFAULT_TASK_EXECUTOR = LIMITED_TASK_EXECUTOR;
	};
	// 下载状态监听，提供回调
	DownloadStateListener listener;
	// 下载目录
	private String downloadPath;

	// 下载链接集合
	private List<Video> videoList;
	// 下载个数
	private int size = 0;

	// 下载完成回调接口
	public interface DownloadStateListener {
		public void onFinish();
		public void onFailed();
	}

	public DownloadService(String downloadPath, List<Video> videoList,
			DownloadStateListener listener) {
		this.downloadPath = downloadPath;
		this.videoList = videoList;
		this.listener = listener;
	}

	/**
	 * 暂未提供设置
	 */
	public void setDefaultExecutor() {

	}

	/**
	 * 开始下载
	 */
	public void startDownload() {
		// 首先检测path是否存在
		File downloadDirectory = new File(downloadPath);
		if (!downloadDirectory.exists()) {
			downloadDirectory.mkdirs();
		}

		for (final Video video : videoList) {
			// 捕获线程池拒绝执行异常
			try {
				// 线程放入线程池
				DEFAULT_TASK_EXECUTOR.execute(new Runnable() {

					@Override
					public void run() {
						downloadBitmap(video);
						System.out.println(Thread.currentThread() + "下载："+video.getImageSource());
					}
				});
			} catch (RejectedExecutionException e) {
				e.printStackTrace();
				// Log. e( TAG, "thread pool rejected error");
				System.out.println("thread pool rejected error");
				listener.onFailed();
			} catch (Exception e) {
				e.printStackTrace();
				listener.onFailed();
			}

		}

	}

	/**
	 * 下载图片
	 * 
	 * @param urlString
	 * @return
	 */
	private File downloadBitmap(Video video) {
//		String fileName = urlString;
		String imageSource = video.getImageSource();
		// 图片命名方式
		final File cacheFile = new File(createFilePath(new File(downloadPath), imageSource));

		HttpURLConnection urlConnection = null;
		BufferedOutputStream out = null;

		try {
			final URL url = new URL(imageSource);
			urlConnection = (HttpURLConnection) url.openConnection();
			final InputStream in = new BufferedInputStream(
					urlConnection.getInputStream(), IO_BUFFER_SIZE);
			out = new BufferedOutputStream(new FileOutputStream(cacheFile),
					IO_BUFFER_SIZE);

			int b;
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			// 每下载成功一个，统计一下图片个数
			statDownloadNum();
			return cacheFile;

		} catch (final IOException e) {
			// 有一个下载失败，则表示批量下载没有成功
			// Log. e( TAG, "download " + urlString + " error");
			System.out.println("download " + video.getImageSource() + " error");
			listener.onFailed();

		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
			if (out != null) {
				try {
					out.close();
				} catch (final IOException e) {
					// Log. e( TAG, "Error in downloadBitmap - " + e);
					System.out.println("Error in downloadBitmap - " + e);
				}
			}
		}

		return null;
	}

	/**
	 * Creates a constant cache file path given a target cache directory and an
	 * image key.
	 * 
	 * @param cacheDir
	 * @param key
	 * @return
	 */
	public static String createFilePath(File cacheDir, String imageSource) {
		return cacheDir + File.separator + imageSource.substring(imageSource.lastIndexOf("/") + 1);
	}

	/**
	 * 统计下载个数
	 */
	private void statDownloadNum() {
		synchronized (lock) {
			size++;
			if (size == videoList.size()) {
				// Log. d( TAG, "download finished total " + size );
				System.out.println("download finished total " + size);
				// 释放资源
				DEFAULT_TASK_EXECUTOR.shutdownNow();
				// 如果下载成功的个数与列表中 url个数一致，说明下载成功
				listener.onFinish(); // 下载成功回调
			}
		}
	}
}

// 使用方式如下：
// new DownloadService( "/mnt/sdcard/test" , listUrl, new
// DownloadStateListener() {
//
// @Override
// public void onFinish() {
// //图片下载成功后，实现您的代码
//
// }
//
// @Override
// public void onFailed() {
// //图片下载成功后，实现您的代码
//
// }
// }).startDownload();