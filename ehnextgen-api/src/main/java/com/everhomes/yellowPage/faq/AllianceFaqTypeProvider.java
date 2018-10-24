package com.everhomes.yellowPage.faq;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.yellowPage.AllianceCommonCommand;

public interface AllianceFaqTypeProvider {
	void createFAQType(AllianceFAQType faqType);

	void updateFAQType(AllianceFAQType faqType);

	void deleteFAQType(Long faqTypeId);
	
	AllianceFAQType getFAQType(Long faqTypeId);
	
	void updateFAQTypeOrders(Long upFAQTypeId, Long lowFAQTypeId);
	
	List<AllianceFAQType> listFAQTypes(AllianceCommonCommand cmd, CrossShardListingLocator locator, Integer pageSize, Long pageAnchor);
}
