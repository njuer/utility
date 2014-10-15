package org.minnie.webservice.client.ces;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.minnie.utility.util.Constant;

import ces.sdk.sdk.db.Base64;
import ces.sdk.sso.bean.SsoToken;
import ces.sdk.sso.util.SsoTokenUtil;

public class LoginCheck {

	sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
	
//	String endpointURL = "http://58.215.198.186:1080/YXHKY/longinBySsotoken@LoginAction.action?cesssotoken=";
	
	
    public static void main(String[] args) {
		
		//System.currentTimeMillis();
		
//		new LoginCheck().check();
		
		new TestImportService().importFile2();
    	

	}
	
	
	
	public void check(){
		
		SsoToken ssoToken = new SsoToken();
		ssoToken.setTokenId(String.valueOf(System.currentTimeMillis()));
		ssoToken.setLoginName("档案管理员");
		ssoToken.setUserName("admin");
		ssoToken.setExpireDate(new Date(new Date().getTime() + 3600*1000));
		// 2. 转化成令牌字符串
		String token = SsoTokenUtil.encodeToken(ssoToken);
		// 3. 令牌加密
		String encodeToken = new String(Base64.encode(token.getBytes()));
		
		System.out.println(new String(Base64.decode(encodeToken)));
		
		System.out.println(encodeToken);


		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String responseBody = null;

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("cesssotoken", encodeToken));
		
		try {

			URIBuilder uriBuilder = new URIBuilder();
			uriBuilder.setScheme("http");
			uriBuilder.setHost("58.215.198.186");
			uriBuilder.setPort(1080);
			uriBuilder.setPath("/YXHKY/gdda/longinBySsotoken@LoginAction.action");
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
			
			System.out.println(responseBody);
			
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
		
	}
	
	
	
}
