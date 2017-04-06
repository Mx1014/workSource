package com.everhomes.ems;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.http.HttpUtils;
import com.everhomes.util.RuntimeErrorException;

@Component
public class EmsServiceImpl implements EmsService{
	
	//通过大客户号，业务类型获取详情单号
	private static final String GET_BILL_NO_URL = "http://os.ems.com.cn:8081/zkweb/bigaccount/getBigAccountDataAction.do?method=getBillNumBySys&xml=#{xml}";
	//大客户号
	private static final String SYS_ACCOUNT = "A1234567890Z";
	//密码
	private static final String PASS_WORD = "e10adc3949ba59abbe56e057f20f883e";
	// APP KEY
	private static final String APP_KEY = "T757620e368dA9904";
	//获取数量
	private static final String BILL_NO_AMOUNT = "1";
	
	@Override
	public String getBillNo(Byte businessType) {
		String paramXml = getBillNoParamXml(businessType);
		String paramXmlBase64 = Base64.encodeBase64String(paramXml.getBytes());
		String url = GET_BILL_NO_URL.replace("#{xml}", encodeUrl(paramXmlBase64));
		
		String resultXmlBase64 = null;
		try {
			resultXmlBase64 = HttpUtils.get(url, null);
		} catch (IOException e) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "get bill no from ems error");
		}
		
		/**
		 * 返回值格式如下：
		 * <?xmlversion="1.0" encoding="UTF-8"?>
			<response>
			<result></result>
			<errorDesc></errorDesc>
			<errorCode></errorCode>
			<assignIds>
			  <assignId>
			      <billno></billno>
			  </assignId>
			  <assignId>
			      ......
			  </assignId>
			     ..........
			<assignIds>
			</response>
		 */
		
		if (resultXmlBase64 != null) {
			String resultXml = new String(Base64.decodeBase64(resultXmlBase64));
			//从结果xml中匹配出billNo
			Pattern pattern = Pattern.compile("<billno>(.*)</billno>");
			Matcher matcher = pattern.matcher(resultXml);
			if (matcher.find()) {
				return matcher.group(1);
			}
		}
		
		return null;
	}
	
	public String updatePrintInfo() {
		return null;
	}
	
	
	private String getBillNoParamXml(Byte businessType) {
		String paramXmlTemplate = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
				"<XMLInfo>"+
				"<sysAccount>#{sysAccount}</sysAccount>"+
				"<passWord>#{passWord}</passWord>"+
				"<appKey>#{appKey}</appKey>"+
				"<businessType>#{businessType}</businessType>"+
				"<billNoAmount>#{billNoAmount}</billNoAmount>"+
				"</XMLInfo>";
		
		return paramXmlTemplate.replace("#{sysAccount}", SYS_ACCOUNT).replace("#{passWord}", PASS_WORD).replace("#{appKey}", APP_KEY)
				.replace("#{businessType}", String.valueOf(businessType)).replace("#{billNoAmount}", BILL_NO_AMOUNT);
	}
	
	private String encodeUrl(String url) {
		try {
			return URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "get bill no from ems error");
		}
	}
}
