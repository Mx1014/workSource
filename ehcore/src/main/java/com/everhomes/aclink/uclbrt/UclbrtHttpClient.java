package com.everhomes.aclink.uclbrt;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.techpark.punch.PunchConstants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.elasticsearch.search.facet.statistical.StatisticalFacet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.everhomes.aclink.DoorAccessServiceImpl;
import com.everhomes.rest.aclink.UclbrtParamsDTO;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author uclbrt
 *
 */
@Component
public class UclbrtHttpClient {

	@Autowired
	private BigCollectionProvider bigCollectionProvider;

    private static final Logger LOGGER = LoggerFactory.getLogger(UclbrtHttpClient.class);
	private static final String protocol = "https";
	private static final String ip = "api.uclbrt.com";
	private static final String port = "8058";
	private static final String accSid = "98651082ab89c3f1b50f35caf794179f";
	private static final String token = "e687bc93c89b9b59611de521a70ed4";
	private static final String areaCode = "86";
	public String getQrCode(UclbrtParamsDTO paramsDTO, String mobile, String begTime, String endTime){
		return TestFuncXML(protocol, ip, port, paramsDTO.getCommunityNo(), paramsDTO.getBuildNo(), paramsDTO.getFloorNo(), paramsDTO.getRoomNo(), paramsDTO.getSid(), paramsDTO.getToken() ,
				areaCode, mobile, begTime, endTime);
	}

	public String getQrCode(UclbrtParamsDTO paramsDTO, String mobile, Long validEndMs) {
		//默认有效期到四个月后
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, 4);
		String endTimeString = new SimpleDateFormat("yyMMddHHmm").format(validEndMs > 0 ? new Date(validEndMs) : calendar.getTime());
		return getQrCode(paramsDTO, mobile, "", endTimeString);
	}
	
	@SuppressWarnings("deprecation")
	public String TestFuncXML(String protocol,String  ip,String  port,String  communityNo,String  buildNo, String floorNo, String roomNo, String accSid,String  token,String areaCode,String mobile,String begTime,String endTime){
		//所有跟ucl的请求共用一套手机号
//		mobile = "13480251015";
		String key = getRoomIdByRedisKey(communityNo, buildNo, floorNo, roomNo, endTime);
		ValueOperations<String, String> valueOperations = getValueOperations(key);
		String roomID = valueOperations.get(key);

		String qr= null;
		qr = getQrCodeRequest(protocol, ip, port, communityNo, buildNo, floorNo, roomNo, accSid, token,areaCode,mobile,roomID);
		if (qr == null) {
			//缓存的得不到qr,就再注册一次然后获取qr
			roomID = registRoomCard(protocol, ip, port, communityNo, buildNo, floorNo, roomNo, accSid, token, areaCode, mobile, begTime, endTime);
			qr = getQrCodeRequest(protocol, ip, port, communityNo, buildNo, floorNo, roomNo, accSid, token,areaCode,mobile,roomID);
		}
		return qr;
		//如果 cardNo 参数为空，则返回该号码下所有房卡， 如果不为空 ，则返回该cardNo 对应的房卡
		//cardNo  if cardNo is null , it will return all cards ,if not ,it will return card that key is cardNo
//		RSATest.encryTest(communityNo,areaCode,mobile, accSid, token,roomID); 
	}

	/**
	 * 获取key在redis操作的valueOperations
	 */
	private ValueOperations<String, String> getValueOperations(String key) {
		final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		Accessor acc = bigCollectionProvider.getMapAccessor(key, "");
		RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

		return valueOperations;
	}

	private String getRoomIdByRedisKey(String communityNo, String buildNo, String floorNo, String roomNo, String endTime) {
		return communityNo + buildNo + floorNo + roomNo + endTime;
	}

	private String getQrCodeRequest(String protocol, String ip, String port, String communityNo, String buildNo, String floorNo, String roomNo, String accSid, String token, String areaCode, String mobile, String roomID) {
		String qr= null;
		String xmlString = null;
		Document doc = null;
		xmlString = getQRXML(protocol, ip, port, communityNo, buildNo, floorNo, roomNo, accSid, token,areaCode,mobile,roomID);
		LOGGER.info("ucl 获取验证码 xml " + xmlString);
		//如果获取到的是html页面,说明roomId失效了,返回null
		if(xmlString.contains("<html xmlns"))
			return null;
		try {
			doc = DocumentHelper.parseText(xmlString);
			Element rootElt = doc.getRootElement();
			Iterator iter = rootElt.elementIterator("baseImg");
			if(iter.hasNext()){
				Element ele = (Element) iter.next();
				qr = ele.getText();
			}
		} catch (DocumentException e) {
			LOGGER.error("error uclbrt ", e);
			return null;
		}
		return qr;
	}

	private String registRoomCard(String protocol, String ip, String port, String communityNo, String buildNo, String floorNo, String roomNo, String accSid, String token, String areaCode, String mobile, String begTime, String endTime) {
		String roomID = "";
		LOGGER.info("开始注册房卡 ");
		String l = getInfoXML(protocol, ip, port, communityNo, buildNo, floorNo, roomNo, accSid, token, areaCode, mobile, begTime, endTime);
		LOGGER.info("ucl 注册 xml " + l);
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(l);
			Element rootElt = doc.getRootElement();
			Iterator iter = rootElt.elementIterator("status");
			if (iter.hasNext()) {
				Element ele = (Element) iter.next();
				if (!"200".equals(ele.getText())) {
					return null;
				}
			}
			iter = rootElt.elementIterator("no");
			if (iter.hasNext()) {
				Element ele = (Element) iter.next();
				roomID = ele.getText();
			}
			String key = getRoomIdByRedisKey(communityNo, buildNo, floorNo, roomNo, endTime);
			ValueOperations<String, String> valueOperations = getValueOperations(key);
			int timeout = 120;
			TimeUnit unit = TimeUnit.DAYS;
			valueOperations.set(key, roomID, timeout, unit);
		} catch (DocumentException e) {
			LOGGER.error("error uclbrt ", e);
			return null;
		}
		return roomID;

	}

	public String getRoomXmlString(String roomCardID){
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version='1.0' encoding='utf-8'?>");
		sb.append("<request>");
		sb.append("<no>").append(roomCardID).append("</no>");
		sb.append("</request>");
		return sb.toString();
	}
	
	public String getXmlString(String protocol, String ip, String port,
			String communityNo, String buildNo, String floorNo, String roomNo,
			String acc, String token ,String areaCode,String mobile,String begTime ,String endTime){
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version='1.0' encoding='utf-8'?>");
		sb.append("<request>");
		if(areaCode != null){
			sb.append("<areaCode>").append(areaCode).append("</areaCode>");
		}
		sb.append("<mobile>").append(mobile).append("</mobile>");
		sb.append("<communityNo>").append(communityNo).append("</communityNo>");
		if(buildNo != null){
			sb.append("<buildNo>").append(buildNo).append("</buildNo>");
		}
		if(floorNo != null){
			sb.append("<floorNo>").append(floorNo).append("</floorNo>");
		}
		sb.append("<roomNo>").append(roomNo).append("</roomNo>");
		sb.append("<startTime>").append(begTime).append("</startTime>");
		sb.append("<endTime>").append(endTime).append("</endTime>");
		sb.append("</request>");
		return sb.toString();
	}

	public String qrXmlString(String protocol, String ip, String port,
			String communityNo, String buildNo, String floorNo, String roomNo,
			String acc, String token ,String areaCode,String mobile, String roomID){
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version='1.0' encoding='utf-8'?>");
		sb.append("<request>");
		if(areaCode != null){
			sb.append("<areaCode>").append(areaCode).append("</areaCode>");
		}
		sb.append("<mobile>").append(mobile).append("</mobile>");
		sb.append("<communityNo>").append(communityNo).append("</communityNo>");
//		if(buildNo != null){
//			sb.append("<buildNo>").append(buildNo).append("</buildNo>");
//		}
//		if(floorNo != null){
//			sb.append("<floorNo>").append(floorNo).append("</floorNo>");
//		}
		sb.append("<no>").append(roomID).append("</no>");
		sb.append("<time>").append(Long.valueOf(new Date().getTime()/1000)).append("</time>");
//		sb.append("<endTime>").append(endTime).append("</endTime>");
		sb.append("</request>");
		return sb.toString();
	}

	
	public CloseableHttpClient createHttpsClient() throws Exception {
		X509TrustManager x509mgr = new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] xcs, String string) {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] xcs, String string) {
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(null, new TrustManager[] { x509mgr },
				new java.security.SecureRandom());
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				sslContext,
				SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		return HttpClients.custom().setSSLSocketFactory(sslsf).build();
	}

	public static String dateFormat() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date curDate = new Date(System.currentTimeMillis());
		return formatter.format(curDate);
	}


	/**
	 * @param protocol   	https
	 * @param ip 			address
	 * @param port			port
	 * @param communityNo	community number
	 * @param buildNo		build number
	 * @param floorNo		floor number
	 * @param roomNo		room number
	 * @param acc			account sid
	 * @param token			token
	 * @param areaCode		areaCode if null, the default is 86(china).
	 * @param mobile		mobile number
	 * @param begTime		begin time
	 * @param endTime		end time
	 * @return				room number
	 */
	public String getInfoXML(String protocol, String ip, String port,
			String communityNo, String buildNo, String floorNo, String roomNo,
			String acc, String token ,String areaCode ,String mobile,String begTime ,String endTime) {
		String result = "";
		EncryptUtil eu = new EncryptUtil();
		String timeT = dateFormat();
		String sig = acc + token + timeT;
		String signature;
		try {
		signature = eu.md5Digest(sig);
		String url = protocol + "://" + ip + ":" + port
				+ "?c=Qrcode&a=getLink&sig=" + signature.toUpperCase();
		CloseableHttpClient closeableHttpClient = createHttpsClient();
		// 建立HttpPost对象
		HttpPost httppost = new HttpPost(url);
		
		httppost.setHeader("Accept", "application/xml");
		httppost.setHeader("Content-Type",
				"application/x-www-form-urlencode;charset=utf-8");
		
		String src = acc + ":" + timeT;
		String auth = eu.base64Encoder(src);
		httppost.setHeader("Authorization", auth);
		//TODO  BEGIN
		
		String reqXml = getXmlString( protocol,  ip,  port,
				 communityNo,  buildNo,  floorNo,  roomNo,
				 acc,  token , areaCode,mobile, begTime , endTime);

		System.out.println("url = "+ url);
		System.out.println("xml is :"+reqXml);
		BasicHttpEntity bhe = new BasicHttpEntity();
		ByteArrayInputStream bis = new ByteArrayInputStream(reqXml.getBytes("UTF-8"));
		bhe.setContent(new ByteArrayInputStream(reqXml
				.getBytes("UTF-8")));
		bhe.setContentLength(reqXml.getBytes("UTF-8").length);
		httppost.setEntity(bhe);
		
		
		 //TODO END		 
		long culTime = System.currentTimeMillis();
		org.apache.http.Header h[] =httppost.getAllHeaders();
		HttpResponse httpResponse = closeableHttpClient.execute(httppost);
		HttpEntity httpEntity1 = httpResponse.getEntity();
		result = EntityUtils.toString(httpEntity1);
		// 关闭连接
		closeableHttpClient.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @param protocol   	https
	 * @param ip 			address
	 * @param port			port
	 * @param communityNo	community number
	 * @param buildNo		build number
	 * @param floorNo		floor number
	 * @param roomNo		room number
	 * @param acc			account sid
	 * @param token			token
	 * @param areaCode		areaCode if null, the default is 86(china).
	 * @param mobile		mobile number 
	 * @return				room number
	 */
	public String getQRXML(String protocol, String ip, String port,
			String communityNo, String buildNo, String floorNo, String roomNo,
			String acc, String token ,String areaCode ,String mobile,String roomID) {
		String result = "";
		EncryptUtil eu = new EncryptUtil();
		String timeT = dateFormat();
		String sig = acc + token + timeT;
		String signature;
		try {
		signature = eu.md5Digest(sig);
		String url = protocol + "://" + ip + ":" + port
				+ "?c=Qrcode&a=getCard&sig=" + signature.toUpperCase();
		CloseableHttpClient closeableHttpClient = createHttpsClient();
		// 建立HttpPost对象
		HttpPost httppost = new HttpPost(url);
		
		httppost.setHeader("Accept", "application/xml");
		httppost.setHeader("Content-Type",
				"application/x-www-form-urlencode;charset=utf-8");
		
		String src = acc + ":" + timeT;
		String auth = eu.base64Encoder(src);
		httppost.setHeader("Authorization", auth);
		//TODO  BEGIN
		
		String reqXml = qrXmlString( protocol,  ip,  port,
				 communityNo,  buildNo,  floorNo,  roomNo,
				 acc,  token , areaCode,mobile,roomID);

		System.out.println("url = "+ url);
		System.out.println("xml is :"+reqXml);
		BasicHttpEntity bhe = new BasicHttpEntity();
		ByteArrayInputStream bis = new ByteArrayInputStream(reqXml.getBytes("UTF-8"));
		bhe.setContent(new ByteArrayInputStream(reqXml
				.getBytes("UTF-8")));
		bhe.setContentLength(reqXml.getBytes("UTF-8").length);
		httppost.setEntity(bhe);
		
		
		 //TODO END		 
		long culTime = System.currentTimeMillis();
		org.apache.http.Header h[] =httppost.getAllHeaders();
		HttpResponse httpResponse = closeableHttpClient.execute(httppost);
		HttpEntity httpEntity1 = httpResponse.getEntity();
		result = EntityUtils.toString(httpEntity1);
		// 关闭连接
		closeableHttpClient.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	public String cancelInfoXML(String protocol, String ip, String port,
			String communityNo, String buildNo, String floorNo, String roomNo,
			String acc, String token ,String areaCode,String mobile,String begTime ,String endTime) {
		String result = "";
		EncryptUtil eu = new EncryptUtil();
		String timeT = dateFormat();
		String sig = acc + token + timeT;
		String signature;
		try {
		signature = eu.md5Digest(sig);
		String url = protocol + "://" + ip + ":" + port
				+ "?c=Qrcode&a=cancelCardByRoom&sig=" + signature.toUpperCase();
		CloseableHttpClient closeableHttpClient = createHttpsClient();
		// 建立HttpPost对象
		HttpPost httppost = new HttpPost(url);
		
		httppost.setHeader("Accept", "application/xml");
		httppost.setHeader("Content-Type",
				"application/x-www-form-urlencode;charset=utf-8");
		
		String src = acc + ":" + timeT;
		String auth = eu.base64Encoder(src);
		httppost.setHeader("Authorization", auth);
		//TODO  BEGIN
		
		String reqXml = getXmlString( protocol,  ip,  port,
				 communityNo,  buildNo,  floorNo,  roomNo,
				 acc,  token , areaCode,mobile, begTime , endTime);
		
		BasicHttpEntity bhe = new BasicHttpEntity();
		ByteArrayInputStream bis = new ByteArrayInputStream(reqXml.getBytes("UTF-8"));
		bhe.setContent(new ByteArrayInputStream(reqXml
				.getBytes("UTF-8")));
		bhe.setContentLength(reqXml.getBytes("UTF-8").length);
		httppost.setEntity(bhe);
		
		
		 //TODO END		 
		long culTime = System.currentTimeMillis();
		org.apache.http.Header h[] =httppost.getAllHeaders();
		HttpResponse httpResponse = closeableHttpClient.execute(httppost);
		HttpEntity httpEntity1 = httpResponse.getEntity();
		result = EntityUtils.toString(httpEntity1);
		// 关闭连接
		closeableHttpClient.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	public String cancelInfoXML(String protocol, String ip, String port,
			String roomCardID,String acc, String token ) {
		String result = "";
		EncryptUtil eu = new EncryptUtil();
		String timeT = dateFormat();
		String sig = acc + token + timeT;
		String signature;
		try {
		signature = eu.md5Digest(sig);
		String url = protocol + "://" + ip + ":" + port
				+ "?c=Qrcode&a=cancelCard&sig=" + signature.toUpperCase();
		System.out.println("url = "+ url);
		CloseableHttpClient closeableHttpClient = createHttpsClient();
		// 建立HttpPost对象
		HttpPost httppost = new HttpPost(url);
		
		httppost.setHeader("Accept", "application/xml");
		httppost.setHeader("Content-Type",
				"application/x-www-form-urlencode;charset=utf-8");
		
		String src = acc + ":" + timeT;
		String auth = eu.base64Encoder(src);
		httppost.setHeader("Authorization", auth);
		//TODO  BEGIN
		
		String reqXml = getRoomXmlString(roomCardID);
		
		BasicHttpEntity bhe = new BasicHttpEntity();
		ByteArrayInputStream bis = new ByteArrayInputStream(reqXml.getBytes("UTF-8"));
		bhe.setContent(new ByteArrayInputStream(reqXml
				.getBytes("UTF-8")));
		bhe.setContentLength(reqXml.getBytes("UTF-8").length);
		httppost.setEntity(bhe);
		
		
		 //TODO END		 
		long culTime = System.currentTimeMillis();
		org.apache.http.Header h[] =httppost.getAllHeaders();
		HttpResponse httpResponse = closeableHttpClient.execute(httppost);
		HttpEntity httpEntity1 = httpResponse.getEntity();
		result = EntityUtils.toString(httpEntity1);
		// 关闭连接
		closeableHttpClient.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
    

}