package com.everhomes.rentalv2;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;  


 


import com.everhomes.configuration.ConfigConstants;
import com.everhomes.order.OrderUtil;
import com.everhomes.rest.order.CommonOrderCommand;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.rentalv2.*;
import com.everhomes.rest.rentalv2.admin.*;
import com.everhomes.rest.rentalv2.admin.AttachmentType;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.user.*;

import net.greghaines.jesque.Job;  

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


import com.alibaba.fastjson.JSON;
import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.app.App;
import com.everhomes.app.AppProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerResource;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseProvider;
import com.everhomes.flow.FlowService;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.naming.NameMapper;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.queue.taskqueue.JesqueClientFactory;
import com.everhomes.queue.taskqueue.WorkerPoolFactory;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowAutoStepDTO;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.flow.FlowReferType;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.pojos.EhRentalv2Cells;
import com.everhomes.server.schema.tables.pojos.EhRentalv2DefaultRules;
import com.everhomes.server.schema.tables.pojos.EhRentalv2Resources;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.SmsProvider;
import com.everhomes.techpark.onlinePay.OnlinePayService;
import com.everhomes.techpark.rental.IncompleteUnsuccessRentalBillAction;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SignatureHelper;
import com.everhomes.util.StringHelper;
import com.everhomes.util.Tuple;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Component
public class Rentalv2ServiceImpl implements Rentalv2Service {
	private static final String downloadDir ="\\download\\";

    private static final Long MILLISECONDGMT=8*3600*1000L;
	// N分钟后取消
	private Long cancelTime = 15 * 60 * 1000L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Rentalv2ServiceImpl.class);

	private ThreadLocal<SimpleDateFormat> timeSF = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return 	new SimpleDateFormat("HH:mm:ss");
		}
	};
	private ThreadLocal<SimpleDateFormat> dateSF = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return 	new SimpleDateFormat("yyyy-MM-dd");
		}
	};
	private ThreadLocal<SimpleDateFormat> datetimeSF = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return 	new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	private String queueName = "rentalService";

	@Autowired
	private RolePrivilegeService rolePrivilegeService;
	@Autowired
	private LocaleTemplateService localeTemplateService;
	@Autowired
	private SmsProvider smsProvider;
	@Autowired
	private MessagingService messagingService;
	@Autowired
	private ContentServerService contentServerService;
    @Autowired
    private WorkerPoolFactory workerPoolFactory;
    @Autowired
    private FlowService flowService;
    @Autowired
    private FlowCaseProvider flowCaseProvider;
	@Autowired
	private SequenceProvider sequenceProvider;
	@Autowired
	private CommunityProvider communityProvider;
	@Autowired
	private DbProvider dbProvider;
	@Autowired
	private OnlinePayService onlinePayService;
	@Autowired
	private OrganizationProvider organizationProvider;
	@Autowired
	private JesqueClientFactory jesqueClientFactory;
	@Autowired
	private CoordinationProvider coordinationProvider; 
	@Autowired
	private ConfigurationProvider configurationProvider;
	@Autowired
	private Rentalv2Provider rentalv2Provider;
	@Autowired
	private ScheduleProvider scheduleProvider;
	@Autowired
	private LocaleStringService localeStringService;
	@Autowired
	private UserProvider userProvider;
	@Autowired
	private AppProvider appProvider;
	@Autowired
	private OrderUtil commonOrderUtil;
	@Autowired
	private UserService userService;

	/**cellList : 当前线程用到的单元格 */
	private static ThreadLocal<List<RentalCell>> cellList = new ThreadLocal<List<RentalCell>>() {
		public List<RentalCell> initialValue() {
			return new ArrayList<>();
		}
	};
	/**seqNum : 计数-申请id用 */
	private static ThreadLocal<Long> seqNum = new ThreadLocal<Long>() {
		public Long initialValue() {
			return 0L;
		}
	};
	/**currentId : 当前id*/
	private static ThreadLocal<Long> currentId = new ThreadLocal<Long>() {

	};
	private ExecutorService executorPool =  Executors.newFixedThreadPool(5);
	private Time convertTime(Long TimeLong) {
		if (null != TimeLong) {
			//从8点开始计算
			return new Time(TimeLong-MILLISECONDGMT);
		}
		return null;
	}

	private Long convertTimeToGMTMillisecond(Time time) {
		if (null != time) {
			//从8点开始计算
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.MILLISECOND, 0);
			return cal.getTimeInMillis()+time.getTime()+MILLISECONDGMT;
		}
		return null;
	}

	@PostConstruct
	public void setup() {
		workerPoolFactory.getWorkerPool().addQueue(queueName);
	}

	private String processFlowURL(Long flowCaseId, String flowUserType, Long moduleId) { 
		return "zl://workflow/detail?flowCaseId="+flowCaseId+"&flowUserType="+flowUserType+"&moduleId="+moduleId  ;
	}
	
	private void checkEnterpriseCommunityIdIsNull(Long enterpriseCommunityId) {
		if (null == enterpriseCommunityId || enterpriseCommunityId.equals(0L)) {
			LOGGER.error("Invalid enterpriseCommunityId   parameter in the command");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid ownerId   parameter in the command");
		}

	}
	
	
	private Object restCall(String api, Object command, Class<?> responseType) {
		String host = this.configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"pay.zuolin.host", "https://pay.zuolin.com");
		return restCall(api, command, responseType, host);
	}
	private Object restCall(String api, Object o, Class<?> responseType,String host) {
		AsyncRestTemplate template = new AsyncRestTemplate();
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
		messageConverters.add(new StringHttpMessageConverter(Charset
				.forName("UTF-8")));
		template.setMessageConverters(messageConverters);
		String[] apis = api.split(" ");
		String method = apis[0];

		String url = host
				+ api.substring(method.length() + 1, api.length()).trim();

		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
		HttpEntity<String> requestEntity = null;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Host", host); 
		headers.add("charset", "UTF-8");

		ListenableFuture<ResponseEntity<String>> future = null;

		if (method.equalsIgnoreCase("POST")) {
			requestEntity = new HttpEntity<>(StringHelper.toJsonString(o),
					headers);
			LOGGER.debug("DEBUG: restCall headers: "+requestEntity.toString());
			future = template.exchange(url, HttpMethod.POST, requestEntity,
					String.class);
		} else {
			Map<String, String> params = new HashMap<String, String>();
			StringHelper.toStringMap("", o, params);
			LOGGER.debug("params is :" + params.toString());

			for (Map.Entry<String, String> entry : params.entrySet()) {
				paramMap.add(entry.getKey().substring(1),
						URLEncoder.encode(entry.getValue()));
			}

			url = UriComponentsBuilder.fromHttpUrl(url).queryParams(paramMap)
					.build().toUriString();
			requestEntity = new HttpEntity<>(null, headers);
			LOGGER.debug("DEBUG: restCall headers: "+requestEntity.toString());
			future = template.exchange(url, HttpMethod.GET, requestEntity,
					String.class);
		}

		ResponseEntity<String> responseEntity = null;
		try {
			responseEntity = future.get();
		} catch (InterruptedException | ExecutionException e) {
			LOGGER.info("restCall error " + e.getMessage());
			return null;
		}

		if (responseEntity != null
				&& responseEntity.getStatusCode() == HttpStatus.OK) {

			// String bodyString = new
			// String(responseEntity.getBody().getBytes("ISO-8859-1"), "UTF-8")
			// ;
			String bodyString = responseEntity.getBody();
			LOGGER.debug(bodyString);
			LOGGER.debug("HEADER" + responseEntity.getHeaders());
//			return bodyString;
			return StringHelper.fromJsonString(bodyString, responseType);

		}

//		LOGGER.info("restCall error " + responseEntity.getStatusCode());
		return null;

	}
	
	@Override
	public void addDefaultRule(AddDefaultRuleAdminCommand cmd){
		//现在根据rentalStartTimeFlag和 rentalEndTimeFlag 标志来判断是否有最多/至少 提前预约时间

		this.dbProvider.execute((TransactionStatus status) -> {
			//default rule
			RentalDefaultRule defaultRule = ConvertHelper.convert(cmd, RentalDefaultRule.class);

			if (null != cmd.getBeginDate()) {
				defaultRule.setBeginDate(new Date(cmd.getBeginDate()));
			}
			if (null != cmd.getEndDate()) {
				defaultRule.setEndDate(new Date(cmd.getEndDate()));
			}

			if(null == defaultRule.getCancelFlag()) {
				defaultRule.setCancelFlag(NormalFlag.NEED.getCode());
			}
			//设置星期 开放时间 如 星期一，星期二
			defaultRule.setOpenWeekday(convertOpenWeekday(cmd.getOpenWeekday()));

			if(cmd.getSiteCounts()==null)
				cmd.setSiteCounts(1.0);
			defaultRule.setResourceCounts(cmd.getSiteCounts());
			defaultRule.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			defaultRule.setCreatorUid(UserContext.current().getUser().getId());
			this.rentalv2Provider.createRentalDefaultRule(defaultRule);

			//time intervals
			if(cmd.getRentalType().equals(RentalType.HOUR.getCode())&& null!=cmd.getTimeIntervals()) {
				setRentalRuleTimeIntervals(EhRentalv2DefaultRules.class.getSimpleName(), defaultRule.getId(), cmd.getTimeIntervals());
			}

			// set half day time intervals
			if(cmd.getRentalType() == RentalType.HALFDAY.getCode() || cmd.getRentalType() == RentalType.THREETIMEADAY.getCode()) {
				setRentalRuleTimeIntervals(RentalTimeIntervalOwnerType.DEFAULT_HALF_DAY.getCode(), defaultRule.getId(), cmd.getTimeIntervals());
			}

			//close dates
			setRentalRuleCloseDates(cmd.getCloseDates(), defaultRule.getId(), EhRentalv2DefaultRules.class.getSimpleName());

			//config attachments
			createRentalConfigAttachment(cmd.getAttachments(), defaultRule.getId(), EhRentalv2DefaultRules.class.getSimpleName());

			return null;
		});
	}

	private String convertOpenWeekday(List<Integer> openWeekdays) {

		if (null == openWeekdays)
			return "0000000";

		int openWorkdayInt = 0;
		String openWorkday;
		//list的数字:1234567代表从星期天到星期六,经过-1作为10的次方放到7位字符串内
		for(Integer weekdayInteger : openWeekdays) {
			openWorkdayInt += Math.pow(10, weekdayInteger - 1);
		}
		openWorkday = String.valueOf(openWorkdayInt);
		for( ;openWorkday.length() < 7; ){
			openWorkday = "0" + openWorkday;
		}
		return openWorkday;
	}

	private List<Integer> resolveOpenWeekday(String openWeekday) {
		List<Integer> result = new ArrayList<>();
		if(null != openWeekday){
			int openWeekInt = Integer.valueOf(openWeekday);
			for(int i=1; i < 8; i++){
				if(openWeekInt % 10 == 1)
					result.add(i);
				openWeekInt = openWeekInt / 10;
			}
		}

		return result;
	}

	@Override
	public QueryDefaultRuleAdminResponse queryDefaultRule(QueryDefaultRuleAdminCommand cmd){

		RentalDefaultRule defaultRule = this.rentalv2Provider
				.getRentalDefaultRule(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getResourceTypeId());
		QueryDefaultRuleAdminResponse response;

		if(null == defaultRule){
			AddDefaultRuleAdminCommand addCmd = new AddDefaultRuleAdminCommand();
	        addCmd.setOwnerType(cmd.getOwnerType());
	        addCmd.setOwnerId(cmd.getOwnerId());
	        addCmd.setResourceTypeId(cmd.getResourceTypeId());
	        addCmd.setExclusiveFlag(NormalFlag.NONEED.getCode());
	        addCmd.setUnit(1.0);
	        addCmd.setAutoAssign(NormalFlag.NONEED.getCode());
	        addCmd.setMultiUnit(NormalFlag.NEED.getCode());
	        addCmd.setNeedPay(NormalFlag.NEED.getCode());
	        addCmd.setMultiTimeInterval(NormalFlag.NEED.getCode());
	        addCmd.setAttachments(new ArrayList<>());
	        AttachmentConfigDTO attachment = new AttachmentConfigDTO();
			attachment.setAttachmentType(AttachmentType.ATTACHMENT.getCode());
			attachment.setMustOptions(NormalFlag.NONEED.getCode());
	        addCmd.getAttachments().add(attachment);
	        addCmd.setRentalType(RentalType.DAY.getCode());

// 			addCmd.setTimeStep(1.0);
//	        cmd.setTimeIntervals(new ArrayList<TimeIntervalDTO>());
	        addCmd.setBeginDate(new java.util.Date().getTime());
	        //当前时间+100天
	        addCmd.setEndDate(new java.util.Date().getTime()+1000*60*60*24*100L);
	        addCmd.setOpenWeekday(new ArrayList<>());
	        addCmd.getOpenWeekday().add(1);
	        addCmd.getOpenWeekday().add(2);
	        addCmd.getOpenWeekday().add(3);
	        addCmd.getOpenWeekday().add(4);
	        addCmd.setCloseDates(null);
	        addCmd.setWorkdayPrice(new BigDecimal(0));
	        addCmd.setWeekendPrice(new BigDecimal(0));
	        addCmd.setSiteCounts(1.0);
	        addCmd.setCancelTime(0L);
	        addCmd.setRefundFlag(NormalFlag.NEED.getCode());
	        addCmd.setRefundRatio(30);

			addCmd.setRentalStartTimeFlag(NormalFlag.NONEED.getCode());
			addCmd.setRentalEndTimeFlag(NormalFlag.NONEED.getCode());
			//默认不开启
			addCmd.setRentalEndTime(0L);
			addCmd.setRentalStartTime(0L);

			addCmd.setApprovingUserWeekendPrice(new BigDecimal(0));
			addCmd.setApprovingUserWorkdayPrice(new BigDecimal(0));
			addCmd.setOrgMemberWeekendPrice(new BigDecimal(0));
			addCmd.setOrgMemberWorkdayPrice(new BigDecimal(0));
			this.addDefaultRule(addCmd);

			response = ConvertHelper.convert(addCmd, QueryDefaultRuleAdminResponse.class);
			return response;
		} 
		response = ConvertHelper.convert(defaultRule, QueryDefaultRuleAdminResponse.class);
		response.setSiteCounts(defaultRule.getResourceCounts());
		if(null != defaultRule.getBeginDate())
			response.setBeginDate(defaultRule.getBeginDate().getTime());
		if(null != defaultRule.getEndDate())
			response.setEndDate(defaultRule.getEndDate().getTime());
		
		response.setOpenWeekday(resolveOpenWeekday(defaultRule.getOpenWeekday()));
		populateRentalRule(response, EhRentalv2DefaultRules.class.getSimpleName(), defaultRule.getId());

		return response;
	}

	private void populateRentalRule(QueryDefaultRuleAdminResponse response, String ownerType, Long ownerId) {

		List<RentalResourceNumber> resourceNumbers = rentalv2Provider.queryRentalResourceNumbersByOwner(ownerType, ownerId);
		if(null != resourceNumbers){
			response.setSiteNumbers(new ArrayList<>());
			for(RentalResourceNumber number: resourceNumbers){
				SiteNumberDTO dto = new SiteNumberDTO();
				dto.setSiteNumber(number.getResourceNumber());
				dto.setSiteNumberGroup(number.getNumberGroup());
				dto.setGroupLockFlag(number.getGroupLockFlag());
				response.getSiteNumbers().add(dto);
			}
		}

		String halfOwnerType = RentalTimeIntervalOwnerType.DEFAULT_HALF_DAY.getCode();
		if (EhRentalv2Resources.class.getSimpleName().equals(ownerType)) {
			halfOwnerType = RentalTimeIntervalOwnerType.RESOURCE_HALF_DAY.getCode();
		}

		List<RentalTimeInterval> halfTimeIntervals = rentalv2Provider.queryRentalTimeIntervalByOwner(halfOwnerType, ownerId);
		if(null != halfTimeIntervals) {
			response.setHalfDayTimeIntervals(halfTimeIntervals.stream().map(h -> ConvertHelper.convert(h, TimeIntervalDTO.class))
			.collect(Collectors.toList()));
		}

		List<RentalTimeInterval> timeIntervals = rentalv2Provider.queryRentalTimeIntervalByOwner(ownerType, ownerId);
		if(null != timeIntervals){
			response.setTimeIntervals(timeIntervals.stream().map(t -> ConvertHelper.convert(t, TimeIntervalDTO.class))
			.collect(Collectors.toList()));
		}
		List<RentalCloseDate> closeDates = rentalv2Provider.queryRentalCloseDateByOwner(ownerType, ownerId);
		if(null != closeDates){
			response.setCloseDates(closeDates.stream().filter(d -> null != d.getCloseDate()).map(c -> c.getCloseDate().getTime())
					.collect(Collectors.toList()));
//			for(RentalCloseDate single:closeDates){
//				try{
//					response.getCloseDates().add(single.getCloseDate().getTime());
//				}catch(java.lang.NullPointerException e){
//					LOGGER.error("why java null point close Date is : ["+single.getCloseDate()+"] response is : "+response.toString());
//				}
//			}
		}
		//set 物资
		List<RentalConfigAttachment> attachments = rentalv2Provider.queryRentalConfigAttachmentByOwner(ownerType, ownerId);
		response.setAttachments(convertAttachments(attachments));

	}

	private List<AttachmentConfigDTO> convertAttachments(List<RentalConfigAttachment> attachments) {
		if(null != attachments){
			return attachments.stream().map(a -> {
				AttachmentConfigDTO rca = ConvertHelper.convert(a, AttachmentConfigDTO.class);

				if (a.getAttachmentType().equals(AttachmentType.GOOD_ITEM.getCode())) {
					List<RentalConfigAttachment> goodItems = rentalv2Provider
							.queryRentalConfigAttachmentByOwner(AttachmentType.GOOD_ITEM.name(), a.getId());

					if (null != goodItems) {
						rca.setGoodItems(goodItems.stream().map(g -> ConvertHelper.convert(g, RentalGoodItem.class))
								.collect(Collectors.toList()));
					}
				}else if (a.getAttachmentType().equals(AttachmentType.RECOMMEND_USER.getCode())) {
					List<RentalConfigAttachment> recommendUsers = rentalv2Provider
							.queryRentalConfigAttachmentByOwner(AttachmentType.RECOMMEND_USER.name(), a.getId());

					if (null != recommendUsers) {
						rca.setRecommendUsers(recommendUsers.stream().map(u -> {
							RentalRecommendUser user = ConvertHelper.convert(u, RentalRecommendUser.class);
							user.setIconUrl(contentServerService.parserUri(u.getIconUri(),
									EntityType.USER.getCode(), User.ROOT_UID));
							return user;
						}).collect(Collectors.toList()));
					}
				}
				return rca;
			}).collect(Collectors.toList());
		}
		return new ArrayList<>();
	}

	@Override
	public QueryDefaultRuleAdminResponse getResourceRule(GetResourceRuleAdminCommand cmd){
		RentalResource resource = this.rentalv2Provider.getRentalSiteById(cmd.getResourceId());
		if (null == resource) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER, "Not found RentalResource   ");
		}

		QueryDefaultRuleAdminResponse response = ConvertHelper.convert(resource, QueryDefaultRuleAdminResponse.class);
		response.setSiteCounts(resource.getResourceCounts());
		response.setBeginDate(resource.getBeginDate().getTime());
		response.setEndDate(resource.getEndDate().getTime());
		response.setOpenWeekday(resolveOpenWeekday(resource.getOpenWeekday()));

		//填充列表信息
		populateRentalRule(response, EhRentalv2Resources.class.getSimpleName(), resource.getId());
		return response;
	}

	/**
	 * 只修改预订规则基本参数
	 * @param cmd
	 */
	@Override
	public void updateDefaultRule(UpdateDefaultRuleAdminCommand cmd) { 
		if(null == cmd.getRefundFlag())
			cmd.setRefundFlag(NormalFlag.NONEED.getCode());
		RentalDefaultRule defaultRule = this.rentalv2Provider.getRentalDefaultRule(cmd.getOwnerType(),cmd.getOwnerId(),cmd.getResourceTypeId());
		if(null==defaultRule){
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_DEFAULT_RULE_NOTFOUND, "didnt have default rule!");
		}
		if(null==cmd.getSiteCounts()) 
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter site counts can not be null");
		if (null == cmd.getRentalEndTimeFlag()) {
			cmd.setRentalEndTimeFlag(NormalFlag.NONEED.getCode());
		}
		if (null == cmd.getRentalStartTimeFlag()) {
			cmd.setRentalStartTimeFlag(NormalFlag.NONEED.getCode());
		}
		this.dbProvider.execute((TransactionStatus status) -> {
			RentalDefaultRule newDefaultRule = ConvertHelper.convert(cmd, RentalDefaultRule.class); 
			if(null==newDefaultRule.getCancelFlag()) {
				newDefaultRule.setCancelFlag(NormalFlag.NEED.getCode());
			}

			if (NormalFlag.NONEED.getCode() == cmd.getRentalEndTimeFlag()) {
				newDefaultRule.setRentalEndTime(0L);
			}
			if (NormalFlag.NONEED.getCode() == cmd.getRentalStartTimeFlag()) {
				newDefaultRule.setRentalStartTime(0L);
			}
			newDefaultRule.setOpenWeekday(defaultRule.getOpenWeekday());
			newDefaultRule.setBeginDate(defaultRule.getBeginDate());
			newDefaultRule.setEndDate(defaultRule.getEndDate());
			newDefaultRule.setId(defaultRule.getId());
			newDefaultRule.setResourceCounts(cmd.getSiteCounts());
			this.rentalv2Provider.updateRentalDefaultRule(newDefaultRule);

			//先删除
			rentalv2Provider.deleteRentalResourceNumbersByOwnerId(EhRentalv2DefaultRules.class.getSimpleName(), defaultRule.getId());
			//set site number
			setRentalRuleSiteNumbers(EhRentalv2DefaultRules.class.getSimpleName(), defaultRule.getId(), cmd.getSiteNumbers());

			// set half day time intervals
			//先删除
			rentalv2Provider.deleteTimeIntervalsByOwnerId(RentalTimeIntervalOwnerType.DEFAULT_HALF_DAY.getCode(), defaultRule.getId());
			setRentalRuleTimeIntervals(RentalTimeIntervalOwnerType.DEFAULT_HALF_DAY.getCode(), defaultRule.getId(), cmd.getHalfDayTimeIntervals());

			//time intervals
			//先删除
			this.rentalv2Provider.deleteTimeIntervalsByOwnerId(EhRentalv2DefaultRules.class.getSimpleName(), defaultRule.getId());
			setRentalRuleTimeIntervals(EhRentalv2DefaultRules.class.getSimpleName(), defaultRule.getId(), cmd.getTimeIntervals());
			return null;
		});
	}

	private void setRentalRuleTimeIntervals(String ownerType, Long ownerId, List<TimeIntervalDTO> timeIntervals) {

		if(null != timeIntervals) {
			timeIntervals.forEach(t -> {
				if (t.getBeginTime() > t.getEndTime()) {
					throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
							RentalServiceErrorCode.ERROR_TIME_STEP, "Invalid parameter");
				}
				RentalTimeInterval timeInterval = ConvertHelper.convert(t, RentalTimeInterval.class);
				timeInterval.setOwnerType(ownerType);
				timeInterval.setOwnerId(ownerId);
				this.rentalv2Provider.createTimeInterval(timeInterval);
			});
		}
	}

	private void setRentalRuleSiteNumbers(String ownerType, Long ownerId, List<SiteNumberDTO> siteNumberDTOS) {

		if(null != siteNumberDTOS) {
			siteNumberDTOS.forEach(s -> {
				RentalResourceNumber resourceNumber = new RentalResourceNumber();
				resourceNumber.setOwnerType(ownerType);
				resourceNumber.setOwnerId(ownerId);
				resourceNumber.setResourceNumber(s.getSiteNumber());
				resourceNumber.setNumberGroup(s.getSiteNumberGroup());
				resourceNumber.setGroupLockFlag(s.getGroupLockFlag());
				this.rentalv2Provider.createRentalResourceNumber(resourceNumber);
			});
		}
	}

	/**
	 * 修改预定规则开放时间
	 * @param cmd
	 */
	@Override
	public void updateDefaultDateRule(UpdateDefaultDateRuleAdminCommand cmd) {

		RentalDefaultRule defaultRule = this.rentalv2Provider.getRentalDefaultRule(cmd.getOwnerType(),cmd.getOwnerId(),cmd.getResourceTypeId());
		if(null==defaultRule){
			throw RuntimeErrorException
					.errorWith(RentalServiceErrorCode.SCOPE,
							RentalServiceErrorCode.ERROR_DEFAULT_RULE_NOTFOUND, "didnt have default rule!");
		}
		this.dbProvider.execute((TransactionStatus status) -> {

			//设置开放时间
			defaultRule.setOpenWeekday(convertOpenWeekday(cmd.getOpenWeekday()));
			defaultRule.setBeginDate(new Date(cmd.getBeginDate()));
			defaultRule.setEndDate(new Date(cmd.getEndDate()));

			//设置关闭日期close dates
			rentalv2Provider.deleteRentalCloseDatesByOwnerId(EhRentalv2DefaultRules.class.getSimpleName(), defaultRule.getId());
			setRentalRuleCloseDates(cmd.getCloseDates(), defaultRule.getId(), EhRentalv2DefaultRules.class.getSimpleName());

			return null;
		});
		this.rentalv2Provider.updateRentalDefaultRule(defaultRule);
	}
	//close dates
	private void setRentalRuleCloseDates(List<Long> closeDates, Long ownerId, String ownerType) {

		if(null != closeDates) {
			closeDates.forEach(c -> {
				RentalCloseDate rcd=new RentalCloseDate();
				rcd.setCloseDate(new Date(c));
				rcd.setOwnerType(ownerType);
				rcd.setOwnerId(ownerId);
				this.rentalv2Provider.createRentalCloseDate(rcd);
			});
		}
	}

	@Override
	public void addItem(AddItemAdminCommand cmd) {
		if (null== cmd.getItemType()) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter item type can not be null");
		}

		RentalItem siteItem = ConvertHelper.convert(cmd,RentalItem.class );
		siteItem.setName(cmd.getItemName());
		siteItem.setRentalResourceId(cmd.getRentalSiteId());
		siteItem.setPrice(cmd.getItemPrice());
		rentalv2Provider.createRentalSiteItem(siteItem);
	}

	@Override
	public FindRentalSiteItemsAndAttachmentsResponse findRentalSiteItems(FindRentalSiteItemsAndAttachmentsCommand cmd) {
		if (null==cmd.getRentalSiteId()) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter site id can not be null");
		}
		if (null==cmd.getRentalSiteRuleIds()) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter rule ids can not be null");
		}

		FindRentalSiteItemsAndAttachmentsResponse response = new FindRentalSiteItemsAndAttachmentsResponse();
		response.setSiteItems(new ArrayList<>());
		List<RentalItem> rsiSiteItems = rentalv2Provider.findRentalSiteItems(cmd.getRentalSiteId());
		if(rsiSiteItems!=null && rsiSiteItems.size()>0)
			for (RentalItem rsi : rsiSiteItems) {
				SiteItemDTO dto = convertItem2DTO(rsi);
				//对于租赁型的要计算当前时段该场所已经租赁的物品（购买型记录的库存不用计算）
				if(rsi.getItemType().equals(RentalItemType.RENTAL.getCode())){
					int maxOrder = 0;
					for (Long siteRuleId : cmd.getRentalSiteRuleIds()) {
						// 对于每一个物品，通过每一个siteRuleID找到它对应的BillIds
						int ruleOrderSum = 0;
						List<RentalResourceOrder> rsbs = rentalv2Provider
								.findRentalSiteBillBySiteRuleId(siteRuleId);
						// 通过每一个billID找已预订的数量
						if (null == rsbs || rsbs.size() == 0) {
							continue;
						}
						for (RentalResourceOrder rsb : rsbs) {
							RentalItemsOrder rib = rentalv2Provider.findRentalItemBill(
									rsb.getRentalOrderId(), rsi.getId());
							if (null == rib || null == rib.getRentalCount()) {
								continue;
							}
							ruleOrderSum += rib.getRentalCount();
						}
						// 获取该物品的最大预订量
						if (ruleOrderSum > maxOrder)
							maxOrder = ruleOrderSum;
					}
					dto.setCounts(dto.getCounts() - maxOrder); 
				} 
				
				response.getSiteItems().add(dto);
			}
		
		List<RentalConfigAttachment> attachments=this.rentalv2Provider.queryRentalConfigAttachmentByOwner(EhRentalv2Resources.class.getSimpleName(),cmd.getRentalSiteId());
		response.setAttachments(convertAttachments(attachments));
//		if(null!=attachments){
//			response.setAttachments(new ArrayList<>());
//			for(RentalConfigAttachment single:attachments){
//				response.getAttachments().add(ConvertHelper.convert(single, AttachmentConfigDTO .class));
//			}
//		}
		
		return response;
	}

	@Override
	public FindRentalSitesCommandResponse findRentalSites(FindRentalSitesCommand cmd) {
		if(null==cmd.getResourceTypeId()||null==cmd.getOwnerId()||null==cmd.getOwnerType())

			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter ResourceTypeId OwnerId OwnerType cant be null");
		FindRentalSitesCommandResponse response = new FindRentalSitesCommandResponse();

		long start = System.currentTimeMillis();
//		if(cmd.getAnchor() == null)
//			cmd.setAnchor(Long.MAX_VALUE);
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getAnchor());
		if(null==cmd.getStatus() || cmd.getStatus().size() == 0){
			cmd.setStatus(new ArrayList<>());
			cmd.getStatus().add(RentalSiteStatus.NORMAL.getCode());
		}
		List<Long> siteIds = new ArrayList<>();
		List<RentalSiteRange> siteOwners = this.rentalv2Provider.findRentalSiteOwnersByOwnerTypeAndId(cmd.getOwnerType(), cmd.getOwnerId());
		if(siteOwners !=null)
			for(RentalSiteRange siteOwner : siteOwners){
				siteIds.add(siteOwner.getRentalResourceId());
			}  
		checkEnterpriseCommunityIdIsNull(cmd.getOwnerId());
		List<RentalResource> rentalSites = rentalv2Provider.findRentalSites(cmd.getResourceTypeId(), cmd.getKeyword(),
				locator, pageSize,cmd.getStatus(),siteIds,cmd.getCommunityId());
		if(null == rentalSites)
			return response;
		 
		Long nextPageAnchor = null;
		if(rentalSites.size() > pageSize) {
			rentalSites.remove(rentalSites.size() - 1);
			nextPageAnchor = rentalSites.get(rentalSites.size() -1).getId();
		}
		response.setNextPageAnchor(nextPageAnchor);
		response.setRentalSites(new ArrayList<>());

		long time1 = System.currentTimeMillis();
		LOGGER.info("Get list time={}", time1 - start);

		SceneTokenDTO sceneTokenDTO = null;
		if (null != cmd.getSceneToken()) {
			User user = UserContext.current().getUser();
			sceneTokenDTO = userService.checkSceneToken(user.getId(), cmd.getSceneToken());
		}

		for (RentalResource rentalSite : rentalSites) {
			RentalSiteDTO rSiteDTO = convertRentalSite2DTO(rentalSite, sceneTokenDTO);
			 
			response.getRentalSites().add(rSiteDTO);
		}

		long time2 = System.currentTimeMillis();
		LOGGER.info("Get list time={}", time2 - time1);

		return response;
	}
	private RentalSiteDTO convertRentalSite2DTO(RentalResource rentalSite, SceneTokenDTO sceneTokenDTO){

		RentalResourceType resourceType = rentalv2Provider.getRentalResourceTypeById(rentalSite.getResourceTypeId());

		long time1 = System.currentTimeMillis();

		proccessCells(rentalSite);

		long time2 = System.currentTimeMillis();
		LOGGER.info("proccessCells time={}", time2 - time1);

		RentalSiteDTO rSiteDTO =ConvertHelper.convert(rentalSite, RentalSiteDTO.class);

		String homeUrl = configurationProvider.getValue(ConfigConstants.HOME_URL, "");
		String detailUrl = configurationProvider.getValue(ConfigConstants.RENTAL_RESOURCE_DETAIL_URL, "");
		detailUrl = String.format(detailUrl, UserContext.getCurrentNamespaceId(), rentalSite.getId());

		rSiteDTO.setDetailUrl(homeUrl + detailUrl);
		rSiteDTO.setResourceTypeId(resourceType.getId());
		rSiteDTO.setPayMode(resourceType.getPayMode());

		if(null!=rentalSite.getDayBeginTime())
			rSiteDTO.setDayBeginTime(convertTimeToGMTMillisecond(rentalSite.getDayBeginTime() ));
		if(null!=rentalSite.getDayEndTime())
			rSiteDTO.setDayEndTime(convertTimeToGMTMillisecond(rentalSite.getDayEndTime()));
		rSiteDTO.setRentalSiteId(rentalSite.getId());
		rSiteDTO.setSiteName(rentalSite.getResourceName());
		//fix bug : charge uid null point 2016-10-9
		if(null != rentalSite.getChargeUid() ){
			User charger = this.userProvider.findUserById(rentalSite.getChargeUid() );
			if(null != charger)
				rSiteDTO.setChargeName(charger.getNickName());
		}
		if(null != rentalSite.getOfflinePayeeUid()){
			OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(rSiteDTO.getOfflinePayeeUid(), rentalSite.getOrganizationId());
			if(null!=member){
				rSiteDTO.setOfflinePayeeName(member.getContactName());
			}
		}
		Community community = this.communityProvider.findCommunityById(rSiteDTO.getCommunityId());
		if(null != community)
			rSiteDTO.setCommunityName(community.getName());
		rSiteDTO.setCreateTime(rentalSite.getCreateTime().getTime()); 
		rSiteDTO.setCoverUrl(this.contentServerService.parserUri(rSiteDTO.getCoverUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId()));
		List<RentalResourcePic> pics = this.rentalv2Provider.findRentalSitePicsByOwnerTypeAndId(EhRentalv2Resources.class.getSimpleName(), rentalSite.getId());
		rSiteDTO.setSitePics(convertRentalSitePicDTOs(pics));

		rSiteDTO.setSiteCounts(rentalSite.getResourceCounts());
		if(rentalSite.getAutoAssign().equals(NormalFlag.NEED.getCode())){
			List<RentalResourceNumber> resourceNumbers = this.rentalv2Provider.queryRentalResourceNumbersByOwner(
					EhRentalv2Resources.class.getSimpleName(),rentalSite.getId());
			if(null!=resourceNumbers){
				rSiteDTO.setSiteNumbers (new ArrayList<>());
				for(RentalResourceNumber number:resourceNumbers){
					rSiteDTO.getSiteNumbers().add( number.getResourceNumber());
				}
			}
		}
		//范围
		List<RentalSiteRange> owners = this.rentalv2Provider.findRentalSiteOwnersBySiteId(rentalSite.getId());
		if(null!=owners){
			rSiteDTO.setOwners(new ArrayList<>());
			for(RentalSiteRange owner : owners){
				SiteOwnerDTO dto = ConvertHelper.convert(owner, SiteOwnerDTO.class);
				Community ownerCom = this.communityProvider.findCommunityById(owner.getOwnerId());
				if(null != ownerCom)
					dto.setOwnerName(ownerCom.getName());
				rSiteDTO.getOwners().add(dto);
			}
		} 
		List<RentalItem> items = rentalv2Provider.findRentalSiteItems(rentalSite.getId());
		if (null != items){
			rSiteDTO.setSiteItems(new ArrayList<>());
			for (RentalItem item : items) {
				SiteItemDTO siteItemDTO =convertItem2DTO(item);
				rSiteDTO.getSiteItems().add(siteItemDTO);
			}
		}
		List<RentalConfigAttachment> attachments=this.rentalv2Provider.queryRentalConfigAttachmentByOwner(EhRentalv2Resources.class.getSimpleName(),rentalSite.getId());
		rSiteDTO.setAttachments(convertAttachments(attachments));

		long time3 = System.currentTimeMillis();
		LOGGER.info("populate time={}", time3 - time2);
		//计算显示价格
		calculatePrice(rentalSite, rSiteDTO, sceneTokenDTO);

		long time4 = System.currentTimeMillis();
		LOGGER.info("calculatePrice time={}", time4 - time3);
		//更新价格平均值
		rentalSite.setAvgPriceStr(rSiteDTO.getAvgPriceStr());
//		rentalv2Provider.updateRentalSite(rentalSite);
//		}
		return rSiteDTO;
	}

	private String calculatePrice(RentalResource rentalSite, RentalSiteDTO rSiteDTO, SceneTokenDTO sceneTokenDTO) {

		String beginTime = null;
		String endTime = null;

		//起止时间为资源允许预定的最早和最晚时间
		//由于记录的是一个资源的最早可以预定时长和最晚可预订时长,所以现在+最晚可预订时长是最早的资源,现在+最早可预订时长 是最晚的资源
		if (NormalFlag.NEED.getCode() == rentalSite.getRentalEndTimeFlag()) {
			Calendar beginCalendar = Calendar.getInstance();
			beginCalendar.setTimeInMillis(DateHelper.currentGMTTime().getTime()+rentalSite.getRentalEndTime());
			beginTime = dateSF.get().format(beginCalendar.getTime());
		}

		if (NormalFlag.NEED.getCode() == rentalSite.getRentalStartTimeFlag()) {
			Calendar endCalendar = Calendar.getInstance();
			endCalendar.setTimeInMillis(DateHelper.currentGMTTime().getTime()+rentalSite.getRentalStartTime());
			endTime = dateSF.get().format(endCalendar.getTime());
		}else {
			Calendar endCalendar = Calendar.getInstance();
			//暂时默认查询7天的表格价格
			endCalendar.setTimeInMillis(DateHelper.currentGMTTime().getTime()+7 * 24 * 60 * 60 * 1000L);
			endTime = dateSF.get().format(endCalendar.getTime());
		}

		BigDecimal minPrice = null;
		Double minTimeStep = 1.0;
		BigDecimal maxPrice = null;
		Double maxTimeStep = 1.0;

		try {
			long time3 = System.currentTimeMillis();
			List<RentalCell> cells = findRentalCellBetweenDates(rSiteDTO.getRentalSiteId(), beginTime, endTime);
			long time4 = System.currentTimeMillis();
			LOGGER.info("calculatePrice get cell time={}", time4 - time3);

			if(null == cells || cells.size() == 0) {
				rSiteDTO.setAvgPrice(new BigDecimal(0));
			}else {
				BigDecimal sum = new BigDecimal(0);
				for(RentalCell cell : cells){
					//根据场景取价格
					BigDecimal price = cell.getPrice();
					if (null != sceneTokenDTO) {
						String scene = sceneTokenDTO.getScene();
						if (SceneType.PM_ADMIN.getCode().equals(scene)) {
							price = cell.getOrgMemberPrice();
						}
					}
					//对于按小时预约的,取平均值进行计算
					if(cell.getRentalType().equals(RentalType.HOUR.getCode())){
						//最小值超过了cell值,则cell值代替最小值
						if(minPrice == null || minPrice.divide(new BigDecimal(minTimeStep), 3, RoundingMode.HALF_UP).compareTo(
								price.divide(new BigDecimal(cell.getTimeStep()), 3, RoundingMode.HALF_UP)) == 1){
							minPrice = price;
							minTimeStep = cell.getTimeStep();
						}
						if(maxPrice == null ||  maxPrice.divide(new BigDecimal( maxTimeStep), 3, RoundingMode.HALF_UP).compareTo(
								price.divide(new BigDecimal(cell.getTimeStep()), 3, RoundingMode.HALF_UP)) == -1){
							maxPrice = price;
							maxTimeStep = cell.getTimeStep();
						}
					}else{
						if(minPrice == null || minPrice.compareTo(price) == 1){
							minPrice = price;
						}
						if(maxPrice == null || maxPrice.compareTo(price) == -1){
							maxPrice = price;
						}
					}
					sum = sum.add(price);
				}
				//四舍五入保留三位
				rSiteDTO.setAvgPrice(sum.divide(new BigDecimal(cells.size()), 3, RoundingMode.HALF_UP));
			}

			long time5 = System.currentTimeMillis();
			LOGGER.info("calculatePrice foreach time={}", time5 - time4);
		} catch (ParseException e) {
			LOGGER.error("calculatePrice error", e);
		}
		if(minPrice == null)
			minPrice = new BigDecimal(0);
		if(maxPrice == null)
			maxPrice = new BigDecimal(0);
		if( minPrice.compareTo(maxPrice) == 0){
			String priceStr = priceToString(minPrice,rentalSite.getRentalType(),minTimeStep);
			rSiteDTO.setAvgPriceStr(priceStr);

			return priceStr;
		}else{
			String priceStr =priceToString(minPrice,rentalSite.getRentalType(),minTimeStep)
					+ "~" + priceToString(maxPrice,rentalSite.getRentalType(),maxTimeStep);
			rSiteDTO.setAvgPriceStr(priceStr);
			return priceStr;
		}

	}

	private boolean isInteger(BigDecimal b){
		if(new BigDecimal(b.intValue()).compareTo(b)==0){
			return true;
		}else{
			return false;
		}
	}
	private boolean isInteger(double d){
		double eps = 0.0001;
		return Math.abs(d - (double)((int)d)) < eps;
	}
	private String priceToString(BigDecimal price, Byte rentalType, Double timeStep) {
		String priceString = isInteger(price)? String.valueOf(price.intValue()): price.toString() ;
		if(price.compareTo(new BigDecimal(0)) == 0)
			return "免费";
		if(rentalType.equals(RentalType.DAY.getCode()))
			return "￥"+ priceString +"/天";
		if(rentalType.equals(RentalType.HALFDAY.getCode()))
			return "￥"+ priceString +"/半天";
		if(rentalType.equals(RentalType.THREETIMEADAY.getCode()))
			return "￥"+ priceString +"/半天";
		if(rentalType.equals(RentalType.HOUR.getCode()))
			return "￥"+ priceString +"/"+(isInteger(timeStep)?String.valueOf(timeStep.intValue()).equals("1")?"":String.valueOf(timeStep.intValue()):timeStep)+"小时";
		return "";
	}
 
	private List<RentalCell> findRentalCellBetweenDates(Long rentalSiteId, String beginTime, String endTime) throws ParseException {
		List<RentalCell>  result = new ArrayList<>();
		List<RentalCell> cells = cellList.get();
		List<Long> ids = cells.stream().map(RentalCell::getId).collect(Collectors.toList());

		List<RentalCell> dbCells = this.rentalv2Provider.getRentalCellsByIds(ids);

		for(RentalCell cell : cells){
			//如果预定时间在开始时间之前,跳过
			if (null != beginTime && 
					cell.getResourceRentalDate().before((new Date(dateSF.get().parse(beginTime).getTime())))) {
				 continue;
			}
			//如果预定时间在结束时间之后,跳过
			if (null != endTime &&
					cell.getResourceRentalDate().after((new Date(dateSF.get().parse(endTime).getTime())))) {
				 continue;
			}  
			//对于单独设置过价格和开放状态的单元格,使用数据库里记录的,改成一次性查出来
//			RentalCell dbCell = this.rentalv2Provider.getRentalCellById(cell.getId());
			for (RentalCell c: dbCells) {
				if (c.getId().equals(cell.getId())) {
					cell = c;
				}
			}

			result.add(cell);
			
		}
		return result;
		 
	}

	private SiteItemDTO convertItem2DTO(RentalItem item ){
		SiteItemDTO siteItemDTO = ConvertHelper.convert(item, SiteItemDTO.class); 
		if(item.getItemType().equals(RentalItemType.SALE.getCode())){
			//售卖型的要计算售卖数量su
			Integer sumInteger = this.rentalv2Provider.countRentalSiteItemSoldCount(item.getId()); 
			siteItemDTO.setSoldCount(sumInteger);
			siteItemDTO.setCounts(item.getCounts()-sumInteger);
		} 
		siteItemDTO.setItemName(item.getName());
		siteItemDTO.setItemPrice(item.getPrice());
		siteItemDTO.setImgUrl(this.contentServerService.parserUri(siteItemDTO.getImgUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId()));
		return siteItemDTO;
	}
	
	@Override
	public FindRentalSiteRulesCommandResponse findRentalSiteRules(
			FindRentalSiteRulesCommand cmd) {
		FindRentalSiteRulesCommandResponse response = new FindRentalSiteRulesCommandResponse();
		response.setRentalSiteRules(new ArrayList<>());

		List<RentalCell> rentalSiteRules = findRentalSiteRules(cmd.getRentalSiteId(), null, null,
						cmd.getRentalType() ,
						cmd.getRentalType().equals(RentalType.DAY.getCode())?DateLength.DAY.getCode():DateLength.MONTH.getCode(),null, null);
		// 查sitebills
		if (null != rentalSiteRules && rentalSiteRules.size() > 0) {
			for (RentalCell rsr : rentalSiteRules) {
				RentalSiteRulesDTO dto = new RentalSiteRulesDTO();
				dto.setId(rsr.getId());
				dto.setRentalSiteId(rsr.getRentalResourceId());
				dto.setRentalType(rsr.getRentalType());
				dto.setRentalStep(rsr.getRentalStep()); 
				if (dto.getRentalType().equals(RentalType.HOUR.getCode())) {
					dto.setTimeStep(rsr.getTimeStep());
					dto.setBeginTime(rsr.getBeginTime().getTime());
					dto.setEndTime(rsr.getEndTime().getTime());
				} else if (dto.getRentalType() == RentalType.HALFDAY.getCode() ||
						dto.getRentalType() == RentalType.THREETIMEADAY.getCode()) {
					dto.setAmorpm(rsr.getAmorpm());
				}
				dto.setUnit(rsr.getUnit());
				dto.setPrice(rsr.getPrice());
				dto.setRuleDate(rsr.getResourceRentalDate().getTime());
				List<RentalResourceOrder> rsbs = rentalv2Provider
						.findRentalSiteBillBySiteRuleId(rsr.getId());
				dto.setStatus(SiteRuleStatus.OPEN.getCode());
				dto.setCounts(rsr.getCounts());
				if (null != rsbs && rsbs.size() > 0) {
					for (RentalResourceOrder rsb : rsbs) {
						dto.setCounts(dto.getCounts() - rsb.getRentalCount());
					}
				}
				if (dto.getCounts() == 0 || rsr.getStatus().equals((byte)-1)) {
					dto.setStatus(SiteRuleStatus.CLOSE.getCode());
				}

				response.getRentalSiteRules().add(dto);

			}
		}

		return response;
	}

	@Override
	public RentalBillDTO addRentalBill(AddRentalBillCommand cmd) {

		if(cmd.getRentalSiteId()==null)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter of null rental site id");
		if(null==cmd.getRentalDate())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter of null rental date");
		if(cmd.getRules()==null||cmd.getRules().size()==0)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter of null rules");
		Long userId = UserContext.current().getUser().getId(); 
		RentalResource rs =this.rentalv2Provider.getRentalSiteById(cmd.getRentalSiteId());
		RentalBillDTO billDTO = ConvertHelper.convert(rs , RentalBillDTO.class);
		proccessCells(rs);
		RentalResourceType rsType = this.rentalv2Provider.getRentalResourceTypeById(rs.getResourceTypeId());
		this.dbProvider.execute((TransactionStatus status) -> {
			java.util.Date reserveTime = new java.util.Date();
			List<RentalCell> rentalSiteRules = new ArrayList<>();

			RentalOrder rentalBill = ConvertHelper.convert(rs, RentalOrder.class);
			if(null== rs.getCancelTime())
				rentalBill.setCancelTime(new Timestamp(0));
			else
				rentalBill.setCancelTime(new Timestamp(rs.getCancelTime()));
			rentalBill.setResourceName(rs.getResourceName());
			rentalBill.setNamespaceId(UserContext.getCurrentNamespaceId()==null?0:
			UserContext.getCurrentNamespaceId());
			rentalBill.setRentalResourceId(cmd.getRentalSiteId());
			rentalBill.setRentalUid(userId);
			rentalBill.setInvoiceFlag(InvoiceFlag.NONEED.getCode());
			rentalBill.setRentalDate(new Date(cmd.getRentalDate()));
			this.valiRentalBill(cmd.getRules());
			//设置订单模式
			if(rsType.getPayMode() == null )
				rentalBill.setPayMode(PayMode.ONLINE_PAY.getCode());
			else
				rentalBill.setPayMode(rsType.getPayMode());
//			rentalBill.setRentalCount(cmd.getRentalCount());
			java.math.BigDecimal siteTotalMoney = new java.math.BigDecimal(0);
			Map<java.sql.Date, Map<String,Set<Byte>>> dayMap= new HashMap<>();
			List<Long> siteRuleIds = new ArrayList<>();
			for (RentalBillRuleDTO siteRule : cmd.getRules()) {

				if (null == siteRule)
					continue;
				RentalCell rentalSiteRule = findRentalSiteRuleById(siteRule.getRuleId());
				if (null == rentalSiteRule)
					continue;

				siteRuleIds.add(siteRule.getRuleId());
				if(siteRule.getRentalCount()==null||siteRule.getRuleId() == null )
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
		                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter siteRule");
				//非独占资源，也不是场所编号的，就要有数量
				if(rs.getAutoAssign().equals(NormalFlag.NONEED.getCode())&& 
						rs.getExclusiveFlag().equals(NormalFlag.NONEED.getCode()))
					rentalBill.setRentalCount(siteRule.getRentalCount());
				else
					rentalBill.setRentalCount(1.0);

				//TODO:不允许一个用户预约多时段的情况
				
				//不允许一个用户预约一个时段多个资源的情况
				
				//给半天预定的日期map加入am和pm的byte
				if(rs.getRentalType().equals(RentalType.HALFDAY.getCode())||rs.getRentalType().equals(RentalType.THREETIMEADAY.getCode())){
					dayMap.putIfAbsent(rentalSiteRule.getResourceRentalDate(), new HashMap<>());
//					if(null==dayMap.get(rentalSiteRule.getResourceRentalDate()))
//						dayMap.put(rentalSiteRule.getResourceRentalDate(), new HashMap<>());
					if(rs.getAutoAssign().equals(NormalFlag.NONEED.getCode())){
						String key = "无场所";
						dayMap.get(rentalSiteRule.getResourceRentalDate()).putIfAbsent(key, new HashSet<>());
//						if(null==dayMap.get(rentalSiteRule.getResourceRentalDate()).get("无场所"))
//							dayMap.get(rentalSiteRule.getResourceRentalDate()).put("无场所",new HashSet<>());
						dayMap.get(rentalSiteRule.getResourceRentalDate()).get(key).add(rentalSiteRule.getAmorpm());
					}
					else{
						dayMap.get(rentalSiteRule.getResourceRentalDate()).putIfAbsent(rentalSiteRule.getResourceNumber(), new HashSet<>());
//						if(null==dayMap.get(rentalSiteRule.getResourceRentalDate()).get(rentalSiteRule.getResourceNumber()))
//							dayMap.get(rentalSiteRule.getResourceRentalDate()).put(rentalSiteRule.getResourceNumber(),new HashSet<>());
						dayMap.get(rentalSiteRule.getResourceRentalDate()).get(rentalSiteRule.getResourceNumber()).add(rentalSiteRule.getAmorpm());
					}
					
				}
				rentalSiteRules.add(rentalSiteRule);
				//计算单元格的起止时间-小时单元格提醒时间-30分钟,结束时间直接用
				//按天和半天预定 提醒时间: 前一天下午4点
				//按天预订结束时间:第二天
				//按半天预定 结束时间根据产品需求,早上 12 下午  18 晚上 21 
				//计算的时间用于定时任务 提醒时间:消息提醒  结束时间:订单过期
				Timestamp startTime = null;
				Timestamp reminderTime = null;
				Timestamp endTime = null;
				
				if(rentalSiteRule.getRentalType() == RentalType.HOUR.getCode()){
					startTime =  new Timestamp(rentalSiteRule.getBeginTime().getTime() );
					reminderTime =  new Timestamp(rentalSiteRule.getBeginTime().getTime() - 30*60*1000L);
					endTime = rentalSiteRule.getEndTime();
				}else if(rentalSiteRule.getRentalType().equals(RentalType.DAY.getCode())){
					startTime = new Timestamp(rentalSiteRule.getResourceRentalDate().getTime() );
					reminderTime = new Timestamp(rentalSiteRule.getResourceRentalDate().getTime() - 8*60*60*1000L);
					endTime = new Timestamp(rentalSiteRule.getResourceRentalDate().getTime() + 24*60*60*1000L);
				}else  {
					if(rentalSiteRule.getAmorpm().equals(AmorpmFlag.AM.getCode())){
						reminderTime = new Timestamp(rentalSiteRule.getResourceRentalDate().getTime() - 8*60*60*1000L);
						startTime = new Timestamp(rentalSiteRule.getResourceRentalDate().getTime() + 10*60*60*1000L);
						endTime = new Timestamp(rentalSiteRule.getResourceRentalDate().getTime() + 12*60*60*1000L);
					}else if(rentalSiteRule.getAmorpm().equals(AmorpmFlag.PM.getCode())){
						reminderTime = new Timestamp(rentalSiteRule.getResourceRentalDate().getTime() - 8*60*60*1000L);
						startTime = new Timestamp(rentalSiteRule.getResourceRentalDate().getTime() + 15*60*60*1000L);
						endTime = new Timestamp(rentalSiteRule.getResourceRentalDate().getTime() + 18*60*60*1000L);
					}else   {
						reminderTime = new Timestamp(rentalSiteRule.getResourceRentalDate().getTime() - 8*60*60*1000L);
						startTime = new Timestamp(rentalSiteRule.getResourceRentalDate().getTime() + 20*60*60*1000L);
						endTime = new Timestamp(rentalSiteRule.getResourceRentalDate().getTime() + 21*60*60*1000L);
					}
				}
				 //如果订单的提醒时间在本单元格 提醒时间之后 或者 订单提醒时间为空
				if (null == rentalBill.getReminderTime()|| rentalBill.getReminderTime().after(reminderTime))
					rentalBill.setReminderTime(reminderTime);
				 //如果订单的起始时间在本单元格 起始时间之后 或者 订单时间为空
				if (null == rentalBill.getStartTime()|| rentalBill.getStartTime().after(startTime))
					rentalBill.setStartTime(startTime);
				//如果订单的结束时间在本单元格 结束时间之前 或者 订单时间为空
				if (null == rentalBill.getEndTime()|| rentalBill.getEndTime().before(endTime))
					rentalBill.setEndTime(endTime);
				 
				if(rs.getNeedPay().equals(NormalFlag.NEED.getCode())){

					//解析场景信息
					SceneTokenDTO sceneTokenDTO = null;
					if (null != cmd.getSceneToken()) {
						User user = UserContext.current().getUser();
						sceneTokenDTO = userService.checkSceneToken(user.getId(), cmd.getSceneToken());
					}
					BigDecimal amount = null == rentalSiteRule.getPrice() ? new java.math.BigDecimal(0) : rentalSiteRule.getPrice();
					BigDecimal halfPrice = null == rentalSiteRule.getHalfresourcePrice()?new java.math.BigDecimal(0):rentalSiteRule.getHalfresourcePrice();
					if (null != sceneTokenDTO) {
						String scene = sceneTokenDTO.getScene();
						if (SceneType.PM_ADMIN.getCode().equals(scene)) {
							amount = null == rentalSiteRule.getOrgMemberPrice() ? new java.math.BigDecimal(0) : rentalSiteRule.getOrgMemberPrice();
							halfPrice = null == rentalSiteRule.getHalfOrgMemberPrice()?new java.math.BigDecimal(0):rentalSiteRule.getHalfOrgMemberPrice();
						}
					}

					if((siteRule.getRentalCount()-siteRule.getRentalCount().intValue())>0){
						//有半个
						//整数部分计算
						if(siteRule.getRentalCount().intValue()>0)
							siteTotalMoney = siteTotalMoney.add(amount.multiply(new BigDecimal(siteRule.getRentalCount().intValue())));
						//小数部分计算
						siteTotalMoney = siteTotalMoney.add(halfPrice);
					}else{
						siteTotalMoney = siteTotalMoney.add(amount.multiply(new BigDecimal(siteRule.getRentalCount())));
			 
					}

					//优惠
					if(rs.getDiscountType()!=null) {
						if(	rs.getDiscountType().equals(DiscountType.FULL_MOENY_CUT_MONEY.getCode())){
							//满减优惠
							//每满一次 ,就减多少
//						int multiple =  siteTotalMoney.divide(rs.getFullPrice()).intValue();
//						siteTotalMoney = siteTotalMoney.subtract(rs.getCutPrice().multiply(new BigDecimal(multiple)));
							//满了多少次,都只减一个
							if(siteTotalMoney.compareTo(rs.getFullPrice())>= 0) {
								siteTotalMoney = siteTotalMoney.subtract(rs.getCutPrice());
							}
						}else if(DiscountType.FULL_DAY_CUT_MONEY.getCode().equals(rs.getDiscountType()) ){
							double multiple =0.0;
							//满天减免
							if(rs.getRentalType().equals(RentalType.HALFDAY.getCode())){
								for(Date rentalDate:dayMap.keySet()){
									for(String resourceNumber : dayMap.get(rentalDate).keySet()) {
										if(dayMap.get(rentalDate).get(resourceNumber).size()>=2) {
											multiple = multiple+cmd.getRules().get(0).getRentalCount();
										}
									}
								}
							}else if (rs.getRentalType().equals(RentalType.THREETIMEADAY.getCode())){
								for(Date rentalDate:dayMap.keySet()){
									for(String resourceNumber : dayMap.get(rentalDate).keySet()) {
										if(dayMap.get(rentalDate).get(resourceNumber).size()>=3) {
											multiple =multiple+cmd.getRules().get(0).getRentalCount();
										}
									}
								}
							}
							siteTotalMoney = siteTotalMoney.subtract(rs.getCutPrice().multiply(new BigDecimal(multiple)));
						}
					}
				}
			}

			//不可以在开始时间-最多提前时间之前 预定 太早了
//			if (reserveTime.before(new java.util.Date(rentalBill.getStartTime().getTime()
//					- rs.getRentalStartTime()))) {
//				LOGGER.error("reserve Time before reserve start time reserveTime = "+ datetimeSF.format(reserveTime));
//				throw RuntimeErrorException 
//						.errorWith(
//								RentalServiceErrorCode.SCOPE,
//								RentalServiceErrorCode.ERROR_RESERVE_TOO_EARLY,
//										"reserve Time before reserve start time");
//			}
//			//也不可以在结束时间-最少提前时间之后预定  太迟了
//			if (reserveTime.after(new java.util.Date(rentalBill.getEndTime().getTime()
//					- rs.getRentalEndTime()))) {
//				LOGGER.error("reserve Time after reserve end time  reserveTime = "+ datetimeSF.format(reserveTime));
//				throw RuntimeErrorException
//						.errorWith(
//								RentalServiceErrorCode.SCOPE,
//								RentalServiceErrorCode.ERROR_RESERVE_TOO_LATE,
//										"reserve Time after reserve end time");
//			}
			// for (SiteItemDTO siDto : cmd.getRentalItems()) {
			// totalMoney += siDto.getItemPrice() * siDto.getCounts();
			// }
			rentalBill.setResourceTotalMoney(siteTotalMoney);
			rentalBill.setPayTotalMoney(siteTotalMoney);
			rentalBill.setReserveTime(Timestamp.valueOf(datetimeSF.get()
					.format(reserveTime)));

			rentalBill.setPaidMoney(new java.math.BigDecimal (0));

			//修改 by sw 点击下一步时，初始化订单状态，点击立即预约才进去各个模式流程  PayMode
			rentalBill.setStatus(SiteBillStatus.INACTIVE.getCode());

			SimpleDateFormat bigentimeSF = new SimpleDateFormat("MM-dd HH:mm");
			SimpleDateFormat bigenDateSF = new SimpleDateFormat("MM-dd");
			SimpleDateFormat endtimeSF = new SimpleDateFormat("HH:mm");
			//拼装使用详情
			StringBuilder useDetailSB = new StringBuilder();
			Collections.sort(siteRuleIds);

			if(rs.getRentalType().equals(RentalType.HOUR.getCode())){
//					useDetailSB.append("使用时间:");
//					useDetailSB.append("从");
				int size = siteRuleIds.size();
				RentalCell firstRsr = findRentalSiteRuleById(siteRuleIds.get(0));
				RentalCell lastRsr = findRentalSiteRuleById(siteRuleIds.get(size - 1));

				useDetailSB.append(bigentimeSF.format(firstRsr.getBeginTime()));
				useDetailSB.append("-");
				useDetailSB.append(endtimeSF.format(lastRsr.getEndTime()));
				if(rs.getExclusiveFlag().equals(NormalFlag.NEED.getCode())){
				}else if(rs.getAutoAssign().equals(NormalFlag.NONEED.getCode())){
				}else {
					// 资源编号
					useDetailSB.append("\n");
					useDetailSB.append(" ");
					useDetailSB.append(firstRsr.getResourceNumber());
				}

			}else {
				// 循环存site订单
				for (Long siteRuleId : siteRuleIds) {
					RentalCell rsr = findRentalSiteRuleById(siteRuleId);
					if (null == rsr) {
						continue;
					}
					if(useDetailSB.length()>1)
						useDetailSB.append("\n");
					if(rsr.getRentalType().equals(RentalType.DAY.getCode())){
//					useDetailSB.append("使用时间:");
						useDetailSB.append(bigenDateSF.format(rsr.getResourceRentalDate()));
					}else if (rsr.getRentalType().equals(RentalType.HALFDAY.getCode())
							|| rsr.getRentalType().equals(RentalType.THREETIMEADAY.getCode())){
//					useDetailSB.append("使用时间:");
						useDetailSB.append(bigenDateSF.format(rsr.getResourceRentalDate())).append(" ");
						if(rsr.getAmorpm().equals(AmorpmFlag.AM.getCode()))
							useDetailSB.append(this.localeStringService.getLocalizedString(RentalNotificationTemplateCode.SCOPE,
									""+AmorpmFlag.AM.getCode(), RentalNotificationTemplateCode.locale, "morning"));
						if(rsr.getAmorpm().equals(AmorpmFlag.PM.getCode()))
							useDetailSB.append(this.localeStringService.getLocalizedString(RentalNotificationTemplateCode.SCOPE,
									""+AmorpmFlag.PM.getCode(), RentalNotificationTemplateCode.locale, "afternoon"));
						if(rsr.getAmorpm().equals(AmorpmFlag.NIGHT.getCode()))
							useDetailSB.append(this.localeStringService.getLocalizedString(RentalNotificationTemplateCode.SCOPE,
									""+AmorpmFlag.NIGHT.getCode(), RentalNotificationTemplateCode.locale, "night"));
					}
					if(rs.getExclusiveFlag().equals(NormalFlag.NEED.getCode())){
						//独占资源 只有时间
					}else if(rs.getAutoAssign().equals(NormalFlag.NONEED.getCode())){

					}else {
						// 资源编号
						useDetailSB.append(" ");
						useDetailSB.append(rsr.getResourceNumber());
					}
				}
			}

			rentalBill.setUseDetail(useDetailSB.toString());
			rentalBill.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			rentalBill.setCreatorUid(userId);
			rentalBill.setVisibleFlag(VisibleFlag.VISIBLE.getCode());
	 
			//用基于服务器平台的锁添加订单（包括验证和添加）
			Tuple<Long, Boolean> tuple = this.coordinationProvider
					.getNamedLock(CoordinationLocks.CREATE_RENTAL_BILL.getCode())
					.enter(() -> {
						// this.groupProvider.updateGroup(group);
						this.valiRentalBill(cmd.getRules());
						return this.rentalv2Provider.createRentalOrder(rentalBill);
					});
			Long rentalBillId = tuple.first();
			 
			for (RentalBillRuleDTO siteRule : cmd.getRules())  {
				BigDecimal money = new BigDecimal(0);
				RentalCell rsr = findRentalSiteRuleById(siteRule.getRuleId());

				if (null == rsr) {
					continue;
				}

				if((siteRule.getRentalCount()-siteRule.getRentalCount().intValue())>0){
					//有半个
					//整数部分计算
					if(siteRule.getRentalCount().intValue()>0)
						money = money.add(  (null == rsr.getPrice()?new java.math.BigDecimal(0):rsr.getPrice()).multiply(
								new   java.math.BigDecimal(siteRule.getRentalCount().intValue() )));
					//小数部分计算
					siteTotalMoney = siteTotalMoney.add(  (null == rsr.getHalfresourcePrice()?new java.math.BigDecimal(0):rsr.getPrice()));
				}
				else{
					siteTotalMoney = siteTotalMoney.add(  (null == rsr.getPrice()?new java.math.BigDecimal(0):rsr.getPrice()).multiply(
							new   java.math.BigDecimal(siteRule.getRentalCount() )));
		 
				}
				RentalResourceOrder rsb = ConvertHelper.convert(rsr, RentalResourceOrder.class);
				rsb.setRentalOrderId(rentalBillId);
				rsb.setAmorpm(rsr.getAmorpm()); 
				rsb.setEndTime(rsr.getEndTime());
				rsb.setBeginTime(rsr.getBeginTime());
				rsb.setTotalMoney(money);
				rsb.setRentalCount(siteRule.getRentalCount());
				rsb.setRentalResourceRuleId(rsr.getId());
				rsb.setResourceRentalDate(new Date(cmd.getRentalDate()));
				rsb.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
						.getTime()));
				rsb.setCreatorUid(userId);
				rsb.setStatus(ResourceOrderStatus.NORMAL.getCode());
	
				rentalv2Provider.createRentalSiteBill(rsb);
				//对于锁住整个group的关联资源
				if(rsr.getGroupLockFlag() != null && rsr.getGroupLockFlag() == NormalFlag.NEED.getCode()){
					List<RentalCell>  rsrs =  findGroupRentalSiteRules(rsr);
					if(rsrs.size()>0){
						for(RentalCell rsrCell : rsrs){
							RentalResourceOrder rsbDisploy = ConvertHelper.convert(rsrCell, RentalResourceOrder.class);
							rsbDisploy.setRentalOrderId(rentalBillId);
							rsbDisploy.setAmorpm(rsr.getAmorpm()); 
							rsbDisploy.setEndTime(rsr.getEndTime());
							rsbDisploy.setBeginTime(rsr.getBeginTime());
							rsbDisploy.setTotalMoney( new BigDecimal(0));
							rsbDisploy.setRentalCount(siteRule.getRentalCount());
							rsbDisploy.setRentalResourceRuleId(rsr.getId());
							rsbDisploy.setResourceRentalDate(new Date(cmd.getRentalDate()));
							rsbDisploy.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
									.getTime()));
							rsbDisploy.setCreatorUid(userId);
							rsbDisploy.setStatus(ResourceOrderStatus.DISPLOY.getCode());
						}
					}
				}
				
			}

			mappingRentalBillDTO(billDTO, rentalBill);
			
			return billDTO;
		});
		return billDTO;
	}

	@Override
	public CommonOrderDTO getRentalBillPayInfo(GetRentalBillPayInfoCommand cmd) {
		RentalOrder bill = rentalv2Provider.findRentalBillById(cmd.getId());

		//调用统一处理订单接口，返回统一订单格式
		CommonOrderCommand orderCmd = new CommonOrderCommand();
		orderCmd.setBody(OrderType.OrderTypeEnum.RENTALORDER.getMsg());
		orderCmd.setOrderNo(bill.getOrderNo());
		orderCmd.setOrderType(OrderType.OrderTypeEnum.RENTALORDER.getPycode());
		orderCmd.setSubject(OrderType.OrderTypeEnum.RENTALORDER.getMsg());
		orderCmd.setTotalFee(bill.getPayTotalMoney());

		CommonOrderDTO dto = null;
		try {
			dto = commonOrderUtil.convertToCommonOrderTemplate(orderCmd);
		} catch (Exception e) {
			LOGGER.error("convertToCommonOrder is fail.",e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"convertToCommonOrder is fail.");
		}
		return dto;

	}

	@Override
	public void changeRentalOrderStatus(RentalOrder order, Byte status, Boolean cancelOtherOrderFlag){

		//用基于服务器平台的锁 验证线下支付 的剩余资源是否足够
		List<RentalBillRuleDTO> rules = new ArrayList<>();
		List<Long> resourceRuleIds = new ArrayList<>();

		//状态为已完成时，不需要验证，之前付款时，已经做过校验
		// 注意： valiRentalBill 方法校验时，会查出当前资源状态：已预约，此时验证会不通过
		if (SiteBillStatus.COMPLETE.getCode() != status && SiteBillStatus.FAIL.getCode() != status) {
			RentalResource rs = this.rentalv2Provider.getRentalSiteById(order.getRentalResourceId());
			proccessCells(rs);
			List<RentalResourceOrder> rsbs = rentalv2Provider
					.findRentalResourceOrderByOrderId(order.getId());
			for(RentalResourceOrder rsb : rsbs){
				RentalBillRuleDTO dto = new RentalBillRuleDTO();
				dto.setRentalCount(rsb.getRentalCount());
				dto.setRuleId(rsb.getRentalResourceRuleId());
				resourceRuleIds.add(rsb.getRentalResourceRuleId());
				rules.add(dto);
			}
		}
		
		this.coordinationProvider.getNamedLock(CoordinationLocks.CREATE_RENTAL_BILL.getCode()+order.getRentalResourceId())
		.enter(() -> {

			//验证订单下的资源是否足够
			this.valiRentalBill(rules);

			//本订单状态置为成功,
//			order.setStatus(SiteBillStatus.SUCCESS.getCode());
			order.setStatus(status);
			rentalv2Provider.updateRentalBill(order);
			return null;
		});
		//TODO: 改成工作流的短信
		//发短信给预订人
//		String templateScope = SmsTemplateCode.SCOPE;
//		List<Tuple<String, Object>> variables = smsProvider.toTupleList("useTime", order.getUseDetail());
//		smsProvider.addToTupleList(variables, "resourceName", order.getResourceName());
//		int templateId = SmsTemplateCode.RENTAL_PAY_SUCCESS_CODE;
//
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("useTime", order.getUseDetail());
//	    map.put("resourceName", order.getResourceName());
//	    sendMessageCode(order.getRentalUid(),  RentalNotificationTemplateCode.locale, map, RentalNotificationTemplateCode.RENTAL_PAY_SUCCESS_CODE);
//		String templateLocale = RentalNotificationTemplateCode.locale;
//
//		UserIdentifier userIdentifier = this.userProvider.findClaimedIdentifierByOwnerAndType(order.getRentalUid(), IdentifierType.MOBILE.getCode()) ;
//		if(null == userIdentifier){
//			LOGGER.debug("userIdentifier is null...userId = " + order.getRentalUid());
//		}else{
//			smsProvider.sendSms(UserContext.getCurrentNamespaceId(), userIdentifier.getIdentifierToken(), templateScope, templateId, templateLocale, variables);
//		}
		//根据产品定义，是在待审批的节点就不允许其他用户预订 同时段统一资源（状态不是已预约成功，比如待付款）
		if (cancelOtherOrderFlag) {
			//找到所有有这些ruleids的订单
			List<RentalOrder> otherOrders = this.rentalv2Provider.findRentalSiteBillBySiteRuleIds(resourceRuleIds);
			if(null != otherOrders && otherOrders.size() > 0) {
				for (RentalOrder otherOrder : otherOrders){
					LOGGER.debug("otherOrder is "+JSON.toJSONString(otherOrder));
					//把自己排除
					if(otherOrder.getId().equals(order.getId())) {
						continue;
					}
					//剩下的用线程池处理flowcase状态和发短信
					executorPool.execute(new Runnable() {
						@Override
						public void run() {
							//其他订单置为失败工作流设置为终止
							FlowCase flowcase = flowCaseProvider.findFlowCaseByReferId(otherOrder.getId(),REFER_TYPE,Rentalv2Controller.moduleId);
							otherOrder.setStatus(SiteBillStatus.FAIL.getCode());
							rentalv2Provider.updateRentalBill(otherOrder);

							FlowAutoStepDTO dto = new FlowAutoStepDTO();
							dto.setAutoStepType(FlowStepType.ABSORT_STEP.getCode());
							dto.setFlowCaseId(flowcase.getId());
							dto.setFlowMainId(flowcase.getFlowMainId());
							dto.setFlowNodeId(flowcase.getCurrentNodeId());
							dto.setFlowVersion(flowcase.getFlowVersion());
							dto.setStepCount(flowcase.getStepCount());
							flowService.processAutoStep(dto);

							//发短信和消息
							Map<String, String> map = new HashMap<>();
							map.put("useTime", order.getUseDetail());
							map.put("resourceName", order.getResourceName());
							sendMessageCode(order.getRentalUid(),  RentalNotificationTemplateCode.locale, map,
									RentalNotificationTemplateCode.RENTAL_CANCEL_CODE);

							String templateScope = SmsTemplateCode.SCOPE;
							int templateId = SmsTemplateCode.RENTAL_CANCEL_CODE;
							String templateLocale = RentalNotificationTemplateCode.locale;

							List<Tuple<String, Object>> variables = smsProvider.toTupleList("useTime", otherOrder.getUseDetail());
							smsProvider.addToTupleList(variables, "resourceName", otherOrder.getResourceName());

							UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(otherOrder.getRentalUid(), IdentifierType.MOBILE.getCode()) ;
							if(null == userIdentifier){
								LOGGER.debug("userIdentifier is null...userId = " + otherOrder.getRentalUid());
							}else{
								smsProvider.sendSms(otherOrder.getNamespaceId(), userIdentifier.getIdentifierToken(), templateScope, templateId, templateLocale, variables);
							}
						}
					});
				}
			}
		}
	}
	
	private List<RentalCell> findGroupRentalSiteRules(RentalCell rsr) {
		List<RentalCell> result = new ArrayList<>();
		for( RentalCell cell : cellList.get()){
			RentalType rentalType = RentalType.fromCode(cell.getRentalType());
			switch(rentalType){
				case DAY: 
					if(cell.getRentalType().equals(rsr.getRentalType()) && cell.getResourceRentalDate().equals(rsr.getResourceRentalDate()))
						result.add(cell);
					break;
				case HOUR: 
					if(cell.getRentalType().equals(rsr.getRentalType()) && cell.getResourceRentalDate().equals(rsr.getResourceRentalDate())
							&& cell.getBeginTime().equals(rsr.getBeginTime()))
						result.add(cell);
					break;
				default:
					if(cell.getRentalType().equals(rsr.getRentalType()) && cell.getResourceRentalDate().equals(rsr.getResourceRentalDate())
							&& cell.getAmorpm().equals(rsr.getAmorpm()))
						result.add(cell);
					break; 
			}	 
		}
		return result;
	}

	/**
	 * 每半小时的50秒钟时候开始执行
	 * */
	@Scheduled(cron = "50 0/30 * * * ?")
	public void rentalSchedule(){
		if(RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE){
			//把所有状态为success-已预约的捞出来 
			Long currTime = DateHelper.currentGMTTime().getTime();
			List<RentalOrder>  orders = rentalv2Provider.listSuccessRentalBills();
			for(RentalOrder order : orders ){
				Long orderReminderTimeLong = order.getReminderTime().getTime();
				Long orderEndTimeLong = order.getEndTime().getTime();
				//时间快到发推送
				if(currTime<orderReminderTimeLong && currTime + 30*60*1000l >= orderReminderTimeLong){
					Map<String, String> map = new HashMap<String, String>();  
			        map.put("resourceName", order.getResourceName());
			        map.put("startTime", order.getUseDetail()); 
					String notifyTextForOther = localeTemplateService.getLocaleTemplateString(RentalNotificationTemplateCode.SCOPE, 
							RentalNotificationTemplateCode.RENTAL_BEGIN_NOTIFY, RentalNotificationTemplateCode.locale, map, "");
					final Job job3 = new Job(
							SendMessageAction.class.getName(),
							new Object[] {order.getRentalUid(),notifyTextForOther});
					jesqueClientFactory.getClientPool().delayedEnqueue(queueName, job3,
							orderReminderTimeLong);
				}
				
				//订单过期,置状态
				if(orderEndTimeLong <= currTime){
					order.setStatus(SiteBillStatus.OVERTIME.getCode());
					rentalv2Provider.updateRentalBill(order);
				}else if(currTime + 30*60*1000l >= orderReminderTimeLong){
					//超期未确认的置为超时
					final Job job1 = new Job(
							IncompleteUnsuccessRentalBillAction.class.getName(),
							new Object[] { String.valueOf(order.getId()) });
		
					jesqueClientFactory.getClientPool().delayedEnqueue(queueName, job1,
							orderEndTimeLong); 
				}
			}
		}
	}
	/**
	 * 给成功的订单发推送
	 * 
	 * */
	@Override
	public void addOrderSendMessage(RentalOrder rentalBill ){
		//消息推送
		//定时任务给用户推送
		User user =this.userProvider.findUserById(rentalBill.getRentalUid()) ;
		if (null == user )
			return; 
//		SimpleDateFormat bigentimeSF = new SimpleDateFormat("MM-dd HH:mm"); 
		Map<String, String> map = new HashMap<String, String>();  
//        map.put("resourceName", rentalBill.getResourceName());
//        map.put("startTime", ""+bigentimeSF.format(rentalBill.getStartTime())); 
//		String notifyTextForOther = localeTemplateService.getLocaleTemplateString(RentalNotificationTemplateCode.SCOPE, 
//				RentalNotificationTemplateCode.RENTAL_BEGIN_NOTIFY, RentalNotificationTemplateCode.locale, map, "");
		RentalResource rs = this.rentalv2Provider.getRentalSiteById(rentalBill.getRentalResourceId());
		if(null == rs)
			return;
//		RentalType rentalType = RentalType.fromCode(rs.getRentalType());
//		final Job job3 = new Job(
//				SendMessageAction.class.getName(),
//				new Object[] {rentalBill.getRentalUid(),notifyTextForOther});
		try{
//			if(rentalType != null) {
//				switch(rentalType) {
//				case HOUR:
//					// 在开始时间前30分钟提醒 
//					jesqueClientFactory.getClientPool().delayedEnqueue(queueName, job3,
//							rentalBill.getStartTime().getTime() - 30*60*1000L );
//					break; 
//				default:
//					// 在开始时间前一天的下午16点提醒
//					Calendar calendar = Calendar.getInstance();
//					calendar.setTime(rentalBill.getStartTime() );
//					calendar.add(Calendar.DATE, -1);
//					calendar.set(Calendar.HOUR_OF_DAY, 16);
//					calendar.set(Calendar.SECOND, 0);
//					calendar.set(Calendar.MINUTE, 0);
//					calendar.set(Calendar.MILLISECOND, 0);
//					jesqueClientFactory.getClientPool().delayedEnqueue(queueName, job3,calendar.getTimeInMillis() );
//					break;
//					
//				}
//			}  
		
			map = new HashMap<>();
			map.put("userName", user.getNickName());
		    map.put("resourceName", rentalBill.getResourceName());
		    map.put("useDetail", rentalBill.getUseDetail());
		    map.put("rentalCount", rentalBill.getRentalCount()==null?"1":""+rentalBill.getRentalCount());  
		    sendMessageCode(rs.getChargeUid(),  RentalNotificationTemplateCode.locale, map, RentalNotificationTemplateCode.RENTAL_ADMIN_NOTIFY);
		}catch(Exception e){
			LOGGER.error("SEND MESSAGE FAILED ,cause "+e.getLocalizedMessage());
		}
	}
	
	@Override
    public void sendMessageCode(Long uid, String locale, Map<String, String> map, int code) {
        String scope = RentalNotificationTemplateCode.SCOPE;
        
        String notifyTextForOther = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
        sendMessageToUser(uid, notifyTextForOther);
    }
    
	private void sendMessageToUser(Long userId, String content) {
		if(null == userId)
			return;
		MessageDTO messageDto = new MessageDTO();
		messageDto.setAppId(AppConstants.APPID_MESSAGING);
		messageDto.setSenderUid(User.SYSTEM_USER_LOGIN.getUserId());
		messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), userId.toString()));
		messageDto
				.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
		messageDto.setBodyType(MessageBodyType.TEXT.getCode());
		messageDto.setBody(content);
		messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);
		LOGGER.debug("messageDTO : ++++ \n " + messageDto);
		// 发消息 +推送
		messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
				userId.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
	}

	@Override
	public void valiRentalBill(Double rentalcount, List<RentalBillRuleDTO> ruleDTOs) {
		// 如果有一个规则，剩余的数量少于预定的数量
		for (RentalBillRuleDTO dto  : ruleDTOs) {
			if (dto == null)
				continue;
			Double totalCount = Double.valueOf( findRentalSiteRuleById(dto.getRuleId()).getCounts());
			Double rentaledCount = this.rentalv2Provider
					.sumRentalRuleBillSumCounts(dto.getRuleId());
			if (null == rentaledCount)
				rentaledCount = 0.0;
			if ((totalCount - rentaledCount) < rentalcount) {
				throw RuntimeErrorException
						.errorWith(
								RentalServiceErrorCode.SCOPE,
								RentalServiceErrorCode.ERROR_NO_ENOUGH_SITES,
										" has no enough sites to rental ");
			}
		}

	}

	@Override
	public void valiRentalBill(List<RentalBillRuleDTO> ruleDTOs) {
		// 如果有一个规则，剩余的数量少于预定的数量
		for (RentalBillRuleDTO dto : ruleDTOs) {
			if (dto.getRuleId() == null)
				continue;
			Double totalCount = findRentalSiteRuleById(dto.getRuleId()).getCounts();
			Double rentaledCount = this.rentalv2Provider
					.sumRentalRuleBillSumCounts(dto.getRuleId());
			if (null == rentaledCount)
				rentaledCount = 0.0;
			if ((totalCount - rentaledCount) < dto.getRentalCount()) {
				throw RuntimeErrorException
						.errorWith(
								RentalServiceErrorCode.SCOPE,
								RentalServiceErrorCode.ERROR_NO_ENOUGH_SITES,
										" has no enough sites to rental ");
			}
		}

	}
	
	
	@Override
	public FindRentalBillsCommandResponse findRentalBills(
			FindRentalBillsCommand cmd) { 
		Long userId = UserContext.current().getUser().getId();
		if (cmd.getPageAnchor() == null)
			cmd.setPageAnchor(9223372036854775807L);
		int pageSize = PaginationConfigHelper.getPageSize(
				configurationProvider, cmd.getPageSize());
		List<Byte> status = new ArrayList<>();
		if(cmd.getBillStatus().equals(BillQueryStatus.UNPAY.getCode())){
			status.add(SiteBillStatus.LOCKED.getCode());
			status.add(SiteBillStatus.RESERVED.getCode());
			status.add(SiteBillStatus.PAYINGFINAL.getCode()); 
		}
		if(cmd.getBillStatus().equals(BillQueryStatus.VALID.getCode())){ 
			status.add(SiteBillStatus.SUCCESS.getCode()); 
		}
		else if(cmd.getBillStatus().equals(BillQueryStatus.CANCELED.getCode())){
			status.add(SiteBillStatus.FAIL.getCode()); 
			status.add(SiteBillStatus.REFUNDED.getCode()); 
			status.add(SiteBillStatus.REFUNDING.getCode()); 
		}
		else if(cmd.getBillStatus().equals(BillQueryStatus.FINISHED.getCode())){
			status.add(SiteBillStatus.COMPLETE.getCode());
			status.add(SiteBillStatus.OVERTIME.getCode()); 
		}
		ListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<RentalOrder> billList = this.rentalv2Provider.listRentalBills(userId,
				cmd.getResourceTypeId(), locator, pageSize + 1,
				status, PayMode.ONLINE_PAY.getCode());
		FindRentalBillsCommandResponse response = new FindRentalBillsCommandResponse();
		if (null == billList)
			return response;
		response.setRentalBills(new ArrayList<>());
		for (RentalOrder bill : billList) {
			RentalBillDTO dto = proccessOrderDTO(bill);
			  
			response.getRentalBills().add(dto);
		}
		return response;
	}
	private RentalBillDTO proccessOrderDTO(RentalOrder bill) {
		RentalResource rs = this.rentalv2Provider.getRentalSiteById(bill.getRentalResourceId());
		// 在转换bill到dto的时候统一先convert一下  modify by wuhan 20160804
		RentalBillDTO dto = ConvertHelper.convert(bill, RentalBillDTO.class);
		mappingRentalBillDTO(dto, bill); 
		if(dto.getStatus().equals(SiteBillStatus.PAYINGFINAL.getCode())){
			dto.setUnpayCancelTime(bill.getReserveTime().getTime() + cancelTime);
		}
		dto.setSiteItems(new ArrayList<>());
		List<RentalItemsOrder> rentalSiteItems = rentalv2Provider
				.findRentalItemsBillBySiteBillId(dto.getRentalBillId());
		if(null!= rentalSiteItems)
			for (RentalItemsOrder rib : rentalSiteItems) {
				SiteItemDTO siDTO = new SiteItemDTO();
				siDTO.setCounts(rib.getRentalCount());
				RentalItem rsItem = rentalv2Provider
						.findRentalSiteItemById(rib.getRentalResourceItemId());
				if(rsItem != null) {
					siDTO.setItemName(rsItem.getName());
				}
				siDTO.setItemPrice(rib.getTotalMoney());
				
				
				dto.getSiteItems().add(siDTO);
			}
		return dto;
	}

	@Override
	public void mappingRentalBillDTO(RentalBillDTO dto, RentalOrder bill) {
		 
		UserIdentifier userIdentifier = this.userProvider.findClaimedIdentifierByOwnerAndType(bill.getRentalUid(), IdentifierType.MOBILE.getCode()) ;
		if(null == userIdentifier){
			LOGGER.debug("userIdentifier is null...userId = " + bill.getRentalUid());
		}else{
			dto.setUserPhone(userIdentifier.getIdentifierToken());}
		User user = this.userProvider.findUserById(bill.getRentalUid());
		if(null!=user)
			dto.setUserName(user.getNickName());
		else {
			LOGGER.debug("user is null...userId = " + bill.getRentalUid());
			dto.setUserName("用户不在系统中");
			}
		dto.setSiteName(bill.getResourceName());
//		dto.setBuildingName(bill.getBuildingName());
		dto.setAddress(bill.getAddress());
		//:写入bill表
		dto.setContactPhonenum(bill.getContactPhonenum());
		
		dto.setSpec(bill.getSpec());
//		dto.setCompanyName(rs.getOwnCompanyName());
//		dto.setContactName(bill.getContactName()); 
		dto.setNotice(bill.getNotice());
		dto.setIntroduction(bill.getIntroduction());
		dto.setRentalBillId(bill.getId()); 
		RentalResource rs = this.rentalv2Provider.getRentalSiteById(bill.getRentalResourceId());
		//只有非独占资源，不需要选择资源编号且允许预约多个资源才显示该字段
		if(rs != null &&NormalFlag.NONEED.getCode() == rs.getExclusiveFlag()
				&& NormalFlag.NONEED.getCode() == rs.getAutoAssign())
			dto.setRentalCount(bill.getRentalCount());
		else
			dto.setRentalCount(null);
		if (null == bill.getStartTime()) {

		} else {
			dto.setStartTime(bill.getStartTime().getTime());
		}
		if (null == bill.getEndTime()) {

		} else {
			dto.setEndTime(bill.getEndTime().getTime());
		}
		dto.setReserveTime(bill.getReserveTime().getTime());
		if (null != bill.getPayStartTime()) {
			dto.setPayStartTime(bill.getPayStartTime().getTime());
		}
		if (null != bill.getPayTime()) {
			dto.setPayTime(bill.getPayTime().getTime());
		}
		if (null != bill.getPayEndTime()) {
			dto.setPayDeadLineTime(bill.getPayEndTime().getTime());
		}
		if (null != bill.getCancelTime()) {
			dto.setCancelTime(bill.getCancelTime().getTime());
		}
		// Integer sitePrice = rentalProvider.getSumSitePrice(dto
		// .getRentalBillId());
		// dto.setSitePrice(sitePrice);
		dto.setTotalPrice(bill.getPayTotalMoney());
		dto.setSitePrice(bill.getResourceTotalMoney());
		dto.setReservePrice(bill.getReserveMoney());
		dto.setPaidPrice(bill.getPaidMoney());
		dto.setUnPayPrice(bill.getPayTotalMoney().subtract(bill.getPaidMoney()));
		dto.setInvoiceFlag(bill.getInvoiceFlag());
		dto.setStatus(bill.getStatus());
		dto.setRentalSiteRules(new ArrayList<>());
		//支付方式
		List<RentalOrderPayorderMap> billPaybillMaps = this.rentalv2Provider.findRentalBillPaybillMapByBillId(bill.getId()) ;
		if(null!=billPaybillMaps && billPaybillMaps.size()>0)
			dto.setVendorType(billPaybillMaps.get(0).getVendorType());
		// 订单的rules
		dto.setRentalSiteRules(new ArrayList<>());
		List<RentalResourceOrder> rsbs = rentalv2Provider
				.findRentalResourceOrderByOrderId(bill.getId());
		for (RentalResourceOrder rsb : rsbs) { 
			RentalSiteRulesDTO ruleDto = new RentalSiteRulesDTO();
			ruleDto.setId(rsb.getId());
			ruleDto.setRentalSiteId(rsb.getRentalResourceRuleId());
			ruleDto.setRentalType(rsb.getRentalType());
			ruleDto.setRentalStep(rsb.getRentalStep()); 
			if (ruleDto.getRentalType().equals(RentalType.HOUR.getCode())) { 
				ruleDto.setBeginTime(rsb.getBeginTime().getTime());
				ruleDto.setEndTime(rsb.getEndTime().getTime());
			} else if (ruleDto.getRentalType() == RentalType.HALFDAY.getCode() ||
					ruleDto.getRentalType() == RentalType.THREETIMEADAY.getCode()) {
				ruleDto.setAmorpm(rsb.getAmorpm());
			} 
			ruleDto.setPrice(rsb.getPrice());
			ruleDto.setRuleDate(rsb.getResourceRentalDate().getTime()); 
			  
			dto.getRentalSiteRules().add(ruleDto); 
			
		}
		List<RentalItemsOrder> ribs = rentalv2Provider
				.findRentalItemsBillBySiteBillId(bill.getId());
		if(null!=ribs){
			dto.setSiteItems(new ArrayList<>());
			//dto不是final的变量就改为for循环,不用foreach
			for(RentalItemsOrder item : ribs){
				SiteItemDTO siDTO = ConvertHelper.convert(item, SiteItemDTO.class);
				siDTO.setImgUrl(this.contentServerService.parserUri(item.getImgUri(), EntityType.USER.getCode(), 
						UserContext.current().getUser().getId()));
				siDTO.setItemName(item.getItemName());
				siDTO.setCounts(item.getRentalCount());
				siDTO.setItemType(item.getItemType());
				dto.getSiteItems().add(siDTO);
			}
		} 
		dto.setUseDetail(bill.getUseDetail()); 
				
		// 订单的附件attachments
		dto.setBillAttachments(new ArrayList<>());
		List<RentalOrderAttachment> attachments = rentalv2Provider
				.findRentalBillAttachmentByBillId(dto.getRentalBillId());
		for (RentalOrderAttachment attachment : attachments) {
			BillAttachmentDTO attachmentDTO = new BillAttachmentDTO();
			attachmentDTO.setAttachmentType(attachment.getAttachmentType());
			attachmentDTO.setBillId(attachment.getRentalOrderId());
			if(attachment.getAttachmentType().equals(AttachmentType.ATTACHMENT.getCode())){
				attachmentDTO.setResourceUrl(this.contentServerService.parserUri(attachment.getContent(), EntityType.USER.getCode(), UserContext.current().getUser().getId()));
				ContentServerResource csr = this.contentServerService.findResourceByUri(attachment.getContent());
				if(csr!=null){
					attachmentDTO.setResourceName(csr.getResourceName());
					attachmentDTO.setResourceSize(csr.getResourceSize());
				}
			}else if (AttachmentType.RECOMMEND_USER.getCode().equals(attachment.getAttachmentType())) {
				List<RentalConfigAttachment> tempAttachments = rentalv2Provider
						.queryRentalConfigAttachmentByOwner(AttachmentType.ORDER_RECOMMEND_USER.name(), bill.getId());
				List<RentalRecommendUser> recommendUsers = tempAttachments.stream()
						.map(r -> ConvertHelper.convert(r, RentalRecommendUser.class)).collect(Collectors.toList());
				attachmentDTO.setRecommendUsers(recommendUsers);

			}else if (attachment.getAttachmentType().equals(AttachmentType.GOOD_ITEM.getCode())) {
				List<RentalConfigAttachment> tempAttachments = rentalv2Provider
						.queryRentalConfigAttachmentByOwner(AttachmentType.ORDER_GOOD_ITEM.name(), bill.getId());
				List<RentalGoodItem> goodItems = tempAttachments.stream()
						.map(r -> ConvertHelper.convert(r, RentalGoodItem.class)).collect(Collectors.toList());
				attachmentDTO.setGoodItems(goodItems);
			}
			attachmentDTO.setContent(attachment.getContent());
			 
			attachmentDTO.setId(attachment.getId());
			dto.getBillAttachments().add(attachmentDTO);
		}
	}

	@Override
	public void addRentalSiteSimpleRules(AddRentalSiteRulesAdminCommand cmd) {

		if(null == cmd.getAutoAssign()) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid parameter AutoAssign");
		}

		if (cmd.getAutoAssign().equals(NormalFlag.NEED.getCode())) {
			Set<SiteNumberDTO> siteNumberSet = new HashSet<>();
			siteNumberSet.addAll(cmd.getSiteNumbers());
			//先判断可预约资源数量和 资源编号列表是否一致
			if (cmd.getSiteCounts().intValue() != cmd.getSiteNumbers().size()) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
						ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter site counts is "+cmd.getSiteCounts()+".but site numbers size is "+cmd.getSiteNumbers().size());
			}
			if (cmd.getSiteCounts().intValue() != siteNumberSet.size()) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
						ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter  site numbers repeat " );
			}
		}

		if(null == cmd.getRefundFlag()) {
			cmd.setRefundFlag(NormalFlag.NONEED.getCode());
		}

		if (null == cmd.getRentalEndTimeFlag()) {
			cmd.setRentalEndTimeFlag(NormalFlag.NONEED.getCode());
		}
		if (null == cmd.getRentalStartTimeFlag()) {
			cmd.setRentalStartTimeFlag(NormalFlag.NONEED.getCode());
		}
		//给开放日期一个缺省值
		//默认从启动开始开放3个月，周六周日关闭
		this.dbProvider.execute((TransactionStatus status) -> {
			//初始化 
			//设置新规则的时候就删除之前的旧单元格
			this.rentalv2Provider.deleteRentalCellsByResourceId(cmd.getRentalSiteId());
			currentId.set(sequenceProvider.getCurrentSequence(NameMapper.getSequenceDomainFromTablePojo(EhRentalv2Cells.class)) );
			seqNum.set(0L);

			RentalResource rs = this.rentalv2Provider.getRentalSiteById(cmd.getRentalSiteId());

			if(null ==rs.getBeginDate()) {
				cmd.setBeginDate(new java.util.Date().getTime());
			}else {
				cmd.setBeginDate(rs.getBeginDate().getTime());
			}

			if(null == rs.getEndDate()){
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.MONTH, 3);
				cmd.setEndDate(calendar.getTimeInMillis());
			}else {
				cmd.setEndDate(rs.getEndDate().getTime());
			}

			List<Integer> openWeekdays = resolveOpenWeekday(rs.getOpenWeekday());
			if(openWeekdays.size() != 0){
				cmd.setOpenWeekday(openWeekdays);
			}else {
				cmd.setOpenWeekday(new ArrayList<>());
				cmd.getOpenWeekday().add(1);
				cmd.getOpenWeekday().add(2);
		        cmd.getOpenWeekday().add(3);
		        cmd.getOpenWeekday().add(4);
		        cmd.getOpenWeekday().add(5);
		        cmd.setCloseDates(null);
		    }
			//独占资源
			if(cmd.getExclusiveFlag() == NormalFlag.NEED.getCode()){
				cmd.setUnit(1.0);
				cmd.setAutoAssign(NormalFlag.NONEED.getCode());
				cmd.setMultiUnit(NormalFlag.NONEED.getCode());
				cmd.setSiteCounts(1.0);
			}

			//不需要支付时，设置价格为0
			if(cmd.getNeedPay().equals(NormalFlag.NONEED.getCode())){
				cmd.setWeekendPrice(new BigDecimal(0));
				cmd.setWorkdayPrice(new BigDecimal(0));
				cmd.setApprovingUserWeekendPrice(new BigDecimal(0));
				cmd.setApprovingUserWorkdayPrice(new BigDecimal(0));
				cmd.setOrgMemberWeekendPrice(new BigDecimal(0));
				cmd.setOrgMemberWorkdayPrice(new BigDecimal(0));
			}

			rs.setDiscountRatio(cmd.getDiscountRatio());
			rs.setDiscountType(cmd.getDiscountType());
			rs.setFullPrice(cmd.getFullPrice());
			rs.setCutPrice(cmd.getCutPrice());
			rs.setExclusiveFlag(cmd.getExclusiveFlag());
			rs.setResourceCounts(cmd.getSiteCounts());
			rs.setStatus(RentalSiteStatus.NORMAL.getCode());
			rs.setAutoAssign(cmd.getAutoAssign());
			rs.setMultiUnit(cmd.getMultiUnit());
			rs.setNeedPay(cmd.getNeedPay());
			rs.setMultiTimeInterval(cmd.getMultiTimeInterval());
			rs.setRentalType(cmd.getRentalType());
			rs.setRentalStartTimeFlag(cmd.getRentalStartTimeFlag());
			rs.setRentalEndTimeFlag(cmd.getRentalEndTimeFlag());
			rs.setRentalEndTime(cmd.getRentalEndTime());
			rs.setRentalStartTime(cmd.getRentalStartTime());
			if (NormalFlag.NONEED.getCode() == cmd.getRentalEndTimeFlag()) {
				rs.setRentalEndTime(0L);
			}
			if (NormalFlag.NONEED.getCode() == cmd.getRentalStartTimeFlag()) {
				rs.setRentalStartTime(0L);
			}

			rs.setTimeStep(cmd.getTimeStep());
			if(null == cmd.getCancelTime()) {
				cmd.setCancelTime(0L);
			}
			rs.setCancelTime(cmd.getCancelTime());
			rs.setRefundFlag(cmd.getRefundFlag());
			rs.setRefundRatio(cmd.getRefundRatio());
			rs.setUnit(cmd.getUnit());

			//modify by wh 2016-11-11 修改时间点和修改操作人的记录
			rs.setOperatorUid(UserContext.current().getUser().getId());
			rs.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

			//删除资源编号再填充
			this.rentalv2Provider.deleteRentalResourceNumbersByOwnerId(EhRentalv2Resources.class.getSimpleName(), rs.getId());
			if (cmd.getAutoAssign().equals(NormalFlag.NEED.getCode())) {
				cmd.getSiteNumbers().forEach(number -> {
					RentalResourceNumber resourceNumber = new RentalResourceNumber();
					resourceNumber.setOwnerType(EhRentalv2Resources.class.getSimpleName());
					resourceNumber.setOwnerId(rs.getId());
					resourceNumber.setResourceNumber(number.getSiteNumber());
					resourceNumber.setNumberGroup(number.getSiteNumberGroup());
					resourceNumber.setGroupLockFlag(number.getGroupLockFlag());
					this.rentalv2Provider.createRentalResourceNumber(resourceNumber);
				});
			}

			BigDecimal weekendPrice = cmd.getWeekendPrice() == null ? new BigDecimal(0) : cmd.getWeekendPrice();
			BigDecimal workdayPrice = cmd.getWorkdayPrice() == null ? new BigDecimal(0) : cmd.getWorkdayPrice();
			BigDecimal orgMemberWeekendPrice = cmd.getOrgMemberWeekendPrice() == null ? new BigDecimal(0) : cmd.getOrgMemberWeekendPrice();
			BigDecimal orgMemberWorkdayPrice = cmd.getOrgMemberWorkdayPrice() == null ? new BigDecimal(0) : cmd.getOrgMemberWorkdayPrice();
			BigDecimal approvingUserWeekendPrice = cmd.getApprovingUserWeekendPrice() == null ? new BigDecimal(0) : cmd.getApprovingUserWeekendPrice();
			BigDecimal approvingUserWorkdayPrice = cmd.getApprovingUserWorkdayPrice() == null ? new BigDecimal(0) : cmd.getApprovingUserWorkdayPrice();

			rs.setWorkdayPrice(workdayPrice);
			rs.setWeekendPrice(weekendPrice);
			rs.setOrgMemberWeekendPrice(orgMemberWeekendPrice);
			rs.setOrgMemberWorkdayPrice(orgMemberWorkdayPrice);
			rs.setApprovingUserWeekendPrice(approvingUserWeekendPrice);
			rs.setApprovingUserWorkdayPrice(approvingUserWorkdayPrice);

			if (RentalType.HOUR.getCode() == cmd.getRentalType()) {
				//删除rentalResource 对应的 timeInterval
				rentalv2Provider.deleteTimeIntervalsByOwnerId(EhRentalv2Resources.class.getSimpleName(), rs.getId());
				if(null != cmd.getTimeIntervals()) {
					
					Double beginTime = null;
					Double endTime = null;
					for (TimeIntervalDTO timeInterval: cmd.getTimeIntervals()) {

						if(null == timeInterval.getBeginTime() || null == timeInterval.getEndTime()) {
							continue;
						}
						RentalTimeInterval timeIntervalDB = ConvertHelper.convert(timeInterval, RentalTimeInterval.class);

						timeIntervalDB.setOwnerType(EhRentalv2Resources.class.getSimpleName());
						timeIntervalDB.setOwnerId(rs.getId());
						this.rentalv2Provider.createTimeInterval(timeIntervalDB);

						if(beginTime == null || beginTime > timeInterval.getBeginTime()) {
							beginTime = timeInterval.getBeginTime();
						}
						if(endTime == null || endTime < timeInterval.getEndTime()) {
							endTime = timeInterval.getEndTime();
						}
						AddRentalSiteSingleSimpleRule singleCmd = ConvertHelper.convert(cmd, AddRentalSiteSingleSimpleRule.class );
						singleCmd.setBeginTime(timeInterval.getBeginTime());
						singleCmd.setEndTime(timeInterval.getEndTime());
						singleCmd.setTimeStep(timeInterval.getTimeStep());
						singleCmd.setWeekendPrice(weekendPrice);
						singleCmd.setWorkdayPrice(workdayPrice);
						singleCmd.setOrgMemberWeekendPrice(orgMemberWeekendPrice);
						singleCmd.setOrgMemberWorkdayPrice(orgMemberWorkdayPrice);
						singleCmd.setApprovingUserWeekendPrice(approvingUserWeekendPrice);
						singleCmd.setApprovingUserWorkdayPrice(approvingUserWorkdayPrice);
						addRentalSiteSingleSimpleRule(singleCmd);
						
					}
					if((null != endTime && endTime > 24.0) || (null != beginTime && beginTime < 0)) {
						throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
								ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid parameter of timeInterval > 24 or < 0");
					}
					if (null != beginTime) {
						rs.setDayBeginTime(convertTime((long) (beginTime*1000*60*60L)));
					}
					if (null != endTime) {
						rs.setDayEndTime(convertTime((long) (endTime*1000*60*60L)));
					}
				}
			}else {
				if (RentalType.HALFDAY.getCode() == cmd.getRentalType() || RentalType.THREETIMEADAY.getCode() == cmd.getRentalType()) {
					rentalv2Provider.deleteTimeIntervalsByOwnerId(RentalTimeIntervalOwnerType.RESOURCE_HALF_DAY.getCode(), rs.getId());
					setRentalRuleTimeIntervals(RentalTimeIntervalOwnerType.RESOURCE_HALF_DAY.getCode(), rs.getId(), cmd.getHalfDayTimeIntervals());
				}
				AddRentalSiteSingleSimpleRule singleCmd=ConvertHelper.convert(cmd, AddRentalSiteSingleSimpleRule.class);
				singleCmd.setWeekendPrice(weekendPrice);
				singleCmd.setWorkdayPrice(workdayPrice);
				singleCmd.setOrgMemberWeekendPrice(orgMemberWeekendPrice);
				singleCmd.setOrgMemberWorkdayPrice(orgMemberWorkdayPrice);
				singleCmd.setApprovingUserWeekendPrice(approvingUserWeekendPrice);
				singleCmd.setApprovingUserWorkdayPrice(approvingUserWorkdayPrice);
				addRentalSiteSingleSimpleRule(singleCmd);
			}
			Long cellBeginId = sequenceProvider.getNextSequenceBlock(NameMapper
					.getSequenceDomainFromTablePojo(EhRentalv2Cells.class), seqNum.get());
			if(LOGGER.isDebugEnabled()) {
	            LOGGER.debug("eh rental cells get next sequence block, id=" + cellBeginId+",block count = "+ seqNum.get()); 
	            LOGGER.debug("eh rental cells current id =" +  sequenceProvider.getCurrentSequence(NameMapper
						.getSequenceDomainFromTablePojo(EhRentalv2Cells.class))); 
	            LOGGER.debug("eh rental cells  , begin id=" + cellList.get().get(0).getId()+
	            		",last id  = "+ cellList.get().get(cellList.get().size()-1).getId()); 
	            
	        }
			//优化方案2.0 不插入数据库,每次需要时候进行计算,根据起始id
//			this.rentalProvider.batchCreateRentalCells(cellList.get());
			rs.setCellBeginId(cellBeginId);
			rs.setCellEndId(cellBeginId+seqNum.get()-1);

			this.rentalv2Provider.updateRentalSite(rs);
			
			return null;
		});
	}
	/**
	 * 取某个场所,某段时间的单元格
	 * */
	private List<RentalCell> findRentalSiteRuleByDate(Long rentalSiteId,
			String siteNumber, Timestamp beginTime, Timestamp endTime,
			List<Byte> ampmList, String rentalDate){
		List<RentalCell>  result = new ArrayList<>();

		List<RentalCell> cells = cellList.get();
		List<Long> ids = cells.stream().map(RentalCell::getId).collect(Collectors.toList());

		List<RentalCell> dbCells = this.rentalv2Provider.getRentalCellsByIds(ids);

		for(RentalCell cell : cells){
			if (null != rentalSiteId && !rentalSiteId.equals(cell.getRentalResourceId()))
				continue;
			if (null != siteNumber && !siteNumber.equals(cell.getResourceNumber()))
				continue; 
			if (null != beginTime && null !=cell.getBeginTime() && cell.getBeginTime().before(beginTime))
				continue;
			if (null != endTime && null !=cell.getEndTime() && cell.getEndTime().after(endTime))
				continue; 
			if (null != ampmList && !ampmList.contains(cell.getAmorpm()))
				continue; 
			if (null != siteNumber && !siteNumber.equals(cell.getResourceNumber()))
				continue; 
			if (null != rentalDate && !rentalDate.equals(dateSF.get().format(cell.getResourceRentalDate())))
				continue; 
			//对于单独设置过价格和开放状态的单元格,使用数据库里记录的
//			RentalCell dbCell = this.rentalv2Provider.getRentalCellById(cell.getId());
//			if(null != dbCell )
//				cell = dbCell;
			for (RentalCell c: dbCells) {
				if (c.getId().equals(cell.getId())) {
					cell = c;
				}
			}

			result.add(cell);
			
		}
		return result;
	}
			
	/**
	 * 取某个场所,某段时间的单元格
	 * */
	private List<RentalCell> findRentalSiteRules(Long rentalSiteId,
			String ruleDate, Timestamp beginDate, Byte rentalType, Byte dateLength,Byte status, Byte rentalStartTimeFlag) {
		List<RentalCell> result = new ArrayList<>();

		List<RentalCell> cells = cellList.get();
		List<Long> ids = cells.stream().map(RentalCell::getId).collect(Collectors.toList());
		List<RentalCell> dbCells = this.rentalv2Provider.getRentalCellsByIds(ids);

		for(RentalCell cell : cells){
			if (null != rentalSiteId && !rentalSiteId.equals(cell.getRentalResourceId()))
				continue;
			if (null != status && !status.equals(cell.getStatus()))
				continue; 
			if (null == status && !cell.getStatus().equals(RentalSiteStatus.NORMAL.getCode()))
				continue;
			//如果cell在最早预定时间之后则跳过
			if (null != beginDate && rentalType.equals(RentalType.HOUR.getCode())
					&& NormalFlag.NEED.getCode() == rentalStartTimeFlag) {
				if (cell.getBeginTime().after(beginDate))
					continue;
			}

			if (null != ruleDate){
				DateLength dateLen = DateLength.fromCode(dateLength);
				if (null != dateLen) {
					switch(dateLen){
						case DAY:
							if(!cell.getResourceRentalDate().equals(Date.valueOf(ruleDate)))
								continue;
							break;
						case MONTH:
							Calendar calendar1 = Calendar.getInstance();
							calendar1.setTime(Date.valueOf(ruleDate));
							// month begin
							calendar1.set(Calendar.DAY_OF_MONTH,
									calendar1.getActualMinimum(Calendar.DAY_OF_MONTH));
							Calendar calendar2 = Calendar.getInstance();
							calendar2.setTime(Date.valueOf(ruleDate));
							// month end
							calendar2.set(Calendar.DAY_OF_MONTH,
									calendar2.getActualMaximum(Calendar.DAY_OF_MONTH));
							if(cell.getResourceRentalDate().before(calendar1.getTime()) ||
									cell.getResourceRentalDate().after(calendar2.getTime())  )
								continue;
							break;
						case WEEK:
							Calendar calendar3 = Calendar.getInstance();
							calendar3.setTime(Date.valueOf(ruleDate));
							// month begin
							calendar3.set(Calendar.DAY_OF_WEEK,
									calendar3.getActualMinimum(Calendar.DAY_OF_WEEK));
							Calendar calendar4 = Calendar.getInstance();
							calendar4.setTime(Date.valueOf(ruleDate));
							// month end
							calendar4.set(Calendar.DAY_OF_WEEK,
									calendar4.getActualMaximum(Calendar.DAY_OF_WEEK));
							if(cell.getResourceRentalDate().before(calendar3.getTime()) ||
									cell.getResourceRentalDate().after(calendar4.getTime())  )
								continue;
							break;
					}
				}
			}
			//对于单独设置过价格和开放状态的单元格,使用数据库里记录的
//			RentalCell dbCell = this.rentalv2Provider.getRentalCellById(cell.getId());
//			if(null != dbCell )
//				cell = dbCell;
			for (RentalCell c: dbCells) {
				if (c.getId().equals(cell.getId())) {
					cell = c;
				}
			}
			result.add(cell);
		}
		return result;
	} 
	/**
	 * 生成某个资源的单元格
	 * */
	private void proccessCells(RentalResource rs){
		
		cellList.set(new ArrayList<>());
		currentId.set(rs.getCellBeginId());
		seqNum.set(0L);
		BigDecimal weekendPrice = rs.getWeekendPrice() == null ? new BigDecimal(0) : rs.getWeekendPrice(); 
		BigDecimal workdayPrice = rs.getWorkdayPrice() == null ? new BigDecimal(0) : rs.getWorkdayPrice();
		BigDecimal orgMemberWeekendPrice = rs.getOrgMemberWeekendPrice() == null ? new BigDecimal(0) : rs.getOrgMemberWeekendPrice();
		BigDecimal orgMemberWorkdayPrice = rs.getOrgMemberWorkdayPrice() == null ? new BigDecimal(0) : rs.getOrgMemberWorkdayPrice();
		BigDecimal approvingUserWeekendPrice = rs.getApprovingUserWeekendPrice() == null ? new BigDecimal(0) : rs.getApprovingUserWeekendPrice();
		BigDecimal approvingUserWorkdayPrice = rs.getApprovingUserWorkdayPrice() == null ? new BigDecimal(0) : rs.getApprovingUserWorkdayPrice();

//		List<AddRentalSiteSingleSimpleRule> addSingleRules =new ArrayList<>();
		AddRentalSiteSingleSimpleRule singleCmd=ConvertHelper.convert(rs, AddRentalSiteSingleSimpleRule.class );
		singleCmd.setRentalSiteId(rs.getId());
		singleCmd.setSiteCounts(rs.getResourceCounts());
		singleCmd.setOpenWeekday(resolveOpenWeekday(rs.getOpenWeekday()));

		QueryDefaultRuleAdminResponse tempResponse = new QueryDefaultRuleAdminResponse();
		populateRentalRule(tempResponse, EhRentalv2Resources.class.getSimpleName(), rs.getId());
		singleCmd.setSiteNumbers(tempResponse.getSiteNumbers());
		singleCmd.setTimeIntervals(tempResponse.getTimeIntervals());
		singleCmd.setCloseDates(tempResponse.getCloseDates());
		singleCmd.setAttachments(tempResponse.getAttachments());
		singleCmd.setHalfDayTimeIntervals(tempResponse.getHalfDayTimeIntervals());

		if(null != rs.getBeginDate() && null != rs.getEndDate()){
			singleCmd.setBeginDate(rs.getBeginDate().getTime());
			singleCmd.setEndDate(rs.getEndDate().getTime());
			singleCmd.setWeekendPrice(weekendPrice);
			singleCmd.setWorkdayPrice(workdayPrice);
			singleCmd.setOrgMemberWeekendPrice(orgMemberWeekendPrice);
			singleCmd.setOrgMemberWorkdayPrice(orgMemberWorkdayPrice);
			singleCmd.setApprovingUserWeekendPrice(approvingUserWeekendPrice);
			singleCmd.setApprovingUserWorkdayPrice(approvingUserWorkdayPrice);
			if (rs.getRentalType().equals(RentalType.HOUR.getCode()))  {
				if(singleCmd.getTimeIntervals() != null){
					Double beginTime = null;
					Double endTime = null;
					for(TimeIntervalDTO timeInterval:singleCmd.getTimeIntervals()){
						if(timeInterval.getBeginTime() == null || timeInterval.getEndTime()==null)
							continue;
						if(beginTime==null||beginTime>timeInterval.getBeginTime())
							beginTime=timeInterval.getBeginTime();
						if(endTime==null||endTime<timeInterval.getEndTime())
							endTime=timeInterval.getEndTime();
						singleCmd.setBeginTime(timeInterval.getBeginTime());
						singleCmd.setEndTime(timeInterval.getEndTime());
						if(null!=timeInterval.getTimeStep())
							singleCmd.setTimeStep(timeInterval.getTimeStep());
						addRentalSiteSingleSimpleRule(singleCmd);
					}
				}
			} else {  
				addRentalSiteSingleSimpleRule(singleCmd);
			}	
		}
	}
	/**
	 * 根据单一时段的规则生成单元格
	 * */
	private void addRentalSiteSingleSimpleRule(AddRentalSiteSingleSimpleRule cmd) {
		Long userId = UserContext.current().getUser().getId();
		if(cmd.getSiteCounts() == null)
			cmd.setSiteCounts(1.0);
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.setTime(new Date(cmd.getBeginDate()));
		end.setTime(new Date(cmd.getEndDate()));
		if(cmd.getAutoAssign().equals(NormalFlag.NEED.getCode()) &&
				cmd.getSiteCounts().intValue() != cmd.getSiteNumbers().size()) {
			LOGGER.info("Invalid paramter site counts={}, but site numbers size is {}.", cmd.getSiteCounts(),
					cmd.getSiteNumbers().size());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid paramter site counts or site numbers");
		}
		List<Long> closeDates = cmd.getCloseDates();

		while (start.before(end)) {
			Integer weekday = start.get(Calendar.DAY_OF_WEEK);
			Integer day = start.get(Calendar.DAY_OF_MONTH);
			if (cmd.getOpenWeekday().contains(weekday) &&
					(null == closeDates || !closeDates.contains(start.getTimeInMillis()))) {

				RentalCell rsr =ConvertHelper.convert(cmd, RentalCell.class);
				rsr.setRentalResourceId(cmd.getRentalSiteId());
				rsr.setAutoAssign(cmd.getAutoAssign()); 
				if (cmd.getRentalType().equals(RentalType.HOUR.getCode())) {
					for (double i = cmd.getBeginTime(); i < cmd.getEndTime();) {
							rsr.setBeginTime(Timestamp.valueOf(dateSF.get().format(start
									.getTime())
									+ " "
//									+ String.valueOf((int) i / 1)
									+ String.valueOf((int) i)
									+ ":"
									+ String.valueOf((int) ((i % 1) * 60))
									+ ":00"));

							// i = i + cmd.getTimeStep();
							rsr.setRentalStep(1);
							rsr.setTimeStep(cmd.getTimeStep());
//						i = i + 0.5;
							i = i + cmd.getTimeStep();
//							if(i > cmd.getEndTime())
//								continue;
							rsr.setEndTime(Timestamp.valueOf(dateSF.get().format(start
									.getTime())
									+ " "
//									+ String.valueOf((int) i / 1)
									+ String.valueOf((int) i)
									+ ":"
									+ String.valueOf((int) ((i % 1) * 60))
									+ ":00"));
							rsr.setRentalResourceId(cmd.getRentalSiteId());
							rsr.setRentalType(cmd.getRentalType());
							rsr.setCounts(cmd.getSiteCounts());
							rsr.setUnit(cmd.getUnit());
							if (weekday == 1 || weekday == 7) {
								rsr.setPrice(cmd.getWeekendPrice());
								rsr.setOrgMemberPrice(cmd.getOrgMemberWeekendPrice());
								rsr.setApprovingUserPrice(cmd.getApprovingUserWeekendPrice());
							} else {
								rsr.setPrice(cmd.getWorkdayPrice());
								rsr.setOrgMemberPrice(cmd.getOrgMemberWorkdayPrice());
								rsr.setApprovingUserPrice(cmd.getApprovingUserWorkdayPrice());
							}
							if(rsr.getUnit()<1){
								rsr.setHalfresourcePrice(rsr.getPrice().divide(new BigDecimal("2"), 3, RoundingMode.HALF_UP) );
								rsr.setHalfApprovingUserPrice(rsr.getApprovingUserPrice().divide(new BigDecimal("2"), 3, RoundingMode.HALF_UP));
								rsr.setHalfOrgMemberPrice(rsr.getOrgMemberPrice().divide(new BigDecimal("2"), 3, RoundingMode.HALF_UP) );
							}
							rsr.setResourceRentalDate(Date.valueOf(dateSF.get().format(start.getTime())));
							rsr.setStatus(RentalSiteStatus.NORMAL.getCode());
							rsr.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
							rsr.setCreatorUid(userId);

							createRSR(rsr, cmd);
					}
				}else if (cmd.getRentalType().equals(RentalType.HALFDAY.getCode()) ||
						cmd.getRentalType().equals(RentalType.THREETIMEADAY.getCode())) {

					// 按半日预定
					rsr.setRentalType(cmd.getRentalType());
					rsr.setCounts(cmd.getSiteCounts() == null ? 1 : cmd.getSiteCounts());
					rsr.setUnit(cmd.getUnit());
					rsr.setRentalResourceId(cmd.getRentalSiteId());
					rsr.setResourceRentalDate(Date.valueOf(dateSF.get().format(start.getTime())));
					rsr.setStatus(RentalSiteStatus.NORMAL.getCode());
					rsr.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
					rsr.setCreatorUid(userId);
					rsr.setRentalStep(1);

					if (weekday == 1 || weekday == 7) {
						rsr.setPrice(cmd.getWeekendPrice());
						rsr.setOrgMemberPrice(cmd.getOrgMemberWeekendPrice());
						rsr.setApprovingUserPrice(cmd.getApprovingUserWeekendPrice());

						rsr.setAmorpm(AmorpmFlag.AM.getCode());
						if(rsr.getUnit()<1){
							rsr.setHalfresourcePrice(rsr.getPrice().divide(new BigDecimal("2"), 3, RoundingMode.HALF_UP) );
							rsr.setHalfApprovingUserPrice(rsr.getApprovingUserPrice().divide(new BigDecimal("2"), 3, RoundingMode.HALF_UP));
							rsr.setHalfOrgMemberPrice(rsr.getOrgMemberPrice().divide(new BigDecimal("2"), 3, RoundingMode.HALF_UP) );

						}
						createRSR(rsr, cmd);
						rsr.setAmorpm(AmorpmFlag.PM.getCode());
						createRSR(rsr, cmd);
						if(cmd.getRentalType().equals(RentalType.THREETIMEADAY.getCode())){
							rsr.setAmorpm(AmorpmFlag.NIGHT.getCode());
							createRSR(rsr, cmd);
						}
							
					} else {
						rsr.setPrice(cmd.getWorkdayPrice());
						rsr.setOrgMemberPrice(cmd.getOrgMemberWorkdayPrice());
						rsr.setApprovingUserPrice(cmd.getApprovingUserWorkdayPrice());

						rsr.setAmorpm(AmorpmFlag.AM.getCode());
						if(rsr.getUnit()<1){
							rsr.setHalfresourcePrice(rsr.getPrice().divide(new BigDecimal("2"), 3, RoundingMode.HALF_UP) );
							rsr.setHalfApprovingUserPrice(rsr.getApprovingUserPrice().divide(new BigDecimal("2"), 3, RoundingMode.HALF_UP));
							rsr.setHalfOrgMemberPrice(rsr.getOrgMemberPrice().divide(new BigDecimal("2"), 3, RoundingMode.HALF_UP) );

						}
						createRSR(rsr, cmd);
						rsr.setAmorpm(AmorpmFlag.PM.getCode());
						createRSR(rsr, cmd);
						if(cmd.getRentalType().equals(RentalType.THREETIMEADAY.getCode())){
							rsr.setAmorpm(AmorpmFlag.NIGHT.getCode());
							createRSR(rsr, cmd);
						}
					}
				}else if (cmd.getRentalType().equals(RentalType.DAY.getCode())) {
					// 按日预定
					//TODO:产品说按天预定没有场所编号，所以默认只为noneed，以后可能会改就注释掉下面这一句
//					cmd.setAutoAssign(NormalFlag.NONEED.getCode());
					rsr.setRentalResourceId(cmd.getRentalSiteId());
					rsr.setRentalType(cmd.getRentalType());
					rsr.setCounts(cmd.getSiteCounts());
					rsr.setRentalStep(1);
					rsr.setUnit(cmd.getUnit());
					if (weekday == 1 || weekday == 7) {
						rsr.setPrice(cmd.getWeekendPrice());
						rsr.setApprovingUserPrice(cmd.getApprovingUserWeekendPrice());
						rsr.setOrgMemberPrice(cmd.getOrgMemberWeekendPrice());
					}else {
						rsr.setPrice(cmd.getWorkdayPrice());
						rsr.setApprovingUserPrice(cmd.getApprovingUserWorkdayPrice());
						rsr.setOrgMemberPrice(cmd.getOrgMemberWorkdayPrice());
					}
					rsr.setResourceRentalDate(Date.valueOf(dateSF.get().format(start.getTime())));
					rsr.setStatus(RentalSiteStatus.NORMAL.getCode());
					rsr.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
					rsr.setCreatorUid(userId);
  
					createRSR(rsr, cmd );
					
				}

			}
			start.add(Calendar.DAY_OF_MONTH, 1);
		}
	}

	private void createRSR(RentalCell rsr, AddRentalSiteSingleSimpleRule cmd){
		if(cmd.getAutoAssign().equals(NormalFlag.NEED.getCode())){
			//根据用户填写分配sitenumber
			for(int num =0;num<cmd.getSiteCounts();num++){  
				rsr.setCounts(1.0);
				SiteNumberDTO dto = cmd.getSiteNumbers().get(num);
				rsr.setResourceNumber(dto.getSiteNumber());
				rsr.setNumberGroup(dto.getSiteNumberGroup());
				rsr.setGroupLockFlag(dto.getGroupLockFlag());
				//改成批量插入 2016-8-23 by wuhan
//				rentalProvider.createRentalSiteRule(rsr);
				rsr.setId(currentId.get()+seqNum.get());
				seqNum.set(seqNum.get()+1);
				cellList.get().add(ConvertHelper.convert(rsr, RentalCell.class)); 
			}
		}else{

			//改成批量插入 2016-8-23 by wuhan
//			rentalProvider.createRentalSiteRule(rsr);
			rsr.setId(currentId.get()+seqNum.get());
			seqNum.set(seqNum.get()+1);
			cellList.get().add(ConvertHelper.convert(rsr, RentalCell.class));
		  
		}
	}

	@Override
	public void deleteRentalSiteRules(DeleteRentalSiteRulesCommand cmd) {
		JSONObject jsonObject = (JSONObject) JSONValue
				.parse(cmd.getRuleDates());
		JSONArray choosenValue = (JSONArray) jsonObject.get("ruleDates");
		Gson gson = new Gson();
		List<Long> deleteDates = gson.fromJson(choosenValue.toString(),
				new TypeToken<List<Long>>() {
				}.getType());
		for (Long deleteDate : deleteDates) {
			rentalv2Provider
					.deleteResourceCells(Long.valueOf(cmd.getRentalSiteId()),
							deleteDate, deleteDate);
		}
	}

	@Override
	public void cancelRentalBill(CancelRentalBillCommand cmd) {
		java.util.Date cancelTime = new java.util.Date();
		if(null == cmd.getRentalBillId())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter of BillId error");
		RentalOrder order = this.rentalv2Provider.findRentalBillById(cmd.getRentalBillId());
		if(null == order)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter of BillId error");
		RentalResource rs = this.rentalv2Provider.getRentalSiteById(order.getRentalResourceId());		
		if (order.getStatus().equals(SiteBillStatus.SUCCESS.getCode())&&cancelTime.after(new java.util.Date(order.getStartTime().getTime()
				- rs.getCancelTime()))) {
			//当成功预约之后要判断是否过了取消时间
			LOGGER.error("cancel over time");
			throw RuntimeErrorException
					.errorWith(RentalServiceErrorCode.SCOPE,
							RentalServiceErrorCode.ERROR_CANCEL_OVERTIME,"cancel bill over time");
		}else{
			this.dbProvider.execute((TransactionStatus status) -> {
				//默认是已退款
				order.setStatus(SiteBillStatus.REFUNDED.getCode());
				if (rs.getRefundFlag().equals(NormalFlag.NEED.getCode())&&(order.getPaidMoney().compareTo(new BigDecimal(0))==1)){ 
					List<RentalOrderPayorderMap>  billmaps = this.rentalv2Provider.findRentalBillPaybillMapByBillId(order.getId());
					for(RentalOrderPayorderMap billMap : billmaps){
						//循环退款
						PayZuolinRefundCommand refundCmd = new PayZuolinRefundCommand();
						String refoundApi =  this.configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"pay.zuolin.refound", "POST /EDS_PAY/rest/pay_common/refund/save_refundInfo_record");
						String appKey = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"pay.appKey", "");
						refundCmd.setAppKey(appKey);
						Long timestamp = System.currentTimeMillis();
						refundCmd.setTimestamp(timestamp);
						Integer randomNum = (int) (Math.random()*1000);
						refundCmd.setNonce(randomNum);
						Long refoundOrderNo = this.onlinePayService.createBillId(DateHelper
								.currentGMTTime().getTime());
						refundCmd.setRefundOrderNo(String.valueOf(refoundOrderNo));
						refundCmd.setOrderNo(String.valueOf(billMap.getOrderNo()));
						refundCmd.setOnlinePayStyleNo(VendorType.fromCode(billMap.getVendorType()).getStyleNo()); 
						refundCmd.setOrderType(OrderType.OrderTypeEnum.RENTALORDER.getPycode());
						//已付金额乘以退款比例除以100
						refundCmd.setRefundAmount(order.getPaidMoney().multiply(new BigDecimal(rs.getRefundRatio()/100)));
						refundCmd.setRefundMsg("预订单取消退款");
						this.setSignatureParam(refundCmd);
						RentalRefundOrder rentalRefundOrder = ConvertHelper.convert(refundCmd,RentalRefundOrder.class);
						rentalRefundOrder.setOrderNo(billMap.getOrderNo());
						rentalRefundOrder.setRefundOrderNo(refoundOrderNo);
						rentalRefundOrder.setOrderId(order.getId()); 
						rentalRefundOrder.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
						rentalRefundOrder.setCreatorUid(UserContext.current().getUser().getId());
						rentalRefundOrder.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
						rentalRefundOrder.setOperatorUid(UserContext.current().getUser().getId());
						rentalRefundOrder.setResourceTypeId(order.getResourceTypeId());
						rentalRefundOrder.setAmount(refundCmd.getRefundAmount());
						//微信直接退款，支付宝置为退款中 
						//update by wuhan 2017-5-15 :支付宝也和微信一样退款
//						if(billMap.getVendorType().equals(VendorType.WEI_XIN.getVendorType())){
							PayZuolinRefundResponse refundResponse = (PayZuolinRefundResponse) this.restCall(refoundApi, refundCmd, PayZuolinRefundResponse.class);
							if(refundResponse.getErrorCode().equals(HttpStatus.OK.value())){
								//退款成功保存退款单信息，修改bill状态
								rentalRefundOrder.setStatus(SiteBillStatus.REFUNDED.getCode());
								order.setStatus(SiteBillStatus.REFUNDED.getCode());
							}
							else{
								LOGGER.error("bill id=["+order.getId()+"] refound error param is "+refundCmd.toString());
								throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
										RentalServiceErrorCode.ERROR_REFOUND_ERROR,
												"bill  refound error"); 
							}	
//						}
//						else{
//							rentalRefundOrder.setStatus(SiteBillStatus.REFUNDING.getCode());
//							order.setStatus(SiteBillStatus.REFUNDING.getCode());
//						}
						this.rentalv2Provider.createRentalRefundOrder(rentalRefundOrder);
					}
				}
				else{
					//如果不需要退款，直接状态为已取消
					order.setStatus(SiteBillStatus.FAIL.getCode());
				
				}
				//更新bill状态
				//只要退款就给管理员发消息,不管是退款中还是已退款
				
				rentalv2Provider.updateRentalBill(order); 
				onOrderCancel(order);
				return null;
			});
		}
	}
	/**
	 * 取消订单发推送
	 * 
	 * */
	@Override
	public void cancelOrderSendMessage(RentalOrder rentalBill){
		RentalResource rs = this.rentalv2Provider.getRentalSiteById(rentalBill.getRentalResourceId());
		if(null == rs)
			return;
		//给负责人推送
		StringBuilder managerContent = new StringBuilder();
		User user =this.userProvider.findUserById(rentalBill.getRentalUid()) ;
		if (null == user )
			return;
		managerContent.append(user.getNickName());
		managerContent.append("取消了");
		managerContent.append(rentalBill.getResourceName());
		managerContent.append("\n使用详情：");
		managerContent.append(rentalBill.getUseDetail());
		if(null != rentalBill.getRentalCount() ){
			managerContent.append("\n预约数：");
			managerContent.append(rentalBill.getRentalCount());
		}
		sendMessageToUser(rs.getChargeUid(), managerContent.toString());
	}
	
	/***给支付相关的参数签名*/
	private void setSignatureParam(PayZuolinRefundCommand cmd) {
		App app = appProvider.findAppByKey(cmd.getAppKey());
		
		Map<String,String> map = new HashMap<>();
		map.put("appKey",cmd.getAppKey());
		map.put("timestamp",cmd.getTimestamp()+"");
		map.put("nonce",cmd.getNonce()+"");
		map.put("refundOrderNo",cmd.getRefundOrderNo());
		map.put("orderNo", cmd.getOrderNo());
		map.put("onlinePayStyleNo",cmd.getOnlinePayStyleNo() );
		map.put("orderType",cmd.getOrderType() );
		//modify by wh 2016-10-24 退款使用toString,下订单的时候使用doubleValue,两边用的不一样,为了和电商保持一致,要修改成toString
//		map.put("refundAmount", cmd.getRefundAmount().doubleValue()+"");
		map.put("refundAmount", cmd.getRefundAmount().toString());
		map.put("refundMsg", cmd.getRefundMsg()); 
		String signature = SignatureHelper.computeSignature(map, app.getSecretKey());
		cmd.setSignature(signature);
	}
	@Override
	public void updateRentalSite(UpdateRentalSiteCommand cmd) {
		// 已有未取消的预定，不能修改
//		Integer billCount = rentalProvider.countRentalSiteBills(
//				cmd.getRentalSiteId(), null, null, null, null);
//		if (billCount > 0) {
//			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
//					RentalServiceErrorCode.ERROR_HAVE_BILL,
//					localeStringService.getLocalizedString(String
//							.valueOf(RentalServiceErrorCode.SCOPE), String
//							.valueOf(RentalServiceErrorCode.ERROR_HAVE_BILL),
//							UserContext.current().getUser().getLocale(),
//							"HAS BILL IN YOUR DELETE STUFF"));
//		}
		RentalResource rentalsite = this.rentalv2Provider.getRentalSiteById(cmd.getRentalSiteId()); 
		rentalsite.setAddress(cmd.getAddress());
		rentalsite.setResourceName(cmd.getSiteName());
		rentalsite.setBuildingName(cmd.getBuildingName());
		rentalsite.setSpec(cmd.getSpec());
		rentalsite.setOwnCompanyName(cmd.getCompany());
		rentalsite.setContactName(cmd.getContactName());
		rentalsite.setContactPhonenum(cmd.getContactPhonenum());
		rentalsite.setIntroduction(cmd.getIntroduction());
		rentalsite.setNotice(cmd.getNotice());
		rentalv2Provider.updateRentalSite(rentalsite);
	}

	@Override
	public void deleteRentalSite(DeleteRentalSiteCommand cmd) {
		// 已有未取消的预定，不能删除
		Integer billCount = rentalv2Provider.countRentalSiteBills(
				cmd.getRentalSiteId(), null, null, null, null);
		if (billCount > 0) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_HAVE_BILL,
							"HAS BILL IN YOUR DELETE STUFF");
		}
		rentalv2Provider.deleteResourceCells(cmd.getRentalSiteId(), null, null);
//		rentalProvider.deleteRentalBillBySiteId(cmd.getRentalSiteId());
		rentalv2Provider.deleteResource(cmd.getRentalSiteId());
	}
	@Override
	public void disableRentalSite(DisableRentalSiteCommand cmd) {
		// 已有未取消的预定，不能停用
//		Integer billCount = rentalProvider.countRentalSiteBills(
//				cmd.getRentalSiteId(), null, null, null, null);
//		if (billCount > 0) {
//			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
//					RentalServiceErrorCode.ERROR_HAVE_BILL,
//					localeStringService.getLocalizedString(String
//							.valueOf(RentalServiceErrorCode.SCOPE), String
//							.valueOf(RentalServiceErrorCode.ERROR_HAVE_BILL),
//							UserContext.current().getUser().getLocale(),
//							"HAS BILL IN YOUR DELETE STUFF"));
//		}
		rentalv2Provider.updateRentalSiteStatus(cmd.getRentalSiteId(),
				RentalSiteStatus.DISABLE.getCode());
	}
	

	@Override
	public void enableRentalSite(EnableRentalSiteCommand cmd) {
		// 已有未取消的预定，不能删除
//		Integer billCount = rentalProvider.countRentalSiteBills(
//				cmd.getRentalSiteId(), null, null, null, null);
//		if (billCount > 0) {
//			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
//					RentalServiceErrorCode.ERROR_HAVE_BILL,
//					localeStringService.getLocalizedString(String
//							.valueOf(RentalServiceErrorCode.SCOPE), String
//							.valueOf(RentalServiceErrorCode.ERROR_HAVE_BILL),
//							UserContext.current().getUser().getLocale(),
//							"HAS BILL IN YOUR DELETE STUFF"));
//		}
		rentalv2Provider.updateRentalSiteStatus(cmd.getRentalSiteId(),
				RentalSiteStatus.NORMAL.getCode());
	}
	
	
	@Override
	public void deleteRentalSiteItem(DeleteItemAdminCommand cmd) {
//		Integer billCount = rentalProvider.countRentalSiteItemBills(cmd
//				.getItemId());
//		if (billCount > 0) {
//			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
//					RentalServiceErrorCode.ERROR_HAVE_BILL,
//					localeStringService.getLocalizedString(String
//							.valueOf(RentalServiceErrorCode.SCOPE), String
//							.valueOf(RentalServiceErrorCode.ERROR_HAVE_BILL),
//							UserContext.current().getUser().getLocale(),
//							"HAS BILL IN YOUR DELETE STUFF"));
//
//		}
		rentalv2Provider.deleteRentalSiteItemById(cmd.getItemId());
	}

	@Override
	public GetItemListCommandResponse listRentalSiteItems(
			GetItemListAdminCommand cmd) {
		if(cmd.getRentalSiteId()==null)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter of null rental site id");
		
		GetItemListCommandResponse response = new GetItemListCommandResponse();
		response.setSiteItems(new ArrayList<>());
		List<RentalItem> rsiSiteItems = rentalv2Provider
				.findRentalSiteItems(cmd.getRentalSiteId());
		for (RentalItem rsi : rsiSiteItems) {
			SiteItemDTO dto = convertItem2DTO(rsi);
			 
			response.getSiteItems().add(dto);
		}
		return response;
	}

	@Override
	public AddRentalBillItemCommandResponse addRentalItemBill(AddRentalBillItemCommand cmd) {

		RentalOrder bill = rentalv2Provider.findRentalBillById(cmd
				.getRentalBillId());

		RentalResource rs =this.rentalv2Provider.getRentalSiteById(bill.getRentalResourceId());
		proccessCells(rs);
		// 循环存物品订单
		AddRentalBillItemCommandResponse response = new AddRentalBillItemCommandResponse();
		this.dbProvider.execute((TransactionStatus status) -> {
			response.setName(OrderType.OrderTypeEnum.RENTALORDER.getMsg());
			response.setDescription(OrderType.OrderTypeEnum.RENTALORDER.getMsg());
			response.setOrderType(OrderType.OrderTypeEnum.RENTALORDER.getPycode()); 
			Long userId = UserContext.current().getUser().getId();
	
			if (bill.getStatus().equals(SiteBillStatus.FAIL.getCode())) {
				throw RuntimeErrorException
						.errorWith(RentalServiceErrorCode.SCOPE,RentalServiceErrorCode.ERROR_BILL_OVERTIME, "BILL OVERTIME");
			}
			 
			//2016-6-2 10:32:44 fix bug :当有物品订单（说明是付款失败再次付款），就不再生成物品订单
			if (null != cmd.getRentalItems()&&this.rentalv2Provider.findRentalItemsBillBySiteBillId(cmd.getRentalBillId())==null) {

				Tuple<Boolean, Boolean> tuple = this.coordinationProvider
						.getNamedLock(CoordinationLocks.CREATE_RENTAL_BILL.getCode())
						.enter(() -> {
							java.math.BigDecimal itemMoney = new java.math.BigDecimal(0);
					for (SiteItemDTO siDto : cmd.getRentalItems()) {
						 
						if(siDto.getId() == null) {
							throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
				                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter of siDto id"+ siDto+".");
						}
						RentalItem rSiteItem = this.rentalv2Provider.getRentalSiteItemById(siDto.getId());
						if (null == rSiteItem)
							throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
				                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter of siDto id"+ siDto+".");
						
						if(!rSiteItem.getRentalResourceId().equals(bill.getRentalResourceId()))
							throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
				                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter item id is not this site");
							
						RentalItemsOrder rib = new RentalItemsOrder();
						rib.setTotalMoney(rSiteItem.getPrice().multiply( new java.math.BigDecimal(siDto.getCounts())));
						rib.setRentalResourceItemId(siDto.getId());
						rib.setRentalCount(siDto.getCounts());
						rib.setItemName(rSiteItem.getName());
						rib.setRentalOrderId(cmd.getRentalBillId());
						rib.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
								.getTime()));
						rib.setCreatorUid(userId);
						itemMoney  = itemMoney.add(rib.getTotalMoney());
						//用基于服务器平台的锁添加订单（包括验证和添加）
									//先验证后添加，由于锁机制，可以保证同时只有一个线程验证和添加
						if(this.valiItem(rib))
							return true;
						rentalv2Provider.createRentalItemBill(rib);
					}
					
					
					if (itemMoney.doubleValue() > 0) {
						bill.setPayTotalMoney(bill.getResourceTotalMoney().add(itemMoney));
//						bill.setReserveMoney(bill.getReserveMoney().add(itemMoney));
					}
					return false;
				});
				Boolean valiBoolean = tuple.first();
				if(valiBoolean)
					throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
						RentalServiceErrorCode.ERROR_NO_ENOUGH_ITEMS,"no enough items");
			}

			//增加审批后线上支付模式的判断 审批模式，订单状态设置成待审批 add by sw 20170506
			//现在审批线上模式，线下订单模式都跟踪状态，金额为0时，直接预约成功，否则设置成待付款
			//线上模式只有成功之后才走工作流
//			if (bill.getPayMode().equals(PayMode.ONLINE_PAY.getCode())&&compare == 0) {
			//线下模式和审批线上模式 都走工作流
			if (PayMode.ONLINE_PAY.getCode().equals(bill.getPayMode())) {
				int compare = bill.getPayTotalMoney().compareTo(BigDecimal.ZERO);
				if (compare == 0) {
					// 总金额为0，直接预订成功状态
					bill.setStatus(SiteBillStatus.SUCCESS.getCode());
//				response.setAmount(new java.math.BigDecimal(0));
				}else{
					bill.setStatus(SiteBillStatus.PAYINGFINAL.getCode());

					// n分钟后，取消未成功的订单
					final Job job1 = new Job(CancelUnsuccessRentalOrderAction.class.getName(),
							new Object[] {String.valueOf(bill.getId())});

					jesqueClientFactory.getClientPool().delayedEnqueue(queueName, job1,
							bill.getReserveTime().getTime() + cancelTime);
				}
			}else {
				bill.setStatus(SiteBillStatus.APPROVING.getCode());
			}


//			else if ( bill.getStatus().equals(
//							SiteBillStatus.LOCKED.getCode())) {
//				// 预付金额为0，且状态为locked，直接进入支付定金成功状态
//				if(bill.getReserveMoney().equals(0.0))
//					bill.setStatus(SiteBillStatus.RESERVED.getCode());
//				else if (bill.getReserveMoney().equals(bill.getPayTotalMoney().subtract(bill.getPaidMoney())))
//					bill.setStatus(SiteBillStatus.PAYINGFINAL.getCode());
//	
//			}
			 
			Long orderNo =  onlinePayService.createBillId(DateHelper.currentGMTTime().getTime());
			if (bill.getStatus().equals(SiteBillStatus.LOCKED.getCode())) {
				response.setAmount(bill.getReserveMoney());
				response.setOrderNo(String.valueOf(orderNo));
				
			}else if (bill.getStatus().equals(SiteBillStatus.PAYINGFINAL.getCode())) {
				response.setAmount(bill.getPayTotalMoney().subtract(bill.getPaidMoney()));
				response.setOrderNo(String.valueOf(orderNo));
			} else {
				response.setAmount(bill.getPayTotalMoney());
			}
			bill.setOrderNo(String.valueOf(orderNo));
//			rentalv2Provider.updateRentalBill(bill);
			// save bill and online pay bill
			RentalOrderPayorderMap billmap = new RentalOrderPayorderMap();
	 
			billmap.setOrderId(cmd.getRentalBillId());
			billmap.setOrderNo(orderNo);
			billmap.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			billmap.setCreatorUid(userId);
			
			rentalv2Provider.createRentalBillPaybillMap(billmap);
			//保证没有attachments,才会去存
			List<RentalOrderAttachment>  attachments = rentalv2Provider.findRentalBillAttachmentByBillId(bill.getId());
			if((attachments == null || attachments.size()==0) && null != cmd.getRentalAttachments()){
				for(AttachmentDTO attachment : cmd.getRentalAttachments()){
					RentalOrderAttachment rba = new RentalOrderAttachment();
					rba.setRentalOrderId(cmd.getRentalBillId());
					rba.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
					rba.setCreatorUid(userId);
					rba.setAttachmentType(attachment.getAttachmentType());
					rba.setContent(attachment.getContent());
					this.rentalv2Provider.createRentalBillAttachment(rba);
					if (AttachmentType.RECOMMEND_USER.getCode().equals(attachment.getAttachmentType())) {
						List<RentalConfigAttachment> tempAttachments = rentalv2Provider
								.queryRentalConfigAttachmentByIds(attachment.getRecommendUsers());
						List<RentalRecommendUser> recommendUsers = tempAttachments.stream()
								.map(r -> ConvertHelper.convert(r, RentalRecommendUser.class)).collect(Collectors.toList());
						addRecommendUsers(recommendUsers, AttachmentType.ORDER_RECOMMEND_USER.name(), cmd.getRentalBillId());

					}else if (attachment.getAttachmentType().equals(AttachmentType.GOOD_ITEM.getCode())) {
						List<RentalConfigAttachment> tempAttachments = rentalv2Provider
								.queryRentalConfigAttachmentByIds(attachment.getGoodItems());
						List<RentalGoodItem> goodItems = tempAttachments.stream()
								.map(r -> ConvertHelper.convert(r, RentalGoodItem.class)).collect(Collectors.toList());
						addGoodItems(goodItems, AttachmentType.ORDER_GOOD_ITEM.name(), cmd.getRentalBillId());
					}
				}
			}
			//签名
			this.setSignatureParam(response);
			return null;
		});

		//用基于服务器平台的锁 验证线下支付 的剩余资源是否足够
		//线下模式和审批线上模式 都走工作流
		if(bill.getPayMode().equals(PayMode.OFFLINE_PAY.getCode())
				|| bill.getPayMode().equals(PayMode.APPROVE_ONLINE_PAY.getCode())) {
			this.coordinationProvider.getNamedLock(CoordinationLocks.CREATE_RENTAL_BILL.getCode()+bill.getRentalResourceId())
					.enter(() -> {
						List<RentalBillRuleDTO> rules = new ArrayList<>();
						//验证订单下的资源是否足够
						List<RentalResourceOrder> rsbs = rentalv2Provider
								.findRentalResourceOrderByOrderId(bill.getId());
						for(RentalResourceOrder rsb : rsbs){
							RentalBillRuleDTO dto = new RentalBillRuleDTO();
							dto.setRentalCount(rsb.getRentalCount());
							dto.setRuleId(rsb.getRentalResourceRuleId());
							rules.add(dto);					
						}
						this.valiRentalBill(rules);
						
						//线下支付要建立工作流
						FlowCase flowCase = this.createflowCase(bill);

						if (null != flowCase) {
							String url = processFlowURL(flowCase.getId(), FlowUserType.APPLIER.getCode(), flowCase.getModuleId());
							response.setFlowCaseUrl(url);
							bill.setFlowCaseId(flowCase.getId());
						}

						rentalv2Provider.updateRentalBill(bill);

						return null;
					}); 
		}else {
			rentalv2Provider.updateRentalBill(bill);

			if(bill.getStatus().equals(SiteBillStatus.SUCCESS.getCode())){
				onOrderSuccess(bill);
			}
		}
		// 客户端生成订单
		return response;
	}
	private static final String REFER_TYPE= FlowReferType.RENTAL.getCode();
	@Override
	public void onOrderCancel(RentalOrder order) {
		//终止工作流
		FlowCase flowcase = flowCaseProvider.findFlowCaseByReferId(order.getId(), REFER_TYPE, Rentalv2Controller.moduleId);
		if(null != flowcase ){
			FlowAutoStepDTO dto = new FlowAutoStepDTO();
			dto.setAutoStepType(FlowStepType.ABSORT_STEP.getCode());
			dto.setFlowCaseId(flowcase.getId());
			dto.setFlowMainId(flowcase.getFlowMainId());
			dto.setFlowNodeId(flowcase.getCurrentNodeId());
			dto.setFlowVersion(flowcase.getFlowVersion());
			dto.setStepCount(flowcase.getStepCount());
			this.flowService.processAutoStep(dto);
		}
		//发消息
		cancelOrderSendMessage(order);
	}
	@Override
	public void onOrderSuccess(RentalOrder order) {
		//加工作流
		createflowCase(order);
		//发消息给管理员
		addOrderSendMessage(order);
		//发短信在对接支付的handler  RentalOrderEmbeddedHandler //TODO:看是否需要移到这里
	}
	private FlowCase createflowCase(RentalOrder order){
		String moduleType = FlowModuleType.NO_MODULE.getCode();
		Long ownerId = order.getResourceTypeId();
		String ownerType = FlowOwnerType.RENTALRESOURCETYPE.getCode();
    	Flow flow = flowService.getEnabledFlow(order.getNamespaceId(), Rentalv2Controller.moduleId, moduleType, ownerId, ownerType);
    	LOGGER.debug("parames : " +order.getNamespaceId()+"*"+ Rentalv2Controller.moduleId+"*"+ moduleType+"*"+ ownerId+"*"+ ownerType );
    	LOGGER.debug("\n flow is "+flow);
    	if(null!=flow){
	    	CreateFlowCaseCommand cmd = new CreateFlowCaseCommand();
	    	cmd.setApplyUserId(order.getRentalUid());
	    	cmd.setFlowMainId(flow.getFlowMainId());
	    	cmd.setFlowVersion(flow.getFlowVersion());
	    	cmd.setReferId(order.getId());
	    	cmd.setReferType(REFER_TYPE);
	    	cmd.setProjectId(order.getCommunityId());
	    	cmd.setProjectType(EntityType.COMMUNITY.getCode());
	    	
	    	
	    	Map<String, String> map = new HashMap<>();
	        map.put("resourceName", order.getResourceName());
	        String useDetail = order.getUseDetail();
	        if(useDetail.contains("\n")){
	        	String[] splitUseDetail = useDetail.split("\n");
	        	useDetail = splitUseDetail[0]+"...";
	        }
	        map.put("useDetail", useDetail ); 
			String contentString = localeTemplateService.getLocaleTemplateString(RentalNotificationTemplateCode.FLOW_SCOPE, 
					RentalNotificationTemplateCode.RENTAL_FLOW_CONTENT, RentalNotificationTemplateCode.locale, map, "");
			RentalResourceType resourceType = rentalv2Provider.getRentalResourceTypeById(order.getResourceTypeId());
			cmd.setTitle(resourceType.getName());
	    	cmd.setContent(contentString);
//	    	LOGGER.debug("cmd = \n"+cmd);

	    	return flowService.createFlowCase(cmd);
    	}
    	return null;
	}
	private boolean valiItem(RentalItemsOrder rib) {

		RentalItem rSiteItem = this.rentalv2Provider.getRentalSiteItemById(rib.getRentalResourceItemId()); 
		List<RentalResourceOrder>  rentalSitesBills = this.rentalv2Provider.findRentalResourceOrderByOrderId(rib.getRentalOrderId());
		if(rSiteItem.getItemType().equals(RentalItemType.SALE.getCode())){
			Integer soldSum = this.rentalv2Provider.countRentalSiteItemSoldCount(rSiteItem.getId());
			//如果订单的商品总数加此次订单的数量超过了商品的总数
			if( rSiteItem.getCounts() < soldSum + rib.getRentalCount())
				return true;
		}
		else if(rSiteItem.getItemType().equals(RentalItemType.RENTAL.getCode())){
			//如果这个租用的 循环每一个单元格 
			for(RentalResourceOrder rentalSitesBill : rentalSitesBills){
				//由于商品订单不和单元格关联，所以要找到该单元格的所有订单
				List<Long> rentalBillIds = this.findRentalBillIdsByRuleId(rentalSitesBill.getRentalResourceRuleId());
				//查该单元格所有订单的预定商品总数
				Integer rentalSum = this.rentalv2Provider.countRentalSiteItemRentalCount(rentalBillIds);
				// 在单元格下租用的所有物品总数+此次订单租赁数，超过商品总数 则报异常
				if( rSiteItem.getCounts() < rentalSum + rib.getRentalCount())
					return true;
			}
		}
		return false;
	}
	
	private List<Long> findRentalBillIdsByRuleId(Long ruleId){
		List<Long> result = new ArrayList<>();
		List<RentalResourceOrder>  rentalSitesBills = this.rentalv2Provider.findRentalSiteBillBySiteRuleId(ruleId);
		for(RentalResourceOrder sitesBill : rentalSitesBills){
			result.add(sitesBill.getRentalOrderId());
		}
		return result;
		
	}
	
	
	private void setSignatureParam(AddRentalBillItemCommandResponse response) {
		String appKey = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"pay.appKey", "");
		Long timestamp = System.currentTimeMillis();
		Integer randomNum = (int) (Math.random()*1000);
		App app = appProvider.findAppByKey(appKey);
		if(app==null){
			LOGGER.error("app not found.key="+appKey);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"app not found.");
		}
		Map<String,String> map = new HashMap<>();
		map.put("appKey",appKey);
		map.put("timestamp",timestamp+"");
		map.put("randomNum",randomNum+"");
		map.put("amount",response.getAmount().doubleValue()+"");
		String signature = SignatureHelper.computeSignature(map, app.getSecretKey());
		response.setAppKey(appKey);
		response.setRandomNum(randomNum);
		response.setSignature(URLEncoder.encode(signature));
		response.setTimestamp(timestamp);
	}

	@Override
	public ListRentalBillsCommandResponse listRentalBills(ListRentalBillsCommand cmd) {
		ListRentalBillsCommandResponse response = new ListRentalBillsCommandResponse();
//		cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
//		int totalCount = rentalProvider.countRentalBills(cmd.getOwnerId(),cmd.getOwnerType(),
//				cmd.getSiteType(), cmd.getRentalSiteId(), cmd.getBillStatus(),
//				cmd.getStartTime(), cmd.getEndTime(), cmd.getInvoiceFlag(),null);
//		if (totalCount == 0)
//			return response;

		if(cmd.getPageAnchor() == null)
			cmd.setPageAnchor(Long.MAX_VALUE); 
		Integer pageSize = PaginationConfigHelper.getPageSize(
				configurationProvider, cmd.getPageSize()); 
//		checkEnterpriseCommunityIdIsNull(cmd.getOwnerId());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<RentalOrder> bills = rentalv2Provider.listRentalBills(cmd.getResourceTypeId(), cmd.getOrganizationId(), 
				cmd.getRentalSiteId(), locator, cmd.getBillStatus(), cmd.getVendorType(), pageSize+1, cmd.getStartTime(), cmd.getEndTime(),
				null, null);

		if (bills == null) {
			return response;
		}

		if(bills.size() > pageSize) {
			bills.remove(bills.size() - 1);
			response.setNextPageAnchor( bills.get(bills.size() -1).getId()); 
		}
		
		response.setRentalBills(new ArrayList<>());
		for (RentalOrder bill : bills) {
			// 在转换bill到dto的时候统一先convert一下  modify by wuhan 20160804
			RentalBillDTO dto = ConvertHelper.convert(bill, RentalBillDTO.class);
			mappingRentalBillDTO(dto, bill);
			List<RentalItemsOrder> rentalSiteItems = rentalv2Provider
					.findRentalItemsBillBySiteBillId(dto.getRentalBillId());
			if(null!=rentalSiteItems){
				dto.setSiteItems(new ArrayList<>());
				for (RentalItemsOrder rib : rentalSiteItems) {
					SiteItemDTO siDTO = new SiteItemDTO();
					siDTO.setCounts(rib.getRentalCount());
					RentalItem rsItem = rentalv2Provider.findRentalSiteItemById(rib.getRentalResourceItemId());
					if(rsItem != null) {
	    				siDTO.setItemName(rsItem.getName());
	    				siDTO.setItemPrice(rib.getTotalMoney());
	    				dto.getSiteItems().add(siDTO);
					} else {
					    LOGGER.error("Rental site item not found, rentalSiteItemId=" + rib.getRentalResourceItemId() + ", cmd=" + cmd);
					}
				}
			}
			response.getRentalBills().add(dto);
		}
 
		return response;
	}

	@Override
	public void deleteRentalBill(DeleteRentalBillCommand cmd) { 
//		rentalProvider.deleteRentalBillById(cmd.getRentalBillId());
		RentalOrder order = this.rentalv2Provider.findRentalBillById(cmd.getRentalBillId());
		if (null==order)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid   parameter in the command: order not found");
		if(!order.getRentalUid().equals(UserContext.current().getUser().getId()))
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_ACCESS_DENIED,
					"Permission denied");
		if(order.getStatus().equals(SiteBillStatus.LOCKED.getCode())||order.getStatus().equals(SiteBillStatus.PAYINGFINAL.getCode()))
			order.setStatus(SiteBillStatus.FAIL.getCode());
		order.setVisibleFlag(VisibleFlag.UNVISIBLE.getCode());
		this.rentalv2Provider.updateRentalBill(order);
	}
 

	@Override
	public OnlinePayCallbackCommandResponse onlinePayCallback(
			OnlinePayCallbackCommand cmd) {
		// 
		OnlinePayCallbackCommandResponse response = new OnlinePayCallbackCommandResponse();
		if(cmd.getPayStatus().toLowerCase().equals("fail")) {
			 
			LOGGER.info(" ----------------- - - - PAY FAIL command is "+cmd.toString());
		}
			
		//success
		if(cmd.getPayStatus().toLowerCase().equals("success"))
		{
			RentalOrderPayorderMap bpbMap= rentalv2Provider.findRentalBillPaybillMapByOrderNo(cmd.getOrderNo());
			RentalOrder bill = rentalv2Provider.findRentalBillById(bpbMap.getOrderId());
			bill.setPaidMoney(bill.getPaidMoney().add(new java.math.BigDecimal(cmd.getPayAmount())));
			bill.setVendorType(cmd.getVendorType());
			bpbMap.setVendorType(cmd.getVendorType());
			bill.setOperateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));

			bpbMap.setOperateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
//			bill.setOperatorUid(UserContext.current().getUser().getId());
			if(bill.getStatus().equals(SiteBillStatus.LOCKED.getCode())){
				bill.setStatus(SiteBillStatus.RESERVED.getCode());
			}
			else if(bill.getStatus().equals(SiteBillStatus.PAYINGFINAL.getCode())){
				if(bill.getPayTotalMoney().equals(bill.getPaidMoney())){
					bill.setStatus(SiteBillStatus.SUCCESS.getCode());
					UserIdentifier userIdentifier = this.userProvider.findClaimedIdentifierByOwnerAndType(bill.getCreatorUid(), IdentifierType.MOBILE.getCode()) ;
					if(null == userIdentifier){
						LOGGER.error("userIdentifier is null...userId = " + bill.getCreatorUid());
					}else{
						sendRentalSuccessSms(bill.getNamespaceId(),userIdentifier.getIdentifierToken(), bill); 
					}
				}
				else{
					LOGGER.error("待付款订单:id ["+bill.getId()+"]付款金额有问题： 应该付款金额："+bill.getPayTotalMoney()+"实际付款金额："+bill.getPaidMoney());

					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
		                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter : price is not right!");  
				}
			}else if(bill.getStatus().equals(SiteBillStatus.SUCCESS.getCode())){
				LOGGER.error("待付款订单:id ["+bill.getId()+"] 状态已经是成功预约");
			}else{
				LOGGER.error("待付款订单:id ["+bill.getId()+"]状态有问题： 订单状态是："+bill.getStatus());
			}
			rentalv2Provider.updateRentalBill(bill);
		}
		return response;
	}

	@Override
	public FindRentalSiteMonthStatusCommandResponse findRentalSiteMonthStatus(
			FindRentalSiteMonthStatusCommand cmd) {
		RentalResource rs = this.rentalv2Provider.getRentalSiteById(cmd.getSiteId());
		proccessCells(rs);
		FindRentalSiteMonthStatusCommandResponse response = ConvertHelper.convert(rs, FindRentalSiteMonthStatusCommandResponse.class);
		response.setRentalSiteId(rs.getId());
		List<RentalResourcePic> pics = this.rentalv2Provider.findRentalSitePicsByOwnerTypeAndId(EhRentalv2Resources.class.getSimpleName(), rs.getId());
		response.setSitePics(convertRentalSitePicDTOs(pics));

		response.setAnchorTime(0L);

		List<RentalConfigAttachment> attachments=this.rentalv2Provider.queryRentalConfigAttachmentByOwner(EhRentalv2Resources.class.getSimpleName(),rs.getId());
		response.setAttachments(convertAttachments(attachments));

		// 查rules
		
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.setTime(new Date(cmd.getRuleDate()));
		end.setTime(new Date(cmd.getRuleDate()));
		//start 这个月第一天
		start.set(Calendar.DAY_OF_MONTH,1);
		//end 下个月的第一天
		end.add(Calendar.MONTH, 1);
		end.set(Calendar.DAY_OF_MONTH,1);
//		end.add(Calendar.DAY_OF_YEAR, 7);
		response.setSiteDays(new ArrayList<>());

		processDayRuleDTOs(start, end, response.getSiteDays(), cmd.getSiteId(), rs, response.getAnchorTime(), cmd.getSceneToken());
		return response;
	}
	
	@Override
	public FindRentalSiteWeekStatusCommandResponse findRentalSiteWeekStatus(FindRentalSiteWeekStatusCommand cmd) {
		
		RentalResource rs = this.rentalv2Provider.getRentalSiteById(cmd.getSiteId());
		proccessCells(rs);
		FindRentalSiteWeekStatusCommandResponse response = ConvertHelper.convert(rs, FindRentalSiteWeekStatusCommandResponse.class);
		response.setRentalSiteId(rs.getId());
		List<RentalResourcePic> pics = this.rentalv2Provider.findRentalSitePicsByOwnerTypeAndId(EhRentalv2Resources.class.getSimpleName(), rs.getId());
		response.setSitePics(convertRentalSitePicDTOs(pics));

		response.setAnchorTime(0L);

		List<RentalConfigAttachment> attachments=this.rentalv2Provider.queryRentalConfigAttachmentByOwner(EhRentalv2Resources.class.getSimpleName(),rs.getId());
		response.setAttachments(convertAttachments(attachments));

		// 查rules
		
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.setTime(new Date(cmd.getRuleDate()));
		end.setTime(new Date(cmd.getRuleDate()));
		start.set(Calendar.DAY_OF_WEEK,
				start.getActualMinimum(Calendar.DAY_OF_WEEK));
		end.set(Calendar.DAY_OF_WEEK,
				start.getActualMinimum(Calendar.DAY_OF_WEEK));
		end.add(Calendar.DAY_OF_YEAR, 7);
		response.setSiteDays(new ArrayList<>());

		processDayRuleDTOs(start, end, response.getSiteDays(), cmd.getSiteId(), rs, response.getAnchorTime(), cmd.getSceneToken());
		//按小时预订的,给客户端找到每一个时间点
		if(rs.getRentalType().equals(RentalType.HOUR.getCode())){
			List<RentalTimeInterval> timeIntervals = this.rentalv2Provider.queryRentalTimeIntervalByOwner(EhRentalv2Resources.class.getSimpleName(),rs.getId());

			List<Long> dayTimes = calculateOrdinateList(timeIntervals);
	 		Collections.sort(dayTimes);
	 		response.setDayTimes(dayTimes);
 		}
		return response;
	}

	private List<Long> calculateOrdinateList(List<RentalTimeInterval> timeIntervals) {
		List<Long> dayTimes = new ArrayList<>();

		if (null != timeIntervals) {
			for(RentalTimeInterval timeInterval : timeIntervals){
				Long dayTimeBegin = Timestamp.valueOf(dateSF.get().format(new java.util.Date())
						+ " "
						+ String.valueOf(timeInterval.getBeginTime().intValue())
						+ ":"
						+ String.valueOf((int) (( timeInterval.getBeginTime() % 1) * 60))
						+ ":00").getTime();
				if(!dayTimes.contains(dayTimeBegin))
					dayTimes.add(dayTimeBegin);

				for (double i = timeInterval.getBeginTime(); i < timeInterval.getEndTime();) {

					i = i + timeInterval.getTimeStep();
					Long dayTimeEnd = Timestamp.valueOf(dateSF.get().format(new java.util.Date())
							+ " "
							+ String.valueOf((int) i)
							+ ":"
							+ String.valueOf((int) (( i % 1) * 60))
							+ ":00").getTime();
					if(!dayTimes.contains(dayTimeEnd))
						dayTimes.add(dayTimeEnd);

				}
			}
		}

		return dayTimes;
	}

	private List<RentalSitePicDTO> convertRentalSitePicDTOs(List<RentalResourcePic> pics) {
		if (null != pics) {
			return pics.stream().map(p -> {
				RentalSitePicDTO picDTO=ConvertHelper.convert(p, RentalSitePicDTO.class);
				picDTO.setUrl(this.contentServerService.parserUri(p.getUri(),
						EntityType.USER.getCode(), UserContext.current().getUser().getId()));
				return picDTO;
			}).collect(Collectors.toList());
		}
		return new ArrayList<>();
	}

	private void processDayRuleDTOs(Calendar start ,Calendar end , List<RentalSiteDayRulesDTO> dtos,Long siteId ,
			RentalResource rs, Long anchorTime, String sceneToken){

		java.util.Date reserveTime = new java.util.Date(); 
//		java.util.Date nowTime = new java.util.Date();
//		response.setContactNum(rs.getContactPhonenum());
		Timestamp beginTime = new Timestamp(reserveTime.getTime()
				+ rs.getRentalStartTime());

		//解析场景信息
		SceneTokenDTO sceneTokenDTO = null;
		if (null != sceneToken) {
			User user = UserContext.current().getUser();
			sceneTokenDTO = userService.checkSceneToken(user.getId(), sceneToken);
		}

		for(;start.before(end);start.add(Calendar.DAY_OF_YEAR, 1)){
			RentalSiteDayRulesDTO dayDto = new RentalSiteDayRulesDTO();
			dtos.add(dayDto);
			dayDto.setSiteRules(new ArrayList<>());
			dayDto.setRentalDate(start.getTimeInMillis());
			List<RentalCell> rentalSiteRules = findRentalSiteRules(siteId, dateSF.get().format(new java.util.Date(start.getTimeInMillis())),
					beginTime, rs.getRentalType()==null?RentalType.DAY.getCode():rs.getRentalType(), DateLength.DAY.getCode(),
					RentalSiteStatus.NORMAL.getCode(), rs.getRentalStartTimeFlag());
			// 查sitebills
			if (null != rentalSiteRules && rentalSiteRules.size() > 0) {
				for (RentalCell rsr : rentalSiteRules) {
					RentalSiteRulesDTO dto =ConvertHelper.convert(rsr, RentalSiteRulesDTO.class);

					//根据场景来设置价格
					setRentalsiteRulePrice(sceneTokenDTO, dto);

					dto.setId(rsr.getId()); 
					if (dto.getRentalType().equals(RentalType.HOUR.getCode())) {
						dto.setTimeStep(rsr.getTimeStep());
						dto.setBeginTime(rsr.getBeginTime().getTime());
						dto.setEndTime(rsr.getEndTime().getTime());
						if(anchorTime.equals(0L)){
							anchorTime = dto.getBeginTime();
						}else{
							try {
								if(timeSF.get().parse(timeSF.get().format(new java.util.Date(anchorTime))).after(
										timeSF.get().parse(timeSF.get().format(new java.util.Date(dto.getBeginTime()))))){
									anchorTime = dto.getBeginTime();
								}
							} catch (Exception e) {
								LOGGER.error("anchorTime error  dto = "+ dto );
							}
							
						}
					} else if (dto.getRentalType() == RentalType.HALFDAY.getCode() ||
							dto.getRentalType() == RentalType.THREETIMEADAY.getCode()) {
						dto.setAmorpm(rsr.getAmorpm());
					} 
					dto.setRuleDate(rsr.getResourceRentalDate().getTime());
					List<RentalResourceOrder> rsbs = rentalv2Provider
							.findRentalSiteBillBySiteRuleId(rsr.getId());
					dto.setStatus(SiteRuleStatus.OPEN.getCode());
					dto.setCounts((double) rsr.getCounts());
					if (null != rsbs && rsbs.size() > 0) {
						for (RentalResourceOrder rsb : rsbs) {
							dto.setCounts(dto.getCounts()
									- rsb.getRentalCount());
						}
					}
					//根据时间判断来设置status
					setRentalCellStatus(reserveTime, dto, rsr, rs);

					if (dto.getCounts() == 0 || rsr.getStatus().equals((byte)-1)) {
						dto.setStatus(SiteRuleStatus.CLOSE.getCode());
					}
					dayDto.getSiteRules().add(dto);
	
				}
			}
		}
	}

	private void setRentalsiteRulePrice(SceneTokenDTO sceneTokenDTO, RentalSiteRulesDTO dto){
		//目前非认证用户，不能预订，后续功能让非认证用户可以使用预订之后
		if (null != sceneTokenDTO) {
			String scene = sceneTokenDTO.getScene();

			if (SceneType.PM_ADMIN.getCode().equals(scene)) {
				dto.setPrice(dto.getOrgMemberPrice());
				dto.setOriginalPrice(dto.getOrgMemberOriginalPrice());
			}
		}
	}


	@Override
	public FindAutoAssignRentalSiteWeekStatusResponse findAutoAssignRentalSiteWeekStatus(
			FindAutoAssignRentalSiteWeekStatusCommand cmd) {

		java.util.Date reserveTime = new java.util.Date(); 
		
		RentalResource rs = this.rentalv2Provider.getRentalSiteById(cmd.getSiteId());
		proccessCells(rs);
		FindAutoAssignRentalSiteWeekStatusResponse response = ConvertHelper.convert(rs, FindAutoAssignRentalSiteWeekStatusResponse.class);
		//场所数量和编号
		response.setSiteCounts(rs.getResourceCounts());
		List<RentalResourceNumber> resourceNumbers = this.rentalv2Provider.queryRentalResourceNumbersByOwner(
				EhRentalv2Resources.class.getSimpleName(),rs.getId());
		if(null!=resourceNumbers){
			response.setSiteNames(new ArrayList<String>());
			for(RentalResourceNumber number:resourceNumbers){
				response.getSiteNames().add( number.getResourceNumber());
			}
		} 
		response.setRentalSiteId(rs.getId());
		List<RentalResourcePic> pics = this.rentalv2Provider.findRentalSitePicsByOwnerTypeAndId(EhRentalv2Resources.class.getSimpleName(), rs.getId());
		response.setSitePics(convertRentalSitePicDTOs(pics));

		response.setAnchorTime(0L);


		List<RentalConfigAttachment> attachments=this.rentalv2Provider.queryRentalConfigAttachmentByOwner(EhRentalv2Resources.class.getSimpleName(),rs.getId());
		response.setAttachments(convertAttachments(attachments));

		// 查rules
		
		java.util.Date nowTime = new java.util.Date();
//		response.setContactNum(rs.getContactPhonenum());
		Timestamp beginTime = new Timestamp(nowTime.getTime()
				+ rs.getRentalStartTime());
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.setTime(new Date(cmd.getRuleDate()));
		end.setTime(new Date(cmd.getRuleDate()));
		start.set(Calendar.DAY_OF_WEEK,
				start.getActualMinimum(Calendar.DAY_OF_WEEK));
		end.set(Calendar.DAY_OF_WEEK,
				start.getActualMinimum(Calendar.DAY_OF_WEEK));
		end.add(Calendar.DAY_OF_YEAR, 7);
		response.setSiteDays(new ArrayList<RentalSiteNumberDayRulesDTO>());
		for(;start.before(end);start.add(Calendar.DAY_OF_YEAR, 1)){
			RentalSiteNumberDayRulesDTO dayDto = new RentalSiteNumberDayRulesDTO();
			response.getSiteDays().add(dayDto);
			Map<String,List<RentalSiteRulesDTO>> siteNumberMap=new HashMap<>();
			for(RentalResourceNumber resourceNumber :resourceNumbers){
				siteNumberMap.put(resourceNumber.getResourceNumber(), new ArrayList<RentalSiteRulesDTO>());
			}
			dayDto.setSiteNumbers(new ArrayList<RentalSiteNumberRuleDTO>()); 
			dayDto.setRentalDate(start.getTimeInMillis());
			List<RentalCell> rentalSiteRules = findRentalSiteRules(cmd.getSiteId(), dateSF.get().format(new java.util.Date(start.getTimeInMillis())),
							beginTime, rs.getRentalType()==null?RentalType.DAY.getCode():rs.getRentalType(), DateLength.DAY.getCode(),
					RentalSiteStatus.NORMAL.getCode(), rs.getRentalStartTimeFlag());
			// 查sitebills
			if (null != rentalSiteRules && rentalSiteRules.size() > 0) {
				for (RentalCell rsr : rentalSiteRules) {
					RentalSiteRulesDTO dto =ConvertHelper.convert(rsr, RentalSiteRulesDTO.class);
					dto.setSiteNumber(String.valueOf(rsr.getResourceNumber()));
					dto.setId(rsr.getId()); 
					if (dto.getRentalType().equals(RentalType.HOUR.getCode())) {
						dto.setTimeStep(rsr.getTimeStep());
						dto.setBeginTime(rsr.getBeginTime().getTime());
						dto.setEndTime(rsr.getEndTime().getTime());
						if(response.getAnchorTime().equals(0L)){
							response.setAnchorTime(dto.getBeginTime());
						}else{
							try {
								if(timeSF.get().parse(timeSF.get().format(new java.util.Date(response.getAnchorTime()))).after(
										timeSF.get().parse(timeSF.get().format(new java.util.Date(dto.getBeginTime()))))){
									response.setAnchorTime(dto.getBeginTime());
								}
							} catch (Exception e) {
								LOGGER.error("anchorTime error  dto = "+ dto );
							}
							
							
						}
					} else if (dto.getRentalType() == RentalType.HALFDAY.getCode()
							|| dto.getRentalType() == RentalType.THREETIMEADAY.getCode()) {
						dto.setAmorpm(rsr.getAmorpm());
					} 
					dto.setRuleDate(rsr.getResourceRentalDate().getTime());
					List<RentalResourceOrder> rsbs = rentalv2Provider
							.findRentalSiteBillBySiteRuleId(rsr.getId());
					dto.setStatus(SiteRuleStatus.OPEN.getCode());
					dto.setCounts((double) rsr.getCounts());
					if (null != rsbs && rsbs.size() > 0) {
						for (RentalResourceOrder rsb : rsbs) {
							dto.setCounts(dto.getCounts()
									- rsb.getRentalCount());
						}
					}
					if (dto.getRentalType().equals(RentalType.HOUR.getCode())) {
						if ((NormalFlag.NEED.getCode() == rs.getRentalStartTimeFlag())&&(reserveTime.before(new java.util.Date(rsr
								.getBeginTime().getTime()
								- rs.getRentalStartTime())))) {
							dto.setStatus(SiteRuleStatus.EARLY.getCode());
						}
						if ((NormalFlag.NEED.getCode() == rs.getRentalEndTimeFlag())&&(reserveTime.after(new java.util.Date(rsr
								.getBeginTime().getTime()
								- rs.getRentalEndTime())))) {
							dto.setStatus(SiteRuleStatus.LATE.getCode());
						}
					}  else{
						Long dayBeginTime = 0l; 
						if(rsr.getAmorpm() != null){
							if(rsr.getAmorpm().equals(AmorpmFlag.AM.getCode())){
								dayBeginTime = 10*60*60*1000L; 
							}else if(rsr.getAmorpm().equals(AmorpmFlag.PM.getCode())){
								dayBeginTime = 15*60*60*1000L; 
							}else if(rsr.getAmorpm().equals(AmorpmFlag.NIGHT.getCode())){
								dayBeginTime = 20*60*60*1000L; 
							}
						}
						if ((NormalFlag.NEED.getCode() == rs.getRentalStartTimeFlag())&&(reserveTime.before(new java.util.Date(rsr
								.getResourceRentalDate().getTime()+dayBeginTime
								- rs.getRentalStartTime())))) {
							dto.setStatus(SiteRuleStatus.EARLY.getCode());
						}
						if ((NormalFlag.NEED.getCode() == rs.getRentalEndTimeFlag())&&(reserveTime.after(new java.util.Date(rsr
								.getResourceRentalDate().getTime()+dayBeginTime
								- rs.getRentalEndTime()))) ){
							dto.setStatus(SiteRuleStatus.LATE.getCode());
						}
					}

					if (dto.getCounts() == 0 || rsr.getStatus().equals((byte)-1)) {
						dto.setStatus(SiteRuleStatus.CLOSE.getCode());
					}
					if(siteNumberMap.get(dto.getSiteNumber())==null)
						siteNumberMap.put(dto.getSiteNumber(), new ArrayList<RentalSiteRulesDTO>());
					siteNumberMap.get(dto.getSiteNumber()).add(dto);
				}
			}
			
			//
			for(String siteNumber : response.getSiteNames()){
				RentalSiteNumberRuleDTO siteNumberRuleDTO = new RentalSiteNumberRuleDTO();
				siteNumberRuleDTO.setSiteNumber(siteNumber);
				siteNumberRuleDTO.setSiteRules(siteNumberMap.get(siteNumber));
				dayDto.getSiteNumbers().add(siteNumberRuleDTO);
			}
		}
//		//即使单元格为空也会有days不用担心空指针
//		response.setSiteCounts(response.getSiteDays().get(0).getSiteNumbers().size());
		return response;
	}

	@Override
	public FindAutoAssignRentalSiteMonthStatusResponse findAutoAssignRentalSiteMonthStatus(
			FindAutoAssignRentalSiteMonthStatusCommand cmd) {

		java.util.Date reserveTime = new java.util.Date(); 
		
		RentalResource rs = this.rentalv2Provider.getRentalSiteById(cmd.getSiteId());
		proccessCells(rs);
		FindAutoAssignRentalSiteMonthStatusResponse response = ConvertHelper.convert(rs, FindAutoAssignRentalSiteMonthStatusResponse.class);
		//场所数量和编号
		response.setSiteCounts(rs.getResourceCounts());
		List<RentalResourceNumber> resourceNumbers = this.rentalv2Provider.queryRentalResourceNumbersByOwner(
				EhRentalv2Resources.class.getSimpleName(),rs.getId());
		if(null!=resourceNumbers){
			response.setSiteNames(new ArrayList<String>());
			for(RentalResourceNumber number:resourceNumbers){
				response.getSiteNames().add( number.getResourceNumber());
			}
		} 
		response.setRentalSiteId(rs.getId());
		List<RentalResourcePic> pics = this.rentalv2Provider.findRentalSitePicsByOwnerTypeAndId(EhRentalv2Resources.class.getSimpleName(), rs.getId());
		response.setSitePics(convertRentalSitePicDTOs(pics));

		response.setAnchorTime(0L);

		List<RentalConfigAttachment> attachments=this.rentalv2Provider.queryRentalConfigAttachmentByOwner(EhRentalv2Resources.class.getSimpleName(),rs.getId());
		response.setAttachments(convertAttachments(attachments));

		// 查rules
		
		java.util.Date nowTime = new java.util.Date();
//		response.setContactNum(rs.getContactPhonenum());
		Timestamp beginTime = new Timestamp(nowTime.getTime()
				+ rs.getRentalStartTime());
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.setTime(new Date(cmd.getRuleDate()));
		end.setTime(new Date(cmd.getRuleDate()));
		start.set(Calendar.DAY_OF_MONTH,
				start.getActualMinimum(Calendar.DAY_OF_MONTH));
		end.set(Calendar.DAY_OF_MONTH,
				start.getActualMinimum(Calendar.DAY_OF_MONTH));
		end.add(Calendar.MONTH, 1);
		response.setSiteDays(new ArrayList<>());

		//解析场景信息
		SceneTokenDTO sceneTokenDTO = null;
		if (null != cmd.getSceneToken()) {
			User user = UserContext.current().getUser();
			sceneTokenDTO = userService.checkSceneToken(user.getId(), cmd.getSceneToken());
		}

		for(;start.before(end);start.add(Calendar.DAY_OF_YEAR, 1)){
			RentalSiteNumberDayRulesDTO dayDto = new RentalSiteNumberDayRulesDTO();
			response.getSiteDays().add(dayDto);
			Map<String,List<RentalSiteRulesDTO>> siteNumberMap=new HashMap<>();
			for(RentalResourceNumber resourceNumber :resourceNumbers){
				siteNumberMap.put(resourceNumber.getResourceNumber(), new ArrayList<RentalSiteRulesDTO>());
			}
			dayDto.setSiteNumbers(new ArrayList<RentalSiteNumberRuleDTO>()); 
			dayDto.setRentalDate(start.getTimeInMillis());
			List<RentalCell> rentalSiteRules = findRentalSiteRules(cmd.getSiteId(), dateSF.get().format(new java.util.Date(start.getTimeInMillis())),
							beginTime, rs.getRentalType()==null?RentalType.DAY.getCode():rs.getRentalType(), DateLength.DAY.getCode(),
					RentalSiteStatus.NORMAL.getCode(), rs.getRentalStartTimeFlag());
			// 查sitebills
				if (null != rentalSiteRules && rentalSiteRules.size() > 0) {
					for (RentalCell rsr : rentalSiteRules) {
						RentalSiteRulesDTO dto =ConvertHelper.convert(rsr, RentalSiteRulesDTO.class);
						dto.setSiteNumber(String.valueOf(rsr.getResourceNumber()));
						dto.setId(rsr.getId());
						//根据场景设置价格
						setRentalsiteRulePrice(sceneTokenDTO, dto);

						if (dto.getRentalType().equals(RentalType.HOUR.getCode())) {
							dto.setTimeStep(rsr.getTimeStep());
							dto.setBeginTime(rsr.getBeginTime().getTime());
							dto.setEndTime(rsr.getEndTime().getTime());
							if(response.getAnchorTime().equals(0L)){
								response.setAnchorTime(dto.getBeginTime());
							}else{
								try {
									if(timeSF.get().parse(timeSF.get().format(new java.util.Date(response.getAnchorTime()))).after(
											timeSF.get().parse(timeSF.get().format(new java.util.Date(dto.getBeginTime()))))){
										response.setAnchorTime(dto.getBeginTime());
									}
								} catch (Exception e) {
									LOGGER.error("anchorTime error  dto = "+ dto );
								}
							}
						} else if (dto.getRentalType() == RentalType.HALFDAY.getCode()
								|| dto.getRentalType() == RentalType.THREETIMEADAY.getCode()) {
							dto.setAmorpm(rsr.getAmorpm());
						}
						dto.setRuleDate(rsr.getResourceRentalDate().getTime());
						List<RentalResourceOrder> rsbs = rentalv2Provider
								.findRentalSiteBillBySiteRuleId(rsr.getId());
						dto.setStatus(SiteRuleStatus.OPEN.getCode());
						dto.setCounts((double) rsr.getCounts());
						if (null != rsbs && rsbs.size() > 0) {
							for (RentalResourceOrder rsb : rsbs) {
								dto.setCounts(dto.getCounts()
										- rsb.getRentalCount());
							}
						}

						//根据时间判断来设置status
						setRentalCellStatus(reserveTime, dto, rsr, rs);

						if (dto.getCounts() == 0 || rsr.getStatus().equals((byte)-1)) {
							dto.setStatus(SiteRuleStatus.CLOSE.getCode());
						}
						if(siteNumberMap.get(dto.getSiteNumber())==null)
							siteNumberMap.put(dto.getSiteNumber(), new ArrayList<RentalSiteRulesDTO>());
						siteNumberMap.get(dto.getSiteNumber()).add(dto);
					}
				}

				//
				for(String siteNumber : response.getSiteNames()){
					RentalSiteNumberRuleDTO siteNumberRuleDTO = new RentalSiteNumberRuleDTO();
					siteNumberRuleDTO.setSiteNumber(siteNumber);
					siteNumberRuleDTO.setSiteRules(siteNumberMap.get(siteNumber));
					dayDto.getSiteNumbers().add(siteNumberRuleDTO);
				}

		}
//		//即使单元格为空也会有days不用担心空指针
//		response.setSiteCounts(response.getSiteDays().get(0).getSiteNumbers().size());
		return response;
	}

	@Override
	public FindAutoAssignRentalSiteDayStatusResponse findAutoAssignRentalSiteDayStatus(
			FindAutoAssignRentalSiteDayStatusCommand cmd) {
		java.util.Date reserveTime = new java.util.Date(); 
		
		RentalResource rs = this.rentalv2Provider.getRentalSiteById(cmd.getSiteId());
		proccessCells(rs);
		FindAutoAssignRentalSiteDayStatusResponse response = ConvertHelper.convert(rs, FindAutoAssignRentalSiteDayStatusResponse.class);
		response.setRentalSiteId(rs.getId());
		List<RentalResourcePic> pics = this.rentalv2Provider.findRentalSitePicsByOwnerTypeAndId(EhRentalv2Resources.class.getSimpleName(), rs.getId());
		response.setSitePics(convertRentalSitePicDTOs(pics));

		List<RentalConfigAttachment> attachments=this.rentalv2Provider.queryRentalConfigAttachmentByOwner(EhRentalv2Resources.class.getSimpleName(),rs.getId());
		response.setAttachments(convertAttachments(attachments));

		response.setAnchorTime(0L);
		//当前时间+预定开始时间 即为可预订开始时间
		java.util.Date nowTime = new java.util.Date();
		Timestamp beginTime = new Timestamp(nowTime.getTime()
				+ rs.getRentalStartTime());
		response.setSiteCounts(rs.getResourceCounts());
		List<RentalResourceNumber> resourceNumbers = this.rentalv2Provider.queryRentalResourceNumbersByOwner(
				EhRentalv2Resources.class.getSimpleName(),rs.getId());
		if(null!=resourceNumbers){
			response.setSiteNames(new ArrayList<String>());
			for(RentalResourceNumber number:resourceNumbers){
				response.getSiteNames().add( number.getResourceNumber());
			}
		}

		//解析场景信息
		SceneTokenDTO sceneTokenDTO = null;
		if (null != cmd.getSceneToken()) {
			User user = UserContext.current().getUser();
			sceneTokenDTO = userService.checkSceneToken(user.getId(), cmd.getSceneToken());
		}

		// 查rules
		Map<String,List<RentalSiteRulesDTO>> siteNumberMap=new HashMap<>();
		for(RentalResourceNumber resourceNumber :resourceNumbers){
			siteNumberMap.put(resourceNumber.getResourceNumber(), new ArrayList<>());
		}
		response.setSiteNumbers(new ArrayList<>());
		//按小时预订的,给客户端找到每一个时间点
		List<RentalTimeInterval> timeIntervals = this.rentalv2Provider.queryRentalTimeIntervalByOwner(EhRentalv2Resources.class.getSimpleName(),rs.getId());
		List<Long> dayTimes = calculateOrdinateList(timeIntervals);
		
		List<RentalCell> rentalSiteRules =  findRentalSiteRules(cmd.getSiteId(), dateSF.get().format(new java.util.Date(cmd.getRuleDate() )),
						beginTime, rs.getRentalType()==null?RentalType.DAY.getCode():rs.getRentalType(), DateLength.DAY.getCode(),
				RentalSiteStatus.NORMAL.getCode(), rs.getRentalStartTimeFlag());
		// 查sitebills
		if (null != rentalSiteRules && rentalSiteRules.size() > 0) {
			for (RentalCell rsr : rentalSiteRules) {
				RentalSiteRulesDTO dto =ConvertHelper.convert(rsr, RentalSiteRulesDTO.class);

				//根据场景设置价格
				setRentalsiteRulePrice(sceneTokenDTO, dto);

				dto.setSiteNumber(String.valueOf(rsr.getResourceNumber() ));
				dto.setId(rsr.getId()); 
				if (dto.getRentalType().equals(RentalType.HOUR.getCode())) {
					dto.setTimeStep(rsr.getTimeStep());
					dto.setBeginTime(rsr.getBeginTime().getTime());
					dto.setEndTime(rsr.getEndTime().getTime()); 
					if(response.getAnchorTime().equals(0L)){
						response.setAnchorTime(dto.getBeginTime());
					}else{
						try {
							if(timeSF.get().parse(timeSF.get().format(new java.util.Date(response.getAnchorTime()))).after(
									timeSF.get().parse(timeSF.get().format(new java.util.Date(dto.getBeginTime()))))){
								response.setAnchorTime(dto.getBeginTime());
							}
						} catch (Exception e) {
							LOGGER.error("anchorTime error  dto = "+ dto );
						}
						
						
					}
				} else if (dto.getRentalType() == RentalType.HALFDAY.getCode()
						|| dto.getRentalType() == RentalType.THREETIMEADAY.getCode()) {
					dto.setAmorpm(rsr.getAmorpm());
				} 
				dto.setRuleDate(rsr.getResourceRentalDate().getTime());
				List<RentalResourceOrder> rsbs = rentalv2Provider
						.findRentalSiteBillBySiteRuleId(rsr.getId());
				dto.setStatus(SiteRuleStatus.OPEN.getCode());
				dto.setCounts((double) rsr.getCounts());
				if (null != rsbs && rsbs.size() > 0) {
					for (RentalResourceOrder rsb : rsbs) {
						dto.setCounts(dto.getCounts()
								- rsb.getRentalCount());
					}
				}
				//根据时间判断来设置status
				setRentalCellStatus(reserveTime, dto, rsr, rs);

				if (dto.getCounts() == 0 || rsr.getStatus().equals((byte)-1)) {
					dto.setStatus(SiteRuleStatus.CLOSE.getCode());
				}
				if(siteNumberMap.get(dto.getSiteNumber())==null)
					siteNumberMap.put(dto.getSiteNumber(), new ArrayList<>());
				siteNumberMap.get(dto.getSiteNumber()).add(dto);
			}
		}
		
		//
		for(String siteNumber : response.getSiteNames()){
			RentalSiteNumberRuleDTO siteNumberRuleDTO = new RentalSiteNumberRuleDTO();
			siteNumberRuleDTO.setSiteNumber(siteNumber);
			siteNumberRuleDTO.setSiteRules(siteNumberMap.get(siteNumber));
			response.getSiteNumbers().add(siteNumberRuleDTO);
		}
		Collections.sort(dayTimes);
 		response.setDayTimes(dayTimes);
//		//即使单元格为空也会有sitenumbers不用担心空指针
//		response.setSiteCounts(response.getSiteNumbers().size());
		return response;
	}

	private void setRentalCellStatus(java.util.Date reserveTime, RentalSiteRulesDTO dto, RentalCell rsr, RentalResource rs) {
		if (dto.getRentalType().equals(RentalType.HOUR.getCode())) {
			Long time = rsr.getBeginTime().getTime();
			if (NormalFlag.NEED.getCode() == rs.getRentalStartTimeFlag()) {
				time -= rs.getRentalStartTime();
				if (reserveTime.before(new java.util.Date(time))) {
					dto.setStatus(SiteRuleStatus.EARLY.getCode());
				}
			}

			if (NormalFlag.NEED.getCode() == rs.getRentalEndTimeFlag()) {
				time -= rs.getRentalEndTime();
			}
			if (reserveTime.after(new java.util.Date(time))) {
				dto.setStatus(SiteRuleStatus.LATE.getCode());
			}
		} else {

			Long dayBeginTime = 0l;
			if(rsr.getAmorpm() != null){
				String halfOwnerType = RentalTimeIntervalOwnerType.RESOURCE_HALF_DAY.getCode();

				List<RentalTimeInterval> halfTimeIntervals = rentalv2Provider.queryRentalTimeIntervalByOwner(halfOwnerType, rs.getId());
				//当有晚上时，如果查询出来的条数不是3，就有数据错误的问题，默认按顺序存和取
				int size = halfTimeIntervals.size();
				if(rsr.getAmorpm().equals(AmorpmFlag.AM.getCode())){
					if (size >= 1) {
						dayBeginTime = (long)(halfTimeIntervals.get(0).getBeginTime() * 60 * 60 * 1000);
					}
				}else if(rsr.getAmorpm().equals(AmorpmFlag.PM.getCode())){
					if (size >= 2) {
						dayBeginTime = (long)(halfTimeIntervals.get(1).getBeginTime() * 60 * 60 * 1000);
					}
				}else if(rsr.getAmorpm().equals(AmorpmFlag.NIGHT.getCode())){
					if (size >= 3) {
						dayBeginTime = (long) (halfTimeIntervals.get(2).getBeginTime() * 60 * 60 * 1000);
					}
				}
			}

			Long time = rsr.getResourceRentalDate().getTime() + dayBeginTime;
			if (NormalFlag.NEED.getCode() == rs.getRentalStartTimeFlag()) {
				time -= rs.getRentalStartTime();
				if (reserveTime.before(new java.util.Date(time))) {
					dto.setStatus(SiteRuleStatus.EARLY.getCode());
				}
			}

			if (NormalFlag.NEED.getCode() == rs.getRentalEndTimeFlag()) {
				time -= rs.getRentalEndTime();
			}
			if (reserveTime.after(new java.util.Date(time))) {
				dto.setStatus(SiteRuleStatus.LATE.getCode());
			}
		}
	}

	@Override
	public RentalBillDTO confirmBill(ConfirmBillCommand cmd) {
		RentalOrder bill = this.rentalv2Provider.findRentalBillById(cmd.getRentalBillId());
		if (bill.getStatus().equals(SiteBillStatus.FAIL.getCode())){
			throw RuntimeErrorException
			.errorWith(
					RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_RESERVE_TOO_LATE,
							"too late to order the service"); 
		}
		if (Double.valueOf(0.0).equals(bill.getPayTotalMoney().doubleValue())){
			bill.setStatus(SiteBillStatus.SUCCESS.getCode());
			rentalv2Provider.updateRentalBill(bill);
		}
		else {

			throw RuntimeErrorException
			.errorWith(
					RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_DID_NOT_PAY,
							"did not pay for the bill ,can not confirm"); 
		}
		// 在转换bill到dto的时候统一先convert一下  modify by wuhan 20160804
		RentalBillDTO dto = ConvertHelper.convert(bill, RentalBillDTO.class);
		mappingRentalBillDTO(dto, bill);
		return dto;
	}

	@Override
	public RentalBillDTO completeBill(CompleteBillCommand cmd) {
		RentalOrder bill = this.rentalv2Provider.findRentalBillById(cmd.getRentalBillId());
		if (!bill.getStatus().equals(SiteBillStatus.SUCCESS.getCode())
				&&!bill.getStatus().equals(SiteBillStatus.OVERTIME.getCode())){
			throw RuntimeErrorException
			.errorWith(RentalServiceErrorCode.SCOPE,RentalServiceErrorCode.ERROR_NOT_SUCCESS, "order is not success order."); 
		} 
		bill.setStatus(SiteBillStatus.COMPLETE.getCode());
		rentalv2Provider.updateRentalBill(bill);
		// 在转换bill到dto的时候统一先convert一下  modify by wuhan 20160804
		RentalBillDTO dto = ConvertHelper.convert(bill, RentalBillDTO.class);
		mappingRentalBillDTO(dto, bill);
		return dto;
	}

	@Override
	public RentalBillDTO incompleteBill(IncompleteBillCommand cmd) {
		RentalOrder bill = this.rentalv2Provider.findRentalBillById(cmd.getRentalBillId());
		if (!bill.getStatus().equals(SiteBillStatus.COMPLETE.getCode())){
			throw RuntimeErrorException
			.errorWith(RentalServiceErrorCode.SCOPE,RentalServiceErrorCode.ERROR_NOT_COMPLETE,"order is not complete order."); 
		} 
//		RentalRule rule = this.rentalProvider.getRentalRule(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getSiteType());
		RentalResource rs = this.rentalv2Provider.getRentalSiteById(bill.getRentalResourceId());
		java.util.Date cancelTime = new java.util.Date();
		if (cancelTime.before(new java.util.Date(bill.getEndTime().getTime()))) {
			bill.setStatus(SiteBillStatus.SUCCESS.getCode());
		}else{
			bill.setStatus(SiteBillStatus.OVERTIME.getCode());
		}
		
		rentalv2Provider.updateRentalBill(bill);
		// 在转换bill到dto的时候统一先convert一下  modify by wuhan 20160804
		RentalBillDTO dto = ConvertHelper.convert(bill, RentalBillDTO.class);
		mappingRentalBillDTO(dto, bill);
		return dto;
	}

	@Override
	public BatchCompleteBillCommandResponse batchIncompleteBill(BatchIncompleteBillCommand cmd) { 
		BatchCompleteBillCommandResponse response = new BatchCompleteBillCommandResponse();
		response.setBills(new ArrayList<>());
		if(null!=cmd.getRentalBillIds())
			for (Long billId : cmd.getRentalBillIds()){
				IncompleteBillCommand cmd2 = ConvertHelper.convert(cmd, IncompleteBillCommand.class);
				cmd2.setRentalBillId(billId);
				response.getBills().add(this.incompleteBill(cmd2));
			}
		return response;
	}

	@Override
	public BatchCompleteBillCommandResponse batchCompleteBill(BatchCompleteBillCommand cmd) {
		BatchCompleteBillCommandResponse response = new BatchCompleteBillCommandResponse();
		response.setBills(new ArrayList<>());
		if(null!=cmd.getRentalBillIds())
			for (Long billId : cmd.getRentalBillIds()){
				CompleteBillCommand cmd2 = ConvertHelper.convert(cmd, CompleteBillCommand.class);
				cmd2.setRentalBillId(billId);
				response.getBills().add(this.completeBill(cmd2));
			}
		return response;
	}
	
	@Override
	public ListRentalBillCountCommandResponse listRentalBillCount(ListRentalBillCountCommand cmd) {
		ListRentalBillCountCommandResponse response = new ListRentalBillCountCommandResponse(); 
//		response.setRentalBillCounts(new ArrayList<RentalBillCountDTO>());
//		if(cmd.getRentalSiteId() == null ){
//			List<RentalSite>  sites = this.rentalProvider.findRentalSites(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getSiteType(),null , null, null,null);
//			for(RentalSite site : sites){
//				response.getRentalBillCounts().add(processRentalBillCountDTO(site,cmd.getBeginDate(),cmd.getEndDate()));
//			}
//		}
//		else{
//			RentalSite site = this.rentalProvider.getRentalSiteById(cmd.getRentalSiteId());
//			response.getRentalBillCounts().add(processRentalBillCountDTO(site,cmd.getBeginDate(),cmd.getEndDate()));
//			
//		}
		
		return response;
	}

	@Override
	public HttpServletResponse exportRentalBills(ListRentalBillsCommand cmd,
			HttpServletResponse response) {
		
//		int totalCount = rentalProvider.countRentalBills(cmd.getOwnerId(),cmd.getOwnerType(),
//				cmd.getSiteType(), cmd.getRentalSiteId(), cmd.getBillStatus(),
//				cmd.getStartTime(), cmd.getEndTime(), cmd.getInvoiceFlag(),null);
//		if (totalCount == 0)
//			return response;

		Integer pageSize = Integer.MAX_VALUE; 
		List<RentalOrder> bills = rentalv2Provider.listRentalBills(cmd.getResourceTypeId(), cmd.getOrganizationId(), 
				cmd.getRentalSiteId(), new CrossShardListingLocator(), cmd.getBillStatus(), cmd.getVendorType(), pageSize, cmd.getStartTime(), cmd.getEndTime(),
				null, null); 
		if(null == bills){
			bills = new ArrayList<>();
		}
		List<RentalBillDTO> dtos = new ArrayList<>();
		for (RentalOrder bill : bills) {
			// 在转换bill到dto的时候统一先convert一下  modify by wuhan 20160804
			RentalBillDTO dto = ConvertHelper.convert(bill, RentalBillDTO.class);
			mappingRentalBillDTO(dto, bill);
			dto.setSiteItems(new ArrayList<>());
			List<RentalItemsOrder> rentalSiteItems = rentalv2Provider
					.findRentalItemsBillBySiteBillId(dto.getRentalBillId());
			if(null != rentalSiteItems) 
				for (RentalItemsOrder rib : rentalSiteItems) {
					SiteItemDTO siDTO = new SiteItemDTO();
					siDTO.setCounts(rib.getRentalCount());
//					RentalItem rsItem = rentalProvider
//							.findRentalSiteItemById(rib.getRentalResourceItemId());
//					if(null != rsItem){
//						siDTO.setItemName(rsItem.getName());
//					}
					siDTO.setItemName(rib.getItemName());
					siDTO.setItemPrice(rib.getTotalMoney());
					dto.getSiteItems().add(siDTO);
				}
			
			dtos.add(dto);
		}
		
		URL rootPath = Rentalv2ServiceImpl.class.getResource("/");
		String filePath =rootPath.getPath() + downloadDir ;
		File file = new File(filePath);
		if(!file.exists())
			file.mkdirs();
		filePath = filePath + "RentalBills"+System.currentTimeMillis()+".xlsx";
		//新建了一个文件
		this.createRentalBillsBook(filePath, dtos);
		
		return download(filePath,response);
	}
	
	public HttpServletResponse download(String path, HttpServletResponse response) {
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = file.getName();
            // 取得文件的后缀名。
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
            
            // 读取完成删除文件
            if (file.isFile() && file.exists()) {  
                file.delete();  
            } 
        } catch (IOException ex) { 
 			LOGGER.error(ex.getMessage());
 			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
 					RentalServiceErrorCode.ERROR_DOWNLOAD_EXCEL,
 					ex.getLocalizedMessage());
     		 
        }
        return response;
    }
	
	private void createRentalBillsBook(String path,List<RentalBillDTO> dtos) {
		if (null == dtos || dtos.size() == 0)
			return;
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("rentalBill");
		
		this.createRentalBillsBookSheetHead(sheet);
		for (RentalBillDTO dto : dtos ) {
			this.setNewRentalBillsBookRow(sheet, dto);
		}
		
		try {
			FileOutputStream out = new FileOutputStream(path);
			
			wb.write(out);
			wb.close();
			out.close();
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_CREATE_EXCEL,
					e.getLocalizedMessage());
		}
	}
	
	private void createRentalBillsBookSheetHead(Sheet sheet){

		Row row = sheet.createRow(sheet.getLastRowNum());
		int i =-1 ;
		row.createCell(++i).setCellValue("序号");
		row.createCell(++i).setCellValue("名称");
		row.createCell(++i).setCellValue("下单时间");
		row.createCell(++i).setCellValue("使用详情");
		row.createCell(++i).setCellValue("预订人"); 
		row.createCell(++i).setCellValue("总价");
		row.createCell(++i).setCellValue("支付方式");
		row.createCell(++i).setCellValue("订单状态");
	}
	
	private void setNewRentalBillsBookRow(Sheet sheet ,RentalBillDTO dto){
		Row row = sheet.createRow(sheet.getLastRowNum()+1);
		int i = -1;
		//序号
		row.createCell(++i).setCellValue(row.getRowNum());
		//名称 - 资源名称
		row.createCell(++i).setCellValue(dto.getSiteName());
		//下单时间 
		if(null!=dto.getReserveTime())
			row.createCell(++i).setCellValue(datetimeSF.get().format(new Timestamp(dto.getReserveTime())));
		else 
			row.createCell(++i).setCellValue("");
		//使用详情
		row.createCell(++i).setCellValue(dto.getUseDetail());
		//预约人 
		row.createCell(++i).setCellValue(dto.getUserName());
		//总价 
		if(null != dto.getTotalPrice())
			row.createCell(++i).setCellValue(dto.getTotalPrice().toString());
		else
			row.createCell(++i).setCellValue("0");
		//支付方式
		if(null != dto.getVendorType())
			row.createCell(++i).setCellValue(VendorType.fromCode(dto.getVendorType()).getDescribe());
		else
			row.createCell(++i).setCellValue("");
		//订单状态
		if(dto.getStatus() != null)
			row.createCell(++i).setCellValue(statusToString(dto.getStatus()));
		else
			row.createCell(++i).setCellValue("");
		 
	} 
	private String statusToString(Byte status) {

		SiteBillStatus siteBillStatus = SiteBillStatus.fromCode(status);
		return null != siteBillStatus ? siteBillStatus.getDescribe() : "";
	}

	@Override
	public GetResourceListAdminResponse getResourceList(GetResourceListAdminCommand cmd) {
		GetResourceListAdminResponse response = new GetResourceListAdminResponse();
		if(null==cmd.getOrganizationId())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid organizationId parameter in the command");
		if(null==cmd.getResourceTypeId())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid launchPadItemId parameter in the command");

		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());

		List<Long> siteIds = null;
		if(null != cmd.getOwnerType()){
			siteIds = new ArrayList<>();
			List<RentalSiteRange> siteOwners = this.rentalv2Provider.findRentalSiteOwnersByOwnerTypeAndId(cmd.getOwnerType(), cmd.getOwnerId());
			if(siteOwners !=null)
				for(RentalSiteRange siteOwner : siteOwners){
					siteIds.add(siteOwner.getRentalResourceId());
				}   
		}
		List<RentalResource> rentalSites = rentalv2Provider.findRentalSites(cmd.getResourceTypeId(), null,
				locator, pageSize,null,siteIds,cmd.getCommunityId());
		if(null == rentalSites)
			return response;

		Long nextPageAnchor = null;
		if(rentalSites.size() > pageSize) {
			rentalSites.remove(rentalSites.size() - 1);
			nextPageAnchor = rentalSites.get(rentalSites.size() -1).getDefaultOrder();
		}
		response.setNextPageAnchor(nextPageAnchor);
		response.setRentalSites(rentalSites.stream().map(r -> convertRentalSite2DTO(r, null))
			.collect(Collectors.toList()));

		return response;
	}

	@Override
	public RentalSiteDTO findRentalSiteById(FindRentalSiteByIdCommand cmd) {
		if(null == cmd.getId())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid organizationId parameter in the command");

		RentalResource rentalSite = rentalv2Provider.getRentalSiteById(cmd.getId());
		SceneTokenDTO sceneTokenDTO = null;
		if (null != cmd.getSceneToken()) {
			User user = UserContext.current().getUser();
			sceneTokenDTO = userService.checkSceneToken(user.getId(), cmd.getSceneToken());
		}
		return convertRentalSite2DTO(rentalSite, sceneTokenDTO);
	}

	@Override
	public void addResource(AddResourceAdminCommand cmd) {
		if(null==cmd.getOrganizationId())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid organizationId parameter in the command");
		if(null==cmd.getCommunityId())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid getCommunityId parameter in the command");
		if(null==cmd.getResourceTypeId())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid launchPadItemId parameter in the command");
		RentalResourceType type = this.rentalv2Provider.getRentalResourceTypeById(cmd.getResourceTypeId());
		if(PayMode.OFFLINE_PAY.getCode().equals(type.getPayMode())&&(null==cmd.getOfflineCashierAddress()||null==cmd.getOfflinePayeeUid())){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Neither offline cashier address nor payee uid can  be null");
		}
		
		this.dbProvider.execute((TransactionStatus status) -> {
			RentalResource resource = ConvertHelper.convert(cmd, RentalResource.class);
			resource.setResourceName(cmd.getSiteName());
			resource.setStatus(RentalSiteStatus.DISABLE.getCode());
			resource.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			resource.setCreatorUid( UserContext.current().getUser().getId());
			QueryDefaultRuleAdminCommand cmd2 = new QueryDefaultRuleAdminCommand();
			cmd2.setOwnerType(RentalOwnerType.ORGANIZATION.getCode());
			cmd2.setOwnerId(resource.getOrganizationId());
			cmd2.setResourceTypeId(resource.getResourceTypeId());
			QueryDefaultRuleAdminResponse defaultRule = this.queryDefaultRule(cmd2); 

			
			if (null == defaultRule){
				cmd2.setOwnerType(RentalOwnerType.COMMUNITY.getCode());
				cmd2.setOwnerId(resource.getCommunityId());
				defaultRule = this.queryDefaultRule(cmd2); 
			} 
			if (null == defaultRule)
				throw RuntimeErrorException
				.errorWith(RentalServiceErrorCode.SCOPE,RentalServiceErrorCode.ERROR_DEFAULT_RULE_NOTFOUND, "didnt have default rule!");
			resource.setExclusiveFlag(defaultRule.getExclusiveFlag());
			if(defaultRule.getExclusiveFlag().equals(NormalFlag.NEED.getCode())){
				defaultRule.setUnit(1.0);
				defaultRule.setAutoAssign(NormalFlag.NONEED.getCode());
				defaultRule.setMultiUnit(NormalFlag.NONEED.getCode());
				defaultRule.setSiteCounts(1.0);
			}
			if(null == defaultRule.getAutoAssign())
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
	                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter AutoAssign   is null");
			resource.setResourceCounts(defaultRule.getSiteCounts());
			resource.setStatus(RentalSiteStatus.NORMAL.getCode());
			resource.setAutoAssign(defaultRule.getAutoAssign());
			resource.setMultiUnit(defaultRule.getMultiUnit());
			resource.setNeedPay(defaultRule.getNeedPay());

			resource.setMultiTimeInterval(defaultRule.getMultiTimeInterval());
			resource.setRentalType(defaultRule.getRentalType());
			resource.setRentalEndTimeFlag(defaultRule.getRentalEndTimeFlag());
			resource.setRentalStartTimeFlag(defaultRule.getRentalStartTimeFlag());
			resource.setRentalEndTime(defaultRule.getRentalEndTime());
			resource.setRentalStartTime(defaultRule.getRentalStartTime());
			resource.setTimeStep(defaultRule.getTimeStep());
			resource.setCancelTime(defaultRule.getCancelTime());
			resource.setRefundFlag(defaultRule.getRefundFlag());
			resource.setRefundRatio(defaultRule.getRefundRatio());
			resource.setUnit(defaultRule.getUnit());
			resource.setBeginDate(new Date(defaultRule.getBeginDate()));
			resource.setEndDate(new Date(defaultRule.getEndDate()));

			resource.setOpenWeekday(convertOpenWeekday(defaultRule.getOpenWeekday()));
			resource.setWorkdayPrice(defaultRule.getWorkdayPrice());
			resource.setWeekendPrice(defaultRule.getWeekendPrice());
			resource.setApprovingUserWeekendPrice(defaultRule.getApprovingUserWeekendPrice());
			resource.setApprovingUserWorkdayPrice(defaultRule.getApprovingUserWorkdayPrice());
			resource.setOrgMemberWeekendPrice(defaultRule.getOrgMemberWeekendPrice());
			resource.setOrgMemberWorkdayPrice(defaultRule.getOrgMemberWorkdayPrice());

			BigDecimal weekendPrice = defaultRule.getWeekendPrice() == null ? new BigDecimal(0) : defaultRule.getWeekendPrice(); 
			BigDecimal workdayPrice = defaultRule.getWorkdayPrice() == null ? new BigDecimal(0) : defaultRule.getWorkdayPrice();
			List<AddRentalSiteSingleSimpleRule> addSingleRules =new ArrayList<>();
			if (defaultRule.getRentalType().equals(RentalType.HOUR.getCode()))  {
				if(defaultRule.getTimeIntervals() != null){
					Double beginTime = null;
					Double endTime = null;
					for(TimeIntervalDTO timeInterval:defaultRule.getTimeIntervals()){
						 
						if(timeInterval.getBeginTime() == null || timeInterval.getEndTime()==null)
							continue;
						if(beginTime==null||beginTime>timeInterval.getBeginTime())
							beginTime=timeInterval.getBeginTime();
						if(endTime==null||endTime<timeInterval.getEndTime())
							endTime=timeInterval.getEndTime();
						AddRentalSiteSingleSimpleRule signleCmd=ConvertHelper.convert(defaultRule, AddRentalSiteSingleSimpleRule.class );
						signleCmd.setBeginTime(timeInterval.getBeginTime());
						signleCmd.setEndTime(timeInterval.getEndTime()); 
						if(null!=timeInterval.getTimeStep())
							signleCmd.setTimeStep(timeInterval.getTimeStep());
						signleCmd.setWeekendPrice(weekendPrice); 
						signleCmd.setWorkdayPrice(workdayPrice);
						addSingleRules.add(signleCmd);
					}

					if(endTime>24.0||beginTime<0.0)
						throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
				                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter of timeInterval  >24 or <0"); 
					resource.setDayBeginTime( convertTime((long) (beginTime*1000*60*60L)));
					resource.setDayEndTime(convertTime((long) (endTime*1000*60*60L)));
				}
			}else {
				AddRentalSiteSingleSimpleRule signleCmd=ConvertHelper.convert(defaultRule, AddRentalSiteSingleSimpleRule.class ); 
				signleCmd.setWeekendPrice(weekendPrice); 
				signleCmd.setWorkdayPrice(workdayPrice);
				addSingleRules.add(signleCmd);
			}

			seqNum.set(0L);
			currentId.set(sequenceProvider.getCurrentSequence(NameMapper.getSequenceDomainFromTablePojo(EhRentalv2Cells.class)) );
			for(AddRentalSiteSingleSimpleRule signleCmd : addSingleRules){
				//在这里统一处理 
				signleCmd.setRentalSiteId(resource.getId()); 
				addRentalSiteSingleSimpleRule(signleCmd );
			}
			

			Long cellBeginId = sequenceProvider.getNextSequenceBlock(NameMapper
					.getSequenceDomainFromTablePojo(EhRentalv2Cells.class), seqNum.get());
			if(LOGGER.isDebugEnabled()) {
	            LOGGER.debug("eh rental cells get next sequence block, id=" + cellBeginId+",block count = "+ seqNum.get()); 
	            LOGGER.debug("eh rental cells current id =" +  sequenceProvider.getCurrentSequence(NameMapper
						.getSequenceDomainFromTablePojo(EhRentalv2Cells.class))); 
	            LOGGER.debug("eh rental cells  , begin id=" + cellList.get().get(0).getId()+
	            		",last id  = "+ cellList.get().get(cellList.get().size()-1).getId()); 
	            
	        } 
			resource.setCellBeginId(cellBeginId);
			resource.setCellEndId(cellBeginId+seqNum.get());
			
			Long siteId = rentalv2Provider.createRentalSite(resource);

			
			//建立了resource之后才有id 
			if(defaultRule.getAutoAssign().equals(NormalFlag.NEED.getCode())){
				HashSet<String> siteNumberSet = new HashSet<>();
				if(defaultRule.getSiteCounts().intValue() == defaultRule.getSiteNumbers().size()){
					if( null!=defaultRule.getSiteNumbers())
						for(SiteNumberDTO number : defaultRule.getSiteNumbers()){
							siteNumberSet.add(number.getSiteNumber());
							RentalResourceNumber resourceNumber = new RentalResourceNumber();
							resourceNumber.setOwnerType(EhRentalv2Resources.class.getSimpleName());
							resourceNumber.setOwnerId(siteId);
							resourceNumber.setResourceNumber(number.getSiteNumber());
							resourceNumber.setNumberGroup(number.getSiteNumberGroup());
							resourceNumber.setGroupLockFlag(number.getGroupLockFlag());
							this.rentalv2Provider.createRentalResourceNumber(resourceNumber);
						}
				}
				else
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
		                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter site counts is "+defaultRule.getSiteCounts()+".but site numbers size is "+defaultRule.getSiteNumbers().size());
				if(defaultRule.getSiteCounts().intValue() !=siteNumberSet.size())
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
	                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter  site numbers repeat " );
					
			} 
			if(cmd.getOwners() != null){
				for(SiteOwnerDTO dto:cmd.getOwners()){
					RentalSiteRange siteOwner = ConvertHelper.convert(dto, RentalSiteRange.class);
					siteOwner.setRentalResourceId(siteId);
					this.rentalv2Provider.createRentalSiteOwner(siteOwner);
				}
			}
			if(cmd.getDetailUris() != null){
				for(String uri:cmd.getDetailUris()){
					RentalResourcePic detailPic =new RentalResourcePic();
					detailPic.setOwnerType(EhRentalv2Resources.class.getSimpleName());
					detailPic.setOwnerId(siteId);
					detailPic.setUri(uri);
					this.rentalv2Provider.createRentalSitePic(detailPic);
				}
			}

			createRentalConfigAttachment(defaultRule.getAttachments(), resource.getId(), EhRentalv2Resources.class.getSimpleName());

			//close dates
			setRentalRuleCloseDates(defaultRule.getCloseDates(), resource.getId(), EhRentalv2Resources.class.getSimpleName());

			setRentalRuleTimeIntervals(EhRentalv2Resources.class.getSimpleName(), resource.getId(), defaultRule.getTimeIntervals());
			setRentalRuleTimeIntervals(RentalTimeIntervalOwnerType.RESOURCE_HALF_DAY.getCode(), resource.getId(), defaultRule.getHalfDayTimeIntervals());

			return null;
		});
	}

	@Override
	public void updateResource(UpdateResourceAdminCommand cmd) {
		
		this.dbProvider.execute((TransactionStatus status) -> {
			RentalResource rentalsite = this.rentalv2Provider.getRentalSiteById(cmd.getId()); 

			RentalResourceType type = this.rentalv2Provider.getRentalResourceTypeById(rentalsite.getResourceTypeId());
			if(PayMode.OFFLINE_PAY.getCode().equals(type.getPayMode())&&(null==cmd.getOfflineCashierAddress()||null==cmd.getOfflinePayeeUid())){
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
						ErrorCodes.ERROR_INVALID_PARAMETER,
						"Neither offline cashier address nor payee uid can  be null");
			}
			
			//TODO: 权限校验
			rentalsite.setResourceName(cmd.getSiteName());
			rentalsite.setSpec(cmd.getSpec());
			rentalsite.setAddress(cmd.getAddress());
			rentalsite.setLatitude(cmd.getLatitude());
			rentalsite.setLongitude(cmd.getLongitude());
			rentalsite.setCommunityId(cmd.getCommunityId());
			rentalsite.setContactPhonenum(cmd.getContactPhonenum());
			rentalsite.setIntroduction(cmd.getIntroduction());
			rentalsite.setNotice(cmd.getNotice()); 
			rentalsite.setChargeUid(cmd.getChargeUid());
			rentalsite.setCoverUri(cmd.getCoverUri());
			rentalsite.setStatus(cmd.getStatus());
			rentalsite.setOfflineCashierAddress(cmd.getOfflineCashierAddress());
			rentalsite.setOfflinePayeeUid(cmd.getOfflinePayeeUid());
			rentalsite.setConfirmationPrompt(cmd.getConfirmationPrompt());
			rentalv2Provider.updateRentalSite(rentalsite);
			this.rentalv2Provider.deleteRentalSitePicsBySiteId(cmd.getId());
			this.rentalv2Provider.deleteRentalSiteOwnersBySiteId(cmd.getId());
			if(cmd.getOwners() != null){
				for(SiteOwnerDTO dto:cmd.getOwners()){
					RentalSiteRange siteOwner = ConvertHelper.convert(dto, RentalSiteRange.class);
					siteOwner.setRentalResourceId(cmd.getId());
					this.rentalv2Provider.createRentalSiteOwner(siteOwner);
				}
			}
			if(cmd.getDetailUris() != null){
				for(String uri:cmd.getDetailUris()){
					RentalResourcePic detailPic =new RentalResourcePic();
					detailPic.setOwnerType(EhRentalv2Resources.class.getSimpleName());
					detailPic.setOwnerId(cmd.getId());
					detailPic.setUri(uri);
					this.rentalv2Provider.createRentalSitePic(detailPic);
				}
			}
			return null;
		});
	}

	@Override
	public void updateItem(UpdateItemAdminCommand cmd) {
		if(null==cmd.getItemType())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid ItemType   parameter in the command : null");
		RentalItem siteItem =this.rentalv2Provider.getRentalSiteItemById(cmd.getId());
		 
		if(cmd.getItemType().equals(RentalItemType.SALE.getCode())){
			//设置的是库存，存储的是总量还要库存+已售
			Integer sumInteger = this.rentalv2Provider.countRentalSiteItemSoldCount(cmd.getId()); 
			siteItem.setCounts(cmd.getCounts()+sumInteger);
			}
		else
			siteItem.setCounts(cmd.getCounts());
			
		siteItem.setDefaultOrder(cmd.getDefaultOrder());
		siteItem.setImgUri(cmd.getImgUri());
		siteItem.setItemType(cmd.getItemType());
		siteItem.setName(cmd.getItemName());
		siteItem.setPrice(cmd.getItemPrice());
		rentalv2Provider.updateRentalSiteItem(siteItem);
	}

	@Override
	public void updateItems(UpdateItemsAdminCommand cmd) {
		this.dbProvider.execute((TransactionStatus status) -> {
			for(SiteItemDTO dto : cmd.getItemDTOs()){
				UpdateItemAdminCommand cmd2 = ConvertHelper.convert(dto, UpdateItemAdminCommand.class); 
				updateItem(cmd2);
			}
			
			return null;
		});
		
	}

	private void updateRSRs(List<RentalCell> changeRentalSiteRules, UpdateRentalSiteRulesAdminCommand cmd){
		if(null==changeRentalSiteRules || changeRentalSiteRules.size()==0)
			return;
		for(RentalCell cell : changeRentalSiteRules){
			cell.setPrice(cmd.getPrice());
			cell.setOriginalPrice(cmd.getOriginalPrice());
			cell.setHalfresourcePrice(cmd.getHalfsitePrice());
			cell.setHalfresourceOriginalPrice(cmd.getHalfsiteOriginalPrice());
			cell.setOrgMemberPrice(cmd.getOrgMemberPrice());
			cell.setOrgMemberOriginalPrice(cmd.getOrgMemberOriginalPrice());
			cell.setApprovingUserPrice(cmd.getApprovingUserPrice());
			cell.setApprovingUserOriginalPrice(cmd.getApprovingUserOriginalPrice());
			cell.setStatus(cmd.getStatus());
			cell.setCounts(cmd.getCounts());
			RentalCell dbCell = this.rentalv2Provider.getRentalCellById(cell.getId());
			if(null == dbCell)
				this.rentalv2Provider.createRentalSiteRule(cell);
			else
				this.rentalv2Provider.updateRentalSiteRule(cell);
		}
	}
	@Override
	public void updateRentalSiteSimpleRules(
			UpdateRentalSiteRulesAdminCommand cmd) { 
		RentalResource rs=this.rentalv2Provider.getRentalSiteById(cmd.getResourceId());
		if(rs==null)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"rental resource (site) cannot found ");
		proccessCells(rs);
		if(null==cmd.getRuleId())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid ruleId   parameter in the command");
		RentalCell choseRSR = findRentalSiteRuleById(cmd.getRuleId());
		if(null==choseRSR)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid ruleId   parameter in the command");
		
		
		if(null!=rs.getAutoAssign() && rs.getAutoAssign().equals(NormalFlag.NEED.getCode())){
			cmd.setCounts(1.0);
		}
		this.dbProvider.execute((TransactionStatus status) -> {
			List<RentalCell> changeRentalSiteRules= null;
			if(cmd.getLoopType().equals(LoopType.ONLYTHEDAY.getCode())){
			//当天的
			
				if(rs.getRentalType().equals(RentalType.HOUR.getCode()))
					{
						//按小时
						Timestamp beginTime = Timestamp.valueOf(dateSF.get().format(choseRSR.getBeginTime().getTime())+ " "
								+ String.valueOf( cmd.getBeginTime().intValue())
								+ ":"
								+ String.valueOf((int) ((cmd.getBeginTime() % 1) * 60))
								+ ":00");
						Timestamp endTime = Timestamp.valueOf(dateSF.get().format(choseRSR.getBeginTime().getTime())+ " "
								+ String.valueOf(cmd.getEndTime().intValue())
								+ ":"
								+ String.valueOf((int) ((cmd.getEndTime() % 1) * 60))
								+ ":00");
						changeRentalSiteRules = findRentalSiteRuleByDate(choseRSR.getRentalResourceId(),choseRSR.getResourceNumber(),beginTime,endTime,
								null, dateSF.get().format(choseRSR.getResourceRentalDate()));
					}
				
				else if(rs.getRentalType().equals(RentalType.HALFDAY.getCode())){
					List<Byte> ampmList = new ArrayList<>();
					//0早上1下午2晚上
					if(AmorpmFlag.AM.getCode()>=cmd.getBeginTime()&&AmorpmFlag.AM.getCode()<=cmd.getEndTime())
						ampmList.add(AmorpmFlag.AM.getCode());
					if(AmorpmFlag.PM.getCode()>=cmd.getBeginTime()&&AmorpmFlag.PM.getCode()<=cmd.getEndTime())
						ampmList.add(AmorpmFlag.PM.getCode());
					
					changeRentalSiteRules = findRentalSiteRuleByDate(choseRSR.getRentalResourceId(),choseRSR.getResourceNumber(),null,null,
							ampmList,dateSF.get().format(choseRSR.getResourceRentalDate()));
				}
				else if(rs.getRentalType().equals(RentalType.THREETIMEADAY.getCode())){
					List<Byte> ampmList = new ArrayList<>();
					//0早上1下午2晚上
					if(AmorpmFlag.AM.getCode()>=cmd.getBeginTime()&&AmorpmFlag.AM.getCode()<=cmd.getEndTime())
						ampmList.add(AmorpmFlag.AM.getCode());
					if(AmorpmFlag.PM.getCode()>=cmd.getBeginTime()&&AmorpmFlag.PM.getCode()<=cmd.getEndTime())
						ampmList.add(AmorpmFlag.PM.getCode());
					if(AmorpmFlag.NIGHT.getCode()>=cmd.getBeginTime()&&AmorpmFlag.NIGHT.getCode()<=cmd.getEndTime())
						ampmList.add(AmorpmFlag.NIGHT.getCode());
					changeRentalSiteRules = findRentalSiteRuleByDate(choseRSR.getRentalResourceId(),choseRSR.getResourceNumber(),null,null,
							ampmList, dateSF.get().format(choseRSR.getResourceRentalDate()));
				}
				else if(rs.getRentalType().equals(RentalType.DAY.getCode())){
					 
					changeRentalSiteRules = findRentalSiteRuleByDate(choseRSR.getRentalResourceId(),choseRSR.getResourceNumber(),null,null,
							null, dateSF.get().format(choseRSR.getResourceRentalDate()));
				}
			
				updateRSRs(changeRentalSiteRules, cmd);
			}else {
				//需要循环的
				Calendar chooseCalendar = Calendar.getInstance();
				Calendar start = Calendar.getInstance();
				Calendar end = Calendar.getInstance();
				chooseCalendar.setTime(new Date(choseRSR.getResourceRentalDate().getTime()));
				 
				start.setTime(new Date(cmd.getBeginDate()));
				end.setTime(new Date(cmd.getEndDate()));
				
				for (;!start.after(end);start.add(Calendar.DAY_OF_MONTH, 1)) {
					Integer weekday = start.get(Calendar.DAY_OF_WEEK);
					Integer monthDay = start.get(Calendar.DAY_OF_MONTH);
					//按周循环的,如果不对就继续循环	
					if(cmd.getLoopType().equals(LoopType.EVERYWEEK.getCode())&&
							!weekday.equals(chooseCalendar.get(Calendar.DAY_OF_WEEK) ))
						continue;
					//按月循环的，如果不对就继续循环
					if(cmd.getLoopType().equals(LoopType.EVERYMONTH.getCode())&&
							!monthDay.equals( chooseCalendar.get(Calendar.DAY_OF_MONTH) ))
						continue;
					//每天循环的
						if(rs.getRentalType().equals(RentalType.HOUR.getCode())){
						//按小时
						Timestamp beginTime = Timestamp.valueOf(dateSF.get().format(start.getTime().getTime())+ " "
								+ String.valueOf( cmd.getBeginTime().intValue())
								+ ":"
								+ String.valueOf((int) ((cmd.getBeginTime() % 1) * 60))
								+ ":00");
						Timestamp endTime = Timestamp.valueOf(dateSF.get().format(start.getTime().getTime())+ " "
								+ String.valueOf(cmd.getEndTime().intValue())
								+ ":"
								+ String.valueOf((int) ((cmd.getEndTime() % 1) * 60))
								+ ":00");
						changeRentalSiteRules = findRentalSiteRuleByDate(choseRSR.getRentalResourceId(),choseRSR.getResourceNumber(),beginTime,endTime,
								null,null);
					}
					else if(rs.getRentalType().equals(RentalType.HALFDAY.getCode())){
						List<Byte> ampmList = new ArrayList<>();
						//0早上1下午2晚上
						if(AmorpmFlag.AM.getCode()>=cmd.getBeginTime()&&AmorpmFlag.AM.getCode()<=cmd.getEndTime())
							ampmList.add(AmorpmFlag.AM.getCode());
						if(AmorpmFlag.PM.getCode()>=cmd.getBeginTime()&&AmorpmFlag.PM.getCode()<=cmd.getEndTime())
							ampmList.add(AmorpmFlag.PM.getCode());
						changeRentalSiteRules = findRentalSiteRuleByDate(choseRSR.getRentalResourceId(),choseRSR.getResourceNumber(),null,null,
								ampmList, dateSF.get().format(new java.util.Date(start.getTimeInMillis())));
					}
					else if(rs.getRentalType().equals(RentalType.THREETIMEADAY.getCode())){
						List<Byte> ampmList = new ArrayList<>();
						//0早上1下午2晚上
						if(AmorpmFlag.AM.getCode()>=cmd.getBeginTime()&&AmorpmFlag.AM.getCode()<=cmd.getEndTime())
							ampmList.add(AmorpmFlag.AM.getCode());
						if(AmorpmFlag.PM.getCode()>=cmd.getBeginTime()&&AmorpmFlag.PM.getCode()<=cmd.getEndTime())
							ampmList.add(AmorpmFlag.PM.getCode());
						if(AmorpmFlag.NIGHT.getCode()>=cmd.getBeginTime()&&AmorpmFlag.NIGHT.getCode()<=cmd.getEndTime())
							ampmList.add(AmorpmFlag.NIGHT.getCode());
						changeRentalSiteRules = findRentalSiteRuleByDate(choseRSR.getRentalResourceId(),choseRSR.getResourceNumber(),null,null,
								ampmList, dateSF.get().format(new java.util.Date(start.getTimeInMillis())));
					}
					else if(rs.getRentalType().equals(RentalType.DAY.getCode())){
						 
						changeRentalSiteRules = findRentalSiteRuleByDate(choseRSR.getRentalResourceId(),choseRSR.getResourceNumber(),null,null,
								null, dateSF.get().format(new java.util.Date(start.getTimeInMillis())));
					}
					updateRSRs(changeRentalSiteRules, cmd); 
				}
			}
			return null;
		});
		
		
	}

	private RentalCell findRentalSiteRuleById(Long ruleId) {

		for( RentalCell cell : cellList.get()){
			if (cell.getId().equals(ruleId)){
				RentalCell dbCell = this.rentalv2Provider.getRentalCellById(cell.getId());
				if(null != dbCell )
					cell = dbCell;
				return cell;
			}
		}
		return null;
	}

	@Override
	public void updateRentalSiteDiscount(
			UpdateRentalSiteDiscountAdminCommand cmd) {
		if(null==cmd.getRentalSiteId())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid RentalSiteId   parameter in the command");
		RentalResource rs = this.rentalv2Provider.getRentalSiteById(cmd.getRentalSiteId());
		if(rs==null)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
				ErrorCodes.ERROR_GENERAL_EXCEPTION,
				"rental resource (site) cannot found ");
		rs.setDiscountType(cmd.getDiscountType());
		if(cmd.getDiscountType().equals(DiscountType.FULL_DAY_CUT_MONEY.getCode())){
			if( null == cmd.getCutPrice()  )
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_GENERAL_EXCEPTION,
					" cut price cannot found ");
		}
		if(cmd.getDiscountType().equals(DiscountType.FULL_MOENY_CUT_MONEY.getCode())){
			if( null == cmd.getCutPrice() || null == cmd.getFullPrice())
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
						ErrorCodes.ERROR_GENERAL_EXCEPTION,
						" full price or cut pricecannot found ");
		}
		rs.setFullPrice(cmd.getFullPrice());
		rs.setCutPrice(cmd.getCutPrice());
		this.rentalv2Provider.updateRentalSite(rs);
	}

	@Override
	public GetRefundOrderListResponse getRefundOrderList(
			GetRefundOrderListCommand cmd) {
		
		GetRefundOrderListResponse response = new GetRefundOrderListResponse();
		if(null == cmd.getResourceTypeId() )
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid resourceTypeId   parameter in the command");
		if(cmd.getPageAnchor() == null)
			cmd.setPageAnchor(Long.MAX_VALUE); 
		Integer pageSize = PaginationConfigHelper.getPageSize(
				configurationProvider, cmd.getPageSize()); 
//		checkEnterpriseCommunityIdIsNull(cmd.getOwnerId());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		String vendorType = null;
		if(null!=cmd.getVendorType())
			vendorType = VendorType.fromCode(cmd.getVendorType()).getStyleNo();
		List<RentalRefundOrder> orders = rentalv2Provider.getRefundOrderList(cmd.getResourceTypeId(),  
				  locator, cmd.getStatus(), vendorType, pageSize+1, cmd.getStartTime(), cmd.getEndTime()); 
		if(orders==null ||orders.size()==0)
			return response;
		if(orders.size() > pageSize) {
			orders.remove(orders.size() - 1);
			response.setNextPageAnchor( orders.get(orders.size() -1).getId()); 
		}
		response.setRefundOrders(new ArrayList<RefundOrderDTO>());
		orders.forEach((order)->{
			RefundOrderDTO dto =ConvertHelper.convert(order, RefundOrderDTO.class);
			dto.setVendorType(VendorType.fromStyleNo(order.getOnlinePayStyleNo()).getVendorType());
			User applyer = this.userProvider.findUserById(order.getCreatorUid());
			if(null != applyer){
				dto.setApplyUserName(applyer.getNickName());
				
				UserIdentifier userIdentifier = this.userProvider.findClaimedIdentifierByOwnerAndType(order.getCreatorUid(), IdentifierType.MOBILE.getCode()) ;
				if(null == userIdentifier){
					LOGGER.debug("userIdentifier is null...userId = " + order.getCreatorUid());
				}else{
					dto.setApplyUserContact(userIdentifier.getIdentifierToken());
				}
				 
			}
			else{
				LOGGER.error("user not found userId = " + order.getCreatorUid());
			}
			RentalOrder rentalOrder = this.rentalv2Provider.findRentalBillById(order.getOrderId());
			if(null!=rentalOrder){
				dto.setUseDetail(rentalOrder.getUseDetail());
				dto.setRentalCount(rentalOrder.getRentalCount());
			}else{
				LOGGER.error("rentalOrder not found  Order id = " + order.getOrderId());
			}
			dto.setApplyTime(order.getCreateTime().getTime());
			dto.setRentalBillId(rentalOrder.getOrderNo());
			response.getRefundOrders().add(dto);
		});
		
		return response;
	}

	@Override
	public RentalBillDTO getRentalBill(GetRentalBillCommand cmd) {
		if(null==cmd.getBillId() )
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter : bill id is null");
		RentalOrder bill = this.rentalv2Provider.findRentalBillById(cmd.getBillId());
		if(null==bill)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter : bill id can not find bill");
		// 在转换bill到dto的时候统一先convert一下  modify by wuhan 20160804
//		RentalBillDTO dto = ConvertHelper.convert(bill, RentalBillDTO.class);
//		mappingRentalBillDTO(dto, bill);
//		dto.setSiteItems(new ArrayList<SiteItemDTO>());
//		List<RentalItemsOrder> rentalSiteItems = rentalProvider
//				.findRentalItemsBillBySiteBillId(dto.getRentalBillId());
//		if(null!=rentalSiteItems)
//			for (RentalItemsOrder rib : rentalSiteItems) {
//				SiteItemDTO siDTO = new SiteItemDTO();
//				siDTO.setCounts(rib.getRentalCount());
////				RentalItem rsItem = rentalProvider.findRentalSiteItemById(rib.getRentalSiteItemId());
//				 
//				siDTO.setItemName(rib.getItemName());
//				siDTO.setItemPrice(rib.getTotalMoney());
//				dto.getSiteItems().add(siDTO);
//				 
//			}
		
		return proccessOrderDTO(bill);
	}

	@Override
	public String getRefundUrl(GetRefundUrlCommand cmd) { 
		if(null==cmd.getRefundId() )
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter : refund order  id is null"); 
		RentalRefundOrder refundOrder = this.rentalv2Provider.getRentalRefundOrderById(cmd.getRefundId());
		if(null==refundOrder)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid paramter : refund order id  can not find refund order ");
		if(refundOrder.getOnlinePayStyleNo().equals(VendorType.WEI_XIN.getStyleNo()))
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, 
					RentalServiceErrorCode.ERROR_REFOUND_ERROR, "refund order is wechat  ");
		PayZuolinRefundCommand refundCmd = new PayZuolinRefundCommand();
		String refoundApi =  this.configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"pay.zuolin.refound", "");
		String appKey = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"pay.appKey", "");
		refundCmd.setAppKey(appKey);
		Long timestamp = System.currentTimeMillis();
		refundCmd.setTimestamp(timestamp);
		Integer randomNum = (int) (Math.random()*1000);
		refundCmd.setNonce(randomNum); 
		refundCmd.setRefundOrderNo(String.valueOf(refundOrder.getRefundOrderNo()) );
		refundCmd.setOrderNo(String.valueOf(refundOrder.getOrderNo()));
		refundCmd.setOnlinePayStyleNo(refundOrder.getOnlinePayStyleNo()); 
		refundCmd.setOrderType(OrderType.OrderTypeEnum.RENTALORDER.getPycode());
		//已付金额乘以退款比例除以100
		refundCmd.setRefundAmount( refundOrder.getAmount());
		refundCmd.setRefundMsg("预订单取消退款");
		this.setSignatureParam(refundCmd);
		PayZuolinRefundResponse refundResponse = (PayZuolinRefundResponse) this.restCall(refoundApi, refundCmd, PayZuolinRefundResponse.class);
		if(refundResponse.getErrorCode().equals(HttpStatus.OK.value())){
			return refundResponse.getResponse();
		}
		else{
			LOGGER.error("refund order no =["+refundOrder.getRefundOrderNo()+"] refound error param is "+refundCmd.toString());
			if (null != refundResponse.getErrorDetails())
				throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
						RentalServiceErrorCode.ERROR_REFOUND_ERROR,
						refundResponse.getErrorDetails()); 
			else
				throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
						RentalServiceErrorCode.ERROR_REFOUND_ERROR,
						"refund order error"); 
		}	
		 
	}

	@Override
	public GetResourceTypeListResponse getResourceTypeList(GetResourceTypeListCommand cmd) {
		if(null == cmd.getNamespaceId() )
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid namespaceId   parameter in the command");
		GetResourceTypeListResponse response = new GetResourceTypeListResponse();
		if(cmd.getPageAnchor() == null)
			cmd.setPageAnchor(Long.MAX_VALUE); 
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor()); 

		Byte status = ResourceTypeStatus.NORMAL.getCode();
		if (null != cmd.getStatus()) {
			status = ResourceTypeStatus.CUSTOM.getCode();
		}

		List<RentalResourceType> resourceTypes =  this.rentalv2Provider.findRentalResourceTypes(cmd.getNamespaceId(), status, locator);
		if(null==resourceTypes)
			return response;

		Long nextPageAnchor = null;
		if(resourceTypes.size() > pageSize) {
			resourceTypes.remove(resourceTypes.size() - 1);
			nextPageAnchor = resourceTypes.get(resourceTypes.size() -1).getId();
		}
		response.setNextPageAnchor(nextPageAnchor);
		response.setResourceTypes(new ArrayList<>());
		resourceTypes.forEach((order)->{
			ResourceTypeDTO dto =ConvertHelper.convert(order, ResourceTypeDTO.class);
			dto.setIconUrl(this.contentServerService.parserUri(dto.getIconUri(),
					EntityType.USER.getCode(), UserContext.current().getUser().getId()));
			response.getResourceTypes().add(dto);
		});
		return response;
	}

	@Override
	public void createResourceType(CreateResourceTypeCommand cmd) {
		cmd.setStatus(ResourceTypeStatus.DISABLE.getCode());
		RentalResourceType rsType = ConvertHelper.convert(cmd, RentalResourceType.class);
		this.rentalv2Provider.createRentalResourceType(rsType);
		
	}

	@Override
	public void deleteResourceType(DeleteResourceTypeCommand cmd) {
		// TODO 图标也要删除
		this.rentalv2Provider.deleteRentalResourceType(cmd.getId());
		
	}

	@Override
	public void updateResourceType(UpdateResourceTypeCommand cmd) {
		//  更新type不更新status
		RentalResourceType rsType = this.rentalv2Provider.getRentalResourceTypeById(cmd.getId());
		rsType.setIconUri(cmd.getIconUri());
		rsType.setName(cmd.getName());
		rsType.setPageType(cmd.getPageType());
		this.rentalv2Provider.updateRentalResourceType(rsType);
	}

	@Override
	public void closeResourceType(CloseResourceTypeCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void openResourceType(OpenResourceTypeCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteResource(DeleteResourceCommand cmd) {

		rentalv2Provider.deleteResourceCells(cmd.getId(), null, null); 
		RentalResource rs = rentalv2Provider.getRentalSiteById(cmd.getId());
		if(rs == null )
			return  ;
		rs.setStatus(RentalSiteStatus.DISABLE.getCode());
		rentalv2Provider.updateRentalSite(rs);
		
	}
	
	/**
	 *  线上模式，没有对接工作流的短信，TODO:以后如果需要对接，则需要修改
	 * 发短信给付费成功的用户
	 * */
	@Override
	public void sendRentalSuccessSms(Integer namespaceId, String phoneNumber,RentalOrder order){  
		String templateScope = SmsTemplateCode.SCOPE;
		List<Tuple<String, Object>> variables = smsProvider.toTupleList("resourceName", order.getResourceName());
		smsProvider.addToTupleList(variables, "useDetail", order.getUseDetail()); 
		//根据条件找模板id
		RentalResource rs = this.rentalv2Provider.getRentalSiteById(order.getRentalResourceId());
		if(rs == null){
			LOGGER.error("send message to user failed rental resource can not found [resource id = "+order.getRentalResourceId()+"]");
			return ;
		} 	
		int templateId = SmsTemplateCode.RENTAL_SUCCESS_EXCLUSIVE_CODE;
		//如果不是独占资源
		if(rs.getExclusiveFlag().equals(NormalFlag.NONEED.getCode())){
			if(rs.getAutoAssign().equals(NormalFlag.NEED.getCode())){
				//带场所编号的
				templateId = SmsTemplateCode.RENTAL_SUCCESS_SITENUMBER_CODE;
			}
			else{
				//不带场所编号的
				templateId = SmsTemplateCode.RENTAL_SUCCESS_NOSITENUMBER_CODE;
				smsProvider.addToTupleList(variables, "count", order.getRentalCount()); 
			}
		}
			
		String templateLocale = RentalNotificationTemplateCode.locale;
		if(LOGGER.isDebugEnabled()) {
            LOGGER.info("begin Send sms message, namespaceId=" + namespaceId + ", phoneNumbers=[" + phoneNumber
                + "], templateScope=" + templateScope + ", templateId=" + templateId + ", templateLocale=" + templateLocale);
        }	
		smsProvider.sendSms(namespaceId, phoneNumber, templateScope, templateId, templateLocale, variables);

		if(LOGGER.isDebugEnabled()) {
            LOGGER.info("end Send sms message, namespaceId=" + namespaceId + ", phoneNumbers=[" + phoneNumber
                + "], templateScope=" + templateScope + ", templateId=" + templateId + ", templateLocale=" + templateLocale);
        }
	}

	@Override
	public void addCheckOperator(AddCheckOperatorCommand cmd) {
		  
		List<Long> privilegeIds = new ArrayList<>();
		privilegeIds.add(PrivilegeConstants.RENTAL_CHECK);
		rolePrivilegeService.assignmentPrivileges(EntityType.ORGANIZATIONS.getCode(),cmd.getOrganizationId(),
				EntityType.USER.getCode(),cmd.getUserId(),RentalServiceErrorCode.SCOPE,privilegeIds);
		
	}

	@Override
	public void deleteCheckOperator(AddCheckOperatorCommand cmd) {
		 
//		List<Long> privilegeIds = new ArrayList<>();
//		privilegeIds.add(PrivilegeConstants.RENTAL_CHECK);
//		rolePrivilegeService.
	}
	

	@Override
	public void updateRentalDate(UpdateRentalDateCommand cmd){

		if(null == cmd.getBeginDate()||null == cmd.getEndDate())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"begin or end date can not be null");

		RentalResource rs = this.rentalv2Provider.getRentalSiteById(cmd.getRentalSiteId());

		this.dbProvider.execute((TransactionStatus status) -> {

			rs.setBeginDate(new Date(cmd.getBeginDate()));
			rs.setEndDate(new Date(cmd.getEndDate()));
			//modify by wh 2016-11-11 修改时间点和修改操作人的记录
			rs.setOperatorUid(UserContext.current().getUser().getId());
			rs.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

			rs.setOpenWeekday(convertOpenWeekday(cmd.getOpenWeekday()));
			this.rentalv2Provider.deleteRentalCloseDatesByOwnerId(EhRentalv2Resources.class.getSimpleName(), rs.getId());
			//close dates
			setRentalRuleCloseDates(cmd.getCloseDates(), rs.getId(), EhRentalv2Resources.class.getSimpleName());

			this.rentalv2Provider.updateRentalSite(rs);
			return null;
		});
		
	}


	@Override
	public void updateResourceAttachment(UpdateResourceAttachmentCommand cmd){
		//重新生成附件
		RentalResource rs = this.rentalv2Provider.getRentalSiteById(cmd.getRentalSiteId());
		this.rentalv2Provider.deleteRentalConfigAttachmentsByOwnerId(EhRentalv2Resources.class.getSimpleName(), rs.getId());
		createRentalConfigAttachment(cmd.getAttachments(), rs.getId(), EhRentalv2Resources.class.getSimpleName());

	}

	@Override
	public void updateDefaultAttachmentRule(UpdateDefaultAttachmentRuleAdminCommand cmd) {
		RentalDefaultRule defaultRule = this.rentalv2Provider.getRentalDefaultRule(cmd.getOwnerType(),cmd.getOwnerId(),cmd.getResourceTypeId());
		if(null==defaultRule){
			throw RuntimeErrorException
			.errorWith(RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_DEFAULT_RULE_NOTFOUND, "didnt have default rule!");
		} 
		this.dbProvider.execute((TransactionStatus status) -> {

			this.rentalv2Provider.deleteRentalConfigAttachmentsByOwnerId(EhRentalv2DefaultRules.class.getSimpleName(),defaultRule.getId());
			//config attachments
//			if (null != cmd.getAttachments()) {
//				for(com.everhomes.rest.rentalv2.admin.AttachmentConfigDTO attachmentDTO:cmd.getAttachments()){
//					RentalConfigAttachment rca =ConvertHelper.convert(attachmentDTO, RentalConfigAttachment.class);
//					rca.setOwnerType(EhRentalv2DefaultRules.class.getSimpleName());
//					rca.setOwnerId(defaultRule.getId());
//					this.rentalv2Provider.createRentalConfigAttachment(rca);
//				}
//			}
			createRentalConfigAttachment(cmd.getAttachments(), defaultRule.getId(), EhRentalv2DefaultRules.class.getSimpleName());
			return null;
		});
	}

	private void createRentalConfigAttachment(List<AttachmentConfigDTO> attachments, Long ownerId, String ownerType) {
		if (null != attachments) {
			attachments.forEach(a -> {

				RentalConfigAttachment rca = ConvertHelper.convert(a, RentalConfigAttachment.class);
				rca.setOwnerType(ownerType);
				rca.setOwnerId(ownerId);
				this.rentalv2Provider.createRentalConfigAttachment(rca);

				if (a.getAttachmentType().equals(AttachmentType.GOOD_ITEM.getCode())) {
					List<RentalGoodItem> goodItems = a.getGoodItems();
					addGoodItems(goodItems, AttachmentType.GOOD_ITEM.name(), rca.getId());
				}else if (a.getAttachmentType().equals(AttachmentType.RECOMMEND_USER.getCode())) {
					List<RentalRecommendUser> recommendUsers = a.getRecommendUsers();
					addRecommendUsers(recommendUsers, AttachmentType.RECOMMEND_USER.name(), rca.getId());
				}
			});
		}
	}

	private void addGoodItems(List<RentalGoodItem> goodItems, String ownerType, Long ownerId) {
		if (null != goodItems) {
			goodItems.forEach(g -> {
				RentalConfigAttachment gg = ConvertHelper.convert(g, RentalConfigAttachment.class);
				gg.setOwnerType(ownerType);
				gg.setOwnerId(ownerId);
				gg.setAttachmentType(AttachmentType.GOOD_ITEM.getCode());
				gg.setMustOptions(NormalFlag.NONEED.getCode());
				this.rentalv2Provider.createRentalConfigAttachment(gg);
			});
		}
	}

	private void addRecommendUsers(List<RentalRecommendUser> recommendUsers, String ownerType, Long ownerId) {
		if (null != recommendUsers) {
			recommendUsers.forEach(u -> {
				RentalConfigAttachment uu = ConvertHelper.convert(u, RentalConfigAttachment.class);
				uu.setOwnerType(ownerType);
				uu.setOwnerId(ownerId);
				uu.setAttachmentType(AttachmentType.RECOMMEND_USER.getCode());
				uu.setMustOptions(NormalFlag.NONEED.getCode());
				this.rentalv2Provider.createRentalConfigAttachment(uu);
			});
		}
	}

	@Override
	public void updateResourceOrder(UpdateResourceOrderAdminCommand cmd) {
		if (null == cmd.getId()) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid id parameter in the command");
		}
		if (null == cmd.getDefaultOrderId()) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid defaultOrderId parameter in the command");
		}
		RentalResource resource = rentalv2Provider.getRentalSiteById(cmd.getId());
		RentalResource exchangeResource = rentalv2Provider.getRentalSiteById(cmd.getDefaultOrderId());

		if (null == resource) {
			LOGGER.error("RentalResource not found, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"RentalResource not found");
		}
		if (null == exchangeResource) {
			LOGGER.error("RentalResource not found, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"RentalResource not found");
		}

		Long order = resource.getDefaultOrder();
		Long exchangeOrder = exchangeResource.getDefaultOrder();

		dbProvider.execute((TransactionStatus status) -> {
			resource.setDefaultOrder(exchangeOrder);
			exchangeResource.setDefaultOrder(order);
			rentalv2Provider.updateRentalSite(resource);
			rentalv2Provider.updateRentalSite(exchangeResource);
			return null;
		});

	}
}
