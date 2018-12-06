// @formatter:off
package com.everhomes.contract;

import static com.everhomes.util.RuntimeErrorException.errorWith;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.TransactionStatus;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.appurl.AppUrlService;
import com.everhomes.asset.AssetPaymentConstants;
import com.everhomes.asset.AssetProvider;
import com.everhomes.asset.AssetService;
import com.everhomes.asset.PaymentBillGroup;
import com.everhomes.asset.bill.AssetBillService;
import com.everhomes.asset.group.AssetGroupProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.community.Building;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.contract.template.GetKeywordsUtils;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.customer.*;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.filedownload.TaskService;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowService;
import com.everhomes.gogs.*;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.openapi.*;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationCommunityRequest;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.organization.OrganizationOwner;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.organization.pm.CommunityAddressMapping;
import com.everhomes.organization.pm.PropertyMgrProvider;
import com.everhomes.organization.pm.PropertyMgrService;
import com.everhomes.portal.PortalService;
import com.everhomes.quality.QualityConstant;
import com.everhomes.requisition.Requisition;
import com.everhomes.requisition.RequisitionProvider;
import com.everhomes.rest.acl.ListServiceModuleAdministratorsCommand;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.address.GetApartmentDetailCommand;
import com.everhomes.rest.address.GetApartmentDetailResponse;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.appurl.AppUrlDTO;
import com.everhomes.rest.appurl.GetAppInfoCommand;
import com.everhomes.rest.asset.*;

import com.everhomes.rest.asset.ChargingVariables;
import com.everhomes.rest.asset.bill.BatchDeleteBillFromContractCmd;
import com.everhomes.rest.asset.bill.BatchDeleteBillFromContractDTO;
import com.everhomes.rest.asset.bill.CheckContractIsProduceBillCmd;
import com.everhomes.rest.asset.bill.CheckContractIsProduceBillDTO;
import com.everhomes.rest.asset.bill.ListBatchDeleteBillFromContractResponse;
import com.everhomes.rest.asset.bill.ListCheckContractIsProduceBillResponse;
import com.everhomes.rest.asset.calculate.AssetOneTimeBillStatus;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.common.SyncDataResponse;
import com.everhomes.rest.community.CommunityServiceErrorCode;
import com.everhomes.rest.contract.*;
import com.everhomes.rest.customer.CustomerType;
import com.everhomes.rest.customer.SyncCustomersCommand;
import com.everhomes.rest.customer.SyncDataTaskType;
import com.everhomes.rest.filedownload.TaskRepeatFlag;
import com.everhomes.rest.filedownload.TaskType;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowCaseEntityType;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.investment.CustomerLevelType;
import com.everhomes.rest.investment.InvitedCustomerType;
import com.everhomes.rest.launchpad.ActionType;
import com.everhomes.rest.openapi.OrganizationDTO;
import com.everhomes.rest.openapi.shenzhou.DataType;
import com.everhomes.rest.organization.OrganizationContactDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationServiceUser;
import com.everhomes.rest.organization.pm.AddressMappingStatus;
import com.everhomes.rest.portal.ContractInstanceConfig;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.rest.varField.FieldDTO;
import com.everhomes.rest.varField.ListFieldCommand;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.search.ContractSearcher;
import com.everhomes.search.EnterpriseCustomerSearcher;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.*;
import com.everhomes.util.*;
import com.everhomes.util.excel.ExcelUtils;
import com.everhomes.varField.FieldItem;
import com.everhomes.varField.FieldProvider;
import com.everhomes.varField.FieldService;
import com.everhomes.varField.ScopeFieldItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.OutputStream;
import java.lang.reflect.Field;

public class DefaultContractServiceImpl implements ContractService, ApplicationListener<ContextRefreshedEvent> {
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(DefaultContractServiceImpl.class);

	@Autowired
	protected ContractProvider contractProvider;
	
	@Autowired
	protected ConfigurationProvider configurationProvider;
	
	@Autowired
	protected ContractBuildingMappingProvider contractBuildingMappingProvider;
	
	@Autowired
	protected OrganizationService organizationService;
	
	@Autowired
	protected SmsProvider smsProvider;
	
	@Autowired
	protected AppUrlService appUrlService;
	
	@Autowired
	protected CommunityProvider communityProvider;
	
	@Autowired
	protected OrganizationProvider organizationProvider;
	
	@Autowired
	protected CoordinationProvider coordinationProvider;
	
	@Autowired
	protected ScheduleProvider scheduleProvider;

	@Autowired
	protected ContractPaymentPlanProvider contractPaymentPlanProvider;

	@Autowired
	protected ContractAttachmentProvider contractAttachmentProvider;

	@Autowired
	protected ContractChargingItemAddressProvider contractChargingItemAddressProvider;

	@Autowired
	protected ContractChargingItemProvider contractChargingItemProvider;

	@Autowired
	protected ContentServerService contentServerService;

	@Autowired
	protected AddressProvider addressProvider;

	@Autowired
	protected ContractSearcher contractSearcher;

	@Autowired
	protected EnterpriseCustomerProvider enterpriseCustomerProvider;

	@Autowired
	protected FlowService flowService;

	@Autowired
	protected LocaleStringService localeStringService;

	@Autowired
	protected UserProvider userProvider;

	@Autowired
	protected AssetProvider assetProvider;

	@Autowired
	protected AssetService assetService;

	@Autowired
	protected PropertyMgrProvider propertyMgrProvider;

	@Autowired
	protected IndividualCustomerProvider individualCustomerProvider;

	@Autowired
	protected FieldProvider fieldProvider;

	@Autowired
	protected ContractChargingChangeProvider contractChargingChangeProvider;
	@Autowired
	protected ContractChargingChangeAddressProvider contractChargingChangeAddressProvider;

	@Autowired
	protected PropertyMgrService propertyMgrService;

	@Autowired
	protected UserPrivilegeMgr userPrivilegeMgr;

	@Autowired
	protected RolePrivilegeService rolePrivilegeService;

	@Autowired
	protected DbProvider dbProvider;

	protected String flowcaseContractOwnerType = FlowOwnerType.CONTRACT.getCode();
	protected String flowcasePaymentContractOwnerType = FlowOwnerType.PAYMENT_CONTRACT.getCode();

	@Autowired
	protected SyncDataTaskService syncDataTaskService;

	@Autowired
	protected ZjSyncdataBackupProvider zjSyncdataBackupProvider;

	@Autowired
	protected RequisitionProvider requisitionProvider;
	
	//多入口相关
	@Autowired
	ServiceModuleAppService serviceModuleAppService;
	
	@Autowired
    protected FieldService fieldService;
	
	@Autowired
	protected CustomerService customerService;

	@Autowired
	protected EnterpriseCustomerSearcher enterpriseCustomerSearcher;
	
	@Autowired
    protected GogsService gogsService;
	
	@Autowired
    protected UserService userService;

	@Autowired
	protected TaskService taskService;
	
	@Autowired
    private PortalService portalService;

	@Autowired
	private AssetGroupProvider assetGroupProvider;
	
	@Autowired
	private AssetBillService assetBillService;

	final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

	@Value("${equipment.ip}")
	protected String equipmentIp;

	protected void checkContractAuth(Integer namespaceId, Long privilegeId, Long orgId, Long communityId) {
		userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), orgId, privilegeId, ServiceModuleConstants.CONTRACT_MODULE, ActionType.OFFICIAL_URL.getCode(), null, null, communityId);
	}
	
    // 升级平台包到1.0.1，把@PostConstruct换成ApplicationListener，
    // 因为PostConstruct存在着平台PlatformContext.getComponent()会有空指针问题 by lqs 20180516
	//@PostConstruct
	public void setup(){
		String triggerName = ContractScheduleJob.SCHEDELE_NAME + System.currentTimeMillis();
		String jobName = triggerName;
		String cronExpression = ContractScheduleJob.CRON_EXPRESSION;
		//启动定时任务
		scheduleProvider.scheduleCronJob(triggerName, jobName, cronExpression, ContractScheduleJob.class, null);
	}

    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            setup();
        }
    }
	
	@Override
	public ListContractsResponse listContracts(ListContractsCommand cmd) {
		//多入口相关，接口内部修改权限校验
	    // 科技园的合同是特殊对接的，是另外一个模块，后面的逻辑是特殊处理的，没有类型ID此时会报空指针，故需要先判断再使用 by lqs 20180629
	    //String categoryId = (cmd.getCategoryId() == null) ? null : cmd.getCategoryId().toString();
		//List<ServiceModuleApp> serviceModuleApp = serviceModuleAppService.listReleaseServiceModuleApp(cmd.getNamespaceId(), 21200L, null, categoryId, null);
		//获取数组第一个对象，取其中的originId字段作为appId，再调用userPrivilegeMgr.checkUserPrivilege接口进行权限校验（当前管理公司ID由前端传过来）
		//Long appId = serviceModuleApp.get(0).getOriginId();
		//userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), cmd.getOrgId(), ServiceModuleConstants.CONTRACT_MODULE, appId, cmd.getOrgId(), cmd.getCommunityId());
		
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		if(namespaceId != 1000000) {
			checkContractAuth(namespaceId, PrivilegeConstants.CONTRACT_LIST, cmd.getOrgId(), cmd.getCommunityId());
		}
//		if(namespaceId == 999971) {
//			ThirdPartContractHandler handler = PlatformContext.getComponent(ThirdPartContractHandler.CONTRACT_PREFIX + namespaceId);
//			ListContractsResponse response = handler.listContracts(cmd);
//			return response;
//		}
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
			contractList = contractProvider.listContractByContractNumbers(namespaceId, contractNumbers, cmd.getCategoryId());
		}else {
			//2. 无关键字
			contractList = contractProvider.listContractByNamespaceId(namespaceId, from, pageSize+1, cmd.getCategoryId());
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

	    		sendMessageToBackTwoMonthsOrganizations(1000000);
	        	sendMessageToBackOneMonthOrganizations(1000000);
	        	sendMessageToNewOrganizations(1000000);
	    	});
    	}
    }

	protected void sendMessageToBackTwoMonthsOrganizations(Integer namespaceId) {
		Timestamp now = getCurrentDate();
		Timestamp lastNow = getNextNow(now);
		long offset = 60*ONE_DAY_MS;
		Timestamp minValue = new Timestamp(lastNow.getTime()+offset);
		Timestamp maxValue = new Timestamp(now.getTime()+offset);
		List<Contract> contractList = contractProvider.listContractsByEndDateRange(minValue, maxValue, namespaceId);
		if (contractList == null || contractList.isEmpty()) {
			return;
		}
		
		for (Contract contract : contractList) {
			Long organizationId = 0L;
			if(CustomerType.ORGANIZATION.equals(CustomerType.fromStatus(contract.getCustomerType()))) {
				organizationId = contract.getCustomerId();
			}

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
	
	protected void sendMessageToBackOneMonthOrganizations(Integer namespaceId) {
		Timestamp now = getCurrentDate();
		Timestamp lastNow = getNextNow(now);
		long offset = 30*ONE_DAY_MS;
		Timestamp minValue = new Timestamp(lastNow.getTime()+offset);
		Timestamp maxValue = new Timestamp(now.getTime()+offset);
		List<Contract> contractList = contractProvider.listContractsByEndDateRange(minValue, maxValue, namespaceId);
		if (contractList == null || contractList.isEmpty()) {
			return;
		}
		
		for (Contract contract : contractList) {
			Long organizationId = 0L;
			if(CustomerType.ORGANIZATION.equals(CustomerType.fromStatus(contract.getCustomerType()))) {
				organizationId = contract.getCustomerId();
			}
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

	protected void sendMessageToNewOrganizations(Integer namespaceId) {
		Timestamp now = getCurrentDate();
		Timestamp lastNow = getNextNow(now);
		List<Contract> contractList = contractProvider.listContractsByCreateDateRange(lastNow, now, namespaceId);
		if (contractList == null || contractList.isEmpty()) {
			return;
		}
		
		for (Contract contract : contractList) {
			Long organizationId = 0L;
			if(CustomerType.ORGANIZATION.equals(CustomerType.fromStatus(contract.getCustomerType()))) {
				organizationId = contract.getCustomerId();
			}
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
	
	protected void sendMessageToUser(Integer namespaceId, Set<String> phoneNumbers, String templateScope, int code, List<Tuple<String, Object>> params) {
		sendMessageToUser(namespaceId, phoneNumbers.toArray(new String[phoneNumbers.size()]), templateScope, code, getLocale(), params);
	}
	
	protected void sendMessageToUser(Integer namespaceId, String[] phoneNumbers, String templateScope, int templateId, String templateLocale, List<Tuple<String, Object>> variables) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("send message parameters are: namespaceId="+namespaceId+", phoneNumbers="+Arrays.toString(phoneNumbers)+", templateScope="+templateScope+", code="+templateId);
		}
		
		smsProvider.sendSms(namespaceId, phoneNumbers, templateScope, templateId, templateLocale, variables);
	}
	
	protected static final SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
	protected static final SimpleDateFormat chineDateSF = new SimpleDateFormat("yyyy年MM月dd日");
	protected static final SimpleDateFormat datetimeSF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	protected static final long ONE_DAY_MS = 24*3600*1000;
	// 获取今天10点钟的这个时间
	protected Timestamp getCurrentDate() {
		Date date = new Date();
		String dateStr = dateSF.format(date);
		try {
			return new Timestamp(datetimeSF.parse(dateStr+" 10:00:00").getTime());
		} catch (ParseException e) {
			return new Timestamp(date.getTime());
		}
	}

	//获取昨天上午10点这个时间
	protected Timestamp getNextNow(Timestamp now) {
		return new Timestamp(now.getTime()-ONE_DAY_MS);
	}

    protected String getLocale() {
        User user = UserContext.current().getUser();
        if(user != null && user.getLocale() != null)
            return user.getLocale();
        return Locale.SIMPLIFIED_CHINESE.toString();
    }
    
	protected String getChinaDate(Timestamp date) {
		if (date == null) {
			return "";
		}
		return chineDateSF.format(date);
	} 
	
	protected String getAppName(Integer namespaceId) {
		AppUrlDTO appUrlDTO = appUrlService.getAppInfo(new GetAppInfoCommand(namespaceId,OSType.Android.getCode()));
		if (appUrlDTO != null) {
			return appUrlDTO.getName();
		}
		return "";
	}
	
	protected String getCommunityName(Long organizationId) {
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
		List<Contract> contractList = contractProvider.listContractByOrganizationId(cmd.getOrganizationId(), cmd.getCategoryId());
		List<ContractDTO> resultList = contractList.stream().map(c->{
			ContractDTO contractDTO = organizationService.processContract(c, namespaceId);
//			List<BuildingApartmentDTO> buildings = contractBuildingMappingProvider.listBuildingsByContractNumber(namespaceId, contractDTO.getContractNumber());
//			contractDTO.setBuildings(buildings);
			return contractDTO;
		}).collect(Collectors.toList());
		
		return new ListContractsResponse(null, resultList);
	}


	@Override
	public List<Object> findCustomerByContractNum(String contractNum,Long ownerId,String ownerType) {
//		if(UserContext.getCurrentNamespaceId()== 999971){
//			//找张江高科
////			return null;
//		}
		return contractProvider.findCustomerByContractNum(contractNum);
	}


	protected void checkContractNumberUnique(Integer namespaceId, String contractNumber, Long categoryId) {
		Contract contract = contractProvider.findActiveContractByContractNumber(namespaceId, contractNumber, categoryId);
		if(contract != null) {
			LOGGER.error("contractNumber {} in namespace {} already exist!", contractNumber, namespaceId);
			throw RuntimeErrorException.errorWith(ContractErrorCode.SCOPE, ContractErrorCode.ERROR_CONTRACTNUMBER_EXIST,
					"contractNumber is already exist");
		}
	}

	// issue-42515 合同变更提示合同编号已存在（HT-ZL-201811-9625）取后面4位太少，导致产生的合同编号有很大几率重复，先修改为取后6
	@Override
	public String generateContractNumber(GenerateContractNumberCommand cmd) {
		/*生成合同编号不用校验权限，下面这个是校验参数的权限，权限也是不对的*/
		//checkContractAuth(cmd.getNamespaceId(), PrivilegeConstants.CONTRACT_PARAM_LIST, cmd.getOrgId(), cmd.getCommunityId());
		ContractParam communityExist = contractProvider.findContractParamByCommunityId(cmd.getNamespaceId(), cmd.getCommunityId(), cmd.getPayorreceiveContractType(),cmd.getOrgId(), cmd.getCategoryId(),null);
		if(communityExist == null && cmd.getCommunityId() != null && cmd.getCategoryId() !=null) {
			communityExist = contractProvider.findContractParamByCommunityId(cmd.getNamespaceId(), null, cmd.getPayorreceiveContractType(),cmd.getOrgId(), cmd.getCategoryId(),null);
		}
		
		Calendar cal=Calendar.getInstance();
		StringBuffer contractNumber = new StringBuffer();
		
		if (communityExist == null || communityExist.getContractNumberRulejson() == null) {
			SimpleDateFormat sdf = new SimpleDateFormat("MM");
			java.util.Date month  = Calendar.getInstance().getTime();
			//收款
			if (ContractPaymentType.RECEIVE.equals(ContractPaymentType.fromStatus(cmd.getPayorreceiveContractType()))) {
				//HT-ZL-年月-流水号
				return "HT-ZL-"+ cal.get(Calendar.YEAR)+sdf.format(month)+ "-" +(System.currentTimeMillis()+"").substring(7, 13);
			}
			//付款
			if (ContractPaymentType.PAY.equals(ContractPaymentType.fromStatus(cmd.getPayorreceiveContractType()))) {
				//HT-FK-年月-流水号
				return "HT-FK-"+ cal.get(Calendar.YEAR)+sdf.format(month)+ "-" +(System.currentTimeMillis()+"").substring(7, 13);
			}
		}
		//获取规则
		String contractNumberRulejsonStr = communityExist.getContractNumberRulejson();
		if (communityExist != null && contractNumberRulejsonStr == null) {
			SimpleDateFormat sdf = new SimpleDateFormat("MM");
			java.util.Date month  = Calendar.getInstance().getTime();
			//收款
			if (ContractPaymentType.RECEIVE.equals(ContractPaymentType.fromStatus(cmd.getPayorreceiveContractType()))) {
				//HT-ZL-年月-流水号
				return "HT-ZL-"+ cal.get(Calendar.YEAR)+sdf.format(month)+ "-" +(System.currentTimeMillis()+"").substring(7, 13);
			}
			//付款
			if (ContractPaymentType.PAY.equals(ContractPaymentType.fromStatus(cmd.getPayorreceiveContractType()))) {
				//HT-FK-年月-流水号
				return "HT-FK-"+ cal.get(Calendar.YEAR)+sdf.format(month)+ "-" +(System.currentTimeMillis()+"").substring(7, 13);
			}
		}
		
		GenerateContractNumberRule contractNumberRulejson = (GenerateContractNumberRule)StringHelper.fromJsonString(contractNumberRulejsonStr, GenerateContractNumberRule.class);
		
		String Sparefield = contractNumberRulejson.getSparefield();
		contractNumber.append(contractNumberRulejson.getConstantVar());
		
		if (ContractNumberDataType.YEAR.equals(ContractNumberDataType.fromStatus(contractNumberRulejson.getDateVar()))) {
			contractNumber.append("-").append(cal.get(Calendar.YEAR));
		}
		if (ContractNumberDataType.MONTH.equals(ContractNumberDataType.fromStatus(contractNumberRulejson.getDateVar()))) {
			SimpleDateFormat sdf = new SimpleDateFormat("MM");
			java.util.Date month  = Calendar.getInstance().getTime();
			contractNumber.append("-").append(cal.get(Calendar.YEAR)).append(sdf.format(month));
		}
		if (Sparefield!=null && !"".equals(Sparefield)) {
			contractNumber.append("-").append(Sparefield);
		}
		
		String currentTime = (System.currentTimeMillis()+"").substring(7, 13);
		return contractNumber.append("-").append(cal.get(Calendar.DATE))+currentTime;
	}

	//创建合同，草稿合同不再占用房源状态，处在审批中的合同才去修改房源状态，
	@Override
	public ContractDetailDTO createContract(CreateContractCommand cmd) {
		if(ContractType.NEW.equals(ContractType.fromStatus(cmd.getContractType()))) {
			checkContractAuth(cmd.getNamespaceId(), PrivilegeConstants.CONTRACT_CREATE, cmd.getOrgId(), cmd.getCommunityId());
		} else if(ContractType.RENEW.equals(ContractType.fromStatus(cmd.getContractType()))) {
			checkContractAuth(cmd.getNamespaceId(), PrivilegeConstants.CONTRACT_RENEW, cmd.getOrgId(), cmd.getCommunityId());
		} else if(ContractType.CHANGE.equals(ContractType.fromStatus(cmd.getContractType()))) {
			checkContractAuth(cmd.getNamespaceId(), PrivilegeConstants.CONTRACT_CHANGE, cmd.getOrgId(), cmd.getCommunityId());
		}

		Contract contract = ConvertHelper.convert(cmd, Contract.class);
		contract.setPaymentFlag((byte)0);
		if(cmd.getContractNumber() != null) {
			checkContractNumberUnique(cmd.getNamespaceId(), cmd.getContractNumber(), cmd.getCategoryId());
		} else {
			//创建合同为啥没有 合同号？
			//contract.setContractNumber(generateContractNumber());
		}

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
		} else {
			contract.setSignedTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		}
		if(cmd.getDepositTime() != null) {
			contract.setDepositTime(new Timestamp(cmd.getDepositTime()));
		}
		if(cmd.getDownpaymentTime() != null) {
			contract.setDownpaymentTime(new Timestamp(cmd.getDownpaymentTime()));
		}
		if(cmd.getCategoryId() != null) {
			contract.setCategoryId(cmd.getCategoryId());
		}
		contract.setCreateUid(UserContext.currentUserId());
		contract.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		contract.setUpdateTime(contract.getCreateTime());
		contract.setStatus(ContractStatus.DRAFT.getCode()); //存草稿状态
		contractProvider.createContract(contract);
		//添加合同日志add by tangcen
		contractProvider.saveContractEvent(ContractTrackingTemplateCode.CONTRACT_ADD,contract,null);
		//如果是变更或续约合同，需在父合同添加日志
		if(ContractType.RENEW.equals(ContractType.fromStatus(contract.getContractType()))) {
			Contract parentContract = checkContract(contract.getParentId());
			contractProvider.saveContractEvent(ContractTrackingTemplateCode.CONTRACT_RENEW,parentContract,contract);
		} else if(ContractType.CHANGE.equals(ContractType.fromStatus(contract.getContractType()))) {
			Contract parentContract = checkContract(contract.getParentId());
			contractProvider.saveContractEvent(ContractTrackingTemplateCode.CONTRACT_CHANGE,parentContract,contract);
		}
		//重新组装变更合的计价条款税率 add ---djm issue-35984
		if (cmd.getChargingItems() != null) {
			for (int i = 0; i < cmd.getChargingItems().size(); i++) {
				Long billItemId = cmd.getChargingItems().get(i).getChargingItemId();
				/*List<PaymentBillGroupRule> groupRules = assetProvider.getBillGroupRule(billItemId,
						cmd.getChargingItems().get(i).getChargingStandardId(), "community", cmd.getCommunityId(), null);
				Long billGroupId = groupRules.get(0).getBillGroupId();*/

				Long billGroupId = cmd.getChargingItems().get(i).getBillGroupId();

				BigDecimal taxRate = assetService.getBillItemTaxRate(billGroupId, billItemId);
				String chargingVariables = cmd.getChargingItems().get(i).getChargingVariables();

				if (chargingVariables.contains("\"variableIdentifier\":\"dj\"")) {// 单价
					ChargingVariables chargingVariableList = (ChargingVariables) StringHelper
							.fromJsonString(chargingVariables, ChargingVariables.class);
					if (chargingVariableList != null && chargingVariableList.getChargingVariables() != null) {
						BigDecimal dj = BigDecimal.ZERO;// 单价
						BigDecimal djbhs = BigDecimal.ZERO;// 单价不含税
						BigDecimal mj = BigDecimal.ZERO;
						for (ChargingVariable chargingVariable : chargingVariableList.getChargingVariables()) {
							if (chargingVariable.getVariableIdentifier() != null) {
								if (chargingVariable.getVariableIdentifier().equals("dj")) {
									dj = BigDecimal.valueOf(Double.parseDouble(chargingVariable.getVariableValue()+""));
								}
								if (chargingVariable.getVariableIdentifier().equals("mj")) {
									mj = BigDecimal.valueOf(Double.parseDouble(chargingVariable.getVariableValue()+""));
								}
							}
						}
						BigDecimal taxRateDiv = taxRate.divide(new BigDecimal(100));
						djbhs = dj.divide(BigDecimal.ONE.add(taxRateDiv), 2, BigDecimal.ROUND_HALF_UP);// 修改税率之后，重新计算不含税
						// 重新组装Tables.EH_DEFAULT_CHARGING_ITEMS.CHARGING_VARIABLES的值
						ChargingVariables newChargingVariableList = new ChargingVariables();
						newChargingVariableList.setChargingVariables(new ArrayList<>());
						ChargingVariable djChargingVariable = new ChargingVariable();
						djChargingVariable.setVariableIdentifier("dj");
						djChargingVariable.setVariableName("单价含税(元)");
						djChargingVariable.setVariableValue(dj.toString());
						newChargingVariableList.getChargingVariables().add(djChargingVariable);
						ChargingVariable taxRateChargingVariable = new ChargingVariable();
						taxRateChargingVariable.setVariableIdentifier("taxRate");
						taxRateChargingVariable.setVariableName("税率(%)");
						taxRateChargingVariable.setVariableValue(taxRate.toString());
						newChargingVariableList.getChargingVariables().add(taxRateChargingVariable);
						ChargingVariable djbhsChargingVariable = new ChargingVariable();
						djbhsChargingVariable.setVariableIdentifier("djbhs");
						djbhsChargingVariable.setVariableName("单价不含税(元)");
						djbhsChargingVariable.setVariableValue(djbhs.toString());
						newChargingVariableList.getChargingVariables().add(djbhsChargingVariable);
						ChargingVariable mjChargingVariable = new ChargingVariable();
						mjChargingVariable.setVariableIdentifier("mj");
						mjChargingVariable.setVariableName("面积(㎡)");
						mjChargingVariable.setVariableValue(mj.toString());
						newChargingVariableList.getChargingVariables().add(mjChargingVariable);

						cmd.getChargingItems().get(i).setChargingVariables(newChargingVariableList.toString());
					}
				} else if (chargingVariables.contains("\"variableIdentifier\":\"gdje\"")) {// 固定金额
					ChargingVariables chargingVariableList = (ChargingVariables) StringHelper
							.fromJsonString(chargingVariables, ChargingVariables.class);
					if (chargingVariableList != null && chargingVariableList.getChargingVariables() != null) {
						BigDecimal gdje = BigDecimal.ZERO;// 固定金额(含税)
						BigDecimal gdjebhs = BigDecimal.ZERO;// 固定金额(不含税)
						for (ChargingVariable chargingVariable : chargingVariableList.getChargingVariables()) {
							if (chargingVariable.getVariableIdentifier() != null) {
								if (chargingVariable.getVariableIdentifier().equals("gdje")) {
									gdje = BigDecimal.valueOf(Double.parseDouble(chargingVariable.getVariableValue()+""));
								}
							}
						}
						BigDecimal taxRateDiv = taxRate.divide(new BigDecimal(100));
						gdjebhs = gdje.divide(BigDecimal.ONE.add(taxRateDiv), 2, BigDecimal.ROUND_HALF_UP);// 修改税率之后，重新计算不含税
						// 重新组装Tables.EH_DEFAULT_CHARGING_ITEMS.CHARGING_VARIABLES的值
						ChargingVariables newChargingVariableList = new ChargingVariables();
						newChargingVariableList.setChargingVariables(new ArrayList<>());
						ChargingVariable gdjeChargingVariable = new ChargingVariable();
						gdjeChargingVariable.setVariableIdentifier("gdje");
						gdjeChargingVariable.setVariableName("固定金额含税(元)");
						gdjeChargingVariable.setVariableValue(gdje.toString());
						newChargingVariableList.getChargingVariables().add(gdjeChargingVariable);
						ChargingVariable taxRateChargingVariable = new ChargingVariable();
						taxRateChargingVariable.setVariableIdentifier("taxRate");
						taxRateChargingVariable.setVariableName("税率(%)");
						taxRateChargingVariable.setVariableValue(taxRate.toString());
						newChargingVariableList.getChargingVariables().add(taxRateChargingVariable);
						ChargingVariable gdjebhsChargingVariable = new ChargingVariable();
						gdjebhsChargingVariable.setVariableIdentifier("gdjebhs");
						gdjebhsChargingVariable.setVariableName("固定金额不含税(元)");
						gdjebhsChargingVariable.setVariableValue(gdjebhs.toString());
						newChargingVariableList.getChargingVariables().add(gdjebhsChargingVariable);

						cmd.getChargingItems().get(i).setChargingVariables(newChargingVariableList.toString());
					}
				}
			}
		}

		//调用计算明细
		ExecutorUtil.submit(new Runnable() {
			@Override
			public void run() {
				generatePaymentExpectancies(contract, cmd.getChargingItems(), cmd.getAdjusts(), cmd.getFrees());
			}
		});

		//计算总的租赁面积
		//查询合同适用场景，物业合同不修改资产状态。
		//创建合同，草稿合同不再占用房源状态，处在审批中的合同才去修改房源状态，
        ContractCategory contractCategory = contractProvider.findContractCategoryById(contract.getCategoryId());
		Double totalSize = dealContractApartments(contract, cmd.getApartments(), contractCategory.getContractApplicationScene());
		dealContractChargingItems(contract, cmd.getChargingItems());
		dealContractAttachments(contract.getId(), cmd.getAttachments());
		dealContractChargingChanges(contract, cmd.getAdjusts(),cmd.getFrees());

		contract.setRentSize(totalSize);
		contract.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		contractProvider.updateContract(contract);
		contractSearcher.feedDoc(contract);
//		if(ContractStatus.WAITING_FOR_APPROVAL.equals(ContractStatus.fromStatus(contract.getStatus()))) {
//			addToFlowCase(contract);
//		}

		FindContractCommand command = new FindContractCommand();
		command.setId(contract.getId());
		command.setPartyAId(contract.getPartyAId());
		//add by tangcen
		command.setCommunityId(contract.getCommunityId());
		command.setNamespaceId(contract.getNamespaceId());
		command.setCategoryId(contract.getCategoryId());

		ContractDetailDTO contractDetailDTO = findContract(command);
		// 签合同的customer 同步到organization
		syncCustomerToOrganization(cmd.getCustomerId());
		return contractDetailDTO;
	}

	protected void syncCustomerToOrganization(Long customerId) {
		EnterpriseCustomer customer = enterpriseCustomerProvider.findById(customerId);
		if (customer != null) {
			Organization organization = null;
			if (customer.getOrganizationId() != null && customer.getOrganizationId() != 0L) {
				organization = organizationProvider.findOrganizationById(customer.getOrganizationId());
				if (null == organization){
					com.everhomes.rest.organization.OrganizationDTO organizationDTO = customerService.createOrganization(customer);
					customer.setOrganizationId(organizationDTO.getId());
					enterpriseCustomerProvider.updateEnterpriseCustomer(customer);
					enterpriseCustomerSearcher.feedDoc(customer);
				}
			}else {
				com.everhomes.rest.organization.OrganizationDTO organizationDTO = customerService.createOrganization(customer);
				customer.setOrganizationId(organizationDTO.getId());
				enterpriseCustomerProvider.updateEnterpriseCustomer(customer);
				enterpriseCustomerSearcher.feedDoc(customer);
			}
		}
	}

	@Override
	public ContractDetailDTO createPaymentContract(CreatePaymentContractCommand cmd) {
		if(ContractType.NEW.equals(ContractType.fromStatus(cmd.getContractType()))) {
			checkContractAuth(cmd.getNamespaceId(), PrivilegeConstants.PAYMENT_CONTRACT_CREATE, cmd.getOrganizationId(), cmd.getCommunityId());
		} else if(ContractType.RENEW.equals(ContractType.fromStatus(cmd.getContractType()))) {
			checkContractAuth(cmd.getNamespaceId(), PrivilegeConstants.PAYMENT_CONTRACT_RENEW, cmd.getOrganizationId(), cmd.getCommunityId());
		} else if(ContractType.CHANGE.equals(ContractType.fromStatus(cmd.getContractType()))) {
			checkContractAuth(cmd.getNamespaceId(), PrivilegeConstants.PAYMENT_CONTRACT_CHANGE, cmd.getOrganizationId(), cmd.getCommunityId());
		}

		Contract contract = ConvertHelper.convert(cmd, Contract.class);
		if(cmd.getContractNumber() != null) {
			checkContractNumberUnique(cmd.getNamespaceId(), cmd.getContractNumber(), cmd.getCategoryId());
		} else {
			//为啥会没有合同号？
			//contract.setContractNumber(generateContractNumber());
		}

		if(cmd.getContractStartDate() != null) {
			contract.setContractStartDate(new Timestamp(cmd.getContractStartDate()));
		}
		if(cmd.getContractEndDate() != null) {
			contract.setContractEndDate(new Timestamp(cmd.getContractEndDate()));
		}
		if(cmd.getSignedTime() != null) {
			contract.setSignedTime(new Timestamp(cmd.getSignedTime()));
		}
		if(cmd.getPaidTime() != null) {
			contract.setPaidTime(new Timestamp(cmd.getPaidTime()));
		}
		if(cmd.getCategoryId() != null) {
			contract.setCategoryId(cmd.getCategoryId());
		}
		contract.setCreateUid(UserContext.currentUserId());
		contract.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		contract.setPaymentFlag((byte)1);
		contractProvider.createContract(contract);

		dealContractAttachments(contract.getId(), cmd.getAttachments());
		dealContractPlans(contract.getId(), cmd.getPlans());

		if(ContractStatus.WAITING_FOR_APPROVAL.equals(ContractStatus.fromStatus(contract.getStatus()))) {
			addToFlowCase(contract, flowcasePaymentContractOwnerType);
		}
		contract.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		contractProvider.updateContract(contract);
		contractSearcher.feedDoc(contract);

		FindContractCommand command = new FindContractCommand();
		command.setId(contract.getId());
		command.setPartyAId(contract.getPartyAId());
		ContractDetailDTO contractDetailDTO = findContract(command);
		return contractDetailDTO;
	}

	@Override
	public ContractDetailDTO updatePaymentContract(UpdatePaymentContractCommand cmd) {
		checkContractAuth(cmd.getNamespaceId(), PrivilegeConstants.PAYMENT_CONTRACT_CREATE, cmd.getOrganizationId(), cmd.getCommunityId());
		Contract exist = checkContract(cmd.getId());
		Contract contract = ConvertHelper.convert(cmd, Contract.class);
		Contract existContract = contractProvider.findActiveContractByContractNumber(cmd.getNamespaceId(), cmd.getContractNumber(), cmd.getCategoryId());
		if(existContract != null && !existContract.getId().equals(contract.getId())) {
			LOGGER.error("contractNumber {} in namespace {} already exist!", cmd.getContractNumber(), cmd.getNamespaceId());
			throw RuntimeErrorException.errorWith(ContractErrorCode.SCOPE, ContractErrorCode.ERROR_CONTRACTNUMBER_EXIST,
					"contractNumber is already exist");
		}
		if(cmd.getContractStartDate() != null) {
			contract.setContractStartDate(new Timestamp(cmd.getContractStartDate()));
		}
		if(cmd.getContractEndDate() != null) {
			contract.setContractEndDate(new Timestamp(cmd.getContractEndDate()));
		}
		if(cmd.getSignedTime() != null) {
			contract.setSignedTime(new Timestamp(cmd.getSignedTime()));
		}
		if(cmd.getCategoryId() != null) {
			contract.setCategoryId(cmd.getCategoryId());
		}
		contract.setCreateTime(exist.getCreateTime());
		contract.setPaymentFlag((byte)1);
		contract.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		contractProvider.updateContract(contract);

		dealContractAttachments(contract.getId(), cmd.getAttachments());
		dealContractPlans(contract.getId(), cmd.getPlans());
		if(ContractStatus.WAITING_FOR_APPROVAL.equals(ContractStatus.fromStatus(contract.getStatus()))) {
			addToFlowCase(contract, flowcasePaymentContractOwnerType);
		}
		contractSearcher.feedDoc(contract);

		return ConvertHelper.convert(contract, ContractDetailDTO.class);
	}

	protected void generatePaymentExpectancies(Contract contract, List<ContractChargingItemDTO> chargingItems, List<ContractChargingChangeDTO> adjusts, List<ContractChargingChangeDTO> frees) {
		assetService.upodateBillStatusOnContractStatusChange(contract.getId(), AssetPaymentConstants.CONTRACT_CANCEL);

		if((chargingItems == null || chargingItems.size() == 0)
				&& (adjusts == null || adjusts.size() == 0) && (frees == null || frees.size() == 0)) {
			return ;
		}

		PaymentExpectanciesCommand command = new PaymentExpectanciesCommand();
		command.setContractNum(contract.getContractNumber());

		if(chargingItems != null && chargingItems.size() > 0) {
			List<FeeRules> feeRules = generateChargingItemsFeeRules(chargingItems, contract);
			command.setFeesRules(feeRules);
		}

		if(adjusts != null && adjusts.size() > 0) {
			List<RentAdjust> rentAdjusts = generateRentAdjust(adjusts);
			command.setRentAdjusts(rentAdjusts);
		}

		if(frees != null && frees.size() > 0) {
			List<RentFree> rentFrees = generateRentFree(frees);
			command.setRentFrees(rentFrees);
		}
		command.setNamesapceId(contract.getNamespaceId());
		command.setOwnerId(contract.getCommunityId());
		command.setOwnerType("community");
		command.setContractId(contract.getId());
		if(CustomerType.ENTERPRISE.equals(CustomerType.fromStatus(contract.getCustomerType()))) {
			command.setTargetType("eh_organization");
			EnterpriseCustomer customer = enterpriseCustomerProvider.findById(contract.getCustomerId());
			if(customer != null) {
				command.setTargetId(customer.getOrganizationId());
				command.setTargetName(customer.getName());
				command.setNoticeTel(customer.getContactMobile());
			}
		} else if(CustomerType.INDIVIDUAL.equals(CustomerType.fromStatus(contract.getCustomerType()))) {
			command.setTargetType("eh_user");
			OrganizationOwner owner = individualCustomerProvider.findOrganizationOwnerById(contract.getCustomerId());
			if(owner != null) {
				command.setTargetName(owner.getContactName());
				command.setNoticeTel(owner.getContactToken());
				UserIdentifier identifier = userProvider.findClaimedIdentifierByToken(owner.getNamespaceId(), owner.getContactToken());
				if(identifier != null) {
					command.setTargetId(identifier.getOwnerUid());
				}
			}
		}
//		assetService.paymentExpectancies(command);

		command.setIsEffectiveImmediately((byte)0);
		command.setCategoryId(contract.getCategoryId());
		command.setModuleId(21200l);
		assetService.paymentExpectanciesCalculate(command);
	}


	protected List<RentAdjust> generateRentAdjust(List<ContractChargingChangeDTO> adjusts) {
		List<RentAdjust> rentAdjusts = new ArrayList<>();
		adjusts.forEach(adjust -> {
			RentAdjust rentAdjust = new RentAdjust();
			rentAdjust.setBillGroupId(adjust.getBillGroupId());//物业缴费V6.3 签合同选择计价条款前，先选择账单组
			rentAdjust.setStart(new Date(adjust.getChangeStartTime()));
			rentAdjust.setEnd(new Date(adjust.getChangeExpiredTime()));
			rentAdjust.setAdjustType(adjust.getChangeMethod());
			rentAdjust.setSeparationTime(adjust.getChangePeriod().floatValue());
			if(adjust.getPeriodUnit() == PeriodUnit.DAY.getCode()){
				rentAdjust.setSeperationType((byte)1);
			}
			if(adjust.getPeriodUnit() == PeriodUnit.MONTH.getCode()){
				rentAdjust.setSeperationType((byte)2);
			}
			if(adjust.getPeriodUnit() == PeriodUnit.YEAR.getCode()){
				rentAdjust.setSeperationType((byte)3);
			}
			rentAdjust.setAdjustAmplitude(adjust.getChangeRange());
			rentAdjust.setChargingItemId(adjust.getChargingItemId());
			if(adjust.getApartments() != null && adjust.getApartments().size() > 0) {
				List<ContractProperty> properties = new ArrayList<ContractProperty>();
				adjust.getApartments().forEach(apartmentDTO -> {
					ContractProperty cp = ConvertHelper.convert(apartmentDTO, ContractProperty.class);
					cp.setPropertyName(cp.getBuldingName() + "-" + cp.getApartmentName());
					properties.add(cp);
				});
				rentAdjust.setProperties(properties);
			}
			rentAdjusts.add(rentAdjust);
		});
		return rentAdjusts;
	}


	protected List<RentFree> generateRentFree(List<ContractChargingChangeDTO> frees) {
		List<RentFree> rentFrees = new ArrayList<>();
		frees.forEach(free -> {
			RentFree rentFree = new RentFree();
			rentFree.setBillGroupId(free.getBillGroupId());//物业缴费V6.3 签合同选择计价条款前，先选择账单组
			rentFree.setChargingItemId(free.getChargingItemId());
			rentFree.setStartDate(new Date(free.getChangeStartTime()));
			rentFree.setEndDate(new Date(free.getChangeExpiredTime()));
			rentFree.setAmount(free.getChangeRange());
			rentFree.setRemark(free.getRemark());
			if(free.getApartments() != null && free.getApartments().size() > 0) {
				List<ContractProperty> properties = new ArrayList<ContractProperty>();
				free.getApartments().forEach(apartmentDTO -> {
					ContractProperty cp = ConvertHelper.convert(apartmentDTO, ContractProperty.class);
					cp.setPropertyName(cp.getBuldingName() + "-" + cp.getApartmentName());
					properties.add(cp);
				});
				rentFree.setProperties(properties);
			}
			rentFrees.add(rentFree);
		});
		return rentFrees;
	}


	protected List<FeeRules> generateChargingItemsFeeRules(List<ContractChargingItemDTO> chargingItems, Contract contract) {
		Gson gson = new Gson();
		List<FeeRules> feeRules = new ArrayList<>();
		chargingItems.forEach(chargingItem -> {
			FeeRules feeRule = new FeeRules();
			feeRule.setBillGroupId(chargingItem.getBillGroupId());//物业缴费V6.3 签合同选择计价条款前，先选择账单组
			feeRule.setChargingItemId(chargingItem.getChargingItemId());
			feeRule.setChargingStandardId(chargingItem.getChargingStandardId());
			feeRule.setLateFeeStandardId(chargingItem.getLateFeeStandardId());
			if(chargingItem.getChargingStartTime() != null){
				feeRule.setDateStrBegin(new Date(chargingItem.getChargingStartTime()));
			}
			if(chargingItem.getChargingExpiredTime() !=null){
				feeRule.setDateStrEnd(new Date(chargingItem.getChargingExpiredTime()));
			}
			List<ContractProperty> contractProperties = new ArrayList<>();
			if(chargingItem.getApartments() != null && chargingItem.getApartments().size() > 0) {
				chargingItem.getApartments().forEach(apartment -> {
					ContractProperty cp = new ContractProperty();
					cp.setApartmentName(apartment.getApartmentName());
					cp.setBuldingName(apartment.getBuildingName());
					cp.setAddressId(apartment.getAddressId());
//					Address address = addressProvider.findAddressById(apartment.getAddressId());
//					cp.setPropertyName(address.getNamespaceAddressToken());
					contractProperties.add(cp);
				});
			}
			feeRule.setProperties(contractProperties);

			ChargingVariablesDTO chargingVariables = gson.fromJson(chargingItem.getChargingVariables(), new TypeToken<ChargingVariablesDTO>() {}.getType());
			List<PaymentVariable> pvs = chargingVariables.getChargingVariables();
			List<VariableIdAndValue> vv = new ArrayList<>();
			if(pvs != null && pvs.size() > 0) {
				pvs.forEach(pv -> {
					VariableIdAndValue variableIdAndValue = new VariableIdAndValue();
					variableIdAndValue.setVaribleIdentifier(pv.getVariableIdentifier());
					variableIdAndValue.setVariableId(pv.getVariableIdentifier());
					variableIdAndValue.setVariableValue(pv.getVariableValue());
					vv.add(variableIdAndValue);
				});
			}
			feeRule.setVariableIdAndValueList(vv);
			//缺陷 #42424 【智谷汇】保证金设置为固定金额，但是实际会以合同签约门牌的数量计价。实际上保证金是按照合同收费，不是按照门牌的数量进行重复计费
            //缺陷 #42424 如果是一次性产生费用，那么只在第一个收费周期产生费用
            if(AssetOneTimeBillStatus.TRUE.getCode().equals(chargingItem.getOneTimeBillStatus())) {
            	feeRule.setOneTimeBillStatus(chargingItem.getOneTimeBillStatus());
            	feeRule.setDateStrBegin(contract.getContractStartDate());
            	feeRule.setDateStrEnd(contract.getContractEndDate());
            }
			feeRules.add(feeRule);
		});

		return feeRules;
	}


	protected void addToFlowCase(Contract contract, String flowcaseOwnerType) {

		Flow flow = null;
		String contractType="";
		
		if ("CONTRACT".equals(flowcaseOwnerType)) {
			//多工作流
			if(contract.getContractType() == 0){
				contractType ="新签";
				flow = flowService.getEnabledFlow(contract.getNamespaceId(),EntityType.COMMUNITY.getCode(),contract.getCommunityId(),
						FlowConstants.CONTRACT_MODULE, FlowModuleType.REVIEW_CONTRACT.getCode(), contract.getCategoryId(), flowcaseOwnerType);
			
			} else if(contract.getContractType() == 1){
				contractType ="续约";
				flow = flowService.getEnabledFlow(contract.getNamespaceId(),EntityType.COMMUNITY.getCode(),contract.getCommunityId(),
						FlowConstants.CONTRACT_MODULE, FlowModuleType.RENEW_CONTRACT.getCode(), contract.getCategoryId(), flowcaseOwnerType);
			
			}else if (contract.getContractType() == 2){
				contractType ="变更";
				flow = flowService.getEnabledFlow(contract.getNamespaceId(),EntityType.COMMUNITY.getCode(),contract.getCommunityId(),
						FlowConstants.CONTRACT_MODULE, FlowModuleType.CHANGE_CONTRACT.getCode(), contract.getCategoryId(), flowcaseOwnerType);
			}
			else if(contract.getContractType() == 3){
				contractType ="退约";
				flow = flowService.getEnabledFlow(contract.getNamespaceId(),EntityType.COMMUNITY.getCode(),contract.getCommunityId(),
						FlowConstants.CONTRACT_MODULE, FlowModuleType.DENUNCIATION_CONTRACT.getCode(), contract.getCategoryId(), flowcaseOwnerType);
			}
			if (flow == null) {
				flow = flowService.getEnabledFlow(contract.getNamespaceId(),EntityType.COMMUNITY.getCode(),contract.getCommunityId(),
						FlowConstants.CONTRACT_MODULE, FlowModuleType.NO_MODULE.getCode(), contract.getCategoryId(), flowcaseOwnerType);
			}
		}else if ("PAYMENT_CONTRACT".equals(flowcaseOwnerType)) {
			flow = flowService.getEnabledFlow(contract.getNamespaceId(),EntityType.COMMUNITY.getCode(),contract.getCommunityId(),
					FlowConstants.PAYMENT_CONTRACT_MODULE, FlowModuleType.NO_MODULE.getCode(), contract.getCommunityId(), flowcaseOwnerType);
		}else {
			flow = flowService.getEnabledFlow(contract.getNamespaceId(),EntityType.COMMUNITY.getCode(),contract.getCommunityId(),
					FlowConstants.CONTRACT_MODULE, FlowModuleType.NO_MODULE.getCode(), contract.getCommunityId(), flowcaseOwnerType);
		}
		
		if(null == flow) {
			LOGGER.error("Enable request flow not found, moduleId={}", FlowConstants.CONTRACT_MODULE);
			throw RuntimeErrorException.errorWith(ContractErrorCode.SCOPE, ContractErrorCode.ERROR_ENABLE_FLOW,
					localeStringService.getLocalizedString(String.valueOf(ContractErrorCode.SCOPE),
							String.valueOf(ContractErrorCode.ERROR_ENABLE_FLOW),
							UserContext.current().getUser().getLocale(),"Enable request flow not found."));
		}
		CreateFlowCaseCommand createFlowCaseCommand = new CreateFlowCaseCommand();
		createFlowCaseCommand.setCurrentOrganizationId(contract.getPartyAId());
		createFlowCaseCommand.setTitle(contract.getCustomerName() + contractType + "的合同申请");
		createFlowCaseCommand.setApplyUserId(contract.getCreateUid());
		createFlowCaseCommand.setFlowMainId(flow.getFlowMainId());
		createFlowCaseCommand.setFlowVersion(flow.getFlowVersion());
		createFlowCaseCommand.setReferId(contract.getId());
		createFlowCaseCommand.setReferType(EntityType.CONTRACT.getCode());
		createFlowCaseCommand.setContent(contract.getContractNumber());
		createFlowCaseCommand.setServiceType("合同管理-"+contractType);
		createFlowCaseCommand.setProjectId(contract.getCommunityId());
		createFlowCaseCommand.setProjectType(EntityType.COMMUNITY.getCode());

		flowService.createFlowCase(createFlowCaseCommand);
	}

	//再处理房源状态时候，草稿合同，待发起不再修改房源状态，审批中的合同房源状态待签约，审批通过待接房，正常合同已出租
	protected Double dealContractApartments(Contract contract, List<BuildingApartmentDTO> buildingApartments, Byte contractApplicationScene) {
		// add by tangcen
		List<String> oldApartments = new ArrayList<>();
		List<String> newApartments = new ArrayList<>();
		// 没有id的，增加
		// 有id的，修改且从已有列表中删除，然后把已有列表中剩余的数据删除
		List<ContractBuildingMapping> existApartments = contractBuildingMappingProvider.listByContract(contract.getId());
		Map<Long, ContractBuildingMapping> map = new HashMap<>();
		if (existApartments != null && existApartments.size() > 0) {
			existApartments.forEach(apartment -> {
				map.put(apartment.getId(), apartment);
				// add by tangcen ContractBuildingMapping中收费面积是AreaSize
				oldApartments.add(" 门牌：" + apartment.getApartmentName() + " 收费面积：" + apartment.getAreaSize());
			});
		}

		// 续约和变更的继承原合同的不用改状态
		List<Long> parentAddressIds = new ArrayList<>();
		if (ContractType.CHANGE.equals(ContractType.fromStatus(contract.getContractType())) || ContractType.RENEW.equals(ContractType.fromStatus(contract.getContractType()))) {
			if (contract.getParentId() != null) {
				Contract parentContract = contractProvider.findContractById(contract.getParentId());
				if (parentContract != null && ContractStatus.ACTIVE.equals(ContractStatus.fromStatus(parentContract.getStatus()))) {
					List<ContractBuildingMapping> parentContractApartments = contractBuildingMappingProvider.listByContract(parentContract.getId());
					if (parentContractApartments != null && parentContractApartments.size() > 0) {
						parentAddressIds = parentContractApartments.stream()
								.map(contractApartment -> contractApartment.getAddressId())
								.collect(Collectors.toList());
					}
				}
			}
		}

		// Double totalSize = 0.0; 计算精度有问题 --by dingjianmin
		BigDecimal totalSize = new BigDecimal(0);
		if (buildingApartments != null && buildingApartments.size() > 0) {
			for (BuildingApartmentDTO buildingApartment : buildingApartments) {
				// add by tangcen
				//newApartments.add(buildingApartment.getApartmentName() + " 收费面积：" + buildingApartment.getChargeArea());
				newApartments.add(" 门牌：" + buildingApartment.getApartmentName() + " 收费面积：" + buildingApartment.getChargeArea());
				Double size = buildingApartment.getChargeArea() == null ? 0.0 : buildingApartment.getChargeArea();
				// totalSize = totalSize + size;
				BigDecimal chargeArea = new BigDecimal(Double.toString(size));
				totalSize = totalSize.add(chargeArea);
				ContractBuildingMapping contractBuildingMapping = contractBuildingMappingProvider.findContractBuildingMappingById(buildingApartment.getId());
				if (contractBuildingMapping == null) {
					// 新增的资产
					ContractBuildingMapping mapping = ConvertHelper.convert(buildingApartment, ContractBuildingMapping.class);
					mapping.setNamespaceId(contract.getNamespaceId());
					mapping.setContractId(contract.getId());
					mapping.setAreaSize(buildingApartment.getChargeArea());
					mapping.setContractNumber(contract.getContractNumber());
					mapping.setStatus(CommonStatus.ACTIVE.getCode());
					mapping.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
					contractBuildingMappingProvider.createContractBuildingMapping(mapping);
					
				} else {
					// 更新合同资产收费面积
					contractBuildingMapping.setAreaSize(buildingApartment.getChargeArea());
					contractBuildingMappingProvider.updateContractBuildingMapping(contractBuildingMapping);
					// 保留的资产
					map.remove(buildingApartment.getId());
				}
				
				// 物业合同不修改资产状态
				//合同审批中的，房源待签约，保存并发起审批走这里
				if (ContractStatus.WAITING_FOR_APPROVAL.equals(ContractStatus.fromStatus(contract.getStatus())) && !parentAddressIds.contains(buildingApartment.getAddressId()) && ((contractApplicationScene== null && contract.getPaymentFlag()==1) || !ContractApplicationScene.PROPERTY.equals(ContractApplicationScene.fromStatus(contractApplicationScene)))) {
					CommunityAddressMapping addressMapping = propertyMgrProvider.findAddressMappingByAddressId(buildingApartment.getAddressId());
					// 26058 已售的状态不变
					if (!AddressMappingStatus.SALED.equals(AddressMappingStatus.fromCode(addressMapping.getLivingStatus()))) {
						// 点击保存并发起走这里，置资产状态，修改合同点保存，不会修改资产状态
						if(ContractType.NEW.equals(ContractType.fromStatus(contract.getContractType()))) {
							FindContractCommand command = new FindContractCommand();
							command.setId(contract.getId());
							command.setPartyAId(contract.getPartyAId());
							command.setCommunityId(contract.getCommunityId());
							command.setNamespaceId(contract.getNamespaceId());
							command.setCategoryId(contract.getCategoryId());
							ContractDetailDTO contractDetailDTO = findContract(command);
							Boolean possibleEnterContractStatus = possibleEnterContract(contractDetailDTO);
							
							if (!possibleEnterContractStatus) {
								LOGGER.error("possibleEnterContractStatus is false, Apartments is not free");
								throw RuntimeErrorException.errorWith(ContractErrorCode.SCOPE, ContractErrorCode.ERROR_APARTMENTS_NOT_FREE_ERROR,
										"apartments status is not free for contract!");
							}
						}
						addressMapping.setLivingStatus(AddressMappingStatus.SIGNEDUP.getCode());
						addressMapping.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
						propertyMgrProvider.updateOrganizationAddressMapping(addressMapping);
					}
				}
				
			}
		}
		// 删除的资产
		if (map.size() > 0) {
			List<Long> finalParents = parentAddressIds;
			map.forEach((id, apartment) -> {
				// 房源是否可以释放 ，复制合同，关联的房源移除不操作资产状态 false 不释放，true 释放
				Boolean possibleAddressReleaseStatus = true;
				// 查询房源状态
				CommunityAddressMapping otherContractAddressMapping = organizationProvider.findOrganizationAddressMappingByAddressId(apartment.getAddressId());
				// 查询房源关联的合同
				List<ContractBuildingMapping> contractBuildingMappingList = addressProvider.findContractBuildingMappingByAddressId(apartment.getAddressId());
				// 查询该房源是否关联其他合同，并且不是待租状态
				for (ContractBuildingMapping contractBuildingMapping : contractBuildingMappingList) {
					if (!(contractBuildingMapping.getContractId()).equals(apartment.getContractId())
							&& otherContractAddressMapping.getLivingStatus() != AddressMappingStatus.FREE.getCode()) {
						possibleAddressReleaseStatus = false;
					}
				}
				contractBuildingMappingProvider.deleteContractBuildingMapping(apartment);
				if (!finalParents.contains(apartment.getAddressId()) && ((contractApplicationScene== null && contract.getPaymentFlag()==1) || !ContractApplicationScene.PROPERTY
						.equals(ContractApplicationScene.fromStatus(contractApplicationScene)))) {
					CommunityAddressMapping addressMapping = propertyMgrProvider.findAddressMappingByAddressId(apartment.getAddressId());
					// 26058 已售的状态不变
					if (!AddressMappingStatus.SALED.equals(AddressMappingStatus.fromCode(addressMapping.getLivingStatus()))) {
						if (possibleAddressReleaseStatus) {
							addressMapping.setLivingStatus(AddressMappingStatus.FREE.getCode());
							addressMapping.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
							propertyMgrProvider.updateOrganizationAddressMapping(addressMapping);
						}
					}
				}
			});
		}
		// add by tangcen
		if (oldApartments.size() == 0) {
			contractProvider.saveContractEventAboutApartments(ContractTrackingTemplateCode.APARTMENT_ADD, contract, null, newApartments.toString());
		} else if (newApartments.size() < oldApartments.size()) {
			contractProvider.saveContractEventAboutApartments(ContractTrackingTemplateCode.APARTMENT_UPDATE, contract, oldApartments.toString(), newApartments.toString());
		} else if (newApartments.size() >= oldApartments.size()) {
			for (String apartmentName : newApartments) {
				if (!oldApartments.contains(apartmentName)) {
					contractProvider.saveContractEventAboutApartments(ContractTrackingTemplateCode.APARTMENT_UPDATE, contract, oldApartments.toString(), newApartments.toString());
					break;
				}
			}
		}
		return totalSize.doubleValue();
	}


	protected void dealContractChargingItems(Contract contract, List<ContractChargingItemDTO> chargingItems) {
		//没有id的，增加
	    //有id的，修改且从已有列表中删除，然后把已有列表中剩余的数据删除
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
				if(item.getChargingStartTime() != null) {
					contractChargingItem.setChargingStartTime(new Timestamp(item.getChargingStartTime()));
				}
				if(item.getChargingExpiredTime() != null) {
					contractChargingItem.setChargingExpiredTime(new Timestamp(item.getChargingExpiredTime()));
				}
				if(item.getId() == null) {
					//新增的计价条款
					contractChargingItem.setContractId(contract.getId());
					contractChargingItem.setNamespaceId(contract.getNamespaceId());
					contractChargingItem.setStatus(CommonStatus.ACTIVE.getCode());
					contractChargingItemProvider.createContractChargingItem(contractChargingItem);
					dealContractChargingItemAddresses(contractChargingItem, item.getApartments());
					//记录合同日志 by tangcen
					contractProvider.saveContractEventAboutChargingItem(ContractTrackingTemplateCode.CHARGING_ITEM_ADD,contract,contractChargingItem);
				} else {
					//保留的计价条款
					ContractChargingItem exist = contractChargingItemProvider.findById(item.getId());
					contractChargingItem.setCreateUid(exist.getCreateUid());
					contractChargingItem.setCreateTime(exist.getCreateTime());
					contractChargingItem.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
					contractChargingItemProvider.updateContractChargingItem(contractChargingItem);
					//修复issue-32556，已存在的ChargingItem如果修改了关联的Apartments，应该更新ChargingItem和Apartments的对应关系
					dealContractChargingItemAddresses(contractChargingItem, item.getApartments());
					map.remove(item.getId());
					//add by tangcen
					if (!compareContractChargingItem(contractChargingItem,exist)) {
						contractProvider.saveContractEventAboutChargingItem(ContractTrackingTemplateCode.CHARGING_ITEM_UPDATE,contract,exist);
					}
				}
			});
		}
		if(map.size() > 0) {
			//删除的计价条款
			map.forEach((id, item) -> {
				contractChargingItemProvider.deleteContractChargingItem(item);
				//记录合同日志 by tangcen
				contractProvider.saveContractEventAboutChargingItem(ContractTrackingTemplateCode.CHARGING_ITEM_DELETE,contract,item);
			});
		}
	}
	//对比ContractChargingItem by tangcen
	protected boolean compareContractChargingItem(ContractChargingItem contractChargingItem, ContractChargingItem exist) {
		if (contractChargingItem==null || exist==null) {
			return false;
		}
		ContractChargingItemEventDTO updateDto = ConvertHelper.convert(contractChargingItem, ContractChargingItemEventDTO.class);
		ContractChargingItemEventDTO existDto = ConvertHelper.convert(exist, ContractChargingItemEventDTO.class);
		return updateDto.equals(existDto);
	}

	protected void dealContractChargingItemAddresses(ContractChargingItem item, List<BuildingApartmentDTO> addresses) {
		//没有id的，增加
	    //有id的，修改且从已有列表中删除，然后把已有列表中剩余的数据删除
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

	protected void dealContractChargingChanges(Contract contract, List<ContractChargingChangeDTO> adjusts, List<ContractChargingChangeDTO> frees) {
		// 没有id的，增加
		//有id的，修改且从已有列表中删除，然后把已有列表中剩余的数据删除
		List<ContractChargingChange> existChargingChanges = contractChargingChangeProvider.listByContractId(contract.getId());
		Map<Long, ContractChargingChange> map = new HashMap<>();
		if(existChargingChanges != null && existChargingChanges.size() > 0) {
			existChargingChanges.forEach(change -> {
				map.put(change.getId(), change);
			});
		}

		if(adjusts != null && adjusts.size() > 0) {
			dealChanges(contract, map, adjusts, ChangeType.ADJUST);
		}
		if(frees != null && frees.size() > 0) {
			dealChanges(contract, map, frees, ChangeType.FREE);
		}
		if(map.size() > 0) {
			map.forEach((id, change) -> {
				//删除的调免租计划
				contractChargingChangeProvider.deleteContractChargingChange(change);
				//记录合同日志 by tangcen
				if (ChangeType.fromStatus(change.getChangeType()).equals(ChangeType.ADJUST)) {
					contractProvider.saveContractEventAboutChargingChange(ContractTrackingTemplateCode.ADJUST_DELETE,contract,change);
				}else if (ChangeType.fromStatus(change.getChangeType()).equals(ChangeType.FREE)) {
					contractProvider.saveContractEventAboutChargingChange(ContractTrackingTemplateCode.FREE_DELETE,contract,change);
				}
			});
		}
	}

	protected Map<Long, ContractChargingChange> dealChanges(Contract contract, Map<Long, ContractChargingChange> map, List<ContractChargingChangeDTO> changes, ChangeType changeType) {
		changes.forEach(change -> {
			ContractChargingChange contractChargingChange = ConvertHelper.convert(change, ContractChargingChange.class);
			if(change.getChangeStartTime() != null) {
				contractChargingChange.setChangeStartTime(new Timestamp(change.getChangeStartTime()));
			}
			if(change.getChangeExpiredTime() != null) {
				contractChargingChange.setChangeExpiredTime(new Timestamp(change.getChangeExpiredTime()));
			}
			if(change.getId() == null) {
				//新增的调免租计划
				contractChargingChange.setContractId(contract.getId());
				contractChargingChange.setNamespaceId(contract.getNamespaceId());
				contractChargingChange.setChangeType(changeType.getCode());
				contractChargingChange.setStatus(CommonStatus.ACTIVE.getCode());
				contractChargingChangeProvider.createContractChargingChange(contractChargingChange);
				dealContractChargingChangeAddresses(contractChargingChange, change.getApartments());
				//记录合同日志 by tangcen
				if (changeType.equals(ChangeType.ADJUST)) {
					contractProvider.saveContractEventAboutChargingChange(ContractTrackingTemplateCode.ADJUST_ADD,contract,contractChargingChange);
				}else if (changeType.equals(ChangeType.FREE)) {
					contractProvider.saveContractEventAboutChargingChange(ContractTrackingTemplateCode.FREE_ADD,contract,contractChargingChange);
				}
			} else {
				//保留的调免租计划
				ContractChargingChange exist = contractChargingChangeProvider.findById(change.getId());
				contractChargingChange.setCreateUid(exist.getCreateUid());
				contractChargingChange.setCreateTime(exist.getCreateTime());
				contractChargingChangeProvider.updateContractChargingChange(contractChargingChange);
				map.remove(change.getId());
				//记录合同日志 by tangcen
				if (changeType.equals(ChangeType.ADJUST)) {
					if (!compareContractChargingChange(contractChargingChange, exist)) {
						contractProvider.saveContractEventAboutChargingChange(ContractTrackingTemplateCode.ADJUST_UPDATE,contract,contractChargingChange);
					}	
				}else if (changeType.equals(ChangeType.FREE)) {
					if (!compareContractChargingChange(contractChargingChange, exist)) {
						contractProvider.saveContractEventAboutChargingChange(ContractTrackingTemplateCode.FREE_UPDATE,contract,contractChargingChange);
					}
				}
			}
		});
		return map;
	}
	//对比ContractChargingChange by tangcen
	protected boolean compareContractChargingChange(ContractChargingChange contractChargingChange,ContractChargingChange exist) {
		if (contractChargingChange == null || exist ==null) {
			return false;
		}
		ContractChargingChangeEventDTO updateDto = ConvertHelper.convert(contractChargingChange, ContractChargingChangeEventDTO.class);
		ContractChargingChangeEventDTO existDto = ConvertHelper.convert(exist, ContractChargingChangeEventDTO.class);
		return updateDto.equals(existDto);
	}

	protected void dealContractChargingChangeAddresses(ContractChargingChange contractChargingChange, List<BuildingApartmentDTO> apartments ) {
		List<ContractChargingChangeAddress> existChangeAddresses = contractChargingChangeAddressProvider.findByChangeId(contractChargingChange.getId());
		Map<Long, ContractChargingChangeAddress> map = new HashMap<>();
		if(existChangeAddresses != null && existChangeAddresses.size() > 0) {
			existChangeAddresses.forEach(address -> {
				map.put(address.getId(), address);
			});
		}

		if(apartments != null && apartments.size() > 0) {
			apartments.forEach(apartment -> {
				if(apartment.getId() == null) {
					ContractChargingChangeAddress address = ConvertHelper.convert(apartment, ContractChargingChangeAddress.class);
					address.setChargingChangeId(contractChargingChange.getId());
					address.setNamespaceId(contractChargingChange.getNamespaceId());
					address.setStatus(CommonStatus.ACTIVE.getCode());
					contractChargingChangeAddressProvider.createContractChargingChangeAddress(address);
				} else {
					map.remove(apartment.getId());
				}
			});
		}
		if(map.size() > 0) {
			map.forEach((id, changeAddress) -> {
				changeAddress.setStatus(CommonStatus.INACTIVE.getCode());
				contractChargingChangeAddressProvider.updateContractChargingChangeAddress(changeAddress);
			});
		}
	}
	protected void dealContractAttachments(Long contractId, List<ContractAttachmentDTO> attachments) {
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
					//新增的合同附件
					ContractAttachment contractAttachment = ConvertHelper.convert(attachment, ContractAttachment.class);
					contractAttachment.setContractId(contractId);
					contractAttachmentProvider.createContractAttachment(contractAttachment);
				} else {
					//保留的合同附件
					map.remove(attachment.getId());
				}
			});
		}
		if(map.size() > 0) {
			map.forEach((id, attachment) -> {
				//删除的合同附件
				contractAttachmentProvider.deleteContractAttachment(attachment);
			});
		}
	}
	
	//add by tangcen
	protected void dealContractAttachments(Contract contract, List<ContractAttachmentDTO> attachments) {
		Long contractId = contract.getId();
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
					//新增的合同附件
					ContractAttachment contractAttachment = ConvertHelper.convert(attachment, ContractAttachment.class);
					contractAttachment.setContractId(contractId);
					contractAttachmentProvider.createContractAttachment(contractAttachment);
					//记录合同日志 by tangcen
					contractProvider.saveContractEventAboutAttachment(ContractTrackingTemplateCode.ATTACHMENT_ADD,contract,contractAttachment);
				} else {
					//保留的合同附件
					map.remove(attachment.getId());
				}
			});
		}
		if(map.size() > 0) {
			map.forEach((id, attachment) -> {
				//删除的合同附件
				contractAttachmentProvider.deleteContractAttachment(attachment);
				//记录合同日志 by tangcen
				contractProvider.saveContractEventAboutAttachment(ContractTrackingTemplateCode.ATTACHMENT_DELETE,contract,attachment);
			});
		}
	}


	protected void dealContractPlans(Long contractId, List<ContractPaymentPlanDTO> plans) {
		List<ContractPaymentPlan> existPlans = contractPaymentPlanProvider.listByContractId(contractId);
		Map<Long, ContractPaymentPlan> map = new HashMap<>();
		if(existPlans != null && existPlans.size() > 0) {
			existPlans.forEach(plan -> {
				map.put(plan.getId(), plan);
			});
		}

		if(plans != null && plans.size() > 0) {
			plans.forEach(plan -> {
				if(plan.getId() == null) {
					ContractPaymentPlan contractPaymentPlan = ConvertHelper.convert(plan, ContractPaymentPlan.class);
					contractPaymentPlan.setContractId(contractId);
					if(plan.getPaidTime() != null) {
						contractPaymentPlan.setPaidTime(new Timestamp(plan.getPaidTime()));
					}
					contractPaymentPlanProvider.createContractPaymentPlan(contractPaymentPlan);
				} else {
					map.remove(plan.getId());
				}
			});
		}
		if(map.size() > 0) {
			map.forEach((id, plan) -> {
				contractPaymentPlanProvider.deleteContractPaymentPlan(plan);
			});
		}
	}

	@Override
	public ContractDetailDTO updateContract(UpdateContractCommand cmd) {
		checkContractAuth(cmd.getNamespaceId(), PrivilegeConstants.CONTRACT_CREATE, cmd.getOrgId(), cmd.getCommunityId());
		Contract exist = checkContract(cmd.getId());
		Contract contract = ConvertHelper.convert(cmd, Contract.class);
		Contract existContract = contractProvider.findActiveContractByContractNumber(cmd.getNamespaceId(), cmd.getContractNumber(), cmd.getCategoryId());
		if(existContract != null && !existContract.getId().equals(contract.getId())) {
			LOGGER.error("contractNumber {} in namespace {} already exist!", cmd.getContractNumber(), cmd.getNamespaceId());
			throw RuntimeErrorException.errorWith(ContractErrorCode.SCOPE, ContractErrorCode.ERROR_CONTRACTNUMBER_EXIST,
					"contractNumber is already exist");
		}
		if(cmd.getContractStartDate() != null) {
			contract.setContractStartDate(setToBegin(cmd.getContractStartDate()));
		}
		if(cmd.getContractEndDate() != null) {
			contract.setContractEndDate(setToEnd(cmd.getContractEndDate()));
		}
		if(cmd.getDecorateBeginDate() != null) {
			contract.setDecorateBeginDate(setToBegin(cmd.getDecorateBeginDate()));
		}
		if(cmd.getDecorateEndDate() != null) {
			contract.setDecorateEndDate(setToEnd(cmd.getDecorateEndDate()));
		}
		if(cmd.getSignedTime() != null) {
			contract.setSignedTime(new Timestamp(cmd.getSignedTime()));
		}
		if(cmd.getDepositTime() != null) {
			contract.setDepositTime(setToEnd(cmd.getDepositTime()));
		}
		if(cmd.getDownpaymentTime() != null) {
			contract.setDownpaymentTime(setToEnd(cmd.getDownpaymentTime()));
		}
		contract.setCreateTime(exist.getCreateTime());
		contract.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		
		//查询合同适用场景，物业合同不修改资产状态。
        ContractCategory contractCategory = contractProvider.findContractCategoryById(contract.getCategoryId());
		Double rentSize = dealContractApartments(contract, cmd.getApartments(), contractCategory.getContractApplicationScene());

		if(cmd.getRentSize() == null) {
			contract.setRentSize(rentSize);
		}
		if(cmd.getCategoryId() != null) {
			contract.setCategoryId(cmd.getCategoryId());
		}
		if(cmd.getTemplateId() != null) {
			contract.setTemplateId(cmd.getTemplateId());
		}
		contract.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

		//by --djm issue-35586
		if(ContractStatus.WAITING_FOR_APPROVAL.equals(ContractStatus.fromStatus(contract.getStatus()))) {

			/*if(ContractType.NEW.equals(ContractType.fromStatus(contract.getContractType()))) {

			if(ContractType.NEW.equals(ContractType.fromStatus(contract.getContractType()))) {

				FindContractCommand command = new FindContractCommand();
				command.setId(contract.getId());
				command.setPartyAId(contract.getPartyAId());
				command.setCommunityId(contract.getCommunityId());
				command.setNamespaceId(contract.getNamespaceId());
				command.setCategoryId(contract.getCategoryId());
				ContractDetailDTO contractDetailDTO = findContract(command);
				Boolean possibleEnterContractStatus = possibleEnterContract(contractDetailDTO);
				
				if (!possibleEnterContractStatus) {
					LOGGER.error("possibleEnterContractStatus is false, Apartments is not free");
					throw RuntimeErrorException.errorWith(ContractErrorCode.SCOPE, ContractErrorCode.ERROR_APARTMENTS_NOT_FREE_ERROR,
							"apartments status is not free for contract!");
				}
			}
		}*/
			addToFlowCase(contract, flowcaseContractOwnerType);
			//添加发起人字段
			contract.setSponsorUid(UserContext.currentUserId().toString());
			contract.setSponsorTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		}
		contractProvider.updateContract(contract);
		//add by tangcen
		//将父合同中关联的未出账单记为无效账单
		//前端传过来的CostGenerationMethod字段实际上是对父合同未出账单的处理方式，因此把CostGenerationMethod的值存在父合同中，而非子合同中
		if(ContractType.CHANGE.equals(ContractType.fromStatus(cmd.getContractType()))){
			contract.setCostGenerationMethod(null);
			Contract parentContract = checkContract(contract.getParentId());
			parentContract.setCostGenerationMethod(cmd.getCostGenerationMethod());
			contractProvider.updateContract(parentContract);
//			if(ContractStatus.WAITING_FOR_APPROVAL.equals(ContractStatus.fromStatus(cmd.getStatus()))){
//				assetService.deleteUnsettledBillsOnContractId(cmd.getCostGenerationMethod(),contract.getParentId(),contract.getContractStartDate());
//			}
		}
		//记录合同事件日志，by tangcen
		contractProvider.saveContractEvent(ContractTrackingTemplateCode.CONTRACT_UPDATE,contract,exist);
		
		dealContractChargingItems(contract, cmd.getChargingItems());
		//dealContractAttachments(contract.getId(), cmd.getAttachments())
		//change by tangcen
		dealContractAttachments(contract, cmd.getAttachments());
		contractSearcher.feedDoc(contract);
		dealContractChargingChanges(contract, cmd.getAdjusts(), cmd.getFrees());

		contract.setPaymentFlag(exist.getPaymentFlag());
		contractSearcher.feedDoc(contract);
		
		ExecutorUtil.submit(new Runnable() {
			@Override
			public void run() {
				generatePaymentExpectancies(contract, cmd.getChargingItems(), cmd.getAdjusts(), cmd.getFrees());
			}
		});

		ContractDetailDTO contractDetailDTO = ConvertHelper.convert(contract, ContractDetailDTO.class);
		
		return contractDetailDTO;
		//ConvertHelper.convert(contract, ContractDetailDTO.class);
	}

	protected Timestamp setToEnd(Long date) {
		try{
			Timestamp stp = new Timestamp(date);
			LocalDateTime time = stp.toLocalDateTime();
			return Timestamp.valueOf(LocalDateTime.of(time.toLocalDate(), LocalTime.MAX).format(dateTimeFormat));
		}catch (Exception e){
			return new Timestamp(date);
		}
	}

	protected static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	protected Timestamp setToBegin(Long date) {
		try{
			Timestamp stp = new Timestamp(date);
			LocalDateTime time = stp.toLocalDateTime();
			return Timestamp.valueOf(LocalDateTime.of(time.toLocalDate(), LocalTime.MIN).format(dateTimeFormat));
		}catch (Exception e){
			return new Timestamp(date);
		}
	}

	@Override
	public void denunciationContract(DenunciationContractCommand cmd) {
		if(cmd.getPaymentFlag() == 1) {
			checkContractAuth(cmd.getNamespaceId(), PrivilegeConstants.PAYMENT_CONTRACT_DENUNCIATION, cmd.getOrgId(), cmd.getCommunityId());
		} else {
			checkContractAuth(cmd.getNamespaceId(), PrivilegeConstants.CONTRACT_DENUNCIATION, cmd.getOrgId(), cmd.getCommunityId());
		}
		Contract contract = checkContract(cmd.getId());
		Contract exist = checkContract(cmd.getId());
		contract.setStatus(ContractStatus.DENUNCIATION.getCode());
		contract.setDenunciationReason(cmd.getDenunciationReason());
		contract.setDenunciationUid(cmd.getDenunciationUid());
		contract.setDenunciationTime(new Timestamp(cmd.getDenunciationTime()));
		contract.setCostGenerationMethod(cmd.getCostGenerationMethod());
		contract.setContractType(ContractType.DENUNCIATION.getCode());

		//退约进工作流，没有工作流不允许退约
		if(cmd.getPaymentFlag() == 1) {
			addToFlowCase(contract, flowcasePaymentContractOwnerType);
		}else {
			addToFlowCase(contract, flowcaseContractOwnerType);
		}
		
		contractProvider.updateContract(contract);
		// 添加退约日志
		contractProvider.saveContractEvent(ContractTrackingTemplateCode.CONTRACT_UPDATE,contract,exist);
		contractSearcher.feedDoc(contract);

	}

	@Override
	public void reviewContract(ReviewContractCommand cmd) {
		Contract contract = checkContract(cmd.getId());
		if(ContractStatus.WAITING_FOR_APPROVAL.equals(cmd.getResult())
				&& !ContractStatus.WAITING_FOR_LAUNCH.equals(contract.getStatus())) {
			LOGGER.error("only waiting for launch contract can launch!");
			throw RuntimeErrorException.errorWith(ContractErrorCode.SCOPE, ContractErrorCode.ERROR_CONTRACT_NOT_WAITING_FOR_LAUNCH,
					"contract status is not waiting for launch!");
		}
		//查询合同适用场景，物业合同不修改资产状态。
        ContractCategory contractCategory = contractProvider.findContractCategoryById(contract.getCategoryId());

        //作废合同，释放房源
		if(ContractStatus.INVALID.equals(ContractStatus.fromStatus(cmd.getResult()))) {
			if(cmd.getPaymentFlag() == 1) {
				checkContractAuth(cmd.getNamespaceId(), PrivilegeConstants.PAYMENT_CONTRACT_INVALID, cmd.getOrgId(), cmd.getCommunityId());
			} else {
				checkContractAuth(cmd.getNamespaceId(), PrivilegeConstants.CONTRACT_INVALID, cmd.getOrgId(), cmd.getCommunityId());
			}
			
			contract.setInvalidReason(cmd.getInvalidReason());
			contract.setInvalidTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			contract.setInvalidUid(UserContext.currentUserId());
			contract.setStatus(cmd.getResult());
			Contract exist = checkContract(cmd.getId());
			contractProvider.updateContract(contract);
			//记录合同事件日志，by tangcen
			contractProvider.saveContractEvent(ContractTrackingTemplateCode.CONTRACT_UPDATE,contract,exist);
			contractSearcher.feedDoc(contract);
//			//todo 将此合同关联的关联的未出账单删除，但账单记录着不用
//			assetService.deleteUnsettledBillsOnContractId(contract.getId());

			//作废合同关联资产释放,by dingjianmin 如果是物业合同场景不释放资产
			if(((contractCategory== null && contract.getPaymentFlag()==1) || !ContractApplicationScene.PROPERTY.equals(ContractApplicationScene.fromStatus(contractCategory.getContractApplicationScene())))){
				List<ContractBuildingMapping> contractApartments = contractBuildingMappingProvider.listByContract(contract.getId());
				if(contractApartments != null && contractApartments.size() > 0) {
					boolean individualFlag = CustomerType.INDIVIDUAL.equals(CustomerType.fromStatus(contract.getCustomerType())) ? true : false;
					contractApartments.forEach(contractApartment -> {
						CommunityAddressMapping addressMapping = propertyMgrProvider.findAddressMappingByAddressId(contractApartment.getAddressId());
						//26058  已售的状态不变
						if(!AddressMappingStatus.SALED.equals(AddressMappingStatus.fromCode(addressMapping.getLivingStatus()))) {
							addressMapping.setLivingStatus(AddressMappingStatus.FREE.getCode());
							propertyMgrProvider.updateOrganizationAddressMapping(addressMapping);
						}

						if(individualFlag) {
							propertyMgrService.deleteAddressToOrgOwner(contract.getNamespaceId(), contractApartment.getAddressId(), contract.getCustomerId());
						}
					});
				}
			}
		}
		//待发起的和审批不通过的能发起审批 ，by dingjianmin 如果是物业合同场景不释放资产
		dbProvider.execute((TransactionStatus status) -> {
			if(ContractStatus.WAITING_FOR_APPROVAL.equals(ContractStatus.fromStatus(cmd.getResult())) &&
					(ContractStatus.WAITING_FOR_LAUNCH.equals(ContractStatus.fromStatus(contract.getStatus()))
							|| ContractStatus.APPROVE_NOT_QUALITIED.equals(ContractStatus.fromStatus(contract.getStatus())))) {
				if(cmd.getPaymentFlag() == 1) {
					checkContractAuth(cmd.getNamespaceId(), PrivilegeConstants.PAYMENT_CONTRACT_LAUNCH, cmd.getOrgId(), cmd.getCommunityId());
				} else {
					checkContractAuth(cmd.getNamespaceId(), PrivilegeConstants.CONTRACT_LAUNCH, cmd.getOrgId(), cmd.getCommunityId());
				}
				//发起审批要把门牌状态置为被占用
				List<ContractBuildingMapping> contractApartments = contractBuildingMappingProvider.listByContract(contract.getId());
				
				//校验房源是否可以发起审批 变更，续约，退约的不校验房源状态
				if(ContractType.NEW.equals(ContractType.fromStatus(contract.getContractType()))) {
					FindContractCommand command = new FindContractCommand();
					command.setId(contract.getId());
					command.setPartyAId(contract.getPartyAId());
					command.setCommunityId(contract.getCommunityId());
					command.setNamespaceId(contract.getNamespaceId());
					command.setCategoryId(contract.getCategoryId());
					ContractDetailDTO contractDetailDTO = findContract(command);
					Boolean possibleEnterContractStatus = possibleEnterContract(contractDetailDTO);
					
					if (!possibleEnterContractStatus) {
						LOGGER.error("possibleEnterContractStatus is false, Apartments is not free");
						throw RuntimeErrorException.errorWith(ContractErrorCode.SCOPE, ContractErrorCode.ERROR_APARTMENTS_NOT_FREE_ERROR,
								"apartments status is not free for contract!");
					}
				}
				
				if(contractApartments != null && contractApartments.size() > 0) {
					List<Long> addressIds = contractApartments.stream().map(contractApartment -> contractApartment.getAddressId()).collect(Collectors.toList());
					//续约和变更的继承原合同的不用检查也不用改状态
					if(ContractType.CHANGE.equals(ContractType.fromStatus(contract.getContractType()))
							|| ContractType.RENEW.equals(ContractType.fromStatus(contract.getContractType()))) {
						if(contract.getParentId() != null) {
							Contract parentContract = contractProvider.findContractById(contract.getParentId());
							if(parentContract != null && ContractStatus.ACTIVE.equals(ContractStatus.fromStatus(parentContract.getStatus()))) {
								List<ContractBuildingMapping> parentContractApartments = contractBuildingMappingProvider.listByContract(parentContract.getId());
								if(parentContractApartments != null && parentContractApartments.size() > 0) {
									List<Long> parentAddressIds = parentContractApartments.stream().map(contractApartment -> contractApartment.getAddressId()).collect(Collectors.toList());
									//去掉已被继承的门牌
									parentAddressIds.forEach(parentAddressId -> {
										addressIds.remove(parentAddressId);
									});
								}
							}
						}
					}
					
					//如果不是物业场景，合同发起审批会把门牌置为已占用状态 待签约
					if((contractCategory== null && contract.getPaymentFlag()==1) || !ContractApplicationScene.PROPERTY.equals(ContractApplicationScene.fromStatus(contractCategory.getContractApplicationScene()))){
						List<CommunityAddressMapping> mappings = propertyMgrProvider.listCommunityAddressMappingByAddressIds(addressIds);
						if(mappings != null && mappings.size() > 0) {
							//对于审批不通过合同 先检查是否全是待租的，不是的话报错
							if(ContractStatus.APPROVE_NOT_QUALITIED.equals(ContractStatus.fromStatus(contract.getStatus()))){
								for(CommunityAddressMapping mapping : mappings) {
									if(!AddressMappingStatus.FREE.equals(AddressMappingStatus.fromCode(mapping.getLivingStatus()))) {
										LOGGER.error("contract apartment is not all free! mapping: {}", mapping);
										throw RuntimeErrorException.errorWith(ContractErrorCode.SCOPE, ContractErrorCode.ERROR_CONTRACT_APARTMENT_IS_NOT_FREE,
												"contract apartment is not all free!");
									}
								}
							}

							mappings.forEach(mapping -> {
								//26058  已售的状态不变
								if(!AddressMappingStatus.SALED.equals(AddressMappingStatus.fromCode(mapping.getLivingStatus()))) {
									mapping.setLivingStatus(AddressMappingStatus.SIGNEDUP.getCode());
									mapping.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
									propertyMgrProvider.updateOrganizationAddressMapping(mapping);
								}
							});
						}
					}

				}
				if(cmd.getPaymentFlag() == 1) {
					addToFlowCase(contract, flowcasePaymentContractOwnerType);
				}else {
					addToFlowCase(contract, flowcaseContractOwnerType);
				}
				//添加发起人字段
				if (UserContext.currentUserId() != null) {
					contract.setSponsorUid(UserContext.currentUserId().toString());
					contract.setSponsorTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
				}
				
				//工作流未开启，修改数据需要回滚，然后同步es，把最新的状态同步到es，否则数据就会不一致  更新完所有的操作，要在最后同步es by -- dingjianmin
				contract.setStatus(cmd.getResult());
				Contract exist = checkContract(cmd.getId());
				contractProvider.updateContract(contract);
				//记录合同事件日志，by tangcen
				contractProvider.saveContractEvent(ContractTrackingTemplateCode.CONTRACT_UPDATE,contract,exist);
				contractSearcher.feedDoc(contract);
			}
			return null;
		});


	}

	@Override
	public void entryContract(EntryContractCommand cmd) {
		// 仅用于多线程任务，用户登陆信息丢失，合同一键免批
		if (!"".equals(cmd.getUser()) && cmd.getUser() != null) {
			User user = (User) StringHelper.fromJsonString(cmd.getUser(), User.class);
			user.setNamespaceId(cmd.getNamespaceId());
			UserContext.setCurrentUser(user);
		}
		checkContractAuth(cmd.getNamespaceId(), PrivilegeConstants.CONTRACT_ENTRY, cmd.getOrgId(), cmd.getCommunityId());
		Contract contract = checkContract(cmd.getId());
		if(!ContractStatus.APPROVE_QUALITIED.equals(ContractStatus.fromStatus(contract.getStatus()))) {
			LOGGER.error("contract is not approve qualitied! id: {}", cmd.getId());
			throw RuntimeErrorException.errorWith(ContractErrorCode.SCOPE, ContractErrorCode.ERROR_CONTRACT_NOT_APPROVE_QUALITIED,
					"contract is not approve qualitied!");
		}
		contract.setStatus(ContractStatus.ACTIVE.getCode());
		contract.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		Contract exist = checkContract(cmd.getId());
		//contractProvider.updateContract(contract);
		//记录合同事件日志，by tangcen
		contractProvider.saveContractEvent(ContractTrackingTemplateCode.CONTRACT_UPDATE,contract,exist);

		//查询合同适用场景，物业合同不修改资产状态。
        ContractCategory contractCategory = contractProvider.findContractCategoryById(contract.getCategoryId());
		
		List<ContractBuildingMapping> contractApartments = contractBuildingMappingProvider.listByContract(contract.getId());
		List<Long> contractAddressIds = new ArrayList<>();
		//物业合同不修改门牌状态
		if((contractCategory== null && exist.getPaymentFlag()==1) || !ContractApplicationScene.PROPERTY.equals(ContractApplicationScene.fromStatus(contractCategory.getContractApplicationScene()))){
			
			if(contractApartments != null && contractApartments.size() > 0) {
				boolean individualFlag = CustomerType.INDIVIDUAL.equals(CustomerType.fromStatus(contract.getCustomerType())) ? true : false;
				contractAddressIds = contractApartments.stream().map(contractApartment -> contractApartment.getAddressId()).collect(Collectors.toList());
				List<CommunityAddressMapping> mappings = propertyMgrProvider.listCommunityAddressMappingByAddressIds(contractAddressIds);
				if(mappings != null && mappings.size() > 0) {
					mappings.forEach(mapping -> {
						//26058  已售的状态不变
						if(!AddressMappingStatus.SALED.equals(AddressMappingStatus.fromCode(mapping.getLivingStatus()))) {
							mapping.setLivingStatus(AddressMappingStatus.RENT.getCode());
							propertyMgrProvider.updateOrganizationAddressMapping(mapping);
						}
						if(individualFlag) {
							propertyMgrService.addAddressToOrganizationOwner(contract.getNamespaceId(), mapping.getAddressId(), contract.getCustomerId());
						}
						//#30536 【智谷汇】后台，“客户资源管理”模块字段显示问题
						if (contract.getCustomerType()==0) {
							CustomerEntryInfo entryInfo = ConvertHelper.convert(contract, CustomerEntryInfo.class);
							entryInfo.setAddressId(mapping.getAddressId());
							entryInfo.setBuildingId(mapping.getBuildingId());
							entryInfo.setAddress(mapping.getOrganizationAddress());
							enterpriseCustomerProvider.createCustomerEntryInfo(entryInfo);
						}
					});
				}
			}
			//查询企业客户信息
			if (contract.getCustomerType()==0) {
				EnterpriseCustomer enterpriseCustomer = enterpriseCustomerProvider.findById(contract.getCustomerId());
				enterpriseCustomer.setLevelItemId((long)CustomerLevelType.REGISTERED_CUSTOMER.getCode());
				enterpriseCustomer.setCustomerSource(InvitedCustomerType.ENTEPRIRSE_CUSTOMER.getCode());
				enterpriseCustomerProvider.updateEnterpriseCustomer(enterpriseCustomer);
				enterpriseCustomerSearcher.feedDoc(enterpriseCustomer);
			}
		}
		
		assetService.upodateBillStatusOnContractStatusChange(contract.getId(), AssetPaymentConstants.CONTRACT_SAVE);
		if(contract.getParentId() != null) {
			Contract parentContract = contractProvider.findContractById(contract.getParentId());
			if(parentContract != null) {
				//add by tangcen 变更合同入场后，要对父合同的未出账单进行处理
				if(ContractType.CHANGE.equals(ContractType.fromStatus(contract.getContractType()))){
					assetService.deleteUnsettledBillsOnContractId(parentContract.getCostGenerationMethod(),contract.getParentId(),contract.getContractStartDate());
					long assetCategoryId = 0l;
    				if(contract.getCategoryId() != null){
    					assetCategoryId = assetProvider.getOriginIdFromMappingApp(21200l, contract.getCategoryId(), ServiceModuleConstants.ASSET_MODULE);
    		        }
					BigDecimal totalAmount = assetProvider.getBillExpectanciesAmountOnContract(parentContract.getContractNumber(),parentContract.getId(), assetCategoryId, cmd.getNamespaceId());
					parentContract.setRent(totalAmount);
				}

				if((contractCategory== null && contract.getPaymentFlag()==1) || !ContractApplicationScene.PROPERTY.equals(ContractApplicationScene.fromStatus(contractCategory.getContractApplicationScene()))){
					List<ContractBuildingMapping> parentContractApartments = contractBuildingMappingProvider.listByContract(parentContract.getId());
					if(parentContractApartments != null && parentContractApartments.size() > 0) {
						List<Long> addressIds = parentContractApartments.stream().map(contractApartment -> contractApartment.getAddressId()).collect(Collectors.toList());
						//去掉已被继承的门牌
						contractAddressIds.forEach(contractAddressId -> {
							addressIds.remove(contractAddressId);
						});
						List<CommunityAddressMapping> mappings = propertyMgrProvider.listCommunityAddressMappingByAddressIds(addressIds);
						if(mappings != null && mappings.size() > 0) {
							boolean individualFlag = CustomerType.INDIVIDUAL.equals(CustomerType.fromStatus(parentContract.getCustomerType())) ? true : false;
							mappings.forEach(mapping -> {
								//26058  已售的状态不变
								if(!AddressMappingStatus.SALED.equals(AddressMappingStatus.fromCode(mapping.getLivingStatus()))) {
									mapping.setLivingStatus(AddressMappingStatus.FREE.getCode());
									mapping.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
									propertyMgrProvider.updateOrganizationAddressMapping(mapping);
								}

								if(individualFlag) {
									propertyMgrService.deleteAddressToOrgOwner(parentContract.getNamespaceId(), mapping.getAddressId(), parentContract.getCustomerId());
								}
								
								//#30536 【智谷汇】后台，“客户资源管理”模块字段显示问题
								if (contract.getCustomerType()==0) {
									CustomerEntryInfo entryInfo = ConvertHelper.convert(contract, CustomerEntryInfo.class);
									entryInfo.setAddressId(mapping.getAddressId());
									entryInfo.setBuildingId(mapping.getBuildingId());
									entryInfo.setAddress(mapping.getOrganizationAddress());
									enterpriseCustomerProvider.createCustomerEntryInfo(entryInfo);
								}
							});
						}
					}
				}
				
				parentContract.setStatus(ContractStatus.HISTORY.getCode());
				contractProvider.updateContract(parentContract);
				contractSearcher.feedDoc(parentContract);
			}
		}

		//入场报错合同变为正常合同
		contractProvider.updateContract(contract);
		contractSearcher.feedDoc(contract);
	}

	@Override
	public void setContractParam(SetContractParamCommand cmd) {
		//查看获得参数接口，不做权限校验。
		//checkContractAuth(cmd.getNamespaceId(), PrivilegeConstants.CONTRACT_PARAM_UPDATE, cmd.getOrgId(), cmd.getCommunityId());
		String contractNumberRulejson = StringHelper.toJsonString(cmd.getGenerateContractNumberRule());
		ContractParam param = ConvertHelper.convert(cmd, ContractParam.class);
		if (cmd.getCommunityId() == null) {
			param.setOwnerId(cmd.getOrgId());
		}
//		param.setOwnerType(EntityType.ORGANIZATIONS.getCode());
		param.setContractNumberRulejson(contractNumberRulejson);
		param.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		ContractParam communityExist = contractProvider.findContractParamByCommunityId(cmd.getNamespaceId(), cmd.getCommunityId(), cmd.getPayorreceiveContractType(),cmd.getOrgId(), cmd.getCategoryId(),null);
		if(cmd.getId() == null && communityExist == null) {
			contractProvider.createContractParam(param);
			dealParamGroupMap(param.getId(), cmd.getNotifyGroups(), cmd.getPaidGroups());
		} else if(cmd.getId() != null && communityExist == null){
			contractProvider.createContractParam(param);
			dealParamGroupMap(param.getId(), cmd.getNotifyGroups(), cmd.getPaidGroups());
		}  else if(cmd.getId() != null && communityExist != null && cmd.getId().equals(communityExist.getId())){
			contractProvider.updateContractParam(param);
			dealParamGroupMap(param.getId(), cmd.getNotifyGroups(), cmd.getPaidGroups());
		} else {
			LOGGER.error("the community already have param: cmd: {}, exist: {}", cmd, communityExist);
			throw RuntimeErrorException.errorWith(ContractErrorCode.SCOPE, ContractErrorCode.ERROR_CONTRACT_PARAM_NOT_EXIST,
					"community contract param is already exit");
		}

	}

	protected void dealParamGroupMap(Long id, List<ContractParamGroupMapDTO> notifyGroups, List<ContractParamGroupMapDTO> paidGroups) {
		List<ContractParamGroupMap> existNotifyGroups = contractProvider.listByParamId(id, ContractParamGroupType.NOTIFY_GROUP.getCode());
		List<ContractParamGroupMap> existPaidGroups = contractProvider.listByParamId(id, ContractParamGroupType.PAY_GROUP.getCode());
		Map<Long, ContractParamGroupMap> notifyGroupMap = new HashMap<>();
		if(existNotifyGroups != null && existNotifyGroups.size() > 0) {
			existNotifyGroups.forEach(notifyGroup -> {
				notifyGroupMap.put(notifyGroup.getId(), notifyGroup);
			});
		}

		Map<Long, ContractParamGroupMap> paidGroupMap = new HashMap<>();
		if(existPaidGroups != null && existPaidGroups.size() > 0) {
			existPaidGroups.forEach(paidGroup -> {
				paidGroupMap.put(paidGroup.getId(), paidGroup);
			});
		}
		dealParamGroupMap(id, notifyGroups, notifyGroupMap);
		dealParamGroupMap(id, paidGroups, paidGroupMap);

	}

	protected void dealParamGroupMap(Long id, List<ContractParamGroupMapDTO> groups, Map<Long, ContractParamGroupMap> map) {
		if(groups != null && groups.size() > 0) {
			groups.forEach(group -> {
				if(group.getId() == null) {
					ContractParamGroupMap paramGroupMap = ConvertHelper.convert(group, ContractParamGroupMap.class);
					paramGroupMap.setParamId(id);
					contractProvider.createContractParamGroupMap(paramGroupMap);
				} else {
					map.remove(group.getId());
				}
			});
		}
		if(map.size() > 0) {
			map.forEach((mapId, group) -> {
				contractProvider.deleteContractParamGroupMap(group);
			});
		}
	}

	@Override
	public ContractParamDTO getContractParam(GetContractParamCommand cmd) {
		/*
		 * 在实现合同多入口时，“合同基础参数配置”页面被产品移到了“管理”按钮里面，当功能属于“管理”里面的功能时，按产品的定义， 权限列表也不应该出现对应的权限（管理里只应用管理员才有权限、不需要通过权限列表来分配）；
		 * 当把权限从权限列表去掉时（从eh_service_modules表里删除了对应的三级module），会导致原来的“合同基础参数配置” 页面所用的接口权限校验报错，原因是找不到对应的module_id了，参考了“管理”里的公共平台实现的“权限列表”、“工作流”界面，
		 * 里面都没有进行权限校验（猜测其是通过控制“管理”按钮显示来实现权限控制的），所以“合同基础参数配置”所用的接口也相应地注释掉权限校验（ 该方式属于临时方式，等公共平台出台统一的规则后再按对应的规则修改）； 
		 * 产品需求: 系统管理员和应用管理员的概念不一样，应用管理员是有这个应用的全部权限，系统管理员是拥有这个系统的全部权限， 所以应用管理员和系统管理员都有权限设置管理里面的所有功能包括合同基础参数设置。
		 * 我们此处获得参数设置也是不需要进行权限校验的，所以在这里以及设置参数那边，我们把权限的校验规则给放开，不做权限校验。
		 */
		//checkContractAuth(cmd.getNamespaceId(), PrivilegeConstants.CONTRACT_PARAM_LIST, cmd.getOrgId(), cmd.getCommunityId());
		ContractParam communityExist = contractProvider.findContractParamByCommunityId(cmd.getNamespaceId(), cmd.getCommunityId(), cmd.getPayorreceiveContractType(), null,cmd.getCategoryId(),cmd.getAppId());
		//查询在某一个入口，某一个小区，某个收付款合同类型，最低规则
		if(communityExist != null) {
			return toContractParamDTO(communityExist);
		} else if(communityExist == null && cmd.getCommunityId() != null) {
			//设置的全部规则，又分为不同入口的全部规则，收付款
			communityExist = contractProvider.findContractParamByCommunityId(cmd.getNamespaceId(), null, cmd.getPayorreceiveContractType(),cmd.getOrgId(),cmd.getCategoryId(),cmd.getAppId());
			if(communityExist != null) {
				return toContractParamDTO(communityExist);
			}else {//原来的规则
				communityExist = contractProvider.findContractParamByCommunityId(cmd.getNamespaceId(), null, null,cmd.getOrgId(), null,cmd.getAppId());
				if(communityExist != null) {
					return toContractParamDTO(communityExist);
				}
			}
		//原来的规则
		} else if(communityExist == null && cmd.getPayorreceiveContractType() != null && cmd.getCategoryId() != null) {
			communityExist = contractProvider.findContractParamByCommunityId(cmd.getNamespaceId(), null, null, cmd.getOrgId(),cmd.getCategoryId(),cmd.getAppId());
			if(communityExist != null) {
				return toContractParamDTO(communityExist);
			}
		}
		return null;
	}

	@Override
	public List<OrganizationDTO> getUserGroups(GetUserGroupsCommand cmd) {
		List<OrganizationMember> results = organizationProvider.listOrganizationMembersByUId(cmd.getUserId());

		List<OrganizationMember> members = results.stream().filter(r ->
				r.getGroupType().equals(OrganizationGroupType.DEPARTMENT.getCode())
						|| r.getGroupType().equals(OrganizationGroupType.DIRECT_UNDER_ENTERPRISE.getCode())
						|| r.getGroupType().equals(OrganizationGroupType.JOB_POSITION.getCode())
		).collect(Collectors.toList());

		if(members != null && members.size() > 0) {
			List<Long> ids = members.stream().map(member -> member.getOrganizationId()).collect(Collectors.toList());
			List<Organization> organizations = organizationProvider.listOrganizationsByIds(ids);
			if(organizations != null && organizations.size() > 0) {
				return organizations.stream().map(organization -> {
					return ConvertHelper.convert(organization, OrganizationDTO.class);
				}).collect(Collectors.toList());
			}
		}
		return null;
	}

	@Override
	public ListContractsBySupplierResponse listContractsBySupplier(ListContractsBySupplierCommand cmd) {
		ListContractsBySupplierResponse response = new ListContractsBySupplierResponse();
		if(cmd.getPageAnchor() == null) cmd.setPageAnchor(0l);
		if(cmd.getPageSize() == null) cmd.setPageSize(20);
		List<ContractLogDTO> dtos = contractProvider.listContractsBySupplier(cmd.getSupplierId()
				,cmd.getPageAnchor(),cmd.getPageSize() + 1);
		if(dtos.size() > cmd.getPageSize()){
			response.setNextPageAnchor(cmd.getPageAnchor() + cmd.getPageSize());
			dtos.remove(dtos.size() - 1);
		}
		response.setDtos(dtos);
		return null;
	}

	protected ContractParamDTO toContractParamDTO(ContractParam param) {
		ContractParamDTO dto = ConvertHelper.convert(param, ContractParamDTO.class);
		List<ContractParamGroupMap> notifyGroups = contractProvider.listByParamId(param.getId(), ContractParamGroupType.NOTIFY_GROUP.getCode());
		List<ContractParamGroupMap> paidGroups = contractProvider.listByParamId(param.getId(), ContractParamGroupType.PAY_GROUP.getCode());

		if(notifyGroups != null && notifyGroups.size() > 0) {
			dto.setNotifyGroups(notifyGroups.stream().map(group -> ConvertHelper.convert(group, ContractParamGroupMapDTO.class)).collect(Collectors.toList()));
		}
		if(paidGroups != null && paidGroups.size() > 0) {
			dto.setPaidGroups(paidGroups.stream().map(group -> ConvertHelper.convert(group, ContractParamGroupMapDTO.class)).collect(Collectors.toList()));
		}
		if (param.getContractNumberRulejson()!= null) {
			String contractNumberRulejson = param.getContractNumberRulejson();
			GenerateContractNumberRule contractNumberRule = (GenerateContractNumberRule)StringHelper.fromJsonString(contractNumberRulejson, GenerateContractNumberRule.class);
			dto.setGenerateContractNumberRule(contractNumberRule);
		}
		return dto;
	}

	@Override
	public void deleteContract(DeleteContractCommand cmd) {
		if(cmd.getPaymentFlag() == 1) {
			checkContractAuth(cmd.getNamespaceId(), PrivilegeConstants.PAYMENT_CONTRACT_DELETE, cmd.getOrgId(), cmd.getCommunityId());
		} else {
			// sync from ebei api has checkAuth flag
			if (cmd.getCheckAuth() == null || cmd.getCheckAuth())
			checkContractAuth(cmd.getNamespaceId(), PrivilegeConstants.CONTRACT_DELETE, cmd.getOrgId(), cmd.getCommunityId());
		}

		Contract contract = checkContract(cmd.getId());
		Boolean flag = false;
		if(ContractStatus.WAITING_FOR_LAUNCH.equals(ContractStatus.fromStatus(contract.getStatus())) || ContractStatus.ACTIVE.equals(ContractStatus.fromStatus(contract.getStatus()))
				|| ContractStatus.WAITING_FOR_APPROVAL.equals(ContractStatus.fromStatus(contract.getStatus()))  || ContractStatus.APPROVE_QUALITIED.equals(ContractStatus.fromStatus(contract.getStatus()))
				|| ContractStatus.EXPIRING.equals(ContractStatus.fromStatus(contract.getStatus()))  || ContractStatus.DRAFT.equals(ContractStatus.fromStatus(contract.getStatus()))) {
			flag = true;
		}
		//查询合同适用场景，物业合同不修改资产状态。
        ContractCategory contractCategory = contractProvider.findContractCategoryById(contract.getCategoryId());

        if (contractCategory== null && contract.getPaymentFlag()==1) {
        	flag = true;
		} else if (ContractApplicationScene.PROPERTY.equals(ContractApplicationScene.fromStatus(contractCategory.getContractApplicationScene()))) {
			flag = false;
		}
        
		contract.setStatus(ContractStatus.INACTIVE.getCode());
		contract.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		contractProvider.updateContract(contract);
		contractSearcher.feedDoc(contract);
		//添加合同日志 by tangcen
		contractProvider.saveContractEvent(ContractTrackingTemplateCode.CONTRACT_DELETE,contract,null);
		//释放资源状态
		if(flag) {
			List<ContractBuildingMapping> contractApartments = contractBuildingMappingProvider.listByContract(contract.getId());
			if(contractApartments != null && contractApartments.size() > 0) {
				boolean individualFlag = CustomerType.INDIVIDUAL.equals(CustomerType.fromStatus(contract.getCustomerType())) ? true : false;
				contractApartments.forEach(contractApartment -> {
					CommunityAddressMapping addressMapping = propertyMgrProvider.findAddressMappingByAddressId(contractApartment.getAddressId());
					if(addressMapping != null) {
						//26058  已售的状态不变
						if(!AddressMappingStatus.SALED.equals(AddressMappingStatus.fromCode(addressMapping.getLivingStatus()))) {
							addressMapping.setLivingStatus(AddressMappingStatus.FREE.getCode());
							propertyMgrProvider.updateOrganizationAddressMapping(addressMapping);
						}

						if(individualFlag) {
							propertyMgrService.addAddressToOrganizationOwner(contract.getNamespaceId(), contractApartment.getAddressId(), contract.getCustomerId());
						}
					}
				});
			}
		}
	}

	@Override
	public ContractDetailDTO findContract(FindContractCommand cmd) {
//		Integer namespaceId = cmd.getNamespaceId()==null?UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
//		if(namespaceId == 999971) {
//			ThirdPartContractHandler handler = PlatformContext.getComponent(ThirdPartContractHandler.CONTRACT_PREFIX + namespaceId);
//			ContractDetailDTO response = handler.findContract(cmd);
//			return response;
//		}
		Contract contract = checkContract(cmd.getId());
		ContractDetailDTO dto = ConvertHelper.convert(contract, ContractDetailDTO.class);

		dto.setCommunityId(cmd.getCommunityId());
		dto.setNamespaceId(cmd.getNamespaceId());
		dto.setCategoryId(cmd.getCategoryId());
		
		if(dto.getCreateUid() != null) {
			User creator = userProvider.findUserById(dto.getCreateUid());
			if(creator != null) {
				dto.setCreatorName(creator.getNickName());
			}

		}

		if(dto.getDenunciationUid() != null) {
			User denunciactionName = userProvider.findUserById(dto.getDenunciationUid());
			if (denunciactionName!=null) {
				OrganizationMemberDetails organizationMember = organizationProvider.findOrganizationMemberDetailsByTargetId(denunciactionName.getId());
				if(organizationMember != null) {
					dto.setDenunciationName(organizationMember.getContactName());
				}else{
					dto.setDenunciationName(denunciactionName.getNickName());
				}
			}

		}

		if(contract.getPartyAId() != null && contract.getPartyAType() != null) {
			if(0 == contract.getPartyAType()) {
				Organization organization = organizationProvider.findOrganizationById(contract.getPartyAId());
				if(organization != null) {
					dto.setPartyAName(organization.getName());
				}
			}

		}

		if(contract.getApplicationId() != null) {
			Requisition requisition = requisitionProvider.findRequisitionById(contract.getApplicationId());
			if(requisition != null) {
				dto.setApplicationTheme(requisition.getTheme());
			}
		}

		if(contract.getCreateOrgId() != null) {
			Organization organization = organizationProvider.findOrganizationById(contract.getCreateOrgId());
			if(organization != null) {
				dto.setCreateOrgName(organization.getName());
			}
		}

		if(contract.getCreatePositionId() != null) {
			Organization organization = organizationProvider.findOrganizationById(contract.getCreatePositionId());
			if(organization != null) {
				dto.setCreatePositionName(organization.getName());
			}
		}

		if(CustomerType.ENTERPRISE.equals(CustomerType.fromStatus(dto.getCustomerType()))) {
			EnterpriseCustomer customer = enterpriseCustomerProvider.findById(dto.getCustomerId());
			if(customer != null) {
				dto.setCustomerName(customer.getName());
			}
		} else if(CustomerType.INDIVIDUAL.equals(CustomerType.fromStatus(dto.getCustomerType()))) {
			OrganizationOwner owner = individualCustomerProvider.findOrganizationOwnerById(dto.getCustomerId());
			if(owner != null) {
				dto.setCustomerName(owner.getContactName());
			}

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

		if(contract.getLayout() != null && StringUtils.isNotBlank(contract.getLayout()) && StringUtils.isNumeric(contract.getLayout())) {
			ScopeFieldItem item =  fieldProvider.findScopeFieldItemByFieldItemId(contract.getNamespaceId(),cmd.getOrgId(), contract.getCommunityId(), Long.valueOf(contract.getLayout()));
			if(item != null) {
				dto.setLayoutName(item.getItemDisplayName());
			}
		}
		//获取合同模板的名称
		if (contract.getTemplateId() != null) {
			ContractTemplate contractTemplateParent = contractProvider.findContractTemplateById(contract.getTemplateId());
			if(contractTemplateParent != null) {
				dto.setTemplateName(contractTemplateParent.getName());
			}
		}
		
		if (ContractType.CHANGE.equals(ContractType.fromStatus(contract.getContractType()))) {
			Contract parentContract = checkContract(contract.getParentId());
			dto.setCostGenerationMethod(parentContract.getCostGenerationMethod());
			//向前端返回时间范围
			SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
			List<ContractChargingItem> chargingItems = contractChargingItemProvider.listByContractId(parentContract.getId());
			if (chargingItems!=null && chargingItems.size()>0) {
				List<ChargingItemVariables>  chargingPaymentTypeVariables = new ArrayList<ChargingItemVariables>();
				for (int i = 0; i < chargingItems.size(); i++) {
					//issue-42416,更新自然季，合同刷新账单报错。
					if (chargingItems.get(i).getChargingStartTime() == null || contract.getContractStartDate() == null) {
						continue ;
					}
					if ((chargingItems.get(i).getChargingStartTime()).before(contract.getContractStartDate())) {
					// <li>costGenerationMethod: 费用截断方式，0：按计费周期，1：按实际天数</li>
					ChargingItemVariables  chargingPaymentTypeVariable = new ChargingItemVariables();
					//根据合同应用的categoryId去查找对应的缴费应用的categoryId
					Long assetCategoryId = assetProvider.getOriginIdFromMappingApp(21200l,dto.getCategoryId(), ServiceModuleConstants.ASSET_MODULE);
					String projectChargingItemName = assetProvider.findProjectChargingItemNameByCommunityId(dto.getCommunityId(),dto.getNamespaceId(),assetCategoryId,chargingItems.get(i).getChargingItemId());
					if(chargingItems.get(i).getChargingStartTime() != null){
						String endTimeByDay = "";
						String endTimeByPeriod = "";
						chargingPaymentTypeVariable.setChargingItemName(projectChargingItemName);
						chargingPaymentTypeVariable.setStartTime(yyyyMMdd.format(chargingItems.get(i).getChargingStartTime()));
						if ((contract.getContractStartDate()).after(chargingItems.get(i).getChargingExpiredTime())) {
							endTimeByDay=yyyyMMdd.format(chargingItems.get(i).getChargingExpiredTime());
							endTimeByPeriod = assetProvider.findEndTimeByPeriod(endTimeByDay,parentContract.getId(), chargingItems.get(i).getChargingItemId());
						}else {
							endTimeByDay=yyyyMMdd.format(contract.getContractStartDate());
							endTimeByPeriod = assetProvider.findEndTimeByPeriod(endTimeByDay,parentContract.getId(), chargingItems.get(i).getChargingItemId());
						}
						chargingPaymentTypeVariable.setEndTimeByDay(endTimeByDay);
						chargingPaymentTypeVariable.setEndTimeByPeriod(endTimeByPeriod);
						chargingPaymentTypeVariables.add(chargingPaymentTypeVariable);
					}
					dto.setChargingPaymentTypeVariables(chargingPaymentTypeVariables);
					}
				}
			}
		}

		if (ContractStatus.DENUNCIATION.equals(ContractStatus.fromStatus(contract.getStatus()))) {
			//向前端返回时间范围
			SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
			List<ContractChargingItem> chargingItems = contractChargingItemProvider.listByContractId(contract.getId());
			if (chargingItems!=null && chargingItems.size()>0) {
				List<ChargingItemVariables>  chargingPaymentTypeVariables = new ArrayList<ChargingItemVariables>();
				for (int i = 0; i < chargingItems.size(); i++) {
					//issue-42416,更新自然季，合同刷新账单报错。
					if (chargingItems.get(i).getChargingStartTime() == null || contract.getDenunciationTime() == null) {
						continue ;
					}
					if ((chargingItems.get(i).getChargingStartTime()).before(contract.getDenunciationTime())) {
					// <li>costGenerationMethod: 费用截断方式，0：按计费周期，1：按实际天数</li>
					ChargingItemVariables  chargingPaymentTypeVariable = new ChargingItemVariables();
					//根据合同应用的categoryId去查找对应的缴费应用的categoryId
					Long assetCategoryId = assetProvider.getOriginIdFromMappingApp(21200l,dto.getCategoryId(), ServiceModuleConstants.ASSET_MODULE);
					String projectChargingItemName = assetProvider.findProjectChargingItemNameByCommunityId(dto.getCommunityId(),dto.getNamespaceId(),assetCategoryId,chargingItems.get(i).getChargingItemId());
					if(chargingItems.get(i).getChargingStartTime() != null){
						String endTimeByDay = "";
						String endTimeByPeriod = "";
						chargingPaymentTypeVariable.setChargingItemName(projectChargingItemName);
						chargingPaymentTypeVariable.setStartTime(yyyyMMdd.format(chargingItems.get(i).getChargingStartTime()));

						//35563 【合同管理2.8】查看退约的合同，费用收取方式字段，收取时间未显示 by djm
						endTimeByDay=yyyyMMdd.format(contract.getDenunciationTime());
						endTimeByPeriod = assetProvider.findEndTimeByPeriod(endTimeByDay,contract.getId(), chargingItems.get(i).getChargingItemId());

						chargingPaymentTypeVariable.setEndTimeByDay(endTimeByDay);
						chargingPaymentTypeVariable.setEndTimeByPeriod(endTimeByPeriod);
						chargingPaymentTypeVariables.add(chargingPaymentTypeVariable);
					}
					dto.setChargingPaymentTypeVariables(chargingPaymentTypeVariables);
					}
				}
			}
		}

		processContractApartments(dto);
		processContractChargingItems(dto);
		processContractAttachments(dto);
		processContractChargingChanges(dto);
		processContractPaymentPlans(dto);
		return dto;
	}

	@Override
	public List<ContractDTO> listCustomerContracts(ListCustomerContractsCommand cmd) {
		if(CustomerType.ENTERPRISE.equals(CustomerType.fromStatus(cmd.getTargetType()))) {
			if(cmd.getAdminFlag() == 1) {
				CheckAdminCommand cmd1 = new CheckAdminCommand();
				cmd1.setNamespaceId(cmd.getNamespaceId());
				cmd1.setOrganizationId(cmd.getTargetId());
				if(checkAdmin(cmd1)) {
					EnterpriseCustomer customer = enterpriseCustomerProvider.findByOrganizationId(cmd.getTargetId());
					if(customer != null) {
						ListEnterpriseCustomerContractsCommand command = new ListEnterpriseCustomerContractsCommand();
						command.setNamespaceId(cmd.getNamespaceId());
						command.setCommunityId(cmd.getCommunityId());
						command.setStatus(cmd.getStatus());
						command.setEnterpriseCustomerId(customer.getId());
						//command.setCategoryId(cmd.getCategoryId());
						return listEnterpriseCustomerContracts(command);
					}
				}
			}

		} else if(CustomerType.INDIVIDUAL.equals(CustomerType.fromStatus(cmd.getTargetType()))) {
			UserIdentifier userIdentifier = userProvider.findUserIdentifiersOfUser(cmd.getTargetId(), cmd.getNamespaceId());
			if(userIdentifier != null) {
//				Integer namespaceId = cmd.getNamespaceId()==null?UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
//				if(namespaceId == 999971) {
//					ThirdPartContractHandler handler = PlatformContext.getComponent(ThirdPartContractHandler.CONTRACT_PREFIX + namespaceId);
//					ListIndividualCustomerContractsCommand command = new ListIndividualCustomerContractsCommand();
//					command.setNamespaceId(cmd.getNamespaceId());
//					command.setCommunityId(cmd.getCommunityId());
//					command.setContactToken(userIdentifier.getIdentifierToken());
//					List<ContractDTO> response = handler.listIndividualCustomerContracts(command);
//					return response;
//				}
				List<OrganizationOwner> owners = organizationProvider.findOrganizationOwnerByTokenOrNamespaceId(userIdentifier.getIdentifierToken(), cmd.getNamespaceId());
				if(owners != null && owners.size() > 0) {
					List<ContractDTO> contracts = new ArrayList<>();
					for(OrganizationOwner owner : owners) {
						ListIndividualCustomerContractsCommand command = new ListIndividualCustomerContractsCommand();
						command.setNamespaceId(cmd.getNamespaceId());
						command.setCommunityId(cmd.getCommunityId());
						command.setStatus(cmd.getStatus());
						command.setIndividualCustomerId(owner.getId());
						//command.setCategoryId(cmd.getCategoryId());
						List<ContractDTO> dtos = listIndividualCustomerContracts(command);
						if(dtos != null && dtos.size() > 0) {
							contracts.addAll(dtos);
						}
					}
					return contracts;
				}
			}
		}

		return null;
	}

	@Override
	public List<ContractDTO> listEnterpriseCustomerContracts(ListEnterpriseCustomerContractsCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId()==null?UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
//		if(namespaceId == 999971) {
//			ThirdPartContractHandler handler = PlatformContext.getComponent(ThirdPartContractHandler.CONTRACT_PREFIX + namespaceId);
//			List<ContractDTO> response = handler.listEnterpriseCustomerContracts(cmd);
//			return response;
//		}

		List<Contract> contracts = contractProvider.listContractByCustomerId(cmd.getCommunityId(), cmd.getEnterpriseCustomerId(), CustomerType.ENTERPRISE.getCode(), cmd.getStatus(), cmd.getCategoryId());
		Map<Long, Long> categoryConfigMap = new HashMap<>();
	
		if(contracts != null && contracts.size() > 0) {
			return contracts.stream().map(contract -> {
				ContractDTO dto = ConvertHelper.convert(contract, ContractDTO.class);
				dto.setOrganizationName(contract.getCustomerName());
				dto.setContractTypeName(ContractType.fromStatus(contract.getContractType()).getDescription());
				if(categoryConfigMap.containsKey(contract.getCategoryId())){
					dto.setConfigId(categoryConfigMap.get(contract.getCategoryId()));
				}else{
					categoryConfigMap.put(contract.getCategoryId(), getConfigId(namespaceId, contract.getCategoryId()));
					dto.setConfigId(categoryConfigMap.get(contract.getCategoryId()));
				}
				//查询合同适用场景，物业合同不修改资产状态。
		        ContractCategory contractCategory = contractProvider.findContractCategoryById(contract.getCategoryId());
		        if (contractCategory != null) {
		        	dto.setContractApplicationScene(contractCategory.getContractApplicationScene());
				}
				return dto;
			}).collect(Collectors.toList());

		}
		
		return null;
	}

	protected Long getConfigId(Integer namespaceId, Long categoryId) {
		List<ServiceModuleApp> serviceModuleApp = serviceModuleAppService.listReleaseServiceModuleApp(namespaceId, 21200L, null, categoryId.toString(), null);
		if (serviceModuleApp != null && serviceModuleApp.size()>0){
			return serviceModuleApp.get(0).getId();
		}
		return 0L;
	}

	@Override
	public List<ContractDTO> listIndividualCustomerContracts(ListIndividualCustomerContractsCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId()==null?UserContext.getCurrentNamespaceId():cmd.getNamespaceId();
//		if(namespaceId == 999971) {
//			ThirdPartContractHandler handler = PlatformContext.getComponent(ThirdPartContractHandler.CONTRACT_PREFIX + namespaceId);
//			if(cmd.getIndividualCustomerId() != null) {
//				OrganizationOwner owner = individualCustomerProvider.findOrganizationOwnerById(cmd.getIndividualCustomerId());
//				if(owner != null) {
//					cmd.setContactToken(owner.getContactToken());
//				}
//			}
//			List<ContractDTO> response = handler.listIndividualCustomerContracts(cmd);
//			return response;
//		}

		List<Contract> contracts = contractProvider.listContractByCustomerId(cmd.getCommunityId(), cmd.getIndividualCustomerId(), CustomerType.INDIVIDUAL.getCode(), cmd.getStatus(), cmd.getCategoryId());
		if(contracts != null && contracts.size() > 0) {
			return contracts.stream().map(contract -> ConvertHelper.convert(contract, ContractDTO.class)).collect(Collectors.toList());
		}
		return null;
	}

	@Override
	public List<ContractDTO> listApartmentContracts(ListApartmentContractsCommand cmd) {
		List<Contract> contracts = contractProvider.listContractsByAddressId(cmd.getAddressId());
		Map<Long, Long> categoryConfigMap = new HashMap<Long, Long>();
		
		if(contracts != null && contracts.size() > 0) {
			return contracts.stream().map(contract -> {
				ContractDTO dto = ConvertHelper.convert(contract, ContractDTO.class);
				
				if(categoryConfigMap.containsKey(contract.getCategoryId())){
					dto.setConfigId(categoryConfigMap.get(contract.getCategoryId()));
				}else{
					categoryConfigMap.put(contract.getCategoryId(), getConfigId(cmd.getNamespaceId(), contract.getCategoryId()));
					dto.setConfigId(categoryConfigMap.get(contract.getCategoryId()));
				}
				return dto;
			}).collect(Collectors.toList());
		}
		return null;
	}

	protected Contract checkContract(Long id) {
		Contract contract = contractProvider.findContractById(id);
		if(contract == null) {
			LOGGER.error("contract is not exit! id: {}", id);
			throw RuntimeErrorException.errorWith(ContractErrorCode.SCOPE, ContractErrorCode.ERROR_CONTRACT_NOT_EXIST,
					"contract is not exit");
		}
		return contract;
	}

	protected void processContractApartments(ContractDetailDTO dto) {
		List<ContractBuildingMapping> contractApartments = contractBuildingMappingProvider.listByContract(dto.getId());
		if(contractApartments != null && contractApartments.size() > 0) {
			List<BuildingApartmentDTO> apartmentDtos = contractApartments.stream().map(apartment -> {
				BuildingApartmentDTO apartmentDto = ConvertHelper.convert(apartment, BuildingApartmentDTO.class);
				apartmentDto.setChargeArea(apartment.getAreaSize());
				return apartmentDto;
			}).collect(Collectors.toList());
			dto.setApartments(apartmentDtos);
		}
	}

	protected void processContractChargingChanges(ContractDetailDTO dto) {
		List<ContractChargingChange> contractChargingChanges = contractChargingChangeProvider.listByContractId(dto.getId());
		if(contractChargingChanges != null && contractChargingChanges.size() > 0) {
			List<ContractChargingChangeDTO> adjusts = new ArrayList<>();
			List<ContractChargingChangeDTO> frees = new ArrayList<>();
			contractChargingChanges.forEach(change -> {
				ContractChargingChangeDTO changeDTO = ConvertHelper.convert(change, ContractChargingChangeDTO.class);
//				String itemName = assetProvider.findChargingItemNameById(change.getChargingItemId());
//				changeDTO.setChargingItemName(itemName);
				//根据合同应用的categoryId去查找对应的缴费应用的categoryId
				Long assetCategoryId = assetProvider.getOriginIdFromMappingApp(21200l,dto.getCategoryId(), ServiceModuleConstants.ASSET_MODULE);
				String projectChargingItemName = assetProvider.findProjectChargingItemNameByCommunityId(dto.getCommunityId(),dto.getNamespaceId(),assetCategoryId,change.getChargingItemId());
				changeDTO.setChargingItemName(projectChargingItemName);
				if(change.getChangeStartTime() != null) {
					changeDTO.setChangeStartTime(change.getChangeStartTime().getTime());
				}
				if(change.getChangeExpiredTime() != null) {
					changeDTO.setChangeExpiredTime(change.getChangeExpiredTime().getTime());
				}
				processContractChargingChangeAddresses(changeDTO);
				//物业缴费V6.3合同概览计价条款需要增加账单组名称字段
				if(changeDTO.getBillGroupId() != null) {
					PaymentBillGroup group = assetGroupProvider.getBillGroupById(changeDTO.getBillGroupId());
					if(group != null) {
						changeDTO.setBillGroupName(group.getName());
					}
				}
				if(ChangeType.ADJUST.equals(ChangeType.fromStatus(change.getChangeType()))) {
					adjusts.add(changeDTO);
				} else if(ChangeType.FREE.equals(ChangeType.fromStatus(change.getChangeType()))) {
					frees.add(changeDTO);
				}
			});

			dto.setAdjusts(adjusts);
			dto.setFrees(frees);
		}
	}

	protected void processContractChargingChangeAddresses(ContractChargingChangeDTO dto) {
		List<ContractChargingChangeAddress> changeAddresses = contractChargingChangeAddressProvider.findByChangeId(dto.getId());
		if(changeAddresses != null && changeAddresses.size() > 0) {
			List<BuildingApartmentDTO> addressDtos = new ArrayList<>();
			List<Long> addressIds = new ArrayList<>();
			changeAddresses.forEach(address -> {
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

			changeAddresses.forEach(changeAddress -> {
				BuildingApartmentDTO apartmentDto = new BuildingApartmentDTO();
				apartmentDto.setId(changeAddress.getId());
				apartmentDto.setAddressId(changeAddress.getAddressId());
				Address address = addressMap.get(changeAddress.getAddressId());
				if(address != null) {
					apartmentDto.setApartmentName(address.getApartmentName());
					apartmentDto.setBuildingName(address.getBuildingName());
				}
				addressDtos.add(apartmentDto);
			});

			dto.setApartments(addressDtos);
		}
	}

	protected void processContractChargingItems(ContractDetailDTO dto) {
		List<ContractChargingItem> contractChargingItems = contractChargingItemProvider.listByContractId(dto.getId());
		if(contractChargingItems != null && contractChargingItems.size() > 0) {
			List<ContractChargingItemDTO> chargingItemsDto = contractChargingItems.stream().map(item -> {
				ContractChargingItemDTO itemDto = ConvertHelper.convert(item, ContractChargingItemDTO.class);
				if(item.getChargingStartTime() != null) {
					itemDto.setChargingStartTime(item.getChargingStartTime().getTime());
				}
				if(item.getChargingExpiredTime() != null) {
					itemDto.setChargingExpiredTime(item.getChargingExpiredTime().getTime());
				}
				//根据合同应用的categoryId去查找对应的缴费应用的categoryId
				Long assetCategoryId = assetProvider.getOriginIdFromMappingApp(21200l,dto.getCategoryId(), ServiceModuleConstants.ASSET_MODULE);
				//add by tangcen 显示客户自定义的收费项名称，需要使用缴费应用的categoryId来查
				String projectChargingItemName = assetProvider.findProjectChargingItemNameByCommunityId(dto.getCommunityId(),dto.getNamespaceId(),assetCategoryId,itemDto.getChargingItemId());

				itemDto.setChargingItemName(projectChargingItemName);
				//String itemName = assetProvider.findChargingItemNameById(itemDto.getChargingItemId());
				//itemDto.setChargingItemName(itemName);
				String standardName = assetProvider.getStandardNameById(itemDto.getChargingStandardId());
				itemDto.setChargingStandardName(standardName);
				String lateFeeStandardName = assetProvider.getStandardNameById(itemDto.getLateFeeStandardId());
				itemDto.setLateFeeStandardName(lateFeeStandardName);
				String lateFeeformula = assetProvider.findFormulaByChargingStandardId(itemDto.getLateFeeStandardId());
				itemDto.setLateFeeformula(lateFeeformula);
				//物业缴费V6.3合同概览计价条款需要增加账单组名称字段
				if(itemDto.getBillGroupId() != null) {
					PaymentBillGroup group = assetGroupProvider.getBillGroupById(itemDto.getBillGroupId());
					if(group != null) {
						itemDto.setBillGroupName(group.getName());
					}
				}
				processContractChargingItemAddresses(itemDto);

				return itemDto;
			}).collect(Collectors.toList());
			dto.setChargingItems(chargingItemsDto);
		}
	}

	protected void processContractChargingItemAddresses(ContractChargingItemDTO dto) {
		List<ContractChargingItemAddress> itemAddresses = contractChargingItemAddressProvider.findByItemId(dto.getId());
		if(itemAddresses != null && itemAddresses.size() > 0) {
			List<BuildingApartmentDTO> addressDtos = new ArrayList<>();
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

	protected void processContractAttachments(ContractDetailDTO dto) {
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

	protected void processContractPaymentPlans(ContractDetailDTO dto) {
		List<ContractPaymentPlan> contractPlans = contractPaymentPlanProvider.listByContractId(dto.getId());
		if(contractPlans != null && contractPlans.size() > 0) {
			List<ContractPaymentPlanDTO> dtos = contractPlans.stream().map(plan -> {
				ContractPaymentPlanDTO planDTO = ConvertHelper.convert(plan, ContractPaymentPlanDTO.class);
				if(plan.getPaidTime() != null) {
					planDTO.setPaidTime(plan.getPaidTime().getTime());
				}
				return planDTO;
			}).collect(Collectors.toList());
			dto.setPlans(dtos);
		}
	}

	/**
	 * 每天早上4点50,自动同步合同信息
	 * */
	@Scheduled(cron = "1 50 4 * * ?")
	public void contractAutoSync() {
//		Accessor accessor = bigCollectionProvider.getMapAccessor(CoordinationLocks.SYNC_THIRD_CONTRACT.getCode() + System.currentTimeMillis(), "");
//		RedisTemplate redisTemplate = accessor.getTemplate(stringRedisSerializer);
//		Map<String,String> runningMap  =new HashMap<>();
//		this.coordinationProvider.getNamedLock(CoordinationLocks.SYNC_THIRD_CONTRACT.getCode()).tryEnter(() -> {
//			String runningFlag = getSyncTaskToken(redisTemplate, CoordinationLocks.SYNC_THIRD_CONTRACT.getCode());
//			runningMap.put(CoordinationLocks.SYNC_THIRD_CONTRACT.getCode(), runningFlag);
//			if(StringUtils.isBlank(runningFlag))
//			redisTemplate.opsForValue().set(CoordinationLocks.SYNC_THIRD_CONTRACT.getCode(), "executing", 5, TimeUnit.HOURS);
//		});
//		if(StringUtils.isEmpty(runningMap.get(CoordinationLocks.SYNC_THIRD_CONTRACT.getCode()))) {
		String taskServer = configurationProvider.getValue(ConfigConstants.TASK_SERVER_ADDRESS, "127.0.0.1");
		if (taskServer.equals(equipmentIp)) {
			List<Community> communities = communityProvider.listAllCommunitiesWithNamespaceToken();
			if (communities != null) {
				for (Community community : communities) {
					//查询categoryid
					Long categoryId =null;
					try {
						categoryId = contractProvider.findCategoryIdByNamespaceId(community.getNamespaceId());
					} catch (Exception e) {
						LOGGER.info("contractAutoSync community get categoryId " + categoryId.toString());
					}
					SyncContractsFromThirdPartCommand command = new SyncContractsFromThirdPartCommand();
					command.setNamespaceId(community.getNamespaceId());
					command.setCommunityId(community.getId());
					command.setCategoryId(categoryId);
					syncContractsFromThirdPart(command, false);
				}
			}
		}
	}

	protected String getSyncTaskToken(RedisTemplate redisTemplate,String code) {
		Map<String, String> map = makeSyncTaskToken(redisTemplate,code);
		if(map == null) {
			return null;
		}
		return  map.get(code);
	}

	protected Map<String, String> makeSyncTaskToken(RedisTemplate redisTemplate,String code) {
		Object o = redisTemplate.opsForValue().get(code);
		if(o != null) {
			Map<String, String> keys = new HashMap<>();
			keys.put(code, (String)o);
			return keys;
		} else {
			return null;
		}
	}


	@Override
	public String syncContractsFromThirdPart(SyncContractsFromThirdPartCommand cmd, Boolean authFlag) {
		if(authFlag) {
			checkContractAuth(cmd.getNamespaceId(), PrivilegeConstants.CONTRACT_SYNC, cmd.getOrgId(), cmd.getCommunityId());
		}

		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		if(community == null) {
			return "0";
		}
		int syncCount = zjSyncdataBackupProvider.listZjSyncdataBackupActiveCountByParam(community.getNamespaceId(), community.getNamespaceCommunityToken(), DataType.CONTRACT.getCode());
		if(syncCount > 0) {
			return "1";
		}
		String version;
		if(cmd.getAllSyncFlag() != null && cmd.getAllSyncFlag() == 1) {
			SyncCustomersCommand cmd2 = ConvertHelper.convert(cmd, SyncCustomersCommand.class);
			String cusResponse = customerService.syncEnterpriseCustomers(cmd2, false);
			if(cusResponse.equals("1")){
				return "1";
			}
			version = "0";
		}else {
			version = contractProvider.findLastContractVersionByCommunity(cmd.getNamespaceId(), community.getId());
		}
		ThirdPartContractHandler contractHandler = PlatformContext.getComponent(ThirdPartContractHandler.CONTRACT_PREFIX + cmd.getNamespaceId());
		//查询合同适用场景，物业合同不修改资产状态。
        ContractCategory contractCategory = contractProvider.findContractCategoryById(cmd.getCategoryId());

		if(contractHandler != null) {
			SyncDataTask task = new SyncDataTask();
			task.setOwnerType(EntityType.COMMUNITY.getCode());
			task.setOwnerId(community.getId());
			task.setType(SyncDataTaskType.CONTRACT.getCode());
			task.setCreatorUid(UserContext.currentUserId());
			task.setLockKey(CoordinationLocks.SYNC_CONTRACT.getCode() + cmd.getNamespaceId() + cmd.getCommunityId());
			SyncDataTask dataTask = syncDataTaskService.executeTask(() -> {
				SyncDataResponse response = new SyncDataResponse();
				contractHandler.syncContractsFromThirdPart("1", version, community.getNamespaceCommunityToken(), task.getId(), cmd.getCategoryId(), contractCategory.getContractApplicationScene());
				return response;
			}, task);

		}

		return "0";
//		this.coordinationProvider.getNamedLock(CoordinationLocks.SYNC_CONTRACT.getCode() + cmd.getNamespaceId() + cmd.getCommunityId()).tryEnter(()-> {
//			ExecutorUtil.submit(new Runnable() {
//				@Override
//				public void run() {
//					try{
//						Community community = communityProvider.findCommunityById(cmd.getCommunityId());
//						if(community == null) {
//							return;
//						}
//						String version = contractProvider.findLastContractVersionByCommunity(cmd.getNamespaceId(), community.getId());
//						ThirdPartContractHandler contractHandler = PlatformContext.getComponent(ThirdPartContractHandler.CONTRACT_PREFIX + cmd.getNamespaceId());
//						if(contractHandler != null) {
//							contractHandler.syncContractsFromThirdPart("1", version, community.getNamespaceCommunityToken());
//						}
//
//					}catch (Exception e){
//						LOGGER.error("syncEnterpriseCustomers error.", e);
//					}
//				}
//			});
//		});
	}

	@Override
	public ContractDetailDTO findContractForApp(FindContractCommand cmd) {
		CheckAdminCommand cmd1 = new CheckAdminCommand();
		cmd1.setNamespaceId(cmd.getNamespaceId());
		cmd1.setOrganizationId(cmd.getOrganizationId());
		return findContract(cmd);
	}

	@Override
	public Boolean checkAdmin(CheckAdminCommand cmd) {
		Long userId = UserContext.currentUserId();
		ListServiceModuleAdministratorsCommand cmd1 = new ListServiceModuleAdministratorsCommand();
		cmd1.setOrganizationId(cmd.getOrganizationId());
		cmd1.setActivationFlag((byte) 1);
		cmd1.setOwnerType("EhOrganizations");
		cmd1.setOwnerId(null);
		LOGGER.info("organization manager check for bill display, cmd = " + cmd1.toString());
		List<OrganizationContactDTO> organizationContactDTOS = rolePrivilegeService.listOrganizationAdministrators(cmd1);
		LOGGER.info("organization manager check for bill display, orgContactsDTOs are = " + organizationContactDTOS.toString());
		LOGGER.info("organization manager check for bill display, userId = " + userId);
		for (OrganizationContactDTO dto : organizationContactDTOS) {
			Long targetId = dto.getTargetId();
			if (targetId.longValue() == userId.longValue()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public DurationParamDTO getDuration(GetDurationParamCommand cmd) {
		Timestamp EndTimeByDayTimestamp = new Timestamp(cmd.getEndTimeByDay());
		DurationParamDTO dto = new DurationParamDTO();
		SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
		List<ContractChargingItem> chargingItems = contractChargingItemProvider.listByContractId(cmd.getContractId());
		if (chargingItems != null && chargingItems.size() > 0) {
			List<ChargingItemVariables> chargingPaymentTypeVariables = new ArrayList<ChargingItemVariables>();
			for (int i = 0; i < chargingItems.size(); i++) {
				//issue-42416,更新自然季，合同刷新账单报错。
				if (chargingItems.get(i).getChargingStartTime() == null || EndTimeByDayTimestamp == null) {
					continue ;
				}
				if ((chargingItems.get(i).getChargingStartTime()).before(EndTimeByDayTimestamp)) {
				// <li>costGenerationMethod: 费用截断方式，0：按计费周期，1：按实际天数</li>
				ChargingItemVariables chargingPaymentTypeVariable = new ChargingItemVariables();
				// 根据合同应用的categoryId去查找对应的缴费应用的categoryId
				Long assetCategoryId = assetProvider.getOriginIdFromMappingApp(21200l, cmd.getCategoryId(), ServiceModuleConstants.ASSET_MODULE);
				String projectChargingItemName = assetProvider.findProjectChargingItemNameByCommunityId(cmd.getCommunityId(), cmd.getNamespaceId(), assetCategoryId,
						chargingItems.get(i).getChargingItemId());

				if (chargingItems.get(i).getChargingStartTime() != null) {
					String endTimeByDay = "";
					String endTimeByPeriod = "";
					chargingPaymentTypeVariable.setChargingItemName(projectChargingItemName);
					chargingPaymentTypeVariable.setStartTime(yyyyMMdd.format(chargingItems.get(i).getChargingStartTime()));
					if (EndTimeByDayTimestamp.after(chargingItems.get(i).getChargingExpiredTime())) {
						endTimeByDay = yyyyMMdd.format(chargingItems.get(i).getChargingExpiredTime());
						endTimeByPeriod = assetProvider.findEndTimeByPeriod(endTimeByDay, cmd.getContractId(), chargingItems.get(i).getChargingItemId());
					} else {
						endTimeByDay = yyyyMMdd.format(EndTimeByDayTimestamp);
						endTimeByPeriod = assetProvider.findEndTimeByPeriod(endTimeByDay, cmd.getContractId(), chargingItems.get(i).getChargingItemId());
					}
					chargingPaymentTypeVariable.setEndTimeByDay(endTimeByDay);
					chargingPaymentTypeVariable.setEndTimeByPeriod(endTimeByPeriod);
					chargingPaymentTypeVariables.add(chargingPaymentTypeVariable);
				}
				dto.setChargingPaymentTypeVariables(chargingPaymentTypeVariables);
				}
			}
		}
		return dto;
	}

	@Override
	public List<ContractEventDTO> listContractEvents(ListContractEventsCommand cmd) {
		List<ContractEvents> contractEventsList = contractProvider.listContractEvents(cmd.getContractId());
		//把操作时间（OpearteTime）相同的日志记录内容（Content）都合起来，传给前端展示
		LinkedHashMap<Timestamp, ContractEventDTO> map = new LinkedHashMap<>();
		for (ContractEvents contractEvents : contractEventsList) {
			if (map.containsKey(contractEvents.getOpearteTime())) {
				ContractEventDTO contractEventDTO = map.get(contractEvents.getOpearteTime());
				String content = contractEventDTO.getContent();
				content = content + ";" +contractEvents.getContent();
				contractEventDTO.setContent(content);
			}else {
				ContractEventDTO dto = ConvertHelper.convert(contractEvents, ContractEventDTO.class);
		        if(dto.getOperatorUid() != null) {
		            //用户可能不在组织架构中 所以用nickname
		            User user = userProvider.findUserById(dto.getOperatorUid());
		            if(user != null) {
		                dto.setOperatorName(user.getNickName());
		            }
		        }
				map.put(dto.getOpearteTime(), dto);
			}
		}
		List<ContractEventDTO> result = new ArrayList<>();
		Set<Entry<Timestamp, ContractEventDTO>> entrySet = map.entrySet();
		for (Entry<Timestamp, ContractEventDTO> entry : entrySet) {
			result.add(entry.getValue());
		}
		return result;
	}

    public Long findContractCategoryIdByContractId(Long contractId) {
		return contractProvider.findContractCategoryIdByContractId(contractId);
    }

	@Override
	public void exportContractListByContractList(SearchContractCommand cmd) {
		Map<String, Object> params = new HashMap<>();
		params.put("namespaceId", cmd.getNamespaceId());
		params.put("communityId", cmd.getCommunityId());
		params.put("categoryId", cmd.getCategoryId());
		params.put("task_Id", cmd.getTaskId());
		String fileName = String.format(localeStringService.getLocalizedString("contract.export","1",UserContext.current().getUser().getLocale(),"") +  com.everhomes.sms.DateUtil.dateToStr(new Date(), com.everhomes.sms.DateUtil.NO_SLASH)) + ".xlsx";

		taskService.createTask(fileName, TaskType.FILEDOWNLOAD.getCode(), ContractExportHandler.class, params, TaskRepeatFlag.REPEAT.getCode(), new java.util.Date());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public OutputStream exportOutputStreamContractListByContractList(SearchContractCommand cmd, Long taskId){
		cmd.setPageSize(10000);
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		//动态字段
		List<ContractDTO> contractListDTO = syncDataTaskService.listContractErrorMsg(cmd);

		taskService.updateTaskProcess(taskId, 20);


		ListFieldCommand command = ConvertHelper.convert(cmd, ListFieldCommand.class);
		command.setModuleName("contract");
		command.setGroupPath(null);
		//页面上所有的动态字段
		String[] propertyNamesAll = {"contractNumber","name","contractType","contractStartDate","contractEndDate","customerId","apartments","status","rent","syncErrorMsg"};
		List<FieldDTO> dtos = fieldService.listFields(command);
		// 属性字段
		String[] fieldpropertyNames = new String[dtos.size() + 2];
		for (int i = 0; i < dtos.size(); i++) {
			fieldpropertyNames[i] = dtos.get(i).getFieldName();
		}
		fieldpropertyNames[fieldpropertyNames.length - 2] = "rent";
		fieldpropertyNames[fieldpropertyNames.length - 1] = "syncErrorMsg";

		List propertyNamesListAll = Arrays.asList(propertyNamesAll); // 将数组转化为list
		List fieldpropertyNamesList = Arrays.asList(fieldpropertyNames);

		List list = (List) propertyNamesListAll.stream().filter(a -> fieldpropertyNamesList.contains(a))
				.collect(Collectors.toList());
		String[] ExcelPropertyNames = (String[]) list.toArray(new String[list.size()]); // 转化为数组
		// 标题
		String[] titleNames = new String[ExcelPropertyNames.length];
		for (int i = 0; i < ExcelPropertyNames.length; i++) {
			for (int j = 0; j < dtos.size(); j++) {
				if (ExcelPropertyNames[i].equals(dtos.get(j).getFieldName())) {
					titleNames[i] = dtos.get(j).getFieldDisplayName();
				}
			}
		}
		titleNames[titleNames.length - 2] = "租赁总额";
		titleNames[titleNames.length - 1] = "错误信息";
		int[] titleSizes = new int[ExcelPropertyNames.length + 2];
		for (int i = 0; i < ExcelPropertyNames.length; i++) {
			titleSizes[i] = 30;
		}
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		taskService.updateTaskProcess(taskId, 50);
		if (community == null) {
			LOGGER.error("Community is not exist.");
			throw errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_NOT_EXIST,
					"Community is not exist.");
		}
		if (contractListDTO != null && contractListDTO.size() > 0) {
			String fileName = String.format("合同信息_%s", community.getName(), com.everhomes.sms.DateUtil.dateToStr(new Date(), com.everhomes.sms.DateUtil.NO_SLASH));
			ExcelUtils excelUtils = new ExcelUtils(null, fileName, "合同信息");
			List<ContractExportDetailDTO> data = contractListDTO.stream().map(this::convertToExportDetail)
					.collect(Collectors.toList());
			taskService.updateTaskProcess(taskId, 80);
			return excelUtils.getOutputStream(ExcelPropertyNames, titleNames, titleSizes, data);
		} else {
			throw errorWith(ContractErrorCode.SCOPE, ContractErrorCode.ERROR_NO_DATA, "no data");
		}
	}


	@Override
	public void exportContractListByCommunityCategoryId(SearchContractCommand cmd) {
		Map<String, Object> params = new HashMap<>();
		params.put("UserContext", UserContext.current().getUser());
		params.put("ListCMD", cmd);
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		if (community == null) {
			LOGGER.error("Community is not exist.");
			throw errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_NOT_EXIST, "Community is not exist.");
		}
		String fileName = String.format("合同信息_%s", community.getName(), com.everhomes.sms.DateUtil.dateToStr(new Date(), com.everhomes.sms.DateUtil.NO_SLASH)) + ".xlsx";
		taskService.createTask(fileName, TaskType.FILEDOWNLOAD.getCode(), ContractListExportHandler.class, params, TaskRepeatFlag.REPEAT.getCode(), new java.util.Date());
	}

	//导出合同列表对接下载中心
	@Override
	public OutputStream exportOutputStreamListByTaskId(SearchContractCommand cmd, Long taskId) {
		// 公用字段
		taskService.updateTaskProcess(taskId, 10);
		cmd.setPageSize(100000);
		// 查询合同列表
		ListContractsResponse contractList = contractSearcher.queryContracts(cmd);
		// 获取合同列表
		List<ContractDTO> contractListDTO = contractList.getContracts();
		// 动态字段
		ListFieldCommand command = ConvertHelper.convert(cmd, ListFieldCommand.class);
		command.setModuleName("contract");
		command.setGroupPath(null);
		// 页面上所有的动态字段
		List<FieldDTO> dtos = fieldService.listFields(command);
		taskService.updateTaskProcess(taskId, 30);
		
		//下载列表的所有字段
		String[] propertyNamesAll = { "contractNumber", "name", "contractType", "contractStartDate", "contractEndDate", "customerId", "apartments", "status", "rent", "sponsorName", "deposit" };

		//用户自定义字段
		Map<String,String>  customFields = new HashMap<String,String>();
		customFields.put("rent", "租赁总额");
		customFields.put("sponsorName", "发起人");
		//customFields.put("depositStatus", "押金支付状态");
		//设置自定义字段的长度
		int[] titleSizes = { 20,20};
		
		ExcelPropertyInfo excelPropertyInfo = exportPropertyInfo(customFields, dtos , propertyNamesAll, titleSizes);
				
		Community community = communityProvider.findCommunityById(cmd.getCommunityId());
		if (community == null) {
			LOGGER.error("Community is not exist.");
			throw errorWith(CommunityServiceErrorCode.SCOPE, CommunityServiceErrorCode.ERROR_COMMUNITY_NOT_EXIST, "Community is not exist.");
		}
		taskService.updateTaskProcess(taskId, 70);
		if (contractListDTO != null && contractListDTO.size() > 0) {
			String fileName = String.format("合同信息_%s", community.getName(), com.everhomes.sms.DateUtil.dateToStr(new Date(), com.everhomes.sms.DateUtil.NO_SLASH));
			ExcelUtils excelUtils = new ExcelUtils(null, fileName, "合同信息");

			List<ContractExportDetailDTO> data = contractListDTO.stream().map(this::convertToExportDetail).collect(Collectors.toList());
			taskService.updateTaskProcess(taskId, 90);
			return excelUtils.getOutputStream(excelPropertyInfo.getPropertyNames(), excelPropertyInfo.getTitleName(), excelPropertyInfo.getTitleSize(), data);
		} else {
			throw errorWith(ContractErrorCode.SCOPE, ContractErrorCode.ERROR_NO_DATA, "no data");
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ExcelPropertyInfo exportPropertyInfo(Map<String, String> customFields, List<FieldDTO> dynamicField, String[] exportfield, int[] customFieldtitleSizes) {
		ExcelPropertyInfo excelPropertyInfo = new ExcelPropertyInfo();
		String[] fieldpropertyNames = new String[dynamicField.size() + customFields.size()];

		List customFieldsList = new ArrayList<>();
		Iterator iter = customFields.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			customFieldsList.add(entry.getKey());
		}

		for (int i = 0; i < dynamicField.size(); i++) {
			fieldpropertyNames[i] = dynamicField.get(i).getFieldName();
		}

		for (int i = 0; i < customFieldsList.size(); i++) {
			fieldpropertyNames[dynamicField.size() + i] = (String) customFieldsList.get(i);
		}

		List propertyNamesListAll = Arrays.asList(exportfield); // 将数组转化为list
		List fieldpropertyNamesList = Arrays.asList(fieldpropertyNames);

		List list = (List) propertyNamesListAll.stream().filter(a -> fieldpropertyNamesList.contains(a)).collect(Collectors.toList());
		String[] ExcelPropertyNames = (String[]) list.toArray(new String[list.size()]); // 转化为数组
		// 标题
		String[] titleNames = new String[ExcelPropertyNames.length];
		for (int i = 0; i < titleNames.length; i++) {
			for (int j = 0; j < dynamicField.size(); j++) {
				if (ExcelPropertyNames[i].equals(dynamicField.get(j).getFieldName())) {
					titleNames[i] = dynamicField.get(j).getFieldDisplayName();
				}
			}
			if (titleNames[i] == null) {
				titleNames[i] = customFields.get(ExcelPropertyNames[i]);
			}
		}

		int dynamicFieldsize = ExcelPropertyNames.length - customFields.size();
		int[] titleSizes = new int[ExcelPropertyNames.length];
		for (int i = 0; i < dynamicFieldsize ; i++) {
			titleSizes[i] = 30;
		}
		for (int i = 0; i < customFieldtitleSizes.length; i++) {
			titleSizes[dynamicFieldsize + i] = customFieldtitleSizes[i];
		}

		excelPropertyInfo.setTitleName(titleNames);
		excelPropertyInfo.setPropertyNames(ExcelPropertyNames);
		excelPropertyInfo.setTitleSize(titleSizes);

		return excelPropertyInfo;
	}

	protected ContractExportDetailDTO convertToExportDetail(ContractDTO dto) {
		ContractExportDetailDTO exportDetailDTO = ConvertHelper.convert(dto, ContractExportDetailDTO.class);
		try {
			StringBuffer buildings = new StringBuffer();
			List<ContractBuildingMapping> contractApartments = contractBuildingMappingProvider.listByContract(dto.getId());
	        if(contractApartments != null && contractApartments.size() > 0) {
	            List<BuildingApartmentDTO> apartmentDtos = contractApartments.stream().map(apartment -> {
	                BuildingApartmentDTO apartmentDto = ConvertHelper.convert(apartment, BuildingApartmentDTO.class);
	                return apartmentDto;
	            }).collect(Collectors.toList());
	            dto.setBuildings(apartmentDtos);
	            
	            for (int i = 0; i < apartmentDtos.size(); i++) {
	            	buildings.append(apartmentDtos.get(0).getBuildingName()+"-"+apartmentDtos.get(0).getApartmentName()+",");
				}
	        }
	        exportDetailDTO.setApartments(buildings.toString());
	        exportDetailDTO.setRent(dto.getRent());
	        exportDetailDTO.setCustomerId(dto.getCustomerName());
	        FieldItem ContractType = contractBuildingMappingProvider.listFieldItems("contract", 104L, dto.getContractType());
	        exportDetailDTO.setContractType(ContractType.getDisplayName());
	        FieldItem ContractStatus = contractBuildingMappingProvider.listFieldItems("contract", 131L, dto.getStatus());
	        exportDetailDTO.setStatus(ContractStatus.getDisplayName());
	        
			if (dto.getContractStartDate() != null) {
				exportDetailDTO.setContractStartDate(new SimpleDateFormat("yyyy-MM-dd").format(dto.getContractStartDate()));
			}
			if (dto.getContractEndDate() != null) {
				exportDetailDTO.setContractEndDate(new SimpleDateFormat("yyyy-MM-dd").format(dto.getContractEndDate()));
			}
			if (dto.getDepositStatus() != null) {
				ContractDepositType depositStatus = ContractDepositType.fromCode(dto.getDepositStatus());
				exportDetailDTO.setDepositStatus(depositStatus.getDesc());
			}
			
		} catch (Exception e) {
			LOGGER.error("dto : {}", dto);
			throw e;
		}
		return exportDetailDTO;
	}

	@Override
	public ContractTemplateDTO addContractTemplate(AddContractTemplateCommand cmd) {
		ContractTemplate contractTemplate = ConvertHelper.convert(cmd, ContractTemplate.class);
		contractTemplate.setContentType("gogs");
		ContractTemplate contractTemplateParent = null;
		//是否是新增的文件 默认不是新文件
		Boolean isNewFile = false;
		String lastCommit = "";
		
		//新增的模板需要进行判断，是属于新增的还是复制的，如果存在id则表示是复制模板来的，需要添加parentId,id不为空，通用修改，小区复制模板
		if(cmd.getId() != null) {
			//添加父节点，版本号+1
			contractTemplateParent = contractProvider.findContractTemplateById(cmd.getId());
			contractTemplate.setParentId(cmd.getId());
			if (cmd.getOwnerType() == null || "".equals(cmd.getOwnerType())) {
				//来自通用模板修改
				contractTemplate.setVersion(contractTemplateParent.getVersion()+1);
			}else {
				isNewFile = true;
				contractTemplate.setVersion(0);
				//查询gogs上面的数据
				String moduleType = "ContractTemplate_" + cmd.getCategoryId();
				GogsRepo repo = gogsRepo(contractTemplateParent.getNamespaceId(), moduleType, ServiceModuleConstants.CONTRACT_MODULE, "EhContractTemplate", contractTemplateParent.getOwnerId());
				String contents=gogsGetScript(repo, contractTemplateParent.gogsPath(), contractTemplateParent.getLastCommit());
				contractTemplate.setContents(gogsGetScript(repo, contractTemplateParent.gogsPath(), contractTemplateParent.getLastCommit()));
			}
			if (contractTemplateParent.getLastCommit() == null) {
				isNewFile = true;
			}else {
				lastCommit = contractTemplateParent.getLastCommit();
			}
		}
		//新增通用，新增园区模板
		if(cmd.getId() == null) {
			contractTemplate.setVersion(0);
			contractTemplate.setParentId(0L);
			isNewFile = true;
			if (null != cmd.getOwnerId() && cmd.getOwnerId() != -1 && cmd.getOwnerId() != 0 ) {
				contractTemplate.setOrgId(0L);
			}
		}
		if ("gogs".equals(contractTemplate.getContentType())) {
			//使用gogs存储合同内容
			//1.建仓库 不同应用建立不同仓库
			try {
				String moduleType = "ContractTemplate_" + contractTemplate.getCategoryId();
				GogsRepo repo = gogsRepo(contractTemplate.getNamespaceId(), moduleType, ServiceModuleConstants.CONTRACT_MODULE, "EhContractTemplate", contractTemplate.getOwnerId());
				//2.提交脚本
				GogsCommit commit = gogsCommitScript(repo, contractTemplate.gogsPath(), lastCommit, contractTemplate.getContents(), isNewFile);
				//3.存储提交脚本返回的id
				contractTemplate.setLastCommit(commit.getId());
				contractTemplate.setContents(commit.getId());
				
				contractProvider.createContractTemplate(contractTemplate);
			} catch (GogsConflictException e) {
				LOGGER.error("contractTemplateName {} in namespace {} already exist!", contractTemplate.gogsPath(), cmd.getNamespaceId());
				throw RuntimeErrorException.errorWith(ContractErrorCode.SCOPE, ContractErrorCode.ERROR_CONTRACTTEMPLATENAME_EXIST,
						"contractTemplateName is already exist");
			} catch (GogsNotExistException e) {
				LOGGER.error("contractGogsFileNotExist {} in namespace {} already exist!", contractTemplate.gogsPath(), cmd.getNamespaceId());
				throw RuntimeErrorException.errorWith(ContractErrorCode.SCOPE, ContractErrorCode.ERROR_CONTRACTGOGSFILENOTEXIST_NOTEXIST,
						"contractGogsFileNotExist is already exist");
			} catch (Exception e){
				LOGGER.error("Gogs OthersException .", e);
			}
		}
		
		return ConvertHelper.convert(contractTemplate, ContractTemplateDTO.class);
	}

	@Override
	public ContractTemplateDTO updateContractTemplate(UpdateContractTemplateCommand cmd) {
		if (null == cmd.getId()) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid id parameter in the command");
		}
		ContractTemplate contractTemplate = ConvertHelper.convert(cmd, ContractTemplate.class);
		contractTemplate.setContentType("gogs");
		
		//是否是新增的文件 默认不是新文件
		Boolean isNewFile = false;
		String lastCommit = "";
		
		//根据id查询数据，
		ContractTemplate contractTemplateParent = contractProvider.findContractTemplateById(cmd.getId());
		
		String oldPath = contractTemplateParent.gogsPath();
        String oldCommit = contractTemplateParent.getLastCommit();
		
		if (cmd.getName() != null) {
			contractTemplateParent.setName(cmd.getName());
		}
		if (cmd.getContents() != null) {
			contractTemplateParent.setContents(cmd.getContents());
		}
		contractTemplateParent.setParentId(cmd.getId());
		contractTemplateParent.setVersion(contractTemplateParent.getVersion()+1);
		
		if (contractTemplateParent.getLastCommit() == null) {
			isNewFile = true;
		}else {
			if (isNewFile = !Objects.equals(oldPath, contractTemplateParent.gogsPath())) {
				isNewFile = true;
			}else {
				lastCommit = contractTemplateParent.getLastCommit();
			}
		}
		
		if ("gogs".equals(contractTemplate.getContentType())) {
			//使用gogs存储合同内容
			try {
				//1.建仓库
				String moduleType = "ContractTemplate_" + contractTemplate.getCategoryId();
				GogsRepo repo = gogsRepo(contractTemplateParent.getNamespaceId(), moduleType, ServiceModuleConstants.CONTRACT_MODULE, "EhContractTemplate", contractTemplateParent.getOwnerId());
				//2.提交脚本
				GogsCommit commit = gogsCommitScript(repo, contractTemplateParent.gogsPath(), lastCommit, contractTemplateParent.getContents(), isNewFile);
				//3.存储提交脚本返回的id
				contractTemplateParent.setLastCommit(commit.getId());
				contractTemplateParent.setContents(commit.getId());
				
				// 目前来说，如果改了名称，在版本仓库里必须删掉原来的，在创建新的
				if (!Objects.equals(oldPath, contractTemplateParent.gogsPath())) {
				    gogsDeleteScript(repo, oldPath, oldCommit);
				}
				contractProvider.createContractTemplate(contractTemplateParent);
				
			} catch (GogsConflictException e) {
				LOGGER.error("contractTemplateName {} in namespace {} already exist!", contractTemplate.gogsPath(), cmd.getNamespaceId());
				throw RuntimeErrorException.errorWith(ContractErrorCode.SCOPE, ContractErrorCode.ERROR_CONTRACTTEMPLATENAME_EXIST,
						"contractTemplateName is already exist");
			} catch (GogsNotExistException e) {
				LOGGER.error("contractGogsFileNotExist {} in namespace {} already exist!", contractTemplate.gogsPath(), cmd.getNamespaceId());
				throw RuntimeErrorException.errorWith(ContractErrorCode.SCOPE, ContractErrorCode.ERROR_CONTRACTGOGSFILENOTEXIST_NOTEXIST,
						"contractGogsFileNotExist is already exist");
			} catch (Exception e){
				LOGGER.error("Gogs OthersException .", e);
			}
		}
		
		//contractProvider.updateContractTemplate(contractTemplateParent);
		return ConvertHelper.convert(contractTemplateParent, ContractTemplateDTO.class);
	}

	@Override
	public ListContractTemplatesResponse searchContractTemplates(listContractTemplateCommand cmd) {
		// this module request param ownerId means communityId
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		int namespaceId =UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		
		List<ContractTemplate> list = contractProvider.listContractTemplates(cmd.getNamespaceId(), cmd.getOwnerId(), cmd.getOwnerType(),cmd.getOrgId(),
				cmd.getCategoryId(), cmd.getName(), cmd.getPageAnchor(), pageSize, cmd.getAppId());
		
		ListContractTemplatesResponse response = new ListContractTemplatesResponse();

		if(list.size() > 0){
			List<ContractTemplateDTO> resultList = list.stream().map((c) -> {
				ContractTemplateDTO dto = ConvertHelper.convert(c, ContractTemplateDTO.class);
				if (dto.getCreatorUid() != null) {
					//用户可能不在组织架构中 所以用nickname
					User user = userProvider.findUserById(dto.getCreatorUid());
					if(user != null) {
						dto.setCreatorName(user.getNickName());
					}
				}
				if (!dto.getOwnerId().equals(0L)) {
					Community community = communityProvider.findCommunityById(dto.getOwnerId());
					if(null == community){
						LOGGER.debug("community is null...");
					}else{
						dto.setTemplateOwner(community.getName());
					}
				}else {
					dto.setTemplateOwner("通用配置");
				}
				if (dto.getCreateTime() != null) {
					dto.setCreateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dto.getCreateTime()));
				}
				//查询模板是否已被绑定 园区
				if (null != cmd.getOwnerId() && cmd.getOwnerId() != -1) {
					//园区获得模板 通用模板都不能删除 
					if (!dto.getOwnerId().equals(0L)) {
						//获取是否关联合同
						Boolean deleteFlag = contractProvider.getContractTemplateById(dto.getId());
						if (deleteFlag) {
							dto.setDeleteFlag(ContractTemplateDeleteStatus.CITED.getCode());
							
						}else {
							dto.setDeleteFlag(ContractTemplateDeleteStatus.DELETED.getCode());
						}
					}else {
						//园区下通用模板不能删除
						dto.setDeleteFlag(ContractTemplateDeleteStatus.NOCOMPETENCE.getCode());
					}
				}else {
					//全部的时候 只有关联合同的模板不能删除
					Boolean deleteFlag = contractProvider.getContractTemplateById(dto.getId());
					if (deleteFlag) {
						dto.setDeleteFlag(ContractTemplateDeleteStatus.CITED.getCode());
					}else {
						dto.setDeleteFlag(ContractTemplateDeleteStatus.DELETED.getCode());
					}
				}
				//优化性能问题，#33761 【合同管理3.0-beta】【合同管理3.0】测试一段时间，就蹦了，如下图（严重阻挡） 后面测试没有问题可以删除，现在先注释  --by dingjianmin
				dto.setContents("");
				return dto;
			}).collect(Collectors.toList());
    		response.setRequests(resultList);
    		if(list.size() != pageSize){
        		response.setNextPageAnchor(null);
        	}else{
        		response.setNextPageAnchor(list.get(list.size()-1).getId());
        	}
    	}
		return response;
	}

	@Override
	public ContractDTO setPrintContractTemplate(SetPrintContractTemplateCommand cmd) {
		if (cmd.getContractId() == null || cmd.getTemplateId() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid id parameter in the command");
		}
		
		//1更新合同模板映射
		contractProvider.setPrintContractTemplate(cmd.getNamespaceId(), cmd.getContractId(), cmd.getCategoryId(), cmd.getContractNumber(), cmd.getOwnerId(), cmd.getTemplateId());
		//
		Contract contract = contractProvider.findContractById(cmd.getContractId());
		ContractDTO dto = ConvertHelper.convert(contract, ContractDTO.class);
		return dto;
	}

	@Override
	public ContractDTO getContractTemplateDetail(GetContractTemplateDetailCommand cmd) {
		if (cmd.getTemplateId() == null || cmd.getNamespaceId() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid id parameter in the command");
		}
		Contract contract = null;
		ContractDTO dto = null;
		
		if (cmd.getContractId() != null) {
			contract = contractProvider.findContractById(cmd.getContractId());
			dto = ConvertHelper.convert(contract, ContractDTO.class);
		}
		
		if (cmd.getTemplateId() != null) {
			dto = ConvertHelper.convert(cmd, ContractDTO.class);
			ContractTemplate contractTemplateParent = contractProvider.findContractTemplateById(cmd.getTemplateId());
			ContractTemplateDTO contractTemplatedto = ConvertHelper.convert(contractTemplateParent, ContractTemplateDTO.class);
			
			if (contractTemplatedto.getCreatorUid() != null) {
				//用户可能不在组织架构中 所以用nickname
				User user = userProvider.findUserById(contractTemplatedto.getCreatorUid());
				if(user != null) {
					contractTemplatedto.setCreatorName(user.getNickName());
				}
			}
			
			if (!contractTemplatedto.getOwnerId().equals(0L)) {
				Community community = communityProvider.findCommunityById(contractTemplatedto.getOwnerId());
				if(null == community){
					LOGGER.debug("community is null...");
				}else{
					contractTemplatedto.setTemplateOwner(community.getName());
				}
			}else {
				contractTemplatedto.setTemplateOwner("通用配置");
			}
			if (contractTemplatedto.getCreateTime() != null) {
				contractTemplatedto.setCreateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(contractTemplatedto.getCreateTime()));
			}
			
			if ("gogs".equals(contractTemplatedto.getContentType())) {
				try {
					//查询gogs上面的数据
					String moduleType = "ContractTemplate_" + contractTemplatedto.getCategoryId();
					GogsRepo repo = gogsRepo(contractTemplatedto.getNamespaceId(), moduleType, ServiceModuleConstants.CONTRACT_MODULE, "EhContractTemplate", contractTemplatedto.getOwnerId());
					contractTemplatedto.setContents(gogsGetScript(repo, contractTemplatedto.gogsPath(), contractTemplatedto.getLastCommit()));
				} catch (GogsConflictException e) {
					LOGGER.error("contractTemplateName {} in namespace {} already exist!", contractTemplatedto.gogsPath(), cmd.getNamespaceId());
					throw RuntimeErrorException.errorWith(ContractErrorCode.SCOPE, ContractErrorCode.ERROR_CONTRACTTEMPLATENAME_EXIST,
							"contractTemplateName is already exist");
				} catch (GogsNotExistException e) {
					LOGGER.error("contractGogsFileNotExist {} in namespace {} already exist!", contractTemplatedto.gogsPath(), cmd.getNamespaceId());
					throw RuntimeErrorException.errorWith(ContractErrorCode.SCOPE, ContractErrorCode.ERROR_CONTRACTGOGSFILENOTEXIST_NOTEXIST,
							"contractGogsFileNotExist is already exist");
				} catch (Exception e){
					LOGGER.error("Gogs OthersException .", e);
				}
			}
			
			dto.setContractTemplate(contractTemplatedto);
		}
		
		return dto;
	}

	@Override
	public void deleteContractTemplate(DeleteContractTemplateCommand cmd) {
		//checkContractAuth(cmd.getNamespaceId(), PrivilegeConstants.CONTRACT_DENUNCIATION, cmd.getOrgId(), cmd.getCommunityId());
		if (cmd.getId() == null || cmd.getNamespaceId() == null || cmd.getCategoryId() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid id parameter in the command");
		}

		ContractTemplate contractTemplateParent = contractProvider.findContractTemplateById(cmd.getId());
		contractTemplateParent.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		contractTemplateParent.setDeleteUid(UserContext.currentUserId());
		contractTemplateParent.setStatus(ContractTemplateStatus.INACTIVE.getCode()); //无效的状态
		
		contractProvider.updateContractTemplate(contractTemplateParent);
		
		//gogs上面的也需要删除相应的合同模板数据 
		String oldPath = contractTemplateParent.gogsPath();
        String oldCommit = contractTemplateParent.getLastCommit();
        try {
			//1.建仓库
			String moduleType = "ContractTemplate_" + cmd.getCategoryId();
			GogsRepo repo = gogsRepo(contractTemplateParent.getNamespaceId(), moduleType, ServiceModuleConstants.CONTRACT_MODULE, "EhContractTemplate", contractTemplateParent.getOwnerId());
			gogsDeleteScript(repo, oldPath, oldCommit);
        } catch (GogsConflictException e) {
			LOGGER.error("contractTemplateName {} in namespace {} already exist!", oldPath, cmd.getNamespaceId());
			throw RuntimeErrorException.errorWith(ContractErrorCode.SCOPE, ContractErrorCode.ERROR_CONTRACTTEMPLATENAME_EXIST,
					"contractTemplateName is already exist");
		} catch (GogsNotExistException e) {
			LOGGER.error("contractGogsFileNotExist {} in namespace {} already exist!", oldPath, cmd.getNamespaceId());
			throw RuntimeErrorException.errorWith(ContractErrorCode.SCOPE, ContractErrorCode.ERROR_CONTRACTGOGSFILENOTEXIST_NOTEXIST,
					"contractGogsFileNotExist is already exist");
		} catch (Exception e){
			LOGGER.error("Gogs OthersException .", e);
		}
		
	}

	@Override
	public List<Long> checkPrintPreviewprivilege(PrintPreviewPrivilegeCommand cmd) {
		List<Long> functionIds = new ArrayList<>();
		try {
			// 打印
			Boolean print = userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), cmd.getOrgId(),
					PrivilegeConstants.CONTRACT_PRINT, ServiceModuleConstants.CONTRACT_MODULE,
					ActionType.OFFICIAL_URL.getCode(), null, null, cmd.getCommunityId());
			if (print) {
				functionIds.add(PrivilegeConstants.CONTRACT_PRINT);
			}
		} catch (Exception e) {
			// TODO: 只用作判断权限用，暂不处理
		}
		try {
			// 打印预览
			Boolean preview = userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), cmd.getOrgId(),
					PrivilegeConstants.CONTRACT_PREVIEW, ServiceModuleConstants.CONTRACT_MODULE,
					ActionType.OFFICIAL_URL.getCode(), null, null, cmd.getCommunityId());
			if (preview) {
				functionIds.add(PrivilegeConstants.CONTRACT_PREVIEW);
			}
		} catch (Exception e) {
			// TODO: 只用作判断权限用，暂不处理
		}
		return functionIds;
	}

    protected GogsRepo gogsRepo(Integer namespaceId, String moduleType, Long moduleId, String ownerType, Long ownerId) {
        GogsRepo repo = gogsService.getAnyRepo(namespaceId, moduleType, moduleId, ownerType, ownerId);
        if (repo == null) {
            repo = new GogsRepo();
            repo.setName("contractTemplate");
            repo.setNamespaceId(namespaceId);
            repo.setModuleType(moduleType);
            repo.setModuleId(moduleId);
            repo.setOwnerType(ownerType);
            repo.setOwnerId(ownerId);
            repo.setRepoType(GogsRepoType.NORMAL.name());
            repo = gogsService.createRepo(repo);
        }
        return repo;
    }
	
    protected GogsCommit gogsCommitScript(GogsRepo repo, String path, String lastCommit, String content, boolean isNewFile) {
        GogsRawFileParam param = new GogsRawFileParam();
        param.setCommitMessage(gogsCommitMessage());
        param.setNewFile(isNewFile);
        param.setContent(content);
        param.setLastCommit(lastCommit);
        return gogsService.commitFile(repo, path, param);  
    }

    protected GogsCommit gogsDeleteScript(GogsRepo repo, String path, String lastCommit) {
        GogsRawFileParam param = new GogsRawFileParam();
        param.setCommitMessage(gogsCommitMessage());
        param.setLastCommit(lastCommit);
        return gogsService.deleteFile(repo, path, param);
    }

    protected String gogsGetScript(GogsRepo repo, String path, String lastCommit) {
        byte[] file = gogsService.getFile(repo, path, lastCommit);
        return new String(file, Charset.forName("UTF-8"));
    }

    protected String gogsCommitMessage() {
        UserInfo userInfo = userService.getUserSnapshotInfoWithPhone(UserContext.currentUserId());
        return String.format(
                "Author: %s\n UID: %s\n Identifier: %s", userInfo.getNickName(), userInfo.getId(), userInfo.getPhones());
    }

    @Override
	public Byte filterAptitudeCustomer(FilterAptitudeCustomerCommand cmd){
		Byte aptitudeFlag = 0;
		aptitudeFlag = contractProvider.filterAptitudeCustomer(cmd.getOwnerId(),cmd.getNamespaceId());
		return aptitudeFlag;
	}

	@Override
	public AptitudeCustomerFlagDTO updateAptitudeCustomer(UpdateContractAptitudeFlagCommand cmd){
		if(cmd.getAptitudeFlag() != null && cmd.getNamespaceId() != null && cmd.getOwnerId() != null) {
			EnterpriseCustomerAptitudeFlag flag = contractProvider.updateAptitudeCustomer(cmd.getOwnerId(), cmd.getNamespaceId(), cmd.getAptitudeFlag());
			return ConvertHelper.convert(flag, AptitudeCustomerFlagDTO.class);
		}else{
			LOGGER.error("the namespaceId or ownerId or flag is null ");
			throw RuntimeErrorException.errorWith(ContractErrorCode.SCOPE, ContractErrorCode.ERROR_ORGIDORCOMMUNITYID_IS_EMPTY,
					"the namespaceId or ownerId or flag is null ");
		}

	}

	@Override
	public EnterpriseContractDTO EnterpriseContractDetail(EnterpriseContractCommand cmd) {
		EnterpriseContractDTO dto = ConvertHelper.convert(cmd, EnterpriseContractDTO.class);
		FindContractCommand cmdFC = new FindContractCommand();
		cmdFC.setId(cmd.getContractId());
		cmdFC.setNamespaceId(cmd.getNamespaceId());
		cmdFC.setCommunityId(cmd.getCategoryId());
		cmdFC.setCategoryId(cmd.getCategoryId());

		ContractDetailDTO contractDetailDTO = findContract(cmdFC);

		List<FlowCaseEntity> entities = new ArrayList<>();
		FlowCaseEntity e;

		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
		e.setKey("合同编号");
		e.setValue(contractDetailDTO.getContractNumber());
		entities.add(e);

		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.LIST.getCode());
		e.setKey("合同名称");
		e.setValue(contractDetailDTO.getName());
		entities.add(e);

		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.LIST.getCode());
		e.setKey("客户名称");
		e.setValue(contractDetailDTO.getCustomerName());
		entities.add(e);

		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.LIST.getCode());
		e.setKey("合同开始时间");
		e.setValue(timeToStr(contractDetailDTO.getContractStartDate()));
		entities.add(e);

		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.LIST.getCode());
		e.setKey("合同结束时间");
		e.setValue(timeToStr(contractDetailDTO.getContractEndDate()));
		entities.add(e);

		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.LIST.getCode());
		e.setKey("经办人");
		e.setValue(contractDetailDTO.getCreatorName());
		entities.add(e);

		if (contractDetailDTO.getSignedTime() != null) {
			e = new FlowCaseEntity();
			e.setEntityType(FlowCaseEntityType.LIST.getCode());
			e.setKey("签约日期");
			e.setValue(timeToStr(contractDetailDTO.getSignedTime()));
			entities.add(e);
		}

		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.LIST.getCode());
		e.setKey("合同资产");
		StringBuffer apartments = new StringBuffer();

		for (BuildingApartmentDTO apartment : contractDetailDTO.getApartments()) {
			apartments.append(apartment.getBuildingName() + "-" + apartment.getApartmentName()/* + "  面积："
					+ apartment.getChargeArea()*/ + "，");
		}
		e.setValue((apartments.toString()).substring(0, (apartments.toString()).length()-1));
		entities.add(e);

		if (contractDetailDTO.getChargingItems() != null) {
			for (int i = 0; i < (contractDetailDTO.getChargingItems()).size(); i++) {
	
				List<FlowCaseEntity> chargingItemEntities = new ArrayList<>();
				FlowCaseEntity chargingItemeE;
	
				e = new FlowCaseEntity();
				e.setEntityType(FlowCaseEntityType.CONTRACT_PRICE.getCode());
				e.setKey("计价条款");
	
				// 计价条款json 转对象
				if (contractDetailDTO.getChargingItems().get(i).getChargingVariables() != null) {
					contractDetailDTO.getChargingItems().get(i).getChargingVariables();
					chargingItemeE = new FlowCaseEntity();
					chargingItemeE.setEntityType(FlowCaseEntityType.LIST.getCode());
					chargingItemeE.setKey("收费项目");
					chargingItemeE.setValue(contractDetailDTO.getChargingItems().get(i).getChargingItemName());
					chargingItemEntities.add(chargingItemeE);
					Map json = (Map) JSONObject.parse(contractDetailDTO.getChargingItems().get(i).getChargingVariables());
					StringBuffer FormulaVariable = new StringBuffer();
					for (Object map : json.entrySet()) {
						List<Map<String, String>> ChargingVariables = (List<Map<String, String>>) ((Map.Entry) map).getValue();
						for (int j = 0; j < ChargingVariables.size(); j++) {
							FormulaVariable.append( ChargingVariables.get(j).get("variableName") + "："
									+ String.valueOf(ChargingVariables.get(j).get("variableValue")));
						}
					}
					chargingItemeE = new FlowCaseEntity();
					chargingItemeE.setEntityType(FlowCaseEntityType.LIST.getCode());
					chargingItemeE.setKey("计费公式");
					chargingItemeE.setValue(contractDetailDTO.getChargingItems().get(i).getFormula() +"("+ FormulaVariable+")");
					chargingItemEntities.add(chargingItemeE);
	
					if (contractDetailDTO.getChargingItems().get(i).getChargingStartTime() != null) {
						chargingItemeE = new FlowCaseEntity();
						chargingItemeE.setEntityType(FlowCaseEntityType.LIST.getCode());
						chargingItemeE.setKey("起计日期");
						chargingItemeE.setValue(timeToStr2(contractDetailDTO.getChargingItems().get(i).getChargingStartTime()));
						chargingItemEntities.add(chargingItemeE);
	
						chargingItemeE = new FlowCaseEntity();
						chargingItemeE.setEntityType(FlowCaseEntityType.LIST.getCode());
						chargingItemeE.setKey("截止日期");
						chargingItemeE.setValue(timeToStr2(contractDetailDTO.getChargingItems().get(i).getChargingExpiredTime()));
						chargingItemEntities.add(chargingItemeE);
					}
					// 添加应用的资产
					StringBuffer apartmentVariable = new StringBuffer();
					contractDetailDTO.getChargingItems().get(i).getApartments();
					for (BuildingApartmentDTO apartment : contractDetailDTO.getChargingItems().get(i).getApartments()) {
						apartmentVariable.append(apartment.getBuildingName() + "-" + apartment.getApartmentName() + "，");
					}
					chargingItemeE = new FlowCaseEntity();
					chargingItemeE.setEntityType(FlowCaseEntityType.LIST.getCode());
					chargingItemeE.setKey("应用资产");
					chargingItemeE.setValue((apartmentVariable.toString()).substring(0, (apartmentVariable.toString()).length()-1));
					chargingItemEntities.add(chargingItemeE);
				}
				e.setChargingItemEntities(chargingItemEntities);
				entities.add(e);
			}
		}
		dto.setEntities(entities);
		return dto;
	}
	protected String timeToStr(Timestamp time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(time);
    }
    protected String timeToStr2(Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(time);
    }

	@Override
	public void deletePrintContractTemplate(SetPrintContractTemplateCommand cmd) {
		if (cmd.getContractId() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid id parameter in the command");
		}
		//1删除合同模板映射,只有待发起，草稿合同能修改
		Contract contract = checkContract(cmd.getContractId());
		if(!ContractStatus.WAITING_FOR_LAUNCH.equals(ContractStatus.fromStatus(contract.getStatus())) && !ContractStatus.DRAFT.equals(ContractStatus.fromStatus(contract.getStatus()))) {
			LOGGER.error("contract is not approve qualitied! id: {}", cmd.getId());
			throw RuntimeErrorException.errorWith(ContractErrorCode.SCOPE, ContractErrorCode.ERROR_ORGIDORCOMMUNITYID_IS_EMPTY,
					"contract is not approve qualitied!");
		}
		contract.setTemplateId(null);
		contract.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

		contractProvider.updateContract(contract);
		contractSearcher.feedDoc(contract);
	}



	@Override
	public List<ContractCategoryListDTO> getContractCategoryList(ContractCategoryCommand cmd) {
		// 查询应用列表
		List<ContractCategoryListDTO> dtos = new ArrayList<ContractCategoryListDTO>();
		ListServiceModuleAppsCommand lerviceModuleAppsCmd = new ListServiceModuleAppsCommand();
		lerviceModuleAppsCmd.setNamespaceId(cmd.getNamespaceId());
		lerviceModuleAppsCmd.setModuleId(ServiceModuleConstants.CONTRACT_MODULE);
		ListServiceModuleAppsResponse response = portalService.listServiceModuleApps(lerviceModuleAppsCmd);
		List<ServiceModuleAppDTO> serviceModuleApps = response.getServiceModuleApps();
		for(ServiceModuleAppDTO appContractCategory : serviceModuleApps) {
			ContractInstanceConfig map = (ContractInstanceConfig) StringHelper.fromJsonString(appContractCategory.getInstanceConfig(), ContractInstanceConfig.class);
			ContractCategoryListDTO dto = new ContractCategoryListDTO();
			dto.setCategoryId(map.getCategoryId());
			dto.setContractApplicationScene(map.getContractApplicationScene());
			dto.setName(appContractCategory.getName());
			dtos.add(dto);
		}
		return dtos;

		
	}

	@Override
	public void dealBillsGeneratedByDenunciationContract(DenunciationContractBillsCommand cmd) {
		List<Contract> contracts = contractProvider.listContractsByNamespaceIdAndStatus(cmd.getNamespaceId(),ContractStatus.DENUNCIATION.getCode());

		for (Contract contract : contracts) {
			if (contract.getCostGenerationMethod() == null) {
				//costGenerationMethod:账单处理方式，0：按计费周期，1：按实际天数
				contract.setCostGenerationMethod(cmd.getCostGenerationMethod());
				assetService.deleteUnsettledBillsOnContractId(contract.getCostGenerationMethod(),contract.getId(),contract.getDenunciationTime());

				long assetCategoryId = 0l;
				if(contract.getCategoryId() != null){
					assetCategoryId = assetProvider.getOriginIdFromMappingApp(21200l, contract.getCategoryId(), ServiceModuleConstants.ASSET_MODULE);
		        }

				BigDecimal totalAmount = assetProvider.getBillExpectanciesAmountOnContract(contract.getContractNumber(),contract.getId(), assetCategoryId, contract.getNamespaceId());
				contract.setRent(totalAmount);
				contractProvider.updateContract(contract);
			}
		}
	}

	@Override
	public List<ContractDTO> getApartmentRentalContract(ListApartmentContractsCommand cmd) {
		List<ContractDTO> results = new ArrayList<>();
		
		List<Contract> contracts = contractProvider.listContractsByAddressId(cmd.getAddressId());
		
		for(Contract contract : contracts){
			if (contract.getCategoryId() != null) {
				ContractCategory contractCategory = contractProvider.findContractCategoryById(contract.getCategoryId());
				if (contractCategory != null) {
					if (ContractApplicationScene.RENTAL.equals(ContractApplicationScene.fromStatus(contractCategory.getContractApplicationScene()))
						|| ContractApplicationScene.COMPREHENSIVE.equals(ContractApplicationScene.fromStatus(contractCategory.getContractApplicationScene()))) {
						ContractDTO dto = ConvertHelper.convert(contract, ContractDTO.class);
						results.add(dto);
					}
				}
			}
		}
		return results;
	}
	
	@Override
	public void autoGeneratingBill(AutoGeneratingBillCommand cmd) {
		if (cmd.getNamespaceId() == null || "".equals(cmd.getContractIds())) {
			return;
		}
		//分割合同id String
        List<Long> contractIdlist = Arrays.asList(cmd.getContractIds().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());

		for (Long contractId : contractIdlist) {
			try {
				Contract contract = contractProvider.findContractById(contractId);
				if (contract == null) {
					continue;
				}
				// 删除所有的合同账单信息
				assetProvider.deleteContractPaymentByContractId(contractId);
				
				FindContractCommand command = new FindContractCommand();
				command.setId(contractId);
				command.setPartyAId(contract.getPartyAId());
				command.setCommunityId(contract.getCommunityId());
				command.setNamespaceId(contract.getNamespaceId());
				command.setCategoryId(contract.getCategoryId());
				ContractDetailDTO contractDetailDTO = findContract(command);

				// 生成正常合同清单
				ExecutorUtil.submit(new Runnable() {
					@Override
					public void run() {
						generatePaymentExpectancies(contract, contractDetailDTO.getChargingItems(),
								contractDetailDTO.getAdjusts(), contractDetailDTO.getFrees());
						// 判断是否为正常合同，为正常合同进行入场
						if (contract.getStatus() == ContractStatus.ACTIVE.getCode()) {// 由于合同入场的接口有Bug，导致无法更新账单，所以如果是正常合同，那么直接调用代码
							assetService.upodateBillStatusOnContractStatusChange(contract.getId(), AssetPaymentConstants.CONTRACT_SAVE);
						}
					}
				});
			} catch (Exception e) {
				LOGGER.info("autoGeneratingBill is error , contract id " + contractId);
				continue;
			}
		}
	}

	public ContractTaskOperateLog initializationContract(InitializationCommand cmd) {
		// 1.查询合同列表,没有数据不用初始化
		if (cmd.getContractIds() == null || cmd.getCommunityId() == null) {
			return null;
		}
		// 返回创建的tasakId
		ContractTaskOperateLog task = new ContractTaskOperateLog();
		task.setNamespaceId(cmd.getNamespaceId());
		task.setOwnerId(cmd.getCommunityId());
		task.setOwnerType("community");
		task.setName("合同初始化");
		task.setProcess(0);
		task.setOperateType(ContractOperateStatus.INITIALIZATION.getCode());
		contractProvider.createContractOperateTask(task);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("UserContext", UserContext.current().getUser());
		
		// 调用初始化，启动线程
		ExecutorUtil.submit(new Runnable() {
			@Override
			public void run() {
				oneKeyInitializationContract(cmd, task, params);
			}
		});
		return task;
	}

	private void oneKeyInitializationContract(InitializationCommand cmd, ContractTaskOperateLog task, Map<String, Object> params) {
		Date StartTime = new Date();
		ContractTaskOperateLog contractTaskOperateLog = contractProvider.findContractOperateTaskById(task.getId());
		contractTaskOperateLog.setStartTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		
		String userStr = String.valueOf(params.get("UserContext"));
		User user = (User) StringHelper.fromJsonString(userStr, User.class);
		user.setNamespaceId(cmd.getNamespaceId());
		UserContext.setCurrentUser(user);

		List<Long> contractAllIds = cmd.getContractIds();
		// 2.调用缴费接口查询该合同是否出现已缴账单，如果存在不允许初始化，费用清单都要删除？还是只删除正常合同,资产状态需要处理吗？
		CheckContractIsProduceBillCmd checkContractIsProduceBillCmd = new CheckContractIsProduceBillCmd();
		checkContractIsProduceBillCmd.setContractIdList(cmd.getContractIds());
		checkContractIsProduceBillCmd.setNamespaceId(cmd.getNamespaceId());
		checkContractIsProduceBillCmd.setOwnerId(cmd.getCommunityId());
		checkContractIsProduceBillCmd.setOwnerType("community");
		ListCheckContractIsProduceBillResponse ListCheckContractIsProduceBillResponse = assetBillService
				.checkContractIsProduceBill(checkContractIsProduceBillCmd);
		List<CheckContractIsProduceBillDTO> lists = ListCheckContractIsProduceBillResponse.getList();
		contractTaskOperateLog.setProcess(5);
		for (CheckContractIsProduceBillDTO entry : lists) {
			// 合同存在已缴账单
			if (entry.getPaymentStatus() == AssetPaymentBillStatus.PAID.getCode()) {
				contractAllIds.remove(entry.getContractId());
			}
		}
		// 3.把符合条件 的合同状态置为草稿合同，去掉不符合条件的合同
		Map<Long, Contract> contractsMap = contractProvider.listContractsByIds(contractAllIds);
		if (contractsMap.size() < 1) {
			return;
		}
		contractTaskOperateLog.setParams(contractsMap.toString());
		contractTaskOperateLog.setTotalNumber(contractsMap.size());
		contractTaskOperateLog.setProcess(35);

		int processedNumber = 0;
		for (Map.Entry<Long, Contract> entry : contractsMap.entrySet()) {
			Contract contract = entry.getValue();
			contract.setContractType(ContractType.NEW.getCode());
			contract.setStatus(ContractStatus.DRAFT.getCode());
			contract.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			// 查询合同适用场景，物业合同不修改资产状态。
			ContractCategory contractCategory = contractProvider.findContractCategoryById(contract.getCategoryId());
			if ((contractCategory == null && contract.getPaymentFlag() == 1) || !ContractApplicationScene.PROPERTY.equals(ContractApplicationScene.fromStatus(contractCategory.getContractApplicationScene()))) {
				// 修改为资产状态待租
				dealAddressLivingStatus(contract, AddressMappingStatus.FREE.getCode());
			}
			contractProvider.updateContract(contract);
			contractSearcher.feedDoc(contract);
			contractProvider.saveContractEvent(ContractTrackingTemplateCode.CONTRACT_INITIALIZE,contract,null);
			processedNumber = processedNumber + 1;

			contractTaskOperateLog.setProcessedNumber(processedNumber);
			contractTaskOperateLog.setOperatorTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			contractProvider.updateContractOperateTask(contractTaskOperateLog);
		}

		// 4.调用缴费接口删除初始化合同的账单，
		BatchDeleteBillFromContractCmd batchDeleteBillFromContractCmd = new BatchDeleteBillFromContractCmd();
		batchDeleteBillFromContractCmd.setContractIdList(contractAllIds);
		batchDeleteBillFromContractCmd.setNamespaceId(cmd.getNamespaceId());
		batchDeleteBillFromContractCmd.setOwnerId(cmd.getCommunityId());
		batchDeleteBillFromContractCmd.setOwnerType("community");
		ListBatchDeleteBillFromContractResponse listBatchDeleteBillFromContractResponse = assetBillService.batchDeleteBillFromContract(batchDeleteBillFromContractCmd);

		List<BatchDeleteBillFromContractDTO> batchDeleteBilllists = listBatchDeleteBillFromContractResponse.getList();
		StringBuffer deleteErrorContract = new StringBuffer();

		for (BatchDeleteBillFromContractDTO entry : batchDeleteBilllists) {
			// 合同存在已缴账单
			if (entry.getPaymentStatus() == AssetPaymentBillStatus.PAID.getCode()) {
				deleteErrorContract.append(entry.getContractId() + ",");
			}
		}
		if (deleteErrorContract.length() > 1) {
			deleteErrorContract.append("初始化失败！");
		} else {
			deleteErrorContract.append("初始化完成！");
		}
		Date FinishTime = new Date();
		contractTaskOperateLog.setExecuteTime((FinishTime.getTime() - StartTime.getTime()) / 1000);
		contractTaskOperateLog.setProcess(100);
		contractTaskOperateLog.setFinishTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		if (deleteErrorContract != null) {
			contractTaskOperateLog.setErrorDescription(deleteErrorContract.toString());
		}
		contractProvider.updateContractOperateTask(contractTaskOperateLog);
	}

	@Override
	public ContractTaskOperateLog exemptionContract(InitializationCommand cmd) {
		// 1.查询合同列表，没有选择审批合同直接返回
		if (cmd.getContractIds() == null) {
			return null;
		}
		// 返回创建的tasakId
		ContractTaskOperateLog task = new ContractTaskOperateLog();
		task.setNamespaceId(cmd.getNamespaceId());
		task.setOwnerId(cmd.getCommunityId());
		task.setOwnerType("community");
		task.setName("合同免批");
		task.setProcess(0);
		task.setOperateType(ContractOperateStatus.INITIALIZATION.getCode());
		contractProvider.createContractOperateTask(task);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("UserContext", UserContext.current().getUser());
		// 调用初始化，启动线程
		ExecutorUtil.submit(new Runnable() {
			@Override
			public void run() {
				oneKeyExemptionContract(cmd, task, params);
			}
		});
		return task;
	}

	private void oneKeyExemptionContract(InitializationCommand cmd, ContractTaskOperateLog task, Map<String, Object> params) {
		Date StartTime = new Date();
		ContractTaskOperateLog contractTaskOperateLog = contractProvider.findContractOperateTaskById(task.getId());
		contractTaskOperateLog.setStartTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		//异步
		String userStr = String.valueOf(params.get("UserContext"));
		User user = (User) StringHelper.fromJsonString(userStr, User.class);
		user.setNamespaceId(cmd.getNamespaceId());
		UserContext.setCurrentUser(user);

		// 3.把符合条件 的合同状态置为审批通过，修改资产状态，
		Map<Long, Contract> contractsMap = contractProvider.listContractsByIds(cmd.getContractIds());
		if (contractsMap.size() < 1) {
			return;
		}
		contractTaskOperateLog.setParams(contractsMap.toString());
		contractTaskOperateLog.setTotalNumber(contractsMap.size());
		contractTaskOperateLog.setProcess(35);
		// 用于记录审批失败的合同
		StringBuffer noChargingItemsContract = new StringBuffer();
		StringBuffer noApartmentsContract = new StringBuffer();
		StringBuffer apartmentsNoRent = new StringBuffer();
		String noChargingItemsContracts = "";
		String noApartmentsContracts = "";
		String apartmentsNoRents = "";
		int processedNumber = 0;
		for (Map.Entry<Long, Contract> entry : contractsMap.entrySet()) {
			Long contractId = entry.getKey();
			Contract contract = entry.getValue();

			contract.setStatus(ContractStatus.APPROVE_QUALITIED.getCode());
			contract.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			// 判断是否可以入场
			// 1、绑定房源（同一时间段只能签订一个合同），只要存在一个不满足条件的就审批不通过，2、绑定计价条款

			FindContractCommand command = new FindContractCommand();
			command.setId(contractId);
			command.setPartyAId(contract.getPartyAId());
			command.setCommunityId(contract.getCommunityId());
			command.setNamespaceId(contract.getNamespaceId());
			command.setCategoryId(contract.getCategoryId());
			ContractDetailDTO contractDetailDTO = findContract(command);

			if (contractDetailDTO.getApartments() == null) {
				// 合同没有绑定房源
				noApartmentsContract.append(contractDetailDTO.getContractNumber() + ",");
				break;
			} else {
				if (contractDetailDTO.getChargingItems() == null) {
					// 合同没有绑定计价条款
					noChargingItemsContract.append(contractDetailDTO.getContractNumber() + ",");
					break;
				}

				// 物业合同不用检查房源状态 不改变房源状态
				ContractCategory contractCategory = contractProvider.findContractCategoryById(contract.getCategoryId());
				if ((contractCategory == null && contract.getPaymentFlag() == 1) || !ContractApplicationScene.PROPERTY.equals(ContractApplicationScene.fromStatus(contractCategory.getContractApplicationScene()))) {
					// 校验房源是否可以入场
					Boolean possibleEnterContractStatus = possibleEnterContract(contractDetailDTO);

					if (!possibleEnterContractStatus) {
						// 房源状态在这个时间点是不允许签合同的，直接返回该合同，审批不通过，操作下一个合同
						apartmentsNoRent.append(contractDetailDTO.getContractNumber() + ",");
						break;
					}
				}
				
				//生成正常合同清单
				ExecutorUtil.submit(new Runnable() {
					@Override
					public void run() {
						// 先把合同置为审批通过
						contractProvider.updateContract(contract);
						contractSearcher.feedDoc(contract);
						
						generatePaymentExpectancies(contract, contractDetailDTO.getChargingItems(), contractDetailDTO.getAdjusts(), contractDetailDTO.getFrees());
						// 合同入场
						EntryContractCommand entryContractCommand = new EntryContractCommand();
						entryContractCommand.setCategoryId(contract.getCategoryId());
						entryContractCommand.setCommunityId(contract.getCommunityId());
						entryContractCommand.setId(contract.getId());
						entryContractCommand.setNamespaceId(contract.getNamespaceId());
						entryContractCommand.setOrgId(contract.getPartyAId());
						entryContractCommand.setPartyAId(contract.getPartyAId());
						// 增减权限校验，需要传入当前登陆用户
						entryContractCommand.setUser(userStr);

						entryContract(entryContractCommand);
					}
				});

				contractProvider.saveContractEvent(ContractTrackingTemplateCode.CONTRACT_EXEMPTION,contract,null);

				processedNumber = processedNumber + 1;
				contractTaskOperateLog.setProcessedNumber(processedNumber);
				contractTaskOperateLog.setOperatorTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
				contractProvider.updateContractOperateTask(contractTaskOperateLog);
			}
			continue;
		}
		// 最后的提示信息
		if (noApartmentsContract.length() > 0) {
			noApartmentsContracts = (noApartmentsContract.toString()).substring(0,
					(noApartmentsContract.toString()).length() - 1) + "合同没有绑定房源";
		}
		if (noChargingItemsContract.length() > 0) {
			noChargingItemsContracts = (noChargingItemsContract.toString()).substring(0,
					(noChargingItemsContract.toString()).length() - 1) + "合同未关联计价条款，无法转为正常合同";
		}
		if (apartmentsNoRent.length() > 0) {
			apartmentsNoRents = (apartmentsNoRent.toString()).substring(0,
					(apartmentsNoRent.toString()).length() - 1) + "合同关联的房源不是待租状态的房源，该房源不可用";
		}

		Date FinishTime = new Date();
		contractTaskOperateLog.setExecuteTime((FinishTime.getTime() - StartTime.getTime()) / 1000);
		contractTaskOperateLog.setProcess(100);
		contractTaskOperateLog.setFinishTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

		if (!"".equals(noApartmentsContracts) && !"".equals(noChargingItemsContracts)) {
			contractTaskOperateLog.setErrorDescription(noApartmentsContracts + "," + noChargingItemsContracts);
		} else if (!"".equals(noApartmentsContracts)) {
			contractTaskOperateLog.setErrorDescription(noApartmentsContracts);
		} else if (!"".equals(noChargingItemsContracts)) {
			contractTaskOperateLog.setErrorDescription(noChargingItemsContracts);
		} else if(!"".equals(apartmentsNoRents)){
			contractTaskOperateLog.setErrorDescription(apartmentsNoRents);
		} else {
			contractTaskOperateLog.setErrorDescription("一键审批完成！");
		}
		contractProvider.updateContractOperateTask(contractTaskOperateLog);
	}


	private Boolean possibleEnterContract(ContractDetailDTO contractDetailDTO) {
		Boolean possibleEnterContractStatus = false;
		// 1、 循环签合同的房源，查看这些房源是否支持签合同
		for (BuildingApartmentDTO apartment : contractDetailDTO.getApartments()) {
			// 根据房源id查询该房源的状态信息
			// 1、查询该房源是否签过合同，获取合同的签署有效期，如果不再本合同的范围内可以签署合同
			// 查房源签署过的合同
			List<ContractBuildingMapping> contractBuildingMappingList = addressProvider.findContractBuildingMappingByAddressId(apartment.getAddressId());
			for (ContractBuildingMapping contractBuildingMapping : contractBuildingMappingList) {
				//如果房源关联的是物业合同，则不用查询
				Contract contract = contractProvider.findContractById(contractBuildingMapping.getContractId());
				ContractCategory contractCategory = contractProvider.findContractCategoryById(contract.getCategoryId());
				if ((contractCategory == null && contract.getPaymentFlag() == 1) || !ContractApplicationScene.PROPERTY.equals(ContractApplicationScene.fromStatus(contractCategory.getContractApplicationScene()))) {
					// 根据合同id,查询合同的有效时间
					Boolean possibleEnterContractFuture = contractProvider.possibleEnterContractFuture(contractDetailDTO, contractBuildingMapping);
					// 存在了，不能审批通过，
					if (possibleEnterContractFuture) {
						return false;
					} else {
						possibleEnterContractStatus = true;
					}
					
				}
			}
			// 房源预定,房源已经被预定，预定时间在签署合同时间范围内
			Boolean resoucreReservationsFuture = contractProvider.resoucreReservationsFuture(contractDetailDTO, apartment);
			if (resoucreReservationsFuture) {
				return false;
			} else {
				possibleEnterContractStatus = true;
			}
			// 查询当前房源状态,这里暂时这样改只有待租房源才能签合同，后面可以签未来合同，不能这样判断，而是判断时间段的状态
			// issue-43523(签约合同关联房源201，走到待发起状态，手动修改房源状态为已预订、待签约等（非待出租），在域空间一键审批为正常合同)
			CommunityAddressMapping communityAddressMapping = organizationProvider.findOrganizationAddressMappingByAddressId(apartment.getAddressId());
			if (communityAddressMapping.getLivingStatus() != AddressMappingStatus.FREE.getCode()) {
				return false;
			}
		}
		return possibleEnterContractStatus;
	}

	private void dealAddressLivingStatus(Contract contract, byte livingStatus) {
		List<ContractBuildingMapping> mappings = contractBuildingMappingProvider.listByContract(contract.getId());
		mappings.forEach(mapping -> {
			CommunityAddressMapping addressMapping = propertyMgrProvider
					.findAddressMappingByAddressId(mapping.getAddressId());
			if (!AddressMappingStatus.SALED.equals(AddressMappingStatus.fromCode(addressMapping.getLivingStatus()))) {
				addressMapping.setLivingStatus(livingStatus);
				propertyMgrProvider.updateOrganizationAddressMapping(addressMapping);
			}
			List<CustomerEntryInfo> entryInfos = enterpriseCustomerProvider
					.listAddressEntryInfos(mapping.getAddressId());
			entryInfos.forEach(entryInfo -> {
				CustomerEntryInfo customerEntryInfo = enterpriseCustomerProvider
						.findCustomerEntryInfoById(entryInfo.getId());
				customerEntryInfo.setStatus(CommonStatus.INACTIVE.getCode());
				enterpriseCustomerProvider.updateCustomerEntryInfo(customerEntryInfo);

			});
		});
	}

	@Override
	public void copyContract(InitializationCommand cmd) {
		// 校验复制权限
		checkContractAuth(cmd.getNamespaceId(), PrivilegeConstants.CONTRACT_COPY, cmd.getOrgId(), cmd.getCommunityId());
		
		// 1.查询合同列表,支持批量复制
		if (cmd.getContractIds() == null) {
			return;
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("UserContext", UserContext.current().getUser());
		// 调用初始化，启动线程
		ExecutorUtil.submit(new Runnable() {
			@Override
			public void run() {
				oneKeyCopyContract(cmd, params);
			}
		});
	}

	private void oneKeyCopyContract(InitializationCommand cmd, Map<String, Object> params) {
		// 3.把符合条件 的合同状态置为草稿合同，去掉不符合条件的合同
		Map<Long, Contract> contractsMap = contractProvider.listContractsByIds(cmd.getContractIds());
		if (contractsMap.size() < 1) {
			return;
		}
		String userStr = String.valueOf(params.get("UserContext"));
		User user = (User) StringHelper.fromJsonString(userStr, User.class);
		user.setNamespaceId(cmd.getNamespaceId());
		UserContext.setCurrentUser(user);
		
		for (Map.Entry<Long, Contract> entry : contractsMap.entrySet()) {
			Long contractId = entry.getKey();
			Contract contract = entry.getValue();

			GenerateContractNumberCommand generateContractNumber = new GenerateContractNumberCommand();
			generateContractNumber.setNamespaceId(contract.getNamespaceId());
			generateContractNumber.setCommunityId(contract.getCommunityId());
			generateContractNumber.setCategoryId(contract.getCategoryId());
			generateContractNumber.setOrgId(contract.getPartyAId());
			generateContractNumber.setPayorreceiveContractType(contract.getPaymentFlag());
			String contractNumber = generateContractNumber(generateContractNumber);

			checkContractNumberUnique(contract.getNamespaceId(), contractNumber, contract.getCategoryId());
			contract.setName(contract.getName()+"(复制)");
			contract.setContractNumber(contractNumber);
			contract.setContractType(ContractType.NEW.getCode());
			contract.setStatus(ContractStatus.DRAFT.getCode());
			contract.setRent(BigDecimal.ZERO);
			contractProvider.createContract(contract);
			contractSearcher.feedDoc(contract);
			Contract existContract = contractProvider.findContractById(contractId);
			contractProvider.saveContractEvent(ContractTrackingTemplateCode.CONTRACT_COPY,contract,existContract);

			FindContractCommand command = new FindContractCommand();
			command.setId(contractId);
			command.setPartyAId(contract.getPartyAId());
			command.setCommunityId(contract.getCommunityId());
			command.setNamespaceId(contract.getNamespaceId());
			command.setCategoryId(contract.getCategoryId());
			ContractDetailDTO contractDetailDTO = findContract(command);

			List<BuildingApartmentDTO> buildingApartments = new ArrayList<BuildingApartmentDTO>();
			List<ContractChargingItemDTO> contractChargingItems = new ArrayList<ContractChargingItemDTO>();
			List<ContractAttachmentDTO> contractAttachments = new ArrayList<ContractAttachmentDTO>();
			List<ContractChargingChangeDTO> contractChargingChanges = new ArrayList<ContractChargingChangeDTO>();
			List<ContractChargingChangeDTO> frees = new ArrayList<ContractChargingChangeDTO>();
			if (contractDetailDTO.getApartments() != null) {
				for (BuildingApartmentDTO apartment : contractDetailDTO.getApartments()) {
					apartment.setId(null);
					buildingApartments.add(apartment);
				}
			}
			if (contractDetailDTO.getChargingItems() != null) {
				for (ContractChargingItemDTO contractChargingItem : contractDetailDTO.getChargingItems()) {
					contractChargingItem.setId(null);
					contractChargingItems.add(contractChargingItem);
				}
			}
			if (contractDetailDTO.getAttachments() != null) {
				for (ContractAttachmentDTO contractAttachment : contractDetailDTO.getAttachments()) {
					contractAttachment.setId(null);
					contractAttachments.add(contractAttachment);
				}
			}
			if (contractDetailDTO.getAdjusts() != null) {
				for (ContractChargingChangeDTO contractChargingChange : contractDetailDTO.getAdjusts()) {
					contractChargingChange.setId(null);
					contractChargingChanges.add(contractChargingChange);
				}
			}
			if (contractDetailDTO.getFrees() != null) {
				for (ContractChargingChangeDTO free : contractDetailDTO.getFrees()) {
					free.setId(null);
					frees.add(free);
				}
			}

			// 合同拷贝，房源不应该修改状态
			Double totalSize = dealContractApartments(contract, buildingApartments, ContractApplicationScene.PROPERTY.getCode());
			dealContractChargingItems(contract, contractChargingItems);
			dealContractAttachments(contract.getId(), contractAttachments);
			dealContractChargingChanges(contract, contractChargingChanges, frees);
			contract.setRentSize(totalSize);
			contract.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

			contractProvider.updateContract(contract);
			contractSearcher.feedDoc(contract);
		}
	}

	@Override
	public SearchProgressDTO findContractOperateTaskById(SearchProgressCommand cmd) {
		if (cmd.getTaskId() == null) {
			return null;
		}
		SearchProgressDTO dto = new SearchProgressDTO();
		ContractTaskOperateLog contractTaskOperateLog = contractProvider.findContractOperateTaskById(cmd.getTaskId());
		dto = ConvertHelper.convert(contractTaskOperateLog, SearchProgressDTO.class);
		return dto;
	}
	
	//合同模板 生成合同文档
	@Override
	public void generateContractDocuments(GenerateContractDocumentsCommand cmd) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("UserContext", UserContext.current().getUser());
		// 调用初始化，启动线程
		ExecutorUtil.submit(new Runnable() {
			@Override
			public void run() {
				generateContractDocuments(cmd, params);
			}
		});
	}
	
	private void generateContractDocuments(GenerateContractDocumentsCommand cmd, Map<String, Object> params) {
		// 1 、 取模板
		
		// 2、循环模板内容
		String contents = "<p>招商客户</p><p><br/></p><p>客户名称：${<span style=\"background:#B8B8B8\">ZS.客户名称<a style=\"display: none\">@@investmentPromotion.name##</a></span>}&nbsp;&nbsp;${<span style=\"background:#B8B8B8\">ZS.客户级别<a style=\"display: none\">@@investmentPromotion.levelItemId##</a></span>}&nbsp;${<span style=\"background:#B8B8B8\">ZS.客户联系人<a style=\"display: none\">@@investmentPromotion.customerContact##</a></span>}&nbsp;${<span style=\"background:#B8B8B8\">ZS.招商跟进人<a style=\"display: none\">@@investmentPromotion.trackerUid##</a></span>}</p><p><br/></p><p>——————————————————</p><p><br/></p><p>客户信息：</p><p>${<span style=\"background:#B8B8B8\">KH.客户名称<a style=\"display: none\">@@enterpriseCustomer.name##</a></span>}&nbsp;${<span style=\"background:#B8B8B8\">KH.客户类型<a style=\"display: none\">@@enterpriseCustomer.categoryItemId##</a></span>}&nbsp;${<span style=\"background:#B8B8B8\">KH.联系人<a style=\"display: none\">@@enterpriseCustomer.contactName##</a></span>}&nbsp;${<span style=\"background:#B8B8B8\">KH.手机号码<a style=\"display: none\">@@enterpriseCustomer.contactMobile##</a></span>}&nbsp;${<span style=\"background:#B8B8B8\">KH.客户来源<a style=\"display: none\">@@enterpriseCustomer.sourceItemId##</a></span>}&nbsp;${<span style=\"background:#B8B8B8\">KH.经营年限<a style=\"display: none\">@@enterpriseCustomer.bizLife##</a></span>}&nbsp;${<span style=\"background:#B8B8B8\">KH.产品类型<a style=\"display: none\">@@enterpriseCustomer.corpProductCategoryItemId##</a></span>}</p><p><br/></p><p><br/></p><p>————————————————</p><p><br/></p><p>资产信息：</p><p>${<span style=\"background:#B8B8B8\">ZC1.项目名称<a style=\"display: none\">@@apartments.0.communityName##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">ZC1.项目地址<a style=\"display: none\">@@apartments.0.communityAddress##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">ZC1.楼宇名称<a style=\"display: none\">@@apartments.0.buildingName##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">ZC1.楼宇地址<a style=\"display: none\">@@apartments.0.buildingAddress##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">ZC1.房源名称<a style=\"display: none\">@@apartments.0.apartmentName##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">ZC1.建筑面积<a style=\"display: none\">@@apartments.0.areaSize##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">ZC1.收费面积<a style=\"display: none\">@@apartments.0.chargeArea##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">ZC1.公摊面积<a style=\"display: none\">@@apartments.0.sharedArea##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">ZC2.项目名称<a style=\"display: none\">@@apartments.1.communityName##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">ZC2.项目地址<a style=\"display: none\">@@apartments.1.communityAddress##</a></span>}</p><p><br/></p><p><br/></p><p>——————————</p><p>合同信息</p><p>${<span style=\"background:#B8B8B8\">合同编号<a style=\"display: none\">@@contractNumber##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">合同名称<a style=\"display: none\">@@name##</a></span>}</p><p><br/></p><p>${<span style=\"background:#B8B8B8\">付款方式<a style=\"display: none\">@@paidType##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">合同状态<a style=\"display: none\">@@status##</a></span>}</p><p><br/></p><p>————————</p><p><br/></p><p>计价条款：</p><p>1、${<span style=\"background:#B8B8B8\">J1.收费项目<a style=\"display: none\">@@chargingItems.0.chargingItemName##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">J1.应用资产<a style=\"display: none\">@@chargingItems.0.apartments##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">J1.单价含税<a style=\"display: none\">@@chargingItems.0.dj##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">J1.单价不含税<a style=\"display: none\">@@chargingItems.0.djbhs##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">J1.单价含税<a style=\"display: none\">@@chargingItems.0.dj##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">J1.面积<a style=\"display: none\">@@chargingItems.0.mj##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">J1.固定金额含税<a style=\"display: none\">@@chargingItems.0.gdje##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">J1.固定金额不含税含税<a style=\"display: none\">@@chargingItems.0.gdjebhs##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">J1.税率<a style=\"display: none\">@@chargingItems.0.taxRate##</a></span>}</p><p><br/></p><p><br/></p><p><br/></p><p>2、${<span style=\"background:#B8B8B8\">J2.截止日期<a style=\"display: none\">@@chargingItems.1.chargingExpiredTime##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">J2.收费项目<a style=\"display: none\">@@chargingItems.1.chargingItemName##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">J2.单价含税<a style=\"display: none\">@@chargingItems.1.dj##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">J2.单价不含税<a style=\"display: none\">@@chargingItems.1.djbhs##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">J2.面积<a style=\"display: none\">@@chargingItems.1.mj##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">J2.固定金额不含税含税<a style=\"display: none\">@@chargingItems.1.gdjebhs##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">J2.固定金额含税<a style=\"display: none\">@@chargingItems.1.gdje##</a></span>}</p><p><br/></p><p><br/></p><p>调租计划：</p><p>1、${<span style=\"background:#B8B8B8\">T1.调整类型<a style=\"display: none\">@@adjusts.0.changeMethod##</a></span>}&nbsp; &nbsp;${<span style=\"background:#B8B8B8\">T1.调整幅度<a style=\"display: none\">@@adjusts.0.changeRange##</a></span>}&nbsp; &nbsp;${<span style=\"background:#B8B8B8\">T1.应用资产<a style=\"display: none\">@@adjusts.0.apartments##</a></span>}&nbsp;&nbsp;${<span style=\"background:#B8B8B8\">T1.起计日期<a style=\"display: none\">@@adjusts.0.changeStartTime##</a></span>}${<span style=\"background:#B8B8B8\">T1.应用资产<a style=\"display: none\">@@adjusts.0.apartments##</a></span>}</p><p><br/></p><p>免租计划：</p><p>1、${<span style=\"background:#B8B8B8\">M1.收费项目<a style=\"display: none\">@@frees.0.chargingItemName##</a></span>}&nbsp;${<span style=\"background:#B8B8B8\">M1.免租金额<a style=\"display: none\">@@frees.0.changeRange##</a></span>}&nbsp;${<span style=\"background:#B8B8B8\">M1.应用资产<a style=\"display: none\">@@frees.0.apartments##</a></span>}&nbsp;${<span style=\"background:#B8B8B8\">M1.起计日期<a style=\"display: none\">@@frees.0.changeStartTime##</a></span>}&nbsp;${<span style=\"background:#B8B8B8\">M1.截止日期<a style=\"display: none\">@@frees.0.changeExpiredTime##</a></span>}</p><p><br/></p><p><br/></p><p><br/></p><p><br/></p>";
		
		Map<String, String> resultMap = new HashMap<String, String>();

		GetKeywordsUtils utils = new GetKeywordsUtils();
		List<String> results = utils.getKeywordsWithPattern(contents, "${", "}");

		// 查询合同value信息
		Contract contract = contractProvider.findContractById(cmd.getContractId());

		FindContractCommand command = new FindContractCommand();
		command.setId(contract.getId());
		command.setPartyAId(contract.getPartyAId());
		command.setCommunityId(contract.getCommunityId());
		command.setNamespaceId(contract.getNamespaceId());
		command.setCategoryId(contract.getCategoryId());
		ContractDetailDTO contractDetailDTO = findContract(command);

		for (String key : results) {
			System.out.println(key);
			List<String> values = utils.getKeywordsWithoutPattern(key, "@@", "##");

			for (int i = 0; i < values.size(); i++) {

				String[] sArray = values.get(i).split("\\.");

				switch (sArray[0]) {

				// 计价条款
				case "chargingItems":

					String ChargingItemInfoKey = "";
					String ChargingItemInfoValue = "";
					for (int j = 0; j < sArray.length; j++) {
						ChargingItemInfoKey = sArray[sArray.length - 1];
					}

					List<ContractChargingItemDTO> contractChargingItemList = contractDetailDTO.getChargingItems();

					if ((Integer.parseInt(sArray[1])) > (contractChargingItemList.size()) - 1) {
						resultMap.put(key, "");
						continue;
					}

					ContractChargingItemDTO contractChargingItem = contractChargingItemList
							.get(Integer.parseInt(sArray[1]));

					Class chargingItemType = ContractChargingItemDTO.class;

					String chargingVariables = contractChargingItem.getChargingVariables();

					if (chargingVariables.contains("\"variableIdentifier\":\"dj\"")) {// 单价
						ChargingVariables chargingVariableList = (ChargingVariables) StringHelper
								.fromJsonString(chargingVariables, ChargingVariables.class);

					} else if (chargingVariables.contains("\"variableIdentifier\":\"gdje\"")) {// 固定金额
						ChargingVariables chargingVariableList = (ChargingVariables) StringHelper
								.fromJsonString(chargingVariables, ChargingVariables.class);
						if (chargingVariableList != null && chargingVariableList.getChargingVariables() != null) {
							BigDecimal gdje = BigDecimal.ZERO;// 固定金额(含税)
							BigDecimal gdjebhs = BigDecimal.ZERO;// 固定金额(不含税)
							for (ChargingVariable chargingVariable : chargingVariableList.getChargingVariables()) {
								if (chargingVariable.getVariableIdentifier() != null) {
									if (chargingVariable.getVariableIdentifier().equals("gdje")) {
										gdje = BigDecimal
												.valueOf(Double.parseDouble(chargingVariable.getVariableValue() + ""));
									}
								}
							}
						}
					}

					try {
						Field chargingItemNamef = chargingItemType.getDeclaredField(ChargingItemInfoKey);
						chargingItemNamef.setAccessible(true);
						ChargingItemInfoValue = chargingItemNamef.get(contractChargingItem).toString();
					} catch (NoSuchFieldException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SecurityException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IllegalArgumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IllegalAccessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					resultMap.put(key, ChargingItemInfoValue);

					break;

				// 调租
				case "adjusts":

					String AdjustsInfoKey = "";
					String AdjustsInfoValue = "";
					for (int j = 0; j < sArray.length; j++) {
						ChargingItemInfoKey = sArray[sArray.length - 1];
					}

					List<ContractChargingChangeDTO> ajustsList = contractDetailDTO.getAdjusts();

					if ((Integer.parseInt(sArray[1])) > (ajustsList.size()) - 1) {
						resultMap.put(key, "");
						continue;
					}

					ContractChargingChangeDTO ajusts = ajustsList.get(Integer.parseInt(sArray[1]));

					Class ajustsType = ContractChargingChangeDTO.class;

					try {
						Field chargingItemNamef = ajustsType.getDeclaredField(AdjustsInfoKey);
						chargingItemNamef.setAccessible(true);
						AdjustsInfoValue = chargingItemNamef.get(ajusts).toString();
					} catch (NoSuchFieldException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SecurityException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IllegalArgumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IllegalAccessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					resultMap.put(key, AdjustsInfoValue);

					break;

				// 免租
				case "frees":

					String freesInfoKey = "";
					String freesInfoValue = "";
					for (int j = 0; j < sArray.length; j++) {
						freesInfoKey = sArray[sArray.length - 1];
					}

					List<ContractChargingChangeDTO> freesList = contractDetailDTO.getFrees();

					if ((Integer.parseInt(sArray[1])) > (freesList.size()) - 1) {
						resultMap.put(key, "");
						continue;
					}

					ContractChargingChangeDTO frees = freesList.get(Integer.parseInt(sArray[1]));

					Class freesType = ContractChargingChangeDTO.class;

					try {
						Field chargingItemNamef = freesType.getDeclaredField(freesInfoKey);
						chargingItemNamef.setAccessible(true);
						freesInfoValue = chargingItemNamef.get(frees).toString();
					} catch (NoSuchFieldException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SecurityException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IllegalArgumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IllegalAccessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					resultMap.put(key, freesInfoValue);

					break;

				// 资产
				case "apartments":

					String apartmentsInfoKey = "";
					String apartmentsInfoValue = "";
					for (int j = 0; j < sArray.length; j++) {
						apartmentsInfoKey = sArray[sArray.length - 1];
					}

					List<BuildingApartmentDTO> buildingApartmentList = contractDetailDTO.getApartments();

					if ((Integer.parseInt(sArray[1])) > (buildingApartmentList.size()) - 1) {
						resultMap.put(key, "");
						continue;
					}

					//查询模板需要的信息
					BuildingApartmentDTO apartments = buildingApartmentList.get(Integer.parseInt(sArray[1]));
					
					Building building = null;
					Address address = addressProvider.findAddressById(apartments.getAddressId());
					if(address.getBuildingId() != null){
						building = communityProvider.findBuildingById(address.getBuildingId());
					}
					Community comminity = communityProvider.findCommunityById(address.getCommunityId());
					
					ContractTemplateBuildingApartmentDTO contractTemplateBuildingApartment = new ContractTemplateBuildingApartmentDTO();
					contractTemplateBuildingApartment.setChargeArea(apartments.getChargeArea());
					contractTemplateBuildingApartment.setAreaSize(address.getAreaSize());
					contractTemplateBuildingApartment.setSharedArea(address.getSharedArea());
					contractTemplateBuildingApartment.setCommunityName(comminity.getName());
					contractTemplateBuildingApartment.setCommunityAddress(comminity.getAddress());
					contractTemplateBuildingApartment.setBuildingName(building.getName());
					contractTemplateBuildingApartment.setBuildingAddress(building.getAddress());
					contractTemplateBuildingApartment.setApartmentName(apartments.getApartmentName());
					
					Class apartmentsType = ContractTemplateBuildingApartmentDTO.class;

						try {
							Field chargingItemNamef = apartmentsType.getDeclaredField(apartmentsInfoKey);
							chargingItemNamef.setAccessible(true);
							apartmentsInfoValue = chargingItemNamef.get(contractTemplateBuildingApartment).toString();
						} catch (NoSuchFieldException e1) {
							LOGGER.info("apartments, 不存在这样的字段 apartmentsInfoKey = " + apartmentsInfoKey);
							resultMap.put(key, "");
							continue;
						} catch (SecurityException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IllegalArgumentException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IllegalAccessException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					resultMap.put(key, apartmentsInfoValue);
					break;

				// 租客属性
				case "enterpriseCustomer":

					break;

				// 租客属性
				case "investmentPromotion":

					resultMap.put(key, "");

					break;

				// 合同基本信息
				default:

					String InfoKey = "";
					String InfoValue = "";
					for (int j = 0; j < sArray.length; j++) {
						InfoKey = sArray[sArray.length - 1];
					}

					Class type = ContractDetailDTO.class;
					try {
						Field namef = type.getDeclaredField(InfoKey);
						namef.setAccessible(true);
						InfoValue = namef.get(contractDetailDTO).toString();
					} catch (NoSuchFieldException e1) {
						LOGGER.info("contract, 合同不存在这样的字段 InfoKey = " + InfoKey);
						resultMap.put(key, "");
						continue;
					} catch (SecurityException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IllegalArgumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IllegalAccessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					resultMap.put(key, InfoValue);

					break;
				}
			}

			// 客户信息

			// 房源信息

			// 把文件中的key换成value

			System.out.println(values.get(0));
			System.out.println();
		}

		// 3 、获取模板的插入数据信息（key）

		// 4、得到含有key 的map

		// 5、查询key对应的value

		// 6、获得完整Map

		// 7、 循环合同模板把模板里面的key换成value，生成合同文档

		// 8、保存合同文档
		
		// 记录存到模板表里面 存到gogs，
		
	}
	
	//首字母转大写
	public static String toUpperCaseFirstOne(String s){
	  if(Character.isUpperCase(s.charAt(0)))
	    return s;
	  else
	    return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
	}
	
	public static void main(String[] args) {
		String contents = "<p>招商客户</p><p><br/></p><p>客户名称：${<span style=\"background:#B8B8B8\">ZS.客户名称<a style=\"display: none\">@@investmentPromotion.name##</a></span>}&nbsp;&nbsp;${<span style=\"background:#B8B8B8\">ZS.客户级别<a style=\"display: none\">@@investmentPromotion.levelItemId##</a></span>}&nbsp;${<span style=\"background:#B8B8B8\">ZS.客户联系人<a style=\"display: none\">@@investmentPromotion.customerContact##</a></span>}&nbsp;${<span style=\"background:#B8B8B8\">ZS.招商跟进人<a style=\"display: none\">@@investmentPromotion.trackerUid##</a></span>}</p><p><br/></p><p>——————————————————</p><p><br/></p><p>客户信息：</p><p>${<span style=\"background:#B8B8B8\">KH.客户名称<a style=\"display: none\">@@enterpriseCustomer.name##</a></span>}&nbsp;${<span style=\"background:#B8B8B8\">KH.客户类型<a style=\"display: none\">@@enterpriseCustomer.categoryItemId##</a></span>}&nbsp;${<span style=\"background:#B8B8B8\">KH.联系人<a style=\"display: none\">@@enterpriseCustomer.contactName##</a></span>}&nbsp;${<span style=\"background:#B8B8B8\">KH.手机号码<a style=\"display: none\">@@enterpriseCustomer.contactMobile##</a></span>}&nbsp;${<span style=\"background:#B8B8B8\">KH.客户来源<a style=\"display: none\">@@enterpriseCustomer.sourceItemId##</a></span>}&nbsp;${<span style=\"background:#B8B8B8\">KH.经营年限<a style=\"display: none\">@@enterpriseCustomer.bizLife##</a></span>}&nbsp;${<span style=\"background:#B8B8B8\">KH.产品类型<a style=\"display: none\">@@enterpriseCustomer.corpProductCategoryItemId##</a></span>}</p><p><br/></p><p><br/></p><p>————————————————</p><p><br/></p><p>资产信息：</p><p>${<span style=\"background:#B8B8B8\">ZC1.项目名称<a style=\"display: none\">@@apartments.0.communityName##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">ZC1.项目地址<a style=\"display: none\">@@apartments.0.communityAddress##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">ZC1.楼宇名称<a style=\"display: none\">@@apartments.0.buildingName##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">ZC1.楼宇地址<a style=\"display: none\">@@apartments.0.buildingAddress##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">ZC1.房源名称<a style=\"display: none\">@@apartments.0.apartmentName##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">ZC1.建筑面积<a style=\"display: none\">@@apartments.0.areaSize##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">ZC1.收费面积<a style=\"display: none\">@@apartments.0.chargeArea##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">ZC1.公摊面积<a style=\"display: none\">@@apartments.0.sharedArea##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">ZC2.项目名称<a style=\"display: none\">@@apartments.1.communityName##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">ZC2.项目地址<a style=\"display: none\">@@apartments.1.communityAddress##</a></span>}</p><p><br/></p><p><br/></p><p>——————————</p><p>合同信息</p><p>${<span style=\"background:#B8B8B8\">合同编号<a style=\"display: none\">@@contractNumber##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">合同名称<a style=\"display: none\">@@name##</a></span>}</p><p><br/></p><p>${<span style=\"background:#B8B8B8\">付款方式<a style=\"display: none\">@@paidType##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">合同状态<a style=\"display: none\">@@status##</a></span>}</p><p><br/></p><p>————————</p><p><br/></p><p>计价条款：</p><p>1、${<span style=\"background:#B8B8B8\">J1.收费项目<a style=\"display: none\">@@chargingItems.0.chargingItemName##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">J1.应用资产<a style=\"display: none\">@@chargingItems.0.apartments##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">J1.单价含税<a style=\"display: none\">@@chargingItems.0.dj##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">J1.单价不含税<a style=\"display: none\">@@chargingItems.0.djbhs##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">J1.单价含税<a style=\"display: none\">@@chargingItems.0.dj##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">J1.面积<a style=\"display: none\">@@chargingItems.0.mj##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">J1.固定金额含税<a style=\"display: none\">@@chargingItems.0.gdje##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">J1.固定金额不含税含税<a style=\"display: none\">@@chargingItems.0.gdjebhs##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">J1.税率<a style=\"display: none\">@@chargingItems.0.taxRate##</a></span>}</p><p><br/></p><p><br/></p><p><br/></p><p>2、${<span style=\"background:#B8B8B8\">J2.截止日期<a style=\"display: none\">@@chargingItems.1.chargingExpiredTime##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">J2.收费项目<a style=\"display: none\">@@chargingItems.1.chargingItemName##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">J2.单价含税<a style=\"display: none\">@@chargingItems.1.dj##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">J2.单价不含税<a style=\"display: none\">@@chargingItems.1.djbhs##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">J2.面积<a style=\"display: none\">@@chargingItems.1.mj##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">J2.固定金额不含税含税<a style=\"display: none\">@@chargingItems.1.gdjebhs##</a></span>}</p><p>${<span style=\"background:#B8B8B8\">J2.固定金额含税<a style=\"display: none\">@@chargingItems.1.gdje##</a></span>}</p><p><br/></p><p><br/></p><p>调租计划：</p><p>1、${<span style=\"background:#B8B8B8\">T1.调整类型<a style=\"display: none\">@@adjusts.0.changeMethod##</a></span>}&nbsp; &nbsp;${<span style=\"background:#B8B8B8\">T1.调整幅度<a style=\"display: none\">@@adjusts.0.changeRange##</a></span>}&nbsp; &nbsp;${<span style=\"background:#B8B8B8\">T1.应用资产<a style=\"display: none\">@@adjusts.0.apartments##</a></span>}&nbsp;&nbsp;${<span style=\"background:#B8B8B8\">T1.起计日期<a style=\"display: none\">@@adjusts.0.changeStartTime##</a></span>}${<span style=\"background:#B8B8B8\">T1.应用资产<a style=\"display: none\">@@adjusts.0.apartments##</a></span>}</p><p><br/></p><p>免租计划：</p><p>1、${<span style=\"background:#B8B8B8\">M1.收费项目<a style=\"display: none\">@@frees.0.chargingItemName##</a></span>}&nbsp;${<span style=\"background:#B8B8B8\">M1.免租金额<a style=\"display: none\">@@frees.0.changeRange##</a></span>}&nbsp;${<span style=\"background:#B8B8B8\">M1.应用资产<a style=\"display: none\">@@frees.0.apartments##</a></span>}&nbsp;${<span style=\"background:#B8B8B8\">M1.起计日期<a style=\"display: none\">@@frees.0.changeStartTime##</a></span>}&nbsp;${<span style=\"background:#B8B8B8\">M1.截止日期<a style=\"display: none\">@@frees.0.changeExpiredTime##</a></span>}</p><p><br/></p><p><br/></p><p><br/></p><p><br/></p>";
		
		GetKeywordsUtils utils = new GetKeywordsUtils();
		List<String> results = utils.getKeywordsWithPattern(contents, "${", "}");
		
		for (String key : results) {
			System.out.println(key);
			List<String> values = utils.getKeywordsWithoutPattern(key, "@@", "##");
			System.out.println(values.get(0));
			System.out.println();
		}
	}
	
	
}