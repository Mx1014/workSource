package com.everhomes.search;

import java.util.List;

import com.everhomes.rest.yellowPage.SearchRequestInfoCommand;
import com.everhomes.rest.yellowPage.SearchRequestInfoResponse;
import com.everhomes.yellowPage.ReservationRequests;

public interface ReserveRequestInfoSearcher {

	void deleteById(Long id);
	void bulkUpdate(List<ReservationRequests> requests);
    void feedDoc(ReservationRequests request);
    void syncFromDb();
    SearchRequestInfoResponse searchRequestInfo(SearchRequestInfoCommand cmd);
    
}
