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

	PostGeneralFormDTO postGeneralForm(PostGeneralFormCommand cmd);

	List<FlowCaseEntity> resolveFormVal(GeneralFormFieldDTO dto, GeneralFormVal val);

	void processFlowEntities(List<FlowCaseEntity> entities, List<GeneralFormVal> vals, List<GeneralFormFieldDTO> fieldDTOs);

	List<FlowCaseEntity> getGeneralFormFlowEntities(GetGeneralFormValuesCommand cmd);

	void processFlowEntities(List<FlowCaseEntity> entities, List<GeneralFormVal> vals,
			List<GeneralFormFieldDTO> fieldDTOs, boolean showDefaultFields);

	List<FlowCaseEntity> getGeneralFormFlowEntities(GetGeneralFormValuesCommand cmd, boolean showDefaultFields);

	void createGeneralFormGroup(CreateGeneralFormGroupCommand cmd);

	List<GeneralFormGroupDTO> listGeneralFormGroups(ListGeneralFormGroupsCommand cmd);
}
