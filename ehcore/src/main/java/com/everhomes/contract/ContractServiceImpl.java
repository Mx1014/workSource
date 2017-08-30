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
import com.everhomes.rest.contract.BuildingApartmentDTO;
import com.everhomes.rest.contract.ContractDTO;
import com.everhomes.rest.contract.ListContractsByOraganizationIdCommand;
import com.everhomes.rest.contract.ListContractsCommand;
import com.everhomes.rest.contract.ListContractsResponse;
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
	
	@Override
	public ListContractsResponse listContracts(ListContractsCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId()==null?UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
		
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
			Long organizationId = contract.getOrganizationId();
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
			Long organizationId = contract.getOrganizationId();
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
			Long organizationId = contract.getOrganizationId();
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

	@Override
	public List<Object> findCustomerByContractNum(String contractNum) {
		return contractProvider.findCustomerByContractNum(contractNum);
	}
}