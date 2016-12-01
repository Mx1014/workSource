package com.everhomes.search;

import java.util.List;

import com.everhomes.rest.yellowPage.SearchRequestInfoCommand;
import com.everhomes.rest.yellowPage.SearchRequestInfoResponse;
import com.everhomes.yellowPage.ReservationRequests;
import com.everhomes.yellowPage.ServiceAllianceRequestInfo;
import com.everhomes.yellowPage.ServiceAllianceRequests;
import com.everhomes.yellowPage.SettleRequests;

public interface ServiceAllianceRequestInfoSearcher {
	void deleteById(Long id);
	void bulkUpdateServiceAllianceRequests(List<ServiceAllianceRequests> requests);
    void bulkUpdateReservationRequests(List<ReservationRequests> requests);
    void bulkUpdateSettleRequests(List<SettleRequests> requests);
    void feedDoc(ServiceAllianceRequestInfo request);
    void syncFromDb();
    SearchRequestInfoResponse searchRequestInfo(SearchRequestInfoCommand cmd);

}
