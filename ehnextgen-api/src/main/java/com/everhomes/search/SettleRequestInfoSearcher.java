package com.everhomes.search;

import java.util.List;

import com.everhomes.rest.yellowPage.SearchRequestInfoCommand;
import com.everhomes.rest.yellowPage.SearchRequestInfoResponse;
import com.everhomes.yellowPage.SettleRequests;

public interface SettleRequestInfoSearcher {

	void deleteById(Long id);
	void bulkUpdate(List<SettleRequests> requests);
    void feedDoc(SettleRequests request);
    void syncFromDb();
    SearchRequestInfoResponse searchRequestInfo(SearchRequestInfoCommand cmd);

}
