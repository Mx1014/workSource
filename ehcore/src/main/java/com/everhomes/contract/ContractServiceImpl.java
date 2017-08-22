// @formatter:off
package com.everhomes.contract;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.asset.AssetProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.customer.EnterpriseCustomer;
import com.everhomes.customer.EnterpriseCustomerProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowService;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.openapi.ContractBuildingMapping;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.contract.*;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.search.ContractSearcher;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.everhomes.appurl.AppUrlService;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.coordinator.NamedLock;
import com.everhomes.openapi.Contract;
import com.everhomes.openapi.ContractBuildingMappingProvider;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.organization.OrganizationCommunityRequest;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.appurl.AppUrlDTO;
import com.everhomes.rest.appurl.GetAppInfoCommand;
import com.everhomes.rest.organization.OrganizationServiceUser;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.OSType;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.Tuple;

@Component
public class ContractServiceImpl implements ContractService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ContractServiceImpl.class);

	@Autowired
	private ContractProvider contractProvider;
	
	@Autowired
	private ConfigurationProvider configurationProvider;
	
	@Autowired
	private ContractBuildingMappingProvider contractBuildingMappingProvider;
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private SmsProvider smsProvider;
	
	@Autowired
	private AppUrlService appUrlService;
	
	@Autowired
	private CommunityProvider communityProvider;
	
	@Autowired
	private OrganizationProvider organizationProvider;
	
	@Autowired
	private CoordinationProvider coordinationProvider;
	
	@Autowired
	private ScheduleProvider scheduleProvider;

	@Autowired
	private ContractAttachmentProvider contractAttachmentProvider;

	@Autowired
	private ContractChargingItemAddressProvider contractChargingItemAddressProvider;

	@Autowired
	private ContractChargingItemProvider contractChargingItemProvider;

	@Autowired
	private ContentServerService contentServerService;

	@Autowired
	private AddressProvider addressProvider;

	@Autowired
	private ContractSearcher contractSearcher;

	@Autowired
	private EnterpriseCustomerProvider enterpriseCustomerProvider;

	@Autowired
	private FlowService flowService;

	@Autowired
	private LocaleStringService localeStringService;

	@Autowired
	private UserProvider userProvider;

	@Autowired
	private AssetProvider assetProvider;
	
	@Override
	public ListContractsResponse listContracts(ListContractsCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId()==null?UserContext.getCurrentNamespaceId():cmd.getNamespaceId();

		if(namespaceId == 999971) {
			ContractHandler handler = PlatformContext.getComponent(ContractHandler.CONTRACT_PREFIX + namespaceId);
			ListContractsResponse response = handler.listContracts(cmd);
			return response;
		}
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		Long pageAnchor = cmd.getPageAnchor() == null?0L:cmd.getPageAnchor();
		int from = (int) (pageAnchor * pageSize);
		Long nextPageAnchor = null;
		
		List<Contract> contractList = null;
		//1. 有关键字
		if (StringUtils.isNotBlank(cmd.getBuildingName()) || StringUtils.isNotBlank(cmd.getKeywords())) {
			List<String> contractNumbers = contractBuildingMappingProvider.listContractByKeywords(namespaceId, cmd.getBuildingName(), cmd.getKeywords());
			contractNumbers.sort((t1, t2)->{
				if (t1.compareTo(t2) > 0) {
					return 1;
				}
				return -1;
			});
			if (contractNumbers.size() > from) {
				int toIndex = from + pageSize;
				if (toIndex >= contractNumbers.size()) {
					toIndex = contractNumbers.size();
				}else {
					nextPageAnchor = pageAnchor.longValue() + 1L;
				}
				contractNumbers = contractNumbers.subList(from, toIndex);
			}
			contractList = contractProvider.listContractByContractNumbers(namespaceId, contractNumbers);
		}else {
			//2. 无关键字
			contractList = contractProvider.listContractByNamespaceId(namespaceId, from, pageSize+1);
			if (contractList.size() > pageSize) {
				contractList.remove(contractList.size()-1);
				nextPageAnchor = pageAnchor.longValue() + 1;
			}
		}
		
		List<ContractDTO> resultList = contractList.stream().map(c->{
			ContractDTO contractDTO = organizationService.processContract(c, namespaceId);
//			List<BuildingApartmentDTO> buildings = contractBuildingMappingProvider.listBuildingsByContractNumber(namespaceId, contractDTO.getContractNumber());
//			contractDTO.setBuildings(buildings);
			return contractDTO;
		}).collect(Collectors.toList());
		
		return new ListContractsResponse(nextPageAnchor, resultList);
	}

	/**
	 * 合同管理定时期，每天上午跑一下，把以下三种类型的客户抓取出来发短信：
	 * 1. 租期到期前两个月
	 * 2. 租期到期前一个月
	 * 3. 新增客户当天早上10点
	 */
    @Scheduled(cron="0 0 10 * * ?")
    @Override
    public void contractSchedule(){
    	if(RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE){
	    	//使用tryEnter()防止分布式部署重复执行
	    	coordinationProvider.getNamedLock(CoordinationLocks.CONTRACT_SCHEDULE.getCode()).tryEnter(()->{
	    		sendMessageToBackTwoMonthsOrganizations();
	        	sendMessageToBackOneMonthOrganizations();
	        	sendMessageToNewOrganizations();
	    	});
    	}
    }

	private void sendMessageToBackTwoMonthsOrganizations() {
		Timestamp now = getCurrentDate();
		Timestamp lastNow = getNextNow(now);
		long offset = 60*ONE_DAY_MS;
		Timestamp minValue = new Timestamp(lastNow.getTime()+offset);
		Timestamp maxValue = new Timestamp(now.getTime()+offset);
		List<Contract> contractList = contractProvider.listContractsByEndDateRange(minValue, maxValue);
		if (contractList == null || contractList.isEmpty()) {
			return;
		}
		
		for (Contract contract : contractList) {
//			Long organizationId = contract.getOrganizationId();
			Long organizationId = contract.getCustomerId();
			OrganizationServiceUser serviceUser = organizationService.getServiceUser(organizationId);
			Set<String> phoneSet = organizationService.getOrganizationContactPhone(organizationId);
			String contractEndDate = getChinaDate(contract.getContractEndDate());
			String appName = getAppName(contract.getNamespaceId());
			if (phoneSet != null && !phoneSet.isEmpty()) {
				Integer code = null;
				List<Tuple<String, Object>> params = new ArrayList<>();
				if (serviceUser == null || StringUtils.isBlank(serviceUser.getServiceUserName()) || StringUtils.isBlank(serviceUser.getServiceUserPhone())) {
					//尊敬的客户您好，您的租期将在${contractEndDate}到期，请到“${appName1}app”办理续签等相关手续。 如未安装“${appName2}app”，请到应用市场下载安装。
					code = SmsTemplateCode.PUSH_MESSAGE_BACK_TWO_MONTHS_WITHOUT_SERVICE_USER_CODE;
					smsProvider.addToTupleList(params, "contractEndDate", contractEndDate);
					smsProvider.addToTupleList(params, "appName1", appName);
					smsProvider.addToTupleList(params, "appName2", appName);
				}else {
					//尊敬的客户您好，我是您的专属客服经理${serviceUserName}，电话${serviceUserPhone}，您的租期将在${contractEndDate}到期，请到“${appName1}app”办理续签等相关手续。 如未安装“${appName2}app”，请到应用市场下载安装。
					code = SmsTemplateCode.PUSH_MESSAGE_BACK_TWO_MONTHS_WITH_SERVICE_USER_CODE;
					smsProvider.addToTupleList(params, "serviceUserName", serviceUser.getServiceUserName());
					smsProvider.addToTupleList(params, "serviceUserPhone", serviceUser.getServiceUserPhone());
					smsProvider.addToTupleList(params, "contractEndDate", contractEndDate);
					smsProvider.addToTupleList(params, "appName1", appName);
					smsProvider.addToTupleList(params, "appName2", appName);
				}
				sendMessageToUser(contract.getNamespaceId(), phoneSet, SmsTemplateCode.SCOPE, code, params);
			}
		}
	}
	
	private void sendMessageToBackOneMonthOrganizations() {
		Timestamp now = getCurrentDate();
		Timestamp lastNow = getNextNow(now);
		long offset = 30*ONE_DAY_MS;
		Timestamp minValue = new Timestamp(lastNow.getTime()+offset);
		Timestamp maxValue = new Timestamp(now.getTime()+offset);
		List<Contract> contractList = contractProvider.listContractsByEndDateRange(minValue, maxValue);
		if (contractList == null || contractList.isEmpty()) {
			return;
		}
		
		for (Contract contract : contractList) {
//			Long organizationId = contract.getOrganizationId();
			Long organizationId = contract.getCustomerId();
			OrganizationServiceUser serviceUser = organizationService.getServiceUser(organizationId);
			Set<String> phoneSet = organizationService.getOrganizationContactPhone(organizationId);
			String contractEndDate = getChinaDate(contract.getContractEndDate());
			String appName = getAppName(contract.getNamespaceId());
			if (phoneSet != null && !phoneSet.isEmpty()) {
				Integer code = null;
				List<Tuple<String, Object>> params = new ArrayList<>();
				if (serviceUser == null || StringUtils.isBlank(serviceUser.getServiceUserName()) || StringUtils.isBlank(serviceUser.getServiceUserPhone())) {
					//尊敬的客户您好，您的租期将在${contractEndDate}到期，请到“${appName1}app”办理续签等相关手续。 如未安装“${appName2}app”，请到应用市场下载安装，如果您已经与科技园联系过，请忽略该短信。
					code = SmsTemplateCode.PUSH_MESSAGE_BACK_ONE_MONTH_WITHOUT_SERVICE_USER_CODE;
					smsProvider.addToTupleList(params, "contractEndDate", contractEndDate);
					smsProvider.addToTupleList(params, "appName1", appName);
					smsProvider.addToTupleList(params, "appName2", appName);
				}else {
					//尊敬的客户您好，我是您的专属客服经理${serviceUserName}，电话${serviceUserPhone}，您的租期将在${contractEndDate}到期，请到“${appName1}app”办理续签等相关手续。 如未安装“${appName2}app”，请到应用市场下载安装，如果您已经与科技园联系过，请忽略该短信。
					code = SmsTemplateCode.PUSH_MESSAGE_BACK_ONE_MONTH_WITH_SERVICE_USER_CODE;
					smsProvider.addToTupleList(params, "serviceUserName", serviceUser.getServiceUserName());
					smsProvider.addToTupleList(params, "serviceUserPhone", serviceUser.getServiceUserPhone());
					smsProvider.addToTupleList(params, "contractEndDate", contractEndDate);
					smsProvider.addToTupleList(params, "appName1", appName);
					smsProvider.addToTupleList(params, "appName2", appName);
				}
				sendMessageToUser(contract.getNamespaceId(), phoneSet, SmsTemplateCode.SCOPE, code, params);
			}
		}
	}

	private void sendMessageToNewOrganizations() {
		Timestamp now = getCurrentDate();
		Timestamp lastNow = getNextNow(now);
		List<Contract> contractList = contractProvider.listContractsByCreateDateRange(lastNow, now);
		if (contractList == null || contractList.isEmpty()) {
			return;
		}
		
		for (Contract contract : contractList) {
//			Long organizationId = contract.getOrganizationId();
			Long organizationId = contract.getCustomerId();
			OrganizationServiceUser serviceUser = organizationService.getServiceUser(organizationId);
			Set<String> phoneSet = organizationService.getOrganizationContactPhone(organizationId);
			String communityName = getCommunityName(organizationId);
			String appName = getAppName(contract.getNamespaceId());
			if (phoneSet != null && !phoneSet.isEmpty()) {
				Integer code = null;
				List<Tuple<String, Object>> params = new ArrayList<>();
				if (serviceUser == null || StringUtils.isBlank(serviceUser.getServiceUserName()) || StringUtils.isBlank(serviceUser.getServiceUserPhone())) {
					//尊敬的客户您好，欢迎入住${communityName}。为更好地为您做好服务，请下载安装“${appName}app”，体会指尖上的园区给您带来的便利和高效，请到应用市场下载安装。
					code = SmsTemplateCode.PUSH_MESSAGE_BACK_ONE_MONTH_WITHOUT_SERVICE_USER_CODE;
					smsProvider.addToTupleList(params, "communityName", communityName);
					smsProvider.addToTupleList(params, "appName", appName);
				}else {
					//尊敬的客户您好，我是您的专属客服经理${serviceUserName}，电话${serviceUserPhone}，欢迎入住${communityName}，有任何问题请随时与我联系。为更好地为您做好服务，请下载安装“${appName}app”，体会指尖上的园区给您带来的便利和高效，请到应用市场下载安装。
					code = SmsTemplateCode.PUSH_MESSAGE_BACK_ONE_MONTH_WITH_SERVICE_USER_CODE;
					smsProvider.addToTupleList(params, "serviceUserName", serviceUser.getServiceUserName());
					smsProvider.addToTupleList(params, "serviceUserPhone", serviceUser.getServiceUserPhone());
					smsProvider.addToTupleList(params, "communityName", communityName);
					smsProvider.addToTupleList(params, "appName", appName);
				}
				sendMessageToUser(contract.getNamespaceId(), phoneSet, SmsTemplateCode.SCOPE, code, params);
			}
		}
	}
	
	private void sendMessageToUser(Integer namespaceId, Set<String> phoneNumbers, String templateScope, int code, List<Tuple<String, Object>> params) {
		sendMessageToUser(namespaceId, phoneNumbers.toArray(new String[phoneNumbers.size()]), templateScope, code, getLocale(), params);
	}
	
	private void sendMessageToUser(Integer namespaceId, String[] phoneNumbers, String templateScope, int templateId, String templateLocale, List<Tuple<String, Object>> variables) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("send message parameters are: namespaceId="+namespaceId+", phoneNumbers="+Arrays.toString(phoneNumbers)+", templateScope="+templateScope+", code="+templateId);
		}
		
		smsProvider.sendSms(namespaceId, phoneNumbers, templateScope, templateId, templateLocale, variables);
	}
	
	private static final SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat chineDateSF = new SimpleDateFormat("yyyy年MM月dd日");
	private static final SimpleDateFormat datetimeSF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final long ONE_DAY_MS = 24*3600*1000;
	// 获取今天10点钟的这个时间
	private Timestamp getCurrentDate() {
		Date date = new Date();
		String dateStr = dateSF.format(date);
		try {
			return new Timestamp(datetimeSF.parse(dateStr+" 10:00:00").getTime());
		} catch (ParseException e) {
			return new Timestamp(date.getTime());
		}
	}

	//获取昨天上午10点这个时间
	private Timestamp getNextNow(Timestamp now) {
		return new Timestamp(now.getTime()-ONE_DAY_MS);
	}

    private String getLocale() {
        User user = UserContext.current().getUser();
        if(user != null && user.getLocale() != null)
            return user.getLocale();
        return Locale.SIMPLIFIED_CHINESE.toString();
    }
    
	private String getChinaDate(Timestamp date) {
		if (date == null) {
			return "";
		}
		return chineDateSF.format(date);
	} 
	
	private String getAppName(Integer namespaceId) {
		AppUrlDTO appUrlDTO = appUrlService.getAppInfo(new GetAppInfoCommand(namespaceId,OSType.Android.getCode()));
		if (appUrlDTO != null) {
			return appUrlDTO.getName();
		}
		return "";
	}
	
	private String getCommunityName(Long organizationId) {
		//1. 找到企业入驻的园区
		OrganizationCommunityRequest organizationCommunityRequest = organizationProvider.getOrganizationCommunityRequestByOrganizationId(organizationId);
		if (organizationCommunityRequest == null) {
			return "";
		}
		//2. 找园区
		Community community = communityProvider.findCommunityById(organizationCommunityRequest.getCommunityId());
		if (community == null) {
			return "";
		}
		return community.getName();
	}

	/**
	 * 获取某机构(一般是企业)的所有有效合同
	 * */
	@Override
	public ListContractsResponse listContractsByOraganizationId(
			ListContractsByOraganizationIdCommand cmd) {
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		List<Contract> contractList = contractProvider.listContractByOrganizationId(cmd.getOrganizationId());
		List<ContractDTO> resultList = contractList.stream().map(c->{
			ContractDTO contractDTO = organizationService.processContract(c, namespaceId);
//			List<BuildingApartmentDTO> buildings = contractBuildingMappingProvider.listBuildingsByContractNumber(namespaceId, contractDTO.getContractNumber());
//			contractDTO.setBuildings(buildings);
			return contractDTO;
		}).collect(Collectors.toList());
		
		return new ListContractsResponse(null, resultList);
	}

	private void checkContractNumberUnique(Integer namespaceId, String contractNumber) {
		Contract contract = contractProvider.findActiveContractByContractNumber(namespaceId, contractNumber);
		if(contract != null) {
			LOGGER.error("contractNumber {} in namespace {} already exist!", contractNumber, namespaceId);
			throw RuntimeErrorException.errorWith(ContractErrorCode.SCOPE, ContractErrorCode.ERROR_CONTRACTNUMBER_EXIST,
					"contractNumber is already exist");
		}
	}

	private String generateContractNumber() {
		return null;
	}

	@Override
	public ContractDetailDTO createContract(CreateContractCommand cmd) {
		Contract contract = ConvertHelper.convert(cmd, Contract.class);
		checkContractNumberUnique(cmd.getNamespaceId(), cmd.getContractNumber());
		if(cmd.getContractStartDate() != null) {
			contract.setContractStartDate(new Timestamp(cmd.getContractStartDate()));
		}
		if(cmd.getContractEndDate() != null) {
			contract.setContractEndDate(new Timestamp(cmd.getContractEndDate()));
		}
		if(cmd.getDecorateBeginDate() != null) {
			contract.setDecorateBeginDate(new Timestamp(cmd.getDecorateBeginDate()));
		}
		if(cmd.getDecorateEndDate() != null) {
			contract.setDecorateEndDate(new Timestamp(cmd.getDecorateEndDate()));
		}
		if(cmd.getSignedTime() != null) {
			contract.setSignedTime(new Timestamp(cmd.getSignedTime()));
		}
		if(cmd.getDepositTime() != null) {
			contract.setDepositTime(new Timestamp(cmd.getDepositTime()));
		}
		if(cmd.getDownpaymentTime() != null) {
			contract.setDownpaymentTime(new Timestamp(cmd.getDownpaymentTime()));
		}
		contract.setCreateUid(UserContext.currentUserId());
		contract.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		contractProvider.createContract(contract);

		dealContractApartments(contract, cmd.getApartments());
		dealContractChargingItems(contract, cmd.getChargingItems());
		dealContractAttachments(contract.getId(), cmd.getAttachments());

		contractSearcher.feedDoc(contract);
		if(ContractStatus.WAITING_FOR_APPROVAL.equals(ContractStatus.fromStatus(contract.getStatus()))) {
			addToFlowCase(contract);
		}

		FindContractCommand command = new FindContractCommand();
		command.setId(contract.getId());
		command.setPartyAId(contract.getPartyAId());
		ContractDetailDTO contractDetailDTO = findContract(command);
		return contractDetailDTO;
	}

	private void addToFlowCase(Contract contract) {
		Flow flow = flowService.getEnabledFlow(contract.getNamespaceId(), FlowConstants.CONTRACT_MODULE,
				FlowModuleType.NO_MODULE.getCode(), contract.getCommunityId(), FlowOwnerType.COMMUNITY.getCode());
		if(null == flow) {
			LOGGER.error("Enable request flow not found, moduleId={}", FlowConstants.CONTRACT_MODULE);
			throw RuntimeErrorException.errorWith(ContractErrorCode.SCOPE, ContractErrorCode.ERROR_ENABLE_FLOW,
					localeStringService.getLocalizedString(String.valueOf(ContractErrorCode.SCOPE),
							String.valueOf(ContractErrorCode.ERROR_ENABLE_FLOW),
							UserContext.current().getUser().getLocale(),"Enable request flow not found."));
		}
		CreateFlowCaseCommand createFlowCaseCommand = new CreateFlowCaseCommand();
		createFlowCaseCommand.setCurrentOrganizationId(contract.getPartyAId());
		createFlowCaseCommand.setTitle("合同申请");
		createFlowCaseCommand.setApplyUserId(contract.getCreateUid());
		createFlowCaseCommand.setFlowMainId(flow.getFlowMainId());
		createFlowCaseCommand.setFlowVersion(flow.getFlowVersion());
		createFlowCaseCommand.setReferId(contract.getId());
		createFlowCaseCommand.setReferType(EntityType.CONTRACT.getCode());
		createFlowCaseCommand.setContent(contract.getContractNumber());

		createFlowCaseCommand.setProjectId(contract.getCommunityId());
		createFlowCaseCommand.setProjectType(EntityType.COMMUNITY.getCode());

		flowService.createFlowCase(createFlowCaseCommand);
	}

	private void dealContractApartments(Contract contract, List<BuildingApartmentDTO> buildingApartments) {
		List<ContractBuildingMapping> existApartments = contractBuildingMappingProvider.listByContract(contract.getId());
		Map<Long, ContractBuildingMapping> map = new HashMap<>();
		if(existApartments != null && existApartments.size() > 0) {
			existApartments.forEach(apartment -> {
				map.put(apartment.getId(), apartment);
			});
		}
		if(buildingApartments != null && buildingApartments.size() > 0) {
			buildingApartments.forEach(buildingApartment -> {
				if(buildingApartment.getId() == null) {
					ContractBuildingMapping mapping = ConvertHelper.convert(buildingApartment, ContractBuildingMapping.class);
					mapping.setNamespaceId(contract.getNamespaceId());
//					mapping.setOrganizationName(contract.getCustomerName());
					mapping.setContractId(contract.getId());
					mapping.setContractNumber(contract.getContractNumber());
					mapping.setStatus(CommonStatus.ACTIVE.getCode());
					mapping.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
					contractBuildingMappingProvider.createContractBuildingMapping(mapping);
				} else {
					map.remove(buildingApartment.getId());
				}
			});
		}
		if(map.size() > 0) {
			map.forEach((id, apartment) -> {
				contractBuildingMappingProvider.deleteContractBuildingMapping(apartment);
			});
		}
	}

	private void dealContractChargingItems(Contract contract, List<ContractChargingItemDTO> chargingItems) {
		List<ContractChargingItem> existChargingItems = contractChargingItemProvider.listByContractId(contract.getId());
		Map<Long, ContractChargingItem> map = new HashMap<>();
		if(existChargingItems != null && existChargingItems.size() > 0) {
			existChargingItems.forEach(item -> {
				map.put(item.getId(), item);
			});
		}

		if(chargingItems != null && chargingItems.size() > 0) {
			chargingItems.forEach(item -> {
				ContractChargingItem contractChargingItem = ConvertHelper.convert(item, ContractChargingItem.class);
				if(item.getId() == null) {
					contractChargingItem.setContractId(contract.getId());
					contractChargingItem.setNamespaceId(contract.getNamespaceId());
					contractChargingItem.setStatus(CommonStatus.ACTIVE.getCode());
					contractChargingItemProvider.createContractChargingItem(contractChargingItem);
					dealContractChargingItemAddresses(contractChargingItem, item.getApartments());
				} else {
					ContractChargingItem exist = contractChargingItemProvider.findById(item.getId());
					contractChargingItem.setCreateUid(exist.getCreateUid());
					contractChargingItem.setCreateTime(exist.getCreateTime());
					contractChargingItemProvider.updateContractChargingItem(contractChargingItem);
					map.remove(item.getId());
				}
			});
		}
		if(map.size() > 0) {
			map.forEach((id, item) -> {
				contractChargingItemProvider.deleteContractChargingItem(item);
			});
		}
	}

	private void dealContractChargingItemAddresses(ContractChargingItem item, List<BuildingApartmentDTO> addresses) {
		List<ContractChargingItemAddress> existItemAddresses = contractChargingItemAddressProvider.findByItemId(item.getId());
		Map<Long, ContractChargingItemAddress> map = new HashMap<>();
		if(existItemAddresses != null && existItemAddresses.size() > 0) {
			existItemAddresses.forEach(address -> {
				map.put(address.getId(), address);
			});
		}

		if(addresses != null && addresses.size() > 0) {
			addresses.forEach(itemAddress -> {
				if(itemAddress.getId() == null) {
					ContractChargingItemAddress address = ConvertHelper.convert(itemAddress, ContractChargingItemAddress.class);
					address.setContractChargingItemId(item.getId());
					address.setNamespaceId(item.getNamespaceId());
					address.setStatus(CommonStatus.ACTIVE.getCode());
					contractChargingItemAddressProvider.createContractChargingItemAddress(address);
				} else {
					map.remove(itemAddress.getId());
				}
			});
		}
		if(map.size() > 0) {
			map.forEach((id, itemAddress) -> {
				itemAddress.setStatus(CommonStatus.INACTIVE.getCode());
				contractChargingItemAddressProvider.updateContractChargingItemAddress(itemAddress);
			});
		}
	}

	private void dealContractAttachments(Long contractId, List<ContractAttachmentDTO> attachments) {
		List<ContractAttachment> existAttachments = contractAttachmentProvider.listByContractId(contractId);
		Map<Long, ContractAttachment> map = new HashMap<>();
		if(existAttachments != null && existAttachments.size() > 0) {
			existAttachments.forEach(attachment -> {
				map.put(attachment.getId(), attachment);
			});
		}

		if(attachments != null && attachments.size() > 0) {
			attachments.forEach(attachment -> {
				if(attachment.getId() == null) {
					ContractAttachment contractAttachment = ConvertHelper.convert(attachment, ContractAttachment.class);
					contractAttachment.setContractId(contractId);
					contractAttachmentProvider.createContractAttachment(contractAttachment);
				} else {
					map.remove(attachment.getId());
				}
			});
		}
		if(map.size() > 0) {
			map.forEach((id, attachment) -> {
				contractAttachmentProvider.deleteContractAttachment(attachment);
			});
		}
	}

	@Override
	public ContractDTO updateContract(UpdateContractCommand cmd) {
		Contract exist = checkContract(cmd.getId());
		Contract contract = ConvertHelper.convert(cmd, Contract.class);
		if(cmd.getContractStartDate() != null) {
			contract.setContractStartDate(new Timestamp(cmd.getContractStartDate()));
		}
		if(cmd.getContractEndDate() != null) {
			contract.setContractEndDate(new Timestamp(cmd.getContractEndDate()));
		}
		if(cmd.getDecorateBeginDate() != null) {
			contract.setDecorateBeginDate(new Timestamp(cmd.getDecorateBeginDate()));
		}
		if(cmd.getDecorateEndDate() != null) {
			contract.setDecorateEndDate(new Timestamp(cmd.getDecorateEndDate()));
		}
		if(cmd.getSignedTime() != null) {
			contract.setSignedTime(new Timestamp(cmd.getSignedTime()));
		}
		if(cmd.getDepositTime() != null) {
			contract.setDepositTime(new Timestamp(cmd.getDepositTime()));
		}
		if(cmd.getDownpaymentTime() != null) {
			contract.setDownpaymentTime(new Timestamp(cmd.getDownpaymentTime()));
		}
		contract.setCreateTime(exist.getCreateTime());
		contract.setCreateUid(exist.getCreateUid());
		contractProvider.updateContract(contract);

		dealContractApartments(contract, cmd.getApartments());
		dealContractChargingItems(contract, cmd.getChargingItems());
		dealContractAttachments(contract.getId(), cmd.getAttachments());

		contractSearcher.feedDoc(contract);
		if(ContractStatus.WAITING_FOR_APPROVAL.equals(ContractStatus.fromStatus(contract.getStatus()))) {
			addToFlowCase(contract);
		}
		return ConvertHelper.convert(contract, ContractDTO.class);
	}

	@Override
	public void denunciationContract(DenunciationContractCommand cmd) {

	}

	@Override
	public void deleteContract(DeleteContractCommand cmd) {
		Contract contract = checkContract(cmd.getId());
		contract.setStatus(ContractStatus.INACTIVE.getCode());

		contractProvider.updateContract(contract);
		contractSearcher.feedDoc(contract);
	}

	@Override
	public ContractDetailDTO findContract(FindContractCommand cmd) {
		Contract contract = checkContract(cmd.getId());
		ContractDetailDTO dto = ConvertHelper.convert(contract, ContractDetailDTO.class);
		User creator = userProvider.findUserById(dto.getCreateUid());
		if(creator != null) {
			dto.setCreatorName(creator.getNickName());
		}

		EnterpriseCustomer customer = enterpriseCustomerProvider.findById(dto.getCustomerId());
		if(customer != null) {
			dto.setCustomerName(customer.getName());
		}
		if(contract.getParentId() != null) {
			Contract parentContract = contractProvider.findContractById(contract.getParentId());
			if(parentContract != null) {
				dto.setParentContractNumber(parentContract.getContractNumber());
			}
		}
		if(contract.getRootParentId() != null) {
			Contract rootContract = contractProvider.findContractById(contract.getRootParentId());
			if(rootContract != null) {
				dto.setRootContractNumber(rootContract.getContractNumber());
			}
		}
		processContractApartments(dto);
		processContractChargingItems(dto);
		processContractAttachments(dto);
		return dto;
	}

	@Override
	public ListContractsResponse listCustomerContracts(ListCustomerContractsCommand cmd) {
		//姑且认为同一个域空间 同一个园区的客户和企业如果同名则是同一个 by xiongying20170822
		return null;
	}

	private Contract checkContract(Long id) {
		Contract contract = contractProvider.findContractById(id);
		if(contract == null) {
			LOGGER.error("contract is not exit! id: {}", id);
			throw RuntimeErrorException.errorWith(ContractErrorCode.SCOPE, ContractErrorCode.ERROR_CONTRACT_NOT_EXIST,
					"contract is not exit");
		}
		return contract;
	}

	private void processContractApartments(ContractDetailDTO dto) {
		List<ContractBuildingMapping> contractApartments = contractBuildingMappingProvider.listByContract(dto.getId());
		if(contractApartments != null && contractApartments.size() > 0) {
			List<BuildingApartmentDTO> apartmentDtos = contractApartments.stream().map(apartment -> {
				BuildingApartmentDTO apartmentDto = ConvertHelper.convert(apartment, BuildingApartmentDTO.class);
				return apartmentDto;
			}).collect(Collectors.toList());
			dto.setApartments(apartmentDtos);
		}
	}

	private void processContractChargingItems(ContractDetailDTO dto) {
		List<ContractChargingItem> contractChargingItems = contractChargingItemProvider.listByContractId(dto.getId());
		if(contractChargingItems != null && contractChargingItems.size() > 0) {
			List<ContractChargingItemDTO> chargingItemsDto = contractChargingItems.stream().map(item -> {
				ContractChargingItemDTO itemDto = ConvertHelper.convert(item, ContractChargingItemDTO.class);
//				assetProvider.findChargingItemById(itemDto.getChargingItemId());
				processContractChargingItemAddresses(itemDto);
				return itemDto;
			}).collect(Collectors.toList());
			dto.setChargingItems(chargingItemsDto);
		}
	}

	private void processContractChargingItemAddresses(ContractChargingItemDTO dto) {
		List<ContractChargingItemAddress> itemAddresses = contractChargingItemAddressProvider.findByItemId(dto.getId());
		if(itemAddresses != null && itemAddresses.size() > 0) {
			List<BuildingApartmentDTO> addressDtos = null;
			List<Long> addressIds = new ArrayList<>();
			itemAddresses.forEach(address -> {
				addressIds.add(address.getAddressId());
			});

			//一把取出关联的门牌地址
			List<Address> addresses =  addressProvider.listAddressByIds(dto.getNamespaceId(), addressIds);
			Map<Long, Address> addressMap = new HashMap<>();
			if(addresses != null && addresses.size() > 0) {
				addresses.forEach(address -> {
					addressMap.put(address.getId(), address);
				});
			}

			itemAddresses.forEach(itemAddress -> {
				BuildingApartmentDTO apartmentDto = new BuildingApartmentDTO();
				apartmentDto.setId(itemAddress.getId());
				apartmentDto.setAddressId(itemAddress.getAddressId());
				Address address = addressMap.get(itemAddress.getAddressId());
				if(address != null) {
					apartmentDto.setApartmentName(address.getApartmentName());
					apartmentDto.setBuildingName(address.getBuildingName());
				}
				addressDtos.add(apartmentDto);
			});

			dto.setApartments(addressDtos);
		}
	}

	private void processContractAttachments(ContractDetailDTO dto) {
		List<ContractAttachment> contractAttachments = contractAttachmentProvider.listByContractId(dto.getId());
		if(contractAttachments != null && contractAttachments.size() > 0) {
			List<ContractAttachmentDTO> dtos = contractAttachments.stream().map(attachment -> {
				ContractAttachmentDTO attachmentDto = ConvertHelper.convert(attachment, ContractAttachmentDTO.class);
				if(attachmentDto.getContentUri() != null) {
					String contentUrl = contentServerService.parserUri(attachmentDto.getContentUri(), EntityType.CONTRACT.getCode(), dto.getId());
					attachmentDto.setContentUrl(contentUrl);
				}
				return attachmentDto;
			}).collect(Collectors.toList());
			dto.setAttachments(dtos);
		}
	}
}