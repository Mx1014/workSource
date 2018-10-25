package com.everhomes.yellowPage.faq;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.yellowPage.AllianceCommonCommand;

public interface AllianceFaqsProvider {

	void createFAQ(AllianceFAQ faq);

	void updateFAQ(AllianceFAQ updateItem);

	void deleteFAQ(Long itemId);

	AllianceFAQ getFAQ(Long itemId);

	void updateFAQOrder(Long itemId, Long defaultOrderId);

	List<AllianceFAQ> listFAQs(AllianceCommonCommand cmd, ListingLocator locator, Integer pageSize,
			Long pageAnchor);

}
