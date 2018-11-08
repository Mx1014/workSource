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
			Long pageAnchor, Long faqType, Byte topFlag, String keyword, Byte orderType, Byte sortType);

	void updateTopFAQFlag(Long faqId, byte topFlag, Long maxTopOrder);

	List<AllianceFAQ> listTopFAQs(AllianceCommonCommand cmd, ListingLocator locator, Integer pageSize, Long pageAnchor);

	void updateTopFAQOrder(Long faqId, Long newTopOrder);

	void plusFAQSolveCounts(Long itemId, Byte solveTimesType);

	void deleteFAQs(AllianceCommonCommand cmd, Long faqTypeId);

	Long getTopFAQMaxOrder(AllianceCommonCommand cmd);

}
