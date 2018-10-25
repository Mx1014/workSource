package com.everhomes.yellowPage.faq;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.common.IdNameDTO;
import com.everhomes.rest.yellowPage.AllianceCommonCommand;

@Component
public class AllianceFAQProviderImpl implements AllianceFAQProvider{
	
	
	@Autowired
	private AllianceFaqTypeProvider allianceFaqTypeProvider;
	
	@Autowired
	private AllianceFaqsProvider allianceFaqsProvider;
	
	@Autowired
	private DbProvider dbProvider;
	

	@Override
	public void createFAQType(AllianceFAQType faqType) {
		allianceFaqTypeProvider.createFAQType(faqType);
	}

	@Override
	public void updateFAQType(AllianceFAQType faqType) {
		allianceFaqTypeProvider.updateFAQType(faqType);
	}

	@Override
	public void deleteFAQType(Long faqTypeId) {
		allianceFaqTypeProvider.deleteFAQType(faqTypeId);
	}

	@Override
	public AllianceFAQType getFAQType(Long faqTypeId) {
		return allianceFaqTypeProvider.getFAQType(faqTypeId);
	}

	@Override
	public void updateFAQTypeOrder(Long FAQTypeId, Long newDefaultOrderId) {
//		dbProvider.execute(arg0)
//		allianceFaqTypeProvider.updateFAQTypeOrder(upFAQTypeId, lowFAQTypeId); 
	}

	@Override
	public List<AllianceFAQType> listFAQTypes(AllianceCommonCommand cmd, ListingLocator locator, Integer pageSize, Long pageAnchor) {
		return allianceFaqTypeProvider.listFAQTypes(cmd, locator, pageSize, pageAnchor);
	}

	@Override
	public void createFAQ(AllianceFAQ faq) {
		allianceFaqsProvider.createFAQ(faq);
	}

	@Override
	public void updateFAQ(AllianceFAQ faq) {
		allianceFaqsProvider.updateFAQ(faq);
	}

	@Override
	public void updateTopFAQFlag(Long faqId, byte topFlag) {
		allianceFaqsProvider.updateTopFAQFlag(faqId, topFlag);
	}

	@Override
	public void deleteFAQ(Long faqId) {
		allianceFaqsProvider.deleteFAQ(faqId);
	}

	@Override
	public AllianceFAQ getFAQ(Long faqId) {
		return allianceFaqsProvider.getFAQ(faqId);
	}

	@Override
	public List<AllianceFAQ> listFAQs(AllianceCommonCommand cmd, ListingLocator locator,  Integer pageSize, Long pageAnchor, Long faqType,
			Byte topFlag, String keyword, Byte orderType, Byte sortType) {
		return allianceFaqsProvider.listFAQs(cmd, locator, pageSize, pageAnchor, faqType, topFlag, keyword, orderType, sortType);
	}

	@Override
	public List<AllianceFAQ> listTopFAQs(AllianceCommonCommand cmd, ListingLocator locator, Integer pageSize, Long pageAnchor) {
		return allianceFaqsProvider.listTopFAQs(cmd, locator, pageSize, pageAnchor);
	}

	@Override
	public void updateTopFAQOrder(Long faqId, Long newTopOrder) {
		
	}

	@Override
	public List<OperateServiceDTO> listOperateServices(AllianceCommonCommand cmd) {
		return null;
	}

	@Override
	public void deleteOperateServices(AllianceCommonCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AllianceOperateService getOperateServices(AllianceCommonCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createOperateService(AllianceOperateService operateService) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateOperateServiceOrders(AllianceCommonCommand cmd, Long upServiceId, Long lowServiceId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateFAQOnlineService(AllianceFAQServiceCustomer onlineService) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createFAQOnlineService(AllianceFAQServiceCustomer onlineService) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AllianceFAQServiceCustomer getFAQOnlineService(AllianceCommonCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

}
