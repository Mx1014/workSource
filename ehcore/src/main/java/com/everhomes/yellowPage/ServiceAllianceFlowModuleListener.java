package com.everhomes.yellowPage;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.flow.FlowCase;
import com.everhomes.general_approval.GeneralApproval;
import com.everhomes.general_approval.GeneralApprovalFlowModuleListener;
import com.everhomes.general_approval.GeneralApprovalProvider;
import com.everhomes.general_approval.GeneralForm;
import com.everhomes.general_approval.GeneralFormProvider;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.general_approval.GeneralFormDataSourceType;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.PostApprovalFormCommand;
import com.everhomes.rest.general_approval.PostApprovalFormItem;

public class ServiceAllianceFlowModuleListener extends GeneralApprovalFlowModuleListener {

    @Autowired
    private YellowPageProvider yellowPageProvider;
    
	@Autowired
	private GeneralApprovalProvider generalApprovalProvider;
	@Autowired
	private GeneralFormProvider generalFormProvider;
	@Override
	public void onFlowCaseCreating(FlowCase flowCase) {
		// 服务联盟的审批拼接工作流 content字符串
		
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
			contentBuffer.append(nameVal.getFieldValue());
		}
		PostApprovalFormItem contactVal = getFormFieldDTO(GeneralFormDataSourceType.USER_PHONE.getCode(),values);
		if(null != contactVal){
			if(contentBuffer.length()>1)
				contentBuffer.append("\n");
			GeneralFormFieldDTO dto = getFieldDTO(contactVal.getFieldName(),fieldDTOs); 
			contentBuffer.append(dto.getFieldDisplayName());
			contentBuffer.append(" : ");
			contentBuffer.append(contactVal.getFieldValue());
		}
		PostApprovalFormItem entpriseVal = getFormFieldDTO(GeneralFormDataSourceType.USER_COMPANY.getCode(),values);
		if(null != entpriseVal){
			if(contentBuffer.length()>1)
				contentBuffer.append("\n");
			GeneralFormFieldDTO dto = getFieldDTO(entpriseVal.getFieldName(),fieldDTOs); 
			contentBuffer.append(dto.getFieldDisplayName());
			contentBuffer.append(" : ");
			contentBuffer.append(entpriseVal.getFieldValue());
		}
		PostApprovalFormItem sourceVal = getFormFieldDTO("sourceId",values);
		if(null != sourceVal){
			if(contentBuffer.length()>1)
				contentBuffer.append("\n");
			Long yellowPageId = Long.valueOf(sourceVal.getFieldValue());
			ServiceAlliances  yellowPage = yellowPageProvider.findServiceAllianceById(yellowPageId,null,null);
			contentBuffer.append("服务机构");
			contentBuffer.append(" : ");
			contentBuffer.append(yellowPage.getName());
		}
		flowCase.setContent(contentBuffer.toString());
	}

	private GeneralFormFieldDTO getFieldDTO(String fieldName, List<GeneralFormFieldDTO> fieldDTOs) {
		for (GeneralFormFieldDTO val : fieldDTOs ){
			if(val.getFieldName().equals(fieldName))
				return val;
		}
		return null;
	}

	private PostApprovalFormItem getFormFieldDTO(String string, List<PostApprovalFormItem> values) { 
		for (PostApprovalFormItem val : values ){
			if(val.getFieldName().equals(string))
				return val;
		}
		return null;
	}

	@Override
	public List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
		List<FlowCaseEntity> entities = super.onFlowCaseDetailRender(flowCase, flowUserType);
		//do somehting special
		return entities;
	}
}
