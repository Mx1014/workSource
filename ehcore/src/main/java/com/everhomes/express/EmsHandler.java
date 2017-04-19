// @formatter:off
package com.everhomes.express;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.http.HttpUtils;
import com.everhomes.rest.express.EmsBill;
import com.everhomes.rest.express.ExpressLogisticsStatus;
import com.everhomes.rest.express.ExpressTraceDTO;
import com.everhomes.rest.express.GetExpressLogisticsDetailResponse;
import com.everhomes.rest.express.TrackBillResponse;
import com.everhomes.util.RuntimeErrorException;

@Component(ExpressHandler.EXPRESS_HANDLER_PREFIX+"1")
public class EmsHandler implements ExpressHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmsHandler.class);
	
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
	//ems公司logo的uri
	private static final String LOGO_URI = "cs://1/image/aW1hZ2UvTVRwak9XSTJOVFJqWXpjMVkyTmtNVGt4WW1NNU1qaGlNR0k1WlRNelpXRTJNdw";
	
	//java8新加的格式化时间类，是线程安全的
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private ContentServerService contentServerService;
	
	@Override
	public String getBillNo(ExpressOrder expressOrder) {
		String billNo = getBillNo(String.valueOf(expressOrder.getSendType()));
		expressOrder.setBillNo(billNo);
		updatePrintInfo(convertToEmsBill(expressOrder));
		return billNo;
	}
	
	private EmsBill convertToEmsBill(ExpressOrder expressOrder) {
		EmsBill emsBill = new EmsBill();
		emsBill.setBigAccountDataId(String.valueOf(expressOrder.getId()));
		emsBill.setBusinessType(String.valueOf(expressOrder.getSendType()));
		emsBill.setBillno(expressOrder.getBillNo());
		
		
//		<billno></billno>	邮件号	VARCHAR2(20)	M	快递单号
//		<dateType></dateType>	时间类型	VARCHAR2(1)	C	1:收寄时间
//		2:打印时间
//		2
//		<procdate></procdate>	时间	VARCHAR2(16)	C	YYYY-MM-DD hh:mi:ss
//		<scontactor></scontactor>	寄件人姓名	VARCHAR2(50)	M	
//		<scustMobile></scustMobile>	寄件人电话1	VARCHAR2(20)	M	两个电话必填一项
//		<scustTelplus></scustTelplus>	寄件人电话2	VARCHAR2(20)	C	
//		<scustPost></scustPost>	寄件人邮编	VARCHAR2(6)	C	
//		<scustAddr></scustAddr>	寄件人地址	VARCHAR2(200)	M	地址与公司名称必填一项
//		<scustComp></scustComp>	寄件人公司名称	VARCHAR2(50)	C	
//		<scustProvince></scustProvince>	寄件人所在省	VARCHAR2(20)	C	行政区划名称
//		<scustCity></scustCity>	寄件人所在市	VARCHAR2(20)	C	行政区划名称
//		<scustCounty></scustCounty>	寄件人所在区县	VARCHAR2(20)	C	行政区划名称
//		<tcontactor></tcontactor>	收件人姓名	VARCHAR2(50)	M	
//		<tcustMobile></tcustMobile>	收件人电话1	VARCHAR2(20)	M	两个电话必填一项
//		<tcustTelplus></tcustTelplus>	收件人电话2	VARCHAR2(20)	C	
//		<tcustPost></tcustPost>	收件人邮编	VARCHAR2(6)	C	
//		<tcustAddr></tcustAddr>	收件人地址	VARCHAR2(200)	M	地址与公司名称必填一项
//		<tcustComp></tcustComp>	收件人公司名称	VARCHAR2(50)	C	
//		<tcustProvince></tcustProvince>	收件人所在省	VARCHAR2(20)	C	行政区划名称
//		<tcustCity></tcustCity>	收件人所在市	VARCHAR2(20)	C	行政区划名称
//		<tcustCounty></tcustCounty>	收件人所在区县	VARCHAR2(20)	C	行政区划名称
//		<weight></weight>	货物重量	NUMBER(14,4)	C	单位：KG
//		<length></length>	货物长度	NUMBER(14,4)	C	单位：CM
//		<insure></insure>	保价金额	NUMBER(14,4)	C	单位：元
//		<insurance></insurance>	保险金额	NUMBER(14,4)	C	单位：元
//		<fee></fee>	货款金额	NUMBER(14,4)	C	单位：元
//		<feeUppercase></feeUppercase>	大写货款金额	VARCHAR2(30)	C	
//		<cargoDesc></cargoDesc>	内件信息	VARCHAR2(100)		
//		<cargoType></cargoType>	内件类型	VARCHAR2(10)	C	传入文字：
//		文件
//		物品
//		<deliveryclaim></deliveryclaim>	揽投员的投递要求	VARCHAR2(30)	C	
//		<remark></remark>	备注	VARCHAR2(100)	C	
//		<productCode></productCode>	邮件产品代码	VARCHAR2(20)	C	EMS内部定义的邮件产品代码
//		<customerDn></customerDn>	订单号	VARCHAR2(30)	C	一票多件必填
//		<subBillCount></subBillCount>	分单数	NUMBER(4)	C	>0时，含分单邮件
//		<mainBillNo></mainBillNo>	主单邮件号	Varchar2(20)	C	分单对应的主单邮件号；主单邮件和普通邮件，该字段放空
//		<mainBillFlag></mainBillFlag>	主分单标识	VARCHAR2(1)	C	1：普通(无分单时)
//		2：主单
//		3：分单
//		<mainSubPayMode></mainSubPayMode>	一票多单计费方式	VARCHAR2(1)	C	1：集中主单计费
//		2：平均重量计费
//		3：分单免首重
//		4：主分单单独计费
//		<payMode></payMode>	付费类型	VARCHAR2(1)	C	1：现金(支票)
//		2：记欠
//		3：托收
//		4：转帐
//		9：其他
//		<insureType></insureType>	所负责任	VARCHAR2(1)	C	2：保价
//		3：保险
//		<addser_dshk></addser_dshk>	代收货款（附加服务）	VARCHAR2(1)	C	1:代收货款
//		0:非代收货款
//		<addser_sjrff></addser_sjrff>	收件人付费(附加服务)	VARCHAR2(1)	C	1:收件人付费
//		0:非收件人付费
//		<blank1></blank1>	留白1	VARCHAR2(100)	C	
//		<blank2></blank2>	留白2	VARCHAR2(100)	C	
//		<blank3></blank3>			C	预留，不实际使用
//		<blank4></blank4>			C	预留，不实际使用
//		<blank5></blank5>			C	预留，不实际使用

		
		
		
		
		
		
		
		return null;
	}

	@Override
	public String getExpressLogoUrl() {
		try {
			return contentServerService.parserUri(LOGO_URI, "", null);
		} catch (Exception e) {

		}
		return "";
	}

	@Override
	public GetExpressLogisticsDetailResponse getExpressLogisticsDetail(ExpressCompany expressCompany, String billNo) {
		TrackBillResponse trackBillResponse = trackBill(billNo);
		List<ExpressTraceDTO> traces = null;
		if (trackBillResponse != null && (traces=trackBillResponse.getTraces()) != null && !traces.isEmpty()) {
			GetExpressLogisticsDetailResponse response = new GetExpressLogisticsDetailResponse();
			response.setExpressCompany(expressCompany.getName());
			response.setBillNo(billNo);
			response.setExpressLogo(getExpressLogoUrl());
			response.setTraces(traces);
			
			String tracesString = JSON.toJSONString(traces);
			if (tracesString.contains("投递并签收")) {
				response.setLogisticsStatus(ExpressLogisticsStatus.RECEIVED.getCode());
//			}else if (tracesString.contains("已收件")) {
			}else {
				response.setLogisticsStatus(ExpressLogisticsStatus.IN_TRANSIT.getCode());
			}
			
			if (traces.size() > 1) {
				ExpressTraceDTO first = traces.get(0);
				ExpressTraceDTO last = traces.get(traces.size()-1);
				response.setConsumeTime(getDeltaTime(last.getAcceptTime(), first.getAcceptTime()));
			}
			
			return response;
		}
		// 返回空表示不存在该物流信息，前端直接提示用户不需要进入物流详情界面
		return null;
	}

	private String getDeltaTime(String first, String last) {
		LocalDateTime dateTime = LocalDateTime.parse(first, DATE_TIME_FORMATTER);
		Instant instant = dateTime.atZone(ZoneOffset.systemDefault()).toInstant();
		LocalDateTime dateTime2 = LocalDateTime.parse(last, DATE_TIME_FORMATTER);
		Instant instant2 = dateTime2.atZone(ZoneOffset.systemDefault()).toInstant();
		long deltaHours = instant2.until(instant, ChronoUnit.HOURS);
		//返回格式x.y表示x天y小时
		return deltaHours/24+"."+deltaHours%24;
	}

	/**
	 * @param businessType 1:标准快递 8:代收到付 9:快递包裹，现在都是1
	 */
	private String getBillNo(String businessType) {
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
			LOGGER.error("get bill no from ems error: {}", e);
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
		
		//如果程序运行到这里，说明未取得邮件号，抛出异常
		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "get bill no from ems error");
	}
	
	private void updatePrintInfo(EmsBill emsBill) {
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
			LOGGER.error("update print info to ems error: {}", e);
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
	
	private TrackBillResponse trackBill(String billno) {
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
