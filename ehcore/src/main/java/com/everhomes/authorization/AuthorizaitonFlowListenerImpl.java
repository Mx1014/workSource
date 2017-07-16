// @formatter:off
package com.everhomes.authorization;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.common.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.authorization.zjgk.ZjgkJsonEntity;
import com.everhomes.authorization.zjgk.ZjgkResponse;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowModuleInfo;
import com.everhomes.flow.FlowModuleListener;
import com.everhomes.flow.FlowService;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowCaseEntityType;
import com.everhomes.rest.flow.FlowCaseStatus;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.flow.FlowModuleDTO;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.user.UserContext;
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
	private LocaleStringService localeStringService;
	
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
		AuthorizationThirdPartyRecord record = recordProvider.getAuthorizationThirdPartyRecordByFlowCaseId(flowCase.getId());
		if(record == null){
			LOGGER.error("unKnown flowcase id = {}",flowCase.getId());
			return null;
		}
		String document = localeStringService.getLocalizedString(AuthorizationErrorCode.SCOPE, 
				AuthorizationErrorCode.WORK_FLOW_CONTENT, UserContext.current().getUser().getLocale(),AuthorizationErrorCode.WORK_FLOW_CONTENT_S);
		String[] documents = document.split("\\|");
		if(AuthorizationModuleHandler.PERSONAL_AUTHORIZATION.equals(record.getType().trim())){
			return createPersonalEntities(record,documents);
		}else if(AuthorizationModuleHandler.ORGANIZATION_AUTHORIZATION.equals(record.getType().trim())){
			return createOrganiztionEntites(record,documents);
		}
		LOGGER.error("unKnown record type = {}",record.getType());
		return null;
	}

	private List<FlowCaseEntity> createOrganiztionEntites(AuthorizationThirdPartyRecord record, String[] documents) {
		String documentflow = localeStringService.getLocalizedString(AuthorizationErrorCode.SCOPE, 
				AuthorizationErrorCode.ORGANIZATION_BACK_CODE_DETAIL, UserContext.current().getUser().getLocale(), AuthorizationErrorCode.ORGANIZATION_BACK_CODE_DETAIL_S);
		String[] documentflows = documentflow.split("\\|");
		
		List<FlowCaseEntity> entities = new ArrayList<>(); 
		ZjgkJsonEntity<List<ZjgkResponse>> zjgkResponses = JSONObject.parseObject(record.getResultJson(),new TypeReference<ZjgkJsonEntity<List<ZjgkResponse>>>(){});
		//组织机构代码
		FlowCaseEntity e = new FlowCaseEntity();
		e.setKey(documents[0]);
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode()); 
		e.setValue(record.getOrganizationCode());
		entities.add(e);
		//企业联系人
		e = new FlowCaseEntity();
		e.setKey(documents[1]);
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode()); 
		e.setValue(record.getOrganizationContact());
		entities.add(e);
		//企业联系电话
		e = new FlowCaseEntity();
		e.setKey(documents[2]);
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode()); 
		e.setValue(record.getOrganizationPhone());
		entities.add(e);
		
		//认证反馈结果
		e = new FlowCaseEntity();
		e.setKey(documents[3]);
		e.setEntityType(FlowCaseEntityType.TEXT.getCode()); 
		e.setValue(generateContent(zjgkResponses,documentflows));
		entities.add(e);
		
		//地址
		generateAddressEntity(entities, zjgkResponses.getResponse(), documentflows);
		
		return entities;
	}

	private List<FlowCaseEntity> createPersonalEntities(AuthorizationThirdPartyRecord record, String[] documents) {
		String documentflow = localeStringService.getLocalizedString(AuthorizationErrorCode.SCOPE, 
				AuthorizationErrorCode.PERSONAL_BACK_CODE_DETAIL, UserContext.current().getUser().getLocale(), AuthorizationErrorCode.PERSONAL_BACK_CODE_DETAIL_S);
		String[] documentflows = documentflow.split("\\|");
		
		List<FlowCaseEntity> entities = new ArrayList<>(); 
		ZjgkJsonEntity<List<ZjgkResponse>> zjgkResponses = JSONObject.parseObject(record.getResultJson(),new TypeReference<ZjgkJsonEntity<List<ZjgkResponse>>>(){});
		//手机号
		FlowCaseEntity e = new FlowCaseEntity();
		e.setKey(documents[4]);
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode()); 
		e.setValue(record.getPhone());
		entities.add(e);
		//姓名
		e = new FlowCaseEntity();
		e.setKey(documents[5]);
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode()); 
		e.setValue(record.getName());
		entities.add(e);
		//证件类型
		e = new FlowCaseEntity();
		e.setKey(documents[6]);
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode()); 
		e.setValue("1".equals(record.getCertificateType())?documents[9]:documents[10]);
		entities.add(e);
		//证件号码
		e = new FlowCaseEntity();
		e.setKey(documents[7]);
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode()); 
		e.setValue(record.getCertificateNo());
		entities.add(e);
		
		//认证反馈结果
		e = new FlowCaseEntity();
		e.setKey(documents[8]);
		e.setEntityType(FlowCaseEntityType.TEXT.getCode()); 
		e.setValue(generateContent(zjgkResponses,documentflows));
		entities.add(e);
		
		//地址
		generateAddressEntity(entities, zjgkResponses.getResponse(),documentflows);
		return entities;
	
	}
	
	public void generateAddressEntity(List<FlowCaseEntity> entities, List<ZjgkResponse> list, String[] documentflows) {
		FlowCaseEntity e = new FlowCaseEntity();
		
		if(CollectionUtils.isEmpty(list)){
			return ;
		}
		
		for (int i = 0; i < list.size(); i++) {
			StringBuffer buffer = new StringBuffer();
			ZjgkResponse zjgkResponse =list.get(i);
			if(zjgkResponse.getExistCommunityFlag() == ZjgkResponse.EXIST_COMMUNITY){
				buffer.append(zjgkResponse.getCommunityName()).append(zjgkResponse.getBuildingName()).append('-').append(zjgkResponse.getApartmentName());
			}else if(zjgkResponse.getExistCommunityFlag() == ZjgkResponse.NOT_EXIST_COMMUNITY){
				buffer.append(documentflows[6]).append(zjgkResponse.getCommunityName()).append(documentflows[7]).append("\n");
			}else if(zjgkResponse.getExistCommunityFlag() == ZjgkResponse.MULTI_COMMUNITY){
				buffer.append(documentflows[8]).append(zjgkResponse.getCommunityName()).append(documentflows[9]).append("\n");
			}
			e.setKey(documentflows[10]+(i+1));
			e.setEntityType(FlowCaseEntityType.TEXT.getCode()); 
			e.setValue(buffer.toString());
			entities.add(e);
		}
	}
	
	public String generateContent(ZjgkJsonEntity<List<ZjgkResponse>> entity, String[] documentflows) {
		StringBuffer buffer = new StringBuffer();
		if(entity.isMismatching()){
			buffer//.append("\n")
			.append(documentflows[0])
			.append("\n")
			.append(documentflows[1])
			.append("\n")
			.append(documentflows[2])
			.append("\n");
		}
		else if(entity.isUnrent()){
			buffer.append("")
			.append(documentflows[3])
			.append("\n");
		}
		else if(entity.isRequestFail()){
			buffer.append("")
			.append(documentflows[11])
			.append("\n");
		}
		else if(entity.isNotPassInZuolin()){
			buffer.append("")
			.append(documentflows[12])
			.append("\n");
		}
		else if(entity.isSuccess())
		{
			buffer.append("\n").append(documentflows[4]).append("\n");
		}
		else{
			buffer.append("\n").append(documentflows[5]).append(entity.getErrorCode()).append(" ");
		}
		return buffer.toString();
		
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
		if(flowCase != null)
			flowCase.setStatus(FlowCaseStatus.FINISHED.getCode());
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
