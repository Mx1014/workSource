package com.everhomes.yellowPage.faq;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.common.IdNameDTO;
import com.everhomes.rest.yellowPage.AllianceCommonCommand;

@Component
public class AllianceFAQProviderImpl implements AllianceFAQProvider{
	
	
	@Autowired
	private AllianceFaqTypeProvider allianceFaqTypeProvider;
	
	@Autowired
	private AllianceFaqProvider allianceFaqProvider;
	
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
	public void updateFAQTypeOrders(Long upFAQTypeId, Long lowFAQTypeId) {
//		dbProvider.execute(arg0)
//		allianceFaqTypeProvider.updateFAQTypeOrder(upFAQTypeId, lowFAQTypeId); 
	}

	@Override
	public ListFAQTypesResponse listFAQTypes(AllianceCommonCommand cmd, Integer pageSize, Long pageAnchor) {
		CrossShardListingLocator locator = new CrossShardListingLocator();
		List<AllianceFAQType> faqTypes = allianceFaqTypeProvider.listFAQTypes(cmd, locator, pageSize, pageAnchor);
		
		//组织
		List<IdNameDTO> dtos = faqTypes.stream().map(r -> {
			IdNameDTO dto = new IdNameDTO();
			dto.setId(r.getId());
			dto.setName(r.getName());
			return dto;
		}).collect(Collectors.toList());
		
		//返回
		ListFAQTypesResponse resp = new ListFAQTypesResponse();
		resp.setDtos(dtos);
		resp.setNextPageAnchor(locator.getAnchor());
		return null;
	}

	@Override
	public void createFAQ(AllianceFAQ faq) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateFAQ(AllianceFAQ faq) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateTopFAQFlag(Long faqId, byte topFlag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteFAQ(Long faqId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AllianceFAQ getFAQ(Long faqId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListFAQsResponse listFAQs(AllianceCommonCommand cmd, Integer pageSize, Long pageAnchor, Long faqType,
			Byte topFlag, String keyword, Byte orderType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListTopFAQsResponse listTopFAQs(AllianceCommonCommand cmd, Integer pageSize, Long pageAnchor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateTopFAQOrders(Long upFAQId, Long lowFAQId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ListOperateServicesResponse listOperateServices(AllianceCommonCommand cmd) {
		// TODO Auto-generated method stub
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
