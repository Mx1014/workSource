package com.everhomes.yellowPage.faq;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.yellowPage.AllianceCommonCommand;

public interface AllianceFaqTypeProvider {
	void createFAQType(AllianceFAQType faqType);

	void updateFAQType(AllianceFAQType faqType);

	void deleteFAQType(Long faqTypeId);
	
	AllianceFAQType getFAQType(Long faqTypeId);
	
	void updateFAQTypeOrder(Long faqTypeId, Long defaultOrderId);
	
	List<AllianceFAQType> listFAQTypes(AllianceCommonCommand cmd, ListingLocator locator, Integer pageSize, Long pageAnchor);
}
