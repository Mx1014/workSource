package com.everhomes.general_form;

import com.everhomes.rest.general_approval.*;
import com.everhomes.rest.general_form.*;

import java.util.List;

public interface GeneralFormService {

	GeneralFormDTO getTemplateByFormId(GetTemplateByFormIdCommand cmd);

	GeneralFormDTO createGeneralForm(CreateGeneralFormCommand cmd);

	ListGeneralFormResponse listGeneralForms(ListGeneralFormsCommand cmd);

	void deleteGeneralFormById(GeneralFormIdCommand cmd);

	GeneralFormDTO updateGeneralForm(UpdateGeneralFormCommand cmd);

	GeneralFormDTO getGeneralForm(GeneralFormIdCommand cmd);

	List<PostApprovalFormItem> getGeneralFormValues(getGeneralFormValuesCommand cmd);

	void addGeneralFormValues(addGeneralFormValuesCommand cmd);
}
