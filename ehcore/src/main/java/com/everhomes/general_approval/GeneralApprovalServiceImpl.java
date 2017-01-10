package com.everhomes.general_approval;

import java.sql.Timestamp;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowService;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rentalv2.RentalNotificationTemplateCode;
import com.everhomes.rentalv2.Rentalv2Controller;
import com.everhomes.rest.approval.ApprovalExceptionContent;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowButtonStatus;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.flow.FlowReferType;
import com.everhomes.rest.flow.GeneralModuleInfo;
import com.everhomes.rest.general_approval.ApprovalFormIdCommand;
import com.everhomes.rest.general_approval.CreateApprovalFormCommand;
import com.everhomes.rest.general_approval.CreateGeneralApprovalCommand;
import com.everhomes.rest.general_approval.GeneralApprovalDTO;
import com.everhomes.rest.general_approval.GeneralApprovalIdCommand;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.general_approval.GeneralFormDataSourceType;
import com.everhomes.rest.general_approval.GeneralFormDataVisibleType;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldType;
import com.everhomes.rest.general_approval.GeneralFormStatus;
import com.everhomes.rest.general_approval.GeneralFormTemplateType;
import com.everhomes.rest.general_approval.GetTemplateByApprovalIdCommand;
import com.everhomes.rest.general_approval.GetTemplateByApprovalIdResponse;
import com.everhomes.rest.general_approval.ListApprovalFormsCommand;
import com.everhomes.rest.general_approval.ListGeneralApprovalCommand;
import com.everhomes.rest.general_approval.ListGeneralApprovalResponse;
import com.everhomes.rest.general_approval.ListGeneralFormResponse;
import com.everhomes.rest.general_approval.PostApprovalFormCommand;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.rest.general_approval.UpdateApprovalFormCommand;
import com.everhomes.rest.general_approval.UpdateGeneralApprovalCommand;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.server.schema.Tables;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.google.zxing.Result;

@Component
public class GeneralApprovalServiceImpl implements GeneralApprovalService {
	@Autowired
	private GeneralApprovalValProvider generalApprovalValProvider;
	@Autowired
	private GeneralFormProvider generalFormProvider;
	@Autowired
	private GeneralApprovalProvider generalApprovalProvider;

	@Autowired
	private FlowService flowService;

	@Autowired
	private DbProvider dbProvider;

	@Override
	public GetTemplateByApprovalIdResponse getTemplateByApprovalId(
			GetTemplateByApprovalIdCommand cmd) {
		//
		GeneralApproval ga = this.generalApprovalProvider.getGeneralApprovalById(cmd
				.getApprovalId());
		GetTemplateByApprovalIdResponse response = ConvertHelper.convert(ga,
				GetTemplateByApprovalIdResponse.class);
		GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginId(ga
				.getFormOriginId());
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
		response.setFormFields(fieldDTOs);
		return response;
	}

	@Override
	public GetTemplateByApprovalIdResponse postApprovalForm(PostApprovalFormCommand cmd) {

		// TODO Auto-generated method stub
		return this.dbProvider.execute((TransactionStatus status) -> {
			Long userId = UserContext.current().getUser().getId();
			GeneralApproval ga = this.generalApprovalProvider.getGeneralApprovalById(cmd
					.getApprovalId());
			GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginId(ga
					.getFormOriginId());

			if (form.getStatus().equals(GeneralFormStatus.CONFIG.getCode())) {
				// 使用表单/审批 注意状态 config
				form.setStatus(GeneralFormStatus.RUNNING.getCode());
				this.generalFormProvider.updateGeneralForm(form);
			}
			Flow flow = flowService.getEnabledFlow(ga.getNamespaceId(), ga.getModuleId(),
					ga.getModuleType(), ga.getId(), FlowOwnerType.GENERAL_APPROVAL.getCode());

			CreateFlowCaseCommand cmd21 = new CreateFlowCaseCommand();
			cmd21.setApplyUserId(userId);
			// cmd21.setReferId(null);
			cmd21.setReferType(FlowReferType.APPROVAL.getCode());
			cmd21.setProjectId(ga.getProjectId());
			cmd21.setProjectType(ga.getProjectType()); 
			//把command作为json传到content里，给flowcase的listener进行处理
			cmd21.setContent(JSON.toJSONString(cmd));
			
			FlowCase flowCase = null;
			if(null == flow) {
				// 给他一个默认哑的flow
				GeneralModuleInfo gm = ConvertHelper.convert(ga, GeneralModuleInfo.class);
				flowCase = flowService.createDumpFlowCase(gm, cmd21);
			} else {
				cmd21.setFlowMainId(flow.getFlowMainId());
				cmd21.setFlowVersion(flow.getFlowVersion());
				flowCase = flowService.createFlowCase(cmd21);
			}
			
			// 把values 存起来
			for (PostApprovalFormItem val : cmd.getValues()) {
				GeneralApprovalVal obj = ConvertHelper.convert(ga, GeneralApprovalVal.class);
				obj.setFlowCaseId(flowCase.getId());
				obj.setFieldName(val.getFieldName());
				obj.setFieldType(val.getFieldType());
				obj.setFieldStr3(val.getFieldValue());
				this.generalApprovalValProvider.createGeneralApprovalVal(obj);
			}
			
			GetTemplateByApprovalIdResponse response =  ConvertHelper.convert(ga, GetTemplateByApprovalIdResponse.class);
			response.setFlowCaseId(flowCase.getId());
			List<GeneralFormFieldDTO> fieldDTOs = new ArrayList<GeneralFormFieldDTO>();
			fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
			response.setFormFields(fieldDTOs);
			response.setValues(cmd.getValues());
			return response;
		});
	}

	@Override
	public GeneralFormDTO createApprovalForm(CreateApprovalFormCommand cmd) {

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
	public GeneralFormDTO updateApprovalForm(UpdateApprovalFormCommand cmd) {
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
	public ListGeneralFormResponse listApprovalForms(ListApprovalFormsCommand cmd) {

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
	public void deleteApprovalFormById(ApprovalFormIdCommand cmd) {
		// 删除是状态置为invalid
		this.generalFormProvider.invalidForms(cmd.getFormOriginId());
	}

	@Override
	public GeneralApprovalDTO createGeneralApproval(CreateGeneralApprovalCommand cmd) {
		//
		GeneralApproval ga = ConvertHelper.convert(cmd, GeneralApproval.class);
		ga.setNamespaceId(UserContext.getCurrentNamespaceId());
		ga.setStatus(GeneralFormStatus.CONFIG.getCode());
		
		//新增加审批的时候可能并为设置 formId
//		GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginId(cmd
//				.getFormOriginId());
//		ga.setFormVersion(form.getFormVersion());// 目前这个值并没用到
		
		this.generalApprovalProvider.createGeneralApproval(ga);

		return processApproval(ga);
	}

	@Override
	public GeneralApprovalDTO updateGeneralApproval(UpdateGeneralApprovalCommand cmd) {
		GeneralApproval ga = this.generalApprovalProvider.getGeneralApprovalById(cmd
				.getApprovalId());

		if (null != cmd.getSupportType())
			ga.setSupportType(cmd.getSupportType());
		if (null != cmd.getFormOriginId())
			ga.setFormOriginId(cmd.getFormOriginId());
		if (null != cmd.getApprovalName())
			ga.setApprovalName(cmd.getApprovalName());
		this.generalApprovalProvider.updateGeneralApproval(ga);
		return processApproval(ga);
	}

	@Override
	public ListGeneralApprovalResponse listGeneralApproval(ListGeneralApprovalCommand cmd) {
		//
		List<GeneralApproval> gas = this.generalApprovalProvider.queryGeneralApprovals(new ListingLocator(),
				Integer.MAX_VALUE - 1, new ListingQueryBuilderCallback() {
					@Override
					public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
							SelectQuery<? extends Record> query) {
						query.addConditions(Tables.EH_GENERAL_APPROVALS.OWNER_ID.eq(cmd
								.getOwnerId()));
						query.addConditions(Tables.EH_GENERAL_APPROVALS.OWNER_TYPE.eq(cmd
								.getOwnerType()));
						query.addConditions(Tables.EH_GENERAL_APPROVALS.STATUS
								.ne(GeneralFormStatus.INVALID.getCode()));
						query.addConditions(Tables.EH_GENERAL_APPROVALS.MODULE_ID.eq(cmd
								.getModuleId()));
						query.addConditions(Tables.EH_GENERAL_APPROVALS.MODULE_TYPE.eq(cmd
								.getModuleType()));
						query.addConditions(Tables.EH_GENERAL_APPROVALS.PROJECT_ID.eq(cmd
								.getProjectId()));
						query.addConditions(Tables.EH_GENERAL_APPROVALS.PROJECT_TYPE.eq(cmd
								.getProjectType()));
						return query;
					}
				});

		ListGeneralApprovalResponse resp = new ListGeneralApprovalResponse();
		resp.setDtos(gas.stream().map((r) -> {
			return processApproval(r);
		}).collect(Collectors.toList()));
		return resp;
	}

	private GeneralApprovalDTO processApproval(GeneralApproval r) {
		GeneralApprovalDTO result = ConvertHelper.convert(r, GeneralApprovalDTO.class);
		// form name
		if(r.getFormOriginId() != null && !r.getFormOriginId().equals(0l)) {
			GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginId(r
					.getFormOriginId());
			if(form != null) {
				result.setFormName(form.getFormName());	
			}	
		}
		
		// flow
		Flow flow = flowService.getEnabledFlow(r.getNamespaceId(), r.getModuleId(),
				r.getModuleType(), r.getOwnerId(), r.getOwnerType());
		if (null != flow) {
			result.setFlowName(flow.getFlowName());
		}
		return result;
	}

	@Override
	public void deleteGeneralApproval(GeneralApprovalIdCommand cmd) {

		// 删除是状态置为invalid
		GeneralApproval ga = this.generalApprovalProvider.getGeneralApprovalById(cmd
				.getApprovalId());
		ga.setStatus(GeneralFormStatus.INVALID.getCode());
		this.generalApprovalProvider.updateGeneralApproval(ga);
	}

	@Override
	public GeneralFormDTO getApprovalForm(ApprovalFormIdCommand cmd) {
		GeneralForm form = this.generalFormProvider.getActiveGeneralFormByOriginId(cmd
				.getFormOriginId());
		return processGeneralFormDTO(form);
	}

}
