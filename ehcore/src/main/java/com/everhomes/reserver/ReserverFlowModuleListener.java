package com.everhomes.reserver;

import com.alibaba.fastjson.JSONObject;
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
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ReserverFlowModuleListener implements FlowModuleListener {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(ReserverFlowModuleListener.class);
	
	private Long moduleId = FlowConstants.RESERVER_PLACE;
	private static final String GET_RESERVER_DETAIL = "/zl-ec/rest/service/front/shop/queryShopsPositionById";
	private static final String UPDATE_RESERVER = "/zl-ec/rest/service/front/shop/updatePositionReservationResult";

	@Autowired
	private FlowService flowService;
	@Autowired
	private ConfigurationProvider configProvider;

	private CloseableHttpClient httpclient = null;

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

		JSONObject param = new JSONObject();
		param.put("id", flowCase.getReferId());
		String json = post(param, GET_RESERVER_DETAIL);

		ReserverDTO dto = JSONObject.parseObject(json, ReserverDTO.class);

		flowCase.setCustomObject(JSONObject.toJSONString(dto));
		
		List<FlowCaseEntity> entities = new ArrayList<>();
		FlowCaseEntity e;

		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.LIST.getCode());
		e.setKey("就餐时间");
		e.setValue(dto.getDinnerTime().toString());
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
		
		return entities;
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
		String params = flowNode.getParams();

		if(StringUtils.isBlank(params)) {
			LOGGER.error("Invalid flowNode param.");
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_FLOW_NODE_PARAM,
					"Invalid flowNode param.");
		}

		JSONObject paramJson = JSONObject.parseObject(params);
		String nodeType = paramJson.getString("nodeType");

		LOGGER.debug("update reserver request, stepType={}, nodeType={}", stepType, nodeType);
		if(FlowStepType.APPROVE_STEP.getCode().equals(stepType)) {
			if ("ASSIGNING".equals(nodeType)) {
				JSONObject param = new JSONObject();
				param.put("id", flowCase.getReferId());
				param.put("status", flowCase.getReferId());
				String json = post(param, UPDATE_RESERVER);


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

	private String post(JSONObject param, String method) {

		String url = configProvider.getValue("pmtask.ebei.url", "");
		HttpPost httpPost = new HttpPost(url + method);
		CloseableHttpResponse response = null;

		String json = null;
		try {
			StringEntity stringEntity = new StringEntity(param.toString(), StandardCharsets.UTF_8);
			httpPost.setEntity(stringEntity);

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

}
