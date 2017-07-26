// @formatter:off
package com.everhomes.express;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.weaver.ReferenceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.express.guomao.GuoMaoChinaPostResponseEntity;
import com.everhomes.express.guomao.GuoMaoChinaPostResponse;
import com.everhomes.parking.Utils;
import com.everhomes.rest.express.GetExpressLogisticsDetailResponse;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SignatureHelper;
//后面的2为表eh_express_companies中父id为0的行的id 国贸 中国邮政
@Component(ExpressHandler.EXPRESS_HANDLER_PREFIX+"3")
public class GuoMaoChinaPostHandler implements ExpressHandler{
	private static final Logger LOGGER = LoggerFactory.getLogger(GuoMaoChinaPostHandler.class);
	
	//创建订单
	private static final String CREATE_ORDER_URL = "http://222.222.2.155:8001/express/createOrder";
	//创建订单
	private static final String GET_ORDER_URL = "http://222.222.2.155:8001/express/getOrderDetail";
	//跟踪运单信息
	private static final String TRACK_BILL_URL = "http://211.156.193.140:8000/cotrackapi/api/track/mail/#{billno}"; 
	// APP KEY
	private static final String APP_KEY = "123";
	private static final String SECRET_KEY = "FC480127D90D26DE382506EE5D409F46";
	//获取数量
	private static final String BILL_NO_AMOUNT = "1";
	//打印类型，1:预制详情单	2:热敏标签式详情单，我们都是2
	private static final String PRINT_KIND = "2";
	
	//java8新加的格式化时间类，是线程安全的
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.systemDefault());
	

	@Override
	public String getBillNo(ExpressOrder expressOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetExpressLogisticsDetailResponse getExpressLogisticsDetail(ExpressCompany expressCompany, String billNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createOrder(ExpressOrder expressOrder) {
		//生成 请求创建订单 参数
		String paramsCreateOrder = getRequestCreateOrderJsonParam(expressOrder);
		//发送 创建订单 请求
		GuoMaoChinaPostResponseEntity<GuoMaoChinaPostResponse> entity = request(paramsCreateOrder, CREATE_ORDER_URL);
		//请求code失败
		checkResponseEntity(entity);
		//生成获取订单详情的参数
		String paramsGetOrder = getRequestGetOrderJsonParam(entity.getResponse());
		//发送获取订单详情请求
		entity = request(paramsGetOrder, GET_ORDER_URL);
		//验证订单是否生成 
		checkResponseEntity(entity);
	
	}
	
	private void checkResponseEntity(GuoMaoChinaPostResponseEntity<GuoMaoChinaPostResponse> entity){
		if(entity.getErrorCode() != 200 || entity.getResponse() == null || entity.getResponse().getBillNo() == null){
			LOGGER.error("response failed, entity = {}", entity);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"response failed, entity = {}"+ entity);
		}
	}
	
	private String getRequestGetOrderJsonParam(GuoMaoChinaPostResponse response) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("appKey",APP_KEY);
	    params.put("timestamp",String.valueOf(System.currentTimeMillis()));
	    params.put("nonce", String.valueOf((int)(Math.random()*10000)));
	    params.put("sendType",response.getSendType());
	    params.put("billNo", response.getBillNo());
	    params.put("signature", SignatureHelper.computeSignature(params, SECRET_KEY));
		return JSONObject.toJSONString(params);
	}

	private GuoMaoChinaPostResponseEntity<GuoMaoChinaPostResponse> request(String params, String url){
		LOGGER.info("createOrder params = {};", params);
		String resultJson = sendHttpRequest(params, url);
		GuoMaoChinaPostResponseEntity entity = null;
		try{
			entity = JSONObject.parseObject(resultJson,new TypeReference<GuoMaoChinaPostResponseEntity<Object>>(){});
			String stringResponse = entity.getResponse() == null ||  entity.getResponse().toString().length() == 0?"{}":entity.getResponse().toString();
			GuoMaoChinaPostResponse response = JSONObject.parseObject(stringResponse,new TypeReference<GuoMaoChinaPostResponse>(){});
			entity.setResponse(response);
		}catch(Exception e){
			LOGGER.error("request {} exception. resultJson = {}", CREATE_ORDER_URL,resultJson);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"Exception occur. "+e);
		}
		return entity;
		
	}
	
	private String sendHttpRequest(String params, String url){
		Map<String,String> heads = new HashMap<String,String>();
		heads.put("Content-Type", "application/json");
		return Utils.post(url, JSONObject.parseObject(params), heads);
	}

	private String getRequestCreateOrderJsonParam(ExpressOrder expressOrder) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("appKey",APP_KEY);
	    params.put("timestamp",String.valueOf(System.currentTimeMillis()));
	    params.put("nonce", String.valueOf((int)(Math.random()*10000)));
	    params.put("orderNo",expressOrder.getOrderNo());
	    params.put("sendType",String.valueOf(expressOrder.getSendType()));
	    params.put("sendName",expressOrder.getSendName());
	    params.put("sendPhone",expressOrder.getSendPhone());
	    params.put("sendOrganization",expressOrder.getSendOrganization());
	    params.put("sendProvince",expressOrder.getSendProvince());
	    params.put("sendCity",expressOrder.getSendCity());
	    params.put("sendCounty",expressOrder.getSendCounty());
	    params.put("sendDetailAddress",expressOrder.getSendDetailAddress());
	    params.put("receiveName",expressOrder.getReceiveName());
	    params.put("receivePhone",expressOrder.getReceivePhone());
	    params.put("receiveOrganization",expressOrder.getReceiveOrganization());
	    params.put("receiveProvince",expressOrder.getReceiveProvince());
	    params.put("receiveCity",expressOrder.getReceiveCity());
	    params.put("receiveCounty",expressOrder.getReceiveCounty());
	    params.put("receiveDetailAddress",expressOrder.getReceiveDetailAddress());
	    params.put("internal",expressOrder.getInternal());
	    params.put("packageCategory",String.valueOf(expressOrder.getPackageType()));
	    params.put("insuredPrice",String.valueOf(expressOrder.getInsuredPrice()));
	    params.put("invoiceCategory",String.valueOf(expressOrder.getInvoiceFlag()));
	    params.put("invoiceHeader",expressOrder.getInvoiceHead());
	    params.put("status",String.valueOf(expressOrder.getStatus()));
	    params.put("signature", SignatureHelper.computeSignature(params, SECRET_KEY));
		return JSONObject.toJSONString(params);
	}

	@Override
	public void updateOrderStatus(ExpressOrder expressOrder) {
		// TODO Auto-generated method stub
		
	}

}
