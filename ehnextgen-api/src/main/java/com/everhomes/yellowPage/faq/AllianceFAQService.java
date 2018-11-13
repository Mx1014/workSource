package com.everhomes.yellowPage.faq;

import com.everhomes.rest.yellowPage.ListOperateServicesResponse;
import com.everhomes.rest.yellowPage.UpdateOperateServiceOrdersCommand;
import com.everhomes.rest.yellowPage.UpdateOperateServicesCommand;
import com.everhomes.rest.yellowPage.faq.CreateFAQCommand;
import com.everhomes.rest.yellowPage.faq.CreateFAQTypeCommand;
import com.everhomes.rest.yellowPage.faq.DeleteFAQCommand;
import com.everhomes.rest.yellowPage.faq.DeleteFAQTypeCommand;
import com.everhomes.rest.yellowPage.faq.GetFAQOnlineServiceCommand;
import com.everhomes.rest.yellowPage.faq.GetFAQOnlineServiceResponse;
import com.everhomes.rest.yellowPage.faq.GetLatestServiceStateCommand;
import com.everhomes.rest.yellowPage.faq.GetLatestServiceStateResponse;
import com.everhomes.rest.yellowPage.faq.GetPendingServiceCountsCommand;
import com.everhomes.rest.yellowPage.faq.GetPendingServiceCountsResponse;
import com.everhomes.rest.yellowPage.faq.ListFAQTypesCommand;
import com.everhomes.rest.yellowPage.faq.ListFAQTypesResponse;
import com.everhomes.rest.yellowPage.faq.ListFAQsCommand;
import com.everhomes.rest.yellowPage.faq.ListFAQsResponse;
import com.everhomes.rest.yellowPage.faq.ListTopFAQsCommand;
import com.everhomes.rest.yellowPage.faq.ListTopFAQsResponse;
import com.everhomes.rest.yellowPage.faq.ListUiFAQsCommand;
import com.everhomes.rest.yellowPage.faq.ListUiFAQsResponse;
import com.everhomes.rest.yellowPage.faq.ListUiServiceRecordsCommand;
import com.everhomes.rest.yellowPage.faq.ListUiServiceRecordsResponse;
import com.everhomes.rest.yellowPage.faq.UpdateFAQCommand;
import com.everhomes.rest.yellowPage.faq.UpdateFAQOnlineServiceCommand;
import com.everhomes.rest.yellowPage.faq.UpdateFAQSolveTimesCommand;
import com.everhomes.rest.yellowPage.faq.UpdateFAQTypeCommand;
import com.everhomes.rest.yellowPage.faq.UpdateFAQTypeOrdersCommand;
import com.everhomes.rest.yellowPage.faq.UpdateTopFAQFlagCommand;
import com.everhomes.rest.yellowPage.faq.UpdateTopFAQOrdersCommand;
import com.everhomes.rest.yellowPage.faq.updateFAQOrderCommand;

public interface AllianceFAQService {

	void createFAQType(CreateFAQTypeCommand cmd);

	void updateFAQType(UpdateFAQTypeCommand cmd);

	void deleteFAQType(DeleteFAQTypeCommand cmd);

	ListFAQTypesResponse listFAQTypes(ListFAQTypesCommand cmd);

	void createFAQ(CreateFAQCommand cmd);

	void updateFAQ(UpdateFAQCommand cmd);

	void updateTopFAQFlag(UpdateTopFAQFlagCommand cmd);

	void deleteFAQ(DeleteFAQCommand cmd);

	ListFAQsResponse listFAQs(ListFAQsCommand cmd);

	ListTopFAQsResponse listTopFAQs(ListTopFAQsCommand cmd);

	void updateTopFAQOrders(UpdateTopFAQOrdersCommand cmd);


	GetLatestServiceStateResponse getLatestServiceState(GetLatestServiceStateCommand cmd);


	ListUiFAQsResponse listUiFAQs(ListUiFAQsCommand cmd);

	void updateFAQOnlineService(UpdateFAQOnlineServiceCommand cmd);
	
	GetFAQOnlineServiceResponse getFAQOnlineService(GetFAQOnlineServiceCommand cmd);

	void updateFAQTypeOrders(UpdateFAQTypeOrdersCommand cmd);

	GetPendingServiceCountsResponse getPendingServiceCounts(GetPendingServiceCountsCommand cmd);

	void updateFAQSolveTimes(UpdateFAQSolveTimesCommand cmd);

	ListUiServiceRecordsResponse listUiServiceRecords(ListUiServiceRecordsCommand cmd);

	void updateFAQOrder(updateFAQOrderCommand cmd);

}
