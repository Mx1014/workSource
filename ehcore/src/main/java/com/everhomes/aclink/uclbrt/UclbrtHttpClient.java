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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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

/**
 * @author uclbrt
 *
 */
public class UclbrtHttpClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(UclbrtHttpClient.class);
	private static final String protocol = "https";
	private static final String ip = "api.uclbrt.com";
	private static final String port = "8058";
	private static final String accSid = "98651082ab89c3f1b50f35caf794179f";
	private static final String token = "e687bc93c89b9b59611de521a70ed4";
	private static final String areaCode = "86";
//	/**
//	 * main function
//	 * @param args
//	 * @throws Exception
//	 */
//	public static void main(String[] args) throws Exception {
//		
//		/**
//		 * 参数格式请参见
//		 *  http://qrm.uclbrt.com/openQrcodeLinkv2.0.html#openCenterMenuAnchor
//		 * 中参数说明
//		 * **/
//		
//		//如下参数格式要求，请参看文档说明，具体数据，参看文档查询。
////		String communityNo = "1316881230";
//		String buildNo =  "001";
//		String floorNo = "001";
////		String roomNo = "001";
////		String accSid = "b229fe4bb0a00ca4d05335021c685cf3";
////		String token = "178630af744e94520643b80555819c";
//		//试用账号
//
//		String communityNo = "1316879946";
//
//		String roomNo = "101";
//		
//		String mobile = "13480251015";
//		String begTime ="";
//		String endTime ="";	
//		String areaCode = "86";
//		
//		TestFuncXML(protocol, ip, port, communityNo, buildNo, floorNo, roomNo, accSid, token,areaCode,mobile,begTime,endTime);
//	}
	public static String getQrCode(UclbrtParamsDTO paramsDTO, String mobile, String begTime, String endTime){
		return TestFuncXML(protocol, ip, port, paramsDTO.getCommunityNo(), paramsDTO.getBuildNo(), paramsDTO.getFloorNo(), paramsDTO.getRoomNo(), paramsDTO.getSid(), paramsDTO.getToken() ,
				areaCode, mobile, begTime, endTime);
	}
	public static String getQrCode(UclbrtParamsDTO paramsDTO, String mobile){
		return getQrCode(paramsDTO, mobile, "", "");
	}
	
	@SuppressWarnings("deprecation")
	public static String TestFuncXML(String protocol,String  ip,String  port,String  communityNo,String  buildNo, String floorNo, String roomNo, String accSid,String  token,String areaCode,String mobile,String begTime,String endTime){
		LOGGER.info("ucl 开始注册房卡 " );
		String l= getInfoXML(protocol, ip, port, communityNo, buildNo, floorNo, roomNo, accSid, token,areaCode,mobile,begTime,endTime);
		LOGGER.info("ucl 注册 xml " + l);
		String roomID = null;
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(l);
			Element rootElt = doc.getRootElement(); 
			Iterator iter = rootElt.elementIterator("status"); 
            if(iter.hasNext()){
            	   Element ele = (Element) iter.next();
                   if(!"200".equals(ele.getText())){
                	   return null;
                   }
            }
            iter = rootElt.elementIterator("no"); 
            if(iter.hasNext()){
            	   Element ele = (Element) iter.next();
                   roomID = ele.getText();
            }
		} catch (DocumentException e) {
			LOGGER.error("error uclbrt ", e);
			return null;
		}
		
		String qr= getQRXML(protocol, ip, port, communityNo, buildNo, floorNo, roomNo, accSid, token,areaCode,mobile,roomID);
		LOGGER.info("ucl 获取验证码 xml " + qr);
		try {
			doc = DocumentHelper.parseText(qr);
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
		//如果 cardNo 参数为空，则返回该号码下所有房卡， 如果不为空 ，则返回该cardNo 对应的房卡
		//cardNo  if cardNo is null , it will return all cards ,if not ,it will return card that key is cardNo
//		RSATest.encryTest(communityNo,areaCode,mobile, accSid, token,roomID); 
	}
//	public static void TestFuncXMLBySamePara(String protocol,String  ip,String  port,String  communityNo,String  buildNo, String floorNo, String roomNo, String accSid,String  token,String areaCode,String mobile,String begTime,String endTime){
//		System.out.println("******************generate card by xml parameters*****************");
//		String l= getInfoXML(protocol, ip, port, communityNo, buildNo, floorNo, roomNo, accSid, token,areaCode,mobile,begTime,endTime);
//		System.out.println(l);
//		
//		System.out.println("******************get card******************");
//		//如果 cardNo 参数为空，则返回该号码下所有房卡， 如果不为空 ，则返回该cardNo 对应的房卡
//		//cardNo  if cardNo is null , it will return all cards ,if not ,it will return card that key is cardNo
//		RSATest.encryTest(communityNo,areaCode,mobile, accSid, token,null);
//		//cancel by parameters same as generate func.
//		System.out.println("******************cancel by get parameters******************");
//		String l6 = cancelInfoXML(protocol, ip, port, communityNo, buildNo, buildNo, roomNo, accSid, token,areaCode,mobile,begTime,endTime);
//		System.out.println(l6);
//		
//	}
//	
//	public static void TestFuncJSON(String protocol,String  ip,String  port,String  communityNo,String  buildNo, String floorNo, String roomNo, String accSid,String  token,String areaCode,String mobile,String begTime,String endTime){
//		System.out.println("***********generate by json parameters*************************");
//		String joStr= getInfoJSON(protocol, ip, port, communityNo, buildNo, floorNo, roomNo, accSid, token,areaCode,mobile,begTime,endTime);
//		System.out.println(joStr);
//		Map<String , String> jsonInfoMap = new HashMap<String, String>();
//		if(null != joStr){
//			joStr = joStr.replace("{", "");
//			joStr = joStr.replace("}", "");
//			String[] jsonInfo = joStr.split(",");
//			for(String ji : jsonInfo){
//				String[] attriInfo = ji.split(":");
//				jsonInfoMap.put(attriInfo[0].replace("\"", ""), attriInfo[1].replace("\"", ""));
//			}
//		}
//		String roomID = jsonInfoMap.get("no");
//		
//		System.out.println("******************get card******************");
//		//如果 cardNo 参数为空，则返回该号码下所有房卡， 如果不为空 ，则返回该cardNo 对应的房卡
//		//cardNo  if cardNo is null , it will return all cards ,if not ,it will return card that key is cardNo
//		RSATest.encryTest(communityNo,areaCode,mobile, accSid, token,roomID);
//		
//		System.out.println("******************cancel by  roomID******************");
//		String l4 = cancelInfoJSON( protocol, ip, port, roomID, accSid, token);
//		System.out.println(l4);
//	}
//	
//	public static void TestFuncJSONBysamePara(String protocol,String  ip,String  port,String  communityNo,String  buildNo, String floorNo, String roomNo, String accSid,String  token,String areaCode,String mobile,String begTime,String endTime){
//		System.out.println("***********generate by json parameters*************************");
//		String joStr= getInfoJSON(protocol, ip, port, communityNo, buildNo, floorNo, roomNo, accSid, token,areaCode,mobile,begTime,endTime);
//		System.out.println(joStr);
//		
//		System.out.println("******************get card******************");
//		//如果 cardNo 参数为空，则返回该号码下所有房卡， 如果不为空 ，则返回该cardNo 对应的房卡
//		//cardNo  if cardNo is null , it will return all cards ,if not ,it will return card that key is cardNo
//		RSATest.encryTest(communityNo,areaCode,mobile, accSid, token,null);
//		
//		System.out.println("******************cancel by get parameters******************");
//		String l5= cancelInfoJSON(protocol, ip, port, communityNo, buildNo, floorNo, roomNo, accSid, token,areaCode,mobile,begTime,endTime);
//		System.out.println(l5);	
//	}
//	
//	/**
//	 * @param protocol
//	 * @param ip
//	 * @param port
//	 * @param communityNo
//	 * @param buildNo
//	 * @param floorNo
//	 * @param roomNo
//	 * @param acc
//	 * @param token
//	 * @return
//	 */
//	public static String getFunc(String protocol, String ip, String port,
//			String communityNo, String buildNo, String floorNo, String roomNo,String startTime ,String endTime,
//			String acc, String token) {
//		String result = "";
//		EncryptUtil eu = new EncryptUtil();
//		String timeT = dateFormat();
//		String sig = acc + token + timeT;
//		String signature;
//		try {
//		signature = eu.md5Digest(sig);
//		String url = protocol + "://" + ip + ":" + port
//				+ "?c=Qrcode&a=get&communityNo=" + communityNo + "&buildNo="
//				+ buildNo + "&floorNo=" + floorNo + "&roomNo=" + roomNo
//				+ "&startTime="+startTime + "&endTime="+endTime
//				+ "&sig=" + signature.toUpperCase();
//		
//
//		System.out.println("url = "+url);
//		CloseableHttpClient closeableHttpClient = createHttpsClient();
//		// 建立HttpPost对象
//		HttpPost httppost = new HttpPost(url);
//		httppost.setHeader("Accept", "application/json");
//		httppost.setHeader("Content-Type",
//				"application/json;charset=utf-8");
//
//		String src = acc + ":" + timeT;
//		String auth = eu.base64Encoder(src);
//		httppost.setHeader("Authorization", auth);
//		
//		//配置post 数据。
//		MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder
//				.create();
//		multipartEntityBuilder.setCharset(Charset.forName(HTTP.UTF_8));
//		// 生成 HTTP POST 实体
//		HttpEntity httpEntity = multipartEntityBuilder.build();
//		httppost.setEntity(httpEntity);
//		// 发送Post,并返回一个HttpResponse对象
//		long culTime = System.currentTimeMillis();
//		org.apache.http.Header h[] =httppost.getAllHeaders();
//		HttpResponse httpResponse = closeableHttpClient.execute(httppost);
//		HttpEntity httpEntity1 = httpResponse.getEntity();
//		result = EntityUtils.toString(httpEntity1);
//		// 关闭连接
//		closeableHttpClient.close();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return result;
//	}
//
//	/**
//	 * @param joStr
//	 * @param imgFilePath
//	 */
//	public static void genPic(String joStr , String imgFilePath){
//		if(null != joStr){
//		joStr = joStr.replace("{", "");
//		joStr = joStr.replace("}", "");
//		String[] jsonInfo = joStr.split(",");
//		Map<String , String> jsonInfoMap = new HashMap<String, String>();
//		for(String ji : jsonInfo){
//			String[] attriInfo = ji.split(":");
//			jsonInfoMap.put(attriInfo[0].replace("\"", ""), attriInfo[1].replace("\"", ""));
//		}
//		System.out.println(jsonInfoMap.get("status"));
//		if(jsonInfoMap.get("status").equals("200")){
//			//保存图片文件
//			String picString = jsonInfoMap.get("info");
//			try {
//				sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
//				picString = picString.replace("\\", "");
//				// Base64解码
//				byte[] b = decoder.decodeBuffer(picString);
//				for (int i = 0; i < b.length; ++i) {
//				if (b[i] < 0) {// 调整异常数据
//				b[i] += 256;
//				}
//				}
//				// 生成jpeg图片
//				FileOutputStream out = new FileOutputStream(new File(imgFilePath));
//				out.write(b);
//				out.flush();
//				out.close();
//				
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//	}
	
	public static String getRoomXmlString(String roomCardID){
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version='1.0' encoding='utf-8'?>");
		sb.append("<request>");
		sb.append("<no>").append(roomCardID).append("</no>");
		sb.append("</request>");
		return sb.toString();
	}
	
	public static String getXmlString(String protocol, String ip, String port,
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

	public static String qrXmlString(String protocol, String ip, String port,
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
//	public static String getRoomJSONString(String roomCardID){
//		StringBuilder sb = new StringBuilder();
//		sb.append("{");
//		sb.append("\"no\":\"").append(roomCardID).append("\"");
//		sb.append("}");
//		return sb.toString();
//	}
//	public static String getJSONString(String protocol, String ip, String port,
//			String communityNo, String buildNo, String floorNo, String roomNo,
//			String acc, String token ,String areaCode,String mobile,String begTime ,String endTime){
//		StringBuilder sb = new StringBuilder();
//		sb.append("{");
//		if(areaCode != null){
//			sb.append("\"areaCode\":\"").append(areaCode).append("\",");
//		}
//		sb.append("\"mobile\":\"").append(mobile).append("\",");
//		sb.append("\"communityNo\":\"").append(communityNo).append("\",");
//		if(buildNo != null){
//			sb.append("\"buildNo\":\"").append(buildNo).append("\",");
//		}
//		if(floorNo != null){
//			sb.append("\"floorNo\":\"").append(floorNo).append("\",");
//		}
//		sb.append("\"roomNo\":\"").append(roomNo).append("\",");
//		sb.append("\"startTime\":\"").append(begTime).append("\",");
//		sb.append("\"endTime\":\"").append(endTime).append("\"");
//		sb.append("}");
//		return sb.toString();
//	}
	
	
	public static CloseableHttpClient createHttpsClient() throws Exception {
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

//	public static HashMap<String, Object> xmlToMap(String xml) {
//		HashMap<String, Object> map = new HashMap<String, Object>();
//		Document doc = null;
//		try {
//			doc = DocumentHelper.parseText(xml); // 将字符串转为XML
//			Element rootElt = doc.getRootElement(); // 获取根节点
//			HashMap<String, Object> hashMap2 = new HashMap<String, Object>();
//			ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
//			for (Iterator i = rootElt.elementIterator(); i.hasNext();) {
//				Element e = (Element) i.next();
//				if ("statusCode".equals(e.getName())
//						|| "statusMsg".equals(e.getName()))
//					map.put(e.getName(), e.getText());
//				else {
//					{
//						HashMap<String, Object> hashMap3 = new HashMap<String, Object>();
//						for (Iterator i2 = e.elementIterator(); i2.hasNext();) {
//							Element e2 = (Element) i2.next();
//							hashMap3.put(e2.getName(), e2.getText());
//						}
//						if (hashMap3.size() != 0) {
//							hashMap2.put(e.getName(), hashMap3);
//						} else {
//							hashMap2.put(e.getName(), e.getText());
//						}
//						map.put("data", hashMap2);
//					}
//				}
//			}
//		} catch (DocumentException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return map;
//	}
	
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
	public static String getInfoXML(String protocol, String ip, String port,
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
	public static String getQRXML(String protocol, String ip, String port,
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
	public static String cancelInfoXML(String protocol, String ip, String port,
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
	
	public static String cancelInfoXML(String protocol, String ip, String port,
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
    
//	public static String getInfoJSON(String protocol, String ip, String port,
//			String communityNo, String buildNo, String floorNo, String roomNo,
//			String acc, String token,String areaCode,String mobile,String begTime ,String endTime) {
//		String result = "";
//		EncryptUtil eu = new EncryptUtil();
//		String timeT = dateFormat();
//		String sig = acc + token + timeT;
//		String signature;
//		try {
//		signature = eu.md5Digest(sig);
//		String url = protocol + "://" + ip + ":" + port
//				+ "?c=Qrcode&a=getLink&sig=" + signature.toUpperCase();
//		CloseableHttpClient closeableHttpClient = createHttpsClient();
//		// 建立HttpPost对象
//		HttpPost httppost = new HttpPost(url);
//		
//		
//		httppost.setHeader("Accept", "application/json");
//		httppost.setHeader("Content-Type",
//				"application/x-www-form-urlencode;charset=utf-8");
//
//		
//		String src = acc + ":" + timeT;
//		String auth = eu.base64Encoder(src);
//		httppost.setHeader("Authorization", auth);
//		String reqXml1 = getJSONString( protocol,  ip,  port,
//				 communityNo,  buildNo,  floorNo,  roomNo,
//				 acc,  token , areaCode ,mobile, begTime , endTime);
//		
//		BasicHttpEntity bhe = new BasicHttpEntity();
//		bhe.setContent(new ByteArrayInputStream(reqXml1
//				.getBytes("UTF-8")));
//		bhe.setContentLength(reqXml1.getBytes("UTF-8").length);
//		httppost.setEntity(bhe);
//		long culTime = System.currentTimeMillis();
//		org.apache.http.Header h[] =httppost.getAllHeaders();
//		HttpResponse httpResponse = closeableHttpClient.execute(httppost);
//		
////		System.out.println("获取数据消耗时间（毫秒）： "+(System.currentTimeMillis()-culTime));
//		HttpEntity httpEntity1 = httpResponse.getEntity();
//		result = EntityUtils.toString(httpEntity1);
//		// 关闭连接
//		closeableHttpClient.close();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return result;
//	}
//	
//	public static String cancelInfoJSON(String protocol, String ip, String port,
//			String communityNo, String buildNo, String floorNo, String roomNo,
//			String acc, String token,String areaCode ,String mobile,String begTime ,String endTime) {
//		String result = "";
//		EncryptUtil eu = new EncryptUtil();
//		String timeT = dateFormat();
//		String sig = acc + token + timeT;
//		String signature;
//		try {
//		signature = eu.md5Digest(sig);
//		String url = protocol + "://" + ip + ":" + port
//				+ "?c=Qrcode&a=cancelCardByRoom&sig=" + signature.toUpperCase();
//		CloseableHttpClient closeableHttpClient = createHttpsClient();
//		// 建立HttpPost对象
//		HttpPost httppost = new HttpPost(url);
//		
//		
//		httppost.setHeader("Accept", "application/json");
//		httppost.setHeader("Content-Type",
//				"application/x-www-form-urlencode;charset=utf-8");
//
//		
//		String src = acc + ":" + timeT;
//		String auth = eu.base64Encoder(src);
//		httppost.setHeader("Authorization", auth);
//		String reqXml1 = getJSONString( protocol,  ip,  port,
//				 communityNo,  buildNo,  floorNo,  roomNo,
//				 acc,  token , areaCode ,mobile, begTime , endTime);
//		
//		BasicHttpEntity bhe = new BasicHttpEntity();
//		bhe.setContent(new ByteArrayInputStream(reqXml1
//				.getBytes("UTF-8")));
//		bhe.setContentLength(reqXml1.getBytes("UTF-8").length);
//		httppost.setEntity(bhe);
//		long culTime = System.currentTimeMillis();
//		org.apache.http.Header h[] =httppost.getAllHeaders();
//		HttpResponse httpResponse = closeableHttpClient.execute(httppost);
//		
////		System.out.println("获取数据消耗时间（毫秒）： "+(System.currentTimeMillis()-culTime));
//		HttpEntity httpEntity1 = httpResponse.getEntity();
//		result = EntityUtils.toString(httpEntity1);
//		// 关闭连接
//		closeableHttpClient.close();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return result;
//	}
//	
//	public static String cancelInfoJSON(String protocol, String ip, String port,
//			String roomCardID,String acc, String token) {
//		String result = "";
//		EncryptUtil eu = new EncryptUtil();
//		String timeT = dateFormat();
//		String sig = acc + token + timeT;
//		String signature;
//		try {
//		signature = eu.md5Digest(sig);
//		String url = protocol + "://" + ip + ":" + port
//				+ "?c=Qrcode&a=cancelCard&sig=" + signature.toUpperCase();
//		CloseableHttpClient closeableHttpClient = createHttpsClient();
//		// 建立HttpPost对象
//		HttpPost httppost = new HttpPost(url);
//		
//		
//		httppost.setHeader("Accept", "application/json");
//		httppost.setHeader("Content-Type",
//				"application/x-www-form-urlencode;charset=utf-8");
//
//		
//		String src = acc + ":" + timeT;
//		String auth = eu.base64Encoder(src);
//		httppost.setHeader("Authorization", auth);
//		String reqXml1 = getRoomJSONString(roomCardID);
//		
//		BasicHttpEntity bhe = new BasicHttpEntity();
//		bhe.setContent(new ByteArrayInputStream(reqXml1
//				.getBytes("UTF-8")));
//		bhe.setContentLength(reqXml1.getBytes("UTF-8").length);
//		httppost.setEntity(bhe);
//		long culTime = System.currentTimeMillis();
//		org.apache.http.Header h[] =httppost.getAllHeaders();
//		HttpResponse httpResponse = closeableHttpClient.execute(httppost);
//		
////		System.out.println("获取数据消耗时间（毫秒）： "+(System.currentTimeMillis()-culTime));
//		HttpEntity httpEntity1 = httpResponse.getEntity();
//		result = EntityUtils.toString(httpEntity1);
//		// 关闭连接
//		closeableHttpClient.close();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return result;
//	}
    
}