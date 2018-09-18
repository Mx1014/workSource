// @formatter:off
package com.everhomes.community_form;

import com.everhomes.rest.community_form.CommunityGeneralFormDTO;
import com.everhomes.rest.community_form.CreateOrUpdateCommunityFormCommand;
import com.everhomes.rest.community_form.DeleteCommunityFormCommand;
import com.everhomes.rest.community_form.ListCommunityFormCommand;
import com.everhomes.rest.community_form.ListCommunityFormResponse;

public interface CommunityFormService {
    CommunityGeneralFormDTO createOrUpdateCommunityGeneralForm(CreateOrUpdateCommunityFormCommand cmd);
    void deleteCommunityGeneralForm(DeleteCommunityFormCommand cmd);
    ListCommunityFormResponse listCommunityForm(ListCommunityFormCommand cmd);
}
