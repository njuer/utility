package org.minnie.utility.parser;
/**
 * @author neiplzer@gmail.com
 * @version 2014-3-23
 * 类说明
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.minnie.utility.util.Constant;

public class SearchDomain {

	public static void main(String[] args) throws ClientProtocolException, IOException {
		//实例化一个HttpClient
		HttpClient httpClient = new DefaultHttpClient();
		//设定目标站点  web的默认端口80可以不写的 当然如果是其它端口就要标明                                                           
		HttpHost httpHost = new HttpHost("zx.caipiao.163.com",80);
		//设置需要下载的文件
		HttpGet httpGet = new HttpGet("/trend/downloadTrendAwardNumber.html?gameEn=hljd11&beginPeriod=14032301&endPeriod=14032329");
		//这里也可以直接使用httpGet的绝对地址，当然如果不是具体地址不要忘记/结尾
		//HttpGet httpGet = new HttpGet("http://www.0431.la/");
		//HttpResponse response = httpClient.execute(httpGet);
		

		
		
		HttpResponse response = httpClient.execute(httpHost, httpGet);
		if(HttpStatus.SC_OK==response.getStatusLine().getStatusCode()){
			//请求成功
			//取得请求内容
			HttpEntity entity = response.getEntity();
			
			//显示内容
			if (entity != null) {
				//这里可以得到文件的类型 如image/jpg /zip /tiff 等等 但是发现并不是十分有效，有时明明后缀是.rar但是取到的是null，这点特别说明
				System.out.println(entity.getContentType());
				//可以判断是否是文件数据流
				System.out.println(entity.isStreaming());
				//设置本地保存的文件
				File storeFile = new File("d:/0431la.xls");  
				FileOutputStream output = new FileOutputStream(storeFile);
				//得到网络资源并写入文件
				InputStream input = entity.getContent();
				byte b[] = new byte[1024];
				int j = 0;
				while( (j = input.read(b))!=-1){
					output.write(b,0,j);
				}
				output.flush();
				output.close(); 
			}
			if (entity != null) {
				entity.consumeContent();
			}
		}
	}
}

