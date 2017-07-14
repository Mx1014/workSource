// @formatter:off
package com.everhomes.authorization;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.authorization.zjgk.ZjgkJsonEntity;
import com.everhomes.authorization.zjgk.ZjgkResponse;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowModuleInfo;
import com.everhomes.flow.FlowModuleListener;
import com.everhomes.flow.FlowService;
import com.everhomes.http.HttpUtils;
import com.everhomes.parking.ParkingProvider;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowCaseEntityType;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.flow.FlowModuleDTO;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.general_approval.PostApprovalFormTextValue;
import com.everhomes.sms.SmsProvider;
import com.everhomes.util.Tuple;

@Component
public class AuthorizaitonFlowListenerImpl implements FlowModuleListener{
	 private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizaitonFlowListenerImpl.class);
	private Long moduleId = FlowConstants.AUTHORIZATION_MODULE;
	@Autowired
	private FlowService flowService;
	@Autowired
	private AuthorizationThirdPartyRecordProvider recordProvider;
	@Autowired
    private ParkingProvider parkingProvider;
	@Autowired
	private SmsProvider smsProvider;
	@Autowired
    private ContentServerService contentServerService;
	
	@Override
	public FlowModuleInfo initModule() {
		FlowModuleInfo module = new FlowModuleInfo();
		FlowModuleDTO moduleDTO = flowService.getModuleById(moduleId);
		module.setModuleName(moduleDTO.getDisplayName());
		module.setModuleId(moduleId);
		return module;
	}

	@Override
	public void onFlowCreating(Flow flow) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFlowCaseStart(FlowCaseState ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFlowCaseAbsorted(FlowCaseState ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFlowCaseStateChanged(FlowCaseState ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFlowCaseEnd(FlowCaseState ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFlowCaseActionFired(FlowCaseState ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String onFlowCaseBriefRender(FlowCase flowCase) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
		List<FlowCaseEntity> entities = new ArrayList<>(); 
//		//前面写服务联盟特有的默认字段-姓名-电话-企业-申请类型-申请来源-服务机构
//		//姓名
		FlowCaseEntity e = new FlowCaseEntity();
		AuthorizationThirdPartyRecord record = recordProvider.getAuthorizationThirdPartyRecordByFlowCaseId(flowCase.getId());
		if(record == null){
			LOGGER.error("unknow flowcase id = {}",flowCase.getId());
			return null;
		}
		if(record.getType() == AuthorizationModuleHandler.PERSONAL_AUTHORIZATION){
			return createPersonalEntities(record);
		}else if(record.getType() == AuthorizationModuleHandler.ORGANIZATION_AUTHORIZATION){
			return createOrganiztionEntites(record);
		}
		LOGGER.error("unknow record type = {}",record.getType());
		return null;
	}

	private List<FlowCaseEntity> createOrganiztionEntites(AuthorizationThirdPartyRecord record) {
		List<FlowCaseEntity> entities = new ArrayList<>(); 
		ZjgkJsonEntity<List<ZjgkResponse>> zjgkResponses = JSONObject.parseObject(record.getResultJson(),new TypeReference<ZjgkJsonEntity<List<ZjgkResponse>>>(){});
		//组织机构代码
		FlowCaseEntity e = new FlowCaseEntity();
		e.setKey("组织机构代码");
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode()); 
		e.setValue(record.getOrganizationCode());
		entities.add(e);
		//企业联系人
		e = new FlowCaseEntity();
		e.setKey("企业联系人");
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode()); 
		e.setValue(record.getOrganizationContact());
		entities.add(e);
		//企业联系电话
		e = new FlowCaseEntity();
		e.setKey("企业联系电话");
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode()); 
		e.setValue(record.getOrganizationPhone());
		entities.add(e);
		
		//认证反馈结果
		e = new FlowCaseEntity();
		e.setKey("认证反馈结果");
		e.setEntityType(FlowCaseEntityType.TEXT.getCode()); 
		e.setValue(ZJAuthorizationModuleHandler.generalContent(zjgkResponses));
		entities.add(e);
		
		return entities;
	}

	private List<FlowCaseEntity> createPersonalEntities(AuthorizationThirdPartyRecord record) {
		List<FlowCaseEntity> entities = new ArrayList<>(); 
		ZjgkJsonEntity<List<ZjgkResponse>> zjgkResponses = JSONObject.parseObject(record.getResultJson(),new TypeReference<ZjgkJsonEntity<List<ZjgkResponse>>>(){});
		//手机号
		FlowCaseEntity e = new FlowCaseEntity();
		e.setKey("手机号");
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode()); 
		e.setValue(record.getPhone());
		entities.add(e);
		//姓名
		e = new FlowCaseEntity();
		e.setKey("姓名");
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode()); 
		e.setValue(record.getName());
		entities.add(e);
		//证件类型
		e = new FlowCaseEntity();
		e.setKey("证件类型");
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode()); 
		e.setValue(record.getCertificateType() == "1"?"身份证":"未知");
		entities.add(e);
		//证件号码
		e = new FlowCaseEntity();
		e.setKey("证件号码");
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode()); 
		e.setValue(record.getCertificateNo());
		entities.add(e);
		
		//认证反馈结果
		e = new FlowCaseEntity();
		e.setKey("认证反馈结果");
		e.setEntityType(FlowCaseEntityType.TEXT.getCode()); 
		e.setValue(ZJAuthorizationModuleHandler.generalContent(zjgkResponses));
		entities.add(e);
		
		return entities;
	
	}

	@Override
	public String onFlowVariableRender(FlowCaseState ctx, String variable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onFlowButtonFired(FlowCaseState ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFlowCaseCreating(FlowCase flowCase) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFlowCaseCreated(FlowCase flowCase) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFlowSMSVariableRender(FlowCaseState ctx, int templateId, List<Tuple<String, Object>> variables) {
		// TODO Auto-generated method stub
		
	}

}
