package com.everhomes.yellowPage;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowModuleInfo;
import com.everhomes.general_approval.GeneralApproval;
import com.everhomes.general_approval.GeneralApprovalFlowModuleListener;
import com.everhomes.general_approval.GeneralApprovalProvider;
import com.everhomes.general_approval.GeneralApprovalVal;
import com.everhomes.general_approval.GeneralForm;
import com.everhomes.general_approval.GeneralFormProvider;
import com.everhomes.module.ServiceModule;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowCaseEntityType;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.general_approval.GeneralFormDataSourceType;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.PostApprovalFormCommand;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.rest.general_approval.PostApprovalFormTextValue;
import com.everhomes.rest.quality.OwnerType;
import com.everhomes.search.ServiceAllianceRequestInfoSearcher;
import com.everhomes.util.DateHelper;
@Component
public class ServiceAllianceFlowModuleListener extends GeneralApprovalFlowModuleListener {
	@Autowired
	private ServiceAllianceRequestInfoSearcher serviceAllianceRequestInfoSearcher;
	private static final long MODULE_ID = 40500;
			
    @Autowired
    private YellowPageProvider yellowPageProvider;
    

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
		StringBuffer contentBuffer = new StringBuffer();
		GeneralApproval ga = this.generalApprovalProvider.getGeneralApprovalById(cmd
				.getApprovalId());
		GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginId(ga
				.getFormOriginId());
		List<PostApprovalFormItem> values = cmd.getValues();
		List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
		PostApprovalFormItem nameVal = getFormFieldDTO(GeneralFormDataSourceType.USER_NAME.getCode(),values);
		if(null != nameVal){
			GeneralFormFieldDTO dto = getFieldDTO(nameVal.getFieldName(),fieldDTOs); 
			contentBuffer.append(dto.getFieldDisplayName());
			contentBuffer.append(" : ");
			contentBuffer.append(JSON.parseObject(nameVal.getFieldValue(), PostApprovalFormTextValue.class).getText());
		}
		PostApprovalFormItem contactVal = getFormFieldDTO(GeneralFormDataSourceType.USER_PHONE.getCode(),values);
		if(null != contactVal){
			if(contentBuffer.length()>1)
				contentBuffer.append("\n");
			GeneralFormFieldDTO dto = getFieldDTO(contactVal.getFieldName(),fieldDTOs); 
			contentBuffer.append(dto.getFieldDisplayName());
			contentBuffer.append(" : ");
			contentBuffer.append(JSON.parseObject(contactVal.getFieldValue(), PostApprovalFormTextValue.class).getText());
		}
		PostApprovalFormItem entpriseVal = getFormFieldDTO(GeneralFormDataSourceType.USER_COMPANY.getCode(),values);
		if(null != entpriseVal){
			if(contentBuffer.length()>1)
				contentBuffer.append("\n");
			GeneralFormFieldDTO dto = getFieldDTO(entpriseVal.getFieldName(),fieldDTOs); 
			contentBuffer.append(dto.getFieldDisplayName());
			contentBuffer.append(" : ");
			contentBuffer.append(JSON.parseObject(entpriseVal.getFieldValue(), PostApprovalFormTextValue.class).getText());
		}
		PostApprovalFormItem sourceVal = getFormFieldDTO(GeneralFormDataSourceType.SOURCE_ID.getCode(),values);
		if(null != sourceVal){
			if(contentBuffer.length()>1)
				contentBuffer.append("\n");
			Long yellowPageId = Long.valueOf(JSON.parseObject(sourceVal.getFieldValue(), PostApprovalFormTextValue.class).getText());
			ServiceAlliances  yellowPage = yellowPageProvider.findServiceAllianceById(yellowPageId,null,null);
			contentBuffer.append("服务机构");
			contentBuffer.append(" : ");
			contentBuffer.append(yellowPage.getName());

			request.setServiceAllianceId(yellowPageId);
			request.setType(yellowPage.getCategoryId());
			
		}
		flowCase.setContent(contentBuffer.toString());
		
		//服务联盟加一个申请
		 
		request.setJumpType(2L);
		request.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime())); 
		request.setCreatorName(JSON.parseObject(nameVal.getFieldValue(), PostApprovalFormTextValue.class).getText());
		 
		request.setCreatorMobile(JSON.parseObject(contactVal.getFieldValue(), PostApprovalFormTextValue.class).getText());
//		request.setCreatorOrganizationId(JSON.parseObject(nameVal.getFieldValue(), PostApprovalFormTextValue.class).getText());
		request.setOwnerType(OwnerType.COMMUNITY.getCode());
		request.setOwnerId(flowCase.getProjectId());
		request.setFlowCaseId(flowCase.getId());
		request.setId(flowCase.getId());
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
		
		//电话
		e = new FlowCaseEntity();
		val = this.generalApprovalValProvider.getGeneralApprovalByFlowCaseAndName(flowCase.getId(),
				GeneralFormDataSourceType.USER_PHONE.getCode()); 
		dto = getFieldDTO(val.getFieldName(),fieldDTOs); 
		e.setKey(dto.getFieldDisplayName());
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode()); 
		e.setValue(JSON.parseObject(val.getFieldStr3(), PostApprovalFormTextValue.class).getText());
		
		//企业
		e = new FlowCaseEntity();
		val = this.generalApprovalValProvider.getGeneralApprovalByFlowCaseAndName(flowCase.getId(),
				GeneralFormDataSourceType.USER_COMPANY.getCode()); 
		dto = getFieldDTO(val.getFieldName(),fieldDTOs); 
		e.setKey(dto.getFieldDisplayName());
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode()); 
		e.setValue(JSON.parseObject(val.getFieldStr3(), PostApprovalFormTextValue.class).getText());
		
		//申请类型
		e = new FlowCaseEntity(); 
		GeneralApproval ga = this.generalApprovalProvider.getGeneralApprovalById(val.getApprovalId());

		e.setKey("申请类型");
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode()); 
		e.setValue(ga.getApprovalName());
		//申请来源

		e = new FlowCaseEntity(); 
		e.setKey("申请来源");
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode()); 
		ServiceModule serviceModule = serviceModuleProvider.findServiceModuleById(ga.getModuleId());
		e.setValue(serviceModule.getName());
		//服务机构

		e = new FlowCaseEntity();
		val = this.generalApprovalValProvider.getGeneralApprovalByFlowCaseAndName(flowCase.getId(),
				GeneralFormDataSourceType.SOURCE_ID.getCode()); 
		e.setKey("服务机构");
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode()); 
		Long yellowPageId = Long.valueOf(JSON.parseObject(val.getFieldStr3(), PostApprovalFormTextValue.class).getText());
		ServiceAlliances  yellowPage = yellowPageProvider.findServiceAllianceById(yellowPageId,null,null);
		e.setValue(yellowPage.getName());
		
		//后面跟自定义模块--通用父类方法
		entities.addAll(super.onFlowCaseDetailRender(flowCase, flowUserType));
		return entities;
	}

	@Override
	public void onFlowCaseCreated(FlowCase flowCase) {
		
	}
}
