package com.everhomes.activity.ruian;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.user.UserContext;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WebApiRuianHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebApiRuianHelper.class);
	private static WebApiRuianHelper webApi = null;


	//以下三个值算是默认配置在此,可能过配置项进行修改,配置项优先
	private String publicKey = "d2NP2Z";
	private String privateKey = "a6cfff2c4aa370f8";
	private String AppID = "5b5046c988ce7e5ad49c9b10";

	public static WebApiRuianHelper getIns() {
		if (webApi == null) {
			webApi = new WebApiRuianHelper();
		}
		return webApi;
	}

	public String requestPost(String requestUrl, String jsonParameter ,Long communityId) {
		return httpsRequest(requestUrl, "POST", jsonParameter , communityId);
	}

	public String requestGet(String requestUrl, String jsonParameter,Long communityId) {
		return httpsRequest(requestUrl, "GET", jsonParameter , communityId);
	}

	private String httpsRequest(String requestUrl, String requestMethod, String jsonParameter , Long communityId) {
		StringBuffer buffer = null;
		try {
			SSLContext sslContext = SSLContext.getInstance("SSL");

			TrustManager[] tm = { new MyX509TrustManager() };
			sslContext.init(null, tm, new java.security.SecureRandom());
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			URL url = new URL(requestUrl);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);

			conn.setRequestProperty("Content-type", "application/json");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");

			conn.setRequestMethod(requestMethod);

			setHeadr(conn, jsonParameter ,communityId);
			conn.setRequestProperty("Content-Length", "0");
			DataOutputStream os = new DataOutputStream(conn.getOutputStream());
			os.write("".getBytes("UTF-8"), 0, 0);
			os.writeBytes(jsonParameter);
			
			os.flush();
			os.close();
			
			conn.setSSLSocketFactory(ssf);
			conn.connect();
			InputStream is = conn.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			buffer = new StringBuffer();
			String line = null;
			while ((line = br.readLine()) != null) {
				buffer.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (buffer != null) {
			return buffer.toString();
		} else {
			return "";
		}
	}

	/**
	 * 设置header参数
	 * @param conn
	 * @param jsonParameter
	 */
	private void setHeadr(HttpsURLConnection conn, String jsonParameter ,Long communityId) {
		if (jsonParameter == null) {
			jsonParameter = "";
		}
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateStr = simpleDateFormat.format(new Date());
		ConfigurationProvider configProvider = PlatformContext.getComponent(ConfigurationProvider.class);
		String publickey = "mall.ruian.publickey" ;
		String privatekey = "mall.ruian.privatekey" ;
		String appid = "mall.ruian.appid" ;
        if(communityId != null && communityId != 0){
			publickey = publickey + "."+ communityId ;
			privatekey = privatekey + "."+ communityId ;
			appid = appid + "."+ communityId ;
		}
		LOGGER.info("publickey:{};privatekey:{};appid:{}",publickey,privatekey,appid);

		String pkey =configProvider.getValue(namespaceId,publickey, publicKey) ;
		publicKey =  pkey==null?publicKey : pkey;
		String prkey = configProvider.getValue(namespaceId,privatekey, privateKey);
		privateKey = prkey==null?privateKey : prkey;
		String appId = configProvider.getValue(namespaceId,appid, AppID);
		AppID =	appId==null?AppID:appId ;

		String timeStamp = dateStr;
		String encryptString = "{publicKey:" + publicKey + ",timeStamp:" + timeStamp + ",data:" + jsonParameter + ",privateKey:" + privateKey + "}";
		LOGGER.info(encryptString);
		String sign = MD5.md5EncryptTo16(encryptString);

		conn.setRequestProperty("AppID", AppID);
		conn.setRequestProperty("TimeStamp", timeStamp);
		conn.setRequestProperty("PublicKey", publicKey);
		conn.setRequestProperty("Sign", sign);

//		System.out.println("encryptString:" + encryptString);
//		System.out.println("timeStamp:" + timeStamp);
//		System.out.println("sign:" + sign);
	}

	/**
	 * 利用HttpClient进行接口请求
	 * @param url 请求地址
	 * @param jsonParameter 请求json参数
	 * @return
	 */
	public String doPost(String url, String jsonParameter ,Long communityId) {
		String charset = "UTF-8";
		String result = null;
		try {
			SSLClient httpClient = new SSLClient();

			HttpPost httpPost = new HttpPost(url);
			setHeadr(httpPost, jsonParameter , communityId);

			// 解决中文乱码问题
			StringEntity stringEntity = new StringEntity(jsonParameter, charset);
			stringEntity.setContentEncoding(charset);

			httpPost.setEntity(stringEntity);

			//System.out.println("Executing request " + httpPost.getRequestLine());
			LOGGER.info("Executing request " + httpPost.getRequestLine());
			// Create a custom response handler
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
				@Override
				public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {//
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity) : null;
					} else {
						throw new ClientProtocolException("Unexpected response status: " + status);
					}
				}
			};
			result = httpClient.execute(httpPost, responseHandler);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 设置header信息
	 * @param httpPost
	 * @param jsonParameter
	 */
	private void setHeadr(HttpPost httpPost, String jsonParameter ,Long communityId) {
		if (jsonParameter == null) {
			jsonParameter = "";
		}
		httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
		httpPost.setHeader("Connection", "Keep-Alive");
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateStr = simpleDateFormat.format(new Date());

		String timeStamp = dateStr;
		ConfigurationProvider configProvider = PlatformContext.getComponent(ConfigurationProvider.class);

		String publickey = "mall.ruian.publickey" ;
		String privatekey = "mall.ruian.privatekey" ;
		String appid = "mall.ruian.appid" ;
		if(communityId != null && communityId != 0){
			publickey = publickey + "."+ communityId ;
			privatekey = privatekey + "."+ communityId ;
			appid = appid + "."+ communityId ;
		}
		LOGGER.info("publickey:{};privatekey:{};appid:{}",publickey,privatekey,appid);
		String pkey =configProvider.getValue(namespaceId,publickey, publicKey) ;
		publicKey =  pkey==null?publicKey : pkey;
		String prkey = configProvider.getValue(namespaceId,privatekey, privateKey);
		privateKey = prkey==null?privateKey : prkey;
		String appId = configProvider.getValue(namespaceId,appid, AppID);
		AppID =	appId==null?AppID:appId ;

		String encryptString = "{publicKey:" + publicKey + ",timeStamp:" + timeStamp + ",data:" + jsonParameter + ",privateKey:" + privateKey + "}";
		//System.out.println(encryptString);
		LOGGER.info(encryptString);
		//md5加密
		String sign = MD5.md5EncryptTo16(encryptString);
		//System.out.println(sign);
		LOGGER.info(sign);
		httpPost.setHeader("AppID", AppID);
		httpPost.setHeader("TimeStamp", timeStamp);
		httpPost.setHeader("PublicKey", publicKey);
		httpPost.setHeader("Sign", sign);

//		System.out.println("encryptString:" + encryptString);
//		System.out.println("timeStamp:" + timeStamp);
//		System.out.println("sign:" + sign);
	}
}
