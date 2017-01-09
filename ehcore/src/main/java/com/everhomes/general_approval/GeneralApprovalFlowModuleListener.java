package com.everhomes.general_approval;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowModuleInfo;
import com.everhomes.flow.FlowModuleListener;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.rentalv2.RentalNotificationTemplateCode;
import com.everhomes.rentalv2.RentalOrder;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowCaseEntityType;
import com.everhomes.rest.flow.FlowReferType;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.general_approval.GeneralFormDataSourceType;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldType;
import com.everhomes.rest.general_approval.PostApprovalFormFileValue;
import com.everhomes.rest.general_approval.PostApprovalFormImageValue;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.rest.general_approval.PostApprovalFormTextValue;
import com.everhomes.user.User;

public abstract class GeneralApprovalFlowModuleListener implements FlowModuleListener {
	protected static List<String> DEFUALT_FIELDS = new ArrayList<String>();

	@Autowired
	protected ServiceModuleProvider serviceModuleProvider;
	@Autowired
	protected GeneralApprovalProvider generalApprovalProvider;
	@Autowired
	protected GeneralApprovalValProvider generalApprovalValProvider;
	@Autowired
	protected GeneralFormProvider generalFormProvider;

	public GeneralApprovalFlowModuleListener() {
		for (GeneralFormDataSourceType value : GeneralFormDataSourceType.values()) {
			DEFUALT_FIELDS.add(value.getCode());
		}
	}

	protected PostApprovalFormItem getFormFieldDTO(String string, List<PostApprovalFormItem> values) {
		for (PostApprovalFormItem val : values) {
			if (val.getFieldName().equals(string))
				return val;
		}
		return null;
	}

	protected GeneralFormFieldDTO getFieldDTO(String fieldName, List<GeneralFormFieldDTO> fieldDTOs) {
		for (GeneralFormFieldDTO val : fieldDTOs) {
			if (val.getFieldName().equals(fieldName))
				return val;
		}
		return null;
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

	/**
	 * 组装自定义字符串
	 * */
	@Override
	public List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
		List<FlowCaseEntity> entities = new ArrayList<>();
		if (flowCase.getReferType().equals(FlowReferType.APPROVAL.getCode())) {
			// TODO
			List<GeneralApprovalVal> vals = this.generalApprovalValProvider
					.queryGeneralApprovalValsByFlowCaseId(flowCase.getId());
			GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginIdAndVersion(
					vals.get(0).getFormOriginId(), vals.get(0).getFormVersion());
			List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(),
					GeneralFormFieldDTO.class);
			for (GeneralApprovalVal val : vals) {
				if (!DEFUALT_FIELDS.contains(val)) {
					// 不在默认fields的就是自定义字符串，组装这些
					FlowCaseEntity e = new FlowCaseEntity();
					GeneralFormFieldDTO dto = getFieldDTO(val.getFieldName(),fieldDTOs); 
					e.setKey(dto.getFieldDisplayName()==null?dto.getFieldName():dto.getFieldDisplayName());
					 
					switch (GeneralFormFieldType.fromCode(val.getFieldType())) {
					case SINGLE_LINE_TEXT:
						e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode()); 
						e.setValue(JSON.parseObject(val.getFieldStr3(), PostApprovalFormTextValue.class).getText());
						break;
					case MULTI_LINE_TEXT:
						e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode()); 
						e.setValue(JSON.parseObject(val.getFieldStr3(), PostApprovalFormTextValue.class).getText());
						break;
					case IMAGE:
						e.setEntityType(FlowCaseEntityType.IMAGE.getCode()); 
						//工作流images怎么传
						PostApprovalFormImageValue imageValue = JSON.parseObject(val.getFieldStr3(), PostApprovalFormImageValue.class);
						break;
					case FILE:
//						e.setEntityType(FlowCaseEntityType.F.getCode()); 
						//工作流files怎么传
						PostApprovalFormFileValue fileValue = JSON.parseObject(val.getFieldStr3(), PostApprovalFormFileValue.class);
						break;
					case INTEGER_TEXT:
						e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode()); 
						e.setValue(JSON.parseObject(val.getFieldStr3(), PostApprovalFormTextValue.class).getText());
						break;
						
					}
					
					entities.add(e);
				}

			}
		}
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

}
