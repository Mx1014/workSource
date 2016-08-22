package com.everhomes.search;

import java.util.List;

import com.everhomes.organization.pm.CommunityPmOwner;
import com.everhomes.rest.organization.pm.SearchPMOwnerCommand;
import com.everhomes.rest.organization.pm.SearchPMOwnerResponse;

public interface PMOwnerSearcher {
	void deleteById(Long id);
    void bulkUpdate(List<CommunityPmOwner> owners);
    void feedDoc(CommunityPmOwner owner);
    void syncFromDb();
    SearchPMOwnerResponse query(SearchPMOwnerCommand cmd);
}
