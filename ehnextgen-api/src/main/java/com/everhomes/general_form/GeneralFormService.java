package com.everhomes.general_form;

import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.general_approval.CreateApprovalFormCommand;
import com.everhomes.rest.general_approval.CreateFormTemplatesCommand;
import com.everhomes.rest.general_approval.CreateOrUpdateGeneralFormValuesWithFlowCommand;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.GeneralFormIdCommand;
import com.everhomes.rest.general_approval.GeneralFormReminderCommand;
import com.everhomes.rest.general_approval.GeneralFormReminderDTO;
import com.everhomes.rest.general_approval.GeneralFormValDTO;
import com.everhomes.rest.general_approval.GetGeneralFormFilterCommand;
import com.everhomes.rest.general_approval.GetGeneralFormValCommand;
import com.everhomes.rest.general_approval.GetGeneralFormValuesCommand;
import com.everhomes.rest.general_approval.GetGeneralFormsAndValuesByFlowNodeCommand;
import com.everhomes.rest.general_approval.GetTemplateByFormIdCommand;
import com.everhomes.rest.general_approval.GetTemplateBySourceIdCommand;
import com.everhomes.rest.general_approval.ListDefaultFieldsCommand;
import com.everhomes.rest.general_approval.ListGeneralFormResponse;
import com.everhomes.rest.general_approval.ListGeneralFormsCommand;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.rest.general_approval.PostGeneralFormDTO;
import com.everhomes.rest.general_approval.PostGeneralFormFilterCommand;
import com.everhomes.rest.general_approval.PostGeneralFormValCommand;
import com.everhomes.rest.general_approval.SearchFormValDTO;
import com.everhomes.rest.general_approval.SearchFormValsCommand;
import com.everhomes.rest.general_approval.UpdateApprovalFormCommand;
import com.everhomes.rest.general_approval.VerifyApprovalFormNameCommand;
import com.everhomes.rest.general_approval.addGeneralFormValuesCommand;

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
}
