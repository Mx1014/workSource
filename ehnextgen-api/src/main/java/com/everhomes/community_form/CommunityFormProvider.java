// @formatter:off
package com.everhomes.community_form;

import java.util.List;

public interface CommunityFormProvider {
    void createCommunityGeneralForm(CommunityGeneralForm communityGeneralForm);
    void updateCommunityGeneralForm(CommunityGeneralForm communityGeneralForm);
    void deleteCommunityGeneralForm(CommunityGeneralForm communityGeneralForm);
    CommunityGeneralForm findCommunityGeneralForm(Long communityId, String type);
    List<CommunityGeneralForm> listCommunityGeneralFormByFormId(Long formId);
    List<CommunityGeneralForm> listCommunityGeneralFormByCommunityIds(List<Long> communityIds, String type, Integer pageOffset, int pageSize);
}
