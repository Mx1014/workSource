package com.everhomes.yellowPage.faq;

import com.everhomes.rest.yellowPage.AllianceCommonCommand;

public interface AllianceFaqServiceCustomerProvider {

	void createServiceCustomer(AllianceFAQServiceCustomer createItem);

	void updateServiceCustomer(AllianceFAQServiceCustomer updateItem);

	void deleteServiceCustomer(Long itemId);

	AllianceFAQServiceCustomer getServiceCustomer(AllianceCommonCommand cmd);

	void deleteFAQOnlineService(AllianceCommonCommand cmd);

}
