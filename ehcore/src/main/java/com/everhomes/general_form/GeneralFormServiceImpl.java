package com.everhomes.general_form;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.general_approval.GeneralApprovalServiceErrorCode;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.rest.general_form.*;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.server.schema.Tables;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GeneralFormServiceImpl implements GeneralFormService {

	@Autowired
	private GeneralFormProvider generalFormProvider;
	@Autowired
	private DbProvider dbProvider;

	@Override
	public GeneralFormDTO getTemplateByFormId(GetTemplateByFormIdCommand cmd) {
		//
		GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginId(cmd.getFormId());
		if(form == null )
			throw RuntimeErrorException.errorWith(GeneralApprovalServiceErrorCode.SCOPE, 
					GeneralApprovalServiceErrorCode.ERROR_FORM_NOTFOUND, "form not found");

		GeneralFormDTO dto = ConvertHelper.convert(form, GeneralFormDTO.class);
//		form.setFormVersion(form.getFormVersion());
		List<GeneralFormFieldDTO> fieldDTOs = new ArrayList<GeneralFormFieldDTO>();
		fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
		//增加一个隐藏的field 用于存放sourceId
		GeneralFormFieldDTO sourceIdField = new GeneralFormFieldDTO();
		sourceIdField.setDataSourceType(GeneralFormDataSourceType.SOURCE_ID.getCode());
		sourceIdField.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
		sourceIdField.setFieldName(GeneralFormDataSourceType.SOURCE_ID.getCode());
		sourceIdField.setRequiredFlag(NormalFlag.NEED.getCode());
		sourceIdField.setDynamicFlag(NormalFlag.NEED.getCode());
		sourceIdField.setVisibleType(GeneralFormDataVisibleType.HIDDEN.getCode());
		fieldDTOs.add(sourceIdField);
		
		GeneralFormFieldDTO organizationIdField = new GeneralFormFieldDTO();
		organizationIdField.setDataSourceType(GeneralFormDataSourceType.ORGANIZATION_ID.getCode());
		organizationIdField.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
		organizationIdField.setFieldName(GeneralFormDataSourceType.ORGANIZATION_ID.getCode());
		organizationIdField.setRequiredFlag(NormalFlag.NEED.getCode());
		organizationIdField.setDynamicFlag(NormalFlag.NEED.getCode());
		organizationIdField.setVisibleType(GeneralFormDataVisibleType.HIDDEN.getCode());
		fieldDTOs.add(organizationIdField);

		dto.setFormFields(fieldDTOs);
		return dto;
	}

	@Override
	public void addGeneralFormValues(addGeneralFormValuesCommand cmd) {
		// 把values 存起来
		for (PostApprovalFormItem val : cmd.getValues()) {
//			GeneralApprovalVal obj = ConvertHelper.convert(ga, GeneralApprovalVal.class);
//			obj.setApprovalId(ga.getId());
//			obj.setFormVersion(form.getFormVersion());
//			obj.setFlowCaseId(flowCase.getId());
//			obj.setFieldName(val.getFieldName());
//			obj.setFieldType(val.getFieldType());
//			obj.setFieldStr3(val.getFieldValue());
//			this.generalApprovalValProvider.createGeneralApprovalVal(obj);
		}
	}

	@Override
	public List<PostApprovalFormItem> getGeneralFormValues(getGeneralFormValuesCommand cmd) {
//		List<GeneralApprovalVal> vals = new ArrayList<>();
//		GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginIdAndVersion(
//				vals.get(0).getFormOriginId(), vals.get(0).getFormVersion());
//		List<GeneralFormFieldDTO> fieldDTOs = JSONObject.parseArray(form.getTemplateText(),
//				GeneralFormFieldDTO.class);
//		for (GeneralApprovalVal val : vals) {
//			try{
//				if (!DEFUALT_FIELDS.contains(val.getFieldName())) {
//					// 不在默认fields的就是自定义字符串，组装这些
//					FlowCaseEntity e = new FlowCaseEntity();
//					GeneralFormFieldDTO dto = getFieldDTO(val.getFieldName(),fieldDTOs);
//					if(null == dto ){
//						LOGGER.error("+++++++++++++++++++error! cannot fand this field  name :["+val.getFieldName()+"] \n form   "+JSON.toJSONString(fieldDTOs));
//						continue;
//					}
//
//					e.setKey(dto.getFieldDisplayName()==null?dto.getFieldName():dto.getFieldDisplayName());
//
//					switch (GeneralFormFieldType.fromCode(val.getFieldType())) {
//						case SINGLE_LINE_TEXT:
//							e.setEntityType(FlowCaseEntityType.LIST.getCode());
//							e.setValue(JSON.parseObject(val.getFieldStr3(), PostApprovalFormTextValue.class).getText());
//							entities.add(e);
//							break;
//						case MULTI_LINE_TEXT:
//							e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
//							e.setValue(JSON.parseObject(val.getFieldStr3(), PostApprovalFormTextValue.class).getText());
//							entities.add(e);
//							break;
//						case IMAGE:
//							e.setEntityType(FlowCaseEntityType.IMAGE.getCode());
//							//工作流images怎么传
//							PostApprovalFormImageValue imageValue = JSON.parseObject(val.getFieldStr3(), PostApprovalFormImageValue.class);
//							for(String uriString : imageValue.getUris()){
//								String url = this.contentServerService.parserUri(uriString, EntityType.USER.getCode(), UserContext.current().getUser().getId());
//								e.setValue(url);
//								FlowCaseEntity e2 = ConvertHelper.convert(e, FlowCaseEntity.class);
//								entities.add(e2);
//							}
//							break;
//						case FILE:
//							//						e.setEntityType(FlowCaseEntityType.F.getCode());
//							//TODO:工作流需要新增类型file
//							e.setEntityType(FlowCaseEntityType.FILE.getCode());
//							PostApprovalFormFileValue fileValue = JSON.parseObject(val.getFieldStr3(), PostApprovalFormFileValue.class);
//							if (null == fileValue || fileValue.getFiles() ==null )
//								break;
//							List<FlowCaseFileDTO> files = new ArrayList<>();
//							for(PostApprovalFormFileDTO dto2 : fileValue.getFiles()){
//								FlowCaseFileDTO fileDTO = new FlowCaseFileDTO();
//								String url = this.contentServerService.parserUri(dto2.getUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId());
//								ContentServerResource resource = contentServerService.findResourceByUri(dto2.getUri());
//								fileDTO.setUrl(url);
//								fileDTO.setFileName(dto2.getFileName());
//								fileDTO.setFileSize(resource.getResourceSize());
//								files.add(fileDTO);
//							}
//							FlowCaseFileValue value = new FlowCaseFileValue();
//							value.setFiles(files);
//							e.setValue(JSON.toJSONString(value));
//							entities.add(e);
//							break;
//						case INTEGER_TEXT:
//							e.setEntityType(FlowCaseEntityType.LIST.getCode());
//							e.setValue(JSON.parseObject(val.getFieldStr3(), PostApprovalFormTextValue.class).getText());
//							entities.add(e);
//							break;
//
//					}
//
//				}
//			}catch(NullPointerException e){
//				LOGGER.error(" ********** 空指针错误  val = "+JSON.toJSONString(val), e);
//			}catch(Exception e){
//				LOGGER.error(" ********** 这是什么错误  = "+JSON.toJSONString(val), e);
//
//			}
//		}
		return null;
	}

	@Override
	public GeneralFormDTO createGeneralForm(CreateGeneralFormCommand cmd) {

		GeneralForm form = ConvertHelper.convert(cmd, GeneralForm.class);
		form.setTemplateType(GeneralFormTemplateType.DEFAULT_JSON.getCode());
		form.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		form.setStatus(GeneralFormStatus.CONFIG.getCode());
		form.setNamespaceId(UserContext.getCurrentNamespaceId());
		form.setFormVersion(0L);
		form.setTemplateText(JSON.toJSONString(cmd.getFormFields()));
		this.generalFormProvider.createGeneralForm(form);
		return processGeneralFormDTO(form);
	}

	public GeneralFormDTO processGeneralFormDTO(GeneralForm form) {
		GeneralFormDTO dto = ConvertHelper.convert(form, GeneralFormDTO.class);
		List<GeneralFormFieldDTO> fieldDTOs = new ArrayList<GeneralFormFieldDTO>();
		fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
		dto.setFormFields(fieldDTOs);
		return dto;
	}

	@Override
	public GeneralFormDTO updateGeneralForm(UpdateGeneralFormCommand cmd) {
		return this.dbProvider.execute((TransactionStatus status) -> {
			GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginId(cmd
					.getFormOriginId());
			if (null == form || form.getStatus().equals(GeneralFormStatus.INVALID.getCode()))
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
						ErrorCodes.ERROR_INVALID_PARAMETER, "form not found");
			form.setFormName(cmd.getFormName());
			form.setTemplateText(JSON.toJSONString(cmd.getFormFields()));
			form.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			if (form.getStatus().equals(GeneralFormStatus.CONFIG.getCode())) {
				// 如果是config状态的直接改
				this.generalFormProvider.updateGeneralForm(form);
			} else if (form.getStatus().equals(GeneralFormStatus.RUNNING.getCode())) {
				// 如果是RUNNING状态的,置原form为失效,重新create一个版本+1的config状态的form
				form.setStatus(GeneralFormStatus.INVALID.getCode());
				this.generalFormProvider.updateGeneralForm(form);
				form.setFormVersion(form.getFormVersion() + 1);
				form.setStatus(GeneralFormStatus.CONFIG.getCode());
				this.generalFormProvider.createGeneralForm(form);
			}
			return processGeneralFormDTO(form);
		});
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

}
