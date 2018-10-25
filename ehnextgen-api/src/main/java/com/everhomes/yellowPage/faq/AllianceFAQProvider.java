package com.everhomes.yellowPage.faq;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.yellowPage.AllianceCommonCommand;

public interface AllianceFAQProvider {

	/*************问题分类******************/
	void createFAQType(AllianceFAQType faqType);

	void updateFAQType(AllianceFAQType faqType);

	void deleteFAQType(Long faqTypeId);
	
	AllianceFAQType getFAQType(Long faqTypeId);
	
	void updateFAQTypeOrders(Long upFAQTypeId, Long lowFAQTypeId);
	
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
	
	void updateTopFAQOrders(Long upFAQId, Long lowFAQId);

	
	//运营商模块
	List<OperateServiceDTO> listOperateServices(AllianceCommonCommand cmd);

	void deleteOperateServices(AllianceCommonCommand cmd);
	
	AllianceOperateService getOperateServices(AllianceCommonCommand cmd);
	
	void createOperateService(AllianceOperateService operateService);
	
	void updateOperateServiceOrders(AllianceCommonCommand cmd, Long upServiceId, Long lowServiceId);

	
	//热线
	void updateFAQOnlineService(AllianceFAQServiceCustomer onlineService);
	
	void createFAQOnlineService(AllianceFAQServiceCustomer onlineService);
	
	AllianceFAQServiceCustomer getFAQOnlineService(AllianceCommonCommand cmd);


}
