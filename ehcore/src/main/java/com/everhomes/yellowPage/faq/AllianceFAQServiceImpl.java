package com.everhomes.yellowPage.faq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.util.ConvertHelper;
import com.everhomes.yellowPage.ListUiFAQsCommand;

@Component
public class AllianceFAQServiceImpl implements AllianceFAQService{
	
	@Autowired
	AllianceFAQProvider allianceFAQProvider;

	@Override
	public void createFAQType(CreateFAQTypeCommand cmd) {
		AllianceFAQType faqType = ConvertHelper.convert(cmd, AllianceFAQType.class);
		faqType.setName(cmd.getFAQTypeName());
		allianceFAQProvider.createFAQType(faqType);
	}

	@Override
	public void updateFAQType(UpdateFAQTypeCommand cmd) {
		
	}

	@Override
	public void deleteFAQType(DeleteFAQTypeCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ListFAQTypesResponse listFAQTypes(ListFAQTypesCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createFAQ(CreateFAQCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateFAQ(UpdateFAQCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateTopFAQFlag(UpdateTopFAQFlagCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteFAQ(DeleteFAQCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ListFAQsResponse listFAQs(ListFAQsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListTopFAQsResponse listTopFAQs(ListTopFAQsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateTopFAQOrders(UpdateTopFAQOrdersCommand cmd) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public ListOperateServicesResponse listOperateServices(ListOperateServicesCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateOperateServices(UpdateOperateServicesCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateOperateServiceOrders(UpdateOperateServiceOrdersCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public GetLatestServiceStateResponse getLatestServiceState(GetLatestServiceStateCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetServiceCountsResponse getServiceCounts(GetServiceCountsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListUiFAQsResponse listUiFAQs(ListUiFAQsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetFAQOnlineServiceResponse getFAQOnlineService(GetFAQOnlineServiceCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateFAQOnlineService(UpdateFAQOnlineServiceCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateFAQTypeOrders(UpdateFAQTypeOrdersCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public GetSquareCardInfosResponse getSquareCardInfos(GetSquareCardInfosCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

}
