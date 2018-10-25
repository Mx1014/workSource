package com.everhomes.yellowPage.faq;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.elasticsearch.common.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.common.IdNameDTO;
import com.everhomes.rest.yellowPage.AllianceCommonCommand;
import com.everhomes.rest.yellowPage.IdNameInfoDTO;
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
	
	@Autowired
	private DbProvider dbProvider;

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
	public void updateFAQTypeOrders(UpdateFAQTypeOrdersCommand cmd) {
		AllianceFAQType upFaqType = allianceFAQProvider.getFAQType(cmd.getUpFAQTypeId());
		AllianceFAQType lowFaqType = allianceFAQProvider.getFAQType(cmd.getLowFAQTypeId());
		if (null == upFaqType ||null == lowFaqType) {
			YellowPageUtils.throwError(YellowPageServiceErrorCode.ERROR_FAQ_TYPE_NOT_FOUND, "faq type not found");
		}		
		
		if (upFaqType.getDefaultOrder() < lowFaqType.getDefaultOrder()) {
			return;
		}
		
		dbProvider.execute(r->{
			allianceFAQProvider.updateFAQTypeOrder(upFaqType.getId(), lowFaqType.getDefaultOrder());
			allianceFAQProvider.updateFAQTypeOrder(lowFaqType.getId(), upFaqType.getDefaultOrder());
			return null;
		});
		
	}


	@Override
	public void createFAQ(CreateFAQCommand cmd) {
		AllianceFAQ faq = ConvertHelper.convert(cmd, AllianceFAQ.class);
		allianceFAQProvider.createFAQ(faq);
	}

	@Override
	public void updateFAQ(UpdateFAQCommand cmd) {
		AllianceFAQ faq = allianceFAQProvider.getFAQ(cmd.getFAQId());
		if (null == faq) {
			YellowPageUtils.throwError(YellowPageServiceErrorCode.ERROR_FAQ_NOT_FOUND, "faq not found");
		}
		faq.setTitle(cmd.getTitle());
		faq.setContent(cmd.getContent());
		faq.setTypeId(cmd.getTypeId());
		faq.setTypeName(cmd.getTypeName());
		allianceFAQProvider.updateFAQ(faq);
	}

	@Override
	public void updateTopFAQFlag(UpdateTopFAQFlagCommand cmd) {
		AllianceFAQ faq = allianceFAQProvider.getFAQ(cmd.getFAQId());
		if (null == faq) {
			YellowPageUtils.throwError(YellowPageServiceErrorCode.ERROR_FAQ_NOT_FOUND, "faq not found");
		}
		
		if (faq.getTopFlag().equals(cmd.getTopFlag())) {
			return;
		}
		
		allianceFAQProvider.updateTopFAQFlag(cmd.getFAQId(), cmd.getTopFlag());
	}

	@Override
	public void deleteFAQ(DeleteFAQCommand cmd) {
		allianceFAQProvider.deleteFAQ(cmd.getFAQId());
	}

	@Override
	public ListFAQsResponse listFAQs(ListFAQsCommand cmd) {
		ListingLocator locator = new ListingLocator();
		List<AllianceFAQ> faqs = allianceFAQProvider.listFAQs(cmd, locator, cmd.getPageSize(), cmd.getPageAnchor(), cmd.getFAQType(), cmd.getTopFlag(), cmd.getKeyword(), cmd.getOrderType(), cmd.getSortType());

		// 组织
		List<FAQDTO> dtos = faqs.stream().map(r -> {
			return ConvertHelper.convert(r, FAQDTO.class);
		}).collect(Collectors.toList());

		// 返回
		ListFAQsResponse resp = new ListFAQsResponse();
		resp.setDtos(dtos);
		resp.setNextPageAnchor(locator.getAnchor());
		return resp;
	}

	@Override
	public ListTopFAQsResponse listTopFAQs(ListTopFAQsCommand cmd) {
		List<AllianceFAQ> faqs = allianceFAQProvider.listTopFAQs(cmd, null, null, null);
		List<IdNameDTO> dtos = faqs.stream().map(r -> {
			IdNameDTO dto = new IdNameDTO();
			dto.setId(r.getId());
			dto.setName(r.getTitle());
			return dto;
		}).collect(Collectors.toList());
		ListTopFAQsResponse resp = new ListTopFAQsResponse();
		resp.setDtos(dtos);
		return null;
	}

	@Override
	public void updateTopFAQOrders(UpdateTopFAQOrdersCommand cmd) {
		AllianceFAQ upFaq = allianceFAQProvider.getFAQ(cmd.getUpFAQId());
		AllianceFAQ lowFaq = allianceFAQProvider.getFAQ(cmd.getLowFAQId());
		if (null == upFaq ||null == lowFaq) {
			YellowPageUtils.throwError(YellowPageServiceErrorCode.ERROR_FAQ_NOT_FOUND, "faq not found");
		}		
		
		if (upFaq.getTopOrder() < lowFaq.getTopOrder()) {
			return;
		}
		
		dbProvider.execute(r->{
			allianceFAQProvider.updateTopFAQOrder(upFaq.getId(), lowFaq.getTopOrder());
			allianceFAQProvider.updateTopFAQOrder(lowFaq.getId(), upFaq.getTopOrder());
			return null;
		});
		
	}
	
	@Override
	public ListUiFAQsResponse listUiFAQs(ListUiFAQsCommand cmd) {
		ListUiFAQsResponse resp = new ListUiFAQsResponse();
		List<IdNameInfoDTO> dtos = new ArrayList<>();
		if (null != cmd.getFAQId()) {
			AllianceFAQ faq = allianceFAQProvider.getFAQ(cmd.getFAQId());
			if (null != faq) {
				IdNameInfoDTO dto = new IdNameInfoDTO();
				dto.setId(faq.getId());
				dto.setName(faq.getTitle());
				dto.setContent(faq.getContent());
				dtos.add(dto);
			}

			resp.setDtos(dtos);
			return resp;
		}

		if (null != cmd.getFAQTypeId()) {
			List<AllianceFAQ> faqs = listFAQByType(cmd, cmd.getFAQTypeId());
			dtos = faqs.stream().map(r -> {
				IdNameInfoDTO dto = new IdNameInfoDTO();
				dto.setId(r.getId());
				dto.setName(r.getTitle());
				return dto;
			}).collect(Collectors.toList());
			resp.setDtos(dtos);
			return resp;
		}

		List<AllianceFAQType> faqTypes = allianceFAQProvider.listFAQTypes(cmd, null, null, null);
		dtos = faqTypes.stream().map(r -> {
			IdNameInfoDTO dto = new IdNameInfoDTO();
			dto.setId(r.getId());
			dto.setName(r.getName());
			return dto;
		}).collect(Collectors.toList());
		resp.setDtos(dtos);
		return resp;
	}
	
	private List<AllianceFAQ> listFAQByType(AllianceCommonCommand cmd, Long faqTypeId) {
		return allianceFAQProvider.listFAQs(cmd, null, null, null,  faqTypeId, null, null, null, null);
	}


	@Override
	public void updateFAQSolveTimes(UpdateFAQSolveTimesCommand cmd) {
//		private Long FAQId;
//		private Byte solveStaus;
//		allianceFAQProvider.updateFAQSolveTimes
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
	public GetFAQOnlineServiceResponse getFAQOnlineService(GetFAQOnlineServiceCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateFAQOnlineService(UpdateFAQOnlineServiceCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public GetPendingServiceCountsResponse getPendingServiceCounts(GetPendingServiceCountsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ListUiServiceRecordsResponse listUiServiceRecords(ListUiServiceRecordsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}
}
