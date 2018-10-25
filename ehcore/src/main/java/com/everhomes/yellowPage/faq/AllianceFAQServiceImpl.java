package com.everhomes.yellowPage.faq;

import java.util.ArrayList;
import java.util.List;

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
		allianceFAQProvider.deleteFAQType(cmd.getFAQTypeId());
	}

	@Override
	public ListFAQTypesResponse listFAQTypes(ListFAQTypesCommand cmd) {
		return allianceFAQProvider.listFAQTypes(cmd, cmd.getPageSize(), cmd.getPageAnchor());
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
		
		GetLatestServiceStateResponse resp = new GetLatestServiceStateResponse();
		resp.setServiceId(500000028L);
		resp.setServiceName("测试");
		resp.setCurrentStatus("处理中");
		List<String> channels = new ArrayList<>();
		channels.add("开始");
		channels.add("中间1");
		channels.add("中间2");
		channels.add("中间3");
		channels.add("结束");
		
		List<String> squareInfos = new ArrayList<>();
		squareInfos.add("http://www.baidu.com");
		squareInfos.add("http://www.baidu.com");
		squareInfos.add("505389");
		squareInfos.add("0755-123456");
		resp.setChannels(channels);
		resp.setSquareInfos(squareInfos);
//		resp.set
		return resp;
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
	public GetPendingServiceCountsResponse getPendingServiceCounts(GetPendingServiceCountsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateFAQSolveTimes(UpdateFAQSolveTimesCommand cmd) {
		 
	}

	@Override
	public ListUiServiceRecordsResponse listUiServiceRecords(ListUiServiceRecordsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

}
