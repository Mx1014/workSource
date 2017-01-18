package com.everhomes.general_approval;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.types.FileList.FileName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.contentserver.ContentServerResource;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowModuleListener;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowCaseEntityType;
import com.everhomes.rest.flow.FlowCaseFileDTO;
import com.everhomes.rest.flow.FlowCaseFileValue;
import com.everhomes.rest.flow.FlowReferType;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.general_approval.GeneralFormDataSourceType;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldType;
import com.everhomes.rest.general_approval.PostApprovalFormFileDTO;
import com.everhomes.rest.general_approval.PostApprovalFormFileValue;
import com.everhomes.rest.general_approval.PostApprovalFormImageValue;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.rest.general_approval.PostApprovalFormTextValue;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;

public abstract class GeneralApprovalFlowModuleListener implements FlowModuleListener {
	protected static List<String> DEFUALT_FIELDS = new ArrayList<String>();

	private static final Logger LOGGER = LoggerFactory.getLogger(GeneralApprovalFlowModuleListener.class);
	@Autowired
	protected ContentServerService contentServerService;
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
			List<GeneralApprovalVal> vals = this.generalApprovalValProvider
					.queryGeneralApprovalValsByFlowCaseId(flowCase.getId());
			GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginIdAndVersion(
					vals.get(0).getFormOriginId(), vals.get(0).getFormVersion());
			List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(),
					GeneralFormFieldDTO.class);
			for (GeneralApprovalVal val : vals) {
				try{
					if (!DEFUALT_FIELDS.contains(val.getFieldName())) {
						// 不在默认fields的就是自定义字符串，组装这些
						FlowCaseEntity e = new FlowCaseEntity();
						GeneralFormFieldDTO dto = getFieldDTO(val.getFieldName(),fieldDTOs); 
						e.setKey(dto.getFieldDisplayName()==null?dto.getFieldName():dto.getFieldDisplayName());
						 
						switch (GeneralFormFieldType.fromCode(val.getFieldType())) {
						case SINGLE_LINE_TEXT:
							e.setEntityType(FlowCaseEntityType.LIST.getCode()); 
							e.setValue(JSON.parseObject(val.getFieldStr3(), PostApprovalFormTextValue.class).getText());
							entities.add(e);
							break;
						case MULTI_LINE_TEXT:
							e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode()); 
							e.setValue(JSON.parseObject(val.getFieldStr3(), PostApprovalFormTextValue.class).getText());
							entities.add(e);
							break;
						case IMAGE:
							e.setEntityType(FlowCaseEntityType.IMAGE.getCode()); 
							//工作流images怎么传
							PostApprovalFormImageValue imageValue = JSON.parseObject(val.getFieldStr3(), PostApprovalFormImageValue.class);
							for(String uriString : imageValue.getUris()){
								String url = this.contentServerService.parserUri(uriString, EntityType.USER.getCode(), UserContext.current().getUser().getId());
								e.setValue(url);
								FlowCaseEntity e2 = ConvertHelper.convert(e, FlowCaseEntity.class);
								entities.add(e2);
							}
							break;
						case FILE:
	//						e.setEntityType(FlowCaseEntityType.F.getCode()); 
							//TODO:工作流需要新增类型file
							e.setEntityType(FlowCaseEntityType.FILE.getCode()); 
							PostApprovalFormFileValue fileValue = JSON.parseObject(val.getFieldStr3(), PostApprovalFormFileValue.class);
							LOGGER.error("filevalue : "+JSON.toJSONString(fileValue));
							if (null == fileValue || fileValue.getFiles() ==null )
								break;
							List<FlowCaseFileDTO> files = new ArrayList<>();
							for(PostApprovalFormFileDTO dto2 : fileValue.getFiles()){
								FlowCaseFileDTO fileDTO = new FlowCaseFileDTO();
								LOGGER.error("BEGIN PARSE URI  DTO = ["+JSON.toJSONString(dto2)+"], userContext " +JSON.toJSONString(UserContext.current()));
								String url = this.contentServerService.parserUri(dto2.getUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId());
								LOGGER.error("file URL = "+url);
								ContentServerResource resource = contentServerService.findResourceByUri(dto2.getUri());
								fileDTO.setUrl(url);
								fileDTO.setFileName(dto2.getFileName());
								fileDTO.setFileSize(resource.getResourceSize());
								files.add(fileDTO);
							}
							FlowCaseFileValue value = new FlowCaseFileValue();
							value.setFiles(files);
							e.setValue(JSON.toJSONString(value));
							entities.add(e);
							break;
						case INTEGER_TEXT:
							e.setEntityType(FlowCaseEntityType.LIST.getCode()); 
							e.setValue(JSON.parseObject(val.getFieldStr3(), PostApprovalFormTextValue.class).getText());
							entities.add(e);
							break;
							
						}
						
					}
				}catch(NullPointerException e){
					LOGGER.error(" ********** 空指针错误  val = "+JSON.toJSONString(val), e);
				}catch(Exception e){
					LOGGER.error(" ********** 这是什么错误  = "+JSON.toJSONString(val), e);
					
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
