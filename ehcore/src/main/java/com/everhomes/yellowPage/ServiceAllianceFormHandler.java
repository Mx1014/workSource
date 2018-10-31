package com.everhomes.yellowPage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.bus.LocalEventBus;
import com.everhomes.bus.LocalEventContext;
import com.everhomes.bus.SystemEvent;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseProvider;
import com.everhomes.flow.FlowProvider;
import com.everhomes.flow.FlowService;
import com.everhomes.general_approval.GeneralApproval;
import com.everhomes.general_approval.GeneralApprovalFormHandler;
import com.everhomes.general_approval.GeneralApprovalProvider;
import com.everhomes.general_approval.GeneralApprovalVal;
import com.everhomes.general_approval.GeneralApprovalValProvider;
import com.everhomes.general_form.GeneralForm;
import com.everhomes.general_form.GeneralFormModuleHandler;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.general_form.GeneralFormVal;
import com.everhomes.general_form.GeneralFormValProvider;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowCaseStatus;
import com.everhomes.rest.flow.FlowDTO;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.flow.FlowReferType;
import com.everhomes.rest.flow.FlowStatusType;
import com.everhomes.rest.flow.GeneralModuleInfo;
import com.everhomes.rest.general_approval.GeneralApprovalServiceErrorCode;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.general_approval.GeneralFormDataSourceType;
import com.everhomes.rest.general_approval.GeneralFormDataVisibleType;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldType;
import com.everhomes.rest.general_approval.GeneralFormReminderCommand;
import com.everhomes.rest.general_approval.GeneralFormReminderDTO;
import com.everhomes.rest.general_approval.GeneralFormRenderType;
import com.everhomes.rest.general_approval.GeneralFormStatus;
import com.everhomes.rest.general_approval.GetTemplateByApprovalIdResponse;
import com.everhomes.rest.general_approval.GetTemplateBySourceIdCommand;
import com.everhomes.rest.general_approval.PostApprovalFormCommand;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.rest.general_approval.PostGeneralFormDTO;
import com.everhomes.rest.general_approval.PostGeneralFormValCommand;
import com.everhomes.rest.rentalv2.NormalFlag;
import com.everhomes.server.schema.tables.pojos.EhFlowCases;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(GeneralFormModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX
		+ YellowPageService.SERVICE_ALLIANCE_HANDLER_NAME)
public class ServiceAllianceFormHandler implements GeneralFormModuleHandler {

	private final long SERVICE_ALLIANCE_MODULE_ID = 40500L;

	@Autowired
	private GeneralApprovalProvider generalApprovalProvider;

	@Autowired
	private GeneralFormProvider generalFormProvider;

	@Autowired
	private GeneralFormValProvider generalFormValProvider;

	@Autowired
	private FlowService flowService;

	@Autowired
	private FlowCaseProvider flowCaseProvider;

	@Autowired
	private YellowPageProvider yellowPageProvider;

	@Autowired
	private DbProvider dbProvider;
	
	@Autowired
	private FlowProvider flowProvider;

	@Override
	public PostGeneralFormDTO postGeneralFormVal(PostGeneralFormValCommand cmd) {
		User user = UserContext.current().getUser();

		ServiceAlliances sa = yellowPageProvider.findServiceAllianceById(cmd.getSourceId(), null, null);
		GeneralForm form = generalFormProvider.getActiveGeneralFormByOriginId(sa.getFormId());
		Long currentOrganizationId = cmd.getCurrentOrganizationId();
		FlowCase fc = dbProvider.execute((TransactionStatus status) -> {

			if (form.getStatus().equals(GeneralFormStatus.CONFIG.getCode())) {
				// 使用表单/审批 注意状态 config
				form.setStatus(GeneralFormStatus.RUNNING.getCode());
				this.generalFormProvider.updateGeneralForm(form);
			}

			Flow flow = sa.getFlowId() == null ? null : flowProvider.getSnapshotFlowById(sa.getFlowId());

			CreateFlowCaseCommand cmd21 = new CreateFlowCaseCommand();
			cmd21.setApplyUserId(user.getId());
			cmd21.setReferType(FlowReferType.SERVICE_ALLIANCE.getCode());
			cmd21.setReferId(sa.getId());
			cmd21.setProjectType(sa.getOwnerType());
			cmd21.setProjectId(sa.getOwnerId());
			// 把command作为json传到content里，给flowcase的listener进行处理
			cmd21.setContent(JSON.toJSONString(cmd));
			cmd21.setCurrentOrganizationId(currentOrganizationId);
			cmd21.setApplierOrganizationId(currentOrganizationId);
			cmd21.setTitle(sa.getName());
			ServiceAllianceCategories category = yellowPageProvider.findCategoryById(sa.getCategoryId());
			if (category != null) {
				cmd21.setServiceType(category.getName());
			}
			Long flowCaseId = flowService.getNextFlowCaseId();
			
			// add by jiarui 20180705
			storeValues(form, cmd, flowCaseId);
			
			FlowCase flowCase;
			if (null == flow) {
				// 给他一个默认哑的flow
				GeneralModuleInfo gm = new GeneralModuleInfo();
				gm.setNamespaceId(UserContext.getCurrentNamespaceId());
				gm.setOwnerId(sa.getType());
				gm.setOwnerType(FlowOwnerType.SERVICE_ALLIANCE.getCode());
				gm.setOrganizationId(currentOrganizationId);
				gm.setModuleType(FlowModuleType.NO_MODULE.getCode());
				gm.setModuleId(SERVICE_ALLIANCE_MODULE_ID);
				gm.setProjectId(cmd.getOwnerId());
				gm.setProjectType(cmd.getOwnerType());

				cmd21.setFlowCaseId(flowCaseId);
				flowCase = flowService.createDumpFlowCase(gm, cmd21);
				flowCase.setStatus(FlowCaseStatus.FINISHED.getCode());
				flowCaseProvider.updateFlowCase(flowCase);
			} else {
				cmd21.setFlowMainId(flow.getFlowMainId());
				cmd21.setFlowVersion(flow.getFlowVersion());
				cmd21.setFlowCaseId(flowCaseId);
				flowCase = flowService.createFlowCase(cmd21);
			}
			return flowCase;
		});

		PostGeneralFormDTO dto = new PostGeneralFormDTO();
		List<PostApprovalFormItem> items = new ArrayList<>();
		PostApprovalFormItem item = new PostApprovalFormItem();
		item.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
		item.setFieldName(GeneralFormDataSourceType.CUSTOM_DATA.getCode());
		JSONObject json = new JSONObject();
		json.put("flowCaseId", fc.getId());
		item.setFieldValue(json.toJSONString());
		items.add(item);
		dto.setValues(items);
		return dto;
	}
	
	private void storeValues(GeneralForm form, PostGeneralFormValCommand cmd, Long flowCaseId) {

		// 把values 存起来
		List<GeneralFormVal> result = new ArrayList<>();
		for (PostApprovalFormItem val : cmd.getValues()) {
			GeneralFormVal obj = ConvertHelper.convert(form, GeneralFormVal.class);
			obj.setSourceType(EhFlowCases.class.getSimpleName());
			obj.setSourceId(flowCaseId);
			obj.setFieldName(val.getFieldName());
			obj.setFieldType(val.getFieldType());
			obj.setFieldValue(val.getFieldValue());
			generalFormValProvider.createGeneralFormVal(obj);
			result.add(obj);
		}

		// add by jiarui 20180705
		LocalEventBus.publish(event -> {
			LocalEventContext localEventcontext = new LocalEventContext();
			localEventcontext.setUid(UserContext.currentUserId());
			localEventcontext.setNamespaceId(UserContext.getCurrentNamespaceId());
			event.setContext(localEventcontext);
			event.setEntityType(EntityType.GENERAL_FORM_VAL.getCode());
			Map<String, Object> map = new HashMap<>();
			map.put(EntityType.GENERAL_FORM_VAL.getCode(), result);
			event.setParams(map);
			event.setEventName(SystemEvent.SERVICE_ALLIANCE_CREATE.dft());
		});

	}

	@Override
	public GeneralFormDTO getTemplateBySourceId(GetTemplateBySourceIdCommand cmd) {
		ServiceAlliances sa = yellowPageProvider.findServiceAllianceById(cmd.getSourceId(), null, null);
		
		GeneralForm form = generalFormProvider.getActiveGeneralFormByOriginId(sa.getFormId());
		if (form == null)
			throw RuntimeErrorException.errorWith(GeneralApprovalServiceErrorCode.SCOPE,
					GeneralApprovalServiceErrorCode.ERROR_FORM_NOTFOUND, "form not found");
		form.setFormVersion(form.getFormVersion());
		List<GeneralFormFieldDTO> fieldDTOs = new ArrayList<GeneralFormFieldDTO>();
		fieldDTOs = JSONObject.parseArray(form.getTemplateText(), GeneralFormFieldDTO.class);
		// 增加一个隐藏的field 用于存放sourceId
		GeneralFormFieldDTO sourceIdField = new GeneralFormFieldDTO();
		sourceIdField.setDataSourceType(GeneralFormDataSourceType.SOURCE_ID.getCode());
		sourceIdField.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
		sourceIdField.setFieldName(GeneralFormDataSourceType.SOURCE_ID.getCode());
		sourceIdField.setRequiredFlag(NormalFlag.NEED.getCode());
		sourceIdField.setDynamicFlag(NormalFlag.NEED.getCode());
		sourceIdField.setRenderType(GeneralFormRenderType.DEFAULT.getCode());
		sourceIdField.setVisibleType(GeneralFormDataVisibleType.HIDDEN.getCode());
		fieldDTOs.add(sourceIdField);

		GeneralFormFieldDTO organizationIdField = new GeneralFormFieldDTO();
		organizationIdField.setDataSourceType(GeneralFormDataSourceType.ORGANIZATION_ID.getCode());
		organizationIdField.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
		organizationIdField.setFieldName(GeneralFormDataSourceType.ORGANIZATION_ID.getCode());
		organizationIdField.setRequiredFlag(NormalFlag.NEED.getCode());
		organizationIdField.setDynamicFlag(NormalFlag.NEED.getCode());
		organizationIdField.setRenderType(GeneralFormRenderType.DEFAULT.getCode());
		organizationIdField.setVisibleType(GeneralFormDataVisibleType.HIDDEN.getCode());
		fieldDTOs.add(organizationIdField);

		GeneralFormFieldDTO customField = new GeneralFormFieldDTO();
		customField.setFieldName(GeneralFormDataSourceType.CUSTOM_DATA.getCode());
		customField.setFieldType(GeneralFormFieldType.SINGLE_LINE_TEXT.getCode());
		customField.setRequiredFlag(NormalFlag.NONEED.getCode());
		customField.setDynamicFlag(NormalFlag.NONEED.getCode());
		customField.setVisibleType(GeneralFormDataVisibleType.HIDDEN.getCode());
		customField.setRenderType(GeneralFormRenderType.DEFAULT.getCode());
//		customField.setDataSourceType(GeneralFormDataSourceType.CUSTOM_DATA.getCode());
//		customField.setFieldValue(JSONObject.toJSONString(ga));
//		fieldDTOs.add(customField);

		GeneralFormDTO dto = ConvertHelper.convert(form, GeneralFormDTO.class);
		dto.setFormFields(fieldDTOs);
		return dto;
	}

	@Override
	public GeneralFormReminderDTO getGeneralFormReminder(GeneralFormReminderCommand cmd) {
		return new GeneralFormReminderDTO(TrueOrFalseFlag.FALSE.getCode());
	}

	@Override
	public PostGeneralFormDTO updateGeneralFormVal(PostGeneralFormValCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}
}
