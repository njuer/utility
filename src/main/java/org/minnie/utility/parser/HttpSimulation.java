package org.minnie.utility.parser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.minnie.utility.util.Constant;

/**
 * @author neiplzer@gmail.com
 * @version 2014-3-21 类说明
 */
public class HttpSimulation {

	private static Logger logger = Logger.getLogger(HttpSimulation.class
			.getName());

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String action = "http://www.17500.cn/ssq/newhot.php";

		List<NameValuePair> paramPair = new ArrayList<NameValuePair>();
		paramPair.add(new BasicNameValuePair("all", "1"));
		paramPair.add(new BasicNameValuePair("bh[]", ""));
		paramPair.add(new BasicNameValuePair("bh[]", ""));
		paramPair.add(new BasicNameValuePair("bh[]", ""));
		paramPair.add(new BasicNameValuePair("hz1", "60"));
		paramPair.add(new BasicNameValuePair("hz2", "140"));
		paramPair.add(new BasicNameValuePair("jo[]", "2"));
		paramPair.add(new BasicNameValuePair("jo[]", "3"));
		paramPair.add(new BasicNameValuePair("jo[]", "4"));
		paramPair.add(new BasicNameValuePair("mid[]", "01"));
		paramPair.add(new BasicNameValuePair("mid[]", "02"));
		paramPair.add(new BasicNameValuePair("mid[]", "03"));
		paramPair.add(new BasicNameValuePair("mid[]", "04"));
		paramPair.add(new BasicNameValuePair("mid[]", "05"));
		paramPair.add(new BasicNameValuePair("mid[]", "06"));
		paramPair.add(new BasicNameValuePair("mid[]", "07"));
		paramPair.add(new BasicNameValuePair("mid[]", "08"));
		paramPair.add(new BasicNameValuePair("mid[]", "09"));
		paramPair.add(new BasicNameValuePair("mid[]", "10"));
		paramPair.add(new BasicNameValuePair("mid[]", "11"));
		paramPair.add(new BasicNameValuePair("mid[]", "12"));
		paramPair.add(new BasicNameValuePair("mid[]", "13"));
		paramPair.add(new BasicNameValuePair("mid[]", "14"));
		paramPair.add(new BasicNameValuePair("mid[]", "15"));
		paramPair.add(new BasicNameValuePair("mid[]", "16"));
		paramPair.add(new BasicNameValuePair("mid[]", "17"));
		paramPair.add(new BasicNameValuePair("mid[]", "18"));
		paramPair.add(new BasicNameValuePair("mid[]", "19"));
		paramPair.add(new BasicNameValuePair("mid[]", "20"));
		paramPair.add(new BasicNameValuePair("mid[]", "21"));
		paramPair.add(new BasicNameValuePair("mid[]", "22"));
		paramPair.add(new BasicNameValuePair("mid[]", "23"));
		paramPair.add(new BasicNameValuePair("mid[]", "24"));
		paramPair.add(new BasicNameValuePair("mid[]", "25"));
		paramPair.add(new BasicNameValuePair("mid[]", "26"));
		paramPair.add(new BasicNameValuePair("mid[]", "27"));
		paramPair.add(new BasicNameValuePair("mid[]", "28"));
		paramPair.add(new BasicNameValuePair("mid[]", "29"));
		paramPair.add(new BasicNameValuePair("mid[]", "30"));
		paramPair.add(new BasicNameValuePair("mid[]", "31"));
		paramPair.add(new BasicNameValuePair("mid[]", "32"));
		paramPair.add(new BasicNameValuePair("mid[]", "33"));
		paramPair
				.add(new BasicNameValuePair("pastresult", "12,18,19,23,24,30"));
		paramPair.add(new BasicNameValuePair("th[]", "0"));
		paramPair.add(new BasicNameValuePair("th[]", "1"));
		paramPair.add(new BasicNameValuePair("th[]", "2"));
		paramPair.add(new BasicNameValuePair("zhushu", "100"));

		HttpSimulation hs = new HttpSimulation();

		System.out.println(hs.getResponseBodyByPost(action, paramPair, "gbk"));

	}

	public String getResponseBodyByPost(String action,
			List<NameValuePair> nvps, String charset) {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		String responseBody = null;

		try {
			HttpPost post = new HttpPost(action);

			// System.out.println("Executing request " + post.getRequestLine());

			UrlEncodedFormEntity params = new UrlEncodedFormEntity(nvps,
					charset);
			post.setEntity(params);

			// Create a custom response handler
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

				public String handleResponse(final HttpResponse response)
						throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity)
								: null;
					} else {
						throw new ClientProtocolException(
								"Unexpected response status: " + status);
					}
				}

			};

			// Execute the request And Get Response Body
			responseBody = httpclient.execute(post, responseHandler);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responseBody;
	}

	public void getFileByDefaultSchemePort(String host, String uriPath,
			List<NameValuePair> nvps, String filePath) {
		downloadFileByGet("http", host, 80, uriPath, nvps, filePath);
	}

	/**
	 * 
	 * @param scheme
	 *            URI scheme
	 * @param host
	 *            URI host
	 * @param port
	 *            URI port
	 * @param uriPath
	 *            URI path. The value is expected to be unescaped and may
	 *            contain non ASCII characters.
	 * @param nvps
	 *            URI query parameters. The parameter name / values are expected
	 *            to be unescaped and may contain non ASCII characters.
	 * @param filePath
	 *            Target file destination path
	 */
	public void downloadFileByGet(String scheme, String host, int port,
			String uriPath, List<NameValuePair> nvps, String filePath) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;

		try {
			URIBuilder uriBuilder = new URIBuilder();
			uriBuilder.setScheme(scheme);
			uriBuilder.setHost(host);
			uriBuilder.setPort(port);
			uriBuilder.setPath(uriPath);
			uriBuilder.setParameters(nvps);

			HttpGet httpGet = new HttpGet(uriBuilder.build());
			httpGet.setHeader("User-Agent", Constant.USER_AGENT_IE);

			response = httpClient.execute(httpGet);

			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				// 请求成功
				// 取得请求内容
				HttpEntity entity = response.getEntity();
				// 显示内容
				if (entity != null) {
					// 设置本地保存的文件
					File destFile = new File(filePath);
					FileOutputStream output = new FileOutputStream(destFile);
					// 得到网络资源并写入文件
					InputStream is = entity.getContent();
					byte bt[] = new byte[Constant.BUFFER_SIZE_1024];
					int index = 0;
					while ((index = is.read(bt)) != -1) {
						output.write(bt, 0, index);
					}
					output.flush();
					output.close();
					is.close();
				}
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != response) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * POST方式下载文件
	 * 
	 * @param action
	 *            URI path. The value is expected to be unescaped and may
	 *            contain non ASCII characters.
	 * @param nvps
	 *            URI query parameters. The parameter name / values are expected
	 *            to be unescaped and may contain non ASCII characters.
	 * @param directory
	 *            Target file destination directory
	 */
	public void downloadFileByPost(String action, List<NameValuePair> nvps,
			String directory) {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {

			HttpPost httpPost = new HttpPost(action);
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
			httpPost.setHeader("User-Agent", Constant.USER_AGENT_IE);
			CloseableHttpResponse response = httpclient.execute(httpPost);

			logger.info("Download from [" + action + "], StatusLine = "
					+ response.getStatusLine());

			String fileName = getFileName(response);
			
			if(null == fileName){
				fileName = action.substring(action.lastIndexOf("/") + 1);
				fileName = fileName.substring(0, fileName.lastIndexOf("."));
			}
			
			HttpEntity entity = response.getEntity();

			// 显示内容
			if (entity != null) {
				// 如果目录不存在，则创建目录
				File dir = new File(directory);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				String path = directory + File.separator + fileName;
				File destFile = new File(path);
				
				FileOutputStream output = new FileOutputStream(destFile);
				// 得到网络资源并写入文件
				InputStream is = entity.getContent();
				byte bt[] = new byte[Constant.BUFFER_SIZE_1024];
				int index = 0;
				while ((index = is.read(bt)) != -1) {
					output.write(bt, 0, index);
				}
				output.flush();
				output.close();
				is.close();
			}
			// and ensure it is fully consumed
			EntityUtils.consume(entity);

			response.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String getResponseBodyByGet(String host, String uriPath,
			List<NameValuePair> nvps) {
		return getResponseBodyByGet("http", host, 80, uriPath, nvps);
	}

	public String getResponseBodyByGet(String scheme, String host, int port,
			String uriPath, List<NameValuePair> nvps) {

		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String responseBody = null;

		try {

			URIBuilder uriBuilder = new URIBuilder();
			uriBuilder.setScheme(scheme);
			uriBuilder.setHost(host);
			uriBuilder.setPort(port);
			uriBuilder.setPath(uriPath);
			uriBuilder.setParameters(nvps);

			HttpGet httpGet = new HttpGet(uriBuilder.build());
			httpGet.setHeader("User-Agent", Constant.USER_AGENT_IE);

			response = httpClient.execute(httpGet);

			// Create a custom response handler
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

				public String handleResponse(final HttpResponse response)
						throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (HttpStatus.SC_OK == status) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity)
								: null;
					} else {
						throw new ClientProtocolException(
								"Unexpected response status: " + status);
					}
				}

			};
			responseBody = httpClient.execute(httpGet, responseHandler);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (null != httpClient) {
					httpClient.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return responseBody;
	}

	/**
	 * 获取Http请求中的文件名
	 * @param response
	 * @return
	 */
	public String getFileName(HttpResponse response) {
		String filename = null;
		if (null != response) {
			Header responseHeader = response
					.getFirstHeader("Content-Disposition");
			if (null != responseHeader) {
				HeaderElement[] values = responseHeader.getElements();
				if (values.length == 1) {
					NameValuePair param = values[0]
							.getParameterByName("filename");
					if (null != param) {
						try {
							// filename = new
							// String(param.getValue().toString().getBytes(),
							// "utf-8");
							// filename=URLDecoder.decode(param.getValue(),"utf-8");
							filename = param.getValue();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return filename;
	}
}
