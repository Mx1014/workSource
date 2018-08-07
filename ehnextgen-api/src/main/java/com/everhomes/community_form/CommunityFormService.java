// @formatter:off
package com.everhomes.community_form;

import com.everhomes.rest.community_form.CommunityGeneralFormDTO;
import com.everhomes.rest.community_form.CreateOrUpdateCommunityFormCommand;
import com.everhomes.rest.community_form.DeleteCommunityFormCommand;

public interface CommunityFormService {
    CommunityGeneralFormDTO createOrUpdateCommunityGeneralForm(CreateOrUpdateCommunityFormCommand cmd);
    void deleteCommunityGeneralForm(DeleteCommunityFormCommand cmd);
}
