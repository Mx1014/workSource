package com.everhomes.search;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.everhomes.rest.yellowPage.SearchOneselfRequestInfoCommand;
import com.everhomes.rest.yellowPage.SearchOrgRequestInfoCommand;
import com.everhomes.rest.yellowPage.SearchRequestInfoCommand;
import com.everhomes.rest.yellowPage.SearchRequestInfoResponse;
import com.everhomes.yellowPage.*;

public interface ServiceAllianceRequestInfoSearcher {
	void deleteById(Long id);
	void bulkUpdateServiceAllianceRequests(List<ServiceAllianceRequests> requests);
    void bulkUpdateReservationRequests(List<ReservationRequests> requests);
    void bulkUpdateSettleRequests(List<SettleRequests> requests);
    void bulkUpdateServiceAllianceApartmentRequests(List<ServiceAllianceApartmentRequests> requests);
    void bulkUpdateServiceAllianceInvestRequests(List<ServiceAllianceInvestRequests> requests);
    void feedDoc(ServiceAllianceRequestInfo request);
    void syncFromDb();
    SearchRequestInfoResponse searchRequestInfo(SearchRequestInfoCommand cmd);
    void exportRequestInfo(SearchRequestInfoCommand cmd,HttpServletResponse resp);
    SearchRequestInfoResponse searchOneselfRequestInfo(SearchOneselfRequestInfoCommand cmd);
    SearchRequestInfoResponse searchOrgRequestInfo(SearchOrgRequestInfoCommand cmd);

}
