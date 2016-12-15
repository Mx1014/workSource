package com.everhomes.search;

import java.util.List;

import com.everhomes.rest.yellowPage.SearchRequestInfoCommand;
import com.everhomes.rest.yellowPage.SearchRequestInfoResponse;
import com.everhomes.yellowPage.*;

public interface ServiceAllianceRequestInfoSearcher {
	void deleteById(Long id);
	void bulkUpdateServiceAllianceRequests(List<ServiceAllianceRequests> requests);
    void bulkUpdateReservationRequests(List<ReservationRequests> requests);
    void bulkUpdateSettleRequests(List<SettleRequests> requests);
    void bulkUpdateServiceAllianceApartmentRequests(List<ServiceAllianceApartmentRequests> requests);
    void feedDoc(ServiceAllianceRequestInfo request);
    void syncFromDb();
    SearchRequestInfoResponse searchRequestInfo(SearchRequestInfoCommand cmd);

}
