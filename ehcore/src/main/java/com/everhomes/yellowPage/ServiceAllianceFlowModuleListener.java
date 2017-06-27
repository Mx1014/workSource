package com.everhomes.yellowPage;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.everhomes.rest.general_approval.*;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessageMetaConstant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowModuleInfo;
import com.everhomes.general_approval.GeneralApproval;
import com.everhomes.general_approval.GeneralApprovalFlowModuleListener;
import com.everhomes.general_approval.GeneralApprovalVal;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.module.ServiceModule;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowCaseEntityType;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.quality.OwnerType;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.search.ServiceAllianceRequestInfoSearcher;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.DateHelper;
import com.everhomes.util.Tuple;
@Component
public class ServiceAllianceFlowModuleListener extends GeneralApprovalFlowModuleListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceAllianceFlowModuleListener.class);
	@Autowired
	private ServiceAllianceRequestInfoSearcher serviceAllianceRequestInfoSearcher;
	private static final long MODULE_ID = 40500;
			
    @Autowired
    private YellowPageProvider yellowPageProvider;
    
    @Autowired
    private UserProvider userProvider;
    
	@Override
	public FlowModuleInfo initModule() {
        FlowModuleInfo moduleInfo = new FlowModuleInfo();
        ServiceModule module = serviceModuleProvider.findServiceModuleById(MODULE_ID);
        moduleInfo.setModuleName(module.getName());
        moduleInfo.setModuleId(MODULE_ID);
        return moduleInfo;
	}
    
	@Override
	public void onFlowCaseCreating(FlowCase flowCase) {
		// 服务联盟的审批拼接工作流 content字符串
		
		ServiceAllianceRequestInfo request = new ServiceAllianceRequestInfo();
		
		PostApprovalFormCommand cmd = JSONObject.parseObject(flowCase.getContent(), PostApprovalFormCommand.class);
		//and by dengs 20170427 异步发送邮件
		sendEmailAsynchronizedTask(flowCase.getContent(),flowCase.getApplyUserId());
		StringBuffer contentBuffer = new StringBuffer();
		GeneralApproval ga = this.generalApprovalProvider.getGeneralApprovalById(cmd
				.getApprovalId());
		GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginId(ga
				.getFormOriginId());
		List<PostApprovalFormItem> values = cmd.getValues();
		List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
//		PostApprovalFormItem nameVal = getFormFieldDTO(GeneralFormDataSourceType.USER_NAME.getCode(),values);
//		if(null != nameVal){
//			GeneralFormFieldDTO dto = getFieldDTO(nameVal.getFieldName(),fieldDTOs); 
//			contentBuffer.append(dto.getFieldDisplayName());
//			contentBuffer.append(" : ");
//			contentBuffer.append(JSON.parseObject(nameVal.getFieldValue(), PostApprovalFormTextValue.class).getText());
//		}
//		PostApprovalFormItem contactVal = getFormFieldDTO(GeneralFormDataSourceType.USER_PHONE.getCode(),values);
//		if(null != contactVal){
//			if(contentBuffer.length()>1)
//				contentBuffer.append("\n");
//			GeneralFormFieldDTO dto = getFieldDTO(contactVal.getFieldName(),fieldDTOs); 
//			contentBuffer.append(dto.getFieldDisplayName());
//			contentBuffer.append(" : ");
//			contentBuffer.append(JSON.parseObject(contactVal.getFieldValue(), PostApprovalFormTextValue.class).getText());
//		}
//		PostApprovalFormItem entpriseVal = getFormFieldDTO(GeneralFormDataSourceType.USER_COMPANY.getCode(),values);
//		if(null != entpriseVal){
//			if(contentBuffer.length()>1)
//				contentBuffer.append("\n");
//			GeneralFormFieldDTO dto = getFieldDTO(entpriseVal.getFieldName(),fieldDTOs); 
//			contentBuffer.append(dto.getFieldDisplayName());
//			contentBuffer.append(" : ");
//			contentBuffer.append(JSON.parseObject(entpriseVal.getFieldValue(), PostApprovalFormTextValue.class).getText());
//		}
		contentBuffer.append("申请类型");
		contentBuffer.append(" : ");
		contentBuffer.append(ga.getApprovalName());
		PostApprovalFormItem sourceVal = getFormFieldDTO(GeneralFormDataSourceType.SOURCE_ID.getCode(),values);
		if(null != sourceVal){
			if(contentBuffer.length()>1)
				contentBuffer.append("\n");
			Long yellowPageId = Long.valueOf(JSON.parseObject(sourceVal.getFieldValue(), PostApprovalFormTextValue.class).getText());
			ServiceAlliances  yellowPage = yellowPageProvider.findServiceAllianceById(yellowPageId,null,null); 
			ServiceAllianceCategories  parentPage = yellowPageProvider.findCategoryById(yellowPage.getParentId());

			contentBuffer.append("申请来源");
			contentBuffer.append(" : ");
			contentBuffer.append(parentPage.getName());
			flowCase.setTitle(parentPage.getName());
			request.setServiceAllianceId(yellowPageId);
			request.setType(yellowPage.getParentId());
			
		}
//		flowCase.setModuleName(ga.getApprovalName());
		flowCase.setContent(contentBuffer.toString());
		
		//服务联盟加一个申请
		PostApprovalFormItem organizationVal = getFormFieldDTO(GeneralFormDataSourceType.ORGANIZATION_ID.getCode(),values);
		
		User user = this.userProvider.findUserById(flowCase.getApplyUserId());
		UserIdentifier identifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
		request.setJumpType(2L);
		request.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime())); 
		request.setCreatorName(user.getNickName());
		request.setCreatorOrganizationId(Long.valueOf(JSON.parseObject(organizationVal.getFieldValue(), PostApprovalFormTextValue.class).getText()));
		request.setCreatorMobile(identifier.getIdentifierToken());
		request.setOwnerType(OwnerType.COMMUNITY.getCode());
		request.setOwnerId(flowCase.getProjectId());
		request.setFlowCaseId(flowCase.getId());
		request.setId(flowCase.getId());
		request.setCreatorUid(UserContext.current().getUser().getId());
		request.setTemplateType("flowCase");
		serviceAllianceRequestInfoSearcher.feedDoc(request);
	}


	@Override
	public List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
		List<FlowCaseEntity> entities = new ArrayList<>(); 
		//前面写服务联盟特有的默认字段-姓名-电话-企业-申请类型-申请来源-服务机构
		//姓名
		FlowCaseEntity e = new FlowCaseEntity();
		GeneralApprovalVal val = this.generalApprovalValProvider.getGeneralApprovalByFlowCaseAndName(flowCase.getId(),
				GeneralFormDataSourceType.USER_NAME.getCode()); 
		GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginIdAndVersion(
				val.getFormOriginId(), val.getFormVersion());
		List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
		GeneralFormFieldDTO dto = getFieldDTO(val.getFieldName(),fieldDTOs); 
		e.setKey(dto.getFieldDisplayName());
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode()); 
		e.setValue(JSON.parseObject(val.getFieldStr3(), PostApprovalFormTextValue.class).getText());
		entities.add(e);
		
		//电话
		e = new FlowCaseEntity();
		val = this.generalApprovalValProvider.getGeneralApprovalByFlowCaseAndName(flowCase.getId(),
				GeneralFormDataSourceType.USER_PHONE.getCode()); 
		if(val != null){
			dto = getFieldDTO(val.getFieldName(),fieldDTOs); 
			e.setKey(dto.getFieldDisplayName());
			e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode()); 
			e.setValue(JSON.parseObject(val.getFieldStr3(), PostApprovalFormTextValue.class).getText());
			entities.add(e);
		}
		
		//企业
		e = new FlowCaseEntity();
		val = this.generalApprovalValProvider.getGeneralApprovalByFlowCaseAndName(flowCase.getId(),
				GeneralFormDataSourceType.USER_COMPANY.getCode()); 
		if(val != null){
			dto = getFieldDTO(val.getFieldName(),fieldDTOs); 
			e.setKey(dto.getFieldDisplayName());
			e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode()); 
			e.setValue(JSON.parseObject(val.getFieldStr3(), PostApprovalFormTextValue.class).getText());
			entities.add(e);
		}
		
		//楼栋门牌
		e = new FlowCaseEntity();
		val = this.generalApprovalValProvider.getGeneralApprovalByFlowCaseAndName(flowCase.getId(),
				GeneralFormDataSourceType.USER_ADDRESS.getCode()); 
		if(val != null){
			dto = getFieldDTO(val.getFieldName(),fieldDTOs); 
			e.setKey(dto.getFieldDisplayName());
			e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode()); 
			if(JSON.parseObject(val.getFieldStr3(), PostApprovalFormTextValue.class) != null){
				e.setValue(JSON.parseObject(val.getFieldStr3(), PostApprovalFormTextValue.class).getText());
				entities.add(e);	
			}
		}
		
		//username是存在的防止空指针异常
		if(val ==null){
			val = this.generalApprovalValProvider.getGeneralApprovalByFlowCaseAndName(flowCase.getId(),
					GeneralFormDataSourceType.USER_NAME.getCode()); 
		}
		
		//申请类型
		e = new FlowCaseEntity(); 
		GeneralApproval ga = this.generalApprovalProvider.getGeneralApprovalById(val.getApprovalId());

		e.setKey("申请类型");
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode()); 
		e.setValue(ga.getApprovalName());
		entities.add(e);

		val = this.generalApprovalValProvider.getGeneralApprovalByFlowCaseAndName(flowCase.getId(),
				GeneralFormDataSourceType.SOURCE_ID.getCode()); 
		Long yellowPageId = Long.valueOf(JSON.parseObject(val.getFieldStr3(), PostApprovalFormTextValue.class).getText());
		ServiceAlliances  yellowPage = yellowPageProvider.findServiceAllianceById(yellowPageId,null,null);
		ServiceAllianceCategories  parentPage = null;
		if(null != yellowPage)
			parentPage = yellowPageProvider.findCategoryById(yellowPage.getParentId());
		
		//申请来源
		e = new FlowCaseEntity(); 
		e.setKey("申请来源");
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());  
		if(null == parentPage)
			e.setValue("已删除");
		else
			e.setValue(parentPage.getName());
		entities.add(e);
		//服务机构

		e = new FlowCaseEntity();
		e.setKey("服务机构");
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode()); 
		if(null == yellowPage)
			e.setValue("已删除");
		else
			e.setValue(yellowPage.getName());
		entities.add(e);
		
		//后面跟自定义模块-- 
		entities.addAll(onFlowCaseCustomDetailRender(flowCase, flowUserType));
		return entities;
	}

	@Override
	public void onFlowCaseCreated(FlowCase flowCase) {
		
	}

	@Override
	public void onFlowSMSVariableRender(FlowCaseState ctx, int templateId,
			List<Tuple<String, Object>> variables) {
		// TODO Auto-generated method stub
		
	}
	
	private void sendEmailAsynchronizedTask(String contents,Long userId) {
		ServiceAllianceAsynchronizedServiceImpl handler = PlatformContext.getComponent("serviceAllianceAsynchronizedServiceImpl");
		handler.pushToQueque(contents,userId);
	}
	
	@Override
	public void onFlowMessageSend(FlowCaseState ctx, MessageDTO messageDto) {
		Map<String, String> metaMap = messageDto.getMeta();
		
		FlowCase flowCase = ctx.getFlowCase();
		//服务联盟的消息提示，标题搞成了服务联盟大分类的名称-也就是功能入口的名称。
		if(flowCase == null){
			LOGGER.info("onFlowMessageSend flowCase = {}", flowCase);
			return ;
		}
		LOGGER.info("onFlowMessageSend title = {}",flowCase.getTitle());
		metaMap.put(MessageMetaConstant.MESSAGE_SUBJECT, flowCase.getTitle());
	}
}
