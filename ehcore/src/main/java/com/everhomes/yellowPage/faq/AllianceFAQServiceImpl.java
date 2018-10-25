package com.everhomes.yellowPage.faq;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.elasticsearch.common.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.common.IdNameDTO;
import com.everhomes.rest.yellowPage.YellowPageServiceErrorCode;
import com.everhomes.util.ConvertHelper;
import com.everhomes.yellowPage.ListUiFAQsCommand;
import com.everhomes.yellowPage.YellowPageUtils;

@Component
public class AllianceFAQServiceImpl implements AllianceFAQService{
	
	@Autowired
	private AllianceFAQProvider allianceFAQProvider;
	
	@Autowired
	private ConfigurationProvider configurationProvider;

	@Override
	public void createFAQType(CreateFAQTypeCommand cmd) {
		AllianceFAQType faqType = ConvertHelper.convert(cmd, AllianceFAQType.class);
		faqType.setName(cmd.getFAQTypeName());
		allianceFAQProvider.createFAQType(faqType);
	}

	@Override
	public void updateFAQType(UpdateFAQTypeCommand cmd) {
		AllianceFAQType faqType = allianceFAQProvider.getFAQType(cmd.getFAQTypeId());
		if (null == faqType) {
			YellowPageUtils.throwError(YellowPageServiceErrorCode.ERROR_FAQ_TYPE_NOT_FOUND, "faq type not found");
		}
		
		faqType.setName(cmd.getFAQTypeName());
		allianceFAQProvider.updateFAQType(faqType);
	}

	@Override
	public void deleteFAQType(DeleteFAQTypeCommand cmd) {
		allianceFAQProvider.deleteFAQType(cmd.getFAQTypeId());
	}

	@Override
	public ListFAQTypesResponse listFAQTypes(ListFAQTypesCommand cmd) {
		ListingLocator locator = new ListingLocator();
		List<AllianceFAQType> faqTypes = allianceFAQProvider.listFAQTypes(cmd, locator, cmd.getPageSize(),
				cmd.getPageAnchor());

		// 组织
		List<IdNameDTO> dtos = faqTypes.stream().map(r -> {
			IdNameDTO dto = new IdNameDTO();
			dto.setId(r.getId());
			dto.setName(r.getName());
			return dto;
		}).collect(Collectors.toList());

		// 返回
		ListFAQTypesResponse resp = new ListFAQTypesResponse();
		resp.setDtos(dtos);
		resp.setNextPageAnchor(locator.getAnchor());
		return resp;
	}

	@Override
	public void createFAQ(CreateFAQCommand cmd) {
		AllianceFAQ faq = ConvertHelper.convert(cmd, AllianceFAQ.class);
		allianceFAQProvider.createFAQ(faq);
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
		
		String prefix = "alliance37.";
		String serviceIdStr = configurationProvider.getValue(prefix+"serviceId", null);
		Long serviceId = serviceIdStr == null ? null : Long.parseLong(serviceIdStr);
		String serviceName = configurationProvider.getValue(prefix+"serviceName", null);
		String currentStatus = configurationProvider.getValue(prefix+"currentStatus", null);
		
		List<String> midChannel = new ArrayList<>();
		for (int i = 1; i < 100; i++) {
			String channel = configurationProvider.getValue(prefix+"channel"+i, null);
			if (StringUtils.isBlank(channel)) {
				break;
			}
			midChannel.add(channel);
		}
		
		
		GetLatestServiceStateResponse resp = new GetLatestServiceStateResponse();
		resp.setServiceId(serviceId);
		resp.setServiceName(serviceName);
		resp.setCurrentStatus(currentStatus);
		List<String> channels = new ArrayList<>();
		channels.add("开始");
		channels.addAll(midChannel);
		channels.add("结束");
		
		List<String> squareInfos = new ArrayList<>();
		squareInfos.add(configurationProvider.getValue(prefix+"squareInfos"+1, "http://www.baidu.com"));
		squareInfos.add(configurationProvider.getValue(prefix+"squareInfos"+2, "http://www.baidu.com"));
		squareInfos.add(configurationProvider.getValue(prefix+"squareInfos"+3, "505389"));
		squareInfos.add(configurationProvider.getValue(prefix+"squareInfos"+4, "0755-331234"));
		resp.setChannels(channels);
		resp.setServiceListUrl(configurationProvider.getValue(prefix+"squareInfos"+1, "http://www.baidu.com"));
		resp.setTopFAQUrl(configurationProvider.getValue(prefix+"squareInfos"+2, "http://www.baidu.com"));
		resp.setServiceCustomerId(Long.parseLong(configurationProvider.getValue(prefix+"squareInfos"+3, "505389")));
		resp.setPhoneNumber(configurationProvider.getValue(prefix+"squareInfos"+4, "0755-331234"));
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
