package com.everhomes.search;

import java.util.List;

import com.everhomes.rest.yellowPage.SearchRequestInfoCommand;
import com.everhomes.rest.yellowPage.SearchRequestInfoResponse;
import com.everhomes.yellowPage.ServiceAllianceApartmentRequests;

public interface ApartmentRequestInfoSearcher {

	void deleteById(Long id);
	void bulkUpdate(List<ServiceAllianceApartmentRequests> requests);
    void feedDoc(ServiceAllianceApartmentRequests request);
    void syncFromDb();
    SearchRequestInfoResponse searchRequestInfo(SearchRequestInfoCommand cmd);

}
