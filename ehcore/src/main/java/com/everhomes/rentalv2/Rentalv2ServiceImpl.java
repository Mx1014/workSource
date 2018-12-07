package com.everhomes.rentalv2;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.acl.ServiceModuleAppAuthorization;
import com.everhomes.acl.ServiceModuleAppAuthorizationService;
import com.everhomes.aclink.DoorAccessProvider;
import com.everhomes.aclink.DoorAccessService;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.app.App;
import com.everhomes.app.AppProvider;
import com.everhomes.archives.ArchivesService;
import com.everhomes.bus.LocalEventBus;
import com.everhomes.bus.LocalEventContext;
import com.everhomes.bus.SystemEvent;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerResource;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.*;
import com.everhomes.http.HttpUtils;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.order.OrderUtil;
import com.everhomes.order.PayProvider;
import com.everhomes.organization.*;
import com.everhomes.parking.vip_parking.DingDingParkingLockHandler;
import com.everhomes.pay.order.PaymentType;
import com.everhomes.portal.PortalService;
import com.everhomes.queue.taskqueue.JesqueClientFactory;
import com.everhomes.queue.taskqueue.WorkerPoolFactory;
import com.everhomes.rentalv2.job.*;
import com.everhomes.rest.aclink.CreateDoorAuthCommand;
import com.everhomes.rest.aclink.DoorAuthDTO;
import com.everhomes.rest.activity.ActivityRosterPayVersionFlag;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.archives.AddArchivesContactCommand;
import com.everhomes.rest.archives.ArchivesContactDTO;
import com.everhomes.rest.enterprise.GetAuthOrgByProjectIdAndAppIdCommand;
import com.everhomes.rest.enterprise.ListUserOrganizationsCommand;
import com.everhomes.rest.enterprise.ListUserOrganizationsResponse;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.launchpadbase.AppContext;
import com.everhomes.rest.organization.*;
import com.everhomes.rest.order.CommonOrderCommand;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.PaymentParamsDTO;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.rentalv2.PreOrderCommand;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.parking.ParkingSpaceDTO;
import com.everhomes.rest.parking.ParkingSpaceLockStatus;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.rest.rentalv2.*;
import com.everhomes.rest.rentalv2.VisibleFlag;
import com.everhomes.rest.rentalv2.admin.*;
import com.everhomes.rest.rentalv2.admin.AttachmentType;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.rentalv2.SceneType;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.EhRentalv2Orders;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.SmsProvider;
import com.everhomes.techpark.onlinePay.OnlinePayService;
import com.everhomes.user.*;
import com.everhomes.util.*;

import com.google.gson.Gson;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import javax.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Rentalv2ServiceImpl implements Rentalv2Service, ApplicationListener<ContextRefreshedEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(Rentalv2ServiceImpl.class);

	//目前价格全部是按照0.5小时来计算价格
	private static final Double PRICE_TIME_STEP = 0.5;

	private static final Long MILLISECONDGMT = 8 * 3600 * 1000L;
	//默认15分钟后取消
	private Long ORDER_AUTO_CANCEL_TIME = 15 * 60 * 1000L;

	private String queueName = "rentalService";
	public static final Long moduleId = 40400L;

	private static final String REFER_TYPE = FlowReferType.RENTAL.getCode();

	private ThreadLocal<SimpleDateFormat> timeSF = ThreadLocal.withInitial(() -> new SimpleDateFormat("HH:mm:ss"));
	private ThreadLocal<SimpleDateFormat> dateSF = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));
	private ThreadLocal<SimpleDateFormat> datetimeSF = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

	//双休日和法定假日的关闭日期集合
	private List<Long> normalWeekend = new ArrayList<>();
	private List<Long> legalHoliday = new ArrayList<>();

	/**cellList : 当前线程用到的单元格 */
	private static ThreadLocal<List<RentalCell>> cellList = ThreadLocal.withInitial(ArrayList::new);
	/**
	 * seqNum : 计数-申请id用
	 */
	private static ThreadLocal<Long> seqNum = ThreadLocal.withInitial(() -> 0L);
	/**
	 * currentId : 当前id
	 */
	private static ThreadLocal<Long> currentId = new ThreadLocal<Long>() {

	};

	private static ThreadLocal<String> currentSceneType = new ThreadLocal<String>(){

	};

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
	@Autowired
	private Rentalv2PriceRuleProvider rentalv2PriceRuleProvider;
	@Autowired
	private Rentalv2PricePackageProvider rentalv2PricePackageProvider;
	@Autowired
	private PayProvider payProvider;
	@Autowired
	private DoorAccessService doorAccessService;
	@Autowired
	private DoorAccessProvider doorAccessProvider;
	@Autowired
	private AddressProvider addressProvider;
	@Autowired
	private RentalCommonServiceImpl rentalCommonService;
	@Autowired
	private DingDingParkingLockHandler dingDingParkingLockHandler;
	@Autowired
	private OrganizationService organizationService;
	@Autowired
	private UserPrivilegeMgr userPrivilegeMgr;
	@Autowired
	private PortalService portalService;
	@Autowired
	private Rentalv2PayService  rentalv2PayService;
	@Autowired
	private Rentalv2AccountProvider rentalv2AccountProvider;
	@Autowired
	private ServiceModuleAppAuthorizationService serviceModuleAppAuthorizationService;
	@Autowired
	private UserActivityService userActivityService;
	@Autowired
	private ArchivesService archivesService;
	@Autowired
	private FlowEventLogProvider flowEventLogProvider;

	private ExecutorService executorPool = Executors.newFixedThreadPool(5);

	private Time convertTime(Long TimeLong) {
		if (null != TimeLong) {
			//从8点开始计算
			return new Time(TimeLong - MILLISECONDGMT);
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
			return cal.getTimeInMillis() + time.getTime() + MILLISECONDGMT;
		}
		return null;
	}
	
    // 升级平台包到1.0.1，把@PostConstruct换成ApplicationListener，
    // 因为PostConstruct存在着平台PlatformContext.getComponent()会有空指针问题 by lqs 20180516
	//@PostConstruct
	public void setup() {
		workerPoolFactory.getWorkerPool().addQueue(queueName);
		try {
			String closeDays = rentalv2Provider.getHolidayCloseDate(RentalHolidayType.NORMAL_WEEKEND.getCode());
			if (closeDays != null) {
				String[] day = closeDays.split(",");
				normalWeekend = Arrays.stream(day).map(r -> Long.valueOf(r)).collect(Collectors.toList());
			}
			closeDays = rentalv2Provider.getHolidayCloseDate(RentalHolidayType.LEGAL_HOLIDAY.getCode());
			if (closeDays != null) {
				String[] day = closeDays.split(",");
				legalHoliday = Arrays.stream(day).map(r -> Long.valueOf(r)).collect(Collectors.toList());
			}
		}catch (Exception e){
			LOGGER.error("inite HolidayCloseDate error");
		}
	}
	
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            setup();
        }
    }

	private String processFlowURL(Long flowCaseId, String flowUserType, Long moduleId) {
		return "zl://workflow/detail?flowCaseId=" + flowCaseId + "&flowUserType=" + flowUserType + "&moduleId=" + moduleId;
	}

	private void checkEnterpriseCommunityIdIsNull(Long enterpriseCommunityId) {
		if (null == enterpriseCommunityId || enterpriseCommunityId.equals(0L)) {
			LOGGER.error("Invalid enterpriseCommunityId parameter in the command");
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_LOST_PARAMETER,
					"Invalid ownerId parameter in the command");
		}

	}

	@Override
	public void addRule(AddDefaultRuleAdminCommand cmd) {
		//现在根据rentalStartTimeFlag和 rentalEndTimeFlag 标志来判断是否有最多/至少 提前预约时间

		//当需要手动选择场所时，判断编号数量和场所编号数量是否匹配
//		checkSiteNumber(cmd.getAutoAssign(), cmd.getSiteNumbers(), cmd.getSiteCounts());

		this.dbProvider.execute((TransactionStatus status) -> {
			//default rule
			RentalDefaultRule rule = ConvertHelper.convert(cmd, RentalDefaultRule.class);

			if (null != cmd.getBeginDate()) {
				rule.setBeginDate(new Date(cmd.getBeginDate()));
			}
			if (null != cmd.getEndDate()) {
				rule.setEndDate(new Date(cmd.getEndDate()));
			}

//			if(null == defaultRule.getCancelFlag()) {
//				defaultRule.setCancelFlag(NormalFlag.NEED.getCode());
//			}
			//设置星期 开放时间 如 星期一，星期二
			rule.setOpenWeekday(convertOpenWeekday(cmd.getOpenWeekday()));
//			rule.setResourceCounts(cmd.getSiteCounts());

			this.rentalv2Provider.createRentalDefaultRule(rule);

			if (cmd.getSourceType().equals(RuleSourceType.DEFAULT.getCode())) {

				//hour time intervals
				setRentalRuleTimeIntervals(cmd.getResourceType(), EhRentalv2DefaultRules.class.getSimpleName(), rule.getId(), cmd.getTimeIntervals());

				// set half day time intervals
				setRentalRuleTimeIntervals(cmd.getResourceType(), RentalTimeIntervalOwnerType.DEFAULT_HALF_DAY.getCode(), rule.getId(), cmd.getHalfDayTimeIntervals());

				createPriceRules(cmd.getResourceType(), PriceRuleType.DEFAULT, rule.getId(), cmd.getPriceRules());

				createPricePackages(cmd.getResourceType(), PriceRuleType.DEFAULT, rule.getId(), cmd.getPricePackages());

				//close dates
				setRentalRuleCloseDates(cmd.getCloseDates(), rule.getId(), EhRentalv2DefaultRules.class.getSimpleName(), cmd.getResourceType());

				//config attachments
				createRentalConfigAttachment(cmd.getAttachments(), rule.getId(), EhRentalv2DefaultRules.class.getSimpleName(), cmd.getResourceType());
				//items
				addItems(cmd.getSiteItems(), rule.getId(), RuleSourceType.DEFAULT.getCode(), cmd.getResourceType());
				//structure
                createRentalStructures(cmd.getStructures(),cmd.getResourceType(),rule.getId(), RuleSourceType.DEFAULT.getCode());

			} else if (cmd.getSourceType().equals(RuleSourceType.RESOURCE.getCode())) {

				//hour time intervals
				setRentalRuleTimeIntervals(cmd.getResourceType(), EhRentalv2Resources.class.getSimpleName(), rule.getSourceId(), cmd.getTimeIntervals());

				// set half day time intervals
				setRentalRuleTimeIntervals(cmd.getResourceType(), RentalTimeIntervalOwnerType.RESOURCE_HALF_DAY.getCode(), rule.getSourceId(), cmd.getHalfDayTimeIntervals());

				//set day open times
				setRentalDayopenTime(cmd.getOpenTimes(), EhRentalv2Resources.class.getSimpleName(), rule.getSourceId(), rule.getResourceType());
				//创建资源时分配单元格
				createResourceCells(cmd.getPriceRules());

				createPriceRules(cmd.getResourceType(), PriceRuleType.RESOURCE, rule.getSourceId(), cmd.getPriceRules());

				createPricePackages(cmd.getResourceType(), PriceRuleType.RESOURCE, rule.getSourceId(), cmd.getPricePackages());

				//退款提示
				createRentalRefundTips(cmd.getResourceType(),RuleSourceType.RESOURCE.getCode(),rule.getSourceId(),cmd.getRefundTips());
				//close dates
				setRentalRuleCloseDates(cmd.getCloseDates(), rule.getSourceId(), EhRentalv2Resources.class.getSimpleName(), cmd.getResourceType());

				//config attachments
				createRentalConfigAttachment(cmd.getAttachments(), rule.getSourceId(), EhRentalv2Resources.class.getSimpleName(), cmd.getResourceType());
				//items
				addItems(cmd.getSiteItems(), rule.getSourceId(), RuleSourceType.RESOURCE.getCode(), cmd.getResourceType());
				//structure
                createRentalStructures(cmd.getStructures(),cmd.getResourceType(),rule.getSourceId(), RuleSourceType.RESOURCE.getCode());
			}

			if (null != cmd.getRefundStrategy() && cmd.getRefundStrategy() == RentalOrderStrategy.CUSTOM.getCode()) {
				createRentalOrderRules(cmd.getResourceType(), rule.getSourceType(), rule.getId(), cmd.getRefundStrategies());
			}
			if (null != cmd.getOvertimeStrategy() && cmd.getOvertimeStrategy() == RentalOrderStrategy.CUSTOM.getCode()) {
				createRentalOrderRules(cmd.getResourceType(), rule.getSourceType(), rule.getId(), cmd.getOvertimeStrategies());
			}
			return null;
		});
	}

	private void createRentalStructures(List<SiteStructureDTO> structures,String resourceType, Long sourceId, String sourceType){
        if (structures != null)
            this.dbProvider.execute((TransactionStatus status) -> {
                structures.stream().forEach(r -> {
                    RentalStructure structure = ConvertHelper.convert(r,RentalStructure.class);
                    structure.setResourceType(resourceType);
                    structure.setSourceId(sourceId);
                    structure.setSourceType(sourceType);
                   this.rentalv2Provider.createRentalStructure(structure);
                });
                return null;
            });
    }

	private void createPriceRules(String resourceType, PriceRuleType priceRuleType, Long ruleId, List<PriceRuleDTO> priceRules) {

		if (priceRules != null && !priceRules.isEmpty()) {
			priceRules.forEach(p -> createPriceRule(resourceType, priceRuleType, ruleId, p));
		}
	}

	private void createPricePackages(String resourceType, PriceRuleType priceRuleType, Long ruleId, List<PricePackageDTO> pricePackages) {
		if (pricePackages != null && !pricePackages.isEmpty()) {
			pricePackages.forEach(p -> createPricePackage(resourceType, priceRuleType, ruleId, p));
		}
	}

	private void createPriceRule(String resourceType, PriceRuleType priceRuleType, Long ruleId, PriceRuleDTO priceRule) {
		Rentalv2PriceRule rentalv2PriceRule = ConvertHelper.convert(priceRule, Rentalv2PriceRule.class);
		rentalv2PriceRule.setOwnerType(priceRuleType.getCode());
		rentalv2PriceRule.setOwnerId(ruleId);
		rentalv2PriceRule.setResourceType(resourceType);
		//默认按时长收费
		if (rentalv2PriceRule.getPriceType() == null)
			rentalv2PriceRule.setPriceType(RentalPriceType.LINEARITY.getCode());
		rentalv2PriceRuleProvider.createRentalv2PriceRule(rentalv2PriceRule);

		if (priceRule.getClassifications() != null){
			for (RentalPriceClassificationDTO dto : priceRule.getClassifications()){
				RentalPriceClassification classification = ConvertHelper.convert(dto,RentalPriceClassification.class);
				classification.setNamespaceId(UserContext.getCurrentNamespaceId());
				classification.setResourceType(resourceType);
				classification.setOwnerType(EhRentalv2PriceRules.class.getSimpleName());
				classification.setOwnerId(rentalv2PriceRule.getId());
				classification.setSourceType(priceRuleType.getCode());
				classification.setSourceId(ruleId);
				rentalv2PriceRuleProvider.createRentalv2PriceClassification(classification);
			}
		}
	}

	private void createPricePackage(String resourceType, PriceRuleType priceRuleType, Long ruleId, PricePackageDTO pricePackage) {
		Rentalv2PricePackage rentalv2PricePackage = ConvertHelper.convert(pricePackage, Rentalv2PricePackage.class);
		rentalv2PricePackage.setOwnerType(priceRuleType.getCode());
		rentalv2PricePackage.setOwnerId(ruleId);
		rentalv2PricePackage.setResourceType(resourceType);

		//默认按时长收费
		if (rentalv2PricePackage.getPriceType() == null)
			rentalv2PricePackage.setPriceType(RentalPriceType.LINEARITY.getCode());
		rentalv2PricePackageProvider.createRentalv2PricePackage(rentalv2PricePackage);

		if (pricePackage.getClassifications() != null)
			for (RentalPriceClassificationDTO dto : pricePackage.getClassifications()){
				RentalPriceClassification classification = ConvertHelper.convert(dto,RentalPriceClassification.class);
				classification.setNamespaceId(UserContext.getCurrentNamespaceId());
				classification.setResourceType(resourceType);
				classification.setOwnerType(EhRentalv2PricePackages.class.getSimpleName());
				classification.setOwnerId(rentalv2PricePackage.getId());
				classification.setSourceType(priceRuleType.getCode());
				classification.setSourceId(ruleId);
				rentalv2PriceRuleProvider.createRentalv2PriceClassification(classification);
			}
	}

	private String convertOpenWeekday(List<Integer> openWeekdays) {

		if (null == openWeekdays)
			return "0000000";

		int openWorkdayInt = 0;
		StringBuilder openWorkday;
		//list的数字:1234567代表从星期天到星期六,经过-1作为10的次方放到7位字符串内
		for (Integer weekdayInteger : openWeekdays) {
			openWorkdayInt += Math.pow(10, weekdayInteger - 1);
		}
		openWorkday = new StringBuilder(String.valueOf(openWorkdayInt));
		for (; openWorkday.length() < 7; ) {
			openWorkday.insert(0, "0");
		}
		return openWorkday.toString();
	}

	private List<Integer> resolveOpenWeekday(String openWeekday) {
		List<Integer> result = new ArrayList<>();
		if (null != openWeekday) {
			int openWeekInt = Integer.valueOf(openWeekday);
			for (int i = 1; i < 8; i++) {
				if (openWeekInt % 10 == 1)
					result.add(i);
				openWeekInt = openWeekInt / 10;
			}
		}

		return result;
	}

	@Override
	public QueryDefaultRuleAdminResponse queryDefaultRule(QueryDefaultRuleAdminCommand cmd) {

		if (StringUtils.isBlank(cmd.getOwnerType()) || null == cmd.getOwnerId()) {
			RentalResource rs = rentalCommonService.getRentalResource(cmd.getResourceType(), cmd.getSourceId());

			RentalResourceHandler handler = rentalCommonService.getRentalResourceHandler(rs.getResourceType());
			handler.setRuleOwnerTypeByResource(cmd, rs);
		}

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}

		if (StringUtils.isBlank(cmd.getSourceType())) {
			cmd.setSourceType(RuleSourceType.DEFAULT.getCode());
		}

		RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(cmd.getOwnerType(), cmd.getOwnerId(),
				cmd.getResourceType(), cmd.getResourceTypeId(), cmd.getSourceType(), cmd.getSourceId());

		if (null == rule) {
			addDefaultRule(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getResourceType(), cmd.getResourceTypeId(),
					cmd.getSourceType(), cmd.getSourceId());
			rule = this.rentalv2Provider.getRentalDefaultRule(cmd.getOwnerType(), cmd.getOwnerId(),
					cmd.getResourceType(), cmd.getResourceTypeId(), cmd.getSourceType(), cmd.getSourceId());
		}

		return convert(rule, cmd.getSourceType());
	}

	//将与资源规则或者默认规则所有关联信息查出
	private QueryDefaultRuleAdminResponse convert(RentalDefaultRule rule, String sourceType) {
		QueryDefaultRuleAdminResponse response = ConvertHelper.convert(rule, QueryDefaultRuleAdminResponse.class);

		response.setOpenWeekday(resolveOpenWeekday(rule.getOpenWeekday()));
//		response.setSiteCounts(rule.getResourceCounts());

		if (null != rule.getBeginDate()) {
			response.setBeginDate(rule.getBeginDate().getTime());
		}
		if (null != rule.getEndDate()) {
			response.setEndDate(rule.getEndDate().getTime());
		}

		String priceRuleType = null;
		String ruleType = null;
		Long id = null;
		if (RuleSourceType.DEFAULT.getCode().equals(sourceType)) {
			priceRuleType = PriceRuleType.DEFAULT.getCode();
			ruleType = EhRentalv2DefaultRules.class.getSimpleName();
			id = rule.getId();
		} else if (RuleSourceType.RESOURCE.getCode().equals(sourceType)) {
			priceRuleType = PriceRuleType.RESOURCE.getCode();
			ruleType = EhRentalv2Resources.class.getSimpleName();
			id = rule.getSourceId();
		}

		List<Rentalv2PriceRule> priceRules = rentalv2PriceRuleProvider.listPriceRuleByOwner(rule.getResourceType(),
				priceRuleType, id);
		response.setPriceRules(priceRules.stream().map(this::convert).collect(Collectors.toList()));
		response.setRentalTypes(priceRules.stream().map(Rentalv2PriceRule::getRentalType).collect(Collectors.toList()));


		List<Rentalv2PricePackage> pricePackages = rentalv2PricePackageProvider.listPricePackageByOwner(rule.getResourceType(),
				priceRuleType, id, null, null);
		response.setPricePackages(pricePackages.stream().map(this::convert).collect(Collectors.toList()));

		String halfOwnerType = RentalTimeIntervalOwnerType.DEFAULT_HALF_DAY.getCode();
		if (EhRentalv2Resources.class.getSimpleName().equals(ruleType)) {
			halfOwnerType = RentalTimeIntervalOwnerType.RESOURCE_HALF_DAY.getCode();
		}

		List<RentalTimeInterval> halfTimeIntervals = rentalv2Provider.queryRentalTimeIntervalByOwner(rule.getResourceType(),
				halfOwnerType, id);
		if (null != halfTimeIntervals) {
			response.setHalfDayTimeIntervals(halfTimeIntervals.stream().map(h -> ConvertHelper.convert(h, TimeIntervalDTO.class))
					.collect(Collectors.toList()));
		}

		List<RentalTimeInterval> timeIntervals = rentalv2Provider.queryRentalTimeIntervalByOwner(rule.getResourceType(),
				ruleType, id);
		if (null != timeIntervals) {
			response.setTimeIntervals(timeIntervals.stream().map(t -> ConvertHelper.convert(t, TimeIntervalDTO.class))
					.collect(Collectors.toList()));
		}
		List<RentalCloseDate> closeDates = rentalv2Provider.queryRentalCloseDateByOwner(rule.getResourceType(),
				ruleType, id,null,null);
		if (null != closeDates) {
			LocalDate today = LocalDate.now();
			Long firstDay = LocalDateTime.of(today.getYear(), 1, 1, 0, 0).atZone(ZoneId.systemDefault())
					.toInstant().toEpochMilli();
			response.setCloseDates(closeDates.stream().filter(d -> null != d.getCloseDate() && d.getCloseDate().getTime() > firstDay).map(c -> c.getCloseDate().getTime())
					.collect(Collectors.toList()));
		}
		List<RentalDayopenTime> dayopenTimes = rentalv2Provider.queryRentalDayopenTimeByOwner(rule.getResourceType(),
				ruleType, id, null);
		if (dayopenTimes != null) {
			response.setOpenTimes(dayopenTimes.stream().map(r -> {
				RentalOpenTimeDTO dto = new RentalOpenTimeDTO();
				dto.setRentalType(r.getRentalType());
				dto.setDayOpenTime(r.getOpenTime());
				dto.setDayCloseTime(r.getCloseTime());
				return dto;
			}).collect(Collectors.toList()));
		}

		//set 物资
		List<RentalConfigAttachment> attachments = rentalv2Provider.queryRentalConfigAttachmentByOwner(rule.getResourceType(),
				ruleType, id, null);
		response.setAttachments(convertAttachments(attachments));
		//订单规则
		List<RentalOrderRule> refundRules = rentalv2Provider.listRentalOrderRules(rule.getResourceType(), rule.getSourceType(),
				rule.getId(), RentalOrderHandleType.REFUND.getCode());

		List<RentalOrderRule> overTimeRules = rentalv2Provider.listRentalOrderRules(rule.getResourceType(), rule.getSourceType(),
				rule.getId(), RentalOrderHandleType.OVERTIME.getCode());

		response.setRefundStrategies(refundRules.stream().map(r -> ConvertHelper.convert(r, RentalOrderRuleDTO.class)).collect(Collectors.toList()));
		response.setOvertimeStrategies(overTimeRules.stream().map(r -> ConvertHelper.convert(r, RentalOrderRuleDTO.class)).collect(Collectors.toList()));
		//退款提示
		List<RentalRefundTip> refundTips = rentalv2Provider.listRefundTips(rule.getResourceType(),rule.getSourceType(),id,
				null);
		response.setRefundTips(refundTips.stream().map(r->ConvertHelper.convert(r,RentalRefundTipDTO.class)).collect(Collectors.toList()));
		//付费物资
		response.setSiteItems(new ArrayList<>());
		List<RentalItem> rsiSiteItems = rentalv2Provider
				.findRentalSiteItems(rule.getSourceType(), rule.getId(), rule.getResourceType());
		for (RentalItem rsi : rsiSiteItems) {
			SiteItemDTO dto = convertItem2DTO(rsi);
			response.getSiteItems().add(dto);
		}
		//基础设施
        response.setStructures(new ArrayList<>());
		List<RentalStructure> structures = rentalv2Provider.listRentalStructures(rule.getSourceType(),id,
                rule.getResourceType(),null,null,null);
		for (RentalStructure structure : structures){
            SiteStructureDTO dto = convertStructure2DTO(structure);
		    response.getStructures().add(dto);
        }

//		populateRentalRule(response, ruleType, id);

		return response;
	}


	private PriceRuleDTO convert(Rentalv2PriceRule priceRule) {
		PriceRuleDTO dto =  ConvertHelper.convert(priceRule, PriceRuleDTO.class);
		List<RentalPriceClassification> classifications = rentalv2Provider.listClassification(priceRule.getResourceType(), EhRentalv2PriceRules.class.getSimpleName(),
				priceRule.getId(), null, null, null, null);
		if (classifications != null)
			dto.setClassifications(classifications.stream().map(r->ConvertHelper.convert(r,RentalPriceClassificationDTO.class)).collect(Collectors.toList()));
		return dto;
	}

	private PricePackageDTO convert(Rentalv2PricePackage pricePackage){
		PricePackageDTO dto = ConvertHelper.convert(pricePackage, PricePackageDTO.class);
		List<RentalPriceClassification> classifications = rentalv2Provider.listClassification(pricePackage.getResourceType(), EhRentalv2PricePackages.class.getSimpleName(),
				pricePackage.getId(), null, null, null, null);
		if (classifications != null)
			dto.setClassifications(classifications.stream().map(r->ConvertHelper.convert(r,RentalPriceClassificationDTO.class)).collect(Collectors.toList()));
		return dto;
	}

	//一个资源类型初始化时 添加默认规则
	private void addDefaultRule(String ownerType, Long ownerId, String resourceType, Long resourceTypeId,
								String sourceType, Long sourceId) {
		AddDefaultRuleAdminCommand addCmd = new AddDefaultRuleAdminCommand();
		addCmd.setOwnerType(ownerType);
		addCmd.setOwnerId(ownerId);
		addCmd.setResourceType(resourceType);
		addCmd.setResourceTypeId(resourceTypeId);
		//设置为默认规则类型
		addCmd.setSourceType(sourceType);
		addCmd.setSourceId(sourceId);

//		addCmd.setSiteCounts(1.0);
//		addCmd.setAutoAssign(NormalFlag.NONEED.getCode());
//        addCmd.setMultiUnit(NormalFlag.NONEED.getCode());
		addCmd.setNeedPay(NormalFlag.NONEED.getCode());
		addCmd.setMultiTimeInterval(NormalFlag.NEED.getCode());
		//设置默认开放时间，当前时间+100天
		addCmd.setBeginDate(new java.util.Date().getTime());
		addCmd.setEndDate(new java.util.Date().getTime() + 1000 * 60 * 60 * 24 * 100L);
		//默认不开启
		addCmd.setRentalStartTimeFlag(NormalFlag.NEED.getCode());
		addCmd.setRentalEndTimeFlag(NormalFlag.NONEED.getCode());
		addCmd.setRentalEndTime(0L);
		addCmd.setRentalStartTime(7776000000L); //默认提前三个月
		//设置默认退款
		addCmd.setRefundFlag(NormalFlag.NEED.getCode());
		addCmd.setRefundRatio(30);
		//附件信息
		//节假日设定
		addCmd.setHolidayType(RentalHolidayType.NORMAL_WEEKEND.getCode());
		addCmd.setHolidayOpenFlag((byte)1);
        //附件信息
//        AttachmentConfigDTO attachment = new AttachmentConfigDTO();
//		attachment.setAttachmentType(AttachmentType.ATTACHMENT.getCode());
//		attachment.setMustOptions(NormalFlag.NONEED.getCode());
//		addCmd.setAttachments(Collections.singletonList(attachment));
		//设置每周开放日期
		addCmd.setOpenWeekday(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
		//设置关闭日期
		addCmd.setCloseDates(null);

		addCmd.setPriceRules(buildDefaultPriceRule(Collections.singletonList(RentalType.HOUR.getCode())));
		addCmd.setRentalTypes(Collections.singletonList(RentalType.HOUR.getCode()));
		//设置按小时模式 每天开放时间
		TimeIntervalDTO timeIntervalDTO = new TimeIntervalDTO();
		timeIntervalDTO.setTimeStep(0.5D);
		timeIntervalDTO.setBeginTime(8D);
		timeIntervalDTO.setEndTime(22D);
		addCmd.setTimeIntervals(Collections.singletonList(timeIntervalDTO));

		addCmd.setRefundStrategy(RentalOrderStrategy.CUSTOM.getCode());
		addCmd.setRefundStrategies(createRefundDefaultRules());
		addCmd.setOvertimeStrategy(RentalOrderStrategy.CUSTOM.getCode());
		addCmd.setOvertimeStrategies(createOverTimeDefaultRules());
		addCmd.setStructures(createStructuresDefaultRules());

		this.addRule(addCmd);

	}

	private List<SiteStructureDTO> createStructuresDefaultRules(){
        List<RentalStructureTemplate> templates = this.rentalv2Provider.listRentalStructureTemplates();
        return templates.stream().map(r->{
            SiteStructureDTO dto = new SiteStructureDTO();
            dto.setDefaultOrder(r.getDefaultOrder());
            dto.setDisplayName(r.getDisplayName());
            dto.setIconUri(r.getIconUri());
            dto.setIsSurport((byte)0);
            dto.setName(r.getName());
            dto.setTemplateId(r.getId());
            return dto;
        }).collect(Collectors.toList());
    }

	private List<RentalOrderRuleDTO> createRefundDefaultRules() {

		List<RentalOrderRuleDTO> result = new ArrayList<>();
		RentalOrderRuleDTO dto = new RentalOrderRuleDTO();
		dto.setDurationType(RentalDurationType.OUTER.getCode());
		dto.setDuration(2D);
		dto.setDurationUnit(RentalDurationUnit.HOUR.getCode());
		dto.setFactor(100D);
		dto.setHandleType(RentalOrderHandleType.REFUND.getCode());

		result.add(dto);
		dto = new RentalOrderRuleDTO();
		dto.setDurationType(RentalDurationType.INNER.getCode());
		dto.setDuration(2D);
		dto.setDurationUnit(RentalDurationUnit.HOUR.getCode());
		dto.setFactor(50D);
		dto.setHandleType(RentalOrderHandleType.REFUND.getCode());

		result.add(dto);

		return result;
	}

	private List<RentalOrderRuleDTO> createOverTimeDefaultRules() {

		List<RentalOrderRuleDTO> result = new ArrayList<>();
		RentalOrderRuleDTO dto = new RentalOrderRuleDTO();
		dto.setDurationType(RentalDurationType.INNER.getCode());
		dto.setDuration(1D);
		dto.setDurationUnit(RentalDurationUnit.HOUR.getCode());
		dto.setFactor(2D);
		dto.setHandleType(RentalOrderHandleType.OVERTIME.getCode());

		result.add(dto);
		dto = new RentalOrderRuleDTO();
		dto.setDurationType(RentalDurationType.OUTER.getCode());
		dto.setDuration(1D);
		dto.setDurationUnit(RentalDurationUnit.HOUR.getCode());
		dto.setFactor(4D);
		dto.setHandleType(RentalOrderHandleType.OVERTIME.getCode());

		result.add(dto);

		return result;
	}


	private List<AttachmentConfigDTO> convertAttachments(List<RentalConfigAttachment> attachments) {
		if (null != attachments) {
			return attachments.stream().map(a -> {
				AttachmentConfigDTO rca = ConvertHelper.convert(a, AttachmentConfigDTO.class);

				if (a.getAttachmentType().equals(AttachmentType.GOOD_ITEM.getCode())) {
					List<RentalConfigAttachment> goodItems = rentalv2Provider
							.queryRentalConfigAttachmentByOwner(a.getResourceType(), AttachmentType.GOOD_ITEM.name(), a.getId(), null);

					if (null != goodItems) {
						rca.setGoodItems(goodItems.stream().map(g -> ConvertHelper.convert(g, RentalGoodItem.class))
								.collect(Collectors.toList()));
					}
				} else if (a.getAttachmentType().equals(AttachmentType.RECOMMEND_USER.getCode())) {
					List<RentalConfigAttachment> recommendUsers = rentalv2Provider
							.queryRentalConfigAttachmentByOwner(a.getResourceType(), AttachmentType.RECOMMEND_USER.name(), a.getId(), null);

					if (null != recommendUsers) {
						rca.setRecommendUsers(recommendUsers.stream().map(u -> {
							RentalRecommendUser user = ConvertHelper.convert(u, RentalRecommendUser.class);
							user.setIconUrl(contentServerService.parserUri(u.getIconUri(),
									EntityType.USER.getCode(), User.ROOT_UID));
							return user;
						}).collect(Collectors.toList()));
					}
				} else if (a.getAttachmentType().equals(AttachmentType.TEXT_REMARK.getCode())) {
					if (StringUtils.isBlank(a.getContent())) {

						String locale = UserContext.current().getUser().getLocale();
						if (StringUtils.isBlank(rca.getContent())) {
							String content = localeStringService.getLocalizedString(RentalNotificationTemplateCode.SCOPE,
									String.valueOf(RentalNotificationTemplateCode.RENTAL_TEXT_REMARK), locale, "");
							rca.setContent(content);
						}
					}
				} else if (a.getAttachmentType().equals(AttachmentType.SHOW_CONTENT.getCode())) {
					if (StringUtils.isBlank(a.getContent())) {

						String locale = UserContext.current().getUser().getLocale();

						String content = localeStringService.getLocalizedString(RentalNotificationTemplateCode.SCOPE,
								String.valueOf(RentalNotificationTemplateCode.RENTAL_SHOW_CONTENT), locale, "");
						rca.setContent(content);
					}
				}
				return rca;
			}).collect(Collectors.toList());
		}
		return new ArrayList<>();
	}

	@Override
	public QueryDefaultRuleAdminResponse getResourceRule(GetResourceRuleAdminCommand cmd) {

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}

		RentalResource resource = rentalCommonService.getRentalResource(cmd.getResourceType(), cmd.getResourceId());

		if (null == resource) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERRPR_LOST_RESOURCE_RULE,
					"RentalResource not found");
		}


		QueryDefaultRuleAdminCommand queryRuleCmd = new QueryDefaultRuleAdminCommand();

		queryRuleCmd.setResourceType(resource.getResourceType());
		queryRuleCmd.setResourceTypeId(resource.getResourceTypeId());
		queryRuleCmd.setSourceType(RuleSourceType.RESOURCE.getCode());
		queryRuleCmd.setSourceId(resource.getId());

		return queryDefaultRule(queryRuleCmd);
	}

	private void checkSiteNumber(Byte autoAssign, List<SiteNumberDTO> siteNumbers, Double siteCounts) {
		//当需要手动选择场所时，判断编号数量和场所编号数量是否匹配
		if (autoAssign.equals(NormalFlag.NEED.getCode())) {
			if (siteCounts.intValue() != siteNumbers.size()) {
				throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_INVALID_PARAMETER,
						"Invalid parameter site counts");
			}
			HashSet<SiteNumberDTO> siteNumberSet = new HashSet<>(siteNumbers);
			if (siteCounts.intValue() != siteNumberSet.size()) {
				throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_INVALID_PARAMETER,
						"Invalid parameter site numbers repeat");
			}
		}
	}

	/**
	 * 只修改预订规则基本参数
	 *
	 * @param cmd
	 */
	@Override
	public void updateDefaultRule(UpdateDefaultRuleAdminCommand cmd) {

//		if(null == cmd.getSiteCounts()) {
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
//					ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid parameter siteCounts");
//		}
//
//		checkSiteNumber(cmd.getAutoAssign(), cmd.getSiteNumbers(), cmd.getSiteCounts());

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}

		if (StringUtils.isBlank(cmd.getSourceType())) {
			cmd.setSourceType(RuleSourceType.DEFAULT.getCode());
		}

		if (null == cmd.getRefundFlag()) {
			cmd.setRefundFlag(NormalFlag.NONEED.getCode());
		}

		if (null == cmd.getRentalEndTimeFlag()) {
			cmd.setRentalEndTimeFlag(NormalFlag.NONEED.getCode());
		}

		if (null == cmd.getRentalStartTimeFlag()) {
			cmd.setRentalStartTimeFlag(NormalFlag.NONEED.getCode());
		}
		if (NormalFlag.NONEED.getCode() == cmd.getNeedPay()) {
			cmd.setPriceRules(buildDefaultPriceRule(cmd.getRentalTypes()));
		}

		//用来记录rentalTypes
		if (NormalFlag.NONEED.getCode() == cmd.getNeedPay()) {
			cmd.setPriceRules(buildDefaultPriceRule(cmd.getRentalTypes()));
		}

		RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(cmd.getOwnerType(), cmd.getOwnerId(),
				cmd.getResourceType(), cmd.getResourceTypeId(), cmd.getSourceType(), cmd.getSourceId());

		if (null == rule) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_DEFAULT_RULE_NOT_FOUND, "RentalDefaultRule not found");
		}

		this.dbProvider.execute((TransactionStatus status) -> {
			RentalDefaultRule newRule = ConvertHelper.convert(cmd, RentalDefaultRule.class);

			if (NormalFlag.NONEED.getCode() == cmd.getRentalEndTimeFlag()) {
				newRule.setRentalEndTime(0L);
			}
			if (NormalFlag.NONEED.getCode() == cmd.getRentalStartTimeFlag()) {
				newRule.setRentalStartTime(0L);
			}
			//保持原有规则
			newRule.setOpenWeekday(rule.getOpenWeekday());
			newRule.setBeginDate(rule.getBeginDate());
			newRule.setEndDate(rule.getEndDate());
			newRule.setId(rule.getId());

//			newRule.setResourceCounts(cmd.getSiteCounts());
			this.rentalv2Provider.updateRentalDefaultRule(newRule);

			String priceRuleType = null;
			String ruleType = null;
			String halfRuleType = null;
			Long id = null;
			if (RuleSourceType.DEFAULT.getCode().equals(rule.getSourceType())) {
				priceRuleType = PriceRuleType.DEFAULT.getCode();
				ruleType = EhRentalv2DefaultRules.class.getSimpleName();
				halfRuleType = RentalTimeIntervalOwnerType.DEFAULT_HALF_DAY.getCode();
				id = rule.getId();
			} else if (RuleSourceType.RESOURCE.getCode().equals(rule.getSourceType())) {
				priceRuleType = PriceRuleType.RESOURCE.getCode();
				ruleType = EhRentalv2Resources.class.getSimpleName();
				halfRuleType = RentalTimeIntervalOwnerType.RESOURCE_HALF_DAY.getCode();
				id = rule.getSourceId();
			}

			//先删除后添加
			rentalv2PriceRuleProvider.deletePriceRuleByOwnerId(rule.getResourceType(), priceRuleType, id);
			createPriceRules(rule.getResourceType(), PriceRuleType.fromCode(priceRuleType), id, cmd.getPriceRules());
			//先删除后添加
			rentalv2PricePackageProvider.deletePricePackageByOwnerId(rule.getResourceType(), priceRuleType, id);
			createPricePackages(rule.getResourceType(), PriceRuleType.fromCode(priceRuleType), id, cmd.getPricePackages());

			// set half day time intervals
			//先删除
			rentalv2Provider.deleteTimeIntervalsByOwnerId(rule.getResourceType(), halfRuleType, id);
			setRentalRuleTimeIntervals(rule.getResourceType(), halfRuleType, id, cmd.getHalfDayTimeIntervals());

			//time intervals
			//先删除
			this.rentalv2Provider.deleteTimeIntervalsByOwnerId(rule.getResourceType(), ruleType, id);
			setRentalRuleTimeIntervals(rule.getResourceType(), ruleType, id, cmd.getTimeIntervals());

			return null;
		});
	}

	private void setRentalRuleTimeIntervals(String resourceType, String ownerType, Long ownerId, List<TimeIntervalDTO> timeIntervals) {

		if (null != timeIntervals) {
			timeIntervals.forEach(t -> {
				if (t.getBeginTime() > t.getEndTime()) {
					throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
							RentalServiceErrorCode.ERROR_TIME_STEP, "Invalid parameter");
				}
				RentalTimeInterval timeInterval = ConvertHelper.convert(t, RentalTimeInterval.class);
				timeInterval.setOwnerType(ownerType);
				timeInterval.setOwnerId(ownerId);
				timeInterval.setResourceType(resourceType);
				this.rentalv2Provider.createTimeInterval(timeInterval);
			});
		}
	}

	private void setRentalRuleSiteNumbers(String resourceType, String ownerType, Long ownerId, List<SiteNumberDTO> siteNumberDTOS) {

		if (null != siteNumberDTOS) {
			siteNumberDTOS.forEach(s -> {
				RentalResourceNumber resourceNumber = new RentalResourceNumber();
				resourceNumber.setOwnerType(ownerType);
				resourceNumber.setOwnerId(ownerId);
				resourceNumber.setResourceNumber(s.getSiteNumber());
				resourceNumber.setNumberGroup(s.getSiteNumberGroup());
				resourceNumber.setGroupLockFlag(s.getGroupLockFlag());
				resourceNumber.setResourceType(resourceType);
				this.rentalv2Provider.createRentalResourceNumber(resourceNumber);
			});
		}
	}

	/**
	 * 修改预定规则开放时间
	 *
	 * @param cmd
	 */
	@Override
	public void updateDefaultDateRule(UpdateDefaultDateRuleAdminCommand cmd) {

		if (StringUtils.isBlank(cmd.getSourceType())) {
			cmd.setSourceType(RuleSourceType.DEFAULT.getCode());
		}

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}

		RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(cmd.getOwnerType(), cmd.getOwnerId(),
				cmd.getResourceType(), cmd.getResourceTypeId(), cmd.getSourceType(), cmd.getSourceId());
		if (null == rule) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_DEFAULT_RULE_NOT_FOUND,
					"Rule not found!");
		}
		this.dbProvider.execute((TransactionStatus status) -> {

			//设置开放时间
			rule.setOpenWeekday(convertOpenWeekday(cmd.getOpenWeekday()));
			rule.setBeginDate(new Date(cmd.getBeginDate()));
			rule.setEndDate(new Date(cmd.getEndDate()));

			this.rentalv2Provider.updateRentalDefaultRule(rule);

			String ruleType = null;
			Long id = null;
			if (RuleSourceType.DEFAULT.getCode().equals(rule.getSourceType())) {
				ruleType = EhRentalv2DefaultRules.class.getSimpleName();
				id = rule.getId();
			} else if (RuleSourceType.RESOURCE.getCode().equals(rule.getSourceType())) {
				ruleType = EhRentalv2Resources.class.getSimpleName();
				id = rule.getSourceId();
			}

			//设置关闭日期close dates
			rentalv2Provider.deleteRentalCloseDatesByOwnerId(rule.getResourceType(), ruleType, id);
			setRentalRuleCloseDates(cmd.getCloseDates(), id, ruleType, rule.getResourceType());

			return null;
		});
	}

	//close dates
	private void setRentalRuleCloseDates(List<Long> closeDates, Long ownerId, String ownerType, String resourceType) {

		if (null != closeDates) {
			closeDates.forEach(c -> {
				RentalCloseDate rcd = new RentalCloseDate();
				rcd.setCloseDate(new Date(c));
				rcd.setOwnerType(ownerType);
				rcd.setOwnerId(ownerId);
				rcd.setResourceType(resourceType);
				this.rentalv2Provider.createRentalCloseDate(rcd);
			});
		}
	}

	private void setRentalDayopenTime(List<RentalOpenTimeDTO> openTime, String ownerType, Long ownerId, String resourceType) {
		if (null != openTime) {
			openTime.forEach(c -> {
				RentalDayopenTime dayopenTime = new RentalDayopenTime();
				dayopenTime.setOwnerId(ownerId);
				dayopenTime.setOwnerType(ownerType);
				dayopenTime.setOpenTime(c.getDayOpenTime());
				dayopenTime.setCloseTime(c.getDayCloseTime());
				dayopenTime.setRentalType(c.getRentalType());
				dayopenTime.setResourceType(resourceType);
				this.rentalv2Provider.createRentalDayopenTime(dayopenTime);
			});
		}
	}

	@Override
	public void addItem(AddItemAdminCommand cmd) {
		if (null == cmd.getItemType()) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_LOST_PARAMETER,
					"Invalid parameter item type can not be null");
		}

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}

		String ownerType = null;
		Long ownerId = null;
		if (RuleSourceType.DEFAULT.getCode().equals(cmd.getSourceType())) {
			ownerType = RuleSourceType.DEFAULT.getCode();
			RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(cmd.getOwnerType(), cmd.getOwnerId(),
					cmd.getResourceType(), cmd.getResourceTypeId(), cmd.getSourceType(), cmd.getSourceId());
			ownerId = rule.getId();
		} else if (RuleSourceType.RESOURCE.getCode().equals(cmd.getSourceType())) {
			RentalResource rs = rentalCommonService.getRentalResource(cmd.getResourceType(), cmd.getSourceId());
			ownerType = RuleSourceType.RESOURCE.getCode();
			ownerId = rs.getId();
		}

		RentalItem siteItem = ConvertHelper.convert(cmd, RentalItem.class);
		siteItem.setName(cmd.getItemName());
		siteItem.setSourceType(ownerType);
		siteItem.setSourceId(ownerId);
		siteItem.setPrice(cmd.getItemPrice());
		rentalv2Provider.createRentalSiteItem(siteItem);
	}

	@Override
	public FindRentalSiteItemsAndAttachmentsResponse findRentalSiteItems(FindRentalSiteItemsAndAttachmentsCommand cmd) {
		if (null == cmd.getRentalSiteId()) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_LOST_PARAMETER,
					"Invalid parameter site id can not be null");
		}
		if (null == cmd.getRentalSiteRuleIds()) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_LOST_PARAMETER,
					"Invalid parameter rule ids can not be null");
		}

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}

		FindRentalSiteItemsAndAttachmentsResponse response = new FindRentalSiteItemsAndAttachmentsResponse();
		response.setSiteItems(new ArrayList<>());
		List<RentalItem> rsiSiteItems = rentalv2Provider.findRentalSiteItems(RuleSourceType.RESOURCE.getCode(), cmd.getRentalSiteId(),
				cmd.getResourceType());
		if (rsiSiteItems != null && rsiSiteItems.size() > 0)
			for (RentalItem rsi : rsiSiteItems) {
				SiteItemDTO dto = convertItem2DTO(rsi);
				response.getSiteItems().add(dto);
			}

		List<RentalConfigAttachment> attachments = this.rentalv2Provider.queryRentalConfigAttachmentByOwner(cmd.getResourceType(),
				EhRentalv2Resources.class.getSimpleName(), cmd.getRentalSiteId(), null);
		RentalResource rs = this.rentalv2Provider.getRentalSiteById(cmd.getRentalSiteId());

		//删掉除物资 和 推销员以外的内容
		attachments = attachments.stream().filter(r -> r.getAttachmentType() > AttachmentType.ATTACHMENT.getCode()).collect(Collectors.toList());
		setAttachmentsByResource(attachments, rs);
		response.setAttachments(convertAttachments(attachments));

		return response;
	}

	//根据业务设置自定义提交内容
	private void setAttachmentsByResource(List<RentalConfigAttachment> attachments, RentalResource rs) {
		RentalResourceType resourceType = this.rentalv2Provider.getRentalResourceTypeById(rs.getResourceTypeId());
		RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(null, null,
				rs.getResourceType(), rs.getResourceTypeId(), RuleSourceType.RESOURCE.getCode(), rs.getId());
		RentalConfigAttachment attachment = new RentalConfigAttachment();
		attachment.setAttachmentType(AttachmentType.TEXT_REMARK.getCode());
		attachment.setContent(rule.getRemark());
		attachment.setMustOptions(rule.getRemarkFlag() == null ? (byte)0 : rule.getRemarkFlag());
		attachments.add(attachment);
		if (RentalV2ResourceType.SCREEN.getCode().equals(resourceType.getIdentify())) {
			attachment = new RentalConfigAttachment();
			attachment.setAttachmentType(AttachmentType.SHOW_CONTENT.getCode());
			attachment.setMustOptions((byte) 0);
			attachments.add(attachment);
		}
	}

	@Override
	public FindRentalSitesCommandResponse findRentalSites(FindRentalSitesCommand cmd) {

		if(null==cmd.getResourceTypeId()||null==cmd.getOwnerId()||null==cmd.getOwnerType()) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_LOST_PARAMETER,
					"Invalid parameter ResourceTypeId, OwnerId, OwnerType");
		}

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}

		FindRentalSitesCommandResponse response = new FindRentalSitesCommandResponse();

		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getAnchor());

		List<Long> siteIds = new ArrayList<>();
		List<RentalSiteRange> siteOwners = this.rentalv2Provider.findRentalSiteOwnersByOwnerTypeAndId(cmd.getResourceType(),
				cmd.getOwnerType(), cmd.getOwnerId());
		if (siteOwners != null)
			for (RentalSiteRange siteOwner : siteOwners) {
				siteIds.add(siteOwner.getRentalResourceId());
			}
		checkEnterpriseCommunityIdIsNull(cmd.getOwnerId());

		//为了降低运算量 在筛选过程前筛一遍 降低资源个数
		List<RentalResource> rentalSites = rentalv2Provider.findRentalSites(cmd.getResourceTypeId(), cmd.getKeyword(),
				locator, Integer.MAX_VALUE, RentalSiteStatus.NORMAL.getCode(), siteIds, cmd.getCommunityId());
		rentalSites = filteRentalSites(rentalSites,cmd);
        if (null == rentalSites){
            response.setRentalSites(new ArrayList<>());
            return response;
        }
		siteIds = rentalSites.stream().map(RentalResource::getId).collect(Collectors.toList());
		siteIds = filteRentalSitesByTime(siteIds,cmd);
        rentalSites = rentalv2Provider.findRentalSites(cmd.getResourceTypeId(), cmd.getKeyword(),
				locator, pageSize + 1, RentalSiteStatus.NORMAL.getCode(), siteIds, cmd.getCommunityId());

		if (null == rentalSites)
			return response;

		Long nextPageAnchor = null;
		if (rentalSites.size() > pageSize) {
			rentalSites.remove(rentalSites.size() - 1);
			nextPageAnchor = rentalSites.get(rentalSites.size() - 1).getId();
		}
		response.setNextPageAnchor(nextPageAnchor);
		response.setRentalSites(new ArrayList<>());

		currentSceneType.set(cmd.getSceneType());
		for (RentalResource rentalSite : rentalSites) {
			RentalSiteDTO rSiteDTO = convertRentalSite2DTO(rentalSite,  true);
			if (cmd.getShowTimeStart() != null){
                rSiteDTO.setEnableTimeRanges(getEnableTimeRanges(rentalSite,cmd.getShowTimeStart(),cmd.getShowTimeEnd()));
            }
			//退款提示
			RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(null, null,
					rentalSite.getResourceType(), rentalSite.getResourceTypeId(), RuleSourceType.RESOURCE.getCode(), rentalSite.getId());
			rSiteDTO.setRefundTip(getRefundTipByRule(rule,rentalSite.getId()));
			//帮客户端处理一下 颠倒按月按周预约规则的位置
			List<SitePriceRuleDTO> sitePriceRules = rSiteDTO.getSitePriceRules();
			int i = 0;
			for (;i<sitePriceRules.size();i++)
				if (RentalType.MONTH.getCode() == sitePriceRules.get(i).getRentalType())
					break;
			int j = 0;
			for (;j<sitePriceRules.size();j++)
				if (RentalType.WEEK.getCode() == sitePriceRules.get(j).getRentalType())
					break;
			if (i<sitePriceRules.size() && j<sitePriceRules.size()){
				sitePriceRules.add(sitePriceRules.get(i));
				sitePriceRules.remove(i);
			}
			response.getRentalSites().add(rSiteDTO);
		}


		return response;
	}

	private List<RangeDTO> getEnableTimeRanges(RentalResource rentalSite, Long showTimeStart, Long showTimeEnd){
        //根据节假日
        List<RentalCloseDate> closeDates = rentalv2Provider.queryRentalCloseDateByOwner(rentalSite.getResourceType(),
                EhRentalv2Resources.class.getSimpleName(), rentalSite.getId(),new Date(showTimeStart),new Date(showTimeEnd));
        if (closeDates != null && closeDates.size() > 0) {
				return new ArrayList<>();
		}
        SegmentTree segmentTree = new SegmentTree();

        //查看格子被关闭
        List<RentalCell> rentalCells = rentalv2Provider.findCellClosedByTimeInterval(rentalSite.getResourceType(), rentalSite.getId(), showTimeStart, showTimeEnd);
        if (rentalCells != null && rentalCells.size() > 0)
            for (RentalCell cell : rentalCells)
                if (cell.getBeginTime() != null && cell.getEndTime() != null)
                    segmentTree.putSegment(cell.getBeginTime().getTime(),cell.getEndTime().getTime(),1);


        //不允许预约的时间
        RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(null, null,
                rentalSite.getResourceType(), rentalSite.getResourceTypeId(), RuleSourceType.RESOURCE.getCode(), rentalSite.getId());
        Long now = new java.util.Date().getTime();
        now = now - now % (1800 * 1000) + 1800 * 1000;
        //至多提前时间
        if (NormalFlag.NEED.getCode() == rule.getRentalStartTimeFlag()) {
            Long time = now + rule.getRentalStartTime();
            if (time < showTimeEnd) {
                segmentTree.putSegment(time,showTimeEnd,1);
            }
        }
        //至少提前时间
        Long time = now;
        if (NormalFlag.NEED.getCode() == rule.getRentalEndTimeFlag()) {
            time = now + rule.getRentalEndTime();
        }
        if (time > showTimeStart) {
            segmentTree.putSegment(showTimeStart, time, 1);
        }
        //是否有格子被预约
        List<RentalOrder> rentalOrders = rentalv2Provider.listActiveBillsByInterval(rentalSite.getId(), showTimeStart, showTimeEnd);
        if (rentalOrders != null && rentalOrders.size()>0) {
            for (RentalOrder order:rentalOrders)
                segmentTree.putSegment(order.getStartTime().getTime(),order.getEndTime().getTime(),1);

        }

        //按小时预约开放时间段
        List<RentalTimeInterval> timeIntervals = rentalv2Provider.queryRentalTimeIntervalByOwner(rentalSite.getResourceType(),
                EhRentalv2Resources.class.getSimpleName(), rentalSite.getId());
        if (timeIntervals != null && timeIntervals.size() > 0){
            SegmentTree tree2 = new SegmentTree();
            for (RentalTimeInterval timeInterval : timeIntervals) {
                Double start = timeInterval.getBeginTime() * 3600 * 1000;
                Double end = timeInterval.getEndTime() * 3600 * 1000;
                tree2.putSegment(start.longValue(),end.longValue(),1);
            }
            List<SegmentNode> zeroConverNodes = tree2.getZeroConverNodes(0, 24 * 3600 * 1000);
            for (SegmentNode node : zeroConverNodes){
                segmentTree.putSegment(showTimeStart + node.getLborder(),showTimeStart + node.getRborder(),1);
            }
        }

        List<SegmentNode> zeroConverNodes = segmentTree.getZeroConverNodes(showTimeStart, showTimeEnd);

        return zeroConverNodes.stream().map(r->{
            RangeDTO dto = new RangeDTO();
            dto.setRangeStart(r.getLborder());
            dto.setRangeEnd(r.getRborder());
            return dto;
        }).collect(Collectors.toList());
    }

	private List<RentalResource> filteRentalSites(List<RentalResource> resources,FindRentalSitesCommand cmd){
		if (resources == null || resources.size()==0)
			return null;
		if (cmd.getPeopleSpecLeast() != null)
			resources = resources.stream().filter(r->r.getPeopleSpec() != null && r.getPeopleSpec()>=cmd.getPeopleSpecLeast()).collect(Collectors.toList());
		if (cmd.getPeopleSpecMost() != null)
			resources = resources.stream().filter(r->r.getPeopleSpec() != null && r.getPeopleSpec()<=cmd.getPeopleSpecMost()).collect(Collectors.toList());
		if (cmd.getStructureList() != null && cmd.getStructureList().size() > 0)
			resources = resources.stream().filter(r->{
				List<RentalStructure> rentalStructures = rentalv2Provider
							.listRentalStructures(RuleSourceType.RESOURCE.getCode(), r.getId(), cmd.getResourceType(), (byte)1,null, null);
				if (rentalStructures == null)
					return false;
				List<Long> templateIds = rentalStructures.stream().map(RentalStructure::getTemplateId).collect(Collectors.toList());
				for (Long id : cmd.getStructureList())
					if (!templateIds.contains(id))
						return false;
				return true;
			}).collect(Collectors.toList());
		return resources;
	}

	private List<Long> filteRentalSitesByTime(List<Long> siteIds,FindRentalSitesCommand cmd){
		if (cmd.getRentalType() == null)
			return siteIds;
		//根据预订类型筛选
		siteIds = siteIds.stream().filter(r->{
			List<Rentalv2PriceRule> priceRules = rentalv2PriceRuleProvider.listPriceRuleByOwner(cmd.getResourceType(),
					PriceRuleType.RESOURCE.getCode(), r);
			return priceRules.stream().map(p->p.getRentalType()).anyMatch(p->p.equals(cmd.getRentalType()));
		}).collect(Collectors.toList());
		//根据节假日是否关闭筛选
        if (cmd.getRentalType()<=3){
            siteIds = siteIds.stream().filter(r-> {
                List<RentalCloseDate> closeDates = rentalv2Provider.queryRentalCloseDateByOwner(cmd.getResourceType(),
                        EhRentalv2Resources.class.getSimpleName(), r,new Date(cmd.getStartTime()),new Date(cmd.getEndTime()));
                if (closeDates == null || closeDates.size() == 0)
                    return true;
                    return false;
            }).collect(Collectors.toList());
        }

		return siteIds.stream().filter(r->{
			Long startTime = cmd.getStartTime();
			Long endTime = cmd.getEndTime();
			if (cmd.getRentalType().equals(RentalType.HALFDAY.getCode())||cmd.getRentalType().equals(RentalType.THREETIMEADAY.getCode())){
				List<RentalTimeInterval> halfTimeIntervals = rentalv2Provider.queryRentalTimeIntervalByOwner(cmd.getResourceType(),
						RentalTimeIntervalOwnerType.RESOURCE_HALF_DAY.getCode(), r);
				if (halfTimeIntervals.size()<3)
					halfTimeIntervals.add(halfTimeIntervals.get(1));
				startTime += Double.valueOf(halfTimeIntervals.get(cmd.getStartTimeAmOrPm()).getBeginTime()*3600*1000).longValue();
				endTime += Double.valueOf(halfTimeIntervals.get(cmd.getEndTimeAmOrPm()).getBeginTime()*3600*1000).longValue();
			}
			//是否有格子被关闭
			List<RentalCell> rentalCells = rentalv2Provider.findCellClosedByTimeInterval(cmd.getResourceType(), r, startTime, endTime);
			if (rentalCells != null && rentalCells.size()>0)
				return false;

			//是否有不允许预约的时间
			RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(null, null,
					cmd.getResourceType(), cmd.getResourceTypeId(), RuleSourceType.RESOURCE.getCode(), r);
			java.util.Date now = new java.util.Date();
			LocalDateTime startDateTime = new java.util.Date(startTime).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			LocalDateTime endDateTime = new java.util.Date(endTime).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			if (cmd.getRentalType().equals(RentalType.MONTH.getCode()))
				endDateTime = endDateTime.minusMonths(1l);
			else if (cmd.getRentalType().equals(RentalType.WEEK.getCode()))
				endDateTime = endDateTime.minusWeeks(1l);
			else if (cmd.getRentalType().equals(RentalType.DAY.getCode()))
				endDateTime = endDateTime.minusDays(1l);
			//至多提前时间，筛选结束时间减去至多提前时间，表示最早可以预订的时间
			if (NormalFlag.NEED.getCode() == rule.getRentalStartTimeFlag()) {
				Long time = endDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - rule.getRentalStartTime();
				if (now.before(new java.util.Date(time))) {
					return false;
				}
			}
			//至少提前时间，筛选开始时间减去至少提前时间，表示最晚可以预订的时间
			if (NormalFlag.NEED.getCode() == rule.getRentalEndTimeFlag()) {
				Long time = startDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - rule.getRentalEndTime();
				if (now.after(new java.util.Date(time))) {
					return false;
				}
			}

			//是否有格子被预约
			List<RentalOrder> rentalOrders = rentalv2Provider.listActiveBillsByInterval(r, startTime, endTime);
			if (rentalOrders != null && rentalOrders.size()>0) {
				RentalResource resource = rentalv2Provider.getRentalSiteById(r);
				SegmentTree segmentTree = new SegmentTree();
				for (RentalOrder order:rentalOrders)
					segmentTree.putSegment(order.getStartTime().getTime(),order.getEndTime().getTime(),order.getRentalCount().intValue());
				if (segmentTree.getMaxCover(startTime,endTime) > resource.getResourceCounts())
					return false;

			}
			return true;
		}).collect(Collectors.toList());

	}

	private RentalSiteDTO convertRentalSite2DTO(RentalResource rentalSite, boolean setShowPriceFlag){
		RentalResourceType resourceType = rentalv2Provider.getRentalResourceTypeById(rentalSite.getResourceTypeId());

		RentalSiteDTO rSiteDTO = convertToDTO(rentalSite, resourceType);
		//兼容老app，把规则信息填充到资源dto里面
		RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(null, null,
				rentalSite.getResourceType(), rentalSite.getResourceTypeId(), RuleSourceType.RESOURCE.getCode(), rentalSite.getId());
		org.springframework.beans.BeanUtils.copyProperties(rule, rSiteDTO);

		String homeUrl = configurationProvider.getValue(ConfigConstants.HOME_URL, "");
		String detailUrl = configurationProvider.getValue(ConfigConstants.RENTAL_RESOURCE_DETAIL_URL, "");
		Byte detailPageType = resourceType.getDetailPageType() == null ? (byte)0 : resourceType.getDetailPageType();
		detailUrl = String.format(detailUrl, UserContext.getCurrentNamespaceId(), rentalSite.getId(),detailPageType.toString());


		rSiteDTO.setDetailUrl(homeUrl + detailUrl);
		rSiteDTO.setResourceTypeId(resourceType.getId());
		rSiteDTO.setPayMode(resourceType.getPayMode());
		rSiteDTO.setIdentify(resourceType.getIdentify());
		rSiteDTO.setAclinkId(rentalSite.getAclinkId());
		if (!StringUtils.isEmpty(rSiteDTO.getAclinkId())) {
			String[] ids = rSiteDTO.getAclinkId().split(",");
			if (ids.length > 0) {
				String aclinkName = "";
				for (String id : ids)
					aclinkName += doorAccessProvider.getDoorAccessById(Long.parseLong(id)).getDisplayName() + ",";
				rSiteDTO.setAclinkName(aclinkName.substring(0, aclinkName.length() - 1));
			}
		}
//		if(null!=rentalSite.getDayBeginTime()) {
//			rSiteDTO.setDayBeginTime(convertTimeToGMTMillisecond(rentalSite.getDayBeginTime()));
//		}
//		if(null!=rentalSite.getDayEndTime()) {
//			rSiteDTO.setDayEndTime(convertTimeToGMTMillisecond(rentalSite.getDayEndTime()));
//		}
		rSiteDTO.setRentalSiteId(rentalSite.getId());
		rSiteDTO.setSiteName(rentalSite.getResourceName());
		if (null != rentalSite.getChargeUid()) {
		    String [] chargers = rentalSite.getChargeUid().split(",");
		    StringBuilder chargeName = new StringBuilder();
		    for (String uid : chargers){
                User charger = this.userProvider.findUserById(Long.valueOf(uid));
                OrganizationMember member = organizationProvider.findOrganizationMemberByUIdAndOrgId(Long.valueOf(uid), rentalSite.getOrganizationId());
                if (member != null) {
                    chargeName.append(member.getContactName()).append(",");
                } else if (null != charger) {
                    chargeName.append(charger.getNickName()).append(",");
                }
            }
            rSiteDTO.setChargeName(chargeName.deleteCharAt(chargeName.length() - 1).toString());
		}

		if (null != rentalSite.getOfflinePayeeUid()) {
			OrganizationMember member = organizationProvider.findOrganizationMemberByUIdAndOrgId(rSiteDTO.getOfflinePayeeUid(), rentalSite.getOrganizationId());
			if (null != member) {
				rSiteDTO.setOfflinePayeeName(member.getContactName());
			}
		}
		Community community = this.communityProvider.findCommunityById(rSiteDTO.getCommunityId());
		if (null != community) {
			rSiteDTO.setCommunityName(community.getName());
		}

		if (null != rentalSite.getCreateTime()) {
			rSiteDTO.setCreateTime(rentalSite.getCreateTime().getTime());
		}
		if (null != rSiteDTO.getCoverUri())
			rSiteDTO.setCoverUrl(this.contentServerService.parserUri(rSiteDTO.getCoverUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId()));
		List<RentalResourcePic> pics = this.rentalv2Provider.findRentalSitePicsByOwnerTypeAndId(rentalSite.getResourceType(),
				EhRentalv2Resources.class.getSimpleName(), rentalSite.getId());
		if (null != pics)
			rSiteDTO.setSitePics(convertRentalSitePicDTOs(pics));
		List<RentalResourceFile> files = this.rentalv2Provider.findRentalSiteFilesByOwnerTypeAndId(rentalSite.getResourceType(),
				EhRentalv2Resources.class.getSimpleName(), rentalSite.getId());
		if (null != files)
			rSiteDTO.setSiteFiles(convertRentalSiteFileDTOs(files));
		rSiteDTO.setSiteCounts(rentalSite.getResourceCounts());
		if (rentalSite.getAutoAssign().equals(NormalFlag.NEED.getCode())) {
			List<RentalResourceNumber> resourceNumbers = this.rentalv2Provider.queryRentalResourceNumbersByOwner(
					rentalSite.getResourceType(), EhRentalv2Resources.class.getSimpleName(), rentalSite.getId());
			if (null != resourceNumbers) {
				rSiteDTO.setSiteNumbers(new ArrayList<>());
				for (RentalResourceNumber number : resourceNumbers) {
					SiteNumberDTO dto = new SiteNumberDTO();
					dto.setSiteNumber(number.getResourceNumber());
					dto.setSiteNumberGroup(number.getNumberGroup());
					dto.setGroupLockFlag(number.getGroupLockFlag());
					rSiteDTO.getSiteNumbers().add(dto);
				}
			}
		}
		//基础设施
		List<RentalStructure> rentalStructures = rentalv2Provider
				.listRentalStructures(RuleSourceType.RESOURCE.getCode(), rentalSite.getId(), rentalSite.getResourceType(),(byte)1, null, null);
		if (rentalStructures != null && rentalStructures.size() > 0){
			rSiteDTO.setStructures(new ArrayList<>());
			for (RentalStructure rst : rentalStructures) {
				SiteStructureDTO dto = convertStructure2DTO(rst);
				rSiteDTO.getStructures().add(dto);
			}
		}
		//范围
		List<RentalSiteRange> owners = this.rentalv2Provider.findRentalSiteOwnersBySiteId(rentalSite.getResourceType(),
				rentalSite.getId());
		if (null != owners) {
			rSiteDTO.setOwners(new ArrayList<>());
			for (RentalSiteRange owner : owners) {
				SiteOwnerDTO dto = ConvertHelper.convert(owner, SiteOwnerDTO.class);
				Community ownerCom = this.communityProvider.findCommunityById(owner.getOwnerId());
				if (null != ownerCom)
					dto.setOwnerName(ownerCom.getName());
				rSiteDTO.getOwners().add(dto);
			}
		}

		//帮客户端处理一下 颠倒按月按周预约规则的位置
		List<SitePriceRuleDTO> sitePriceRules = rSiteDTO.getSitePriceRules();
		int i = 0;
		for (;i<sitePriceRules.size();i++)
			if (RentalType.MONTH.getCode() == sitePriceRules.get(i).getRentalType())
				break;
		int j = 0;
		for (;j<sitePriceRules.size();j++)
			if (RentalType.WEEK.getCode() == sitePriceRules.get(j).getRentalType())
				break;
		if (i<sitePriceRules.size() && j<sitePriceRules.size()){
			sitePriceRules.add(sitePriceRules.get(i));
			sitePriceRules.remove(i);
		}

		if (setShowPriceFlag) {
			//计算显示价格
			setShowPrice(rSiteDTO);
		}

		return rSiteDTO;
	}

	private void setShowPrice(RentalSiteDTO rSiteDTO) {
		String scene = getClassification(RentalUserPriceType.USER_TYPE.getCode());
		if (!StringUtils.isBlank(scene)) {
			if (!SceneType.PM_ADMIN.getCode().equals(scene)
					&& !SceneType.ENTERPRISE.getCode().equals(scene)
					&& TrueOrFalseFlag.fromCode(rSiteDTO.getUnauthVisible()) != TrueOrFalseFlag.TRUE) {
				rSiteDTO.setAvgPriceStr("价格认证可见");
				return;
			}
		}else if (TrueOrFalseFlag.fromCode(rSiteDTO.getUnauthVisible()) != TrueOrFalseFlag.TRUE){
			rSiteDTO.setAvgPriceStr("价格认证可见");
			return;
		}

		if (rSiteDTO.getNeedPay() == NormalFlag.NONEED.getCode()) {
			rSiteDTO.setAvgPriceStr("免费");
			return;
		}
		List<SitePriceRuleDTO> sitePriceRuleDTOs = rSiteDTO.getSitePriceRules();
		if (sitePriceRuleDTOs.size() == 1) {
			rSiteDTO.setAvgPriceStr(sitePriceRuleDTOs.get(0).getPriceStr());
		} else {
			BigDecimal minPrice = new BigDecimal(Integer.MAX_VALUE);
			for (SitePriceRuleDTO sitePriceRuleDTO : sitePriceRuleDTOs) {
				if (sitePriceRuleDTO.getMinPrice() != null && sitePriceRuleDTO.getMinPrice().compareTo(minPrice) < 0) {
					minPrice = sitePriceRuleDTO.getMinPrice();
				}
			}
			if (minPrice.compareTo(new BigDecimal(0)) == 0 || minPrice.intValue() == Integer.MAX_VALUE) {
				rSiteDTO.setAvgPriceStr("最低免费");
			} else {
				String priceString = isInteger(minPrice) ? String.valueOf(minPrice.intValue()) : minPrice.toString();
				rSiteDTO.setAvgPriceStr("￥" + priceString + " 起");
			}
		}
	}

	private RentalSiteDTO convertToDTO(RentalResource rentalSite, RentalResourceType resourceType) {
		List<Rentalv2PricePackage> pricePackages = rentalv2PricePackageProvider.listPricePackageByOwner(rentalSite.getResourceType(),
				PriceRuleType.RESOURCE.getCode(), rentalSite.getId(), null, null);

		List<Rentalv2PriceRule> priceRules = rentalv2PriceRuleProvider.listPriceRuleByOwner(rentalSite.getResourceType(),
				PriceRuleType.RESOURCE.getCode(), rentalSite.getId());

		RentalSiteDTO rentalSiteDTO = ConvertHelper.convert(rentalSite, RentalSiteDTO.class);
		rentalSiteDTO.setUnauthVisible(resourceType.getUnauthVisible());

		if (priceRules.size() > 0) {
			Rentalv2PriceRule priceRule = priceRules.get(0);
			rentalSiteDTO.setRentalType(priceRule.getRentalType());
		}

		rentalSiteDTO.setSitePriceRules(priceRules.stream().map(p -> convertToSitePriceRuleDTO(rentalSite, p, resourceType))
				.collect(Collectors.toList()));

		//添加套餐
		List<SitePriceRuleDTO> sitePriceRules = new ArrayList<>();
		SitePriceRuleDTO rule = convertToSitePackageDTO(pricePackages.stream().filter(p -> p.getRentalType().equals(RentalType.HOUR.getCode())).
					collect(Collectors.toList()), rentalSite, resourceType);
		if (rule != null) {//删掉普通定价
			removeSitePriceRules(rentalSiteDTO.getSitePriceRules(), RentalType.HOUR.getCode());
			sitePriceRules.add(rule);
		}
		rule = convertToSitePackageDTO(pricePackages.stream().filter(p -> p.getRentalType().equals(RentalType.DAY.getCode())).
					collect(Collectors.toList()), rentalSite, resourceType);
		if (rule != null) {//删掉普通定价
			removeSitePriceRules(rentalSiteDTO.getSitePriceRules(), RentalType.DAY.getCode());
			sitePriceRules.add(rule);
		}

		rule = convertToSitePackageDTO(pricePackages.stream().filter(p -> p.getRentalType().equals(RentalType.HALFDAY.getCode())).
                    collect(Collectors.toList()), rentalSite, resourceType);
		if (rule != null) {//删掉普通定价
			removeSitePriceRules(rentalSiteDTO.getSitePriceRules(), RentalType.HALFDAY.getCode());
			sitePriceRules.add(rule);
		}

		rule = convertToSitePackageDTO(pricePackages.stream().filter(p -> p.getRentalType().equals(RentalType.THREETIMEADAY.getCode())).
					collect(Collectors.toList()), rentalSite, resourceType);
		if (rule != null) {//删掉普通定价
			removeSitePriceRules(rentalSiteDTO.getSitePriceRules(), RentalType.THREETIMEADAY.getCode());
			sitePriceRules.add(rule);
		}
		rule = convertToSitePackageDTO(pricePackages.stream().filter(p -> p.getRentalType().equals(RentalType.MONTH.getCode())).
                    collect(Collectors.toList()), rentalSite, resourceType);
		if (rule != null) {//删掉普通定价
			removeSitePriceRules(rentalSiteDTO.getSitePriceRules(), RentalType.MONTH.getCode());
			sitePriceRules.add(rule);
		}
		rule = convertToSitePackageDTO(pricePackages.stream().filter(p -> p.getRentalType().equals(RentalType.WEEK.getCode())).
                    collect(Collectors.toList()), rentalSite, resourceType);
		if (rule != null) {//删掉普通定价
			removeSitePriceRules(rentalSiteDTO.getSitePriceRules(), RentalType.WEEK.getCode());
			sitePriceRules.add(rule);
		}
		rentalSiteDTO.getSitePriceRules().addAll(sitePriceRules);

		return rentalSiteDTO;

	}

	private void removeSitePriceRules(List<SitePriceRuleDTO> sitePriceRules, Byte rentalType) {
		if (sitePriceRules == null || sitePriceRules.size() == 0)
			return;
		int t = -1;
		for (int i = 0; i < sitePriceRules.size(); i++)
			if (sitePriceRules.get(i).getRentalType().equals(rentalType)) {
				t = i;
				break;
			}
		if (t != -1)
			sitePriceRules.remove(t);
	}

	//根据套餐算出价格区间
	private SitePriceRuleDTO convertToSitePackageDTO(List<Rentalv2PricePackage> pricePackages, RentalResource rentalSite, RentalResourceType resourceType) {

		if (pricePackages == null || pricePackages.size() == 0)
			return null;
		SitePriceRuleDTO sitePriceRuleDTO = new SitePriceRuleDTO();
		sitePriceRuleDTO.setRentalType(pricePackages.get(0).getRentalType());
		sitePriceRuleDTO.setPriceType(pricePackages.get(0).getPriceType());
		String scene = getClassification(pricePackages.get(0).getUserPriceType());
		List<Long> packageIds = rentalv2Provider.listCellPackageId(rentalSite.getResourceType(), rentalSite.getId(),
				pricePackages.get(0).getRentalType());
		BigDecimal maxPrice = new BigDecimal(0);
		BigDecimal minPrice = new BigDecimal(Integer.MAX_VALUE);
		if (SceneType.PARK_TOURIST.getCode().equals(scene) && TrueOrFalseFlag.fromCode(resourceType.getUnauthVisible()) == TrueOrFalseFlag.FALSE){
			sitePriceRuleDTO.setPriceStr("");
		}else if (RentalUserPriceType.UNIFICATION.getCode() == pricePackages.get(0).getUserPriceType()) { // 统一价格
			MaxMinPrice maxMinPrice = rentalv2PricePackageProvider.findMaxMinPrice(packageIds, pricePackages.get(0).getRentalType(), null);
			for (Rentalv2PricePackage pricePackage : pricePackages){
				maxPrice = max(pricePackage.getPrice(),maxMinPrice.getMaxPrice(),maxPrice);
				minPrice = min(pricePackage.getPrice(),maxMinPrice.getMinPrice(),minPrice);
			}
			sitePriceRuleDTO.setMaxPrice(maxPrice);
			sitePriceRuleDTO.setMinPrice(minPrice);
			sitePriceRuleDTO.setPriceStr(getPriceStr(maxPrice, minPrice, pricePackages.get(0).getRentalType(),pricePackages.get(0).getPriceType(), PRICE_TIME_STEP));
		}else{
			MaxMinPrice maxMinPrice =  rentalv2Provider.findMaxMinPriceByClassifycation(rentalSite.getResourceType(), EhRentalv2PricePackages.class.getSimpleName(), pricePackages.stream().map(Rentalv2PricePackage::getId).collect(Collectors.toList()),
					PriceRuleType.RESOURCE.getCode(), rentalSite.getId(), pricePackages.get(0).getUserPriceType(), scene);
			maxPrice = maxMinPrice.getMaxPrice();
			minPrice = maxMinPrice.getMinPrice();
			maxMinPrice = rentalv2Provider.findMaxMinPriceByClassifycation(rentalSite.getResourceType(), EhRentalv2PricePackages.class.getSimpleName(), packageIds,
						PriceRuleType.RESOURCE.getCode(), rentalSite.getId(), pricePackages.get(0).getUserPriceType(), scene);
			maxPrice = max(maxPrice,maxMinPrice.getMaxPrice());
			minPrice = min(minPrice,maxMinPrice.getMinPrice());

			sitePriceRuleDTO.setMaxPrice(maxPrice);
			sitePriceRuleDTO.setMinPrice(minPrice);
			sitePriceRuleDTO.setPriceStr(getPriceStr(maxPrice, minPrice, pricePackages.get(0).getRentalType(),pricePackages.get(0).getPriceType(), PRICE_TIME_STEP));
		}

		sitePriceRuleDTO.setPricePackages(new ArrayList<>()); //设置每个套餐的价格范围
		pricePackages.forEach(r -> {
			SitePricePackageDTO dto = new SitePricePackageDTO();
			dto.setName(r.getName());
			BigDecimal maxPrice2 = new BigDecimal(0);;
			BigDecimal minPrice2 = new BigDecimal(Integer.MAX_VALUE);
			MaxMinPrice maxMinPrice2;
			if (SceneType.PARK_TOURIST.getCode().equals(scene) && TrueOrFalseFlag.fromCode(resourceType.getUnauthVisible()) == TrueOrFalseFlag.FALSE){
				dto.setPriceStr("");
			}else if (RentalUserPriceType.UNIFICATION.getCode() == r.getUserPriceType()) {
				maxMinPrice2 = rentalv2PricePackageProvider.findMaxMinPrice(packageIds, pricePackages.get(0).getRentalType(), r.getName());
				maxPrice2 = max(maxMinPrice2.getMaxPrice(),r.getPrice());
				minPrice2 = min(maxMinPrice2.getMinPrice(),r.getPrice());
                if (r.getPriceType().equals(RentalPriceType.LINEARITY.getCode()))
                    dto.setPriceStr(getPriceStr(maxPrice2, minPrice2, r.getRentalType(),r.getPriceType(), PRICE_TIME_STEP));
                else
                    dto.setPriceStr(getInitiatePriceStr(r.getPrice(),r.getInitiatePrice(),r.getRentalType(), PRICE_TIME_STEP));
			}else{
				maxMinPrice2 =  rentalv2Provider.findMaxMinPriceByClassifycation(rentalSite.getResourceType(), EhRentalv2PricePackages.class.getSimpleName(), Collections.singletonList(r.getId()),
						PriceRuleType.RESOURCE.getCode(), rentalSite.getId(), pricePackages.get(0).getUserPriceType(), scene);
				maxPrice2 = maxMinPrice2.getMaxPrice();
				minPrice2 = maxMinPrice2.getMinPrice();
				List<Long> packageIds2 = rentalv2PricePackageProvider.listPricePackageIdsByCellPackages(packageIds,r.getName());
				maxMinPrice2 = rentalv2Provider.findMaxMinPriceByClassifycation(rentalSite.getResourceType(), EhRentalv2PricePackages.class.getSimpleName(), packageIds2,
						PriceRuleType.RESOURCE.getCode(), rentalSite.getId(), pricePackages.get(0).getUserPriceType(), scene);
				maxPrice2 = max(maxPrice2,maxMinPrice2.getMaxPrice());
				minPrice2 = min(minPrice2,maxMinPrice2.getMinPrice());
				if (r.getPriceType().equals(RentalPriceType.LINEARITY.getCode()))
					dto.setPriceStr(getPriceStr(maxPrice2, minPrice2, r.getRentalType(),r.getPriceType(), PRICE_TIME_STEP));
				else {
					List<RentalPriceClassification> classification = rentalv2Provider.listClassification(rentalSite.getResourceType(), EhRentalv2PricePackages.class.getSimpleName(), r.getId(),
							PriceRuleType.RESOURCE.getCode(), rentalSite.getId(), pricePackages.get(0).getUserPriceType(), scene);
					if (classification != null && classification.size() > 0)
						dto.setPriceStr(getInitiatePriceStr(classification.get(0).getWorkdayPrice(),classification.get(0).getInitiatePrice(), r.getRentalType(), PRICE_TIME_STEP));
				}
			}

			sitePriceRuleDTO.getPricePackages().add(dto);
		});
		return sitePriceRuleDTO;
	}

	private SitePriceRuleDTO convertToSitePriceRuleDTO(RentalResource rentalSite, Rentalv2PriceRule priceRule, RentalResourceType resourceType) {
		SitePriceRuleDTO sitePriceRuleDTO = new SitePriceRuleDTO();
		sitePriceRuleDTO.setRentalType(priceRule.getRentalType());
		sitePriceRuleDTO.setPriceType(priceRule.getPriceType());
		String scene = getClassification(priceRule.getUserPriceType());
		if (SceneType.PARK_TOURIST.getCode().equals(scene) && TrueOrFalseFlag.fromCode(resourceType.getUnauthVisible()) == TrueOrFalseFlag.FALSE){
			sitePriceRuleDTO.setPriceStr("");
			return sitePriceRuleDTO;
		}
		BigDecimal maxPrice = new BigDecimal(0);
		BigDecimal minPrice = new BigDecimal(Integer.MAX_VALUE);
        MaxMinPrice maxMinPrice = null;
		if (RentalUserPriceType.UNIFICATION.getCode() == priceRule.getUserPriceType()){ //统一价格
			maxPrice = priceRule.getWorkdayPrice();
			minPrice = priceRule.getWorkdayPrice();
            //单元格统一价格
            maxMinPrice = rentalv2Provider.findMaxMinPrice(rentalSite.getResourceType(),priceRule.getOwnerId(), priceRule.getRentalType());
            maxPrice = max(maxMinPrice.getMaxPrice(),maxPrice);
            minPrice = min(maxMinPrice.getMinPrice(),minPrice);
		}else{
			List<RentalPriceClassification> classifications = rentalv2Provider.listClassification(rentalSite.getResourceType(), EhRentalv2PriceRules.class.getSimpleName(),
					priceRule.getId(), null, null, priceRule.getUserPriceType(), scene);
			if (classifications != null && classifications.size() > 0){
				maxPrice = classifications.get(0).getWorkdayPrice();
				minPrice = classifications.get(0).getWorkdayPrice();
			}
            //单元格按类型区分价格
            List<Long> cellId = rentalv2Provider.listCellId(rentalSite.getResourceType(), rentalSite.getId(), priceRule.getRentalType());
            if (cellId != null && cellId.size() > 0){
                maxMinPrice = rentalv2Provider.findMaxMinPriceByClassifycation(rentalSite.getResourceType(),EhRentalv2Cells.class.getSimpleName(),cellId,
                        null,null,priceRule.getUserPriceType(),scene);
                maxPrice = max(maxMinPrice.getMaxPrice(),maxPrice);
                minPrice = min(maxMinPrice.getMinPrice(),minPrice);
            }
		}

		sitePriceRuleDTO.setMaxPrice(maxPrice);
		sitePriceRuleDTO.setMinPrice(minPrice);
		sitePriceRuleDTO.setPriceStr(getPriceStr(maxPrice, minPrice, priceRule.getRentalType(),priceRule.getPriceType(), PRICE_TIME_STEP));

		return sitePriceRuleDTO;
	}

	private String getClassification(Byte userPriceType){
		if (userPriceType == null || userPriceType.equals(RentalUserPriceType.UNIFICATION.getCode()))
			return null;
		String sceneType = currentSceneType.get();
		if (!StringHelper.hasContent(sceneType))
			return null;
		if (userPriceType.equals(RentalUserPriceType.USER_TYPE.getCode())){
			String[] sceneTypes = sceneType.split(",");
			if (sceneTypes.length > 0)
				return sceneTypes[0];
		}else{
			String[] sceneTypes = sceneType.split(",");
			if (sceneTypes.length > 1)
				return sceneTypes[1];
		}
		return null;
	}

	private String getPriceStr(BigDecimal maxPrice, BigDecimal minPrice, Byte rentalType, Byte priceType, Double timeStep) {
		if (priceType == null || RentalPriceType.LINEARITY.getCode() == priceType)
			if (minPrice.compareTo(maxPrice) == 0) {
				return priceToString(minPrice, rentalType, timeStep);
			} else {
				return priceToString(minPrice, rentalType, timeStep)
						+ "~" + priceToString(maxPrice, rentalType, timeStep);
			}
		else
			return "￥" + minPrice + "起";
	}

	private String getInitiatePriceStr(BigDecimal price, BigDecimal initiatePrice, Byte rentalType, Double timeStep) {
		String priceString = isInteger(price) ? String.valueOf(price.intValue()) : price.toString();
		String initiatePriceString = isInteger(initiatePrice) ? String.valueOf(initiatePrice.intValue()) : initiatePrice.toString();
		if (price.compareTo(new BigDecimal(0)) == 0 && initiatePrice.compareTo(new BigDecimal(0)) == 0)
			return "免费";
		if (rentalType.equals(RentalType.DAY.getCode()))
			return "起步价第一天￥" + priceString + "，以后￥" + initiatePriceString + "/天";
		if (rentalType.equals(RentalType.MONTH.getCode()))
			return "起步价第一月￥" + priceString + "，以后￥" + initiatePriceString + "/月";
		if (rentalType.equals(RentalType.HALFDAY.getCode()))
			return "起步价半天￥" + priceString + "，以后￥" + initiatePriceString + "/半天";
		if (rentalType.equals(RentalType.THREETIMEADAY.getCode()))
			return "起步价半天￥" + priceString + "，以后￥" + initiatePriceString + "/半天";
		if (rentalType.equals(RentalType.WEEK.getCode()))
			return "起步价第一周￥" + priceString + "，以后￥" + initiatePriceString + "/周";
		String tmp = "" + (isInteger(timeStep) ? String.valueOf(timeStep.intValue()).equals("1") ? "" : String.valueOf(timeStep.intValue()) : timeStep);
		if (rentalType.equals(RentalType.HOUR.getCode()))
			return "起步价" + tmp + "小时￥" + priceString + "，以后￥" + initiatePriceString + "/" + tmp + "h";
		return "";
	}

	private BigDecimal max(BigDecimal... b) {
		BigDecimal max = new BigDecimal(Integer.MIN_VALUE);
		for (BigDecimal bigDecimal : b) {
			max = maxBig(max, bigDecimal);
		}
		return max.intValue() == Integer.MIN_VALUE ? new BigDecimal(0) : max;
	}

	private BigDecimal maxBig(BigDecimal b1, BigDecimal b2) {
		if (b2 != null && b2.compareTo(b1) > 0) {
			return b2;
		}
		return b1;
	}

	private BigDecimal min(BigDecimal... b) {
		BigDecimal min = new BigDecimal(Integer.MAX_VALUE);
		for (BigDecimal bigDecimal : b) {
			min = minBig(min, bigDecimal);
		}
		return min.intValue() == Integer.MAX_VALUE ? new BigDecimal(0) : min;
	}

	private BigDecimal minBig(BigDecimal b1, BigDecimal b2) {
		if (b2 != null && b2.compareTo(b1) < 0) {
			return b2;
		}
		return b1;
	}

	private boolean isInteger(BigDecimal b) {
		return new BigDecimal(b.intValue()).compareTo(b) == 0;
	}

	private boolean isInteger(double d) {
		double eps = 0.0001;
		return Math.abs(d - (double) ((int) d)) < eps;
	}

	private String priceToString(BigDecimal price, Byte rentalType, Double timeStep) {
		String priceString = isInteger(price) ? String.valueOf(price.intValue()) : price.toString();
		if (price.compareTo(new BigDecimal(0)) == 0)
			return "免费";
		if (rentalType.equals(RentalType.DAY.getCode()))
			return "￥" + priceString + "/天";
		if (rentalType.equals(RentalType.MONTH.getCode()))
			return "￥" + priceString + "/月";
		if (rentalType.equals(RentalType.HALFDAY.getCode()))
			return "￥" + priceString + "/半天";
		if (rentalType.equals(RentalType.THREETIMEADAY.getCode()))
			return "￥" + priceString + "/半天";
		if (rentalType.equals(RentalType.WEEK.getCode()))
			return "￥" + priceString + "/周";
		if (rentalType.equals(RentalType.HOUR.getCode()))
			return "￥" + priceString + "/" + (isInteger(timeStep) ? String.valueOf(timeStep.intValue()).equals("1") ? "" : String.valueOf(timeStep.intValue()) : timeStep) + "小时";
		return "";
	}

	private SiteItemDTO convertItem2DTO(RentalItem item) {
		SiteItemDTO siteItemDTO = ConvertHelper.convert(item, SiteItemDTO.class);

		siteItemDTO.setItemName(item.getName());
		siteItemDTO.setItemPrice(item.getPrice());
		siteItemDTO.setDescription(item.getDescription());
		siteItemDTO.setImgUrl(this.contentServerService.parserUri(siteItemDTO.getImgUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId()));
		return siteItemDTO;
	}

	private SiteStructureDTO convertStructure2DTO(RentalStructure structure){
		SiteStructureDTO structureDTO = ConvertHelper.convert(structure,SiteStructureDTO.class);
		structureDTO.setIconUrl(this.contentServerService.parserUri(structureDTO.getIconUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId()));
		return structureDTO;
	}

	@Override
	public RentalBillDTO addRentalBill(AddRentalBillCommand cmd) {

		if(cmd.getRentalSiteId() == null || null == cmd.getRentalDate() ||
				cmd.getRules() == null || cmd.getRules().size() == 0) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_LOST_PARAMETER,
					"Invalid parameter");
		}

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}

		Long userId = cmd.getUid() == null ? UserContext.currentUserId() : cmd.getUid();

		RentalResource rs = rentalCommonService.getRentalResource(cmd.getResourceType(), cmd.getRentalSiteId());

		RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(null, null,
				rs.getResourceType(), rs.getResourceTypeId(), RuleSourceType.RESOURCE.getCode(), rs.getId());

		processCells(rs, cmd.getRentalType());

		this.validateRentalBill(cmd.getRules(), rs, rule, cmd.getRentalType());

		RentalResourceType rsType = this.rentalv2Provider.getRentalResourceTypeById(rs.getResourceTypeId());

		//用来存每个单元格计算之后的价格
		Map<Long, BigDecimal> cellAmountMap = new HashMap<>();


//		Long orgId = resolveOrganizationId(null);

		RentalOrder rentalBill = ConvertHelper.convert(rs, RentalOrder.class);
		BeanUtils.copyProperties(rule, rentalBill);

		rentalBill.setRentalType(cmd.getRentalType());
		//设置当前场景公司id
//		rentalBill.setUserEnterpriseId(orgId);
		rentalBill.setResourceName(rs.getResourceName());
		//rentalBill.setAddress(null);
		rentalBill.setNamespaceId(rsType.getNamespaceId());
		rentalBill.setRentalResourceId(cmd.getRentalSiteId());
		rentalBill.setRentalUid(userId);
		rentalBill.setInvoiceFlag(InvoiceFlag.NONEED.getCode());
		rentalBill.setRentalDate(new Date(cmd.getRentalDate()));
		rentalBill.setPackageName(cmd.getPackageName());
		rentalBill.setScene(cmd.getSceneType());
		User user = userProvider.findUserById(userId);
		if (user.getVipLevelText() != null)
			rentalBill.setVipLevel(user.getVipLevelText());
		rentalBill.setSource(cmd.getSource() == null ? (byte)1 : cmd.getSource());
		rentalBill.setNamespaceId(UserContext.getCurrentNamespaceId());
		//设置订单模式
		rentalBill.setPayMode(rsType.getPayMode());
		if (cmd.getSource() != null && cmd.getSource().equals(RentalBillSource.BACK_GROUND.getCode()))
			rentalBill.setPayMode(PayMode.OFFLINE_PAY.getCode());
		rentalBill.setReserveTime(new Timestamp(System.currentTimeMillis()));
		//修改 by sw 点击下一步时，初始化订单状态，点击立即预约才进去各个模式流程  PayMode
		rentalBill.setStatus(SiteBillStatus.INACTIVE.getCode());
		rentalBill.setVisibleFlag(VisibleFlag.VISIBLE.getCode());

		//计算并设置设置订单金额
		setOrderAmount(rs, cmd.getRules(), rentalBill, cellAmountMap, rule);
		//设置只用详情
		setUseDetailStr(cmd.getRules(), rs, rentalBill);
		//设置订单提醒时间
		setRentalOrderReminderTime(cmd.getRules(), rs, rentalBill);
		//设置预约单元格数量
		rentalBill.setRentalCount(cmd.getRules().stream().filter(r -> null != r.getRentalCount())
				.mapToDouble(RentalBillRuleDTO::getRentalCount).findFirst().getAsDouble());

		Long orderNo = onlinePayService.createBillId(DateHelper.currentGMTTime().getTime());
		rentalBill.setOrderNo(String.valueOf(orderNo));

		this.dbProvider.execute((TransactionStatus status) -> {

			//用基于服务器平台的锁添加订单（包括验证和添加）
			Tuple<Long, Boolean> tuple = this.coordinationProvider
					.getNamedLock(CoordinationLocks.CREATE_RENTAL_BILL.getCode() + rs.getResourceType() + rs.getId())
					.enter(() -> {
						// this.groupProvider.updateGroup(group);
						this.validateRentalBill(cmd.getRules(), rs, rule, cmd.getRentalType());

						return this.rentalv2Provider.createRentalOrder(rentalBill);
					});

			createResourceOrder(cmd.getRules(), rentalBill, cellAmountMap);

			return null;
		});

		cellList.get().clear();

		RentalBillDTO billDTO = ConvertHelper.convert(rentalBill, RentalBillDTO.class);
		mappingRentalBillDTO(billDTO, rentalBill, rs);

		billDTO.setConfirmationPrompt(rs.getConfirmationPrompt());
		billDTO.setHolidayOpenFlag(rule.getHolidayOpenFlag());
		billDTO.setHolidayType(rule.getHolidayType());
		List<RentalCloseDate> closeDates = rentalv2Provider.queryRentalCloseDateByOwner(rentalBill.getResourceType(),
				EhRentalv2Resources.class.getSimpleName(), rentalBill.getRentalResourceId(),null,null);
		List<Long> defaultDate = new ArrayList<>();
		if (rule.getHolidayOpenFlag() !=null && rule.getHolidayOpenFlag() == 0){//不开放
			defaultDate = rule.getHolidayType().equals(RentalHolidayType.NORMAL_WEEKEND.getCode())?normalWeekend:legalHoliday;
		}
		List<Long> settingDate = closeDates == null || closeDates.size() == 0 ?new ArrayList<>():
				closeDates.stream().map(r->r.getCloseDate().getTime()).collect(Collectors.toList());
		Set<Long> defaultDateSet = new HashSet<>(defaultDate);
		Set<Long> settingDateSet = new HashSet<>(settingDate);
		defaultDateSet.removeAll(settingDateSet);
		billDTO.setSpecialOpenDate(new ArrayList<>(defaultDateSet));
		defaultDateSet = new HashSet<>(defaultDate);
		settingDateSet.removeAll(defaultDateSet);
		billDTO.setSpecialCloseDate(new ArrayList<>(settingDateSet));
		//退款提示
		billDTO.setRefundTip(getRefundTipByRule(rule,rentalBill.getRentalResourceId()));
		billDTO.setFileFlag(rule.getFileFlag() == null ? (byte) 0 :rule.getFileFlag());

		return billDTO;
	}

	//由于和退款按钮的提示有微妙的不同 必须另写一个方法
	private String getRefundTipByRule(RentalDefaultRule rule,Long rentalSiteId){
		//退款提示
		if (null != rule.getRefundStrategy()) {
			List<RentalRefundTip> rentalRefundTips = rentalv2Provider.listRefundTips(rule.getResourceType(), RuleSourceType.RESOURCE.getCode(), rentalSiteId, rule.getRefundStrategy());
			if (rentalRefundTips != null && rentalRefundTips.size() > 0)
				return rentalRefundTips.get(0).getTips();
		}
		return null;
	}

	private void createResourceOrder(List<RentalBillRuleDTO> rules, RentalOrder rentalBill, Map<Long, BigDecimal> cellAmountMap) {
		for (RentalBillRuleDTO siteRule : rules) {

			RentalCell rentalCell = findRentalSiteRuleById(siteRule.getRuleId(),rentalBill.getRentalResourceId(),
                    rentalBill.getRentalType(),rentalBill.getResourceType());

			if (null == rentalCell) {
				continue;
			}

			RentalResourceOrder rsb = ConvertHelper.convert(rentalCell, RentalResourceOrder.class);
			rsb.setRentalOrderId(rentalBill.getId());
//			rsb.setAmorpm(rsr.getAmorpm());
//			rsb.setEndTime(rsr.getEndTime());
//			rsb.setBeginTime(rsr.getBeginTime());
			rsb.setTotalMoney(cellAmountMap.get(rentalCell.getId()));
			rsb.setRentalCount(siteRule.getRentalCount());
			rsb.setRentalResourceRuleId(rentalCell.getId());
			rsb.setResourceRentalDate(rentalCell.getResourceRentalDate());
			rsb.setStatus(ResourceOrderStatus.NORMAL.getCode());
			rsb.setResourceNumber(rentalCell.getResourceNumber());

			rentalv2Provider.createRentalSiteBill(rsb);

		}
	}

	private void setOrderAmount(RentalResource rs, List<RentalBillRuleDTO> rules, RentalOrder rentalBill,
								Map<Long, BigDecimal> cellAmountMap, RentalDefaultRule rule) {
		BigDecimal amount = new BigDecimal(0);
		if (NormalFlag.NEED.getCode() == rule.getNeedPay())
			amount = calculateOrderAmount(rs, rules, rentalBill, cellAmountMap);

		rentalBill.setPaidMoney(new BigDecimal(0));
		rentalBill.setResourceTotalMoney(amount);
		rentalBill.setPayTotalMoney(amount);

	}

	private BigDecimal calculateOrderAmount(RentalResource rs, List<RentalBillRuleDTO> rules, RentalOrder rentalBill,
											Map<Long, BigDecimal> cellAmountMap) {
		currentSceneType.set(rentalBill.getScene());
		BigDecimal[] siteTotalMoneys = {new BigDecimal(0)};

		List<Rentalv2PricePackage> resourcePackages = rentalv2PricePackageProvider.listPricePackageByOwner(rs.getResourceType(),
				PriceRuleType.RESOURCE.getCode(), rs.getId(), rentalBill.getRentalType(), rentalBill.getPackageName());
		//填充套餐价格分类
		if (resourcePackages != null && resourcePackages.size() > 0)
			resourcePackages.forEach(r->r.setPriceClassification(rentalv2Provider.listClassification(rs.getResourceType(),EhRentalv2PricePackages.class.getSimpleName(),
					r.getId(),null,null,null,null)));
		Rentalv2PriceRule priceRule = rentalv2PriceRuleProvider.findRentalv2PriceRuleByOwner(rs.getResourceType(),
				PriceRuleType.RESOURCE.getCode(), rs.getId(),rentalBill.getRentalType());
		//填充价格分类
		priceRule.setPriceClassification(rentalv2Provider.listClassification(rs.getResourceType(),EhRentalv2PriceRules.class.getSimpleName(),
				priceRule.getId(),null,null,null,null));
		boolean initiateFlag = false;
		Map<java.sql.Date, Map<String, Set<Byte>>> dayMap = new HashMap<>();
		for (RentalBillRuleDTO siteRule : rules) {
			if (null == siteRule) {
				continue;
			}

			if (siteRule.getRentalCount() == null || siteRule.getRuleId() == null) {
				throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_LOST_PARAMETER,
						"Invalid parameter siteRule");
			}

			RentalCell rentalCell = findRentalSiteRuleById(siteRule.getRuleId(),rs.getId(),
                    rentalBill.getRentalType(),rentalBill.getResourceType());
			if (null == rentalCell) {
				continue;
			}
			//给半天预定的日期map加入am和pm的byte
			if (rentalBill.getRentalType().equals(RentalType.HALFDAY.getCode()) ||
					rentalBill.getRentalType().equals(RentalType.THREETIMEADAY.getCode())) {
				dayMap.putIfAbsent(rentalCell.getResourceRentalDate(), new HashMap<>());
				if (rs.getAutoAssign().equals(NormalFlag.NONEED.getCode())) {
					String key = "无场所";
					dayMap.get(rentalCell.getResourceRentalDate()).putIfAbsent(key, new HashSet<>());
					dayMap.get(rentalCell.getResourceRentalDate()).get(key).add(rentalCell.getAmorpm());
				} else {
					dayMap.get(rentalCell.getResourceRentalDate()).putIfAbsent(rentalCell.getResourceNumber(), new HashSet<>());
					dayMap.get(rentalCell.getResourceRentalDate()).get(rentalCell.getResourceNumber()).add(rentalCell.getAmorpm());
				}
			}

			if (rentalBill.getPackageName() != null) { //有套餐的情况下 使用套餐价格
				Rentalv2PricePackage pricePackage;
				if (rentalCell.getPricePackageId() == null) {
					String classification = getClassification(resourcePackages.get(0).getUserPriceType());
					pricePackage = resourcePackages.get(0);
					if (classification != null && resourcePackages.get(0).getPriceClassification() != null){
						for (RentalPriceClassification priceClassification : resourcePackages.get(0).getPriceClassification())
							if (classification.equals(priceClassification.getClassification()) &&
									resourcePackages.get(0).getUserPriceType().equals(priceClassification.getUserPriceType())){
								pricePackage.setPrice(priceClassification.getWorkdayPrice());
								pricePackage.setInitiatePrice(priceClassification.getInitiatePrice());
								break;
							}
					}
				} else {
					String classification = getClassification(resourcePackages.get(0).getUserPriceType());
					pricePackage = rentalv2PricePackageProvider.listPricePackageByOwner(rs.getResourceType(), PriceRuleType.CELL.getCode(),
							rentalCell.getPricePackageId(), rentalBill.getRentalType(), rentalBill.getPackageName()).get(0);
					List<RentalPriceClassification> cla = rentalv2Provider.listClassification(rs.getResourceType(), EhRentalv2PricePackages.class.getSimpleName(),
							pricePackage.getId(), null, null, null, classification);
					if (classification != null && cla != null){
						for (RentalPriceClassification priceClassification : cla)
							if (classification.equals(priceClassification.getClassification()) &&
									pricePackage.getUserPriceType().equals(priceClassification.getUserPriceType())){
								pricePackage.setPrice(priceClassification.getWorkdayPrice());
								pricePackage.setInitiatePrice(priceClassification.getInitiatePrice());
								break;
							}
					}
				}
				rentalCell.setPrice(pricePackage.getPrice());
				//设置起步后价格
				if (pricePackage.getPriceType().equals(RentalPriceType.INITIATE.getCode()) && initiateFlag) {
					rentalCell.setPrice(pricePackage.getInitiatePrice());
				}

			} else {
				String classification = getClassification(priceRule.getUserPriceType());
				if (classification != null && priceRule.getPriceClassification() != null)
					for (RentalPriceClassification priceClassification : priceRule.getPriceClassification())
						if (classification.equals(priceClassification.getClassification()) &&
								priceRule.getUserPriceType().equals(priceClassification.getUserPriceType())){
							rentalCell.setPrice(priceClassification.getWorkdayPrice());
							rentalCell.setInitiatePrice(priceClassification.getInitiatePrice());
							break;
						}
				if (rentalCell.getPriceType().equals(RentalPriceType.INITIATE.getCode()) && initiateFlag) {
					rentalCell.setPrice(rentalCell.getInitiatePrice());
				}
			}
			initiateFlag = true;

			if (rentalBill.getRentalType().equals(RentalType.HOUR.getCode())) {//按小时模式 单元格价格等于半小时价格乘单元格长度
				rentalCell.setPrice(rentalCell.getPrice().multiply(new BigDecimal(rentalCell.getTimeStep() * 2)));
			}

			BigDecimal cellPrice = null == rentalCell.getPrice() ? new BigDecimal(0) : rentalCell.getPrice();
			if (null != rentalBill.getScene()) {
				cellPrice = null == rentalCell.getPrice() ? new BigDecimal(0) : rentalCell.getPrice();
			}
			BigDecimal amount = cellPrice.multiply(new BigDecimal(siteRule.getRentalCount()));
			siteTotalMoneys[0] = siteTotalMoneys[0].add(amount);
			cellAmountMap.put(siteRule.getRuleId(), amount);
		}

		//计算满减，规则从priceRule获取
		if (null != rentalBill.getScene()) { //根据用户类型区分优惠
			if (rentalBill.getPackageName() == null) {
				String classification = getClassification(priceRule.getUserPriceType());
				if (classification != null && priceRule.getPriceClassification() != null)
					for (RentalPriceClassification priceClassification : priceRule.getPriceClassification())
						if (classification.equals(priceClassification.getClassification()) &&
								priceRule.getUserPriceType().equals(priceClassification.getUserPriceType())){
							priceRule.setDiscountType(priceClassification.getDiscountType());
							priceRule.setFullPrice(priceClassification.getFullPrice());
							priceRule.setCutPrice(priceClassification.getCutPrice());
							priceRule.setDiscountRatio(priceClassification.getDiscountRatio());
							break;
						}
			} else { //启用套餐的优惠
				List<Rentalv2PricePackage> list = rentalv2PricePackageProvider.listPricePackageByOwner(rs.getResourceType(),
						PriceRuleType.RESOURCE.getCode(), rs.getId(), rentalBill.getRentalType(), rentalBill.getPackageName());
				if (list != null && list.size() > 0) {
					Rentalv2PricePackage pricePackage = list.get(0);
					String classification = getClassification(pricePackage.getUserPriceType());
					List<RentalPriceClassification> cla = rentalv2Provider.listClassification(rs.getResourceType(), EhRentalv2PricePackages.class.getSimpleName(),
							pricePackage.getId(), null, null, null, classification);
					if (classification != null && cla != null){
						for (RentalPriceClassification priceClassification : cla)
							if (classification.equals(priceClassification.getClassification()) &&
									pricePackage.getUserPriceType().equals(priceClassification.getUserPriceType())){
								priceRule.setDiscountType(priceClassification.getDiscountType());
								priceRule.setFullPrice(priceClassification.getFullPrice());
								priceRule.setCutPrice(priceClassification.getCutPrice());
								priceRule.setDiscountRatio(priceClassification.getDiscountRatio());
								break;
							}
					}else{
						priceRule.setDiscountType(pricePackage.getDiscountType());
						priceRule.setFullPrice(pricePackage.getFullPrice());
						priceRule.setCutPrice(pricePackage.getCutPrice());
						priceRule.setDiscountRatio(pricePackage.getDiscountRatio());
					}
				}
			}
		}

		//优惠
		if (priceRule.getDiscountType() != null) {
			if (priceRule.getDiscountType().equals(DiscountType.FULL_MOENY_CUT_MONEY.getCode())) {
				//满减优惠
				//满了多少次,都只减一个
				if (siteTotalMoneys[0].compareTo(priceRule.getFullPrice()) >= 0) {
					siteTotalMoneys[0] = siteTotalMoneys[0].subtract(priceRule.getCutPrice());
				}
			} else if (DiscountType.FULL_DAY_CUT_MONEY.getCode().equals(priceRule.getDiscountType())) {
				//不允许一个用户预约一个时段多个资源的情况
				double multiple = 0.0;
				//满天减免
				if (priceRule.getRentalType().equals(RentalType.HALFDAY.getCode())) {
					for (Date rentalDate : dayMap.keySet()) {
						for (String resourceNumber : dayMap.get(rentalDate).keySet()) {
							if (dayMap.get(rentalDate).get(resourceNumber).size() >= 2) {
								multiple = multiple + rules.get(0).getRentalCount();
							}
						}
					}
				} else if (priceRule.getRentalType().equals(RentalType.THREETIMEADAY.getCode())) {
					for (Date rentalDate : dayMap.keySet()) {
						for (String resourceNumber : dayMap.get(rentalDate).keySet()) {
							if (dayMap.get(rentalDate).get(resourceNumber).size() >= 3) {
								multiple = multiple + rules.get(0).getRentalCount();
							}
						}
					}
				}
				siteTotalMoneys[0] = siteTotalMoneys[0].subtract(priceRule.getCutPrice().multiply(new BigDecimal(multiple)));
			}
		}

		return siteTotalMoneys[0].setScale(3, BigDecimal.ROUND_DOWN);
	}

	//设置各种提醒时间
	private void setRentalOrderReminderTime(List<RentalBillRuleDTO> rules, RentalResource rs, RentalOrder rentalBill) {

		//计算单元格的起止时间-小时单元格提醒时间提前30分钟, 结束时间直接用
		//按天和半天预定 提醒时间: 前一天下午4点, 按天预订结束时间:第二天
		//按半天预定 结束时间根据产品需求,早上 12 下午  18 晚上 21
		//计算的时间用于定时任务 提醒时间:消息提醒  结束时间:订单过期

		//计算结束提醒时间 提前15分钟提醒
		//计算门禁时间 按小时半天预约 按预定时间延伸1小时(未来可变) 按天预约 6点到20点 按月预约 前一天6点到后一天20点

		List<RentalTimeInterval> halfTimeIntervals = rentalv2Provider.queryRentalTimeIntervalByOwner(rs.getResourceType(),
				RentalTimeIntervalOwnerType.RESOURCE_HALF_DAY.getCode(), rs.getId());

		for (RentalBillRuleDTO siteRule : rules) {
			if (null == siteRule) {
				continue;
			}

			if (siteRule.getRentalCount() == null || siteRule.getRuleId() == null) {
				throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_LOST_PARAMETER,
						"Invalid parameter siteRule");
			}

			RentalCell rentalCell = findRentalSiteRuleById(siteRule.getRuleId(),rs.getId(),rentalBill.getRentalType(),
                    rentalBill.getResourceType());
			if (null == rentalCell) {
				continue;
			}

			Timestamp startTime;
			Timestamp reminderTime;
			Timestamp reminderEndTime;
			Timestamp endTime;
			Timestamp authStartTime;
			Timestamp authEndTime;

			if (rentalCell.getRentalType() == RentalType.HOUR.getCode()) {
				startTime = new Timestamp(rentalCell.getBeginTime().getTime());
				reminderTime = new Timestamp(rentalCell.getBeginTime().getTime() - 15 * 60 * 1000L);
				endTime = rentalCell.getEndTime();
				reminderEndTime = new Timestamp(endTime.getTime() - 15 * 60 * 1000L);
				authStartTime = new Timestamp(startTime.getTime() - 60 * 60 * 1000L);
				authEndTime = new Timestamp(endTime.getTime() + 60 * 60 * 1000L);
			} else if (rentalCell.getRentalType().equals(RentalType.DAY.getCode())) {
				startTime = new Timestamp(rentalCell.getResourceRentalDate().getTime());
				reminderTime = new Timestamp(rentalCell.getResourceRentalDate().getTime() - 8 * 60 * 60 * 1000L);
				endTime = new Timestamp(rentalCell.getResourceRentalDate().getTime() + 24 * 60 * 60 * 1000L);
				reminderEndTime = new Timestamp(endTime.getTime() - 15 * 60 * 1000L);
				authStartTime = new Timestamp(rentalCell.getResourceRentalDate().getTime() + 6 * 60 * 60 * 1000L);
				authEndTime = new Timestamp(rentalCell.getResourceRentalDate().getTime() + 20 * 60 * 60 * 1000L);
			} else if (rentalCell.getRentalType().equals(RentalType.MONTH.getCode())) {
				// 按月的在前一天下午四点提醒，月底第二天结束
				startTime = new Timestamp(rentalCell.getResourceRentalDate().getTime());
				reminderTime = new Timestamp(rentalCell.getResourceRentalDate().getTime() - 8 * 60 * 60 * 1000L);

				Calendar calendar = Calendar.getInstance();
				calendar.setTime(rentalCell.getResourceRentalDate());
				calendar.add(Calendar.MONTH, 1);
				endTime = new Timestamp(calendar.getTimeInMillis());
				reminderEndTime = new Timestamp(endTime.getTime() - 15 * 60 * 1000L);
				authStartTime = new Timestamp(rentalCell.getResourceRentalDate().getTime() - 18 * 60 * 60 * 1000L);
				authEndTime = new Timestamp(endTime.getTime() + 20 * 60 * 60 * 1000L);
			} else if (rentalCell.getRentalType().equals(RentalType.WEEK.getCode())) {
				startTime = new Timestamp(rentalCell.getResourceRentalDate().getTime());
				reminderTime = new Timestamp(rentalCell.getResourceRentalDate().getTime() - 8 * 60 * 60 * 1000L);
				endTime = new Timestamp(rentalCell.getResourceRentalDate().getTime() + 7 * 24 * 60 * 60 * 1000L);
				reminderEndTime = new Timestamp(endTime.getTime() - 15 * 60 * 1000L);
				authStartTime = new Timestamp(rentalCell.getResourceRentalDate().getTime() + 6 * 60 * 60 * 1000L);
				authEndTime = new Timestamp(rentalCell.getResourceRentalDate().getTime() + 20 * 60 * 60 * 1000L+ 7 * 24 * 60 * 60 * 1000L);
			} else {
				if (rentalCell.getAmorpm().equals(AmorpmFlag.AM.getCode())) {
					RentalTimeInterval timeInterval = halfTimeIntervals.get(0);
					reminderTime = new Timestamp(rentalCell.getResourceRentalDate().getTime() - 8 * 60 * 60 * 1000L);
					startTime = new Timestamp(rentalCell.getResourceRentalDate().getTime() + (long) (timeInterval.getBeginTime() * 60 * 60 * 1000L));
					endTime = new Timestamp(rentalCell.getResourceRentalDate().getTime() + (long) (timeInterval.getEndTime() * 60 * 60 * 1000L));
				} else if (rentalCell.getAmorpm().equals(AmorpmFlag.PM.getCode())) {
					RentalTimeInterval timeInterval = halfTimeIntervals.get(1);

					reminderTime = new Timestamp(rentalCell.getResourceRentalDate().getTime() - 8 * 60 * 60 * 1000L);
					startTime = new Timestamp(rentalCell.getResourceRentalDate().getTime() + (long) (timeInterval.getBeginTime() * 60 * 60 * 1000L));
					endTime = new Timestamp(rentalCell.getResourceRentalDate().getTime() + (long) (timeInterval.getEndTime() * 60 * 60 * 1000L));
				} else {
					RentalTimeInterval timeInterval = halfTimeIntervals.get(2);

					reminderTime = new Timestamp(rentalCell.getResourceRentalDate().getTime() - 8 * 60 * 60 * 1000L);
					startTime = new Timestamp(rentalCell.getResourceRentalDate().getTime() + (long) (timeInterval.getBeginTime() * 60 * 60 * 1000L));
					endTime = new Timestamp(rentalCell.getResourceRentalDate().getTime() + (long) (timeInterval.getEndTime() * 60 * 60 * 1000L));

				}
				reminderEndTime = new Timestamp(endTime.getTime() - 15 * 60 * 1000L);
				authStartTime = new Timestamp(startTime.getTime() - 60 * 60 * 1000L);
				authEndTime = new Timestamp(endTime.getTime() + 60 * 60 * 1000L);
			}
			//如果订单的提醒时间在本单元格 提醒时间之后 或者 订单提醒时间为空
			if (null == rentalBill.getReminderTime() || rentalBill.getReminderTime().after(reminderTime)) {
				rentalBill.setReminderTime(reminderTime);
			}
			//如果订单的起始时间在本单元格 起始时间之后 或者 订单时间为空
			if (null == rentalBill.getStartTime() || rentalBill.getStartTime().after(startTime)) {
				rentalBill.setStartTime(startTime);
			}
			//如果订单的结束时间在本单元格 结束时间之前 或者 订单时间为空
			if (null == rentalBill.getEndTime() || rentalBill.getEndTime().before(endTime)) {
				rentalBill.setEndTime(endTime);
			}
			if (null == rentalBill.getReminderEndTime() || rentalBill.getReminderEndTime().before(reminderEndTime)) {
				rentalBill.setReminderEndTime(reminderEndTime);
			}
			//设置门禁时间
			if (rs.getAclinkId() != null && (null == rentalBill.getAuthStartTime() || rentalBill.getAuthStartTime().after(authStartTime))) {
				rentalBill.setAuthStartTime(authStartTime);
			}
			if (rs.getAclinkId() != null && (null == rentalBill.getAuthEndTime() || rentalBill.getAuthEndTime().before(authEndTime))) {
				rentalBill.setAuthEndTime(authEndTime);
			}
		}
	}

	private void setUseDetailStr(List<RentalBillRuleDTO> rules, RentalResource rs, RentalOrder rentalBill) {
		//拼装使用详情
		List<RentalCell> rentalCells = rules.stream().map(r-> findRentalSiteRuleById(r.getRuleId(),rs.getId(),rentalBill.getRentalType(),rentalBill.getResourceType())).
                collect(Collectors.toList());
		List<String> resourceNumbers = new ArrayList<>();
		if(rs.getAutoAssign() == NormalFlag.NEED.getCode())
			resourceNumbers = rentalCells.stream().map(r->r.getResourceNumber()).distinct().collect(Collectors.toList());
		else
			resourceNumbers.add("");
		SimpleDateFormat beginTimeSF = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat beginDateSF = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat endTimeSF = new SimpleDateFormat("HH:mm");
		StringBuilder useDetailSB = new StringBuilder();
		for (String resourceNumber:resourceNumbers){
			if (!StringUtils.isBlank(resourceNumber))
				useDetailSB.append(resourceNumber+"：");
			List<RentalCell> collect = rentalCells.stream().filter(r -> StringUtils.isBlank(resourceNumber) || resourceNumber.equals(r.getResourceNumber()))
					.collect(Collectors.toList());
			Collections.sort(collect,(q,p)->q.getId().compareTo(p.getId()));
			useDetailSB.append(getSingleNumberUseDetail(rentalBill.getRentalType(),collect,beginTimeSF,endTimeSF,beginDateSF,beginDateSF));
			useDetailSB.append("\n");
		}
		useDetailSB.deleteCharAt(useDetailSB.length()-1);
		rentalBill.setUseDetail(useDetailSB.toString());
	}

	//如果还有更多时间格式再逐步增加。。。
	private String getSingleNumberUseDetail(Byte rentalType,List<RentalCell> collect,SimpleDateFormat beginTimeSF,
											SimpleDateFormat endTimeSF,SimpleDateFormat beginDateSF,SimpleDateFormat beginMonthSF){
		StringBuilder useDetailSB = new StringBuilder();
		if(rentalType == RentalType.HOUR.getCode()){
			//按小时模式，拼接预约时间
			int size = collect.size();
			RentalCell firstRsr = collect.get(0);
			RentalCell lastRsr = collect.get(size - 1);
			if (null != firstRsr && null != lastRsr) {
				useDetailSB.append(beginTimeSF.format(firstRsr.getBeginTime()));
				useDetailSB.append("-");
				useDetailSB.append(endTimeSF.format(lastRsr.getEndTime()));

			}
		} else if (rentalType == RentalType.HALFDAY.getCode() ||
				rentalType == RentalType.THREETIMEADAY.getCode()) {
			int size = collect.size();
			RentalCell firstRsr = collect.get(0);
			RentalCell lastRsr = collect.get(size - 1);
			List<RentalTimeInterval> halfTimeIntervals = rentalv2Provider.queryRentalTimeIntervalByOwner(firstRsr.getResourceType(),
					RentalTimeIntervalOwnerType.RESOURCE_HALF_DAY.getCode(), firstRsr.getRentalResourceId());
			String [] name = new String[]{"上午","下午","晚上"};
			for (int i = 0 ; i < 3 ; i++)
				if (halfTimeIntervals != null && halfTimeIntervals.size() >i)
					name[i] = halfTimeIntervals.get(i).getName() == null ? name[i] : halfTimeIntervals.get(i).getName();

			Function<RentalCell,String>  f =r->{
				StringBuilder tmp = new StringBuilder();
				if (r.getAmorpm().equals(AmorpmFlag.AM.getCode())) {
					tmp.append(name[0]);
				} else if (r.getAmorpm().equals(AmorpmFlag.PM.getCode())) {
					tmp.append(name[1]);
				} else if (r.getAmorpm().equals(AmorpmFlag.NIGHT.getCode())) {
					tmp.append(name[2]);
				}
				return tmp.toString();
			};
			if (null != firstRsr && null != lastRsr) {
				useDetailSB.append(beginDateSF.format(firstRsr.getResourceRentalDate())).append(" ");
				useDetailSB.append(f.apply(firstRsr));
				if (!firstRsr.getId().equals(lastRsr.getId())){
					useDetailSB.append("至");
					useDetailSB.append(beginDateSF.format(lastRsr.getResourceRentalDate())).append(" ");
					useDetailSB.append(f.apply(lastRsr));
				}
			}
		} else{
			int size = collect.size();
			RentalCell firstRsr = collect.get(0);
			RentalCell lastRsr = collect.get(size - 1);
			if (null != firstRsr && null != lastRsr) {
				Calendar calendar = Calendar.getInstance();
				if (rentalType == RentalType.MONTH.getCode()) {
					useDetailSB.append(beginMonthSF.format(firstRsr.getResourceRentalDate()));
					useDetailSB.append("至");
					calendar.setTime(lastRsr.getResourceRentalDate());
					calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
					useDetailSB.append(beginMonthSF.format(calendar.getTime()));
				}else if (rentalType == RentalType.WEEK.getCode()){
					useDetailSB.append(beginDateSF.format(firstRsr.getResourceRentalDate()));
					useDetailSB.append("至");
					calendar.setTime(lastRsr.getResourceRentalDate());
					calendar.set(Calendar.DAY_OF_WEEK,7);
					calendar.add(Calendar.DATE,1);
					useDetailSB.append(beginDateSF.format(calendar.getTime()));
				}else if (rentalType == RentalType.DAY.getCode()){
					useDetailSB.append(beginDateSF.format(firstRsr.getResourceRentalDate()));
					useDetailSB.append("至");
					useDetailSB.append(beginDateSF.format(lastRsr.getResourceRentalDate()));
				}
			}
		}
		return useDetailSB.toString();
	}


	@Override
	public CommonOrderDTO getRentalBillPayInfo(GetRentalBillPayInfoCommand cmd) {
		RentalOrder bill = rentalv2Provider.findRentalBillById(cmd.getId());

		if (null == bill) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER, "RentalOrder not found");
		}

		if (bill.getStatus().equals(SiteBillStatus.FAIL.getCode())) {
			LOGGER.error("Order has been canceled");
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_ORDER_CANCELED, "Order has been canceled");
		}
		if (bill.getPayTotalMoney().compareTo(new BigDecimal(0)) == 0 &&
				bill.getPayMode().equals(PayMode.APPROVE_ONLINE_PAY.getCode())){
			changeRentalOrderStatus(bill,SiteBillStatus.SUCCESS.getCode(),true);
            //工作流自动进到下一节点
            FlowCase flowCase = flowCaseProvider.findFlowCaseByReferId(bill.getId(), REFER_TYPE, moduleId);
            FlowCaseTree tree = flowService.getProcessingFlowCaseTree(flowCase.getId());
            flowCase = tree.getLeafNodes().get(0).getFlowCase();//获取真正正在进行的flowcase
            FlowAutoStepDTO stepDTO = new FlowAutoStepDTO();
            stepDTO.setAutoStepType(FlowStepType.APPROVE_STEP.getCode());
            stepDTO.setFlowCaseId(flowCase.getId());
            stepDTO.setFlowMainId(flowCase.getFlowMainId());
            stepDTO.setFlowNodeId(flowCase.getCurrentNodeId());
            stepDTO.setFlowVersion(flowCase.getFlowVersion());
            stepDTO.setStepCount(flowCase.getStepCount());
            flowService.processAutoStep(stepDTO);
			return null;
		}
		return buildCommonOrderDTO(bill);

	}

	private CommonOrderDTO buildCommonOrderDTO(RentalOrder bill) {
		//调用统一处理订单接口，返回统一订单格式
		CommonOrderCommand orderCmd = new CommonOrderCommand();
		orderCmd.setBody(OrderType.OrderTypeEnum.RENTALORDER.getMsg());
		//续费 欠费订单重新生成订单号
		if (bill.getStatus() != SiteBillStatus.PAYINGFINAL.getCode()&&
				bill.getStatus() != SiteBillStatus.APPROVING.getCode()) {
			bill.setOrderNo(onlinePayService.createBillId(DateHelper.currentGMTTime().getTime()).toString());
			rentalv2Provider.updateRentalBill(bill);//更新新的订单号
		}
		orderCmd.setOrderNo(bill.getOrderNo());
		orderCmd.setOrderType(OrderType.OrderTypeEnum.RENTALORDER.getPycode());
		orderCmd.setSubject(OrderType.OrderTypeEnum.RENTALORDER.getMsg());
		orderCmd.setTotalFee(bill.getPayTotalMoney().subtract(bill.getPaidMoney()));

		CommonOrderDTO dto;
		try {
			dto = commonOrderUtil.convertToCommonOrderTemplate(orderCmd);
		} catch (Exception e) {
			LOGGER.error("convertToCommonOrder is fail.", e);
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_CREATE_ORDER,
					"convertToCommonOrder is fail.");
		}

		return dto;
	}

	private PreOrderCommand buildPreOrderDTO(RentalOrder order, String clientAppName, Integer paymentType) {
		PreOrderCommand preOrderCommand = new PreOrderCommand();

		preOrderCommand.setOrderType(OrderType.OrderTypeEnum.RENTALORDER.getPycode());
		boolean ifCreateNewOrder = false;
		//续费 欠费订单重新生成订单号
		if (order.getStatus() != SiteBillStatus.PAYINGFINAL.getCode() &&
				order.getStatus() != SiteBillStatus.APPROVING.getCode())
		    ifCreateNewOrder = true;
		preOrderCommand.setOrderId(Long.valueOf(order.getOrderNo()));
		BigDecimal amount = order.getPayTotalMoney().subtract(order.getPaidMoney());
		preOrderCommand.setAmount(amount);

		preOrderCommand.setPayerId(order.getRentalUid());
		preOrderCommand.setNamespaceId(UserContext.getCurrentNamespaceId());
		preOrderCommand.setExtendInfo(String.format("项目名称:%s,资源名称:%s", communityProvider.findCommunityById(order.getCommunityId()).getName()
				,order.getResourceName()));
		//公众号支付
		if (paymentType != null && paymentType == PaymentType.WECHAT_JS_PAY.getCode()) {
			preOrderCommand.setPaymentType(PaymentType.WECHAT_JS_ORG_PAY.getCode());
			PaymentParamsDTO paymentParamsDTO = new PaymentParamsDTO();
			paymentParamsDTO.setPayType("no_credit");
			User user = UserContext.current().getUser();
			paymentParamsDTO.setAcct(user.getNamespaceUserToken());
			//TODO: 临时给越空间解决公众号支付
			String vspCusid = configurationProvider.getValue(UserContext.getCurrentNamespaceId(), "tempVspCusid", "");
			paymentParamsDTO.setVspCusid(vspCusid);
			preOrderCommand.setPaymentParams(paymentParamsDTO);
			preOrderCommand.setCommitFlag(1);
		}
		preOrderCommand.setClientAppName(clientAppName);

		if (ifCreateNewOrder){
            order.setOrderNo(onlinePayService.createBillId(DateHelper.currentGMTTime().getTime()).toString());
            rentalv2Provider.updateRentalBill(order);//更新新的订单号
			preOrderCommand.setOrderId(Long.valueOf(order.getOrderNo()));
        }return preOrderCommand;
	}

	public PreOrderDTO getRentalBillPayInfoV2(GetRentalBillPayInfoCommand cmd) {
		return (PreOrderDTO)getRentalBillPayInfo(cmd,ActivityRosterPayVersionFlag.V2);
	}

	private Object getRentalBillPayInfo(GetRentalBillPayInfoCommand cmd,ActivityRosterPayVersionFlag version){RentalOrder order = rentalv2Provider.findRentalBillById(cmd.getId());

		if (null == order) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_CANNOT_FIND_ORDER,
					 "RentalOrder not found");
		}

		if (order.getStatus().equals(SiteBillStatus.FAIL.getCode())) {
			LOGGER.error("Order has been canceled");
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_ORDER_CANCELED, "Order has been canceled");
		}
		if (order.getPayTotalMoney().compareTo(new BigDecimal(0)) == 0 ){

			changeRentalOrderStatus(order,SiteBillStatus.SUCCESS.getCode(),true);//工作流自动进到下一节点
			//工作流自动进到下一节点
            FlowCase flowCase = flowCaseProvider.findFlowCaseByReferId(order.getId(), REFER_TYPE, moduleId);
            FlowCaseTree tree = flowService.getProcessingFlowCaseTree(flowCase.getId());
            flowCase = tree.getLeafNodes().get(0).getFlowCase();//获取真正正在进行的flowcase
            FlowAutoStepDTO stepDTO = new FlowAutoStepDTO();
            stepDTO.setAutoStepType(FlowStepType.APPROVE_STEP.getCode());
            stepDTO.setFlowCaseId(flowCase.getId());
            stepDTO.setFlowMainId(flowCase.getFlowMainId());
            stepDTO.setFlowNodeId(flowCase.getCurrentNodeId());
            stepDTO.setFlowVersion(flowCase.getFlowVersion());
            stepDTO.setStepCount(flowCase.getStepCount());
            flowService.processAutoStep(stepDTO);return null;
		}
		PreOrderCommand preOrderCommand = buildPreOrderDTO(order, cmd.getClientAppName(), cmd.getPaymentType());
		Object obj = null;
        if (ActivityRosterPayVersionFlag.V2 == version) {
            PreOrderDTO preOrderDTO = rentalv2PayService.createPreOrder(preOrderCommand, order);
            obj = preOrderDTO;
        }else if (ActivityRosterPayVersionFlag.V3 == version){
            AddRentalBillItemV3Response merchantPreOrder = rentalv2PayService.createMerchantPreOrder(preOrderCommand, order);
            obj = merchantPreOrder;
        }//保存支付订单信息
		Rentalv2OrderRecord record = this.rentalv2AccountProvider.getOrderRecordByOrderNo(Long.valueOf(order.getOrderNo()));
		if (record != null){ //欠费订单保存
			record.setOrderId(order.getId());
			record.setStatus((byte)0);//未支付
			record.setNamespaceId(UserContext.getCurrentNamespaceId());
			if (order.getStatus().equals(SiteBillStatus.OWING_FEE.getCode()))
				record.setPaymentOrderType(OrderRecordType.OWNINGFEE.getCode());//欠费订单
			else
				record.setPaymentOrderType(OrderRecordType.NORMAL.getCode());//支付订单
			this.rentalv2AccountProvider.updateOrderRecord(record);
		}
		return obj;
    }

	@Override
	public AddRentalBillItemV3Response getRentalBillPayInfoV3(GetRentalBillPayInfoCommand cmd) {
		return (AddRentalBillItemV3Response)getRentalBillPayInfo(cmd,ActivityRosterPayVersionFlag.V3);
	}

	@Override
	public void offlinePayOrder(OfflinePayOrderCommand cmd) {
		RentalOrder order = rentalv2Provider.findRentalBillById(cmd.getOrderId());
		if (null == order) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER, "RentalOrder not found");
		}
		order.setVendorType(VendorType.OFFLINE.getCode());
		rentalv2Provider.updateRentalBill(order);
		changeRentalOrderStatus(order,SiteBillStatus.SUCCESS.getCode(),true);
	}

	@Override
	public void changeRentalOrderStatus(RentalOrder order, Byte status, Boolean cancelOtherOrderFlag) {
	    //防止二次进入
        if (order.getStatus().equals(status))
            return;
		//用基于服务器平台的锁 验证线下支付 的剩余资源是否足够
		List<RentalBillRuleDTO> rules = new ArrayList<>();
		List<Long> resourceRuleIds = new ArrayList<>();

		RentalResource rs = rentalCommonService.getRentalResource(order.getResourceType(), order.getRentalResourceId());

		RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(null, null,
				rs.getResourceType(), rs.getResourceTypeId(), RuleSourceType.RESOURCE.getCode(), rs.getId());

		//状态为已完成时，不需要验证，之前付款时，已经做过校验
		// 注意： validateRentalBill 方法校验时，会查出当前资源状态：已预约，此时验证会不通过
		if (SiteBillStatus.COMPLETE.getCode() != status && SiteBillStatus.FAIL.getCode() != status) {
			processCells(rs, order.getRentalType());
			rules.addAll(getBillRules(order));
		}

		this.coordinationProvider.getNamedLock(CoordinationLocks.CREATE_RENTAL_BILL.getCode() + order.getRentalResourceId())
				.enter(() -> {
					//验证订单下的资源是否足够
					this.validateRentalBill(rules, rs, rule,order.getRentalType());

					//本订单状态置为成功,
					order.setStatus(status);
					rentalv2Provider.updateRentalBill(order);
					return null;
				});

		if (SiteBillStatus.SUCCESS.getCode() == status){
			onOrderSuccess(order);
		}


		//根据产品定义，是在待审批的节点就不允许其他用户预订 同时段统一资源（状态不是已预约成功，比如待付款）
		if (cancelOtherOrderFlag) {
			//找到所有有这些ruleids的订单
			List<RentalOrder> otherOrders = this.rentalv2Provider.findRentalSiteBillBySiteRuleIds(resourceRuleIds);
			if (null != otherOrders && otherOrders.size() > 0) {
				for (RentalOrder otherOrder : otherOrders) {
					LOGGER.debug("otherOrder is " + JSON.toJSONString(otherOrder));
					//把自己排除
					if (otherOrder.getId().equals(order.getId())) {
						continue;
					}
					//剩下的用线程池处理flowcase状态和发短信
					executorPool.execute(() -> {
						//其他订单置为失败工作流设置为终止
						FlowCase flowcase = flowCaseProvider.findFlowCaseByReferId(otherOrder.getId(), REFER_TYPE, Rentalv2Controller.moduleId);
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
						rentalCommonService.sendMessageCode(order.getRentalUid(), map, RentalNotificationTemplateCode.RENTAL_CANCEL_CODE);

						String templateScope = SmsTemplateCode.SCOPE;
						int templateId = SmsTemplateCode.RENTAL_CANCEL_CODE;
						String templateLocale = RentalNotificationTemplateCode.locale;

						List<Tuple<String, Object>> variables = smsProvider.toTupleList("useTime", otherOrder.getUseDetail());
						smsProvider.addToTupleList(variables, "resourceName", otherOrder.getResourceName());

						UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(otherOrder.getRentalUid(), IdentifierType.MOBILE.getCode());
						if (null == userIdentifier) {
							LOGGER.debug("userIdentifier is null...userId = " + otherOrder.getRentalUid());
						} else {
							smsProvider.sendSms(otherOrder.getNamespaceId(), userIdentifier.getIdentifierToken(), templateScope, templateId, templateLocale, variables);
						}
					});
				}
			}
		}
	}

	/**
	 * 给成功的订单发推送
	 */
	@Override
	public void addOrderSendMessage(RentalOrder rentalBill) {
		//消息推送
		//定时任务给用户推送
		User user = this.userProvider.findUserById(rentalBill.getRentalUid());
		if (null == user)
			return;
		Map<String, String> map = new HashMap<String, String>();
		RentalResource rs = this.rentalv2Provider.getRentalSiteById(rentalBill.getRentalResourceId());
		if (null == rs)
			return;

		try {
			map = new HashMap<>();
			map.put("userName", user.getNickName());
			map.put("resourceName", rentalBill.getResourceName());
			map.put("useDetail", rentalBill.getUseDetail());
			map.put("rentalCount", rentalBill.getRentalCount() == null ? "1" : "" + rentalBill.getRentalCount());
			sendMessageCode(rs.getChargeUid(), RentalNotificationTemplateCode.locale, map, RentalNotificationTemplateCode.RENTAL_ADMIN_NOTIFY);
		} catch (Exception e) {
			LOGGER.error("SEND MESSAGE FAILED ,cause " + e.getLocalizedMessage());
		}
	}



	public void sendMessageCode(String uids, String locale, Map<String, String> map, int code) {
		String scope = RentalNotificationTemplateCode.SCOPE;
		String notifyTextForOther = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
		rentalCommonService.sendMessageToUser(uids,notifyTextForOther);
	}


	private List<RentalCell> findGroupRentalSiteRules(RentalCell rsr) {
		List<RentalCell> result = new ArrayList<>();
		for (RentalCell cell : cellList.get()) {
			RentalType rentalType = RentalType.fromCode(cell.getRentalType());
			if (null != rentalType) {
				switch (rentalType) {
					case DAY:
						if (cell.getRentalType().equals(rsr.getRentalType()) && cell.getResourceRentalDate().equals(rsr.getResourceRentalDate()))
							result.add(cell);
						break;
					case MONTH:
						if (cell.getRentalType().equals(rsr.getRentalType()) && cell.getResourceRentalDate().equals(rsr.getResourceRentalDate()))
							result.add(cell);
						break;
					case HOUR:
						if (cell.getRentalType().equals(rsr.getRentalType()) && cell.getResourceRentalDate().equals(rsr.getResourceRentalDate())
								&& cell.getBeginTime().equals(rsr.getBeginTime()))
							result.add(cell);
						break;
					default:
						if (cell.getRentalType().equals(rsr.getRentalType()) && cell.getResourceRentalDate().equals(rsr.getResourceRentalDate())
								&& cell.getAmorpm().equals(rsr.getAmorpm()))
							result.add(cell);
						break;
				}
			}
		}
		return result;
	}

	@Scheduled(cron = "30 29,59 * * * ? ")
	public void autoCompleteBills() {
		if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
			coordinationProvider.getNamedLock("Rental_schedule_flag1") //集群运行时只有一台执行定时任务
					.tryEnter(() -> {
						LOGGER.info("autoCompleteBills start:");
						List<RentalOrder> orders = rentalv2Provider.listTargetRentalBills(SiteBillStatus.IN_USING.getCode()); //捞出使用中的订单
						Long currTime = DateHelper.currentGMTTime().getTime();
						for (RentalOrder order : orders) {
							if (order.getResourceType().equals(RentalV2ResourceType.VIP_PARKING.getCode())) {
								LOGGER.info("the bill id is:{} startTime:{} endTime:{}", order.getId(), order.getStartTime(), order.getEndTime());
								if (currTime + 60 * 1000L >= order.getEndTime().getTime()) {
									VipParkingUseInfoDTO parkingInfo = JSONObject.parseObject(order.getCustomObject(), VipParkingUseInfoDTO.class);
									ParkingSpaceDTO spaceDTO = dingDingParkingLockHandler.getParkingSpaceLock(parkingInfo.getLockId());
									if (null != spaceDTO && spaceDTO.getLockStatus().equals(ParkingSpaceLockStatus.UP.getCode())) {//车锁升起 自动结束
										RentalOrderHandler orderHandler = rentalCommonService.getRentalOrderHandler(order.getResourceType());
										restoreRentalBill(order);
										if ((order.getPayTotalMoney().subtract(order.getPaidMoney())).compareTo(BigDecimal.ZERO) == 0)
											order.setStatus(SiteBillStatus.COMPLETE.getCode());
										else
											order.setStatus(SiteBillStatus.OWING_FEE.getCode());
										order.setActualEndTime(new Timestamp(System.currentTimeMillis()));
										order.setActualStartTime(order.getStartTime());
										rentalv2Provider.updateRentalBill(order);
										orderHandler.completeRentalOrder(order);
										//发消息
										RentalMessageHandler handler = rentalCommonService.getRentalMessageHandler(order.getResourceType());

										if (order.getStatus() == SiteBillStatus.OWING_FEE.getCode()) {
											handler.overTimeSendMessage(order);
										} else if (order.getStatus() == SiteBillStatus.COMPLETE.getCode()) {
											handler.completeOrderSendMessage(order);
										}
									}
								}
							}else{
								if (currTime + 60 * 1000L >= order.getEndTime().getTime()) {
									order.setStatus(SiteBillStatus.COMPLETE.getCode());
									rentalv2Provider.updateRentalBill(order);
								}
							}
						}
					});
		}
	}

	/**
	 * 每半小时的50秒钟时候开始执行
	 */
	@Scheduled(cron = "50 0/30 * * * ?")
	@Override
	public void rentalSchedule(){
		Boolean [] flag = {false};
		Long currTime = DateHelper.currentGMTTime().getTime();
		coordinationProvider.getNamedLock("Rental_schedule_flag2" ) //集群运行时只有一台执行定时任务
				.tryEnter(() -> {
					Long temp = configurationProvider.getLongValue(0,"rental.shcedule.flag",0l);
					Long timeFlag = currTime / 600000;
					if (!temp.equals(timeFlag)){
						configurationProvider.setLongValue(0,"rental.shcedule.flag",timeFlag);
						flag[0] = true;
					}
				});
		if(RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE  && flag[0]){
			//把所有状态为success-已预约的捞出来
			List<RentalOrder>  orders = rentalv2Provider.listTargetRentalBills(SiteBillStatus.SUCCESS.getCode());
			for(RentalOrder order : orders ){
				if (order.getResourceType().equals(RentalV2ResourceType.DEFAULT.getCode())) {
					Long orderReminderTimeLong = order.getReminderTime()!=null?order.getReminderTime().getTime():0L;
					Long orderEndTimeLong = order.getEndTime()!=null?order.getEndTime().getTime():0L;
					Long orderReminderEndTimeLong = order.getReminderEndTime()!=null?order.getReminderEndTime().getTime():0L;
					//时间快到发推送
					if(currTime<orderReminderTimeLong && currTime + 30*60*1000L >= orderReminderTimeLong){
						Map<String, Object> messageMap = new HashMap<>();
						messageMap.put("orderId",order.getId());
						messageMap.put("resourceType",order.getResourceType());
						scheduleProvider.scheduleSimpleJob(
								"RentalNearStartMessageJob" + order.getId(),
								"RentalNearStartMessageJob" + order.getId(),
								new java.util.Date(orderReminderTimeLong),
								RentalNearStartMessageJob.class,
								messageMap
						);
					}

					//结束时间快到发推送
					if(currTime<orderReminderEndTimeLong && currTime + 30*60*1000L >= orderReminderEndTimeLong){
						Map<String, Object> messageMap = new HashMap<>();
						messageMap.put("orderId",order.getId());
						messageMap.put("resourceType",order.getResourceType());
						scheduleProvider.scheduleSimpleJob(
								"RentalNearStartMessageJob" + order.getId(),
								"RentalNearEndMessageJob" + order.getId(),
								new java.util.Date(orderReminderEndTimeLong),
								RentalNearEndMessageJob.class,
								messageMap
						);
					}
					//使用中
					if (currTime >= order.getStartTime().getTime() ) {
						order.setStatus(SiteBillStatus.IN_USING.getCode());
						rentalv2Provider.updateRentalBill(order);
					}

					if(orderEndTimeLong <= currTime){
						order.setStatus(SiteBillStatus.COMPLETE.getCode());
						rentalv2Provider.updateRentalBill(order);
					}
				}else if (order.getResourceType().equals(RentalV2ResourceType.VIP_PARKING.getCode())) {
					//订单开始 置为使用中的状态
					if (currTime >= order.getStartTime().getTime() ) {
						RentalOrderHandler orderHandler = rentalCommonService.getRentalOrderHandler(order.getResourceType());
						orderHandler.autoUpdateOrder(order);
					}
					Long orderReminderEndTimeLong = order.getReminderEndTime()!=null?order.getReminderEndTime().getTime():0L;

					if(currTime<orderReminderEndTimeLong && currTime + 30*60*1000L >= orderReminderEndTimeLong){

						Map<String, Object> messageMap = new HashMap<>();
						messageMap.put("orderId",order.getId());
						messageMap.put("methodName", "endReminderSendMessage");
						scheduleProvider.scheduleSimpleJob(
								"RentalMessageQuartzJob" + order.getId(),
								"RentalMessageQuartzJob" + order.getId(),
								new java.util.Date(orderReminderEndTimeLong),
								RentalMessageQuartzJob.class,
								messageMap
						);

					}

				}
			}
			//超时取消
			orders = rentalv2Provider.listTargetRentalBills(SiteBillStatus.APPROVING.getCode());
			for(RentalOrder order : orders ){
				if (currTime >= order.getStartTime().getTime() ) {
					onOrderCancel(order,"由于管理员超时未处理，自动中止");
				}
			}
		}
	}

	@Override
	public void test(GetRentalOrderDetailCommand cmd) {
		RentalOrder order = this.rentalv2Provider.findRentalBillById(cmd.getOrderId());
			Map<String, Object> messageMap = new HashMap<>();
			messageMap.put("orderId",order.getId());
			messageMap.put("resourceType",order.getResourceType());
			scheduleProvider.scheduleSimpleJob(
					"RentalNearStartMessageJob" + order.getId(),
					"RentalNearStartMessageJob" + order.getId(),
					new java.util.Date(order.getReminderTime().getTime()),
					RentalNearStartMessageJob.class,
					messageMap
			);


		//结束时间快到发推送
			messageMap = new HashMap<>();
			messageMap.put("orderId",order.getId());
			messageMap.put("resourceType",order.getResourceType());
			scheduleProvider.scheduleSimpleJob(
					"RentalNearEndMessageJob" + order.getId(),
					"RentalNearEndMessageJob" + order.getId(),
					new java.util.Date(order.getReminderEndTime().getTime()),
					RentalNearEndMessageJob.class,
					messageMap
			);

	}

	@Override
	public void validateRentalBill(List<RentalBillRuleDTO> ruleDTOs, RentalResource rs,
								   RentalDefaultRule rule,Byte rentalType) {

		List<Rentalv2PriceRule> priceRules = rentalv2PriceRuleProvider.listPriceRuleByOwner(rs.getResourceType(),
				PriceRuleType.RESOURCE.getCode(), rs.getId());
		// 如果有一个规则，剩余的数量少于预定的数量
		for (RentalBillRuleDTO dto : ruleDTOs) {
			if (dto.getRuleId() == null) {
				throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERRPR_LOST_RESOURCE_RULE,
						"Invalid param rules");
			}
			RentalCell cell = findRentalSiteRuleById(dto.getRuleId(),rs.getId(),rentalType,rs.getResourceType());
			if (null == cell) {
				throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERRPR_LOST_RESOURCE_RULE,
						"Invalid param rules");
			}

			validateCellStatus(cell, priceRules, rs, rule, true);
		}

	}


	@Override
	public FindRentalBillsCommandResponse findRentalBills(FindRentalBillsCommand cmd) {
		Long userId = UserContext.current().getUser().getId();
		if (cmd.getPageAnchor() == null)
			cmd.setPageAnchor(9223372036854775807L);
		int pageSize = PaginationConfigHelper.getPageSize(
				configurationProvider, cmd.getPageSize());
		List<Byte> status = null;
		if (cmd.getBillStatus() != null) {
			status = new ArrayList<>();
			if (cmd.getBillStatus().equals(BillQueryStatus.UNPAY.getCode())) {

				status.add(SiteBillStatus.PAYINGFINAL.getCode());
				status.add(SiteBillStatus.APPROVING.getCode());
			}
			if (cmd.getBillStatus().equals(BillQueryStatus.VALID.getCode())) {
				status.add(SiteBillStatus.SUCCESS.getCode());
				status.add(SiteBillStatus.IN_USING.getCode());
				status.add(SiteBillStatus.REFUNDING.getCode());
			} else if (cmd.getBillStatus().equals(BillQueryStatus.CANCELED.getCode())) {
				status.add(SiteBillStatus.FAIL.getCode());
				status.add(SiteBillStatus.REFUNDED.getCode());
			} else if (cmd.getBillStatus().equals(BillQueryStatus.FINISHED.getCode())) {
				status.add(SiteBillStatus.COMPLETE.getCode());
				status.add(SiteBillStatus.OVERTIME.getCode());
			} else if (cmd.getBillStatus().equals(BillQueryStatus.OWNFEE.getCode()))
				status.add(SiteBillStatus.OWING_FEE.getCode());
		}
		ListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<RentalOrder> billList = this.rentalv2Provider.listRentalBills(cmd.getId(), userId, cmd.getRentalSiteId(), cmd.getResourceType(),
				cmd.getResourceTypeId(), locator, pageSize + 1, status, null);
		FindRentalBillsCommandResponse response = new FindRentalBillsCommandResponse();
		if (null == billList)
			return response;
		if(billList.size() > pageSize) {
			billList.remove(billList.size() - 1);
			response.setNextPageAnchor( billList.get(billList.size() -1).getReserveTime().getTime());
		}
		checkRentalBills(billList,false);
		response.setRentalBills(new ArrayList<>());
		for (RentalOrder bill : billList) {
			RentalBillDTO dto = processOrderDTO(bill);
			if (dto.getStatus().equals(SiteBillStatus.FAIL.getCode()) && dto.getPaidPrice()!=null && dto.getPaidPrice().compareTo(new BigDecimal(0))>0)
				dto.setStatus(SiteBillStatus.FAIL_PAID.getCode());
			response.getRentalBills().add(dto);
		}
		return response;
	}


	//自动取消待支付订单的定时任务会在重启后失效 每次查询都校验一下有没有漏的
	private void checkRentalBills(List<RentalOrder> billList , boolean ifHold){
		if (billList == null || billList.isEmpty())
			return;
		int i = 0;
		while (i < billList.size()) {
			if (SiteBillStatus.PAYINGFINAL.getCode() == billList.get(i).getStatus() && billList.get(i).getPayMode() == PayMode.ONLINE_PAY.getCode()) {
				if (System.currentTimeMillis() > billList.get(i).getReserveTime().getTime() + ORDER_AUTO_CANCEL_TIME) {
					billList.get(i).setStatus(SiteBillStatus.FAIL.getCode());
					rentalv2Provider.updateRentalBill(billList.get(i));
					if (!ifHold){
						billList.remove(i);
						continue;
					}

				}
			}
			i++;
		}
	}

	private RentalBillDTO processOrderDTO(RentalOrder bill) {

		// 在转换bill到dto的时候统一先convert一下  modify by wuhan 20160804
		RentalBillDTO dto = ConvertHelper.convert(bill, RentalBillDTO.class);
		mappingRentalBillDTO(dto, bill, null);
		if (dto.getStatus().equals(SiteBillStatus.PAYINGFINAL.getCode())) {
			dto.setUnpayCancelTime(bill.getReserveTime().getTime() + ORDER_AUTO_CANCEL_TIME);
		}
		if (dto.getStatus().equals(SiteBillStatus.REFUNDED.getCode())) {
			dto.setRefundAmount(bill.getRefundAmount());
		}

		if (bill.getDoorAuthId() != null) {
			{
				SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm");
				dto.setDoorAuthTime(dateFormat.format(new java.util.Date(bill.getAuthStartTime().getTime())) + "到" +
						dateFormat.format(new java.util.Date(bill.getAuthEndTime().getTime())));
			}
		}

		//设置退款人姓名 联系方式

		RentalResource rs = rentalCommonService.getRentalResource(bill.getResourceType(),bill.getRentalResourceId());
		if (rs.getOfflinePayeeUid()!=null){
			OrganizationMember member = organizationProvider.findOrganizationMemberByUIdAndOrgId(rs.getOfflinePayeeUid(), rs.getOrganizationId());
			if(null!=member){
				dto.setOfflinePayName(member.getContactName());
				UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(member.getTargetId(), IdentifierType.MOBILE.getCode());
				if (userIdentifier != null)
					dto.setOfflinePayPhone(userIdentifier.getIdentifierToken());
			}
		}

		//获取真正的工作流
		if (dto.getFlowCaseId() != null){
			FlowCaseTree tree = flowService.getProcessingFlowCaseTree(dto.getFlowCaseId());
			FlowCase flowCase = tree.getLeafNodes().get(0).getFlowCase();
			if (flowCase != null)
				dto.setFlowCaseId(flowCase.getId());
		}

		//设置scene
		if (StringHelper.hasContent(dto.getScene()))
			dto.setScene(dto.getScene().split(",")[0]);
		return dto;
	}

	@Override
	public void mappingRentalBillDTO(RentalBillDTO dto, RentalOrder bill, RentalResource rs) {
		//返回资源数据
		if (null != rs && rs.getResourceType().equals(RentalV2ResourceType.VIP_PARKING.getCode())) {
			VipParkingUseInfoDTO vipParkingUseInfoDTO = new VipParkingUseInfoDTO();
			vipParkingUseInfoDTO.setParkingLotId(rs.getId());
			vipParkingUseInfoDTO.setParkingLotName(rs.getResourceName());
			dto.setCustomObject(JSONObject.toJSONString(vipParkingUseInfoDTO));
		}

		dto.setOrderNo(bill.getOrderNo());
		UserIdentifier userIdentifier = this.userProvider.findClaimedIdentifierByOwnerAndType(bill.getRentalUid(), IdentifierType.MOBILE.getCode());
		if (null == userIdentifier) {
			LOGGER.debug("userIdentifier is null...userId = " + bill.getRentalUid());
		} else {
			dto.setUserPhone(userIdentifier.getIdentifierToken());
		}
		User user = this.userProvider.findUserById(bill.getRentalUid());
		if (null != user) {
			dto.setUserName(user.getNickName());
		} else {
			LOGGER.debug("user is null...userId = " + bill.getRentalUid());
			dto.setUserName("用户不在系统中");
		}
		dto.setSiteName(bill.getResourceName());
		dto.setResourceAddress(bill.getAddress());
		dto.setContactPhonenum(bill.getContactPhonenum());

		dto.setSpec(bill.getSpec());
		dto.setNotice(bill.getNotice());
		dto.setIntroduction(bill.getIntroduction());
		dto.setRentalBillId(bill.getId());
		dto.setPackageName(bill.getPackageName());

		if (null != bill.getStartTime()) {
			dto.setStartTime(bill.getStartTime().getTime());

		}
		if (null != bill.getEndTime()) {
			dto.setEndTime(bill.getEndTime().getTime());
		}
		dto.setReserveTime(bill.getReserveTime().getTime());

		if (null != bill.getPayTime()) {
			dto.setPayTime(bill.getPayTime().getTime());
		}

		if (null != bill.getCancelTime()) {
			dto.setCancelTime(bill.getCancelTime().getTime());
		}

		if (null != bill.getUserEnterpriseId()) {
			Organization org = this.organizationProvider.findOrganizationById(bill.getUserEnterpriseId());
			if (org != null) {
				dto.setCompanyName(org.getName());
				List<OrganizationAddress> addresses = this.organizationProvider.findOrganizationAddressByOrganizationId(bill.getUserEnterpriseId());
				if (addresses != null && addresses.size() > 0) {
					dto.setBuildingName(addresses.get(0).getBuildingName());
					Address add = this.addressProvider.findAddressById(addresses.get(0).getAddressId());
					if (add != null)
						dto.setAddress(add.getAddress());
				}
			}
		}

		dto.setTotalPrice(bill.getPayTotalMoney());
		dto.setSitePrice(bill.getResourceTotalMoney());
		dto.setPaidPrice(bill.getPaidMoney());
		dto.setUnPayPrice(bill.getPayTotalMoney().subtract(bill.getPaidMoney()));
		dto.setInvoiceFlag(bill.getInvoiceFlag());
		dto.setStatus(bill.getStatus());
		dto.setRentalSiteRules(new ArrayList<>());
		//支付方式
		dto.setVendorType(bill.getVendorType());

		// 订单的rules
		dto.setRentalSiteRules(new ArrayList<>());
		List<RentalResourceOrder> rsbs = rentalv2Provider.findRentalResourceOrderByOrderId(bill.getId());
		for (RentalResourceOrder rsb : rsbs) {
			RentalSiteRulesDTO ruleDto = new RentalSiteRulesDTO();
			ruleDto.setId(rsb.getId());
			ruleDto.setRentalSiteId(rsb.getRentalResourceRuleId());
			ruleDto.setRentalType(rsb.getRentalType());
			ruleDto.setRentalStep(1);
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
		List<RentalItemsOrder> ribs = rentalv2Provider.findRentalItemsBillBySiteBillId(bill.getId(), bill.getResourceType());
		if (null != ribs) {
			dto.setSiteItems(new ArrayList<>());
			//dto不是final的变量就改为for循环,不用foreach
			for (RentalItemsOrder item : ribs) {
				SiteItemDTO siDTO = ConvertHelper.convert(item, SiteItemDTO.class);
				siDTO.setImgUrl(this.contentServerService.parserUri(item.getImgUri(), EntityType.USER.getCode(),
						UserContext.currentUserId()));
				siDTO.setItemName(item.getItemName());
				siDTO.setCounts(item.getRentalCount());
				siDTO.setItemType(item.getItemType());
				dto.getSiteItems().add(siDTO);
			}
		}
		dto.setUseDetail(bill.getUseDetail());

		// 订单的附件attachments
		dto.setBillAttachments(new ArrayList<>());
		List<RentalOrderAttachment> attachments = rentalv2Provider.findRentalBillAttachmentByBillId(dto.getRentalBillId());
		for (RentalOrderAttachment attachment : attachments) {
			BillAttachmentDTO attachmentDTO = new BillAttachmentDTO();
			attachmentDTO.setAttachmentType(attachment.getAttachmentType());
			attachmentDTO.setBillId(attachment.getRentalOrderId());
			if (attachment.getAttachmentType().equals(AttachmentType.ATTACHMENT.getCode())) {
				attachmentDTO.setResourceUrl(this.contentServerService.parserUri(attachment.getContent(), EntityType.USER.getCode(), UserContext.current().getUser().getId()));
				ContentServerResource csr = this.contentServerService.findResourceByUri(attachment.getContent());
				if (csr != null) {
					attachmentDTO.setResourceName(csr.getResourceName());
					attachmentDTO.setResourceSize(csr.getResourceSize());
				}
			} else if (AttachmentType.RECOMMEND_USER.getCode().equals(attachment.getAttachmentType())) {
				List<RentalConfigAttachment> tempAttachments = rentalv2Provider
						.queryRentalConfigAttachmentByOwner(bill.getResourceType(), AttachmentType.ORDER_RECOMMEND_USER.name(),
								bill.getId(), null);
				List<RentalRecommendUser> recommendUsers = tempAttachments.stream()
						.map(r -> ConvertHelper.convert(r, RentalRecommendUser.class)).collect(Collectors.toList());
				attachmentDTO.setRecommendUsers(recommendUsers);

			} else if (attachment.getAttachmentType().equals(AttachmentType.GOOD_ITEM.getCode())) {
				List<RentalConfigAttachment> tempAttachments = rentalv2Provider
						.queryRentalConfigAttachmentByOwner(bill.getResourceType(), AttachmentType.ORDER_GOOD_ITEM.name(),
								bill.getId(), null);
				List<RentalGoodItem> goodItems = tempAttachments.stream()
						.map(r -> ConvertHelper.convert(r, RentalGoodItem.class)).collect(Collectors.toList());
				attachmentDTO.setGoodItems(goodItems);
			}
			attachmentDTO.setContent(attachment.getContent());

			attachmentDTO.setId(attachment.getId());
			dto.getBillAttachments().add(attachmentDTO);
		}

		//订单的文件
		List<RentalResourceFile> files = rentalv2Provider.findRentalSiteFilesByOwnerTypeAndId(bill.getResourceType(), EhRentalv2Orders.class.getSimpleName(),
				bill.getId());
		dto.setFileUris(convertRentalSiteFileDTOs(files));

		//加上资源类型的类型
		if (bill.getResourceTypeId() != null) {
			RentalResourceType resourceType = this.rentalv2Provider.getRentalResourceTypeById(bill.getResourceTypeId());
			dto.setResourceType(resourceType.getIdentify());
		}
		//每日开放时间
		dto.setOpenTime(getResourceOpenTime(bill.getResourceType(),bill.getRentalResourceId(),bill.getRentalType(),"\n"));

	}

	private String parseTimeInterval(Double time){
		if (time.compareTo(time.intValue()+0.0)>0)
			return time.intValue()<10?"0"+time.intValue()+":30":time.intValue()+":30";
		else
			return time.intValue()<10?"0"+time.intValue()+":00":time.intValue()+":00";
	}

	@Override
	public void addRentalSiteSimpleRules(AddRentalSiteRulesAdminCommand cmd) {

		if (null == cmd.getAutoAssign()) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_LOST_PARAMETER,
					"Invalid parameter AutoAssign");
		}

		if (null == cmd.getSiteCounts()) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_LOST_PARAMETER,
					"Invalid parameter siteCounts");
		}

		checkSiteNumber(cmd.getAutoAssign(), cmd.getSiteNumbers(), cmd.getSiteCounts());

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}

//		}

		RentalResource rs = rentalCommonService.getRentalResource(cmd.getResourceType(), cmd.getRentalSiteId());

//		RentalResource rs = this.rentalv2Provider.getRentalSiteById(cmd.getRentalSiteId());

		UpdateDefaultRuleAdminCommand updateRuleCmd = ConvertHelper.convert(cmd, UpdateDefaultRuleAdminCommand.class);
		updateRuleCmd.setSourceType(RuleSourceType.RESOURCE.getCode());
		updateRuleCmd.setSourceId(rs.getId());
		updateRuleCmd.setResourceTypeId(rs.getResourceTypeId());
		updateRuleCmd.setResourceType(rs.getResourceType());

		QueryDefaultRuleAdminCommand queryRuleCmd = new QueryDefaultRuleAdminCommand();
		queryRuleCmd.setSourceType(RuleSourceType.RESOURCE.getCode());
		queryRuleCmd.setSourceId(rs.getId());
		queryRuleCmd.setResourceTypeId(rs.getResourceTypeId());
		queryRuleCmd.setResourceType(rs.getResourceType());

		this.dbProvider.execute((TransactionStatus status) -> {
			//初始化
			//先删除
			rentalv2Provider.deleteRentalResourceNumbersByOwnerId(rs.getResourceType(), EhRentalv2Resources.class.getSimpleName(), rs.getId());
			//set site number
			setRentalRuleSiteNumbers(rs.getResourceType(), EhRentalv2Resources.class.getSimpleName(), rs.getId(), cmd.getSiteNumbers());
			//更新规则
			updateDefaultRule(updateRuleCmd);

			//设置新规则的时候就删除之前的旧单元格
			this.rentalv2Provider.deleteRentalCellsByResourceId(rs.getResourceType(), rs.getId());
			QueryDefaultRuleAdminResponse rule = queryDefaultRule(queryRuleCmd);

			rs.setSiteNumbers(cmd.getSiteNumbers());
			createResourceCells(rule.getPriceRules());
			//先删除后添加, 创建单元格之后在重新添加一次价格，存cellBeginId和cellEndId
			rentalv2PriceRuleProvider.deletePriceRuleByOwnerId(rule.getResourceType(), PriceRuleType.RESOURCE.getCode(), rs.getId());
			createPriceRules(rule.getResourceType(), PriceRuleType.RESOURCE, rs.getId(), rule.getPriceRules());

			return null;
		});
	}

	private List<PriceRuleDTO> buildDefaultPriceRule(List<Byte> rentalTypes) {
		List<PriceRuleDTO> priceRules = new ArrayList<>();
		rentalTypes.forEach(r -> {
			priceRules.add(createInitPriceRuleDTO(r));
		});

		return priceRules;
	}

	private PriceRuleDTO createInitPriceRuleDTO(Byte rentalType) {
		PriceRuleDTO rule = new PriceRuleDTO();
		rule.setRentalType(rentalType);
		rule.setPriceType(RentalPriceType.LINEARITY.getCode());
		rule.setUserPriceType(RentalUserPriceType.UNIFICATION.getCode());
		rule.setInitiatePrice(new BigDecimal(0));
		rule.setWorkdayPrice(new BigDecimal(0));

		//设置类型价格
		rule.setClassifications(new ArrayList<>());
		RentalPriceClassificationDTO classification = new RentalPriceClassificationDTO();
		classification.setWorkdayPrice(new BigDecimal(0));
		classification.setInitiatePrice(new BigDecimal(0));
		classification.setUserPriceType(RentalUserPriceType.USER_TYPE.getCode());
		classification.setClassification(SceneType.ENTERPRISE.getCode());
		rule.getClassifications().add(classification);
		classification = ConvertHelper.convert(classification,RentalPriceClassificationDTO.class);
		classification.setClassification(SceneType.PM_ADMIN.getCode());
		rule.getClassifications().add(classification);
		classification = ConvertHelper.convert(classification,RentalPriceClassificationDTO.class);
		classification.setClassification(SceneType.PARK_TOURIST.getCode());
		rule.getClassifications().add(classification);
		return rule;
	}

	/**
	 * 取某个场所,某段时间的单元格
	 */
	private List<RentalCell> findRentalSiteRuleByDate(String resourceType,Long rentalSiteId,
													  String siteNumber, Timestamp beginTime, Timestamp endTime,
													  List<Byte> ampmList, String rentalDate,Byte rentalType) {
		List<RentalCell> result = new ArrayList<>();

		List<RentalCell> cells = cellList.get();
		List<Long> ids = cells.stream().map(RentalCell::getId).collect(Collectors.toList());

		List<RentalCell> dbCells = this.rentalv2Provider.getRentalCellsByIds(resourceType,ids,rentalSiteId,rentalType);

		for (RentalCell cell : cells) {
			if (null != rentalSiteId && !rentalSiteId.equals(cell.getRentalResourceId()))
				continue;
			if (null != siteNumber && !siteNumber.equals(cell.getResourceNumber()))
				continue;
			if (null != beginTime && null != cell.getBeginTime() && cell.getBeginTime().before(beginTime))
				continue;
			if (null != endTime && null != cell.getEndTime() && cell.getEndTime().after(endTime))
				continue;
			if (null != ampmList && !ampmList.contains(cell.getAmorpm()))
				continue;
			if (null != siteNumber && !siteNumber.equals(cell.getResourceNumber()))
				continue;
			if (null != rentalDate && !rentalDate.equals(dateSF.get().format(cell.getResourceRentalDate())))
				continue;
			//对于单独设置过价格和开放状态的单元格,使用数据库里记录的

			for (RentalCell c : dbCells) {
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
	 */
	private List<RentalCell> findRentalSiteRules(String resourceType,Long rentalSiteId,
												 String ruleDate, Timestamp beginDate, Byte rentalType, DateLength dateLen, Byte status, Byte rentalStartTimeFlag) {
		List<RentalCell> result = new ArrayList<>();

		List<RentalCell> cells = cellList.get();


		for (RentalCell cell : cells) {
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

			if (null != ruleDate) {
				if (null != dateLen) {
					switch (dateLen) {
						case DAY:
							if (!cell.getResourceRentalDate().equals(Date.valueOf(ruleDate)))
								continue;
							break;
						case MONTH:
							Calendar calendar1 = Calendar.getInstance();
							calendar1.setTime(Date.valueOf(ruleDate));
							// month begin
							calendar1.set(Calendar.DAY_OF_MONTH, calendar1.getActualMinimum(Calendar.DAY_OF_MONTH));

							Calendar calendar2 = Calendar.getInstance();
							calendar2.setTime(Date.valueOf(ruleDate));
							// month end
							calendar2.set(Calendar.DAY_OF_MONTH, calendar2.getActualMaximum(Calendar.DAY_OF_MONTH));
							if (cell.getResourceRentalDate().before(calendar1.getTime()) ||
									cell.getResourceRentalDate().after(calendar2.getTime()))
								continue;
							break;
						case WEEK:
							Calendar calendar3 = Calendar.getInstance();
							calendar3.setTime(Date.valueOf(ruleDate));
							// week begin
							calendar3.set(Calendar.DAY_OF_WEEK, calendar3.getActualMinimum(Calendar.DAY_OF_WEEK));

							Calendar calendar4 = Calendar.getInstance();
							calendar4.setTime(Date.valueOf(ruleDate));
							// week end
							calendar4.set(Calendar.DAY_OF_WEEK, calendar4.getActualMaximum(Calendar.DAY_OF_WEEK));
							if (cell.getResourceRentalDate().before(calendar3.getTime()) ||
									cell.getResourceRentalDate().after(calendar4.getTime()))
								continue;
							break;
					}
				}
			}

			result.add(cell);
		}

		//对于单独设置过价格和开放状态的单元格,使用数据库里记录的
        //现在每个单元格的价格单独计算
//		List<Long> ids = result.stream().map(r->r.getId()).collect(Collectors.toList());
//
//		if (!ids.isEmpty()) {
//		    Long minId = ids.stream().min(Long::compareTo).get();
//            Long maxId = ids.stream().max(Long::compareTo).get();
//			List<RentalCell> dbCells = this.rentalv2Provider.getRentalCellsByRange(resourceType,minId,maxId,rentalSiteId,rentalType);
//			for (int i = 0;i<result.size();i++)
//				for (RentalCell c2 : dbCells) {
//					if (c2.getId().equals(result.get(i).getId())) {
//						result.set(i,c2);
//					}
//				}
//		}
		return result;
	}

	/**
	 * 生成某个资源的单元格
	 */
	private void processCells(RentalResource rs, byte rentalType) {
		Long timeCost = System.currentTimeMillis();

		GetResourceRuleAdminCommand getResourceRuleCmd = new GetResourceRuleAdminCommand();
		getResourceRuleCmd.setResourceId(rs.getId());
		getResourceRuleCmd.setResourceType(rs.getResourceType());
		QueryDefaultRuleAdminResponse rule = getResourceRule(getResourceRuleCmd);

		Rentalv2PriceRule priceRule = rentalv2PriceRuleProvider.findRentalv2PriceRuleByOwner(rs.getResourceType(),
				PriceRuleType.RESOURCE.getCode(), rs.getId(), rentalType);

		if (rs.getAutoAssign().equals(NormalFlag.NEED.getCode())) {
			List<RentalResourceNumber> resourceNumbers = this.rentalv2Provider.queryRentalResourceNumbersByOwner(
					rs.getResourceType(), EhRentalv2Resources.class.getSimpleName(), rs.getId());
			if (null != resourceNumbers) {
				rs.setSiteNumbers(new ArrayList<>());
				for (RentalResourceNumber number : resourceNumbers) {
					SiteNumberDTO dto = new SiteNumberDTO();
					dto.setSiteNumber(number.getResourceNumber());
					dto.setSiteNumberGroup(number.getNumberGroup());
					dto.setGroupLockFlag(number.getGroupLockFlag());
					rs.getSiteNumbers().add(dto);
				}
			}
		}

		cellList.set(new ArrayList<>());
		currentId.set(priceRule.getCellBeginId());
		seqNum.set(0L);
		//现在单元格的尽头由最长提前预约时间决定
		rule.setEndDate(System.currentTimeMillis() + rule.getRentalStartTime());
		List<AddRentalSiteSingleSimpleRule> addSingleRules = createAddRuleParams(convert(priceRule), rule, rs);

		for (AddRentalSiteSingleSimpleRule singleCmd : addSingleRules) {
			//在这里统一处理
			addRentalSiteSingleSimpleRule(singleCmd);
		}

		if (LOGGER.isDebugEnabled()) {
			timeCost = System.currentTimeMillis() - timeCost;
			LOGGER.debug("Rentalv2 process cell resourceId = {} rentalType = {} time cost = {} ", rs.getId(), rentalType, timeCost);
		}
	}

	/**
	 * 根据单一时段的规则生成单元格
	 */
	private void addRentalSiteSingleSimpleRule(AddRentalSiteSingleSimpleRule cmd) {
		Long userId = UserContext.currentUserId();
//		if(cmd.getSiteCounts() == null)
//			cmd.setSiteCounts(1.0);
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.setTime(new Date(cmd.getBeginDate()));
		end.setTime(new Date(cmd.getEndDate()));
		if (cmd.getAutoAssign().equals(NormalFlag.NEED.getCode()) &&
				cmd.getSiteCounts().intValue() != cmd.getSiteNumbers().size()) {
			LOGGER.info("Invalid parameter site counts={}, but site numbers size is {}.", cmd.getSiteCounts(),
					cmd.getSiteNumbers().size());
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERRPR_LOST_RESOURCE_RULE,
					"Invalid parameter site counts or site numbers");
		}
		List<Long> closeDates = cmd.getCloseDates();

		// 月的需要单独计算
		if (cmd.getRentalType().equals(RentalType.MONTH.getCode())) {
			// 这里计算月的
			// TODO
			// 初始化开始时间，按每月的第一天算
			Calendar temp = Calendar.getInstance();
			temp.setTime(new Date(cmd.getBeginDate()));
			initToMonthFirstDay(temp);
			if (start.after(temp)) {
				temp.add(Calendar.MONTH, 1);
				start = temp;
			}

			RentalCell rsr = ConvertHelper.convert(cmd, RentalCell.class);
			rsr.setRentalResourceId(cmd.getRentalSiteId());
			rsr.setAutoAssign(cmd.getAutoAssign());
			rsr.setRentalType(cmd.getRentalType());
			rsr.setPriceType(cmd.getPriceType());
			rsr.setCounts(cmd.getSiteCounts());
//			rsr.setRentalStep(1);
//			rsr.setUnit(cmd.getUnit());
			rsr.setPrice(cmd.getWorkdayPrice());
			rsr.setInitiatePrice(cmd.getInitiatePrice());
			rsr.setApprovingUserPrice(cmd.getApprovingUserWorkdayPrice());
			rsr.setApprovingUserInitiatePrice(cmd.getApprovingUserInitiatePrice());
			rsr.setOrgMemberPrice(cmd.getOrgMemberWorkdayPrice());
			rsr.setOrgMemberInitiatePrice(cmd.getOrgMemberInitiatePrice());
			rsr.setStatus(SiteRuleStatus.OPEN.getCode());
			rsr.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			rsr.setCreatorUid(userId);
			while (start.before(end)) {
				rsr.setResourceRentalDate(Date.valueOf(dateSF.get().format(start.getTime())));
				rsr.setBeginTime(new Timestamp(start.getTime().getTime()));
                start.add(Calendar.MONTH, 1);
                rsr.setEndTime(new Timestamp(start.getTime().getTime()));
				createRSR(rsr, cmd);
			}
		} else if (cmd.getRentalType().equals(RentalType.WEEK.getCode())) {
			Calendar temp = Calendar.getInstance();
			temp.setTime(new Date(cmd.getBeginDate()));
			initToWeekFirstDay(temp);
			if (start.after(temp)) {
				temp.add(Calendar.DATE, 7);
				start = temp;
			}

			RentalCell rsr = ConvertHelper.convert(cmd, RentalCell.class);
			rsr.setRentalResourceId(cmd.getRentalSiteId());
			rsr.setAutoAssign(cmd.getAutoAssign());
			rsr.setRentalType(cmd.getRentalType());
			rsr.setPriceType(cmd.getPriceType());
			rsr.setCounts(cmd.getSiteCounts());
//			rsr.setRentalStep(1);
//			rsr.setUnit(cmd.getUnit());
			rsr.setPrice(cmd.getWorkdayPrice());
			rsr.setInitiatePrice(cmd.getInitiatePrice());
			rsr.setApprovingUserPrice(cmd.getApprovingUserWorkdayPrice());
			rsr.setApprovingUserInitiatePrice(cmd.getApprovingUserInitiatePrice());
			rsr.setOrgMemberPrice(cmd.getOrgMemberWorkdayPrice());
			rsr.setOrgMemberInitiatePrice(cmd.getOrgMemberInitiatePrice());
			rsr.setStatus(SiteRuleStatus.OPEN.getCode());
			rsr.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			rsr.setCreatorUid(userId);
			while (start.before(end)) {
				rsr.setResourceRentalDate(Date.valueOf(dateSF.get().format(start.getTime())));
                rsr.setBeginTime(new Timestamp(start.getTime().getTime()));
                start.add(Calendar.DATE, 7);
                rsr.setEndTime(new Timestamp(start.getTime().getTime()));
				createRSR(rsr, cmd);
			}

		} else {
			while (start.before(end)) {
				//获取每周的开放天
				Integer weekday = start.get(Calendar.DAY_OF_WEEK);

//				if (cmd.getOpenWeekday().contains(weekday) &&
				if (null == closeDates || !closeDates.contains(start.getTimeInMillis())) {

					RentalCell rsr = ConvertHelper.convert(cmd, RentalCell.class);
					//单元格通用设置
					rsr.setRentalResourceId(cmd.getRentalSiteId());
					rsr.setAutoAssign(cmd.getAutoAssign());

					rsr.setRentalType(cmd.getRentalType());
					rsr.setPriceType(cmd.getPriceType());
					rsr.setCounts(cmd.getSiteCounts());

					rsr.setPrice(cmd.getWorkdayPrice());
					rsr.setInitiatePrice(cmd.getInitiatePrice());
					rsr.setApprovingUserPrice(cmd.getApprovingUserWorkdayPrice());
					rsr.setApprovingUserInitiatePrice(cmd.getApprovingUserInitiatePrice());
					rsr.setOrgMemberPrice(cmd.getOrgMemberWorkdayPrice());
					rsr.setOrgMemberInitiatePrice(cmd.getOrgMemberInitiatePrice());

					rsr.setStatus(SiteRuleStatus.OPEN.getCode());
					rsr.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
					rsr.setCreatorUid(userId);

					if (cmd.getRentalType().equals(RentalType.HOUR.getCode())) {
						for (double i = cmd.getBeginTime(); i < cmd.getEndTime(); ) {
							rsr.setBeginTime(Timestamp.valueOf(dateSF.get().format(start
									.getTime())
									+ " "
									+ String.valueOf((int) i)
									+ ":"
									+ String.valueOf((int) ((i % 1) * 60))
									+ ":00"));

//								rsr.setRentalStep(1);
							rsr.setTimeStep(cmd.getTimeStep());
							i = i + cmd.getTimeStep();

							rsr.setEndTime(Timestamp.valueOf(dateSF.get().format(start
									.getTime())
									+ " "
									+ String.valueOf((int) i)
									+ ":"
									+ String.valueOf((int) ((i % 1) * 60))
									+ ":00"));

//								rsr.setUnit(cmd.getUnit());
//
//								if(rsr.getUnit()<1){
//									rsr.setHalfresourcePrice(rsr.getPrice().divide(new BigDecimal("2"), 3, RoundingMode.HALF_UP) );
//									rsr.setHalfApprovingUserPrice(rsr.getApprovingUserPrice().divide(new BigDecimal("2"), 3, RoundingMode.HALF_UP));
//									rsr.setHalfOrgMemberPrice(rsr.getOrgMemberPrice().divide(new BigDecimal("2"), 3, RoundingMode.HALF_UP) );
//								}
							rsr.setResourceRentalDate(Date.valueOf(dateSF.get().format(start.getTime())));


							createRSR(rsr, cmd);
						}
					} else if (cmd.getRentalType().equals(RentalType.HALFDAY.getCode()) ||
							cmd.getRentalType().equals(RentalType.THREETIMEADAY.getCode())) {

						// 按半日预定
//						rsr.setUnit(cmd.getUnit());
						rsr.setResourceRentalDate(Date.valueOf(dateSF.get().format(start.getTime())));


						rsr.setAmorpm(AmorpmFlag.AM.getCode());
                        Double v = cmd.getHalfDayTimeIntervals().get(0).getBeginTime() * 3600000;
                        rsr.setBeginTime(new Timestamp(start.getTime().getTime()+v.longValue()));
                        v = cmd.getHalfDayTimeIntervals().get(0).getEndTime() * 3600000;
                        rsr.setEndTime(new Timestamp(start.getTime().getTime()+v.longValue()));
                        createRSR(rsr, cmd);
                        if (cmd.getHalfDayTimeIntervals().size() > 1) {
							rsr.setAmorpm(AmorpmFlag.PM.getCode());
							v = cmd.getHalfDayTimeIntervals().get(1).getBeginTime() * 3600000;
							rsr.setBeginTime(new Timestamp(start.getTime().getTime() + v.longValue()));
							v = cmd.getHalfDayTimeIntervals().get(1).getEndTime() * 3600000;
							rsr.setEndTime(new Timestamp(start.getTime().getTime() + v.longValue()));
							createRSR(rsr, cmd);
						}
						//逻辑有点复杂 必须保证是在3.6.2后更新的资源 才适用于第二条规则
						if (cmd.getRentalType().equals(RentalType.THREETIMEADAY.getCode()) ||
								(cmd.getHalfDayTimeIntervals().size() > 2 && cmd.getHalfDayTimeIntervals().get(2).getName() != null)) {
							rsr.setAmorpm(AmorpmFlag.NIGHT.getCode());
                            v = cmd.getHalfDayTimeIntervals().get(2).getBeginTime() * 3600000;
                            rsr.setBeginTime(new Timestamp(start.getTime().getTime()+v.longValue()));
                            v = cmd.getHalfDayTimeIntervals().get(2).getEndTime() * 3600000;
                            rsr.setEndTime(new Timestamp(start.getTime().getTime()+v.longValue()));
							createRSR(rsr, cmd);
						}

					} else if (cmd.getRentalType().equals(RentalType.DAY.getCode())) {
						// 按日预定
						rsr.setResourceRentalDate(Date.valueOf(dateSF.get().format(start.getTime())));
						rsr.setBeginTime(new Timestamp(start.getTime().getTime()));
						rsr.setEndTime(new Timestamp(start.getTime().getTime() + 24*3600*1000));
						createRSR(rsr, cmd);
					}
				}
				start.add(Calendar.DAY_OF_MONTH, 1);
			}
		}

	}

	private void initToMonthFirstDay(Calendar temp) {
		temp.set(Calendar.DAY_OF_MONTH, 1);
		temp.set(Calendar.HOUR_OF_DAY, 0);
		temp.set(Calendar.MINUTE, 0);
		temp.set(Calendar.SECOND, 0);
		temp.set(Calendar.MILLISECOND, 0);
	}

	private void initToWeekFirstDay(Calendar temp) {
		if (temp.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)//默认周日是一周第一天
			temp.add(Calendar.DATE, -1);
		temp.set(Calendar.DAY_OF_WEEK, 2);
		temp.set(Calendar.HOUR_OF_DAY, 0);
		temp.set(Calendar.MINUTE, 0);
		temp.set(Calendar.SECOND, 0);
		temp.set(Calendar.MILLISECOND, 0);

	}

	private void createRSR(RentalCell rsr, AddRentalSiteSingleSimpleRule cmd) {
		if (cmd.getAutoAssign().equals(NormalFlag.NEED.getCode())) {
			//根据用户填写分配 siteNumber
			for (int num = 0; num < cmd.getSiteCounts(); num++) {
				rsr.setCounts(1.0);
				SiteNumberDTO dto = cmd.getSiteNumbers().get(num);
				rsr.setResourceNumber(dto.getSiteNumber());
				rsr.setNumberGroup(dto.getSiteNumberGroup());
				rsr.setGroupLockFlag(dto.getGroupLockFlag());
				//改成批量插入 2016-8-23 by wuhan
//				rentalProvider.createRentalSiteRule(rsr);
				rsr.setId(currentId.get() + seqNum.get());
				seqNum.set(seqNum.get() + 1);
				cellList.get().add(ConvertHelper.convert(rsr, RentalCell.class));
			}
		} else {

			//改成批量插入 2016-8-23 by wuhan
//			rentalProvider.createRentalSiteRule(rsr);
			rsr.setId(currentId.get() + seqNum.get());
			seqNum.set(seqNum.get() + 1);
			cellList.get().add(ConvertHelper.convert(rsr, RentalCell.class));

		}
	}

	@Override
	public void changeRentalBillPayInfo(ChangeRentalBillPayInfoCommand cmd) {
		RentalOrder order = rentalv2Provider.findRentalBillById(cmd.getId());
		order.setPayTotalMoney(changePayAmount(cmd.getAmount()));
		//删除记录
        //rentalv2AccountProvider.deleteOrderRecordByOrderNo(Long.valueOf(order.getOrderNo()));
        //重新生成订单号 防止支付那边重复
        order.setOrderNo(onlinePayService.createBillId(DateHelper.currentGMTTime().getTime()).toString());
        rentalv2Provider.updateRentalBill(order);
		//payProvider.deleteOrderRecordByOrder(OrderType.OrderTypeEnum.RENTALORDER.getPycode(), Long.valueOf(order.getOrderNo()));
		Map<String, String> map = new HashMap<>();
		map.put("resourceName", order.getResourceName());
		map.put("startTime", order.getUseDetail());
		map.put("amount", order.getPayTotalMoney().toString());
		String notifyTextForOther = localeTemplateService.getLocaleTemplateString(RentalNotificationTemplateCode.SCOPE,
				RentalNotificationTemplateCode.RENTAL_CHANGE_AMOUNT, RentalNotificationTemplateCode.locale, map, "");

		//发送消息
		rentalCommonService.sendMessageToUser(order.getRentalUid(), notifyTextForOther);
	}

	public BigDecimal changePayAmount(Long amount){

		if(amount == null){
			return new BigDecimal(0);
		}
		return  new BigDecimal(amount).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
	}

	@Override
	public void cancelRentalBill(CancelRentalBillCommand cmd) {

		Long timestamp = System.currentTimeMillis();

		if (null == cmd.getRentalBillId()) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_LOST_PARAMETER,
					"Invalid parameter BillId");
		}

		RentalOrder order = this.rentalv2Provider.findRentalBillById(cmd.getRentalBillId());
		if (null == order) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_CANNOT_FIND_ORDER,
					"RentalOrder not found");
		}

		if (order.getStatus().equals(SiteBillStatus.FAIL.getCode())) {
			LOGGER.error("Order has been canceled");
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_ORDER_CANCELED, "Order has been canceled");
		}

		if (order.getStatus().equals(SiteBillStatus.IN_USING.getCode())) {
			//当成功预约之后要判断是否过了取消时间
			LOGGER.error("cancel over time");
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_ORDER_CANCEL_OVERTIME, "cancel bill over time");

		}

		RentalOrderHandler handler = rentalCommonService.getRentalOrderHandler(order.getResourceType());
		dbProvider.execute((TransactionStatus status) -> {
            RentalMessageHandler messageHandler = rentalCommonService.getRentalMessageHandler(order.getResourceType());
			//如果是预约成功，则要判断是否退款，否则将订单置为已取消
			if (order.getStatus().equals(SiteBillStatus.SUCCESS.getCode())) {
				if (null != order.getRefundStrategy() && order.getRefundStrategy() != RentalOrderStrategy.NONE.getCode()
						&& (order.getPayTotalMoney().compareTo(new BigDecimal(0)) > 0)) {

					BigDecimal orderAmount = handler.getRefundAmount(order, timestamp);
					if (orderAmount.compareTo(new BigDecimal(0)) > 0) {
						if (PayMode.ONLINE_PAY.getCode() == (order.getPayMode()) || PayMode.APPROVE_ONLINE_PAY.getCode() == (order.getPayMode())) {
							rentalCommonService.refundOrder(order, timestamp, orderAmount);
							//更新bill状态
							order.setStatus(SiteBillStatus.REFUNDED.getCode());
							order.setRefundAmount(orderAmount);
							messageHandler.cancelOrderNeedRefund(order);
						} else {
							order.setRefundAmount(orderAmount);
							order.setStatus(SiteBillStatus.REFUNDING.getCode());//线下支付人工退款
						}
					} else {//退款金额过小
						if (PayMode.ONLINE_PAY.getCode() == (order.getPayMode()) || PayMode.APPROVE_ONLINE_PAY.getCode() == (order.getPayMode()))
							rentalCommonService.refundOrder(order, timestamp, orderAmount);//可能有卡券
						order.setStatus(SiteBillStatus.FAIL.getCode());
						messageHandler.cancelOrderWithoutRefund(order);
					}
				} else {
					//如果不需要退款，直接状态为已取消
					order.setStatus(SiteBillStatus.FAIL.getCode());
					if (order.getPaidMoney().compareTo(new BigDecimal(0)) > 0)
						messageHandler.cancelOrderWithoutRefund(order);
					else
						messageHandler.cancelOrderWithoutPaySendMessage(order);
				}
			} else if (order.getStatus().equals(SiteBillStatus.PAYINGFINAL.getCode()) ||
					order.getStatus().equals(SiteBillStatus.APPROVING.getCode())) {
				//如果不需要退款，直接状态为已取消
				order.setStatus(SiteBillStatus.FAIL.getCode());
                messageHandler.cancelOrderWithoutPaySendMessage(order);

			} else {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
						ErrorCodes.ERROR_INVALID_PARAMETER, "Can not cancel order");
			}

			rentalv2Provider.updateRentalBill(order);

			handler.releaseOrderResourceStatus(order);

			if (!StringUtils.isEmpty(order.getDoorAuthId())) {//解除门禁授权
				String[] ids = order.getDoorAuthId().split(",");
				for (String id : ids)
					doorAccessService.deleteDoorAuth(Long.parseLong(id));
			}
			rentalv2Provider.setAuthDoorId(order.getId(), null);
			//用户积分
			LocalEventBus.publish(event -> {
				LocalEventContext context = new LocalEventContext();
				context.setUid(order.getRentalUid());
				context.setNamespaceId(order.getNamespaceId());
				context.setCommunityId(order.getCommunityId());
				event.setContext(context);
				event.setEntityType(EhRentalv2Orders.class.getSimpleName());
				event.setEntityId(order.getId());
				event.setEventName(SystemEvent.RENTAL_RESOURCE_APPLY_CANCEL.dft());
			});
			//删除统计信息
			rentalv2Provider.deleteRentalOrderStatisticsByOrderId(order.getId());
			return null;
		});
        onOrderCancel(order,null);
		//发消息
		RentalMessageHandler messageHandler = rentalCommonService.getRentalMessageHandler(order.getResourceType());
		messageHandler.cancelOrderSendMessage(order);
	}

	private long generateRandomNumber(int n) {
		return (long) ((Math.random() * 9 + 1) * Math.pow(10, n - 1));
	}

	/**
	 * 取消订单发推送
	 */
	@Override
	public void cancelOrderSendMessage(RentalOrder rentalBill) {
		RentalResource rs = this.rentalv2Provider.getRentalSiteById(rentalBill.getRentalResourceId());
		if (null == rs)
			return;
		//给负责人推送
		StringBuilder managerContent = new StringBuilder();
		User user = this.userProvider.findUserById(rentalBill.getRentalUid());
		if (null == user)
			return;
		managerContent.append(user.getNickName());
		managerContent.append("取消了");
		managerContent.append(rentalBill.getResourceName());
		managerContent.append("\n使用详情：");
		managerContent.append(rentalBill.getUseDetail());
		if (null != rentalBill.getRentalCount()) {
			managerContent.append("\n预约数：");
			managerContent.append(rentalBill.getRentalCount());
		}
		//sendMessageToUser(rs.getChargeUid(), managerContent.toString());
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

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}

		if (cmd.getSourceType().equals(RuleSourceType.DEFAULT.getCode())) {
			RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(cmd.getOwnerType(), cmd.getOwnerId(),
					cmd.getResourceType(), cmd.getResourceTypeId(), cmd.getSourceType(), cmd.getSourceId());
			if (rule == null)
				throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERRPR_LOST_RESOURCE_RULE,
						"cannot find default rule");
			cmd.setSourceId(rule.getId());
		}

		GetItemListCommandResponse response = new GetItemListCommandResponse();
		response.setSiteItems(new ArrayList<>());
		List<RentalItem> rsiSiteItems = rentalv2Provider
				.findRentalSiteItems(cmd.getSourceType(), cmd.getSourceId(), cmd.getResourceType());
		for (RentalItem rsi : rsiSiteItems) {
			SiteItemDTO dto = convertItem2DTO(rsi);
			response.getSiteItems().add(dto);
		}
		return response;
	}

	@Override
	public AddRentalBillItemV2Response addRentalItemBillV2(AddRentalBillItemCommand cmd) {
		AddRentalBillItemCommandResponse response = actualAddRentalItemBill(cmd, ActivityRosterPayVersionFlag.V2);
		return (AddRentalBillItemV2Response)createPayOrder(response,cmd,ActivityRosterPayVersionFlag.V2);
	}

	@Override
	public AddRentalBillItemV3Response addRentalItemBillV3(AddRentalBillItemCommand cmd) {
		AddRentalBillItemCommandResponse response = actualAddRentalItemBill(cmd, ActivityRosterPayVersionFlag.V3);
		return (AddRentalBillItemV3Response)createPayOrder(response,cmd,ActivityRosterPayVersionFlag.V3);
	}

	private PreOrderCommand convertOrderDTOForV2(RentalOrder order, String clientAppName, Integer paymentType) {
		PreOrderCommand preOrderCommand = new PreOrderCommand();

		preOrderCommand.setOrderType(OrderType.OrderTypeEnum.RENTALORDER.getPycode());
		preOrderCommand.setOrderId(Long.valueOf(order.getOrderNo()));
		preOrderCommand.setAmount(order.getPayTotalMoney());

		preOrderCommand.setPayerId(order.getRentalUid());
		preOrderCommand.setNamespaceId(UserContext.getCurrentNamespaceId());
		preOrderCommand.setClientAppName(clientAppName);
		preOrderCommand.setExtendInfo(String.format("项目名称:%s,资源名称:%s", communityProvider.findCommunityById(order.getCommunityId()).getName()
												,order.getResourceName()));

		//微信公众号支付，重新设置ClientName，设置支付方式和参数
		if (paymentType != null && paymentType == PaymentType.WECHAT_JS_PAY.getCode()) {


			preOrderCommand.setPaymentType(PaymentType.WECHAT_JS_ORG_PAY.getCode());
			PaymentParamsDTO paymentParamsDTO = new PaymentParamsDTO();
			paymentParamsDTO.setPayType("no_credit");
			User user = UserContext.current().getUser();
			paymentParamsDTO.setAcct(user.getNamespaceUserToken());
			//TODO: 临时给越空间解决公众号支付
			String vspCusid = configurationProvider.getValue(UserContext.getCurrentNamespaceId(), "tempVspCusid", "");
			paymentParamsDTO.setVspCusid(vspCusid);
			preOrderCommand.setPaymentParams(paymentParamsDTO);
			preOrderCommand.setCommitFlag(1);
		}

		return preOrderCommand;
	}


	@Override
	public AddRentalBillItemCommandResponse addRentalItemBill(AddRentalBillItemCommand cmd) {
		AddRentalBillItemCommandResponse response = actualAddRentalItemBill(cmd, ActivityRosterPayVersionFlag.V1);
		return response ;
	}

	private Object createPayOrder(AddRentalBillItemCommandResponse responseV1,AddRentalBillItemCommand cmd,
                                  ActivityRosterPayVersionFlag version){
		RentalOrder bill = rentalv2Provider.findRentalBillById(cmd.getRentalBillId());
        PreOrderCommand preOrderCommand = convertOrderDTOForV2(bill, cmd.getClientAppName(), cmd.getPaymentType());
        Object obj = null;
        if (ActivityRosterPayVersionFlag.V2 == version) {
            PreOrderDTO callBack = null;
            if (preOrderCommand.getAmount().compareTo(new BigDecimal(0)) > 0
                    && bill.getPayMode().equals(PayMode.ONLINE_PAY.getCode())) //只有线上支付在这个时候下单
                callBack = rentalv2PayService.createPreOrder(preOrderCommand, bill);

            AddRentalBillItemV2Response response = new AddRentalBillItemV2Response();
            response.setPreOrderDTO(callBack);
            response.setFlowCaseUrl(responseV1.getFlowCaseUrl());
            response.setBillId(bill.getId());
            obj = response;
        }else if (ActivityRosterPayVersionFlag.V3 == version) {
            AddRentalBillItemV3Response response = new AddRentalBillItemV3Response();
            if (preOrderCommand.getAmount().compareTo(new BigDecimal(0)) > 0
                    && bill.getPayMode().equals(PayMode.ONLINE_PAY.getCode())) //只有线上支付在这个时候下单
                response = rentalv2PayService.createMerchantPreOrder(preOrderCommand,bill);
            response.setFlowCaseUrl(responseV1.getFlowCaseUrl());
            response.setBillId(bill.getId());
            obj = response;
        }

		//保存支付订单信息
		Rentalv2OrderRecord record = this.rentalv2AccountProvider.getOrderRecordByOrderNo(Long.valueOf(bill.getOrderNo()));
		if (record != null){
			record.setOrderId(bill.getId());
			record.setStatus((byte)0);//未支付
			record.setNamespaceId(UserContext.getCurrentNamespaceId());
			record.setPaymentOrderType(OrderRecordType.NORMAL.getCode());//支付订单
			this.rentalv2AccountProvider.updateOrderRecord(record);
		}
		return obj;
	}

	private AddRentalBillItemCommandResponse actualAddRentalItemBill(AddRentalBillItemCommand cmd, ActivityRosterPayVersionFlag version) {
		RentalOrder bill = rentalv2Provider.findRentalBillById(cmd.getRentalBillId());

		if (!bill.getStatus().equals(SiteBillStatus.INACTIVE.getCode()) &&
				!bill.getStatus().equals(SiteBillStatus.PAYINGFINAL.getCode())) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_CANNOT_FIND_ORDER,
					"Invalid parameter");
		}

		RentalResource rs = rentalCommonService.getRentalResource(bill.getResourceType(), bill.getRentalResourceId());

		RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(null, null,
				rs.getResourceType(), rs.getResourceTypeId(), RuleSourceType.RESOURCE.getCode(), rs.getId());

		processCells(rs, bill.getRentalType());

		//线上支付模式订单取消标志
		boolean[] orderCancelFlag = {false};

		AddRentalBillItemCommandResponse response = new AddRentalBillItemCommandResponse();

		//增加审批后线上支付模式的判断 审批模式，订单状态设置成待审批 add by sw 20170506
		//现在审批线上模式，线下订单模式都跟踪状态，金额为0时，直接预约成功，否则设置成待付款
		//线上模式只有成功之后才走工作流
		//线下模式和审批线上模式 都走工作流
		//用基于服务器平台的锁 验证线下支付 的剩余资源是否足够
		//线下模式和审批线上模式 都走工作流

		this.dbProvider.execute((TransactionStatus status) -> {
			//现验证物品
			createOrderItems(cmd.getRentalItems(), bill);
			//set业务信息
			bill.setCustomObject(cmd.getCustomObject());
			if (cmd.getUserEnterpriseId() != null)
				bill.setUserEnterpriseId(cmd.getUserEnterpriseId());
			bill.setUserEnterpriseName(cmd.getUserEnterpriseName());
			bill.setUserName(cmd.getUserName());
			bill.setUserPhone(cmd.getUserPhone());
			bill.setAddressId(cmd.getAddressId());
			bill.setPaidVersion(version.getCode());
			//
			if (bill.getPayMode().equals(PayMode.ONLINE_PAY.getCode()) || bill.getSource().equals(RentalBillSource.BACK_GROUND.getCode())) {

				int compare = bill.getPayTotalMoney().compareTo(BigDecimal.ZERO);
				if (compare == 0 || bill.getSource().equals((byte)2)) {
					// 总金额为0 或者后台录入，直接预订成功状态
					bill.setStatus(SiteBillStatus.SUCCESS.getCode());
					bill.setPaidMoney(bill.getPayTotalMoney());
				} else {
					bill.setStatus(SiteBillStatus.PAYINGFINAL.getCode());
					orderCancelFlag[0] = true;
				}

				//发消息给管理员
				RentalOrderHandler handler = rentalCommonService.getRentalOrderHandler(bill.getResourceType());

				handler.updateOrderResourceInfo(bill);

				rentalv2Provider.updateRentalBill(bill);

				if (bill.getStatus().equals(SiteBillStatus.SUCCESS.getCode())) {
					onOrderSuccess(bill);
				}
			}else{
				bill.setStatus(SiteBillStatus.APPROVING.getCode());

				//验证订单下的资源是否足够
				coordinationProvider.getNamedLock(CoordinationLocks.CREATE_RENTAL_BILL.getCode() + bill.getResourceType() +
						bill.getRentalResourceId())
						.enter(() -> {
							List<RentalBillRuleDTO> rules = getBillRules(bill);
							this.validateRentalBill(rules, rs, rule,bill.getRentalType());
							return null;
						});

				//线下支付要建立工作流
				FlowCase flowCase = this.createFlowCase(bill);

				if (null != flowCase) {
					String url = processFlowURL(flowCase.getId(), FlowUserType.APPLIER.getCode(), flowCase.getModuleId());
					response.setFlowCaseUrl(url);
					bill.setFlowCaseId(flowCase.getId());
				} else {
					LOGGER.error("Enable rental flow not found, moduleId={}", FlowConstants.PM_TASK_MODULE);
					throw RuntimeErrorException.errorWith("Rentalv2",
							10001, "请开启工作流后重试");

				}

				rentalv2Provider.updateRentalBill(bill);
			}
			//save Attachments
			createOrderAttachments(bill, cmd.getRentalAttachments());
			//save files
			createOrderFileUris(cmd.getFileUris(),bill);
			return null;
		});

		response.setName(OrderType.OrderTypeEnum.RENTALORDER.getMsg());
		response.setDescription(OrderType.OrderTypeEnum.RENTALORDER.getMsg());
		response.setOrderType(OrderType.OrderTypeEnum.RENTALORDER.getPycode());
		response.setOrderNo(bill.getOrderNo());
		response.setAmount(bill.getPayTotalMoney());
		response.setBillId(bill.getId());
		// 客户端生成订单
		if (ActivityRosterPayVersionFlag.V1 == version) {
			this.setSignatureParam(response);
		}
		//创建定时任务
		if (orderCancelFlag[0]) {
			createOrderOverTimeTask(bill);
		}
		return response;
	}

	private List<RentalBillRuleDTO> getBillRules(RentalOrder bill) {
		List<RentalBillRuleDTO> rules = new ArrayList<>();

		List<RentalResourceOrder> rsbs = rentalv2Provider.findRentalResourceOrderByOrderId(bill.getId());
		for (RentalResourceOrder rsb : rsbs) {
			RentalBillRuleDTO dto = new RentalBillRuleDTO();
			dto.setRentalCount(rsb.getRentalCount());
			dto.setRuleId(rsb.getRentalResourceRuleId());
			rules.add(dto);
		}
		return rules;
	}

	private void createOrderOverTimeTask(RentalOrder bill) {
		// n分钟后，取消未成功的订单
//		final Job job1 = new Job(CancelUnsuccessRentalOrderAction.class.getName(), String.valueOf(bill.getId()));
//
//		jesqueClientFactory.getClientPool().delayedEnqueue(queueName, job1,
//				bill.getReserveTime().getTime() + ORDER_AUTO_CANCEL_TIME);
		Map<String, Object> messageMap = new HashMap<>();
		messageMap.put("orderId", bill.getId());
		scheduleProvider.scheduleSimpleJob(
				queueName,
				"cancelBill" + bill.getId(),
				new java.util.Date(bill.getReserveTime().getTime() + ORDER_AUTO_CANCEL_TIME),
				RentalCancelOrderJob.class,
				messageMap
		);
	}

	private void createOrderItems(List<SiteItemDTO> rentalItems, RentalOrder bill) {
		//2016-6-2 10:32:44 fix bug :当有物品订单（说明是付款失败再次付款），就不再生成物品订单

		List<RentalItemsOrder> items = this.rentalv2Provider.findRentalItemsBillBySiteBillId(bill.getId(), bill.getResourceType());

		if (null == items && null != rentalItems) {

//			Tuple<Boolean, Boolean> tuple = this.coordinationProvider.getNamedLock(CoordinationLocks.CREATE_RENTAL_BILL.getCode()
//					+ bill.getRentalResourceId())
//					.enter(() -> {
			BigDecimal itemMoney = new BigDecimal(0);
			for (SiteItemDTO siDto : rentalItems) {

				if (siDto.getId() == null) {
					throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_INVALID_PARAMETER,
							"Invalid parameter of siDto id" + siDto + ".");
				}
				RentalItem rSiteItem = this.rentalv2Provider.getRentalSiteItemById(siDto.getId());
				if (null == rSiteItem)
					throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_INVALID_PARAMETER,
							"Invalid parameter of siDto id" + siDto + ".");

				if (!rSiteItem.getSourceId().equals(bill.getRentalResourceId()))
					throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_INVALID_PARAMETER,
							"Invalid parameter item id is not this site");

				RentalItemsOrder rib = new RentalItemsOrder();
				rib.setTotalMoney(rSiteItem.getPrice().multiply(new BigDecimal(siDto.getCounts())));
				rib.setRentalResourceItemId(siDto.getId());
				rib.setRentalCount(siDto.getCounts());
				rib.setItemName(rSiteItem.getName());
				rib.setRentalOrderId(bill.getId());
				rib.setResourceType(bill.getResourceType());

				itemMoney = itemMoney.add(rib.getTotalMoney());
				//用基于服务器平台的锁添加订单（包括验证和添加）
				//先验证后添加，由于锁机制，可以保证同时只有一个线程验证和添加
				//付费商品没有库存管理了
//							if(this.validateItem(rib))
//								return true;
				rentalv2Provider.createRentalItemBill(rib);
			}

			if (itemMoney.doubleValue() > 0) {
				bill.setPayTotalMoney(bill.getResourceTotalMoney().add(itemMoney));
			}
//						return false;
//					});
//			Boolean validateBoolean = tuple.first();
//			if(validateBoolean) {
//				throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
//						RentalServiceErrorCode.ERROR_NO_ENOUGH_ITEMS,"no enough items");
//			}
		}
	}

	private void createOrderAttachments(RentalOrder bill, List<AttachmentDTO> rentalAttachments) {
		//保证没有attachments,才会去存
		List<RentalOrderAttachment> attachments = rentalv2Provider.findRentalBillAttachmentByBillId(bill.getId());
		if ((attachments == null || attachments.isEmpty()) && null != rentalAttachments) {
			for (AttachmentDTO attachment : rentalAttachments) {
				RentalOrderAttachment rba = new RentalOrderAttachment();
				rba.setRentalOrderId(bill.getId());
				rba.setResourceType(bill.getResourceType());
				rba.setAttachmentType(attachment.getAttachmentType());
				rba.setContent(attachment.getContent());
				this.rentalv2Provider.createRentalBillAttachment(rba);

				if (AttachmentType.RECOMMEND_USER.getCode().equals(attachment.getAttachmentType())) {
					List<RentalConfigAttachment> tempAttachments = rentalv2Provider
							.queryRentalConfigAttachmentByIds(attachment.getRecommendUsers());
					List<RentalRecommendUser> recommendUsers = tempAttachments.stream()
							.map(r -> ConvertHelper.convert(r, RentalRecommendUser.class)).collect(Collectors.toList());
					addRecommendUsers(recommendUsers, AttachmentType.ORDER_RECOMMEND_USER.name(), bill.getId(), bill.getResourceType());

				} else if (attachment.getAttachmentType().equals(AttachmentType.GOOD_ITEM.getCode())) {
					List<RentalConfigAttachment> tempAttachments = rentalv2Provider
							.queryRentalConfigAttachmentByIds(attachment.getGoodItems());
					List<RentalGoodItem> goodItems = tempAttachments.stream()
							.map(r -> ConvertHelper.convert(r, RentalGoodItem.class)).collect(Collectors.toList());
					addGoodItems(goodItems, AttachmentType.ORDER_GOOD_ITEM.name(), bill.getId(), bill.getResourceType());
				}
			}
		}
	}

	@Override
	public void onOrderCancel(RentalOrder order,String content) {
        //终止工作流
		FlowCase flowCase = flowCaseProvider.findFlowCaseByReferId(order.getId(), REFER_TYPE, moduleId);
        if(null != flowCase  && !flowCase.getCaseType().equals(FlowCaseType.DUMB.getCode())){
            FlowAutoStepDTO dto = new FlowAutoStepDTO();
            dto.setAutoStepType(FlowStepType.ABSORT_STEP.getCode());
            dto.setFlowCaseId(flowCase.getId());
            dto.setFlowMainId(flowCase.getFlowMainId());
            dto.setFlowNodeId(flowCase.getCurrentNodeId());
            dto.setFlowVersion(flowCase.getFlowVersion());
            dto.setStepCount(flowCase.getStepCount());
            if (content != null){
				FlowEventLog log = new FlowEventLog();
				log.setId(flowEventLogProvider.getNextId());
				log.setFlowMainId(flowCase.getFlowMainId());
				log.setFlowVersion(flowCase.getFlowVersion());
				log.setNamespaceId(flowCase.getNamespaceId());
				log.setFlowNodeId(flowCase.getCurrentNodeId());
				log.setFlowCaseId(flowCase.getId());
				log.setStepCount(flowCase.getStepCount());
				log.setSubjectId(0L);
				log.setParentId(0L);
				User user = userProvider.findUserById(User.SYSTEM_UID);
				log.setFlowUserId(user.getId());
				log.setFlowUserName(user.getNickName());
				log.setLogType(FlowLogType.NODE_TRACKER.getCode());
				log.setButtonFiredStep(FlowStepType.ABSORT_STEP.getCode());
				log.setTrackerApplier(1L); // 申请人可以看到此条log，为0则看不到
				log.setTrackerProcessor(1L);// 处理人可以看到此条log，为0则看不到
				log.setLogContent(content);
				List<FlowEventLog> logList = new ArrayList<>(1);
				logList.add(log);
				dto.setEventLogs(logList);
			}
            try {
                this.flowService.processAutoStep(dto);
            }catch (Exception e){

            }
        }

	}
	@Override
	public void onOrderSuccess(RentalOrder order) {

		if (order.getPayMode() == PayMode.ONLINE_PAY.getCode() && order.getResourceType().equals(RentalV2ResourceType.DEFAULT.getCode())) {
			//加工作流
			FlowCase flowCase = createFlowCase(order);
			if (null != flowCase) {
				order.setFlowCaseId(flowCase.getId());
				rentalv2Provider.updateRentalBill(order);
			}
		}

        //预约成功 授权门禁
        RentalResource rentalResource = rentalCommonService.getRentalResource(order.getResourceType(), order.getRentalResourceId());

        if (!StringUtils.isEmpty(rentalResource.getAclinkId())) {
            String[] ids = rentalResource.getAclinkId().split(",");
            if (ids.length > 0) {
                String doorAuthId = "";
                for (String id : ids)
                    doorAuthId += createDoorAuth(order.getRentalUid(), order.getAuthStartTime().getTime(), order.getAuthEndTime().getTime(),
                            Long.parseLong(id), rentalResource.getCreatorUid()) + ",";
                order.setDoorAuthId(doorAuthId.substring(0, doorAuthId.length() - 1));
                rentalv2Provider.updateRentalBill(order);
            }
        }

		//发消息给管理员
		RentalMessageHandler handler = rentalCommonService.getRentalMessageHandler(order.getResourceType());
		handler.addOrderSendMessage(order);
		//发短信 推送给用户
		handler.sendRentalSuccessSms(order);

		//用户积分
		LocalEventBus.publish(event -> {
			LocalEventContext context = new LocalEventContext();
			context.setUid(order.getRentalUid());
			context.setNamespaceId(order.getNamespaceId());
			context.setCommunityId(order.getCommunityId());
			event.setContext(context);
			event.setEntityType(EhRentalv2Orders.class.getSimpleName());
			event.setEntityId(order.getId());
			event.setEventName(SystemEvent.RENTAL_RESOURCE_APPLY.dft());
		});
		//创建订单统计数据
		createOrderStatistics(order);

	}

	private void createOrderStatistics(RentalOrder order) {
		RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(null, null,
				order.getResourceType(), order.getResourceTypeId(), RuleSourceType.RESOURCE.getCode(), order.getRentalResourceId());

		if (order.getRentalType().equals(RentalType.HOUR.getCode()) || order.getRentalType().equals(RentalType.HALFDAY.getCode())
				|| order.getRentalType().equals(RentalType.THREETIMEADAY.getCode())) {
			RentalOrderStatistics statistics = ConvertHelper.convert(order, RentalOrderStatistics.class);
			statistics.setOrderId(order.getId());
			statistics.setValidTimeLong(order.getEndTime().getTime() - order.getStartTime().getTime());
			rentalv2Provider.createRentalOrderStatistics(statistics);
		} else {
			//计算有效时长 去掉关闭的天
			List<RentalCloseDate> closeDates = rentalv2Provider.queryRentalCloseDateByOwner(order.getResourceType(),
					EhRentalv2Resources.class.getSimpleName(), order.getRentalResourceId(),null,null);
			Set<Long> closeTime = closeDates == null ? new HashSet<>() : closeDates.stream().map(r -> r.getCloseDate().getTime()).collect(Collectors.toSet());
			LocalDateTime startDate = new java.util.Date(order.getStartTime().getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			startDate = LocalDateTime.of(startDate.toLocalDate(), LocalTime.MIN);
			LocalDateTime endDate = new java.util.Date(order.getEndTime().getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			while (!startDate.isAfter(endDate)) {
				Long time = startDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
				if (!closeTime.contains(time)) {
					RentalOrderStatistics statistics = ConvertHelper.convert(order, RentalOrderStatistics.class);
					statistics.setOrderId(order.getId());
					List<RentalDayopenTime> dayopenTimes = rentalv2Provider.queryRentalDayopenTimeByOwner(order.getResourceType(),
							EhRentalv2Resources.class.getSimpleName(), order.getRentalResourceId(), order.getRentalType());
					if (dayopenTimes != null && dayopenTimes.size() > 0) {
						RentalDayopenTime t = dayopenTimes.get(0);
						statistics.setValidTimeLong((long) (t.getCloseTime() - t.getOpenTime()) * 3600 * 1000);
					} else
						statistics.setValidTimeLong((long) ( rule.getDayCloseTime()-rule.getDayOpenTime()) * 3600 * 1000);
					statistics.setRentalDate(new Date(time));
					rentalv2Provider.createRentalOrderStatistics(statistics);
				}
				startDate = startDate.plusDays(1);
			}

		}
	}


	private FlowCase createFlowCase(RentalOrder order) {
		String moduleType = FlowModuleType.NO_MODULE.getCode();
		Long ownerId = order.getResourceTypeId();
		String ownerType = FlowOwnerType.RENTALRESOURCETYPE.getCode();
		Flow flow = flowService.getEnabledFlow(order.getNamespaceId(), EntityType.COMMUNITY.getCode(), order.getCommunityId(),
				Rentalv2Controller.moduleId, moduleType, ownerId, ownerType);
		LOGGER.debug("param : " + order.getNamespaceId() + "*" + order.getCommunityId() + "*" + moduleType + "*" + ownerId + "*" + ownerType);
		LOGGER.debug("\n flow is " + flow);

		CreateFlowCaseCommand cmd = new CreateFlowCaseCommand();
		cmd.setApplyUserId(order.getRentalUid());
		cmd.setReferId(order.getId());
		cmd.setReferType(REFER_TYPE);
		cmd.setProjectId(order.getCommunityId());
		cmd.setProjectType(EntityType.COMMUNITY.getCode());


		Map<String, String> map = new HashMap<>();
		map.put("resourceName", order.getResourceName());
		String useDetail = order.getUseDetail();
		if (useDetail.contains("\n")) {
			String[] splitUseDetail = useDetail.split("\n");
			useDetail = splitUseDetail[0] + "...";
		}
		map.put("useDetail", useDetail);
		String contentString = localeTemplateService.getLocaleTemplateString(RentalNotificationTemplateCode.FLOW_SCOPE,
				RentalNotificationTemplateCode.RENTAL_FLOW_CONTENT, RentalNotificationTemplateCode.locale, map, "");
		RentalResourceType resourceType = rentalv2Provider.getRentalResourceTypeById(order.getResourceTypeId());
		cmd.setTitle(resourceType.getName());
		cmd.setServiceType(resourceType.getName());
		cmd.setContent(contentString);
//	    	LOGGER.debug("cmd = \n"+cmd);
		if (null != flow) {
			cmd.setFlowMainId(flow.getFlowMainId());
			cmd.setFlowVersion(flow.getFlowVersion());
			return flowService.createFlowCase(cmd);
		}
		return null;
	}


	private Long createDoorAuth(Long userId, Long timeBegin, Long timeEnd, Long doorId, Long authUserId) {
		CreateDoorAuthCommand cmd = new CreateDoorAuthCommand();
		cmd.setAuthType((byte) 1);//临时授权
		cmd.setApproveUserId(authUserId);
		cmd.setDoorId(doorId);
		cmd.setUserId(userId);
		cmd.setValidFromMs(timeBegin);
		cmd.setValidEndMs(timeEnd);

		DoorAuthDTO dto = doorAccessService.createDoorAuth(cmd);
		return dto.getId();
	}

//	private boolean validateItem(RentalItemsOrder rib) {
//
//		RentalItem rSiteItem = this.rentalv2Provider.getRentalSiteItemById(rib.getRentalResourceItemId());
//		List<RentalResourceOrder> rentalSitesBills = this.rentalv2Provider.findRentalResourceOrderByOrderId(rib.getRentalOrderId());
//		if (rSiteItem.getItemType().equals(RentalItemType.SALE.getCode())) {
//			Integer soldSum = this.rentalv2Provider.countRentalSiteItemSoldCount(rSiteItem.getId());
//			//如果订单的商品总数加此次订单的数量超过了商品的总数
//			if (rSiteItem.getCounts() < soldSum + rib.getRentalCount())
//				return true;
//		} else if (rSiteItem.getItemType().equals(RentalItemType.RENTAL.getCode())) {
//			//如果这个租用的 循环每一个单元格
//			for (RentalResourceOrder rentalSitesBill : rentalSitesBills) {
//				//由于商品订单不和单元格关联，所以要找到该单元格的所有订单
//				List<Long> rentalBillIds = this.findRentalBillIdsByRuleId(rentalSitesBill.getRentalResourceRuleId(),
//						rentalSitesBill.getResourceType());
//				//查该单元格所有订单的预定商品总数
//				Integer rentalSum = this.rentalv2Provider.countRentalSiteItemRentalCount(rentalBillIds);
//				// 在单元格下租用的所有物品总数+此次订单租赁数，超过商品总数 则报异常
//				if (rSiteItem.getCounts() < rentalSum + rib.getRentalCount())
//					return true;
//			}
//		}
//		return false;
//	}

//	private List<Long> findRentalBillIdsByRuleId(Long ruleId, String resourceType) {
//		List<Long> result = new ArrayList<>();
//		List<RentalResourceOrder> rentalSitesBills = this.rentalv2Provider.findRentalSiteBillBySiteRuleId(ruleId, resourceType);
//		for (RentalResourceOrder sitesBill : rentalSitesBills) {
//			result.add(sitesBill.getRentalOrderId());
//		}
//		return result;
//
//	}


	private void setSignatureParam(AddRentalBillItemCommandResponse response) {
		String appKey = configurationProvider.getValue(UserContext.getCurrentNamespaceId(), "pay.appKey", "");
		Long timestamp = System.currentTimeMillis();
		Integer randomNum = (int) (Math.random() * 1000);
		App app = appProvider.findAppByKey(appKey);
		if (app == null) {
			LOGGER.error("app not found.key=" + appKey);
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
		if (cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4040040420L, cmd.getAppId(), null, cmd.getCurrentProjectId());//订单记录权限
		}
		ListRentalBillsCommandResponse response = new ListRentalBillsCommandResponse();
		if (cmd.getPageAnchor() == null)
			cmd.setPageAnchor(Long.MAX_VALUE);
		Integer pageSize = PaginationConfigHelper.getPageSize(
				configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());

		List<RentalOrder> bills = rentalv2Provider.listRentalBills(cmd.getResourceTypeId(), cmd.getOrganizationId(), cmd.getCommunityId(),
				cmd.getRentalSiteId(), locator, cmd.getBillStatus(), cmd.getVendorType(), pageSize+1, cmd.getStartTime(), cmd.getEndTime(),
				null, null,cmd.getPayChannel(),cmd.getSource());

		if (bills == null) {
			return response;
		}

		if (bills.size() > pageSize) {
			bills.remove(bills.size() - 1);
			response.setNextPageAnchor(bills.get(bills.size() - 1).getReserveTime().getTime());
		}

		if (cmd.getBillStatus() != null)
			checkRentalBills(bills,true);
		else
			checkRentalBills(bills,false);
		response.setRentalBills(new ArrayList<>());
		for (RentalOrder bill : bills) {
			RentalBillDTO dto = processOrderDTO(bill);
			if (dto.getStatus().equals(SiteBillStatus.FAIL.getCode()) && dto.getPaidPrice()!=null && dto.getPaidPrice().compareTo(new BigDecimal(0))>0)
				dto.setStatus(SiteBillStatus.FAIL_PAID.getCode());
			response.getRentalBills().add(dto);
		}
 
		return response;
	}

	@Override
	public ListRentalBillsCommandResponse listRentalBillsByOrdId(ListRentalBillsByOrdIdCommand cmd) {
		if (cmd.getOrganizationId() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"organizationId not found.");
		}
		ListRentalBillsCommandResponse response = new ListRentalBillsCommandResponse();
		if (cmd.getPageAnchor() == null)
			cmd.setPageAnchor(Long.MAX_VALUE);
		Integer pageSize = PaginationConfigHelper.getPageSize(
				configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<RentalOrder> bills = rentalv2Provider.listRentalBillsByUserOrgId(cmd.getOrganizationId(), locator, pageSize + 1);
		if (bills == null) {
			return response;
		}
		if (bills.size() > pageSize) {
			bills.remove(bills.size() - 1);
			response.setNextPageAnchor(bills.get(bills.size() - 1).getReserveTime().getTime());
		}
		response.setRentalBills(new ArrayList<>());
		Map<Long, Long> tagAppidMap = new HashMap<>();
		for (RentalOrder bill : bills) {
			RentalBillDTO dto = processOrderDTO(bill);
			if (tagAppidMap.get(bill.getResourceTypeId()) == null) {
				ListServiceModuleAppsCommand cmd2 = new ListServiceModuleAppsCommand();
				cmd2.setNamespaceId(bill.getNamespaceId());
				cmd2.setCustomTag(bill.getResourceTypeId().toString());
				cmd2.setModuleId(Rentalv2Controller.moduleId);
				ListServiceModuleAppsResponse rsp = portalService.listServiceModuleAppsWithConditon(cmd2);
				if (rsp!=null && rsp.getServiceModuleApps()!=null) {
					dto.setAppId(rsp.getServiceModuleApps().get(0).getOriginId());
					tagAppidMap.put(bill.getResourceTypeId(),rsp.getServiceModuleApps().get(0).getOriginId());
				}
			} else
				dto.setAppId(tagAppidMap.get(bill.getResourceTypeId()));
			response.getRentalBills().add(dto);
		}
		return response;
	}

	@Override
	public ListRentalBillsCommandResponse listActiveRentalBills(ListRentalBillsCommand cmd) {
		ListRentalBillsCommandResponse response = new ListRentalBillsCommandResponse();
		if (cmd.getPageAnchor() == null)
			cmd.setPageAnchor(Long.MAX_VALUE);
		Integer pageSize = PaginationConfigHelper.getPageSize(
				configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<RentalOrder> bills = rentalv2Provider.listActiveBills(cmd.getRentalSiteId(), locator, pageSize, cmd.getStartTime(), cmd.getEndTime());

		if (bills == null) {
			return response;
		}

		if (bills.size() > pageSize) {
			bills.remove(bills.size() - 1);
			response.setNextPageAnchor(bills.get(bills.size() - 1).getReserveTime().getTime());
		}

		response.setRentalBills(new ArrayList<>());
		for (RentalOrder bill : bills) {
			RentalBillDTO dto = processOrderDTO(bill);
			response.getRentalBills().add(dto);
		}

		return response;
	}

	@Override
	public void deleteRentalBill(DeleteRentalBillCommand cmd) {
		RentalOrder order = this.rentalv2Provider.findRentalBillById(cmd.getRentalBillId());
		if (null == order)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid   parameter in the command: order not found");
		if (!order.getRentalUid().equals(UserContext.current().getUser().getId()))
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_ACCESS_DENIED,
					"Permission denied");
		if (order.getStatus().equals(SiteBillStatus.PAYINGFINAL.getCode()))
			order.setStatus(SiteBillStatus.FAIL.getCode());
		order.setVisibleFlag(VisibleFlag.UNVISIBLE.getCode());
		this.rentalv2Provider.updateRentalBill(order);
	}


	@Override
	public OnlinePayCallbackCommandResponse onlinePayCallback(
			OnlinePayCallbackCommand cmd) {
		// 
		OnlinePayCallbackCommandResponse response = new OnlinePayCallbackCommandResponse();
		if (cmd.getPayStatus().toLowerCase().equals("fail")) {

			LOGGER.info(" ----------------- - - - PAY FAIL command is " + cmd.toString());
		}

		//success
		if (cmd.getPayStatus().toLowerCase().equals("success")) {
//			RentalOrderPayorderMap bpbMap= rentalv2Provider.findRentalBillPaybillMapByOrderNo(cmd.getOrderNo());
//			RentalOrder bill = rentalv2Provider.findRentalBillById(bpbMap.getOrderId());

			RentalOrder bill = rentalv2Provider.findRentalBillByOrderNo(String.valueOf(cmd.getOrderNo()));

			bill.setPaidMoney(bill.getPaidMoney().add(new BigDecimal(cmd.getPayAmount())));
			bill.setVendorType(cmd.getVendorType());
//			bpbMap.setVendorType(cmd.getVendorType());
			bill.setOperateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));

//			bpbMap.setOperateTime(new Timestamp(DateHelper.currentGMTTime()
//					.getTime()));
//			bill.setOperatorUid(UserContext.current().getUser().getId());
			if (bill.getStatus().equals(SiteBillStatus.PAYINGFINAL.getCode())) {
				if (bill.getPayTotalMoney().equals(bill.getPaidMoney())) {
					bill.setStatus(SiteBillStatus.SUCCESS.getCode());
					RentalMessageHandler handler = rentalCommonService.getRentalMessageHandler(bill.getResourceType());
					handler.sendRentalSuccessSms(bill);

				} else {
					LOGGER.error("待付款订单:id [" + bill.getId() + "]付款金额有问题： 应该付款金额：" + bill.getPayTotalMoney() + "实际付款金额：" + bill.getPaidMoney());

					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
							ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid parameter : price is not right!");
				}
			} else if (bill.getStatus().equals(SiteBillStatus.SUCCESS.getCode())) {
				LOGGER.error("待付款订单:id [" + bill.getId() + "] 状态已经是成功预约");
			} else {
				LOGGER.error("待付款订单:id [" + bill.getId() + "]状态有问题： 订单状态是：" + bill.getStatus());
			}
			rentalv2Provider.updateRentalBill(bill);
		}
		return response;
	}

	@Override
	public FindRentalSiteMonthStatusCommandResponse findRentalSiteMonthStatus(
			FindRentalSiteMonthStatusCommand cmd) {
//		RentalResource rs = this.rentalv2Provider.getRentalSiteById(cmd.getSiteId());

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}
		currentSceneType.set(cmd.getSceneType());
		RentalResource rs = rentalCommonService.getRentalResource(cmd.getResourceType(), cmd.getSiteId());

		RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(null, null,
				rs.getResourceType(), rs.getResourceTypeId(), RuleSourceType.RESOURCE.getCode(), rs.getId());

//		correctRetalResource(rs, cmd.getRentalType());
		processCells(rs, cmd.getRentalType());
		FindRentalSiteMonthStatusCommandResponse response = ConvertHelper.convert(rule, FindRentalSiteMonthStatusCommandResponse.class);
		response.setRentalSiteId(rs.getId());
		List<RentalResourcePic> pics = this.rentalv2Provider.findRentalSitePicsByOwnerTypeAndId(cmd.getResourceType(),
				EhRentalv2Resources.class.getSimpleName(), rs.getId());
		response.setSitePics(convertRentalSitePicDTOs(pics));

		response.setAnchorTime(0L);

		List<RentalConfigAttachment> attachments = this.rentalv2Provider.queryRentalConfigAttachmentByOwner(rs.getResourceType(),
				EhRentalv2Resources.class.getSimpleName(), rs.getId(), null);
		response.setAttachments(convertAttachments(attachments));

		// 查rules

		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.setTime(new Date(cmd.getRuleDate()));
		end.setTime(new Date(cmd.getRuleDate()));
		//start 这个月第一天
		start.set(Calendar.DAY_OF_MONTH, 1);
		//end 下个月的第一天
		end.add(Calendar.MONTH, 1);
		end.set(Calendar.DAY_OF_MONTH,1);
//		end.add(Calendar.DAY_OF_YEAR, 7);
		response.setSiteDays(new ArrayList<>());


		processDayRuleDTOs(start, end, response.getSiteDays(), rs, rule, cmd.getRentalType(), cmd.getPackageName());
		response.setResourceCounts(rs.getResourceCounts());
		//设置优惠信息
		PriceRuleDTO dto = processPriceCut(cmd.getSiteId(),rs,  cmd.getRentalType(),cmd.getPackageName());
		response.setFullPrice(dto.getFullPrice());
		response.setCutPrice(dto.getCutPrice());
		response.setDiscountType(dto.getDiscountType());
		response.setDiscountRatio(dto.getDiscountRatio());
		//给出按半天预约的 区间信息
		if (cmd.getRentalType().equals(RentalType.HALFDAY.getCode()) || cmd.getRentalType().equals(RentalType.THREETIMEADAY.getCode())){
			List<RentalTimeInterval> halfTimeIntervals = rentalv2Provider.queryRentalTimeIntervalByOwner(rs.getResourceType(),
					RentalTimeIntervalOwnerType.RESOURCE_HALF_DAY.getCode(), rs.getId());
			setDefaultTimeIntervalName(halfTimeIntervals);
			response.setHalfDayTimeIntervals(halfTimeIntervals.stream().map(r->ConvertHelper.convert(r,TimeIntervalDTO.class)).collect(Collectors.toList()));
		}
		//每日开放时间
		response.setOpenTimes(getResourceOpenTime(rs.getResourceType(),rs.getId(),cmd.getRentalType(),","));
		cellList.get().clear();
		return response;
	}

	private String getResourceOpenTime(String resourceType, Long rentalSiteId,Byte rentalType,String separate){

			if (rentalType.equals(RentalType.HALFDAY.getCode()) || rentalType.equals(RentalType.THREETIMEADAY.getCode())){
				List<RentalTimeInterval> halfTimeIntervals = rentalv2Provider.queryRentalTimeIntervalByOwner(resourceType,
						RentalTimeIntervalOwnerType.RESOURCE_HALF_DAY.getCode(), rentalSiteId);
				if(null != halfTimeIntervals) {
					List<TimeIntervalDTO> halfDayIntervals = halfTimeIntervals.stream().map(h -> ConvertHelper.convert(h, TimeIntervalDTO.class))
							.collect(Collectors.toList());
					StringBuilder builder = new StringBuilder();
					String name = halfDayIntervals.get(0).getName() == null ? "上午" : halfDayIntervals.get(0).getName();
					builder.append(name).append(":").append(parseTimeInterval(halfDayIntervals.get(0).getBeginTime())).append("-")
							.append(parseTimeInterval(halfDayIntervals.get(0).getEndTime())).append(separate);
					if (halfDayIntervals.size()>1) {
						name = halfDayIntervals.get(1).getName() == null ? "下午" : halfDayIntervals.get(1).getName();
						builder.append(name).append(":").append(parseTimeInterval(halfDayIntervals.get(1).getBeginTime())).append("-")
								.append(parseTimeInterval(halfDayIntervals.get(1).getEndTime())).append(separate);
					}
					if ( halfDayIntervals.size()>2) {
						name = halfDayIntervals.get(2).getName() == null ? "晚上" : halfDayIntervals.get(2).getName();
						builder.append(name).append(":").append(parseTimeInterval(halfDayIntervals.get(2).getBeginTime())).append("-")
								.append(parseTimeInterval(halfDayIntervals.get(2).getEndTime())).append(separate);
					}
					builder.deleteCharAt(builder.length()-1);
					return builder.toString();
				}
			}else {
				List<RentalDayopenTime> dayopenTimes = rentalv2Provider.queryRentalDayopenTimeByOwner(resourceType,
						EhRentalv2Resources.class.getSimpleName(), rentalSiteId,rentalType);
				if (dayopenTimes != null && dayopenTimes.size()>0)
					return parseTimeInterval(dayopenTimes.get(0).getOpenTime())+"-"+parseTimeInterval(dayopenTimes.get(0).getCloseTime());
			}

		return null;
	}

	@Override
	public FindRentalSiteMonthStatusByWeekCommandResponse findRentalSiteMonthStatusByWeek(FindRentalSiteMonthStatusByWeekCommand cmd) {
//		RentalResource rs = this.rentalv2Provider.getRentalSiteById(cmd.getSiteId());


		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}

		RentalResource rs = rentalCommonService.getRentalResource(cmd.getResourceType(), cmd.getSiteId());
		currentSceneType.set(cmd.getSceneType());
		RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(null, null,
				rs.getResourceType(), rs.getResourceTypeId(), RuleSourceType.RESOURCE.getCode(), rs.getId());

		processCells(rs, cmd.getRentalType());
		FindRentalSiteMonthStatusByWeekCommandResponse response = ConvertHelper.convert(rule, FindRentalSiteMonthStatusByWeekCommandResponse.class);
		response.setRentalSiteId(rs.getId());
		List<RentalResourcePic> pics = this.rentalv2Provider.findRentalSitePicsByOwnerTypeAndId(cmd.getResourceType(),
				EhRentalv2Resources.class.getSimpleName(), rs.getId());
		response.setSitePics(convertRentalSitePicDTOs(pics));

		response.setAnchorTime(0L);

		List<RentalConfigAttachment> attachments = this.rentalv2Provider.queryRentalConfigAttachmentByOwner(rs.getResourceType(),
				EhRentalv2Resources.class.getSimpleName(), rs.getId(), null);
		response.setAttachments(convertAttachments(attachments));

		// 查rules
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.setTime(new Date(cmd.getRuleDate()));
		end.setTime(new Date(cmd.getRuleDate()));
		//start 这个月第一天
		start.set(Calendar.DAY_OF_MONTH, 1);

		//end 下个月的第一天
		end.add(Calendar.MONTH, 1);
		end.set(Calendar.DAY_OF_MONTH, 1);

		initFirstWeekMonday(start);
		initFirstWeekMonday(end);

		response.setSiteDays(new ArrayList<>());

		processWeekRuleDTOs(start, end, response.getSiteDays(), rs, rule, cmd.getRentalType(),cmd.getPackageName());
		response.setResourceCounts(rs.getResourceCounts());
		//设置优惠信息
		PriceRuleDTO dto = processPriceCut(cmd.getSiteId(), rs,  cmd.getRentalType(), cmd.getPackageName());
		response.setFullPrice(dto.getFullPrice());
		response.setCutPrice(dto.getCutPrice());
		response.setDiscountType(dto.getDiscountType());
		response.setDiscountRatio(dto.getDiscountRatio());
		//每日开放时间
		response.setOpenTimes(getResourceOpenTime(rs.getResourceType(),rs.getId(),cmd.getRentalType(),","));
		cellList.get().clear();
		return response;
	}


	@Override
	public FindRentalSiteWeekStatusCommandResponse findRentalSiteWeekStatus(FindRentalSiteWeekStatusCommand cmd) {

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}

		currentSceneType.set(cmd.getSceneType());
		RentalResource rs = rentalCommonService.getRentalResource(cmd.getResourceType(), cmd.getSiteId());

		RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(null, null,
				rs.getResourceType(), rs.getResourceTypeId(), RuleSourceType.RESOURCE.getCode(), rs.getId());

		Long timeStamp = System.currentTimeMillis();
		processCells(rs, cmd.getRentalType());
		LOGGER.info("processCells costs time :"+(System.currentTimeMillis()-timeStamp)/1000);
		FindRentalSiteWeekStatusCommandResponse response = ConvertHelper.convert(rule, FindRentalSiteWeekStatusCommandResponse.class);
		response.setRentalSiteId(rs.getId());
//		List<RentalResourcePic> pics = this.rentalv2Provider.findRentalSitePicsByOwnerTypeAndId(EhRentalv2Resources.class.getSimpleName(), rs.getId());
//		response.setSitePics(convertRentalSitePicDTOs(pics));

		response.setAnchorTime(0L);
		// 查rules

		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.setTime(new Date(cmd.getRuleDate()));
		end.setTime(new Date(cmd.getRuleDate()));
		start.set(Calendar.DAY_OF_WEEK, start.getActualMinimum(Calendar.DAY_OF_WEEK));
		end.set(Calendar.DAY_OF_WEEK, start.getActualMinimum(Calendar.DAY_OF_WEEK));
		end.add(Calendar.DAY_OF_YEAR, 7);
		response.setSiteDays(new ArrayList<>());

		timeStamp = System.currentTimeMillis();
		processDayRuleDTOs(start, end, response.getSiteDays(), rs, rule,  cmd.getRentalType(),
				cmd.getPackageName());
		response.setResourceCounts(rs.getResourceCounts());
		LOGGER.info("processDayRuleDTO costs time :"+(System.currentTimeMillis()-timeStamp)/1000);
		//设置优惠信息
		PriceRuleDTO dto = processPriceCut(cmd.getSiteId(),rs,  cmd.getRentalType(),cmd.getPackageName());
		response.setFullPrice(dto.getFullPrice());
		response.setCutPrice(dto.getCutPrice());
		response.setDiscountType(dto.getDiscountType());
		response.setDiscountRatio(dto.getDiscountRatio());

		//按小时预订的,给客户端找到每一个时间点
		if (cmd.getRentalType().equals(RentalType.HOUR.getCode())) {
			List<RentalTimeInterval> timeIntervals = this.rentalv2Provider.queryRentalTimeIntervalByOwner(rs.getResourceType(),
					EhRentalv2Resources.class.getSimpleName(), rs.getId());

			List<Long> dayTimes = calculateOrdinateList(timeIntervals);
	 		Collections.sort(dayTimes);
	 		response.setDayTimes(dayTimes);
 		}
		//给出按半天预约的 区间信息
		if (cmd.getRentalType().equals(RentalType.HALFDAY.getCode()) || cmd.getRentalType().equals(RentalType.THREETIMEADAY.getCode())){
			List<RentalTimeInterval> halfTimeIntervals = rentalv2Provider.queryRentalTimeIntervalByOwner(rs.getResourceType(),
					RentalTimeIntervalOwnerType.RESOURCE_HALF_DAY.getCode(), rs.getId());
			setDefaultTimeIntervalName(halfTimeIntervals);
			response.setHalfDayTimeIntervals(halfTimeIntervals.stream().map(r->ConvertHelper.convert(r,TimeIntervalDTO.class)).collect(Collectors.toList()));
		}
		//每日开放时间
		response.setOpenTimes(getResourceOpenTime(rs.getResourceType(),rs.getId(),cmd.getRentalType(),","));


		cellList.get().clear();
		return response;
	}

	private PriceRuleDTO processPriceCut(Long siteId, RentalResource rs,
										 Byte rentalType, String packageName) {
		PriceRuleDTO dto = new PriceRuleDTO();
		//解析场景信息

		if (packageName == null) { //使用本身的优惠
			List<Rentalv2PriceRule> priceRules = rentalv2PriceRuleProvider.listPriceRuleByOwner(rs.getResourceType(),
					PriceRuleType.RESOURCE.getCode(), rs.getId());
			Rentalv2PriceRule priceRule = priceRules.stream().filter(p -> p.getRentalType().equals(rentalType)).findFirst().get();
			if (priceRule == null)
				return dto;
			String classification = getClassification(priceRule.getUserPriceType());
			if (classification == null || priceRule.getUserPriceType().equals(RentalUserPriceType.UNIFICATION.getCode())){
				dto.setFullPrice(priceRule.getFullPrice());
				dto.setCutPrice(priceRule.getCutPrice());
				dto.setDiscountRatio(priceRule.getDiscountRatio());
				dto.setDiscountType(priceRule.getDiscountType());
			}else{
				List<RentalPriceClassification> classifications = rentalv2Provider.listClassification(rs.getResourceType(), EhRentalv2PriceRules.class.getSimpleName(), priceRule.getId(),
						null, null, priceRule.getUserPriceType(), classification);
				if (classifications != null && classifications.size() > 0){
					dto.setFullPrice(classifications.get(0).getFullPrice());
					dto.setCutPrice(classifications.get(0).getCutPrice());
					dto.setDiscountRatio(classifications.get(0).getDiscountRatio());
					dto.setDiscountType(classifications.get(0).getDiscountType());
				}
			}
		} else {
			List<Rentalv2PricePackage> pricePackages = rentalv2PricePackageProvider.listPricePackageByOwner(rs.getResourceType(),
					PriceRuleType.RESOURCE.getCode(), rs.getId(), rentalType, packageName);
			Rentalv2PricePackage pricePackage = pricePackages.get(0);
			if (pricePackage == null)
				return dto;
			String classification = getClassification(pricePackage.getUserPriceType());
			if (classification == null || pricePackage.getUserPriceType().equals(RentalUserPriceType.UNIFICATION.getCode())){
				dto.setFullPrice(pricePackage.getFullPrice());
				dto.setCutPrice(pricePackage.getCutPrice());
				dto.setDiscountRatio(pricePackage.getDiscountRatio());
				dto.setDiscountType(pricePackage.getDiscountType());
			}else{
				List<RentalPriceClassification> classifications = rentalv2Provider.listClassification(rs.getResourceType(),
						EhRentalv2PricePackages.class.getSimpleName(), pricePackage.getId(), null, null,
						pricePackage.getUserPriceType(), classification);
				if (classifications != null && classifications.size() > 0){
					dto.setFullPrice(classifications.get(0).getFullPrice());
					dto.setCutPrice(classifications.get(0).getCutPrice());
					dto.setDiscountRatio(classifications.get(0).getDiscountRatio());
					dto.setDiscountType(classifications.get(0).getDiscountType());
				}
			}
		}
		return dto;
	}

	@Override
	public FindRentalSiteYearStatusCommandResponse findRentalSiteYearStatus(FindRentalSiteYearStatusCommand cmd) {

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}

		RentalResource rs = rentalCommonService.getRentalResource(cmd.getResourceType(), cmd.getSiteId());


		processCells(rs, cmd.getRentalType());
		currentSceneType.set(cmd.getSceneType());
		RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(null, null,
				rs.getResourceType(), rs.getResourceTypeId(), RuleSourceType.RESOURCE.getCode(), rs.getId());

		FindRentalSiteYearStatusCommandResponse response = ConvertHelper.convert(rule, FindRentalSiteYearStatusCommandResponse.class);
		response.setRentalSiteId(rs.getId());


		response.setAnchorTime(0L);
		response.setRentalType(cmd.getRentalType());


		// 查rules

		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.setTime(new Date(cmd.getRuleDate()));
		end.setTime(new Date(cmd.getRuleDate()));
		start.set(Calendar.MONTH, start.getActualMinimum(Calendar.MONTH));
		initToMonthFirstDay(start);
		end.set(Calendar.MONTH, start.getActualMinimum(Calendar.MONTH));
		initToMonthFirstDay(end);
		end.add(Calendar.YEAR, 1);
		response.setSiteDays(new ArrayList<>());

		processMonthRuleDTOs(start, end, response, rule, rs, cmd.getPackageName());
		response.setResourceCounts(rs.getResourceCounts());
		//设置优惠信息
		PriceRuleDTO dto = processPriceCut(cmd.getSiteId(),rs,  cmd.getRentalType(),cmd.getPackageName());
		response.setFullPrice(dto.getFullPrice());
		response.setCutPrice(dto.getCutPrice());
		response.setDiscountType(dto.getDiscountType());
		response.setDiscountRatio(dto.getDiscountRatio());
		//每日开放时间
		response.setOpenTimes(getResourceOpenTime(rs.getResourceType(),rs.getId(),cmd.getRentalType(),","));
		cellList.get().clear();
		return response;
	}

	private void processMonthRuleDTOs(Calendar start, Calendar end, FindRentalSiteYearStatusCommandResponse response,
									  RentalDefaultRule rule, RentalResource rs,  String packageName) {
		java.util.Date reserveTime = new java.util.Date();
		Timestamp beginTime = new Timestamp(reserveTime.getTime() + rule.getRentalStartTime());

		List<Rentalv2PricePackage> pricePackages = rentalv2PricePackageProvider.listPricePackageByOwner(rs.getResourceType(),
				PriceRuleType.RESOURCE.getCode(), rs.getId(), response.getRentalType(), packageName);
		//填充套餐价格分类
		if (pricePackages != null && pricePackages.size() > 0)
			pricePackages.forEach(r->r.setPriceClassification(rentalv2Provider.listClassification(rs.getResourceType(),EhRentalv2PricePackages.class.getSimpleName(),
					r.getId(),null,null,null,null)));

		List<Rentalv2PriceRule> priceRules = rentalv2PriceRuleProvider.listPriceRuleByOwner(rs.getResourceType(),
				PriceRuleType.RESOURCE.getCode(), rs.getId());
		Rentalv2PriceRule priceRule = rentalv2PriceRuleProvider.findRentalv2PriceRuleByOwner(rs.getResourceType(),
				PriceRuleType.RESOURCE.getCode(), rs.getId(),response.getRentalType());
		//填充价格分类
		priceRule.setPriceClassification(rentalv2Provider.listClassification(rs.getResourceType(),EhRentalv2PriceRules.class.getSimpleName(),
				priceRule.getId(),null,null,null,null));
		for (; start.before(end); start.add(Calendar.MONTH, 1)) {
			RentalSiteDayRulesDTO dayDto = new RentalSiteDayRulesDTO();
			response.getSiteDays().add(dayDto);
			dayDto.setSiteRules(new ArrayList<>());
			dayDto.setRentalDate(start.getTimeInMillis());
			List<RentalCell> rentalSiteRules = findRentalSiteRules(rs.getResourceType(),rs.getId(), dateSF.get().format(new java.util.Date(start.getTimeInMillis())),
					beginTime, response.getRentalType(), DateLength.DAY,
					RentalSiteStatus.NORMAL.getCode(), rule.getRentalStartTimeFlag());
			// 查sitebills
			if (null != rentalSiteRules && rentalSiteRules.size() > 0) {
				for (RentalCell rsr : rentalSiteRules) {
					RentalSiteRulesDTO dto = ConvertHelper.convert(rsr, RentalSiteRulesDTO.class);

					//根据场景来设置价格
					setRentalsiteRulePrice(dto,rsr,packageName,priceRule,pricePackages);
					dto.setId(rsr.getId());
					dto.setRuleDate(rsr.getResourceRentalDate().getTime());
					//dto.setStatus(SiteRuleStatus.OPEN.getCode());

					// 支持复选，要换一种方式计算剩余数量
					dto.setResourceCounts(rsr.getCounts());
					calculateAvailableCount(dto, rs, rsr, priceRules);
					//根据时间判断来设置status
					setRentalCellStatus(reserveTime, dto, rsr, rule);

					if (dto.getCounts() == 0 ) {
						dto.setStatus(SiteRuleStatus.CLOSE.getCode());
					}

					// 多种模式的情况下，一种模式下关闭的其它模式下对应的时间段也要关闭
					if (SiteRuleStatus.fromCode(dto.getStatus()) == SiteRuleStatus.OPEN && priceRules.size() > 1) {
						calculateCurrentStatus(dto, rs, rsr, priceRules);
					}

					setRentalSiteRulesDTOExtraInfo(dto);

					dayDto.getSiteRules().add(dto);
				}
			}
		}
	}

	private void calculateCurrentStatus(RentalSiteRulesDTO dto, RentalResource rentalResource, RentalCell rentalCell, List<Rentalv2PriceRule> priceRules) {
		boolean isClosed = rentalv2Provider.findOtherModeClosed(rentalResource, rentalCell, priceRules);
		if (isClosed) {
			dto.setStatus(SiteRuleStatus.MANUAL_CLOSE.getCode());
		}
	}

	private List<Long> calculateOrdinateList(List<RentalTimeInterval> timeIntervals) {
		List<Long> dayTimes = new ArrayList<>();

		if (null != timeIntervals) {
			for (RentalTimeInterval timeInterval : timeIntervals) {
				Long dayTimeBegin = Timestamp.valueOf(dateSF.get().format(new java.util.Date())
						+ " "
						+ String.valueOf(timeInterval.getBeginTime().intValue())
						+ ":"
						+ String.valueOf((int) ((timeInterval.getBeginTime() % 1) * 60))
						+ ":00").getTime();
				if (!dayTimes.contains(dayTimeBegin))
					dayTimes.add(dayTimeBegin);

				for (double i = timeInterval.getBeginTime(); i < timeInterval.getEndTime(); ) {

					i = i + timeInterval.getTimeStep();
					Long dayTimeEnd = Timestamp.valueOf(dateSF.get().format(new java.util.Date())
							+ " "
							+ String.valueOf((int) i)
							+ ":"
							+ String.valueOf((int) ((i % 1) * 60))
							+ ":00").getTime();
					if (!dayTimes.contains(dayTimeEnd))
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

	private List<RentalSiteFileDTO> convertRentalSiteFileDTOs(List<RentalResourceFile> files) {
		if (null != files) {
			return files.stream().map(p -> {
				RentalSiteFileDTO fileDTO=ConvertHelper.convert(p, RentalSiteFileDTO.class);
				fileDTO.setUrl(this.contentServerService.parserUri(p.getUri(),
						EntityType.USER.getCode(), UserContext.current().getUser().getId()));
				return fileDTO;
			}).collect(Collectors.toList());
		}
		return new ArrayList<>();
	}

	private void processWeekRuleDTOs(Calendar start, Calendar end, List<RentalSiteDayRulesDTO> dtos, RentalResource rs,
									 RentalDefaultRule rule,  byte rentalType, String packageName) {

		java.util.Date reserveTime = new java.util.Date();
		Timestamp beginTime = new Timestamp(reserveTime.getTime()
				+ rule.getRentalStartTime());


		List<Rentalv2PriceRule> priceRules = rentalv2PriceRuleProvider.listPriceRuleByOwner(rs.getResourceType(),
				PriceRuleType.RESOURCE.getCode(), rs.getId());
		Rentalv2PriceRule priceRule = rentalv2PriceRuleProvider.findRentalv2PriceRuleByOwner(rs.getResourceType(),
				PriceRuleType.RESOURCE.getCode(), rs.getId(),rentalType);
		//填充价格分类
		priceRule.setPriceClassification(rentalv2Provider.listClassification(rs.getResourceType(),EhRentalv2PriceRules.class.getSimpleName(),
				priceRule.getId(),null,null,null,null));
		List<Rentalv2PricePackage> pricePackages = rentalv2PricePackageProvider.listPricePackageByOwner(rs.getResourceType(),
				PriceRuleType.RESOURCE.getCode(), rs.getId(), rentalType, packageName);
		//填充套餐价格分类
		if (pricePackages != null && pricePackages.size() > 0)
			pricePackages.forEach(r->r.setPriceClassification(rentalv2Provider.listClassification(rs.getResourceType(),EhRentalv2PricePackages.class.getSimpleName(),
					r.getId(),null,null,null,null)));

		for (;start.before(end);start.add(Calendar.DATE,7)){
			RentalSiteDayRulesDTO dayDto = new RentalSiteDayRulesDTO();
			dtos.add(dayDto);
			dayDto.setSiteRules(new ArrayList<>());
			dayDto.setRentalDate(start.getTimeInMillis());
			List<RentalCell> rentalSiteRules = findRentalSiteRules(rs.getResourceType(),rs.getId(), dateSF.get().format(new java.util.Date(start.getTimeInMillis())),
					beginTime, rentalType, DateLength.DAY, RentalSiteStatus.NORMAL.getCode(), rule.getRentalStartTimeFlag());
			if (null != rentalSiteRules && rentalSiteRules.size() > 0) {
				for (RentalCell rsr : rentalSiteRules) {
					RentalSiteRulesDTO dto = ConvertHelper.convert(rsr, RentalSiteRulesDTO.class);
					//根据场景来设置价格
					setRentalsiteRulePrice(dto,rsr,packageName,priceRule,pricePackages);
					dto.setId(rsr.getId());

					dto.setRuleDate(rsr.getResourceRentalDate().getTime());

					// 支持复选，要换一种方式计算剩余数量
					dto.setResourceCounts(rsr.getCounts());
					calculateAvailableCount(dto, rs, rsr, priceRules);
					//根据时间判断来设置status
					setRentalCellStatus(reserveTime, dto, rsr, rule);

					if (dto.getCounts() == 0 ) {
						dto.setStatus(SiteRuleStatus.CLOSE.getCode());
					}

					// 多种模式的情况下，一种模式下关闭的其它模式下对应的时间段也要关闭
					if (SiteRuleStatus.fromCode(dto.getStatus()) == SiteRuleStatus.OPEN && priceRules.size() > 1) {
						calculateCurrentStatus(dto, rs, rsr, priceRules);
					}
					setRentalSiteRulesDTOExtraInfo(dto);
					dayDto.getSiteRules().add(dto);
				}
			}
		}
	}

	private void processDayRuleDTOs(Calendar start, Calendar end, List<RentalSiteDayRulesDTO> rulesDTOS,
									RentalResource rs, RentalDefaultRule rule,  byte rentalType, String packageName) {

		java.util.Date reserveTime = new java.util.Date();
		//当前时间 加上最多提前时间，得出开始时间
		Timestamp beginTime = new Timestamp(reserveTime.getTime() + rule.getRentalStartTime());

		Rentalv2PriceRule priceRule = rentalv2PriceRuleProvider.findRentalv2PriceRuleByOwner(rs.getResourceType(),
				PriceRuleType.RESOURCE.getCode(), rs.getId(),rentalType);
		//填充价格分类
		priceRule.setPriceClassification(rentalv2Provider.listClassification(rs.getResourceType(),EhRentalv2PriceRules.class.getSimpleName(),
				priceRule.getId(),null,null,null,null));
		List<Rentalv2PricePackage> pricePackages = rentalv2PricePackageProvider.listPricePackageByOwner(rs.getResourceType(),
				PriceRuleType.RESOURCE.getCode(), rs.getId(), rentalType, packageName);
		//填充套餐价格分类
		if (pricePackages != null && pricePackages.size() > 0)
			pricePackages.forEach(r->r.setPriceClassification(rentalv2Provider.listClassification(rs.getResourceType(),EhRentalv2PricePackages.class.getSimpleName(),
				r.getId(),null,null,null,null)));

		List<RentalResourceOrder> rentalResourceOrders = this.rentalv2Provider.findAllRentalSiteBillByTime(rs, start.getTimeInMillis(),
				end.getTimeInMillis());
		List<RentalCell> closedCells = this.rentalv2Provider.findCellClosedByTimeInterval(rs.getResourceType(), rs.getId(), start.getTimeInMillis(),
				end.getTimeInMillis());
		SegmentTree usedSegment = new SegmentTree();
		SegmentTree closedSegment = new SegmentTree();
		if (rentalResourceOrders != null && rentalResourceOrders.size()>0)
			for (RentalResourceOrder resourceOrder:rentalResourceOrders)
				usedSegment.putSegment(resourceOrder.getBeginTime().getTime(),resourceOrder.getEndTime().getTime(),
						resourceOrder.getRentalCount().intValue());
		if (closedCells != null && closedCells.size() >0)
			for (RentalCell cell : closedCells)
				closedSegment.putSegment(cell.getBeginTime().getTime(),cell.getEndTime().getTime(),1);

		for (; start.before(end); start.add(Calendar.DAY_OF_YEAR, 1)) {
			RentalSiteDayRulesDTO dayDto = new RentalSiteDayRulesDTO();
			rulesDTOS.add(dayDto);
			dayDto.setSiteRules(new ArrayList<>());
			dayDto.setRentalDate(start.getTimeInMillis());
			List<RentalCell> rentalSiteRules = findRentalSiteRules(rs.getResourceType(),rs.getId(), dateSF.get().format(new java.util.Date(start.getTimeInMillis())),
					beginTime, rentalType, DateLength.DAY, RentalSiteStatus.NORMAL.getCode(), rule.getRentalStartTimeFlag());

			// 查sitebills
			for (RentalCell rsr : rentalSiteRules) {
				RentalSiteRulesDTO dto = ConvertHelper.convert(rsr, RentalSiteRulesDTO.class);
				//根据场景来设置价格
				setRentalsiteRulePrice(dto,rsr,packageName,priceRule,pricePackages);
//				dto.setId(rsr.getId());
				if (dto.getRentalType().equals(RentalType.HOUR.getCode())) {
					dto.setTimeStep(rsr.getTimeStep());
					dto.setBeginTime(rsr.getBeginTime().getTime());
					dto.setEndTime(rsr.getEndTime().getTime());
					multiPriceByHour(dto,rsr.getTimeStep());
				} else if (dto.getRentalType() == RentalType.HALFDAY.getCode() ||
						dto.getRentalType() == RentalType.THREETIMEADAY.getCode()) {
					dto.setAmorpm(rsr.getAmorpm());
				}
				dto.setRuleDate(rsr.getResourceRentalDate().getTime());

				// 支持复选，要换一种方式计算剩余数量
				Double rentedCount = rsr.getCounts() - usedSegment.getMaxCover(rsr.getBeginTime().getTime(), rsr.getEndTime().getTime());
				dto.setCounts(rentedCount < 0 ?0.0:rentedCount);
				dto.setResourceCounts(rsr.getCounts());
				//根据时间判断来设置status
				setRentalCellStatus(reserveTime, dto, rsr, rule);
				//当可预约数量为0时
				if (dto.getCounts() <= 0 ) {
					dto.setStatus(SiteRuleStatus.CLOSE.getCode());
				}

				//如果多个模式，那么其它模式关的，当前模式对应时间也要关闭
				if (SiteRuleStatus.fromCode(dto.getStatus()) == SiteRuleStatus.OPEN ) {
					if (closedSegment.getMaxCover(rsr.getBeginTime().getTime(),rsr.getEndTime().getTime()) > 0)
						dto.setStatus(SiteRuleStatus.MANUAL_CLOSE.getCode());
				}

				setRentalSiteRulesDTOExtraInfo(dto);

				dayDto.getSiteRules().add(dto);
			}
		}
	}

	private void multiPriceByHour(RentalSiteRulesDTO dto,Double timeStep){
		dto.setPrice(dto.getPrice().multiply(new BigDecimal(timeStep*2)));
		dto.setOriginalPrice(dto.getOriginalPrice()==null?null:dto.getOriginalPrice().multiply(new BigDecimal(timeStep*2)));
		dto.setInitiatePrice(dto.getInitiatePrice()==null?null:dto.getInitiatePrice().multiply(new BigDecimal(timeStep*2)));
		if (dto.getPriceRules() != null) {
			for (RentalPriceClassificationDTO dto1 : dto.getPriceRules()) {
				dto1.setWorkdayPrice(dto1.getWorkdayPrice()==null?null:dto1.getWorkdayPrice().multiply(new BigDecimal(timeStep * 2)));
				dto1.setOriginalPrice(dto1.getOriginalPrice()==null?null:dto1.getOriginalPrice().multiply(new BigDecimal(timeStep * 2)));
				dto1.setInitiatePrice(dto1.getInitiatePrice()==null?null:dto1.getInitiatePrice().multiply(new BigDecimal(timeStep * 2)));
			}
		}

		if (dto.getSitePackages() != null){
			for (RentalSitePackagesDTO packagesDTO : dto.getSitePackages()){
				packagesDTO.setPrice(packagesDTO.getPrice()==null?null:packagesDTO.getPrice().multiply(new BigDecimal(timeStep * 2)));
				packagesDTO.setOriginalPrice(packagesDTO.getOriginalPrice()==null?null:packagesDTO.getOriginalPrice().multiply(new BigDecimal(timeStep * 2)));
				packagesDTO.setInitiatePrice(packagesDTO.getInitiatePrice()==null?null:packagesDTO.getInitiatePrice().multiply(new BigDecimal(timeStep * 2)));
				if (packagesDTO.getPriceRules() != null) {
					for (RentalPriceClassificationDTO dto1 : packagesDTO.getPriceRules()) {
						dto1.setWorkdayPrice(dto1.getWorkdayPrice()==null?null:dto1.getWorkdayPrice().multiply(new BigDecimal(timeStep * 2)));
						dto1.setOriginalPrice(dto1.getOriginalPrice()==null?null:dto1.getOriginalPrice().multiply(new BigDecimal(timeStep * 2)));
						dto1.setInitiatePrice(dto1.getInitiatePrice()==null?null:dto1.getInitiatePrice().multiply(new BigDecimal(timeStep * 2)));
					}
				}
			}
		}

	}

	private void validateCellStatus(RentalCell rsr, List<Rentalv2PriceRule> priceRules, RentalResource rs, RentalDefaultRule rule,
									boolean validateTime) {

		RentalSiteRulesDTO dto = ConvertHelper.convert(rsr, RentalSiteRulesDTO.class);
		// 支持复选，要换一种方式计算剩余数量
		calculateAvailableCount(dto, rs, rsr, priceRules);
		//根据时间判断来设置status
		if (validateTime) {
			setRentalCellStatus(new java.util.Date(), dto, rsr, rule);
		}
		//当可预约数量为0时
		if (dto.getCounts() == 0) {
			dto.setStatus(SiteRuleStatus.CLOSE.getCode());
		}

		//如果多个模式，那么其它模式关的，当前模式对应时间也要关闭
		if (SiteRuleStatus.fromCode(dto.getStatus()) == SiteRuleStatus.OPEN && priceRules.size() > 1) {
			calculateCurrentStatus(dto, rs, rsr, priceRules);
		}

		if (dto.getStatus() != SiteRuleStatus.OPEN.getCode()) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_NO_ENOUGH_SITES,
					"Not enough sites to rental");
		}
	}

	//兼容老版本app
	private void setRentalSiteRulesDTOExtraInfo(RentalSiteRulesDTO dto) {
		//独占资源
		dto.setExclusiveFlag((byte) 1);
		dto.setUnit(1D);
		dto.setRentalStep(1);
	}

	private void processPricePackage(List<RentalSitePackagesDTO> packageDtos, List<Rentalv2PricePackage> pricePackages) {
		for (Rentalv2PricePackage pricePackage : pricePackages) {
			RentalSitePackagesDTO dto = new RentalSitePackagesDTO();
			dto.setId(pricePackage.getId());
			dto.setName(pricePackage.getName());
			dto.setRentalSiteId(pricePackage.getOwnerId());
			dto.setRentalType(pricePackage.getRentalType());
			dto.setOrgMemberPrice(pricePackage.getOrgMemberPrice() == null ? new BigDecimal(0) : pricePackage.getOrgMemberPrice());
			dto.setOrgMemberInitiatePrice(pricePackage.getOrgMemberInitiatePrice());
			dto.setOrgMemberOriginalPrice(pricePackage.getOrgMemberOriginalPrice());
			dto.setApprovingUserPrice((pricePackage.getApprovingUserPrice() == null ? new BigDecimal(0) : pricePackage.getApprovingUserPrice()));
			dto.setApprovingUserInitiatePrice(pricePackage.getApprovingUserInitiatePrice());
			dto.setApprovingUserOriginalPrice(pricePackage.getApprovingUserOriginalPrice());
			dto.setPrice(pricePackage.getPrice() == null ? new BigDecimal(0) : pricePackage.getPrice());
			dto.setInitiatePrice(pricePackage.getInitiatePrice());
			dto.setOriginalPrice(pricePackage.getOriginalPrice());
			dto.setUserPriceType(pricePackage.getUserPriceType());
			packageDtos.add(dto);
		}
	}

	private void setRentalsiteRulePrice(RentalSiteRulesDTO dto,RentalCell rsr,String packageName,Rentalv2PriceRule priceRule,
											List<Rentalv2PricePackage> pricePackages) {
	    //取得单元格默认的价格
		if (priceRule.getPriceClassification() != null)
			dto.setPriceRules(priceRule.getPriceClassification().stream().map(r->ConvertHelper.convert(r,RentalPriceClassificationDTO.class))
				.collect(Collectors.toList()));
		List<RentalSitePackagesDTO> sitePackages = new ArrayList<>();
		if (pricePackages != null) {
			for (Rentalv2PricePackage pricePackage : pricePackages) {
				RentalSitePackagesDTO packagesDTO = ConvertHelper.convert(pricePackage, RentalSitePackagesDTO.class);
				if (pricePackage.getPriceClassification() != null)
					packagesDTO.setPriceRules(pricePackage.getPriceClassification().stream().map(r -> ConvertHelper.convert(r, RentalPriceClassificationDTO.class))
							.collect(Collectors.toList()));
				sitePackages.add(packagesDTO);
			}
		}
		dto.setSitePackages(sitePackages);

		//如果单元格单独设置价格
		RentalCell dbCell = rentalv2Provider.getRentalCellById(rsr.getId(),rsr.getRentalResourceId(),priceRule.getRentalType(),priceRule.getResourceType());
		if (dbCell != null){
			dto.setUserPriceType(dbCell.getUserPriceType());
			dto.setPrice(dbCell.getPrice());
			dto.setOriginalPrice(dbCell.getOriginalPrice());
			//因为交换过 cellId才是真正的主键id
			List<RentalPriceClassification> classification = rentalv2Provider.listClassification(rsr.getResourceType(), EhRentalv2Cells.class.getSimpleName(),
					rsr.getCellId(), null, null, null, null);
			dto.setPriceRules(classification.stream().map(r->ConvertHelper.convert(r,RentalPriceClassificationDTO.class))
					.collect(Collectors.toList()));
			if (dbCell.getPricePackageId() != null){ //设置单独套餐价格
				List<Rentalv2PricePackage> pricePackages2 = rentalv2PricePackageProvider.listPricePackageByOwner(
						rsr.getResourceType(), PriceRuleType.CELL.getCode(), dbCell.getPricePackageId(), null, packageName);
				List<RentalSitePackagesDTO> sitePackages2 = new ArrayList<>();
				if (pricePackages2 != null) {
					for (Rentalv2PricePackage pricePackage : pricePackages2) {
						RentalSitePackagesDTO packagesDTO = ConvertHelper.convert(pricePackage, RentalSitePackagesDTO.class);
						if (pricePackage.getPriceClassification() != null) {
							List<RentalPriceClassification> cla = rentalv2Provider.listClassification(rsr.getResourceType(), EhRentalv2PricePackages.class.getSimpleName(),
									pricePackage.getId(), null, null, null, null);
							packagesDTO.setPriceRules(cla.stream().map(r->ConvertHelper.convert(r,RentalPriceClassificationDTO.class)).collect(Collectors.toList()));
						}
						sitePackages2.add(packagesDTO);
					}
				}
				dto.setSitePackages(sitePackages2);
			}
		}

		//根据场景选择价格
		String classification = getClassification(dto.getUserPriceType());
		if (classification != null) {
			if (packageName == null) {
				for (RentalPriceClassificationDTO dto1 : dto.getPriceRules())
					if (classification.equals(dto1.getClassification()) && dto1.getUserPriceType().equals(dto.getUserPriceType())) {
						dto.setPrice(dto1.getWorkdayPrice());
						dto.setOriginalPrice(dto1.getOriginalPrice());
						dto.setInitiatePrice(dto1.getInitiatePrice());
						break;
					}
			}else if (dto.getSitePackages() != null && dto.getSitePackages().size() > 0){
				for (RentalPriceClassificationDTO dto1 : dto.getSitePackages().get(0).getPriceRules())
					if (classification.equals(dto1.getClassification()) && dto1.getUserPriceType().equals(dto.getUserPriceType())) {
						dto.setPrice(dto1.getWorkdayPrice());
						dto.setOriginalPrice(dto1.getOriginalPrice());
						dto.setInitiatePrice(dto1.getInitiatePrice());
						break;
					}
			}
		}else if (packageName != null) {
				dto.setPrice(dto.getSitePackages().get(0).getPrice());
				dto.setOriginalPrice(dto.getSitePackages().get(0).getOriginalPrice());
				dto.setInitiatePrice(dto.getSitePackages().get(0).getInitiatePrice());
			}
	}


	@Override
	public FindAutoAssignRentalSiteWeekStatusResponse findAutoAssignRentalSiteWeekStatus(
			FindAutoAssignRentalSiteWeekStatusCommand cmd) {

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}

		java.util.Date reserveTime = new java.util.Date();

		RentalResource rs = rentalCommonService.getRentalResource(cmd.getResourceType(), cmd.getSiteId());

//		RentalResource rs = this.rentalv2Provider.getRentalSiteById(cmd.getSiteId());

		RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(null, null,
				rs.getResourceType(), rs.getResourceTypeId(), RuleSourceType.RESOURCE.getCode(), rs.getId());

//		correctRetalResource(rs, cmd.getRentalType());
		processCells(rs, cmd.getRentalType());
		FindAutoAssignRentalSiteWeekStatusResponse response = ConvertHelper.convert(rule, FindAutoAssignRentalSiteWeekStatusResponse.class);
		//场所数量和编号
		response.setAutoAssign(rs.getAutoAssign());
		response.setMultiUnit(rs.getMultiUnit());
		response.setSiteCounts(rs.getResourceCounts());
		List<RentalResourceNumber> resourceNumbers = this.rentalv2Provider.queryRentalResourceNumbersByOwner(cmd.getResourceType(),
				EhRentalv2Resources.class.getSimpleName(), rs.getId());
		if (null != resourceNumbers) {
			response.setSiteNames(new ArrayList<>());
			for (RentalResourceNumber number : resourceNumbers) {
				response.getSiteNames().add(number.getResourceNumber());
			}
		}
		response.setRentalSiteId(rs.getId());
		List<RentalResourcePic> pics = this.rentalv2Provider.findRentalSitePicsByOwnerTypeAndId(cmd.getResourceType(),
				EhRentalv2Resources.class.getSimpleName(), rs.getId());
		response.setSitePics(convertRentalSitePicDTOs(pics));

		response.setAnchorTime(0L);


		List<RentalConfigAttachment> attachments = this.rentalv2Provider.queryRentalConfigAttachmentByOwner(rs.getResourceType(),
				EhRentalv2Resources.class.getSimpleName(), rs.getId(), null);
		response.setAttachments(convertAttachments(attachments));

		// 查rules

		java.util.Date nowTime = new java.util.Date();
//		response.setContactNum(rs.getContactPhonenum());
		Timestamp beginTime = new Timestamp(nowTime.getTime()
				+ rule.getRentalStartTime());
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
		List<Rentalv2PriceRule> priceRules = rentalv2PriceRuleProvider.listPriceRuleByOwner(rs.getResourceType(),
				PriceRuleType.RESOURCE.getCode(), rs.getId());
		List<RentalTimeInterval> halfTimeIntervals = rentalv2Provider.queryRentalTimeIntervalByOwner(rs.getResourceType(),
				RentalTimeIntervalOwnerType.RESOURCE_HALF_DAY.getCode(), rs.getId());
		for (; start.before(end); start.add(Calendar.DAY_OF_YEAR, 1)) {
			RentalSiteNumberDayRulesDTO dayDto = new RentalSiteNumberDayRulesDTO();
			response.getSiteDays().add(dayDto);
			Map<String, List<RentalSiteRulesDTO>> siteNumberMap = new HashMap<>();
			for (RentalResourceNumber resourceNumber : resourceNumbers) {
				siteNumberMap.put(resourceNumber.getResourceNumber(), new ArrayList<>());
			}
			dayDto.setSiteNumbers(new ArrayList<>());
			dayDto.setRentalDate(start.getTimeInMillis());
			List<RentalCell> rentalSiteRules = findRentalSiteRules(cmd.getResourceType(),cmd.getSiteId(), dateSF.get().format(new java.util.Date(start.getTimeInMillis())),
					beginTime, cmd.getRentalType() == null ? RentalType.DAY.getCode() : cmd.getRentalType(), DateLength.DAY,
					RentalSiteStatus.NORMAL.getCode(), rule.getRentalStartTimeFlag());
			// 查sitebills
			if (null != rentalSiteRules && rentalSiteRules.size() > 0) {
				for (RentalCell rsr : rentalSiteRules) {
					RentalSiteRulesDTO dto = ConvertHelper.convert(rsr, RentalSiteRulesDTO.class);
					dto.setSiteNumber(String.valueOf(rsr.getResourceNumber()));
					dto.setId(rsr.getId());
					if (dto.getRentalType().equals(RentalType.HOUR.getCode())) {
						dto.setTimeStep(rsr.getTimeStep());
						dto.setBeginTime(rsr.getBeginTime().getTime());
						dto.setEndTime(rsr.getEndTime().getTime());
						if (response.getAnchorTime().equals(0L)) {
							response.setAnchorTime(dto.getBeginTime());
						} else {
							try {
								if (timeSF.get().parse(timeSF.get().format(new java.util.Date(response.getAnchorTime()))).after(
										timeSF.get().parse(timeSF.get().format(new java.util.Date(dto.getBeginTime()))))) {
									response.setAnchorTime(dto.getBeginTime());
								}
							} catch (Exception e) {
								LOGGER.error("anchorTime error  dto = " + dto);
							}


						}
					} else if (dto.getRentalType() == RentalType.HALFDAY.getCode()
							|| dto.getRentalType() == RentalType.THREETIMEADAY.getCode()) {
						dto.setAmorpm(rsr.getAmorpm());
					}
					dto.setRuleDate(rsr.getResourceRentalDate().getTime());
					dto.setStatus(SiteRuleStatus.OPEN.getCode());

					// 支持复选，要换一种方式计算剩余数量
					calculateAvailableCount(dto, rs, rsr, priceRules);
					if (dto.getRentalType().equals(RentalType.HOUR.getCode())) {
						if ((NormalFlag.NEED.getCode() == rule.getRentalStartTimeFlag()) && (reserveTime.before(new java.util.Date(rsr
								.getBeginTime().getTime()
								- rule.getRentalStartTime())))) {
							dto.setStatus(SiteRuleStatus.EARLY.getCode());
						}
						if ((NormalFlag.NEED.getCode() == rule.getRentalEndTimeFlag()) && (reserveTime.after(new java.util.Date(rsr
								.getBeginTime().getTime()
								- rule.getRentalEndTime())))) {
							dto.setStatus(SiteRuleStatus.LATE.getCode());
						}
					} else {
						Long dayBeginTime = 0L;
						if (rsr.getAmorpm() != null) {
							if (rsr.getAmorpm().equals(AmorpmFlag.AM.getCode())) {
								dayBeginTime = 10 * 60 * 60 * 1000L;
								if (halfTimeIntervals != null && halfTimeIntervals.size() > 0)
									dayBeginTime = (long)(halfTimeIntervals.get(0).getBeginTime() * 60 * 60 * 1000L);
							} else if (rsr.getAmorpm().equals(AmorpmFlag.PM.getCode())) {
								dayBeginTime = 15 * 60 * 60 * 1000L;
								if (halfTimeIntervals != null && halfTimeIntervals.size() > 1)
									dayBeginTime = (long)(halfTimeIntervals.get(1).getBeginTime() * 60 * 60 * 1000L);
							} else if (rsr.getAmorpm().equals(AmorpmFlag.NIGHT.getCode())) {
								dayBeginTime = 20 * 60 * 60 * 1000L;
								if (halfTimeIntervals != null && halfTimeIntervals.size() > 2)
									dayBeginTime = (long)(halfTimeIntervals.get(2).getBeginTime() * 60 * 60 * 1000L);
							}
						}
						if ((NormalFlag.NEED.getCode() == rule.getRentalStartTimeFlag()) && (reserveTime.before(new java.util.Date(rsr
								.getResourceRentalDate().getTime() + dayBeginTime
								- rule.getRentalStartTime())))) {
							dto.setStatus(SiteRuleStatus.EARLY.getCode());
						}
						if ((NormalFlag.NEED.getCode() == rule.getRentalEndTimeFlag()) && (reserveTime.after(new java.util.Date(rsr
								.getResourceRentalDate().getTime() + dayBeginTime
								- rule.getRentalEndTime())))) {
							dto.setStatus(SiteRuleStatus.LATE.getCode());
						}
					}

					if (dto.getCounts() == 0 || rsr.getStatus().equals((byte) -1)) {
						dto.setStatus(SiteRuleStatus.CLOSE.getCode());
					}

					//如果多个模式，那么其它模式关的，当前模式对应时间也要关闭
					if (SiteRuleStatus.fromCode(dto.getStatus()) == SiteRuleStatus.OPEN && priceRules.size() > 1) {
						calculateCurrentStatus(dto, rs, rsr, priceRules);
					}

					setRentalSiteRulesDTOExtraInfo(dto);

					siteNumberMap.computeIfAbsent(dto.getSiteNumber(), k -> new ArrayList<>());
					siteNumberMap.get(dto.getSiteNumber()).add(dto);
				}
			}

			//
			for (String siteNumber : response.getSiteNames()) {
				RentalSiteNumberRuleDTO siteNumberRuleDTO = new RentalSiteNumberRuleDTO();
				siteNumberRuleDTO.setSiteNumber(siteNumber);
				siteNumberRuleDTO.setSiteRules(siteNumberMap.get(siteNumber));
				dayDto.getSiteNumbers().add(siteNumberRuleDTO);
			}
		}

		cellList.get().clear();
		return response;
	}

	@Override
	public FindAutoAssignRentalSiteMonthStatusResponse findAutoAssignRentalSiteMonthStatus(
			FindAutoAssignRentalSiteMonthStatusCommand cmd) {

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}

		java.util.Date reserveTime = new java.util.Date();

		RentalResource rs = rentalCommonService.getRentalResource(cmd.getResourceType(), cmd.getSiteId());
		currentSceneType.set(cmd.getSceneType());
//		RentalResource rs = this.rentalv2Provider.getRentalSiteById(cmd.getSiteId());
		RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(null, null,
				rs.getResourceType(), rs.getResourceTypeId(), RuleSourceType.RESOURCE.getCode(), rs.getId());

//		correctRetalResource(rs, cmd.getRentalType());
		processCells(rs, cmd.getRentalType());
		FindAutoAssignRentalSiteMonthStatusResponse response = ConvertHelper.convert(rule, FindAutoAssignRentalSiteMonthStatusResponse.class);
		//场所数量和编号
		response.setAutoAssign(rs.getAutoAssign());
		response.setMultiUnit(rs.getMultiUnit());
		response.setSiteCounts(rs.getResourceCounts());
		List<RentalResourceNumber> resourceNumbers = this.rentalv2Provider.queryRentalResourceNumbersByOwner(cmd.getResourceType(),
				EhRentalv2Resources.class.getSimpleName(), rs.getId());
		if (null != resourceNumbers) {
			response.setSiteNames(new ArrayList<>());
			for (RentalResourceNumber number : resourceNumbers) {
				response.getSiteNames().add(number.getResourceNumber());
			}
		}
		response.setRentalSiteId(rs.getId());
		List<RentalResourcePic> pics = this.rentalv2Provider.findRentalSitePicsByOwnerTypeAndId(cmd.getResourceType(),
				EhRentalv2Resources.class.getSimpleName(), rs.getId());
		response.setSitePics(convertRentalSitePicDTOs(pics));

		response.setAnchorTime(0L);

		List<RentalConfigAttachment> attachments = this.rentalv2Provider.queryRentalConfigAttachmentByOwner(rs.getResourceType(),
				EhRentalv2Resources.class.getSimpleName(), rs.getId(), null);
		response.setAttachments(convertAttachments(attachments));

		//设置优惠信息
		PriceRuleDTO ruleDto = processPriceCut(cmd.getSiteId(),rs,  cmd.getRentalType(),cmd.getPackageName());
		response.setFullPrice(ruleDto.getFullPrice());
		response.setCutPrice(ruleDto.getCutPrice());
		response.setDiscountType(ruleDto.getDiscountType());
		response.setDiscountRatio(ruleDto.getDiscountRatio());

		// 查rules

		java.util.Date nowTime = new java.util.Date();
//		response.setContactNum(rs.getContactPhonenum());
		Timestamp beginTime = new Timestamp(nowTime.getTime()
				+ rule.getRentalStartTime());
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

		List<Rentalv2PriceRule> priceRules = rentalv2PriceRuleProvider.listPriceRuleByOwner(rs.getResourceType(),
				PriceRuleType.RESOURCE.getCode(), rs.getId());
		Rentalv2PriceRule priceRule = rentalv2PriceRuleProvider.findRentalv2PriceRuleByOwner(rs.getResourceType(),
				PriceRuleType.RESOURCE.getCode(), rs.getId(),cmd.getRentalType());
		//填充价格分类
		priceRule.setPriceClassification(rentalv2Provider.listClassification(rs.getResourceType(),EhRentalv2PriceRules.class.getSimpleName(),
				priceRule.getId(),null,null,null,null));
		List<Rentalv2PricePackage> pricePackages = rentalv2PricePackageProvider.listPricePackageByOwner(rs.getResourceType(),
				PriceRuleType.RESOURCE.getCode(), rs.getId(), cmd.getRentalType(), cmd.getPackageName());
		//填充套餐价格分类
		if (pricePackages != null && pricePackages.size() > 0)
			pricePackages.forEach(r->r.setPriceClassification(rentalv2Provider.listClassification(rs.getResourceType(),EhRentalv2PricePackages.class.getSimpleName(),
					r.getId(),null,null,null,null)));

		for (; start.before(end); start.add(Calendar.DAY_OF_YEAR, 1)) {
			RentalSiteNumberDayRulesDTO dayDto = new RentalSiteNumberDayRulesDTO();
			response.getSiteDays().add(dayDto);
			Map<String, List<RentalSiteRulesDTO>> siteNumberMap = new HashMap<>();
			for (RentalResourceNumber resourceNumber : resourceNumbers) {
				siteNumberMap.put(resourceNumber.getResourceNumber(), new ArrayList<>());
			}
			dayDto.setSiteNumbers(new ArrayList<>());
			dayDto.setRentalDate(start.getTimeInMillis());
			dayDto.setSitePackages(new ArrayList<>());
			List<RentalCell> rentalSiteRules = findRentalSiteRules(cmd.getResourceType(),cmd.getSiteId(), dateSF.get().format(new java.util.Date(start.getTimeInMillis())),
					beginTime, cmd.getRentalType() == null ? RentalType.DAY.getCode() : cmd.getRentalType(), DateLength.DAY,
					RentalSiteStatus.NORMAL.getCode(), rule.getRentalStartTimeFlag());
			// 查sitebills
			if (null != rentalSiteRules && rentalSiteRules.size() > 0) {
				for (RentalCell rsr : rentalSiteRules) {
					RentalSiteRulesDTO dto = ConvertHelper.convert(rsr, RentalSiteRulesDTO.class);
					dto.setSiteNumber(String.valueOf(rsr.getResourceNumber()));
					dto.setId(rsr.getId());
					//根据场景设置价格
					setRentalsiteRulePrice(dto,rsr,cmd.getPackageName(),priceRule,pricePackages);

					if (dto.getRentalType() == RentalType.HALFDAY.getCode()
							|| dto.getRentalType() == RentalType.THREETIMEADAY.getCode()) {
						dto.setAmorpm(rsr.getAmorpm());
					}
					dto.setRuleDate(rsr.getResourceRentalDate().getTime());
					//dto.setStatus(SiteRuleStatus.OPEN.getCode());

					// 支持复选，要换一种方式计算剩余数量
					calculateAvailableCount(dto, rs, rsr, priceRules);
					dto.setResourceCounts(rsr.getCounts());
					//根据时间判断来设置status
					setRentalCellStatus(reserveTime, dto, rsr, rule);

					if (dto.getCounts() == 0 ) {
						dto.setStatus(SiteRuleStatus.CLOSE.getCode());
					}

					//如果多个模式，那么其它模式关的，当前模式对应时间也要关闭
					if (SiteRuleStatus.fromCode(dto.getStatus()) == SiteRuleStatus.OPEN && priceRules.size() > 1) {
						calculateCurrentStatus(dto, rs, rsr, priceRules);
					}

					setRentalSiteRulesDTOExtraInfo(dto);

					siteNumberMap.computeIfAbsent(dto.getSiteNumber(), k -> new ArrayList<>());
					siteNumberMap.get(dto.getSiteNumber()).add(dto);
				}
			}

			//
			for (String siteNumber : response.getSiteNames()) {
				RentalSiteNumberRuleDTO siteNumberRuleDTO = new RentalSiteNumberRuleDTO();
				siteNumberRuleDTO.setSiteNumber(siteNumber);
				siteNumberRuleDTO.setSiteRules(siteNumberMap.get(siteNumber));
				dayDto.getSiteNumbers().add(siteNumberRuleDTO);
			}

		}
		//给出按半天预约的 区间信息
		if (cmd.getRentalType().equals(RentalType.HALFDAY.getCode()) || cmd.getRentalType().equals(RentalType.THREETIMEADAY.getCode())){
			List<RentalTimeInterval> halfTimeIntervals = rentalv2Provider.queryRentalTimeIntervalByOwner(rs.getResourceType(),
					RentalTimeIntervalOwnerType.RESOURCE_HALF_DAY.getCode(), rs.getId());
			setDefaultTimeIntervalName(halfTimeIntervals);
			response.setHalfDayTimeIntervals(halfTimeIntervals.stream().map(r->ConvertHelper.convert(r,TimeIntervalDTO.class)).collect(Collectors.toList()));
		}
		//每日开放时间
		response.setOpenTimes(getResourceOpenTime(rs.getResourceType(),rs.getId(),cmd.getRentalType(),","));
		cellList.get().clear();
		return response;
	}

	private void initFirstWeekMonday(Calendar date) {
		if (date.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			date.add(Calendar.DATE, -1);
		}
		if (date.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
			date.set(Calendar.DAY_OF_WEEK, 2);
			date.add(Calendar.DATE, 7);
		}
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.MILLISECOND, 0);
	}

	@Override
	public FindAutoAssignRentalSiteMonthStatusByWeekResponse findAutoAssignRentalSiteMonthStatusByWeek(FindAutoAssignRentalSiteMonthStatusByWeekCommand cmd) {

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}

		java.util.Date reserveTime = new java.util.Date();

//		RentalResource rs = this.rentalv2Provider.getRentalSiteById(cmd.getSiteId());

		RentalResource rs = rentalCommonService.getRentalResource(cmd.getResourceType(), cmd.getSiteId());
		currentSceneType.set(cmd.getSceneType());
		RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(null, null,
				rs.getResourceType(), rs.getResourceTypeId(), RuleSourceType.RESOURCE.getCode(), rs.getId());

		processCells(rs, cmd.getRentalType());
		FindAutoAssignRentalSiteMonthStatusByWeekResponse response = ConvertHelper.convert(rule, FindAutoAssignRentalSiteMonthStatusByWeekResponse.class);

		//场所数量和编号
		response.setAutoAssign(rs.getAutoAssign());
		response.setMultiUnit(rs.getMultiUnit());
		response.setSiteCounts(rs.getResourceCounts());
		List<RentalResourceNumber> resourceNumbers = this.rentalv2Provider.queryRentalResourceNumbersByOwner(cmd.getResourceType(),
				EhRentalv2Resources.class.getSimpleName(), rs.getId());
		if (null != resourceNumbers) {
			response.setSiteNames(new ArrayList<>());
			for (RentalResourceNumber number : resourceNumbers) {
				response.getSiteNames().add(number.getResourceNumber());
			}
		}
		response.setRentalSiteId(rs.getId());
		List<RentalResourcePic> pics = this.rentalv2Provider.findRentalSitePicsByOwnerTypeAndId(cmd.getResourceType(),
				EhRentalv2Resources.class.getSimpleName(), rs.getId());
		response.setSitePics(convertRentalSitePicDTOs(pics));

		response.setAnchorTime(0L);

		List<RentalConfigAttachment> attachments = this.rentalv2Provider.queryRentalConfigAttachmentByOwner(rs.getResourceType(),
				EhRentalv2Resources.class.getSimpleName(), rs.getId(), null);
		response.setAttachments(convertAttachments(attachments));

		//设置优惠信息
		PriceRuleDTO ruleDto = processPriceCut(cmd.getSiteId(),rs,  cmd.getRentalType(),cmd.getPackageName());
		response.setFullPrice(ruleDto.getFullPrice());
		response.setCutPrice(ruleDto.getCutPrice());
		response.setDiscountType(ruleDto.getDiscountType());
		response.setDiscountRatio(ruleDto.getDiscountRatio());


		java.util.Date nowTime = new java.util.Date();
//		response.setContactNum(rs.getContactPhonenum());
		Timestamp beginTime = new Timestamp(nowTime.getTime()
				+ rule.getRentalStartTime());
		// 查rules
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.setTime(new Date(cmd.getRuleDate()));
		end.setTime(new Date(cmd.getRuleDate()));
		//start 这个月第一天
		start.set(Calendar.DAY_OF_MONTH, 1);

		//end 下个月的第一天
		end.add(Calendar.MONTH, 1);
		end.set(Calendar.DAY_OF_MONTH, 1);

		initFirstWeekMonday(start);
		initFirstWeekMonday(end);
		response.setSiteDays(new ArrayList<>());

		List<Rentalv2PriceRule> priceRules = rentalv2PriceRuleProvider.listPriceRuleByOwner(rs.getResourceType(),
				PriceRuleType.RESOURCE.getCode(), rs.getId());
		Rentalv2PriceRule priceRule = rentalv2PriceRuleProvider.findRentalv2PriceRuleByOwner(rs.getResourceType(),
				PriceRuleType.RESOURCE.getCode(), rs.getId(),cmd.getRentalType());
		//填充价格分类
		priceRule.setPriceClassification(rentalv2Provider.listClassification(rs.getResourceType(),EhRentalv2PriceRules.class.getSimpleName(),
				priceRule.getId(),null,null,null,null));
		List<Rentalv2PricePackage> pricePackages = rentalv2PricePackageProvider.listPricePackageByOwner(rs.getResourceType(),
				PriceRuleType.RESOURCE.getCode(), rs.getId(), cmd.getRentalType(), cmd.getPackageName());
		//填充套餐价格分类
		if (pricePackages != null && pricePackages.size() > 0)
			pricePackages.forEach(r->r.setPriceClassification(rentalv2Provider.listClassification(rs.getResourceType(),EhRentalv2PricePackages.class.getSimpleName(),
					r.getId(),null,null,null,null)));

		for (; start.before(end); start.add(Calendar.DATE, 7)) {
			RentalSiteNumberDayRulesDTO dayDto = new RentalSiteNumberDayRulesDTO();
			response.getSiteDays().add(dayDto);
			Map<String, List<RentalSiteRulesDTO>> siteNumberMap = new HashMap<>();
			for (RentalResourceNumber resourceNumber : resourceNumbers) {
				siteNumberMap.put(resourceNumber.getResourceNumber(), new ArrayList<>());
			}
			dayDto.setSiteNumbers(new ArrayList<>());
			dayDto.setRentalDate(start.getTimeInMillis());
			dayDto.setSitePackages(new ArrayList<>());
			List<RentalCell> rentalSiteRules = findRentalSiteRules(cmd.getResourceType(),cmd.getSiteId(), dateSF.get().format(new java.util.Date(start.getTimeInMillis())),
					beginTime, cmd.getRentalType() == null ? RentalType.DAY.getCode() : cmd.getRentalType(), DateLength.DAY,
					RentalSiteStatus.NORMAL.getCode(), rule.getRentalStartTimeFlag());
			if (null != rentalSiteRules && rentalSiteRules.size() > 0) {
				for (RentalCell rsr : rentalSiteRules) {
					RentalSiteRulesDTO dto = ConvertHelper.convert(rsr, RentalSiteRulesDTO.class);
					dto.setSiteNumber(String.valueOf(rsr.getResourceNumber()));
					dto.setId(rsr.getId());
					//根据场景设置价格
					setRentalsiteRulePrice(dto,rsr,cmd.getPackageName(),priceRule,pricePackages);

					dto.setRuleDate(rsr.getResourceRentalDate().getTime());
					//dto.setStatus(SiteRuleStatus.OPEN.getCode());

					// 支持复选，要换一种方式计算剩余数量
					calculateAvailableCount(dto, rs, rsr, priceRules);
					dto.setResourceCounts(rsr.getCounts());
					//根据时间判断来设置status
					setRentalCellStatus(reserveTime, dto, rsr, rule);

					if (dto.getCounts() == 0 ) {
						dto.setStatus(SiteRuleStatus.CLOSE.getCode());
					}

					//如果多个模式，那么其它模式关的，当前模式对应时间也要关闭
					if (SiteRuleStatus.fromCode(dto.getStatus()) == SiteRuleStatus.OPEN && priceRules.size() > 1) {
						calculateCurrentStatus(dto, rs, rsr, priceRules);
					}
					setRentalSiteRulesDTOExtraInfo(dto);
					if (siteNumberMap.get(dto.getSiteNumber()) == null)
						siteNumberMap.put(dto.getSiteNumber(), new ArrayList<>());
					siteNumberMap.get(dto.getSiteNumber()).add(dto);

				}
			}

			for (String siteNumber : response.getSiteNames()) {
				RentalSiteNumberRuleDTO siteNumberRuleDTO = new RentalSiteNumberRuleDTO();
				siteNumberRuleDTO.setSiteNumber(siteNumber);
				siteNumberRuleDTO.setSiteRules(siteNumberMap.get(siteNumber));
				dayDto.getSiteNumbers().add(siteNumberRuleDTO);
			}
		}
		//每日开放时间
		response.setOpenTimes(getResourceOpenTime(rs.getResourceType(),rs.getId(),cmd.getRentalType(),","));
		cellList.get().clear();
		return response;
	}

	@Override
	public FindAutoAssignRentalSiteYearStatusResponse findAutoAssignRentalSiteYearStatus(
			FindAutoAssignRentalSiteYearStatusCommand cmd) {

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}

		java.util.Date reserveTime = new java.util.Date();

		RentalResource rs = rentalCommonService.getRentalResource(cmd.getResourceType(), cmd.getSiteId());
		currentSceneType.set(cmd.getSceneType());
//		RentalResource rs = this.rentalv2Provider.getRentalSiteById(cmd.getSiteId());

		RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(null, null,
				rs.getResourceType(), rs.getResourceTypeId(), RuleSourceType.RESOURCE.getCode(), rs.getId());

//		correctRetalResource(rs, cmd.getRentalType());
		processCells(rs, cmd.getRentalType());
		FindAutoAssignRentalSiteYearStatusResponse response = ConvertHelper.convert(rs, FindAutoAssignRentalSiteYearStatusResponse.class);
		//场所数量和编号
		response.setAutoAssign(rs.getAutoAssign());
		response.setMultiUnit(rs.getMultiUnit());
		response.setSiteCounts(rs.getResourceCounts());
		List<RentalResourceNumber> resourceNumbers = this.rentalv2Provider.queryRentalResourceNumbersByOwner(cmd.getResourceType(),
				EhRentalv2Resources.class.getSimpleName(), rs.getId());
		if (null != resourceNumbers) {
			response.setSiteNames(new ArrayList<>());
			for (RentalResourceNumber number : resourceNumbers) {
				response.getSiteNames().add(number.getResourceNumber());
			}
		}
		response.setRentalSiteId(rs.getId());
		List<RentalResourcePic> pics = this.rentalv2Provider.findRentalSitePicsByOwnerTypeAndId(cmd.getResourceType(),
				EhRentalv2Resources.class.getSimpleName(), rs.getId());
		response.setSitePics(convertRentalSitePicDTOs(pics));

		response.setAnchorTime(0L);

		List<RentalConfigAttachment> attachments = this.rentalv2Provider.queryRentalConfigAttachmentByOwner(rs.getResourceType(),
				EhRentalv2Resources.class.getSimpleName(), rs.getId(), null);
		response.setAttachments(convertAttachments(attachments));

		//设置优惠信息
		PriceRuleDTO ruleDto = processPriceCut(cmd.getSiteId(),rs,  cmd.getRentalType(),cmd.getPackageName());
		response.setFullPrice(ruleDto.getFullPrice());
		response.setCutPrice(ruleDto.getCutPrice());
		response.setDiscountType(ruleDto.getDiscountType());
		response.setDiscountRatio(ruleDto.getDiscountRatio());

		// 查rules

		java.util.Date nowTime = new java.util.Date();
//		response.setContactNum(rs.getContactPhonenum());
		Timestamp beginTime = new Timestamp(nowTime.getTime()
				+ rule.getRentalStartTime());
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.setTime(new Date(cmd.getRuleDate()));
		end.setTime(new Date(cmd.getRuleDate()));
		start.set(Calendar.MONTH,
				start.getActualMinimum(Calendar.MONTH));
		initToMonthFirstDay(start);
		end.set(Calendar.MONTH,
				start.getActualMinimum(Calendar.MONTH));
		initToMonthFirstDay(end);
		end.add(Calendar.YEAR, 1);
		response.setSiteDays(new ArrayList<>());

		List<Rentalv2PriceRule> priceRules = rentalv2PriceRuleProvider.listPriceRuleByOwner(rs.getResourceType(),
				PriceRuleType.RESOURCE.getCode(), rs.getId());
		Rentalv2PriceRule priceRule = rentalv2PriceRuleProvider.findRentalv2PriceRuleByOwner(rs.getResourceType(),
				PriceRuleType.RESOURCE.getCode(), rs.getId(),cmd.getRentalType());
		//填充价格分类
		priceRule.setPriceClassification(rentalv2Provider.listClassification(rs.getResourceType(),EhRentalv2PriceRules.class.getSimpleName(),
				priceRule.getId(),null,null,null,null));
		List<Rentalv2PricePackage> pricePackages = rentalv2PricePackageProvider.listPricePackageByOwner(rs.getResourceType(),
				PriceRuleType.RESOURCE.getCode(), rs.getId(), cmd.getRentalType(), cmd.getPackageName());
		//填充套餐价格分类
		if (pricePackages != null && pricePackages.size() > 0)
			pricePackages.forEach(r->r.setPriceClassification(rentalv2Provider.listClassification(rs.getResourceType(),EhRentalv2PricePackages.class.getSimpleName(),
					r.getId(),null,null,null,null)));
		for (; start.before(end); start.add(Calendar.MONTH, 1)) {
			RentalSiteNumberDayRulesDTO dayDto = new RentalSiteNumberDayRulesDTO();
			response.getSiteDays().add(dayDto);
			Map<String, List<RentalSiteRulesDTO>> siteNumberMap = new HashMap<>();
			for (RentalResourceNumber resourceNumber : resourceNumbers) {
				siteNumberMap.put(resourceNumber.getResourceNumber(), new ArrayList<>());
			}
			dayDto.setSiteNumbers(new ArrayList<>());
			dayDto.setRentalDate(start.getTimeInMillis());
			dayDto.setSitePackages(new ArrayList<>());
			List<RentalCell> rentalSiteRules = findRentalSiteRules(cmd.getResourceType(),cmd.getSiteId(), dateSF.get().format(new java.util.Date(start.getTimeInMillis())),
					beginTime, cmd.getRentalType() == null ? RentalType.DAY.getCode() : cmd.getRentalType(), DateLength.DAY,
					RentalSiteStatus.NORMAL.getCode(), rule.getRentalStartTimeFlag());
			// 查sitebills
			if (null != rentalSiteRules && rentalSiteRules.size() > 0) {
				for (RentalCell rsr : rentalSiteRules) {
					RentalSiteRulesDTO dto = ConvertHelper.convert(rsr, RentalSiteRulesDTO.class);
					dto.setSiteNumber(String.valueOf(rsr.getResourceNumber()));
					dto.setId(rsr.getId());
					//根据场景设置价格
					setRentalsiteRulePrice(dto,rsr,cmd.getPackageName(),priceRule,pricePackages);
					dto.setRuleDate(rsr.getResourceRentalDate().getTime());
					//dto.setStatus(SiteRuleStatus.OPEN.getCode());

					// 支持复选，要换一种方式计算剩余数量
					calculateAvailableCount(dto, rs, rsr, priceRules);
					dto.setResourceCounts(rsr.getCounts());
					//根据时间判断来设置status
					setRentalCellStatus(reserveTime, dto, rsr, rule);

					if (dto.getCounts() == 0 ) {
						dto.setStatus(SiteRuleStatus.CLOSE.getCode());
					}

					//如果多个模式，那么其它模式关的，当前模式对应时间也要关闭
					if (SiteRuleStatus.fromCode(dto.getStatus()) == SiteRuleStatus.OPEN && priceRules.size() > 1) {
						calculateCurrentStatus(dto, rs, rsr, priceRules);
					}
					setRentalSiteRulesDTOExtraInfo(dto);
					if (siteNumberMap.get(dto.getSiteNumber()) == null)
						siteNumberMap.put(dto.getSiteNumber(), new ArrayList<>());
					siteNumberMap.get(dto.getSiteNumber()).add(dto);
				}
			}

			//
			for (String siteNumber : response.getSiteNames()) {
				RentalSiteNumberRuleDTO siteNumberRuleDTO = new RentalSiteNumberRuleDTO();
				siteNumberRuleDTO.setSiteNumber(siteNumber);
				siteNumberRuleDTO.setSiteRules(siteNumberMap.get(siteNumber));
				dayDto.getSiteNumbers().add(siteNumberRuleDTO);
			}

		}
		//每日开放时间
		response.setOpenTimes(getResourceOpenTime(rs.getResourceType(),rs.getId(),cmd.getRentalType(),","));
		cellList.get().clear();
		return response;
	}

	@Override
	public FindAutoAssignRentalSiteDayStatusResponse findAutoAssignRentalSiteDayStatus(
			FindAutoAssignRentalSiteDayStatusCommand cmd) {

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}
		java.util.Date reserveTime = new java.util.Date();

		RentalResource rs = rentalCommonService.getRentalResource(cmd.getResourceType(), cmd.getSiteId());
		currentSceneType.set(cmd.getSceneType());

		RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(null, null,
				rs.getResourceType(), rs.getResourceTypeId(), RuleSourceType.RESOURCE.getCode(), rs.getId());

//		correctRetalResource(rs, cmd.getRentalType());
		processCells(rs, cmd.getRentalType());
		FindAutoAssignRentalSiteDayStatusResponse response = ConvertHelper.convert(rule, FindAutoAssignRentalSiteDayStatusResponse.class);
		response.setRentalSiteId(rs.getId());
		List<RentalResourcePic> pics = this.rentalv2Provider.findRentalSitePicsByOwnerTypeAndId(cmd.getResourceType(),
				EhRentalv2Resources.class.getSimpleName(), rs.getId());
		response.setSitePics(convertRentalSitePicDTOs(pics));

		List<RentalConfigAttachment> attachments = this.rentalv2Provider.queryRentalConfigAttachmentByOwner(rs.getResourceType(),
				EhRentalv2Resources.class.getSimpleName(), rs.getId(), null);
		response.setAttachments(convertAttachments(attachments));

		//设置优惠信息
		PriceRuleDTO ruleDto = processPriceCut(cmd.getSiteId(),rs,  cmd.getRentalType(),cmd.getPackageName());
		response.setFullPrice(ruleDto.getFullPrice());
		response.setCutPrice(ruleDto.getCutPrice());
		response.setDiscountType(ruleDto.getDiscountType());
		response.setDiscountRatio(ruleDto.getDiscountRatio());

		response.setAnchorTime(0L);
		//当前时间+预定开始时间 即为可预订开始时间
		java.util.Date nowTime = new java.util.Date();
		Timestamp beginTime = new Timestamp(nowTime.getTime()
				+ rule.getRentalStartTime());
		response.setAutoAssign(rs.getAutoAssign());
		response.setMultiUnit(rs.getMultiUnit());
		response.setSiteCounts(rs.getResourceCounts());
		List<RentalResourceNumber> resourceNumbers = this.rentalv2Provider.queryRentalResourceNumbersByOwner(cmd.getResourceType(),
				EhRentalv2Resources.class.getSimpleName(), rs.getId());
		if (null != resourceNumbers) {
			response.setSiteNames(new ArrayList<>());
			for (RentalResourceNumber number : resourceNumbers) {
				response.getSiteNames().add(number.getResourceNumber());
			}
		}

		// 查rules
		Map<String, List<RentalSiteRulesDTO>> siteNumberMap = new HashMap<>();
		for (RentalResourceNumber resourceNumber : resourceNumbers) {
			siteNumberMap.put(resourceNumber.getResourceNumber(), new ArrayList<>());
		}
		response.setSiteNumbers(new ArrayList<>());
		//按小时预订的,给客户端找到每一个时间点
		List<RentalTimeInterval> timeIntervals = this.rentalv2Provider.queryRentalTimeIntervalByOwner(rs.getResourceType(),
				EhRentalv2Resources.class.getSimpleName(), rs.getId());
		List<Long> dayTimes = calculateOrdinateList(timeIntervals);

		List<RentalCell> rentalSiteRules = findRentalSiteRules(cmd.getResourceType(),cmd.getSiteId(), dateSF.get().format(new java.util.Date(cmd.getRuleDate())),
				beginTime, cmd.getRentalType() == null ? RentalType.DAY.getCode() : cmd.getRentalType(), DateLength.DAY,
				RentalSiteStatus.NORMAL.getCode(), rule.getRentalStartTimeFlag());
		// 查sitebills
		if (null != rentalSiteRules && rentalSiteRules.size() > 0) {
			List<Rentalv2PriceRule> priceRules = rentalv2PriceRuleProvider.listPriceRuleByOwner(rs.getResourceType(),
					PriceRuleType.RESOURCE.getCode(), rs.getId());
			Rentalv2PriceRule priceRule = rentalv2PriceRuleProvider.findRentalv2PriceRuleByOwner(rs.getResourceType(),
					PriceRuleType.RESOURCE.getCode(), rs.getId(),cmd.getRentalType());
			//填充价格分类
			priceRule.setPriceClassification(rentalv2Provider.listClassification(rs.getResourceType(),EhRentalv2PriceRules.class.getSimpleName(),
					priceRule.getId(),null,null,null,null));
			List<Rentalv2PricePackage> pricePackages = rentalv2PricePackageProvider.listPricePackageByOwner(rs.getResourceType(),
					PriceRuleType.RESOURCE.getCode(), rs.getId(), cmd.getRentalType(), cmd.getPackageName());
			//填充套餐价格分类
			if (pricePackages != null && pricePackages.size() > 0)
				pricePackages.forEach(r->r.setPriceClassification(rentalv2Provider.listClassification(rs.getResourceType(),EhRentalv2PricePackages.class.getSimpleName(),
						r.getId(),null,null,null,null)));

			for (RentalCell rsr : rentalSiteRules) {
				RentalSiteRulesDTO dto = ConvertHelper.convert(rsr, RentalSiteRulesDTO.class);

				//根据场景设置价格
				setRentalsiteRulePrice(dto,rsr,cmd.getPackageName(),priceRule,pricePackages);

				dto.setSiteNumber(String.valueOf(rsr.getResourceNumber()));
				dto.setId(rsr.getId());
				if (dto.getRentalType().equals(RentalType.HOUR.getCode())) {
					dto.setTimeStep(rsr.getTimeStep());
					dto.setBeginTime(rsr.getBeginTime().getTime());
					dto.setEndTime(rsr.getEndTime().getTime());
					multiPriceByHour(dto,rsr.getTimeStep());
					if (response.getAnchorTime().equals(0L)) {
						response.setAnchorTime(dto.getBeginTime());
					} else {
						try {
							if (timeSF.get().parse(timeSF.get().format(new java.util.Date(response.getAnchorTime()))).after(
									timeSF.get().parse(timeSF.get().format(new java.util.Date(dto.getBeginTime()))))) {
								response.setAnchorTime(dto.getBeginTime());
							}
						} catch (Exception e) {
							LOGGER.error("anchorTime error  dto = " + dto);
						}


					}
				} else if (dto.getRentalType() == RentalType.HALFDAY.getCode()
						|| dto.getRentalType() == RentalType.THREETIMEADAY.getCode()) {
					dto.setAmorpm(rsr.getAmorpm());
				}
				dto.setRuleDate(rsr.getResourceRentalDate().getTime());
				//dto.setStatus(SiteRuleStatus.OPEN.getCode());

				// 支持复选，要换一种方式计算剩余数量
				calculateAvailableCount(dto, rs, rsr, priceRules);
				dto.setResourceCounts(rsr.getCounts());

				//根据时间判断来设置status
				setRentalCellStatus(reserveTime, dto, rsr, rule);

				if (dto.getCounts() == 0 ) {
					dto.setStatus(SiteRuleStatus.CLOSE.getCode());
				}

				//如果多个模式，那么其它模式关的，当前模式对应时间也要关闭
				if (SiteRuleStatus.fromCode(dto.getStatus()) == SiteRuleStatus.OPEN && priceRules.size() > 1) {
					calculateCurrentStatus(dto, rs, rsr, priceRules);
				}
				setRentalSiteRulesDTOExtraInfo(dto);
				if (siteNumberMap.get(dto.getSiteNumber()) == null)
					siteNumberMap.put(dto.getSiteNumber(), new ArrayList<>());
				siteNumberMap.get(dto.getSiteNumber()).add(dto);
			}
		}

		//
		for (String siteNumber : response.getSiteNames()) {
			RentalSiteNumberRuleDTO siteNumberRuleDTO = new RentalSiteNumberRuleDTO();
			siteNumberRuleDTO.setSiteNumber(siteNumber);
			siteNumberRuleDTO.setSiteRules(siteNumberMap.get(siteNumber));
			response.getSiteNumbers().add(siteNumberRuleDTO);
		}
		Collections.sort(dayTimes);
 		response.setDayTimes(dayTimes);
		//每日开放时间
		response.setOpenTimes(getResourceOpenTime(rs.getResourceType(),rs.getId(),cmd.getRentalType(),","));
		//给出按半天预约的 区间信息
		if (cmd.getRentalType().equals(RentalType.HALFDAY.getCode()) || cmd.getRentalType().equals(RentalType.THREETIMEADAY.getCode())){
			List<RentalTimeInterval> halfTimeIntervals = rentalv2Provider.queryRentalTimeIntervalByOwner(rs.getResourceType(),
					RentalTimeIntervalOwnerType.RESOURCE_HALF_DAY.getCode(), rs.getId());
			setDefaultTimeIntervalName(halfTimeIntervals);
			response.setHalfDayTimeIntervals(halfTimeIntervals.stream().map(r->ConvertHelper.convert(r,TimeIntervalDTO.class)).collect(Collectors.toList()));
		}
		cellList.get().clear();
		return response;
	}

	private void setDefaultTimeIntervalName(List<RentalTimeInterval> halfTimeIntervals){
		if (halfTimeIntervals == null || halfTimeIntervals.size() == 0)
			return;
		if (halfTimeIntervals.get(0).getName() == null)
			halfTimeIntervals.get(0).setName("上午");
		if (halfTimeIntervals.size()>1 && halfTimeIntervals.get(1).getName() == null)
			halfTimeIntervals.get(1).setName("下午");
		if (halfTimeIntervals.size()>2 && halfTimeIntervals.get(2).getName() == null)
			halfTimeIntervals.get(2).setName("晚上");
	}

	private void calculateAvailableCount(RentalSiteRulesDTO dto, RentalResource rs, RentalCell rentalCell, List<Rentalv2PriceRule> priceRules) {
		Double rentedCount;
		if (priceRules.size() == 1) {
			// 如果只有一种规则，那就是当前单元格的规则，则按原来的方式计算
			rentedCount = rentalv2Provider.countRentalSiteBillBySiteRuleId(rentalCell.getId(),rs,rentalCell.getRentalType());
		} else {
			// 如果超过一种规则，则需要计算其它规则下是否已经占用了此资源（前方高能，即将进入一个极其复杂的方法）
			rentedCount = rentalv2Provider.countRentalSiteBillOfAllScene(rs, rentalCell, priceRules);
		}

		dto.setCounts(rentalCell.getCounts() - rentedCount < 0 ? 0 : rentalCell.getCounts() - rentedCount);
	}

	private void setRentalCellStatus(java.util.Date reserveTime, RentalSiteRulesDTO dto, RentalCell rsr, RentalDefaultRule rule) {
		if (rsr.getRentalType().equals(RentalType.HOUR.getCode())) {
			Long time = rsr.getBeginTime().getTime();
			//至多提前时间，当单元格时间减去至多提前时间，表示最早可以预订的时间
			if (NormalFlag.NEED.getCode() == rule.getRentalStartTimeFlag()) {
				time -= rule.getRentalStartTime();
				if (reserveTime.before(new java.util.Date(time))) {
					dto.setStatus(SiteRuleStatus.EARLY.getCode());
				}
			}
			//至少提前时间，当单元格时间减去至少提前时间，表示最晚可以预订的时间
			time = rsr.getBeginTime().getTime();
			if (NormalFlag.NEED.getCode() == rule.getRentalEndTimeFlag()) {
				time -= rule.getRentalEndTime();
			}
			if (reserveTime.after(new java.util.Date(time))) {
				dto.setStatus(SiteRuleStatus.LATE.getCode());
			}
		} else {

			Long dayBeginTime = 0L;
			if (rsr.getAmorpm() != null) {
				String halfOwnerType = RentalTimeIntervalOwnerType.RESOURCE_HALF_DAY.getCode();

				List<RentalTimeInterval> halfTimeIntervals = rentalv2Provider.queryRentalTimeIntervalByOwner(rule.getResourceType(),
						halfOwnerType, rsr.getRentalResourceId());
				//当有晚上时，如果查询出来的条数不是3，就有数据错误的问题，默认按顺序存和取
				int size = halfTimeIntervals.size();
				if (rsr.getAmorpm().equals(AmorpmFlag.AM.getCode())) {
					if (size >= 1) {
						dayBeginTime = (long) (halfTimeIntervals.get(0).getBeginTime() * 60 * 60 * 1000);
					}
				} else if (rsr.getAmorpm().equals(AmorpmFlag.PM.getCode())) {
					if (size >= 2) {
						dayBeginTime = (long) (halfTimeIntervals.get(1).getBeginTime() * 60 * 60 * 1000);
					}
				} else if (rsr.getAmorpm().equals(AmorpmFlag.NIGHT.getCode())) {
					if (size >= 3) {
						dayBeginTime = (long) (halfTimeIntervals.get(2).getBeginTime() * 60 * 60 * 1000);
					}
				}
			}

			Long time = rsr.getResourceRentalDate().getTime() + dayBeginTime;
			if (NormalFlag.NEED.getCode() == rule.getRentalStartTimeFlag()) {
				time -= rule.getRentalStartTime();
				if (reserveTime.before(new java.util.Date(time))) {
					dto.setStatus(SiteRuleStatus.EARLY.getCode());
				}
			}
			time = rsr.getResourceRentalDate().getTime() + dayBeginTime;
			if (NormalFlag.NEED.getCode() == rule.getRentalEndTimeFlag()) {
				time -= rule.getRentalEndTime();
			}
			if (reserveTime.after(new java.util.Date(time))) {
				dto.setStatus(SiteRuleStatus.LATE.getCode());
			}
		}
	}



	@Override
	public void exportRentalBills(ListRentalBillsCommand cmd, HttpServletResponse response) {
		if (cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4040040420L, cmd.getAppId(), null, cmd.getCurrentProjectId());//订单记录权限
		}
		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}

		RentalResourceHandler handler = rentalCommonService.getRentalResourceHandler(cmd.getResourceType());
		handler.exportRentalBills(cmd, response);
	}

	@Override
	public void exportRentalBills(SearchRentalOrdersCommand cmd, HttpServletResponse response) {
		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}
		RentalResourceHandler handler = rentalCommonService.getRentalResourceHandler(cmd.getResourceType());
		handler.exportRentalBills(cmd, response);
	}
	//    }


	@Override
	public GetResourceListAdminResponse getResourceList(GetResourceListAdminCommand cmd) {
		if (cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4040040410L, cmd.getAppId(), null, cmd.getCurrentProjectId());//资源管理权限
		}
		GetResourceListAdminResponse response = new GetResourceListAdminResponse();
		if (null == cmd.getOrganizationId())
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_LOST_PARAMETER,
					"Invalid organizationId parameter in the command");
		if (null == cmd.getResourceTypeId())
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_LOST_PARAMETER,
					"Invalid launchPadItemId parameter in the command");

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}

		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());


		List<RentalResource> rentalSites = rentalv2Provider.findRentalSites(cmd.getResourceTypeId(), null,
				locator, pageSize+1,null, null, cmd.getOwnerId());
		if(null == rentalSites)
			return response;

		Long nextPageAnchor = null;
		if (rentalSites.size() > pageSize) {
			rentalSites.remove(rentalSites.size() - 1);
			nextPageAnchor = rentalSites.get(rentalSites.size() - 1).getDefaultOrder();
		}
		response.setNextPageAnchor(nextPageAnchor);
		response.setRentalSites(rentalSites.stream().map(r -> convertRentalSite2DTO(r,  false))
				.collect(Collectors.toList()));

		return response;
	}

	@Override
	public GetResourceListAdminResponse listResourceAbstract(GetResourceListAdminCommand cmd) {
		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}

		GetResourceListAdminResponse response = new GetResourceListAdminResponse();
		List<Long> siteIds = null;
		if (cmd.getOwnerType() != null && cmd.getOwnerId() != null) {
			siteIds = new ArrayList<>();
			List<RentalSiteRange> siteOwners = this.rentalv2Provider.findRentalSiteOwnersByOwnerTypeAndId(cmd.getResourceType(),
					cmd.getOwnerType(), cmd.getOwnerId());
			if (siteOwners != null)
				for (RentalSiteRange siteOwner : siteOwners) {
					siteIds.add(siteOwner.getRentalResourceId());
				}
		}
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<RentalResource> rentalSites = rentalv2Provider.findRentalSites(cmd.getResourceTypeId(), null,
				locator, pageSize+1,cmd.getStatus(), siteIds, cmd.getCommunityId());
		if(null == rentalSites)
			return response;
		Long nextPageAnchor = null;
		if (rentalSites.size() > pageSize) {
			rentalSites.remove(rentalSites.size() - 1);
			nextPageAnchor = rentalSites.get(rentalSites.size() - 1).getDefaultOrder();
		}
		response.setNextPageAnchor(nextPageAnchor);
		response.setRentalSites(rentalSites.stream().map(r -> {
			RentalSiteDTO dto = new RentalSiteDTO();
			dto.setSiteName(r.getResourceName());
			dto.setRentalSiteId(r.getId());
			return dto;
		}).collect(Collectors.toList()));
		return response;
	}

	@Override
	public RentalSiteDTO findRentalSiteById(FindRentalSiteByIdCommand cmd) {
		if (null == cmd.getId())
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_LOST_PARAMETER,
					"Invalid organizationId parameter in the command");

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}

		RentalResource rentalSite = rentalCommonService.getRentalResource(cmd.getResourceType(), cmd.getId());

		currentSceneType.set(cmd.getSceneType());
		RentalSiteDTO rentalSiteDTO = convertRentalSite2DTO(rentalSite, true);
		//退款提示
		RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(null, null,
				rentalSite.getResourceType(), rentalSite.getResourceTypeId(), RuleSourceType.RESOURCE.getCode(), rentalSite.getId());
		rentalSiteDTO.setRefundTip(getRefundTipByRule(rule,rentalSite.getId()));
		//帮客户端处理一下 颠倒按月按周预约规则的位置
		List<SitePriceRuleDTO> sitePriceRules = rentalSiteDTO.getSitePriceRules();
		int i = 0;
		for (;i<sitePriceRules.size();i++)
			if (RentalType.MONTH.getCode() == sitePriceRules.get(i).getRentalType())
				break;
		int j = 0;
		for (;j<sitePriceRules.size();j++)
			if (RentalType.WEEK.getCode() == sitePriceRules.get(j).getRentalType())
				break;
		if (i<sitePriceRules.size() && j<sitePriceRules.size()){
			sitePriceRules.add(sitePriceRules.get(i));
			sitePriceRules.remove(i);
		}
		return rentalSiteDTO;
	}

	@Override
	public void addResource(AddResourceAdminCommand cmd) {
		if (cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4040040410L, cmd.getAppId(), null, cmd.getCurrentProjectId());//资源管理权限
		}
		if (null == cmd.getOrganizationId()) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_LOST_PARAMETER,
					"Invalid organizationId parameter in the command");
		}
		if (null == cmd.getCommunityId()) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_LOST_PARAMETER,
					"Invalid getCommunityId parameter in the command");
		}
		if (null == cmd.getResourceTypeId()) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_LOST_PARAMETER,
					"Invalid launchPadItemId parameter in the command");
		}

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}

		RentalResourceType type = rentalv2Provider.getRentalResourceTypeById(cmd.getResourceTypeId());
		if (PayMode.OFFLINE_PAY.getCode() == type.getPayMode()
				&& (null == cmd.getOfflineCashierAddress() || null == cmd.getOfflinePayeeUid())) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_LOST_PARAMETER,
					"Neither offline cashier address nor payee uid can  be null");
		}

		this.dbProvider.execute((TransactionStatus status) -> {
			RentalResource resource = ConvertHelper.convert(cmd, RentalResource.class);
			resource.setResourceName(cmd.getSiteName());
			resource.setStatus(RentalSiteStatus.NORMAL.getCode());
			//设置资源时 默认为1
			resource.setResourceCounts(1.0);
			resource.setAutoAssign((byte) 0);
			resource.setMultiUnit((byte) 0);
			if (resource.getPeopleSpec() != null)
				resource.setSpec(resource.getPeopleSpec() + "人");

			rentalv2Provider.createRentalSite(resource);
			//添加资源时，才添加场所编号 TODO:新的对接需要添加
//			setRentalRuleSiteNumbers(resource.getResourceType(), EhRentalv2Resources.class.getSimpleName(), resource.getId(), cmd.getSiteNumbers());
			//resource.setSiteNumbers(cmd.getOwners());
			//新增资源规则
			createResourceRule(resource);
			//资源范围
			createSiteOwners(cmd.getOwners(), resource);
			//资源图片
			createSiteDetailUris(cmd.getDetailUris(), resource);
			//文件附件
			createSiteFileUris(cmd.getFileUris(),resource);
			return null;
		});
	}

	private void createResourceRule(RentalResource resource) {

		QueryDefaultRuleAdminCommand cmd2 = new QueryDefaultRuleAdminCommand();
		//查询默认规则，创建资源规则
		cmd2.setResourceTypeId(resource.getResourceTypeId());
		cmd2.setResourceType(resource.getResourceType());
		cmd2.setOwnerType(RentalOwnerType.COMMUNITY.getCode());
		cmd2.setOwnerId(resource.getCommunityId());
		//cmd2.setSourceType(RuleSourceType.DEFAULT.getCode());
		QueryDefaultRuleAdminResponse rule = this.queryDefaultRule(cmd2);

		if (null == rule) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_DEFAULT_RULE_NOT_FOUND,
					"Rule not found!");
		}
		//单元格开始的时间为创建资源的时间
		rule.setBeginDate(System.currentTimeMillis());

		//添加资源规则
		AddDefaultRuleAdminCommand addRuleCmd = ConvertHelper.convert(rule, AddDefaultRuleAdminCommand.class);
		addRuleCmd.setSourceType(RuleSourceType.RESOURCE.getCode());
		addRuleCmd.setSourceId(resource.getId());
		addRuleCmd.setOwnerType(RentalOwnerType.ORGANIZATION.getCode());
		addRuleCmd.setOwnerId(resource.getOrganizationId());
		addRule(addRuleCmd);
	}

	private void createResourceCells(List<PriceRuleDTO> priceRules) {
		for (PriceRuleDTO priceRule : priceRules) {
			priceRule.setCellBeginId(0l);
			priceRule.setCellEndId(0l);
		}
	}

	private List<AddRentalSiteSingleSimpleRule> createAddRuleParams(PriceRuleDTO priceRule, QueryDefaultRuleAdminResponse rule,
																	RentalResource resource) {
		List<AddRentalSiteSingleSimpleRule> addSingleRules = new ArrayList<>();

		BigDecimal workdayPrice = priceRule.getWorkdayPrice() == null ? new BigDecimal(0) : priceRule.getWorkdayPrice();
		BigDecimal initiatePrice = priceRule.getInitiatePrice() == null ? new BigDecimal(0) : priceRule.getInitiatePrice();

		if (priceRule.getRentalType().equals(RentalType.HOUR.getCode())) {

			List<TimeIntervalDTO> timeIntervals = rule.getTimeIntervals();
			if (timeIntervals != null) {
				//设置时间的每天的最早开始时间，和结束时间
				checkCreateCellsParamForHour(timeIntervals);
				for (TimeIntervalDTO timeInterval : timeIntervals) {

					if (timeInterval.getBeginTime() == null || timeInterval.getEndTime() == null || null == timeInterval.getTimeStep()) {
						continue;
					}

					AddRentalSiteSingleSimpleRule singleCmd = ConvertHelper.convert(rule, AddRentalSiteSingleSimpleRule.class);
					singleCmd.setRentalSiteId(resource.getId());
					singleCmd.setRentalType(priceRule.getRentalType());
					singleCmd.setPriceType(priceRule.getPriceType());
					singleCmd.setBeginTime(timeInterval.getBeginTime());
					singleCmd.setEndTime(timeInterval.getEndTime());
					singleCmd.setTimeStep(timeInterval.getTimeStep());
					singleCmd.setWorkdayPrice(workdayPrice);
					singleCmd.setInitiatePrice(initiatePrice);
					singleCmd.setOriginalPrice(priceRule.getOriginalPrice());

					singleCmd.setSiteCounts(resource.getResourceCounts());
					singleCmd.setAutoAssign(resource.getAutoAssign());
					singleCmd.setMultiUnit(resource.getMultiUnit());
					singleCmd.setSiteNumbers(resource.getSiteNumbers());
					singleCmd.setSiteCounts(resource.getResourceCounts());
					singleCmd.setUserPriceType(priceRule.getUserPriceType());
					addSingleRules.add(singleCmd);
				}
			}
		} else {
			AddRentalSiteSingleSimpleRule singleCmd = ConvertHelper.convert(rule, AddRentalSiteSingleSimpleRule.class);
			singleCmd.setRentalSiteId(resource.getId());
			singleCmd.setRentalType(priceRule.getRentalType());
			singleCmd.setPriceType(priceRule.getPriceType());
			singleCmd.setWorkdayPrice(workdayPrice);
			singleCmd.setInitiatePrice(initiatePrice);
			singleCmd.setOriginalPrice(priceRule.getOriginalPrice());

			singleCmd.setSiteCounts(resource.getResourceCounts());
			singleCmd.setAutoAssign(resource.getAutoAssign());
			singleCmd.setMultiUnit(resource.getMultiUnit());
			singleCmd.setSiteNumbers(resource.getSiteNumbers());
			singleCmd.setSiteCounts(resource.getResourceCounts());
			singleCmd.setUserPriceType(priceRule.getUserPriceType());
			addSingleRules.add(singleCmd);
		}

		return addSingleRules;
	}

	private void checkCreateCellsParamForHour(List<TimeIntervalDTO> timeIntervals) {
		double initValue = 0.0;
		Double beginTime = initValue;
		Double endTime = initValue;
		for (TimeIntervalDTO timeInterval : timeIntervals) {
			if (beginTime == initValue || beginTime > timeInterval.getBeginTime()) {
				beginTime = timeInterval.getBeginTime();
			}
			if (endTime == initValue || endTime < timeInterval.getEndTime()) {
				endTime = timeInterval.getEndTime();
			}
		}

		if ((null != endTime && endTime > 24.0) || (null != beginTime && beginTime < 0.0)) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_INVALID_PARAMETER,
					"Invalid parameter of timeInterval endTime > 24 or beginTime < 0");
		}
		//TODO:
//		resource.setDayBeginTime(convertTime((long) (beginTime*1000*60*60L)));
//		resource.setDayEndTime(convertTime((long) (endTime*1000*60*60L)));
	}

	private void createSiteOwners(List<SiteOwnerDTO> ownerDTOS, RentalResource resource) {
		if (ownerDTOS != null) {
			for (SiteOwnerDTO dto : ownerDTOS) {
				RentalSiteRange siteOwner = ConvertHelper.convert(dto, RentalSiteRange.class);
				siteOwner.setRentalResourceId(resource.getId());
				siteOwner.setResourceType(resource.getResourceType());
				this.rentalv2Provider.createRentalSiteOwner(siteOwner);
			}
		}
	}

	private void createSiteDetailUris(List<String> detailUris, RentalResource resource) {
		if (detailUris != null) {
			for (String uri : detailUris) {
				RentalResourcePic detailPic = new RentalResourcePic();
				detailPic.setOwnerType(EhRentalv2Resources.class.getSimpleName());
				detailPic.setOwnerId(resource.getId());
				detailPic.setUri(uri);
				detailPic.setResourceType(resource.getResourceType());
				this.rentalv2Provider.createRentalSitePic(detailPic);
			}
		}
	}

	private void createSiteFileUris(List<RentalSiteFileDTO> fileUris, RentalResource resource) {
		if (fileUris != null) {
			for (RentalSiteFileDTO dto : fileUris) {
				RentalResourceFile file = new RentalResourceFile();
				file.setOwnerType(EhRentalv2Resources.class.getSimpleName());
				file.setOwnerId(resource.getId());
				file.setUri(dto.getUri());
				file.setName(dto.getName());
				file.setSize(dto.getSize());
				file.setResourceType(resource.getResourceType());
				this.rentalv2Provider.createRentalSiteFile(file);
			}
		}
	}

	private void createOrderFileUris(List<RentalSiteFileDTO> fileUris,RentalOrder order){
		if (fileUris != null) {
			for (RentalSiteFileDTO dto : fileUris) {
				RentalResourceFile file = new RentalResourceFile();
				file.setOwnerType(EhRentalv2Orders.class.getSimpleName());
				file.setOwnerId(order.getId());
				file.setUri(dto.getUri());
				file.setName(dto.getName());
				file.setSize(dto.getSize());
				file.setResourceType(order.getResourceType());
				this.rentalv2Provider.createRentalSiteFile(file);
			}
		}
	}

	@Override
	public void updateResource(UpdateResourceAdminCommand cmd) {

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}

		this.dbProvider.execute((TransactionStatus status) -> {

			RentalResource rentalSite = rentalCommonService.getRentalResource(cmd.getResourceType(), cmd.getId());

//			RentalResource rentalSite = this.rentalv2Provider.getRentalSiteById(cmd.getId());

			RentalResourceType type = this.rentalv2Provider.getRentalResourceTypeById(rentalSite.getResourceTypeId());

			if (PayMode.OFFLINE_PAY.getCode() == type.getPayMode() &&
					(null == cmd.getOfflineCashierAddress() ||
							null == cmd.getOfflinePayeeUid())) {
				throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
						RentalServiceErrorCode.ERROR_OFFLINE_PAY_UPDATE_RESOURCE_STATUS,
						"Neither offline cashier address nor payee uid can  be null");
			}

			//TODO: 权限校验
			rentalSite.setResourceName(cmd.getSiteName());
			rentalSite.setSpec(cmd.getSpec());
			rentalSite.setPeopleSpec(cmd.getPeopleSpec());
			if (rentalSite.getPeopleSpec() != null)
				rentalSite.setSpec(rentalSite.getPeopleSpec() + "人");
			rentalSite.setAddress(cmd.getAddress());
			rentalSite.setLatitude(cmd.getLatitude());
			rentalSite.setLongitude(cmd.getLongitude());
			if (cmd.getCommunityId() != null)
				rentalSite.setCommunityId(cmd.getCommunityId());
			rentalSite.setContactPhonenum(cmd.getContactPhonenum());
			rentalSite.setIntroduction(cmd.getIntroduction());
			rentalSite.setNotice(cmd.getNotice());
			rentalSite.setChargeUid(cmd.getChargeUid());
			rentalSite.setCoverUri(cmd.getCoverUri());
			rentalSite.setStatus(cmd.getStatus());
			rentalSite.setOfflineCashierAddress(cmd.getOfflineCashierAddress());
			rentalSite.setOfflinePayeeUid(cmd.getOfflinePayeeUid());
			rentalSite.setConfirmationPrompt(cmd.getConfirmationPrompt());
			rentalSite.setAclinkId(cmd.getAclinkId());
			rentalv2Provider.updateRentalSite(rentalSite);
			this.rentalv2Provider.deleteRentalSitePicsBySiteId(rentalSite.getResourceType(), cmd.getId());
			this.rentalv2Provider.deleteRentalSiteFilesBySiteId(rentalSite.getResourceType(), cmd.getId());
			this.rentalv2Provider.deleteRentalSiteOwnersBySiteId(rentalSite.getResourceType(), cmd.getId());
			//先删除
//			rentalv2Provider.deleteRentalResourceNumbersByOwnerId(rentalSite.getResourceType(), EhRentalv2Resources.class.getSimpleName(), rentalSite.getId());

			createSiteOwners(cmd.getOwners(), rentalSite);
			createSiteDetailUris(cmd.getDetailUris(), rentalSite);
			createSiteFileUris(cmd.getFileUris(),rentalSite);

			return null;
		});
	}

	@Override
	public void updateResourceStatus(UpdateResourceAdminCommand cmd) {
		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}
		RentalResource rentalSite = rentalCommonService.getRentalResource(cmd.getResourceType(), cmd.getId());
		rentalSite.setStatus(cmd.getStatus());
		rentalv2Provider.updateRentalSite(rentalSite);
	}

	@Override
	public void updateItem(UpdateItemAdminCommand cmd) {
		if (null == cmd.getItemType())
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_LOST_PARAMETER,
					"Invalid ItemType   parameter in the command : null");
		RentalItem siteItem = this.rentalv2Provider.getRentalSiteItemById(cmd.getId());

		siteItem.setDefaultOrder(cmd.getDefaultOrder());
		siteItem.setImgUri(cmd.getImgUri());
		siteItem.setItemType(cmd.getItemType());
		siteItem.setName(cmd.getItemName());
		siteItem.setPrice(cmd.getItemPrice());
		siteItem.setDescription(cmd.getDescription());
		siteItem.setCounts(cmd.getCounts());
		rentalv2Provider.updateRentalSiteItem(siteItem);
	}

	@Override
	public void updateItems(UpdateItemsAdminCommand cmd) {
		this.dbProvider.execute((TransactionStatus status) -> {
			for (SiteItemDTO dto : cmd.getItemDTOs()) {
				UpdateItemAdminCommand cmd2 = ConvertHelper.convert(dto, UpdateItemAdminCommand.class);
				updateItem(cmd2);
			}

			return null;
		});
	}

	private void addItems(List<SiteItemDTO> siteItems, Long sourceId, String sourceType, String resourceType) {
		if (siteItems != null)
			this.dbProvider.execute((TransactionStatus status) -> {
				siteItems.stream().forEach(r -> {
					AddItemAdminCommand cmd = ConvertHelper.convert(r, AddItemAdminCommand.class);
					cmd.setResourceType(resourceType);
					cmd.setSourceType(sourceType);

					cmd.setSourceId(sourceId);
					this.addItem(cmd);
				});
				return null;
			});
	}

	private void updateRSRs(List<RentalCell> changeRentalSiteRules, UpdateRentalSiteCellRuleAdminCommand cmd) {
		if (null == changeRentalSiteRules || changeRentalSiteRules.size() == 0)
			return;
		for (RentalCell cell : changeRentalSiteRules) {
			cell.setPrice(cmd.getPrice());
			cell.setInitiatePrice(cmd.getInitiatePrice());
			cell.setOriginalPrice(cmd.getOriginalPrice());
			cell.setStatus(cmd.getStatus());
			if (cmd.getCounts() != null)
				cell.setCounts(cmd.getCounts());
			cell.setPricePackageId(cmd.getSitePackageId());
			cell.setResourceType(cmd.getResourceType());
			cell.setUserPriceType(cmd.getUserPriceType());
			RentalCell dbCell = this.rentalv2Provider.getRentalCellById(cell.getId(),cmd.getResourceId(),
                    cell.getRentalType(),cmd.getResourceType());
			if (null == dbCell) {
				this.rentalv2Provider.createRentalSiteRule(cell);
				createCellClassification(cell,cmd.getClassifications());
			}
			else {
				this.rentalv2Provider.updateRentalSiteRule(cell);
				this.rentalv2Provider.deleteClassificationByOwnerId(cell.getResourceType(),EhRentalv2Cells.class.getSimpleName(),dbCell.getCellId());
				createCellClassification(cell,cmd.getClassifications());
			}
		}
	}

	private void createCellClassification(RentalCell cell,List<RentalPriceClassificationDTO> classifications){
		if (classifications == null || classifications.size() == 0)
			return;
		for (RentalPriceClassificationDTO dto : classifications){
			RentalPriceClassification classification = ConvertHelper.convert(dto,RentalPriceClassification.class);
			classification.setNamespaceId(UserContext.getCurrentNamespaceId());
			classification.setResourceType(cell.getResourceType());
			classification.setOwnerType(EhRentalv2Cells.class.getSimpleName());
			classification.setOwnerId(cell.getCellId());
			classification.setSourceType(PriceRuleType.RESOURCE.getCode());
			classification.setSourceId(cell.getRentalResourceId());
			rentalv2PriceRuleProvider.createRentalv2PriceClassification(classification);
		}
	}

	@Override
	public void updateRentalSiteCellRule(UpdateRentalSiteCellRuleAdminCommand cmd) {

		if (null == cmd.getRuleId()) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_LOST_PARAMETER,
					"Invalid ruleId parameter in the command");
		}

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}

		RentalResource rs = rentalCommonService.getRentalResource(cmd.getResourceType(), cmd.getResourceId());

		if (rs == null) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERRPR_LOST_RESOURCE_RULE,
					"rental resource (site) cannot found ");
		}

//		correctRetalResource(rs, cmd.getRentalType());
		//创建单元格
		processCells(rs, cmd.getRentalType());

		RentalCell choseRSR = findRentalSiteRuleById(cmd.getRuleId(),rs.getId(), cmd.getRentalType(),cmd.getResourceType());
		if (null == choseRSR) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERRPR_LOST_RESOURCE_RULE,
					"Invalid ruleId parameter in the command");
		}

//		if(null!=rs.getAutoAssign() && rs.getAutoAssign().equals(NormalFlag.NEED.getCode())){
//			cmd.setCounts(1.0);
//		}
		if (cmd.getSitePackages() != null && !cmd.getSitePackages().isEmpty()) {
			Long sitePackageId = createCellPricePackage(cmd.getSitePackages(), rs.getResourceType());
			cmd.setSitePackageId(sitePackageId);
		}

		this.dbProvider.execute((TransactionStatus status) -> {
			List<RentalCell> changeRentalSiteRules = null;
			Long cellDayBeginTime = choseRSR.getBeginTime().getTime();
			Long cellDayEndTime = choseRSR.getEndTime().getTime();
			if (cmd.getWholeDayFlag() != null && TrueOrFalseFlag.TRUE.getCode() == cmd.getWholeDayFlag()){
				cellDayBeginTime = choseRSR.getResourceRentalDate().getTime();
				cellDayEndTime = choseRSR.getResourceRentalDate().getTime() + 24*3600*1000 - 1;
			}
			if (cmd.getLoopType().equals(LoopType.ONLYTHEDAY.getCode())) {
				//当天的
				if (cmd.getRentalType().equals(RentalType.HOUR.getCode())) {
					//按小时
					changeRentalSiteRules = findRentalSiteRuleByDate(rs.getResourceType(), choseRSR.getRentalResourceId(), choseRSR.getResourceNumber(),
							new Timestamp(cellDayBeginTime), new Timestamp(cellDayEndTime),
							null, dateSF.get().format(choseRSR.getResourceRentalDate()), cmd.getRentalType());
				} else {
					changeRentalSiteRules = Collections.singletonList(choseRSR);
				}

				updateRSRs(changeRentalSiteRules, cmd);
			} else {
				//需要循环的
				Calendar chooseCalendar = Calendar.getInstance();
				Calendar start = Calendar.getInstance();
				Calendar end = Calendar.getInstance();
				chooseCalendar.setTime(new Date(choseRSR.getResourceRentalDate().getTime()));

				start.setTime(new Date(cmd.getBeginDate()));
				end.setTime(new Date(cmd.getEndDate()));

				for (; !start.after(end); start.add(Calendar.DAY_OF_MONTH, 1)) {
					Integer weekday = start.get(Calendar.DAY_OF_WEEK);
					Integer monthDay = start.get(Calendar.DAY_OF_MONTH);
					//按周循环的,如果不对就继续循环
					if (cmd.getLoopType().equals(LoopType.EVERYWEEK.getCode()) &&
							!weekday.equals(chooseCalendar.get(Calendar.DAY_OF_WEEK)))
						continue;
					//按月循环的，如果不对就继续循环
					if (cmd.getLoopType().equals(LoopType.EVERYMONTH.getCode()) &&
							!monthDay.equals(chooseCalendar.get(Calendar.DAY_OF_MONTH)))
						continue;
					//每天循环的
					if (cmd.getRentalType().equals(RentalType.HOUR.getCode())) {
						//按小时
						Timestamp beginTime = new Timestamp(start.getTime().getTime() + getDayTime(cellDayBeginTime));
						Timestamp endTime = new Timestamp(start.getTime().getTime() + getDayTime(cellDayEndTime));
						changeRentalSiteRules = findRentalSiteRuleByDate(rs.getResourceType(), choseRSR.getRentalResourceId(), choseRSR.getResourceNumber(), beginTime, endTime,
								null, null, cmd.getRentalType());
					} else if (cmd.getRentalType().equals(RentalType.HALFDAY.getCode()) ||
							cmd.getRentalType().equals(RentalType.THREETIMEADAY.getCode())) {
						List<Byte> ampmList = new ArrayList<>();
						//0早上1下午2晚上
						ampmList.add(choseRSR.getAmorpm());
						changeRentalSiteRules = findRentalSiteRuleByDate(rs.getResourceType(), choseRSR.getRentalResourceId(), choseRSR.getResourceNumber(), null, null,
								ampmList, dateSF.get().format(new java.util.Date(start.getTimeInMillis())), cmd.getRentalType());
					} else if (cmd.getRentalType().equals(RentalType.DAY.getCode())) {

						changeRentalSiteRules = findRentalSiteRuleByDate(rs.getResourceType(), choseRSR.getRentalResourceId(), choseRSR.getResourceNumber(), null, null,
								null, dateSF.get().format(new java.util.Date(start.getTimeInMillis())), cmd.getRentalType());
					} else if (cmd.getRentalType().equals(RentalType.MONTH.getCode())) {
						// TODO
						changeRentalSiteRules = findRentalSiteRuleByDate(rs.getResourceType(), choseRSR.getRentalResourceId(), choseRSR.getResourceNumber(), null, null,
								null, dateSF.get().format(new java.util.Date(start.getTimeInMillis())), cmd.getRentalType());
					} else if (cmd.getRentalType().equals(RentalType.WEEK.getCode())) {
						changeRentalSiteRules = findRentalSiteRuleByDate(rs.getResourceType(), choseRSR.getRentalResourceId(), choseRSR.getResourceNumber(), null, null,
								null, dateSF.get().format(new java.util.Date(start.getTimeInMillis())), cmd.getRentalType());
					}
					updateRSRs(changeRentalSiteRules, cmd);
				}
			}

			return null;
		});

		cellList.get().clear();
	}

	//输入时间戳 返回现在是当天的几时几分几秒(毫秒数)
	private Long getDayTime(Long timeStamp) {
		SimpleDateFormat trueDay = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return timeStamp - trueDay.parse(trueDay.format(new Date(timeStamp))).getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0l;
	}

	private Long createCellPricePackage(List<PricePackageDTO> pricePackages, String resourceType) {
		Rentalv2PricePackage rentalv2PricePackage = new Rentalv2PricePackage();
		rentalv2PricePackage.setOwnerType(PriceRuleType.CELL.getCode());
		rentalv2PricePackage.setOwnerId(0L);
		rentalv2PricePackage.setName(pricePackages.get(0).getName());
		rentalv2PricePackage.setRentalType(pricePackages.get(0).getRentalType());
		rentalv2PricePackage.setResourceType(resourceType);
		rentalv2PricePackage.setUserPriceType(pricePackages.get(0).getUserPriceType());
		Long id = rentalv2PricePackageProvider.createRentalv2PricePackage(rentalv2PricePackage);

		createPricePackages(resourceType, PriceRuleType.CELL, id, pricePackages);
		return id;
	}
//	private Rentalv2PriceRule correctRetalResource(RentalResource rs, Byte rentalType) {
//		Rentalv2PriceRule priceRule = null;
//		if (rentalType != null) {
//			priceRule = rentalv2PriceRuleProvider.findRentalv2PriceRuleByOwner(PriceRuleType.RESOURCE.getCode(), rs.getId(), rentalType);
//		}else {
//			List<Rentalv2PriceRule> priceRules = rentalv2PriceRuleProvider.listPriceRuleByOwner(PriceRuleType.RESOURCE.getCode(), rs.getId());
//			if (priceRules.size() > 0) {
//				priceRule = priceRules.get(0);
//			}
//		}
//
//		return priceRule;
////		if (priceRule != null) {
////			rs.setRentalType(priceRule.getRentalType());
////			rs.setWorkdayPrice(priceRule.getWorkdayPrice());
////			rs.setWeekendPrice(priceRule.getWeekendPrice());
////			rs.setOrgMemberWeekendPrice(priceRule.getOrgMemberWeekendPrice());
////			rs.setOrgMemberWorkdayPrice(priceRule.getOrgMemberWorkdayPrice());
////			rs.setApprovingUserWeekendPrice(priceRule.getApprovingUserWeekendPrice());
////			rs.setApprovingUserWorkdayPrice(priceRule.getApprovingUserWorkdayPrice());
////			rs.setDiscountType(priceRule.getDiscountType());
////			rs.setFullPrice(priceRule.getFullPrice());
////			rs.setCutPrice(priceRule.getCutPrice());
////			rs.setDiscountRatio(priceRule.getDiscountRatio());
////			rs.setCellBeginId(priceRule.getCellBeginId());
////			rs.setCellEndId(priceRule.getCellEndId());
////		}
//	}

	private RentalCell findRentalSiteRuleById(Long ruleId,Long resourceId,Byte rentalType,String resourceType) {

		for (RentalCell cell : cellList.get()) {
			if (cell.getId().equals(ruleId)) {
				RentalCell dbCell = this.rentalv2Provider.getRentalCellById(cell.getId(),resourceId,rentalType,resourceType);
				if (null != dbCell)
					cell = dbCell;
				return cell;
			}
		}
		return null;
	}

	@Override
	public GetRefundOrderListResponse getRefundOrderList(GetRefundOrderListCommand cmd) {

		GetRefundOrderListResponse response = new GetRefundOrderListResponse();
		if (null == cmd.getResourceTypeId())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid resourceTypeId parameter in the command");

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}

		if (cmd.getPageAnchor() == null)
			cmd.setPageAnchor(Long.MAX_VALUE);
		Integer pageSize = PaginationConfigHelper.getPageSize(
				configurationProvider, cmd.getPageSize());
//		checkEnterpriseCommunityIdIsNull(cmd.getOwnerId());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		String vendorType = null;
		if (null != cmd.getVendorType())
			vendorType = VendorType.fromCode(cmd.getVendorType()).getStyleNo();
		List<RentalRefundOrder> orders = rentalv2Provider.getRefundOrderList(cmd.getResourceType(), cmd.getResourceTypeId(),
				locator, cmd.getStatus(), vendorType, pageSize + 1, cmd.getStartTime(), cmd.getEndTime());
		if (orders == null || orders.size() == 0)
			return response;
		if (orders.size() > pageSize) {
			orders.remove(orders.size() - 1);
			response.setNextPageAnchor(orders.get(orders.size() - 1).getId());
		}
		response.setRefundOrders(new ArrayList<>());
		orders.forEach((order) -> {
			RefundOrderDTO dto = ConvertHelper.convert(order, RefundOrderDTO.class);
			dto.setVendorType(VendorType.fromStyleNo(order.getOnlinePayStyleNo()).getVendorType());
			User applyer = this.userProvider.findUserById(order.getCreatorUid());
			if (null != applyer) {
				dto.setApplyUserName(applyer.getNickName());

				UserIdentifier userIdentifier = this.userProvider.findClaimedIdentifierByOwnerAndType(order.getCreatorUid(), IdentifierType.MOBILE.getCode());
				if (null == userIdentifier) {
					LOGGER.debug("userIdentifier is null...userId = " + order.getCreatorUid());
				} else {
					dto.setApplyUserContact(userIdentifier.getIdentifierToken());
				}

			} else {
				LOGGER.error("user not found userId = " + order.getCreatorUid());
			}
			RentalOrder rentalOrder = this.rentalv2Provider.findRentalBillById(order.getOrderId());
			if (null != rentalOrder) {
				dto.setUseDetail(rentalOrder.getUseDetail());
				dto.setRentalCount(rentalOrder.getRentalCount());
			} else {
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
		if (null == cmd.getBillId()) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_CANNOT_FIND_ORDER,
					"Invalid parameter : bill id is null");
		}
		RentalOrder bill = this.rentalv2Provider.findRentalBillById(cmd.getBillId());
		if (null == bill) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_CANNOT_FIND_ORDER,
					"Invalid parameter : bill id can not find bill");
		}
		RentalBillDTO dto = processOrderDTO(bill);
		if (dto.getStatus().equals(SiteBillStatus.FAIL.getCode()) && dto.getPaidPrice()!=null && dto.getPaidPrice().compareTo(new BigDecimal(0))>0)
			dto.setStatus(SiteBillStatus.FAIL_PAID.getCode());
		return dto;
	}

	@Override
	public String getRefundUrl(GetRefundUrlCommand cmd) {
		if (null == cmd.getRefundId())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid parameter : refund order  id is null");
		RentalRefundOrder refundOrder = this.rentalv2Provider.getRentalRefundOrderById(cmd.getRefundId());
		if (null == refundOrder)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid parameter : refund order id  can not find refund order ");
		if (refundOrder.getOnlinePayStyleNo().equals(VendorType.WEI_XIN.getStyleNo()))
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_REFUND_ERROR, "refund order is wechat");
		PayZuolinRefundCommand refundCmd = new PayZuolinRefundCommand();
		String refoundApi = this.configurationProvider.getValue(UserContext.getCurrentNamespaceId(), "pay.zuolin.refound", "");
		String appKey = configurationProvider.getValue(UserContext.getCurrentNamespaceId(), "pay.appKey", "");
		refundCmd.setAppKey(appKey);
		Long timestamp = System.currentTimeMillis();
		refundCmd.setTimestamp(timestamp);
		Integer randomNum = (int) (Math.random() * 1000);
		refundCmd.setNonce(randomNum);
		refundCmd.setRefundOrderNo(String.valueOf(refundOrder.getRefundOrderNo()));
		refundCmd.setOrderNo(String.valueOf(refundOrder.getOrderNo()));
		refundCmd.setOnlinePayStyleNo(refundOrder.getOnlinePayStyleNo());
		refundCmd.setOrderType(OrderType.OrderTypeEnum.RENTALORDER.getPycode());
		//已付金额乘以退款比例除以100
		refundCmd.setRefundAmount(refundOrder.getAmount());
		refundCmd.setRefundMsg("预订单取消退款");
		rentalCommonService.setSignatureParam(refundCmd);
		PayZuolinRefundResponse refundResponse = (PayZuolinRefundResponse) rentalCommonService.restCall(refoundApi,
				refundCmd, PayZuolinRefundResponse.class);
		if (refundResponse.getErrorCode().equals(HttpStatus.OK.value())) {
			return refundResponse.getResponse();
		} else {
			LOGGER.error("refund order no =[" + refundOrder.getRefundOrderNo() + "] refound error param is " + refundCmd.toString());
			if (null != refundResponse.getErrorDetails()) {
				throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_REFUND_ERROR,
						refundResponse.getErrorDetails());
			} else {
				throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_REFUND_ERROR,
						"refund order error");
			}

		}

	}

	@Override
	public GetResourceTypeListResponse getResourceTypeList(GetResourceTypeListCommand cmd) {
		if (null == cmd.getNamespaceId()) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid namespaceId parameter in the command");
		}

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}

		GetResourceTypeListResponse response = new GetResourceTypeListResponse();
		if (cmd.getPageAnchor() == null)
			cmd.setPageAnchor(Long.MAX_VALUE);
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());

		List<RentalResourceType> resourceTypes = this.rentalv2Provider.findRentalResourceTypes(cmd.getNamespaceId(),
				cmd.getMenuType(), cmd.getResourceType(), locator);
		if (null == resourceTypes) {
			return response;
		}

		Long nextPageAnchor = null;
		if (resourceTypes.size() > pageSize) {
			resourceTypes.remove(resourceTypes.size() - 1);
			nextPageAnchor = resourceTypes.get(resourceTypes.size() - 1).getId();
		}
		response.setNextPageAnchor(nextPageAnchor);
		response.setResourceTypes(new ArrayList<>());
		resourceTypes.forEach((type) -> {
			ResourceTypeDTO dto = ConvertHelper.convert(type, ResourceTypeDTO.class);
			dto.setIconUrl(this.contentServerService.parserUri(dto.getIconUri(),
					EntityType.USER.getCode(), UserContext.current().getUser().getId()));
			response.getResourceTypes().add(dto);
		});
		return response;
	}

	@Override
	public ResourceTypeDTO getResourceType(GetResourceTypeCommand cmd) {
		return ConvertHelper.convert(this.rentalv2Provider.findRentalResourceTypeById(cmd.getId()), ResourceTypeDTO.class);
	}

	//	@Override
//	public void createResourceType(CreateResourceTypeCommand cmd) {
//		cmd.setStatus(ResourceTypeStatus.DISABLE.getCode());
//		RentalResourceType rsType = ConvertHelper.convert(cmd, RentalResourceType.class);
//		this.rentalv2Provider.createRentalResourceType(rsType);
//
//	}
//
//	@Override
//	public void deleteResourceType(DeleteResourceTypeCommand cmd) {
//		// TODO 图标也要删除
//		this.rentalv2Provider.deleteRentalResourceType(cmd.getId());
//
//	}
//
//	@Override
//	public void updateResourceType(UpdateResourceTypeCommand cmd) {
//		//  更新type不更新status
//		RentalResourceType rsType = this.rentalv2Provider.getRentalResourceTypeById(cmd.getId());
//		rsType.setIconUri(cmd.getIconUri());
//		rsType.setName(cmd.getName());
//		rsType.setPageType(cmd.getPageType());
//		this.rentalv2Provider.updateRentalResourceType(rsType);
//	}

//	@Override
//	public void closeResourceType(CloseResourceTypeCommand cmd) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void openResourceType(OpenResourceTypeCommand cmd) {
//		// TODO Auto-generated method stub
//
//	}

	@Override
	public void deleteResource(DeleteResourceCommand cmd) {

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}

		rentalv2Provider.deleteRentalCellsByResourceId(cmd.getResourceType(), cmd.getId());

		RentalResource rs = rentalCommonService.getRentalResource(cmd.getResourceType(), cmd.getId());

//		RentalResource rs = rentalv2Provider.getRentalSiteById(cmd.getId());
		if (rs == null) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERRPR_LOST_RESOURCE_RULE,
					"Invalid namespaceId parameter in the command");
		}
		rs.setStatus(RentalSiteStatus.DELETED.getCode());
		rentalv2Provider.updateRentalSite(rs);

	}

//	@Override
//	public void addCheckOperator(AddCheckOperatorCommand cmd) {
//
//		List<Long> privilegeIds = new ArrayList<>();
//		privilegeIds.add(PrivilegeConstants.RENTAL_CHECK);
//		rolePrivilegeService.assignmentPrivileges(EntityType.ORGANIZATIONS.getCode(),cmd.getOrganizationId(),
//				EntityType.USER.getCode(),cmd.getUserId(),RentalServiceErrorCode.SCOPE,privilegeIds);
//
//	}
//
//	@Override
//	public void deleteCheckOperator(AddCheckOperatorCommand cmd) {
//
//
//	}

	@Override
	public void updateRentalDate(UpdateRentalDateCommand cmd) {

		if (null == cmd.getBeginDate() || null == cmd.getEndDate()) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"begin or end date can not be null");
		}

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}

		RentalResource rs = rentalCommonService.getRentalResource(cmd.getResourceType(), cmd.getRentalSiteId());

//		RentalResource rs = this.rentalv2Provider.getRentalSiteById(cmd.getRentalSiteId());

		UpdateDefaultDateRuleAdminCommand updateRuleCmd = ConvertHelper.convert(cmd, UpdateDefaultDateRuleAdminCommand.class);
		updateRuleCmd.setSourceType(RuleSourceType.RESOURCE.getCode());
		updateRuleCmd.setSourceId(rs.getId());
		updateRuleCmd.setResourceTypeId(rs.getResourceTypeId());
		updateRuleCmd.setResourceType(rs.getResourceType());
		updateDefaultDateRule(updateRuleCmd);


//		this.dbProvider.execute((TransactionStatus status) -> {
//
//			rs.setBeginDate(new Date(cmd.getBeginDate()));
//			rs.setEndDate(new Date(cmd.getEndDate()));
//			//modify by wh 2016-11-11 修改时间点和修改操作人的记录
//			rs.setOperatorUid(UserContext.current().getUser().getId());
//			rs.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//
//			rs.setOpenWeekday(convertOpenWeekday(cmd.getOpenWeekday()));
//			this.rentalv2Provider.deleteRentalCloseDatesByOwnerId(EhRentalv2Resources.class.getSimpleName(), rs.getId());
//			//close dates
//			setRentalRuleCloseDates(cmd.getCloseDates(), rs.getId(), EhRentalv2Resources.class.getSimpleName());
//
//			this.rentalv2Provider.updateRentalSite(rs);
//			return null;
//		});

	}


	@Override
	public void updateResourceAttachment(UpdateResourceAttachmentCommand cmd) {

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}
		String ownerType = null;
		Long ownerId = null;
		if (RuleSourceType.DEFAULT.getCode().equals(cmd.getSourceType())) {
			ownerType = EhRentalv2DefaultRules.class.getSimpleName();
			RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(cmd.getOwnerType(), cmd.getOwnerId(),
					cmd.getResourceType(), cmd.getResourceTypeId(), cmd.getSourceType(), cmd.getSourceId());
			ownerId = rule.getId();
		} else if (RuleSourceType.RESOURCE.getCode().equals(cmd.getSourceType())) {
			RentalResource rs = rentalCommonService.getRentalResource(cmd.getResourceType(), cmd.getSourceId());
			ownerType = EhRentalv2Resources.class.getSimpleName();
			ownerId = rs.getId();
		}

		this.rentalv2Provider.deleteRentalConfigAttachmentsByOwnerId(cmd.getResourceType(),
				ownerType, ownerId, cmd.getAttachment().getAttachmentType());
		List<AttachmentConfigDTO> list = new ArrayList<>();
		list.add(cmd.getAttachment());
		createRentalConfigAttachment(list, ownerId, ownerType, cmd.getResourceType());

	}

	@Override
	public void updateDefaultAttachmentRule(UpdateDefaultAttachmentRuleAdminCommand cmd) {

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}

		RentalDefaultRule defaultRule = this.rentalv2Provider.getRentalDefaultRule(cmd.getOwnerType(), cmd.getOwnerId(),
				cmd.getResourceType(), cmd.getResourceTypeId(), RuleSourceType.DEFAULT.getCode(), null);
		if (null == defaultRule) {
			throw RuntimeErrorException
					.errorWith(RentalServiceErrorCode.SCOPE,
							RentalServiceErrorCode.ERROR_DEFAULT_RULE_NOT_FOUND, "didnt have default rule!");
		}
		this.dbProvider.execute((TransactionStatus status) -> {

			this.rentalv2Provider.deleteRentalConfigAttachmentsByOwnerId(cmd.getResourceType(),
					EhRentalv2DefaultRules.class.getSimpleName(), defaultRule.getId(), null);
			//config attachments

			createRentalConfigAttachment(cmd.getAttachments(), defaultRule.getId(), EhRentalv2DefaultRules.class.getSimpleName(),
					cmd.getResourceType());
			return null;
		});
	}

	private void createRentalConfigAttachment(List<AttachmentConfigDTO> attachments, Long ownerId, String ownerType, String resourceType) {
		if (null != attachments) {
			attachments.forEach(a -> {

				RentalConfigAttachment rca = ConvertHelper.convert(a, RentalConfigAttachment.class);
				rca.setOwnerType(ownerType);
				rca.setOwnerId(ownerId);
				rca.setResourceType(resourceType);
				this.rentalv2Provider.createRentalConfigAttachment(rca);

				if (a.getAttachmentType().equals(AttachmentType.GOOD_ITEM.getCode())) {
					List<RentalGoodItem> goodItems = a.getGoodItems();
					addGoodItems(goodItems, AttachmentType.GOOD_ITEM.name(), rca.getId(), resourceType);
				} else if (a.getAttachmentType().equals(AttachmentType.RECOMMEND_USER.getCode())) {
					List<RentalRecommendUser> recommendUsers = a.getRecommendUsers();
					addRecommendUsers(recommendUsers, AttachmentType.RECOMMEND_USER.name(), rca.getId(), resourceType);
				}
			});
		}
	}

	private void addGoodItems(List<RentalGoodItem> goodItems, String ownerType, Long ownerId, String resourceType) {
		if (null != goodItems) {
			goodItems.forEach(g -> {
				RentalConfigAttachment gg = ConvertHelper.convert(g, RentalConfigAttachment.class);
				gg.setOwnerType(ownerType);
				gg.setOwnerId(ownerId);
				gg.setAttachmentType(AttachmentType.GOOD_ITEM.getCode());
				gg.setMustOptions(NormalFlag.NONEED.getCode());
				gg.setResourceType(resourceType);
				gg.setDefaultOrder(g.getDefaultOrder());
				this.rentalv2Provider.createRentalConfigAttachment(gg);
			});
		}
	}

	private void addRecommendUsers(List<RentalRecommendUser> recommendUsers, String ownerType, Long ownerId, String resourceType) {
		if (null != recommendUsers) {
			recommendUsers.forEach(u -> {
				RentalConfigAttachment uu = ConvertHelper.convert(u, RentalConfigAttachment.class);
				uu.setOwnerType(ownerType);
				uu.setOwnerId(ownerId);
				uu.setAttachmentType(AttachmentType.RECOMMEND_USER.getCode());
				uu.setMustOptions(NormalFlag.NONEED.getCode());
				uu.setResourceType(resourceType);
				uu.setDefaultOrder(u.getDefaultOrder());
				this.rentalv2Provider.createRentalConfigAttachment(uu);
			});
		}
	}

	@Override
	public void updateResourceOrder(UpdateResourceOrderAdminCommand cmd) {
		if (null == cmd.getDefaultOrder()) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_LOST_PARAMETER,
					"Invalid id parameter in the command");
		}

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}
		for (DefaultOrderDTO dto : cmd.getDefaultOrder()){
			RentalResource resource = rentalCommonService.getRentalResource(cmd.getResourceType(), dto.getId());
			if (null == resource) {
				LOGGER.error("RentalResource not found, cmd={}", cmd);
				throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERRPR_LOST_RESOURCE_RULE,
						"RentalResource not found");
			}
			resource.setDefaultOrder(dto.getDefaultOrderId());
			rentalv2Provider.updateRentalSite(resource);

		}
	}

	@Override
	public void updateResourceTimeRule(UpdateResourceTimeRuleCommand cmd) {

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}


		RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(cmd.getOwnerType(), cmd.getOwnerId(),
				cmd.getResourceType(), cmd.getResourceTypeId(), cmd.getSourceType(), cmd.getSourceId());

		if (null == rule) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_DEFAULT_RULE_NOT_FOUND, "RentalDefaultRule not found");
		}

		if (null != cmd.getOpenTimes() && !cmd.getOpenTimes().isEmpty()) {
			Optional<RentalOpenTimeDTO> optional = cmd.getOpenTimes().stream().filter(r -> r.getRentalType() == RentalType.DAY.getCode())
					.findFirst();
			if (optional.isPresent()) {
				RentalOpenTimeDTO openTimeDTO = optional.get();
				rule.setDayOpenTime(openTimeDTO.getDayOpenTime());
				rule.setDayCloseTime(openTimeDTO.getDayCloseTime());
			}
		}

		BeanUtils.copyProperties(cmd, rule,"ownerType","ownerId");
		rule.setBeginDate(new Date(System.currentTimeMillis()));//重置开始时间

		this.dbProvider.execute((TransactionStatus status) -> {

			this.rentalv2Provider.updateRentalDefaultRule(rule);

			String priceRuleType = null;
			String ownerType = null;
			String halfOwnerType = null;
			Long id = null;
			if (RuleSourceType.DEFAULT.getCode().equals(rule.getSourceType())) {
				priceRuleType = PriceRuleType.DEFAULT.getCode();
				ownerType = EhRentalv2DefaultRules.class.getSimpleName();
				halfOwnerType = RentalTimeIntervalOwnerType.DEFAULT_HALF_DAY.getCode();
				id = rule.getId();
			} else if (RuleSourceType.RESOURCE.getCode().equals(rule.getSourceType())) {
				priceRuleType = PriceRuleType.RESOURCE.getCode();
				ownerType = EhRentalv2Resources.class.getSimpleName();
				halfOwnerType = RentalTimeIntervalOwnerType.RESOURCE_HALF_DAY.getCode();
				id = rule.getSourceId();
			}

			// set half day time intervals
			//先删除
			rentalv2Provider.deleteTimeIntervalsByOwnerId(rule.getResourceType(), halfOwnerType, id);
			setRentalRuleTimeIntervals(rule.getResourceType(), halfOwnerType, id, cmd.getHalfDayTimeIntervals());

			//time intervals
			//先删除
			this.rentalv2Provider.deleteTimeIntervalsByOwnerId(rule.getResourceType(), ownerType, id);
			setRentalRuleTimeIntervals(rule.getResourceType(), ownerType, id, cmd.getTimeIntervals());

			//设置关闭日期close dates
			rentalv2Provider.deleteRentalCloseDatesByOwnerId(rule.getResourceType(), ownerType, id);
			setRentalRuleCloseDates(cmd.getCloseDates(), id, ownerType, rule.getResourceType());

			//设置每天开启关闭时间
			rentalv2Provider.deleteRentalDayopenTimeByOwnerId(rule.getResourceType(), ownerType, id);
			setRentalDayopenTime(cmd.getOpenTimes(), ownerType, id, rule.getResourceType());

			//先删除后添加
			List<Rentalv2PriceRule> prices = rentalv2PriceRuleProvider.listPriceRuleByOwner(rule.getResourceType(), priceRuleType, id);
			if (prices.isEmpty()) {
				List<PriceRuleDTO> priceRules = buildDefaultPriceRule(cmd.getRentalTypes());
				rentalv2PriceRuleProvider.deletePriceRuleByOwnerId(rule.getResourceType(), priceRuleType, id);
				createPriceRules(rule.getResourceType(), PriceRuleType.fromCode(priceRuleType), id, priceRules);
			} else {
				//当价格已经设置过之后，在设置时间界面的 预约类型，注意要同步价格和套餐 预约类型
				prices.forEach(r -> r.setPriceClassification(rentalv2Provider.listClassification(r.getResourceType(),EhRentalv2PriceRules.class.getSimpleName(),
						r.getId(),null,null,null,null)));
				List<PriceRuleDTO> priceRules = new ArrayList<>();
				cmd.getRentalTypes().forEach(r -> {
					Optional<Rentalv2PriceRule> optional = prices.stream().filter(p -> p.getRentalType().equals(r)).findFirst();
					if (optional.isPresent()) {
						Rentalv2PriceRule temp = optional.get();
						priceRules.add(convert(temp));
					} else {
						priceRules.add(createInitPriceRuleDTO(r));
					}
				});
				rentalv2PriceRuleProvider.deletePriceRuleByOwnerId(rule.getResourceType(), priceRuleType, id);
				rentalv2Provider.deleteClassificationBySourceId(rule.getResourceType(),priceRuleType,id);
				rentalv2PricePackageProvider.deletePricePackageByRentalTypes(rule.getResourceType(), priceRuleType, id, cmd.getRentalTypes());

				createPriceRules(rule.getResourceType(), PriceRuleType.fromCode(priceRuleType), id, priceRules);

			}

			if (RuleSourceType.RESOURCE.getCode().equals(cmd.getSourceType())) {
				RentalResource rs = rentalCommonService.getRentalResource(cmd.getResourceType(), cmd.getSourceId());
				updateResourceRule(rs, true);
			}
			return null;
		});
	}

	@Override
	public ResourceTimeRuleDTO getResourceTimeRule(GetResourceTimeRuleCommand cmd) {

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}

		QueryDefaultRuleAdminCommand queryRuleCmd = ConvertHelper.convert(cmd, QueryDefaultRuleAdminCommand.class);

		QueryDefaultRuleAdminResponse queryRule = queryDefaultRule(queryRuleCmd);
		return convertResourceTimeRuleDTO(queryRule, cmd.getSourceType());

	}

	private QueryDefaultRuleAdminResponse updateResourceRule(RentalResource rs, boolean updateCell) {
		QueryDefaultRuleAdminCommand queryRuleCmd = new QueryDefaultRuleAdminCommand();
		queryRuleCmd.setSourceType(RuleSourceType.RESOURCE.getCode());
		queryRuleCmd.setSourceId(rs.getId());
		queryRuleCmd.setResourceTypeId(rs.getResourceTypeId());
		queryRuleCmd.setResourceType(rs.getResourceType());

		QueryDefaultRuleAdminResponse queryRule = queryDefaultRule(queryRuleCmd);

		if (updateCell) {
			//设置新规则的时候就删除之前的旧单元格
			this.rentalv2Provider.deleteRentalCellsByResourceId(rs.getResourceType(), rs.getId());

			createResourceCells(queryRule.getPriceRules());
			//先删除后添加, 创建单元格之后在重新添加一次价格，存cellBeginId和cellEndId
			rentalv2PriceRuleProvider.deletePriceRuleByOwnerId(rs.getResourceType(), PriceRuleType.RESOURCE.getCode(), rs.getId());
			createPriceRules(rs.getResourceType(), PriceRuleType.RESOURCE, rs.getId(), queryRule.getPriceRules());
		}

		return queryRule;
	}

	private ResourceTimeRuleDTO convertResourceTimeRuleDTO(QueryDefaultRuleAdminResponse rule, String sourceType) {
		ResourceTimeRuleDTO dto = ConvertHelper.convert(rule, ResourceTimeRuleDTO.class);

		return dto;
	}

	@Override
	public void updateResourcePriceRule(UpdateResourcePriceRuleCommand cmd) {

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}

		RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(cmd.getOwnerType(), cmd.getOwnerId(),
				cmd.getResourceType(), cmd.getResourceTypeId(), cmd.getSourceType(), cmd.getSourceId());

		if (null == rule) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_DEFAULT_RULE_NOT_FOUND, "RentalDefaultRule not found");
		}


		BeanUtils.copyProperties(cmd, rule,"ownerType","ownerId");

		this.dbProvider.execute((TransactionStatus status) -> {

			this.rentalv2Provider.updateRentalDefaultRule(rule);

			String priceRuleType = null;
			Long ownerId = null;
			if (RuleSourceType.DEFAULT.getCode().equals(rule.getSourceType())) {
				priceRuleType = PriceRuleType.DEFAULT.getCode();
				ownerId = rule.getId();
			} else if (RuleSourceType.RESOURCE.getCode().equals(rule.getSourceType())) {
				priceRuleType = PriceRuleType.RESOURCE.getCode();
				ownerId = rule.getSourceId();
				//cellbeginid 和 cellendid拷贝
				List<Rentalv2PriceRule> priceRules = rentalv2PriceRuleProvider.listPriceRuleByOwner(rule.getResourceType(), priceRuleType, ownerId);
				cmd.getPriceRules().forEach(r -> {
					priceRules.forEach(g -> {
						if (r.getRentalType().equals(g.getRentalType())) {
							r.setCellBeginId(g.getCellBeginId());
							r.setCellEndId(g.getCellEndId());
						}
					});
				});

				//修改价格规则 覆盖掉单个单元格的设置
				this.rentalv2Provider.deleteRentalCellsByResourceId(rule.getResourceType(), rule.getSourceId());
			}

			//先删除后添加
			rentalv2Provider.deleteClassificationBySourceId(rule.getResourceType(),priceRuleType, ownerId);
			rentalv2PriceRuleProvider.deletePriceRuleByOwnerId(rule.getResourceType(), priceRuleType, ownerId);
			createPriceRules(rule.getResourceType(), PriceRuleType.fromCode(priceRuleType), ownerId, cmd.getPriceRules());
			//先删除后添加
			rentalv2PricePackageProvider.deletePricePackageByOwnerId(rule.getResourceType(), priceRuleType, ownerId);
			createPricePackages(rule.getResourceType(), PriceRuleType.fromCode(priceRuleType), ownerId, cmd.getPricePackages());

			return null;
		});
	}

	@Override
	public ResourcePriceRuleDTO getResourcePriceRule(GetResourcePriceRuleCommand cmd) {

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}

		RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(cmd.getOwnerType(), cmd.getOwnerId(),
				cmd.getResourceType(), cmd.getResourceTypeId(), cmd.getSourceType(), cmd.getSourceId());

		return convertResourcePriceRuleDTO(rule, cmd.getSourceType());
	}

	private ResourcePriceRuleDTO convertResourcePriceRuleDTO(RentalDefaultRule rule, String sourceType) {
		ResourcePriceRuleDTO dto = ConvertHelper.convert(rule, ResourcePriceRuleDTO.class);
		String priceRuleType = null;
		Long id = null;
		if (RuleSourceType.DEFAULT.getCode().equals(sourceType)) {
			priceRuleType = PriceRuleType.DEFAULT.getCode();
			id = rule.getId();
		} else if (RuleSourceType.RESOURCE.getCode().equals(sourceType)) {
			priceRuleType = PriceRuleType.RESOURCE.getCode();
			id = rule.getSourceId();
		}

		List<Rentalv2PriceRule> priceRules = rentalv2PriceRuleProvider.listPriceRuleByOwner(rule.getResourceType(), priceRuleType, id);
		dto.setPriceRules(priceRules.stream().map(this::convert).collect(Collectors.toList()));
		dto.setRentalTypes(priceRules.stream().map(Rentalv2PriceRule::getRentalType).collect(Collectors.toList()));

		List<Rentalv2PricePackage> pricePackages = rentalv2PricePackageProvider.listPricePackageByOwner(rule.getResourceType(),
				priceRuleType, id, null, null);
		dto.setPricePackages(pricePackages.stream().map(this::convert).collect(Collectors.toList()));
		dto.setClassification(new ArrayList<>());
		//按用户类型
		RentalPriceClassificationTitleDTO titleDTO = new RentalPriceClassificationTitleDTO();
		titleDTO.setUserPriceType(RentalUserPriceType.USER_TYPE.getCode());
		titleDTO.setLevels(new ArrayList<>());
		RentalPriceClassificationDTO classification = new RentalPriceClassificationDTO();
		classification.setUserPriceType(RentalUserPriceType.USER_TYPE.getCode());
		classification.setClassification(SceneType.ENTERPRISE.getCode());
		titleDTO.getLevels().add(classification);
		classification = ConvertHelper.convert(classification,RentalPriceClassificationDTO.class);
		classification.setClassification(SceneType.PM_ADMIN.getCode());
		titleDTO.getLevels().add(classification);
		classification = ConvertHelper.convert(classification,RentalPriceClassificationDTO.class);
		classification.setClassification(SceneType.PARK_TOURIST.getCode());
		titleDTO.getLevels().add(classification);
		dto.getClassification().add(titleDTO);
		//获取会员等级
		List<VipPriority> vipPriorities = userActivityService.listVipPriorityByNamespaceId(UserContext.getCurrentNamespaceId());
		if (vipPriorities != null){
			titleDTO = new RentalPriceClassificationTitleDTO();
			titleDTO.setUserPriceType(RentalUserPriceType.VIP_TYPE.getCode());
			titleDTO.setLevels(new ArrayList<>());
			for (VipPriority vipPriority : vipPriorities){
				RentalPriceClassificationDTO dto1 = new RentalPriceClassificationDTO();
				dto1.setUserPriceType(RentalUserPriceType.VIP_TYPE.getCode());
				dto1.setClassification(vipPriority.getVipLevelText());
				titleDTO.getLevels().add(dto1);
			}
			dto.getClassification().add(titleDTO);
		}
		return dto;
	}

	@Override
	public void updateResourceRentalRule(UpdateResourceRentalRuleCommand cmd) {

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}
		RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(cmd.getOwnerType(), cmd.getOwnerId(),
				cmd.getResourceType(), cmd.getResourceTypeId(), cmd.getSourceType(), cmd.getSourceId());

		if (null == rule) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_DEFAULT_RULE_NOT_FOUND, "RentalDefaultRule not found");
		}

		BeanUtils.copyProperties(cmd, rule,"ownerType","ownerId");
		rentalv2Provider.updateRentalDefaultRule(rule);

	}

	@Override
	public ResourceRentalRuleDTO getResourceRentalRule(GetResourceRentalRuleCommand cmd) {

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}

		RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(cmd.getOwnerType(), cmd.getOwnerId(),
				cmd.getResourceType(), cmd.getResourceTypeId(), cmd.getSourceType(), cmd.getSourceId());

		return ConvertHelper.convert(rule, ResourceRentalRuleDTO.class);
	}

	@Override
	public void updateResourceOrderRule(UpdateResourceOrderRuleCommand cmd) {

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}

		RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(cmd.getOwnerType(), cmd.getOwnerId(),
				cmd.getResourceType(), cmd.getResourceTypeId(), cmd.getSourceType(), cmd.getSourceId());

		if (null == rule) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_DEFAULT_RULE_NOT_FOUND, "RentalDefaultRule not found");
		}

		BeanUtils.copyProperties(cmd, rule,"ownerType","ownerId");

		this.dbProvider.execute((TransactionStatus status) -> {

			this.rentalv2Provider.updateRentalDefaultRule(rule);

			if (RuleSourceType.DEFAULT.getCode().equals(rule.getSourceType())) {
				cmd.setSourceId(rule.getId());
			}
			//先删除后添加
			rentalv2Provider.deleteRentalOrderRules(rule.getResourceType(), rule.getSourceType(), rule.getId(),
					RentalOrderHandleType.REFUND.getCode());
			createRentalOrderRules(rule.getResourceType(), rule.getSourceType(), rule.getId(), cmd.getRefundStrategies());
			//先删除后添加
			rentalv2Provider.deleteRentalOrderRules(rule.getResourceType(), rule.getSourceType(), rule.getId(),
					RentalOrderHandleType.OVERTIME.getCode());
			createRentalOrderRules(rule.getResourceType(), rule.getSourceType(), rule.getId(), cmd.getOvertimeStrategies());

			rentalv2Provider.deleteRefundTip(cmd.getResourceType(),cmd.getSourceType(),cmd.getSourceId());
			createRentalRefundTips(cmd.getResourceType(),cmd.getSourceType(),cmd.getSourceId(),cmd.getRefundTips());
			return null;
		});
	}

	private void createRentalRefundTips(String resourceType,String sourceType,Long sourceId,List<RentalRefundTipDTO> refundTips){
		if (refundTips != null && !refundTips.isEmpty()){
			refundTips.forEach(r->{
				RentalRefundTip tip = ConvertHelper.convert(r,RentalRefundTip.class);
				tip.setNamespaceId(UserContext.getCurrentNamespaceId());
				tip.setSourceId(sourceId);
				tip.setSourceType(sourceType);
				tip.setResourceType(resourceType);
				rentalv2Provider.createRefundTip(tip);
			});
		}
	}

	private void createRentalOrderRules(String resourceType, String ownerType, Long ownerId, List<RentalOrderRuleDTO> orderRules) {
		if (orderRules != null && !orderRules.isEmpty()) {

			orderRules.forEach(r -> {
				RentalOrderRule rule = ConvertHelper.convert(r, RentalOrderRule.class);
				rule.setOwnerId(ownerId);
				rule.setOwnerType(ownerType);
				rule.setResourceType(resourceType);
				rentalv2Provider.createRentalOrderRule(rule);
			});
		}
	}

	@Override
	public ResourceOrderRuleDTO getResourceOrderRule(GetResourceOrderRuleCommand cmd) {

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}

		RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(cmd.getOwnerType(), cmd.getOwnerId(),
				cmd.getResourceType(), cmd.getResourceTypeId(), cmd.getSourceType(), cmd.getSourceId());

		ResourceOrderRuleDTO dto = ConvertHelper.convert(rule, ResourceOrderRuleDTO.class);

		List<RentalOrderRule> refundRules = rentalv2Provider.listRentalOrderRules(rule.getResourceType(), rule.getSourceType(),
				rule.getId(), RentalOrderHandleType.REFUND.getCode());

		List<RentalOrderRule> overTimeRules = rentalv2Provider.listRentalOrderRules(rule.getResourceType(), rule.getSourceType(),
				rule.getId(), RentalOrderHandleType.OVERTIME.getCode());
		
        if (RuleSourceType.DEFAULT.getCode().equals(rule.getSourceType())) {
            cmd.setSourceId(rule.getId());
        }
		List<RentalRefundTip> refundTips = rentalv2Provider.listRefundTips(cmd.getResourceType(),cmd.getSourceType(),cmd.getSourceId(),
				null);

		dto.setRefundStrategies(refundRules.stream().map(r -> ConvertHelper.convert(r, RentalOrderRuleDTO.class)).collect(Collectors.toList()));
		dto.setOvertimeStrategies(overTimeRules.stream().map(r -> ConvertHelper.convert(r, RentalOrderRuleDTO.class)).collect(Collectors.toList()));
		dto.setRefundTips(refundTips.stream().map(r->ConvertHelper.convert(r,RentalRefundTipDTO.class)).collect(Collectors.toList()));
		return dto;
	}

	@Override
	public ResourceAttachmentDTO getResourceAttachment(GetResourceAttachmentCommand cmd) {
		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}
		String ownerType = null;
		Long ownerId = null;
		if (RuleSourceType.DEFAULT.getCode().equals(cmd.getSourceType())) {
			ownerType = EhRentalv2DefaultRules.class.getSimpleName();
			RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(cmd.getOwnerType(), cmd.getOwnerId(),
					cmd.getResourceType(), cmd.getResourceTypeId(), cmd.getSourceType(), cmd.getSourceId());
			ownerId = rule.getId();
		} else if (RuleSourceType.RESOURCE.getCode().equals(cmd.getSourceType())) {
			RentalResource rs = rentalCommonService.getRentalResource(cmd.getResourceType(), cmd.getSourceId());
			ownerType = EhRentalv2Resources.class.getSimpleName();
			ownerId = rs.getId();
		}
		List<RentalConfigAttachment> attachments = rentalv2Provider.queryRentalConfigAttachmentByOwner(cmd.getResourceType(),
				ownerType, ownerId, cmd.getAttachmentType());

		ResourceAttachmentDTO dto = new ResourceAttachmentDTO();
		dto.setAttachments(convertAttachments(attachments));
		return dto;
	}

	@Override
	public ResourceSiteNumbersDTO getResourceSiteNumbers(GetResourceSiteNumbersCommand cmd) {
		RentalResource rentalSite = rentalCommonService.getRentalResource(cmd.getResourceType(), cmd.getRentalSiteId());
		ResourceSiteNumbersDTO dto = new ResourceSiteNumbersDTO();
		dto.setRentalSiteId(rentalSite.getId());
		dto.setAutoAssign(rentalSite.getAutoAssign());
		dto.setMultiUnit(rentalSite.getMultiUnit());
		dto.setSiteCounts(rentalSite.getResourceCounts());

		if (rentalSite.getAutoAssign().equals(NormalFlag.NEED.getCode())) {
			List<RentalResourceNumber> resourceNumbers = this.rentalv2Provider.queryRentalResourceNumbersByOwner(
					rentalSite.getResourceType(), EhRentalv2Resources.class.getSimpleName(), rentalSite.getId());
			if (null != resourceNumbers) {
				dto.setSiteNumbers(new ArrayList<>());
				for (RentalResourceNumber number : resourceNumbers) {
					SiteNumberDTO dto2 = new SiteNumberDTO();
					dto2.setSiteNumber(number.getResourceNumber());
					dto2.setSiteNumberGroup(number.getNumberGroup());
					dto2.setGroupLockFlag(number.getGroupLockFlag());
					dto.getSiteNumbers().add(dto2);
				}
			}
		}
		return dto;
	}

	@Override
	public void updateResourceSiteNumbers(UpdateResourceSiteNumbersCommand cmd) {
		RentalResource rentalSite = rentalCommonService.getRentalResource(cmd.getResourceType(), cmd.getRentalSiteId());
		rentalSite.setMultiUnit(cmd.getMultiUnit());
		rentalSite.setAutoAssign(cmd.getAutoAssign());
		rentalSite.setResourceCounts(cmd.getSiteCounts());
		rentalv2Provider.updateRentalSite(rentalSite);

		//先删除
		rentalv2Provider.deleteRentalResourceNumbersByOwnerId(rentalSite.getResourceType(), EhRentalv2Resources.class.getSimpleName(), rentalSite.getId());
		//set site number
		setRentalRuleSiteNumbers(rentalSite.getResourceType(), EhRentalv2Resources.class.getSimpleName(), rentalSite.getId(), cmd.getSiteNumbers());
		//更新所有单元格
		updateResourceRule(rentalSite, true);
	}

	@Override
	public void confirmRefund(ConfirmRefundCommand cmd) {
		RentalOrder order = this.rentalv2Provider.findRentalBillById(cmd.getRentalBillId());
		if (SiteBillStatus.REFUNDING.getCode() == order.getStatus()) {
			order.setStatus(SiteBillStatus.REFUNDED.getCode());
			this.rentalv2Provider.updateRentalBill(order);
		}
	}

	@Override
	public AddRentalOrderUsingInfoResponse addRentalOrderUsingInfo(AddRentalOrderUsingInfoCommand cmd) {

		AddRentalBillItemCommand actualCmd = ConvertHelper.convert(cmd, AddRentalBillItemCommand.class);

		AddRentalBillItemCommandResponse tempResp =  actualAddRentalItemBill(actualCmd,
				ActivityRosterPayVersionFlag.V1);

		CommonOrderDTO dto = ConvertHelper.convert(tempResp, CommonOrderDTO.class);
		dto.setTotalFee(tempResp.getAmount());
		dto.setSubject(tempResp.getName());
		dto.setBody(tempResp.getDescription());

		AddRentalOrderUsingInfoResponse response = new AddRentalOrderUsingInfoResponse();
		response.setOrderDTO(dto);
		response.setFlowCaseUrl(tempResp.getFlowCaseUrl());
		response.setBillId(tempResp.getBillId());
		return response;
	}

	@Override
	public AddRentalOrderUsingInfoV2Response addRentalOrderUsingInfoV2(AddRentalOrderUsingInfoCommand cmd) {

		AddRentalBillItemCommand actualCmd = ConvertHelper.convert(cmd, AddRentalBillItemCommand.class);
		AddRentalBillItemCommandResponse responseV1 = actualAddRentalItemBill(actualCmd, ActivityRosterPayVersionFlag.V2);
		AddRentalBillItemV2Response tempResp = (AddRentalBillItemV2Response)createPayOrder(responseV1,actualCmd,ActivityRosterPayVersionFlag.V2);
		AddRentalOrderUsingInfoV2Response response = ConvertHelper.convert(tempResp, AddRentalOrderUsingInfoV2Response.class);
		return response;
	}

	@Override
	public ListRentalOrdersResponse listRentalOrders(ListRentalOrdersCommand cmd) {

		ListRentalOrdersResponse response = new ListRentalOrdersResponse();

		Long userId = UserContext.current().getUser().getId();

		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());

		ListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		List<Byte> list = new ArrayList<>();


		for (SiteBillStatus status : SiteBillStatus.values())
			if (status != SiteBillStatus.OWING_FEE)
				list.add(status.getCode());
		List<RentalOrder> billList = this.rentalv2Provider.listRentalBills(null, userId, cmd.getRentalSiteId(), cmd.getResourceType(),
				cmd.getResourceTypeId(), locator, pageSize + 1, list, null);

		if (null == billList)
			return response;
		if (cmd.getPageAnchor() == null) {//第一页 把欠费订单拖到第一位
			list = new ArrayList<>();
			list.add(SiteBillStatus.OWING_FEE.getCode());
			List<RentalOrder> billList2 = this.rentalv2Provider.listRentalBills(null, userId, cmd.getRentalSiteId(), cmd.getResourceType(),
					cmd.getResourceTypeId(), locator, pageSize + 1, list, null);
			if (list.size() > 0) {
				billList2.addAll(billList);
				billList = billList2;
			}
		}
		response.setRentalBills(new ArrayList<>());
		if(billList.size() > pageSize){
			billList.remove(billList.size()-1);
			response.setNextPageAnchor(billList.get(billList.size()-1).getId());
		}
		checkRentalBills(billList,true);
		for (RentalOrder bill : billList) {
			RentalBriefOrderDTO dto = new RentalBriefOrderDTO();
			dto.setResourceType(bill.getResourceType());
			dto.setResourceTypeId(bill.getResourceTypeId());
			dto.setOrderNo(bill.getOrderNo());
			dto.setRentalBillId(bill.getId());
			dto.setCreateTime(bill.getCreateTime().getTime());
			dto.setTotalAmount(bill.getPayTotalMoney());
			dto.setCustomObject(bill.getCustomObject());
			dto.setStatus(bill.getStatus());

			response.getRentalBills().add(dto);
		}

		return response;
	}

	@Override
	public RentalOrderDTO getRentalOrderDetail(GetRentalOrderDetailCommand cmd) {

		if (null == cmd.getOrderId()) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_LOST_PARAMETER,
					"Invalid parameter : bill id is null");
		}
		RentalOrder bill = this.rentalv2Provider.findRentalBillById(cmd.getOrderId());
		if (null == bill) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_CANNOT_FIND_ORDER,
					"Invalid parameter : bill id can not find bill");
		}

		long currTime = System.currentTimeMillis();
		//订单开始 置为使用中的状态,防止定时器没有更新到订单状态
		if (bill.getResourceType().equals(RentalV2ResourceType.DEFAULT.getCode()) &&bill.getStatus() == SiteBillStatus.SUCCESS.getCode() &&
				currTime >= bill.getStartTime().getTime() && currTime <= bill.getEndTime().getTime()) {
			bill.setStatus(SiteBillStatus.IN_USING.getCode());
			rentalv2Provider.updateRentalBill(bill);
		}

		RentalOrderDTO dto = ConvertHelper.convert(bill, RentalOrderDTO.class);
		convertRentalOrderDTO(dto, bill);
		if (!RentalV2ResourceType.VIP_PARKING.getCode().equals(bill.getResourceType())) {
			RentalBillDTO billDto = processOrderDTO(bill);
			String json = StringHelper.toJsonString(billDto);
			dto.setCustomObject(json);
		}
		if (dto.getStatus().equals(SiteBillStatus.FAIL.getCode()) && bill.getPaidMoney()!=null && bill.getPaidMoney().compareTo(new BigDecimal(0))>0)
			dto.setStatus(SiteBillStatus.FAIL_PAID.getCode());
		return dto;
	}

	@Override
	public void convertRentalOrderDTO(RentalOrderDTO dto, RentalOrder bill) {

		dto.setCustomObject(bill.getCustomObject());
		dto.setUserEnterpriseId(bill.getUserEnterpriseId());
		dto.setUserEnterpriseName(bill.getUserEnterpriseName());
		dto.setUserName(bill.getUserName());
		dto.setUserPhone(bill.getUserPhone());
		dto.setAddressId(bill.getAddressId());

		dto.setOrderNo(bill.getOrderNo());

		dto.setSiteName(bill.getResourceName());
		if (null != bill.getAddressId()) {
			Address address = addressProvider.findAddressById(bill.getAddressId());
			if (null != address) {
				dto.setAddress(address.getAddress());
			}
		}
		dto.setRentalBillId(bill.getId());
		dto.setCreateTime(bill.getCreateTime().getTime());
		if (null != bill.getStartTime()) {
			dto.setStartTime(bill.getStartTime().getTime());
		}
		if (null != bill.getEndTime()) {
			dto.setEndTime(bill.getEndTime().getTime());
		}
		if (null != bill.getActualStartTime()) {
			dto.setActualStartTime(bill.getActualStartTime().getTime());
		}
		if (null != bill.getActualEndTime()) {
			dto.setActualEndTime(bill.getActualEndTime().getTime());
		}
		if (null != bill.getPayTime()) {
			dto.setPayTime(bill.getPayTime().getTime());
		}
		if (null != bill.getCancelTime()) {
			dto.setCancelTime(bill.getCancelTime().getTime());
		}
		if (SiteBillStatus.OWING_FEE.getCode() == bill.getStatus())
			dto.setOverTime(bill.getActualEndTime().getTime() - bill.getEndTime().getTime());

		dto.setTotalAmount(bill.getPayTotalMoney());
		dto.setPaidAmount(bill.getPaidMoney());
		dto.setUnPayAmount(bill.getPayTotalMoney().subtract(bill.getPaidMoney()));
		dto.setRefundAmount(bill.getRefundAmount());
		dto.setStatus(bill.getStatus());
		//支付方式
		dto.setVendorType(bill.getVendorType());
		dto.setRentalType(bill.getRentalType());
		List<RentalTimeInterval> timeIntervals = rentalv2Provider.queryRentalTimeIntervalByOwner(bill.getResourceType(),
				EhRentalv2Resources.class.getSimpleName(), bill.getRentalResourceId());
		if (null != timeIntervals && !timeIntervals.isEmpty()) {
			dto.setTimeStep(timeIntervals.get(0).getTimeStep());
		}
		//发票信息
		Rentalv2OrderRecord record = rentalv2AccountProvider.getOrderRecordByOrderNo(Long.valueOf(bill.getOrderNo()));
		if (record != null) {
			dto.setInvoiceFlag(bill.getInvoiceFlag());
			String invoiceUrl = configurationProvider.getValue(0, "home.url", "");//营销系统和core共用一个域名
			String systemId = configurationProvider.getValue(0,"prmt.system_id","10");
			if (TrueOrFalseFlag.FALSE.getCode().equals(bill.getInvoiceFlag())) {
				 invoiceUrl = invoiceUrl + "/promotion/app-invoice?businessOrderNumber=%s&systemId=%s#/invoice-application";
			} else {
				invoiceUrl = invoiceUrl + "/promotion/app-invoice?businessOrderNumber=%s&systemId=%s#/invoice-detail/2";
			}
			invoiceUrl = String.format(invoiceUrl, record.getBizOrderNum(),systemId);
			dto.setInvoiceUrl(invoiceUrl);
		}


	}

	@Override
	public SearchRentalOrdersResponse searchRentalOrders(SearchRentalOrdersCommand cmd) {

		SearchRentalOrdersResponse response = new SearchRentalOrdersResponse();

		Integer pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());

		List<RentalOrder> orders = rentalv2Provider.searchRentalOrders(cmd.getResourceTypeId(), cmd.getResourceType(),
				cmd.getResourceId(), cmd.getBillStatus(), cmd.getStartTime(), cmd.getEndTime(), cmd.getTag1(),
				cmd.getTag2(), cmd.getVendorType(), cmd.getKeyword(), cmd.getPageAnchor(), pageSize);
		response.setTotalAmount(rentalv2Provider.getRentalOrdersTotalAmount(cmd.getResourceTypeId(), cmd.getResourceType(),
				cmd.getResourceId(), cmd.getBillStatus(), cmd.getStartTime(), cmd.getEndTime(), cmd.getTag1(),
				cmd.getTag2(), cmd.getKeyword()));

		int size = orders.size();
		if (size > 0) {
			response.setRentalBills(orders.stream().map(r -> {
				RentalOrderDTO dto = ConvertHelper.convert(r, RentalOrderDTO.class);
				convertRentalOrderDTO(dto, r);
				return dto;
			}).collect(Collectors.toList()));

			if (size != pageSize) {
				response.setNextPageAnchor(null);
			} else {
				response.setNextPageAnchor(orders.get(size - 1).getReserveTime().getTime());
			}
		}

		return response;

	}

	@Override
	public RentalOrderDTO getRentalOrderById(GetRentalBillCommand cmd) {
		if (null == cmd.getBillId()) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_LOST_PARAMETER,
					"Invalid parameter : bill id is null");
		}
		RentalOrder bill = this.rentalv2Provider.findRentalBillById(cmd.getBillId());
		if (null == bill) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_CANNOT_FIND_ORDER,
					"Invalid parameter : bill id can not find bill");
		}
		RentalOrderDTO dto = ConvertHelper.convert(bill, RentalOrderDTO.class);
		convertRentalOrderDTO(dto, bill);

		return dto;
	}

	@Override
	public GetRenewRentalOrderInfoResponse getRenewRentalOrderInfo(GetRenewRentalOrderInfoCommand cmd) {

		RentalOrder bill = rentalv2Provider.findRentalBillById(cmd.getRentalBillId());
		//使用中才可以续费
		if (!bill.getStatus().equals(SiteBillStatus.IN_USING.getCode())) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_CANNOT_FIND_ORDER,
					"Invalid parameter");
		}
		if (DateHelper.currentGMTTime().getTime() > bill.getEndTime().getTime()) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_ORDER_RENEW_OVERTIME, "订单已超时，无法进行延时");
		}

		RentalResource rs = rentalCommonService.getRentalResource(bill.getResourceType(), bill.getRentalResourceId());

		processCells(rs, bill.getRentalType());

		Timestamp startTime = bill.getStartTime();
		Timestamp endTime = bill.getEndTime();
//		changeRentalSiteRules = findRentalSiteRuleByDate(bill.getRentalResourceId(),choseRSR.getResourceNumber(),
//				beginTime,endTime, null, dateSF.get().format(bill.getRentalDate()));

		List<RentalResourceOrder> resourceOrders = rentalv2Provider.findRentalResourceOrderByOrderId(bill.getId());
		Long ruleId = resourceOrders.stream().mapToLong(RentalResourceOrder::getRentalResourceRuleId).max().getAsLong();

		List<Rentalv2PriceRule> priceRules = rentalv2PriceRuleProvider.listPriceRuleByOwner(rs.getResourceType(),
				PriceRuleType.RESOURCE.getCode(), rs.getId());

		RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(null, null,
				rs.getResourceType(), rs.getResourceTypeId(), RuleSourceType.RESOURCE.getCode(), rs.getId());

		//每次获取延时支付信息 都重置一次之前未支付留下的信息
		restoreRentalBill(bill);

		List<RentalBillRuleDTO> rules = new ArrayList<>();

		RentalCell lastCell = null;

		for (int i = 1; i <= cmd.getCellCount().intValue(); i++) {

			RentalCell cell = findRentalSiteRuleById(ruleId + i,rs.getId(), bill.getRentalType(),bill.getResourceType());
			if (null == cell) {
				throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERRPR_LOST_RESOURCE_RULE,
						"Invalid param rules");
			}

			if (i == cmd.getCellCount().intValue()) {
				lastCell = cell;
			}

			validateCellStatus(cell, priceRules, rs, rule, true);

			RentalBillRuleDTO dto = new RentalBillRuleDTO();
			dto.setRuleId(cell.getId());
			dto.setRentalCount(1D);
			rules.add(dto);
		}

		//用来存每个单元格计算之后的价格
		Map<Long, BigDecimal> cellAmountMap = new HashMap<>();
		BigDecimal amount = calculateOrderAmount(rs, rules, bill, cellAmountMap);

		GetRenewRentalOrderInfoResponse response = ConvertHelper.convert(cmd, GetRenewRentalOrderInfoResponse.class);

		response.setEndTime(lastCell.getEndTime().getTime());
		response.setAmount(amount);

		cellList.get().clear();
		return response;
	}

	@Override
	public CommonOrderDTO renewRentalOrder(RenewRentalOrderCommand cmd) {

		RentalOrder bill = actualRenewRentalOrder(cmd);

		return buildCommonOrderDTO(bill);
	}

	@Override
	public PreOrderDTO renewRentalOrderV2(RenewRentalOrderCommand cmd) {
		RentalOrder bill = actualRenewRentalOrder(cmd);
		PreOrderCommand preOrderCommand = buildPreOrderDTO(bill, cmd.getClientAppName(), null);
        PreOrderDTO preOrderDTO = rentalv2PayService.createPreOrder(preOrderCommand, bill);
		//保存支付订单信息
		Rentalv2OrderRecord record = this.rentalv2AccountProvider.getOrderRecordByOrderNo(Long.valueOf(bill.getOrderNo()));
		if (record != null){
			record.setOrderId(bill.getId());
			record.setStatus((byte)0);//未支付
			record.setNamespaceId(UserContext.getCurrentNamespaceId());
			record.setPaymentOrderType(OrderRecordType.RENEW.getCode());//续费订单
			this.rentalv2AccountProvider.updateOrderRecord(record);
		}
		return preOrderDTO;
	}

	private RentalOrder actualRenewRentalOrder(RenewRentalOrderCommand cmd) {
		RentalOrder bill = rentalv2Provider.findRentalBillById(cmd.getRentalBillId());
		//使用中才可以续费
		if (!bill.getStatus().equals(SiteBillStatus.IN_USING.getCode())) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_CANNOT_FIND_ORDER,
					"Invalid parameter");
		}
		if (DateHelper.currentGMTTime().getTime() > bill.getEndTime().getTime()) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_ORDER_RENEW_OVERTIME, "订单已超时，无法进行延时");
		}

		//RentalResource rs = rentalCommonService.getRentalResource(bill.getResourceType(), bill.getRentalResourceId());

		//processCells(rs, bill.getRentalType());
		if (cmd.getAmount().compareTo(new BigDecimal(0)) == 1) {
			GetRenewRentalOrderInfoCommand cmd2 = new GetRenewRentalOrderInfoCommand();
			cmd2.setCellCount(cmd.getCellCount());
			cmd2.setRentalBillId(cmd.getRentalBillId());
			cmd2.setRentalType(cmd.getRentalType());
			cmd2.setTimeStep(cmd.getTimeStep());
			GetRenewRentalOrderInfoResponse response = getRenewRentalOrderInfo(cmd2);//校验一次金额
			if (null != response.getAmount() && cmd.getAmount().compareTo(response.getAmount()) != 0) {
				throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_INVALID_PARAMETER,
						"Invalid param amount");
			}
			BigDecimal totalAmount = bill.getPayTotalMoney().add(response.getAmount());//计算价格
			//bill.setResourceTotalMoney(totalAmount);
			bill.setPayTotalMoney(totalAmount);
		}else{
			RentalResource rs = rentalCommonService.getRentalResource(bill.getResourceType(), bill.getRentalResourceId());
			processCells(rs, bill.getRentalType());
			updateRentalOrder(rs, bill, new BigDecimal(0), cmd.getCellCount(), false);
			//发消息
			RentalMessageHandler handler = rentalCommonService.getRentalMessageHandler(bill.getResourceType());

			handler.renewRentalOrderSendMessage(bill);
		}
		rentalv2Provider.updateRentalBill(bill);

		cellList.get().clear();


		return bill;
	}

	@Override
	public void renewOrderSuccess(RentalOrder rentalBill, Double rentalCount) {
		RentalResource rs = rentalCommonService.getRentalResource(rentalBill.getResourceType(), rentalBill.getRentalResourceId());
		processCells(rs, rentalBill.getRentalType());
		updateRentalOrder(rs, rentalBill, null, rentalCount, false);
		rentalBill.setResourceTotalMoney(rentalBill.getPayTotalMoney());

		//发消息
		RentalMessageHandler handler = rentalCommonService.getRentalMessageHandler(rentalBill.getResourceType());

		handler.renewRentalOrderSendMessage(rentalBill);
	}

	private BigDecimal updateRentalOrder(RentalResource rs, RentalOrder bill, BigDecimal payAmount, Double cellCount, boolean validateTime) {
		List<RentalResourceOrder> resourceOrders = rentalv2Provider.findRentalResourceOrderByOrderId(bill.getId());
		//TODO:此处取的是连续单元格后面的id，不适用跨场景延长时间
		Long ruleId = resourceOrders.stream().mapToLong(RentalResourceOrder::getRentalResourceRuleId).max().getAsLong();

		List<Rentalv2PriceRule> priceRules = rentalv2PriceRuleProvider.listPriceRuleByOwner(rs.getResourceType(),
				PriceRuleType.RESOURCE.getCode(), rs.getId());

		RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(null, null,
				rs.getResourceType(), rs.getResourceTypeId(), RuleSourceType.RESOURCE.getCode(), rs.getId());

		List<RentalBillRuleDTO> rules = new ArrayList<>();

		for (int i = 1; i <= cellCount.intValue(); i++) {

			RentalCell cell = findRentalSiteRuleById(ruleId + i,rs.getId(), bill.getRentalType(),bill.getResourceType());
			if (null == cell) {
				throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERRPR_LOST_RESOURCE_RULE,
						"Invalid param rules");
			}


			validateCellStatus(cell, priceRules, rs, rule, validateTime);


			RentalBillRuleDTO dto = new RentalBillRuleDTO();
			dto.setRuleId(cell.getId());
			dto.setRentalCount(1D);
			rules.add(dto);
		}

		//用来存每个单元格计算之后的价格
		Map<Long, BigDecimal> cellAmountMap = new HashMap<>();
		BigDecimal amount = calculateOrderAmount(rs, rules, bill, cellAmountMap);

		if (null != payAmount && amount.compareTo(payAmount) != 0) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_INVALID_PARAMETER,
					"Invalid param amount");
		}

		List<RentalBillRuleDTO> totalRules = getBillRules(bill);

		totalRules.addAll(rules);
		//设置只用详情
		setUseDetailStr(totalRules, rs, bill);
		//设置订单提醒时间,设置之前先把老时间存起来
		bill.setOldEndTime(bill.getEndTime());
		setRentalOrderReminderTime(totalRules, rs, bill);
		//设置预约单元格数量
//		bill.setRentalCount(totalRules.stream().filter(r -> null != r.getRentalCount())
//				.mapToDouble(RentalBillRuleDTO::getRentalCount).sum());

		this.dbProvider.execute((TransactionStatus status) -> {

			//用基于服务器平台的锁添加订单（包括验证和添加）
			Tuple<Long, Boolean> tuple = this.coordinationProvider
					.getNamedLock(CoordinationLocks.CREATE_RENTAL_BILL.getCode() + rs.getResourceType() + rs.getId())
					.enter(() -> {
						for (int i = 1; i <= cellCount.intValue(); i++) {

							RentalCell cell = findRentalSiteRuleById(ruleId + i,rs.getId(), bill.getRentalType(),bill.getResourceType());
							if (null == cell) {
								throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERRPR_LOST_RESOURCE_RULE,
										"Invalid param rules");
							}
							validateCellStatus(cell, priceRules, rs, rule, validateTime);
						}

						this.rentalv2Provider.updateRentalBill(bill);
						return null;
					});

			createResourceOrder(rules, bill, cellAmountMap);

			return null;
		});
		if (rule.getNeedPay() == NormalFlag.NEED.getCode())
			return amount;
		else
			return new BigDecimal(0);
	}

	@Override
	public RentalOrderDTO completeRentalOrder(CompleteRentalOrderCommand cmd) {

		Long now = System.currentTimeMillis();

		if (null == cmd.getRentalBillId()) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_LOST_PARAMETER,
					"Invalid parameter BillId");
		}

		RentalOrder order = this.rentalv2Provider.findRentalBillById(cmd.getRentalBillId());
		if (null == order) {
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE, RentalServiceErrorCode.ERROR_CANNOT_FIND_ORDER,
					"RentalOrder not found");
		}
		//只有使用中的订单可以结束使用
		if (order.getStatus() != SiteBillStatus.IN_USING.getCode()) {
			LOGGER.error("Order not in using");
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_ORDER_CANCELED, "结束失败，订单不在使用中");
		}

		VipParkingUseInfoDTO parkingInfo = JSONObject.parseObject(order.getCustomObject(), VipParkingUseInfoDTO.class);
		ParkingSpaceDTO spaceDTO = dingDingParkingLockHandler.getParkingSpaceLock(parkingInfo.getLockId());
		if (null != spaceDTO && spaceDTO.getLockStatus().equals(ParkingSpaceLockStatus.DOWN.getCode())) {
			LOGGER.error("Parking lock not raise");
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
					RentalServiceErrorCode.ERROR_DOWN_PARKING_LOCK, "结束失败，请先升起车锁");
		}

		RentalResource rs = rentalCommonService.getRentalResource(order.getResourceType(), order.getRentalResourceId());
		RentalOrderHandler orderHandler = rentalCommonService.getRentalOrderHandler(order.getResourceType());
		restoreRentalBill(order);

		if (now > order.getEndTime().getTime()) {
			rs.setResourceCounts(rs.getResourceCounts()+99999.0);//超时订单无视车锁数量
			processCells(rs, order.getRentalType());
			long overTimeStartTime = order.getEndTime().getTime();
			long overTimeEndTime = now;

			long interval = (now - order.getEndTime().getTime()) / (1000 * 60);

			List<RentalBillRuleDTO> rules = getBillRules(order);
			RentalTimeInterval rentalTimeInterval = null;
			List<RentalTimeInterval> timeIntervals = rentalv2Provider.queryRentalTimeIntervalByOwner(order.getResourceType(),
					EhRentalv2Resources.class.getSimpleName(), order.getRentalResourceId());
			if (null != timeIntervals) {
				for (RentalTimeInterval r : timeIntervals) {
					long endTime = Timestamp.valueOf(dateSF.get().format(new java.util.Date(now))
							+ " "
							+ String.valueOf(r.getEndTime().intValue())
							+ ":"
							+ String.valueOf((int) ((r.getEndTime() % 1) * 60))
							+ ":00").getTime();
					long startTime = Timestamp.valueOf(dateSF.get().format(new java.util.Date(now))
							+ " "
							+ String.valueOf(r.getBeginTime().intValue())
							+ ":"
							+ String.valueOf((int) ((r.getBeginTime() % 1) * 60))
							+ ":00").getTime();
					////TODO:寻找区间,如果找不到，表示跨过了不可预约的时间段,此处如何处理
					if (startTime <= overTimeStartTime && endTime >= overTimeEndTime) {
						rentalTimeInterval = r;
					}
				}
				if (null != rentalTimeInterval) {
					double timeStep = rentalTimeInterval.getTimeStep() * 60;
					double rentalCount = interval / timeStep;
					if (interval % timeStep != 0) {
						rentalCount = (int) rentalCount + 1;
					}
					BigDecimal amount = updateRentalOrder(rs, order, null, rentalCount, false);
					order.setEndTime(order.getOldEndTime());
					amount = rentalCommonService.calculateOverTimeFee(order, amount, now);
					if (amount.compareTo(new BigDecimal(0)) == 1)
						order.setOrderNo(onlinePayService.createBillId(DateHelper.currentGMTTime().getTime()).toString());
				}
//				dto.setTimeIntervals(timeIntervals.stream().map(t -> ConvertHelper.convert(t, TimeIntervalDTO.class))
//						.collect(Collectors.toList()));
			}

			//当前时间超时了，设置成欠费
			order.setStatus(SiteBillStatus.OWING_FEE.getCode());
			//欠费0元  置为已完成
			if ((order.getPayTotalMoney().subtract(order.getPaidMoney())).compareTo(BigDecimal.ZERO) == 0) {
				order.setStatus(SiteBillStatus.COMPLETE.getCode());
			}
//			order.setPayTotalMoney();
		} else {
			if ((order.getPayTotalMoney().subtract(order.getPaidMoney())).compareTo(BigDecimal.ZERO) == 0)
				order.setStatus(SiteBillStatus.COMPLETE.getCode());
			else
				order.setStatus(SiteBillStatus.OWING_FEE.getCode());
		}

		order.setActualEndTime(new Timestamp(now));
		order.setActualStartTime(order.getStartTime());
		rentalv2Provider.updateRentalBill(order);

		cellList.get().clear();

		orderHandler.completeRentalOrder(order);
		//发消息
		RentalMessageHandler handler = rentalCommonService.getRentalMessageHandler(order.getResourceType());

		if (order.getStatus() == SiteBillStatus.OWING_FEE.getCode()) {
			handler.overTimeSendMessage(order);
		} else if (order.getStatus() == SiteBillStatus.COMPLETE.getCode()) {
			handler.completeOrderSendMessage(order);
		}

		RentalOrderDTO dto = ConvertHelper.convert(order, RentalOrderDTO.class);
		convertRentalOrderDTO(dto, order);
		return dto;
	}

//	private List<RentalTimeInterval> findRentalTimeIntervals(List<RentalTimeInterval> timeIntervals, long overTimeStartTime,
//															 long overTimeEndTime) {
//		for (RentalTimeInterval r: timeIntervals) {
//			long endTime = Timestamp.valueOf(dateSF.get().format(new java.util.Date(now))
//					+ " "
//					+ String.valueOf(r.getEndTime().intValue())
//					+ ":"
//					+ String.valueOf((int) ((r.getEndTime() % 1) * 60))
//					+ ":00").getTime();
//			long startTime = Timestamp.valueOf(dateSF.get().format(new java.util.Date(now))
//					+ " "
//					+ String.valueOf(r.getBeginTime().intValue())
//					+ ":"
//					+ String.valueOf((int) ((r.getBeginTime() % 1) * 60))
//					+ ":00").getTime();
//			////TODO:寻找区间,如果找不到，表示跨过了不可预约的时间段,此处如何处理
//			if (startTime <= overTimeStartTime && endTime >= overTimeEndTime) {
//				rentalTimeInterval = r;
//			}
//		}
//	}

	//停车缴费续费但没支付的订单 恢复原样
	private void restoreRentalBill(RentalOrder order) {
		order.setPayTotalMoney(order.getResourceTotalMoney());
		rentalv2Provider.updateRentalBill(order);
	}

	@Override
	public GetResourceRuleV2Response getResourceRuleV2(GetResourceRuleV2Command cmd) {
		RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(cmd.getOwnerType(), cmd.getOwnerId(),
				cmd.getResourceType(), cmd.getResourceTypeId(), cmd.getSourceType(), cmd.getSourceId());
		QueryDefaultRuleAdminResponse queryDefaultRuleAdminResponse = convert(rule, cmd.getSourceType());

		GetResourceOrderRuleCommand getResourceOrderRuleCommand = ConvertHelper.convert(cmd, GetResourceOrderRuleCommand.class);
		ResourceOrderRuleDTO orderRuleDTO = getResourceOrderRule(getResourceOrderRuleCommand);

		GetResourceRuleV2Response response = ConvertHelper.convert(orderRuleDTO, GetResourceRuleV2Response.class);
		response.setHolidayOpenFlag(rule.getHolidayOpenFlag());
		response.setHolidayType(rule.getHolidayType());
		currentSceneType.set(cmd.getSceneType());

		List<Rentalv2PriceRule> priceRules = rentalv2PriceRuleProvider.listPriceRuleByOwner(rule.getResourceType(),
				PriceRuleType.RESOURCE.getCode(), rule.getSourceId());
		List<Byte> rentalTypes = priceRules.stream().map(Rentalv2PriceRule::getRentalType).collect(Collectors.toList());
		response.setRentalType(rentalTypes.get(0));
		String classification = getClassification(priceRules.get(0).getUserPriceType());
        BigDecimal price = priceRules.get(0).getWorkdayPrice();
        if (classification != null && !priceRules.get(0).getUserPriceType().equals(RentalUserPriceType.UNIFICATION.getCode())){
			List<RentalPriceClassification> priceClassification =rentalv2Provider.listClassification(rule.getResourceType(),EhRentalv2PriceRules.class.getSimpleName(),
					priceRules.get(0).getId(),null,null,priceRules.get(0).getUserPriceType(),classification);
			if (priceClassification != null && priceClassification.size() > 0)
				price = priceClassification.get(0).getWorkdayPrice();
		}

        response.setPrice(rule.getNeedPay() == NormalFlag.NEED.getCode() ? price : new BigDecimal(0));
        if (RentalType.HOUR.getCode() == response.getRentalType()) {
			response.setTimeIntervals(queryDefaultRuleAdminResponse.getTimeIntervals());
		} else if (RentalType.DAY.getCode() == response.getRentalType()) {
			response.setDayOpenTime(rule.getDayOpenTime());
			response.setDayCloseTime(rule.getDayCloseTime());
		}

		return response;
	}

	@Override
	public GetCancelOrderTipResponse getCancelOrderTip(GetCancelOrderTipCommand cmd) {

		RentalOrder order = this.rentalv2Provider.findRentalBillById(cmd.getOrderId());
		if (null == order) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER, "RentalOrder not found");
		}

		GetCancelOrderTipResponse response = new GetCancelOrderTipResponse();
		if (order.getPayTotalMoney().compareTo(BigDecimal.ZERO) == 0) {
			return response;
		}
		Long now = System.currentTimeMillis();

		BigDecimal refundAmount = rentalCommonService.calculateRefundAmount(order, now);

		response.setTip(order.getTip());
		return response;
	}

	@Override
	public QueryRentalStatisticsResponse queryRentalStatistics(QueryRentalStatisticsCommand cmd) {
		if (cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4040040430L, cmd.getAppId(), null, cmd.getCurrentProjectId());
		}
		QueryRentalStatisticsResponse response = new QueryRentalStatisticsResponse();
		BigDecimal totalAmount = rentalv2Provider.countRentalBillAmount(cmd.getResourceType(), cmd.getResourceTypeId(), cmd.getCommunityId(),
				cmd.getStartDate(), cmd.getEndDate(), null, null);
		response.setTotalAmount(totalAmount);
		Integer orderCount = rentalv2Provider.countRentalBillNum(cmd.getResourceType(), cmd.getResourceTypeId(), cmd.getCommunityId(),
				cmd.getStartDate(), cmd.getEndDate(), null, null);
		response.setOrderCount(orderCount);

		response.setClassifyStatistics(new ArrayList<>());
		List<RentalResource> rentalSites = rentalv2Provider.findRentalSitesByCommunityId(cmd.getResourceType(), cmd.getResourceTypeId(), cmd.getCommunityId());
		if (rentalSites == null || rentalSites.size() == 0)
			return response;
		for (RentalResource rentalSite : rentalSites) {
			RentalStatisticsDTO dto = new RentalStatisticsDTO();
			dto.setName(rentalSite.getResourceName());
			dto.setAmount(rentalv2Provider.countRentalBillAmount(cmd.getResourceType(), null, null, cmd.getStartDate(),
					cmd.getEndDate(), rentalSite.getId(), null));
			dto.setOrderCount(rentalv2Provider.countRentalBillNum(cmd.getResourceType(), null, null, cmd.getStartDate(),
					cmd.getEndDate(), rentalSite.getId(), null));
			dto.setUsedTime(rentalv2Provider.countRentalBillValidTime(cmd.getResourceType(), null, null, cmd.getStartDate(),
					cmd.getEndDate(), rentalSite.getId(), null));
			response.getClassifyStatistics().add(dto);
		}

		return response;
	}

	@Override
	public QueryOrgRentalStatisticsResponse queryOrgRentalStatistics(QueryRentalStatisticsCommand cmd) {
		if (cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4040040430L, cmd.getAppId(), null, cmd.getCurrentProjectId());
		}
		QueryOrgRentalStatisticsResponse response = new QueryOrgRentalStatisticsResponse();


		if (StringUtils.isEmpty(cmd.getOrderBy()))
			cmd.setOrderBy(RentalStatisticsOrder.orderCount);
		if (cmd.getOrder() == null)
			cmd.setOrder(-1);

		if (RentalStatisticsOrder.amount.equals(cmd.getOrderBy())) {
			response.setOrgStatistics(rentalv2Provider.listRentalBillAmountByOrgId(cmd.getResourceType(), cmd.getResourceTypeId(), cmd.getCommunityId(),
					cmd.getStartDate(), cmd.getEndDate(), cmd.getOrder()));
		}

		if (RentalStatisticsOrder.orderCount.equals(cmd.getOrderBy())) {
			response.setOrgStatistics(rentalv2Provider.listRentalBillNumByOrgId(cmd.getResourceType(), cmd.getResourceTypeId(), cmd.getCommunityId(),
					cmd.getStartDate(), cmd.getEndDate(), cmd.getOrder()));
		}

		if (RentalStatisticsOrder.usedTime.equals(cmd.getOrderBy())) {
			response.setOrgStatistics(rentalv2Provider.listRentalBillValidTimeByOrgId(cmd.getResourceType(), cmd.getResourceTypeId(), cmd.getCommunityId(),
					cmd.getStartDate(), cmd.getEndDate(), cmd.getOrder()));
		}

		List<RentalStatisticsDTO> tmp = new ArrayList<>();
		if (cmd.getPageAnchor() == null)
			cmd.setPageAnchor(0L);
		cmd.setPageSize(PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize()));

		for (Integer i = cmd.getPageAnchor().intValue(); i < cmd.getPageAnchor().intValue() + cmd.getPageSize(); i++)
			if (i < response.getOrgStatistics().size())
				tmp.add(response.getOrgStatistics().get(i));

		//剩余的两种 填充
		fillStatisticsUsedTime(tmp, cmd);
		fillStatisticsOrderAmount(tmp, cmd);
		fillStatisticsAmount(tmp, cmd);
		tmp.forEach(r -> {
			Organization org = organizationProvider.findOrganizationById(r.getEnterpriseId());
			if (org != null)
				r.setName(org.getName());
		});

		if (response.getOrgStatistics().size() > cmd.getPageAnchor() + cmd.getPageSize())
			response.setNextPageAnchor(cmd.getPageAnchor() + tmp.size());
		response.setOrgStatistics(tmp);
		return response;
	}

	private void fillStatisticsAmount(List<RentalStatisticsDTO> dtos, QueryRentalStatisticsCommand cmd) {
		dtos.stream().forEach(r -> {
			if (r.getAmount() != null)
				return;
			r.setAmount(rentalv2Provider.countRentalBillAmount(cmd.getResourceType(), cmd.getResourceTypeId(), cmd.getCommunityId(), cmd.getStartDate(),
					cmd.getEndDate(), null, r.getEnterpriseId()));
			r.setAmount(r.getAmount() == null ? new BigDecimal(0) : r.getAmount());
		});
	}

	private void fillStatisticsOrderAmount(List<RentalStatisticsDTO> dtos, QueryRentalStatisticsCommand cmd) {
		dtos.stream().forEach(r -> {
			if (r.getOrderCount() != null)
				return;
			r.setOrderCount(rentalv2Provider.countRentalBillNum(cmd.getResourceType(), cmd.getResourceTypeId(), cmd.getCommunityId(), cmd.getStartDate(),
					cmd.getEndDate(), null, r.getEnterpriseId()));
			r.setOrderCount(r.getOrderCount() == null ? 0 : r.getOrderCount());
		});
	}

	private void fillStatisticsUsedTime(List<RentalStatisticsDTO> dtos, QueryRentalStatisticsCommand cmd) {
		dtos.stream().forEach(r -> {
			if (r.getUsedTime() != null)
				return;
			r.setUsedTime(rentalv2Provider.countRentalBillValidTime(cmd.getResourceType(), cmd.getResourceTypeId(), cmd.getCommunityId(), cmd.getStartDate(),
					cmd.getEndDate(), null, r.getEnterpriseId()));
			r.setUsedTime(r.getUsedTime() == null ? 0L : r.getUsedTime());
		});
	}

	@Override
	public List<Long> getHolidayCloseDates(GetHolidayCloseDatesCommand cmd) {
		if (cmd.getHolidayType().equals(RentalHolidayType.NORMAL_WEEKEND.getCode()))
			return normalWeekend;
		else
			return legalHoliday;
	}

	@Override
	public GetResourceUsingInfoResponse getResourceUsingInfo(FindRentalSiteByIdCommand cmd) {
		RentalResource rentalSite = this.rentalv2Provider.getRentalSiteById(cmd.getId());
		if (rentalSite == null || rentalSite.getStatus().equals(-1))
			throw RuntimeErrorException.errorWith(RentalServiceErrorCode.SCOPE,
					1001, "资源不存在");
		GetResourceUsingInfoResponse response = new GetResourceUsingInfoResponse();

		response.setSourceName(rentalSite.getResourceName());
		List<RentalCloseDate> closeDates = rentalv2Provider.queryRentalCloseDateByOwner(rentalSite.getResourceType(),
				EhRentalv2Resources.class.getSimpleName(), rentalSite.getId(),null,null);
		LocalDate today = LocalDate.now();
		Long startTime = LocalDateTime.of(today.getYear(),today.getMonth(),today.getDayOfMonth(),0,0).atZone(ZoneId.systemDefault())
				.toInstant().toEpochMilli();
		//如果今天被关闭 显示空闲
		if (closeDates != null &&
		closeDates.stream().anyMatch(r-> startTime == r.getCloseDate().getTime()))
			return response;
		//每日开放时间
		RentalType[] iterators = {RentalType.HALFDAY,RentalType.DAY,RentalType.WEEK,RentalType.MONTH};
		StringBuilder builder = new StringBuilder();
		String separate = "注：";
		for (RentalType iterator:iterators){
            String s = getResourceOpenTime(rentalSite.getResourceType(),rentalSite.getId(),iterator.getCode(),"，");
            if (s == null)
                continue;
			builder.append(separate);
			switch (iterator){
				case HALFDAY:
					builder.append("按半天预订时每日开放时间为");
					break;
				case DAY:
					builder.append("按天预订时每日开放时间为");
					break;
				case WEEK:
					builder.append("按周预订时每日开放时间为");
					break;
				case MONTH:
                    builder.append("按月预订时每日开放时间为");
                    break;
			}
			builder.append(s);
			separate = "；";
		}
		response.setOpenTimes(builder.toString());
		//今天(包含)的订单
		today = today.plusDays(1);
		Long endTime = LocalDateTime.of(today.getYear(),today.getMonth(),today.getDayOfMonth(),0,0).atZone(ZoneId.systemDefault())
				.toInstant().toEpochMilli();
		List<RentalOrder> rentalOrders = rentalv2Provider.listActiveBillsByInterval(rentalSite.getId(), startTime, endTime);
		if (rentalOrders == null || rentalOrders.size()==0)
			return response;
        List<UsingInfoDTO> usingInfos = rentalOrders.stream().flatMap(r -> {
            List<RentalResourceOrder> resourceOrders = rentalv2Provider.findRentalResourceOrderByOrderId(r.getId());
            String userName = userProvider.findUserById(r.getRentalUid()).getNickName();
            if (userName.length() > 1)
                userName = userName.substring(0, userName.length() - 2) + "*" + userName.substring(userName.length() - 1, userName.length());
            if (null != r.getUserEnterpriseId()) {
                Organization org = this.organizationProvider.findOrganizationById(r.getUserEnterpriseId());
                if (org != null) {
                    userName = org.getName();
                }
            }
            if (rentalSite.getResourceCounts() > 1.0 && NormalFlag.NEED.getCode() == rentalSite.getAutoAssign()) {
                List<RentalResourceNumber> resourceNumbers = this.rentalv2Provider.queryRentalResourceNumbersByOwner(
                        rentalSite.getResourceType(), EhRentalv2Resources.class.getSimpleName(), rentalSite.getId());
                List<UsingInfoDTO> dtos = new ArrayList<>();
                for (RentalResourceNumber number : resourceNumbers) {
                    String detail = parseUsingInfoDetail(resourceOrders, number.getResourceNumber());
                    if (StringUtils.isBlank(detail))
                        continue;
                    UsingInfoDTO dto = new UsingInfoDTO();
                    dto.setUsingDetail(detail);
                    dto.setResourceName(rentalSite.getResourceName() + number.getResourceNumber());
                    dto.setUserName(userName);
                    RentalResourceOrder firstOrder = resourceOrders.stream().filter(p -> number.getResourceNumber().equals(p.getResourceNumber())).
                            min((p, q) -> p.getRentalResourceRuleId().compareTo(q.getRentalResourceRuleId())).get();
                    RentalResourceOrder lastOrder = resourceOrders.stream().filter(p -> number.getResourceNumber().equals(p.getResourceNumber())).
                            max((p, q) -> p.getRentalResourceRuleId().compareTo(q.getRentalResourceRuleId())).get();
                    dto.setStartTime(firstOrder.getBeginTime() != null ? firstOrder.getBeginTime().getTime() : firstOrder.getResourceRentalDate().getTime());
                    dto.setEndTime(lastOrder.getEndTime() != null ? lastOrder.getEndTime().getTime() : lastOrder.getResourceRentalDate().getTime());
                    dto.setRentalType(r.getRentalType());
                    dtos.add(dto);
                }
                return dtos.stream();
            }
            UsingInfoDTO dto = new UsingInfoDTO();
            dto.setResourceName(r.getRentalCount() > 1.0 ? r.getResourceName() + "*" + r.getRentalCount().intValue() : r.getResourceName());
            dto.setStartTime(r.getStartTime().getTime());
            dto.setEndTime(r.getEndTime().getTime());
            dto.setUserName(userName);
            dto.setRentalType(r.getRentalType());
            dto.setUsingDetail(parseUsingInfoDetail(resourceOrders, null));
            return Stream.of(dto);
        }).collect(Collectors.toList());
        //重新排序
        Collections.sort(usingInfos,(q,p)->q.getStartTime().compareTo(p.getStartTime()));
        response.setUsingInfos(usingInfos);
        response.setCurrentUsingInfos( usingInfos.stream().filter(r->{
            if (RentalType.DAY.getCode() == r.getRentalType() || RentalType.WEEK.getCode() == r.getRentalType() ||
                    RentalType.MONTH.getCode() == r.getRentalType())
                return true;
            return System.currentTimeMillis()>r.getStartTime() && System.currentTimeMillis()<r.getEndTime();
        }).collect(Collectors.toList()));
        response.setUsingInfos(usingInfos.stream().filter(r->r.getStartTime()>System.currentTimeMillis()).collect(Collectors.toList()));
        return response;
	}

	private String parseUsingInfoDetail(List<RentalResourceOrder> resourceOrders,String number){
		List<RentalCell> collect = resourceOrders.stream().filter(r -> number == null || number.equals(r.getResourceNumber())).map(r -> {
			RentalCell cell = new RentalCell();
			cell.setId(r.getRentalResourceRuleId());
			cell.setBeginTime(r.getBeginTime());
			cell.setEndTime(r.getEndTime());
			cell.setResourceRentalDate(r.getResourceRentalDate());
			cell.setAmorpm(r.getAmorpm());
			return cell;
		}).collect(Collectors.toList());
		if (collect.size() == 0)
			return "";
        collect.sort(Comparator.comparing(EhRentalv2Cells::getId));
		SimpleDateFormat beginTimeSF = new SimpleDateFormat("HH:mm");
		SimpleDateFormat beginDateSF = new SimpleDateFormat("MM-dd");
		SimpleDateFormat beginMonthSF = new SimpleDateFormat("yyyy-MM-dd");
		String result = getSingleNumberUseDetail(resourceOrders.get(0).getRentalType(),collect,beginTimeSF,beginTimeSF,beginDateSF,beginMonthSF);
		if (RentalType.HOUR.getCode() == resourceOrders.get(0).getRentalType())
		    result = result.replaceAll("-","~");//弱智需求。。
		return result;
	}

	@Override
	public GetStructureListResponse getStructureList(GetStructureListAdminCommand cmd) {

		if (StringUtils.isBlank(cmd.getResourceType())) {
			cmd.setResourceType(RentalV2ResourceType.DEFAULT.getCode());
		}

		if (cmd.getSourceType().equals(RuleSourceType.DEFAULT.getCode())) {
			RentalDefaultRule rule = this.rentalv2Provider.getRentalDefaultRule(cmd.getOwnerType(), cmd.getOwnerId(),
					cmd.getResourceType(), cmd.getResourceTypeId(), cmd.getSourceType(), cmd.getSourceId());
			if (rule == null)
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"cannot find default rule");
			cmd.setSourceId(rule.getId());
		}

		GetStructureListResponse response = new GetStructureListResponse();
		response.setSiteStructures(new ArrayList<>());
		List<RentalStructure> rentalStructures = rentalv2Provider
				.listRentalStructures(cmd.getSourceType(), cmd.getSourceId(), cmd.getResourceType(), null,null, null);
		for (RentalStructure rst : rentalStructures) {
			SiteStructureDTO dto = convertStructure2DTO(rst);
			response.getSiteStructures().add(dto);
		}
		return response;
	}

	@Override
	public void updateStructure(UpdateStructureAdminCommand cmd) {
		RentalStructure rentalStructure = rentalv2Provider.getRentalStructureById(cmd.getId());
		if (rentalStructure == null)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"cannot find structure");
		rentalStructure.setDisplayName(cmd.getDisplayName());
		rentalStructure.setIconUri(cmd.getIconUri());
		rentalStructure.setIsSurport(cmd.getIsSurport());
		rentalStructure.setDefaultOrder(cmd.getDefaultOrder());
		rentalv2Provider.updateRentalStructure(rentalStructure);
	}

	@Override
	public void updateStructures(UpdateStructuresAdminCommand cmd) {
		List<SiteStructureDTO> structures = cmd.getStructures();
		for (SiteStructureDTO dto:structures){
			UpdateStructureAdminCommand cmd2 = ConvertHelper.convert(dto,UpdateStructureAdminCommand.class);
			updateStructure(cmd2);
		}
	}

	@Override
	public GetUserClosestBillResponse getUserClosestBill(GetUserClosestBillCommand cmd) {
		RentalOrder order = rentalv2Provider.getUserClosestBill(UserContext.currentUserId(),cmd.getResourceTypeId());
		if (order == null)
			return null;
		GetUserClosestBillResponse response = new GetUserClosestBillResponse();
		response.setOrderId(order.getId());
		response.setResourceName(order.getResourceName());
		response.setUserDetail(order.getUseDetail());
		return response;
	}

	@Override
	public String parseSceneToken(String sceneToken) {
		try { //兼容5.9.6瑞安的bug 以后这个方法不存在
			String s = base64SafeUrlDecode(sceneToken);
			Gson gson = new Gson();
			AppContext context = gson.fromJson(s, AppContext.class);
			if (context.getOrganizationId() == null)
				return SceneType.PARK_TOURIST.getCode();
			GetAuthOrgByProjectIdAndAppIdCommand cmd = new GetAuthOrgByProjectIdAndAppIdCommand();
			cmd.setProjectId(context.getCommunityId());
			OrganizationDTO dto = organizationService.getAuthOrgByProjectIdAndAppId(cmd);
			if (dto != null && context.getOrganizationId().equals(dto.getId()))
				return SceneType.PM_ADMIN.getCode();
			return SceneType.ENTERPRISE.getCode();
		}catch (Exception ex){
			//try WebTokenGenerator
		}

		if (null != sceneToken) {
			User user = UserContext.current().getUser();
			SceneTokenDTO sceneTokenDTO = userService.checkSceneToken(user.getId(), sceneToken);
			if (sceneTokenDTO != null){
				String scene = sceneTokenDTO.getScene();
				return scene;
			}
		}
		return null;
	}

	private static String base64SafeUrlDecode(String path) {
		String result = path.replace("_", "/").replace("-", "+");
		int length = result.length();
		if (length % 4 == 2) {
			result += "==";
		} else if (length % 4 == 3) {
			result += "=";
		}
		return new String(Base64.getDecoder().decode(result.getBytes(Charset.forName("utf-8"))));
	}

	@Override
	public ArchivesContactDTO registerUser(RegisterUserCommand cmd){
		AddArchivesContactCommand cmd2 = new AddArchivesContactCommand();
		cmd2.setContactName(cmd.getContactName());
		cmd2.setGender((byte) 1);
		cmd2.setRegionCode("86");
		cmd2.setVisibleFlag((byte) 0);
		cmd2.setContactToken(cmd.getPhone());
		cmd2.setOrganizationId(cmd.getOrganizationId());
		cmd2.setDepartmentIds(new ArrayList<>());
		cmd2.getDepartmentIds().add(cmd.getOrganizationId());
		ArchivesContactDTO dto = archivesService.addArchivesContact(cmd2);

		return dto;
	}

	@Override
	public GetSceneTypeResponse getSceneType(GetSceneTypeCommand cmd) {
		GetSceneTypeResponse response = new GetSceneTypeResponse();
		Long userId = cmd.getUid() == null ? UserContext.currentUserId() : cmd.getUid();
		String sceneType = SceneType.PARK_TOURIST.getCode();
		RentalResourceType resourceType = rentalv2Provider.findRentalResourceTypeById(cmd.getResourceTypeId());
		GetAuthOrgByProjectIdAndAppIdCommand cmd2 = new GetAuthOrgByProjectIdAndAppIdCommand();
		cmd2.setProjectId(cmd.getCommunityId());
		cmd2.setAppId(cmd.getAppid());
		OrganizationDTO manageOrganization = organizationService.getAuthOrgByProjectIdAndAppId(cmd2);//管理公司
		if (resourceType != null && TrueOrFalseFlag.TRUE.getCode().equals(resourceType.getCrossCommuFlag())){
			List<Long> communityIds = new ArrayList<>();
			if (UserContext.getCurrentNamespaceId().equals(2)) { //标准版
				List<ServiceModuleAppAuthorization> appAuthorizations = serviceModuleAppAuthorizationService.listCommunityRelationOfOrgIdAndAppId(UserContext.getCurrentNamespaceId(),
						manageOrganization.getId(), cmd.getAppid());
				communityIds = appAuthorizations.stream().map(r->r.getProjectId()).collect(Collectors.toList());
			}else{
				List<Community> communities = communityProvider.listCommunitiesByNamespaceId(UserContext.getCurrentNamespaceId());
				communityIds = communities.stream().map(Community::getId).collect(Collectors.toList());
			}
			for (Long communityId : communityIds){
				ListUserOrganizationsCommand cmd3 = new ListUserOrganizationsCommand();
				cmd3.setAppId(cmd.getAppid());
				cmd3.setProjectId(communityId);
				cmd3.setUserId(userId);
				ListUserOrganizationsResponse response1 = organizationService.listUserOrganizations(cmd3);
				if (response1 != null && response1.getDtos() != null){
					for (OrganizationDTO dto : response1.getDtos()) {
						if (dto.getId().equals(cmd.getOrganizationId())){
							if (TrueOrFalseFlag.TRUE.getCode().equals(dto.getProjectManageFlag()))
								sceneType = SceneType.PM_ADMIN.getCode();
							else if (sceneType.equals(SceneType.PARK_TOURIST.getCode()))
										sceneType = SceneType.ENTERPRISE.getCode();
							break;
						}
					}
				}
			}
		}else{
			ListUserOrganizationsCommand cmd3 = new ListUserOrganizationsCommand();
			cmd3.setAppId(cmd.getAppid());
			cmd3.setProjectId(cmd.getCommunityId());
			cmd3.setUserId(userId);
			ListUserOrganizationsResponse response1 = organizationService.listUserOrganizations(cmd3);
			if (response1 != null && response1.getDtos() != null){
				for (OrganizationDTO dto : response1.getDtos()) {
					if (dto.getId().equals(cmd.getOrganizationId())){
						if (TrueOrFalseFlag.TRUE.getCode().equals(dto.getProjectManageFlag()))
							sceneType = SceneType.PM_ADMIN.getCode();
						else
							sceneType = SceneType.ENTERPRISE.getCode();
						break;
					}
				}
			}
		}
		response.setAllowRent(TrueOrFalseFlag.TRUE.getCode());
		if (!SceneType.PM_ADMIN.getCode().equals(sceneType)
				&& !SceneType.ENTERPRISE.getCode().equals(sceneType)
				&& TrueOrFalseFlag.fromCode(resourceType.getUnauthVisible()) != TrueOrFalseFlag.TRUE){
			response.setAllowRent(TrueOrFalseFlag.FALSE.getCode());
		}

		//会员等级
		User user = userProvider.findUserById(userId);
		if (user.getVipLevelText() != null){
			sceneType = sceneType + "," + user.getVipLevelText();
		}
		response.setSceneType(sceneType);
		return response;
	}

	@Override
	public SearchShopsResponse searchShops(SearchShopsCommand cmd) {
		SearchShopsResponse response = new SearchShopsResponse();
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		String bizServer = configurationProvider.getValue("stat.biz.server.url", "");
		String bizApi = "/zl-ec/rest/openapi/shop/queryShopInfoByNamespace";
		Map<String, Object> param = new HashMap<>();
		param.put("namespaceId", namespaceId);
		param.put("keyword", cmd.getKeyword());
		try {
			String jsonStr = HttpUtils.postJson((bizServer + bizApi), StringHelper.toJsonString(param), 1000, "UTF-8");
			SearchShopsResponse searchShopsResponse = (SearchShopsResponse) StringHelper.fromJsonString(jsonStr,SearchShopsResponse.class);
			if (searchShopsResponse != null && searchShopsResponse.getResult() && searchShopsResponse.getBody() != null){
				return searchShopsResponse;
			}
		}catch (Exception e) {
			LOGGER.error("biz server response error", e);
		}

		return response;
	}
}
