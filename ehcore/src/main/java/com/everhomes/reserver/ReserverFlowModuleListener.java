package com.everhomes.reserver;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.flow.*;
import com.everhomes.pmtask.*;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.parking.ParkingErrorCode;
import com.everhomes.rest.pmtask.*;
import com.everhomes.user.UserContext;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SignatureHelper;
import com.everhomes.util.StringHelper;
import com.everhomes.util.Tuple;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ReserverFlowModuleListener implements FlowModuleListener {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(ReserverFlowModuleListener.class);
	
	private Long moduleId = FlowConstants.RESERVER_PLACE;
	private static final String GET_RESERVER_DETAIL = "/zl-ec/rest/openapi/shop/queryShopsPositionById";
	private static final String UPDATE_RESERVER = "/zl-ec/rest/openapi/shop/updatePositionReservationResult";

	@Autowired
	private FlowService flowService;
	@Autowired
	private ConfigurationProvider configProvider;

	private CloseableHttpClient httpclient = null;
//	private String serverUrl;
//	private String appKey;
//	private String secretKey;

	@PostConstruct
	public void init() {
		httpclient = HttpClients.createDefault();


	}

	@PreDestroy
	public void destroy() {
		if(null != httpclient) {
			try {
				httpclient.close();
			} catch (IOException e) {
				LOGGER.error("Reserver close httpclient, response error, httpclient={}", httpclient, e);
			}
		}
	}

	@Override
	public FlowModuleInfo initModule() {
		FlowModuleInfo module = new FlowModuleInfo();
		FlowModuleDTO moduleDTO = flowService.getModuleById(moduleId);
		module.setModuleName(moduleDTO.getDisplayName());
		module.setModuleId(moduleId);
		return module;
	}

	@Override
	public void onFlowCaseStart(FlowCaseState ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFlowCaseAbsorted(FlowCaseState ctx) {


	}

	//状态改变之后
	@Override
	public void onFlowCaseStateChanged(FlowCaseState ctx) {

	}

	@Override
	public void onFlowCaseEnd(FlowCaseState ctx) {

	}

	@Override
	public void onFlowCaseActionFired(FlowCaseState ctx) {

	}

	@Override
	public String onFlowCaseBriefRender(FlowCase flowCase) {
		return null;
	}

	@Override
	public List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType) {

		Map<String,String> params = new HashMap<>();
		params.put("id", String.valueOf(flowCase.getReferId()));
		String json = post(createRequestParam(params), GET_RESERVER_DETAIL);

		ReserverEntity<ReserverDTO> entity = JSONObject.parseObject(json, new TypeReference<ReserverEntity<ReserverDTO>>(){});

		ReserverDTO dto = entity.getBody();

		flowCase.setCustomObject(JSONObject.toJSONString(dto));
		
		List<FlowCaseEntity> entities = new ArrayList<>();
		FlowCaseEntity e;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.LIST.getCode());
		e.setKey("就餐时间");
		e.setValue(sdf.format(dto.getDinnerTime()));
		entities.add(e);
		
		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.LIST.getCode());
		e.setKey("就餐人数");
		e.setValue(dto.getDinnerNum().toString());
		entities.add(e);

		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
		e.setKey("备注说明");
		e.setValue(dto.getDinnerNote());
		entities.add(e);
		
		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.LIST.getCode());
		e.setKey("申请人");
		e.setValue(dto.getBuyerName());
		entities.add(e);
		
		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.LIST.getCode());
		e.setKey("联系电话");
		e.setValue(dto.getBuyerPhone());
		entities.add(e);
		
		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.LIST.getCode());
		e.setKey("店铺名称");
		e.setValue(dto.getShopName());
		entities.add(e);

		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.LIST.getCode());
		e.setKey("联系商家");
		e.setValue(dto.getShopPhone());
		entities.add(e);

		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.LIST.getCode());
		e.setKey("申请结果");
		e.setValue(convert(dto.getStatus()));
		entities.add(e);
		
		return entities;
	}

	private  String convert(Integer status) {
		switch (status) {
			case 1: return "成功";
			case 2: return "失败";
			default: return "处理中";
		}
	}

	@Override
	public String onFlowVariableRender(FlowCaseState ctx, String variable) {
		return null;
	}

	//fireButton 之前
	@Override
	public void onFlowButtonFired(FlowCaseState ctx) {

		FlowGraphNode currentNode = ctx.getCurrentNode();
		FlowNode flowNode = currentNode.getFlowNode();
		FlowCase flowCase = ctx.getFlowCase();

		String stepType = ctx.getStepType().getCode();
//		String params = flowNode.getParams();
//
//		JSONObject paramJson = JSONObject.parseObject(params);
//		String nodeType = paramJson.getString("nodeType");

		LOGGER.debug("update reserver request, stepType={}", stepType);

		Map<String,String> param = new HashMap<>();
		if(FlowStepType.APPROVE_STEP.getCode().equals(stepType)) {
			param.put("status", "1");
			param.put("id", String.valueOf(flowCase.getReferId()));
			String json = post(createRequestParam(param), UPDATE_RESERVER);
			ReserverEntity<Object> entity = JSONObject.parseObject(json, new TypeReference<ReserverEntity<Object>>(){});

			if (!entity.getResult()) {
				LOGGER.error("sychronized position reserver to biz fail.");
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
						"sychronized position reserver to biz fail.");
			}
		}else if (FlowStepType.ABSORT_STEP.getCode().equals(stepType)) {
			param.put("status", "2");
			param.put("id", String.valueOf(flowCase.getReferId()));
			String json = post(createRequestParam(param), UPDATE_RESERVER);
			ReserverEntity<Object> entity = JSONObject.parseObject(json, new TypeReference<ReserverEntity<Object>>(){});

			if (!entity.getResult()) {
				LOGGER.error("sychronized position reserver to biz fail.");
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
						"sychronized position reserver to biz fail.");
			}
		}

	}
	
	@Override
	public void onFlowCreating(Flow flow) {
		

	}

	@Override
	public void onFlowCaseCreating(FlowCase flowCase) {

	}

	@Override
	public void onFlowCaseCreated(FlowCase flowCase) {

	}

	private String post(Map<String,String> param, String method) {
		String serverUrl = configProvider.getValue("position.reserver.serverUrl", "");

		HttpPost httpPost = new HttpPost(serverUrl + method);
		CloseableHttpResponse response = null;

		String json = null;
		try {
			String p = StringHelper.toJsonString(param);
			StringEntity stringEntity = new StringEntity(p, StandardCharsets.UTF_8);
			httpPost.setEntity(stringEntity);
			httpPost.addHeader("content-type", "application/json");

			response = httpclient.execute(httpPost);

			int status = response.getStatusLine().getStatusCode();
			if(status == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();

				if (entity != null) {
					json = EntityUtils.toString(entity, "utf8");
				}
			}
		} catch (IOException e) {
			LOGGER.error("Reserver request error, param={}", param, e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"Reserver request error.");
		}finally {
			if (null != response) {
				try {
					response.close();
				} catch (IOException e) {
					LOGGER.error("Reserver close instream, response error, param={}", param, e);
				}
			}
		}
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("Data from business, param={}, json={}", param, json);

		return json;
	}

	private Map createRequestParam(Map<String,String> params) {
		Integer nonce = (int)(Math.random()*1000);
		Long timestamp = System.currentTimeMillis();

		String appKey = configProvider.getValue("position.reserver.appKey", "");
		String secretKey = configProvider.getValue("position.reserver.secretKey", "");
		params.put("nonce", String.valueOf(nonce));
		params.put("timestamp", String.valueOf(timestamp));
		params.put("appKey", appKey);

		Map<String, String> mapForSignature = new HashMap<>();
		for(Map.Entry<String, String> entry : params.entrySet()) {
			mapForSignature.put(entry.getKey(), entry.getValue());
		}

		String signature = SignatureHelper.computeSignature(mapForSignature, secretKey);
		try {
			params.put("signature", URLEncoder.encode(signature,"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return params;
	}

	@Override
	public void onFlowSMSVariableRender(FlowCaseState ctx, int templateId,
			List<Tuple<String, Object>> variables) {
		// TODO Auto-generated method stub
		
	}
}
