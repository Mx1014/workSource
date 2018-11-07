package com.everhomes.yellowPage.faq;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.yellowPage.AllianceCommonCommand;
import com.everhomes.yellowPage.AllianceOperateService;
import com.everhomes.yellowPage.AllianceOperateServiceProvider;

@Component
public class AllianceFAQProviderImpl implements AllianceFAQProvider{
	
	
	@Autowired
	private AllianceFaqTypeProvider allianceFaqTypeProvider;
	
	@Autowired
	private AllianceFaqsProvider allianceFaqsProvider;
	
	@Autowired
	private AllianceOperateServiceProvider allianceOperateServiceProvider;
	
	@Autowired
	private AllianceFaqServiceCustomerProvider allianceFaqServiceCustomerProvider;
	

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
		allianceFaqTypeProvider.updateFAQTypeOrder(FAQTypeId, newDefaultOrderId);
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
	public void updateTopFAQFlag(Long faqId, byte topFlag, Long maxTopOrder) {
		allianceFaqsProvider.updateTopFAQFlag(faqId, topFlag, maxTopOrder);
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
		allianceFaqsProvider.updateTopFAQOrder(faqId, newTopOrder);
	}


	@Override
	public void updateFAQOnlineService(AllianceFAQServiceCustomer onlineService) {
		allianceFaqServiceCustomerProvider.updateServiceCustomer(onlineService);
	}

	@Override
	public void createFAQOnlineService(AllianceFAQServiceCustomer onlineService) {
		allianceFaqServiceCustomerProvider.createServiceCustomer(onlineService);
	}

	@Override
	public AllianceFAQServiceCustomer getFAQOnlineService(AllianceCommonCommand cmd) {
		return allianceFaqServiceCustomerProvider.getServiceCustomer(cmd);
	}

	@Override
	public void updateFAQSolveTimes(Long faqId, Byte solveStaus) {
		allianceFaqsProvider.plusFAQSolveCounts(faqId, solveStaus);
	}

	@Override
	public void updateFAQOrder(Long id, Long defaultOrder) {
		allianceFaqsProvider.updateFAQOrder(id, defaultOrder);
	}

	@Override
	public List<AllianceFAQ> listAllFAQs(AllianceCommonCommand cmd) {
		return listFAQs(cmd, null, null, null, null, null, null, null, null);
	}

	@Override
	public void deleteFAQTypes(AllianceCommonCommand cmd) {
		allianceFaqTypeProvider.deleteFAQTypes(cmd);
		
	}

	@Override
	public void deleteFAQs(AllianceCommonCommand cmd, Long faqTypeId) {
		allianceFaqsProvider.deleteFAQs(cmd, faqTypeId);
		
	}

	@Override
	public void deleteFAQOnlineService(AllianceCommonCommand cmd) {
		allianceFaqServiceCustomerProvider.deleteFAQOnlineService(cmd);
		
	}

	@Override
	public Long getTopFAQMaxOrder(AllianceCommonCommand cmd) {
		return allianceFaqsProvider.getTopFAQMaxOrder(cmd);
	}

}
