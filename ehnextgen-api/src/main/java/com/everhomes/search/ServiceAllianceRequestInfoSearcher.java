package com.everhomes.search;

import java.util.List;

import com.everhomes.rest.yellowPage.SearchRequestInfoCommand;
import com.everhomes.rest.yellowPage.SearchRequestInfoResponse;
import com.everhomes.yellowPage.ServiceAllianceRequests;

public interface ServiceAllianceRequestInfoSearcher {
	void deleteById(Long id);
	void bulkUpdate(List<ServiceAllianceRequests> requests);
    void feedDoc(ServiceAllianceRequests request);
    void syncFromDb();
    SearchRequestInfoResponse searchRequestInfo(SearchRequestInfoCommand cmd);

}
