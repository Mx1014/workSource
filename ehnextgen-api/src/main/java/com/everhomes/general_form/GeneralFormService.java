package com.everhomes.general_form;

import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.general_approval.*;

import java.util.List;

public interface GeneralFormService {

	GeneralFormDTO getTemplateByFormId(GetTemplateByFormIdCommand cmd);

	GeneralFormDTO createGeneralForm(CreateApprovalFormCommand cmd);

	ListGeneralFormResponse listGeneralForms(ListGeneralFormsCommand cmd);

	void deleteGeneralFormById(GeneralFormIdCommand cmd);

	GeneralFormDTO updateGeneralForm(UpdateApprovalFormCommand cmd);

	GeneralFormDTO getGeneralForm(GeneralFormIdCommand cmd);

	List<PostApprovalFormItem> getGeneralFormValues(GetGeneralFormValuesCommand cmd);

	void addGeneralFormValues(addGeneralFormValuesCommand cmd);

	GeneralFormDTO getTemplateBySourceId(GetTemplateBySourceIdCommand cmd);

	PostGeneralFormDTO postGeneralForm(PostGeneralFormValCommand cmd);

	List<FlowCaseEntity> resolveFormVal(GeneralFormFieldDTO dto, GeneralFormVal val);

	void processFlowEntities(List<FlowCaseEntity> entities, List<GeneralFormVal> vals, List<GeneralFormFieldDTO> fieldDTOs);

	List<FlowCaseEntity> getGeneralFormFlowEntities(GetGeneralFormValuesCommand cmd);

	void processFlowEntities(List<FlowCaseEntity> entities, List<GeneralFormVal> vals,
			List<GeneralFormFieldDTO> fieldDTOs, boolean showDefaultFields);

	List<FlowCaseEntity> getGeneralFormFlowEntities(GetGeneralFormValuesCommand cmd, boolean showDefaultFields);

    /**
     * 根据owner和字段名称获取字段的值
     */
    GeneralFormFieldDTO getGeneralFormValueByOwner(Long formOriginId, Long formVersion, String moduleType, Long moduleId, String ownerType, Long ownerId, String fieldName);

	GeneralFormDTO verifyApprovalFormName(VerifyApprovalFormNameCommand cmd);

	GeneralFormDTO createGeneralFormByTemplate(Long templateId, CreateFormTemplatesCommand cmd);

	PostGeneralFormDTO updateGeneralFormVal(PostGeneralFormValCommand cmd);

	GeneralFormReminderDTO getGeneralFormReminder(GeneralFormReminderCommand cmd);

    void enableProjectCustomize(EnableProjectCustomizeCommand cmd);

    GeneralForm mirrorGeneralForm(Long formId, String mirrorOwnerType, Long mirrorOwnerId, String mirrorModuleType, Long mirrorModuleId);

    void disableProjectCustomize(DisableProjectCustomizeCommand cmd);

	Byte getProjectCustomize(GetProjectCustomizeCommand cmd);
}
