package com.everhomes.advertisement;

import org.springframework.stereotype.Component;

import com.everhomes.general_form.GeneralFormModuleHandler;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.general_approval.GetTemplateBySourceIdCommand;
import com.everhomes.rest.general_approval.PostGeneralFormDTO;
import com.everhomes.rest.general_approval.PostGeneralFormValCommand;

@Component(GeneralFormModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX + "business_invitation")
public class AdvertisementFormHandler implements GeneralFormModuleHandler{

	@Override
	public PostGeneralFormDTO postGeneralFormVal(PostGeneralFormValCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GeneralFormDTO getTemplateBySourceId(GetTemplateBySourceIdCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PostGeneralFormDTO updateGeneralFormVal(PostGeneralFormValCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

}
