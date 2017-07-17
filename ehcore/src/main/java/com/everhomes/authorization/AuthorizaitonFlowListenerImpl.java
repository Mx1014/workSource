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
		entities.add(getFlowCaseEntity(documents[0], FlowCaseEntityType.MULTI_LINE.getCode(), record.getOrganizationCode()));
		//企业联系人
		entities.add(getFlowCaseEntity(documents[1], FlowCaseEntityType.MULTI_LINE.getCode(), record.getOrganizationContact()));
		//企业联系电话
		entities.add(getFlowCaseEntity(documents[2], FlowCaseEntityType.MULTI_LINE.getCode(), record.getOrganizationPhone()));
		
		//认证反馈结果
		entities.add(generateContent(zjgkResponses, documentflows,documents[3]));
//		entities.addAll(generateContent(zjgkResponses,documentflows));
		//地址
//		generateAddressEntity(entities, zjgkResponses.getResponse(), documentflows);
		return entities;
	}

	private List<FlowCaseEntity> createPersonalEntities(AuthorizationThirdPartyRecord record, String[] documents) {
		String documentflow = localeStringService.getLocalizedString(AuthorizationErrorCode.SCOPE, 
				AuthorizationErrorCode.PERSONAL_BACK_CODE_DETAIL, UserContext.current().getUser().getLocale(), AuthorizationErrorCode.PERSONAL_BACK_CODE_DETAIL_S);
		String[] documentflows = documentflow.split("\\|");
		
		List<FlowCaseEntity> entities = new ArrayList<>(); 
		ZjgkJsonEntity<List<ZjgkResponse>> zjgkResponses = JSONObject.parseObject(record.getResultJson(),new TypeReference<ZjgkJsonEntity<List<ZjgkResponse>>>(){});
		//手机号
		entities.add(getFlowCaseEntity(documents[4], FlowCaseEntityType.MULTI_LINE.getCode(), record.getPhone()));
		//姓名
		entities.add(getFlowCaseEntity(documents[5], FlowCaseEntityType.MULTI_LINE.getCode(), record.getName()));
		//证件类型
		entities.add(getFlowCaseEntity(documents[6], FlowCaseEntityType.MULTI_LINE.getCode(), "1".equals(record.getCertificateType())?documents[9]:documents[10]));
		//证件号码
		entities.add(getFlowCaseEntity(documents[7], FlowCaseEntityType.MULTI_LINE.getCode(), record.getCertificateNo()));
		
		//认证反馈结果
		entities.add(generateContent(zjgkResponses, documentflows,documents[8]));
//		entities.addAll(generateContent(zjgkResponses,documentflows));
		
		//地址
//		generateAddressEntity(entities, zjgkResponses.getResponse(),documentflows);
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
	
	public String getAddressString(List<ZjgkResponse> list, String[] documentflows) {
		if(CollectionUtils.isEmpty(list)){
			return "";
		}
		
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			ZjgkResponse zjgkResponse =list.get(i);
			if(zjgkResponse.getExistCommunityFlag() == ZjgkResponse.EXIST_COMMUNITY){
				buffer.append(documentflows[10]+(i+1)+" : ").append(zjgkResponse.getCommunityName()).append(zjgkResponse.getBuildingName()).append('-').append(zjgkResponse.getApartmentName());
			}else if(zjgkResponse.getExistCommunityFlag() == ZjgkResponse.NOT_EXIST_COMMUNITY){
				buffer.append(documentflows[10]+(i+1)+" : ").append(documentflows[6]).append(zjgkResponse.getCommunityName()).append(documentflows[7]).append("\n");
			}else if(zjgkResponse.getExistCommunityFlag() == ZjgkResponse.MULTI_COMMUNITY){
				buffer.append(documentflows[10]+(i+1)+" : ").append(documentflows[8]).append(zjgkResponse.getCommunityName()).append(documentflows[9]).append("\n");
			}
		}
		return buffer.toString();
	}
	
	public FlowCaseEntity generateContent(ZjgkJsonEntity<List<ZjgkResponse>> entity, String[] documentflows, String documents) {
		FlowCaseEntity e = new FlowCaseEntity();
		e.setKey(documents);
		e.setEntityType(FlowCaseEntityType.TEXT.getCode());
		StringBuffer buffer = new StringBuffer();
		if(entity.isMismatching()){
//			entities.add(getFlowCaseEntity(documentflows[0], FlowCaseEntityType.TEXT.getCode()));
//			entities.add(getFlowCaseEntity(documentflows[1], FlowCaseEntityType.TEXT.getCode()));
//			entities.add(getFlowCaseEntity(documentflows[2], FlowCaseEntityType.TEXT.getCode()));
			buffer//.append("\n")
			.append(documentflows[0])
			.append("\n")
			.append(documentflows[1])
			.append("\n")
			.append(documentflows[2])
			.append("\n");
		}
		else if(entity.isUnrent()){
//			entities.add(getFlowCaseEntity(documentflows[3], FlowCaseEntityType.TEXT.getCode()));
			buffer
			.append(documentflows[3])
			.append("\n");
		}
		else if(entity.isRequestFail()){
//			entities.add(getFlowCaseEntity(documentflows[11], FlowCaseEntityType.TEXT.getCode()));
			buffer
			.append(documentflows[11])
			.append("\n");
		}
		else if(entity.isNotPassInZuolin()){
//			entities.add(getFlowCaseEntity(documentflows[12], FlowCaseEntityType.TEXT.getCode()));
			buffer
			.append(documentflows[12])
			.append("\n");
		}
		else if(entity.isSuccess())
		{
//			entities.add(getFlowCaseEntity(documentflows[4], FlowCaseEntityType.TEXT.getCode()));
			buffer.append(documentflows[4]).append("\n").append(getAddressString(entity.getResponse(), documentflows));
		}
		else{
//			entities.add(getFlowCaseEntity(documentflows[5], FlowCaseEntityType.TEXT.getCode()));
			buffer.append(documentflows[5]).append(entity.getErrorCode()).append(" ");
		}
//		return buffer.toString();
		e.setValue(buffer.toString());
		return e;
	}
	
	public FlowCaseEntity getFlowCaseEntity(String key,String entityType,String value){
		FlowCaseEntity e = new FlowCaseEntity();
		e.setKey(key);
		e.setEntityType(entityType);
		e.setValue(value);
		return e;
	}
	
	public FlowCaseEntity getFlowCaseEntity(String key,String entityType){
		return getFlowCaseEntity(key, entityType, " ");
	}
	
	public FlowCaseEntity getFlowCaseEntity(String key){
		return getFlowCaseEntity(key, FlowCaseEntityType.MULTI_LINE.getCode());
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
