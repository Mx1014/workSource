package com.everhomes.yellowPage.faq;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.yellowPage.AllianceCommonCommand;
import com.everhomes.yellowPage.AllianceOperateService;

public interface AllianceFAQProvider {

	/*************问题分类******************/
	void createFAQType(AllianceFAQType faqType);

	void updateFAQType(AllianceFAQType faqType);

	void deleteFAQType(Long faqTypeId);
	
	AllianceFAQType getFAQType(Long faqTypeId);
	
	void updateFAQTypeOrder(Long upFAQTypeId, Long lowFAQTypeId);
	
	List<AllianceFAQType> listFAQTypes(AllianceCommonCommand cmd, ListingLocator locator, Integer pageSize, Long pageAnchor);

	/*************问题******************/
	void createFAQ(AllianceFAQ faq);

	void updateFAQ(AllianceFAQ faq);

	void updateTopFAQFlag(Long faqId, byte topFlag);

	void deleteFAQ(Long faqId);
	
	AllianceFAQ getFAQ(Long faqId);
	
	List<AllianceFAQ> listFAQs(AllianceCommonCommand cmd, ListingLocator locator, Integer pageSize, Long pageAnchor,
			Long faqType, Byte topFlag, String keyword, Byte orderType, Byte sortType);

	//热门问题
	List<AllianceFAQ> listTopFAQs(AllianceCommonCommand cmd, ListingLocator locator, Integer pageSize, Long pageAnchor);
	
	void updateTopFAQOrder(Long upFAQId, Long lowFAQId);
	
	//热线
	void updateFAQOnlineService(AllianceFAQServiceCustomer onlineService);
	
	void createFAQOnlineService(AllianceFAQServiceCustomer onlineService);
	
	AllianceFAQServiceCustomer getFAQOnlineService(AllianceCommonCommand cmd);

	void updateFAQSolveTimes(Long faqId, Byte solveStaus);

	void updateFAQOrder(Long id, Long defaultOrder);

	List<AllianceFAQ> listAllFAQs(AllianceCommonCommand cmd);

	void deleteFAQTypes(AllianceCommonCommand cmd);

	void deleteFAQs(AllianceCommonCommand cmd);

	void deleteFAQOnlineService(AllianceCommonCommand cmd);

}
