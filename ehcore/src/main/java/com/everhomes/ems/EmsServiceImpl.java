package com.everhomes.ems;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.http.HttpUtils;
import com.everhomes.rest.ems.EmsBill;
import com.everhomes.rest.ems.TrackBillResponse;
import com.everhomes.util.RuntimeErrorException;

@Component
public class EmsServiceImpl implements EmsService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EmsServiceImpl.class);
	
	//通过大客户号，业务类型获取详情单号
	private static final String GET_BILL_NO_URL = "http://os.ems.com.cn:8081/zkweb/bigaccount/getBigAccountDataAction.do?method=getBillNumBySys&xml=#{xml}";
	//更新信息到EMS系统
	private static final String UPDATE_PRINT_INFO_URL = "http://os.ems.com.cn:8081/zkweb/bigaccount/getBigAccountDataAction.do?method=updatePrintDatas&xml=#{xml}"; 
	//跟踪运单信息
	private static final String TRACK_BILL_URL = "http://211.156.193.140:8000/cotrackapi/api/track/mail/#{billno}"; 
	//大客户号
	private static final String SYS_ACCOUNT = "A1234567890Z";
	//密码
	private static final String PASS_WORD = "e10adc3949ba59abbe56e057f20f883e";
	// APP KEY
	private static final String APP_KEY = "T757620e368dA9904";
	//获取数量
	private static final String BILL_NO_AMOUNT = "1";
	//打印类型，1:预制详情单	2:热敏标签式详情单，我们都是2
	private static final String PRINT_KIND = "2";
	
	/**
	 * @param businessType 1:标准快递 8:代收到付 9:快递包裹，现在都是1
	 */
	@Override
	public String getBillNo(String businessType) {
		String paramXml = getBillNoParamXml(businessType);
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("get bill no param is : {}", paramXml);
		}
		String paramXmlBase64 = Base64.encodeBase64String(paramXml.getBytes());
		String url = GET_BILL_NO_URL.replace("#{xml}", encodeUrl(paramXmlBase64));
		
		String resultXmlBase64 = null;
		try {
			resultXmlBase64 = HttpUtils.get(url, null);
		} catch (IOException e) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "get bill no from ems error");
		}
		
		if (resultXmlBase64 != null) {
			String resultXml = new String(Base64.decodeBase64(resultXmlBase64));
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("get bill no result is : {}", resultXml);
			}
			//从结果xml中匹配出billNo
			Pattern pattern = Pattern.compile("<billno>(.*)</billno>");
			Matcher matcher = pattern.matcher(resultXml);
			if (matcher.find()) {
				return matcher.group(1);
			}
		}
		
		//如果程序运行到这里，说明未取得邮件号，招聘异常
		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "get bill no from ems error");
	}
	
	@Override
	public void updatePrintInfo() {
		EmsBill emsBill = new EmsBill();
		String paramXml = getUpdatePrintInfoParamXml(emsBill);
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("update print info param is : {}", paramXml);
		}
		String paramXmlBase64 = Base64.encodeBase64String(paramXml.getBytes());
		String url = UPDATE_PRINT_INFO_URL.replace("#{xml}", encodeUrl(paramXmlBase64));
		
		String resultXmlBase64 = null;
		try {
			resultXmlBase64 = HttpUtils.get(url, null);
		} catch (IOException e) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "update print info to ems error");
		}
		
		if (resultXmlBase64 != null) {
			String resultXml = new String(Base64.decodeBase64(resultXmlBase64));
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("update print info result is : {}", resultXml);
			}
			//从结果xml中匹配出billNo
			Pattern pattern = Pattern.compile("<result>(.*)</result>");
			Matcher matcher = pattern.matcher(resultXml);
			if (matcher.find()) {
				String result = matcher.group(1);
				if (result == null || !result.equals("1")) {
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "update print info to ems error");
				}
			}
		}
	}
	
	@Override
	public TrackBillResponse trackBill(String billno) {
		String url = TRACK_BILL_URL.replace("#{billno}", billno);
		String result = null;
		try {
			result = HttpUtils.get(url, null);
		} catch (IOException e) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "track bill error");
		}
		if (result == null || result.isEmpty()) {
			result = "{}";
		}
		TrackBillResponse response = JSON.parseObject(result, TrackBillResponse.class);
		return response;
	}
	
	private String encodeUrl(String url) {
		try {
			return URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "get bill no from ems error");
		}
	}

	private String getBillNoParamXml(String businessType) {
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
	
	private String getUpdatePrintInfoParamXml(EmsBill emsBill) {
		String paramXmlTemplate = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><XMLInfo>"+
				"<sysAccount>#{sysAccount}</sysAccount>"+
				"<passWord>#{passWord}</passWord>"+
				"<printKind>#{printKind}</printKind>"+
				"<appKey>#{appKey}</appKey>"+
				"<printDatas>"+
				"<printData>"+
				"<bigAccountDataId>#{bigAccountDataId}</bigAccountDataId>"+
				"<businessType>#{businessType}</businessType>"+
				"<billno>#{billno}</billno>"+
				"<dateType>#{dateType}</dateType>"+
				"<procdate>#{procdate}</procdate>"+
				"<scontactor>#{scontactor}</scontactor>"+
				"<scustMobile>#{scustMobile}</scustMobile>"+
				"<scustTelplus>#{scustTelplus}</scustTelplus>"+
				"<scustPost>#{scustPost}</scustPost>"+
				"<scustAddr>#{scustAddr}</scustAddr>"+
				"<scustComp>#{scustComp}</scustComp>"+
				"<scustProvince>#{scustProvince}</scustProvince>"+
				"<scustCity>#{scustCity}</scustCity>"+
				"<scustCounty>#{scustCounty}</scustCounty>"+
				"<tcontactor>#{tcontactor}</tcontactor>"+
				"<tcustMobile>#{tcustMobile}</tcustMobile>"+
				"<tcustTelplus>#{tcustTelplus}</tcustTelplus>"+
				"<tcustPost>#{tcustPost}</tcustPost>"+
				"<tcustAddr>#{tcustAddr}</tcustAddr>"+
				"<tcustComp>#{tcustComp}</tcustComp>"+
				"<tcustProvince>#{tcustProvince}</tcustProvince>"+
				"<tcustCity>#{tcustCity}</tcustCity>"+
				"<tcustCounty>#{tcustCounty}</tcustCounty>"+
				"<weight>#{weight}</weight>"+
				"<length>#{length}</length>"+
				"<insure>#{insure}</insure>"+
				"<insurance>#{insurance}</insurance>"+
				"<fee>#{fee}</fee>"+
				"<feeUppercase>#{feeUppercase}</feeUppercase>"+
				"<cargoDesc>#{cargoDesc}</cargoDesc>"+
				"<cargoType>#{cargoType}</cargoType>"+
				"<deliveryclaim>#{deliveryclaim}</deliveryclaim>"+
				"<remark>#{remark}</remark>"+
				"<productCode>#{productCode}</productCode>"+
				"<customerDn>#{customerDn}</customerDn>"+
				"<subBillCount>#{subBillCount}</subBillCount>"+
				"<mainBillNo>#{mainBillNo}</mainBillNo>"+
				"<mainBillFlag>#{mainBillFlag}</mainBillFlag>"+
				"<mainSubPayMode>#{mainSubPayMode}</mainSubPayMode>"+
				"<payMode>#{payMode}</payMode>"+
				"<insureType>#{insureType}</insureType>"+
				"<addser_dshk>#{addser_dshk}</addser_dshk>"+
				"<addser_sjrff>#{addser_sjrff}</addser_sjrff>"+
				"<blank1>#{blank1}</blank1>"+
				"<blank2>#{blank2}</blank2>"+
				"<blank3>#{blank3}</blank3>"+
				"<blank4>#{blank4}</blank4>"+
				"<blank5>#{blank5}</blank5>"+
				"</printData>"+
				"</printDatas>"+
				"</XMLInfo>";
		
		return paramXmlTemplate.replace("#{sysAccount}", SYS_ACCOUNT).replace("#{passWord}", PASS_WORD).replace("#{appKey}", APP_KEY)
				.replace("#{printKind}", PRINT_KIND);
	}
}
