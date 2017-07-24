package com.everhomes.general_approval;

import java.util.ArrayList;
import java.util.List;
import java.util.spi.LocaleServiceProvider;

import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormProvider;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.contentserver.ContentServerResource;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowModuleInfo;
import com.everhomes.flow.FlowModuleListener;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.module.ServiceModule;
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
import com.everhomes.rest.general_approval.GeneralFormSubformDTO;
import com.everhomes.rest.general_approval.PostApprovalFormContactValue;
import com.everhomes.rest.general_approval.PostApprovalFormFileDTO;
import com.everhomes.rest.general_approval.PostApprovalFormFileValue;
import com.everhomes.rest.general_approval.PostApprovalFormSubformItemValue;
import com.everhomes.rest.general_approval.PostApprovalFormSubformValue;
import com.everhomes.rest.general_approval.PostApprovalFormImageValue;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.rest.general_approval.PostApprovalFormTextValue;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.Tuple;
@Component
public class GeneralApprovalFlowModuleListener implements FlowModuleListener {
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
	@Autowired
	protected LocaleStringService localeStringService;
	
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
	public void onFlowCaseCreating(FlowCase flowCase) {
		// 服务联盟的审批拼接工作流 content字符串
		flowCase.setContent(null);
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
		entities.addAll(onFlowCaseCustomDetailRender(flowCase, flowUserType));
		return entities;
	}

	public List<FlowCaseEntity> onFlowCaseCustomDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
		List<FlowCaseEntity> entities = new ArrayList<>();
		if (flowCase.getReferType().equals(FlowReferType.APPROVAL.getCode())) { 
			List<GeneralApprovalVal> vals = this.generalApprovalValProvider
					.queryGeneralApprovalValsByFlowCaseId(flowCase.getId());
			GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginIdAndVersion(
					vals.get(0).getFormOriginId(), vals.get(0).getFormVersion());
			// 模板设定的字段DTOs
			List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(),
					GeneralFormFieldDTO.class);
			processEntities(entities, vals,fieldDTOs);
			
		}
		return entities;
	}
	
	private void processEntities(
			List<FlowCaseEntity> entities,List<GeneralApprovalVal> vals ,List<GeneralFormFieldDTO> fieldDTOs ){
		
		for (GeneralApprovalVal val : vals) {
			try{
				if (!DEFUALT_FIELDS.contains(val.getFieldName())) {
					// 不在默认fields的就是自定义字符串，组装这些
					FlowCaseEntity e = new FlowCaseEntity();
					GeneralFormFieldDTO dto = getFieldDTO(val.getFieldName(),fieldDTOs); 
					if(null == dto ){
						LOGGER.error("+++++++++++++++++++error! cannot fand this field  name :["+val.getFieldName()+"] \n form   "+JSON.toJSONString(fieldDTOs));
						continue;
					}
						
					e.setKey(dto.getFieldDisplayName()==null?dto.getFieldName():dto.getFieldDisplayName()); 
					switch (GeneralFormFieldType.fromCode(val.getFieldType())) {
					case SINGLE_LINE_TEXT:
					case NUMBER_TEXT:
					case DATE:
					case DROP_BOX :
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
						if (null == fileValue || fileValue.getFiles() ==null )
							break;
						List<FlowCaseFileDTO> files = new ArrayList<>();
						for(PostApprovalFormFileDTO dto2 : fileValue.getFiles()){
							FlowCaseFileDTO fileDTO = new FlowCaseFileDTO(); 
							String url = this.contentServerService.parserUri(dto2.getUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId()); 
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
					case SUBFORM:
						
						PostApprovalFormSubformValue subFormValue = JSON.parseObject(val.getFieldStr3(), PostApprovalFormSubformValue.class);
						//取出设置的子表单fields
						GeneralFormSubformDTO subFromExtra = JSON.parseObject(dto.getFieldExtra(), GeneralFormSubformDTO.class) ;
						//给子表单计数从1开始
						int formCount = 1;
						//循环取出每一个子表单值
						for(PostApprovalFormSubformItemValue subForm1:subFormValue.getForms()){
							e = new FlowCaseEntity(); 
							e.setKey(dto.getFieldDisplayName()==null?dto.getFieldName():dto.getFieldDisplayName()); 
							e.setEntityType(FlowCaseEntityType.LIST.getCode()); 
							e.setValue(formCount++ +"");
							entities.add(e);
							List<GeneralApprovalVal> subVals = new ArrayList<>();
							//循环取出一个子表单的每一个字段值
							for(PostApprovalFormItem subFromValue1:subForm1.getValues()){
								GeneralApprovalVal obj =  new GeneralApprovalVal(); 
								obj.setFieldName(subFromValue1.getFieldName());
								obj.setFieldType(subFromValue1.getFieldType());
								obj.setFieldStr3(subFromValue1.getFieldValue());
								subVals.add(obj);
							}
							List<FlowCaseEntity> subSingleEntities = new ArrayList<>();
							processEntities(subSingleEntities, subVals,subFromExtra.getFormFields());
							entities.addAll(subSingleEntities);
						}
						break;
					case CONTACT:
						//企业联系人
						PostApprovalFormContactValue contactValue = JSON.parseObject(val.getFieldStr3(), PostApprovalFormContactValue.class);
						e = new FlowCaseEntity();  
						e.setKey(localeStringService.getLocalizedString("general_approval.contact.key", "1", "zh_CN", "企业"));  
						e.setEntityType(FlowCaseEntityType.LIST.getCode()); 
						e.setValue(contactValue.getEnterpriseName());
						entities.add(e);

						e = new FlowCaseEntity();  
						e.setKey(localeStringService.getLocalizedString("general_approval.contact.key", "2", "zh_CN", "楼栋-门牌"));  
						e.setEntityType(FlowCaseEntityType.LIST.getCode()); 
						String addresses = "";
						
						if(null != contactValue.getAddresses() && contactValue.getAddresses().size()>0)
							addresses = StringUtils.join(contactValue.getAddresses(),",");
						e.setValue(addresses);
						entities.add(e);

						e = new FlowCaseEntity();  
						e.setKey(localeStringService.getLocalizedString("general_approval.contact.key", "3", "zh_CN", "联系人"));  
						e.setEntityType(FlowCaseEntityType.LIST.getCode()); 
						e.setValue(contactValue.getContactName());
						entities.add(e);

						e = new FlowCaseEntity();  
						e.setKey(localeStringService.getLocalizedString("general_approval.contact.key", "4", "zh_CN", "联系方式"));  
						e.setEntityType(FlowCaseEntityType.LIST.getCode()); 
						e.setValue(contactValue.getContactNumber());
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
	public FlowModuleInfo initModule() {
        FlowModuleInfo moduleInfo = new FlowModuleInfo();
        ServiceModule module = serviceModuleProvider.findServiceModuleById(GeneralApprovalController.MODULE_ID);
        moduleInfo.setModuleName(module.getName());
        moduleInfo.setModuleId(GeneralApprovalController.MODULE_ID);
        return moduleInfo;
	}
     

	@Override
	public void onFlowCaseCreated(FlowCase flowCase) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFlowSMSVariableRender(FlowCaseState ctx, int templateId,
			List<Tuple<String, Object>> variables) {
		// TODO Auto-generated method stub
		
	}

}
