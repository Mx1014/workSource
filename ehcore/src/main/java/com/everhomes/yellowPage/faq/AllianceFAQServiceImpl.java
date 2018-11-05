package com.everhomes.yellowPage.faq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseProvider;
import com.everhomes.flow.FlowLane;
import com.everhomes.flow.FlowLaneProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.common.IdNameDTO;
import com.everhomes.rest.flow.FlowCaseStatus;
import com.everhomes.rest.portal.ServiceAllianceInstanceConfig;
import com.everhomes.rest.yellowPage.AllianceCommonCommand;
import com.everhomes.rest.yellowPage.AllianceDisplayType;
import com.everhomes.rest.yellowPage.IdNameInfoDTO;
import com.everhomes.rest.yellowPage.ListOperateServicesResponse;
import com.everhomes.rest.yellowPage.ServiceAllianceWorkFlowStatus;
import com.everhomes.rest.yellowPage.UpdateOperateServiceOrdersCommand;
import com.everhomes.rest.yellowPage.UpdateOperateServicesCommand;
import com.everhomes.rest.yellowPage.YellowPageServiceErrorCode;
import com.everhomes.rest.yellowPage.faq.CreateFAQCommand;
import com.everhomes.rest.yellowPage.faq.CreateFAQTypeCommand;
import com.everhomes.rest.yellowPage.faq.DeleteFAQCommand;
import com.everhomes.rest.yellowPage.faq.DeleteFAQTypeCommand;
import com.everhomes.rest.yellowPage.faq.FAQDTO;
import com.everhomes.rest.yellowPage.faq.GetFAQOnlineServiceCommand;
import com.everhomes.rest.yellowPage.faq.GetFAQOnlineServiceResponse;
import com.everhomes.rest.yellowPage.faq.GetLatestServiceStateCommand;
import com.everhomes.rest.yellowPage.faq.GetLatestServiceStateResponse;
import com.everhomes.rest.yellowPage.faq.GetPendingServiceCountsCommand;
import com.everhomes.rest.yellowPage.faq.GetPendingServiceCountsResponse;
import com.everhomes.rest.yellowPage.faq.ListFAQTypesCommand;
import com.everhomes.rest.yellowPage.faq.ListFAQTypesResponse;
import com.everhomes.rest.yellowPage.faq.ListFAQsCommand;
import com.everhomes.rest.yellowPage.faq.ListFAQsResponse;
import com.everhomes.rest.yellowPage.faq.ListTopFAQsCommand;
import com.everhomes.rest.yellowPage.faq.ListTopFAQsResponse;
import com.everhomes.rest.yellowPage.faq.ListUiFAQsCommand;
import com.everhomes.rest.yellowPage.faq.ListUiFAQsResponse;
import com.everhomes.rest.yellowPage.faq.ListUiServiceRecordsCommand;
import com.everhomes.rest.yellowPage.faq.ListUiServiceRecordsResponse;
import com.everhomes.rest.yellowPage.faq.OperateServiceDTO;
import com.everhomes.rest.yellowPage.faq.ServiceRecordDTO;
import com.everhomes.rest.yellowPage.faq.UpdateFAQCommand;
import com.everhomes.rest.yellowPage.faq.UpdateFAQOnlineServiceCommand;
import com.everhomes.rest.yellowPage.faq.UpdateFAQSolveTimesCommand;
import com.everhomes.rest.yellowPage.faq.UpdateFAQTypeCommand;
import com.everhomes.rest.yellowPage.faq.UpdateFAQTypeOrdersCommand;
import com.everhomes.rest.yellowPage.faq.UpdateTopFAQFlagCommand;
import com.everhomes.rest.yellowPage.faq.UpdateTopFAQOrdersCommand;
import com.everhomes.rest.yellowPage.faq.updateFAQOrderCommand;
import com.everhomes.sms.DateUtil;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;
import com.everhomes.yellowPage.AllianceOperateService;
import com.everhomes.yellowPage.AllianceOperateServiceProvider;
import com.everhomes.yellowPage.AllianceStandardService;
import com.everhomes.yellowPage.ServiceAllianceApplicationRecord;
import com.everhomes.yellowPage.ServiceAllianceApplicationRecordProvider;
import com.everhomes.yellowPage.ServiceAllianceCategories;
import com.everhomes.yellowPage.ServiceAlliances;
import com.everhomes.yellowPage.YellowPageProvider;
import com.everhomes.yellowPage.YellowPageService;
import com.everhomes.yellowPage.YellowPageServiceImpl;
import com.everhomes.yellowPage.YellowPageUtils;
import com.everhomes.yellowPage.standard.ServiceCategoryMatch;

@Component
public class AllianceFAQServiceImpl implements AllianceFAQService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AllianceFAQServiceImpl.class);
	
	@Autowired
	private AllianceFAQProvider allianceFAQProvider;
	
	@Autowired
	private ConfigurationProvider configurationProvider;
	
	@Autowired
	private DbProvider dbProvider;
	@Autowired
	private ServiceAllianceApplicationRecordProvider saapplicationRecordProvider;
	@Autowired
	private FlowLaneProvider flowLaneProvider;
	@Autowired
	private FlowCaseProvider flowCaseProvider;
	@Autowired
	private AllianceStandardService allianceStandardService;
	@Autowired
	private YellowPageService yellowPageService;


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
		
		AllianceFAQType faqType = allianceFAQProvider.getFAQType(cmd.getTypeId());
		if (null == faqType) {
			YellowPageUtils.throwError(YellowPageServiceErrorCode.ERROR_FAQ_TYPE_NOT_FOUND, "faq type not found");
		}
		
		faq.setTypeName(faqType.getName());
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
		return resp;
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
		allianceFAQProvider.updateFAQSolveTimes(cmd.getFAQId(), cmd.getSolveStaus());
	}

	@Override
	public GetLatestServiceStateResponse getLatestServiceState(GetLatestServiceStateCommand cmd) {
		GetLatestServiceStateResponse resp = new GetLatestServiceStateResponse();
		ServiceAllianceApplicationRecord record = saapplicationRecordProvider.listLatestRecord(cmd,
				UserContext.currentUserId(), buildWorkingStatusList());
		if (null != record) {
			
			//获取最新服务状态
			resp.setServiceId(record.getServiceAllianceId());
			resp.setServiceName(record.getServiceOrganization());
			ServiceAllianceWorkFlowStatus currentStatus = ServiceAllianceWorkFlowStatus.fromType(record.getWorkflowStatus());
			resp.setCurrentStatus(currentStatus == null ? null : currentStatus.getDescription());
			
			// 获取泳道
			try {
				
				FlowCase flowCase = flowCaseProvider.getFlowCaseById(record.getFlowCaseId());
				List<FlowLane> flowLanes = flowLaneProvider.listFlowLane(flowCase.getFlowMainId(),
						flowCase.getFlowVersion());
				List<String> channels = flowLanes.stream().map(FlowLane::getDisplayName).collect(Collectors.toList());
				resp.setFlowCaseId(record.getFlowCaseId());
				byte index = (byte)channels.indexOf(flowCase.getCurrentLane());
				resp.setChannelPos(index < 0 ? 0 : index);
				resp.setChannels(channels);
				
			} catch (Exception e) {
				LOGGER.error("can't find flow case info record:"+StringHelper.toJsonString(record)+" e:"+e.getMessage());
			}

		}
		
		
		//获取公单addr 和常见问题addr
		ServiceAllianceCategories mainCa = allianceStandardService.queryHomePageCategoryByScene(cmd.getType(),
				cmd.getOwnerId());
		if (null != mainCa) {

			ServiceAllianceInstanceConfig config = new ServiceAllianceInstanceConfig();
			config.setDisplayType(AllianceDisplayType.HOUSE_KEEPER.getCode());
			config.setEnableComment(mainCa.getEnableComment() == null ? (byte) 0 : mainCa.getEnableComment());
			config.setType(mainCa.getType());
			
			String serviceListUrl = yellowPageService.buildAllianceUrl(UserContext.getCurrentNamespaceId(), config,
					AllianceDisplayType.HOUSE_KEEPER.getShowType());
			String topFaqUrl = yellowPageService.buildAllianceUrl(UserContext.getCurrentNamespaceId(), config,
					AllianceDisplayType.FAQ.getShowType());
			
			String homeUrl = configurationProvider.getValue("home.url", null);
			if (null != homeUrl) {
				serviceListUrl = replaceHomeUrl(serviceListUrl, homeUrl);
				topFaqUrl = replaceHomeUrl(topFaqUrl, homeUrl);
			}
			
			resp.setServiceListUrl(serviceListUrl);
			resp.setTopFAQUrl(topFaqUrl);

		}
		
		// 获取客服
		AllianceCommonCommand cmd2 = ConvertHelper.convert(cmd, AllianceCommonCommand.class);
		AllianceFAQServiceCustomer onlineService = allianceFAQProvider.getFAQOnlineService(cmd2);
		if (null != onlineService) {
			resp.setServiceCustomerId(onlineService.getUserId());
			resp.setPhoneNumber(onlineService.getHotlineNumber());
		}
		
		return resp;
	}
	
	private String replaceHomeUrl(String origin, String homeUrl) {
		String replaceStr = "${home.url}";
		int index1 = origin.indexOf(replaceStr);

		return origin.substring(0, index1) + homeUrl + origin.substring(index1 + replaceStr.length());

	}
	
	@Override
	public GetFAQOnlineServiceResponse getFAQOnlineService(GetFAQOnlineServiceCommand cmd) {
		GetFAQOnlineServiceResponse resp = new GetFAQOnlineServiceResponse();

		AllianceFAQServiceCustomer onlineService = allianceFAQProvider.getFAQOnlineService(cmd);
		if (null != onlineService) {
			resp = ConvertHelper.convert(onlineService, GetFAQOnlineServiceResponse.class);
		}

		return resp;
	}

	@Override
	public void updateFAQOnlineService(UpdateFAQOnlineServiceCommand cmd) {
		AllianceFAQServiceCustomer newOne = ConvertHelper.convert(cmd, AllianceFAQServiceCustomer.class);
		newOne.setNamespaceId(null == cmd.getNamespaceId() ? UserContext.getCurrentNamespaceId() : cmd.getNamespaceId());
		
		AllianceFAQServiceCustomer oldOne = allianceFAQProvider.getFAQOnlineService(cmd);
		if (null == oldOne) {
			allianceFAQProvider.createFAQOnlineService(newOne);
			return ;
		}
		
		allianceFAQProvider.updateFAQOnlineService(newOne);
	}

	@Override
	public GetPendingServiceCountsResponse getPendingServiceCounts(GetPendingServiceCountsCommand cmd) {
		Integer count = saapplicationRecordProvider.listRecordCounts(cmd, UserContext.currentUserId(),
				buildWorkingStatusList());
		GetPendingServiceCountsResponse resp = new GetPendingServiceCountsResponse();
		resp.setCount(count);
		return resp;
	}

	@Override
	public ListUiServiceRecordsResponse listUiServiceRecords(ListUiServiceRecordsCommand cmd) {
		ListingLocator locator = new ListingLocator();
		List<Byte> workFlowStatusList = null;
		if (null != cmd.getStatus() && ServiceAllianceWorkFlowStatus.PROCESSING.getCode() == cmd.getStatus()) {
			workFlowStatusList = buildWorkingStatusList();
		}
		
		List<ServiceAllianceApplicationRecord> records = saapplicationRecordProvider
				.listServiceAllianceApplicationRecord(cmd, cmd.getPageSize(), locator,  UserContext.currentUserId(), workFlowStatusList);
		List<ServiceRecordDTO> dtos = records.stream().map(r -> {
			ServiceRecordDTO dto = new ServiceRecordDTO();
			dto.setFlowCaseId(r.getFlowCaseId());
			dto.setServiceId(r.getServiceAllianceId());
			dto.setServiceName(r.getServiceOrganization());
			dto.setApplyTime(DateUtil.dateToStr(r.getCreateTime(), "yyyy-MM-dd HH:mm"));
			dto.setStatus(r.getWorkflowStatus());
			ServiceAllianceWorkFlowStatus wStatus = ServiceAllianceWorkFlowStatus.fromType(dto.getStatus());
			dto.setStatusName(wStatus.getDescription());
			return dto;
		}).collect(Collectors.toList());

		ListUiServiceRecordsResponse resp = new ListUiServiceRecordsResponse();
		resp.setDtos(dtos);
		resp.setNextPageAnchor(locator.getAnchor());
		return resp;
	}
	
	private List<Byte> buildWorkingStatusList() {
		return Arrays.asList(
				ServiceAllianceWorkFlowStatus.PROCESSING.getCode(),
				ServiceAllianceWorkFlowStatus.SUSPEND.getCode());
	}
	
	
	private List<FlowLane> getLanesByFlowCase(Long flowCaseId) {
		List<FlowLane> lanes = new ArrayList<>();
		FlowCase flowCase = flowCaseProvider.getFlowCaseById(flowCaseId);
		if (null != flowCase) {
			lanes = flowLaneProvider.listFlowLane(flowCase.getFlowMainId(), flowCase.getFlowVersion());
		}
		return lanes;
	}

	@Override
	public void updateFAQOrder(updateFAQOrderCommand cmd) {
		AllianceFAQ upFaq = allianceFAQProvider.getFAQ(cmd.getUpFAQId());
		AllianceFAQ lowFaq = allianceFAQProvider.getFAQ(cmd.getLowFAQId());
		if (null == upFaq ||null == lowFaq) {
			YellowPageUtils.throwError(YellowPageServiceErrorCode.ERROR_FAQ_NOT_FOUND, "faq not found");
		}		
		
		if (upFaq.getDefaultOrder() < lowFaq.getDefaultOrder()) {
			return;
		}
		
		dbProvider.execute(r->{
			allianceFAQProvider.updateFAQOrder(upFaq.getId(), lowFaq.getDefaultOrder());
			allianceFAQProvider.updateFAQOrder(lowFaq.getId(), upFaq.getDefaultOrder());
			return null;
		});
		
	}
}
