package com.everhomes.yellowPage.faq;

import com.everhomes.yellowPage.ListUiFAQsCommand;

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

	ListOperateServicesResponse listOperateServices(ListOperateServicesCommand cmd);

	void updateOperateServices(UpdateOperateServicesCommand cmd);

	void updateOperateServiceOrders(UpdateOperateServiceOrdersCommand cmd);

	GetLatestServiceStateResponse getLatestServiceState(GetLatestServiceStateCommand cmd);

	GetServiceCountsResponse getServiceCounts(GetServiceCountsCommand cmd);

	ListUiFAQsResponse listUiFAQs(ListUiFAQsCommand cmd);

	void updateFAQOnlineService(UpdateFAQOnlineServiceCommand cmd);
	
	GetFAQOnlineServiceResponse getFAQOnlineService(GetFAQOnlineServiceCommand cmd);

	void updateFAQTypeOrders(UpdateFAQTypeOrdersCommand cmd);


}
