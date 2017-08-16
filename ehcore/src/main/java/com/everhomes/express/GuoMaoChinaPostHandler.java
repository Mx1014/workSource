// @formatter:off
package com.everhomes.express;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.express.guomao.GuoMaoChinaPostResponse;
import com.everhomes.express.guomao.GuoMaoChinaPostResponseEntity;
import com.everhomes.express.guomao.rit.model.ArrayOfMail;
import com.everhomes.express.guomao.rit.model.Mail;
import com.everhomes.express.guomao.rit.service.MailTtServiceGn;
import com.everhomes.express.guomao.rit.service.MailTtServiceGnPortType;
import com.everhomes.parking.Utils;
import com.everhomes.rest.express.ExpressLogisticsStatus;
import com.everhomes.rest.express.ExpressOrderStatus;
import com.everhomes.rest.express.ExpressPackageType;
import com.everhomes.rest.express.ExpressSendType;
import com.everhomes.rest.express.ExpressTraceDTO;
import com.everhomes.rest.express.GetExpressLogisticsDetailResponse;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SignatureHelper;

//后面的2为表eh_express_companies中父id为0的行的id 国贸 中国邮政
@Component(ExpressHandler.EXPRESS_HANDLER_PREFIX+"3")
public class GuoMaoChinaPostHandler implements ExpressHandler{
	private static final Logger LOGGER = LoggerFactory.getLogger(GuoMaoChinaPostHandler.class);
	
	//创建订单
	private static final String CREATE_ORDER_CONTEXT = "/express/createOrder";
	//创建订单
	private static final String GET_ORDER_CONTEXT = "/express/getOrderDetail";
	//创建订单
	private static final String UPDATE_ORDER_CONTEXT = "/express/updateOrderStatus";
	//服务进入方式(必选项)
	//用于标识是从何处发起的查询；数据来源：0-本系统；1-电子化支局系统；2-中心局系统；3-投递系统；4-短信；5-11185；6-电子商务网站
	private static final String SER_KIND = "6";
	//查询方标识(必选项)
	//每个客户提供一个经过认证的标识码
	private static final String SER_SIGN = "83b6fe9b4cbb442d";
	
	//java8新加的格式化时间类，是线程安全的
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.systemDefault());

	@Autowired
	private ExpressOrderProvider expressOrderProvider;
	
	@Autowired
	private ExpressService expressService;
	
	@Override
	public String getBillNo(ExpressOrder expressOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetExpressLogisticsDetailResponse getExpressLogisticsDetail(ExpressCompany expressCompany, String billNo) {
//		URL wsdlURL = MailTtServiceGn.WSDL_LOCATION;
		URL wsdlURL = null;
		try {
			wsdlURL = new URL(expressCompany.getLogisticsUrl());
		} catch (MalformedURLException e) {
			wsdlURL = MailTtServiceGn.WSDL_LOCATION;
		}
		MailTtServiceGn service = new MailTtServiceGn(wsdlURL, MailTtServiceGn.SERVICE);
		MailTtServiceGnPortType port = service.getMailTtServiceGnHttpPort();
		ArrayOfMail arrayOfMail = port.getMails(SER_KIND, SER_SIGN, billNo);
		List<Mail> maillist = arrayOfMail.getMail();
		if(maillist == null || maillist.size() == 0){
			return null;
		}
		return converMailToResponse(maillist,expressCompany,billNo);
	}

	private GetExpressLogisticsDetailResponse converMailToResponse(List<Mail> maillist,ExpressCompany expressCompany, String billNo) {
		GetExpressLogisticsDetailResponse response = new GetExpressLogisticsDetailResponse();
		response.setBillNo(billNo);
		response.setExpressCompany(expressCompany.getName());
		response.setExpressLogo(expressService.getUrl(expressCompany.getLogo()));
		response.setTraces(new ArrayList<ExpressTraceDTO>());
		for (Mail mail : maillist) {
			ExpressTraceDTO dto = new ExpressTraceDTO();
			if(mail.getOfficeName()!=null && mail.getOfficeName().getValue()!=null && mail.getOfficeName().getValue().trim().length()>0){
				dto.setAcceptAddress(mail.getOfficeName().getValue());
			}
			if(mail.getRelationOfficeDesc()!=null && mail.getRelationOfficeDesc().getValue()!=null && mail.getRelationOfficeDesc().getValue().trim().length()>0){
				dto.setRemark(mail.getRelationOfficeDesc().getValue());
			}
			XMLGregorianCalendar cdar = mail.getActionDateTime();
			if(cdar != null){
				dto.setAcceptTime(cdar.toString().replace("T", " ").replace("+08:00", ""));
			}
			response.getTraces().add(dto);
		}
		String statusvalue = maillist.get(maillist.size()-1).getActionInfoOut().getValue();
		if("已妥投".equals(statusvalue)){
			response.setLogisticsStatus(ExpressLogisticsStatus.RECEIVED.getCode());
		}else {
			response.setLogisticsStatus(ExpressLogisticsStatus.IN_TRANSIT.getCode());
		}
		
		if (maillist.size() > 1) {
			Mail first = maillist.get(0);
			Mail last = maillist.get(maillist.size()-1);
			last.getActionDateTime().toString();
			response.setConsumeTime(getDeltaTime(first, last));
		}
		
		return response;
	}
	
	private String getDeltaTime(Mail first, Mail last) {
		XMLGregorianCalendar fc = first.getActionDateTime();
		XMLGregorianCalendar lc = last.getActionDateTime();
		if(fc == null || fc.toString() == null || lc == null || lc.toString()==null ){
			return null;
		}
		LocalDateTime firstDatetime = LocalDateTime.parse(fc.toString().replace("T", " ").replace("+08:00", ""), DATE_TIME_FORMATTER);
		LocalDateTime lastDatetime = LocalDateTime.parse(lc.toString().replace("T", " ").replace("+08:00", ""), DATE_TIME_FORMATTER);
		Instant finstant = firstDatetime.atZone(ZoneOffset.systemDefault()).toInstant();
		Instant linstant = lastDatetime.atZone(ZoneOffset.systemDefault()).toInstant();
		long deltaHours = finstant.until(linstant, ChronoUnit.HOURS);
		//返回格式x.y表示x天y小时
		return deltaHours/24+"."+deltaHours%24;
	}

	@Override
	public void createOrder(ExpressOrder expressOrder, ExpressCompany expressCompany) {
		if(expressOrder.getSendType().byteValue() == ExpressSendType.CITY_EMPTIES.getCode().byteValue()){
			ExpressPackageType packageType = ExpressPackageType.fromCode(expressOrder.getPackageType());
			expressOrder.setPaySummary(new BigDecimal(packageType.getPrice()));
			return ;
		}
		transferOrderToChinaPost(expressOrder, expressCompany);
	}
	
	private void transferOrderToChinaPost(ExpressOrder expressOrder, ExpressCompany expressCompany) {
		//生成 请求创建订单 参数
		String paramsCreateOrder = getRequestCreateOrderJsonParam(expressOrder,expressCompany);
		//发送 创建订单 请求
		GuoMaoChinaPostResponseEntity<GuoMaoChinaPostResponse> entity = request(paramsCreateOrder, expressCompany.getOrderUrl()+CREATE_ORDER_CONTEXT);
		//请求code失败
		checkResponseEntity(entity);
		
		expressOrder.setBillNo(entity.getResponse().getBillNo());
		
		//通过再次获取订单详情，判断订单是否创建成功。
		getOrderDetail(entity.getResponse().getSendType(), entity.getResponse().getBillNo(), expressCompany);
	}

	@Override
	public void updateOrderStatus(ExpressOrder expressOrder, ExpressCompany expressCompany) {
		//同城信筒，此时才创建订单
		if(expressOrder.getSendType().byteValue() == ExpressSendType.CITY_EMPTIES.getCode().byteValue()){
			transferOrderToChinaPost(expressOrder, expressCompany);
		}else{
			//参数
			String params = getRequestUpdateOrderJsonParam(expressOrder, expressCompany);
			//发送 创建订单 请求
			GuoMaoChinaPostResponseEntity<GuoMaoChinaPostResponse> entity = request(params, expressCompany.getOrderUrl()+UPDATE_ORDER_CONTEXT);
			//验证订单是否生成 
			checkErrorCode(entity);
			
			entity = getOrderDetail(String.valueOf(expressOrder.getSendType()), expressOrder.getBillNo(), expressCompany);
			
			if(!entity.getResponse().getStatus().equals(String.valueOf(expressOrder.getStatus()))){
				LOGGER.error("updateOrderStatus failed, reponse.status = {}, order.status = {}", entity.getResponse().getStatus(),expressOrder.getStatus());
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
						"updateOrderStatus failed, reponse.status = "+entity.getResponse().getStatus()+", order.status = "+expressOrder.getStatus());
			}
		}
	}
	
	public GuoMaoChinaPostResponseEntity<GuoMaoChinaPostResponse> getOrderDetail(String sendType, String billNo, ExpressCompany expressCompany){
		if(billNo == null){//同城信筒，存在billNO为空的情况，不做查询
			return null;
		}
		//生成获取订单详情的参数
		String paramsGetOrder = getRequestGetOrderJsonParam(sendType,billNo,expressCompany);
		//发送获取订单详情请求
		GuoMaoChinaPostResponseEntity<GuoMaoChinaPostResponse> entity = request(paramsGetOrder,expressCompany.getOrderUrl()+GET_ORDER_CONTEXT);
		//验证订单是否生成 
		checkResponseEntity(entity);
		
		return entity;
	}
	
	private GuoMaoChinaPostResponseEntity<GuoMaoChinaPostResponse> request(String params, String url){
		LOGGER.info("createOrder params = {};", params);
		String resultJson = sendApplicationJsonRequest(params, url);
		GuoMaoChinaPostResponseEntity entity = null;
		try{
			entity = JSONObject.parseObject(resultJson,new TypeReference<GuoMaoChinaPostResponseEntity<Object>>(){});
			String stringResponse = entity.getResponse() == null ||  entity.getResponse().toString().length() == 0?"{}":entity.getResponse().toString();
			GuoMaoChinaPostResponse response = JSONObject.parseObject(stringResponse,new TypeReference<GuoMaoChinaPostResponse>(){});
			entity.setResponse(response);
		}catch(Exception e){
			LOGGER.error("request {} exception. resultJson = {}", url,resultJson);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"Exception occur. "+e);
		}
		return entity;
	}
	
	private String sendApplicationJsonRequest(String params, String url){
		Map<String,String> heads = new HashMap<String,String>();
		heads.put("Content-Type", "application/json");
		return Utils.post(url, JSONObject.parseObject(params), heads);
	}
		
	private void checkResponseEntity(GuoMaoChinaPostResponseEntity<GuoMaoChinaPostResponse> entity){
		checkErrorCode(entity);
		checkResponse(entity);
	}
	
	private void checkErrorCode(GuoMaoChinaPostResponseEntity<GuoMaoChinaPostResponse> entity){
		if(entity.getErrorCode() != 200 ){
			LOGGER.error("response failed, errorCode = {}, entity = {}", entity.getErrorCode(), entity);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"response failed, entity = {}"+ entity);
		}
	}
	
	private void checkResponse(GuoMaoChinaPostResponseEntity<GuoMaoChinaPostResponse> entity){
		if(entity.getResponse() == null || entity.getResponse().getBillNo() == null){
			LOGGER.error("response failed, entity = {}", entity);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"response failed, entity = {}"+ entity);
		}
	}
	
	private String getRequestGetOrderJsonParam(String sendType,String billNo, ExpressCompany expressCompany) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("appKey",expressCompany.getAppKey());
	    params.put("timestamp",String.valueOf(System.currentTimeMillis()));
	    params.put("nonce", String.valueOf((int)(Math.random()*10000)));
	    params.put("sendType",sendType);
	    params.put("billNo", billNo);
	    params.put("signature", SignatureHelper.computeSignature(params, expressCompany.getAppSecret()));
		return JSONObject.toJSONString(params);
	}

	private String getRequestCreateOrderJsonParam(ExpressOrder expressOrder, ExpressCompany expressCompany) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("appKey",expressCompany.getAppKey());
	    params.put("timestamp",String.valueOf(System.currentTimeMillis()));
	    params.put("nonce", String.valueOf((int)(Math.random()*10000)));
	    if(expressOrder.getOrderNo() != null){
	    	params.put("orderNo",expressOrder.getOrderNo());
	    }
	    if(expressOrder.getSendType() != null){
	    	params.put("sendType",String.valueOf(expressOrder.getSendType()));
	    }
	    //同城信筒，不传寄件地址
	    if(expressOrder.getSendType().byteValue() != ExpressSendType.CITY_EMPTIES.getCode().byteValue()){
		    params.put("sendName",expressOrder.getSendName());
		    params.put("sendPhone",expressOrder.getSendPhone());
		    params.put("sendOrganization",expressOrder.getSendOrganization());
		    params.put("sendProvince",expressOrder.getSendProvince());
		    params.put("sendCity",expressOrder.getSendCity());
		    params.put("sendCounty",expressOrder.getSendCounty());
		    params.put("sendDetailAddress",expressOrder.getSendDetailAddress());
	    }
	    params.put("receiveName",expressOrder.getReceiveName());
	    params.put("receivePhone",expressOrder.getReceivePhone());
	    params.put("receiveOrganization",expressOrder.getReceiveOrganization());
	    params.put("receiveProvince",expressOrder.getReceiveProvince());
	    params.put("receiveCity",expressOrder.getReceiveCity());
	    params.put("receiveCounty",expressOrder.getReceiveCounty());
	    params.put("receiveDetailAddress",expressOrder.getReceiveDetailAddress());
	    if(expressOrder.getInternal() != null){
	    	params.put("internal",expressOrder.getInternal());
	    }
	    if(expressOrder.getPackageType() != null){
	    	params.put("packageCategory",String.valueOf(expressOrder.getPackageType()));
	    }
	    if(expressOrder.getInsuredPrice() != null){
	    	params.put("insuredPrice",String.valueOf(expressOrder.getInsuredPrice()));
	    }
	    if(expressOrder.getInvoiceFlag() != null){
	    	params.put("invoiceCategory",String.valueOf(expressOrder.getInvoiceFlag()));
	    }
	    if(expressOrder.getInvoiceHead() != null){
	    	params.put("invoiceHeader",expressOrder.getInvoiceHead());
	    }
	    if(expressOrder.getStatus() != null){
	    	params.put("status",String.valueOf(expressOrder.getStatus()));
	    }
	    params.put("signature", SignatureHelper.computeSignature(params, expressCompany.getAppSecret()));
		String stringparam = JSONObject.toJSONString(params);
		LOGGER.info("GuoMaoChinaPostHandler params = {}", stringparam);
		return stringparam;
	}

	private String getRequestUpdateOrderJsonParam(ExpressOrder expressOrder, ExpressCompany expressCompany) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("appKey",expressCompany.getAppKey());
	    params.put("timestamp",String.valueOf(System.currentTimeMillis()));
	    params.put("nonce", String.valueOf((int)(Math.random()*10000)));
	    params.put("billNo", expressOrder.getBillNo());
	    params.put("sendType",String.valueOf(expressOrder.getSendType()));//文档中 没有，后面加上的
	    params.put("status",String.valueOf(expressOrder.getStatus()));
	    params.put("signature", SignatureHelper.computeSignature(params, expressCompany.getAppSecret()));
		return JSONObject.toJSONString(params);
	}

	@Override
	public void getOrderStatus(ExpressOrder expressOrder, ExpressCompany expressCompany) {
		ExpressOrderStatus orderStatus = ExpressOrderStatus.fromCode(expressOrder.getStatus());
		 if(orderStatus == ExpressOrderStatus.FINISHED){
			 return ;
		 }
		 GuoMaoChinaPostResponseEntity<GuoMaoChinaPostResponse> entity = getOrderDetail(String.valueOf(expressOrder.getSendType()), expressOrder.getBillNo(), expressCompany);
		 if(entity == null){
			 return ;
		 }
		 boolean isUpdateOrder = false;
		 //待支付状态，才获取订单的支付金额
		 if(orderStatus == ExpressOrderStatus.WAITING_FOR_PAY){
			 if(entity.getResponse().getPaySummary() != null){
				 isUpdateOrder = true;
				 expressOrder.setPaySummary(new BigDecimal(entity.getResponse().getPaySummary()));
			 }
		 }
		 Byte byteStatus = Byte.valueOf(entity.getResponse().getStatus());
		 if(byteStatus.byteValue() == ExpressOrderStatus.FINISHED.getCode().byteValue()){
			 isUpdateOrder = true;
			 expressOrder.setStatus(byteStatus);
		 }
		 if(isUpdateOrder){
			 expressOrderProvider.updateExpressOrder(expressOrder);
		 }
	}

	@Override
	public void orderStatusCallback(ExpressOrder expressOrder, ExpressCompany expressCompany, Map<String,String> params) {
		
	}

}
