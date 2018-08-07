// @formatter:off
package com.everhomes.community_form;

import com.everhomes.rest.community_form.CommunityGeneralFormDTO;
import com.everhomes.rest.community_form.CreateOrUpdateCommunityFormCommand;
import com.everhomes.rest.community_form.DeleteCommunityFormCommand;
import com.everhomes.util.ConvertHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommunityFormServiceImpl implements CommunityFormService{

    @Autowired
    private CommunityFormProvider communityFormProvider;

    @Override
    public CommunityGeneralFormDTO createOrUpdateCommunityGeneralForm(CreateOrUpdateCommunityFormCommand cmd) {
        CommunityGeneralForm communityGeneralForm  = this.communityFormProvider.findCommunityGeneralForm(cmd.getCommunityId(),cmd.getType());
        if (communityGeneralForm == null) {
            communityGeneralForm = ConvertHelper.convert(cmd,CommunityGeneralForm.class);
            this.communityFormProvider.createCommunityGeneralForm(communityGeneralForm);
        }else {
            communityGeneralForm.setFormId(cmd.getFormId());
            this.communityFormProvider.updateCommunityGeneralForm(communityGeneralForm);
        }
        return ConvertHelper.convert(communityGeneralForm,CommunityGeneralFormDTO.class);
    }

    @Override
    public void deleteCommunityGeneralForm(DeleteCommunityFormCommand cmd) {
        CommunityGeneralForm communityGeneralForm  = this.communityFormProvider.findCommunityGeneralForm(cmd.getCommunityId(),cmd.getType());
        if (communityGeneralForm != null) {
            this.communityFormProvider.deleteCommunityGeneralForm(communityGeneralForm);
        }
    }
}
