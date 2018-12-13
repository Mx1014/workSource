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

    GeneralForm mirrorGeneralForm(Long formId, String mirrorModuleType, Long mirrorModuleId,
                                  String mirrorProjectType, Long mirrorProjectId, String mirrorOwnerType, Long mirrorOwnerId);

    void disableProjectCustomize(DisableProjectCustomizeCommand cmd);

	Byte getProjectCustomize(GetProjectCustomizeCommand cmd);

	void doFormMirror(DoFormMirrorCommand cmd);

	SearchFormValDTO searchGeneralFormVals(SearchFormValsCommand cmd);

	/**
	 * 根据所进入的模块选择对应的模板列表
	 * @param cmd
	 * @return
	 */
	List<GeneralFormFieldDTO> getDefaultFieldsByModuleId(ListDefaultFieldsCommand cmd);

	Long deleteGeneralFormVal(PostGeneralFormValCommand cmd);
	Long deleteGeneralFormValWithPrivi(PostGeneralFormValCommand cmd);

	Long deleteGeneralForm(PostGeneralFormValCommand cmd);

	List<GeneralFormValDTO> getGeneralFormVal(GetGeneralFormValCommand cmd);


	Long saveGeneralFormVal(PostGeneralFormValCommand cmd);
	/**
	 * 保存但不提交工作流
	 * @param cmd
	 */
	Long saveGeneralForm(PostGeneralFormValCommand cmd);

	List<String> listGeneralFormFilter(GetGeneralFormFilterCommand cmd);

	List<String> saveGeneralFormFilter(PostGeneralFormFilterCommand cmd);

	List<GeneralFormValDTO> getGeneralFormValWithPrivi(GetGeneralFormValCommand cmd);


    /**
     * 表单打印模板接口
     */
    GeneralFormPrintTemplateDTO createGeneralFormPrintTemplate(AddGeneralFormPrintTemplateCommand cmd);

    GeneralFormPrintTemplateDTO updateGeneralFormPrintTemplate(UpdateGeneralFormPrintTemplateCommand cmd);

    GeneralFormPrintTemplateDTO getGeneralFormPrintTemplate(GetGeneralFormPrintTemplateCommand cmd);

	/**
	 * 工作流节点关联的全局表单字段配置接口
	 */
	GeneralFormFieldsConfigDTO createFormFieldsConfig(CreateFormFieldsConfigCommand cmd);

	GeneralFormFieldsConfigDTO updateFormFieldsConfig(UpdateFormFieldsConfigCommand cmd);

	GeneralFormFieldsConfigDTO updateFormFieldsConfigStatus(Long formFieldsConfigId);

	void deleteFormFieldsConfig(DeleteFormFieldsConfigCommand cmd);

	GeneralFormFieldsConfigDTO getFormFieldsConfig(GetFormFieldsConfigCommand cmd);

	GeneralFormDTO getGeneralFormByOriginIdAndVersion(GetGeneralFormByOriginIdAndVersionCommand cmd);
}
