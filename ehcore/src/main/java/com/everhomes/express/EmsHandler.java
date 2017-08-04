// @formatter:off
package com.everhomes.express;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.bouncycastle.jcajce.provider.digest.MD5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.everhomes.util.MD5Utils;
import com.everhomes.util.RuntimeErrorException;

//后面的1为表eh_express_companies中父id为0的行的id, 华润 EMS
@Component(ExpressHandler.EXPRESS_HANDLER_PREFIX+"1")
public class EmsHandler implements ExpressHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmsHandler.class);
	
	//通过大客户号，业务类型获取详情单号
	private static final String GET_BILL_NO_URL = "http://os.ems.com.cn:8081/zkweb/bigaccount/getBigAccountDataAction.do?method=getBillNumBySys&xml=#{xml}";
	//更新信息到EMS系统
	private static final String UPDATE_PRINT_INFO_URL = "http://os.ems.com.cn:8081/zkweb/bigaccount/getBigAccountDataAction.do?method=updatePrintDatas&xml=#{xml}"; 
	//跟踪运单信息
	private static final String TRACK_BILL_URL = "http://211.156.193.140:8000/cotrackapi/api/track/mail/#{billno}"; 
	// APP KEY
	private static final String APP_KEY = "T757620e368dA9904";
	//获取数量
	private static final String BILL_NO_AMOUNT = "1";
	//打印类型，1:预制详情单	2:热敏标签式详情单，我们都是2
	private static final String PRINT_KIND = "2";
	
	//java8新加的格式化时间类，是线程安全的
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.systemDefault());
	
	//大客户号
	@Value("${ems.sys.account:}")
	private String sysAccount;
	//密码
	@Value("${ems.sys.password:}")
	private String sysPassword;
	
	@Autowired
	private ExpressService expressService;
	
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
		emsBill.setDateType("2");
		emsBill.setProcdate(DATE_TIME_FORMATTER.format(expressOrder.getPrintTime().toInstant()));
		emsBill.setScontactor(expressOrder.getSendName());
		emsBill.setScustMobile(expressOrder.getSendPhone());
		emsBill.setScustAddr(getAddress(expressOrder.getSendProvince(), expressOrder.getSendCity(), expressOrder.getSendCounty(), expressOrder.getSendDetailAddress()));
		emsBill.setScustComp(expressOrder.getSendOrganization());
		emsBill.setScustProvince(expressOrder.getSendProvince());
		emsBill.setScustCity(expressOrder.getSendCity());
		emsBill.setScustCounty(expressOrder.getSendCounty());
		emsBill.setTcontactor(expressOrder.getReceiveName());
		emsBill.setTcustMobile(expressOrder.getReceivePhone());
		emsBill.setTcustAddr(getAddress(expressOrder.getReceiveProvince(), expressOrder.getReceiveCity(), expressOrder.getReceiveCounty(), expressOrder.getReceiveDetailAddress()));
		emsBill.setTcustComp(expressOrder.getReceiveOrganization());
		emsBill.setTcustProvince(expressOrder.getReceiveProvince());
		emsBill.setTcustCity(expressOrder.getReceiveCity());
		emsBill.setTcustCounty(expressOrder.getReceiveCounty());
		emsBill.setInsure(expressOrder.getInsuredPrice()==null?null:String.valueOf(expressOrder.getInsuredPrice()));
		emsBill.setCargoDesc(expressOrder.getInternal());
		return emsBill;
	}

	private String getAddress(String province, String city, String county, String detailAddress) {
		return formatString(province) + formatString(city) + formatString(county) + formatString(detailAddress);
	}
	
	private String formatString(String string) {
		return string == null ? "" : string;
	}

	@Override
	public GetExpressLogisticsDetailResponse getExpressLogisticsDetail(ExpressCompany expressCompany, String billNo) {
		TrackBillResponse trackBillResponse = trackBill(billNo);
		List<ExpressTraceDTO> traces = null;
		if (trackBillResponse != null && (traces=trackBillResponse.getTraces()) != null && !traces.isEmpty()) {
			GetExpressLogisticsDetailResponse response = new GetExpressLogisticsDetailResponse();
			response.setExpressCompany(expressCompany.getName());
			response.setBillNo(billNo);
			response.setExpressLogo(expressService.getUrl(expressCompany.getLogo()));
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
			Header[] headers = new Header[2];
			headers[0] = new BasicHeader("version", "ems_track_cn_1.0");
			headers[1] = new BasicHeader("authenticate", "4CCC070F8E214EBEE050030A240B4A4A");
			result = HttpUtils.get(url, null, headers);
		} catch (IOException e) {
			LOGGER.error("track bill from ems error: {}", e);
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
		
		return paramXmlTemplate.replace("#{sysAccount}", sysAccount).replace("#{passWord}", MD5Utils.getMD5(sysPassword)).replace("#{appKey}", APP_KEY)
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
				"<bigAccountDataId></bigAccountDataId>"+
				"<businessType></businessType>"+
				"<billno></billno>"+
				"<dateType></dateType>"+
				"<procdate></procdate>"+
				"<scontactor></scontactor>"+
				"<scustMobile></scustMobile>"+
				"<scustTelplus></scustTelplus>"+
				"<scustPost></scustPost>"+
				"<scustAddr></scustAddr>"+
				"<scustComp></scustComp>"+
				"<scustProvince></scustProvince>"+
				"<scustCity></scustCity>"+
				"<scustCounty></scustCounty>"+
				"<tcontactor></tcontactor>"+
				"<tcustMobile></tcustMobile>"+
				"<tcustTelplus></tcustTelplus>"+
				"<tcustPost></tcustPost>"+
				"<tcustAddr></tcustAddr>"+
				"<tcustComp></tcustComp>"+
				"<tcustProvince></tcustProvince>"+
				"<tcustCity></tcustCity>"+
				"<tcustCounty></tcustCounty>"+
				"<weight></weight>"+
				"<length></length>"+
				"<insure></insure>"+
				"<insurance></insurance>"+
				"<fee></fee>"+
				"<feeUppercase></feeUppercase>"+
				"<cargoDesc></cargoDesc>"+
				"<cargoType></cargoType>"+
				"<deliveryclaim></deliveryclaim>"+
				"<remark></remark>"+
				"<productCode></productCode>"+
				"<customerDn></customerDn>"+
				"<subBillCount></subBillCount>"+
				"<mainBillNo></mainBillNo>"+
				"<mainBillFlag></mainBillFlag>"+
				"<mainSubPayMode></mainSubPayMode>"+
				"<payMode></payMode>"+
				"<insureType></insureType>"+
				"<addser_dshk></addser_dshk>"+
				"<addser_sjrff></addser_sjrff>"+
				"<blank1></blank1>"+
				"<blank2></blank2>"+
				"<blank3></blank3>"+
				"<blank4></blank4>"+
				"<blank5></blank5>"+
				"</printData>"+
				"</printDatas>"+
				"</XMLInfo>";
		
		paramXmlTemplate = paramXmlTemplate.replace("#{sysAccount}", sysAccount).replace("#{passWord}", MD5Utils.getMD5(sysPassword)).replace("#{appKey}", APP_KEY)
				.replace("#{printKind}", PRINT_KIND);
		
		Method[] methods = emsBill.getClass().getMethods();
		for (Method method : methods) {
			String methodName = method.getName();
			if (methodName.startsWith("get")) {
				try {
					String fieldName = uncapFirst(methodName.substring(3));
					Object value = method.invoke(emsBill);
					if (value != null) {
						paramXmlTemplate = paramXmlTemplate.replaceAll("(<"+fieldName+">)(</"+fieldName+">)", "$1"+value.toString()+"$2");
					}
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					LOGGER.error("get field value by reflect error: {}", e);
				}
			}
		}
		
		return paramXmlTemplate;
	}
	
	private String uncapFirst(String string) {
		return string.substring(0, 1).toLowerCase() + string.substring(1);
	}

	@Override
	public void createOrder(ExpressOrder expressOrder, ExpressCompany expressCompany) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateOrderStatus(ExpressOrder expressOrder, ExpressCompany expressCompany) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getOrderStatus(ExpressOrder expressOrder, ExpressCompany expressCompany) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}
