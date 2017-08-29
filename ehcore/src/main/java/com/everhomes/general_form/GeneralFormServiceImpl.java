package com.everhomes.general_form;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerResource;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowCaseEntityType;
import com.everhomes.rest.flow.FlowCaseFileDTO;
import com.everhomes.rest.flow.FlowCaseFileValue;
import com.everhomes.rest.general_approval.*;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.server.schema.Tables;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GeneralFormServiceImpl implements GeneralFormService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GeneralFormServiceImpl.class);

	@Autowired
	private GeneralFormProvider generalFormProvider;
	@Autowired
	private DbProvider dbProvider;
	@Autowired
	private GeneralFormValProvider generalFormValProvider;
	@Autowired
	private ContentServerService contentServerService;

	@Override
	public GeneralFormDTO getTemplateByFormId(GetTemplateByFormIdCommand cmd) {
		//
		GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginId(cmd.getFormId());
		if(form == null )
			throw RuntimeErrorException.errorWith(GeneralApprovalServiceErrorCode.SCOPE, 
					GeneralApprovalServiceErrorCode.ERROR_FORM_NOTFOUND, "form not found");

		GeneralFormDTO dto = ConvertHelper.convert(form, GeneralFormDTO.class);
//		form.setFormVersion(form.getFormVersion());
		List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
//		//增加一个隐藏的field 用于存放sourceId
//		GeneralFormFieldDTO sourceIdField = new GeneralFormFieldDTO();
//		sourceIdField.setDataSourceType(GeneralFormDataSourceType.SOURCE_ID.getCode());
//		sourceIdField.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
//		sourceIdField.setFieldName(GeneralFormDataSourceType.SOURCE_ID.getCode());
//		sourceIdField.setRequiredFlag(NormalFlag.NEED.getCode());
//		sourceIdField.setDynamicFlag(NormalFlag.NEED.getCode());
//		sourceIdField.setVisibleType(GeneralFormDataVisibleType.HIDDEN.getCode());
//		fieldDTOs.add(sourceIdField);
		
//		GeneralFormFieldDTO organizationIdField = new GeneralFormFieldDTO();
//		organizationIdField.setDataSourceType(GeneralFormDataSourceType.ORGANIZATION_ID.getCode());
//		organizationIdField.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
//		organizationIdField.setFieldName(GeneralFormDataSourceType.ORGANIZATION_ID.getCode());
//		organizationIdField.setRequiredFlag(NormalFlag.NEED.getCode());
//		organizationIdField.setDynamicFlag(NormalFlag.NEED.getCode());
//		organizationIdField.setVisibleType(GeneralFormDataVisibleType.HIDDEN.getCode());
//		fieldDTOs.add(organizationIdField);

		dto.setFormFields(fieldDTOs);
		return dto;
	}

	@Override
	public GeneralFormDTO   getTemplateBySourceId(GetTemplateBySourceIdCommand cmd) {
		GeneralFormModuleHandler handler = getOrderHandler(cmd.getSourceType());

		return handler.getTemplateBySourceId(cmd);
	}

	@Override
	public PostGeneralFormDTO postGeneralForm(PostGeneralFormCommand cmd) {
		GeneralFormModuleHandler handler = getOrderHandler(cmd.getSourceType());
		PostGeneralFormDTO dto = handler.postGeneralForm(cmd);

		return dto;
	}

	private GeneralFormModuleHandler getOrderHandler(String type) {
		String handler = GeneralFormModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX + type;
		return PlatformContext.getComponent(handler);
	}

	@Override
	public void addGeneralFormValues(addGeneralFormValuesCommand cmd) {
		// 把values 存起来
		if (null != cmd.getValues()) {
			GeneralForm form = generalFormProvider.getActiveGeneralFormByOriginId(cmd.getGeneralFormId());

			dbProvider.execute(status -> {
				if (form.getStatus().equals(GeneralFormStatus.CONFIG.getCode())) {
					// 使用表单/审批 注意状态 config
					form.setStatus(GeneralFormStatus.RUNNING.getCode());
					generalFormProvider.updateGeneralForm(form);
				}

				for (PostApprovalFormItem val : cmd.getValues()) {
					GeneralFormVal obj = new GeneralFormVal();
					//与表单信息一致
					obj.setNamespaceId(form.getNamespaceId());
					obj.setOrganizationId(form.getOrganizationId());
					obj.setOwnerId(form.getOwnerId());
					obj.setOwnerType(form.getOwnerType());
					obj.setModuleId(form.getModuleId());
					obj.setModuleType(form.getModuleType());
					obj.setFormOriginId(form.getFormOriginId());
					obj.setFormVersion(form.getFormVersion());

					obj.setSourceType(cmd.getSourceType());
					obj.setSourceId(cmd.getSourceId());
					obj.setFieldName(val.getFieldName());
					obj.setFieldType(val.getFieldType());
					obj.setFieldValue(val.getFieldValue());
					generalFormValProvider.createGeneralFormVal(obj);
				}
				return null;
			});
		}
	}

	@Override
	public List<PostApprovalFormItem> getGeneralFormValues(GetGeneralFormValuesCommand cmd) {
		List<PostApprovalFormItem> result = new ArrayList<>();
		List<GeneralFormVal> vals = generalFormValProvider.queryGeneralFormVals(cmd.getSourceType(), cmd.getSourceId());

		if (null != vals && vals.size() != 0) {
			GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginIdAndVersion(
					vals.get(0).getFormOriginId(), vals.get(0).getFormVersion());
			LOGGER.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!, form={}, id={}, version={}", form, vals.get(0).getFormOriginId(), vals.get(0).getFormVersion());
			List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(),
					GeneralFormFieldDTO.class);

			if (null == cmd.getOriginFieldFlag()) {
				cmd.setOriginFieldFlag(NormalFlag.NONEED.getCode());
			}

			for (GeneralFormVal val : vals) {
				try{

					GeneralFormFieldDTO dto = getFieldDTO(val.getFieldName(),fieldDTOs);
					if(null == dto ){
						LOGGER.error("+++++++++++++++++++error! cannot fand this field  name :["+val.getFieldName()+"] \n form   "+JSON.toJSONString(fieldDTOs));
						continue;
					}
					if (StringUtils.isEmpty(val.getFieldValue())) {
						continue;
					}
					PostApprovalFormItem formVal = new PostApprovalFormItem();

					if (NormalFlag.NEED.getCode() == cmd.getOriginFieldFlag()) {
						formVal.setFieldName(dto.getFieldName());
						formVal.setFieldDisplayName(dto.getFieldDisplayName());
					}else {
						formVal.setFieldName(dto.getFieldDisplayName() == null ? dto.getFieldName() : dto.getFieldDisplayName());
					}
					formVal.setFieldType(val.getFieldType());

					GeneralFormFieldType fieldType = GeneralFormFieldType.fromCode(val.getFieldType());
					if (null != fieldType) {
						switch (GeneralFormFieldType.fromCode(val.getFieldType())) {
							case SINGLE_LINE_TEXT:
							case NUMBER_TEXT:
							case DATE:
							case DROP_BOX :
								if (NormalFlag.NEED.getCode() == cmd.getOriginFieldFlag()) {
									formVal.setFieldValue(val.getFieldValue());
								}else{
									formVal.setFieldValue(JSON.parseObject(val.getFieldValue(), PostApprovalFormTextValue.class).getText());
								}
								result.add(formVal);
								break;
							case MULTI_LINE_TEXT:
								if (NormalFlag.NEED.getCode() == cmd.getOriginFieldFlag()) {
									formVal.setFieldValue(val.getFieldValue());
								}else{
									formVal.setFieldValue(JSON.parseObject(val.getFieldValue(), PostApprovalFormTextValue.class).getText());
								}
								result.add(formVal);
								break;
							case IMAGE:
								//工作流images怎么传
								PostApprovalFormImageValue imageValue = JSON.parseObject(val.getFieldValue(), PostApprovalFormImageValue.class);
								List<String> urls = new ArrayList<>();
								for(String uriString : imageValue.getUris()){
									String url = this.contentServerService.parserUri(uriString, EntityType.USER.getCode(), UserContext.current().getUser().getId());
									urls.add(url);
								}
								imageValue.setUrls(urls);
								formVal.setFieldValue(imageValue.toString());
//							PostApprovalFormItem formVal2 = ConvertHelper.convert(formVal, PostApprovalFormItem.class);
								result.add(formVal);
								break;
							case FILE:
								//TODO:工作流需要新增类型file
								PostApprovalFormFileValue fileValue = JSON.parseObject(val.getFieldValue(), PostApprovalFormFileValue.class);
								if (null == fileValue || fileValue.getFiles() == null )
									break;
								List<FlowCaseFileDTO> files = new ArrayList<>();
								for(PostApprovalFormFileDTO dto2 : fileValue.getFiles()){
									FlowCaseFileDTO fileDTO = new FlowCaseFileDTO();
									String url = this.contentServerService.parserUri(dto2.getUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId());
									ContentServerResource resource = contentServerService.findResourceByUri(dto2.getUri());
									fileDTO.setUri(dto2.getUri());
									fileDTO.setUrl(url);
									fileDTO.setFileName(dto2.getFileName());
									fileDTO.setFileSize(resource.getResourceSize());
									files.add(fileDTO);
								}
								FlowCaseFileValue value = new FlowCaseFileValue();
								value.setFiles(files);
								formVal.setFieldValue(JSON.toJSONString(value));
								result.add(formVal);
								break;
							case INTEGER_TEXT:
								if (NormalFlag.NEED.getCode() == cmd.getOriginFieldFlag()) {
									formVal.setFieldValue(val.getFieldValue());
								}else{
									formVal.setFieldValue(JSON.parseObject(val.getFieldValue(), PostApprovalFormTextValue.class).getText());
								}
								result.add(formVal);
								break;
							case SUBFORM:

//								PostApprovalFormSubformValue subFormValue = JSON.parseObject(val.getFieldValue(), PostApprovalFormSubformValue.class);
//								//取出设置的子表单fields
//								GeneralFormSubformDTO subFromExtra = JSON.parseObject(dto.getFieldExtra(), GeneralFormSubformDTO.class) ;
//								//给子表单计数从1开始
//								int formCount = 1;
//								//循环取出每一个子表单值
//								for(PostApprovalFormSubformItemValue subForm1:subFormValue.getForms()){
//									e = new FlowCaseEntity();
//									e.setKey(dto.getFieldDisplayName()==null?dto.getFieldName():dto.getFieldDisplayName());
//									e.setEntityType(FlowCaseEntityType.LIST.getCode());
//									e.setValue(formCount++ +"");
//									entities.add(e);
//									List<GeneralApprovalVal> subVals = new ArrayList<>();
//									//循环取出一个子表单的每一个字段值
//									for(PostApprovalFormItem subFromValue1:subForm1.getValues()){
//										GeneralApprovalVal obj =  new GeneralApprovalVal();
//										obj.setFieldName(subFromValue1.getFieldName());
//										obj.setFieldType(subFromValue1.getFieldType());
//										obj.setFieldStr3(subFromValue1.getFieldValue());
//										subVals.add(obj);
//									}
//									List<FlowCaseEntity> subSingleEntities = new ArrayList<>();
//									processEntities(subSingleEntities, subVals,subFromExtra.getFormFields());
//									entities.addAll(subSingleEntities);
//								}
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

		return result;
	}

	@Override
	public List<FlowCaseEntity> getGeneralFormFlowEntities(GetGeneralFormValuesCommand cmd) {
		return getGeneralFormFlowEntities(cmd, false);
	}
		
	@Override
	public List<FlowCaseEntity> getGeneralFormFlowEntities(GetGeneralFormValuesCommand cmd, boolean showDefaultFields) {

		List<GeneralFormVal> vals = generalFormValProvider.queryGeneralFormVals(cmd.getSourceType(), cmd.getSourceId());

		List<FlowCaseEntity> entities = new ArrayList<>();

		if (null != vals && vals.size() != 0) {
			GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginIdAndVersion(
					vals.get(0).getFormOriginId(), vals.get(0).getFormVersion());
			List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(),
					GeneralFormFieldDTO.class);

			processFlowEntities(entities, vals, fieldDTOs, showDefaultFields);
		}
		return entities;
	}

	@Override
	public void processFlowEntities(List<FlowCaseEntity> entities, List<GeneralFormVal> vals, List<GeneralFormFieldDTO> fieldDTOs) {
		processFlowEntities(entities, vals, fieldDTOs, false);
	}
	
	@Override
	public void processFlowEntities(List<FlowCaseEntity> entities, List<GeneralFormVal> vals, List<GeneralFormFieldDTO> fieldDTOs, boolean showDefaultFields) {

		List<String> defaultFields = Arrays.stream(GeneralFormDataSourceType.values()).map(GeneralFormDataSourceType::getCode)
				.collect(Collectors.toList());

		for (GeneralFormVal val : vals) {
			try{
				if (showDefaultFields || !defaultFields.contains(val.getFieldName())) {
					// 不在默认fields的就是自定义字符串，组装这些
					GeneralFormFieldDTO dto = getFieldDTO(val.getFieldName(), fieldDTOs);
					if(null == dto || GeneralFormDataVisibleType.fromCode(dto.getVisibleType()) == GeneralFormDataVisibleType.HIDDEN){
						LOGGER.error("+++++++++++++++++++error! cannot fand this field  name :["+val.getFieldName()+"] \n form   "+JSON.toJSONString(fieldDTOs));
						continue;
					}

					entities.addAll(resolveFormVal(dto, val));

				}
			}catch(NullPointerException e){
				LOGGER.error(" ********** 空指针错误  val = "+JSON.toJSONString(val), e);
			}catch(Exception e){
				LOGGER.error(" ********** 这是什么错误  = "+JSON.toJSONString(val), e);

			}
		}
	}
	@Override
	public List<FlowCaseEntity> resolveFormVal(GeneralFormFieldDTO dto, GeneralFormVal val) {

		List<FlowCaseEntity> entities = new ArrayList<>();
		FlowCaseEntity e = new FlowCaseEntity();

		e.setKey(dto.getFieldDisplayName()==null?dto.getFieldName():dto.getFieldDisplayName());

		switch (GeneralFormFieldType.fromCode(val.getFieldType())) {
			case SINGLE_LINE_TEXT:
			case NUMBER_TEXT:
			case DATE:
			case DROP_BOX :
				e.setEntityType(FlowCaseEntityType.LIST.getCode());
				e.setValue(JSON.parseObject(val.getFieldValue(), PostApprovalFormTextValue.class).getText());
				entities.add(e);
				break;
			case MULTI_LINE_TEXT:
				e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
				e.setValue(JSON.parseObject(val.getFieldValue(), PostApprovalFormTextValue.class).getText());
				entities.add(e);
				break;
			case IMAGE:
				e.setEntityType(FlowCaseEntityType.IMAGE.getCode());
				//工作流images怎么传
				PostApprovalFormImageValue imageValue = JSON.parseObject(val.getFieldValue(), PostApprovalFormImageValue.class);
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
				PostApprovalFormFileValue fileValue = JSON.parseObject(val.getFieldValue(), PostApprovalFormFileValue.class);
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
				e.setValue(JSON.parseObject(val.getFieldValue(), PostApprovalFormTextValue.class).getText());
				entities.add(e);
				break;
			case SUBFORM:

				PostApprovalFormSubformValue subFormValue = JSON.parseObject(val.getFieldValue(), PostApprovalFormSubformValue.class);
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
					List<GeneralFormVal> subVals = new ArrayList<>();
					//循环取出一个子表单的每一个字段值
					for(PostApprovalFormItem subFromValue1:subForm1.getValues()){
						GeneralFormVal obj =  new GeneralFormVal();
						obj.setFieldName(subFromValue1.getFieldName());
						obj.setFieldType(subFromValue1.getFieldType());
						obj.setFieldValue(subFromValue1.getFieldValue());
						subVals.add(obj);
					}
					List<FlowCaseEntity> subSingleEntities = new ArrayList<>();
					processFlowEntities(subSingleEntities, subVals,subFromExtra.getFormFields());
					entities.addAll(subSingleEntities);
				}
				break;
		}

		return entities;
	}

	private GeneralFormFieldDTO getFieldDTO(String fieldName, List<GeneralFormFieldDTO> fieldDTOs) {
		for (GeneralFormFieldDTO val : fieldDTOs) {
			if (val.getFieldName().equals(fieldName))
				return val;
		}
		return null;
	}

	@Override
	public GeneralFormDTO createGeneralForm(CreateApprovalFormCommand cmd) {

		GeneralForm form = ConvertHelper.convert(cmd, GeneralForm.class);
		form.setTemplateType(GeneralFormTemplateType.DEFAULT_JSON.getCode());
		form.setStatus(GeneralFormStatus.CONFIG.getCode());
		form.setNamespaceId(UserContext.getCurrentNamespaceId());
		form.setFormVersion(0L);
		form.setTemplateText(JSON.toJSONString(cmd.getFormFields()));
		this.generalFormProvider.createGeneralForm(form);

		//  创建字段组(此时表单已经建立，故在建立字段组时即可同步)
		if (cmd.getFormGroups() != null) {
			for(GeneralFormGroupDTO dto : cmd.getFormGroups()){
                createGeneralFormGroup(form,dto,cmd.getOwnerId(),cmd.getOwnerType(),cmd.getOrganizationId());
			}
		}
		return processGeneralFormDTO(form);
	}

	private GeneralFormDTO processGeneralFormDTO(GeneralForm form) {
		GeneralFormDTO dto = ConvertHelper.convert(form, GeneralFormDTO.class);
		List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
		dto.setFormFields(fieldDTOs);
		return dto;
	}

	private void createGeneralFormGroup(GeneralForm form, GeneralFormGroupDTO dto, Long ownerId, String ownerType, Long organizationId){
        CreateGeneralFormGroupCommand createCommand = new CreateGeneralFormGroupCommand();
        createCommand.setFormOriginId(form.getFormOriginId());
        createCommand.setFormVersion(form.getFormVersion());
        createCommand.setGroupName(dto.getFieldGroupName());
        createCommand.setOwnerId(ownerId);
        createCommand.setOwnerType(ownerType);
        createCommand.setOrganizationId(organizationId);
        createGeneralFormGroup(createCommand);
    }

	@Override
	public GeneralFormDTO updateGeneralForm(UpdateApprovalFormCommand cmd) {
		return this.dbProvider.execute((TransactionStatus status) -> {
			GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginId(cmd
					.getFormOriginId());
			if (null == form || form.getStatus().equals(GeneralFormStatus.INVALID.getCode()))
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
						ErrorCodes.ERROR_INVALID_PARAMETER, "form not found");

			if (form.getStatus().equals(GeneralFormStatus.CONFIG.getCode())) {
				// 如果是config状态的直接改,
				//这里更新老的form之后才把表单内容，才把内容设置到新表单里面 by dengs issue:12817
				form.setFormName(cmd.getFormName());
				form.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
				form.setTemplateText(JSON.toJSONString(cmd.getFormFields()));
				this.generalFormProvider.updateGeneralForm(form);
			} else if (form.getStatus().equals(GeneralFormStatus.RUNNING.getCode())) {
				// 如果是RUNNING状态的,置原form为失效,重新create一个版本+1的config状态的form
				form.setStatus(GeneralFormStatus.INVALID.getCode());
				form.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
				//这里更新老的form之后才把表单内容，才把内容设置到新表单里面 by dengs issue:12817
				this.generalFormProvider.updateGeneralForm(form);
				form.setFormName(cmd.getFormName());
				form.setTemplateText(JSON.toJSONString(cmd.getFormFields()));
				form.setFormVersion(form.getFormVersion() + 1);
				form.setStatus(GeneralFormStatus.CONFIG.getCode());
				form.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
				form.setUpdateTime(null);
				this.generalFormProvider.createGeneralForm(form);
			}

			//  对字段组进行修改
            if(cmd.getFormGroups() != null){
			    List<Long> groupIds = new ArrayList<>();
			    for(GeneralFormGroupDTO dto : cmd.getFormGroups()){
			        if(dto.getFieldGroupId()!=null){
                        //  若存在的则修改
                        syncGeneralFormGroupFormOriginId(form.getFormOriginId(),form.getFormVersion(),dto.getFieldGroupId(),dto.getFieldGroupName());
			            groupIds.add(dto.getFieldGroupId());
                    }else{
			            //  若不存在则新增
                        createGeneralFormGroup(form,dto,cmd.getOwnerId(),cmd.getOwnerType(),cmd.getOrganizationId());
                    }
                }
                //  删除用户删除的字段组
                generalFormProvider.deleteGeneralFormGroupsNotInIds(form.getFormOriginId(),cmd.getOrganizationId(),groupIds);
            }
			return processGeneralFormDTO(form);
		});
	}


    private void syncGeneralFormGroupFormOriginId(Long formOriginId, Long formVersion, Long fieldGroupId, String fieldGroupName) {
        GeneralFormGroups group = generalFormProvider.findGeneralFormGroupById(fieldGroupId);
        if (group != null) {
            group.setFormOriginId(formOriginId);
            group.setFormVersion(formVersion);
            group.setGroupName(fieldGroupName);
            generalFormProvider.updateGeneralFormGroup(group);
        }
    }

	/**
	 * 取状态不为失效的form
	 * */
	@Override
	public ListGeneralFormResponse listGeneralForms(ListGeneralFormsCommand cmd) {

		List<GeneralForm> forms = this.generalFormProvider.queryGeneralForms(new ListingLocator(),
				Integer.MAX_VALUE - 1, new ListingQueryBuilderCallback() {
					@Override
					public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
							SelectQuery<? extends Record> query) {
						query.addConditions(Tables.EH_GENERAL_FORMS.OWNER_ID.eq(cmd.getOwnerId()));
						query.addConditions(Tables.EH_GENERAL_FORMS.OWNER_TYPE.eq(cmd
								.getOwnerType()));
						if (cmd.getModuleId()!=null)
							query.addConditions(Tables.EH_GENERAL_FORMS.MODULE_ID.eq(cmd.getModuleId()));
						if (cmd.getModuleType()!=null)
							query.addConditions(Tables.EH_GENERAL_FORMS.MODULE_TYPE.eq(cmd.getModuleType()));
						query.addConditions(Tables.EH_GENERAL_FORMS.STATUS
								.ne(GeneralFormStatus.INVALID.getCode()));
						return query;
					}
				});
		ListGeneralFormResponse resp = new ListGeneralFormResponse();
		resp.setForms(forms.stream().map((r) -> {
			return processGeneralFormDTO(r);
		}).collect(Collectors.toList()));
		return resp;
	}

	@Override
	public void deleteGeneralFormById(GeneralFormIdCommand cmd) {
		// 删除是状态置为invalid
		this.generalFormProvider.invalidForms(cmd.getFormOriginId());
	}

	@Override
	public GeneralFormDTO getGeneralForm(GeneralFormIdCommand cmd) {
		GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginId(cmd
				.getFormOriginId());
		return processGeneralFormDTO(form);
	}

	@Override
	public void createGeneralFormGroup(CreateGeneralFormGroupCommand cmd){
		User user = UserContext.current().getUser();
		if(cmd.getGroupName()==null)
			return;
		GeneralFormGroups group = new GeneralFormGroups();
		group.setGroupName(cmd.getGroupName());
		group.setOrganizationId(cmd.getOrganizationId());
		group.setOwnerId(cmd.getOwnerId());
		group.setOwnerType(cmd.getOwnerType());
		group.setNamespaceId(user.getNamespaceId());
		group.setOperatorUid(user.getId());
		if (cmd.getFormOriginId() != null)
			group.setFormOriginId(cmd.getFormOriginId());
		if (cmd.getFormVersion() != null)
			group.setFormVersion(cmd.getFormVersion());
		generalFormProvider.createGeneralFormGroup(group);
	}

/*	@Override
	public void updateGeneralFormGroup(CreateGeneralFormGroupCommand cmd){


	}*/

	@Override
	public List<GeneralFormGroupDTO> listGeneralFormGroups(ListGeneralFormGroupsCommand cmd){
		List<GeneralFormGroupDTO> results = new ArrayList<>();
		List<GeneralFormGroups> groups = generalFormProvider.listGeneralFormGroups(UserContext.getCurrentNamespaceId(),cmd.getOrganizationId(),cmd.getFormOriginId());
		if(groups !=null){
			groups.forEach(r ->{
				GeneralFormGroupDTO dto = new GeneralFormGroupDTO();
				dto.setFieldGroupId(r.getId());
				dto.setFieldGroupName(r.getGroupName());
				results.add(dto);
			});
		}
		return results;
	}
}
