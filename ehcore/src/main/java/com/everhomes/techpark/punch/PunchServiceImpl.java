package com.everhomes.techpark.punch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.approval.ApprovalCategory;
import com.everhomes.approval.ApprovalCategoryProvider;
import com.everhomes.approval.ApprovalRequestDefaultHandler;
import com.everhomes.approval.ApprovalRule;
import com.everhomes.approval.ApprovalRuleProvider;
import com.everhomes.approval.ApprovalService;
import com.everhomes.approval.ApprovalServiceConstants;
import com.everhomes.archives.ArchivesService;
import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.bus.BusBridgeProvider;
import com.everhomes.bus.LocalBus;
import com.everhomes.bus.LocalBusOneshotSubscriber;
import com.everhomes.bus.LocalBusOneshotSubscriberBuilder;
import com.everhomes.bus.LocalBusSubscriber;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.enterpriseApproval.EnterpriseApprovalService;
import com.everhomes.entity.EntityType;
import com.everhomes.filedownload.TaskService;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseProvider;
import com.everhomes.general_approval.GeneralApproval;
import com.everhomes.general_approval.GeneralApprovalConstants;
import com.everhomes.general_approval.GeneralApprovalService;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.locale.LocaleString;
import com.everhomes.locale.LocaleStringProvider;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.naming.NameMapper;
import com.everhomes.openapi.AppNamespaceMapping;
import com.everhomes.openapi.AppNamespaceMappingProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.portal.PortalService;
import com.everhomes.rentalv2.RentalNotificationTemplateCode;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.acl.PrivilegeServiceErrorCode;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.approval.ApprovalCategoryDTO;
import com.everhomes.rest.approval.ApprovalCategoryStatus;
import com.everhomes.rest.approval.ApprovalCategoryTimeSelectType;
import com.everhomes.rest.approval.ApprovalCategoryTimeUnit;
import com.everhomes.rest.approval.ApprovalOwnerType;
import com.everhomes.rest.approval.ApprovalType;
import com.everhomes.rest.approval.ExceptionRequestDTO;
import com.everhomes.rest.approval.ListApprovalCategoryCommand;
import com.everhomes.rest.common.PunchClockRecordData;
import com.everhomes.rest.common.Router;
import com.everhomes.rest.enterpriseApproval.EnterpriseApprovalRecordDTO;
import com.everhomes.rest.filedownload.TaskRepeatFlag;
import com.everhomes.rest.filedownload.TaskType;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.general_approval.GeneralApprovalAttribute;
import com.everhomes.rest.launchpad.ActionType;
import com.everhomes.rest.messaging.ChannelType;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessageMetaConstant;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.messaging.MetaObjectType;
import com.everhomes.rest.messaging.RouterMetaObject;
import com.everhomes.rest.openapi.CheckInDataDTO;
import com.everhomes.rest.openapi.GetOrgCheckInDataCommand;
import com.everhomes.rest.openapi.GetOrgCheckInDataResponse;
import com.everhomes.rest.organization.EmployeeStatus;
import com.everhomes.rest.organization.ListOrganizationContactCommand;
import com.everhomes.rest.organization.ListOrganizationMemberCommandResponse;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationMemberDTO;
import com.everhomes.rest.organization.OrganizationMemberStatus;
import com.everhomes.rest.organization.OrganizationMemberTargetType;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.rest.print.PrintErrorCode;
import com.everhomes.rest.techpark.punch.*;
import com.everhomes.rest.techpark.punch.admin.AddPunchGroupCommand;
import com.everhomes.rest.techpark.punch.admin.AddPunchPointCommand;
import com.everhomes.rest.techpark.punch.admin.AddPunchTimeRuleCommand;
import com.everhomes.rest.techpark.punch.admin.AddPunchWiFiCommand;
import com.everhomes.rest.techpark.punch.admin.DeleteCommonCommand;
import com.everhomes.rest.techpark.punch.admin.DeletePunchRuleMapCommand;
import com.everhomes.rest.techpark.punch.admin.GetPunchGroupCommand;
import com.everhomes.rest.techpark.punch.admin.GetPunchGroupsCountCommand;
import com.everhomes.rest.techpark.punch.admin.GetPunchGroupsCountResponse;
import com.everhomes.rest.techpark.punch.admin.GetTargetPunchAllRuleCommand;
import com.everhomes.rest.techpark.punch.admin.GetUserPunchRuleInfoCommand;
import com.everhomes.rest.techpark.punch.admin.GetUserPunchRuleInfoResponse;
import com.everhomes.rest.techpark.punch.admin.ListApprovalCategoriesResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchDetailsCommand;
import com.everhomes.rest.techpark.punch.admin.ListPunchDetailsResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchGroupsCommand;
import com.everhomes.rest.techpark.punch.admin.ListPunchGroupsResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchMonthLogsCommand;
import com.everhomes.rest.techpark.punch.admin.ListPunchMonthLogsResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchPointsCommand;
import com.everhomes.rest.techpark.punch.admin.ListPunchPointsResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchRuleMapsCommand;
import com.everhomes.rest.techpark.punch.admin.ListPunchRuleMapsResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchRulesCommonCommand;
import com.everhomes.rest.techpark.punch.admin.ListPunchRulesResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchSchedulingMonthCommand;
import com.everhomes.rest.techpark.punch.admin.ListPunchSchedulingMonthResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchWiFiRuleListResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchWiFisResponse;
import com.everhomes.rest.techpark.punch.admin.ListPunchWorkdayRuleListResponse;
import com.everhomes.rest.techpark.punch.admin.PunchDayDetailDTO;
import com.everhomes.rest.techpark.punch.admin.PunchGroupDTO;
import com.everhomes.rest.techpark.punch.admin.PunchLocationRuleDTO;
import com.everhomes.rest.techpark.punch.admin.PunchSchedulingDTO;
import com.everhomes.rest.techpark.punch.admin.PunchSchedulingEmployeeDTO;
import com.everhomes.rest.techpark.punch.admin.PunchSpecialDayDTO;
import com.everhomes.rest.techpark.punch.admin.PunchTargetType;
import com.everhomes.rest.techpark.punch.admin.PunchWiFiDTO;
import com.everhomes.rest.techpark.punch.admin.PunchWiFiRuleDTO;
import com.everhomes.rest.techpark.punch.admin.PunchWorkdayRuleDTO;
import com.everhomes.rest.techpark.punch.admin.QryPunchLocationRuleListResponse;
import com.everhomes.rest.techpark.punch.admin.UpdatePunchPointCommand;
import com.everhomes.rest.techpark.punch.admin.UpdatePunchSchedulingMonthCommand;
import com.everhomes.rest.techpark.punch.admin.UpdatePunchTimeRuleCommand;
import com.everhomes.rest.techpark.punch.admin.UpdateTargetPunchAllRuleCommand;
import com.everhomes.rest.techpark.punch.admin.UserMonthLogsDTO;
import com.everhomes.rest.techpark.punch.admin.listPunchTimeRuleListResponse;
import com.everhomes.rest.uniongroup.GetUniongroupConfiguresCommand;
import com.everhomes.rest.uniongroup.SaveUniongroupConfiguresCommand;
import com.everhomes.rest.uniongroup.UniongroupConfiguresDTO;
import com.everhomes.rest.uniongroup.UniongroupTarget;
import com.everhomes.rest.uniongroup.UniongroupTargetType;
import com.everhomes.rest.uniongroup.UniongroupType;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhOrganizationMembers;
import com.everhomes.server.schema.tables.pojos.EhPunchSchedulings;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.DateUtil;
import com.everhomes.socialSecurity.SocialSecurityService;
import com.everhomes.techpark.punch.recordmapper.DailyPunchStatusStatisticsHistoryRecordMapper;
import com.everhomes.techpark.punch.recordmapper.DailyPunchStatusStatisticsTodayRecordMapper;
import com.everhomes.techpark.punch.recordmapper.DailyStatisticsByDepartmentBaseRecordMapper;
import com.everhomes.techpark.punch.recordmapper.MonthlyPunchStatusStatisticsRecordMapper;
import com.everhomes.techpark.punch.recordmapper.MonthlyStatisticsByDepartmentRecordMapper;
import com.everhomes.techpark.punch.recordmapper.MonthlyStatisticsByMemberRecordMapper;
import com.everhomes.techpark.punch.recordmapper.PunchExceptionRequestStatisticsRecordMapper;
import com.everhomes.techpark.punch.recordmapper.PunchStatisticsParser;
import com.everhomes.techpark.punch.utils.PunchDateUtils;
import com.everhomes.techpark.punch.utils.PunchDayParseUtils;
import com.everhomes.uniongroup.UniongroupConfigureProvider;
import com.everhomes.uniongroup.UniongroupConfigures;
import com.everhomes.uniongroup.UniongroupMemberDetail;
import com.everhomes.uniongroup.UniongroupService;
import com.everhomes.uniongroup.UniongroupVersion;
import com.everhomes.uniongroup.UniongroupVersionProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.user.UserProvider;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.ExecutorUtil;
import com.everhomes.util.RouterBuilder;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.Tuple;
import com.everhomes.util.Version;
import com.everhomes.util.WebTokenGenerator;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.elasticsearch.common.cli.CliToolConfig.Cmd;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class PunchServiceImpl implements PunchService {
    private static final Long ONE_DAY_MS = 24 * 3600 * 1000L;
    private static final Long ONE_MINUTE_MS = 60 * 1000L;

    private static final List<String> timeIntervalApprovalAttribute;
    private static final List<String> SMART_APPROVAL_ATTRIBUTES = Arrays.asList(GeneralApprovalAttribute.ASK_FOR_LEAVE.getCode(), GeneralApprovalAttribute.BUSINESS_TRIP.getCode(), GeneralApprovalAttribute.GO_OUT.getCode());

    static {
        timeIntervalApprovalAttribute = new ArrayList<>();
        timeIntervalApprovalAttribute.add(GeneralApprovalAttribute.ASK_FOR_LEAVE.getCode());
        timeIntervalApprovalAttribute.add(GeneralApprovalAttribute.BUSINESS_TRIP.getCode());
        timeIntervalApprovalAttribute.add(GeneralApprovalAttribute.GO_OUT.getCode());
        timeIntervalApprovalAttribute.add(GeneralApprovalAttribute.OVERTIME.getCode());
    }

    @Override
    public List<String> getTimeIntervalApprovalAttribute() {
        return timeIntervalApprovalAttribute;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(PunchServiceImpl.class);
    @Autowired
    private FlowCaseProvider flowCaseProvider;

    private ThreadLocal<SimpleDateFormat> dayStatusSF = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("MM-dd EEE",Locale.SIMPLIFIED_CHINESE);
        }
    };
    private ThreadLocal<SimpleDateFormat> dateSF = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };
    private ThreadLocal<SimpleDateFormat> timeSF = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("HH:mm:ss");
        }
    };
    private ThreadLocal<SimpleDateFormat> monthSF = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMM");
        }
    };
    private ThreadLocal<SimpleDateFormat> monthChineseSF = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy年MM月");
        }
    };
    private ThreadLocal<SimpleDateFormat> datetimeChineseSF = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        }
    };
    private ThreadLocal<SimpleDateFormat> datetimeSF = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    public static final Integer CONFIG_VERSION_CODE = 0;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ArchivesService archivesService;
    @Autowired
    private GeneralApprovalService generalApprovalService;
	@Autowired
	private AppNamespaceMappingProvider appNamespaceMappingProvider;
    @Autowired
    private ContentServerService contentServerService;
    @Autowired
    private PunchMonthReportProvider punchMonthReportProvider;
    @Autowired
    private SequenceProvider sequenceProvider;
    @Autowired
    private ScheduleProvider scheduleProvider;
    @Autowired
    private PunchOperationLogProvider punchOperationLogProvider;
    @Autowired
    private PunchProvider punchProvider;
    @Autowired
    private PunchSchedulingProvider punchSchedulingProvider;
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private DbProvider dbProvider;
    @Autowired
    private UniongroupConfigureProvider uniongroupConfigureProvider;
    @Autowired
    private UniongroupService uniongroupService;
    @Autowired
    private UniongroupVersionProvider uniongroupVersionProvider;
    @Autowired
    private ConfigurationProvider configurationProvider;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private OrganizationProvider organizationProvider;
    @Autowired
    private ApprovalRuleProvider approvalRuleProvider;
    @Autowired
    private ApprovalCategoryProvider approvalCategoryProvider;
    @Autowired
    private LocaleStringProvider localeStringProvider;
    @Autowired
    private LocaleStringService localeStringService;
    @Autowired
    private MessagingService messagingService;
    @Autowired
    private CoordinationProvider coordinationProvider;
    @Autowired
    private LocaleTemplateService localeTemplateService;
    @Autowired
    private BigCollectionProvider bigCollectionProvider;
    @Autowired
    private LocalBusOneshotSubscriberBuilder localBusSubscriberBuilder;
    @Autowired
    private LocalBus localBus;
    @Autowired
    private BusBridgeProvider busBridgeProvider;
    @Autowired
    private PortalService portalService;
    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;
    @Autowired
    private PunchVacationBalanceProvider punchVacationBalanceProvider;
    @Autowired
    private SocialSecurityService socialSecurityService;
    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private EnterpriseApprovalService enterpriseApprovalService; // add by ryan for enterprise approval instead of general approval.
    @Autowired
    private PunchNotificationService punchNotificationService;
    @Autowired
    private PunchBaseService punchBaseService;

    @Override
    public void checkAppPrivilege(Long orgId, Long checkOrgId, Long privilege) {
        if (checkBooleanAppPrivilege(orgId, checkOrgId, privilege)) {
            return;
        }
        LOGGER.error("Permission is prohibited, namespaceId={}, taskCategoryId={}, orgId={}, ownerType={}, ownerId={}," +
                        " privilege={},check org id = {}", UserContext.getCurrentNamespaceId(), "", orgId, EntityType.COMMUNITY.getCode(),
                orgId, privilege, checkOrgId);
        throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_CHECK_APP_PRIVILEGE,
                "check app privilege error");
    }

    private boolean checkBooleanAppPrivilege(Long orgId, Long checkOrgId, Long privilege) {
        ListServiceModuleAppsCommand cmd = new ListServiceModuleAppsCommand();
        cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
        cmd.setModuleId(PunchConstants.PUNCH_MODULE_ID);
        cmd.setActionType(ActionType.PUNCH.getCode());
        ListServiceModuleAppsResponse apps = portalService.listServiceModuleAppsWithConditon(cmd);
        Long appId = null;
        if (null != apps && apps.getServiceModuleApps().size() > 0) {
            appId = apps.getServiceModuleApps().get(0).getOriginId();
        }
        if (null != apps) {
            if (userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), EntityType.ORGANIZATIONS.getCode(), orgId,
                    orgId, privilege, appId, checkOrgId, null)) {
                return true;
            }
        }
        return false;
    }

    private void checkCompanyIdIsNull(Long companyId) {
        if (null == companyId || companyId.equals(0L)) {
            LOGGER.error("Invalid company Id parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid company Id parameter in the command");
        }

    }

    /**
     * <ul>审批后的状态
     * <li></li>
     * <li>HALFOUTWORK(13):  半天公出</li>
     * <li>HALFEXCHANGE(12):  半天调休</li>
     * <li>HALFABSENCE(11):  半天病假</li>
     * <li>HALFSICK(10):  半天事假</li>
     * <li>OVERTIME(9):  加班</li>
     * <li>OUTWORK(8):  公出</li>
     * <li>EXCHANGE(7): 调休</li>
     * <li>SICK(6): 病假</li>
     * <li>ABSENCE(5): 事假</li>
     * <li>BLANDLE(4): 迟到且早退</li>
     * <li>UNPUNCH(3): 缺勤</li>
     * <li>LEAVEEARLY(2): 早退</li>
     * <li>BELATE(1): 迟到</li>
     * <li>NORMAL(0): 正常</li>
     * </ul>
     */
    @Override
    public String statusToString(Timestamp splitDate, Byte status) {
        if (null == status) {
            return "";
        }
        String code = status.toString();
        // 当天未打卡，（即打卡时间未过分界点），不显示旷工而是未打卡
        if (PunchStatus.UNPUNCH == PunchStatus.fromCode(status) && splitDate != null && splitDate.getTime() > System.currentTimeMillis()) {
            code = String.valueOf(PunchStatus.UN_PUNCH_CHECKING.getCode());
        }
        LocaleString localeString = localeStringProvider.find(PunchConstants.PUNCH_STATUS_SCOPE, code, PunchConstants.locale);
        if (null == localeString)
            return "";
        return localeString.getText();
    }

    /**
     * 获取公司当前的version,如果没有,则新建一个从1开始
     */
    public Integer getPunchGroupCurrentVersion(Long enterpriseId) {
        return punchBaseService.getPunchGroupVersion(enterpriseId).getCurrentVersionCode();
    }

    @Override
    public ListYearPunchLogsCommandResponse getlistPunchLogs(
            ListYearPunchLogsCommand cmd) {

        checkCompanyIdIsNull(cmd.getEnterpriseId());
        if (cmd.getQueryYear() == null || cmd.getQueryYear().isEmpty())
            throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
                    ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid queryYear parameter in the command");

        ListYearPunchLogsCommandResponse pyl = new ListYearPunchLogsCommandResponse();
        pyl.setPunchYear(cmd.getQueryYear());
        pyl.setPunchLogsMonthList(new ArrayList<PunchLogsMonthList>());
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        LOGGER.debug("*****************debug : begin getlistPunchLogs line 87");
        try {
            // 从年初开始，如果是查询今年，到今天截止，如果不是查询今年，则到该年年末
            // 如果要修改，只需要修改范围即可
            start.setTime(dateSF.get().parse(cmd.getQueryYear() + "-01-01"));
            if (end.before(start)) {
                throw RuntimeErrorException.errorWith(
                        PunchServiceErrorCode.SCOPE,
                        PunchServiceErrorCode.ERROR_QUERY_YEAR_ERROR,
                        "query Year is later than now ,please check again ");
            }
            if (start.get(Calendar.YEAR) != end.get(Calendar.YEAR)) {
                end.setTime(dateSF.get().parse(cmd.getQueryYear() + "-01-01"));
                end.add(Calendar.YEAR, 1);
            }
        } catch (ParseException e) {
            throw RuntimeErrorException
                    .errorWith(PunchServiceErrorCode.SCOPE,
                            PunchServiceErrorCode.ERROR_QUERY_YEAR_ERROR,
                            "there is something wrong with queryYear,please check again ");
        }

        pyl = getlistPunchLogsBetweenTwoCalendar(pyl, cmd.getEnterpriseId(),
                start, end);
        return pyl;
    }

    @Override
    public ListYearPunchLogsCommandResponse getlistPunchLogsBetweenTwoCalendar(
            ListYearPunchLogsCommandResponse pyl, long CompanyId,
            Calendar start, Calendar end) {
        Long userId = UserContext.current().getUser().getId();
        PunchLogsMonthList pml = null;
        while (start.before(end)) {

            if (null == pml) {
                // 如果pml为空，即是循环第一次，建立新的pml
                pml = new PunchLogsMonthList();
                pml.setPunchMonth(String.valueOf(start.get(Calendar.MONTH) + 1));
                pml.setPunchLogsDayListInfos(new ArrayList<PunchLogsDay>());
                pyl.getPunchLogsMonthList().add(pml);
            } else if (!pml.getPunchMonth().equals(
                    String.valueOf(start.get(Calendar.MONTH) + 1))) {
                // 如果pml的月份和start不一样，建立新的pml
                pml = new PunchLogsMonthList();
                pml.setPunchMonth(String.valueOf(start.get(Calendar.MONTH) + 1));
                pml.setPunchLogsDayListInfos(new ArrayList<PunchLogsDay>());
                pyl.getPunchLogsMonthList().add(pml);
            }

            try {
                PunchLogsDay pdl = makePunchLogsDayStatus(userId,
                        CompanyId, start);
                if (null != pdl) {
                    pdl.setPunchStatusNew(pdl.getPunchStatus());
                    pdl.setMorningPunchStatusNew(pdl.getMorningPunchStatus());
                    pdl.setAfternoonPunchStatusNew(pdl.getAfternoonPunchStatus());
                    if (pdl.getPunchStatus() != null && ApprovalStatus.FORGOT.equals(ApprovalStatus.fromCode(pdl.getPunchStatus())))
                        pdl.setPunchStatus(ApprovalStatus.UNPUNCH.getCode());
                    if (pdl.getMorningPunchStatus() != null && ApprovalStatus.FORGOT.equals(ApprovalStatus.fromCode(pdl.getMorningPunchStatus())))
                        pdl.setMorningPunchStatus(ApprovalStatus.UNPUNCH.getCode());
                    if (pdl.getAfternoonPunchStatus() != null && ApprovalStatus.FORGOT.equals(ApprovalStatus.fromCode(pdl.getAfternoonPunchStatus())))
                        pdl.setAfternoonPunchStatus(ApprovalStatus.UNPUNCH.getCode());
                    pdl.setApprovalStatusNew(pdl.getApprovalStatus());
                    pdl.setMorningApprovalStatusNew(pdl.getMorningApprovalStatus());
                    pdl.setAfternoonApprovalStatusNew(pdl.getAfternoonApprovalStatus());
                    if (pdl.getApprovalStatus() != null && ApprovalStatus.FORGOT.equals(ApprovalStatus.fromCode(pdl.getApprovalStatus())))
                        pdl.setApprovalStatus(ApprovalStatus.UNPUNCH.getCode());
                    if (pdl.getMorningApprovalStatus() != null && ApprovalStatus.FORGOT.equals(ApprovalStatus.fromCode(pdl.getMorningApprovalStatus())))
                        pdl.setMorningApprovalStatus(ApprovalStatus.UNPUNCH.getCode());
                    if (pdl.getAfternoonApprovalStatus() != null && ApprovalStatus.FORGOT.equals(ApprovalStatus.fromCode(pdl.getAfternoonApprovalStatus())))
                        pdl.setAfternoonApprovalStatus(ApprovalStatus.UNPUNCH.getCode());
                    pml.getPunchLogsDayList().add(pdl);
                }
            } catch (ParseException e) {

                throw RuntimeErrorException.errorWith(
                        PunchServiceErrorCode.SCOPE,
                        ErrorCodes.ERROR_INVALID_PARAMETER,
                        "punch Rule has somthing wrong");
            }
            start.add(Calendar.DAY_OF_MONTH, 1);
        }
        return pyl;
    }

    /**
     * 从数据库取某天某人的打卡记录,如果为空则计算.
     * <p>
     * 并取审批状态
     */
    private PunchLogsDay makePunchLogsDayStatus(Long userId,
                                                Long companyId, Calendar logDay) throws ParseException {
        PunchLogsDay pdl = new PunchLogsDay();
        pdl.setPunchDay(String.valueOf(logDay.get(Calendar.DAY_OF_MONTH)));
        // Long beginFunctionTimeLong = DateHelper.currentGMTTime().getTime();
        PunchDayLog punchDayLog = punchProvider.getDayPunchLogByDateAndUserId(userId,
                companyId, dateSF.get().format(logDay.getTime()));
        if (null == punchDayLog) {
            // 插入数据
            OrganizationMemberDetails memberDetail = organizationProvider.findOrganizationMemberDetailsByTargetIdAndOrgId(userId, companyId);
            punchDayLog = refreshPunchDayLog(memberDetail, logDay);
            if (null == punchDayLog) {
                // 验证后为空
                return null;
            }
        }
        pdl.setWorkTime(convertTimeToGMTMillisecond(punchDayLog.getWorkTime()));

        if (punchDayLog.getPunchTimesPerDay().equals(PunchTimesPerDay.TWICE.getCode())) {
            if (!StringUtils.isEmpty(punchDayLog.getStatusList()) && null == punchDayLog.getStatus()) {
                punchDayLog.setStatus(Byte.valueOf(punchDayLog.getStatusList()));
            }
            pdl.setPunchStatus(punchDayLog.getStatus());

            pdl.setExceptionStatus(punchDayLog.getStatus().equals(
                    PunchStatus.NORMAL.getCode()) || punchDayLog.getStatus().equals(
                    ApprovalStatus.OVERTIME.getCode()) ? ExceptionStatus.NORMAL
                    .getCode() : ExceptionStatus.EXCEPTION.getCode());

            PunchExceptionApproval exceptionApproval = punchProvider
                    .getPunchExceptionApprovalByDate(userId, companyId,
                            dateSF.get().format(logDay.getTime()));
            if (null != exceptionApproval) {
                pdl.setApprovalStatus(exceptionApproval.getApprovalStatus());

                // 如果有申报审批结果，并且审批结果和实际打卡结果有一个是正常的话，异常结果为正常 别的为异常
                pdl.setExceptionStatus(calculateExceptionCode(exceptionApproval.getApprovalStatus()));

            }
        } else if (punchDayLog.getPunchTimesPerDay().equals(PunchTimesPerDay.FORTH.getCode())) {
            if (!StringUtils.isEmpty(punchDayLog.getStatusList())) {
                String[] status = punchDayLog.getStatusList().split(PunchConstants.STATUS_SEPARATOR);
                if (status.length <= 1) {
                    punchDayLog.setMorningStatus(Byte.valueOf(status[0]));
                    punchDayLog.setAfternoonStatus(Byte.valueOf(status[0]));
                } else {
                    punchDayLog.setMorningStatus(Byte.valueOf(status[0]));
                    punchDayLog.setAfternoonStatus(Byte.valueOf(status[1]));
                }
            }
            pdl.setMorningPunchStatus(punchDayLog.getMorningStatus());
            pdl.setAfternoonPunchStatus(punchDayLog.getAfternoonStatus());
            pdl.setExceptionStatus(punchDayLog.getExceptionStatus());
            PunchExceptionApproval exceptionApproval = punchProvider.getPunchExceptionApprovalByDate(userId, companyId,
                    dateSF.get().format(logDay.getTime()));
            if (null != exceptionApproval) {
                pdl.setMorningApprovalStatus(exceptionApproval.getMorningApprovalStatus());
                pdl.setAfternoonApprovalStatus(exceptionApproval.getAfternoonApprovalStatus());
                Byte morningStatus = pdl.getMorningPunchStatus();
                if (null != exceptionApproval.getMorningApprovalStatus())
                    morningStatus = exceptionApproval.getMorningApprovalStatus();
                Byte afternoonStatus = pdl.getAfternoonPunchStatus();
                if (null != exceptionApproval.getAfternoonApprovalStatus())
                    afternoonStatus = exceptionApproval.getAfternoonApprovalStatus();
                if (calculateExceptionCode(afternoonStatus).equals(ExceptionStatus.NORMAL.getCode())
                        && calculateExceptionCode(morningStatus).equals(ExceptionStatus.NORMAL.getCode())) {

                    pdl.setExceptionStatus(ExceptionStatus.NORMAL.getCode());
                }
            }

        }

        return pdl;
    }

    private Byte calculateExceptionCode(Byte status) {
        if (status == null) {
            return ExceptionStatus.NORMAL.getCode();
        }
        if (status.equals(ApprovalStatus.NORMAL.getCode()) || status.equals(ApprovalStatus.OVERTIME.getCode())
                || status.equals(ApprovalStatus.EXCHANGE.getCode()) || status.equals(ApprovalStatus.OVERTIME.getCode())
                || status.equals(ApprovalStatus.NORMAL.getCode()) || status.equals(ApprovalStatus.OUTWORK.getCode())
                || status.equals(ApprovalStatus.SICK.getCode()) || status.equals(ApprovalStatus.ABSENCE.getCode())
                || status.equals(ApprovalStatus.HALFSICK.getCode()) || status.equals(ApprovalStatus.HALFABSENCE.getCode())
                || status.equals(ApprovalStatus.HALFEXCHANGE.getCode()) || status.equals(ApprovalStatus.HALFOUTWORK.getCode()))
            return ExceptionStatus.NORMAL.getCode();

        return ExceptionStatus.EXCEPTION.getCode();
    }

    /**
     * 刷新取某人某日的打卡日状态
     */
    @Override
    public PunchDayLog refreshPunchDayLog(OrganizationMemberDetails memberDetail, Calendar logDay) throws ParseException {
        //每一个user建立dayLog的地方加锁
        PunchDayLog newPunchDayLog = new PunchDayLog();
        this.coordinationProvider.getNamedLock(CoordinationLocks.CREATE_PUNCH_LOG.getCode() + memberDetail.getId()).enter(() -> {

            PunchDayLog punchDayLog = punchProvider.getDayPunchLogByDateAndDetailId(memberDetail.getId(), memberDetail.getOrganizationId(), dateSF.get().format(logDay.getTime()));

            refreshPunchDayLog(memberDetail, punchDayLog, logDay, newPunchDayLog);
            return null;
        });
        return newPunchDayLog;
    }

    private ThreadLocal<Map<Long, PunchTimeRule>> timeRuleCache = new ThreadLocal<Map<Long, PunchTimeRule>>() {
        protected Map<Long, PunchTimeRule> initialValue() {
            return new HashMap<>();
        }
    };


    /*
     * 刷新一个pdl记录
	 * @param userId : 要刷新的用户id
	 * @param companyId : 公司id
	 * @param punchDayLog : 旧的pdl,为null就新增,不为null就覆盖(只用到id)
	 * @param logDay : 打卡日期
     * @param ptr : 传参就是保持原本的打卡规则,不传参就是用新的打卡规则
	 * @param newPunchDayLog : 方法会计算出新的值写入这个对象
     */
    private void refreshPunchDayLog(OrganizationMemberDetails memberDetail, PunchDayLog oldPunchDayLog, Calendar logDay,PunchDayLog newPunchDayLog) {

        PunchLogsDay pdl = new PunchLogsDay();
        pdl.setExceptionStatus(ExceptionStatus.NORMAL.getCode());
        pdl.setPunchDay(String.valueOf(logDay.get(Calendar.DAY_OF_MONTH)));
        pdl.setPunchLogs(new ArrayList<PunchLogDTO>());

        try {
            pdl = calculateDayLog(memberDetail.getTargetId(), memberDetail.getId(), memberDetail.getOrganizationId(), logDay, pdl, newPunchDayLog, oldPunchDayLog);
        } catch (ParseException e) {
            LOGGER.error("error", e);
        }
        if (null == pdl) {
            return;
        }
        //2018-7-17 给pdl加上了detailId 和 deptId
        newPunchDayLog.setDetailId(memberDetail.getId());
        Long deptId = organizationService.getDepartmentByDetailIdAndOrgId(memberDetail.getId(), memberDetail.getOrganizationId());
    	newPunchDayLog.setDeptId(deptId);
    	
        newPunchDayLog.setApprovalStatusList(pdl.getApprovalStatusList());
        newPunchDayLog.setStatusList(pdl.getStatusList());
        if (null != pdl.getArriveTime())
            newPunchDayLog.setArriveTime(new Time(pdl.getArriveTime()));
        if (null != pdl.getLeaveTime())
            newPunchDayLog.setLeaveTime(new Time(pdl.getLeaveTime()));
        newPunchDayLog.setPunchCount(pdl.getPunchCount());
        newPunchDayLog.setUserId(memberDetail.getTargetId());
        newPunchDayLog.setEnterpriseId(memberDetail.getOrganizationId());
        newPunchDayLog.setCreatorUid(memberDetail.getTargetId());
        newPunchDayLog.setPunchDate(java.sql.Date.valueOf(dateSF.get().format(logDay
                .getTime())));
        newPunchDayLog.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
                .getTime()));
        newPunchDayLog.setPunchTimesPerDay(pdl.getPunchTimesPerDay());
        newPunchDayLog.setStatus(pdl.getPunchStatus());
        newPunchDayLog.setMorningStatus(pdl.getMorningPunchStatus());
        newPunchDayLog.setAfternoonStatus(pdl.getAfternoonPunchStatus());
        newPunchDayLog.setViewFlag(ViewFlags.NOTVIEW.getCode());
        newPunchDayLog.setExceptionStatus(pdl.getExceptionStatus());
        newPunchDayLog.setDeviceChangeFlag(getDeviceChangeFlag(memberDetail.getTargetId(), java.sql.Date.valueOf(dateSF.get().format(logDay.getTime())), memberDetail.getOrganizationId()));
        newPunchDayLog.setOvertimeTotalWorkday(pdl.getOvertimeTotalWorkday() != null ? pdl.getOvertimeTotalWorkday() : 0L);
        newPunchDayLog.setOvertimeTotalRestday(pdl.getOvertimeTotalRestday() != null ? pdl.getOvertimeTotalRestday() : 0L);
        newPunchDayLog.setOvertimeTotalLegalHoliday(pdl.getOvertimeTotalLegalHoliday() != null ? pdl.getOvertimeTotalLegalHoliday() : 0L);
        newPunchDayLog.setGoOutPunchFlag(punchProvider.processGoOutPunchFlag(pdl.getPunchDate(), memberDetail.getTargetId()));
        punchStatusCountByPunchDayLog(newPunchDayLog);
        if (null == oldPunchDayLog) {
            // 数据库没有计算好的数据
            punchProvider.createPunchDayLog(newPunchDayLog);

        } else {
            // 数据库有计算好的数据
            newPunchDayLog.setId(oldPunchDayLog.getId());
            punchProvider.updatePunchDayLog(newPunchDayLog);
        }
    }

    private Byte getDeviceChangeFlag(Long userId, java.sql.Date punchDate,
                                     Long companyId) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(punchDate);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Integer count = this.punchProvider.countPunchLogDevice(userId, companyId, new java.sql.Date(cal.getTime().getTime()), punchDate);
        if (count > 1)
            return NormalFlag.YES.getCode();
        return NormalFlag.NO.getCode();
    }

    /***
     * @param userId    ： 打卡用户;
     * @param companyId :打卡规则
     * @param logDay    : 计算的打卡日期
     * @return PunchLogsDayList：计算好的当日打卡状态
     */
    @Override
    public PunchLogsDay makePunchLogsDayListInfo(Long userId,
                                                 Long companyId, Calendar logDay) {
        Date now = new Date();

        PunchRule pr = this.getPunchRule(PunchOwnerType.ORGANIZATION.getCode(), companyId, userId);
        PunchDayLog punchDayLog = punchProvider.getDayPunchLogByDateAndUserId(userId,
                companyId, dateSF.get().format(logDay.getTime()));
        if (null == punchDayLog) {
            // 插入数据
            try {
                OrganizationMemberDetails memberDetail = organizationProvider.findOrganizationMemberDetailsByTargetIdAndOrgId(userId, companyId);
                punchDayLog = refreshPunchDayLog(memberDetail, logDay);
            } catch (ParseException e) {
                throw RuntimeErrorException.errorWith(
                        PunchServiceErrorCode.SCOPE,
                        PunchServiceErrorCode.ERROR_PUNCH_REFRESH_DAYLOG,
                        "ERROR IN REFRESHPUNCHDAYLOG  ");
            }
            if (null == punchDayLog) {
                // 验证后为空
                return new PunchLogsDay();
            }
        }
        PunchLogsDay pdl = ConvertHelper.convert(punchDayLog, PunchLogsDay.class);
        if (punchDayLog.getArriveTime() != null)
            pdl.setArriveTime(punchDayLog.getArriveTime().getTime());
        if (punchDayLog.getLeaveTime() != null)
            pdl.setLeaveTime(punchDayLog.getLeaveTime().getTime());
        if (punchDayLog.getAfternoonArriveTime() != null)
            pdl.setAfternoonArriveTime(punchDayLog.getAfternoonArriveTime().getTime());
        if (punchDayLog.getNoonLeaveTime() != null)
            pdl.setNoonLeaveTime(punchDayLog.getNoonLeaveTime().getTime());
        if (punchDayLog.getPunchTimesPerDay().equals(PunchTimesPerDay.TWICE.getCode())) {
            if (!StringUtils.isEmpty(punchDayLog.getStatusList()) && punchDayLog.getStatus() == null) {
                punchDayLog.setStatus(Byte.valueOf(punchDayLog.getStatusList()));
            }
        } else if (punchDayLog.getPunchTimesPerDay().equals(PunchTimesPerDay.FORTH.getCode())) {
            if (!StringUtils.isEmpty(punchDayLog.getStatusList())) {
                String[] status = punchDayLog.getStatusList().split(PunchConstants.STATUS_SEPARATOR);
                if (status.length <= 1) {
                    punchDayLog.setMorningStatus(Byte.valueOf(status[0]));
                    punchDayLog.setAfternoonStatus(Byte.valueOf(status[0]));
                } else {
                    punchDayLog.setMorningStatus(Byte.valueOf(status[0]));
                    punchDayLog.setAfternoonStatus(Byte.valueOf(status[1]));
                }
            }
        }
        pdl.setPunchStatus(punchDayLog.getStatus());
        pdl.setMorningPunchStatus(punchDayLog.getMorningStatus());
        pdl.setAfternoonPunchStatus(punchDayLog.getAfternoonStatus());
        pdl.setPunchDay(String.valueOf(logDay.get(Calendar.DAY_OF_MONTH)));
        pdl.setPunchLogs(new ArrayList<PunchLogDTO>());
        List<PunchLog> punchLogs = punchProvider.listPunchLogsByDate(userId,
                companyId, dateSF.get().format(logDay.getTime()),
                ClockCode.SUCESS.getCode());
        for (PunchLog log : punchLogs) {
            PunchLogDTO dto = ConvertHelper.convert(log, PunchLogDTO.class);
            dto.setPunchTime(log.getPunchTime().getTime());
            pdl.getPunchLogs().add(dto);
        }
        pdl.setPunchStatus(punchDayLog.getStatus());
        //通过打卡记录计算状态
        // 如果是非工作日和当天，则异常为normal
        if (!isWorkDay(logDay.getTime(), pr, userId)
                || dateSF.get().format(now).equals(dateSF.get().format(logDay.getTime()))) {
            pdl.setExceptionStatus(ExceptionStatus.NORMAL.getCode());
        } else {
            if (pdl.getPunchTimesPerDay().equals(PunchTimesPerDay.TWICE.getCode())) {
                pdl.setExceptionStatus(punchDayLog.getStatus().equals(
                        PunchStatus.NORMAL.getCode()) || punchDayLog.getStatus().equals(
                        ApprovalStatus.OVERTIME.getCode()) ? ExceptionStatus.NORMAL
                        .getCode() : ExceptionStatus.EXCEPTION.getCode());
            } else if (pdl.getPunchTimesPerDay().equals(PunchTimesPerDay.FORTH.getCode())) {
                //上午为加班或者普通 且 下午为加班或者普通 则
                pdl.setExceptionStatus((punchDayLog.getMorningStatus().equals(
                        PunchStatus.NORMAL.getCode()) || punchDayLog.getMorningStatus().equals(
                        ApprovalStatus.OVERTIME.getCode())) && (punchDayLog.getAfternoonStatus().equals(
                        PunchStatus.NORMAL.getCode()) || punchDayLog.getAfternoonStatus().equals(
                        ApprovalStatus.OVERTIME.getCode())) ? ExceptionStatus.NORMAL
                        .getCode() : ExceptionStatus.EXCEPTION.getCode());


            }
        }
        //通过审批计算状态
        PunchExceptionApproval exceptionApproval = punchProvider.getPunchExceptionApprovalByDate(userId, companyId,
                dateSF.get().format(logDay.getTime()));
        if (null != exceptionApproval) {
            pdl.setMorningApprovalStatus(exceptionApproval.getMorningApprovalStatus());
            pdl.setAfternoonApprovalStatus(exceptionApproval.getAfternoonApprovalStatus());
            if (calculateExceptionCode(pdl.getAfternoonApprovalStatus()).equals(ExceptionStatus.NORMAL.getCode())
                    && calculateExceptionCode(pdl.getMorningApprovalStatus()).equals(ExceptionStatus.NORMAL.getCode())) {

                pdl.setExceptionStatus(ExceptionStatus.NORMAL.getCode());
            }
        }
        List<PunchExceptionRequest> exceptionRequests = punchProvider
                .listExceptionRequestsByDate(userId, companyId,
                        dateSF.get().format(logDay.getTime()));
        if (exceptionRequests.size() > 0) {
            for (PunchExceptionRequest exceptionRequest : exceptionRequests) {
                //是否有请求的flag
                if (exceptionRequest.getApprovalStatus() != null) {
                    pdl.setRequestFlag(NormalFlag.YES.getCode());
                    FlowCase flowCase = flowCaseProvider.findFlowCaseByReferId(exceptionRequest.getRequestId(), ApprovalRequestDefaultHandler.REFER_TYPE, PunchConstants.PUNCH_MODULE_ID);
                    if (null != flowCase)
                        pdl.setRequestToken(ApprovalRequestDefaultHandler.processFlowURL(flowCase.getId(), FlowUserType.APPLIER.getCode(), flowCase.getModuleId()));
                    else
                        pdl.setRequestToken(WebTokenGenerator.getInstance().toWebToken(exceptionRequest.getRequestId()));
                }
                if (exceptionRequest.getMorningApprovalStatus() != null) {
                    pdl.setMorningRequestFlag(NormalFlag.YES.getCode());
                    FlowCase flowCase = flowCaseProvider.findFlowCaseByReferId(exceptionRequest.getRequestId(), ApprovalRequestDefaultHandler.REFER_TYPE, PunchConstants.PUNCH_MODULE_ID);
                    if (null != flowCase)
                        pdl.setMorningRequestToken(ApprovalRequestDefaultHandler.processFlowURL(flowCase.getId(), FlowUserType.APPLIER.getCode(), flowCase.getModuleId()));
                    else
                        pdl.setMorningRequestToken(WebTokenGenerator.getInstance().toWebToken(exceptionRequest.getRequestId()));
                }
                if (exceptionRequest.getAfternoonApprovalStatus() != null) {
                    pdl.setAfternoonRequestFlag(NormalFlag.YES.getCode());
                    FlowCase flowCase = flowCaseProvider.findFlowCaseByReferId(exceptionRequest.getRequestId(), ApprovalRequestDefaultHandler.REFER_TYPE, PunchConstants.PUNCH_MODULE_ID);
                    if (null != flowCase)
                        pdl.setAfternoonRequestToken(ApprovalRequestDefaultHandler.processFlowURL(flowCase.getId(), FlowUserType.APPLIER.getCode(), flowCase.getModuleId()));
                    else
                        pdl.setAfternoonRequestToken(WebTokenGenerator.getInstance().toWebToken(exceptionRequest.getRequestId()));
                }
                PunchExceptionDTO punchExceptionDTO = ConvertHelper.convert(exceptionRequest, PunchExceptionDTO.class);

                punchExceptionDTO.setRequestType(exceptionRequest
                        .getRequestType());
                punchExceptionDTO.setCreateTime(exceptionRequest
                        .getCreateTime().getTime());
                Organization organization = organizationProvider.findOrganizationById(companyId);
                if (exceptionRequest.getRequestType().equals(
                        PunchRquestType.REQUEST.getCode())) {
                    // 对于申请
                    punchExceptionDTO.setExceptionComment(exceptionRequest
                            .getDescription());
                    OrganizationMember member = findOrganizationMemberByOrgIdAndUId(exceptionRequest.getUserId(), organization.getPath());
                    if (null == member) {
                        punchExceptionDTO.setName("无此人");
                    } else {
                        punchExceptionDTO.setName(member.getContactName());
                    }
                } else {
                    // 审批
                    punchExceptionDTO.setExceptionComment(exceptionRequest
                            .getProcessDetails());
                    OrganizationMember member = findOrganizationMemberByOrgIdAndUId(exceptionRequest.getUserId(), organization.getPath());
                    if (null == member) {
                        punchExceptionDTO.setName("无此人");
                    } else {
                        punchExceptionDTO.setName(member.getContactName());
                    }
                }
                if (null == pdl.getPunchExceptionDTOs()) {
                    pdl.setPunchExceptionDTOs(new ArrayList<PunchExceptionDTO>());
                }
                pdl.getPunchExceptionDTOs().add(punchExceptionDTO);
            }
        }
        return pdl;
    }

    /***
     * 计算每一天的打卡状态，返回值PDL
     *
     * @param companyId   永远为总公司id
     * @param newPunchDayLog
     */
    private PunchLogsDay calculateDayLog(Long userId, Long detailId, Long companyId, Calendar logDay, PunchLogsDay pdl, PunchDayLog newPunchDayLog, PunchDayLog oldPunchDayLog) throws ParseException {
        pdl = calculateDayLogByeverypunch(userId, detailId, companyId, logDay, pdl, newPunchDayLog, oldPunchDayLog);
        //对statuslist进行统一处理,从每次打卡状态->每个班状态
        String[] statusArrary = pdl.getStatusList().split(PunchConstants.STATUS_SEPARATOR);
        String statusList = "";
        if (statusArrary == null) {
            // TODO: 2017/11/3  什么情况下这里是null,只有一个状态?那就要计算approvalStatus了
//			return pdl;
        } else if (statusArrary.length > 1) {
            for (int i = 0; i < statusArrary.length / 2; i++) {
                String status = processIntevalStatus(statusArrary[2 * i], statusArrary[2 * i + 1]);
                if (!status.equals(String.valueOf(PunchStatus.NORMAL.getCode()))) {
                    pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
                }
                if (i == 0) {
                    statusList = status;
                } else {
                    statusList = statusList + PunchConstants.STATUS_SEPARATOR + status;
                }
            }
            pdl.setStatusList(statusList);
        }

        String asList = "";
        if (null != pdl.getApprovalStatusList() && !StringUtils.isEmpty(pdl.getApprovalStatusList())) {
            String[] asArrary = org.apache.commons.lang.StringUtils.splitPreserveAllTokens(pdl.getApprovalStatusList(), PunchConstants.STATUS_SEPARATOR, statusArrary.length);
            pdl.setExceptionStatus(ExceptionStatus.NORMAL.getCode());
            for (int i = 0; i < statusArrary.length / 2; i++) {
                String approvalStatus = "";
                String a1 = asArrary[2 * i];
                String a2 = asArrary[2 * i + 1];
                if (StringUtils.isBlank(a1) && StringUtils.isBlank(a2)) {
                    //俩都是空,那就空
                    a1 = "";
                    a2 = "";
                    //按照打卡的状态来计算是否正常
                    String status = null;
                    if (null == statusArrary || statusArrary.length == 0) {
                        status = pdl.getStatusList();
                    } else {
                        status = processIntevalStatus(statusArrary[2 * i], statusArrary[2 * i + 1]);
                    }
                    if (!status.equals(String.valueOf(PunchStatus.NORMAL.getCode()))) {
                        pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
                    }

                    if (i == 0) {
                        asList = approvalStatus;
                    } else {
                        asList = asList + PunchConstants.STATUS_SEPARATOR + approvalStatus;
                    }
                    continue;
                } else if (StringUtils.isBlank(a1) || StringUtils.isBlank(a2)) {
                    //2个有一个为空就要拿status来匹配
                    if (StringUtils.isBlank(a1)) {
                        a1 = findPunchStatus(2 * i, statusArrary, pdl.getStatusList());
                    } else {
                        a2 = findPunchStatus(2 * i + 1, statusArrary, pdl.getStatusList());
                    }
                } else {
                    //两个都有审批结果那就用审批结果这里不用动
                }
                approvalStatus = processIntevalStatus(a1, a2);
                if (!approvalStatus.equals(String.valueOf(PunchStatus.NORMAL.getCode()))) {
                    pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
                }
                if (i == 0) {
                    asList = approvalStatus;
                } else {
                    asList = asList + PunchConstants.STATUS_SEPARATOR + approvalStatus;
                }

            }
            pdl.setApprovalStatusList(asList);
        }
        return pdl;
    }

    private String findPunchStatus(int i, String[] statusArrary, String statusList) {
        if (null == statusArrary || statusArrary.length == 0) {
            return statusList;
        } else {
            if (i >= statusArrary.length) {
                LOGGER.error("数组越界~ 状态列表是[" + statusArrary + "] 现在计算的是第[" + i + "]次打卡");
                return PunchStatus.UNPUNCH.getCode() + "";
            }
            return statusArrary[i];
        }
    }

    private String processIntevalStatus(String arrStatus, String leaveStatus) {
        // 上班卡状态是未打卡
        if (arrStatus.equals(String.valueOf(PunchStatus.UNPUNCH.getCode()))) {
            if (leaveStatus.equals(String.valueOf(PunchStatus.UNPUNCH.getCode()))) {
                return String.valueOf(PunchStatus.UNPUNCH.getCode());
            } else {
                return String.valueOf(PunchStatus.FORGOT_ON_DUTY.getCode());
            }
        }

        String status = "";
        if (leaveStatus.equals(String.valueOf(PunchStatus.UNPUNCH.getCode()))) {
            if (arrStatus.equals(String.valueOf(PunchStatus.BELATE.getCode()))) {
                status = String.valueOf(PunchStatus.BELATE_AND_FORGOT.getCode());
            } else if (arrStatus.equals(String.valueOf(PunchStatus.NORMAL.getCode()))) {
                status = String.valueOf(PunchStatus.FORGOT_OFF_DUTY.getCode());
            }
        } else if (leaveStatus.equals(String.valueOf(PunchStatus.LEAVEEARLY.getCode()))) {
            if (arrStatus.equals(String.valueOf(PunchStatus.BELATE.getCode()))) {
                status = String.valueOf(PunchStatus.BLANDLE.getCode());
            } else if (arrStatus.equals(String.valueOf(PunchStatus.NORMAL.getCode()))) {
                status = String.valueOf(PunchStatus.LEAVEEARLY.getCode());
            }
        } else if (leaveStatus.equals(String.valueOf(PunchStatus.NORMAL.getCode()))) {
            status = arrStatus;
        }
        return status;
    }

    private PunchLogsDay calculateDayLogByeverypunch(Long userId, Long detailId, Long companyId, Calendar logDay, PunchLogsDay pdl, PunchDayLog newPunchDayLog, PunchDayLog oldPunchDayLog) {
        java.sql.Date punchDate = java.sql.Date.valueOf(dateSF.get().format(logDay.getTime()));
        pdl.setPunchDate(punchDate);
        List<PunchLog> currentPunchLogs = punchProvider.listPunchLogsByDate(userId, companyId, dateSF.get().format(punchDate), ClockCode.SUCESS.getCode());
        List<PunchLog> punchLogs = new ArrayList<>();
        if (null != currentPunchLogs) {
            for (PunchLog log : currentPunchLogs) {
                if (log.getPunchTime() == null || log.getPunchStatus().equals(PunchStatus.UNPUNCH.getCode())) {
                    punchProvider.deletePunchLog(log);
                    continue;
                }
                punchLogs.add(log);
                pdl.getPunchLogs().add(ConvertHelper.convert(log, PunchLogDTO.class));
                if (null == pdl.getArriveTime() || pdl.getArriveTime() > log.getPunchTime().getTime())
                    pdl.setArriveTime(log.getPunchTime().getTime());
                if (null == pdl.getLeaveTime() || pdl.getLeaveTime() < log.getPunchTime().getTime())
                    pdl.setLeaveTime(log.getPunchTime().getTime());
            }
            pdl.setPunchCount(pdl.getPunchLogs().size());
            currentPunchLogs = null;
        } else {
            pdl.setPunchCount(0);
        }
        PunchRule pr = null;
        // 如果pdl还没有生成，则取最新的考勤规则,如果已生成取历史规则，否则不更新每日统计
        if (oldPunchDayLog == null) {
            pr = findPunchRuleByCache(PunchOwnerType.ORGANIZATION.getCode(), companyId, userId);
        } else if (oldPunchDayLog.getPunchOrganizationId() != null && oldPunchDayLog.getPunchOrganizationId() > 0) {
            pr = punchProvider.getPunchruleByPunchOrgId(oldPunchDayLog.getPunchOrganizationId());
        }
        if (pr == null) {
            LOGGER.error("userId={} have no punch rule", userId);
            throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
                    PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_SETTING,
                    "have no punch rule");
        }

        newPunchDayLog.setRuleType(pr.getRuleType());
        newPunchDayLog.setPunchOrganizationId(pr.getPunchOrganizationId());

        PunchTimeRule ptr = null;
        if (oldPunchDayLog != null && oldPunchDayLog.getTimeRuleId() != null && oldPunchDayLog.getTimeRuleId() > 0) {
            ptr = punchProvider.findPunchTimeRuleById(oldPunchDayLog.getTimeRuleId());
        }
        if (ptr == null) {
            ptr = getPunchTimeRuleWithPunchDayTypeByRuleIdAndDate(pr, logDay.getTime(), userId);
        }

        if (ptr == null || ptr.getId() == null || ptr.getId() == 0) {
            // 休息日或未排班的处理
            return processRestDayPunchDayLogs(userId, companyId, pdl, newPunchDayLog, ptr, punchDate, punchLogs, pr);
        }

        newPunchDayLog.setSplitDateTime(new Timestamp(punchDate.getTime() + (ptr.getDaySplitTimeLong() != null ? ptr.getDaySplitTimeLong() : ONE_DAY_MS + PunchConstants.DEFAULT_SPLIT_TIME)));
        newPunchDayLog.setTimeRuleName(ptr.getName());
        newPunchDayLog.setTimeRuleId(ptr.getId());

        //没有规则就是没有排班,就是非工作日
        pdl.setTimeRuleId(ptr.getId());
        pdl.setTimeRuleName(ptr.getName());
        pdl.setPunchOrganizationId(pr.getPunchOrganizationId());
        pdl.setRuleType(ptr.getRuleType());
        pdl.setPunchTimesPerDay(ptr.getPunchTimesPerDay());
        //对于已离职和未入职的判断
        OrganizationMemberDetails memberDetail = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
        if (memberDetail == null || EmployeeStatus.DISMISSAL == EmployeeStatus.fromCode(memberDetail.getEmployeeStatus())) {
            //找不到就是已离职
            pdl.setStatusList(PunchStatus.RESIGNED.getCode() + "");
            pdl.setPunchTimesPerDay(PunchTimesPerDay.TWICE.getCode());
            newPunchDayLog.setStatus(ApprovalStatus.RESIGNED.getCode());
            pdl.setMorningPunchStatus(PunchStatus.RESIGNED.getCode());
            pdl.setAfternoonPunchStatus(PunchStatus.RESIGNED.getCode());
            pdl.setPunchStatus(ApprovalStatus.RESIGNED.getCode());
            return pdl;
        }

        //请假的汇总interval
        List<TimeInterval> tiDTOs = null;
        Timestamp dayStart = new Timestamp(punchDate.getTime() + (ptr.getDaySplitTimeLong() == null ? 104400000L : ptr.getDaySplitTimeLong()) - ONE_DAY_MS + 1);
        Timestamp dayEnd = new Timestamp(punchDate.getTime() + (ptr.getDaySplitTimeLong() == null ? 104400000L : ptr.getDaySplitTimeLong()));
        List<PunchExceptionRequest> exceptionRequests = punchProvider.listPunchExceptionRequestBetweenBeginAndEndTime(userId, companyId, dayStart, dayEnd);
        List<PunchExceptionRequest> abnormalExceptionRequests = punchProvider.listpunchexceptionRequestByDate(userId, companyId, punchDate);

        tiDTOs = processTimeRuleDTO(exceptionRequests, tiDTOs);

        punchExceptionRequestCountByPunchDayLog(newPunchDayLog, exceptionRequests, abnormalExceptionRequests);

        List<PunchLog> efficientLogs = new ArrayList<>();
        //2次打卡的计算
        if (PunchTimesPerDay.TWICE.getCode().equals(ptr.getPunchTimesPerDay())) {
            Integer punchIntervalNo = 1;
            Byte punchType = PunchType.ON_DUTY.getCode();
            PunchLog onDutyLog = findPunchLog(punchLogs, punchType, punchIntervalNo);
            if (onDutyLog == null) {
                onDutyLog = getNewPunchLog(punchDate, userId, detailId, companyId, punchIntervalNo, punchType, pr);
                onDutyLog.setRuleTime(ptr.getStartEarlyTimeLong());
                onDutyLog.setStatus(PunchStatus.UNPUNCH.getCode());
            }
            punchType = PunchType.OFF_DUTY.getCode();
            PunchLog offDutyLog = findPunchLog(punchLogs, punchType, punchIntervalNo);
            if (offDutyLog == null) {
                offDutyLog = getNewPunchLog(punchDate, userId, detailId, companyId, punchIntervalNo, punchType, pr);
                offDutyLog.setRuleTime(ptr.getStartEarlyTimeLong() + ptr.getWorkTimeLong());
                offDutyLog.setStatus(PunchStatus.UNPUNCH.getCode());
                pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
            }

            //算请假对上下班打卡的影响
            HommizationType ht = HommizationType.fromCode(ptr.getHommizationType());
            Long flexTimeLong = 0L;
            if (ht == HommizationType.FLEX || ht == HommizationType.LATEARRIVE) {
                flexTimeLong = ptr.getFlexTimeLong();
            }
            Long latestOnDutyTimeLong = punchDate.getTime() + onDutyLog.getRuleTime();
            Long earliestOffDutyTimeLong = punchDate.getTime() + offDutyLog.getRuleTime();
            // 上班未打卡，则弹性时间和晚到晚走失效
            if (onDutyLog.getPunchTime() != null) {
                latestOnDutyTimeLong += flexTimeLong;
                if (HommizationType.LATEARRIVE == ht) {
                    Long punchLaterTime = onDutyLog.getPunchTime().getTime() - punchDate.getTime() - onDutyLog.getRuleTime();
                    // 晚到才需要计算晚走的时间
                    if (punchLaterTime > 0) {
                        earliestOffDutyTimeLong += Math.min(punchLaterTime, flexTimeLong);  // 晚到的时间不能超出设置的最晚时间
                    }
                } else if (HommizationType.FLEX == ht) {
                    earliestOffDutyTimeLong -= flexTimeLong;
                }
            }
            calculateOnDutyApprovalStatus(ptr, onDutyLog, tiDTOs, latestOnDutyTimeLong, earliestOffDutyTimeLong, abnormalExceptionRequests);
            calculateOffDutyApprovalStatus(ptr, offDutyLog, onDutyLog, tiDTOs, latestOnDutyTimeLong, earliestOffDutyTimeLong, abnormalExceptionRequests);
            newPunchDayLog.setBelateTimeTotal(newPunchDayLog.getBelateTimeTotal() + calculateBelateTime(ptr,onDutyLog, tiDTOs, latestOnDutyTimeLong, earliestOffDutyTimeLong));
            newPunchDayLog.setLeaveEarlyTimeTotal(newPunchDayLog.getLeaveEarlyTimeTotal() + calculateLeaveEarlyTime(ptr,offDutyLog, onDutyLog, tiDTOs, latestOnDutyTimeLong, earliestOffDutyTimeLong));
            efficientLogs.add(onDutyLog);
            efficientLogs.add(offDutyLog);
            if (null == onDutyLog.getPunchTime() || null == offDutyLog.getPunchTime()) {
                pdl.setWorkTime(0L);

            } else {
                //			/**上班时间*/
                Calendar arriveCalendar = Calendar.getInstance();
                arriveCalendar.setTime(onDutyLog.getPunchTime());
                Calendar leaveCalendar = Calendar.getInstance();
                leaveCalendar.setTime(offDutyLog.getPunchTime());
                long realWorkTime = leaveCalendar.getTimeInMillis() - arriveCalendar.getTimeInMillis();
                if (null != ptr.getNoonLeaveTimeLong()) {
                    /**最早上班时间*/
                    Calendar startMinTime = Calendar.getInstance();
                    /**最晚上班时间*/
                    Calendar startMaxTime = Calendar.getInstance();
                    startMinTime.setTimeInMillis(punchLogs.get(0).getPunchDate().getTime() + ptr.getStartEarlyTimeLong());
                    startMaxTime.setTimeInMillis(punchLogs.get(0).getPunchDate().getTime() + ptr.getStartLateTimeLong());
                    Calendar AfternoonArriveCalendar = Calendar.getInstance();
                    AfternoonArriveCalendar.setTimeInMillis((punchLogs.get(0).getPunchDate().getTime() +
                            ptr.getAfternoonArriveTimeLong()));
                    Calendar NoonLeaveTimeCalendar = Calendar.getInstance();
                    NoonLeaveTimeCalendar.setTimeInMillis((punchLogs.get(0).getPunchDate().getTime() +
                            ptr.getNoonLeaveTimeLong()));

                    if (leaveCalendar.after(AfternoonArriveCalendar) && arriveCalendar.before(NoonLeaveTimeCalendar)) {
                        realWorkTime = leaveCalendar.getTimeInMillis() - arriveCalendar.getTimeInMillis()
                                - ptr.getAfternoonArriveTimeLong() + ptr.getNoonLeaveTimeLong();
                    }
                }
                newPunchDayLog.setWorkTime(convertTime(realWorkTime));
            }
        } else if (PunchTimesPerDay.FORTH.getCode().equals(ptr.getPunchTimesPerDay())) {
            Long workTimeLong = 0L;
            Integer punchIntervalNo = 1;
            Byte punchType = PunchType.ON_DUTY.getCode();
            PunchLog onDutyLog = findPunchLog(punchLogs, punchType, punchIntervalNo);
            if (onDutyLog == null) {
                onDutyLog = getNewPunchLog(punchDate, userId, detailId, companyId, punchIntervalNo, punchType, pr);
                onDutyLog.setRuleTime(ptr.getStartEarlyTimeLong());
                onDutyLog.setStatus(PunchStatus.UNPUNCH.getCode());
            }
            punchType = PunchType.OFF_DUTY.getCode();
            PunchLog offDutyLog = findPunchLog(punchLogs, punchType, punchIntervalNo);
            if (offDutyLog == null) {
                offDutyLog = getNewPunchLog(punchDate, userId, detailId, companyId, punchIntervalNo, punchType, pr);
                offDutyLog.setRuleTime(ptr.getNoonLeaveTimeLong());
                offDutyLog.setStatus(PunchStatus.UNPUNCH.getCode());
            }
            //算请假对上下班打卡的影响
            HommizationType ht = HommizationType.fromCode(ptr.getHommizationType());
            Long flexTimeLong = 0L;
            if (ht == HommizationType.FLEX || ht == HommizationType.LATEARRIVE) {
                flexTimeLong = ptr.getFlexTimeLong();
            }
            Long latestOnDutyTimeLong1 = punchDate.getTime() + onDutyLog.getRuleTime();
            Long earliestOffDutyTimeLong1 = punchDate.getTime() + offDutyLog.getRuleTime();
            Long laterTime = onDutyLog.getPunchTime() != null ? (onDutyLog.getPunchTime().getTime() - punchDate.getTime() - onDutyLog.getRuleTime()) : 0L;
            if (PunchStatus.UNPUNCH != PunchStatus.fromCode(onDutyLog.getStatus())) {
                latestOnDutyTimeLong1 += flexTimeLong;
            }
            calculateOnDutyApprovalStatus(ptr, onDutyLog, tiDTOs, latestOnDutyTimeLong1, earliestOffDutyTimeLong1, abnormalExceptionRequests);
            calculateOffDutyApprovalStatus(ptr, offDutyLog, onDutyLog, tiDTOs, latestOnDutyTimeLong1, earliestOffDutyTimeLong1, abnormalExceptionRequests);
            newPunchDayLog.setBelateTimeTotal(newPunchDayLog.getBelateTimeTotal() + calculateBelateTime(ptr,onDutyLog, tiDTOs, latestOnDutyTimeLong1, earliestOffDutyTimeLong1));
            newPunchDayLog.setLeaveEarlyTimeTotal(newPunchDayLog.getLeaveEarlyTimeTotal() + calculateLeaveEarlyTime(ptr,offDutyLog, onDutyLog, tiDTOs, latestOnDutyTimeLong1, earliestOffDutyTimeLong1));
            efficientLogs.add(onDutyLog);
            efficientLogs.add(offDutyLog);
            if (onDutyLog.getPunchTime() != null && offDutyLog.getPunchTime() != null) {
                workTimeLong += offDutyLog.getPunchTime().getTime() - onDutyLog.getPunchTime().getTime();
            }
            punchIntervalNo = 2;
            punchType = PunchType.ON_DUTY.getCode();
            onDutyLog = findPunchLog(punchLogs, punchType, punchIntervalNo);
            if (onDutyLog == null) {
                onDutyLog = getNewPunchLog(punchDate, userId, detailId, companyId, punchIntervalNo, punchType, pr);
                onDutyLog.setRuleTime(ptr.getAfternoonArriveTimeLong());
                onDutyLog.setStatus(PunchStatus.UNPUNCH.getCode());
            }
            efficientLogs.add(onDutyLog);
            punchType = PunchType.OFF_DUTY.getCode();
            offDutyLog = findPunchLog(punchLogs, punchType, punchIntervalNo);
            if (offDutyLog == null) {
                offDutyLog = getNewPunchLog(punchDate, userId, detailId, companyId, punchIntervalNo, punchType, pr);
                offDutyLog.setRuleTime(ptr.getStartEarlyTimeLong() + ptr.getWorkTimeLong());
                offDutyLog.setStatus(PunchStatus.UNPUNCH.getCode());
            }
            efficientLogs.add(offDutyLog);

            Long latestOnDutyTimeLong2 = punchDate.getTime() + ptr.getAfternoonArriveTimeLong();
            Long earliestOffDutyTimeLong2 = punchDate.getTime() + offDutyLog.getRuleTime();
            if (PunchStatus.UNPUNCH != PunchStatus.fromCode(onDutyLog.getStatus())) {
                if (HommizationType.LATEARRIVE == HommizationType.fromCode(ptr.getHommizationType())) {
                    // 晚到才需要计算晚走的时间
                    if (laterTime > 0) {
                        earliestOffDutyTimeLong2 += Math.min(laterTime, flexTimeLong);
                    }
                } else if (HommizationType.FLEX == HommizationType.fromCode(ptr.getHommizationType())) {
                    earliestOffDutyTimeLong2 -= flexTimeLong;
                }
            }

            calculateOnDutyApprovalStatus(ptr, onDutyLog, tiDTOs, latestOnDutyTimeLong2, earliestOffDutyTimeLong2, abnormalExceptionRequests);
            calculateOffDutyApprovalStatus(ptr, offDutyLog, onDutyLog, tiDTOs, latestOnDutyTimeLong2, earliestOffDutyTimeLong2, abnormalExceptionRequests);
            newPunchDayLog.setBelateTimeTotal(newPunchDayLog.getBelateTimeTotal() + calculateBelateTime(ptr, onDutyLog, tiDTOs, latestOnDutyTimeLong2, earliestOffDutyTimeLong2));
            newPunchDayLog.setLeaveEarlyTimeTotal(newPunchDayLog.getLeaveEarlyTimeTotal() + calculateLeaveEarlyTime(ptr, offDutyLog, onDutyLog, tiDTOs, latestOnDutyTimeLong2, earliestOffDutyTimeLong2));
            if (onDutyLog.getPunchTime() != null && offDutyLog.getPunchTime() != null) {
                workTimeLong += offDutyLog.getPunchTime().getTime() - onDutyLog.getPunchTime().getTime();
            }

            newPunchDayLog.setWorkTime(convertTime(workTimeLong));
        } else {
            //多次打卡
            List<PunchTimeInterval> intervals = punchProvider.listPunchTimeIntervalByTimeRuleId(ptr.getId());
            if (null == intervals)
                throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
                        PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_SETTING,
                        "公司没有设置打卡时间段");

            Long workTimeLong = 0L;
            for (int punchIntervalNo = 1; punchIntervalNo <= intervals.size(); punchIntervalNo++) {
                PunchLog onDutyLog = findPunchLog(punchLogs, PunchType.ON_DUTY.getCode(), punchIntervalNo);
                if (onDutyLog == null) {
                    onDutyLog = getNewPunchLog(punchDate, userId, detailId, companyId, punchIntervalNo, PunchType.ON_DUTY.getCode(), pr);
                    onDutyLog.setRuleTime(intervals.get(punchIntervalNo - 1).getArriveTimeLong());
                    onDutyLog.setStatus(PunchStatus.UNPUNCH.getCode());
                    punchLogs.add(onDutyLog);
                }
                efficientLogs.add(onDutyLog);
                PunchLog offDutyLog = findPunchLog(punchLogs, PunchType.OFF_DUTY.getCode(), punchIntervalNo);
                if (offDutyLog == null) {
                    offDutyLog = getNewPunchLog(punchDate, userId, detailId, companyId, punchIntervalNo, PunchType.OFF_DUTY.getCode(), pr);
                    offDutyLog.setRuleTime(intervals.get(punchIntervalNo - 1).getLeaveTimeLong());
                    offDutyLog.setStatus(PunchStatus.UNPUNCH.getCode());
                }
                efficientLogs.add(offDutyLog);

                //算请假对上班打卡的影响
                HommizationType ht = HommizationType.fromCode(ptr.getHommizationType());
                Long latestOnDutyTimeLong = punchDate.getTime() + onDutyLog.getRuleTime();
                Long earliestOffDutyTimeLong = punchDate.getTime() + offDutyLog.getRuleTime();
                if (punchIntervalNo == 1 && (ht == HommizationType.FLEX || ht == HommizationType.LATEARRIVE)) {
                    if (onDutyLog.getPunchTime() != null) {
                        latestOnDutyTimeLong += ptr.getFlexTimeLong();
                    }
                }

                //算请假对下班打卡的影响
                //只有最后一次打卡要计算晚到晚走
                if (punchIntervalNo == intervals.size()) {
                    PunchLog firstOnDutyLog = findPunchLog(punchLogs, PunchType.ON_DUTY.getCode(), 1);
                    if (PunchStatus.UNPUNCH != PunchStatus.fromCode(onDutyLog.getStatus())) {
                        if (HommizationType.LATEARRIVE == HommizationType.fromCode(ptr.getHommizationType())) {
                            Long laterTime = (firstOnDutyLog.getPunchTime().getTime() - punchDate.getTime() - firstOnDutyLog.getRuleTime());
                            // 晚到才需要计算晚走的时间
                            if (laterTime > 0) {
                                earliestOffDutyTimeLong += Math.min(laterTime, ptr.getFlexTimeLong());
                            }
                        } else if (HommizationType.FLEX == HommizationType.fromCode(ptr.getHommizationType())) {
                            earliestOffDutyTimeLong -= ptr.getFlexTimeLong();
                        }
                    }
                }
                calculateOnDutyApprovalStatus(ptr, onDutyLog, tiDTOs, latestOnDutyTimeLong, earliestOffDutyTimeLong, abnormalExceptionRequests);
                calculateOffDutyApprovalStatus(ptr, offDutyLog, onDutyLog, tiDTOs, latestOnDutyTimeLong, earliestOffDutyTimeLong, abnormalExceptionRequests);
                newPunchDayLog.setBelateTimeTotal(newPunchDayLog.getBelateTimeTotal() + calculateBelateTime(ptr, onDutyLog, tiDTOs, latestOnDutyTimeLong, earliestOffDutyTimeLong));
                newPunchDayLog.setLeaveEarlyTimeTotal(newPunchDayLog.getLeaveEarlyTimeTotal() + calculateLeaveEarlyTime(ptr, offDutyLog, onDutyLog, tiDTOs, latestOnDutyTimeLong, earliestOffDutyTimeLong));
                if (onDutyLog.getPunchTime() != null && offDutyLog.getPunchTime() != null) {
                    workTimeLong += offDutyLog.getPunchTime().getTime() - onDutyLog.getPunchTime().getTime();
                }
            }
            newPunchDayLog.setWorkTime(convertTime(workTimeLong));
        }

        for (PunchLog log : efficientLogs) {
            if (null == pdl.getStatusList()) {
                pdl.setStatusList(log.getStatus() + "");
            } else {
                pdl.setStatusList(pdl.getStatusList() + PunchConstants.STATUS_SEPARATOR + log.getStatus());
            }
            String approvalStatus = log.getApprovalStatus() == null ? "" : log.getApprovalStatus() + "";
            if (null == pdl.getApprovalStatusList()) {
                pdl.setApprovalStatusList(approvalStatus);
            } else {
                pdl.setApprovalStatusList(pdl.getApprovalStatusList() + PunchConstants.STATUS_SEPARATOR + approvalStatus);
            }

        }

        // 计算加班时间
        calculateOvertimeWorkCount(pdl, ptr != null ? ptr.getPunchDayType() : null, pr, punchLogs, exceptionRequests);
        return pdl;
    }

    private PunchLogsDay processRestDayPunchDayLogs(Long userId, Long companyId, PunchLogsDay pdl, PunchDayLog punchDayLog, PunchTimeRule ptr, java.sql.Date punchDate, List<PunchLog> punchLogs, PunchRule pr) {
        pdl.setStatusList(PunchStatus.NOTWORKDAY.getCode() + "");
        pdl.setPunchTimesPerDay((byte) 0);
        pdl.setPunchStatus(PunchStatus.NORMAL.getCode());
        pdl.setMorningPunchStatus(PunchStatus.NORMAL.getCode());
        pdl.setAfternoonPunchStatus(PunchStatus.NORMAL.getCode());
        pdl.setExceptionStatus(null);
        punchDayLog.setTimeRuleId(null);
        punchDayLog.setSplitDateTime(new Timestamp(punchDate.getTime() + ONE_DAY_MS + PunchConstants.DEFAULT_SPLIT_TIME));
        // 获取加班申请等
        Timestamp dayStart = new Timestamp(punchDate.getTime());
        Timestamp dayEnd = new Timestamp(punchDate.getTime() + ONE_DAY_MS);
        List<PunchExceptionRequest> exceptionRequests = punchProvider.listPunchExceptionRequestBetweenBeginAndEndTime(userId, companyId, dayStart, dayEnd);

        // 当天是休息日，不存在异常打卡申请的数据
        punchExceptionRequestCountByPunchDayLog(punchDayLog, exceptionRequests, null);
        // 计算加班时间
        calculateOvertimeWorkCount(pdl, ptr != null ? ptr.getPunchDayType() : null, pr, punchLogs, exceptionRequests);

        if (pr != null && PunchRuleType.GUDING == PunchRuleType.fromCode(pr.getRuleType())) {
            return pdl;
        }
        // ptr.getId()==0  包含休息和未安排班次
        if (ptr != null && ptr.getId() == 0) {
            if (NormalFlag.YES == NormalFlag.fromCode(ptr.getUnscheduledFlag())) {
                pdl.setStatusList(PunchStatus.NO_ASSIGN_PUNCH_SCHEDULED.getCode() + "");
            } else {
                punchDayLog.setTimeRuleId(0L);
                punchDayLog.setTimeRuleName("休息");
                pdl.setStatusList(PunchStatus.NOTWORKDAY.getCode() + "");
            }
        }
        return pdl;
    }

    /**
     * 每次打卡和加班申请审核通过以后，计算当天加班的时长
     * 三种加班方式的设置会影响加班时长的计算：
     * 1、需审批，时长以打卡为准，但不能超过审批单时长
     * 2、需审批，时长以审批单为准，无需打卡
     * 3、无需审批，时长以打卡为准
     */
    private void calculateOvertimeWorkCount(PunchLogsDay pdl, PunchDayType type, PunchRule punchRule, List<PunchLog> punchLogs, List<PunchExceptionRequest> exceptionRequests) {
        PunchOvertimeRule punchOvertimeRule = getPunchOvertimeRuleByPunchDayType(punchRule, type);
        // 没有设置加班规则，加班申请无效，不计算总加班时间
        if (punchOvertimeRule == null) {
            return;
        }

        Long total = 0L;
        List<TimeInterval> overtimeApprovalIntervals = selectPunchOvertimeIntervals(exceptionRequests);
        // 需审批，无需打卡，时长以审批单为准
        if (PunchOvertimeApprovalType.APPROVAL == PunchOvertimeApprovalType.fromCode(punchOvertimeRule.getOvertimeApprovalType())) {
            // 没有审批单，不需要计算加班时长
            if (CollectionUtils.isEmpty(overtimeApprovalIntervals)) {
                return;
            }
            // 由于加班申请可能跨多个工作日，因此Math.min和Math.max的操作是为了将加班时长的计算限制在当天（由于不关联打卡，因此当天是0点到23:59:59.999）
            Long dayBegin = pdl.getPunchDate().getTime();
            Long dayEnd = pdl.getPunchDate().getTime() + ONE_DAY_MS - 1;
            for (TimeInterval t : overtimeApprovalIntervals) {
                total += calculateOvertimeCount(dayBegin, dayEnd, t);
            }
            setOvertimeWorkCountByType(pdl, total, type, punchOvertimeRule);
            return;
        }

        if (PunchDayType.WORKDAY == type) {
            // 工作日加班

            // 最后一次打卡状态不正常（未校准前），则不会有加班打卡的情况
            if (StringUtils.isEmpty(pdl.getStatusList()) || !pdl.getStatusList().endsWith(String.valueOf(PunchStatus.NORMAL.getCode()))) {
                return;
            }
            // 获取最后一次下班打卡时间
            PunchLog lastOffDutyPunch = findPunchLog(punchLogs, PunchType.OFF_DUTY.getCode(), pdl.getPunchTimesPerDay() / 2);
            if (lastOffDutyPunch == null || lastOffDutyPunch.getPunchTime() == null) {
                // 正常工作日没有下班打卡，不需要计算加班
                return;
            }
            // 工作日下班时间(正常下班时间，不算弹性)validInterval以后才算加班
            Long beginTime = lastOffDutyPunch.getPunchDate().getTime() + lastOffDutyPunch.getRuleTime() + punchOvertimeRule.getValidInterval() * ONE_MINUTE_MS;
            Long endTime = lastOffDutyPunch.getPunchTime().getTime();
            if (beginTime >= endTime) {
                return;
            }
            if (PunchOvertimeApprovalType.PUNCH == PunchOvertimeApprovalType.fromCode(punchOvertimeRule.getOvertimeApprovalType())) {
                // 无需审批，时长以打卡时间为准
                total = endTime - beginTime;
                setOvertimeWorkCountByType(pdl, total, type, punchOvertimeRule);
                return;
            }
            if (PunchOvertimeApprovalType.PUNCH_AND_APPROVAL == PunchOvertimeApprovalType.fromCode(punchOvertimeRule.getOvertimeApprovalType())) {
                // 没有加班审批单，不需要计算加班时间
                if (CollectionUtils.isEmpty(overtimeApprovalIntervals)) {
                    return;
                }
                for (TimeInterval t : overtimeApprovalIntervals) {
                    // 判断加班申请单的时间段是否包含打卡时间段（没有重叠的时间，即表示加班时长=0）
                    total += calculateOvertimeCount(beginTime, endTime, t);
                }
                setOvertimeWorkCountByType(pdl, total, type, punchOvertimeRule);
                return;
            }
        } else {
            // 休息日和法定节假日加班

            if (CollectionUtils.isEmpty(punchLogs)) {
                return;
            }
            // 将punchLogs按签到签退组成timeInterval
            List<TimeInterval> punchTimeIntervals = new ArrayList<>();
            for (int i = 1; ; i++) {
                PunchLog onDuty = findPunchLog(punchLogs, PunchType.OVERTIME_ON_DUTY.getCode(), i);
                PunchLog offDuty = findPunchLog(punchLogs, PunchType.OVERTIME_OFF_DUTY.getCode(), i);
                if (onDuty == null || onDuty.getPunchTime() == null || offDuty == null || offDuty.getPunchTime() == null) {
                    break;
                }
                TimeInterval t = new TimeInterval();
                t.setBeginTime(onDuty.getPunchTime());
                t.setEndTime(offDuty.getPunchTime());
                punchTimeIntervals.add(t);
            }
            if (CollectionUtils.isEmpty(punchTimeIntervals)) {
                return;
            }
            if (PunchOvertimeApprovalType.PUNCH == PunchOvertimeApprovalType.fromCode(punchOvertimeRule.getOvertimeApprovalType())) {
                // 无需审批，时长以打卡时间为准
                for (TimeInterval t : punchTimeIntervals) {
                    long c = t.getEndTime().getTime() - t.getBeginTime().getTime();
                    total += (c > 0 ? c : 0);
                }
                setOvertimeWorkCountByType(pdl, total, type, punchOvertimeRule);
                return;
            }
            if (PunchOvertimeApprovalType.PUNCH_AND_APPROVAL == PunchOvertimeApprovalType.fromCode(punchOvertimeRule.getOvertimeApprovalType())) {
                // 需审批，时长以打卡为准，但不能超过审批单时长
                // 没有加班审批单，不需要计算加班时间
                if (CollectionUtils.isEmpty(overtimeApprovalIntervals)) {
                    return;
                }

                for (TimeInterval pt : punchTimeIntervals) {
                    for (TimeInterval t : overtimeApprovalIntervals) {
                        total += calculateOvertimeCount(pt.getBeginTime().getTime(), pt.getEndTime().getTime(), t);
                    }
                }
                setOvertimeWorkCountByType(pdl, total, type, punchOvertimeRule);
                return;
            }
        }
    }

    private Long calculateOvertimeCount(Long beginTime, Long endTime, TimeInterval t) {
        if (isTimeIntervalPartCovered(new Date(beginTime), new Date(endTime), t)) {
            long c = Math.min(t.getEndTime().getTime(), endTime) - Math.max(t.getBeginTime().getTime(), beginTime);
            return (c > 0 ? c : 0);
        }
        return 0L;
    }

    private List<TimeInterval> selectPunchOvertimeIntervals(List<PunchExceptionRequest> exceptionRequests) {
        if (CollectionUtils.isEmpty(exceptionRequests)) {
            return Collections.emptyList();
        }
        List<TimeInterval> overtimeIntervals = new ArrayList<>();
        for (PunchExceptionRequest request : exceptionRequests) {
            if (GeneralApprovalAttribute.OVERTIME != GeneralApprovalAttribute.fromCode(request.getApprovalAttribute())) {
                continue;
            }
            if (com.everhomes.rest.approval.ApprovalStatus.AGREEMENT.getCode() != request.getStatus()) {
                continue;
            }
            overtimeIntervals = addExceptionRequest2TimeIntervals(request, overtimeIntervals);
        }
        return overtimeIntervals;
    }

    private void setOvertimeWorkCountByType(PunchLogsDay pdl, Long total, PunchDayType type, PunchOvertimeRule punchOvertimeRule) {
        // 加班时间小于最小加班时长（OvertimeMinUnit）则记0，超过最长加班时间（OvertimeMax）则记最长加班时间
        total = Math.min(total, punchOvertimeRule.getOvertimeMax() * ONE_MINUTE_MS);
        if (total < punchOvertimeRule.getOvertimeMinUnit() * ONE_MINUTE_MS) {
            total = 0L;
        }
        switch (type) {
            case WORKDAY:
                pdl.setOvertimeTotalWorkday(total);
                break;
            case RESTDAY:
                pdl.setOvertimeTotalRestday(total);
                break;
            case HOLIDAY:
                pdl.setOvertimeTotalLegalHoliday(total);
                break;
            default:
        }
    }

    private PunchOvertimeRule getPunchOvertimeRuleByPunchDayType(PunchRule punchRule, PunchDayType punchDayType) {
        if (punchRule == null || punchDayType == null) {
            return null;
        }
        List<PunchOvertimeRule> punchOvertimeRules = punchProvider.findPunchOvertimeRulesByPunchRuleId(punchRule.getId(), PunchRuleStatus.ACTIVE.getCode());
        if (CollectionUtils.isEmpty(punchOvertimeRules)) {
            return null;
        }
        for (PunchOvertimeRule rule : punchOvertimeRules) {
            if (PunchDayType.WORKDAY == punchDayType && PunchOvertimeRuleType.WORKDAY == PunchOvertimeRuleType.fromCode(rule.getType())) {
                return rule;
            } else if (PunchDayType.WORKDAY != punchDayType && PunchOvertimeRuleType.HOLIDAY == PunchOvertimeRuleType.fromCode(rule.getType())) {
                return rule;
            }
        }
        return null;
    }

    private PunchRule findPunchRuleByCache(String ownerType, Long companyId, Long userId) {
    	return findPunchRuleByCache(ownerType, companyId, userId, punchRuleCacheByUserId.get()); 
    }
    
    private PunchRule findPunchRuleByCache(String ownerType, Long companyId, Long userId, Map<String, PunchRule> ruleMap) {
        PunchRule pr = ruleMap.get(companyId + "-" + userId);
        if (null == pr) {
            pr = getPunchRule(ownerType, companyId, userId);
            ruleMap.put(companyId + "-" + userId, pr);
        }
        return pr;
    }

    private PunchLog getNewPunchLog(Date punchDate, Long userId, Long detailId, Long companyId, Integer punchIntervalNo, Byte punchType, PunchRule pr) {
        PunchLog pl = new PunchLog();
        pl.setUserId(userId);
        pl.setDetailId(detailId);
        pl.setEnterpriseId(companyId);
        pl.setPunchDate(new java.sql.Date(punchDate.getTime()));
        pl.setPunchType(punchType);
        pl.setPunchStatus(ClockCode.SUCESS.getCode());
        pl.setPunchIntervalNo(punchIntervalNo);
        pl.setStatus(PunchStatus.UNPUNCH.getCode());
        if (pr != null) {
            pl.setPunchOrganizationId(pr.getPunchOrganizationId());
        } else {
            pl.setPunchOrganizationId(0L);
        }
        return pl;
    }


    /**
     * 审批单对下班打卡的影响
     */
    private void calculateOffDutyApprovalStatus(PunchTimeRule ptr, PunchLog offDutyLog, PunchLog onDutyLog, List<TimeInterval> approvalTimeIntervals, Long latestOnDutyTimeLong, Long earliestOffDutyTimeLong, List<PunchExceptionRequest> abnormalExceptionRequests) {
        PunchStatus punchStatus = PunchStatus.fromCode(offDutyLog.getStatus());
        if (punchStatus == null || Arrays.asList(PunchStatus.NORMAL, PunchStatus.NOTWORKDAY, PunchStatus.RESIGNED, PunchStatus.NONENTRY).contains(punchStatus)) {
            return;
        }
        cancelPunchLogSmartAlignment(offDutyLog);
        // 判断是否该打卡有异常申请通过的记录
        PunchExceptionRequest punchExceptionRequest = getAbnormalExceptionByPunchLog(offDutyLog, abnormalExceptionRequests);
        if (punchExceptionRequest != null) {
            offDutyLog.setApprovalStatus(PunchStatus.NORMAL.getCode());
            createOrUpdatePunchLog(offDutyLog);
            return;
        }
        if (CollectionUtils.isEmpty(approvalTimeIntervals)) {
            return;
        }
        // 打卡时间晚于最早下班时间则校正为正常
        if (offDutyLog.getPunchTime() != null && offDutyLog.getPunchTime().getTime() >= earliestOffDutyTimeLong) {
            offDutyLog.setApprovalStatus(PunchStatus.NORMAL.getCode());
            updateSmartAlignment(offDutyLog);
            return;
        }
        // 过滤出开始上班时间到最早下班时间段的申请单
        Long beginPunchTimeLong = onDutyLog.getPunchDate().getTime() + onDutyLog.getRuleTime();
        List<TimeInterval> approvalTimeIntervalsThisInterval = filterTimeIntervalsCoverBetween(approvalTimeIntervals, beginPunchTimeLong, earliestOffDutyTimeLong);
        if (CollectionUtils.isEmpty(approvalTimeIntervalsThisInterval)) {
            return;
        }

        // 下班卡早退
        if (offDutyLog.getPunchTime() != null) {
            // 中间有中午休息时间的场景
            if (ptr.getNoonLeaveTimeLong() != null && ptr.getAfternoonArriveTimeLong() != null) {
                // 下班打卡时间在午休之后，下班之前
                if (offDutyLog.getPunchTime().getTime() >= offDutyLog.getPunchDate().getTime() + ptr.getAfternoonArriveTimeLong()) {
                    if (isTimeIntervalFullCovered(offDutyLog.getPunchTime(), new Date(earliestOffDutyTimeLong), approvalTimeIntervalsThisInterval)) {
                        onDutyLog.setApprovalStatus(PunchStatus.NORMAL.getCode());
                        updateSmartAlignment(onDutyLog);
                    } else {
                        offDutyLog.setApprovalStatus(PunchStatus.LEAVEEARLY.getCode());
                        updateSmartAlignment(offDutyLog);
                    }
                    return;
                }
                // 午休期间打下班卡
                if (offDutyLog.getPunchTime().getTime() >= offDutyLog.getPunchDate().getTime() + ptr.getNoonLeaveTimeLong()) {
                    if (isTimeIntervalFullCovered(new Date(offDutyLog.getPunchDate().getTime() + ptr.getAfternoonArriveTimeLong()), new Date(earliestOffDutyTimeLong), approvalTimeIntervalsThisInterval)) {
                        onDutyLog.setApprovalStatus(PunchStatus.NORMAL.getCode());
                        updateSmartAlignment(onDutyLog);
                    } else {
                        offDutyLog.setApprovalStatus(PunchStatus.LEAVEEARLY.getCode());
                        updateSmartAlignment(offDutyLog);
                    }
                    return;
                }
                // 上班之后，午休之前打下班卡
                if (!isTimeIntervalFullCovered(new Date(Math.max(latestOnDutyTimeLong, offDutyLog.getPunchTime().getTime())), new Date(offDutyLog.getPunchDate().getTime() + ptr.getNoonLeaveTimeLong()), approvalTimeIntervalsThisInterval)) {
                    offDutyLog.setApprovalStatus(PunchStatus.LEAVEEARLY.getCode());
                    updateSmartAlignment(offDutyLog);
                    return;
                }
                if (isTimeIntervalFullCovered(new Date(offDutyLog.getPunchDate().getTime() + ptr.getAfternoonArriveTimeLong()), new Date(earliestOffDutyTimeLong), approvalTimeIntervalsThisInterval)) {
                    onDutyLog.setApprovalStatus(PunchStatus.NORMAL.getCode());
                    updateSmartAlignment(onDutyLog);
                }
                offDutyLog.setApprovalStatus(PunchStatus.LEAVEEARLY.getCode());
                updateSmartAlignment(offDutyLog);
                return;
            }
            if (isTimeIntervalFullCovered(new Date(Math.max(latestOnDutyTimeLong, offDutyLog.getPunchTime().getTime())), new Date(earliestOffDutyTimeLong), approvalTimeIntervalsThisInterval)) {
                offDutyLog.setApprovalStatus(PunchStatus.NORMAL.getCode());
                updateSmartAlignment(offDutyLog);
                return;
            }
            offDutyLog.setApprovalStatus(PunchStatus.LEAVEEARLY.getCode());
            updateSmartAlignment(offDutyLog);
            return;
        }
        Byte onDutyStatus = onDutyLog.getApprovalStatus() == null ? onDutyLog.getStatus() : onDutyLog.getApprovalStatus();
        PunchExceptionRequest punchExceptionRequestOnDuty = getAbnormalExceptionByPunchLog(onDutyLog, abnormalExceptionRequests);
        if (PunchStatus.NORMAL != PunchStatus.fromCode(onDutyStatus) || punchExceptionRequestOnDuty != null) {
            if (isTimeIntervalCoveredEndTime(new Date(earliestOffDutyTimeLong), approvalTimeIntervalsThisInterval)) {
                offDutyLog.setApprovalStatus(PunchStatus.NORMAL.getCode());
                updateSmartAlignment(offDutyLog);
            } else {
                offDutyLog.setApprovalStatus(PunchStatus.LEAVEEARLY.getCode());
                updateSmartAlignment(offDutyLog);
            }
            return;
        }

        // 把休息时间覆盖到申请申请时间中
        if (ptr.getNoonLeaveTimeLong() != null && ptr.getAfternoonArriveTimeLong() != null) {
            PunchExceptionRequest request = new PunchExceptionRequest();
            request.setBeginTime(new Timestamp(offDutyLog.getPunchDate().getTime() + ptr.getNoonLeaveTimeLong()));
            request.setEndTime(new Timestamp(offDutyLog.getPunchDate().getTime() + ptr.getAfternoonArriveTimeLong()));
            addExceptionRequest2TimeIntervals(request, approvalTimeIntervalsThisInterval);
        }
        // 上下班都没有打卡，需要覆盖全段时间
        if (onDutyLog.getPunchTime() == null || offDutyLog.getPunchTime() == null) {
            if (isTimeIntervalFullCovered(new Date(latestOnDutyTimeLong), new Date(earliestOffDutyTimeLong), approvalTimeIntervalsThisInterval)) {
                offDutyLog.setApprovalStatus(PunchStatus.NORMAL.getCode());
                updateSmartAlignment(offDutyLog);
            } else {
                offDutyLog.setApprovalStatus(PunchStatus.LEAVEEARLY.getCode());
                updateSmartAlignment(offDutyLog);
            }
            return;
        }
        // 上班有打卡,覆盖上班打卡到下班的时间
        if (onDutyLog.getPunchTime() != null || offDutyLog.getPunchTime() == null) {
            if (isTimeIntervalFullCovered(new Date(Math.max(onDutyLog.getPunchTime().getTime(), latestOnDutyTimeLong)), new Date(earliestOffDutyTimeLong), approvalTimeIntervalsThisInterval)) {
                offDutyLog.setApprovalStatus(PunchStatus.NORMAL.getCode());
                updateSmartAlignment(offDutyLog);
                return;
            }
        }
        // 不满足以上情况的就是早退
        offDutyLog.setApprovalStatus(PunchStatus.LEAVEEARLY.getCode());
        updateSmartAlignment(offDutyLog);
    }

    /**
     * 智能校准状态
     */
    private void updateSmartAlignment(PunchLog pl) {
        pl.setSmartAlignment(NormalFlag.YES.getCode());
        createOrUpdatePunchLog(pl);
    }

    private void createOrUpdatePunchLog(PunchLog pl) {
        pl.setUpdateDate(new java.sql.Date(DateHelper.currentGMTTime().getTime()));
        if (pl.getId() == null) {
            punchProvider.createPunchLog(pl);
        } else {
            punchProvider.updatePunchLog(pl);
        }
    }

    private void cancelPunchLogSmartAlignment(PunchLog pl) {
        if (pl.getId() != null && pl.getId() > 0 && PunchStatus.fromCode(pl.getApprovalStatus()) != null) {
            pl.setUpdateDate(new java.sql.Date(DateHelper.currentGMTTime().getTime()));
            pl.setApprovalStatus(null);
            pl.setSmartAlignment(NormalFlag.NO.getCode());
            punchProvider.updatePunchLog(pl);
        }
    }

    private PunchExceptionRequest getAbnormalExceptionByPunchLog(PunchLog punchLog, List<PunchExceptionRequest> abnormalExceptionRequests) {
        if (punchLog == null || CollectionUtils.isEmpty(abnormalExceptionRequests)) {
            return null;
        }
        for (PunchExceptionRequest request : abnormalExceptionRequests) {
            if (GeneralApprovalAttribute.ABNORMAL_PUNCH != GeneralApprovalAttribute.fromCode(request.getApprovalAttribute())) {
                continue;
            }
            if (com.everhomes.rest.approval.ApprovalStatus.AGREEMENT != com.everhomes.rest.approval.ApprovalStatus.fromCode(request.getStatus())) {
                continue;
            }
            if (punchLog.getUserId().equals(request.getUserId()) && punchLog.getEnterpriseId().equals(request.getEnterpriseId())
                    && punchLog.getPunchDate().compareTo(request.getPunchDate()) == 0 && punchLog.getPunchIntervalNo().equals(request.getPunchIntervalNo())
                    && punchLog.getPunchType().equals(request.getPunchType())) {
                return request;
            }
        }
        return null;
    }

    /**
     * 审批单对上班打卡的影响
     */
    private void calculateOnDutyApprovalStatus(PunchTimeRule ptr, PunchLog onDutyLog, List<TimeInterval> approvalTimeIntervals, Long latestOnDutyTimeLong, Long earliestOffDutyTimeLong, List<PunchExceptionRequest> abnormalExceptionRequests) {
        PunchStatus punchStatus = PunchStatus.fromCode(onDutyLog.getStatus());
        // 打卡状态为正常或非工作时间、离职等，则不需要校正
        if (punchStatus == null || Arrays.asList(PunchStatus.NORMAL, PunchStatus.NOTWORKDAY, PunchStatus.RESIGNED, PunchStatus.NONENTRY).contains(punchStatus)) {
            return;
        }
        cancelPunchLogSmartAlignment(onDutyLog);
        // 判断是否该打卡有异常申请通过的记录
        PunchExceptionRequest punchExceptionRequest = getAbnormalExceptionByPunchLog(onDutyLog, abnormalExceptionRequests);
        if (punchExceptionRequest != null) {
            onDutyLog.setApprovalStatus(PunchStatus.NORMAL.getCode());
            createOrUpdatePunchLog(onDutyLog);
            return;
        }
        if (CollectionUtils.isEmpty(approvalTimeIntervals)) {
            return;
        }
        // 上班有打卡且打卡时间早于最晚上班时间则校正为正常
        if (onDutyLog.getPunchTime() != null && onDutyLog.getPunchTime().getTime() <= latestOnDutyTimeLong) {
            onDutyLog.setApprovalStatus(PunchStatus.NORMAL.getCode());
            updateSmartAlignment(onDutyLog);
            return;
        }
        // 过滤出开始上班时间到最早下班时间段的申请单
        Long beginPunchTimeLong = onDutyLog.getPunchDate().getTime() + onDutyLog.getRuleTime();
        List<TimeInterval> approvalTimeIntervalsThisInterval = filterTimeIntervalsCoverBetween(approvalTimeIntervals, beginPunchTimeLong, earliestOffDutyTimeLong);
        if (CollectionUtils.isEmpty(approvalTimeIntervalsThisInterval)) {
            return;
        }
        // 上班卡有打卡且迟到
        if (onDutyLog.getPunchTime() != null) {
            // 中间有中午休息时间的场景
            if (ptr.getNoonLeaveTimeLong() != null && ptr.getAfternoonArriveTimeLong() != null) {
                // 打卡时间在午休之前
                if (onDutyLog.getPunchTime().getTime() <= onDutyLog.getPunchDate().getTime() + ptr.getNoonLeaveTimeLong()) {
                    if (isTimeIntervalFullCovered(new Date(latestOnDutyTimeLong), onDutyLog.getPunchTime(), approvalTimeIntervalsThisInterval)) {
                        onDutyLog.setApprovalStatus(PunchStatus.NORMAL.getCode());
                        updateSmartAlignment(onDutyLog);
                    } else {
                        onDutyLog.setApprovalStatus(PunchStatus.BELATE.getCode());
                        updateSmartAlignment(onDutyLog);
                    }
                    return;
                }
                // 在午休期间打卡
                if (onDutyLog.getPunchTime().getTime() <= onDutyLog.getPunchDate().getTime() + ptr.getAfternoonArriveTimeLong()) {
                    if (isTimeIntervalFullCovered(new Date(latestOnDutyTimeLong), new Date(onDutyLog.getPunchDate().getTime() + ptr.getNoonLeaveTimeLong()), approvalTimeIntervalsThisInterval)) {
                        onDutyLog.setApprovalStatus(PunchStatus.NORMAL.getCode());
                        updateSmartAlignment(onDutyLog);
                    } else {
                        onDutyLog.setApprovalStatus(PunchStatus.BELATE.getCode());
                        updateSmartAlignment(onDutyLog);
                    }
                    return;
                }
                // 午休之后打上班卡，那么请假需要覆盖早上的区间同时覆盖下午打卡前的区间
                if (!isTimeIntervalFullCovered(new Date(latestOnDutyTimeLong), new Date(onDutyLog.getPunchDate().getTime() + ptr.getNoonLeaveTimeLong()), approvalTimeIntervalsThisInterval)) {
                    onDutyLog.setApprovalStatus(PunchStatus.BELATE.getCode());
                    updateSmartAlignment(onDutyLog);
                    return;
                }
                if (isTimeIntervalFullCovered(new Date(onDutyLog.getPunchDate().getTime() + ptr.getAfternoonArriveTimeLong()), new Date(Math.min(onDutyLog.getPunchTime().getTime(), earliestOffDutyTimeLong)), approvalTimeIntervalsThisInterval)) {
                    onDutyLog.setApprovalStatus(PunchStatus.NORMAL.getCode());
                    updateSmartAlignment(onDutyLog);
                }
                onDutyLog.setApprovalStatus(PunchStatus.BELATE.getCode());
                updateSmartAlignment(onDutyLog);
                return;
            }
            // 无中午休息的场景
            if (isTimeIntervalFullCovered(new Date(latestOnDutyTimeLong), new Date(Math.min(onDutyLog.getPunchTime().getTime(), earliestOffDutyTimeLong)), approvalTimeIntervalsThisInterval)) {
                onDutyLog.setApprovalStatus(PunchStatus.NORMAL.getCode());
                updateSmartAlignment(onDutyLog);
                return;
            }
            onDutyLog.setApprovalStatus(PunchStatus.BELATE.getCode());
            updateSmartAlignment(onDutyLog);
            return;
        }
        // 上班未打卡,(没有上班卡就不存在下班卡，申请只要覆盖上班打卡时间校正为正常)
        if (isTimeIntervalCoveredBeginTime(new Date(latestOnDutyTimeLong), approvalTimeIntervalsThisInterval)) {
            onDutyLog.setApprovalStatus(PunchStatus.NORMAL.getCode());
            updateSmartAlignment(onDutyLog);
        } else {
            onDutyLog.setApprovalStatus(PunchStatus.BELATE.getCode());
            updateSmartAlignment(onDutyLog);
        }
    }

    private List<TimeInterval> filterTimeIntervalsCoverBetween(List<TimeInterval> approvalTimeIntervals, Long beginTimeLong, Long endTimeLong) {
        List<TimeInterval> approvalTimeIntervalsThisInterval = new ArrayList<>();
        // 获取到本次打卡时间段的请假
        if (!CollectionUtils.isEmpty(approvalTimeIntervals)) {
            approvalTimeIntervals.forEach(t -> {
                if (!(Long.compare(beginTimeLong, t.getEndTime().getTime()) >= 0 || Long.compare(endTimeLong, t.getBeginTime().getTime()) <= 0)) {
                    approvalTimeIntervalsThisInterval.add(t);
                }
            });
        }
        return approvalTimeIntervalsThisInterval;
    }

    private boolean isTimeIntervalCoveredBeginTime(Date beginTime, List<TimeInterval> tiDTOs) {
        if (beginTime == null) {
            return false;
        }
        if (CollectionUtils.isEmpty(tiDTOs)) {
            return false;
        }
        for (TimeInterval t : tiDTOs) {
            if (beginTime.compareTo(t.getBeginTime()) >= 0 && beginTime.compareTo(t.getEndTime()) < 0) {
                return true;
            }
        }
        return false;
    }

    private boolean isTimeIntervalCoveredEndTime(Date endTime, List<TimeInterval> tiDTOs) {
        if (endTime == null) {
            return false;
        }
        if (CollectionUtils.isEmpty(tiDTOs)) {
            return false;
        }
        for (TimeInterval t : tiDTOs) {
            if (endTime.compareTo(t.getBeginTime()) > 0 && endTime.compareTo(t.getEndTime()) <= 0) {
                return true;
            }
        }
        return false;
    }

    private boolean isTimeIntervalFullCovered(Date beginTime, Date endTime, List<TimeInterval> tiDTOs) {
        if (beginTime == null || endTime == null) {
            return false;
        }
        if (CollectionUtils.isEmpty(tiDTOs)) {
            return false;
        }
        for (TimeInterval t : tiDTOs) {
            if (t.getBeginTime().compareTo(beginTime) <= 0 && t.getEndTime().compareTo(endTime) >= 0) {
                return true;
            }
        }
        return false;
    }

    private boolean isTimeIntervalPartCovered(Date beginTime, Date endTime, TimeInterval t) {
        if (beginTime == null || endTime == null || t == null) {
            return false;
        }
        if (t.getBeginTime().compareTo(beginTime) >= 0 && t.getBeginTime().compareTo(endTime) <= 0) {
            return true;
        }
        if (t.getBeginTime().compareTo(beginTime) < 0 && t.getEndTime().compareTo(endTime) > 0) {
            return true;
        }
        if (t.getEndTime().compareTo(beginTime) >= 0 && t.getEndTime().compareTo(endTime) <= 0) {
            return true;
        }
        return false;
    }

    private Long calculateBelateTime(PunchTimeRule ptr, PunchLog onDutyLog, List<TimeInterval> approvalTimeIntervals, Long latestOnDutyTimeLong, Long earliestOffDutyTimeLong) {
        if (onDutyLog == null) {
            return Long.valueOf(0L);
        }
        // 不是迟到状态就直接返回0
        List<PunchStatus> belateStatusList = new ArrayList<>();
        belateStatusList.add(PunchStatus.BELATE);
        belateStatusList.add(PunchStatus.BLANDLE);
        belateStatusList.add(PunchStatus.BELATE_AND_FORGOT);
        Byte finalStatus = onDutyLog.getApprovalStatus() == null ? onDutyLog.getStatus() : onDutyLog.getApprovalStatus();
        if (!belateStatusList.contains(PunchStatus.fromCode(finalStatus))) {
            return Long.valueOf(0L);
        }
        List<TimeInterval> approvalTimeIntervalsThisInterval = filterTimeIntervalsCoverBetween(approvalTimeIntervals, latestOnDutyTimeLong, earliestOffDutyTimeLong);
        // 把休息时间覆盖到申请时间中
        if (ptr.getNoonLeaveTimeLong() != null && ptr.getAfternoonArriveTimeLong() != null) {
            PunchExceptionRequest request = new PunchExceptionRequest();
            request.setBeginTime(new Timestamp(onDutyLog.getPunchDate().getTime() + ptr.getNoonLeaveTimeLong()));
            request.setEndTime(new Timestamp(onDutyLog.getPunchDate().getTime() + ptr.getAfternoonArriveTimeLong()));
            addExceptionRequest2TimeIntervals(request, approvalTimeIntervalsThisInterval);
        }
        // 迟到并且该打卡时间段内没有异常申请的，直接计算实际打卡时间和上班时间的差
        if (CollectionUtils.isEmpty(approvalTimeIntervalsThisInterval)) {
            if (onDutyLog.getPunchTime() != null) {
                Long c = onDutyLog.getPunchTime().getTime() - latestOnDutyTimeLong;
                return c > 0 ? c : 0L;
            } else {
                return 0L;
            }
        }
        Collections.sort(approvalTimeIntervalsThisInterval, (o1, o2) -> {
            return Long.compare(o1.getBeginTime().getTime(), o2.getBeginTime().getTime());
        });
        if (onDutyLog.getPunchTime() != null) {
            // 上班有打卡、迟到
            return totalTimeIntervalGaps(approvalTimeIntervalsThisInterval, new Date(latestOnDutyTimeLong), onDutyLog.getPunchTime());
        }
        // 上班未打卡,校正后迟到
        return totalTimeIntervalGaps(approvalTimeIntervalsThisInterval, new Date(latestOnDutyTimeLong), approvalTimeIntervalsThisInterval.get(approvalTimeIntervalsThisInterval.size() - 1).getBeginTime());
    }

    private Long calculateLeaveEarlyTime(PunchTimeRule ptr, PunchLog offDutyLog, PunchLog onDutyLog, List<TimeInterval> approvalTimeIntervals, Long latestOnDutyTimeLong, Long earliestOffDutyTimeLong) {
        if (offDutyLog == null) {
            return Long.valueOf(0L);
        }
        // 不是早退状态就直接返回0
        List<PunchStatus> leaveEarlyStatusList = new ArrayList<>();
        leaveEarlyStatusList.add(PunchStatus.LEAVEEARLY);
        leaveEarlyStatusList.add(PunchStatus.BLANDLE);
        Byte finalStatus = offDutyLog.getApprovalStatus() == null ? offDutyLog.getStatus() : offDutyLog.getApprovalStatus();
        if (!leaveEarlyStatusList.contains(PunchStatus.fromCode(finalStatus))) {
            return Long.valueOf(0L);
        }
        List<TimeInterval> approvalTimeIntervalsThisInterval = filterTimeIntervalsCoverBetween(approvalTimeIntervals, latestOnDutyTimeLong, earliestOffDutyTimeLong);
        // 把休息时间覆盖到申请时间中
        if (ptr.getNoonLeaveTimeLong() != null && ptr.getAfternoonArriveTimeLong() != null) {
            PunchExceptionRequest request = new PunchExceptionRequest();
            request.setBeginTime(new Timestamp(offDutyLog.getPunchDate().getTime() + ptr.getNoonLeaveTimeLong()));
            request.setEndTime(new Timestamp(offDutyLog.getPunchDate().getTime() + ptr.getAfternoonArriveTimeLong()));
            addExceptionRequest2TimeIntervals(request, approvalTimeIntervalsThisInterval);
        }
        // 早退并且没有异常申请的，直接计算实际打卡时间和下班时间的差
        if (CollectionUtils.isEmpty(approvalTimeIntervalsThisInterval)) {
            if (offDutyLog.getPunchTime() != null) {
                Long c = earliestOffDutyTimeLong - offDutyLog.getPunchTime().getTime();
                return c > 0 ? c : 0L;
            } else {
                return 0L;
            }
        }
        Collections.sort(approvalTimeIntervalsThisInterval, (o1, o2) -> {
            return Long.compare(o1.getBeginTime().getTime(), o2.getBeginTime().getTime());
        });
        // 下班有打卡，早退
        if (offDutyLog.getPunchTime() != null) {
            return totalTimeIntervalGaps(approvalTimeIntervalsThisInterval, offDutyLog.getPunchTime(), new Date(earliestOffDutyTimeLong));
        }
        // 下班缺卡校正后早退
        Byte onDutyStatus = onDutyLog.getApprovalStatus() == null ? onDutyLog.getStatus() : onDutyLog.getApprovalStatus();
        if (PunchStatus.NORMAL == PunchStatus.fromCode(onDutyStatus)) {
            return totalTimeIntervalGaps(approvalTimeIntervalsThisInterval, new Date(latestOnDutyTimeLong), new Date(earliestOffDutyTimeLong));
        } else {
            Long c = earliestOffDutyTimeLong - approvalTimeIntervalsThisInterval.get(approvalTimeIntervalsThisInterval.size() - 1).getEndTime().getTime();
            return c > 0 ? c : 0L;
        }
    }

    // 计算打卡时间、考勤时间、多个审批单之间的时间差合
    private Long totalTimeIntervalGaps(List<TimeInterval> approvalTimeIntervalsThisInterval, Date betweenFrom, Date betweenTo) {
        List<TimeInterval> approvalTimeIntervalsBetween = filterTimeIntervalsCoverBetween(approvalTimeIntervalsThisInterval, betweenFrom.getTime(), betweenTo.getTime());
        Collections.sort(approvalTimeIntervalsBetween, new Comparator<TimeInterval>() {
            @Override
            public int compare(TimeInterval o1, TimeInterval o2) {
                return Long.compare(o1.getBeginTime().getTime(), o2.getBeginTime().getTime());
            }
        });
        Long total = 0L;
        Date begin = betweenFrom;
        if (!CollectionUtils.isEmpty(approvalTimeIntervalsBetween)) {
            Date end = approvalTimeIntervalsBetween.get(0).getBeginTime();
            for (int i = 0; i < approvalTimeIntervalsBetween.size(); i++) {
                Long a = end.getTime() - begin.getTime();
                if (a > 0) {
                    total += a;
                }
                begin = approvalTimeIntervalsBetween.get(i).getEndTime();
                if (i < approvalTimeIntervalsBetween.size() - 1) {
                    end = approvalTimeIntervalsBetween.get(i + 1).getBeginTime();
                }
            }
        }
        Long a = betweenTo.getTime() - begin.getTime();
        if (a > 0) {
            total += a;
        }
        return total;
    }


    /***
     * 组装异常申报和回复的记录
     * 更改异常标志
     */
    private void makeExceptionForDayList(Long userId, Long companyId,
                                         Calendar logDay, PunchLogsDay pdl) {
        //
        PunchExceptionApproval exceptionApproval = punchProvider
                .getPunchExceptionApprovalByDate(userId, companyId,
                        dateSF.get().format(logDay.getTime()));
        if (null != exceptionApproval) {
            if (pdl.getPunchTimesPerDay().equals(PunchTimesPerDay.TWICE.getCode())) {
                pdl.setApprovalStatus(exceptionApproval.getApprovalStatus());
                if (pdl.getApprovalStatus().equals(ApprovalStatus.NORMAL.getCode())
                        || pdl.getPunchStatus().equals(PunchStatus.NORMAL.getCode())
                        || pdl.getPunchStatus().equals(ApprovalStatus.OVERTIME.getCode())) {
                    // 如果有申报审批结果，并且审批结果和实际打卡结果有一个是正常的话，异常结果为正常 别的为异常
                    pdl.setExceptionStatus(ExceptionStatus.NORMAL.getCode());
                } else {
                    pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
                }
            } else if (pdl.getPunchTimesPerDay().equals(PunchTimesPerDay.FORTH.getCode())) {
                pdl.setMorningApprovalStatus(exceptionApproval.getMorningApprovalStatus());
                pdl.setAfternoonApprovalStatus(exceptionApproval.getAfternoonApprovalStatus());
                if (pdl.getMorningApprovalStatus().equals(ApprovalStatus.NORMAL.getCode()) &&
                        pdl.getAfternoonApprovalStatus().equals(ApprovalStatus.NORMAL.getCode())) {
                    pdl.setExceptionStatus(ExceptionStatus.NORMAL.getCode());
                } else {
                    pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
                }
            }
        }

        List<PunchExceptionRequest> exceptionRequests = punchProvider
                .listExceptionRequestsByDate(userId, companyId,
                        dateSF.get().format(logDay.getTime()));
        if (exceptionRequests.size() > 0) {
            for (PunchExceptionRequest exceptionRequest : exceptionRequests) {


                PunchExceptionDTO punchExceptionDTO = ConvertHelper.convert(exceptionRequest, PunchExceptionDTO.class);
                punchExceptionDTO.setRequestType(exceptionRequest
                        .getRequestType());
                punchExceptionDTO.setCreateTime(exceptionRequest
                        .getCreateTime().getTime());
                Organization organization = organizationProvider.findOrganizationById(companyId);
                if (exceptionRequest.getRequestType().equals(
                        PunchRquestType.REQUEST.getCode())) {
                    // 对于申请
                    punchExceptionDTO.setExceptionComment(exceptionRequest
                            .getDescription());
                    OrganizationMember member = findOrganizationMemberByOrgIdAndUId(exceptionRequest.getUserId(), organization.getPath());
                    if (null == member) {
                        punchExceptionDTO.setName("无此人");
                    } else {
                        punchExceptionDTO.setName(member.getContactName());
                    }
                } else {
                    // 审批
                    punchExceptionDTO.setExceptionComment(exceptionRequest
                            .getProcessDetails());
                    OrganizationMember member = findOrganizationMemberByOrgIdAndUId(exceptionRequest.getUserId(), organization.getPath());
                    if (null == member) {
                        punchExceptionDTO.setName("无此人");
                    } else {
                        punchExceptionDTO.setName(member.getContactName());
                    }
                }
                if (null == pdl.getPunchExceptionDTOs()) {
                    pdl.setPunchExceptionDTOs(new ArrayList<PunchExceptionDTO>());
                }
                pdl.getPunchExceptionDTOs().add(punchExceptionDTO);
            }
        }
    }


    @Override
    public PunchClockResponse createPunchLog(PunchClockCommand cmd) {

        checkCompanyIdIsNull(cmd.getEnterpriseId());
        cmd.setEnterpriseId(getTopEnterpriseId(cmd.getEnterpriseId()));
        String punchTime = datetimeSF.get().format(new Date());
        return createPunchLog(cmd, punchTime);

    }

    private PunchClockResponse createPunchLog(PunchClockCommand cmd, String punchTime) {
        byte punchCode;
        PunchLog punchLog = ConvertHelper.convert(cmd, PunchLog.class);
        
        try {
            punchCode = verifyPunchClock(cmd, punchLog).getCode();
            return createPunchLog(punchTime, punchCode, punchLog);
        } catch (Exception e) {
            //有报错就表示不成功
            LOGGER.error("punch clock error", e);
            punchLog.setPunchStatus(ClockCode.FAIL.getCode());
            punchProvider.createPunchLog(punchLog);
            throw e;
        }
    }

    private PunchClockResponse createPunchLog(String punchTime,
			byte punchCode, PunchLog punchLog) {

        PunchClockResponse response = new PunchClockResponse();
        if(punchLog.getCreateType() == null){
			punchLog.setCreateType(CreateType.NORMAL_PUNCH.getCode());
		}
        Long userId = UserContext.current().getUser().getId();
        // new Date()为获取当前系统时间为打卡时间
        punchLog.setUserId(userId);
        punchLog.setPunchTime(Timestamp.valueOf(punchTime));
        Calendar punCalendar = Calendar.getInstance();
        punCalendar.setTime(punchLog.getPunchTime());
        punchLog.setPunchDate(calculatePunchDate(punCalendar, punchLog.getEnterpriseId(), userId));

        PunchLogDTO punchType = getPunchType(userId, punchLog.getEnterpriseId(), punchLog.getPunchTime(), punchLog.getPunchDate());
        response.setClockStatus(punchType.getClockStatus());
        punchLog.setPunchOrganizationId(punchType.getPunchOrgnizationId());
        punchLog.setRuleTime(punchType.getRuleTime());
        punchLog.setShouldPunchTime(punchType.getShouldPunchTime());
        punchLog.setStatus(punchType.getClockStatus());
        //如果是下班之后打卡当做下班打卡
        if (punchType.getPunchType().equals(PunchType.FINISH.getCode())) {
            punchType.setPunchType(PunchType.OFF_DUTY.getCode());
        }
        punchLog.setPunchIntervalNo(punchType.getPunchIntervalNo());
        if (null == punchLog.getPunchType()) {
            punchLog.setPunchType(punchType.getPunchType());
        } else {
            switch (PunchType.fromCode(punchType.getPunchType())) {
                case NOT_WORKDAY:
                case MEIPAIBAN:
                    punchLog.setPunchStatus(ClockCode.FAIL.getCode());
                    break;
                default:
                    if (!punchLog.getPunchType().equals(punchType.getPunchType())) {
                    	//2018-10-11对自动打卡来说,就直接返回成功了
                    	if(CreateType.NORMAL_PUNCH != CreateType.fromCode(punchLog.getCreateType()))
                    		return response;
                        throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
                                PunchServiceErrorCode.ERROR_PUNCH_TYPE, "重新获取上下班类型");
                    }
                    break;
            }
        }


        response.setPunchCode(punchCode);
        punchLog.setPunchStatus(punchCode);
        punchLog.setDeviceChangeFlag(getDeviceChangeFlag(userId, punchLog.getEnterpriseId(),punchLog));
        //异常处理
        PunchExceptionRequest request = punchProvider.findPunchExceptionRequest(userId, punchLog.getEnterpriseId(), punchLog.getPunchDate(),
                punchLog.getPunchIntervalNo(), punchLog.getPunchType());
        if (null != request && request.getStatus().equals(com.everhomes.rest.approval.ApprovalStatus.AGREEMENT.getCode())) {
            punchLog.setApprovalStatus(PunchStatus.NORMAL.getCode());
        }


        try {
            OrganizationMemberDetails memberDetail = organizationProvider.findOrganizationMemberDetailsByTargetIdAndOrgId(userId, punchLog.getEnterpriseId());
            punchLog.setDetailId(memberDetail.getId());
            punchProvider.createPunchLog(punchLog);
            refreshPunchDayLog(memberDetail, punCalendar);
            punchNotificationService.setPunchNotificationInvalidBackground(memberDetail, punchLog, punchType.getPunchRuleId());
        } catch (ParseException e) {
            LOGGER.error("refresh punch day log error", e);
        }

        response.setPunchTime(punchTime);
        //发消息
        if(CreateType.NORMAL_PUNCH != CreateType.fromCode(punchLog.getCreateType())){
        	sendPunchMsg(punchLog);
        }
        return response;
    }

    private void sendPunchMsg(PunchLog punchLog) {  
        Map<String,String> map = new HashMap<>(); 
        String locale = UserContext.current().getUser() == null ? PunchConstants.locale : UserContext.current().getUser().getLocale();
        String createType = localeStringService.getLocalizedString(PunchConstants.PUNCH_CREATE_TYPE, String.valueOf(punchLog.getCreateType()), locale, "");
		map.put("createType", createType);
		map.put("punchTime", datetimeChineseSF.get().format(punchLog.getPunchTime()));
        String content = localeTemplateService.getLocaleTemplateString(PunchConstants.PUNCH_CREATE_MSG,
                PunchConstants.PUNCH_EXCEL_SCHEDULING_REMINDER, locale, map, "");
        MessageDTO message = new MessageDTO();
        message.setBodyType(MessageBodyType.TEXT.getCode());
        message.setBody(content);
        message.setMetaAppId(AppConstants.APPID_DEFAULT);
        message.setChannels(new MessageChannel(ChannelType.USER.getCode(), String.valueOf(punchLog.getUserId())));
        PunchClockRecordData actionData = new PunchClockRecordData();
		//  set the route 
        actionData.setDate(punchLog.getPunchDate().getTime());
        actionData.setOrganizationId(punchLog.getEnterpriseId());
        actionData.setUserId(punchLog.getUserId());
        String url = RouterBuilder.build(Router.ATTENDANCE_PUNCHCLOCK_RECORD, actionData);
        RouterMetaObject metaObject = new RouterMetaObject();
        metaObject.setUrl(url);
        Map<String, String> meta = new HashMap<>();
        meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.MESSAGE_ROUTER.getCode());
        meta.put(MessageMetaConstant.MESSAGE_SUBJECT, "上班打卡成功");
        meta.put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(metaObject));
        message.setMeta(meta);

        //  send the message
        messagingService.routeMessage(
                User.SYSTEM_USER_LOGIN,
                AppConstants.APPID_MESSAGING,
                ChannelType.USER.getCode(),
                String.valueOf(punchLog.getUserId()),
                message,
                MessagingConstants.MSG_FLAG_STORED.getCode()
        );

	}

	private Byte getDeviceChangeFlag(Long userId,  Long enterpriseId,
			PunchLog punchLog) {
		PunchLog lastLog = punchProvider.findLastPunchLog(userId, enterpriseId, punchLog.getPunchTime());
		if(null !=lastLog && punchLog.getIdentification() != null && !punchLog.getIdentification().equals(lastLog.getIdentification())){
			return NormalFlag.YES.getCode();
		}
		return NormalFlag.NO.getCode();
	}

	/**
     * 当前时间点 应该算哪一天
     * 1.看昨天的刷新时间
     * 2.看今天的刷新时间
     * 3.如果今天没排班,看明天的最早上班
     */
    @Override
    public java.sql.Date calculatePunchDate(Calendar punCalendar, Long enterpriseId, Long userId) {

        PunchRule pr = getPunchRule(PunchOwnerType.ORGANIZATION.getCode(), enterpriseId, userId);
        if (null == pr)
            throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
                    PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_SETTING,
                    "公司没有设置打卡规则");
        //把当天的时分秒转换成Long型
        Long punchTimeLong = getTimeLong(punCalendar, null);

//				punchProvider.getPunchTimeRuleById(pr.getTimeRuleId());
        PunchTimeRule ptr = getPunchTimeRuleByRuleIdAndDate(pr, punCalendar.getTime(), userId);

        Calendar yesterday = Calendar.getInstance();
        yesterday.setTime(punCalendar.getTime());
        yesterday.add(Calendar.DATE, -1);
        PunchTimeRule yesterdayPtr = getPunchTimeRuleByRuleIdAndDate(pr, yesterday.getTime(), userId);
        //默认分界点是次日4点,如果timerule有设置就用设置的
        Long splitTime = PunchConstants.ONE_DAY_MS + PunchConstants.DEFAULT_SPLIT_TIME;
        Long yesterdaySplitTime = PunchConstants.ONE_DAY_MS + PunchConstants.DEFAULT_SPLIT_TIME;


        if (null != ptr && null != ptr.getDaySplitTimeLong())
            splitTime = ptr.getDaySplitTimeLong();
        else if (null != ptr && null != ptr.getDaySplitTime())
            splitTime = convertTimeToGMTMillisecond(ptr.getDaySplitTime());
        else if (null == ptr) {

            Calendar tomorrow = Calendar.getInstance();
            tomorrow.setTime(punCalendar.getTime());
            tomorrow.add(Calendar.DATE, 1);

            PunchTimeRule tomorrowPtr = getPunchTimeRuleByRuleIdAndDate(pr, tomorrow.getTime(), userId);
            if (null != tomorrowPtr) {
                PunchTimeRuleDTO ptrDTO = convertPunchTimeRule2DTO(tomorrowPtr);
                splitTime = PunchConstants.ONE_DAY_MS + ptrDTO.getPunchTimeIntervals().get(0).getArriveTime()
                        - timeIsNull(ptrDTO.getBeginPunchTime(), PunchConstants.DEFAULT_SPLIT_TIME);
            }
        }

        if (null != yesterdayPtr && null != yesterdayPtr.getDaySplitTimeLong())
            yesterdaySplitTime = yesterdayPtr.getDaySplitTimeLong();
        else if (null != yesterdayPtr && null != yesterdayPtr.getDaySplitTime())
            yesterdaySplitTime = convertTimeToGMTMillisecond(yesterdayPtr.getDaySplitTime());
        //TODO: 用日期来处理
        if (punchTimeLong + PunchConstants.ONE_DAY_MS < yesterdaySplitTime) {
            //取前一天的ptr,如果周期分界点>打卡时间+86400000 则算前一天
            punCalendar.setTime(yesterday.getTime());
        } else if (punchTimeLong.compareTo(splitTime) > 0) {
            //取今天的ptr,如果周期分界点<打卡时间 则算后一天
            punCalendar.add(Calendar.DATE, 1);
        }
        //其它算当天

        return java.sql.Date.valueOf(dateSF.get().format(punCalendar
                .getTime()));
    }

    private ClockCode verifyPunchClock(PunchClockCommand cmd, PunchLog punchLog) {
        //获取打卡规则
//		ClockCode code = ClockCode.SUCESS;
        Long userId = UserContext.current().getUser().getId();
        PunchRule pr = getPunchRule(PunchOwnerType.ORGANIZATION.getCode(), cmd.getEnterpriseId(), userId);
        if (null == pr)
            throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
                    PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_SETTING,
                    "公司没有设置打卡规则 userId =[" + userId + "] orgId=[" + cmd.getEnterpriseId() + "]");
        //是否有wifi打卡,如果是判断wifi是否符合

        List<PunchGeopoint> punchGeopoints = punchProvider.listPunchGeopointsByOwner(PunchOwnerType.ORGANIZATION.getCode(), pr.getPunchOrganizationId());
        List<PunchWifi> wifis = punchProvider.listPunchWifsByOwner(PunchOwnerType.ORGANIZATION.getCode(), pr.getPunchOrganizationId());
        if (null != wifis && null != cmd.getWifiMac()) {
            for (PunchWifi wifi : wifis) {
                if (null != wifi.getMacAddress() && wifi.getMacAddress().toLowerCase().equals(cmd.getWifiMac().toLowerCase())) {
                    punchLog.setWifiInfo(wifi.getSsid() + "(" + wifi.getMacAddress() + ")");
                    return ClockCode.SUCESS;
                }
            }

        }

        //参数有地址规则看地址范围是否正确,不正确则报错
        if (null == punchGeopoints || punchGeopoints.size() == 0) {
            //wifi不符合看是否有地址规则,没有地址规则直接报错
            if (null == cmd.getWifiMac())
                throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
                        PunchServiceErrorCode.ERROR_WIFI_NULL, "wifi mac address is null");
            else
                throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
                        PunchServiceErrorCode.ERROR_WIFI_WRONG, "wifi mac address is wrong");
        }
        //有地址规则查看参数是否有地址规则,无则报错
        if (null == cmd.getLatitude() || null == cmd.getLongitude())
            throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
                    PunchServiceErrorCode.ERROR_GEOPOINT_NULL, "user location is null");

        for (PunchGeopoint punchGeopoint : punchGeopoints) {
            if (calculateDistance(cmd.getLongitude(), cmd.getLatitude(),
                    punchGeopoint.getLongitude(), punchGeopoint.getLatitude()) <= punchGeopoint
                    .getDistance()) {
                punchLog.setLocationInfo(punchGeopoint.getDescription());
                // 如果找到了一个就跳出
                return ClockCode.SUCESS;
            }

        }
        if (null == wifis || wifis.size() == 0)
            throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
                    PunchServiceErrorCode.ERROR_USER_NOT_IN_PUNCHAREA, "not in punch area");
        else
            throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
                    PunchServiceErrorCode.ERROR_NOT_IN_AREA_AND_WIFI, "not in punch area and not in wifi");

    }

    /***
     * return 两个坐标之间的距离 单位 米
     */
    private double calculateDistance(double longitude1, double latitude1,
                                     double longitude2, double latitude2) {
        double radianLat1 = angle2Radian(latitude1);
        double radianLat2 = angle2Radian(latitude2);
        double differenceLat = radianLat1 - radianLat2;
        double differenceLng = angle2Radian(longitude1)
                - angle2Radian(longitude2);

        double s = 2 * Math.asin(Math.sqrt(Math.pow(
                Math.sin(differenceLat / 2), 2)
                + Math.cos(radianLat1)
                * Math.cos(radianLat2)
                * Math.pow(Math.sin(differenceLng / 2), 2)));
        s = s * 6378137.0D;
        s = Math.round(s * 10000) / 10000;

        return s;
    }

    private double angle2Radian(double angle) {
        return angle * Math.PI / 180.0;
    }

    private void convertTime(PunchTimeRule punchRule, Long startEarlyTime,
                             Long startLastTime, Long endEarlyTime) {
        Time startEarly = convertTime(startEarlyTime);
        punchRule.setStartEarlyTime(startEarly);
        punchRule.setStartLateTime(convertTime(startLastTime));
        Time endEarly = convertTime(endEarlyTime);
        Long workTime = endEarly.getTime() - startEarly.getTime();

        punchRule.setWorkTimeLong(workTime);
        punchRule.setWorkTime(convertTime(workTime));
    }

    private Time convertTime(Long TimeLong) {
        if (null != TimeLong) {
            //从8点开始计算
            return new Time(TimeLong - PunchConstants.MILLISECONDGMT);
        }
        return null;
    }

    @Override
    public Long convertTimeToGMTMillisecond(Time time) {
        if (null != time) {
            //从8点开始计算
            return time.getTime() + PunchConstants.MILLISECONDGMT;
        }
        return null;
    }

    @Override
    public boolean isWorkDay(Date date1, PunchRule punchRule, Long userId) {

        PunchTimeRule punchTimeRule = getPunchTimeRuleByRuleIdAndDate(punchRule, date1, userId);
        if (null != punchTimeRule)
            return true;
        return false;

    }

    private boolean isWorkTime(Time time, PunchTimeRule punchTimeRule) {
        if (time == null || punchTimeRule == null) {
            return false;
        }
        time = Time.valueOf(timeSF.get().format(time));
        //时间在最早上班时间到最晚下班时间之间且不在午休时间范围内
        if (!time.before(punchTimeRule.getStartEarlyTime()) && !time.after(getEndTime(punchTimeRule.getStartLateTime(), punchTimeRule.getWorkTime()))
                && !(time.after(punchTimeRule.getNoonLeaveTime()) && time.before(punchTimeRule.getAfternoonArriveTime()))) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isSameDay(Date date1, Date date2) {
        return dateSF.get().format(date1).equals(dateSF.get().format(date2));
    }


    @Override
    public Time getEndTime(Time startTime, Time workTime) {
        return new Time(convertTimeToGMTMillisecond(startTime) + convertTimeToGMTMillisecond(workTime) - PunchConstants.MILLISECONDGMT);
    }

    @Override
    public boolean isWorkTime(Time time, PunchRule punchRule, Date date, Long userId) {
        return isWorkTime(time, getPunchTimeRuleByRuleIdAndDate(punchRule, date, userId));
    }

    @Override
    public boolean isRestTime(Date fromTime, Date endTime, PunchRule punchRule, Long userId) {
        return isSameDay(fromTime, endTime)
                && timeSF.get().format(fromTime).equals(timeSF.get().format(getPunchTimeRuleByRuleIdAndDate(punchRule, fromTime, userId).getNoonLeaveTime()))
                && timeSF.get().format(endTime).equals(timeSF.get().format(getPunchTimeRuleByRuleIdAndDate(punchRule, endTime, userId).getAfternoonArriveTime()));

    }


    @Override
    public ListPunchExceptionRequestCommandResponse listExceptionRequests(
            ListPunchExceptionRequestCommand cmd) {
        checkCompanyIdIsNull(cmd.getEnterpriseId());
        Organization organization = organizationProvider.findOrganizationById(cmd.getEnterpriseId());
        ListPunchExceptionRequestCommandResponse response = new ListPunchExceptionRequestCommandResponse();
        cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());

        List<String> groupTypeList = new ArrayList<String>();
        groupTypeList.add(OrganizationGroupType.ENTERPRISE.getCode());
        groupTypeList.add(OrganizationGroupType.DEPARTMENT.getCode());
        List<OrganizationMemberDTO> organizationMembers = this.organizationService.listAllChildOrganizationPersonnel
                (cmd.getEnterpriseId(), groupTypeList, cmd.getKeyword());
        if (null == organizationMembers)
            return response;
        List<Long> userIds = new ArrayList<Long>();
        for (OrganizationMemberDTO member : organizationMembers) {
            userIds.add(member.getTargetId());
        }
        int totalCount = punchProvider.countExceptionRequests(userIds,
                cmd.getEnterpriseId(), cmd.getStartDay(), cmd.getEndDay(),
                cmd.getExceptionStatus(), cmd.getProcessCode(),
                PunchRquestType.REQUEST.getCode());
        if (totalCount == 0)
            return response;

        int pageSize = PaginationConfigHelper.getPageSize(
                configurationProvider, cmd.getPageSize());
        int pageCount = getPageCount(totalCount, pageSize);

        List<PunchExceptionRequest> result = punchProvider
                .listExceptionRequests(userIds, cmd.getEnterpriseId(),
                        cmd.getStartDay(), cmd.getEndDay(),
                        cmd.getExceptionStatus(), cmd.getProcessCode(),
                        cmd.getPageOffset(), pageSize,
                        PunchRquestType.REQUEST.getCode());
        response.setExceptionRequestList(result
                .stream()
                .map(r -> {
                    PunchExceptionRequestDTO dto = ConvertHelper.convert(r,
                            PunchExceptionRequestDTO.class);
                    Calendar logDay = Calendar.getInstance();
                    dto.setPunchDate(r.getPunchDate().getTime());
                    logDay.setTime(r.getPunchDate());

                    PunchDayLog punchDayLog = punchProvider.getDayPunchLogByDateAndUserId(dto.getUserId(),
                            dto.getEnterpriseId(), dateSF.get().format(logDay.getTime()));
                    if (null == punchDayLog) {
                        // 插入数据
                        try {
                            OrganizationMemberDetails memberDetail = organizationProvider.findOrganizationMemberDetailsByTargetIdAndOrgId(dto.getUserId(), dto.getEnterpriseId());
                            punchDayLog = refreshPunchDayLog(memberDetail, logDay);
                        } catch (ParseException e) {
                            throw RuntimeErrorException.errorWith(
                                    PunchServiceErrorCode.SCOPE,
                                    PunchServiceErrorCode.ERROR_PUNCH_REFRESH_DAYLOG,
                                    "ERROR IN REFRESHPUNCHDAYLOG  LINE 353");
                        }
                        if (null == punchDayLog) {
                            // 验证后为空
                            throw RuntimeErrorException.errorWith(
                                    ErrorCodes.SCOPE_GENERAL,
                                    ErrorCodes.ERROR_INVALID_PARAMETER,
                                    "Invalid description parameter in the command");
                        }
                    }
                    dto.setPunchStatus(punchDayLog.getStatus());
                    dto.setMorningPunchStatus(punchDayLog.getMorningStatus());
                    dto.setAfternoonPunchStatus(punchDayLog.getAfternoonStatus());
                    if (null != punchDayLog.getNoonLeaveTime())
                        dto.setNoonLeaveTime(punchDayLog.getNoonLeaveTime().getTime());
                    if (null != punchDayLog.getAfternoonArriveTime())
                        dto.setAfternoonArriveTime(punchDayLog.getAfternoonArriveTime().getTime());
                    if (null != punchDayLog.getArriveTime())
                        dto.setArriveTime(punchDayLog.getArriveTime().getTime());
                    if (null != punchDayLog.getLeaveTime())
                        dto.setLeaveTime(punchDayLog.getLeaveTime().getTime());
                    if (null != punchDayLog.getWorkTime())
                        dto.setWorkTime(punchDayLog.getWorkTime().getTime());
                    dto.setPunchTimesPerDay(punchDayLog.getPunchTimesPerDay());
                    OrganizationMember member = findOrganizationMemberByOrgIdAndUId(dto.getUserId(), organization.getPath());

                    if (null == member) {
                    } else {
                        dto.setUserName(member.getContactName());
                        dto.setUserPhoneNumber(member.getContactToken());
                    }

                    if (null != dto.getOperatorUid()
                            && 0 != dto.getOperatorUid()) {

                        member = findOrganizationMemberByOrgIdAndUId(dto.getOperatorUid(), organization.getPath());

                        if (null == member) {
                            dto.setOperatorName("无此人");
                        } else {
                            dto.setOperatorName(member.getContactName());
                        }
                    }

                    PunchExceptionApproval approval = punchProvider.getExceptionApproval(r.getUserId(), cmd.getEnterpriseId(), r.getPunchDate());
                    if (null != approval) {
                        dto.setMorningApprovalStatus(approval.getMorningApprovalStatus());
                        dto.setAfternoonApprovalStatus(approval.getAfternoonApprovalStatus());
                    }

                    return dto;
                }).collect(Collectors.toList()));

        response.setNextPageOffset(cmd.getPageOffset() == pageCount ? null
                : cmd.getPageOffset() + 1);
        return response;
    }

    private int getPageCount(int totalCount, int pageSize) {
        int pageCount = totalCount / pageSize;

        if (totalCount % pageSize != 0) {
            pageCount++;
        }
        return pageCount;
    }

    @Override
    public ListPunchExceptionRequestCommandResponse listExceptionApprovals(
            ListPunchExceptionApprovalCommand cmd) {
        if (null == cmd.getUserId() || cmd.getUserId().equals(0L)) {
            LOGGER.error("Invalid user Id parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid company Id parameter in the command");
        }
        checkCompanyIdIsNull(cmd.getEnterpriseId());
        ListPunchExceptionRequestCommandResponse response = new ListPunchExceptionRequestCommandResponse();
        cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
        int totalCount = punchProvider.countExceptionRequests(cmd.getUserId(),
                null, cmd.getEnterpriseId(), cmd.getPunchDate(),
                cmd.getPunchDate(), null, null,
                PunchRquestType.APPROVAL.getCode());
        if (totalCount == 0)
            return response;
        int pageSize = PaginationConfigHelper.getPageSize(
                configurationProvider, cmd.getPageSize());
        int pageCount = getPageCount(totalCount, pageSize);

        List<PunchExceptionRequest> result = punchProvider
                .listExceptionRequests(cmd.getUserId(), null,
                        cmd.getEnterpriseId(), cmd.getPunchDate(),
                        cmd.getPunchDate(), null, null, cmd.getPageOffset(),
                        pageSize, PunchRquestType.APPROVAL.getCode());
        response.setExceptionRequestList(result
                .stream()
                .map(r -> {
                    PunchExceptionRequestDTO dto = ConvertHelper.convert(r,
                            PunchExceptionRequestDTO.class);
                    OrganizationMember member = organizationProvider.findOrganizationMemberByUIdAndOrgId(dto.getUserId(), cmd.getEnterpriseId());
                    PunchExceptionApproval approval = punchProvider.getExceptionApproval(cmd.getUserId(), cmd.getEnterpriseId(), java.sql.Date.valueOf(cmd.getPunchDate()));
                    if (null != member) {
                        dto.setUserName(member.getContactName());
                        dto.setUserPhoneNumber(member.getContactToken());
                    }
                    if (null != dto.getOperatorUid()
                            && 0 != dto.getOperatorUid()) {

                        member = organizationProvider.findOrganizationMemberByUIdAndOrgId(dto.getOperatorUid(), cmd.getEnterpriseId());
                        if (null == member) {
                            dto.setOperatorName("");
                        } else {
                            dto.setOperatorName(member.getContactName());
                        }

                    }

                    if (null == approval) {
                        return dto;
                    }
                    dto.setPunchTimesPerDay(approval.getPunchTimesPerDay());
                    dto.setPunchDate(approval.getPunchDate().getTime());


                    return dto;
                }).collect(Collectors.toList()));

        response.setNextPageOffset(cmd.getPageOffset() == pageCount ? null
                : cmd.getPageOffset() + 1);
        return response;
    }

    @Override
    public void punchExceptionApproval(ApprovalPunchExceptionCommand cmd) {
        if (null == cmd.getUserId() || cmd.getUserId().equals(0L)) {
            LOGGER.error("Invalid user Id parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid company Id parameter in the command");
        }

        if (cmd.getStatus() == null) {
            LOGGER.error("Invalid Status parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid Status   parameter in the command");

        }
        if (cmd.getStatus().equals(ExceptionProcessStatus.ACTIVE.getCode())
                && cmd.getMorningApprovalStatus() == null && cmd.getAfternoonApprovalStatus() == null && cmd.getApprovalStatus() == null) {
            LOGGER.error("morningApprovalStatus、afternoonApprovalStatus、approvalStatus cannot be null at the same time!");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER,
                    "morningApprovalStatus、afternoonApprovalStatus、approvalStatus cannot be null at the same time!");
        }
        checkCompanyIdIsNull(cmd.getEnterpriseId());
        // 插入一条eh_punch_exception_requests 记录
        PunchExceptionRequest punchExceptionRequest = ConvertHelper.convert(cmd, PunchExceptionRequest.class);
        punchExceptionRequest.setEnterpriseId(cmd.getEnterpriseId());
        punchExceptionRequest
                .setRequestType(PunchRquestType.APPROVAL.getCode());
        punchExceptionRequest.setDescription(cmd.getProcessDetails());
        punchExceptionRequest.setUserId(cmd.getUserId());
        punchExceptionRequest.setCreatorUid(cmd.getCreatorUid());
        punchExceptionRequest.setCreateTime(new Timestamp(DateHelper
                .currentGMTTime().getTime()));
        punchExceptionRequest.setOperatorUid(cmd.getOperatorUid());
        punchExceptionRequest.setOperateTime(new Timestamp(DateHelper
                .currentGMTTime().getTime()));
        punchExceptionRequest.setPunchDate(java.sql.Date.valueOf(cmd
                .getPunchDate()));
        punchExceptionRequest.setViewFlag(ViewFlags.NOTVIEW.getCode());
        punchProvider.createPunchExceptionRequest(punchExceptionRequest);
        // 查eh_punch_exception_approvals有无数据：无数据，结果是同意则插入 /有数据 如果结果是同意
        // 则修改，结果是驳回则删除
        PunchExceptionApproval punchExceptionApproval = new PunchExceptionApproval();
        punchExceptionApproval.setEnterpriseId(cmd.getEnterpriseId());
        punchExceptionApproval.setApprovalStatus(cmd.getApprovalStatus());
        punchExceptionApproval.setMorningApprovalStatus(cmd.getMorningApprovalStatus());
        punchExceptionApproval.setAfternoonApprovalStatus(cmd.getAfternoonApprovalStatus());
        punchExceptionApproval.setUserId(cmd.getUserId());
        punchExceptionApproval.setCreatorUid(cmd.getCreatorUid());
        punchExceptionApproval.setCreateTime(new Timestamp(DateHelper
                .currentGMTTime().getTime()));
        punchExceptionApproval.setOperatorUid(cmd.getOperatorUid());
        punchExceptionApproval.setOperateTime(new Timestamp(DateHelper
                .currentGMTTime().getTime()));
        punchExceptionApproval.setPunchDate(java.sql.Date.valueOf(cmd
                .getPunchDate()));
        punchExceptionApproval.setViewFlag(ViewFlags.NOTVIEW.getCode());
        punchExceptionApproval.setPunchTimesPerDay(cmd.getPunchTimesPerDay());
        PunchExceptionApproval oldpunchExceptionApproval = punchProvider
                .getExceptionApproval(cmd.getUserId(), cmd.getEnterpriseId(),
                        java.sql.Date.valueOf(cmd.getPunchDate()));
        if (null == oldpunchExceptionApproval) {
            if (cmd.getStatus().equals(ExceptionProcessStatus.ACTIVE.getCode())) {
                punchProvider.createPunchExceptionApproval(punchExceptionApproval);
            }
        } else {
            punchExceptionApproval.setId(oldpunchExceptionApproval.getId());
            if (cmd.getStatus().equals(ExceptionProcessStatus.ACTIVE.getCode())) {
                punchProvider.updatePunchExceptionApproval(punchExceptionApproval);
            } else {
                punchProvider.deletePunchExceptionApproval(punchExceptionApproval.getId());
            }
        }
        // 更新eh_punch_exception_requests当天当人的申请记录
        List<PunchExceptionRequest> results = punchProvider
                .listExceptionRequests(cmd.getUserId(), null,
                        cmd.getEnterpriseId(), cmd.getPunchDate(),
                        cmd.getPunchDate(), null, null, 1, 999999,
                        PunchRquestType.REQUEST.getCode());
        for (PunchExceptionRequest result : results) {

            result.setApprovalStatus(cmd.getApprovalStatus());
            result.setMorningApprovalStatus(cmd.getMorningApprovalStatus());
            result.setAfternoonApprovalStatus(cmd.getAfternoonApprovalStatus());
            result.setProcessDetails(cmd.getProcessDetails());
            result.setUserId(cmd.getUserId());
            result.setOperatorUid(cmd.getOperatorUid());
            result.setOperateTime(new Timestamp(DateHelper.currentGMTTime()
                    .getTime()));
            result.setStatus(cmd.getStatus());
            punchProvider.updatePunchExceptionRequest(result);
        }
    }

    /**
     * 打卡1.0的统计,已经不用了
     */
    @Override
    public ListPunchStatisticsCommandResponse listPunchStatistics(
            ListPunchStatisticsCommand cmd) {
        checkCompanyIdIsNull(cmd.getEnterpriseId());
        ListPunchStatisticsCommandResponse response = new ListPunchStatisticsCommandResponse();
        cmd.setPageOffset(cmd.getPageOffset() == null ? 1 : cmd.getPageOffset());
        ListOrganizationContactCommand orgCmd = new ListOrganizationContactCommand();
        orgCmd.setOrganizationId(cmd.getEnterpriseId());
//		if(null != cmd.getEnterpriseGroupId()){
//			orgCmd.setOrganizationId(cmd.getEnterpriseGroupId());
//		}
        orgCmd.setKeywords(cmd.getKeyword());
        orgCmd.setPageSize(100000);
        ListOrganizationMemberCommandResponse resp = organizationService.listOrganizationPersonnelsWithDownStream(orgCmd);
        List<OrganizationMemberDTO> members = resp.getMembers();
        List<Long> userIds = new ArrayList<Long>();
        if (null != members) {
            for (OrganizationMemberDTO member : members) {
                if (member.getTargetType().equals(OrganizationMemberTargetType.USER.getCode())) {
                    userIds.add(member.getTargetId());
                }
            }
        }

        int totalCount = punchProvider.countPunchDayLogs(userIds,
                cmd.getEnterpriseId(), cmd.getStartDay(), cmd.getEndDay(),
                cmd.getArriveTimeCompareFlag(), cmd.getArriveTime(),
                cmd.getLeaveTimeCompareFlag(), cmd.getLeaveTime(),
                cmd.getWorkTimeCompareFlag(), cmd.getWorkTime(),
                cmd.getStatus());
        if (totalCount == 0)
            return response;

        Integer pageSize = PaginationConfigHelper.getPageSize(
                configurationProvider, cmd.getPageSize());
        int pageCount = getPageCount(totalCount, pageSize);
        Organization organization = organizationProvider.findOrganizationById(cmd.getEnterpriseId());
        List<String> groupTypes = new ArrayList<String>();
        groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
        List<Organization> departments = organizationProvider.listOrganizationByGroupTypes(organization.getPath() + "%", groupTypes);
        Map<Long, Organization> deptMap = this.convertDeptListToMap(departments);
        List<PunchDayLog> result = punchProvider.listPunchDayLogs(userIds,
                cmd.getEnterpriseId(), cmd.getStartDay(), cmd.getEndDay(),
                cmd.getStatus(), cmd.getArriveTimeCompareFlag(),
                cmd.getArriveTime(), cmd.getLeaveTimeCompareFlag(),
                cmd.getLeaveTime(), cmd.getWorkTimeCompareFlag(),
                cmd.getWorkTime(), cmd.getPageOffset(), pageSize);
        response.setPunchList(result
                .stream()
                .map(r -> {
                    PunchStatisticsDTO dto = ConvertHelper.convert(r,
                            PunchStatisticsDTO.class);
                    processPunchStatisticsDTOTime(dto, r);
                    if (dto != null) {
                        OrganizationMember member = organizationProvider.findOrganizationMemberByUIdAndOrgId(dto.getUserId(), cmd.getEnterpriseId());
                        if (null != member) {
                            Organization department = deptMap.get(member.getGroupId());
                            if (null != department) {
                                dto.setUserEnterpriseGroup(department.getName());
                            }

                            dto.setUserName(member.getContactName());
                            dto.setUserPhoneNumber(member.getContactToken());
                            // dto.setUserDepartment(enterpriseContact.get);
                            PunchExceptionApproval approval = punchProvider
                                    .getExceptionApproval(dto.getUserId(),
                                            dto.getEnterpriseId(),
                                            new java.sql.Date(dto.getPunchDate()));
                            if (approval != null) {
                                dto.setApprovalStatus(approval
                                        .getApprovalStatus());
                                dto.setMorningApprovalStatus(approval.getMorningApprovalStatus());
                                dto.setAfternoonApprovalStatus(approval.getAfternoonApprovalStatus());
                                OrganizationMember operator = organizationProvider.findOrganizationMemberByUIdAndOrgId(approval.getOperatorUid(), cmd.getEnterpriseId());
                                if (null != operator)
                                    dto.setOperatorName(operator.getContactName());
                            } else {
                                //do nothing
//								dto.setApprovalStatus((byte) 0);
                            }
                        }
                    }
                    return dto;
                }).collect(Collectors.toList()));

        response.setNextPageOffset(cmd.getPageOffset() == pageCount ? null
                : cmd.getPageOffset() + 1);
        return response;

    }

    private Map<Long, Organization> convertDeptListToMap(List<Organization> depts) {
        Map<Long, Organization> map = new HashMap<Long, Organization>();
        if (null == depts) {
            return map;
        }
        for (Organization dept : depts) {
            map.put(dept.getId(), dept);
        }
        return map;
    }

    private void processPunchStatisticsDTOTime(PunchStatisticsDTO dto,
                                               PunchDayLog r) {
        if (null != r.getArriveTime())
            dto.setArriveTime(convertTimeToGMTMillisecond(r.getArriveTime()));

        if (null != r.getLeaveTime())
            dto.setLeaveTime(convertTimeToGMTMillisecond(r.getLeaveTime()));

        if (null != r.getWorkTime())
            dto.setWorkTime(convertTimeToGMTMillisecond(r.getWorkTime()));

        if (null != r.getNoonLeaveTime())
            dto.setNoonLeaveTime(convertTimeToGMTMillisecond(r.getNoonLeaveTime()));

        if (null != r.getAfternoonArriveTime())
            dto.setAfternoonArriveTime(convertTimeToGMTMillisecond(r.getAfternoonArriveTime()));
        if (null != r.getPunchDate())
            dto.setPunchDate(r.getPunchDate().getTime());
        dto.setPunchTimesPerDay(r.getPunchTimesPerDay());
    }

    @Override
    public GetPunchNewExceptionCommandResponse getPunchNewException(
            GetPunchNewExceptionCommand cmd) {
        checkCompanyIdIsNull(cmd.getEnterpriseId());
        Long userId = UserContext.current().getUser().getId();
        GetPunchNewExceptionCommandResponse response = new GetPunchNewExceptionCommandResponse();
        response.setExceptionCode(ExceptionStatus.NORMAL.getCode());
        // TODO：从本月初，或者第一次打卡开始
//		Calendar start = Calendar.getInstance();
        // 月初
        Calendar monthStart = Calendar.getInstance();
        monthStart.add(Calendar.DAY_OF_MONTH, -30);
        // 前一天
        Calendar end = Calendar.getInstance();
        end.add(Calendar.DAY_OF_MONTH, -1);
        // 找出异常的记录
        List<PunchDayLog> PunchDayLogs = punchProvider
                .listPunchDayExceptionLogs(userId, cmd.getEnterpriseId(),
                        dateSF.get().format(monthStart.getTime()),
                        dateSF.get().format(end.getTime()));
        if (PunchDayLogs.size() > 0)
            response.setExceptionCode(ExceptionStatus.EXCEPTION.getCode());
        List<PunchExceptionRequest> exceptionRequests = punchProvider
                .listExceptionNotViewRequests(userId, cmd.getEnterpriseId(),
                        dateSF.get().format(monthStart.getTime()),
                        dateSF.get().format(end.getTime()));
        if (exceptionRequests.size() > 0)
            response.setExceptionCode(ExceptionStatus.EXCEPTION.getCode());
        return response;
    }

    @Override
    public PunchLogsDay getDayPunchLogs(GetDayPunchLogsCommand cmd) {
        Long userId = UserContext.current().getUser().getId();

        checkCompanyIdIsNull(cmd.getEnterpirseId());

        cmd.setEnterpirseId(getTopEnterpriseId(cmd.getEnterpirseId()));
        Calendar logDay = Calendar.getInstance();

        try {

            logDay.setTime(dateSF.get().parse(cmd.getQueryDate()));
            Calendar today = Calendar.getInstance();
            // 对于今天以后的查询，都返回为null
            if (logDay.after(today))
                return null;
        } catch (Exception e) {
            throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
                    ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid queryDate parameter in the command");
        }
        Version version = new Version(1, 0, 0);
        Version anchorVerison = new Version(4, 9, 1);
        if (null != UserContext.current() && null != UserContext.current().getVersion()) {
            version = Version.fromVersionString(UserContext.current().getVersion());
        }
        PunchLogsDay pdl = null;
        if (version.getEncodedValue() < anchorVerison.getEncodedValue()) {

            pdl = makePunchLogsDayListInfo(userId,
                    cmd.getEnterpirseId(), logDay);
            pdl.setPunchStatusNew(pdl.getPunchStatus());
            pdl.setMorningPunchStatusNew(pdl.getMorningPunchStatus());
            pdl.setAfternoonPunchStatusNew(pdl.getAfternoonPunchStatus());
            if (pdl.getPunchStatus() != null && ApprovalStatus.FORGOT.equals(ApprovalStatus.fromCode(pdl.getPunchStatus()))) {
                pdl.setPunchStatus(ApprovalStatus.UNPUNCH.getCode());
            }
            if (pdl.getMorningPunchStatus() != null && ApprovalStatus.FORGOT.equals(ApprovalStatus.fromCode(pdl.getMorningPunchStatus()))) {
                pdl.setMorningPunchStatus(ApprovalStatus.UNPUNCH.getCode());
            }
            if (pdl.getAfternoonPunchStatus() != null && ApprovalStatus.FORGOT.equals(ApprovalStatus.fromCode(pdl.getAfternoonPunchStatus()))) {
                pdl.setAfternoonPunchStatus(ApprovalStatus.UNPUNCH.getCode());
            }
            pdl.setApprovalStatusNew(pdl.getApprovalStatus());
            pdl.setMorningApprovalStatusNew(pdl.getMorningApprovalStatus());
            pdl.setAfternoonApprovalStatusNew(pdl.getAfternoonApprovalStatus());
            if (pdl.getApprovalStatus() != null && ApprovalStatus.FORGOT.equals(ApprovalStatus.fromCode(pdl.getApprovalStatus()))) {
                pdl.setApprovalStatus(ApprovalStatus.UNPUNCH.getCode());
            }
            if (pdl.getMorningApprovalStatus() != null && ApprovalStatus.FORGOT.equals(ApprovalStatus.fromCode(pdl.getMorningApprovalStatus()))) {
                pdl.setMorningApprovalStatus(ApprovalStatus.UNPUNCH.getCode());
            }
            if (pdl.getAfternoonApprovalStatus() != null && ApprovalStatus.FORGOT.equals(ApprovalStatus.fromCode(pdl.getAfternoonApprovalStatus()))) {

            }
            pdl.setAfternoonApprovalStatus(ApprovalStatus.UNPUNCH.getCode());
            punchProvider.viewDateFlags(userId, cmd.getEnterpirseId(),
                    dateSF.get().format(logDay.getTime()));

        } else {
            pdl = new PunchLogsDay();
            List<PunchLog> punchLogs = punchProvider.listPunchLogsByDate(userId,
                    cmd.getEnterpirseId(), dateSF.get().format(logDay.getTime()),
                    ClockCode.SUCESS.getCode());
            pdl.setPunchLogs(new ArrayList<>());
            for (PunchLog log : punchLogs) {
                PunchLogDTO dto = ConvertHelper.convert(log, PunchLogDTO.class);
                dto.setPunchTime(log.getPunchTime().getTime());
                pdl.getPunchLogs().add(dto);
            }

        }
        return pdl;
    }

    @Override
    public PunchTimeRule getPunchTimeRuleByRuleIdAndDate(PunchRule pr, Date date, Long userId) {
        if (pr == null)
            return null;
        PunchTimeRule ptr = getPunchTimeRuleWithPunchDayTypeByRuleIdAndDate(pr, date, userId);
        if (ptr == null || ptr.getId() == null || ptr.getId() == 0) {
            return null;
        }
        return ptr;
    }

    public PunchTimeRule getPunchTimeRuleWithPunchDayTypeByRuleIdAndDate(PunchRule pr, Date date, Long userId) {
        //分为排班制和固定班
        if (pr.getRuleType().equals(PunchRuleType.GUDING.getCode())) {
            //固定班次
            return punchBaseService.getPunchTimeRuleGuDing(pr, date);
        } else {
            //查询当天那个人的班次
            OrganizationMember member = organizationProvider.findOrganizationMemberByUIdAndOrgId(userId, pr.getOwnerId());
            return getPunchTimeRuleScheduling(pr, date, member.getDetailId());
        }
    }

    private PunchTimeRule getPunchTimeRuleWithPunchDayTypeByRuleIdAndDateUseDetailId(PunchRule pr, Date date, Long detailId) {
        //分为排班制和固定班
        if (pr.getRuleType().equals(PunchRuleType.GUDING.getCode())) {
            //固定班次
            //看是否为特殊日期
            return punchBaseService.getPunchTimeRuleGuDing(pr, date);
        } else {
            //查询当天那个人的班次
            return getPunchTimeRuleScheduling(pr, date, detailId);
        }
    }

    private PunchTimeRule getPunchTimeRuleScheduling(PunchRule pr, Date date, Long detailId) {
        PunchScheduling punchScheduling = this.punchSchedulingProvider.getPunchSchedulingByRuleDateAndTarget(pr.getOwnerId(), detailId, date, pr.getId());
        if (null == punchScheduling || punchScheduling.getPunchRuleId() == null || punchScheduling.getTimeRuleId() == 0) {
            PunchTimeRule rule = new PunchTimeRule(0L, PunchDayType.RESTDAY);
            PunchHoliday punchHoliday = punchBaseService.checkHoliday(pr, new java.sql.Date(date.getTime()));
            if (punchHoliday != null && punchHoliday.getStatus().equals(NormalFlag.YES.getCode())) {
                rule = new PunchTimeRule(0L, NormalFlag.YES == NormalFlag.fromCode(punchHoliday.getLegalFlag()) ? PunchDayType.HOLIDAY : PunchDayType.RESTDAY);
            }
            // punchScheduling==null意味着当天还没有排班
            if (punchScheduling == null) {
                rule.setUnscheduledFlag(NormalFlag.YES.getCode());
            }
            return rule;
        }
        return punchProvider.getPunchTimeRuleById(punchScheduling.getTimeRuleId());
    }

    private void addPunchStatistics(OrganizationMemberDetails detail, Long orgId, String punchMonth) {
        try {
            String startDay = PunchDateUtils.getTheFirstMonthDate(punchMonth, null);
            String endDay = PunchDateUtils.getTheLastMonthDate(punchMonth, null);

            PunchStatistic statistic = new PunchStatistic();
            statistic.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            statistic.setPunchMonth(punchMonth);
            statistic.setOwnerType(PunchOwnerType.ORGANIZATION.getCode());
            Long ownerId = getTopEnterpriseId(detail.getOrganizationId());
            statistic.setOwnerId(ownerId);
            statistic.setUserId(detail.getTargetId());
            statistic.setDetailId(detail.getId());
            statistic.setUserName(detail.getContactName());
            Map<Long, String> dptMap = archivesService.getEmployeeDepartment(detail.getId());
            if (null != dptMap) {
                statistic.setDeptId(dptMap.keySet().iterator().next());
                String depName = archivesService.convertToOrgNames(dptMap);
                statistic.setDeptName(depName);
            }

            //2018年5月17日 by wh 删除statistics提前到这里
            this.punchProvider.deletePunchStatisticByUser(statistic.getOwnerType(), Collections.singletonList(ownerId), statistic.getPunchMonth(), statistic.getDetailId());

            List<PunchDayLog> dayLogList = this.punchProvider.listPunchDayLogsIncludeEndDay(detail.getId(), ownerId, startDay, endDay);

            //找到每一个打卡日期的异常申请修改审批后状态
            //既然轮询了顺便就统计一下 设备异常次数
//            int deviceChangeCounts = 0;
            List<DayStatusDTO> dayStatusDTOList = new ArrayList<>();
            for (PunchDayLog dayLog : dayLogList) {
//                if (NormalFlag.YES == NormalFlag.fromCode(dayLog.getDeviceChangeFlag())) {
//                    deviceChangeCounts++;
//                }
                DayStatusDTO dayStatusDTO = new DayStatusDTO();
                dayStatusDTO.setDate(dayStatusSF.get().format(dayLog.getPunchDate()));
                dayStatusDTO.setStatus(processStatus(dayLog.getSplitDateTime(), dayLog.getStatusList(), dayLog.getApprovalStatusList()));
                dayStatusDTOList.add(dayStatusDTO);
            }
            statistic.setStatusList(StringHelper.toJsonString(dayStatusDTOList));
            statistic.setDeviceChangeCounts(punchProvider.countDeviceChanges(socialSecurityService.getTheFirstDate(statistic.getPunchMonth()),
            		socialSecurityService.getTheLastDate(statistic.getPunchMonth()),  detail.getTargetId(), ownerId));

            //算每一项需要计算的
            processPunchListCount(dayLogList, statistic);
            // 计算本月的应出勤天数
            Calendar startDayOfThisMonth = Calendar.getInstance();
            startDayOfThisMonth.setTime(dateSF.get().parse(startDay));
            PunchRule pr = this.getPunchRule(detail);
            if(null != pr){
            	statistic.setPunchOrgName(pr.getName());
            	statistic.setWorkDayCount(calculateWorkDayCountThisMonth(detail, dayLogList, startDayOfThisMonth, pr));
            }
            this.punchProvider.createPunchStatistic(statistic);
        } catch (Exception e) {
            LOGGER.error("#####refresh month log error!! detailId:[" + detail.getId() + "] organization id :[" + orgId + "] ", e);
        }
    }

    private int calculateWorkDayCountThisMonth(OrganizationMemberDetails detail, List<PunchDayLog> punchDayLogs, Calendar startDayOfThisMonth, PunchRule punchRule) {
        int workDayCount = 0;
        Set<Long> punchDayLongSet = new HashSet<>();
        for (PunchDayLog pdl : punchDayLogs) {
            if (pdl.getTimeRuleId() != null && pdl.getTimeRuleId().longValue() != 0L) {
                punchDayLongSet.add(pdl.getPunchDate().getTime());
                workDayCount++;
            }
        }

        if (punchRule == null) {
            // 当前没有设置考勤规则，就不需要计算未来的排班情况
            return workDayCount;
        }

        int currentMonth = startDayOfThisMonth.get(Calendar.MONTH);
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        for (Calendar dayOfMonth = startDayOfThisMonth; currentMonth == dayOfMonth.get(Calendar.MONTH); dayOfMonth.add(Calendar.DAY_OF_MONTH, 1)) {
            if (punchDayLongSet.contains(dayOfMonth.getTimeInMillis()) || dayOfMonth.getTimeInMillis() < today.getTimeInMillis()) {
                continue;
            }
            if ((detail.getCheckInTime() != null && detail.getCheckInTime().compareTo(dayOfMonth.getTime()) < 0) &&
                    (detail.getDismissTime() == null || detail.getDismissTime().compareTo(dayOfMonth.getTime()) > 0)) {
                // 当前时间是入职状态才去计算是否有排班
                PunchTimeRule ptr = getPunchTimeRuleWithPunchDayTypeByRuleIdAndDateUseDetailId(punchRule, dayOfMonth.getTime(), detail.getId());
                if (ptr != null && ptr.getId() != null && ptr.getId() != 0) {
                    workDayCount++;
                }
            }
        }
        return workDayCount;
    }

    private void processPunchListCount(List<PunchDayLog> list, PunchStatistic statistic) {
        statistic.setWorkCount(0.0);
        statistic.setUnpunchCount(0.0);
        statistic.setSickCount(0.0);
        statistic.setOutworkCount(0.0);
        statistic.setLeaveEarlyCount(0);
        statistic.setExchangeCount(0.0);
        statistic.setBlandleCount(0);
        statistic.setBelateCount(0);
        statistic.setAbsenceCount(0.0);
        statistic.setOverTimeSum(0L);
        statistic.setExceptionDayCount(0);
        statistic.setExceptionRequestCounts(0);
        statistic.setBelateTime(0L);
        statistic.setLeaveEarlyTime(0L);
        statistic.setForgotPunchCountOnDuty(0);
        statistic.setForgotPunchCountOffDuty(0);
        statistic.setOvertimeTotalLegalHoliday(0L);
        statistic.setOvertimeTotalRestday(0L);
        statistic.setOvertimeTotalWorkday(0L);
        statistic.setAskForLeaveRequestCount(0);
        statistic.setGoOutRequestCount(0);
        statistic.setBusinessTripRequestCount(0);
        statistic.setOvertimeRequestCount(0);
        statistic.setPunchExceptionRequestCount(0);
        statistic.setRestDayCount(0);
        statistic.setGoOutPunchDayCount(0);
        statistic.setFullNormalFlag(NormalFlag.YES.getCode());
        statistic.setExceptionStatus(ExceptionStatus.NORMAL.getCode());

        Map<Long, Integer> abonormalExceptionRequestCountMap = new HashMap<>();
        if (statistic.getUserId() != null && statistic.getUserId() > 0) {
            statistic.setExceptionRequestCounts(punchProvider.countExceptionRequests(statistic.getUserId(), statistic.getOwnerId(), statistic.getPunchMonth(),
                    Arrays.asList(com.everhomes.rest.approval.ApprovalStatus.WAITING_FOR_APPROVING.getCode(), com.everhomes.rest.approval.ApprovalStatus.AGREEMENT.getCode())));
            statistic.setPunchExceptionRequestCount(statistic.getExceptionRequestCounts());

            List<PunchExceptionRequestStatisticsItemDTO> punchExceptionRequestStatisticsItemDTOS = punchProvider.countPunchExceptionRequestGroupByMonth(statistic.getUserId(), statistic.getOwnerId(), statistic.getPunchMonth());
            punchExceptionRequestCountByPunchMonthStatistics(statistic, punchExceptionRequestStatisticsItemDTOS);

            abonormalExceptionRequestCountMap = punchProvider.countAbonormalExceptionRequestGroupByPunchDate(statistic.getOwnerId(), statistic.getUserId(), statistic.getPunchMonth(), Arrays.asList(com.everhomes.rest.approval.ApprovalStatus.AGREEMENT.getCode()));
        }



        for (PunchDayLog pdl : list) {
            statistic.setBelateTime(statistic.getBelateTime() + pdl.getBelateTimeTotal());
            statistic.setLeaveEarlyTime(statistic.getLeaveEarlyTime() + pdl.getLeaveEarlyTimeTotal());
            statistic.setOvertimeTotalLegalHoliday(statistic.getOvertimeTotalLegalHoliday() + pdl.getOvertimeTotalLegalHoliday());
            statistic.setOvertimeTotalRestday(statistic.getOvertimeTotalRestday() + pdl.getOvertimeTotalRestday());
            statistic.setOvertimeTotalWorkday(statistic.getOvertimeTotalWorkday() + pdl.getOvertimeTotalWorkday());
            if (NormalFlag.NO == NormalFlag.fromCode(pdl.getNormalFlag())) {
                statistic.setFullNormalFlag(NormalFlag.NO.getCode());
            }

            statistic.setRestDayCount(statistic.getRestDayCount() + (NormalFlag.YES == NormalFlag.fromCode(pdl.getRestFlag()) ? 1 : 0));
            statistic.setGoOutPunchDayCount(statistic.getGoOutPunchDayCount() + (NormalFlag.YES == NormalFlag.fromCode(pdl.getGoOutPunchFlag()) ? 1 : 0));
            List<TimeInterval> tiDTOs = null;
            PunchTimeRule ptr = null;

            List<PunchExceptionRequest> exceptionRequests = new ArrayList<>();
            if (pdl.getTimeRuleId() != null && pdl.getTimeRuleId().longValue() != 0L) {
                ptr = punchProvider.getPunchTimeRuleById(pdl.getTimeRuleId());
                if (ptr != null && statistic.getUserId() != null && statistic.getUserId() > 0) {
                    Timestamp dayStart = new Timestamp(pdl.getPunchDate().getTime() + (ptr.getDaySplitTimeLong() == null ? 104400000L : ptr.getDaySplitTimeLong()) - ONE_DAY_MS + 1);
                    Timestamp dayEnd = new Timestamp(pdl.getPunchDate().getTime() + (ptr.getDaySplitTimeLong() == null ? 104400000L : ptr.getDaySplitTimeLong()));
                    exceptionRequests = punchProvider.listPunchExceptionRequestBetweenBeginAndEndTime(pdl.getUserId(), pdl.getEnterpriseId(), dayStart, dayEnd);

                    tiDTOs = processTimeRuleDTO(exceptionRequests, tiDTOs);
                }
            }
            PunchTimeRuleDTO ptrDTO = convertPunchTimeRule2DTO(ptr);

            if (pdl.getStatusList() != null) {
                //2018年4月23日:这里的定义改了,不是是否为正常,而是统计出勤天数:
                //出勤天数的计算是:打卡(包括正常,迟到,早退) + 出差/外出申请审核通过视为出勤（不计算请假，不重复统计）
                Byte isWorkDay = NormalFlag.NO.getCode();
                Byte isAbsence = NormalFlag.NO.getCode();
                if (pdl.getStatusList().contains(PunchConstants.STATUS_SEPARATOR)) {
                    String[] status = pdl.getStatusList().split(PunchConstants.STATUS_SEPARATOR);
                    isWorkDay = countWorkDay(status, exceptionRequests,abonormalExceptionRequestCountMap.get(pdl.getPunchDate().getTime()));
                    if (pdl.getApprovalStatusList() != null && pdl.getApprovalStatusList().contains(PunchConstants.STATUS_SEPARATOR)) {
                        String[] asList = StringUtils.splitPreserveAllTokens(pdl.getApprovalStatusList(), PunchConstants.STATUS_SEPARATOR);
                        for (int i = 0; i < asList.length && i < status.length; i++) {
                            if (StringUtils.isNotBlank(asList[i])) {
                                status[i] = asList[i];
                            }
                        }
                    }
                    isAbsence = isAbsence(status);
                    statistic.setBelateCount(statistic.getBelateCount() + belateCount(status));
                    statistic.setLeaveEarlyCount(statistic.getLeaveEarlyCount() + leaveEarlyCount(status));
                    statistic.setForgotPunchCountOnDuty(statistic.getForgotPunchCountOnDuty() + forgotCountOnDuty(status));
                    statistic.setForgotPunchCountOffDuty(statistic.getForgotPunchCountOffDuty() + forgotCountOffDuty(status));

                } else {
                    if (pdl.getStatusList().equals(String.valueOf(PunchStatus.NOTWORKDAY.getCode()))) {
                        //不上班跳过
                        continue;
                    } else {
                        isWorkDay = countWorkDay(pdl.getStatusList(), exceptionRequests, abonormalExceptionRequestCountMap.get(pdl.getPunchDate().getTime()));
                        String status = pdl.getStatusList();
                        if (StringUtils.isNotBlank(pdl.getApprovalStatusList())) {
                            status = pdl.getApprovalStatusList();
                        }
                        isAbsence = isAbsence(new String[]{status});
                        statistic.setBelateCount(statistic.getBelateCount() + belateCount(new String[]{status}));
                        statistic.setLeaveEarlyCount(statistic.getLeaveEarlyCount() + leaveEarlyCount(new String[]{status}));
                        statistic.setForgotPunchCountOnDuty(statistic.getForgotPunchCountOnDuty() + forgotCountOnDuty(new String[]{status}));
                        statistic.setForgotPunchCountOffDuty(statistic.getForgotPunchCountOffDuty() + forgotCountOffDuty(new String[]{status}));
                        if (status.equals(String.valueOf(PunchStatus.UNPUNCH.getCode()))
                                && ptrDTO != null && ptrDTO.getAfternoonArriveTime() != null && ptrDTO.getNoonLeaveTime() != null) {
                            PunchTimeIntervalDTO dto1 = new PunchTimeIntervalDTO();
                            dto1.setArriveTime(ptrDTO.getPunchTimeIntervals().get(0).getArriveTime());
                            dto1.setLeaveTime(ptrDTO.getNoonLeaveTime());
                            PunchTimeIntervalDTO dto2 = new PunchTimeIntervalDTO();
                            dto2.setArriveTime(ptrDTO.getAfternoonArriveTime());
                            dto2.setLeaveTime(ptrDTO.getPunchTimeIntervals().get(0).getLeaveTime());
                            ptrDTO.setPunchTimeIntervals(new ArrayList<>());
                            ptrDTO.getPunchTimeIntervals().add(dto1);
                            ptrDTO.getPunchTimeIntervals().add(dto2);
                            countOneDayStatistic(status, statistic, 1, tiDTOs, pdl.getPunchDate(), ptrDTO);
                            countOneDayStatistic(status, statistic, 2, tiDTOs, pdl.getPunchDate(), ptrDTO);
                        } else {
                            countOneDayStatistic(status, statistic, 1, tiDTOs, pdl.getPunchDate(), ptrDTO);
                        }
                    }
                }

                if (NormalFlag.fromCode(isWorkDay) == NormalFlag.YES) {
                    statistic.setWorkCount(statistic.getWorkCount() + 1);
                } else {
                    statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
                    statistic.setExceptionDayCount(statistic.getExceptionDayCount() + 1);
                }
                // 还没过分界点的UN_PUNCH状态不计入旷工
                if (NormalFlag.fromCode(isAbsence) == NormalFlag.YES
                        && (pdl.getSplitDateTime() == null || new Timestamp(System.currentTimeMillis()).compareTo(pdl.getSplitDateTime()) >= 0)) {
                    statistic.setAbsenceCount(statistic.getAbsenceCount() + 1);
                }
            }
        }

        if(NormalFlag.YES == NormalFlag.fromCode(statistic.getFullNormalFlag())){
        	statistic.setExceptionStatus(ExceptionStatus.NORMAL.getCode());
        }else{
        	statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
        }
    }

    private Byte countWorkDay(String[] status, List<PunchExceptionRequest> exceptionRequests, Integer abonormalExceptionRequestCount) {
        //下面要看外出/出差等申请结果
        if (null != exceptionRequests) {
            List workApprovalAttribute = new ArrayList<>();
            workApprovalAttribute.add(GeneralApprovalAttribute.BUSINESS_TRIP.getCode());
            workApprovalAttribute.add(GeneralApprovalAttribute.GO_OUT.getCode());
            workApprovalAttribute.add(GeneralApprovalAttribute.ABNORMAL_PUNCH.getCode());
            for (PunchExceptionRequest request : exceptionRequests) {
                if (workApprovalAttribute.contains(request.getApprovalAttribute())
                        && com.everhomes.rest.approval.ApprovalStatus.AGREEMENT == com.everhomes.rest.approval.ApprovalStatus.fromCode(request.getStatus())) {
                    return NormalFlag.YES.getCode();
                }
            }
        }
        for (String s : status) {
            if (NormalFlag.fromCode(countWorkDay(s, exceptionRequests, abonormalExceptionRequestCount)) == NormalFlag.YES) {
                return NormalFlag.YES.getCode();
            }
        }
        return NormalFlag.NO.getCode();
    }

    // 全天考勤正常，即全勤
    private Byte isFullNormal(String[] status) {
        if (status == null || status.length == 0) {
            return NormalFlag.YES.getCode();
        }
        for (String s : status) {
            if (!String.valueOf(PunchStatus.NORMAL.getCode()).equals(s) && 
            		!String.valueOf(PunchStatus.NOTWORKDAY.getCode()).equals(s) && 
            		!String.valueOf(PunchStatus.NO_ASSIGN_PUNCH_RULE.getCode()).equals(s) && 
            		!String.valueOf(PunchStatus.NONENTRY.getCode()).equals(s) && 
            		!String.valueOf(PunchStatus.RESIGNED.getCode()).equals(s) && 
            		!String.valueOf(PunchStatus.NO_ASSIGN_PUNCH_SCHEDULED.getCode()).equals(s)) {
                return NormalFlag.NO.getCode();
            }
        }
        return NormalFlag.YES.getCode();
    }

    // 全天都没有打卡记录，则记旷工一天（旷工半天不记录）
    private Byte isAbsence(String[] status) {
        if (status == null || status.length == 0) {
            return NormalFlag.NO.getCode();
        }
        for (String s : status) {
            if (!String.valueOf(PunchStatus.UNPUNCH.getCode()).equals(s)) {
                return NormalFlag.NO.getCode();
            }
        }
        return NormalFlag.YES.getCode();
    }

    private int forgotCountOnDuty(String[] status) {
        if (status == null || status.length == 0) {
            return 0;
        }
        int c = 0;
        for (String s : status) {
            if (Arrays.asList(String.valueOf(PunchStatus.FORGOT_ON_DUTY.getCode())).contains(s)) {
                c++;
            }
        }
        return c;
    }

    private int forgotCountOffDuty(String[] status) {
        if (status == null || status.length == 0) {
            return 0;
        }
        int c = 0;
        for (String s : status) {
            if (Arrays.asList(String.valueOf(PunchStatus.FORGOT_OFF_DUTY.getCode()), String.valueOf(PunchStatus.BELATE_AND_FORGOT.getCode())).contains(s)) {
                c++;
            }
        }
        return c;
    }

    private int belateCount(String[] status) {
        if (status == null || status.length == 0) {
            return 0;
        }
        int c = 0;
        for (String s : status) {
            if (Arrays.asList(String.valueOf(PunchStatus.BELATE.getCode()), String.valueOf(PunchStatus.BELATE_AND_FORGOT.getCode()), String.valueOf(PunchStatus.BLANDLE.getCode())).contains(s)) {
                c++;
            }
        }
        return c;
    }

    private int leaveEarlyCount(String[] status) {
        if (status == null || status.length == 0) {
            return 0;
        }
        int c = 0;
        for (String s : status) {
            if (Arrays.asList(String.valueOf(PunchStatus.LEAVEEARLY.getCode()), String.valueOf(PunchStatus.BLANDLE.getCode())).contains(s)) {
                c++;
            }
        }
        return c;
    }

    private Long processLeaveEarlyTime(PunchLog log) {
        Calendar punchTime = Calendar.getInstance();
        punchTime.setTime(log.getPunchTime());
//        new  .divide(new BigDecimal(8*3600*1000),2, RoundingMode.HALF_UP);
        return log.getShouldPunchTime()==null?log.getRuleTime():log.getShouldPunchTime() - getTimeLong(punchTime, log.getPunchDate());
    }

    private Long processBelateTime(PunchLog log) {
        Calendar punchTime = Calendar.getInstance();
        punchTime.setTime(log.getPunchTime());
//        new  .divide(new BigDecimal(8*3600*1000),2, RoundingMode.HALF_UP);
        return getTimeLong(punchTime, log.getPunchDate()) - (log.getShouldPunchTime()==null?log.getRuleTime():log.getShouldPunchTime());
    }

    private Byte countWorkDay(String status, List<PunchExceptionRequest> exceptionRequests, Integer abonormalExceptionRequestCount) {
        Byte isNormal = NormalFlag.NO.getCode();
        if (status.equals(String.valueOf(PunchStatus.LEAVEEARLY.getCode()))) {
            isNormal = NormalFlag.YES.getCode();
        } else if (status.equals(String.valueOf(PunchStatus.BLANDLE.getCode()))) {
            isNormal = NormalFlag.YES.getCode();
        } else if (status.equals(String.valueOf(PunchStatus.BELATE.getCode()))) {
            isNormal = NormalFlag.YES.getCode();
        } else if (status.equals(String.valueOf(PunchStatus.FORGOT_ON_DUTY.getCode()))) {
            isNormal = NormalFlag.YES.getCode();
        } else if (status.equals(String.valueOf(PunchStatus.FORGOT_OFF_DUTY.getCode()))) {
            isNormal = NormalFlag.YES.getCode();
        } else if (status.equals(String.valueOf(PunchStatus.NORMAL.getCode()))) {
            isNormal = NormalFlag.YES.getCode();
        } else if (status.equals(String.valueOf(PunchStatus.BELATE_AND_FORGOT.getCode()))) {
            isNormal = NormalFlag.YES.getCode();
        }
        //下面要看外出/出差等申请结果
        if (null != exceptionRequests) {
            List workApprovalAttribute = new ArrayList<>();
            workApprovalAttribute.add(GeneralApprovalAttribute.BUSINESS_TRIP.getCode());
            workApprovalAttribute.add(GeneralApprovalAttribute.GO_OUT.getCode());
            workApprovalAttribute.add(GeneralApprovalAttribute.ABNORMAL_PUNCH.getCode());
            for (PunchExceptionRequest request : exceptionRequests) {
                if (workApprovalAttribute.contains(request.getApprovalAttribute())
                        && com.everhomes.rest.approval.ApprovalStatus.AGREEMENT == com.everhomes.rest.approval.ApprovalStatus.fromCode(request.getStatus())) {
                    isNormal = NormalFlag.YES.getCode();
                }
            }
        }
        if (abonormalExceptionRequestCount != null && abonormalExceptionRequestCount > 0) {
            isNormal = NormalFlag.YES.getCode();
        }
        return isNormal;
    }

    private void countOneDayStatistic(String status, PunchStatistic statistic,
                                      int punchTimeNo, List<TimeInterval> tiDTOs, java.sql.Date punchDate, PunchTimeRuleDTO ptrDTO) {
        if (status.equals(String.valueOf(PunchStatus.UNPUNCH.getCode()))) {
            statistic.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
            //缺勤计算小时
            if (null != ptrDTO) {
                PunchTimeIntervalDTO interval = ptrDTO.getPunchTimeIntervals().get(punchTimeNo - 1);
                Long leaveTime = interval.getLeaveTime();
                Long arrvieTime = interval.getArriveTime();
                long unpunchTimeLong = leaveTime - arrvieTime;
                if (null != tiDTOs) {
                    for (TimeInterval ti : tiDTOs) {
                        Long tiBeginTime = ti.getBeginTime().getTime() - punchDate.getTime();
                        Long tiEndTime = ti.getEndTime().getTime() - punchDate.getTime();
                        if (tiBeginTime <= arrvieTime) {
                            if (tiEndTime >= leaveTime) {
                                //如果请假开始时间比打卡开始时间小,结束时间比打卡结束时间晚,缺勤时间0
                                unpunchTimeLong = 0L;
                            } else {
                                //如果请假开始时间比打卡开始时间小,结束时间比打卡结束时间早,缺勤时间-(请假结束-打卡开始)
                                unpunchTimeLong -= (tiEndTime - arrvieTime);
                            }
                        } else {
                            if (tiEndTime >= leaveTime) {
                                //如果请假开始时间比打卡开始时间大,结束时间比打卡结束时间晚,缺勤时间-(打卡结束-请假开始)
                                unpunchTimeLong -= (leaveTime - tiBeginTime);
                            } else {
                                //如果请假开始时间比打卡开始时间大,结束时间比打卡结束时间早,缺勤时间-(请假结束-请假开始)
                                unpunchTimeLong -= (tiEndTime - tiBeginTime);
                            }
                        }
                    }
                }
                if (unpunchTimeLong < 0L) {
                    unpunchTimeLong = 0L;
                }
                BigDecimal b = new BigDecimal(unpunchTimeLong);
                statistic.setUnpunchCount(statistic.getUnpunchCount() + b.divide(new BigDecimal(3600000), 2, RoundingMode.HALF_UP).doubleValue());
            }
        }
    }


    @Override
    public ListMonthPunchLogsCommandResponse listMonthPunchLogs(
            ListMonthPunchLogsCommand cmd) {

        checkCompanyIdIsNull(cmd.getEnterpriseId());

        cmd.setEnterpriseId(getTopEnterpriseId(cmd.getEnterpriseId()));
        SimpleDateFormat yearSF = new SimpleDateFormat("yyyy");

        ListMonthPunchLogsCommandResponse response = new ListMonthPunchLogsCommandResponse();
        response.setPunchYear(yearSF.format(new java.sql.Date(cmd
                .getQueryTime())));
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        // start 设置为本月月初
        start.set(Calendar.DAY_OF_MONTH,
                start.getActualMinimum(Calendar.DAY_OF_MONTH));
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        start.set(Calendar.MINUTE, 0);
        if (start.getTime().after(new java.sql.Date(cmd.getQueryTime()))) {
            // 如果查询日在月初之前 则设置为查询月的月初和月末
            start.setTime(new java.sql.Date(cmd.getQueryTime()));
            end.setTime(new java.sql.Date(cmd.getQueryTime()));
            start.set(Calendar.DAY_OF_MONTH,
                    start.getActualMinimum(Calendar.DAY_OF_MONTH));
            end.add(Calendar.MONTH, 1);
            end.set(Calendar.DAY_OF_MONTH,
                    end.getActualMinimum(Calendar.DAY_OF_MONTH));
        }

        ListYearPunchLogsCommandResponse pyl = new ListYearPunchLogsCommandResponse();
        pyl.setPunchLogsMonthList(new ArrayList<PunchLogsMonthList>());
        pyl = getlistPunchLogsBetweenTwoCalendar(pyl, cmd.getEnterpriseId(),
                start, end);
        response.setPunchLogsMonthList(pyl.getPunchLogsMonthList());
        return response;
    }

    private void createPunchStatisticsBookSheetHead(Sheet sheet, ListPunchCountCommandResponse resp) {
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        int i = -1;

        row.createCell(++i).setCellValue("姓名");
        row.createCell(++i).setCellValue("部门");
        row.createCell(++i).setCellValue("应出勤天数");
        row.createCell(++i).setCellValue("实出勤天数");
        row.createCell(++i).setCellValue("旷工天数");
        row.createCell(++i).setCellValue("迟到次数");
        row.createCell(++i).setCellValue("迟到时长");
        row.createCell(++i).setCellValue("早退次数");
        row.createCell(++i).setCellValue("早退时长");
        row.createCell(++i).setCellValue("上班缺卡次数");
        row.createCell(++i).setCellValue("下班缺卡次数");
        row.createCell(++i).setCellValue("设备异常次数");
        row.createCell(++i).setCellValue("异常申请次数");
        row.createCell(++i).setCellValue("工作日加班");
        row.createCell(++i).setCellValue("休息日加班");
        row.createCell(++i).setCellValue("节假日加班");
        row.createCell(++i).setCellValue("请假总时长");
        if (null != resp) {
            if (null != resp.getExtColumns()) {
                for (String ext : resp.getExtColumns()) {
                    row.createCell(++i).setCellValue(ext);
                }
            }
        }
        if (null != resp) {
            if (null != resp.getDateList()) {
                for (String ext : resp.getDateList()) {
                    row.createCell(++i).setCellValue(ext);
                }
            }

        }

    }

    private String objToString(Object o) {
        if (null == o) {
            return "";
        }
        return o.toString();
    }

    public void setNewPunchStatisticsBookRow(Sheet sheet, PunchCountDTO statistic, List<String> dateList, XSSFWorkbook wb) {
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        int i = -1;
        row.createCell(++i).setCellValue(statistic.getUserName());
        row.createCell(++i).setCellValue(statistic.getDeptName());
        row.createCell(++i).setCellValue(objToString(statistic.getWorkDayCount()));
        row.createCell(++i).setCellValue(objToString(statistic.getWorkCount()));
        row.createCell(++i).setCellValue(objToString(statistic.getAbsenceCount()));
        row.createCell(++i).setCellValue(objToString(statistic.getBelateCount()));
        row.createCell(++i).setCellValue(objToString(statistic.getBelateTime()));
        row.createCell(++i).setCellValue(objToString(statistic.getLeaveEarlyCount()));
        row.createCell(++i).setCellValue(objToString(statistic.getLeaveEarlyTime()));
        row.createCell(++i).setCellValue(objToString(statistic.getForgotPunchCountOnDuty()));
        row.createCell(++i).setCellValue(objToString(statistic.getForgotPunchCountOffDuty()));
        row.createCell(++i).setCellValue(objToString(statistic.getDeviceChangeCounts()));
        row.createCell(++i).setCellValue(objToString(statistic.getExceptionRequestCounts()));
        row.createCell(++i).setCellValue(statistic.getOvertimeTotalWorkdayDisplay());
        row.createCell(++i).setCellValue(statistic.getOvertimeTotalRestdayDisplay());
        row.createCell(++i).setCellValue(statistic.getOvertimeTotalLegalHolidayDisplay());
        Cell cellForAskForLeaveTotal = row.createCell(++i);
        if (null != statistic.getExts()) {
            for (ExtDTO ext : statistic.getExts()) {
                row.createCell(++i).setCellValue(ext.getTimeCountDisplay());
            }
        }
        cellForAskForLeaveTotal.setCellValue(statistic.getExtTotalDisplay());
        Map<String, String> statusMap = new HashMap<>();
        if (statistic.getStatusList() != null) {
            for (DayStatusDTO dto : statistic.getStatusList()) {
                statusMap.put(dto.getDate(), dto.getStatus());
            }
        }
        if (null != dateList) {
            for (String date : dateList) {
                String value = statusMap.get(date);
                if (null == value) {
                    value = "";
                }
                Cell cell = row.createCell(++i);
                cell.setCellValue(value);
                XSSFCellStyle cellStyle = wb.createCellStyle();
                if ("正常".equals(value) || "休息".equals(value) || "非工作日".equals(value)) {
                    //不处理
                } else if ("旷工".equals(value)) {
                    cellStyle.setFillBackgroundColor(IndexedColors.DARK_RED.getIndex());
                } else {
                    cellStyle.setFillBackgroundColor(IndexedColors.DARK_YELLOW.getIndex());
                }
                cell.setCellStyle(cellStyle);
            }
        }

    }

    public Workbook createPunchStatisticsBook(ListPunchCountCommandResponse resp, ListPunchCountCommand cmd, Long taskId) {

        Map<Long, PunchCountDTO> userDeptMap = new HashMap<>();
        XSSFWorkbook wb = new XSSFWorkbook();
        createPunchStatisticsSheet(resp, cmd, taskId, wb, userDeptMap);
        List<Long> userIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(resp.getPunchCountList())) {
            for (PunchCountDTO dto : resp.getPunchCountList()) {
                if (dto.getUserId() != null && dto.getUserId() > 0) {
                    userIds.add(dto.getUserId());
                }
            }
            // 手动clear数据引用，提前结束数据的生命周期，以便GC适时回收
            resp.getPunchCountList().clear();
        }

        ListPunchDetailsCommand listPunchDetailsCommand = new ListPunchDetailsCommand();
        listPunchDetailsCommand.setStartDay(cmd.getStartDay());
        listPunchDetailsCommand.setEndDay(cmd.getEndDay());
        listPunchDetailsCommand.setMonthReportId(cmd.getMonthReportId());
        listPunchDetailsCommand.setOwnerId(cmd.getOwnerId());
        listPunchDetailsCommand.setOwnerType(cmd.getOwnerType());
        listPunchDetailsCommand.setUserName(cmd.getUserName());
        // 进度条30% -- 60%
        createPunchDetailsBookSheet(resp.getUpdateTime(), listPunchDetailsCommand, taskId, wb);
        // 进度条60% -- 100%
        createPunchLogsSheet(resp.getUpdateTime(),userIds, cmd, taskId, wb, userDeptMap);
        return wb;
    }

    private void createPunchLogsSheet(Long dataUpdateTime, List<Long> userIds, ListPunchCountCommand cmd, Long taskId, XSSFWorkbook wb, Map<Long, PunchCountDTO> userDeptMap) {
        int columnNo = 9;

        XSSFSheet sheet = wb.createSheet("打卡记录");
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columnNo));
        XSSFCellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 20);
        font.setFontName("Courier New");

        style.setFont(font);

        XSSFCellStyle titleStyle = wb.createCellStyle();
        titleStyle.setFont(font);
        titleStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);

        //  创建标题
        XSSFRow rowTitle = sheet.createRow(0);
        rowTitle.createCell(0).setCellValue("打卡记录报表:" + monthChineseSF.get().format(cmd.getStartDay()));
        rowTitle.setRowStyle(titleStyle);
        //副标题

        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, columnNo));
        XSSFCellStyle style1 = wb.createCellStyle();
        Font font1 = wb.createFont();

        font1.setFontHeightInPoints((short) 20);
        font1.setFontName("Courier New");

        style1.setFont(font1);
        XSSFCellStyle titleStyle1 = wb.createCellStyle();

        XSSFRow rowReminder = sheet.createRow(1);
        rowReminder.createCell(0).setCellValue("数据更新时间:" + datetimeSF.get().format(new Date(dataUpdateTime)) + ", 报表生成时间: " + datetimeSF.get().format(DateHelper.currentGMTTime()));
        rowReminder.setRowStyle(titleStyle1);
        createPunchLogsBookSheetHead(sheet);
        sheet.createFreezePane(2, 3, 3, 4);

        fullPunchLogsBookRow(userIds, cmd, taskId, userDeptMap, sheet);
    }

    private void fullPunchLogsBookRow(List<Long> userIds, ListPunchCountCommand cmd, Long taskId, Map<Long, PunchCountDTO> userDeptMap, XSSFSheet sheet) {
        Map<Long, String> ruleMap = new HashMap<>();
        Long ownerId = getTopEnterpriseId(cmd.getOwnerId());
        String startDay = dateSF.get().format(new Date(cmd.getStartDay()));
        String endDay = dateSF.get().format(new Date(cmd.getEndDay()));
        Integer pageOffset = 0;
        int num = 0;
        List<PunchLog> results = punchProvider.listPunchLogs(userIds, ownerId, startDay, endDay, cmd.getExceptionStatus(), pageOffset, PunchConstants.PAGE_SIZE_AT_A_TIME + 1);
        boolean process = CollectionUtils.isNotEmpty(results);
        while (process) {
            Integer nextPageAnchor = null;
            if (results.size() == PunchConstants.PAGE_SIZE_AT_A_TIME + 1) {
                results.remove(PunchConstants.PAGE_SIZE_AT_A_TIME.intValue());
                nextPageAnchor = pageOffset + PunchConstants.PAGE_SIZE_AT_A_TIME;
            }
            for (PunchLog log : results) {
                setNewPunchLogsBookRow(sheet, log, userDeptMap, ruleMap);
                if (taskId != null && ++num % 100 == 0) {
                    taskService.updateTaskProcess(taskId, 60 + Math.min(40, num / 100));
                }
            }
            // 手动clear数据引用，提前结束数据的生命周期，以便GC适时回收
            results.clear();
            if (nextPageAnchor != null && nextPageAnchor > 0) {
                pageOffset = nextPageAnchor.intValue();
                results = punchProvider.listPunchLogs(userIds, ownerId, startDay, endDay, cmd.getExceptionStatus(), pageOffset, PunchConstants.PAGE_SIZE_AT_A_TIME + 1);
                process = CollectionUtils.isNotEmpty(results);
            } else {
                process = false;
            }
        }
    }

    private void setNewPunchLogsBookRow(XSSFSheet sheet, PunchLog log, Map<Long, PunchCountDTO> userDeptMap, Map<Long, String> ruleMap) {

        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        int i = -1;

        row.createCell(++i).setCellValue(dateSF.get().format(log.getPunchDate()));
        PunchCountDTO dto = userDeptMap.get(log.getUserId());
        if (null != dto) {
            row.createCell(++i).setCellValue(dto.getUserName());
            row.createCell(++i).setCellValue(dto.getDeptName());
        } else {
            row.createCell(++i).setCellValue(log.getUserId());
            row.createCell(++i).setCellValue("");
        }
        row.createCell(++i).setCellValue(findPunchOrganizationName(ruleMap, log.getPunchOrganizationId()));
        row.createCell(++i).setCellValue(PunchType.fromCode(log.getPunchType()).toString());
        if (null != log.getPunchTime()) {
            row.createCell(++i).setCellValue(timeSF.get().format(log.getPunchTime()));
        } else {
            row.createCell(++i).setCellValue("");
        }
        if (null != log.getLocationInfo()) {
            row.createCell(++i).setCellValue("位置 :" + log.getLocationInfo());
        } else {
            row.createCell(++i).setCellValue("WiFi :" + log.getWifiInfo());
        }
        if (PunchStatus.LEAVEEARLY == PunchStatus.fromCode(log.getStatus())) {
            row.createCell(++i).setCellValue(statusToString(null, log.getStatus()) + getLeaveEarlyString(log));
        } else if (PunchStatus.BELATE == PunchStatus.fromCode(log.getStatus())) {
            row.createCell(++i).setCellValue(statusToString(null, log.getStatus()) + getBelateString(log));
        } else {
            row.createCell(++i).setCellValue(statusToString(null, log.getStatus()));
        }
        row.createCell(++i).setCellValue(log.getIdentification());
        row.createCell(++i).setCellValue(NormalFlag.YES == NormalFlag.fromCode(log.getDeviceChangeFlag()) ? "异常" : "正常");
    }

    private String findPunchOrganizationName(Map<Long, String> ruleMap, Long punchOrganizationId) {
        if (null == punchOrganizationId) {
            return "";
        }
        String name = ruleMap.get(punchOrganizationId);
        if (null == name) {
            Organization org = organizationProvider.findOrganizationById(punchOrganizationId);
            if (null != org) {
                name = org.getName();
            } else {
                name = "";
            }
            ruleMap.put(punchOrganizationId, name);
        }
        return name;
    }

    private String getLeaveEarlyString(PunchLog log) {
        Long time = processLeaveEarlyTime(log);
        return processTimeLongToString(time);
    }

    private String getBelateString(PunchLog log) {
        Long time = processBelateTime(log);
        return processTimeLongToString(time);
    }

    private String processTimeLongToString(Long time) {
        StringBuilder sb = new StringBuilder();

        time = time / 1000;
        int hour = Integer.valueOf((time / 3600) + "");
        if (hour > 0) {
            sb.append(hour);
            sb.append("小时");
        }
        time = time % 3600;
        int min = Integer.valueOf((time / 60) + "");
        if (min > 0) {
            sb.append(min);
            sb.append("分");
        }
        if (sb.length() < 1) {
            time = time % 60;
            sb.append(time);
            sb.append("秒");
        }
        return sb.toString();
    }

    private void createPunchLogsBookSheetHead(XSSFSheet sheet) {

        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        int i = -1;
        row.createCell(++i).setCellValue("日期");
        row.createCell(++i).setCellValue("姓名");
        row.createCell(++i).setCellValue("部门");
        row.createCell(++i).setCellValue("所属规则");
        row.createCell(++i).setCellValue("打卡类型");
        row.createCell(++i).setCellValue("打卡时间");
        row.createCell(++i).setCellValue("打卡地点");
        row.createCell(++i).setCellValue("状态");
        row.createCell(++i).setCellValue("打卡设备号");
        row.createCell(++i).setCellValue("设备异常");
    }

    private void createPunchStatisticsSheet(ListPunchCountCommandResponse resp, ListPunchCountCommand cmd, Long taskId,
                                            XSSFWorkbook wb, Map<Long, PunchCountDTO> userDeptMap) {
        int columnNo = 16;
        List<PunchCountDTO> results = resp.getPunchCountList();
        if (null != resp) {
            if (null != resp.getExtColumns()) {
                columnNo += resp.getExtColumns().size();
            }
            if (null != resp.getDateList()) {
                columnNo += resp.getDateList().size();
            }
        }
        XSSFSheet sheet = wb.createSheet("月度统计");
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columnNo));
        XSSFCellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 20);
        font.setFontName("Courier New");

        style.setFont(font);

        XSSFCellStyle titleStyle = wb.createCellStyle();
        titleStyle.setFont(font);
        titleStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);

        //  创建标题
        XSSFRow rowTitle = sheet.createRow(0);
        rowTitle.createCell(0).setCellValue("月度统计报表:" + monthChineseSF.get().format(cmd.getStartDay()));
        rowTitle.setRowStyle(titleStyle);
        //副标题

        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, columnNo));
        XSSFCellStyle style1 = wb.createCellStyle();
        Font font1 = wb.createFont();

        font1.setFontHeightInPoints((short) 20);
        font1.setFontName("Courier New");

        style1.setFont(font1);
        XSSFCellStyle titleStyle1 = wb.createCellStyle();

        XSSFRow rowReminder = sheet.createRow(1);
        rowReminder.createCell(0).setCellValue("数据更新时间:" + datetimeSF.get().format(new Date(resp.getUpdateTime()))
                + ", 报表生成时间: " + datetimeSF.get().format(DateHelper.currentGMTTime()));
        rowReminder.setRowStyle(titleStyle1);
        this.createPunchStatisticsBookSheetHead(sheet, resp);
        sheet.createFreezePane(1, 3, 2, 4);

        if (null == results || results.size() == 0)
            return;
        Integer num = 0;
        for (PunchCountDTO statistic : results) {
            if (statistic.getUserId() != null && statistic.getUserId() > 0) {
                userDeptMap.put(statistic.getUserId(), statistic);
            }
            this.setNewPunchStatisticsBookRow(sheet, statistic, resp.getDateList(), wb);
            if (taskId != null && num++ % 100 == 0) {
                taskService.updateTaskProcess(taskId, 20 + (int) ((num / (results.size()) * 10.00)));
            }
        }
    }

    public HttpServletResponse download(Workbook workbook, String fileName, HttpServletResponse response) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes()));
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/msexcel");
            toClient.write(out.toByteArray());
            toClient.flush();
            toClient.close();

//            // 读取完成删除文件
//            if (file.isFile() && file.exists()) {
//                file.delete();
//            }
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
            throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
                    PunchServiceErrorCode.ERROR_PUNCH_ADD_DAYLOG,
                    ex.getLocalizedMessage());

        }
        return response;
    }


    @Override
    public listPunchTimeRuleListResponse listPunchTimeRuleList(ListPunchRulesCommonCommand cmd) {
        cmd.setOwnerId(getTopEnterpriseId(cmd.getOwnerId()));
        listPunchTimeRuleListResponse response = new listPunchTimeRuleListResponse();
        if (cmd.getPageAnchor() == null)
            cmd.setPageAnchor(0L);
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

        List<PunchTimeRule> results = this.punchProvider.queryPunchTimeRuleList(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getTargetType(), cmd.getTargetId(), locator, pageSize + 1);
        if (null == results)
            return response;
        Long nextPageAnchor = null;
        if (results != null && results.size() > pageSize) {
            results.remove(results.size() - 1);
            nextPageAnchor = results.get(results.size() - 1).getId();
        }
        response.setNextPageAnchor(nextPageAnchor);
        response.setTimeRules(new ArrayList<PunchTimeRuleDTO>());
        results.forEach((other) -> {
            PunchTimeRuleDTO dto = this.processTimeRuleDTO(other);
            response.getTimeRules().add(dto);
        });
        return response;
    }


    @Override
    public QryPunchLocationRuleListResponse listPunchLocationRules(ListPunchRulesCommonCommand cmd) {
        QryPunchLocationRuleListResponse response = new QryPunchLocationRuleListResponse();
        if (cmd.getPageAnchor() == null)
            cmd.setPageAnchor(0L);
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

        List<PunchLocationRule> results = this.punchProvider.queryPunchLocationRuleList(cmd.getOwnerType(), cmd.getOwnerId(), locator, pageSize + 1);
        if (null == results)
            return response;
        Long nextPageAnchor = null;
        if (results != null && results.size() > pageSize) {
            results.remove(results.size() - 1);
            nextPageAnchor = results.get(results.size() - 1).getId();
        }
        response.setNextPageAnchor(nextPageAnchor);
        response.setLocationRules(new ArrayList<PunchLocationRuleDTO>());
        results.forEach((other) -> {
            PunchLocationRuleDTO dto = this.processLocationRuleDTO(other);
            response.getLocationRules().add(dto);
        });
        return response;
    }

    @Override
    public void addPunchWiFiRule(PunchWiFiRuleDTO cmd) {

        Long userId = UserContext.current().getUser().getId();
        if (null == cmd.getOwnerId() || null == cmd.getOwnerType()) {
            LOGGER.error("Invalid owner type or  Id parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid owner type or  Id parameter in the command");
        }
        if (null == cmd.getName()) {
            LOGGER.error("Invalid name parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid name parameter in the command");
        }
        List<PunchWifiRule> punchWiFiRules = punchProvider.queryPunchWiFiRulesByName(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getName());
        if (null != punchWiFiRules) {
            LOGGER.error("Invalid name parameter in the command");
            throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE, PunchServiceErrorCode.ERROR_NAME_REPEAT,
                    "name repeat");

        } else {
            this.dbProvider.execute((TransactionStatus status) -> {
                PunchWifiRule obj = ConvertHelper.convert(cmd, PunchWifiRule.class);
                obj.setCreatorUid(userId);
                obj.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
                        .getTime()));
                this.punchProvider.createPunchWifiRule(obj);
                if (null == cmd.getWifis())
                    return null;
                for (PunchWiFiDTO dto : cmd.getWifis()) {
                    PunchWifi punchWifi = ConvertHelper.convert(dto, PunchWifi.class);
                    punchWifi.setOwnerType(cmd.getOwnerType());
                    punchWifi.setOwnerId(cmd.getOwnerId());
                    punchWifi.setCreatorUid(userId);
                    punchWifi.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    punchWifi.setWifiRuleId(obj.getId());
                    punchProvider.createPunchWifi(punchWifi);
                }
                return null;
            });
        }
    }

    @Override
    public ListPunchWiFiRuleListResponse listPunchWiFiRule(ListPunchRulesCommonCommand cmd) {
        ListPunchWiFiRuleListResponse response = new ListPunchWiFiRuleListResponse();
        if (cmd.getPageAnchor() == null)
            cmd.setPageAnchor(0L);
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

        List<PunchWifiRule> results = this.punchProvider.queryPunchWifiRuleList(cmd.getOwnerType(), cmd.getOwnerId(), locator, pageSize + 1);
        if (null == results)
            return response;
        Long nextPageAnchor = null;
        if (results != null && results.size() > pageSize) {
            results.remove(results.size() - 1);
            nextPageAnchor = results.get(results.size() - 1).getId();
        }
        response.setNextPageAnchor(nextPageAnchor);
        response.setWifiRules(new ArrayList<PunchWiFiRuleDTO>());
        results.forEach((other) -> {
            PunchWiFiRuleDTO dto = processWifiRuleDTO(other);
            response.getWifiRules().add(dto);
        });
        return response;
    }


    @Override
    public void addPunchWorkdayRule(PunchWorkdayRuleDTO cmd) {

        Long userId = UserContext.current().getUser().getId();
        if (null == cmd.getOwnerId() || null == cmd.getOwnerType()) {
            LOGGER.error("Invalid owner type or  Id parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid owner type or  Id parameter in the command");
        }
        if (null == cmd.getName()) {
            LOGGER.error("Invalid name parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid name parameter in the command");
        }
        List<PunchWorkdayRule> punchWorkdayRules = punchProvider.queryPunchWorkdayRulesByName(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getName());
        if (null != punchWorkdayRules) {
            LOGGER.error("Invalid name parameter in the command");
            throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE, PunchServiceErrorCode.ERROR_NAME_REPEAT,
                    "name repeat");

        } else {
            this.dbProvider.execute((TransactionStatus status) -> {
                PunchWorkdayRule obj = converDTO2WorkdayRule(cmd);

                this.punchProvider.createPunchWorkdayRule(obj);
                if (null != cmd.getWorkdays()) {
                    for (Long date : cmd.getWorkdays()) {
                        PunchHoliday holiday = new PunchHoliday();
                        holiday.setOwnerType(cmd.getOwnerType());
                        holiday.setOwnerId(cmd.getOwnerId());
                        holiday.setCreatorUid(userId);
                        holiday.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                        holiday.setWorkdayRuleId(obj.getId());
                        holiday.setRuleDate(new java.sql.Date(date));
                        holiday.setStatus(DateStatus.WORKDAY.getCode());
                        this.punchProvider.createPunchHoliday(holiday);

                    }
                }
                if (null != cmd.getHolidays()) {
                    for (Long date : cmd.getHolidays()) {
                        PunchHoliday holiday = new PunchHoliday();
                        holiday.setOwnerType(cmd.getOwnerType());
                        holiday.setOwnerId(cmd.getOwnerId());
                        holiday.setCreatorUid(userId);
                        holiday.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                        holiday.setWorkdayRuleId(obj.getId());
                        holiday.setRuleDate(new java.sql.Date(date));
                        holiday.setStatus(DateStatus.HOLIDAY.getCode());
                        this.punchProvider.createPunchHoliday(holiday);
                    }
                }
                return null;
            });
        }
    }

    @Override
    public void updatePunchWorkdayRule(PunchWorkdayRuleDTO cmd) {
        Long userId = UserContext.current().getUser().getId();
        if (null == cmd.getId()) {
            LOGGER.error("Invalid   Id parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid  Id parameter in the command");
        }
        if (null == cmd.getOwnerId() || null == cmd.getOwnerType()) {
            LOGGER.error("Invalid owner type or  Id parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid owner type or  Id parameter in the command");
        }
        if (null == cmd.getName()) {
            LOGGER.error("Invalid name parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid name parameter in the command");
        }

        List<PunchWorkdayRule> punchWorkdayRules = punchProvider.queryPunchWorkdayRulesByName(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getName());
        if (null != punchWorkdayRules && (punchWorkdayRules.size() > 1 || !punchWorkdayRules.get(0).getId().equals(cmd.getId()))) {
            //有两个同名rules(正常业务不可能) 或者 同名rule的id不等于修改的id 则重名错误
            LOGGER.error("Invalid name parameter in the command");
            throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE, PunchServiceErrorCode.ERROR_NAME_REPEAT,
                    "name repeat");

        }

        PunchWorkdayRule old = this.punchProvider.getPunchWorkdayRuleById(cmd.getId());
        if (null == old) {
            LOGGER.error("Invalid   Id parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid  Id parameter in the command:can not found rule");
        } else {
            this.dbProvider.execute((TransactionStatus status) -> {
                PunchWorkdayRule obj = converDTO2WorkdayRule(cmd);
                obj.setCreateTime(old.getCreateTime());
                obj.setCreatorUid(old.getCreatorUid());
                this.punchProvider.updatePunchWorkdayRule(obj);
                this.punchProvider.deletePunchHolidayByRuleId(cmd.getId());
                if (null != cmd.getWorkdays()) {
                    for (Long date : cmd.getWorkdays()) {
                        PunchHoliday holiday = new PunchHoliday();
                        holiday.setOwnerType(cmd.getOwnerType());
                        holiday.setOwnerId(cmd.getOwnerId());
                        holiday.setCreatorUid(userId);
                        holiday.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                        holiday.setWorkdayRuleId(obj.getId());
                        holiday.setRuleDate(new java.sql.Date(date));
                        holiday.setStatus(DateStatus.WORKDAY.getCode());
                        this.punchProvider.createPunchHoliday(holiday);

                    }
                }
                if (null != cmd.getHolidays()) {
                    for (Long date : cmd.getHolidays()) {
                        PunchHoliday holiday = new PunchHoliday();
                        holiday.setOwnerType(cmd.getOwnerType());
                        holiday.setOwnerId(cmd.getOwnerId());
                        holiday.setCreatorUid(userId);
                        holiday.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                        holiday.setWorkdayRuleId(obj.getId());
                        holiday.setRuleDate(new java.sql.Date(date));
                        holiday.setStatus(DateStatus.HOLIDAY.getCode());
                        this.punchProvider.createPunchHoliday(holiday);
                    }
                }
                return null;
            });
        }
    }


    @Override
    public void addPunchTimeRule(AddPunchTimeRuleCommand cmd) {
        Long userId = UserContext.current().getUser().getId();
        if (null == cmd.getOwnerId() || null == cmd.getOwnerType()) {
            LOGGER.error("Invalid owner type or  Id parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid owner type or  Id parameter in the command");
        }
        if (null == cmd.getPunchTimesPerDay()) {
            LOGGER.error("Invalid PunchTimesPerDay parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid PunchTimesPerDay parameter in the command");
        }
        cmd.setOwnerId(getTopEnterpriseId(cmd.getOwnerId()));
        List<PunchTimeRule> punchTimeRules = punchProvider.queryPunchTimeRules(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getTargetType(), cmd.getTargetId(), cmd.getName());
        if (null != punchTimeRules) {
            LOGGER.error("Invalid name parameter in the command");
            throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE, PunchServiceErrorCode.ERROR_NAME_REPEAT,
                    "name repeat");
        } else {
            PunchTimeRule punchTimeRule = ConvertHelper.convert(cmd, PunchTimeRule.class);
            punchTimeRule.setAfternoonArriveTime(convertTime(cmd.getAfternoonArriveTime()));
            punchTimeRule.setPunchTimesPerDay(cmd.getPunchTimesPerDay());
            punchTimeRule.setNoonLeaveTime(convertTime(cmd.getNoonLeaveTime()));
            punchTimeRule.setDaySplitTime(convertTime(cmd.getDaySplitTime()));
            //计算work time 在下面方法中
            convertTime(punchTimeRule, cmd.getStartEarlyTime(), cmd.getStartLateTime(), cmd.getEndEarlyTime());

            punchTimeRule.setStartEarlyTimeLong(cmd.getStartEarlyTime());
            punchTimeRule.setAfternoonArriveTimeLong(cmd.getAfternoonArriveTime());
            punchTimeRule.setNoonLeaveTimeLong(cmd.getNoonLeaveTime());
            punchTimeRule.setStartLateTimeLong(cmd.getStartLateTime());
            punchTimeRule.setDaySplitTimeLong(cmd.getDaySplitTime());

            punchTimeRule.setCreatorUid(userId);
            punchTimeRule.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
                    .getTime()));
            punchProvider.createPunchTimeRule(punchTimeRule);

        }
    }

    PunchTimeRule convertPunchTimeRule(PunchTimeRuleDTO dto) {
        PunchTimeRule punchTimeRule = ConvertHelper.convert(dto, PunchTimeRule.class);
        punchTimeRule.setAfternoonArriveTime(convertTime(dto.getAfternoonArriveTime()));
//		punchTimeRule.setPunchTimesPerDay(dto.getPunchTimesPerDay());
        punchTimeRule.setNoonLeaveTime(convertTime(dto.getNoonLeaveTime()));
        punchTimeRule.setDaySplitTime(convertTime(dto.getDaySplitTime()));
//		convertTime(punchTimeRule, dto.getStartEarlyTime(), dto.getStartLateTime(), dto.getEndEarlyTime());
        punchTimeRule.setCreatorUid(UserContext.current().getUser().getId());
        punchTimeRule.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
                .getTime()));
        return punchTimeRule;
    }

    @Override
    public void updatePunchTimeRule(UpdatePunchTimeRuleCommand cmd) {
        Long userId = UserContext.current().getUser().getId();
        if (null == cmd.getId()) {
            LOGGER.error("Invalid   Id parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid  Id parameter in the command");
        }
        if (null == cmd.getOwnerId() || null == cmd.getOwnerType()) {
            LOGGER.error("Invalid owner type or  Id parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid owner type or  Id parameter in the command");
        }
        cmd.setOwnerId(getTopEnterpriseId(cmd.getOwnerId()));
        if (null == cmd.getPunchTimesPerDay()) {
            LOGGER.error("Invalid PunchTimesPerDay parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid PunchTimesPerDay parameter in the command");
        }

        List<PunchTimeRule> punchTimeRules = punchProvider.queryPunchTimeRules(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getTargetType(), cmd.getTargetId(), cmd.getName());
        if (null != punchTimeRules && (punchTimeRules.size() > 1 || !punchTimeRules.get(0).getId().equals(cmd.getId()))) {
            //有两个同名rules(正常业务不可能) 或者 同名rule的id不等于修改的id 则重名错误
            LOGGER.error("Invalid name parameter in the command");
            throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE, PunchServiceErrorCode.ERROR_NAME_REPEAT,
                    "name repeat");

        }
        PunchTimeRule punchTimeRule = punchProvider.getPunchTimeRuleById(cmd.getId());
        if (null == punchTimeRule) {
            LOGGER.error("Invalid   Id parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid  Id parameter in the command:can not found rule");
        } else {
            punchTimeRule.setName(cmd.getName());
            punchTimeRule.setAfternoonArriveTime(convertTime(cmd.getAfternoonArriveTime()));
            punchTimeRule.setPunchTimesPerDay(cmd.getPunchTimesPerDay());
            punchTimeRule.setNoonLeaveTime(convertTime(cmd.getNoonLeaveTime()));
            punchTimeRule.setDaySplitTime(convertTime(cmd.getDaySplitTime()));
            convertTime(punchTimeRule, cmd.getStartEarlyTime(), cmd.getStartLateTime(), cmd.getEndEarlyTime());

            punchTimeRule.setStartEarlyTimeLong(cmd.getStartEarlyTime());
            punchTimeRule.setAfternoonArriveTimeLong(cmd.getAfternoonArriveTime());
            punchTimeRule.setNoonLeaveTimeLong(cmd.getNoonLeaveTime());
            punchTimeRule.setStartLateTimeLong(cmd.getStartLateTime());
            punchTimeRule.setDaySplitTimeLong(cmd.getDaySplitTime());
            punchTimeRule.setDescription(cmd.getDescription());
            punchTimeRule.setOperatorUid(userId);
            punchTimeRule.setOperateTime(new Timestamp(DateHelper.currentGMTTime()
                    .getTime()));
            punchProvider.updatePunchTimeRule(punchTimeRule);

        }
    }

    @Override
    public void deletePunchTimeRule(DeleteCommonCommand cmd) {
        List<PunchRule> punchRules = this.punchProvider.findPunchRules(null, null, cmd.getId(), null, null, null);
        if (null == punchRules) {
            //验证是否有这个权限删除这个owner的rule

            //根据id和ownerid删除一条rule
//			this.punchProvider.deletePunchTimeRuleByOwnerAndId(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getId());
        } else {
            //被使用的不删,报错
            throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
                    PunchServiceErrorCode.ERROR_RULE_USING,
                    "delete rule is using ");
        }
    }


    PunchTimeRuleDTO processTimeRuleDTO(PunchTimeRule other) {
        PunchTimeRuleDTO dto = ConvertHelper.convert(other, PunchTimeRuleDTO.class);
        dto.setAfternoonArriveTime(null != other.getAfternoonArriveTimeLong() ? other.getAfternoonArriveTimeLong() : convertTimeToGMTMillisecond(other.getAfternoonArriveTime()));
        dto.setNoonLeaveTime(null != other.getNoonLeaveTimeLong() ? other.getNoonLeaveTimeLong() : convertTimeToGMTMillisecond(other.getNoonLeaveTime()));
//		dto.setStartEarlyTime(null!=other.getStartEarlyTimeLong()?other.getStartEarlyTimeLong():convertTimeToGMTMillisecond(other.getStartEarlyTime()));
//		dto.setStartLateTime(null!=other.getStartLateTimeLong()?other.getStartLateTimeLong():convertTimeToGMTMillisecond(other.getStartLateTime()));
//
//		dto.setEndEarlyTime(dto.getStartEarlyTime() + (null!=other.getWorkTimeLong()?other.getWorkTimeLong():convertTimeToGMTMillisecond(other.getWorkTime())));
        dto.setDaySplitTime(null != other.getDaySplitTimeLong() ? other.getDaySplitTimeLong() : convertTimeToGMTMillisecond(other.getDaySplitTime()));
        return dto;
    }

    @Override
    public void addPunchLocationRule(PunchLocationRuleDTO cmd) {

        Long userId = UserContext.current().getUser().getId();
        if (null == cmd.getOwnerId() || null == cmd.getOwnerType()) {
            LOGGER.error("Invalid owner type or  Id parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid owner type or  Id parameter in the command");
        }
        if (null == cmd.getName()) {
            LOGGER.error("Invalid name parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid name parameter in the command");
        }
        List<PunchLocationRule> punchLocationRules = punchProvider.queryPunchLocationRulesByName(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getName());
        if (null != punchLocationRules) {
            LOGGER.error("Invalid name parameter in the command");
            throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE, PunchServiceErrorCode.ERROR_NAME_REPEAT,
                    "name repeat");

        } else {
            this.dbProvider.execute((TransactionStatus status) -> {
                PunchLocationRule obj = ConvertHelper.convert(cmd, PunchLocationRule.class);
                obj.setCreatorUid(userId);
                obj.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
                        .getTime()));
                this.punchProvider.createPunchLocationRule(obj);
                if (null == cmd.getPunchGeoPoints())
                    return null;
                for (PunchGeoPointDTO punchGeopointDTO : cmd.getPunchGeoPoints()) {
                    PunchGeopoint punchGeopoint = ConvertHelper.convert(punchGeopointDTO, PunchGeopoint.class);
                    punchGeopoint.setOwnerType(cmd.getOwnerType());
                    punchGeopoint.setOwnerId(cmd.getOwnerId());
                    punchGeopoint.setLocationRuleId(obj.getId());
                    punchGeopoint.setCreatorUid(userId);
                    punchGeopoint.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    punchGeopoint.setGeohash(GeoHashUtils.encode(
                            punchGeopoint.getLatitude(), punchGeopoint.getLongitude()));
                    punchProvider.createPunchGeopoint(punchGeopoint);
                }
                return null;
            });
        }
    }

    @Override
    public void updatePunchLocationRule(PunchLocationRuleDTO cmd) {
        Long userId = UserContext.current().getUser().getId();
        if (null == cmd.getId()) {
            LOGGER.error("Invalid   Id parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid  Id parameter in the command");
        }
        if (null == cmd.getOwnerId() || null == cmd.getOwnerType()) {
            LOGGER.error("Invalid owner type or  Id parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid owner type or  Id parameter in the command");
        }
        if (null == cmd.getName()) {
            LOGGER.error("Invalid name parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid name parameter in the command");
        }

        List<PunchLocationRule> punchLocationRules = punchProvider.queryPunchLocationRulesByName(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getName());
        if (null != punchLocationRules && (punchLocationRules.size() > 1 || !punchLocationRules.get(0).getId().equals(cmd.getId()))) {
            //有两个同名rules(正常业务不可能) 或者 同名rule的id不等于修改的id 则重名错误
            LOGGER.error("Invalid name parameter in the command");
            throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE, PunchServiceErrorCode.ERROR_NAME_REPEAT,
                    "name repeat");

        }
        PunchLocationRule obj = this.punchProvider.getPunchLocationRuleById(cmd.getId());
        if (null == obj) {
            LOGGER.error("Invalid   Id parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid  Id parameter in the command:can not found rule");
        } else {
            this.dbProvider.execute((TransactionStatus status) -> {
                obj.setDescription(cmd.getDescription());
                obj.setName(cmd.getName());
                obj.setOwnerType(cmd.getOwnerType());
                obj.setOwnerId(cmd.getOwnerId());
                this.punchProvider.updatePunchLocationRule(obj);
                this.punchProvider.deletePunchGeopointsByRuleId(cmd.getId());
                if (null == cmd.getPunchGeoPoints())
                    return null;
                for (PunchGeoPointDTO punchGeopointDTO : cmd.getPunchGeoPoints()) {
                    PunchGeopoint punchGeopoint = ConvertHelper.convert(punchGeopointDTO, PunchGeopoint.class);
                    punchGeopoint.setOwnerType(cmd.getOwnerType());
                    punchGeopoint.setOwnerId(cmd.getOwnerId());
                    punchGeopoint.setLocationRuleId(obj.getId());
                    punchGeopoint.setCreatorUid(userId);
                    punchGeopoint.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    punchGeopoint.setGeohash(GeoHashUtils.encode(
                            punchGeopoint.getLatitude(), punchGeopoint.getLongitude()));
                    punchProvider.createPunchGeopoint(punchGeopoint);
                }
                return null;
            });
        }
    }

    @Override
    public void deletePunchLocationRule(DeleteCommonCommand cmd) {
        List<PunchRule> punchRules = this.punchProvider.findPunchRules(null, null, null, cmd.getId(), null, null);
        if (null == punchRules) {
            //验证是否有这个权限删除这个owner的rule


            this.dbProvider.execute((TransactionStatus status) -> {
                //根据id和ownerid删除一条rule
                this.punchProvider.deletePunchLocationRuleByOwnerAndId(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getId());
                this.punchProvider.deletePunchGeopointsByRuleId(cmd.getId());
                ;

                return null;
            });
        } else {
            //被使用的不删,报错
            throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
                    PunchServiceErrorCode.ERROR_RULE_USING,
                    "delete rule is using ");
        }
    }


    PunchLocationRuleDTO processLocationRuleDTO(PunchLocationRule other) {
        PunchLocationRuleDTO dto = ConvertHelper.convert(other, PunchLocationRuleDTO.class);
        List<PunchGeopoint> geos = this.punchProvider.listPunchGeopointsByRuleId(other.getOwnerType(), other.getOwnerId(), other.getId());
        if (null != geos) {
            dto.setPunchGeoPoints(new ArrayList<PunchGeoPointDTO>());
            for (PunchGeopoint geo : geos) {
                PunchGeoPointDTO geoDTO = ConvertHelper.convert(geo, PunchGeoPointDTO.class);
                dto.getPunchGeoPoints().add(geoDTO);
            }
        }
        return dto;
    }


    @Override
    public void updatePunchWiFiRule(PunchWiFiRuleDTO cmd) {
        Long userId = UserContext.current().getUser().getId();
        if (null == cmd.getId()) {
            LOGGER.error("Invalid   Id parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid  Id parameter in the command");
        }
        if (null == cmd.getOwnerId() || null == cmd.getOwnerType()) {
            LOGGER.error("Invalid owner type or  Id parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid owner type or  Id parameter in the command");
        }
        if (null == cmd.getName()) {
            LOGGER.error("Invalid name parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid name parameter in the command");
        }
        List<PunchWifiRule> punchWiFiRules = punchProvider.queryPunchWiFiRulesByName(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getName());
        if (null != punchWiFiRules && (punchWiFiRules.size() > 1 || !punchWiFiRules.get(0).getId().equals(cmd.getId()))) {
            //有两个同名rules(正常业务不可能) 或者 同名rule的id不等于修改的id 则重名错误
            LOGGER.error("Invalid name parameter in the command");
            throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE, PunchServiceErrorCode.ERROR_NAME_REPEAT,
                    "name repeat");

        }
        PunchWifiRule obj = this.punchProvider.getPunchWifiRuleById(cmd.getId());
        if (null == obj) {
            LOGGER.error("Invalid   Id parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid  Id parameter in the command:can not found rule");
        } else {
            this.dbProvider.execute((TransactionStatus status) -> {
                obj.setDescription(cmd.getDescription());
                obj.setName(cmd.getName());
                obj.setOwnerType(cmd.getOwnerType());
                obj.setOwnerId(cmd.getOwnerId());
                this.punchProvider.updatePunchWifiRule(obj);
                this.punchProvider.deletePunchWifisByRuleId(cmd.getId());
                if (null == cmd.getWifis())
                    return null;
                for (PunchWiFiDTO dto : cmd.getWifis()) {
                    PunchWifi punchWifi = ConvertHelper.convert(dto, PunchWifi.class);
                    punchWifi.setOwnerType(cmd.getOwnerType());
                    punchWifi.setOwnerId(cmd.getOwnerId());
                    punchWifi.setCreatorUid(userId);
                    punchWifi.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    punchWifi.setWifiRuleId(obj.getId());
                    punchProvider.createPunchWifi(punchWifi);
                }
                return null;
            });
        }
    }

    @Override
    public void deletePunchWiFiRule(DeleteCommonCommand cmd) {
        List<PunchRule> punchRules = this.punchProvider.findPunchRules(null, null, null, null, cmd.getId(), null);
        if (null == punchRules) {
            //验证是否有这个权限删除这个owner的rule


            this.dbProvider.execute((TransactionStatus status) -> {
                //根据id和ownerid删除一条rule
                this.punchProvider.deletePunchWifiRuleByOwnerAndId(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getId());
                this.punchProvider.deletePunchWifisByRuleId(cmd.getId());
                ;

                return null;
            });
        } else {
            //被使用的不删,报错
            throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
                    PunchServiceErrorCode.ERROR_RULE_USING,
                    "delete rule is using ");
        }

    }


    PunchWiFiRuleDTO processWifiRuleDTO(PunchWifiRule other) {
        PunchWiFiRuleDTO dto = ConvertHelper.convert(other, PunchWiFiRuleDTO.class);

        List<PunchWifi> wifis = this.punchProvider.listPunchWifisByRuleId(other.getOwnerType(), other.getOwnerId(), other.getId());
        if (null != wifis) {
            dto.setWifis(new ArrayList<PunchWiFiDTO>());
            for (PunchWifi geo : wifis) {
                PunchWiFiDTO wifiDTO = ConvertHelper.convert(geo, PunchWiFiDTO.class);
                dto.getWifis().add(wifiDTO);
            }
        }
        return dto;
    }


    private PunchWorkdayRule converDTO2WorkdayRule(PunchWorkdayRuleDTO dto) {
        PunchWorkdayRule obj = ConvertHelper.convert(dto, PunchWorkdayRule.class);
        if (null == dto.getWorkWeekDates()) {
            obj.setWorkWeekDates("0000000");
        } else {
            int openWorkdayInt = 0;
            for (Integer weekdayInteger : dto.getWorkWeekDates())
                openWorkdayInt += Math.pow(10, weekdayInteger);
            String openWorkday = String.valueOf(openWorkdayInt);
            for (; openWorkday.length() < 7; ) {
                openWorkday = "0" + openWorkday;
            }
            obj.setWorkWeekDates(openWorkday);
        }
        obj.setCreatorUid(UserContext.current().getUser().getId());
        obj.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
                .getTime()));

        return obj;
    }


    @Override
    public void deletePunchWorkdayRule(DeleteCommonCommand cmd) {
        List<PunchRule> punchRules = this.punchProvider.findPunchRules(null, null, null, null, null, cmd.getId());
        if (null == punchRules) {
            //验证是否有这个权限删除这个owner的rule


            this.dbProvider.execute((TransactionStatus status) -> {
                //根据id和ownerid删除一条rule
                this.punchProvider.deletePunchWorkdayRuleByOwnerAndId(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getId());
                this.punchProvider.deletePunchHolidayByRuleId(cmd.getId());

                return null;
            });
        } else {
            //被使用的不删,报错
            throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
                    PunchServiceErrorCode.ERROR_RULE_USING,
                    "delete rule is using ");
        }

    }

    @Override
    public ListPunchWorkdayRuleListResponse listPunchWorkdayRule(ListPunchRulesCommonCommand cmd) {
        ListPunchWorkdayRuleListResponse response = new ListPunchWorkdayRuleListResponse();
        if (cmd.getPageAnchor() == null)
            cmd.setPageAnchor(0L);
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

        List<PunchWorkdayRule> results = this.punchProvider.queryPunchWorkdayRuleList(cmd.getOwnerType(), cmd.getOwnerId(), locator, pageSize + 1);
        if (null == results)
            return response;
        Long nextPageAnchor = null;
        if (results != null && results.size() > pageSize) {
            results.remove(results.size() - 1);
            nextPageAnchor = results.get(results.size() - 1).getId();
        }
        response.setNextPageAnchor(nextPageAnchor);
        response.setWorkdayRules(new ArrayList<PunchWorkdayRuleDTO>());
        results.forEach((other) -> {
            PunchWorkdayRuleDTO dto = ConvertPunchWiFiRule2DTO(other);
            response.getWorkdayRules().add(dto);
        });
        return response;
    }

    private PunchWorkdayRuleDTO ConvertPunchWiFiRule2DTO(PunchWorkdayRule other) {
        PunchWorkdayRuleDTO dto = ConvertHelper.convert(other, PunchWorkdayRuleDTO.class);
        dto.setWorkWeekDates(new ArrayList<Integer>());
        int openWeekInt = Integer.valueOf(other.getWorkWeekDates());
        for (int i = 0; i < 7; i++) {
            if (openWeekInt % 10 == 1)
                dto.getWorkWeekDates().add(i);
            openWeekInt = openWeekInt / 10;
        }

        List<PunchHoliday> holidays = this.punchProvider.listPunchHolidaysByRuleId(other.getOwnerType(), other.getOwnerId(), other.getId());
        if (null != holidays) {
            dto.setWorkdays(new ArrayList<Long>());
            dto.setHolidays(new ArrayList<Long>());
            for (PunchHoliday day : holidays) {
                if (DateStatus.HOLIDAY.equals(DateStatus.fromCode(day.getStatus())))
                    dto.getHolidays().add(day.getRuleDate().getTime());
                if (DateStatus.WORKDAY.equals(DateStatus.fromCode(day.getStatus())))
                    dto.getWorkdays().add(day.getRuleDate().getTime());
            }
        }
        return dto;
    }

    @Override
    public void addPunchRule(PunchRuleDTO cmd) {

        if (null == cmd.getTimeRuleId()) {
            LOGGER.error("Invalid TimeRuleId parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid TimeRuleId parameter in the command");
        }
        if (null == cmd.getWorkdayRuleId()) {
            LOGGER.error("Invalid  WorkdayRuleId parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid  WorkdayRuleId parameter in the command");
        }
        if (null == cmd.getWifiRuleId() && null == cmd.getLocationRuleId()) {
            LOGGER.error("wifi and location can not be both null ");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "wifi and location can not be both null ");
        }
        if (null == cmd.getOwnerId() || null == cmd.getOwnerType()) {
            LOGGER.error("Invalid owner type or  Id parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid owner type or  Id parameter in the command");
        }
        if (null == cmd.getName()) {
            LOGGER.error("Invalid name parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid name parameter in the command");
        }
        List<PunchRule> punchRules = punchProvider.queryPunchRulesByName(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getName());
        if (null != punchRules) {
            LOGGER.error("Invalid name parameter in the command");
            throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE, PunchServiceErrorCode.ERROR_NAME_REPEAT,
                    "name repeat");

        } else {
            PunchRule obj = ConvertHelper.convert(cmd, PunchRule.class);
            obj.setCreatorUid(UserContext.current().getUser().getId());
            obj.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
                    .getTime()));

            this.punchProvider.createPunchRule(obj);

        }
    }

    @Override
    public void updatePunchRule(PunchRuleDTO cmd) {
        Long userId = UserContext.current().getUser().getId();
        if (null == cmd.getId()) {
            LOGGER.error("Invalid   Id parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid  Id parameter in the command");
        }
        if (null == cmd.getOwnerId() || null == cmd.getOwnerType()) {
            LOGGER.error("Invalid owner type or  Id parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid owner type or  Id parameter in the command");
        }
        if (null == cmd.getName()) {
            LOGGER.error("Invalid name parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid name parameter in the command");
        }
        List<PunchRule> punchRules = punchProvider.queryPunchRulesByName(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getName());
        if (null != punchRules && (punchRules.size() > 1 || !punchRules.get(0).getId().equals(cmd.getId()))) {
            //有两个同名rules(正常业务不可能) 或者 同名rule的id不等于修改的id 则重名错误
            LOGGER.error("Invalid name parameter in the command");
            throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE, PunchServiceErrorCode.ERROR_NAME_REPEAT,
                    "name repeat");

        }

        PunchRule old = this.punchProvider.getPunchRuleById(cmd.getId());
        if (null == old) {
            LOGGER.error("Invalid   Id parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid  Id parameter in the command:can not found rule");
        } else {
            PunchRule obj = ConvertHelper.convert(cmd, PunchRule.class);
            obj.setCreateTime(old.getCreateTime());
            obj.setCreatorUid(old.getCreatorUid());
            obj.setOperatorUid(userId);
            obj.setOperateTime(new Timestamp(DateHelper.currentGMTTime()
                    .getTime()));
            this.punchProvider.updatePunchRule(obj);
        }
    }

    @Override
    public void deletePunchRule(DeleteCommonCommand cmd) {
        List<PunchRuleOwnerMap> punchRuleMaps = this.punchProvider.queryPunchRuleOwnerMapsByRuleId(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getId());
        if (null == punchRuleMaps) {
            //验证是否有这个权限删除这个owner的rule


            this.dbProvider.execute((TransactionStatus status) -> {
                //根据id和ownerid删除一条rule
                PunchRule obj = this.punchProvider.getPunchRuleById(cmd.getId());
                if (obj.getOwnerId().equals(cmd.getOwnerId()) && obj.getOwnerType().equals(cmd.getOwnerType()))
                    this.punchProvider.deletePunchRule(obj);

                return null;
            });
        } else {
            //被使用的不删,报错
            throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
                    PunchServiceErrorCode.ERROR_RULE_USING,
                    "delete rule is using ");
        }
    }

    @Override
    public ListPunchRulesResponse listPunchRules(ListPunchRulesCommonCommand cmd) {
        ListPunchRulesResponse response = new ListPunchRulesResponse();
        if (cmd.getPageAnchor() == null)
            cmd.setPageAnchor(0L);
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

        List<PunchRule> results = this.punchProvider.queryPunchRuleList(cmd.getOwnerType(), cmd.getOwnerId(), locator, pageSize + 1);
        if (null == results)
            return response;
        Long nextPageAnchor = null;
        if (results != null && results.size() > pageSize) {
            results.remove(results.size() - 1);
            nextPageAnchor = results.get(results.size() - 1).getId();
        }
        response.setNextPageAnchor(nextPageAnchor);
        response.setPunchRuleDTOs(new ArrayList<PunchRuleDTO>());
        results.forEach((other) -> {
            PunchRuleDTO dto = ConvertHelper.convert(other, PunchRuleDTO.class);
            PunchTimeRule timeRule = this.punchProvider.getPunchTimeRuleById(other.getTimeRuleId());
            if (null != timeRule)
                dto.setTimeRuleName(timeRule.getName());
            PunchLocationRule locationRule = this.punchProvider.getPunchLocationRuleById(other.getLocationRuleId());
            if (null != locationRule)
                dto.setLocationRuleName(locationRule.getName());
            PunchWifiRule wifiRule = this.punchProvider.getPunchWifiRuleById(other.getWifiRuleId());
            if (null != wifiRule)
                dto.setWifiRuleName(wifiRule.getName());
            PunchWorkdayRule workdayRule = this.punchProvider.getPunchWorkdayRuleById(other.getWorkdayRuleId());
            if (null != workdayRule)
                dto.setWorkdayRuleName(workdayRule.getName());
            response.getPunchRuleDTOs().add(dto);
        });
        return response;
    }

    @Override
    public void addPunchRuleMap(PunchRuleMapDTO cmd) {

        if (null == cmd.getOwnerId() || null == cmd.getOwnerType()) {
            LOGGER.error("Invalid owner type or  Id parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid owner type or  Id parameter in the command");
        }
        if (null == cmd.getTargetId() || null == cmd.getTargetType()) {
            LOGGER.error("Invalid owner type or  Id parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid target type or  Id parameter in the command");
        }
//		if(null == cmd.getPunchRuleId()){
//			LOGGER.error("Invalid PunchRuleId parameter in the command");
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
//					"Invalid PunchRuleId parameter in the command");
//		}
//
        PunchRuleOwnerMap old = this.punchProvider.getPunchRuleOwnerMapByOwnerAndTarget(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getTargetType(), cmd.getTargetId());
        PunchRuleOwnerMap obj = ConvertHelper.convert(cmd, PunchRuleOwnerMap.class);
        if (null == old) {
            //如果没有就新建
            obj.setCreatorUid(UserContext.current().getUser().getId());
            obj.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
                    .getTime()));
            this.punchProvider.createPunchRuleOwnerMap(obj);
        } else {
            //有就更新
            obj.setId(old.getId());
            obj.setCreatorUid(old.getCreatorUid());
            obj.setCreateTime(old.getCreateTime());
            this.punchProvider.updatePunchRuleOwnerMap(obj);
        }
    }

    @Override
    public ListPunchRuleMapsResponse listPunchRuleMaps(ListPunchRuleMapsCommand cmd) {
        ListPunchRuleMapsResponse response = new ListPunchRuleMapsResponse();
        if (cmd.getPageAnchor() == null)
            cmd.setPageAnchor(0L);
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        //找到所有子部门 下面的用户
        List<Long> userIds = null;
        if (cmd.getOrganizationId() != null) {

            List<String> groupTypeList = new ArrayList<String>();
            groupTypeList.add(OrganizationGroupType.ENTERPRISE.getCode());
            groupTypeList.add(OrganizationGroupType.DEPARTMENT.getCode());
            List<OrganizationMemberDTO> organizationMembers = this.organizationService.listAllChildOrganizationPersonnel
                    (cmd.getOrganizationId(), groupTypeList, null);
            if (null == organizationMembers)
                return response;
            userIds = new ArrayList<Long>();
            for (OrganizationMemberDTO member : organizationMembers) {
                if (member.getTargetType() != null && member.getTargetType().equals(OrganizationMemberTargetType.USER.getCode()))
                    userIds.add(member.getTargetId());
            }
        }
        List<PunchRuleOwnerMap> results = this.punchProvider.queryPunchRuleOwnerMapList(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getTargetType(),
                cmd.getTargetId(), userIds, locator, pageSize + 1);
        if (null == results)
            return response;
        Long nextPageAnchor = null;
        if (results != null && results.size() > pageSize) {
            results.remove(results.size() - 1);
            nextPageAnchor = results.get(results.size() - 1).getId();
        }
        response.setNextPageAnchor(nextPageAnchor);
        response.setPunchRuleMaps(new ArrayList<PunchRuleMapDTO>());

        Organization organization = checkOrganization(cmd.getOwnerId());
        for (PunchRuleOwnerMap other : results) {
            PunchRuleMapDTO dto = ConvertHelper.convert(other, PunchRuleMapDTO.class);
//			PunchRule pr = this.punchProvider.getPunchRuleById(other.getPunchRuleId());
//			if(pr == null)
//				throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
//						PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_SETTING,
//						"have no punch rule");
//			dto.setPunchRuleName(pr.getName());
            if (PunchOwnerType.User.getCode().equals(other.getTargetType())) {
                OrganizationMember member = findOrganizationMemberByOrgIdAndUId(other.getTargetId(), organization.getPath());
                if (null == member)
                    continue;
                dto.setTargetName(member.getContactName());
                OrganizationDTO dept = this.findUserDepartment(other.getTargetId(), other.getOwnerId());
                if (null != dept)
                    dto.setTargetDept(dept.getName());

            }
//			if (other.getReviewRuleId() != null) {
//				ApprovalRule approvalRule = approvalRuleProvider.findApprovalRuleById(other.getReviewRuleId());
//				if (approvalRule != null) {
//					dto.setReviewRuleName(approvalRule.getName());
//				}
//			}
            response.getPunchRuleMaps().add(dto);
        }
        return response;
    }

    @Override
    public OrganizationMember findOrganizationMemberByOrgIdAndUId(Long targetId, String path) {
        List<OrganizationMember> members = this.organizationProvider.listOrganizationMemberByPath(null, path, null, null, new CrossShardListingLocator(), 1000000);
        if (null == members || members.size() == 0)
            return null;
        for (OrganizationMember member : members) {
            if (member != null && member.getTargetId() != null && member.getTargetId().equals(targetId))
                return member;
        }
        return null;
    }

    @Override
    public Long getTopEnterpriseId(Long organizationId) {
        Organization organization = organizationProvider.findOrganizationById(organizationId);
        if (organization.getParentId() == null)
            return organizationId;
        else {
            return Long.valueOf(organization.getPath().split("/")[1]);
        }
    }


    /**
     * 找到用户的部门-多部门取最上级第一个
     */
    private OrganizationDTO findUserDepartment(Long userId, Long organizationId) {
//		// 多部门找顶级部门
//		List<OrganizationMember> organizationMembers = this.organizationProvider.findOrganizationMembersByOrgIdAndUId( userId,  organizationId);
//		Organization result = null;
//		if(organizationMembers == null || organizationMembers.isEmpty())
//			return null;
//		for(OrganizationMember member : organizationMembers){
//			//groupid == 0 话直接返回总公司
//			if(null == member.getGroupId() || member.getGroupId().equals(0L))
//				return this.organizationProvider.findOrganizationById(member.getOrganizationId());
//			if(result == null)
//				result = this.organizationProvider.findOrganizationById(member.getGroupId());
//			else{
//				//多部门找顶级部门
//				Organization org = this.organizationProvider.findOrganizationById(member.getGroupId());
//				if(org.getLevel()<result.getLevel()){
//					//取最顶层的
//					result = org;
//				}
//			}
//		}
        UserIdentifier userIdentifier = this.userProvider.findClaimedIdentifierByOwnerAndType(userId, IdentifierType.MOBILE.getCode());
        if (null == userIdentifier) {
            LOGGER.debug("userIdentifier is null...userId = " + userId);
            return null;
        } else {
            List<String> groupTypes = new ArrayList<String>();
            groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
            return this.organizationService.getMemberTopDepartment(groupTypes,
                    userIdentifier.getIdentifierToken(), organizationId);
        }


    }

    /**
     * 向上递归找规则
     */
    private PunchRuleOwnerMap getPunchRule(Long ownerId, Organization dept, int loopMax) {
        if (--loopMax < 0 || null == dept)
            throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
                    PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_SETTING,
                    "have no punch rule");
        PunchRuleOwnerMap ruleMap = this.punchProvider.getPunchRuleOwnerMapByTarget(
                PunchOwnerType.ORGANIZATION.getCode(), dept.getId());
        if (null != ruleMap)
            return ruleMap;
        else {
            Organization parent = this.organizationProvider.findOrganizationById(dept.getParentId());
            if (null == parent)
                throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
                        PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_SETTING,
                        "have no punch rule owner id = '" + ownerId + "'");
            else
                return getPunchRule(ownerId, parent, loopMax);
        }
    }

    /**
     * 找到用户的打卡规则
     * modify by wh 2018年07月02日:没有注册的用户pr 返回null
     */
    @Override
    public PunchRule getPunchRule(String ownerType, Long ownerId, Long userId) {
        if(userId == null || userId.equals(0L)){
            return null;
        }
        UniongroupMemberDetail detail = findUserMemberDetail(userId, ownerId);
        if (null == detail)
            return null;
        PunchRule pr = punchProvider.getPunchruleByPunchOrgId(detail.getGroupId());
        return pr;

    }

    @Override
    public PunchRule getPunchRule(OrganizationMemberDetails memberDetail) {
        if (memberDetail == null) {
            return null;
        }
        UniongroupMemberDetail detail = findUniongroupMemberDetail(memberDetail);
        if (detail == null)
            return null;
        PunchRule pr = punchProvider.getPunchruleByPunchOrgId(detail.getGroupId());
        return pr;
    }

    private PunchRule getPunchRuleByDetailId(Integer namespaceId, String ownerType, Long ownerId, Long detailId) {
        Integer currentVersion = getPunchGroupCurrentVersion(ownerId);
        UniongroupMemberDetail detail = this.uniongroupConfigureProvider.findUniongroupMemberDetailByDetailId(namespaceId,
                detailId, UniongroupType.PUNCHGROUP.getCode(), currentVersion);
        if (null == detail)
            return null;
        PunchRule pr = punchProvider.getPunchruleByPunchOrgId(detail.getGroupId());
        return pr;
    }

    public UniongroupMemberDetail findUserMemberDetail(Long userId, Long organizationId) {
        List<OrganizationMember> orgMembers = organizationProvider.findOrganizationMembersByOrgIdAndUId(userId, organizationId);
        if (null == orgMembers || orgMembers.size() == 0)
            return null;
        Integer currentVersion = getPunchGroupCurrentVersion(organizationId);
        UniongroupMemberDetail detail = this.uniongroupConfigureProvider.findUniongroupMemberDetailByDetailId(orgMembers.get(0).getNamespaceId(),
                orgMembers.get(0).getDetailId(), UniongroupType.PUNCHGROUP.getCode(), currentVersion);
        return detail;
    }

    public UniongroupMemberDetail findUniongroupMemberDetail(OrganizationMemberDetails memberDetail) {
        if (memberDetail == null)
            return null;
        Integer currentVersion = getPunchGroupCurrentVersion(memberDetail.getOrganizationId());
        UniongroupMemberDetail detail = this.uniongroupConfigureProvider.findUniongroupMemberDetailByDetailId(memberDetail.getNamespaceId(), memberDetail.getId(), UniongroupType.PUNCHGROUP.getCode(), currentVersion);
        return detail;
    }

    //ownerType为组织，ownerId为组织id
    @Override
    public ApprovalRule getApprovalRule(String ownerType, Long ownerId, Long userId) {
        //如果有个人规则就返回个人规则
        PunchRuleOwnerMap map = this.punchProvider.getPunchRuleOwnerMapByOwnerAndTarget(ownerType, ownerId, PunchOwnerType.User.getCode(), userId);
        if (null == map || map.getReviewRuleId() == null) {
            //没有个人规则,向上递归找部门规则
            if (!ownerType.equals(PunchOwnerType.ORGANIZATION.getCode()))
                return null;
            //加循环限制
            int loopMax = 10;
            OrganizationDTO deptDTO = findUserDepartment(userId, ownerId);
            Organization dept = ConvertHelper.convert(deptDTO, Organization.class);
            map = getPunchRule(ownerId, dept, loopMax);
        }
        return approvalRuleProvider.findApprovalRuleById(map.getReviewRuleId());
    }


    /**
     * 打卡2.0 的考勤统计月报
     */
    @Override
    public ListPunchCountCommandResponse listPunchCount(ListPunchCountCommand cmd) {
        if (cmd.getOwnerId() == null || cmd.getOwnerType() == null) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid owner type or  Id parameter in the command");
        }
        ListPunchCountCommandResponse response = new ListPunchCountCommandResponse();
        response.setPunchCountList(new ArrayList<>());
        List<PunchCountDTO> punchCountDTOList = new ArrayList<PunchCountDTO>();

        Organization org = this.checkOrganization(cmd.getOwnerId());
        Long organizationId = getTopEnterpriseId(cmd.getOwnerId());

        //2017-8-2 加入了时间区间
        //2018-05-21 加入了reportId
        List<String> months = new ArrayList<>();
        if (null != cmd.getMonthReportId()) {
            PunchMonthReport report = punchMonthReportProvider.findPunchMonthReportById(cmd.getMonthReportId());
            if (null != report) {
                months.add(report.getPunchMonth());
                //为了防止redis和数据库同步问题,用getMonthReportProcess方法拿process;
                response.setProcess(getMonthReportProcessByRedis(report));
                response.setErrorInfo(report.getErrorInfo());
                response.setStatus(report.getStatus());
                response.setFilerName(report.getFilerName());
                response.setFileTime(report.getFileTime());
                if (null != report.getUpdateTime()) {
                    response.setUpdateTime(report.getUpdateTime().getTime());
                }
                response.setPunchMemberNumber(report.getPunchMemberNumber());
            }
        } else if (null != cmd.getStartDay()) {
            //这里早晚注释掉,4.2版本上线吼就没用了
            if (cmd.getStartDay() > cmd.getEndDay()) {
                LOGGER.error("Invalid  parameter in the command : startDay > end day");
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                        "Invalid  parameter in the command : startDay > end day");
            }
            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            start.setTime(new Date(cmd.getStartDay()));
            end.setTime(new Date(cmd.getEndDay()));
            while (start.before(end)) {
                months.add(monthSF.get().format(start.getTime()));
                start.add(Calendar.MONTH, 1);
            }
        } else if (null != cmd.getMonth()) {
            months.add(cmd.getMonth());
        } else {
        }
        if (months.size() == 0) {
            return response;
        }
        int pageSize = getPageSize(configurationProvider, cmd.getPageSize());

        //注释掉直接查询公司人员的部分,改为还是查询现有考勤月统计数据
        List<OrganizationMemberDetails> members = listMembers(organizationId, cmd.getOwnerId().equals(organizationId) ? null : cmd.getOwnerId(), months.get(0), Integer.MAX_VALUE - 1, null, cmd.getUserName());
        if (members == null || members.size() == 0) {
            return response;
        }

        List<Long> detailIds = new ArrayList<>();
        for (OrganizationMemberDetails member : members) {
            detailIds.add(member.getId());
        }

        if (null == detailIds || detailIds.size() == 0)
            return response;
        //分页查询
        if (cmd.getPageAnchor() == null)
            cmd.setPageAnchor(0L);
        List<PunchStatistic> results = this.punchProvider.queryPunchStatistics(cmd.getOwnerType(), organizationId, months, cmd.getExceptionStatus(), detailIds, cmd.getPageAnchor().intValue(), pageSize + 1);
        if (CollectionUtils.isEmpty(results)) {
            return response;
        }
        Long nextPageAnchor = null;
        if (results.size() > pageSize) {
            nextPageAnchor = Long.valueOf(cmd.getPageAnchor() + pageSize);
            results.remove(results.size() - 1);
        }

        response.setExtColumns(new ArrayList<>());
        ListApprovalCategoryCommand listApprovalCategoryCommand = new ListApprovalCategoryCommand();
        listApprovalCategoryCommand.setNamespaceId(org.getNamespaceId());
        listApprovalCategoryCommand.setOwnerType(ApprovalOwnerType.ORGANIZATION.getCode());
        listApprovalCategoryCommand.setOwnerId(organizationId);
        listApprovalCategoryCommand.setApprovalType(ApprovalType.ABSENCE.getCode());
        List<ApprovalCategoryDTO> categories = approvalService.initAndListApprovalCategory(listApprovalCategoryCommand).getCategoryList();

        if (null != categories) {
            for (ApprovalCategoryDTO category : categories) {
                response.getExtColumns().add(category.getCategoryName());
            }
        }
        response.setDateList(new ArrayList<>());
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(socialSecurityService.getTheFirstDate(months.get(0)));
        setCalendarMonthBegin(start);
        end.setTime(start.getTime());
        end.add(Calendar.MONTH, 1);
        Calendar today = Calendar.getInstance();
        setCalendarDateBegin(today);
        while (start.before(end)) {
            response.getDateList().add(dayStatusSF.get().format(start.getTime()));
            start.add(Calendar.DAY_OF_MONTH, 1);
        }

        List<Long> absenceUserIdList = new ArrayList<>();

        for (PunchStatistic statistic : results) {
            if (null != statistic) {
                PunchCountDTO dto = ConvertHelper.convert(statistic, PunchCountDTO.class);

                if (null != statistic.getStatusList()) {
                    dto.setStatusList(JSON.parseArray(statistic.getStatusList(), DayStatusDTO.class));
                }

                if (dto.getExceptionDayCount() == null) {
                    dto.setExceptionDayCount((int) ((dto.getWorkDayCount() == null ? 0 : dto.getWorkDayCount()) - (dto.getWorkCount() == null ? 0 : dto.getWorkCount())));
                    if (dto.getExceptionDayCount() < 0) {
                        dto.setExceptionDayCount(0);
                    }
                }
                String locale = UserContext.current().getUser() == null ? PunchConstants.locale : UserContext.current().getUser().getLocale();
                String dayUnit = localeStringService.getLocalizedString(ApprovalServiceConstants.SCOPE, String.valueOf(ApprovalServiceConstants.TIME_UNIT_OF_DAY), locale, "天");
                String hourUnit = localeStringService.getLocalizedString(ApprovalServiceConstants.SCOPE, String.valueOf(ApprovalServiceConstants.TIME_UNIT_OF_HOUR), locale, "小时");
                String minuteUnit = localeStringService.getLocalizedString(ApprovalServiceConstants.SCOPE, String.valueOf(ApprovalServiceConstants.TIME_UNIT_OF_MINUTE), locale, "分钟");
                if (null != dto.getUnpunchCount()) {
                    dto.setUnpunchCount(new BigDecimal(dto.getUnpunchCount()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                }
                if (null != statistic.getBelateTime()) {
                    dto.setBelateTime(PunchDayParseUtils.parseHourMinuteDisplayStringZeroWithoutUnit(statistic.getBelateTime(), hourUnit, minuteUnit));
                }
                if (null != statistic.getLeaveEarlyTime()) {
                    dto.setLeaveEarlyTime(PunchDayParseUtils.parseHourMinuteDisplayStringZeroWithoutUnit(statistic.getLeaveEarlyTime(), hourUnit, minuteUnit));
                }
                dto.setOvertimeTotalWorkdayDisplay(PunchDayParseUtils.parseHourMinuteDisplayStringZeroWithoutUnit(statistic.getOvertimeTotalWorkday(), hourUnit, minuteUnit));
                dto.setOvertimeTotalRestdayDisplay(PunchDayParseUtils.parseHourMinuteDisplayStringZeroWithoutUnit(statistic.getOvertimeTotalRestday(), hourUnit, minuteUnit));
                dto.setOvertimeTotalLegalHolidayDisplay(PunchDayParseUtils.parseHourMinuteDisplayStringZeroWithoutUnit(statistic.getOvertimeTotalLegalHoliday(), hourUnit, minuteUnit));

                punchCountDTOList.add(dto);
                List<ExtDTO> extDTOs = new ArrayList<>();
                if (statistic.getUserId() != null && statistic.getUserId() > 0) {
                    extDTOs = punchProvider.listAskForLeaveExtDTOs(statistic.getUserId(), statistic.getOwnerType(), statistic.getOwnerId(), statistic.getPunchMonth(), getApprovalCategoryIdNameMap(categories));
                }
                if (categories != null && CollectionUtils.isEmpty(extDTOs)) {
                    for (ApprovalCategoryDTO category : categories) {
                        extDTOs.add(new ExtDTO(category.getCategoryName(), "0"));
                    }
                }

                BigDecimal extTotal = BigDecimal.ZERO;
                if (!CollectionUtils.isEmpty(extDTOs)) {
                    for (ExtDTO e : extDTOs) {
                        e.setTimeCountDisplay(PunchDayParseUtils.parseDayTimeDisplayStringZeroWithoutUnit(new BigDecimal(e.getTimeCount()).doubleValue(), dayUnit, hourUnit));
                        extTotal = extTotal.add(new BigDecimal(e.getTimeCount()));
                    }
                }
                dto.setExts(extDTOs);
                dto.setExtTotal(extTotal.doubleValue());
                dto.setExtTotalDisplay(PunchDayParseUtils.parseDayTimeDisplayStringZeroWithoutUnit(extTotal.doubleValue(), dayUnit, hourUnit));
                absenceUserIdList.add(statistic.getUserId());
            }
        }
        response.setNextPageAnchor(nextPageAnchor);
        response.setPunchCountList(punchCountDTOList);
        return response;
    }

    private Map<Long, String> getApprovalCategoryIdNameMap(List<ApprovalCategoryDTO> approvalCategories) {
        if (CollectionUtils.isEmpty(approvalCategories)) {
            return Collections.emptyMap();
        }
        Map<Long, String> idNameMap = new HashMap<>();
        approvalCategories.forEach(approvalCategory -> {
            idNameMap.put(approvalCategory.getId(), approvalCategory.getCategoryName());
            if (approvalCategory.getOriginId() != null) {
                idNameMap.put(approvalCategory.getOriginId(), approvalCategory.getCategoryName());
            }
        });
        return idNameMap;
    }

    public List<OrganizationMember> listDptOrganizationMembers(Organization org, Long ownerId, String userName, Byte includeSubDpt) {
        //找到所有子部门 下面的用户

        List<OrganizationMember> organizationMembers = null;
        if (null == includeSubDpt || includeSubDpt.equals(NormalFlag.YES.getCode())) {

            List<Organization> orgs =  findSubDepartments(org);
            List<Long> orgIds = new ArrayList<Long>();
            orgIds.add(org.getId());
            for (Organization o : orgs) {
                orgIds.add(o.getId());
            }
            CrossShardListingLocator locator = new CrossShardListingLocator();
            organizationMembers = this.organizationProvider.listOrganizationPersonnels(userName, orgIds,
                    OrganizationMemberStatus.ACTIVE.getCode(), null, locator, Integer.MAX_VALUE - 1);

            LOGGER.debug("members  : " + organizationMembers.size());
        } else {
            org.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
            organizationMembers = this.organizationProvider.listOrganizationPersonnels(org.getNamespaceId(), userName, org, null,
                    null, null, Integer.MAX_VALUE - 1);
        }
        return organizationMembers;
    }


    @Override
    public List<Long> listDptUserIds(Organization org, Long ownerId, String userName, Byte includeSubDpt) {
        //找到所有子部门 下面的用户
    	List<OrganizationMember> organizationMembers =  listDptOrganizationMembers(org,ownerId,userName,includeSubDpt);

        if (null == organizationMembers)
            return null;
        List<Long> userIds = new ArrayList<Long>();
        for (OrganizationMember member : organizationMembers) {
            if (member.getTargetType() != null && member.getTargetType().equals(OrganizationMemberTargetType.USER.getCode()))
                userIds.add(member.getTargetId());
        }

        LOGGER.debug("userIds  : " + StringHelper.toJsonString(userIds));
        return userIds;
    }

    @Override
    public OutputStream getPunchDetailsOutputStream(Long startDay, Long endDay, Byte exceptionStatus, String userName, String ownerType, Long ownerId, Long taskId, Long userId) {

        ListPunchDetailsCommand cmd = new ListPunchDetailsCommand();
        cmd.setPageSize(PunchConstants.PAGE_SIZE_AT_A_TIME);
        cmd.setStartDay(startDay);
        cmd.setEndDay(endDay);
        cmd.setExceptionStatus(exceptionStatus);
        cmd.setUserName(userName);
        cmd.setOwnerId(ownerId);
        cmd.setOwnerType(ownerType);
        cmd.setUserId(userId);
        taskService.updateTaskProcess(taskId, 2);
        Workbook workbook = createPunchDetailsBook(cmd, taskId);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            workbook.write(out);
        } catch (IOException e) {
            LOGGER.error("something woring with build output stream");
            e.printStackTrace();
        }
        return out;

    }

    @Override
    public HttpServletResponse exportPunchDetails(ListPunchDetailsCommand cmd, HttpServletResponse response) {
        // TODO: 2018/1/2 导出
        // TODO 导出
        Map<String, Object> params = new HashMap();

        //如果是null的话会被传成“null”
        params.put("ownerType", cmd.getOwnerType());
        params.put("ownerId", cmd.getOwnerId());
        params.put("startDay", cmd.getStartDay());
        params.put("endDay", cmd.getEndDay());
        params.put("userId", cmd.getUserId());
        params.put("exceptionStatus", cmd.getExceptionStatus());
        params.put("userName", cmd.getUserName());
        params.put("reportType", "exportPunchDetails");
        String fileName = "";
        if (null != cmd.getUserId()) {
            Calendar endDay = Calendar.getInstance();
            endDay.add(Calendar.DAY_OF_MONTH, -1);
            if (endDay.after(new Date(cmd.getEndDay()))) {
                endDay.setTimeInMillis(cmd.getEndDay());
            }
            fileName = String.format("个人按月统计报表_%s到%s.xlsx", DateUtil.dateToStr(new Date(cmd.getStartDay()), DateUtil.NO_SLASH)
                    , DateUtil.dateToStr(endDay.getTime(), DateUtil.NO_SLASH));
        } else {
            fileName = String.format("按日统计报表_%s.xlsx", DateUtil.dateToStr(new Date(), DateUtil.NO_SLASH));
        }

        taskService.createTask(fileName, TaskType.FILEDOWNLOAD.getCode(), PunchExportTaskHandler.class, params, TaskRepeatFlag.REPEAT.getCode(), new Date());
        return response;
    }

    private Workbook createPunchDetailsBook(ListPunchDetailsCommand cmd, Long taskId) {
        XSSFWorkbook wb = new XSSFWorkbook();
        createPunchDetailsBookSheet(System.currentTimeMillis(), cmd, taskId, wb);
        return wb;
    }

    private void createPunchDetailsBookSheet(Long updateTime, ListPunchDetailsCommand listPunchDetailsCommand, Long taskId, XSSFWorkbook wb) {

        XSSFSheet sheet = wb.createSheet("每日统计");

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 14));
        XSSFCellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 20);
        font.setFontName("Courier New");

        style.setFont(font);

        XSSFCellStyle titleStyle = wb.createCellStyle();
        titleStyle.setFont(font);
        titleStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);

        //  创建标题
        XSSFRow rowTitle = sheet.createRow(0);
        rowTitle.createCell(0).setCellValue("每日汇总报表:" + monthChineseSF.get().format(new Date(listPunchDetailsCommand.getStartDay())));
        rowTitle.setRowStyle(titleStyle);
        //副标题

        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 14));
        XSSFCellStyle style1 = wb.createCellStyle();
        Font font1 = wb.createFont();

        font1.setFontHeightInPoints((short) 20);
        font1.setFontName("Courier New");

        style1.setFont(font1);

        XSSFCellStyle titleStyle1 = wb.createCellStyle();

        XSSFRow rowReminder = sheet.createRow(1);
        rowReminder.createCell(0).setCellValue("数据更新时间:" + datetimeSF.get().format(new Date(updateTime)) + ", 报表生成时间: " + datetimeSF.get().format(DateHelper.currentGMTTime()));
        rowReminder.setRowStyle(titleStyle1);

        this.createPunchDetailsBookSheetHead(sheet);
        sheet.createFreezePane(2, 3, 3, 4);

        // 分页填充excel表数据
        fullPunchDetailsBookRowByPage(listPunchDetailsCommand, taskId, sheet);
    }

    /**
     * 分页填充excel表数据
     */
    private void fullPunchDetailsBookRowByPage(ListPunchDetailsCommand listPunchDetailsCommand, Long taskId, XSSFSheet sheet) {
        listPunchDetailsCommand.setPageSize(PunchConstants.PAGE_SIZE_AT_A_TIME);
        listPunchDetailsCommand.setPageAnchor(null);
        ListPunchDetailsResponse listPunchDetailsResponse = listPunchDetails(listPunchDetailsCommand);
        boolean process = listPunchDetailsResponse != null && CollectionUtils.isNotEmpty(listPunchDetailsResponse.getPunchDayDetails());
        int num = 0;
        while (process) {
            List<PunchDayDetailDTO> punchDayDetails = listPunchDetailsResponse.getPunchDayDetails();
            for (PunchDayDetailDTO dto : punchDayDetails) {
                this.setNewPunchDetailsBookRow(sheet, dto);
                if (taskId != null && ++num % 100 == 0) {
                    taskService.updateTaskProcess(taskId, 30 + Math.min(30, num / 100));
                }
            }
            // 手动clear数据引用，提前结束数据的生命周期，以便GC适时回收
            punchDayDetails.clear();
            if (listPunchDetailsResponse.getNextPageAnchor() != null && listPunchDetailsResponse.getNextPageAnchor() > 0) {
                listPunchDetailsCommand.setPageAnchor(listPunchDetailsResponse.getNextPageAnchor());
                listPunchDetailsResponse = listPunchDetails(listPunchDetailsCommand);
                process = listPunchDetailsResponse != null && CollectionUtils.isNotEmpty(listPunchDetailsResponse.getPunchDayDetails());
            } else {
                process = false;
            }
        }
        taskService.updateTaskProcess(taskId, 60);
    }

    private String convertTimeLongToString(Long timeLong) {
        if (null == timeLong)
            return "";
        else {
            return timeSF.get().format(convertTime(timeLong));
        }
    }


    @Override
    public ListPunchMonthLogsResponse listPunchMonthLogs(ListPunchMonthLogsCommand cmd) {
        if (null == cmd.getOwnerId() || null == cmd.getOwnerType()) {
            LOGGER.error("Invalid owner type or  Id parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid owner type or  Id parameter in the command");
        }
        long daylogUseTime = 0L;

        ListPunchMonthLogsResponse response = new ListPunchMonthLogsResponse();
        PunchOwnerType ownerType = PunchOwnerType.fromCode(cmd.getOwnerType());
        if (PunchOwnerType.ORGANIZATION.equals(ownerType)) {
            //找到所有子部门 下面的用户
//			Organization org = this.checkOrganization(cmd.getOwnerId());
            List<String> groupTypeList = new ArrayList<String>();
            groupTypeList.add(OrganizationGroupType.ENTERPRISE.getCode());
            groupTypeList.add(OrganizationGroupType.DEPARTMENT.getCode());
            List<OrganizationMemberDTO> organizationMembers = this.organizationService.listAllChildOrganizationPersonnel
                    (cmd.getOwnerId(), groupTypeList, cmd.getUserName());
            if (null == organizationMembers)
                return response;
            response.setUserLogs(new ArrayList<UserMonthLogsDTO>());
            //取查询月的第一天和最后一天
            Calendar monthBegin = Calendar.getInstance();
            try {
                monthBegin.setTime(monthSF.get().parse(cmd.getPunchMonth()));
                //月初
                monthBegin.set(Calendar.DAY_OF_MONTH, 1);
                Calendar monthEnd = Calendar.getInstance();
                if (!monthSF.get().format(monthEnd.getTime()).equals(cmd.getPunchMonth())) {
                    monthEnd.setTime(monthSF.get().parse(cmd.getPunchMonth()));
                    //月末
                    monthEnd.set(Calendar.DAY_OF_MONTH, monthEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
                }
                for (OrganizationMemberDTO member : organizationMembers) {
                    if (null == member.getTargetType())
                        continue;
                    UserMonthLogsDTO userMonthLogsDTO = new UserMonthLogsDTO();
                    Organization dept = this.organizationProvider.findOrganizationById(member.getGroupId());
                    if (null == dept)
                        dept = this.organizationProvider.findOrganizationById(member.getOrganizationId());
                    userMonthLogsDTO.setDeptName(dept.getName());
                    userMonthLogsDTO.setUserName(member.getContactName());
                    long beginDayLogTime = DateHelper.currentGMTTime().getTime();
                    List<PunchDayLog> punchDayLogs = punchProvider.listPunchDayLogs(member.getTargetId(), member.getOrganizationId(), dateSF.get().format(monthBegin.getTime()),
                            dateSF.get().format(monthEnd.getTime()));
                    long endDayLogTime = DateHelper.currentGMTTime().getTime();
                    daylogUseTime += endDayLogTime - beginDayLogTime;
                    if (null == punchDayLogs || punchDayLogs.isEmpty())
                        continue;
                    userMonthLogsDTO.setPunchLogsDayList(new ArrayList<PunchLogsDay>());
                    ExceptionStatus exceptionStatus = ExceptionStatus.NORMAL;
                    userMonthLogsDTO.setUserStatus(PunchUserStatus.NORMAL.getCode());
                    for (PunchDayLog dayLog : punchDayLogs) {
                        if (null != dayLog.getStatus()) {
                            if (dayLog.getStatus().equals(PunchStatus.NONENTRY.getCode()))
                                userMonthLogsDTO.setUserStatus(PunchUserStatus.NONENTRY.getCode());
                            if (dayLog.getStatus().equals(PunchStatus.RESIGNED.getCode()))
                                userMonthLogsDTO.setUserStatus(PunchUserStatus.RESIGNED.getCode());
                        }
                        PunchLogsDay pdl = ConvertHelper.convert(dayLog, PunchLogsDay.class);
                        Calendar logDay = Calendar.getInstance();
                        logDay.setTime(dayLog.getPunchDate());
                        pdl.setPunchDay(String.valueOf(logDay.get(Calendar.DAY_OF_MONTH)));
                        pdl.setWorkTime(convertTimeToGMTMillisecond(dayLog.getWorkTime()));
                        pdl.setPunchStatus(dayLog.getStatus());
                        pdl.setAfternoonPunchStatus(dayLog.getAfternoonStatus());
                        pdl.setMorningPunchStatus(dayLog.getMorningStatus());
                        //TODO: 对于请假
                        pdl.setStatuString(processStatus(dayLog.getSplitDateTime(), dayLog.getStatusList(), null));
                        userMonthLogsDTO.getPunchLogsDayList().add(pdl);
                        if (dayLog.getExceptionStatus() != null && ExceptionStatus.EXCEPTION.equals(ExceptionStatus.fromCode(dayLog.getExceptionStatus()))) {
                            exceptionStatus = ExceptionStatus.EXCEPTION;
                        }

                    }
                    if (cmd.getExceptionStatus() != null && !exceptionStatus.equals(ExceptionStatus.fromCode(cmd.getExceptionStatus())))
                        continue;
                    response.getUserLogs().add(userMonthLogsDTO);
                }

            } catch (ParseException e) {
                LOGGER.error("ParseException : punch month : " + cmd.getPunchMonth());

                throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
                        ErrorCodes.ERROR_INVALID_PARAMETER,
                        "ParseException : punch month INVALID :  " + cmd.getPunchMonth());
            }
            LOGGER.debug("daylog use  :" + daylogUseTime + " ms");
        }
        return response;
    }

    private void setNewPunchDetailsBookRow(Sheet sheet, PunchDayDetailDTO dto) {
        CellStyle style = sheet.getWorkbook().createCellStyle();
        style.setWrapText(true);
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        int i = -1;
        row.createCell(++i).setCellValue(dateSF.get().format(new Date(dto.getPunchDate())));
        row.createCell(++i).setCellValue(dto.getUserName());
        row.createCell(++i).setCellValue(dto.getDeptName());
        row.createCell(++i).setCellValue(dto.getPunchOrgName());
        // 使单元格支持换行符
        Cell cell = row.createCell(++i);
        cell.setCellStyle(style);
        cell.setCellValue(buildWorkTimeShowString(dto.getTimeIntervals()));
        row.createCell(++i).setCellValue((dto.getArriveTime() == null) ? "" : timeSF.get().format(convertTime(dto.getArriveTime())));
        row.createCell(++i).setCellValue((dto.getLeaveTime() == null) ? "" : timeSF.get().format(convertTime(dto.getLeaveTime())));
        row.createCell(++i).setCellValue(String.valueOf(dto.getPunchCount()));
        row.createCell(++i).setCellValue(convertTimeLongToString(dto.getWorkTime()));
        row.createCell(++i).setCellValue(dto.getOvertimeTotalWorkdayDisplay());
        row.createCell(++i).setCellValue(dto.getOvertimeTotalRestdayDisplay());
        row.createCell(++i).setCellValue(dto.getOvertimeTotalLegalHolidayDisplay());
        if (null != dto.getApprovalRecords()) {
            StringBuilder sb = new StringBuilder();
            for (EnterpriseApprovalRecordDTO record : dto.getApprovalRecords()) {
                sb.append(record.getApprovalNo());
            }
            row.createCell(++i).setCellValue(sb.toString());
        } else {
            row.createCell(++i).setCellValue("");
        }
        row.createCell(++i).setCellValue(dto.getStatuString());
        row.createCell(++i).setCellValue(dto.getApprovalStatuString());
    }

    private String buildWorkTimeShowString(List<PunchTimeIntervalDTO> timeIntervals) {
        if (CollectionUtils.isEmpty(timeIntervals)) {
            return "休息";
        }
        StringBuilder s = new StringBuilder();
        for (PunchTimeIntervalDTO dto : timeIntervals) {
            s.append(timeSF.get().format(convertTime(dto.getArriveTime())));
            s.append("-");
            s.append(timeSF.get().format(convertTime(dto.getLeaveTime())));
            s.append("\n");
        }
        return s.substring(0, s.length() - 1);
    }

    /**
     * 刷新某公司某一段时间的所有打卡day logs
     */
    @Override
    public void refreshPunchDayLogs(ListPunchDetailsCommand cmd) {

        Long companyId = getTopEnterpriseId(cmd.getOwnerId());

        Organization org = this.checkOrganization(cmd.getOwnerId());
        List<Long> userIds = listDptUserIds(org, cmd.getOwnerId(), cmd.getUserName(), (byte) 1);
        for (Long userId : userIds) {
            UserIdentifier userIdentifier = this.userProvider.findClaimedIdentifierByOwnerAndType(userId, IdentifierType.MOBILE.getCode());
            if (null == userIdentifier) {
                continue;
            }
            refreshPunchDayLog(userId, companyId, cmd.getStartDay(), cmd.getEndDay());
        }
    }


    private void createPunchDetailsBookSheetHead(Sheet sheet) {
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        int i = -1;
        row.createCell(++i).setCellValue("时间");
        row.createCell(++i).setCellValue("姓名");
        row.createCell(++i).setCellValue("部门");
        row.createCell(++i).setCellValue("所属规则");
        row.createCell(++i).setCellValue("上下班时间");
        row.createCell(++i).setCellValue("最早打卡");
        row.createCell(++i).setCellValue("最晚打卡");
        row.createCell(++i).setCellValue("打卡次数");
        row.createCell(++i).setCellValue("工作时长");
        row.createCell(++i).setCellValue("工作日加班");
        row.createCell(++i).setCellValue("休息日加班");
        row.createCell(++i).setCellValue("节假日加班");
        row.createCell(++i).setCellValue("审批单");
        row.createCell(++i).setCellValue("状态");
        row.createCell(++i).setCellValue("校正状态");
    }

    private Organization checkOrganization(Long orgId) {
        Organization org = organizationProvider.findOrganizationById(orgId);
        if (org == null) {
            LOGGER.error("Unable to find the organization.organizationId=" + orgId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Unable to find the organization.");
        }
        return org;
    }

    @Override
    public void refreshPunchDayLog(Long userId, Long companyId, Long startDay, Long endDay) {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTimeInMillis(startDay);
        end.setTimeInMillis(endDay);
        refreshPunchDayLog(userId, companyId, start, end);
    }

    @Override
    public void refreshPunchDayLog(Long userId, Long companyId, Date startDay, Date endDay) {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(startDay);
        end.setTime(endDay);
        refreshPunchDayLog(userId, companyId, start, end);
    }

    private void refreshPunchDayLog(Long userId, Long companyId, Calendar start, Calendar end) {
        OrganizationMemberDetails memberDetail = organizationProvider.findOrganizationMemberDetailsByTargetIdAndOrgId(userId, companyId);
        Date today = PunchDateUtils.getDateBeginDate(new Date());
        while (!start.after(end)) {
            try {
                if (PunchDateUtils.getDateBeginDate(start.getTime()).after(today)) {
                    // 未来的日期不刷新日报
                    return;
                }
                PunchDayLog newPunchDayLog = new PunchDayLog();
                this.coordinationProvider.getNamedLock(CoordinationLocks.CREATE_PUNCH_LOG.getCode() + userId).enter(() -> {
                    PunchDayLog punchDayLog = punchProvider.getDayPunchLogByDateAndUserId(userId,
                            companyId, dateSF.get().format(start.getTime()));

                    LOGGER.debug("refresh day log : userid =[" + userId
                            + "] day is [" + dateSF.get().format(start.getTime()) + "] enddate ["
                            + dateSF.get().format(end.getTime()) + "]");

                    refreshPunchDayLog(memberDetail, punchDayLog, start, newPunchDayLog);

                    return null;
                });

            } catch (Exception e) {
                LOGGER.error("refresh day log wrong  userId[" + userId + "],  day" + start.getTime(), e);
            }

            start.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    public static int getPageSize(ConfigurationProvider configProvider, Integer requestedPageSize) {
        if (requestedPageSize == null) {
            return configProvider.getIntValue("pagination.default.size", AppConstants.PAGINATION_DEFAULT_SIZE);
        }

        return requestedPageSize.intValue();
    }

    /**
     * 打卡2.0 的考勤详情
     */
    @Override
    public ListPunchDetailsResponse listPunchDetails(ListPunchDetailsCommand cmd) {
        if (cmd.getOwnerId() == null || cmd.getOwnerType() == null || cmd.getMonthReportId() == null) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid owner type or  Id parameter in the command");
        }
        PunchMonthReport report = punchMonthReportProvider.findPunchMonthReportById(cmd.getMonthReportId());
        if (report == null) {
            LOGGER.error("reportId = {} not found", cmd.getMonthReportId());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "resource not found");
        }
        cmd.setStartDay(socialSecurityService.getTheFirstDate(report.getPunchMonth()).getTime());
        cmd.setEndDay(socialSecurityService.getTheLastDate(report.getPunchMonth()).getTime());
        String startDay = dateSF.get().format(new Date(cmd.getStartDay()));
        String endDay = dateSF.get().format(new Date(cmd.getEndDay()));

        Organization selectedOrg = this.checkOrganization(cmd.getOwnerId());
        Long ownerId = getTopEnterpriseId(cmd.getOwnerId());
        //分页查询 由于用到多条件排序,所以使用pageOffset方式分页
        Integer pageOffset = 0;
        if (cmd.getPageAnchor() != null)
            pageOffset = cmd.getPageAnchor().intValue();
        int pageSize = getPageSize(configurationProvider, cmd.getPageSize());

        ListPunchDetailsResponse response = new ListPunchDetailsResponse();
        response.setPunchDayDetails(new ArrayList<>());

        List<Long> userIds = null;
        List<Long> detailIds = null;
        List<Long> deptIds = new ArrayList<>();
        if (cmd.getUserId() != null) {
            userIds = new ArrayList<>();
            userIds.add(cmd.getUserId());
        }
        if (StringUtils.isNotBlank(cmd.getUserName())) {
            List<OrganizationMemberDetails> members = organizationProvider.listOrganizationMemberDetails(ownerId, cmd.getUserName());
            if (CollectionUtils.isEmpty(members)) {
                return response;
            }
            detailIds = new ArrayList<>();
            for (OrganizationMemberDetails member : members) {
                detailIds.add(member.getId());
            }
        }
        if (PunchOwnerType.ORGANIZATION.equals(PunchOwnerType.fromCode(cmd.getOwnerType()))) {
            deptIds.add(cmd.getOwnerId());
            //找到所有子部门 下面的用户
            if (cmd.getIncludeSubDpt() == null || NormalFlag.YES == NormalFlag.fromCode(cmd.getIncludeSubDpt())) {
                List<Organization> orgs = findSubDepartments(selectedOrg);
                for (Organization o : orgs) {
                    deptIds.add(o.getId());
                }
            }
        }

        Long t1 = System.currentTimeMillis();
        List<PunchDayLog> results = punchProvider.listPunchDayLogs(userIds, ownerId, detailIds, deptIds, startDay, endDay, cmd.getArriveTimeCompareFlag(), convertTime(cmd.getArriveTime()),
                cmd.getLeaveTimeCompareFlag(), convertTime(cmd.getLeaveTime()), cmd.getWorkTimeCompareFlag(), convertTime(cmd.getWorkTime()), cmd.getExceptionStatus(), pageOffset, pageSize + 1);
        Long t2 = System.currentTimeMillis();

        LOGGER.debug("query punchDayLogs  " + t2 + "cost: " + (t2 - t1));

        if (CollectionUtils.isEmpty(results)) {
            return response;
        }

        if (results.size() == pageSize + 1) {
            results.remove(pageSize);
            response.setNextPageAnchor(Long.valueOf(pageOffset + pageSize));
        }

        Map<Long, Organization> cacheOrganizationMap = new HashMap<>();

        List<PunchExceptionRequest> exceptionRequests = punchProvider.listPunchExceptionRequestBetweenBeginAndEndTime(null, ownerId, new Timestamp(cmd.getStartDay()), new Timestamp(cmd.getEndDay()));
        if (exceptionRequests == null) {
            exceptionRequests = new ArrayList<>();
        }
        List<PunchExceptionRequest> requests2 = punchProvider.listpunchexceptionRequestByDate(null, ownerId, new java.sql.Date(cmd.getStartDay()), new java.sql.Date(cmd.getEndDay()));
        if (CollectionUtils.isNotEmpty(requests2)) {
            exceptionRequests.addAll(requests2);
        }
        Map<Long, OrganizationMemberDetails> memberDetailsMap = findOrganizationMemberDetailsToMap(results);
        try {
            for (PunchDayLog r : results) {
                if (r.getDetailId() == null) {
                    continue;
                }
                response.getPunchDayDetails().add(convertToPunchDayDetailDTO(memberDetailsMap.get(r.getDetailId()), r, cacheOrganizationMap, exceptionRequests));
            }
        } finally {
            // 该接口在调度任务中被调用，使用的是线程池，因此需要手工清理缓存数据
            idAndOrganizationMapCache.get().clear();
            detailIdAndDepartmentMapCache.get().clear();
        }

        Long t3 = System.currentTimeMillis();
        LOGGER.debug("processStat pdls   " + t3 + "cost: " + (t3 - t2));
        return response;
    }

    private Map<Long, OrganizationMemberDetails> findOrganizationMemberDetailsToMap(List<PunchDayLog> logs) {
        if (CollectionUtils.isEmpty(logs)) {
            return new HashMap<>();
        }
        Set<Long> queryDetailIds = new HashSet<>();
        for (PunchDayLog r : logs) {
            if (r.getDetailId() == null) {
                continue;
            }
            queryDetailIds.add(r.getDetailId());
        }
        List<OrganizationMemberDetails> memberDetailsList = organizationProvider.findDetailInfoListByIdIn(new ArrayList<>(queryDetailIds));
        if (CollectionUtils.isEmpty(memberDetailsList)) {
            return new HashMap<>();
        }
        Map<Long, OrganizationMemberDetails> result = new HashMap<>();
        for (OrganizationMemberDetails detail : memberDetailsList) {
            result.put(detail.getId(), detail);
        }
        return result;
    }

    private List<Organization> findSubDepartments(Organization org) {
        List<String> groupTypeList = new ArrayList<String>();
        groupTypeList.add(OrganizationGroupType.ENTERPRISE.getCode());
        groupTypeList.add(OrganizationGroupType.DEPARTMENT.getCode());
        groupTypeList.add(OrganizationGroupType.DIRECT_UNDER_ENTERPRISE.getCode());
        List<Organization> orgs = organizationProvider.listOrganizationByGroupTypes(org.getPath() + "/%", groupTypeList);
        return orgs;
    }

    String processStatus(Timestamp splitDate, String statuList, String approvalStatusList) {
        if (StringUtils.isEmpty(statuList) && StringUtils.isEmpty(approvalStatusList)) {
            return "";
        }
        String result = "";
        if (statuList.contains(PunchConstants.STATUS_SEPARATOR)) {
            String[] statulist = statuList.split(PunchConstants.STATUS_SEPARATOR);
            String[] approvalStatuArray = null;
            if (null != approvalStatusList) {
                approvalStatuArray = StringUtils.splitPreserveAllTokens(approvalStatusList, PunchConstants.STATUS_SEPARATOR);
            }
            int arrayLength = approvalStatuArray != null ? approvalStatuArray.length : statulist.length;
            for (int i = 0; i < arrayLength; i++) {
                Byte statusByte = null;
                if (null != statulist) {
                    if (statulist.length > i && statulist[i] != null) {
                        statusByte = Byte.valueOf(statulist[i]);
                    }
                }
                if (null != approvalStatuArray) {
                    if (approvalStatuArray.length > i && StringUtils.isNotBlank(approvalStatuArray[i])) {
                        statusByte = Byte.valueOf(approvalStatuArray[i]);
                    }
                }
                if (i == 0) {
                    result = statusToString(splitDate, statusByte);
                } else {
                    result = result + PunchConstants.STATUS_SEPARATOR + statusToString(splitDate, statusByte);
                }
            }
        } else {
            if (null != approvalStatusList && !StringUtils.isEmpty(approvalStatusList)) {
                result = statusToString(splitDate, Byte.valueOf(approvalStatusList));
            } else {
                result = statusToString(splitDate, Byte.valueOf(statuList));
            }
        }
        return result;
    }

    public PunchDayDetailDTO convertToPunchDayDetailDTO(OrganizationMemberDetails memberDetail, PunchDayLog punchDayLog, Map<Long, Organization> cacheOrganizationMap, List<PunchExceptionRequest> exceptionRequests) {
        PunchDayDetailDTO dto = ConvertHelper.convert(punchDayLog, PunchDayDetailDTO.class);
        dto.setPunchOrgName("未设置规则");

        dto.setStatuString(processStatus(punchDayLog.getSplitDateTime(), punchDayLog.getStatusList(), null));
        dto.setApprovalStatuString(processStatus(punchDayLog.getSplitDateTime(), punchDayLog.getStatusList(), punchDayLog.getApprovalStatusList()));
        if (punchDayLog.getPunchOrganizationId() != null) {
            Organization punchGroup = getOrganizationFromLocalCache(cacheOrganizationMap, punchDayLog.getPunchOrganizationId());
            if (punchGroup != null)
                dto.setPunchOrgName(punchGroup.getName());
        }

        PunchTimeRule ptr = (punchDayLog.getTimeRuleId() == null || punchDayLog.getTimeRuleId() == 0) ? null : punchProvider.getPunchTimeRuleById(punchDayLog.getTimeRuleId());
        if (null != ptr) {
            // 获取上下班时间
            dto.setTimeIntervals(punchBaseService.findPunchTimeIntervals(ptr));
            //当日+上班打卡时间-最早打卡时间 默认0
            Timestamp dayStart = new Timestamp(punchDayLog.getPunchDate().getTime() + (ptr.getStartEarlyTimeLong() == null ? 0L : ptr.getStartEarlyTimeLong()) - (ptr.getBeginPunchTime() == null ? 0L : ptr.getBeginPunchTime()));
            //当日+切分时间 默认一天
            Timestamp dayEnd = new Timestamp(punchDayLog.getPunchDate().getTime() + (ptr.getDaySplitTimeLong() == null ? PunchConstants.ONE_DAY_MS : ptr.getDaySplitTimeLong()));

            if (CollectionUtils.isNotEmpty(exceptionRequests)) {
                dto.setApprovalRecords(new ArrayList<>());
                for (PunchExceptionRequest request : exceptionRequests) {
                    if (request.getUserId().equals(punchDayLog.getUserId())) {
                        if (checkPunchDayInRequest(request, dayStart, dayEnd, punchDayLog.getPunchDate())) {
                            FlowCase flowCase = flowCaseProvider.getFlowCaseById(request.getRequestId());
                            if (null != flowCase) {
                                EnterpriseApprovalRecordDTO recordDTO = enterpriseApprovalService.convertEnterpriseApprovalRecordDTO(flowCase);
                                dto.getApprovalRecords().add(recordDTO);
                            }
                        }
                    }
                }
            }
        }

        if (null != punchDayLog.getArriveTime())
            dto.setArriveTime(convertTimeToGMTMillisecond(punchDayLog.getArriveTime()));

        if (null != punchDayLog.getLeaveTime())
            dto.setLeaveTime(convertTimeToGMTMillisecond(punchDayLog.getLeaveTime()));

        if (null != punchDayLog.getWorkTime())
            dto.setWorkTime(convertTimeToGMTMillisecond(punchDayLog.getWorkTime()));
        else
            dto.setWorkTime(0L);

        if (null != punchDayLog.getNoonLeaveTime())
            dto.setNoonLeaveTime(convertTimeToGMTMillisecond(punchDayLog.getNoonLeaveTime()));

        if (null != punchDayLog.getAfternoonArriveTime())
            dto.setAfternoonArriveTime(convertTimeToGMTMillisecond(punchDayLog.getAfternoonArriveTime()));
        if (null != punchDayLog.getPunchDate())
            dto.setPunchDate(punchDayLog.getPunchDate().getTime());

        dto.setPunchTimesPerDay(punchDayLog.getPunchTimesPerDay());

        if (null != memberDetail) {
            dto.setUserName(memberDetail.getContactName());
            Organization department = getDepartmentFromLocalCache(memberDetail.getId());
            if (department != null) {
                dto.setDeptName(department.getName());
                dto.setDeptId(department.getId());
            }
        }
        String locale = UserContext.current().getUser() == null ? PunchConstants.locale : UserContext.current().getUser().getLocale();
        String hourUnit = localeStringService.getLocalizedString(ApprovalServiceConstants.SCOPE, String.valueOf(ApprovalServiceConstants.TIME_UNIT_OF_HOUR), locale, "小时");
        String minuteUnit = localeStringService.getLocalizedString(ApprovalServiceConstants.SCOPE, String.valueOf(ApprovalServiceConstants.TIME_UNIT_OF_MINUTE), locale, "分钟");
        dto.setOvertimeTotalWorkdayDisplay(PunchDayParseUtils.parseHourMinuteDisplayStringZeroWithoutUnit(dto.getOvertimeTotalWorkday(), hourUnit, minuteUnit));
        dto.setOvertimeTotalRestdayDisplay(PunchDayParseUtils.parseHourMinuteDisplayStringZeroWithoutUnit(dto.getOvertimeTotalRestday(), hourUnit, minuteUnit));
        dto.setOvertimeTotalLegalHolidayDisplay(PunchDayParseUtils.parseHourMinuteDisplayStringZeroWithoutUnit(dto.getOvertimeTotalLegalHoliday(), hourUnit, minuteUnit));
        return dto;
    }

    private boolean checkPunchDayInRequest(PunchExceptionRequest request, Timestamp dayStart,
			Timestamp dayEnd, java.sql.Date punchDate) {
    	//如果是begin和end time 的话,在开始时间在今天结束前,结束时间在今天开始前的统计
		return (request.getBeginTime() != null && request.getEndTime() != null
                && !request.getBeginTime().after(dayEnd) && !request.getEndTime().before(dayStart))
                //如果是punchdate的话
                || (request.getPunchDate() != null && request.getPunchDate().equals(punchDate));
	}

    @Override
    public OutputStream getPunchStatisticsOutputStream(Long startDay, Long endDay, Byte exceptionStatus, String userName, String ownerType, Long ownerId, Long taskId, Long monthReportId) {
        ListPunchCountCommand cmd = new ListPunchCountCommand();
        cmd.setPageSize(Integer.MAX_VALUE - 1);
        cmd.setStartDay(startDay);
        cmd.setEndDay(endDay);
        cmd.setExceptionStatus(exceptionStatus);
        cmd.setUserName(userName);
        cmd.setOwnerId(ownerId);
        cmd.setOwnerType(ownerType);
        cmd.setMonthReportId(monthReportId);
        taskService.updateTaskProcess(taskId, 2);
        ListPunchCountCommandResponse resp = listPunchCount(cmd);
        taskService.updateTaskProcess(taskId, 20);
        Workbook wb = createPunchStatisticsBook(resp, cmd, taskId);
        taskService.updateTaskProcess(taskId, 100);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            wb.write(out);
        } catch (IOException e) {
            LOGGER.error("something woring with build output stream");
            e.printStackTrace();
        }
        return out;

    }


    /**
     * 打卡2.0 的考勤统计-按月统计
     */
    @Override
    public HttpServletResponse exportPunchStatistics(ListPunchCountCommand cmd, HttpServletResponse response) {
        Map<String, Object> params = new HashMap();
        if (null != cmd.getMonthReportId()) {
            PunchMonthReport report = punchMonthReportProvider.findPunchMonthReportById(cmd.getMonthReportId());
            cmd.setStartDay(socialSecurityService.getTheFirstDate(report.getPunchMonth()).getTime());
            cmd.setEndDay(socialSecurityService.getTheLastDate(report.getPunchMonth()).getTime());
        }
        //如果是null的话会被传成“null”
        params.put("ownerType", cmd.getOwnerType());
        params.put("ownerId", getTopEnterpriseId(cmd.getOwnerId()));
        params.put("startDay", cmd.getStartDay());
        params.put("endDay", cmd.getEndDay());
        params.put("monthReportId", cmd.getMonthReportId());
        params.put("exceptionStatus", cmd.getExceptionStatus());
//        params.put("userName", cmd.getUserName());
        params.put("reportType", "exportPunchStatistics");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
        String fileName = String.format("考勤报表_%s.xlsx", sdf.format(cmd.getStartDay()));

        taskService.createTask(fileName, TaskType.FILEDOWNLOAD.getCode(), PunchExportTaskHandler.class, params, TaskRepeatFlag.REPEAT.getCode(), new Date());
        return response;
    }

    @Override
    public void refreshMonthDayLogs(String month) {

        Calendar monthBegin = Calendar.getInstance();
        Calendar monthEnd = Calendar.getInstance();
        try {
            monthBegin.setTime(monthSF.get().parse(month));
            //月初
            monthBegin.set(Calendar.DAY_OF_MONTH, 1);
            if (!monthSF.get().format(monthEnd.getTime()).equals(month)) {
                monthEnd.setTime(monthSF.get().parse(month));
                //月末
                monthEnd.set(Calendar.DAY_OF_MONTH, monthEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
            }

            while (true) {

                List<Long> orgIds = this.punchProvider.queryPunchOrganizationsFromRules();
                for (Long orgId : orgIds) {

                    List<String> groupTypeList = new ArrayList<String>();
                    groupTypeList.add(OrganizationGroupType.ENTERPRISE.getCode());
                    groupTypeList.add(OrganizationGroupType.DEPARTMENT.getCode());
                    List<OrganizationMemberDTO> members = this.organizationService.listAllChildOrganizationPersonnel
                            (orgId, groupTypeList, null);
                    //循环刷所有员工
                    for (OrganizationMemberDTO member : members) {
                        if (member.getTargetType().equals(OrganizationMemberTargetType.USER.getCode()) && null != member.getTargetId()) {
                            try {
                                //刷新 daylog
                                OrganizationMemberDetails memberDetail = organizationProvider.findOrganizationMemberDetailsByTargetIdAndOrgId(member.getTargetId(), orgId);
                                this.refreshPunchDayLog(memberDetail, monthBegin);

                            } catch (Exception e) {
                                LOGGER.error("#####refresh day log error!! userid:[" + member.getTargetId()
                                        + "] organization id :[" + orgId + "] ");
                                LOGGER.error(e.getLocalizedMessage());

                                e.printStackTrace();
                            }
                        }
                    }
                }
                monthBegin.add(Calendar.DAY_OF_MONTH, 1);
                if (monthBegin.after(monthEnd)) {
                    return;
                }
            }
        } catch (ParseException e) {
            throw RuntimeErrorException
                    .errorWith(PunchServiceErrorCode.SCOPE,
                            PunchServiceErrorCode.ERROR_QUERY_YEAR_ERROR,
                            "there is something wrong with queryYear,please check again ");
        }

    }


    /**
     * 每天早上5点50,在前一天数据刷完了的时候,进行考勤修改次日生效的处理
     */
    @Scheduled(cron = "1 50 5 * * ?")
    @Override
    public void dayRefreshPunchGroupScheduled() {
        if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
            this.coordinationProvider.getNamedLock(CoordinationLocks.REFRESH_PUNCH_RULE.getCode()).enter(() -> {
                LOGGER.debug("dayRefreshPunchGroupScheduled BEGIN !!! ");
                if (UserContext.current().getUser() == null) {
                    User user = userProvider.findUserById(User.SYSTEM_UID);
                    UserContext.current().setUser(user);
                }
                Set<Long> orgIds = new HashSet<>();
                List<Byte> statusList = new ArrayList<>();
                statusList.add(PunchRuleStatus.MODIFYED.getCode());
                statusList.add(PunchRuleStatus.NEW.getCode());
                statusList.add(PunchRuleStatus.DELETING.getCode());
                List<PunchRule> punchRules = punchProvider.listPunchRulesByStatus(statusList);
                if (null != punchRules)
                    for (PunchRule pr : punchRules) {
                        try {
                            if (PunchRuleStatus.DELETING == PunchRuleStatus.fromCode(pr.getStatus())) {
                                deletepunchGroup(pr.getPunchOrganizationId(), pr.getOwnerId());
                            } else if (!pr.getStatus().equals(PunchRuleStatus.ACTIVE.getCode())) {
                                Organization org = organizationProvider.findOrganizationById(pr.getOwnerId());
                                //对ptr表,psd表的处理
                                processOvertimeRule2Active(pr);
                                processTimeRule2Active(pr);
                                pr.setStatus(PunchRuleStatus.ACTIVE.getCode());
                                punchProvider.updatePunchRule(pr);
                                orgIds.add(org.getId());
                            }
                        } catch (Exception e) {
                            LOGGER.error("dayRefreshPunchGroupScheduled error!!! pr id : " + pr.getId());
                        }
                    }
                //把uniongroup相关表version改为0
                for (Long orgId : orgIds) {
                    Integer versionId = 0;
                    try {
                        Organization org = organizationProvider.findOrganizationById(orgId);
                        UniongroupVersion unionGroupVersion = punchBaseService.getPunchGroupVersion(org.getId());
                        versionId = unionGroupVersion.getCurrentVersionCode() + 1;
                        unionGroupVersion.setCurrentVersionCode(versionId);
                        //把config版本复制一份新的,
                        uniongroupService.cloneGroupTypeDataToVersion(org.getNamespaceId(), org.getId(), UniongroupType.PUNCHGROUP.getCode(),
                                CONFIG_VERSION_CODE, unionGroupVersion.getCurrentVersionCode());

                        //更新当前版本到新的
                        uniongroupVersionProvider.updateUniongroupVersion(unionGroupVersion);
                    } catch (Exception e) {
                        LOGGER.error("dayRefreshPunchGroupScheduled error!!!+ org id : " + orgId + " current version : " + versionId);
                    }
                }
                // 初始化打卡提醒原始数据，并动态创建提醒的定时任务
                punchNotificationService.punchNotificationInitialize();
                LOGGER.debug("dayRefreshPunchGroupScheduled ---------- END ");
                return null;
            });
        }

    }

    private void processOvertimeRule2Active(PunchRule pr) {
        // 正常的删除
        deletePunchOvertimeRules(pr.getId(), PunchRuleStatus.ACTIVE.getCode());
        // 状态为次日更新的置为激活状态
        List<PunchOvertimeRule> punchOvertimeRules = punchProvider.findPunchOvertimeRulesByPunchRuleId(pr.getId(), pr.getStatus());
        if (CollectionUtils.isEmpty(punchOvertimeRules)) {
            return;
        }
        for (PunchOvertimeRule punchOvertimeRule : punchOvertimeRules) {
            punchOvertimeRule.setStatus(PunchRuleStatus.ACTIVE.getCode());
            punchProvider.updatePunchOvertimeRule(punchOvertimeRule);
        }
    }

    private void processTimeRule2Active(PunchRule pr) {
        //正常的删除
        deletePunchTimeRules(pr.getPunchOrganizationId(), PunchRuleStatus.ACTIVE.getCode());
        //别的置为正常
        List<PunchTimeRule> timeRules = punchProvider.listActivePunchTimeRuleByOwner(PunchOwnerType.ORGANIZATION.getCode(), pr.getPunchOrganizationId(), pr.getStatus());
        if (null != timeRules) {
            for (PunchTimeRule timeRule : timeRules) {
                timeRule.setStatus(PunchRuleStatus.ACTIVE.getCode());
                punchProvider.updatePunchTimeRule(timeRule);
            }
        }

        if (pr.getRuleType().equals(PunchRuleType.PAIBAN.getCode())) {
            //排班处理
            List<PunchScheduling> schedulings = punchSchedulingProvider.queryPunchSchedulings(pr.getId(), pr.getStatus());
            if (null != schedulings)
                for (PunchScheduling ps : schedulings) {
                    //删除现在active的,然后把自己变成active的
                    ps.setStatus(PunchRuleStatus.ACTIVE.getCode());
                    punchSchedulingProvider.deletePunchSchedulingByOwnerAndTarget(ps.getOwnerType(), ps.getOwnerId(),
                            ps.getTargetType(), ps.getTargetId(), ps.getRuleDate(), ps.getStatus());
                    punchSchedulingProvider.updatePunchScheduling(ps);
                }
        }
    }


    private static int refreshGap = 15;

    /**
     * !!! 注：考勤7.0后，该定时任务作废，由punchDayLogInitialize取而代之，每天定时全量初始化eh_punch_day_logs数据
     * 每15分轮询刷打卡记录
     * 1.刷punchdate为前一天的
     * 2.找timerule里分界点(分界点只会是0,15,30,45)在这一个15分钟内的(当前时间点取整-15分钟,当前时间点取整]
     * 3.找到规则映射的公司/部门/个人,然后精确到个人.刷前一天的记录.
     */
//    @Scheduled(cron = "1 0/15 * * * ?")
    @Override
    @Deprecated
    public void dayRefreshLogScheduled() {
        if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
            Date runDate = DateHelper.currentGMTTime();
            dayRefreshLogScheduled(runDate);
        }
    }

    /**
     * 该方法用于全量初始化用户的eh_punch_day_logs每日统计，包含有设置考勤规则和没有设置考勤规则的用户
     * 设置定时任务凌晨6点执行一次初始化，晚上22点二次执行（主要是retry的作用，防止6点的任务失败）
     */
    @Override
    @Scheduled(cron = "1 10 6,22 * * ?")
    public void punchDayLogInitialize() {
        if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
            this.coordinationProvider.getNamedLock(CoordinationLocks.PUNCH_DAY_LOG_INIT_SCHEDULE.getCode()).enter(() -> {
                PunchDayLogInitializeCommand cmd = new PunchDayLogInitializeCommand();
                cmd.setInitialDateTime(System.currentTimeMillis());
                punchDayLogInitialize(cmd);
                return null;
            });
        }
    }

    @Override
    public void punchDayLogInitialize(PunchDayLogInitializeCommand cmd) {
        // 通过UniongroupMemberDetails来查询有在使用考勤功能的公司 ( 即没有使用考勤功能的不进行初始化 )
        List<Long> organizationIds = uniongroupConfigureProvider.distinctEnterpriseIdsFromUniongroupMemberDetails();
        if (CollectionUtils.isEmpty(organizationIds)) {
            return;
        }
        Calendar punCalendar = Calendar.getInstance();
        punCalendar.setTimeInMillis(cmd.getInitialDateTime() == null ? System.currentTimeMillis() : cmd.getInitialDateTime());
        for (Long organizationId : organizationIds) {
            List<OrganizationMemberDetails> memberDetails = organizationProvider.queryOrganizationMemberDetails(new ListingLocator(), organizationId, (locator, query) -> {
                query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.EMPLOYEE_STATUS.ne(EmployeeStatus.DISMISSAL.getCode()));
                return query;
            });
            if (CollectionUtils.isEmpty(memberDetails)) {
                continue;
            }
            for (OrganizationMemberDetails detail : memberDetails) {
                punchDayLogInitialize(detail, organizationId, punCalendar);
            }
        }
    }

    @Override
    public void testDayRefreshLogs(Long runDate, Long orgId) {
        try {
            Calendar punCalendar = Calendar.getInstance();
            if(null != runDate){
            	punCalendar.setTimeInMillis(runDate);
            }
            List<OrganizationMemberDetails> memberDetails = organizationProvider.queryOrganizationMemberDetails(new ListingLocator(), orgId, (locator, query) -> {
                query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.EMPLOYEE_STATUS.ne(EmployeeStatus.DISMISSAL.getCode()));
                return query;
            });
            if (CollectionUtils.isEmpty(memberDetails)) {
                return;
            }
            //循环刷所有员工
            for (OrganizationMemberDetails memberDetail : memberDetails) { 
            	punchDayLogInitialize(memberDetail, orgId, punCalendar);
            } 
        } catch (Exception e) {

        }
//        dayRefreshLogScheduled(new Date(runDate));
    }
    @Deprecated
    public void dayRefreshLogScheduled(Date runDate) {

        coordinationProvider.getNamedLock(
                CoordinationLocks.PUNCH_DAY_SCHEDULE.getCode() + runDate.toString()).enter(
                () -> {

                    organizationMemberDetailsCacheDetailId.set(new HashMap<>());
                    timeRuleCache.set(new HashMap<>());
                    punchRuleCacheByUserId.set(new HashMap<>());
                    //刷新前一天的
                    Calendar punCalendar = Calendar.getInstance();
                    punCalendar.setTime(runDate);
                    Long timeLong = getTimeLong(punCalendar, null);
                    Calendar yesterday = Calendar.getInstance();
                    yesterday.setTime(runDate);
                    yesterday.add(Calendar.DAY_OF_MONTH, -1);
                    //找今天刷新的(当前时间点前15分钟 到当前时间点之间split的
                    List<PunchTimeRule> timeRules = punchProvider.listPunchTimeRulesBySplitTime(timeLong - refreshGap * 60 * 1000,
                            timeLong);
                    if (null == timeRules) {
                        timeRules = new ArrayList<>();
                    }
                    List<PunchTimeRule> timeRules2 = punchProvider.listPunchTimeRulesBySplitTime(timeLong - refreshGap * 60 * 1000
                            + PunchConstants.ONE_DAY_MS, timeLong + PunchConstants.ONE_DAY_MS);
                    if (null != timeRules2) {
                        timeRules.addAll(timeRules2);
                    }
                    Set<PunchRule> punchRules = new HashSet<>();
                    for (PunchTimeRule ptr : timeRules) {
                        try {
                            if (ptr.getPunchRuleId() == null)
                                continue;
                            PunchRule pr = punchProvider.getPunchRuleById(ptr.getPunchRuleId());
                            if (null == pr || pr.getRuleType() == null) {
                                continue;
                            }

                            if (PunchRuleType.GUDING.equals(PunchRuleType.fromCode(pr.getRuleType()))) {
                                //只刷新有效状态的-针对固定班次
                                if (!ptr.getStatus().equals(PunchRuleStatus.ACTIVE.getCode())) {
                                    continue;
                                }
                                //看昨天是否为特殊日期
                                PunchSpecialDay specialDay = punchProvider.findSpecialDayByDateAndOrgId(pr.getPunchOrganizationId(), yesterday.getTime());
                                if (null != specialDay) {
                                    if (specialDay.getStatus().equals(NormalFlag.YES.getCode())) {
                                        continue;
                                    } else {
                                        //特殊上班工作日,要处理它
                                        punchRules.add(pr);
                                    }
                                }
                                //如果为节假日则返回null  如果是节假调休日,用调休日期代替
                                java.sql.Date punchDate = new java.sql.Date(yesterday.getTime().getTime());
                                PunchHoliday punchHoliday = punchBaseService.checkHoliday(pr, punchDate);
                                if (punchHoliday != null && NormalFlag.NO == NormalFlag.fromCode(punchHoliday.getStatus())) {
                                    punchDate = punchHoliday.getExchangeFromDate();
                                }
                                //看是循环timerule找当天的timeRule
                                Integer openWeek = Integer.parseInt(ptr.getOpenWeekday(), 2);
                                Integer weekDayInt = punchBaseService.getWeekDayInt(punchDate);
                                if (weekDayInt.equals(openWeek & weekDayInt)) {
                                    //当天上班
                                    punchRules.add(pr);
                                }
                            } else {
                                Calendar schedulingCalendar = Calendar.getInstance();
                                schedulingCalendar = yesterday;
                                if (ptr.getDaySplitTimeLong() < PunchConstants.ONE_DAY_MS) {
                                    schedulingCalendar = punCalendar;
                                }
                                final java.sql.Date queryDate = new java.sql.Date(schedulingCalendar.getTimeInMillis());
                                List<PunchScheduling> punchSchedulings = punchSchedulingProvider.queryPunchSchedulings(null, Integer.MAX_VALUE, new ListingQueryBuilderCallback() {
                                    @Override
                                    public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                                                                                        SelectQuery<? extends Record> query) {
                                        query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.RULE_DATE.eq(queryDate));
                                        query.addConditions(Tables.EH_PUNCH_SCHEDULINGS.TIME_RULE_ID.eq(ptr.getId()));
                                        query.addOrderBy(Tables.EH_PUNCH_SCHEDULINGS.RULE_DATE.asc());
                                        return null;
                                    }
                                });
                                if (null != punchSchedulings) {
                                    for (PunchScheduling punchScheduling : punchSchedulings) {
                                        OrganizationMemberDetails memberDetail = findOrganizationMemberDetailByCacheDetailId(punchScheduling.getTargetId());
                                        if (null != memberDetail)
                                            punchDayLogInitialize(memberDetail , pr.getOwnerId(), schedulingCalendar);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            LOGGER.error("this is something wrong with ptr + " + JSON.toJSONString(ptr), e);
                        }

                    }
                    for (PunchRule pr : punchRules) {
                        try {
                            refreshGroupDayLogAndMonthStat(pr, yesterday);
                        } catch (Exception e) {
                            LOGGER.error("this is something wrong with pr + " + JSON.toJSONString(pr), e);
                        }
                    }
                    organizationMemberDetailsCacheDetailId.get().clear();
                    timeRuleCache.get().clear();
                    punchRuleCacheByUserId.get().clear();
                    return null;
                });
    }

    ThreadLocal<Map<Long, OrganizationMemberDetails>> organizationMemberDetailsCacheDetailId = new ThreadLocal<Map<Long, OrganizationMemberDetails>>() {
        protected Map<Long, OrganizationMemberDetails> initialValue() {
            return new HashMap<>();
        }
    };

    private OrganizationMemberDetails findOrganizationMemberDetailByCacheDetailId(Long detailId) {
        OrganizationMemberDetails memberDetail = organizationMemberDetailsCacheDetailId.get().get(detailId);
        if (null == memberDetail) {
            memberDetail = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
            organizationMemberDetailsCacheDetailId.get().put(detailId, memberDetail);
        }
        return memberDetail;
    }

    /**
     * 刷固定排班
     */
    private void refreshGroupDayLogAndMonthStat(PunchRule pr, Calendar yesterday) {
        Integer currentVersion = getPunchGroupCurrentVersion(pr.getOwnerId());
        List<UniongroupMemberDetail> members = uniongroupConfigureProvider.listUniongroupMemberDetail(pr.getPunchOrganizationId(), currentVersion);
        if (null != members && members.size() > 0) {
            for (UniongroupMemberDetail member : members) {
                if (member != null) {
                    OrganizationMemberDetails memberDetail = organizationProvider.findOrganizationMemberDetailsByDetailId(member.getDetailId());
                    punchDayLogInitialize(memberDetail, pr.getOwnerId(), yesterday);
                    punchRuleCacheByUserId.get().put(pr.getOwnerId() + "-" + member.getTargetId(), pr);
                }
            }
        }
    }

    private ThreadLocal<Map<String, PunchRule>> punchRuleCacheByUserId = new ThreadLocal<Map<String, PunchRule>>() {
        protected Map<String, PunchRule> initialValue() {
            return new HashMap<>();
        }
    };

    /**
     * 初始化pdl，对已经存在的pdl数据不做更新操作
     * 主要用于定时任务每天初始化
     */
    private void punchDayLogInitialize(OrganizationMemberDetails memberDetail, Long ownerId, Calendar punCalendar) {
        if (memberDetail == null) {
            return;
        }
        try {
            String punchDate = dateSF.get().format(punCalendar.getTime());
            PunchDayLog punchDayLog = punchProvider.getDayPunchLogByDateAndDetailId(memberDetail.getId(), ownerId, punchDate);
            if (punchDayLog != null) {
                return;
            }
            PunchRule pr = getPunchRuleByDetailId(memberDetail.getNamespaceId(), PunchOwnerType.ORGANIZATION.getCode(), ownerId, memberDetail.getId());
            if (memberDetail.getTargetId() == null || memberDetail.getTargetId().equals(0L) || pr == null) {
                // 用户未激活和没有设置考勤规则，因此不存在打卡记录和请假等申请记录，所以日报统计只有一些基础数据
                initPunchDayLog4UntrackOrUnPunchRuleOrganizationMember(memberDetail, ownerId, pr, punchDate);
            } else {
                this.refreshPunchDayLog(memberDetail, punCalendar);
            }
        } catch (Exception e) {
            LOGGER.error("refresh day log and month stat Wrong [{}] detailId is {} owner id is {}", memberDetail.getContactName(), memberDetail.getId(), ownerId, e);
        }
    }

    /**
     * 初始化pdl数据，对已经存在的pdl数据进行重新计算
     * 主要用于考勤5.0,6.0,7.0上线用于批量更新数据的一次性操作
     */
    private void punchDayLogRefresh(OrganizationMemberDetails memberDetail, Long ownerId, Calendar punCalendar) {
        if (memberDetail == null) {
            return;
        }
        try {
            String punchDate = dateSF.get().format(punCalendar.getTime());
            PunchDayLog punchDayLog = punchProvider.getDayPunchLogByDateAndDetailId(memberDetail.getId(), ownerId, punchDate);
            if (memberDetail.getTargetId() == null || memberDetail.getTargetId().equals(0L)) {
                // 用户未激活和没有设置考勤规则，因此不存在打卡记录和请假等申请记录，所以日报统计只有一些基础数据
                PunchRule pr = getPunchRuleByDetailId(memberDetail.getNamespaceId(), PunchOwnerType.ORGANIZATION.getCode(), ownerId, memberDetail.getId());
                initPunchDayLog4UntrackOrUnPunchRuleOrganizationMember(memberDetail, ownerId, pr, punchDate);
            } else {
                PunchRule pr = null;
                if (punchDayLog == null) {
                    pr = getPunchRuleByDetailId(memberDetail.getNamespaceId(), PunchOwnerType.ORGANIZATION.getCode(), ownerId, memberDetail.getId());
                } else if (punchDayLog.getPunchOrganizationId() != null && punchDayLog.getPunchOrganizationId() > 0) {
                    pr = punchProvider.getPunchruleByPunchOrgId(punchDayLog.getPunchOrganizationId());
                }
                if (pr != null) {
                    this.refreshPunchDayLog(memberDetail, punCalendar);
                } else if (punchDayLog == null) {
                    initPunchDayLog4UntrackOrUnPunchRuleOrganizationMember(memberDetail, ownerId, pr, punchDate);
                }
            }
        } catch (Exception e) {
            LOGGER.error("refresh day log and month stat Wrong [{}] detailId is {} owner id is {}", memberDetail.getContactName(), memberDetail.getId(), ownerId, e);
        }
    }

    /**
     * 用户未激活和没有设置考勤规则，因此不存在打卡记录和请假等申请记录，所以日报统计只有一些基础数据
     */
    private void initPunchDayLog4UntrackOrUnPunchRuleOrganizationMember(OrganizationMemberDetails memberDetail, Long ownerId, PunchRule pr, String punCalendar) {
        if (memberDetail.getTargetId() != null && memberDetail.getTargetId() > 0 && pr != null) {
            return;
        }
        this.coordinationProvider.getNamedLock(CoordinationLocks.CREATE_PUNCH_LOG.getCode() + memberDetail.getId()).enter(() -> {
            punchProvider.deletePunchDayLogByDateAndDetailId(memberDetail.getId(), ownerId, punCalendar);

            Long deptId = organizationService.getDepartmentByDetailIdAndOrgId(memberDetail.getId(), memberDetail.getOrganizationId());
            PunchDayLog punchDayLog = new PunchDayLog();
            punchDayLog.setEnterpriseId(ownerId);
            punchDayLog.setDetailId(memberDetail.getId());
            punchDayLog.setUserId(memberDetail.getTargetId());
            punchDayLog.setDeptId(deptId);
            punchDayLog.setPunchDate(java.sql.Date.valueOf(punCalendar));
            punchDayLog.setStatusList(String.valueOf(PunchStatus.NO_ASSIGN_PUNCH_RULE.getCode()));
            punchDayLog.setApprovalStatusList("");
            punchDayLog.setViewFlag(NormalFlag.YES.getCode());
            punchDayLog.setMorningStatus(NormalFlag.YES.getCode());
            punchDayLog.setAfternoonStatus(NormalFlag.YES.getCode());
            punchDayLog.setExceptionStatus(NormalFlag.NO.getCode());
            punchDayLog.setDeviceChangeFlag(NormalFlag.NO.getCode());
            punchDayLog.setPunchCount(0);
            punchDayLog.setPunchTimesPerDay((byte) 0);
            punchDayLog.setRestFlag(NormalFlag.NO.getCode());
            punchDayLog.setAbsentFlag(NormalFlag.NO.getCode());
            punchDayLog.setNormalFlag(NormalFlag.YES.getCode());
            if (pr != null) {
                punchDayLog.setRuleType(pr.getRuleType());
                punchDayLog.setPunchOrganizationId(pr.getPunchOrganizationId());
                PunchTimeRule ptr = getPunchTimeRuleWithPunchDayTypeByRuleIdAndDateUseDetailId(pr, punchDayLog.getPunchDate(), memberDetail.getId());
                if (ptr != null && ptr.getId() != null && ptr.getId() != 0) {
                    punchDayLog.setStatusList(String.valueOf(PunchStatus.UNPUNCH.getCode()));
                    punchDayLog.setPunchTimesPerDay(ptr.getPunchTimesPerDay());
                    punchDayLog.setTimeRuleId(ptr.getId());
                    punchDayLog.setTimeRuleName(ptr.getName());
                } else if (ptr != null && NormalFlag.YES == NormalFlag.fromCode(ptr.getUnscheduledFlag())) {
                    punchDayLog.setStatusList(String.valueOf(PunchStatus.NO_ASSIGN_PUNCH_SCHEDULED.getCode()));
                } else {
                    punchDayLog.setStatusList(String.valueOf(PunchStatus.NOTWORKDAY.getCode()));
                    // 已设置考勤规则同时排休息班的才计入休息
                    punchDayLog.setRestFlag(NormalFlag.YES.getCode());
                }
            }
            punchDayLog.setCreatorUid(0L);
            punchDayLog.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            punchDayLog.setSplitDateTime(new Timestamp(punchDayLog.getPunchDate().getTime() + ONE_DAY_MS + PunchConstants.DEFAULT_SPLIT_TIME));
            punchProvider.createPunchDayLog(punchDayLog);
            return null;
        });
    }

    @Override
    public void deletePunchRuleMap(DeletePunchRuleMapCommand cmd) {
        if (null == cmd.getOwnerId() || null == cmd.getOwnerType()) {
            LOGGER.error("Invalid owner type or  Id parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid owner type or  Id parameter in the command");
        }
        cmd.setOwnerId(getTopEnterpriseId(cmd.getOwnerId()));
        if (null == cmd.getId()) {
            LOGGER.error("Invalid   Id parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid   Id parameter in the command");
        }
        PunchRuleOwnerMap obj = this.punchProvider.getPunchRuleOwnerMapById(cmd.getId());
        if (obj.getOwnerId().equals(cmd.getOwnerId()) && obj.getOwnerType().equals(cmd.getOwnerType())) {
//			if(null != cmd.getTargetId() && null != cmd.getTargetType()){
//				if(cmd.getTargetId().equals(obj.getTargetId())&& cmd.getTargetType().equals(obj.getTargetType())){
//					this.punchProvider.deletePunchRuleOwnerMap(obj);
//					this.punchSchedulingProvider.deletePunchSchedulingByOwnerAndTarget(obj.getOwnerType(),obj.getOwnerId(),obj.getTargetType(),obj.getTargetId());
//					this.punchProvider.deletePunchTimeRulesByOwnerAndTarget(obj.getOwnerType(),obj.getOwnerId(),obj.getTargetType(),obj.getTargetId());
//				}
//				else{
//
//					LOGGER.error("Invalid target type or  Id parameter in the command");
//					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_INVALID_PARAMETER,
//							"Invalid   target type or  Id parameter in the command");
//
//				}
//			}
//			else
//				this.punchProvider.deletePunchRuleOwnerMap(obj);
        } else {

            LOGGER.error("Invalid owner type or  Id parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid owner type or  Id parameter in the command");
        }


    }

    /**
     * 给一个target设置规则
     */
    @Override
    public void updateTargetPunchAllRule(UpdateTargetPunchAllRuleCommand cmd) {

        if (null == cmd.getOwnerId() || null == cmd.getOwnerType()) {
            LOGGER.error("Invalid owner type or  Id parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid owner type or  Id parameter in the command");
        }
        if (null == cmd.getTargetId() || null == cmd.getTargetType()) {
            LOGGER.error("Invalid owner type or  Id parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid target type or  Id parameter in the command");
        }
        // TODO 删除之前的规则
        coordinationProvider.getNamedLock(
                CoordinationLocks.UPDATE_APPROVAL_TARGET_RULE.getCode() + cmd.getTargetId()).enter(
                () -> {
                    //建立所有规则
                    PunchRule pr = new PunchRule();
                    pr.setOwnerId(cmd.getOwnerId());
                    pr.setOwnerType(cmd.getOwnerType());
                    pr.setName(cmd.getTargetId() + "rule");
                    pr.setCreatorUid(UserContext.current().getUser().getId());
                    pr.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
                            .getTime()));
                    //time rule
                    if (null != cmd.getTimeRule()) {
                        PunchTimeRule ptr = convertPunchTimeRule(cmd.getTimeRule());
                        ptr.setOwnerId(cmd.getOwnerId());
                        ptr.setOwnerType(cmd.getOwnerType());
                        this.punchProvider.createPunchTimeRule(ptr);
                        pr.setTimeRuleId(ptr.getId());
                    }
                    //location rule
                    if (null != cmd.getLocationRule()) {
                        PunchLocationRule obj = ConvertHelper.convert(cmd.getLocationRule(), PunchLocationRule.class);
                        obj.setOwnerId(cmd.getOwnerId());
                        obj.setOwnerType(cmd.getOwnerType());
                        obj.setName(cmd.getTargetId() + "rule");
                        obj.setCreatorUid(UserContext.current().getUser().getId());
                        obj.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
                                .getTime()));
                        this.punchProvider.createPunchLocationRule(obj);
                        if (null != cmd.getLocationRule().getPunchGeoPoints()) {
                            for (PunchGeoPointDTO punchGeopointDTO : cmd.getLocationRule().getPunchGeoPoints()) {
                                PunchGeopoint punchGeopoint = ConvertHelper.convert(punchGeopointDTO, PunchGeopoint.class);
                                punchGeopoint.setOwnerType(cmd.getOwnerType());
                                punchGeopoint.setOwnerId(cmd.getOwnerId());
                                punchGeopoint.setLocationRuleId(obj.getId());
                                punchGeopoint.setCreatorUid(UserContext.current().getUser().getId());
                                punchGeopoint.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                                punchGeopoint.setGeohash(GeoHashUtils.encode(
                                        punchGeopoint.getLatitude(), punchGeopoint.getLongitude()));
                                punchProvider.createPunchGeopoint(punchGeopoint);
                            }
                        }
                        pr.setLocationRuleId(obj.getId());
                    }
                    //wifi rule
                    if (null != cmd.getWifiRule()) {
                        PunchWifiRule obj = ConvertHelper.convert(cmd, PunchWifiRule.class);
                        obj.setOwnerId(cmd.getOwnerId());
                        obj.setOwnerType(cmd.getOwnerType());
                        obj.setName(cmd.getTargetId() + "rule");
                        obj.setCreatorUid(UserContext.current().getUser().getId());
                        obj.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
                                .getTime()));
                        this.punchProvider.createPunchWifiRule(obj);
                        if (null != cmd.getWifiRule().getWifis()) {
                            for (PunchWiFiDTO dto : cmd.getWifiRule().getWifis()) {
                                PunchWifi punchWifi = ConvertHelper.convert(dto, PunchWifi.class);
                                punchWifi.setOwnerType(cmd.getOwnerType());
                                punchWifi.setOwnerId(cmd.getOwnerId());
                                punchWifi.setCreatorUid(UserContext.current().getUser().getId());
                                punchWifi.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                                punchWifi.setWifiRuleId(obj.getId());
                                punchProvider.createPunchWifi(punchWifi);
                            }
                        }
                        pr.setWifiRuleId(obj.getId());
                    }
                    //workday rule
                    if (null != cmd.getWorkdayRule()) {
                        PunchWorkdayRule obj = converDTO2WorkdayRule(cmd.getWorkdayRule());
                        obj.setOwnerId(cmd.getOwnerId());
                        obj.setOwnerType(cmd.getOwnerType());
                        obj.setName(cmd.getTargetId() + "rule");
                        this.punchProvider.createPunchWorkdayRule(obj);
                        if (null != cmd.getWorkdayRule().getWorkdays()) {
                            for (Long date : cmd.getWorkdayRule().getWorkdays()) {
                                PunchHoliday holiday = new PunchHoliday();
                                holiday.setOwnerType(cmd.getOwnerType());
                                holiday.setOwnerId(cmd.getOwnerId());
                                holiday.setCreatorUid(UserContext.current().getUser().getId());
                                holiday.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                                holiday.setWorkdayRuleId(obj.getId());
                                holiday.setRuleDate(new java.sql.Date(date));
                                holiday.setStatus(DateStatus.WORKDAY.getCode());
                                this.punchProvider.createPunchHoliday(holiday);

                            }
                        }
                        if (null != cmd.getWorkdayRule().getHolidays()) {
                            for (Long date : cmd.getWorkdayRule().getHolidays()) {
                                PunchHoliday holiday = new PunchHoliday();
                                holiday.setOwnerType(cmd.getOwnerType());
                                holiday.setOwnerId(cmd.getOwnerId());
                                holiday.setCreatorUid(UserContext.current().getUser().getId());
                                holiday.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                                holiday.setWorkdayRuleId(obj.getId());
                                holiday.setRuleDate(new java.sql.Date(date));
                                holiday.setStatus(DateStatus.HOLIDAY.getCode());
                                this.punchProvider.createPunchHoliday(holiday);
                            }
                        }
                        pr.setWorkdayRuleId(obj.getId());
                    }


                    PunchRuleOwnerMap map = this.punchProvider.getPunchRuleOwnerMapByOwnerAndTarget(cmd.getOwnerType(), cmd.getOwnerId(),
                            cmd.getTargetType(), cmd.getTargetId());

                    if (null == map) {
                        map = ConvertHelper.convert(cmd, PunchRuleOwnerMap.class);
                        map.setCreatorUid(UserContext.current().getUser().getId());
                        map.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
                                .getTime()));
                        this.punchProvider.createPunchRule(pr);
                        map.setPunchRuleId(pr.getId());
                        this.punchProvider.createPunchRuleOwnerMap(map);
                    } else {
                        if (null == map.getPunchRuleId())
                            this.punchProvider.createPunchRule(pr);
                        else {
                            PunchRule oldPr = this.punchProvider.getPunchRuleById(map.getPunchRuleId());
                            if (null == oldPr)
                                this.punchProvider.createPunchRule(pr);
                            else {
                                pr.setId(oldPr.getId());
                                this.punchProvider.updatePunchRule(pr);
                            }
                        }
                        map.setCreatorUid(UserContext.current().getUser().getId());
                        map.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
                                .getTime()));
                        map.setPunchRuleId(pr.getId());
                        this.punchProvider.updatePunchRuleOwnerMap(map);
                    }
                    return null;
                });

    }

    @Override
    public void deleteTargetPunchAllRule(GetTargetPunchAllRuleCommand cmd) {
        if (null == cmd.getOwnerId() || null == cmd.getOwnerType()) {
            LOGGER.error("Invalid owner type or  Id parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid owner type or  Id parameter in the command");
        }
        if (null == cmd.getTargetId() || null == cmd.getTargetType()) {
            LOGGER.error("Invalid owner type or  Id parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid target type or  Id parameter in the command");
        }

        if (cmd.getTargetType().equals(cmd.getOwnerType()) && cmd.getTargetId().equals(cmd.getOwnerId())) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "不能删除公司规则");
        }
        PunchRuleOwnerMap map = this.punchProvider.getPunchRuleOwnerMapByOwnerAndTarget(cmd.getOwnerType(), cmd.getOwnerId(),
                cmd.getTargetType(), cmd.getTargetId());

        if (null != map) {
            // 删除规则
            deletePunchRules(cmd.getOwnerType(), cmd.getOwnerId(), map.getPunchRuleId());
            map.setPunchRuleId(null);
            map.setCreatorUid(UserContext.current().getUser().getId());
            map.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
                    .getTime()));
            this.punchProvider.updatePunchRuleOwnerMap(map);
        }
    }

    private void deletePunchRules(String ownerType, Long ownerId, Long punchRuleId) {

        if (null == punchRuleId)
            return;
        PunchRule pr = this.punchProvider.getPunchRuleById(punchRuleId);
        if (null == pr)
            return;
        this.punchProvider.deletePunchTimeRuleByOwnerAndId(ownerType, ownerId, pr.getTimeRuleId());
        this.punchProvider.deletePunchLocationRuleByOwnerAndId(ownerType, ownerId, pr.getLocationRuleId());
        this.punchProvider.deletePunchGeopointsByRuleId(pr.getLocationRuleId());
        this.punchProvider.deletePunchWifiRuleByOwnerAndId(ownerType, ownerId, pr.getWifiRuleId());
        this.punchProvider.deletePunchWifisByRuleId(pr.getWifiRuleId());
        this.punchProvider.deletePunchWorkdayRuleByOwnerAndId(ownerType, ownerId, pr.getWorkdayRuleId());
        this.punchProvider.deletePunchHolidayByRuleId(pr.getWorkdayRuleId());
        this.punchProvider.deletePunchRule(pr);
    }

    public PunchRuleOwnerMap getOrCreateTargetRuleMap(String ownerType, Long ownerId, String targetType, Long targetId) {
        if (null == ownerId || null == ownerType) {
            LOGGER.error("Invalid owner type or  Id parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid owner type or  Id parameter in the command");
        }
        if (null == targetType || null == targetId) {
            LOGGER.error("Invalid owner type or  Id parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid target type or  Id parameter in the command");
        }
        //z注释map和rule 的关系
        PunchRuleOwnerMap map = this.punchProvider.getPunchRuleOwnerMapByOwnerAndTarget(ownerType, ownerId,
                targetType, targetId);
        if (map == null) {
            map = new PunchRuleOwnerMap();
            map.setOwnerType(ownerType);
            map.setOwnerId(ownerId);
            map.setTargetId(targetId);
            map.setTargetType(targetType);
            map.setCreatorUid(UserContext.current().getUser().getId());
            map.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            PunchRule pr = new PunchRule();
            pr.setOwnerId(ownerId);
            pr.setOwnerType(ownerType);
            pr.setName(targetId + "rule");
            pr.setCreatorUid(UserContext.current().getUser().getId());
            pr.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            this.punchProvider.createPunchRule(pr);
            map.setPunchRuleId(pr.getId());
            this.punchProvider.createPunchRuleOwnerMap(map);
        } else if (null == map.getPunchRuleId()) {
            PunchRule pr = new PunchRule();
            pr.setOwnerId(ownerId);
            pr.setOwnerType(ownerType);
            pr.setName(targetId + "rule");
            pr.setCreatorUid(UserContext.current().getUser().getId());
            pr.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
                    .getTime()));
            this.punchProvider.createPunchRule(pr);
            map.setPunchRuleId(pr.getId());
            this.punchProvider.updatePunchRuleOwnerMap(map);

        }
        return map;
    }

    @Override
    public void updatePunchPoint(UpdatePunchPointCommand cmd) {
        PunchGeopoint punchGeopoint = punchProvider.findPunchGeopointById(cmd.getId());
        punchGeopoint.setDescription(cmd.getDescription());
        punchGeopoint.setLatitude(cmd.getLatitude());
        punchGeopoint.setLongitude(cmd.getLongitude());
        punchGeopoint.setDistance(cmd.getDistance());
        punchGeopoint.setGeohash(GeoHashUtils.encode(
                punchGeopoint.getLatitude(), punchGeopoint.getLongitude()));
        punchProvider.updatePunchGeopoint(punchGeopoint);


    }

    @Override
    public void addPunchPoint(AddPunchPointCommand cmd) {
        cmd.setOwnerId(getTopEnterpriseId(cmd.getOwnerId()));
        PunchRuleOwnerMap map = getOrCreateTargetRuleMap(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getTargetType(), cmd.getTargetId());
        PunchRule pr = punchProvider.getPunchRuleById(map.getPunchRuleId());
        PunchLocationRule plr = null;
        if (pr.getLocationRuleId() == null) {
            plr = new PunchLocationRule();
            plr.setOwnerType(cmd.getOwnerType());
            plr.setOwnerId(cmd.getOwnerId());
            pr.setCreatorUid(UserContext.current().getUser().getId());
            pr.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
                    .getTime()));
            plr.setName("punch location rule " + cmd.getTargetId());
            punchProvider.createPunchLocationRule(plr);
            pr.setLocationRuleId(plr.getId());
            punchProvider.updatePunchRule(pr);
        } else {
            plr = punchProvider.getPunchLocationRuleById(pr.getLocationRuleId());
        }
        PunchGeopoint punchGeopoint = ConvertHelper.convert(cmd, PunchGeopoint.class);
        punchGeopoint.setOwnerType(cmd.getOwnerType());
        punchGeopoint.setOwnerId(cmd.getOwnerId());
        punchGeopoint.setLocationRuleId(plr.getId());
        punchGeopoint.setCreatorUid(UserContext.current().getUser().getId());
        punchGeopoint.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        punchGeopoint.setGeohash(GeoHashUtils.encode(
                punchGeopoint.getLatitude(), punchGeopoint.getLongitude()));
        punchProvider.createPunchGeopoint(punchGeopoint);

    }

    @Override
    public ListPunchPointsResponse listPunchPoints(ListPunchPointsCommand cmd) {
        cmd.setOwnerId(getTopEnterpriseId(cmd.getOwnerId()));
        ListPunchPointsResponse response = new ListPunchPointsResponse();
        PunchRuleOwnerMap map = punchProvider.getPunchRuleOwnerMapByOwnerAndTarget(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getTargetType(), cmd.getTargetId());
        if (null == map)
            return response;
        PunchRule pr = punchProvider.getPunchRuleById(map.getPunchRuleId());
        if (null == pr)
            return response;
        PunchLocationRule plr = null;
        if (pr.getLocationRuleId() == null) {
            return response;
        } else {
            plr = punchProvider.getPunchLocationRuleById(pr.getLocationRuleId());
        }

        List<PunchGeopoint> geos = this.punchProvider.listPunchGeopointsByRuleId(cmd.getOwnerType(), cmd.getOwnerId(), plr.getId());
        if (null != geos) {
            response.setPoints(new ArrayList<PunchGeoPointDTO>());
            for (PunchGeopoint geo : geos) {
                PunchGeoPointDTO geoDTO = ConvertHelper.convert(geo, PunchGeoPointDTO.class);
                response.getPoints().add(geoDTO);
            }
        }
        return response;
    }

    @Override
    public void addPunchWiFi(AddPunchWiFiCommand cmd) {
        cmd.setOwnerId(getTopEnterpriseId(cmd.getOwnerId()));
        PunchRuleOwnerMap map = getOrCreateTargetRuleMap(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getTargetType(), cmd.getTargetId());
        PunchRule pr = punchProvider.getPunchRuleById(map.getPunchRuleId());
        PunchWifiRule pwr = null;
        if (pr.getWifiRuleId() == null) {
            pwr = new PunchWifiRule();
            pwr.setOwnerType(cmd.getOwnerType());
            pwr.setOwnerId(cmd.getOwnerId());
            pwr.setCreatorUid(UserContext.current().getUser().getId());
            pwr.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
                    .getTime()));
            pwr.setName("punch location rule " + cmd.getTargetId());
            punchProvider.createPunchWifiRule(pwr);
            pr.setWifiRuleId(pwr.getId());
            punchProvider.updatePunchRule(pr);
        } else {
            pwr = punchProvider.getPunchWifiRuleById(pr.getWifiRuleId());
        }
        PunchWifi punchWifi = ConvertHelper.convert(cmd, PunchWifi.class);
        punchWifi.setOwnerType(cmd.getOwnerType());
        punchWifi.setOwnerId(cmd.getOwnerId());
        pr.setCreatorUid(UserContext.current().getUser().getId());
        pr.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
                .getTime()));
        punchWifi.setWifiRuleId(pwr.getId());
        punchProvider.createPunchWifi(punchWifi);
    }

    @Override
    public ListPunchWiFisResponse listPunchWiFis(ListPunchRulesCommonCommand cmd) {
        cmd.setOwnerId(getTopEnterpriseId(cmd.getOwnerId()));
        ListPunchWiFisResponse response = new ListPunchWiFisResponse();
        PunchRuleOwnerMap map = punchProvider.getPunchRuleOwnerMapByOwnerAndTarget(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getTargetType(), cmd.getTargetId());
        if (null == map)
            return response;
        PunchRule pr = punchProvider.getPunchRuleById(map.getPunchRuleId());
        if (null == pr)
            return response;
        PunchWifiRule pwr = null;
        if (pr.getWifiRuleId() == null) {
            return response;
        } else {
            pwr = punchProvider.getPunchWifiRuleById(pr.getWifiRuleId());
        }

        List<PunchWifi> wifis = this.punchProvider.listPunchWifisByRuleId(cmd.getOwnerType(), cmd.getOwnerId(), pwr.getId());
        if (null != wifis) {
            response.setWifis(new ArrayList<PunchWiFiDTO>());
            for (PunchWifi wifi : wifis) {
                PunchWiFiDTO geoDTO = ConvertHelper.convert(wifi, PunchWiFiDTO.class);
                response.getWifis().add(geoDTO);
            }
        }
        return response;
    }


    @Override
    public void updatePunchWiFi(PunchWiFiDTO cmd) {
        PunchWifi punchWifi = punchProvider.getPunchWifiById(cmd.getId());
        punchWifi.setSsid(cmd.getSsid());
        punchWifi.setMacAddress(cmd.getMacAddress());
        punchProvider.updatePunchWifi(punchWifi);

    }

    @Override
    public void deletePunchWiFi(PunchWiFiDTO cmd) {
        PunchWifi punchWifi = punchProvider.getPunchWifiById(cmd.getId());
        punchProvider.deletePunchWifi(punchWifi);
    }

    @Override
    public ListPunchSchedulingMonthResponse listPunchScheduling(ListPunchSchedulingMonthCommand cmd) {
        cmd.setOwnerId(getTopEnterpriseId(cmd.getOwnerId()));
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        startCalendar.setTime(new Date(cmd.getQueryTime()));
        startCalendar.set(Calendar.DAY_OF_MONTH, 1);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);
        endCalendar.setTime(startCalendar.getTime());
        endCalendar.add(Calendar.MONTH, 1);
        ListPunchSchedulingMonthResponse response = new ListPunchSchedulingMonthResponse();

        PunchRule pr = punchProvider.getPunchruleByPunchOrgId(cmd.getPunchOriganizationId());

        java.sql.Date startDate = new java.sql.Date(startCalendar.getTime().getTime());
        java.sql.Date endDate = new java.sql.Date(endCalendar.getTime().getTime());
        //		response.setTimeRules(listPunchTimeRuleList(cmd2).getTimeRules());
        List<PunchSchedulingDTO> schedulingDTOs = processschedulings(pr, startDate, endDate);
        response.setSchedulings(schedulingDTOs);
        return response;
    }

    @Override
    public HttpServletResponse exportPunchScheduling(ListPunchSchedulingMonthCommand cmd,
                                                     HttpServletResponse response) {

        Map<String, Object> params = new HashMap();

        //如果是null的话会被传成“null”
        params.put("queryTime", cmd.getQueryTime());
        params.put("employees", cmd.getEmployees());
        params.put("timeRules", cmd.getTimeRules());
        params.put("reportType", "exportPunchScheduling");
        String fileName = String.format(DateUtil.dateToStr(new Date(cmd.getQueryTime()), DateUtil.YM_NO_SLASH) + cmd.getPunchRuleName() + "考勤排班.xlsx");

        taskService.createTask(fileName, TaskType.FILEDOWNLOAD.getCode(), PunchExportTaskHandler.class, params, TaskRepeatFlag.REPEAT.getCode(), new Date());
        return response;

    }

    @Override
    public OutputStream getPunchSchedulingOutputStream(Long queryTime,
                                                       List<PunchSchedulingEmployeeDTO> employees, List<PunchTimeRuleDTO> timeRules, Long taskId) {

        Workbook wb = createPunchSchedulingsBook(queryTime, employees, timeRules);
        taskService.updateTaskProcess(taskId, 50);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            wb.write(out);
        } catch (IOException e) {
            LOGGER.error("something woring with build output stream");
            e.printStackTrace();
        }
        return out;

    }


    @Override
    public HttpServletResponse exportPunchSchedulingTemplate(ListPunchSchedulingMonthCommand cmd,
                                                             HttpServletResponse response) {
        String filePath = "Schedule" + monthSF.get().format(new Date(cmd.getQueryTime())) + ".xlsx";
        //新建了一个文件

        Workbook wb = createPunchSchedulingTemplateBook(cmd.getQueryTime(), cmd.getTimeRules());

        return download(wb, filePath, response);

    }


    private XSSFWorkbook createPunchSchedulingTemplateBook(Long queryTime, List<PunchTimeRuleDTO> timeRules) {

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("sheet1");

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 31));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 31));
        XSSFCellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 20);
        font.setFontName("Courier New");

        style.setFont(font);

        XSSFCellStyle titleStyle = wb.createCellStyle();
        titleStyle.setFont(font);
        titleStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);

        int rowNum = 0;

        //  创建标题
        XSSFRow rowTitle = sheet.createRow(rowNum++);
        rowTitle.createCell(0).setCellValue(monthSF.get().format(new Date(queryTime)));
        rowTitle.setRowStyle(titleStyle);

        XSSFRow rowReminder = sheet.createRow(rowNum++);

        Map<String, String> map = new HashMap<String, String>();
        StringBuilder ruleSB = new StringBuilder();
        if (null != timeRules) {
            for (PunchTimeRuleDTO rule : timeRules) {
                ruleSB.append(rule.getName());
                ruleSB.append("(");
                if (rule.getPunchTimeIntervals() != null) {
                    for (int i = 0; i < rule.getPunchTimeIntervals().size(); i++) {
                        if (i > 0) {
                            ruleSB.append(" ");
                        }
                        ruleSB.append(convertTimeLongToString(rule.getPunchTimeIntervals().get(i).getArriveTime()));
                        ruleSB.append("-");
                        ruleSB.append(convertTimeLongToString(rule.getPunchTimeIntervals().get(i).getLeaveTime()));
                    }
                }
                ruleSB.append(");");
            }
        }
        map.put("timeRules", ruleSB.toString());
        String result = localeTemplateService.getLocaleTemplateString(PunchConstants.PUNCH_EXCEL_SCOPE,
                PunchConstants.PUNCH_EXCEL_SCHEDULING_REMINDER, RentalNotificationTemplateCode.locale, map, "");

        rowReminder.createCell(0).setCellValue(result);
        rowReminder.setRowStyle(titleStyle);

        this.createPunchSchedulingsBookSheetHead(sheet, queryTime);
        return wb;
    }

    private Workbook createPunchSchedulingsBook(Long queryTime,
                                                List<PunchSchedulingEmployeeDTO> employees, List<PunchTimeRuleDTO> timeRules) {

        XSSFWorkbook wb = createPunchSchedulingTemplateBook(queryTime, timeRules);
        XSSFSheet sheet = wb.getSheetAt(0);
        if (null == employees || employees.size() == 0)
            return wb;
        for (PunchSchedulingEmployeeDTO employee : employees)
            setNewPunchSchedulingsBookRow(sheet, employee, queryTime);
        return wb;
    }

    private void createPunchSchedulingsBookSheetHead(Sheet sheet, Long queryTime) {
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        int cellNum = 0;
        SimpleDateFormat sf = new SimpleDateFormat("dd日 EEE", Locale.SIMPLIFIED_CHINESE);

        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        startCalendar.setTime(new Date(queryTime));
        startCalendar.set(Calendar.DAY_OF_MONTH, 1);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);
        endCalendar.setTime(startCalendar.getTime());
        endCalendar.add(Calendar.MONTH, 1);
        for (; startCalendar.before(endCalendar); startCalendar.add(Calendar.DAY_OF_MONTH, 1)) {
            row.createCell(++cellNum).setCellValue(sf.format(startCalendar.getTime()));
        }
    }

    private void setNewPunchSchedulingsBookRow(Sheet sheet, PunchSchedulingEmployeeDTO employee, Long queryTime) {

        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        startCalendar.setTime(new Date(queryTime));
        startCalendar.set(Calendar.DAY_OF_MONTH, 1);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);
        endCalendar.setTime(startCalendar.getTime());
        endCalendar.add(Calendar.MONTH, 1);

        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        int i = -1;
        row.createCell(++i).setCellValue(employee.getContactName());
        if (null != employee.getDaySchedulings())
            for (; startCalendar.before(endCalendar); startCalendar.add(Calendar.DAY_OF_MONTH, 1)) {
                row.createCell(++i).setCellValue(employee.getDaySchedulings()
                        .get(startCalendar.get(Calendar.DAY_OF_MONTH) - 1));
            }
    }

    private void storeFile(MultipartFile file, String filePath1) throws Exception {
        OutputStream out = new FileOutputStream(new File(filePath1));
        InputStream in = file.getInputStream();
        byte[] buffer = new byte[1024];
        while (in.read(buffer) != -1) {
            out.write(buffer);
        }
        out.close();
        in.close();
    }

    @Override
    public ArrayList processImportExcel2ArrayList(MultipartFile[] files){
    	ArrayList resultList = new ArrayList();
        String rootPath = System.getProperty("user.dir");
        String filePath = rootPath + File.separator + UUID.randomUUID().toString() + ".xlsx";
        LOGGER.error("importOrganization-filePath=" + filePath);
        //将原文件暂存在服务器中
        try {
            this.storeFile(files[0], filePath);
        } catch (Exception e) {
            LOGGER.error("importOrganization-store file fail.message=" + e.getMessage());
        }
        try {
            File file = new File(filePath);
            if (!file.exists())
                LOGGER.error("executeImportOrganization-file is not exist.filePath=" + filePath);
            InputStream in = new FileInputStream(file);
            resultList = PropMrgOwnerHandler.processorExcel(in);
        } catch (IOException e) {
            LOGGER.error("executeImportOrganization-parse file fail.message=" + e.getMessage());
        } finally{
            File file = new File(filePath);
            file.delete();
        }
        return resultList;
    }
    @Override
    public PunchSchedulingDTO importPunchScheduling(MultipartFile[] files) {
    	ArrayList resultList = processImportExcel2ArrayList(files);
        PunchSchedulingDTO result = convertToPunchSchedulings(resultList);
        addOperateLog(0L, "", result.toString(), 0L, "importPunchScheduling");
        return result;
    }

    @Override
    public void importPunchLogs(MultipartFile[] files) {
        // TODO Auto-generated method stub
        ArrayList resultList = new ArrayList();
        String rootPath = System.getProperty("user.dir");
        String filePath = rootPath + File.separator + UUID.randomUUID().toString() + ".xlsx";
        LOGGER.error("importOrganization-filePath=" + filePath);
        //将原文件暂存在服务器中
        try {
            this.storeFile(files[0], filePath);
        } catch (Exception e) {
            LOGGER.error("importOrganization-store file fail.message=" + e.getMessage());
        }
        try {
            File file = new File(filePath);
            if (!file.exists())
                LOGGER.error("executeImportOrganization-file is not exist.filePath=" + filePath);
            InputStream in = new FileInputStream(file);
            resultList = PropMrgOwnerHandler.processorExcel(in);
        } catch (IOException e) {
            LOGGER.error("executeImportOrganization-parse file fail.message=" + e.getMessage());
        }

        for (int rowIndex = 1; rowIndex < resultList.size(); rowIndex++) {
            RowResult r = (RowResult) resultList.get(rowIndex);
            if (r.getA() == null || r.getA().trim().equals("")) {
                LOGGER.error("have row is empty.rowIndex=" + (rowIndex + 1));
                break;
            } 
            try {
                PunchLog pl = new PunchLog();
                pl.setEnterpriseId(Long.valueOf(r.getA()));
                createPunchLog( datetimeSF.get().format(new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss").parse(r.getB())), ClockCode.SUCESS.getCode(), pl);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                LOGGER.error("row time format wrong.rowIndex=" + (rowIndex + 1));
                break;
            }

        }
    }

    private PunchSchedulingDTO convertToPunchSchedulings(ArrayList list) {
        PunchSchedulingDTO result = new PunchSchedulingDTO();
        result.setEmployees(new ArrayList<PunchSchedulingEmployeeDTO>());
        RowResult title = (RowResult) list.get(0);
        String monthString = title.getCells().get("A");
        Calendar calendar = Calendar.getInstance();
        try {

            Date month = monthSF.get().parse(monthString);
            result.setMonth(month.getTime());

            calendar.setTime(month);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.MONTH, 1);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            int days = calendar.get(Calendar.DAY_OF_MONTH);

            for (int rowIndex = 3; rowIndex < list.size(); rowIndex++) {
                RowResult r = (RowResult) list.get(rowIndex);
                PunchSchedulingEmployeeDTO dto = new PunchSchedulingEmployeeDTO();
                // 名字去空格
                if (r.getCells().get("A") == null || StringUtils.isEmpty(r.getCells().get("A")))
                    continue;
                dto.setContactName(r.getCells().get("A").replace(" ", ""));
                dto.setDaySchedulings(new ArrayList<>());
                for (int i = 1; i <= days; i++) {
                    String val = r.getCells().get(GetExcelLetter(i + 1));
                    if (!StringUtils.isEmpty(val)) {
                        val = val.replace(" ", "");
                    }
                    dto.getDaySchedulings().add(val);
                }
                result.getEmployees().add(dto);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }


    /*******
     * 导入部分
     *******/
    //  数字转换(1-A,2-B...)
    //  接下来会是很长的导入导出代码
    private static String GetExcelLetter(int n) {
        String s = "";
        while (n > 0) {
            int m = n % 26;
            if (m == 0)
                m = 26;
            s = (char) (m + 64) + s;
            n = (n - m) / 26;
        }
        return s;
    }

    @Override
    public void updatePunchRuleMap(PunchRuleMapDTO cmd) {

        PunchRuleOwnerMap old = this.punchProvider.getPunchRuleOwnerMapById(cmd.getId());
        if (null == old) {
            //如果没有就新建
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "can not find this rule");
        } else {
            //有就更新
            old.setTargetId(cmd.getTargetId());
            old.setTargetType(cmd.getTargetType());
            this.punchProvider.updatePunchRuleOwnerMap(old);
        }
    }

    @Override
    public void deletePunchPoint(DeleteCommonCommand cmd) {
        PunchGeopoint punchGeopoint = punchProvider.findPunchGeopointById(cmd.getId());
        punchProvider.deletePunchGeopoint(punchGeopoint);

    }

    @Override
    public void updatePunchSchedulings(UpdatePunchSchedulingMonthCommand cmd) {
//		if(null == cmd.getSchedulings() ||cmd.getSchedulings().size() == 0 )
//			return ;
//		PunchRuleOwnerMap map = getOrCreateTargetRuleMap(cmd.getSchedulings().get(0).getOwnerType(), cmd.getSchedulings().get(0).getOwnerId(),
//				cmd.getSchedulings().get(0).getTargetType(), cmd.getSchedulings().get(0).getTargetId());
//		PunchRule pr = punchProvider.getPunchRuleById(map.getPunchRuleId());
//		if(null == pr)
//			return  ;
//		for(PunchSchedulingDTO dto : cmd.getSchedulings()){
//
//			PunchScheduling punchScheduling = ConvertHelper.convert(dto, PunchScheduling.class);
//			punchScheduling.setPunchRuleId(pr.getId());
////			punchScheduling.setRuleDate(new java.sql.Date(dto.getRuleDate()));
//			punchScheduling.setCreatorUid(UserContext.current().getUser().getId());
//			punchScheduling.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
//					.getTime()));
//			punchSchedulingProvider.deletePunchScheduling(punchScheduling);
//			punchSchedulingProvider.createPunchScheduling(punchScheduling);
//
//		}

    }

    @Override
    public CheckPunchAdminResponse checkPunchAdmin(CheckPunchAdminCommand cmd) {

        checkCompanyIdIsNull(cmd.getOrganizationId());
        cmd.setOrganizationId(getTopEnterpriseId(cmd.getOrganizationId()));
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        CheckPunchAdminResponse response = new CheckPunchAdminResponse();
        response.setIsAdminFlag(NormalFlag.NO.getCode());
        try {
            if (resolver.checkSuperAdmin(UserContext.current().getUser().getId(), cmd.getOrganizationId())
                    || resolver.checkOrganizationAdmin(UserContext.current().getUser().getId(), cmd.getOrganizationId()))
                response.setIsAdminFlag(NormalFlag.YES.getCode());
        } catch (Exception e) {
            LOGGER.error("there is a error when check org admin ", e);
        }
        return response;
    }

    //  punch 2.8 added by R 20170725
    @Override
    public ListPunchSupportiveAddressCommandResponse listPunchSupportiveAddress(ListPunchSupportiveAddressCommand cmd) {

        ListPunchSupportiveAddressCommandResponse response = new ListPunchSupportiveAddressCommandResponse();
        cmd.setEnterpriseId(getTopEnterpriseId(cmd.getEnterpriseId()));
        Long userId = UserContext.current().getUser().getId();
        PunchRule pr = getPunchRule(PunchOwnerType.ORGANIZATION.getCode(), cmd.getEnterpriseId(), userId);
        if (null == pr)
            throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
                    PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_SETTING,
                    "公司没有设置打卡规则");
        //  查询用户对应的wifi mac地址
        List<PunchWifi> wifis = this.punchProvider
                .listPunchWifsByOwner(PunchOwnerType.ORGANIZATION.getCode(), pr.getPunchOrganizationId());
        //  查询用户对应的经纬度信息
        List<PunchGeopoint> punchGeopoints = punchProvider
                .listPunchGeopointsByOwner(PunchOwnerType.ORGANIZATION.getCode(), pr.getPunchOrganizationId());

        if (wifis != null) {
            response.setWifis(wifis.stream().map(r -> {
                PunchWiFiDTO dto = ConvertHelper.convert(r, PunchWiFiDTO.class);
                return dto;
            }).collect(Collectors.toList()));
        }

        if (punchGeopoints != null) {
            response.setGeoPoints(punchGeopoints.stream().map(r -> {
                PunchGeoPointDTO dto = ConvertHelper.convert(r, PunchGeoPointDTO.class);
                dto.setDistance(r.getDistance());
                return dto;
            }).collect(Collectors.toList()));
        }

        return response;
    }
    public void deletepunchGroup(Long punchOrgId, Long ownerId){
    	Organization organization = this.organizationProvider.findOrganizationById(punchOrgId);

        this.organizationProvider.deleteOrganization(organization);
        //  组织架构删除薪酬组人员关联及配置
        this.uniongroupService.deleteUniongroupConfigresByGroupId(punchOrgId, ownerId);
        this.uniongroupService.deleteUniongroupMemberDetailByGroupId(punchOrgId, ownerId);
        //删除考勤规则
        punchProvider.deletePunchGeopointsByOwnerId(punchOrgId);
        punchProvider.deletePunchWifisByOwnerId(punchOrgId);
        PunchRule pr = punchProvider.getPunchruleByPunchOrgId(punchOrgId);
        // 新增一条修改状态的规则
        punchProvider.updatePunchRule(pr);
        punchProvider.deletePunchTimeRuleByPunchOrgId(punchOrgId);
        punchProvider.deletePunchSpecialDaysByPunchOrgId(punchOrgId);
        punchProvider.deletePunchTimeIntervalByPunchRuleId(pr.getId());
        punchProvider.deletePunchOvertimeRulesByPunchRuleId(pr.getId());
        punchSchedulingProvider.deletePunchSchedulingByPunchRuleId(pr.getId());
        punchProvider.deletePunchRule(pr);
    }
    @Override
    public void deletePunchGroup(DeleteCommonCommand cmd) {
        Organization organization = this.organizationProvider.findOrganizationById(cmd.getId());
        checkAppPrivilege(organization.getDirectlyEnterpriseId(), organization.getDirectlyEnterpriseId(), PrivilegeConstants.PUNCH_RULE_DELETE);
        this.dbProvider.execute((TransactionStatus status) -> {

            PunchRule pr = punchProvider.getPunchruleByPunchOrgId(cmd.getId());
            pr.setStatus(PunchRuleStatus.DELETING.getCode());
            pr.setOperatorUid(UserContext.current().getUser().getId());
            pr.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            punchProvider.updatePunchRule(pr);
            punchProvider.setPunchTimeRuleStatus(pr.getId(), PunchRuleStatus.DELETING.getCode());

            Calendar tomorrowCalendar = Calendar.getInstance();
            tomorrowCalendar.add(Calendar.DAY_OF_MONTH, 1);
            java.sql.Date tomorrow = new java.sql.Date(tomorrowCalendar.getTimeInMillis());
            punchProvider.setPunchSchedulingsStatus(pr.getId(), PunchRuleStatus.DELETING.getCode(), tomorrow);
//            this.organizationProvider.deleteOrganization(organization);
//            //  组织架构删除薪酬组人员关联及配置
//            this.uniongroupService.deleteUniongroupConfigresByGroupId(cmd.getId(), cmd.getOwnerId());
//            this.uniongroupService.deleteUniongroupMemberDetailByGroupId(cmd.getId(), cmd.getOwnerId());
//            //删除考勤规则
//            punchProvider.deletePunchGeopointsByOwnerId(cmd.getId());
//            punchProvider.deletePunchWifisByOwnerId(cmd.getId());
//            PunchRule pr = punchProvider.getPunchruleByPunchOrgId(cmd.getId());
//            // 新增一条修改状态的规则
//            punchProvider.updatePunchRule(pr);
//            punchProvider.deletePunchTimeRuleByPunchOrgId(cmd.getId());
//            punchProvider.deletePunchSpecialDaysByPunchOrgId(cmd.getId());
//            punchProvider.deletePunchTimeIntervalByPunchRuleId(pr.getId());
//            punchProvider.deletePunchOvertimeRulesByPunchRuleId(pr.getId());
//            punchSchedulingProvider.deletePunchSchedulingByPunchRuleId(pr.getId());
//            punchProvider.deletePunchRule(pr);
            return null;
        });


    }

    @Override
    public PunchGroupDTO addPunchGroup(AddPunchGroupCommand cmd) {
        //
        this.dbProvider.execute((status) -> {
            if (cmd.getRuleType() == null)
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                        ErrorCodes.ERROR_INVALID_PARAMETER,
                        "Invalid rule type parameter in the command");
            if (NormalFlag.fromCode(cmd.getPunchRemindFlag()) == null) {
                cmd.setPunchRemindFlag(NormalFlag.NO.getCode());
            }
            if (cmd.getRemindMinutesOnDuty() == null || NormalFlag.NO == NormalFlag.fromCode(cmd.getPunchRemindFlag())) {
                cmd.setRemindMinutesOnDuty(0);
            }
            Long originOwnerId = cmd.getOwnerId();
            cmd.setOwnerId(getTopEnterpriseId(cmd.getOwnerId()));
            checkAppPrivilege(cmd.getOwnerId(), originOwnerId, PrivilegeConstants.PUNCH_RULE_CREATE);

            //建立考勤组
            Organization punchOrg = this.organizationService.createUniongroupOrganization(cmd.getOwnerId(), cmd.getGroupName(), UniongroupType.PUNCHGROUP.getCode());
            //添加关联
            SaveUniongroupConfiguresCommand command = new SaveUniongroupConfiguresCommand();
            command.setGroupId(punchOrg.getId());
            command.setGroupType(UniongroupType.PUNCHGROUP.getCode());
            command.setEnterpriseId(cmd.getOwnerId());
            command.setTargets(cmd.getTargets());
            //修改都修改config版本的数据
            command.setVersionCode(CONFIG_VERSION_CODE);
            try {
                this.uniongroupService.saveUniongroupConfigures(command);
            } catch (NoNodeAvailableException e) {
                LOGGER.error("NoNodeAvailableException", e);
            }

            //打卡地点和wifi

            saveGeopointsAndWifis(punchOrg.getId(), cmd.getPunchGeoPoints(), cmd.getWifis());

            PunchRule pr = ConvertHelper.convert(cmd, PunchRule.class);
            pr.setChinaHolidayFlag(cmd.getChinaHolidayFlag());
            pr.setOwnerType(PunchOwnerType.ORGANIZATION.getCode());
            pr.setOwnerId(cmd.getOwnerId());
            pr.setChinaHolidayFlag(cmd.getChinaHolidayFlag());
            if (PunchRuleType.GUDING == PunchRuleType.fromCode(cmd.getRuleType())) {
                pr.setPunchRemindFlag(cmd.getPunchRemindFlag());
                pr.setRemindMinutesOnDuty(cmd.getRemindMinutesOnDuty());
            } else {
                pr.setPunchRemindFlag(NormalFlag.NO.getCode());
                pr.setRemindMinutesOnDuty(0);
            }
            pr.setName(cmd.getGroupName());
            pr.setRuleType(cmd.getRuleType());
            pr.setPunchOrganizationId(punchOrg.getId());
            pr.setStatus(PunchRuleStatus.NEW.getCode());
            pr.setCreatorUid(UserContext.current().getUser().getId());
            pr.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            pr.setOperatorUid(UserContext.current().getUser().getId());
            pr.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            punchProvider.createPunchRule(pr);
            //打卡时间
            savePunchTimeRule(ConvertHelper.convert(cmd, PunchGroupDTO.class), pr);
            savePunchOvertimeRules(cmd.getPunchOvertimeRules(), pr);
            addOperateLog(pr.getId(), pr.getName(), cmd.toString(), pr.getOwnerId(), "addPunchGroup");

            //发消息
            //发消息 暂时屏蔽
//			sendMessageToGroupUser(pr, cmd.getTimeRules());

            return null;
        });
        return null;
    }

    /**获取某人的加班说明文字*/
    private String processOvertimeInfo(Long ownerId,Long userId){
    	PunchRule pr = getPunchRule("organization", ownerId, userId);
    	return processOvertimeInfo(pr);
    }

    private String processOvertimeInfo(PunchRule pr){
    	if(null == pr){
    		return localeStringService.getLocalizedString(PunchConstants.OVERTIME_INFO_SCOPE, PunchConstants.OVERTIME_INFO_NORULE,
    				UserContext.current().getUser().getLocale(), "未设置打卡规则");
    	}

    	StringBuilder sb = new StringBuilder();

    	PunchOvertimeRule workdayRule =  getPunchOvertimeRuleByPunchDayType(pr, PunchDayType.WORKDAY);
    	sb.append(localeStringService.getLocalizedString(PunchConstants.OVERTIME_INFO_SCOPE, PunchConstants.OVERTIME_INFO_WORKDAY,
    				UserContext.current().getUser().getLocale(), "工作日加班："));
    	sb.append(processOvertimeSingleRuleInfo(workdayRule));
    	sb.append("\n");
    	PunchOvertimeRule holidayRule = getPunchOvertimeRuleByPunchDayType(pr,PunchDayType.HOLIDAY);
    	sb.append(localeStringService.getLocalizedString(PunchConstants.OVERTIME_INFO_SCOPE, PunchConstants.OVERTIME_INFO_HOLIDAY,
    				UserContext.current().getUser().getLocale(), "休息日/节假日加班："));
    	sb.append(processOvertimeSingleRuleInfo(holidayRule));
    	return sb.toString();
    }

    private String processOvertimeSingleRuleInfo(PunchOvertimeRule rule ) {
		if(rule == null){
			return localeStringService.getLocalizedString(PunchConstants.OVERTIME_INFO_SCOPE, PunchConstants.OVERTIME_INFO_CLOSE,
    				UserContext.current().getUser().getLocale(), "未开启");
		}else if(PunchOvertimeApprovalType.fromCode(rule.getOvertimeApprovalType()) == PunchOvertimeApprovalType.PUNCH_AND_APPROVAL){
			return localeStringService.getLocalizedString(PunchConstants.OVERTIME_INFO_SCOPE, PunchConstants.OVERTIME_INFO_BOTH,
    				UserContext.current().getUser().getLocale(), "需要申请和打卡，时长按打卡时间计算，但不能超过申请的时间");
		}else if(PunchOvertimeApprovalType.fromCode(rule.getOvertimeApprovalType()) == PunchOvertimeApprovalType.APPROVAL){
			return localeStringService.getLocalizedString(PunchConstants.OVERTIME_INFO_SCOPE, PunchConstants.OVERTIME_INFO_REQUEST,
    				UserContext.current().getUser().getLocale(), "需要申请，时长按申请单时间计算");
		}else if(PunchOvertimeApprovalType.fromCode(rule.getOvertimeApprovalType()) == PunchOvertimeApprovalType.PUNCH){
			return localeStringService.getLocalizedString(PunchConstants.OVERTIME_INFO_SCOPE, PunchConstants.OVERTIME_INFO_PUNCH,
    				UserContext.current().getUser().getLocale(), "不需要申请，时长按打卡时间计算");
		}
		return null;
	}


    /**
     * 根据orgId,先删除后添加
     */
    private void saveGeopointsAndWifis(Long orgId, List<PunchGeoPointDTO> punchGeoPoints,
                                       List<PunchWiFiDTO> wifis) {
        punchProvider.deletePunchGeopointsByOwnerId(orgId);
        punchProvider.deletePunchWifisByOwnerId(orgId);
        if (null != punchGeoPoints) {
            for (PunchGeoPointDTO point : punchGeoPoints) {
                PunchGeopoint punchGeopoint = convertDTO2GeoPoint(point);
                punchGeopoint.setOwnerId(orgId);
                punchProvider.createPunchGeopoint(punchGeopoint);
            }
        }
        if (null != wifis) {
            for (PunchWiFiDTO wifi : wifis) {
                PunchWifi punchWifi = convertDTO2Wifi(wifi);
                punchWifi.setOwnerId(orgId);
                punchProvider.createPunchWifi(punchWifi);
            }
        }
    }

    private void savePunchOvertimeRules(List<PunchOvertimeRuleDTO> punchOvertimeRules, PunchRule pr) {
        deletePunchOvertimeRules(pr.getId(), pr.getStatus());
        if (CollectionUtils.isEmpty(punchOvertimeRules)) {
            return;
        }
        for (PunchOvertimeRuleDTO dto : punchOvertimeRules) {
            PunchOvertimeRule punchOvertimeRule = ConvertHelper.convert(dto, PunchOvertimeRule.class);
            punchOvertimeRule.setNamespaceId(UserContext.getCurrentNamespaceId());
            punchOvertimeRule.setPunchRuleId(pr.getId());
            punchOvertimeRule.setStatus(pr.getStatus());
            punchProvider.createPunchOvertimeRule(punchOvertimeRule);
        }
    }

    /**
     * 根据pr ,先删除再添加打卡时间,排班,特殊日期
     */
    private void savePunchTimeRule(PunchGroupDTO punchGroupDTO, PunchRule pr) {
        Long punchOrgId = pr.getPunchOrganizationId();
        //timeRules 不删除,等第二天早上刷新到删除状态
//		deletePunchTimeRuleNotAbleByPunchOrgId(punchOrgId);
        //删除眸状态的 timeRules -- 更新的时候删除次日生效状态的timeRules
        deletePunchTimeRules(punchOrgId, pr.getStatus());
//
//		punchProvider.deletePunchTimeIntervalByPunchRuleId(pr.getId());
        List<PunchTimeRule> ptrs = new ArrayList<>();
        if (null != punchGroupDTO.getTimeRules()) {
            for (PunchTimeRuleDTO timeRule : punchGroupDTO.getTimeRules()) {
                if (timeRule.getPunchTimeIntervals() == null || timeRule.getPunchTimeIntervals().size() == 0)
                    continue;
                PunchTimeRule ptr = ConvertHelper.convert(timeRule, PunchTimeRule.class);
                ptr.setRuleType(pr.getRuleType());
                ptr.setOwnerType(PunchOwnerType.ORGANIZATION.getCode());
                ptr.setOwnerId(punchOrgId);
                ptr.setPunchTimesPerDay((byte) (timeRule.getPunchTimeIntervals().size() * 2));
                ptr.setPunchRuleId(pr.getId());
                ptr.setPunchOrganizationId(punchOrgId);
                ptr.setStatus(pr.getStatus());
                ptr.setFlexTimeLong(timeRule.getFlexTime());
                ptr.setCreatorUid(UserContext.current().getUser().getId());
                ptr.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                ptrs.add(ptr);
                if (pr.getRuleType().equals(PunchRuleType.GUDING.getCode())) {
                    //固定班次 默认第二天4点
                    ptr.setDaySplitTimeLong(PunchConstants.ONE_DAY_MS + PunchConstants.DEFAULT_SPLIT_TIME);
                } else {
                    //排班制 是下班时间+弹性时间+结束打卡时间
                    if (timeRule.getHommizationType().equals(PunchHommizationType.LATEARRIVE_LATELEAVE.getCode()))
                        ptr.setDaySplitTimeLong(timeRule.getPunchTimeIntervals().get(timeRule.getPunchTimeIntervals().size() - 1).getLeaveTime()
                                + (timeRule.getFlexTime() == null ? 0 : timeRule.getFlexTime())
                                + timeRule.getEndPunchTime());
                    else {
                        ptr.setDaySplitTimeLong(timeRule.getPunchTimeIntervals().get(timeRule.getPunchTimeIntervals().size() - 1).getLeaveTime()
                                + timeRule.getEndPunchTime());
                    }
                }
                //前端有跨日少一天的bug
                for (PunchTimeIntervalDTO interval : timeRule.getPunchTimeIntervals()) {
                    if (interval.getLeaveTime() < interval.getArriveTime()) {
                        interval.setLeaveTime(interval.getLeaveTime() + PunchConstants.ONE_DAY_MS);
                    }

                }
                saveTimerRuleIntervals(timeRule, ptr);
            }
        }

        //特殊日期
        punchProvider.deletePunchSpecialDaysByPunchOrgId(punchOrgId);
        if (null != punchGroupDTO.getSpecialDays()) {
            for (PunchSpecialDayDTO specialDayDTO : punchGroupDTO.getSpecialDays()) {
                PunchSpecialDay psd = ConvertHelper.convert(specialDayDTO, PunchSpecialDay.class);
//				psd.setStatus(pr.getStatus());
                psd.setOwnerType(PunchOwnerType.ORGANIZATION.getCode());
                psd.setOwnerId(punchGroupDTO.getOwnerId());
                psd.setPunchRuleId(pr.getId());
                psd.setPunchOrganizationId(punchOrgId);
                psd.setRuleDate(new java.sql.Date(specialDayDTO.getRuleDate()));
                if (null != specialDayDTO.getTimeRule()) {
                    PunchTimeRuleDTO timeRule = specialDayDTO.getTimeRule();
                    PunchTimeRule ptr2 = ConvertHelper.convert(timeRule, PunchTimeRule.class);
                    ptr2.setPunchTimesPerDay((byte) (timeRule.getPunchTimeIntervals().size() * 2));
                    ptr2.setFlexTimeLong(timeRule.getFlexTime());
                    //固定班次 默认第二天4点
                    ptr2.setDaySplitTimeLong(PunchConstants.ONE_DAY_MS + PunchConstants.DEFAULT_SPLIT_TIME);
                    saveTimerRuleIntervals(timeRule, ptr2);
                    psd.setTimeRuleId(ptr2.getId());
                }
                punchProvider.createPunchSpecialDay(psd);
            }
        }
        //排班
        this.dbProvider.execute((status) -> {

            if (punchGroupDTO.getRuleType().equals(PunchRuleType.PAIBAN.getCode()) && punchGroupDTO.getSchedulings() != null) {
                List<PunchScheduling> schedulings = new ArrayList<>();
                Long monthLong = 0L;
                for (PunchSchedulingDTO monthScheduling : punchGroupDTO.getSchedulings()) {
                    if (monthScheduling.getMonth() == null)
                        continue;
                    if (monthLong.compareTo(monthScheduling.getMonth()) < 0) {
                        monthLong = monthScheduling.getMonth();
                    }
                    List<PunchScheduling> psList = saveMonthSchedulings(monthScheduling, pr, ptrs);
                    if (null != psList && psList.size() > 0) {
                        schedulings.addAll(psList);
                    }
                }
                if (schedulings.size() > 0) {
                    //批量保存

//				Long t1 = System.currentTimeMillis();
                    List<EhPunchSchedulings> ehPsList = new ArrayList<>();
                    Long beginId = sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(EhPunchSchedulings.class), schedulings.size());

                    for (PunchScheduling ps : schedulings) {
                        ps.setId(beginId++);
                        EhPunchSchedulings eps = ConvertHelper.convert(ps, EhPunchSchedulings.class);
                        ehPsList.add(eps);
                    }
//				Long t2 = System.currentTimeMillis();

//				LOGGER.debug("for schedulings time "+  t2 + "cost: " +(t2-t1));

                    punchSchedulingProvider.batchCreatePunchSchedulings(ehPsList);
                    //把设置的最大月份之后的排班都置为pr的status
                    punchSchedulingProvider.updatePunchSchedulingsStatusByDate(pr.getStatus(), pr.getId(), new java.sql.Date(monthLong));
//				Long t3 = System.currentTimeMillis();

//				LOGGER.debug("batch save schedulings time "+  t3 + "cost: " +(t3-t2));
                }
            }
            return null;
        });
    }

    private List<PunchScheduling> saveMonthSchedulings(PunchSchedulingDTO monthScheduling, PunchRule pr, List<PunchTimeRule> ptrs) {

        //删除今日之后的,并且在monthScheduling的月份的
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(monthScheduling.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date monthBeginDate = calendar.getTime();
        calendar.add(Calendar.MONTH, 1);
        Date monthEndDate = calendar.getTime();
        //只删除当前状态的--比如新增,比如修改
        punchSchedulingProvider.deleteAfterTodayPunchSchedulingByPunchRuleId(pr.getId(),
                monthBeginDate, monthEndDate, pr.getStatus());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        int monthDays = calendar.get(Calendar.DAY_OF_MONTH);

        List<PunchScheduling> schedulings = new ArrayList<>();
        if (null != monthScheduling.getEmployees()) {
            for (PunchSchedulingEmployeeDTO r : monthScheduling.getEmployees()) {
                if (r.getUserId() == null) {
                    continue;
                }
                List<PunchScheduling> psList = saveEmployeeScheduling(r, monthScheduling.getMonth(), pr, ptrs, monthDays);
                if (null != psList && psList.size() > 0) {
                    schedulings.addAll(psList);
                }
            }
        }
        return schedulings;
    }

    private void deletePunchOvertimeRules(Long punchRuleId, Byte status) {
        List<PunchOvertimeRule> punchOvertimeRules = punchProvider.findPunchOvertimeRulesByPunchRuleId(punchRuleId, status);
        if (CollectionUtils.isEmpty(punchOvertimeRules)) {
            return;
        }
        for (PunchOvertimeRule punchOvertimeRule : punchOvertimeRules) {
            punchOvertimeRule.setStatus(PunchRuleStatus.DELETED.getCode());
            punchProvider.updatePunchOvertimeRule(punchOvertimeRule);
        }
    }

    private void deletePunchTimeRules(Long punchOrgId, Byte status) {
        List<PunchTimeRule> timeRules = punchProvider.listActivePunchTimeRuleByOwner(PunchOwnerType.ORGANIZATION.getCode(), punchOrgId, status);
        if (null != timeRules) {
            for (PunchTimeRule timeRule : timeRules) {

                timeRule.setStatus(PunchRuleStatus.DELETED.getCode());
                punchProvider.updatePunchTimeRule(timeRule);
            }
        }
    }

    private void saveTimerRuleIntervals(PunchTimeRuleDTO timeRule, PunchTimeRule ptr) {

        if (timeRule.getPunchTimeIntervals().size() == 1) {
            ptr.setNoonLeaveTimeLong(timeRule.getNoonLeaveTime());
            ptr.setAfternoonArriveTimeLong(timeRule.getAfternoonArriveTime());
            ptr.setStartEarlyTimeLong(timeRule.getPunchTimeIntervals().get(0).getArriveTime());
            ptr.setStartLateTimeLong(timeRule.getPunchTimeIntervals().get(0).getArriveTime() + (timeRule.getFlexTime() == null ? 0 : timeRule.getFlexTime()));
            ptr.setWorkTimeLong(timeRule.getPunchTimeIntervals().get(0).getLeaveTime() - timeRule.getPunchTimeIntervals().get(0).getArriveTime());
            punchProvider.createPunchTimeRule(ptr);
        } else if (timeRule.getPunchTimeIntervals().size() == 2) {
            ptr.setStartEarlyTimeLong(timeRule.getPunchTimeIntervals().get(0).getArriveTime());
            ptr.setStartLateTimeLong(timeRule.getPunchTimeIntervals().get(0).getArriveTime() + (timeRule.getFlexTime() == null ? 0 : timeRule.getFlexTime()));
            ptr.setNoonLeaveTimeLong(timeRule.getPunchTimeIntervals().get(0).getLeaveTime());
            ptr.setAfternoonArriveTimeLong(timeRule.getPunchTimeIntervals().get(1).getArriveTime());
            ptr.setWorkTimeLong(timeRule.getPunchTimeIntervals().get(1).getLeaveTime() - timeRule.getPunchTimeIntervals().get(0).getArriveTime());
            punchProvider.createPunchTimeRule(ptr);
        } else {
            punchProvider.createPunchTimeRule(ptr);
            for (PunchTimeIntervalDTO interval : timeRule.getPunchTimeIntervals()) {
                PunchTimeInterval ptInterval = ConvertHelper.convert(ptr, PunchTimeInterval.class);
                ptInterval.setArriveTimeLong(interval.getArriveTime());
                ptInterval.setLeaveTimeLong(interval.getLeaveTime());
                ptInterval.setPunchRuleId(ptr.getPunchRuleId());
                ptInterval.setTimeRuleId(ptr.getId());
                punchProvider.createPunchTimeInterval(ptInterval);

            }
        }

    }

    private List<PunchScheduling> saveEmployeeScheduling(PunchSchedulingEmployeeDTO r, Long month, PunchRule pr, List<PunchTimeRule> ptrs, int monthDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(month);
        List<PunchScheduling> schedulings = new ArrayList<>();
        int i = 1;
        for (String ruleName : r.getDaySchedulings()) {
            if (i > monthDays) {
                break;
            }
            PunchTimeRule ptr = findPtrByName(ptrs, ruleName);
            calendar.set(Calendar.DAY_OF_MONTH, i);
            PunchScheduling ps = ConvertHelper.convert(pr, PunchScheduling.class);
            ps.setPunchRuleId(pr.getId());

            ps.setRuleDate(new java.sql.Date(calendar.getTimeInMillis()));
            if (ps.getRuleDate().before(new java.sql.Date(new Date().getTime()))) {
                //今天之前continue
                i++;
                continue;
            }
            if (null != ptr) {
                ps.setTimeRuleId(ptr.getId());
            } else {
                ps.setTimeRuleId(0L);
            }
            ps.setTargetType(PunchTargetType.USER.getCode());
            ps.setTargetId(r.getUserId());
            ps.setStatus(pr.getStatus());
            schedulings.add(ps);
            i++;
        }
        return schedulings;
    }

    private PunchTimeRule findPtrByName(List<PunchTimeRule> ptrs, String ruleName) {
        for (PunchTimeRule ptr : ptrs) {
            if (ptr.getName().equals(ruleName))
                return ptr;
        }
        return null;
    }

    private PunchWifi convertDTO2Wifi(PunchWiFiDTO wifi) {
        PunchWifi punchWifi = ConvertHelper.convert(wifi, PunchWifi.class);
        punchWifi.setOwnerType(PunchOwnerType.ORGANIZATION.getCode());
        punchWifi.setCreatorUid(UserContext.current().getUser().getId());
        punchWifi.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
                .getTime()));
        return punchWifi;
    }

    private PunchGeopoint convertDTO2GeoPoint(PunchGeoPointDTO point) {
        PunchGeopoint punchGeopoint = ConvertHelper.convert(point, PunchGeopoint.class);
        punchGeopoint.setOwnerType(PunchOwnerType.ORGANIZATION.getCode());
        punchGeopoint.setCreatorUid(UserContext.current().getUser().getId());
        punchGeopoint.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        punchGeopoint.setGeohash(GeoHashUtils.encode(
                punchGeopoint.getLatitude(), punchGeopoint.getLongitude()));
        return punchGeopoint;
    }

    @Override
    public PunchGroupDTO getPunchGroup(GetPunchGroupCommand cmd) {
        Organization org = organizationProvider.findOrganizationById(cmd.getId());
        PunchRule pr = punchProvider.getPunchruleByPunchOrgId(org.getId());
        addOperateLog(pr.getId(), pr.getName(), cmd.toString(), pr.getOwnerId(), "getPunchGroup");

        return getPunchGroupDTOByOrg(org);
    }

    ThreadLocal<Map<Long, Organization>> detailIdAndDepartmentMapCache = new ThreadLocal<Map<Long, Organization>>() {
        protected Map<Long, Organization> initialValue() {
            return new HashMap<>();
        }
    };

    ThreadLocal<Map<Long, Organization>> idAndOrganizationMapCache = new ThreadLocal<Map<Long, Organization>>() {
        protected Map<Long, Organization> initialValue() {
            return new HashMap<>();
        }
    };

    public Organization getDepartmentFromLocalCache(Long detailId) {
        Organization organization = detailIdAndDepartmentMapCache.get().get(detailId);
        if (organization == null) {
            Map<Long, String> dptMap = archivesService.getEmployeeDepartment(detailId);
            if (dptMap == null || dptMap.isEmpty()) {
                return null;
            }
            Long deptId = dptMap.keySet().iterator().next();
            organization = idAndOrganizationMapCache.get().get(deptId);
            if (organization == null) {
                organization = organizationProvider.findOrganizationById(deptId);
                idAndOrganizationMapCache.get().put(deptId, organization);
            }
            detailIdAndDepartmentMapCache.get().put(detailId, organization);
        }
        return organization;
    }

    @Override
    public ListPunchGroupsResponse listPunchGroups(ListPunchGroupsCommand cmd) {
        ListPunchGroupsResponse response = new ListPunchGroupsResponse();
        Integer currentVersion = getPunchGroupCurrentVersion(cmd.getOwnerId());
        Long creatorUid = null;
        if (!checkBooleanAppPrivilege(cmd.getOwnerId(), cmd.getDeptId() == null ? cmd.getOwnerId() : cmd.getDeptId(), PrivilegeConstants.PUNCH_RULE_QUERY_ALL)) {
            //没通过全部校验的就要进行creator校验
            if ((checkBooleanAppPrivilege(cmd.getOwnerId(), cmd.getDeptId() == null ? cmd.getOwnerId() : cmd.getDeptId(), PrivilegeConstants.PUNCH_RULE_QUERY_CREATOR))) {
                creatorUid = UserContext.current().getUser().getId();
            } else {
                throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_CHECK_APP_PRIVILEGE,
                        "check app privilege error");
            }
        }
        addOperateLog(0L, "", cmd.toString(), cmd.getOwnerId(), "listPunchGroups");


        //  获取所有批次
        if (cmd.getPageAnchor() == null)
            cmd.setPageAnchor(0L);
        int pageSize = cmd.getPageSize() == null ? 5 : cmd.getPageSize();
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        List<Long> orgIds = null;
        //员工姓名和部门
        if (null != cmd.getDeptId() && !cmd.getOwnerId().equals(cmd.getDeptId())) {
            orgIds = new ArrayList<>();
            UniongroupConfigures unc = uniongroupConfigureProvider.findUniongroupConfiguresByCurrentId(
                    UserContext.getCurrentNamespaceId(), cmd.getDeptId(), UniongroupType.PUNCHGROUP.getCode(), CONFIG_VERSION_CODE, UniongroupTargetType.ORGANIZATION.getCode());
            if (null != unc)
                orgIds.add(unc.getGroupId());

        } else if (null != cmd.getUserName()) {
            orgIds = new ArrayList<>();
            List<UniongroupMemberDetail> details = uniongroupConfigureProvider.listUniongroupMemberDetailsByUserName(cmd.getOwnerId(), cmd.getUserName());
            if (null != details && details.size() > 0) {
                for (UniongroupMemberDetail detail : details) {
                    orgIds.add(detail.getGroupId());
                }
            }

        }

        List<Organization> organizations = this.organizationProvider.listOrganizationsByGroupType(UniongroupType.PUNCHGROUP.getCode(), cmd.getOwnerId(),
                orgIds, cmd.getGroupName(), creatorUid, locator, pageSize + 1);

        if (null == organizations)
            return response;
        Long nextPageAnchor = null;
        if (organizations != null && organizations.size() > pageSize) {
            organizations.remove(organizations.size() - 1);
            nextPageAnchor = organizations.get(organizations.size() - 1).getId();
        }
        response.setNextPageAnchor(nextPageAnchor);
        List<PunchGroupDTO> punchGroups = new ArrayList<>();
        for (Organization r : organizations) {
            PunchGroupDTO dto = getPunchGroupDTOByOrg(r);
            //2018年2月7日 by wh  :如果dto为空,就是找不到punchrule,那就是脏数据删掉
            if (null == dto) {
                organizationProvider.deleteOrganization(r);
            }
            if (null != dto) {
                punchGroups.add(dto);
            }
        }
        response.setPunchGroups(punchGroups);
        return response;
    }

    @Override
    public ListApprovalCategoriesResponse listApprovalCategories(ListApprovalCategoriesCommand cmd, HttpServletRequest request) {
        cmd.setOwnerType(ApprovalOwnerType.ORGANIZATION.getCode());
        // 接口兼容APP旧版本没传参数的场景，返回除年假和调休外的所有请假类型，无论开启已否
        if (cmd.getNamespaceId() == null || cmd.getOwnerId() == null) {
            return new ListApprovalCategoriesResponse(approvalService.listBaseApprovalCategory(Arrays.asList("年假", "调休")), null);
        }
        ListApprovalCategoryCommand listApprovalCategoryCommand = new ListApprovalCategoryCommand();
        listApprovalCategoryCommand.setNamespaceId(cmd.getNamespaceId());
        listApprovalCategoryCommand.setOwnerType(cmd.getOwnerType());
        listApprovalCategoryCommand.setOwnerId(cmd.getOwnerId());
        listApprovalCategoryCommand.setApprovalType(ApprovalType.ABSENCE.getCode());
        listApprovalCategoryCommand.setStatusList(Arrays.asList(ApprovalCategoryStatus.ACTIVE_FOREVER.getCode(), ApprovalCategoryStatus.ACTIVE.getCode()));
        List<ApprovalCategoryDTO> categories = approvalService.initAndListApprovalCategory(listApprovalCategoryCommand).getCategoryList();

        buildMoreInfoOfCurrentUser(cmd.getOwnerId(), categories);
        return new ListApprovalCategoriesResponse(categories, processApprovalCategorieUrl(cmd.getOwnerId(), cmd.getNamespaceId(), request));
    }

    // 生成不同请假类型的提示信息和当前用户的假期余额
    private void buildMoreInfoOfCurrentUser(Long organizationId, List<ApprovalCategoryDTO> categories) {
        if (CollectionUtils.isEmpty(categories)) {
            return;
        }
        PunchVacationBalance punchVacationBalance = null;
        if (organizationId != null && organizationId != 0) {
            OrganizationMember organizationMember = organizationProvider.findActiveOrganizationMemberByOrgIdAndUId(UserContext.currentUserId(), organizationId);
            if (organizationMember != null) {
                punchVacationBalance = punchVacationBalanceProvider.findPunchVacationBalanceByDetailId(organizationMember.getDetailId());
            }
        }
        String locale = UserContext.current().getUser() == null ? PunchConstants.locale : UserContext.current().getUser().getLocale();
        String dayUnit = localeStringService.getLocalizedString(ApprovalServiceConstants.SCOPE, String.valueOf(ApprovalServiceConstants.TIME_UNIT_OF_DAY), locale, "天");
        String hourUnit = localeStringService.getLocalizedString(ApprovalServiceConstants.SCOPE, String.valueOf(ApprovalServiceConstants.TIME_UNIT_OF_HOUR), locale, "小时");
        Map<String, String> model = new HashMap<>();
        for (ApprovalCategoryDTO category : categories) {
            model.put("timeStep", category.getTimeStep().toString());
            model.put("timeUnit", ApprovalCategoryTimeUnit.DAY == ApprovalCategoryTimeUnit.fromCode(category.getTimeUnit()) ? dayUnit : hourUnit);
            model.put("categoryName", category.getCategoryName());
            model.put("remainCountDisplay", "");
            category.setRemainCount(0D);
            int code = ApprovalServiceConstants.TIP_INFO_FOR_APPROVAL_CATEGORY;

            if (ApprovalServiceConstants.ANNUAL_LEAVE.equals(category.getCategoryName())) {
                code = ApprovalServiceConstants.TIP_INFO_FOR_REMAIN_APPROVAL_CATEGORY;
                if (punchVacationBalance != null) {
                    category.setRemainCount(punchVacationBalance.getAnnualLeaveBalance());
                }
                category.setRemainCountDisplay(PunchDayParseUtils.parseDayTimeDisplayStringZeroWithUnit(category.getRemainCount(), dayUnit, hourUnit));
                model.put("remainCountDisplay", category.getRemainCountDisplay());
            } else if (ApprovalServiceConstants.OVERTIME_COMPENSATION.equals(category.getCategoryName())) {
                code = ApprovalServiceConstants.TIP_INFO_FOR_REMAIN_APPROVAL_CATEGORY;
                if (punchVacationBalance != null) {
                    category.setRemainCount(punchVacationBalance.getOvertimeCompensationBalance());
                }
                category.setRemainCountDisplay(PunchDayParseUtils.parseDayTimeDisplayStringZeroWithUnit(category.getRemainCount(), dayUnit, hourUnit));
                model.put("remainCountDisplay", category.getRemainCountDisplay());
            }

            String timeUnitTipInfo = localeTemplateService.getLocaleTemplateString(ApprovalServiceConstants.SCOPE, code, UserContext.current().getUser().getLocale(), model, "");
            category.setTimeUnitTipInfo(timeUnitTipInfo);
        }
    }

    private PunchGroupDTO getPunchGroupDTOByOrg(Organization r) {
        PunchRule pr = punchProvider.getPunchruleByPunchOrgId(r.getId());
        if (null == pr)
            return null;
        PunchGroupDTO dto = ConvertHelper.convert(pr, PunchGroupDTO.class);
        dto.setGroupName(r.getName());
        if (null != pr.getOperateTime())
            dto.setOperateTime(pr.getOperateTime().getTime());
        if (null != pr.getOperatorUid()) {
            Organization org = organizationProvider.findOrganizationById(r.getDirectlyEnterpriseId());
            OrganizationMember member = findOrganizationMemberByOrgIdAndUId(pr.getOperatorUid(), org.getPath());
            if (null != member) {
                dto.setOperatorName(member.getContactName());
            }
            dto.setOperatorUid(pr.getOperatorUid());
        }
        dto.setId(pr.getPunchOrganizationId());

//		if (pr.getStatus().equals(PunchRuleStatus.NEW.getCode()) || pr.getStatus().equals(PunchRuleStatus.MODIFYED.getCode())) {
//			versionCode = 1;
//		}
        List<UniongroupMemberDetail> employees = uniongroupConfigureProvider.listUniongroupMemberDetail(r.getId(), CONFIG_VERSION_CODE);
        dto.setEmployeeCount(employees == null ? 0 : employees.size());
        List<Long> detailIds = new ArrayList<>();
        if (null != employees) {
            dto.setEmployees(new ArrayList<>());
            for (UniongroupMemberDetail detail : employees) {
                UniongroupTarget target = new UniongroupTarget();
                target.setName(detail.getContactName());
                target.setId(detail.getDetailId());
                target.setType(UniongroupTargetType.MEMBERDETAIL.getCode());
                dto.getEmployees().add(target);
                detailIds.add(detail.getDetailId());
            }
        }
        // 关联 人员和机构
        GetUniongroupConfiguresCommand cmd1 = new GetUniongroupConfiguresCommand();
        cmd1.setGroupId(r.getId());
        List<UniongroupConfiguresDTO> resp = uniongroupService.getConfiguresInfosListByGroupId(cmd1);
        dto.setTargets(new ArrayList<UniongroupTarget>());
        if (null != resp) {
            for (UniongroupConfiguresDTO obj : resp) {
                UniongroupTarget target = new UniongroupTarget();
                target.setName(obj.getCurrentName());
                target.setId(obj.getCurrentId());
                target.setType(obj.getCurrentType());
                dto.getTargets().add(target);
            }
        }
        //打卡时间
        dto.setTimeRules(processPunchTimeRuleDTOs(r.getId(), pr.getStatus()));


        //打卡地点和WiFi
        dto.setPunchGeoPoints(processPunchGeoPointDTOs(r.getId()));
        dto.setWifis(processPunchWiFiDTOs(r.getId()));



        //排班和非排班的特殊处理
        if (pr.getRuleType().equals(PunchRuleType.PAIBAN.getCode())) {
            Calendar start = Calendar.getInstance();
            start.set(Calendar.DAY_OF_MONTH, 1);
            setCalendarDateBegin(start);
            Calendar end = Calendar.getInstance();
            end.setTime(start.getTime());
            end.add(Calendar.MONTH, 1);
            Integer linkedCount = punchSchedulingProvider.countSchedulingUser(pr.getId(), new java.sql.Date(start.getTimeInMillis()), new java.sql.Date(end.getTimeInMillis()), detailIds);
            dto.setUnSchedulingCount(dto.getEmployeeCount() - linkedCount);
            //排班
            List<PunchSchedulingDTO> schedulings = processschedulings(pr, new java.sql.Date(start.getTimeInMillis()), new java.sql.Date(end.getTimeInMillis()));
            dto.setSchedulings(schedulings);
        } else {
            //特殊日期
            List<PunchSpecialDay> specialDays = punchProvider.listPunchSpecailDaysByOrgId(pr.getPunchOrganizationId());
            if (null != specialDays) {
                dto.setSpecialDays(new ArrayList<>());
                for (PunchSpecialDay specialDay : specialDays) {
                    PunchSpecialDayDTO dto1 = ConvertHelper.convert(specialDay, PunchSpecialDayDTO.class);
                    dto1.setRuleDate(specialDay.getRuleDate().getTime());
                    if (null != specialDay.getTimeRuleId()) {
                        PunchTimeRule timeRule = punchProvider.getPunchTimeRuleById(specialDay.getTimeRuleId());
                        if (null != timeRule) {
                            PunchTimeRuleDTO timeRuleDTO = convertPunchTimeRule2DTO(timeRule);
                            dto1.setTimeRule(timeRuleDTO);
                        }
                    }
                    dto.getSpecialDays().add(dto1);
                }
            }
        }
        dto.setPunchOvertimeRules(processPunchOvertimeRuleDTOs(pr.getId(),pr.getStatus()));

        return dto;
    }

    private List<PunchTimeRuleDTO> processPunchTimeRuleDTOs(Long pucnhOrgId, Byte status) {
        List<PunchTimeRule> timeRules = punchProvider.listActivePunchTimeRuleByOwner(PunchOwnerType.ORGANIZATION.getCode(), pucnhOrgId, status);
    	if (null != timeRules && timeRules.size() > 0)
            return timeRules.stream().map(r1 -> {
                PunchTimeRuleDTO dto1 = convertPunchTimeRule2DTO(r1);
                if (r1.getPunchTimesPerDay().equals((byte) 2)) {
                    dto1.setAfternoonArriveTime(r1.getAfternoonArriveTimeLong());
                    dto1.setNoonLeaveTime(r1.getNoonLeaveTimeLong());
                }
                return dto1;
            }).collect(Collectors.toList());
		return null;
	}

	private List<PunchWiFiDTO> processPunchWiFiDTOs(Long pucnhOrgId) {
    	List<PunchWifi> wifis = punchProvider.listPunchWifsByOwner(PunchOwnerType.ORGANIZATION.getCode(), pucnhOrgId);
    	if (!CollectionUtils.isEmpty(wifis))
    		return wifis.stream().map(punchOvertimeRule -> {
                return ConvertHelper.convert(punchOvertimeRule, PunchWiFiDTO.class);
            }).collect(Collectors.toList());
		return null;
	}

	private List<PunchGeoPointDTO> processPunchGeoPointDTOs(Long pucnhOrgId) {
    	List<PunchGeopoint> points = punchProvider.listPunchGeopointsByOwner(PunchOwnerType.ORGANIZATION.getCode(), pucnhOrgId);
        if (!CollectionUtils.isEmpty(points))
        	return points.stream().map(punchOvertimeRule -> {
        		return ConvertHelper.convert(punchOvertimeRule, PunchGeoPointDTO.class);
            }).collect(Collectors.toList());
		return null;
	}

	private List<PunchOvertimeRuleDTO> processPunchOvertimeRuleDTOs(Long ruleId, Byte status) {
    	List<PunchOvertimeRule> punchOvertimeRules = punchProvider.findPunchOvertimeRulesByPunchRuleId(ruleId, status);
        if (!CollectionUtils.isEmpty(punchOvertimeRules)) {
            return punchOvertimeRules.stream().map(punchOvertimeRule -> {
                return ConvertHelper.convert(punchOvertimeRule, PunchOvertimeRuleDTO.class);
            }).collect(Collectors.toList());
        }
		return null;
	}

	private List<PunchSchedulingDTO> processschedulings(PunchRule pr, java.sql.Date startDate,
                                                        java.sql.Date endDate) {
        List<PunchSchedulingDTO> result = new ArrayList<PunchSchedulingDTO>();
        PunchSchedulingDTO dto = new PunchSchedulingDTO();
        dto.setMonth(startDate.getTime());
        Calendar tomorrowCalendar = Calendar.getInstance();
        tomorrowCalendar.add(Calendar.DAY_OF_MONTH, 1);
        java.sql.Date tomorrow = new java.sql.Date(tomorrowCalendar.getTimeInMillis());
        if (tomorrow.before(startDate)) {
            //防止把无关数据查出来
            tomorrow.setTime(startDate.getTime());
        }
        //今日之后用pr的status查,之前用active查
        List<PunchScheduling> schedulings = punchSchedulingProvider.queryPunchSchedulings(tomorrow, endDate, pr.getId(), pr.getStatus());
        if ((null == schedulings || schedulings.size() == 0)
//				&&PunchRuleStatus.ACTIVE!=PunchRuleStatus.fromCode(pr.getStatus())
                ) {
            //如果查不到当前状态的就 并且pr 的状态不是正常
            schedulings = punchSchedulingProvider.queryPunchSchedulings(tomorrow, endDate, pr.getId(), PunchRuleStatus.ACTIVE.getCode());
//			LOGGER.debug("取正常状态的schedulings "+StringHelper.toJsonString(schedulings));
        } else {
//			LOGGER.debug("他说有schedulings "+StringHelper.toJsonString(schedulings));
        }
        if (null == schedulings) {
            schedulings = new ArrayList<>();
        }
        List<PunchScheduling> schedulings2 = punchSchedulingProvider.queryPunchSchedulings(startDate, tomorrow, pr.getId(), PunchRuleStatus.ACTIVE.getCode());
        if (null != schedulings2) {
            schedulings.addAll(schedulings2);
        }
        if (null != schedulings) {
            Map<Long, List<PunchScheduling>> scheMap = new HashMap<>();
            for (PunchScheduling sche : schedulings) {
                if (scheMap.get(sche.getTargetId()) == null)
                    scheMap.put(sche.getTargetId(), new ArrayList<PunchScheduling>());
                scheMap.get(sche.getTargetId()).add(sche);
            }
            List<PunchSchedulingEmployeeDTO> employeeDTOs = new ArrayList<PunchSchedulingEmployeeDTO>();
            //循环每个人
            for (Long detaliId : scheMap.keySet()) {
                PunchSchedulingEmployeeDTO employeeDTO = new PunchSchedulingEmployeeDTO();
                employeeDTO.setDaySchedulings(new ArrayList<>());
                employeeDTO.setUserId(detaliId);
                OrganizationMemberDetails memberDetail = findOrganizationMemberDetailByCacheDetailId(detaliId);
                if (null != memberDetail)
                    employeeDTO.setContactName(memberDetail.getContactName());
                //循环每个人这个月每一天
                Calendar start = Calendar.getInstance();
                start.setTime(startDate);
                Calendar end = Calendar.getInstance();
                end.setTime(endDate);
                for (; start.before(end); start.add(Calendar.DAY_OF_MONTH, 1)) {
                    PunchScheduling scheduling = findSchedlingByDate(scheMap.get(detaliId), new java.sql.Date(start.getTimeInMillis()));
                    if (null != scheduling) {
                        if (scheduling.getTimeRuleId() == null || scheduling.getTimeRuleId().equals(0L)) {
                            employeeDTO.getDaySchedulings().add("休息");
                        } else {
                            PunchTimeRule ptr = punchProvider.findPunchTimeRuleById(scheduling.getTimeRuleId());
                            if (null != ptr) {
                                employeeDTO.getDaySchedulings().add(ptr.getName());
                            } else {
                                employeeDTO.getDaySchedulings().add("");
                            }
                        }
                    } else {
                        employeeDTO.getDaySchedulings().add("");
                    }
                }
                employeeDTOs.add(employeeDTO);
            }
            dto.setEmployees(employeeDTOs);
        }
        result.add(dto);
        return result;
    }

    private PunchScheduling findSchedlingByDate(List<PunchScheduling> list, java.sql.Date date) {
        if (null != list) {
            String time1 = dateSF.get().format(date);
            for (PunchScheduling sche : list) {

                String time2 = dateSF.get().format(sche.getRuleDate());
                if (time1.equals(time2) && sche.getTimeRuleId() != null)
                    return sche;
            }
        }
        return null;
    }

    private PunchTimeRuleDTO convertPunchTimeRule2DTO(PunchTimeRule r) {
        if (null == r) {
            return null;
        }
        PunchTimeRuleDTO dto = ConvertHelper.convert(r, PunchTimeRuleDTO.class);
        dto.setFlexTime(r.getFlexTimeLong());
        dto.setNoonLeaveTime(r.getNoonLeaveTimeLong());
        dto.setBeginPunchTime(r.getBeginPunchTime());
        dto.setEndPunchTime(r.getEndPunchTime());
        dto.setAfternoonArriveTime(r.getAfternoonArriveTimeLong());
        dto.setPunchTimeIntervals(new ArrayList<>());
        if (r.getPunchTimesPerDay().equals((byte) 2)) {
            PunchTimeIntervalDTO intervalDTO = new PunchTimeIntervalDTO();
            intervalDTO.setArriveTime(r.getStartEarlyTimeLong());
            intervalDTO.setLeaveTime(r.getStartEarlyTimeLong() + r.getWorkTimeLong());
            dto.getPunchTimeIntervals().add(intervalDTO);
        } else if (r.getPunchTimesPerDay().equals((byte) 4)) {
            PunchTimeIntervalDTO intervalDTO = new PunchTimeIntervalDTO();
            intervalDTO.setArriveTime(r.getStartEarlyTimeLong());
            intervalDTO.setLeaveTime(r.getNoonLeaveTimeLong());
            dto.getPunchTimeIntervals().add(intervalDTO);
            intervalDTO = new PunchTimeIntervalDTO();
            intervalDTO.setArriveTime(r.getAfternoonArriveTimeLong());
            intervalDTO.setLeaveTime(r.getStartEarlyTimeLong() + r.getWorkTimeLong());
            dto.getPunchTimeIntervals().add(intervalDTO);
        } else {
            List<PunchTimeInterval> intervals = punchProvider.listPunchTimeIntervalByTimeRuleId(r.getId());
            if (null != intervals)
                for (PunchTimeInterval interval : intervals) {
                    PunchTimeIntervalDTO intervalDTO = ConvertHelper.convert(interval, PunchTimeIntervalDTO.class);
                    intervalDTO.setArriveTime(interval.getArriveTimeLong());
                    intervalDTO.setLeaveTime(interval.getLeaveTimeLong());
                    dto.getPunchTimeIntervals().add(intervalDTO);
                }
        }

        return dto;
    }

    @Override
    public PunchGroupDTO updatePunchGroup(PunchGroupDTO cmd) {
        if (cmd.getRuleType() == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid rule type parameter in the command");
        if (NormalFlag.fromCode(cmd.getPunchRemindFlag()) == null) {
            cmd.setPunchRemindFlag(NormalFlag.NO.getCode());
        }
        if (cmd.getRemindMinutesOnDuty() == null || NormalFlag.NO == NormalFlag.fromCode(cmd.getPunchRemindFlag())) {
            cmd.setRemindMinutesOnDuty(0);
        }
        //获取考勤组
        Organization punchOrg = this.organizationProvider.findOrganizationById(cmd.getId());
        checkAppPrivilege(punchOrg.getDirectlyEnterpriseId(), punchOrg.getDirectlyEnterpriseId(), PrivilegeConstants.PUNCH_RULE_UPDATE);

        punchOrg.setName(cmd.getGroupName());
        organizationService.checkNameRepeat(punchOrg.getDirectlyEnterpriseId(), cmd.getGroupName(), punchOrg.getGroupType(), punchOrg.getId());
        organizationProvider.updateOrganization(punchOrg);
        PunchRule pr = punchProvider.getPunchruleByPunchOrgId(cmd.getId());

        addOperateLog(pr.getId(), pr.getName(), cmd.toString(), pr.getOwnerId(), "updatePunchGroup");

        //添加关联
        SaveUniongroupConfiguresCommand command = new SaveUniongroupConfiguresCommand();
        command.setGroupId(punchOrg.getId());
        command.setGroupType(UniongroupType.PUNCHGROUP.getCode());
        command.setEnterpriseId(cmd.getOwnerId());
        command.setTargets(cmd.getTargets());
        //次日生效的versioncode为1
        command.setVersionCode(CONFIG_VERSION_CODE);
        try {
            this.uniongroupService.saveUniongroupConfigures(command);
        } catch (NoNodeAvailableException e) {
            LOGGER.error("NoNodeAvailableException", e);
        }
        Long t8 = System.currentTimeMillis();
        //打卡地点和wifi
        saveGeopointsAndWifis(punchOrg.getId(), cmd.getPunchGeoPoints(), cmd.getWifis());


        //打卡时间,特殊日期,排班等
        pr.setOwnerType(PunchOwnerType.ORGANIZATION.getCode());
        pr.setOwnerId(cmd.getOwnerId());
        pr.setChinaHolidayFlag(cmd.getChinaHolidayFlag());
        if (PunchRuleType.GUDING == PunchRuleType.fromCode(cmd.getRuleType())) {
            pr.setPunchRemindFlag(cmd.getPunchRemindFlag());
            pr.setRemindMinutesOnDuty(cmd.getRemindMinutesOnDuty());
        } else {
            pr.setPunchRemindFlag(NormalFlag.NO.getCode());
            pr.setRemindMinutesOnDuty(0);
        }
        pr.setName(cmd.getGroupName());
        pr.setRuleType(cmd.getRuleType());
        pr.setPunchOrganizationId(punchOrg.getId());
        if (PunchRuleStatus.NEW.getCode() != pr.getStatus().byteValue())
            pr.setStatus(PunchRuleStatus.MODIFYED.getCode());
        pr.setOperatorUid(UserContext.current().getUser().getId());
        pr.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        // 新增一条修改状态的规则
        punchProvider.updatePunchRule(pr);
//		punchProvider.deletePunchTimeRuleByRuleId(pr.getId());

        savePunchTimeRule(cmd, pr);
        savePunchOvertimeRules(cmd.getPunchOvertimeRules(), pr);
        //删除不在考勤组的排班
//			punchSchedulingProvider.deletePunchSchedulingByPunchRuleIdAndNotInTarget(pr.getId(), detailIds);
        Long t9 = System.currentTimeMillis();
        LOGGER.debug("saveUnion Time9 " + t9 + "cost: " + (t9 - t8));

        return null;
    }

    private void addOperateLog(Long ruleId, String name, String string, Long orgId, String api) {
        // TODO Auto-generated method stub
        PunchOperationLog log = new PunchOperationLog();
        log.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        log.setNamespaceId(UserContext.getCurrentNamespaceId());
        log.setOperatorUid(UserContext.currentUserId());
        User user = userProvider.findUserById(UserContext.currentUserId());
        if (null != user)
            log.setOperatorName(user.getNickName());
        log.setOperateApi(api);
        log.setOrganizationId(orgId);
        log.setRuleId(ruleId);
        log.setRuleName(name);
        log.setRequestParameter(string);
        punchOperationLogProvider.createPunchOperationLog(log);
    }

    @Override
    public GetPunchDayStatusResponse getPunchDayStatus(GetPunchDayStatusCommand cmd) {
        //是否刷新当日打卡的flag,在查当天(没有queryTime)和queryTime大于今天的情况下 不刷新
        GetPunchDayStatusResponse response = new GetPunchDayStatusResponse();

        Long userId = UserContext.current().getUser().getId();
        if (null != cmd.getUserId()) {
            userId = cmd.getUserId();
        }
        OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByTargetId(userId, cmd.getEnterpriseId());
        if(null !=detail){
            response.setDetailId(detail.getId());
        }
        response.setUserId(userId);
        response.setEnterpriseId(cmd.getEnterpriseId());

        cmd.setEnterpriseId(getTopEnterpriseId(cmd.getEnterpriseId()));
        PunchRule pr = getPunchRule(detail);
        Date punchTime = new Date();
        Calendar punCalendar = Calendar.getInstance();
        punCalendar.setTime(punchTime);
        //现在打卡属于哪一天
        java.sql.Date pDate = new java.sql.Date(punchTime.getTime());
        if (null == cmd.getQueryTime()) {
            cmd.setQueryTime(punchTime.getTime());
            if (null != pr) {
                pDate = calculatePunchDate(punCalendar, cmd.getEnterpriseId(), userId);
                PunchLogDTO punchLog = getPunchType(userId, cmd.getEnterpriseId(), punchTime, pDate);
                if (null != punchLog) {

                    if (null != punchLog.getExpiryTime()) {
                        punchLog.setExpiryTime(process24hourTimeToGMTTime(pDate, punchLog.getExpiryTime()));
                    }
                    punchLog.setRuleTime(process24hourTimeToGMTTime(punchTime, punchLog.getRuleTime()));
                    response = ConvertHelper.convert(punchLog, GetPunchDayStatusResponse.class);
                    response.setClockStatus(punchLog.getClockStatus());
                }
                punchTime = pDate;
                //2018年4月11日 修改: 之前punCalendar就没用了,所以没有赋值为计算出来的打卡日,但是现在使用了punCalendar作为后面计算所以要赋值
                punCalendar.setTime(punchTime);
            }
            //前段有用这个punchDate做分割今天还是明天,所以没有pr也要返回一个今天给客户端
            response.setPunchDate(pDate.getTime());

        } else {
            punchTime = new Date(cmd.getQueryTime());
            pDate = new java.sql.Date(punchTime.getTime());
            punCalendar.setTime(punchTime);
        }

        response.setIntervals(new ArrayList<>());
        //这里之前用cmd.getQueryTime 查询,现在改用punchTime ,他们的区别是查当天的时候是查出来今天的还是打卡日的
        PunchDayLog pdl = punchProvider.findPunchDayLog(userId, cmd.getEnterpriseId(), new java.sql.Date(punchTime.getTime()));
        List<PunchLog> punchLogs = punchProvider.listPunchLogsByDate(userId, cmd.getEnterpriseId(), dateSF.get().format(punchTime),
                ClockCode.SUCESS.getCode());
        PunchTimeRule ptr = null;
        if (null != pr) {
            ptr = getPunchTimeRuleWithPunchDayTypeByRuleIdAndDate(pr, punchTime, userId);
        }
        //2018年08月08日 暂时注释掉:对于之前没有刷新pdl的查询的时候也不刷新了(防止未设置规则)
//        if (refreshDayLogFlag && null != ptr && pdl == null) {
//            //2018年4月11日 修改:
//            //之前直接添加一条,理论上应该是没问题的,但是现网出错了,看来这里需要先确定有没有旧数据
//            //所以现在还是采用new 一个新的pdl进行计算,然后给旧的赋值
//            PunchDayLog newPdl = new PunchDayLog();
//            refreshPunchDayLog(detail, pdl, punCalendar, ptr, newPdl);
//            pdl = newPdl;
//            //refreshPunchDayLog(userId, cmd.getEnterpriseId(), null, punCalendar, ptr, pdl);
//            LOGGER.debug("pdl is {}", StringHelper.toJsonString(pdl));
//        }
        String[] statusList = null;
        String[] approvalStatus = null;
        if (null != pdl) {
            if (pdl.getTimeRuleId() != null && pdl.getTimeRuleId() > 0L) {
                ptr = punchProvider.getPunchTimeRuleById(pdl.getTimeRuleId());
            }
            response.setStatusList(pdl.getStatusList());
            if (null != pdl.getStatusList()) {
                if (!StringUtils.isEmpty(pdl.getApprovalStatusList()))
                    approvalStatus = org.apache.commons.lang.StringUtils.splitPreserveAllTokens(pdl.getApprovalStatusList(), PunchConstants.STATUS_SEPARATOR, pdl.getPunchTimesPerDay() / 2);
                statusList = org.apache.commons.lang.StringUtils.splitPreserveAllTokens(pdl.getStatusList(), PunchConstants.STATUS_SEPARATOR);
                if (statusList.length < pdl.getPunchTimesPerDay() / 2) {
                    if (statusList.length == 1) {
                        statusList = new String[pdl.getPunchTimesPerDay() / 2];
                        for (int i = 0; i < pdl.getPunchTimesPerDay() / 2; i++) {
                            statusList[i] = pdl.getStatusList();
                        }
                    } else
                        statusList = null;
                }
                for (int i = 0; i < pdl.getPunchTimesPerDay() / 2; i++) {
                    try {
                        if (approvalStatus != null && !StringUtils.isEmpty(approvalStatus[i])) {
                            statusList[i] = approvalStatus[i];
                        }
                    } catch (Exception e) {
                        LOGGER.error("approval status error", e);
                    }
                }
            }
        }
        if (null != ptr) {
        	if(null != pdl && !pdl.getStatusList().equals(PunchStatus.NO_ASSIGN_PUNCH_RULE.getCode() + "")){
        		//这里判断如果没有pdl 或者pdl的状态是无规则就是没有规则,不去统计intervals
                if (PunchDayType.WORKDAY == ptr.getPunchDayType()) {
                    response = processWorkdayPunchIntervalDTO(response, pr, ptr, punchLogs, statusList, approvalStatus, pDate);
                } else {
                    PunchOvertimeRule por = getPunchOvertimeRuleByPunchDayType(pr, ptr.getPunchDayType());
                    if (por != null) {
                        response = processHolidayPunchIntervalDTO(response, ptr, punchLogs);
                    }
                }
            }
            //在今天之后的工作日也要组装intervals modify by wh:包括今天,所以只要大于今天0点0分0秒0毫秒
            if (PunchDayType.WORKDAY == ptr.getPunchDayType() && pDate.after(PunchDateUtils.getDateBeginDate(DateHelper.currentGMTTime()))) {
                response = processWorkdayPunchIntervalDTO(response, pr, ptr, punchLogs, statusList, approvalStatus, pDate);
            }
            //找到用户当日申请列表
            List<ExceptionRequestDTO> requestDTOs = listUserException(pDate, userId, cmd.getEnterpriseId(), ptr);
            response.setRequestDTOs(requestDTOs);
        }
        List<PunchGoOutLog> punchGoOutLogs = punchProvider.listPunchGoOutLogs(userId, cmd.getEnterpriseId(), pDate);
        if(CollectionUtils.isNotEmpty(punchGoOutLogs)){
        	response.setGoOutPunchLogs(punchGoOutLogs.stream().map(this :: convertGoOutLogDTO).collect(Collectors.toList()));
        }
//		}
        //旧版本兼容性处理
        oldVersionProcess(response, cmd);
        return response;
    }

    private void oldVersionProcess(GetPunchDayStatusResponse response, GetPunchDayStatusCommand cmd) {
    	if(UserContext.current() != null && UserContext.current().getVersion() != null){
            Version appVersion = Version.fromVersionString(UserContext.current().getVersion());
            if(Version.encodeValue(appVersion.getMajor(), appVersion.getMinor(), appVersion.getRevision()) <
    					Version.encodeValue(5,8,0) ){
            	//2018年8月9日 对5.8.0之前版本的签到签退做特殊处理 -- 修改为非工作日
	        	if(PunchType.fromCode(response.getPunchType()) == PunchType.OVERTIME_ON_DUTY ||
	        			PunchType.fromCode(response.getPunchType()) == PunchType.OVERTIME_OFF_DUTY ){
	        				response.setPunchType(PunchType.NOT_WORKDAY.getCode());
	        	}
	        	if((cmd.getQueryTime() == null ||
	        			dateSF.get().format(DateHelper.currentGMTTime()).equals(dateSF.get().format(new java.sql.Date(cmd.getQueryTime()))))
	        			&& PunchType.fromCode(response.getPunchType()) == PunchType.NOT_WORKDAY  ){
	        		//今日的数据,状态是休息的 intervals清空
	        		response.setIntervals(new ArrayList<>());
	        	}
	        	for(PunchIntevalLogDTO intevalLogDTO : response.getIntervals()){
	        		if(intevalLogDTO.getStatus() == null){
	        			continue;
	        		}
	        		try{
		        		if(PunchStatus.fromCode(Byte.valueOf(intevalLogDTO.getStatus())) == PunchStatus.FORGOT_OFF_DUTY
		        				|| PunchStatus.fromCode(Byte.valueOf(intevalLogDTO.getStatus())) == PunchStatus.BELATE_AND_FORGOT){
		        			intevalLogDTO.setStatus(PunchStatus.FORGOT_ON_DUTY.getCode() + "");
		        		}
	        		}catch(Exception e){
	        			LOGGER.error("版本兼容处理出现了问题", e);
	        		}
	        	}
            }
    	}
	}

	private List<ExceptionRequestDTO> listUserException(java.sql.Date pDate, Long userId, Long enterpriseId, PunchTimeRule ptr) {
    	List<ExceptionRequestDTO> result = new ArrayList<>();
    	//当日+上班打卡时间-最早打卡时间 默认0
    	Timestamp dayStart = new Timestamp(pDate.getTime() +(ptr.getStartEarlyTimeLong() == null ? 0L : ptr.getStartEarlyTimeLong())
    			- (ptr.getBeginPunchTime() == null ? 0L : ptr.getBeginPunchTime()));

    	//当日+切分时间 默认一天
		Timestamp dayEnd = new Timestamp(pDate.getTime() + (ptr.getDaySplitTimeLong() == null ? PunchConstants.ONE_DAY_MS : ptr.getDaySplitTimeLong()));

		List<PunchExceptionRequest> exceptionRequests = punchProvider.listPunchExceptionRequestBetweenBeginAndEndTime(userId,
				enterpriseId, dayStart, dayEnd);
		if (null == exceptionRequests) {
		    exceptionRequests = new ArrayList<>();
		}
		List<PunchExceptionRequest> requests2 = punchProvider.listpunchexceptionRequestByDate(userId,enterpriseId, pDate);
		if (null != requests2) {
			exceptionRequests.addAll(requests2);
		}
		if (exceptionRequests.size() > 0) {
		    for (PunchExceptionRequest request : exceptionRequests) {
		        if (timeIntervalApprovalAttribute.contains(request.getApprovalAttribute())
		        		&& checkPunchDayInRequest(request, dayStart, dayEnd, pDate)) {
		        	ExceptionRequestDTO dto = ConvertHelper.convert(request, ExceptionRequestDTO.class);
		        	if(null != request.getBeginTime()){
		        		dto.setBeginTime(request.getBeginTime().getTime());
		        	}
		        	if(null != request.getEndTime()){
		        		dto.setEndTime(request.getEndTime().getTime());
		        	}
		        	if(request.getApprovalAttribute() != null){
		        		String attributeLocaleString = localeStringService.getLocalizedString(GeneralApprovalConstants.ATTRIBUTE_SCOPE, request.getApprovalAttribute(), PunchConstants.locale, "");
		        		if(GeneralApprovalAttribute.fromCode(request.getApprovalAttribute()) == GeneralApprovalAttribute.ASK_FOR_LEAVE){
		        			ApprovalCategory category = approvalCategoryProvider.findApprovalCategoryById(request.getCategoryId());
		        			dto.setRequestTitle(category==null?attributeLocaleString:category.getCategoryName());
		        		}else{
		        			dto.setRequestTitle(attributeLocaleString);
		        		}
		        	}
		        	dto.setFlowCaseId(request.getRequestId());
		        	result.add(dto);
		        }
		    }
		}

		return result;
	}

	private GetPunchDayStatusResponse processHolidayPunchIntervalDTO(
			GetPunchDayStatusResponse response, PunchTimeRule ptr, List<PunchLog> punchLogs) {
    	if(null != punchLogs ){
    		Integer punchIntervalNoMax = getPunchIntervalNo(punchLogs);
    		for(int punchIntervalNo =1; punchIntervalNo<=punchIntervalNoMax; punchIntervalNo++){
    			PunchIntevalLogDTO intervalDTO = new PunchIntevalLogDTO();
                intervalDTO.setPunchIntervalNo(punchIntervalNo);
                intervalDTO.setPunchLogs(new ArrayList<>());
    			PunchLog pl = findPunchLog(punchLogs, PunchType.OVERTIME_ON_DUTY.getCode(), punchIntervalNo);
    			if(null != pl){
    				PunchLogDTO dto = convertPunchLog2DTOWithExceptionRequestToken(pl);
    	            intervalDTO.getPunchLogs().add(dto);
    			}else{
    				//如果找不到上班卡就退出循环
    				break;
    			}
    			pl = findPunchLog(punchLogs, PunchType.OVERTIME_OFF_DUTY.getCode(), punchIntervalNo);
    			if(null != pl){
    				PunchLogDTO dto = convertPunchLog2DTOWithExceptionRequestToken(pl);
    	            intervalDTO.getPunchLogs().add(dto);
    			}else{
    				PunchLogDTO dto = new PunchLogDTO();
    				dto.setPunchType(PunchType.OVERTIME_OFF_DUTY.getCode());
    	            intervalDTO.getPunchLogs().add(dto);
    			}
                response.getIntervals().add(intervalDTO);
    		}
    	}
		return response;
	}

    private GetPunchDayStatusResponse processWorkdayPunchIntervalDTO(GetPunchDayStatusResponse response, PunchRule pr, PunchTimeRule ptr, List<PunchLog> punchLogs, String[] statusList, String[] approvalStatus, Date punchDate) {
        response.setPunchTimesPerDay(ptr.getPunchTimesPerDay());
        for (Integer punchIntervalNo = 1; punchIntervalNo <= ptr.getPunchTimesPerDay() / 2; punchIntervalNo++) {
            PunchIntevalLogDTO intervalDTO = new PunchIntevalLogDTO();
            intervalDTO.setPunchIntervalNo(punchIntervalNo);
            intervalDTO.setPunchLogs(new ArrayList<>());
            PunchLog pl = findPunchLog(punchLogs, PunchType.ON_DUTY.getCode(), punchIntervalNo);
            if (null == pl) {
                pl = getNewPunchLog(punchDate, response.getUserId(), response.getDetailId(), response.getEnterpriseId(), punchIntervalNo, PunchType.ON_DUTY.getCode(), pr);
                pl.setStatus(PunchStatus.UNPUNCH.getCode());
                if (null != ptr)
                    pl.setRuleTime(findRuleTime(ptr, PunchType.ON_DUTY.getCode(), punchIntervalNo));
            }
            //因为如果有异常审批一定会有pl,所以异常写在里面
            PunchLogDTO dto1 = convertPunchLog2DTOWithExceptionRequestToken(pl);

            if(PunchStatus.fromCode(dto1.getClockStatus()) == PunchStatus.BELATE || PunchStatus.fromCode(dto1.getClockStatus()) == PunchStatus.LEAVEEARLY
            		|| PunchStatus.fromCode(dto1.getClockStatus()) == PunchStatus.NORMAL)
            	dto1.setStatusString(statusToString(null, dto1.getClockStatus()));
            intervalDTO.getPunchLogs().add(dto1);

            pl = findPunchLog(punchLogs, PunchType.OFF_DUTY.getCode(), punchIntervalNo);
            if (null == pl) {
                pl = getNewPunchLog(punchDate, response.getUserId(), response.getDetailId(), response.getEnterpriseId(), punchIntervalNo, PunchType.OFF_DUTY.getCode(), pr);
                pl.setStatus(PunchStatus.UNPUNCH.getCode());
                if (null != ptr)
                    pl.setRuleTime(findRuleTime(ptr, PunchType.OFF_DUTY.getCode(), punchIntervalNo));
            }
            PunchLogDTO dto2 = convertPunchLog2DTOWithExceptionRequestToken(pl);

            if(PunchStatus.fromCode(dto2.getClockStatus()) == PunchStatus.BELATE || PunchStatus.fromCode(dto2.getClockStatus()) == PunchStatus.LEAVEEARLY
            		|| PunchStatus.fromCode(dto2.getClockStatus()) == PunchStatus.NORMAL)	 
            	dto2.setStatusString(statusToString(null, dto2.getClockStatus()));
            intervalDTO.getPunchLogs().add(dto2);
            LOGGER.debug("dto1 =" + dto1 + "dto 2 = " + dto2);
            if (NormalFlag.fromCode(dto1.getSmartAlignment()) == NormalFlag.YES ||
                    NormalFlag.fromCode(dto2.getSmartAlignment()) == NormalFlag.YES) {
                intervalDTO.setSmartAlignment(NormalFlag.YES.getCode());
            }
            if (null == statusList) {
                intervalDTO.setStatus(processIntevalStatus(String.valueOf(dto1.getClockStatus()), String.valueOf(dto2.getClockStatus())));
            } else {
                intervalDTO.setStatus(statusList[punchIntervalNo - 1]);
                if (approvalStatus != null && approvalStatus.length >= punchIntervalNo && !StringUtils.isEmpty(approvalStatus[punchIntervalNo - 1])) {

                    if (intervalDTO.getStatus().equals(PunchStatus.NORMAL.getCode() + "")) {
                        dto1.setClockStatus(PunchStatus.NORMAL.getCode());
                        dto2.setClockStatus(PunchStatus.NORMAL.getCode());
                    } else if (intervalDTO.getStatus().equals(PunchStatus.BELATE.getCode() + "")) {
                        dto1.setClockStatus(PunchStatus.BELATE.getCode());
                        dto2.setClockStatus(PunchStatus.NORMAL.getCode());
                    } else if (intervalDTO.getStatus().equals(PunchStatus.LEAVEEARLY.getCode() + "")) {
                        dto1.setClockStatus(PunchStatus.NORMAL.getCode());
                        dto2.setClockStatus(PunchStatus.LEAVEEARLY.getCode());
                    } else if (intervalDTO.getStatus().equals(PunchStatus.BLANDLE.getCode() + "")) {
                        dto1.setClockStatus(PunchStatus.BELATE.getCode());
                        dto2.setClockStatus(PunchStatus.LEAVEEARLY.getCode());
                    } else if (intervalDTO.getStatus().equals(PunchStatus.UNPUNCH.getCode() + "")) {
//						dto1.setClockStatus(PunchStatus.UNPUNCH.getCode());
//						dto2.setClockStatus(PunchStatus.UNPUNCH.getCode());
                    } else if (intervalDTO.getStatus().equals(PunchStatus.FORGOT_OFF_DUTY.getCode() + "") || intervalDTO.getStatus().equals(PunchStatus.BELATE_AND_FORGOT.getCode() + "")) {
//						dto1.setClockStatus(PunchStatus.NORMAL.getCode());
                        dto2.setClockStatus(PunchStatus.UNPUNCH.getCode());
                    }
                }
            }
            intervalDTO.setStatusString(statusToString(null, Byte.valueOf(intervalDTO.getStatus())));
            response.getIntervals().add(intervalDTO);
        }
        return response;
	}

    private PunchLogDTO convertPunchLog2DTO(PunchLog pl) {
        PunchLogDTO dto1 = new PunchLogDTO();
        if (pl.getRuleTime() != null) {
            dto1 = ConvertHelper.convert(pl, PunchLogDTO.class);
        } else {
            dto1.setPunchType(pl.getPunchType());
            dto1.setIdentification(pl.getIdentification());
        }
        if (pl.getPunchDate() != null) {
            dto1.setPunchDate(pl.getPunchDate().getTime());
        }
        if (null != pl.getPunchTime()) {
            dto1.setPunchTime(pl.getPunchTime().getTime());
        }
        if (pl.getApprovalStatus() != null) {
            dto1.setClockStatus(pl.getApprovalStatus());
        } else {
            dto1.setClockStatus(pl.getStatus());
        }
        dto1.setWifiInfo(pl.getWifiInfo());
        dto1.setLocationInfo(pl.getLocationInfo());

        return dto1;
    }

    private PunchLogDTO convertPunchLog2DTOWithExceptionRequestToken(PunchLog pl) {
        PunchLogDTO dto1 = convertPunchLog2DTO(pl);

        PunchExceptionRequest exceptionRequest = punchProvider.findPunchExceptionRequest(pl.getUserId(), pl.getEnterpriseId(),
                pl.getPunchDate(), pl.getPunchIntervalNo(), pl.getPunchType());

        if (null != exceptionRequest) {
            dto1.setApprovalStatus(exceptionRequest.getStatus());
            FlowCase flowCase = flowCaseProvider.getFlowCaseById(exceptionRequest.getRequestId());
            if (null != flowCase)
                dto1.setRequestToken(ApprovalRequestDefaultHandler.processFlowURL(flowCase.getId(),
                        FlowUserType.APPLIER.getCode(), flowCase.getModuleId()));
            else
                dto1.setRequestToken(WebTokenGenerator.getInstance().toWebToken(exceptionRequest.getRequestId()));
        }
        return dto1;
    }

    private Long process24hourTimeToGMTTime(Date punchTime, Long ruleTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(punchTime);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis() + (ruleTime == null ? 0L : ruleTime);
    }

    private Long findRuleTime(PunchTimeRule ptr, Byte punchType, Integer punchIntervalNo) {
		LOGGER.debug("find rule time ptr:"+JSON.toJSONString(ptr));
        if (null == ptr || ptr.getId() == null || ptr.getId() == 0) {
            return null;
        }
        if (ptr.getPunchTimesPerDay().intValue() == 2) {
            if (punchType.equals(PunchType.ON_DUTY.getCode())) {
                return ptr.getStartEarlyTimeLong();
            } else {
                return ptr.getStartEarlyTimeLong() + ptr.getWorkTimeLong();
            }
        } else if (ptr.getPunchTimesPerDay().intValue() == 4) {
            if (punchIntervalNo.equals(1)) {
                if (punchType.equals(PunchType.ON_DUTY.getCode())) {
                    return ptr.getStartEarlyTimeLong();
                } else {
                    return ptr.getNoonLeaveTimeLong();
                }
            } else {
                if (punchType.equals(PunchType.ON_DUTY.getCode())) {
                    return ptr.getAfternoonArriveTimeLong();
                } else {
                    return ptr.getStartEarlyTimeLong() + ptr.getWorkTimeLong();
                }
            }
        } else {
            List<PunchTimeInterval> intervals = punchProvider.listPunchTimeIntervalByTimeRuleId(ptr.getId());
            if (null == intervals)
                throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
                        PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_SETTING,
                        "公司没有设置打卡时间段");
            if (punchType.equals(PunchType.ON_DUTY.getCode())) {
                return intervals.get(punchIntervalNo - 1).getArriveTimeLong();
            } else {
                return intervals.get(punchIntervalNo - 1).getLeaveTimeLong();
            }
        }
    }


    /**
     * 获取打卡状态:
     * list: 元素0 : 上班打卡/下班打卡/不用打卡 元素1:第几次排班的打卡
     */
    private PunchLogDTO getPunchType(Long userId, Long enterpriseId, Date punchTime, java.sql.Date punchDate) {
        PunchLogDTO result = new PunchLogDTO();
        // 获取打卡规则->timerule
        PunchRule pr = getPunchRule(PunchOwnerType.ORGANIZATION.getCode(), enterpriseId, userId);
        if (null == pr)
            throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
                    PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_SETTING,
                    "公司没有设置打卡规则");
        result.setPunchOrgnizationId(pr.getPunchOrganizationId());
        result.setPunchRuleId(pr.getId());
        PunchTimeRule ptr = getPunchTimeRuleWithPunchDayTypeByRuleIdAndDate(pr, punchDate, userId);
        result.setPunchDayType(ptr.getPunchDayType().getCode());
        result.setPunchRuleTimeId(ptr.getId());
        if(PunchDayType.WORKDAY != ptr.getPunchDayType()){
        	return getHolidayPunchType(ptr, pr, punchDate, userId, enterpriseId, result);
        }else{
        	return getWorkDayPunchType(ptr, punchTime, punchDate, userId, enterpriseId, result);
        }
        
    }
    private PunchLogDTO getHolidayPunchType(PunchTimeRule ptr, PunchRule pr, java.sql.Date punchDate, Long userId, Long enterpriseId,
			PunchLogDTO result) {
    	PunchOvertimeRule por = getPunchOvertimeRuleByPunchDayType(pr, ptr.getPunchDayType());
    	if (por == null) {
    		//加班规则未开启的
    		result.setPunchType(PunchType.NOT_WORKDAY.getCode());
            if (pr.getRuleType().equals(PunchRuleType.PAIBAN.getCode())){
                //ptr的id 是空 就是 没排班
            	if(ptr.getId() == null){
            		result.setPunchType(PunchType.MEIPAIBAN.getCode());
                }
            }
            result.setPunchIntervalNo(0);
            return result;
        }else{
            result.setClockStatus(PunchStatus.NORMAL.getCode());
    		//加班规则开启的
            List<PunchLog> punchLogs = punchProvider.listPunchLogsByDate(userId, enterpriseId, dateSF.get().format(punchDate),
                    ClockCode.SUCESS.getCode());
            //默认是第一次打卡的签到
            Integer punchIntervalNo = 1;
            result.setPunchType(PunchType.OVERTIME_ON_DUTY.getCode());
    		if(punchLogs != null && punchLogs.size() > 0){
    			//如果有打卡
    			//找到已打卡最大的打卡时间段
    			punchIntervalNo = getPunchIntervalNo(punchLogs);
    			//看这个时间段有没有签退,如果没有就把PunchType设置为签退,如果有PunchType不变,打卡时间段+1
        		PunchLog offDutyPunch = findPunchLog(punchLogs, PunchType.OVERTIME_OFF_DUTY.getCode(), punchIntervalNo );
            	if(offDutyPunch == null){
            		result.setPunchType(PunchType.OVERTIME_OFF_DUTY.getCode());
            	}else{
            		punchIntervalNo++; 
            	}  
    		}
    		result.setPunchIntervalNo(punchIntervalNo);
        }
    	return result ;
	}
 

	private Integer getPunchIntervalNo(List<PunchLog> punchLogs) {
		Integer punchIntervalNo = 1;
		if(null == punchLogs || punchLogs.size() == 0){
			return punchIntervalNo;
		}
		for(PunchLog pLog : punchLogs){
			if(pLog.getPunchIntervalNo() != null && pLog.getPunchIntervalNo() > punchIntervalNo){
				punchIntervalNo = pLog.getPunchIntervalNo();
			}
		}
		return punchIntervalNo;
	}

	private PunchLogDTO getWorkDayPunchType(PunchTimeRule ptr, Date punchTime,
			java.sql.Date punchDate, Long userId, Long enterpriseId, PunchLogDTO result) {
    	//发现之前的特殊日期会少了这个字段,手工设置为第二天早上4点
        if (null == ptr.getDaySplitTimeLong()) {
            ptr.setDaySplitTimeLong(PunchConstants.ONE_DAY_MS + PunchConstants.DEFAULT_SPLIT_TIME);
        }
        Calendar punCalendar = Calendar.getInstance();
        punCalendar.setTime(punchTime);
        //把当天的时分秒转换成Long型
        Long punchTimeLong = getTimeLong(punCalendar, punchDate);
        List<PunchLog> punchLogs = punchProvider.listPunchLogsByDate(userId, enterpriseId, dateSF.get().format(punchDate),
                ClockCode.SUCESS.getCode());
        int punchIntervalNo = 1;

        if (ptr.getPunchTimesPerDay().equals((byte) 2)) {
            //对于2次打卡:
            result = calculate2timePunchStatus(ptr, punchTimeLong, punchLogs, result, punchDate);
        } else if (ptr.getPunchTimesPerDay().equals((byte) 4)) {
            result = calculate4timePunchStatus(ptr, punchTimeLong, punchLogs, result, punchIntervalNo, punchDate);
        } else {
            //对于多次打卡
            result = calculateMoretimePunchStatus(ptr, punchTimeLong, punchLogs, result, punchIntervalNo, punchDate);
        }

        return result;
	}

	/**
     * 手动刷新punchLog的应打卡时间 
     * */
    @Override
    public void addPunchLogShouldPunchTime(AddPunchLogShouldPunchTimeCommand cmd){
    	if(cmd.getStartDayString()==null || cmd.getEndDayString()==null){
    		 throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                     ErrorCodes.ERROR_INVALID_PARAMETER,
                     "start end day can not null"); 
    	}
    	try {
			Date startDay = dateSF.get().parse(cmd.getStartDayString());
			Date endDay = dateSF.get().parse(cmd.getEndDayString());
			Calendar startCalendar = Calendar.getInstance();
	        startCalendar.setTime(startDay);
	        startCalendar.set(Calendar.DAY_OF_MONTH, 1);
	        Calendar endCalendar = Calendar.getInstance();
	        endCalendar.setTime(endDay); 

	        Map<Long, PunchTimeRule> punchTimeRuleMapId = new HashMap<>();
	        for (; startCalendar.before(endCalendar); ) {
	        	//循环公司
	        	String startDayString = dateSF.get().format(startCalendar.getTime());
	        	startCalendar.add(Calendar.MONTH, 1);
	        	String endDayString = dateSF.get().format(startCalendar.getTime());
	        	List<Long> enterpriseIds = punchProvider.listPunchLogEnterprise(startDayString, endDayString);
	        	if(null != enterpriseIds){
	        		for(Long enterpriseId : enterpriseIds){
	        			LOGGER.debug("addPunchLogShouldPunchTime startDay {} endDay {} enterpriseId {} ",startDayString, endDayString,enterpriseId);
	        			List<Long> userIds = punchProvider.listPunchLogUserId(enterpriseId, startDayString, endDayString);
	    	        	if(null != userIds){
	    	        		for(Long userId : userIds){
	    	        			LOGGER.debug("addPunchLogShouldPunchTime startDay {} endDay {} enterpriseId {} userId {}",
	    	        					startDayString, endDayString,enterpriseId,userId);
	    	        			addPunchLogShouldPunchTime(userId, enterpriseId, startDayString, endDayString, punchTimeRuleMapId);
	    	        		}
	    	        	}
	        		}
	        	}
	        }
	    	
    	} catch (Exception e) {
			LOGGER.error(" addPunchLogShouldPunchTime error : ",e);
		}
    }
    private void addPunchLogShouldPunchTime(Long userId, Long enterpriseId, String startDayString,
			String endDayString, Map<Long, PunchTimeRule> punchTimeRuleMapId) {
    	List<PunchDayLog> dayLogs = punchProvider.listPunchDayLogs(userId, enterpriseId, startDayString, endDayString);
    	List<PunchLog> logs =  punchProvider.listPunchLogs(userId, enterpriseId, startDayString, endDayString);
    	if(null == dayLogs || null ==logs){
    		return;
    	}

        //存放每个公司每个人每天的规则 大小= 日期*公司*公司人数
        Map<String, PunchTimeRule> punchTimeRuleMapUserIdDate = new HashMap<>();
        // 循环每一天的考勤数据 把考勤规则查出来(使用缓存减少IO) 放到map里,供每次打卡查询
        for (PunchDayLog dayLog : dayLogs) {
            if (dayLog.getTimeRuleId() == null || dayLog.getTimeRuleId() == 0) {
                continue;
            }
            PunchTimeRule ptr = punchProvider.getPunchTimeRuleById(dayLog.getTimeRuleId());
            if (ptr != null) {
                punchTimeRuleMapUserIdDate.put(getPtrMapKey(dayLog.getUserId(), dayLog.getEnterpriseId(), dateSF.get().format(dayLog.getPunchDate())), ptr);
            }
        }
    	for(PunchLog log :logs){
    		if(log.getShouldPunchTime() != null ){
    			continue;
    		}
    		try{
	    		String ptrKey = getPtrMapKey(log.getUserId(),log.getEnterpriseId(),dateSF.get().format(log.getPunchDate()));
	    		if (punchTimeRuleMapUserIdDate.containsKey(ptrKey)) {
	            	PunchTimeRule ptr = punchTimeRuleMapUserIdDate.get(ptrKey);
	            	 //请假的汇总interval
	                addPunchLogShouldPunchTime(log,logs,ptr);
	            } else {
	            	//无法追溯到这一天的考勤规则 就直接用规则时间,如果规则时间也没有,就是非常历史的数据不管他
	                log.setShouldPunchTime(log.getRuleTime());
	                
	            }
	    		punchProvider.updatePunchLog(log);
    		}catch(Exception e){
    			LOGGER.error("add log shoud punch time error : ",e);
    		}
    	}
	}

	private void addPunchLogShouldPunchTime(PunchLog log,List<PunchLog> logs, PunchTimeRule ptr) {
		if(PunchType.ON_DUTY == PunchType.fromCode(log.getPunchType())){
			if(HommizationType.fromCode(ptr.getHommizationType())==HommizationType.FLEX){
				//规则是弹性 加上弹性时间
				log.setShouldPunchTime(log.getRuleTime()+ (ptr.getFlexTimeLong()==null?0:ptr.getFlexTimeLong()));
			}else if((HommizationType.fromCode(ptr.getHommizationType())==HommizationType.LATEARRIVE)&&
					log.getPunchIntervalNo().equals(1)){ 
				//晚到晚走的第一次打卡 加上弹性时间
				log.setShouldPunchTime(log.getRuleTime()+ (ptr.getFlexTimeLong()==null?0:ptr.getFlexTimeLong()));
			}else{
				log.setShouldPunchTime(log.getRuleTime());
			}
		}else if(PunchType.OFF_DUTY == PunchType.fromCode(log.getPunchType())){
			if(HommizationType.fromCode(ptr.getHommizationType())==HommizationType.FLEX){
				//规则是弹性 减去弹性时间
				log.setShouldPunchTime(log.getRuleTime() - (ptr.getFlexTimeLong()==null?0:ptr.getFlexTimeLong()));
			}else if((HommizationType.fromCode(ptr.getHommizationType())==HommizationType.LATEARRIVE)&&
					log.getPunchIntervalNo().equals(ptr.getPunchTimesPerDay()/2)){
				//晚到晚走的最后一次打卡 
				//要根据第一次上班打卡时间 来决定加多久
				long leaveTime = getLeaveTime(ptr, findPunchLog(log.getUserId(),log.getEnterpriseId(),log.getPunchDate(),logs, PunchType.ON_DUTY.getCode(), 1));
		        log.setShouldPunchTime(leaveTime);
			}else{
				log.setShouldPunchTime(log.getRuleTime());
			}
		}
	}
 

	private String getPtrMapKey(Long userId, Long enterpriseId, String format) {
		return userId+"-"+enterpriseId+"-"+format;
	}
	static class TimeInterval {

        private Date beginTime;
        private Date endTime;

        public Date getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(Date beginTime) {
            this.beginTime = beginTime;
        }

        public Date getEndTime() {
            return endTime;
        }

        public void setEndTime(Date endTime) {
            this.endTime = endTime;
        }


    }

    private List<TimeInterval> processTimeRuleDTO(List<PunchExceptionRequest> exceptionRequests, List<TimeInterval> tiDTOs) {
        if (null == exceptionRequests) {
            return null;
        }
        Collections.sort(exceptionRequests, new Comparator<PunchExceptionRequest>() {
            @Override
            public int compare(PunchExceptionRequest o1, PunchExceptionRequest o2) {
                return o1.getBeginTime().compareTo(o2.getBeginTime());
            }
        });
        for (PunchExceptionRequest request : exceptionRequests) {
            if (com.everhomes.rest.approval.ApprovalStatus.AGREEMENT != com.everhomes.rest.approval.ApprovalStatus.fromCode(request.getStatus())) {
                continue;
            }
            if (SMART_APPROVAL_ATTRIBUTES.contains(request.getApprovalAttribute())) {
                tiDTOs = addExceptionRequest2TimeIntervals(request, tiDTOs);
            }
        }
        return tiDTOs;
    }

    public void setCalendarDateBegin(Calendar calendarDateBegin) {
        calendarDateBegin.set(Calendar.HOUR_OF_DAY, 0);
        calendarDateBegin.set(Calendar.MINUTE, 0);
        calendarDateBegin.set(Calendar.SECOND, 0);
        calendarDateBegin.set(Calendar.MILLISECOND, 0);
    }

    public void setCalendarMonthBegin(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        setCalendarDateBegin(calendar);
    }

    private static List<TimeInterval> addExceptionRequest2TimeIntervals(PunchExceptionRequest request, List<TimeInterval> tiDTOs) {
        if (null == tiDTOs) {
            tiDTOs = new ArrayList<>();
            TimeInterval dto = new TimeInterval();
            dto.setBeginTime(request.getBeginTime());
            dto.setEndTime(request.getEndTime());
            tiDTOs.add(dto);
            return tiDTOs;
        }
        for (TimeInterval ti : tiDTOs) {
            //如果request开始和结束时间都在ti时间区间内,直接return
            if (request.getBeginTime().compareTo(ti.getBeginTime()) >= 0 && request.getEndTime().compareTo(ti.getEndTime()) <= 0) {
                return tiDTOs;
            }
            //如果request有开始时间在ti时间区间内,把request的结束时间代替ti的结束时间
            else if (request.getBeginTime().compareTo(ti.getBeginTime()) >= 0 && request.getBeginTime().compareTo(ti.getEndTime()) <= 0) {
                ti.setEndTime(request.getEndTime());
                return tiDTOs;
            }
            //如果request的结束时间在ti时间区间,把request的开始时间代替ti的开始时间
            else if (request.getEndTime().compareTo(ti.getBeginTime()) >= 0 && request.getEndTime().compareTo(ti.getEndTime()) <= 0) {
                ti.setBeginTime(request.getBeginTime());
                return tiDTOs;
            }
        }
        //循环过后request和tiDTOs没有什么重合,就把request加入tiDTOs
        TimeInterval dto = new TimeInterval();
        dto.setBeginTime(request.getBeginTime());
        dto.setEndTime(request.getEndTime());
        tiDTOs.add(dto);

        return tiDTOs;
    }

    private Long getTimeLong(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Long l = calendar.get(Calendar.HOUR_OF_DAY) * 3600 * 1000L; //hour
        l += calendar.get(Calendar.MINUTE) * 60 * 1000L; //min
        l += calendar.get(Calendar.SECOND) * 1000L;//second
        return l;
    }

    private Long getTimeLong(Calendar punCalendar, java.sql.Date punchDate) {

        Long punchTimeLong = punCalendar.get(Calendar.HOUR_OF_DAY) * 3600 * 1000L; //hour
        punchTimeLong += punCalendar.get(Calendar.MINUTE) * 60 * 1000L; //min
        punchTimeLong += punCalendar.get(Calendar.SECOND) * 1000L;//second
        if (null != punchDate) {
            SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMDD");
            Integer punchTimeInt = Integer.valueOf(sdf.format(punCalendar.getTime()));
            Integer punchDateInt = Integer.valueOf(sdf.format(punchDate));
            if (punchTimeInt < punchDateInt) {
                //打明天的卡, 打卡时间-1天
                punchTimeLong -= PunchConstants.ONE_DAY_MS;
            } else if (punchTimeInt > punchDateInt) {
                //打前一天的卡, 打卡时间+1天
                punchTimeLong += PunchConstants.ONE_DAY_MS;
            }
        }
        return punchTimeLong;
    }

    private PunchLogDTO calculateMoretimePunchStatus(PunchTimeRule ptr, Long punchTimeLong,
                                                     List<PunchLog> punchLogs, PunchLogDTO result, Integer PunchIntervalNo, java.sql.Date punchDate) {
        List<PunchTimeInterval> intervals = punchProvider.listPunchTimeIntervalByTimeRuleId(ptr.getId());
        if (null == intervals)
            throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
                    PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_SETTING,
                    "公司没有设置打卡时间段");
        //非打卡时段
        if ((punchTimeLong < (intervals.get(0).getArriveTimeLong() - (timeIsNull(ptr.getBeginPunchTime(), PunchConstants.ONE_DAY_MS)))) ||
                (punchTimeLong > (intervals.get(intervals.size() - 1).getLeaveTimeLong() + timeIsNull(ptr.getEndPunchTime(), PunchConstants.ONE_DAY_MS)))) {
            result.setPunchType(PunchType.NOT_WORKTIME.getCode());
            result.setPunchIntervalNo(PunchIntervalNo);

            if (punchTimeLong < (ptr.getStartEarlyTimeLong() - (timeIsNull(ptr.getBeginPunchTime(), PunchConstants.ONE_DAY_MS)))) {
                result.setExpiryTime(ptr.getStartEarlyTimeLong() - (timeIsNull(ptr.getBeginPunchTime(), PunchConstants.ONE_DAY_MS)));
            }
            return result;
        }

        for (int i = 0; i < intervals.size(); i++) {
            //循环每一次打卡
            PunchIntervalNo = i + 1;
            if (i == 0) {
                //第一次: 如果时间在最早打卡后 下一次上班打卡之前为本次打卡
                if (punchTimeLong > (intervals.get(i).getArriveTimeLong() - (timeIsNull(ptr.getBeginPunchTime(), PunchConstants.ONE_DAY_MS)))
                        && punchTimeLong < intervals.get(i + 1).getArriveTimeLong()) {
                    PunchLog onDutyPunch = findPunchLog(punchLogs, PunchType.ON_DUTY.getCode(), PunchIntervalNo);
                    if (null == onDutyPunch) {
                        return processOndutyPunchLog(ptr, intervals, result, PunchIntervalNo, punchTimeLong, punchDate);
                    }
                    PunchLog offDutyPunch = findPunchLog(punchLogs, PunchType.OFF_DUTY.getCode(), PunchIntervalNo);
                    if (null == offDutyPunch) {
                        return processOffdutyPunchLog(ptr, intervals, result, PunchIntervalNo, punchTimeLong, punchDate);
                    } else {
                        continue;
                    }
                }
            } else if (i == intervals.size() - 1) {
                //最后一次  如果打卡时间比本次下班时间+最晚下班时间早  则是本次打卡否则
                if (punchTimeLong < intervals.get(i).getLeaveTimeLong() + timeIsNull(ptr.getEndPunchTime(), PunchConstants.ONE_DAY_MS)) {
                    PunchLog onDutyPunch = findPunchLog(punchLogs, PunchType.ON_DUTY.getCode(), PunchIntervalNo);
                    if (null == onDutyPunch) {
                        return processOndutyPunchLog(ptr, intervals, result, PunchIntervalNo, punchTimeLong, punchDate);
                    }
                    PunchLog offDutyPunch = findPunchLog(punchLogs, PunchType.OFF_DUTY.getCode(), PunchIntervalNo);
                    if (null == offDutyPunch) {
                        return processOffdutyPunchLog(ptr, intervals, result, PunchIntervalNo, punchTimeLong, punchDate);
                    } else {
                        result = processOffdutyPunchLog(ptr, intervals, result, PunchIntervalNo, punchTimeLong, punchDate);
                        result.setPunchType(PunchType.FINISH.getCode());
                        return result;
                    }
                }
            } else {
                //中间的
                //如果时间在下一次上班打卡之前则是本次打卡
                //如果本次没有上班打卡,则是本次上班打卡.
                //本次有上班打卡,没有下班打卡,则是本次下班打卡,否则是下次打卡
                if (punchTimeLong < intervals.get(i + 1).getArriveTimeLong()) {
                    PunchLog onDutyPunch = findPunchLog(punchLogs, PunchType.ON_DUTY.getCode(), PunchIntervalNo);
                    if (null == onDutyPunch) {
                        return processOndutyPunchLog(ptr, intervals, result, PunchIntervalNo, punchTimeLong, punchDate);
                    }
                    PunchLog offDutyPunch = findPunchLog(punchLogs, PunchType.OFF_DUTY.getCode(), PunchIntervalNo);
                    if (null == offDutyPunch) {
                        return processOffdutyPunchLog(ptr, intervals, result, PunchIntervalNo, punchTimeLong, punchDate);
                    } else {
                        continue;
                    }
                }

            }
        }
        return result;
    }

    private PunchLogDTO processOffdutyPunchLog(PunchTimeRule ptr, List<PunchTimeInterval> intervals, PunchLogDTO result, Integer punchIntervalNo, Long punchTimeLong, java.sql.Date punchDate) {
        result.setPunchType(PunchType.OFF_DUTY.getCode());
        result.setPunchIntervalNo(punchIntervalNo);
        result.setRuleTime(intervals.get(punchIntervalNo - 1).getLeaveTimeLong());
        long leaveTime = result.getRuleTime();
        if (ptr.getHommizationType().equals(HommizationType.FLEX.getCode())) {
            leaveTime = leaveTime + ptr.getFlexTimeLong();
        }
        result.setShouldPunchTime(leaveTime);
        if (punchTimeLong < leaveTime) {
            result.setClockStatus(PunchStatus.LEAVEEARLY.getCode());
            result.setExpiryTime(leaveTime);
        } else {
            result.setClockStatus(PunchStatus.NORMAL.getCode());
            if (punchIntervalNo.equals(intervals.size())) {
                result.setExpiryTime(
                        findSmallOne(intervals.get(punchIntervalNo - 1).getLeaveTimeLong() + timeIsNull(ptr.getEndPunchTime(), PunchConstants.ONE_DAY_MS), ptr.getDaySplitTimeLong()));
            } else {
                result.setExpiryTime(intervals.get(punchIntervalNo).getArriveTimeLong());
            }
        }
        result.setPunchNormalTime(result.getRuleTime());
        return result;
    }

    private Long findSmallOne(long l, Long l2) {
        return l < l2 ? l : l2;
    }

    private PunchLogDTO processOndutyPunchLog(PunchTimeRule ptr, List<PunchTimeInterval> intervals, PunchLogDTO result, Integer PunchIntervalNo, Long punchTimeLong, java.sql.Date punchDate) {
        result.setPunchType(PunchType.ON_DUTY.getCode());
        result.setPunchIntervalNo(PunchIntervalNo);
        result.setRuleTime(intervals.get(PunchIntervalNo - 1).getArriveTimeLong());
        long arriveTIme = result.getRuleTime();
        if (ptr.getHommizationType().equals(HommizationType.FLEX.getCode())) {
            arriveTIme = arriveTIme + ptr.getFlexTimeLong();
        }
        result.setShouldPunchTime(arriveTIme);
        if (punchTimeLong > arriveTIme) {
            result.setClockStatus(PunchStatus.BELATE.getCode());
            if (PunchIntervalNo.equals(intervals.size())) {
                result.setExpiryTime(findSmallOne(intervals.get(PunchIntervalNo - 1).getLeaveTimeLong() + timeIsNull(ptr.getEndPunchTime(), PunchConstants.ONE_DAY_MS), ptr.getDaySplitTimeLong()));
            } else {
                result.setExpiryTime(intervals.get(PunchIntervalNo).getArriveTimeLong());
            }
            result.setPunchNormalTime(intervals.get(PunchIntervalNo - 1).getArriveTimeLong() - (timeIsNull(ptr.getBeginPunchTime(), PunchConstants.ONE_DAY_MS)));
        } else {
            result.setClockStatus(PunchStatus.NORMAL.getCode());
            result.setExpiryTime(arriveTIme);
            result.setPunchNormalTime(arriveTIme);
        }
        return result;
    }

    private PunchLogDTO calculate4timePunchStatus(PunchTimeRule ptr, Long punchTimeLong, List<PunchLog> punchLogs, PunchLogDTO result, Integer PunchIntervalNo, java.sql.Date punchDate) {
        //对于4次打卡:
        //如果在最早上班打卡之前,或者  最晚下班打卡之后. 那就是非打卡时间
        if ((punchTimeLong < (ptr.getStartEarlyTimeLong() - (timeIsNull(ptr.getBeginPunchTime(), PunchConstants.ONE_DAY_MS)))) ||
                (punchTimeLong > (ptr.getStartLateTimeLong() + ptr.getWorkTimeLong() + timeIsNull(ptr.getEndPunchTime(), PunchConstants.ONE_DAY_MS)))) {
            result.setPunchType(PunchType.NOT_WORKTIME.getCode());
            result.setPunchIntervalNo(PunchIntervalNo);

            if (punchTimeLong < (ptr.getStartEarlyTimeLong() - (timeIsNull(ptr.getBeginPunchTime(), PunchConstants.ONE_DAY_MS)))) {
                result.setExpiryTime(ptr.getStartEarlyTimeLong() - (timeIsNull(ptr.getBeginPunchTime(), PunchConstants.ONE_DAY_MS)));
            }
            return result;
        }
        //如果在在最早上班打卡之后 下午上班之前
        // 则是第一段打卡,如果没有第一段上班打卡就是上班打卡,有上班打卡没有下班打卡就是下班打卡,
        if (punchTimeLong < ptr.getAfternoonArriveTimeLong()) {
            PunchLog onDutyPunch = findPunchLog(punchLogs, PunchType.ON_DUTY.getCode(), PunchIntervalNo);
            if (null == onDutyPunch) {
                result.setPunchType(PunchType.ON_DUTY.getCode());
                result.setPunchIntervalNo(PunchIntervalNo);
                result.setRuleTime(ptr.getStartEarlyTimeLong());
                if (punchTimeLong < ptr.getStartLateTimeLong()) {
                    result.setClockStatus(PunchStatus.NORMAL.getCode());
                    result.setExpiryTime(ptr.getStartLateTimeLong());
                } else {
                    result.setClockStatus(PunchStatus.BELATE.getCode());
                    result.setExpiryTime(ptr.getAfternoonArriveTimeLong());
                }
                result.setShouldPunchTime(ptr.getStartLateTimeLong());
                result.setPunchNormalTime(ptr.getStartEarlyTimeLong());
                return result;
            }
            PunchLog offDutyPunch = findPunchLog(punchLogs, PunchType.OFF_DUTY.getCode(), PunchIntervalNo);
            if (null == offDutyPunch) {
                result.setPunchType(PunchType.OFF_DUTY.getCode());
                result.setPunchIntervalNo(PunchIntervalNo);
                result.setRuleTime(ptr.getNoonLeaveTimeLong());
                long noonLeaveTime = ptr.getNoonLeaveTimeLong();
                if (ptr.getHommizationType() != null && ptr.getHommizationType().equals(HommizationType.FLEX.getCode())) {
                    noonLeaveTime = noonLeaveTime - ptr.getFlexTimeLong();
                }
                if (punchTimeLong < noonLeaveTime) {
                    result.setClockStatus(PunchStatus.LEAVEEARLY.getCode());
                    result.setExpiryTime(noonLeaveTime);
                } else {
                    result.setClockStatus(PunchStatus.NORMAL.getCode());
                    result.setExpiryTime(ptr.getAfternoonArriveTimeLong());
                }
                result.setPunchNormalTime(ptr.getNoonLeaveTimeLong());
                return result;
            } else {

            }
        }
        // 否则则是第二段上班打卡
        PunchIntervalNo = 2;
        //第二段打卡:
        // 如果有第二段上班打卡则是第二段上班打卡,如果有第二段上班打卡没有第二段下班打卡则是下班打卡,否则不打卡
        PunchLog onDutyPunch = findPunchLog(punchLogs, PunchType.ON_DUTY.getCode(), PunchIntervalNo);
        if (null == onDutyPunch) {
            result.setPunchType(PunchType.ON_DUTY.getCode());
            result.setPunchIntervalNo(PunchIntervalNo);
            result.setRuleTime(ptr.getAfternoonArriveTimeLong());
            long afternoonArriveTime = ptr.getAfternoonArriveTimeLong();
            if (ptr.getHommizationType() != null && ptr.getHommizationType().equals(HommizationType.FLEX.getCode())) {
                afternoonArriveTime = afternoonArriveTime + ptr.getFlexTimeLong();
            }
            if (punchTimeLong < afternoonArriveTime) {
                result.setClockStatus(PunchStatus.NORMAL.getCode());
                result.setExpiryTime(afternoonArriveTime);
            } else {
                result.setClockStatus(PunchStatus.BELATE.getCode());
                result.setExpiryTime(ptr.getStartLateTimeLong() + ptr.getWorkTimeLong() + timeIsNull(ptr.getEndPunchTime(), PunchConstants.ONE_DAY_MS));
            }
            result.setShouldPunchTime(afternoonArriveTime);
            result.setPunchNormalTime(punchDate.getTime() + ptr.getAfternoonArriveTimeLong());
            return result;
        }
        PunchLog offDutyPunch = findPunchLog(punchLogs, PunchType.OFF_DUTY.getCode(), PunchIntervalNo);
        if (null == offDutyPunch) {
            result.setPunchType(PunchType.OFF_DUTY.getCode());
            result.setPunchIntervalNo(PunchIntervalNo);
        } else {
            result.setPunchType(PunchType.FINISH.getCode());
            result.setPunchIntervalNo(PunchIntervalNo);
        }
        processLastOffDutyPunchLog(result, ptr, punchTimeLong, punchLogs, punchDate);

        return result;
    }

    private PunchLogDTO calculate2timePunchStatus(PunchTimeRule ptr, Long punchTimeLong, List<PunchLog> punchLogs, PunchLogDTO result, java.sql.Date punchDate) {
        result.setPunchIntervalNo(1);

        //如果时间在最早上班打卡之后,
        if (punchTimeLong > (ptr.getStartEarlyTimeLong() - (timeIsNull(ptr.getBeginPunchTime(), PunchConstants.ONE_DAY_MS)))
                //在最晚下班打卡之前
                && punchTimeLong < (ptr.getStartLateTimeLong() + ptr.getWorkTimeLong() + timeIsNull(ptr.getEndPunchTime(), PunchConstants.ONE_DAY_MS))) {
            //这里是有效时间范围内的打卡
            PunchLog onDutyPunch = findPunchLog(punchLogs, PunchType.ON_DUTY.getCode(), 1);
            // 没有上班打卡  则是上班打卡
            if (null == onDutyPunch) {
                result.setPunchType(PunchType.ON_DUTY.getCode());
                result.setRuleTime(ptr.getStartEarlyTimeLong());
                //最晚上班时间_结合请假时间进行计算
//				Long startlateTime = calculateStartlateTime(ptr, tiDTOs);
                result.setShouldPunchTime(ptr.getStartLateTimeLong());
                if (punchTimeLong < ptr.getStartLateTimeLong()) {
                    result.setClockStatus(PunchStatus.NORMAL.getCode());
                    result.setExpiryTime(ptr.getStartLateTimeLong());
                } else {
                    result.setClockStatus(PunchStatus.BELATE.getCode());
                    result.setExpiryTime(findSmallOne(ptr.getStartLateTimeLong() + ptr.getWorkTimeLong() + timeIsNull(ptr.getEndPunchTime(), PunchConstants.ONE_DAY_MS), ptr.getDaySplitTimeLong()));
                }
                result.setPunchNormalTime(punchDate.getTime() + ptr.getStartLateTimeLong());
                return result;
            }
            PunchLog offDutyPunch = findPunchLog(punchLogs, PunchType.OFF_DUTY.getCode(), 1);
            //没有下班打卡则是下班打卡
            if (null == offDutyPunch) {
                result.setPunchType(PunchType.OFF_DUTY.getCode());
            } else {
                //否则就是已完成打卡-但是可以更新打卡
                result.setPunchType(PunchType.FINISH.getCode());
            }
            processLastOffDutyPunchLog(result, ptr, punchTimeLong, punchLogs, punchDate);
            return result;
        } else {
            //不在时间范围内无法打卡
            result.setPunchType(PunchType.NOT_WORKTIME.getCode());
            if (punchTimeLong < (ptr.getStartEarlyTimeLong() - (timeIsNull(ptr.getBeginPunchTime(), PunchConstants.ONE_DAY_MS)))) {
                result.setExpiryTime(ptr.getStartEarlyTimeLong() - (timeIsNull(ptr.getBeginPunchTime(), PunchConstants.ONE_DAY_MS)));
            }
            return result;
        }
    }

    //获取离开时间 --对于4次打卡弹性时间,要根据上班打卡时间决定
    private void processLastOffDutyPunchLog(PunchLogDTO result, PunchTimeRule ptr, Long punchTimeLong, List<PunchLog> punchLogs, java.sql.Date punchDate) {
        long leaveTime = getLeaveTime(ptr, findPunchLog(punchLogs, PunchType.ON_DUTY.getCode(), 1));
        result.setShouldPunchTime(leaveTime);
        result.setRuleTime(ptr.getStartEarlyTimeLong() + ptr.getWorkTimeLong());
        if (punchTimeLong < leaveTime) {
            result.setClockStatus(PunchStatus.LEAVEEARLY.getCode());
            result.setExpiryTime(leaveTime);
        } else {
            result.setClockStatus(PunchStatus.NORMAL.getCode());
            result.setExpiryTime(findSmallOne(ptr.getStartLateTimeLong() + ptr.getWorkTimeLong() + timeIsNull(ptr.getEndPunchTime(), PunchConstants.ONE_DAY_MS), ptr.getDaySplitTimeLong()));
        }
        if (ptr.getHommizationType().equals(HommizationType.LATEARRIVE.getCode())) {
            result.setPunchNormalTime(leaveTime);
        } else {
            result.setPunchNormalTime(result.getRuleTime());
        }
    }

    private long getLeaveTime(PunchTimeRule ptr, PunchLog onDutyPunch) {

        long leaveTime = ptr.getStartEarlyTimeLong() + ptr.getWorkTimeLong();
        if (null == onDutyPunch || onDutyPunch.getPunchTime() == null)
            return leaveTime;
        if (ptr.getHommizationType().equals(HommizationType.LATEARRIVE.getCode())) {

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(onDutyPunch.getPunchTime());
            //把当天的时分秒转换成Long型
            Long ondutyTime = calendar.get(Calendar.HOUR_OF_DAY) * 3600 * 1000L; //hour
            ondutyTime += calendar.get(Calendar.MINUTE) * 60 * 1000L; //min
            ondutyTime += calendar.get(Calendar.SECOND) * 1000L;//second
            if (ondutyTime > ptr.getStartEarlyTimeLong()) {
                if (ondutyTime > ptr.getStartLateTimeLong()) {
                    leaveTime = ptr.getStartLateTimeLong() + ptr.getWorkTimeLong();
                } else {
                    leaveTime = ondutyTime + ptr.getWorkTimeLong();
                }
            }
        } else if (ptr.getHommizationType().equals(HommizationType.FLEX.getCode())) {
            leaveTime = leaveTime - (ptr.getFlexTimeLong()==null?0:ptr.getFlexTimeLong());
        }
        return leaveTime;
    }

    //上班取最早,下班取最晚
    private PunchLog findPunchLog(Long userId, Long enterpriseId, java.sql.Date punchDate, List<PunchLog> punchLogs, Byte pucnhType, Integer punchIntervalNo) {
        if (null == punchLogs || punchLogs.size() == 0)
            return null;
        PunchLog result = null;
        for (PunchLog log : punchLogs) {

        	if(null !=userId && !log.getUserId().equals(userId)){
        		continue;
        	}
        	if(null !=enterpriseId && !log.getEnterpriseId().equals(enterpriseId)){
        		continue;
        	}
        	if(null !=punchDate && !log.getPunchDate().equals(punchDate)){
        		continue;
        	}
            if (log.getPunchType().equals(pucnhType) && log.getPunchIntervalNo().equals(punchIntervalNo)) {
            	//added by wh 2018-6-14 如果result为空或者result的punchTime为空直接替换
            	//之后要判断log的punchTime不为空
            	if(null == result || result.getPunchTime() == null){
            		result = log;
            	} 
            	else {
            		switch(PunchType.fromCode(pucnhType)){ 
            		//上班和签到一样判断:比较打卡时间找出最小的 下班和签退同理
	            		case ON_DUTY:
	            		case OVERTIME_ON_DUTY:
	            			if (log.getPunchTime() != null && log.getPunchTime().before(result.getPunchTime()))
	                            result = log;
	            			break;
	            		case OFF_DUTY:
	            		case OVERTIME_OFF_DUTY:
	            			if (log.getPunchTime() != null && log.getPunchTime().after(result.getPunchTime()))
	                            result = log;
		    			default:
		    				break;
                	}
            	} 
            }
        }
        return result;
    }

    private PunchLog findPunchLog(List<PunchLog> punchLogs, Byte pucnhType, Integer punchIntervalNo) {
        return findPunchLog(null,null,null,punchLogs,pucnhType,punchIntervalNo);
    }

    private Long timeIsNull(Long value, Long defaultValue) {
        return value == null ? defaultValue : value;
    }

    @Override
    public ListPunchMonthStatusResponse listPunchMonthStatus(ListPunchMonthStatusCommand cmd) {
        // TODO Auto-generated method sub
        cmd.setEnterpriseId(getTopEnterpriseId(cmd.getEnterpriseId()));
        if (null == cmd.getQueryTime()) {
            Calendar punCalendar = Calendar.getInstance();
            java.sql.Date pDate = calculatePunchDate(punCalendar, cmd.getEnterpriseId(), UserContext.currentUserId());
            cmd.setQueryTime(pDate.getTime());
        }
        ListPunchMonthStatusResponse response = new ListPunchMonthStatusResponse();
        response.setDayStatus(new ArrayList<>());
        response.setMonthDate(cmd.getQueryTime());
        Long userId = UserContext.current().getUser().getId();
        if (null != cmd.getUserId()) {
            userId = cmd.getUserId();
        }

        OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByTargetId(userId, cmd.getEnterpriseId());
        if (null != detail) {
            response.setDetailId(detail.getId());
        }
        response.setUserId(userId);
        response.setEnterpriseId(cmd.getEnterpriseId());
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(cmd.getQueryTime());
        startCalendar.set(Calendar.DAY_OF_MONTH, 1);
        Calendar today = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(startCalendar.getTime());
        endCalendar.add(Calendar.MONTH, 1);
        List<PunchDayLog> dayLogList = this.punchProvider.listPunchDayLogsIncludeEndDay(detail.getId(), cmd.getEnterpriseId(),
                dateSF.get().format(startCalendar.getTime()), dateSF.get().format(endCalendar.getTime()));
        for (; startCalendar.before(endCalendar); startCalendar.add(Calendar.DAY_OF_MONTH, 1)) {
            PunchDayLog log = findPunchDayLogByDate(startCalendar.getTime(), dayLogList);
            MonthDayStatusDTO dto = new MonthDayStatusDTO();
            if (null != log) {
                dto = ConvertHelper.convert(log, MonthDayStatusDTO.class); 
                if( log.getStatusList().equals(PunchStatus.NO_ASSIGN_PUNCH_RULE + "")
                		||  log.getStatusList().equals(PunchStatus.NO_ASSIGN_PUNCH_SCHEDULED + "")){
                	dto.setRuleType(null);
                	dto.setTimeRuleId(null);
                }
                //异常状态用log的
//				if (null == log.getStatusList()) {
//					dto.setExceptionStatus(log.getStatus().equals(PunchStatus.NORMAL.getCode()) ?
//							ExceptionStatus.NORMAL.getCode() : ExceptionStatus.EXCEPTION.getCode());
//				} else {
//					String[] status = log.getStatusList().split(PunchConstants.STATUS_SEPARATOR);
//					dto.setExceptionStatus(ExceptionStatus.NORMAL.getCode());
//					if (status == null) {
//						continue;
//					}
//					else {
//						for (String s1 : status) {
//							if (!s1.equals(String.valueOf(PunchStatus.NORMAL.getCode()))) {
//								dto.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
//							}
//						}
//					}
//				}
            } else if(today.before(startCalendar)) {
//              //今天之后的日期进行处理
            		 
                PunchRule pr = this.getPunchRule(PunchOwnerType.ORGANIZATION.getCode(), cmd.getEnterpriseId(), userId);
                if (null == pr)
                    continue;
                dto.setRuleType(pr.getRuleType());
                //获取当天的排班
                PunchTimeRule ptr = getPunchTimeRuleWithPunchDayTypeByRuleIdAndDate(pr, startCalendar.getTime(), userId);
                if (ptr != null) {
                    dto.setTimeRuleId(ptr.getId());
                    if (ptr.getId() == null || ptr.getId() == 0) {
                        dto.setTimeRuleName("休息");
                    } else {
                        dto.setTimeRuleName(ptr.getName());
                    }
                }
            } 
            //放在这里设置punchDate因为 ConvertHelper.convert(log, MonthDayStatusDTO.class); 会让punchDate消失
            dto.setPunchDate(startCalendar.getTime().getTime());
            response.getDayStatus().add(dto);
        }
        return response;
    }

    private PunchDayLog findPunchDayLogByDate(Date time, List<PunchDayLog> dayLogList) {
        String time1 = dateSF.get().format(time);
        for (PunchDayLog log : dayLogList) {
            String time2 = dateSF.get().format(log.getPunchDate());
            if (time1.equals(time2)) {
                return log;
            }
        }
        return null;
    }

    @Override
    public String getPunchQRCode(GetPunchQRCodeCommand cmd,
                                 HttpServletResponse response) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("qrtype", String.valueOf(cmd.getCodeType()));
        map.put("token", cmd.getQrToken().trim());
        String result = localeTemplateService.getLocaleTemplateString(PunchConstants.PUNCH_TOOL_URI_SCOPE,
                PunchConstants.PUNCH_TOOL_URI_CODE, RentalNotificationTemplateCode.locale, map, "");
        String key = cmd.getQrToken().trim();
        ValueOperations<String, String> valueOperations = getValueOperations(key);
        int timeout = configurationProvider.getIntValue(PunchConstants.PUNCH_QRCODE_TIMEOUT, 15);
        TimeUnit unit = TimeUnit.MINUTES;
        // 先放一个和key一样的值,表示这个人key有效
        valueOperations.set(key, key, timeout, unit);
//		try{
//			ByteArrayOutputStream out = QRCodeEncoder.createSimpleQrCode(Base64.getEncoder().encodeToString(result.getBytes()));
//			return downloadPng(out,response);
//		} catch (WriterException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

        return Base64.getEncoder().encodeToString(result.getBytes());
    }

    public HttpServletResponse downloadPng(ByteArrayOutputStream out, HttpServletResponse response) {
        try {
            response.reset();
            // 设置response的Header
//			response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes()));
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("image/png");
            response.setContentLength(out.size());
            toClient.write(out.toByteArray());
            toClient.flush();
            toClient.close();

        } catch (IOException ex) {
            LOGGER.error("down load pic error", ex);
            throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
                    PunchServiceErrorCode.ERROR_PUNCH_ADD_DAYLOG,
                    ex.getLocalizedMessage());

        }
        return response;
    }

    @Override
    public void addPunchPoints(AddPunchPointsCommand cmd) {
        RestResponse restResponse = new RestResponse(cmd.getPunchGeoPoints());
        busMessage(restResponse, cmd.getQrToken().trim());
    }

    private void busMessage(RestResponse restResponse, String key) {
        ValueOperations<String, String> valueOperations = getValueOperations(key);
        String value = valueOperations.get(key);
        if (null == value) {
            //找不到key,说明过期了 报错
            throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
                    PunchServiceErrorCode.ERROR_PUNCH_TOKEN_TIMEOUT,
                    "token time out ");
        } else {
            submitLocalBus(PunchConstants.PUNCH_QRCODE_SUBJECT + "." + key, restResponse);
        }
    }

    @Override
    public void addPunchWifis(AddPunchWifisCommand cmd) {
        RestResponse restResponse = new RestResponse(cmd.getWifis());
        busMessage(restResponse, cmd.getQrToken().trim());
    }

    @Override
    public DeferredResult<RestResponse> getPunchQRCodeResult(GetPunchQRCodeCommand cmd) {
        // TODO Auto-generated method stub
        DeferredResult<RestResponse> deferredResult = new DeferredResult<>();
        RestResponse response = new RestResponse();
        String key = cmd.getQrToken().trim();
        ValueOperations<String, String> valueOperations = getValueOperations(key);
        String value = valueOperations.get(key);
        if (null == value) {
            //找不到key,说明过期了 报错
            throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
                    PunchServiceErrorCode.ERROR_PUNCH_TOKEN_TIMEOUT,
                    "token time out ");
        }
        //如果value 和key 不一样,说明value被放入了前端给的数据,直接返回结果
        if (null != value && !value.equals(key)) {
            LOGGER.debug(String.format("key :{},value:{}", key, value));
            deferredResult.setResult(JSONObject.parseObject(value, RestResponse.class));
            int timeout = configurationProvider.getIntValue(PunchConstants.PUNCH_QRCODE_TIMEOUT, 15);
            TimeUnit unit = TimeUnit.MINUTES;
            ;
            // 先放一个和key一样的值,表示这个key有效
            valueOperations.set(key, key, timeout, unit);
            return deferredResult;
        }

        int scanTimeout = configurationProvider.getIntValue(PrintErrorCode.PRINT_LOGON_SCAN_TIMOUT, 100000);
        LOGGER.debug("build key:" + PunchConstants.PUNCH_QRCODE_SUBJECT + "." + key);
        localBusSubscriberBuilder.build(PunchConstants.PUNCH_QRCODE_SUBJECT + "." + key, new LocalBusOneshotSubscriber() {
            @Override
            public Action onLocalBusMessage(Object sender, String subject,
                                            Object restResponse, String path) {
                LOGGER.debug("进入了 bus message  restResponse:" + JSON.toJSONString(restResponse));
                //这里要不要重新把timeou的时间刷一刷?
                deferredResult.setResult(JSONObject.parseObject((String) restResponse, RestResponse.class));
                return null;
            }

            @Override
            public void onLocalBusListeningTimeout() {
                response.setResponseObject("timed out");
                response.setErrorCode(408);
                deferredResult.setResult(response);
            }

        }).setTimeout(scanTimeout).create();

        return deferredResult;
    }

    //给每台机器发通知
    private void submitLocalBus(String localKey, RestResponse restResponse) {
        LOGGER.debug("submit bus message : key = " + localKey);
        ExecutorUtil.submit(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LocalBusSubscriber localBusSubscriber = (LocalBusSubscriber) busBridgeProvider;
                    localBusSubscriber.onLocalBusMessage(null, localKey, JSONObject.toJSONString(restResponse), null);
                } catch (Exception e) {
                    LOGGER.error("submit LocalBusSubscriber {} got excetion", localKey, e);
                }
                localBus.publish(null, localKey, JSONObject.toJSONString(restResponse));
            }
        }, "subscriberPrint"));
    }

    @Override
    public void invalidPunchQRCode(GetPunchQRCodeCommand cmd) {
        String key = cmd.getQrToken().trim();
        deleteValueOperations(key);
    }

    @Override
    public GetPunchGroupsCountResponse getPunchGroupsCount(GetPunchGroupsCountCommand cmd) {
        GetPunchGroupsCountResponse response = new GetPunchGroupsCountResponse();
        Organization org = organizationProvider.findOrganizationById(cmd.getOwnerId());
        Integer allOrganizationInteger = organizationProvider.countOrganizationMemberDetailsByOrgId(org.getNamespaceId(), cmd.getOwnerId());
        response.setAllEmployeeCount(allOrganizationInteger);
        // 未关联人数
        List<OrganizationMemberDetails> details = uniongroupConfigureProvider.listDetailNotInUniongroup(org.getNamespaceId(), org.getId(), null, CONFIG_VERSION_CODE, null);
        response.setUnjoinPunchGroupCount(details == null ? 0 : details.size());
        //未排班的人
        response.setUnSchedulingCount(0);
        List<PunchRule> prList = punchProvider.listPunchRulesByOwnerAndRuleType(cmd.getOwnerType(), cmd.getOwnerId(), PunchRuleType.PAIBAN.getCode());
        if (null != prList) {
            Calendar start = Calendar.getInstance();
            start.set(Calendar.DAY_OF_MONTH, 1);
            start.set(Calendar.HOUR_OF_DAY, 0);
            start.set(Calendar.MINUTE, 0);
            start.set(Calendar.SECOND, 0);
            start.set(Calendar.MILLISECOND, 0);
            Calendar end = Calendar.getInstance();
            end.setTime(start.getTime());
            end.add(Calendar.MONTH, 1);
            List<Long> groupList = new ArrayList<Long>();
            List<Long> prIdList = new ArrayList<Long>();

            for (PunchRule pr : prList) {
                groupList.add(pr.getPunchOrganizationId());
                prIdList.add(pr.getId());
//				LOGGER.debug("开始查询"+StringHelper.toJsonString(pr));
//				List<UniongroupMemberDetail> employees = uniongroupConfigureProvider.listUniongroupMemberDetail(pr.getPunchOrganizationId(),CONFIG_VERSION_CODE);
//
//				List<Long> detailIds = new ArrayList<>();
//				if (null != employees) {
//					for (UniongroupMemberDetail detail : employees) {
//						detailIds.add(detail.getDetailId());
//					}
//				}
//				LOGGER.debug("排班ids"+StringHelper.toJsonString(detailIds));
//				Integer linkedCount = punchSchedulingProvider.countSchedulingUser(pr.getId(), new java.sql.Date(start.getTimeInMillis()), new java.sql.Date(end.getTimeInMillis()), detailIds);
//				int unlikedCount = detailIds.size() - linkedCount;
//				response.setUnSchedulingCount(response.getUnSchedulingCount()+unlikedCount);
            }
            List<UniongroupMemberDetail> employees = uniongroupConfigureProvider.listUniongroupMemberDetail(groupList, CONFIG_VERSION_CODE);
            List<Long> detailIds = new ArrayList<>();
            if (null != employees) {
                for (UniongroupMemberDetail detail : employees) {
                    detailIds.add(detail.getDetailId());
                }
            }
            Integer linkedCount = punchSchedulingProvider.countSchedulingUser(prIdList, new java.sql.Date(start.getTimeInMillis()), new java.sql.Date(end.getTimeInMillis()), detailIds);
            int unlikedCount = detailIds.size() - linkedCount;
            response.setUnSchedulingCount(unlikedCount);
        }

        return response;
    }

    /**
     * 获取key在redis操作的valueOperations
     */
    private ValueOperations<String, String> getValueOperations(String key) {
        final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        Accessor acc = bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        return valueOperations;
    }


    /**
     * 清除redis中key的缓存
     */
    private void deleteValueOperations(String key) {
        final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        Accessor acc = bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        redisTemplate.delete(key);
    }

    @Override
    public CheckAbnormalStatusResponse checkAbnormalStatus(CheckPunchAdminCommand cmd) {
        CheckAbnormalStatusResponse response = new CheckAbnormalStatusResponse();
        cmd.setOrganizationId(getTopEnterpriseId(cmd.getOrganizationId()));
        GeneralApproval approval = generalApprovalService.getGeneralApprovalByAttribute(cmd.getOrganizationId(), GeneralApprovalAttribute.ABNORMAL_PUNCH.getCode());
        if (null != approval) {
            response.setAbnormalStatus(approval.getStatus());
            String approvalRoute = "zl://form/create?sourceType=GENERAL_APPROVE&sourceId="
                    + approval.getId() + "&ownerType=" + approval.getOwnerType() + "&ownerId="
                    + approval.getOwnerId() + "&displayName=打卡异常&metaObject=";
            response.setApprovalRoute(approvalRoute);
        }
        return response;
    }

    @Override
    public void punchGroupAddNewEmployee(Long groupId) {
        PunchRule pr = punchProvider.getPunchruleByPunchOrgId(groupId);

        if (null == pr)
            return;
        //对于活动状态的要变成修改状态,并且复制班次和排班
        if (PunchRuleStatus.ACTIVE.getCode() == pr.getStatus().byteValue()) {

            List<PunchTimeRule> timeRules = punchProvider.listActivePunchTimeRuleByOwner(PunchOwnerType.ORGANIZATION.getCode(), pr.getPunchOrganizationId(), pr.getStatus());
            //这个考勤组改成修改状态
            pr.setStatus(PunchRuleStatus.MODIFYED.getCode());
            //把班次复制一份修改状态的
            if (null != timeRules) {
                for (PunchTimeRule timeRule : timeRules) {
                    List<PunchTimeInterval> timeIntervals = punchProvider.listPunchTimeIntervalByTimeRuleId(timeRule.getId());
                    timeRule.setStatus(pr.getStatus());
                    punchProvider.createPunchTimeRule(timeRule);
                    if (null != timeIntervals) {
                        for (PunchTimeInterval interval : timeIntervals) {
                            interval.setTimeRuleId(timeRule.getId());
                            punchProvider.createPunchTimeInterval(interval);
                        }
                    }
                }
            }

            //排班表复制一份修改状态的
            Calendar end = Calendar.getInstance();
            end.set(Calendar.DAY_OF_MONTH, 1);
            end.set(Calendar.HOUR_OF_DAY, 0);
            end.set(Calendar.MINUTE, 0);
            end.set(Calendar.SECOND, 0);
            end.set(Calendar.MILLISECOND, 0);
            end.add(Calendar.MONTH, 2);
            List<PunchScheduling> psList = punchSchedulingProvider.queryPunchSchedulings(new java.sql.Date(new Date().getTime()),
                    new java.sql.Date(end.getTimeInMillis()), pr.getId(), PunchRuleStatus.ACTIVE.getCode());
            if (null != psList) {
                for (PunchScheduling ps : psList) {
                    ps.setStatus(pr.getStatus());
                    punchSchedulingProvider.createPunchScheduling(ps);
                }
            }

            //组织架构调用的接口不一定有userContext
//			pr.setOperatorUid(UserContext.current().getUser().getId());
            pr.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            punchProvider.updatePunchRule(pr);
        }
    }

    @Override
    public ListPunchLogsResponse listPunchLogs(ListPunchLogsCommand cmd) {
        List<PunchLog> punchLogs = punchProvider.listPunchLogsByDate(cmd.getUserId(), cmd.getEnterpriseId(),
                dateSF.get().format(new Date(cmd.getQueryTime())), ClockCode.SUCESS.getCode());
        if (null == punchLogs) {
            return null;
        }
        ListPunchLogsResponse response = new ListPunchLogsResponse();
        List<PunchLogDTO> dtos = punchLogs.stream().map(pl -> {
            PunchLogDTO dto1 = new PunchLogDTO();
            if (pl.getRuleTime() != null) {
                dto1 = ConvertHelper.convert(pl, PunchLogDTO.class);
            } else {
                dto1.setPunchType(pl.getPunchType());
            }

            if (null != pl.getPunchTime()) {
                dto1.setPunchTime(pl.getPunchTime().getTime());
            }
            dto1.setClockStatus(pl.getStatus());
            return dto1;
        }).collect(Collectors.toList());
        response.setPunchLogs(dtos);
        return response;
    }

    private Organization getOrganizationFromLocalCache(Map<Long, Organization> cacheOrganizationMap, Long punchOrganizationId) {
        Organization org = null;
        if (cacheOrganizationMap.containsKey(punchOrganizationId)) {
            org = cacheOrganizationMap.get(punchOrganizationId);
        } else {
            org = organizationProvider.findOrganizationById(punchOrganizationId);
            if (org != null) {
                cacheOrganizationMap.put(org.getId(), org);
            }
        }
        return org;
    }
    /**
     * 每日早上5点30,刷新前一天的当月的月报数据
     */
    @Scheduled(cron = "1 30 5 * * ?")
    public void refreshStatistics() {
    	Calendar calendar = Calendar.getInstance();
    	calendar.add(Calendar.DAY_OF_MONTH, -1);
    	refreshMonthReport(monthSF.get().format(calendar.getTime()));
    }
    /**
     * 每月1日早上1点50,新建当月的月报数据
     */
    @Scheduled(cron = "1 50 1 1 * ?")
    public void refreshMonthReport() {
        refreshMonthReport(monthSF.get().format(DateHelper.currentGMTTime()));
    }
    @Override
    public void refreshMonthReport(String month) {
        if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
            this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_PUNCH_MONTH_REPORT.getCode()).enter(() -> {
                List<Long> enterpriseIds = uniongroupConfigureProvider.distinctEnterpriseIdsFromUniongroupMemberDetails();
                if(null != enterpriseIds) {
                    for (Long enterpriseId : enterpriseIds) {
                    	try{
                        this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_PUNCH_MONTH_REPORT.getCode() + enterpriseId).enter(() -> {
                            PunchMonthReport report = punchMonthReportProvider.findPunchMonthReportByOwnerMonth(enterpriseId, month);
                            if (null == report) {
                                report = createNewReport(PunchOwnerType.ORGANIZATION.getCode(), enterpriseId, month);
                            }
                            UpdateMonthReportCommand cmd = new UpdateMonthReportCommand();
                            cmd.setOwnerType(PunchOwnerType.ORGANIZATION.getCode());
                            cmd.setOwnerId(enterpriseId);
                            cmd.setMonthReportId(report.getId());
                            updateMonthReport(cmd);
                            return null;
                        });
                    	}catch(Exception e){
                    		LOGGER.error("refresh month report error enterprise "+enterpriseId,e);
                    	}
                    }
                }
                return null;
            });
        }
    }

	@Override
	public ListPunchMonthReportsResponse listPunchMonthReports(ListPunchMonthReportsCommand cmd) {
        ListPunchMonthReportsResponse resp = new ListPunchMonthReportsResponse(null, new ArrayList<>());

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_PUNCH_MONTH_REPORT.getCode() + cmd.getOwnerId()).enter(() -> {
            List<PunchMonthReport> results = punchMonthReportProvider.listPunchMonthReport(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getPunchYear(), pageSize + 1, locator);
            if (null != results && results.size() > 0) {
                Long nextPageAnchor = null;
                if (results != null && results.size() > pageSize) {
                    results.remove(results.size() - 1);
                    nextPageAnchor = results.get(results.size() - 1).getId();
                }
                resp.setNextPageAnchor(nextPageAnchor);
                for (PunchMonthReport report : results) {
                    PunchMonthReportDTO dto = ConvertHelper.convert(report, PunchMonthReportDTO.class);
                    resp.getMonthReports().add(dto);
                }
            } else {
                PunchMonthReport report = createNewReport(cmd.getOwnerType(), cmd.getOwnerId(), monthSF.get().format(DateHelper.currentGMTTime()));
                PunchMonthReportDTO dto = ConvertHelper.convert(report, PunchMonthReportDTO.class);
                resp.getMonthReports().add(dto);
            }
            return null;
        });
        return resp;
	}

    private PunchMonthReport createNewReport(String ownerType, Long ownerId, String month) {
        PunchMonthReport report = new PunchMonthReport();
        report.setOwnerType(ownerType);
        report.setOwnerId(ownerId);
        report.setPunchMonth(month);
        report.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        report.setCreatorUid(UserContext.currentUserId());
        report.setStatus(PunchMonthReportStatus.CREATED.getCode());
        punchMonthReportProvider.createPunchMonthReport(report);
        return report;
    }

    /**
     * 处理更新月报的线程池,预设最大值是10(同时又10个线程进行)
     */
    private static ExecutorService monthReportExecutorPool = Executors.newFixedThreadPool(10);
    @Override
	public void updateMonthReport(UpdateMonthReportCommand cmd) {
        PunchMonthReport report =  this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_PUNCH_MONTH_REPORT.getCode() + cmd.getOwnerId()+"."+ cmd.getMonthReportId()).enter(() -> {
            PunchMonthReport report1 = punchMonthReportProvider.findPunchMonthReportById(cmd.getMonthReportId());
            if (PunchMonthReportStatus.fromCode(report1.getStatus()) == PunchMonthReportStatus.UPDATING
            		|| PunchMonthReportStatus.fromCode(report1.getStatus()) == PunchMonthReportStatus.FILING) {
                throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
                		PunchServiceErrorCode.ERROR_CANNOT_UPDATE_MONTH,
                        "当前状态不可更新");
            }
            report1.setProcess(0);
            setMonthReportProcess(report1, 0);
            report1.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime())); 
            report1.setStatus(PunchMonthReportStatus.UPDATING.getCode());
            punchMonthReportProvider.updatePunchMonthReport(report1);
            Tuple<PunchMonthReport, Boolean> tuple = new Tuple<PunchMonthReport, Boolean>(report1,true);
            return tuple;
        }).first().first();

        monthReportExecutorPool.execute(() -> {

            try {
            	this.dbProvider.execute((TransactionStatus status) -> {
            		List<OrganizationMemberDetails> members = listMembers(cmd.getOwnerId(), null, report.getPunchMonth(), Integer.MAX_VALUE -1 , null, null);
            		Integer count = (members == null) ? 0 : members.size();
                    report.setPunchMemberNumber(count);
	                setMonthReportProcess(report, 5);
                    for (int i = 0; i < members.size(); i++) {
                        try {
                            addPunchStatistics(members.get(i), cmd.getOwnerId(), report.getPunchMonth());
                        } catch (Exception e) {
                            LOGGER.error("update punch month report error ", e);
                        }
                        if (i % 10 == 0) {
                            setMonthReportProcess(report, 5 + (int) (i / (Double.valueOf(members.size()) / 94.00)));
                        }
                    }
	                return null;
                });
            } catch (Exception e) {
                LOGGER.error("calculate reports error!! cmd is  :" + cmd, e);
            } finally {
                report.setProcess(100);
                //更新完成删除redis的记录
                deleteValueOperations(getMonthReportProcessKey(report));
                setReportStatusAfterUpdate(report);
                punchMonthReportProvider.updatePunchMonthReport(report);
            }
        });

	}
    /**
     * 更新结束后,将report的状态置回创建完成或已归档
     * */
    private void setReportStatusAfterUpdate(PunchMonthReport report) {
        if(report.getFileTime()==null){
        	report.setStatus(PunchMonthReportStatus.CREATED.getCode());
        }else{
        	report.setStatus(PunchMonthReportStatus.FILED.getCode());
        }
	}

	private void setMonthReportProcess(PunchMonthReport report, int i) {
//        report.setProcess(i);
//        punchMonthReportProvider.updatePunchMonthReport(report);
		String key = getMonthReportProcessKey(report);
        ValueOperations<String, String> valueOperations = getValueOperations(key);
        int timeout = configurationProvider.getIntValue(PunchConstants.PUNCH_QRCODE_TIMEOUT, 15);
        TimeUnit unit = TimeUnit.MINUTES;
        valueOperations.set(key, i + "", timeout, unit);

    }

    private List<OrganizationMemberDetails> listMembers(Long ownerId, Long deptId, String punchMonth, Integer pageSize, Long anchor, String userName) {
        // yyyyMM to yyyy-MM
        String punchMonthInLine = punchMonth.substring(0, 4) + "-" + punchMonth.substring(4, 6);
        List<OrganizationMemberDetails> records = archivesService.queryArchivesEmployees(new ListingLocator(), ownerId, deptId, (locator, query) -> {
            //月底之后离职或者未离职
            query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.EMPLOYEE_STATUS.ne(EmployeeStatus.DISMISSAL.getCode())
                    .or(Tables.EH_ORGANIZATION_MEMBER_DETAILS.DISMISS_TIME.like(punchMonthInLine + "%")));
            //月底之前入职
            query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.CHECK_IN_TIME.lessOrEqual(socialSecurityService.getTheLastDate(punchMonth)));

            if (null != userName) {
                query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.CONTACT_NAME.like("%"+userName+"%"));
            }
            if (null != anchor) {
                query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.ID.lt(anchor));
            }
            query.addOrderBy(Tables.EH_ORGANIZATION_MEMBER_DETAILS.ID.desc());

            query.addOrderBy(Tables.EH_ORGANIZATION_MEMBER_DETAILS.CHECK_IN_TIME.desc());
            query.addLimit( pageSize + 1);
            return query;
        });
        if (records == null || records.size() == 0) {
            return null;
        }
        return records;
    }

	@Override
	public GetMonthReportProcessResponse getMonthReportProcess(GetMonthReportProcessCommand cmd) {
        PunchMonthReport report = punchMonthReportProvider.findPunchMonthReportById(cmd.getMonthReportId());
        return new GetMonthReportProcessResponse(getMonthReportProcessByRedis(report));
	}

	private Integer getMonthReportProcessByRedis(PunchMonthReport report) {
        if (PunchMonthReportStatus.fromCode(report.getStatus()) != PunchMonthReportStatus.UPDATING
                && PunchMonthReportStatus.fromCode(report.getStatus()) != PunchMonthReportStatus.FILING) {
            return 100;
        }
		String key = getMonthReportProcessKey(report);
        ValueOperations<String, String> valueOperations = getValueOperations(key);
        String value = valueOperations.get(key);
        if(null == value){
        	//redis里面找不到key可能是中途服务器宕机,这时候把report状态置为可以更新
        	report.setProcess(100);
        	setReportStatusAfterUpdate(report);
            punchMonthReportProvider.updatePunchMonthReport(report);
        	return 100;
        }
		return Integer.valueOf(value);
	}

	private String getMonthReportProcessKey(PunchMonthReport report) {
		return PunchConstants.MONTH_REPORT_PROCESS + report.getId();
	}

	@Override
	public void fileMonthReport(FileMonthReportCommand cmd) {
        PunchMonthReport report =  this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_PUNCH_MONTH_REPORT.getCode() + cmd.getOwnerId()+"."+ cmd.getMonthReportId()).enter(() -> {
            PunchMonthReport report1 = punchMonthReportProvider.findPunchMonthReportById(cmd.getMonthReportId());
            if (PunchMonthReportStatus.fromCode(report1.getStatus()) == PunchMonthReportStatus.UPDATING
            		|| PunchMonthReportStatus.fromCode(report1.getStatus()) == PunchMonthReportStatus.FILING) {
                throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
                		PunchServiceErrorCode.ERROR_CANNOT_UPDATE_MONTH,
                        "当前状态不可归档");
            }

            report1.setProcess(0);
            setMonthReportProcess(report1, 0);
            report1.setFilerUid(UserContext.currentUserId());
            report1.setFileTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            report1.setFilerName(socialSecurityService.findNameByOwnerAndUser(cmd.getOwnerId(), report1.getFilerUid()));
            report1.setStatus(PunchMonthReportStatus.FILING.getCode());
        	punchMonthReportProvider.updatePunchMonthReport(report1);
            Tuple<PunchMonthReport, Boolean> tuple = new Tuple<PunchMonthReport, Boolean>(report1,true);
            return tuple;
        }).first().first();
        report.setStatus(PunchMonthReportStatus.FILED.getCode());
        //把本月本公司三个表归档数据复制一份到file表
        monthReportExecutorPool.execute(() -> {
            this.dbProvider.execute((TransactionStatus status) -> {
            	try{
	            	setMonthReportProcess(report, 10);
	            	filePunchLogs(report);
	            	setMonthReportProcess(report, 50);
	            	filePunchDayLogs(report);
	            	setMonthReportProcess(report, 70);
	            	filePunchStatistics(report);
	            	setMonthReportProcess(report, 90);
            	}catch(Exception e){
            		LOGGER.error("归档出错 ",e);
            		report.setErrorInfo("归档出错");
            	}
            	finally{
                    deleteValueOperations(getMonthReportProcessKey(report));
                    report.setProcess(100);
                    report.setStatus(PunchMonthReportStatus.FILED.getCode());
	            	punchMonthReportProvider.updatePunchMonthReport(report);
            	}
            	return null;
            });
        });
	}

	private void filePunchStatistics(PunchMonthReport report1) {
		punchProvider.deletePunchDayLogs(report1.getOwnerId(), report1.getPunchMonth());
		punchProvider.filePunchDayLogs(report1.getOwnerId(), report1.getPunchMonth(), report1);
	}

	private void filePunchDayLogs(PunchMonthReport report1) { 
		java.sql.Date monthBegin = socialSecurityService.getTheFirstDate(report1.getPunchMonth());
		java.sql.Date monthEnd = socialSecurityService.getTheLastDate(report1.getPunchMonth());
		punchProvider.deletePunchDayLogs(report1.getOwnerId(), monthBegin, monthEnd);
		punchProvider.filePunchDayLogs(report1.getOwnerId(), monthBegin, monthEnd, report1);
	}

	private void filePunchLogs(PunchMonthReport report1) { 
		java.sql.Date monthBegin = socialSecurityService.getTheFirstDate(report1.getPunchMonth());
		java.sql.Date monthEnd = socialSecurityService.getTheLastDate(report1.getPunchMonth());
		punchProvider.deletePunchLogs(report1.getOwnerId(), monthBegin, monthEnd);
		punchProvider.filePunchLogs(report1.getOwnerId(), monthBegin, monthEnd, report1);
	}

	@Override
    public ListOrganizationPunchLogsResponse listOrganizationPunchLogs(ListOrganizationPunchLogsCommand cmd) {
        if (null == cmd.getOwnerId() || null == cmd.getOwnerType()) {
            LOGGER.error("Invalid owner type or  Id parameter in the command");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid owner type or  Id parameter in the command");
        }
        if (null != cmd.getMonthReportId()) {
            PunchMonthReport report = punchMonthReportProvider.findPunchMonthReportById(cmd.getMonthReportId());
            if (null != report) {
                cmd.setStartDay(socialSecurityService.getTheFirstDate(report.getPunchMonth()).getTime());
                cmd.setEndDay(socialSecurityService.getTheLastDate(report.getPunchMonth()).getTime());
            }
        }  
        ListOrganizationPunchLogsResponse response = new ListOrganizationPunchLogsResponse();
        Long ownerId = getTopEnterpriseId(cmd.getOwnerId());
        //分页查询 由于用到多条件排序,所以使用pageOffset方式分页
        Integer pageOffset = 0;
        if (cmd.getPageAnchor() != null)
            pageOffset = cmd.getPageAnchor().intValue();
        int pageSize = getPageSize(configurationProvider, cmd.getPageSize());
        List<Long> userIds = new ArrayList<>(); 

            //找到所有子部门 下面的用户
        Organization org = this.checkOrganization(cmd.getOwnerId());
        if (null != cmd.getUserId()) {
            userIds.add(cmd.getUserId());
        } else {
            userIds = listDptUserIds(org, cmd.getOwnerId(), cmd.getUserName(), NormalFlag.YES.getCode());
            if (CollectionUtils.isEmpty(userIds))
                return response;
        }
        String startDay = null;
        if (null != cmd.getStartDay())
            startDay = dateSF.get().format(new Date(cmd.getStartDay()));
        String endDay = null;
        if (null != cmd.getEndDay())
            endDay = dateSF.get().format(new Date(cmd.getEndDay()));

        List<PunchLog> results = punchProvider.listPunchLogs(userIds, ownerId, startDay, endDay, cmd.getExceptionStatus(), pageOffset, pageSize + 1);
        if (CollectionUtils.isEmpty(results))
            return response;
        if (results.size() == pageSize + 1) {
            results.remove(pageSize);
            Long nextPageAnchor = Long.valueOf(pageOffset + pageSize);
            response.setNextPageAnchor(nextPageAnchor);
        }

        Map<Long, OrganizationMemberDetails> memberDetailMap = new HashMap<>();
        Map<Long, String> dptMap = new HashMap<>();
        Map<String, PunchRule> punchRuleMap = new HashMap<>();
        Map<Long,String> ruleMap = new HashMap<>();
        response.setPunchLogs(results.stream().map(r->{
            return processPunchLogDTO(r,memberDetailMap,punchRuleMap,dptMap, ruleMap);
        }).collect(Collectors.toList()));
        
		return response;
	}
 
	@Override
	public GetOrgCheckInDataResponse getOrgCheckInData(GetOrgCheckInDataCommand cmd) {
		
		AppNamespaceMapping appNamespaceMapping = appNamespaceMappingProvider.findAppNamespaceMappingByAppKey(cmd.getAppKey());
		if (appNamespaceMapping == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
					"not exist app namespace mapping");
		}
		GetOrgCheckInDataResponse response = new GetOrgCheckInDataResponse();
		
		List<Long> userIds = null;
		if(null != cmd.getUserId()){
			userIds = new ArrayList<>();
			userIds.add(cmd.getUserId());
		}
		else if(StringUtils.isNotEmpty(cmd.getUserContactToken())){
			List<EhOrganizationMembers> organizationMembers = organizationProvider.listOrganizationMemberByToken(cmd.getUserContactToken());
			if(null != organizationMembers){
				userIds = new ArrayList<>();
				for(EhOrganizationMembers member : organizationMembers){
					if(member.getTargetId() != null && !member.getTargetId().equals(0L)){
						userIds.add(member.getTargetId());
					}
				}
			}
		}
		List<OrganizationMember> members = organizationProvider.listOrganizationMembersByOrgId(cmd.getOrgId());
		List<PunchLog> logs = punchProvider.listPunchLogsForOpenApi(cmd.getOrgId(), userIds , cmd.getBeginDate(),cmd.getEndDate());
		if(null != logs && logs.size() > 0){
			response.setCheckinDatas(new ArrayList<>());
			for(PunchLog pl : logs){
				try{
					CheckInDataDTO dto = new CheckInDataDTO();
					dto.setId(pl.getId());
					if(null != pl.getPunchDate()){
						dto.setCheckInDate(pl.getPunchDate().getTime());
					}
					dto.setLatitude(pl.getLatitude());
					dto.setLongitude(pl.getLongitude());
					dto.setLocationInfo(pl.getLocationInfo());
					dto.setWifiInfo(pl.getWifiInfo());
					dto.setUserId(pl.getUserId());
					dto.setStatus(statusToString(null, pl.getApprovalStatus() == null ? pl.getStatus() : pl.getApprovalStatus()));
					if(null != pl.getPunchTime()){
						dto.setCheckInTime(pl.getPunchTime().getTime());
					}else if(PunchStatus.NORMAL == PunchStatus.fromCode(pl.getApprovalStatus() == null ? pl.getStatus() : pl.getApprovalStatus())){
						dto.setCheckInTime(pl.getPunchDate().getTime() + (pl.getShouldPunchTime() == null ? pl.getRuleTime() : pl.getShouldPunchTime()));
					}
					OrganizationMember member = findOrganizationMemberByTargetId(members, pl.getUserId());
					if(null != member){
						dto.setUserName(member.getContactName());
						dto.setContactToken(member.getContactToken());
					}
					response.getCheckinDatas().add(dto);
				}catch(Exception e){
					LOGGER.error("pl " + StringHelper.toJsonString(pl) + " 出问题了!", e);
				}
				 
			}
		} 
		return response;
	}
 
    private OrganizationMember findOrganizationMemberByTargetId(List<OrganizationMember> members,
			Long userId) {
    	if(null != members && null != userId){
			for(OrganizationMember member : members){
				if(userId.equals(member.getTargetId())){
					return member;
				}
			}
    	}
		return null;
	}

	private PunchLogDTO processPunchLogDTO(PunchLog log, Map<Long, OrganizationMemberDetails> memberDetailMap, Map<String, PunchRule> punchRuleMap, Map<Long, String> dptMap, Map<Long, String> ruleMap) {
        PunchLogDTO dto = convertPunchLog2DTO(log);
        OrganizationMemberDetails detail = findOrganizationMemberDetailByCacheUserId(log.getEnterpriseId(), log.getUserId(), memberDetailMap);
        if (null != detail) {
            dto.setUserName(detail.getContactName());
        }
        String depName = findDptNameByCache(detail.getId(), dptMap);
        if (null != depName) {
            dto.setDeptName(depName);
        }

        dto.setRuleName(findPunchOrganizationName(ruleMap, log.getPunchOrganizationId()));

        if (null == log.getLocationInfo()) {
            dto.setLocationInfo(log.getWifiInfo());
        }

        return dto;
    }


    private String findDptNameByCache(Long detailId, Map<Long, String> dptMap) {
        String result = dptMap.get(detailId);
        if (null == result) {
            result = archivesService.convertToOrgNames(archivesService.getEmployeeDepartment(detailId));
            dptMap.put(detailId, result);
        }
        return result;
    }

    private OrganizationMemberDetails findOrganizationMemberDetailByCacheUserId(Long enterpriseId, Long userId, Map<Long, OrganizationMemberDetails> memberDetailMap) {
        OrganizationMemberDetails result = memberDetailMap.get(userId);
        if (result == null) {
            result = organizationProvider.findOrganizationMemberDetailsByTargetId(userId, enterpriseId);
            memberDetailMap.put(userId, result);
        }
        return result;
    }

    @Override
    public Date parseDateTimeByTimeSelectType(Long organizationId, Long userId, String day, ApprovalCategoryTimeSelectType type) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(day);
            PunchRule pr = getPunchRule(PunchOwnerType.ORGANIZATION.getCode(), organizationId, userId);
            if (pr == null) {
                return getDefaultDateTimeByTimeSelectType(date, type);
            }
            PunchTimeRule ptr = getPunchTimeRuleByRuleIdAndDate(pr, date, userId);
            if (ptr == null) {
                return getDefaultDateTimeByTimeSelectType(date, type);
            }

            List<PunchTimeIntervalDTO> timeIntervals = punchBaseService.findPunchTimeIntervals(ptr);

            if (CollectionUtils.isEmpty(timeIntervals)) {
                return getDefaultDateTimeByTimeSelectType(date, type);
            }

            Long beginTime = timeIntervals.get(0).getArriveTime();
            Long endTime = timeIntervals.get(0).getLeaveTime();

            if (timeIntervals.size() > 1) {
                for (int i = 1; i < timeIntervals.size(); i++) {
                    // beginTime取最小（最早）
                    if (Long.compare(beginTime, timeIntervals.get(i).getArriveTime()) > 0) {
                        beginTime = timeIntervals.get(i).getArriveTime();
                    }
                    // endTime取最大（最晚）
                    if (Long.compare(endTime, timeIntervals.get(i).getLeaveTime()) < 0) {
                        endTime = timeIntervals.get(i).getLeaveTime();
                    }
                }
            }
            return getDateTimeByTimeSelectType(date, beginTime, endTime, type);
        } catch (ParseException e) {
            throw new RuntimeException();
        }
    }

    private Date getDefaultDateTimeByTimeSelectType(Date date, ApprovalCategoryTimeSelectType type) {
        if (type == null) {
            return date;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        switch (type) {
            case FIRST_HALF_BEGIN:
                calendar.set(Calendar.HOUR_OF_DAY, 8);
                break;
            case FIRST_HALF_END:
                calendar.set(Calendar.HOUR_OF_DAY, 12);
                break;
            case SECOND_HALF_BEGIN:
                calendar.set(Calendar.HOUR_OF_DAY, 13);
                break;
            case SECOND_HALF_END:
                calendar.set(Calendar.HOUR_OF_DAY, 18);
                break;
        }
        return calendar.getTime();
    }

    private static Date getDateTimeByTimeSelectType(Date date, Long beginTime, Long endTime, ApprovalCategoryTimeSelectType type) {
        if (type == null) {
            return date;
        }
        Long middleTime = beginTime + (endTime - beginTime) / 2;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        switch (type) {
            case FIRST_HALF_BEGIN:
                calendar.add(Calendar.MILLISECOND, beginTime.intValue());
                break;
            case FIRST_HALF_END:
            case SECOND_HALF_BEGIN:
                calendar.add(Calendar.MILLISECOND, middleTime.intValue());
                break;
            case SECOND_HALF_END:
                calendar.add(Calendar.MILLISECOND, endTime.intValue());
                break;
        }
        return calendar.getTime();
    }

    private void punchStatusCountByPunchDayLog(PunchDayLog pdl) {
        if (pdl == null) {
            return;
        }
        String[] statusList = null;
        if (pdl.getStatusList().contains(PunchConstants.STATUS_SEPARATOR)) {
            statusList = pdl.getStatusList().split(PunchConstants.STATUS_SEPARATOR);
            if (pdl.getApprovalStatusList() != null && pdl.getApprovalStatusList().contains(PunchConstants.STATUS_SEPARATOR)) {
                String[] asList = StringUtils.splitPreserveAllTokens(pdl.getApprovalStatusList(), PunchConstants.STATUS_SEPARATOR);
                for (int i = 0; i < asList.length && i < statusList.length; i++) {
                    if (StringUtils.isNotBlank(asList[i])) {
                        statusList[i] = asList[i];
                    }
                }
            }
        } else {
            statusList = new String[]{pdl.getStatusList()};
            if (StringUtils.isNotBlank(pdl.getApprovalStatusList())) {
                statusList[0] = pdl.getApprovalStatusList();
            }
        }
        if (pdl.getTimeRuleId() == null || pdl.getTimeRuleId() == 0) {
            pdl.setRestFlag(NormalFlag.YES.getCode());
        } else {
            pdl.setRestFlag(NormalFlag.NO.getCode());
        }
        pdl.setAbsentFlag(isAbsence(statusList));
        pdl.setNormalFlag(isFullNormal(statusList));
        if(NormalFlag.NO == NormalFlag.fromCode(pdl.getNormalFlag())){
        	pdl.setExceptionStatus(ExceptionStatus.EXCEPTION.getCode());
        }
        pdl.setBelateCount(belateCount(statusList));
        pdl.setLeaveEarlyCount(leaveEarlyCount(statusList));
        pdl.setForgotPunchCountOnDuty(forgotCountOnDuty(statusList));
        pdl.setForgotPunchCountOffDuty(forgotCountOffDuty(statusList));
    }

    private void punchExceptionRequestCountByPunchDayLog(PunchDayLog pdl, List<PunchExceptionRequest> exceptionRequests, List<PunchExceptionRequest> abnormalExceptionRequests) {
        pdl.setAskForLeaveRequestCount(0);
        pdl.setGoOutRequestCount(0);
        pdl.setOvertimeRequestCount(0);
        pdl.setBusinessTripRequestCount(0);
        pdl.setPunchExceptionRequestCount(0);
        if (!CollectionUtils.isEmpty(exceptionRequests)) {
            for (PunchExceptionRequest request : exceptionRequests) {
                GeneralApprovalAttribute attribute = GeneralApprovalAttribute.fromCode(request.getApprovalAttribute());
                switch (attribute) {
                    case ASK_FOR_LEAVE:
                        pdl.setAskForLeaveRequestCount(pdl.getAskForLeaveRequestCount() + 1);
                        break;
                    case GO_OUT:
                        pdl.setGoOutRequestCount(pdl.getGoOutRequestCount() + 1);
                        break;
                    case OVERTIME:
                        pdl.setOvertimeRequestCount(pdl.getOvertimeRequestCount() + 1);
                        break;
                    case BUSINESS_TRIP:
                        pdl.setBusinessTripRequestCount(pdl.getBusinessTripRequestCount() + 1);
                        break;
                }
            }
        }
        if (!CollectionUtils.isEmpty(abnormalExceptionRequests)) {
            pdl.setPunchExceptionRequestCount(abnormalExceptionRequests.size());
        } 
    }

    private void punchExceptionRequestCountByPunchMonthStatistics(PunchStatistic statistic, List<PunchExceptionRequestStatisticsItemDTO> statisticsItemDTOS) {
        statistic.setAskForLeaveRequestCount(0);
        statistic.setGoOutRequestCount(0);
        statistic.setOvertimeRequestCount(0);
        statistic.setBusinessTripRequestCount(0);

        if (!CollectionUtils.isEmpty(statisticsItemDTOS)) {
            for (PunchExceptionRequestStatisticsItemDTO item : statisticsItemDTOS) {
                if (PunchExceptionRequestStatisticsItemType.ASK_FOR_LEAVE.name().equals(item.getItemName())) {
                    statistic.setAskForLeaveRequestCount(item.getNum() != null ? item.getNum() : 0);
                } else if (PunchExceptionRequestStatisticsItemType.GO_OUT.name().equals(item.getItemName())) {
                    statistic.setGoOutRequestCount(item.getNum() != null ? item.getNum() : 0);
                } else if (PunchExceptionRequestStatisticsItemType.BUSINESS_TRIP.name().equals(item.getItemName())) {
                    statistic.setBusinessTripRequestCount(item.getNum() != null ? item.getNum() : 0);
                } else if (PunchExceptionRequestStatisticsItemType.OVERTIME.name().equals(item.getItemName())) {
                    statistic.setOvertimeRequestCount(item.getNum() != null ? item.getNum() : 0);
                }
            }
        }
    }

    @Override
    public GetOvertimeInfoResponse getOvertimeInfo(GetOvertimeInfoCommand cmd){
    	return new GetOvertimeInfoResponse(processOvertimeInfo(cmd.getOwnerId(), UserContext.currentUserId()));
    }
    /**
     * 查询某日某部门的人员列表--通过日报查询
     * */
    public List<PunchMemberDTO> listDailyPunchMemberDTOs(Long orgId, Long deptId, Date queryDate, PunchStatusStatisticsItemType itemType, Integer pageOffset, Integer pageSize){
    	List<PunchMemberDTO> results = new ArrayList<>();
        List<Long> deptIds = findSubDepartmentIds(deptId);
        String startDay = dateSF.get().format(queryDate);
        String endDay = dateSF.get().format(queryDate);

        List<PunchDayLog> pdls = punchProvider.listPunchDayLogsByItemTypeAndDeptIds(orgId, deptIds, startDay, endDay, itemType, pageOffset, pageSize);
        if(pdls != null && pdls.size() > 0){
            for(PunchDayLog pdl : pdls){
                PunchMemberDTO dto = convertPDLToPunchMemberDTO(pdl);
                dto.setStatisticsUnit(itemType ==null ? null : itemType.getUnit());
                dto.setStatisticsCount(getPunchStatisticCountByItemType(pdl, itemType));

                results.add(dto);
            }
        }
	    return results;
    }
    @Override
    public String getAdjustRuleUrl(HttpServletRequest request){
        String homeUrl = request.getHeader("Host");
        return "http://" + homeUrl + "/mobile/static/oa_punch/adjust_rule.html#sign_suffix";
    }

    private Integer getPunchStatisticCountByItemType(PunchDayLog pdl, PunchStatusStatisticsItemType itemType) {
        Integer result = 0 ;
        if (itemType != null) {
            switch (itemType) {
                case GO_OUT:
                    result = 1;
                    break;
                case BELATE:
                    result = pdl.getBelateCount();
                    break;
                case LEAVE_EARLY:
                    result = pdl.getLeaveEarlyCount();
                    break;
                case NORMAL:
                    result = 1;
                    break;
                case REST:
                    result = 1;
                    break;
                case ABSENT:
                    result = 1;
                    break;
                case FORGOT_PUNCH:
                    result = pdl.getForgotPunchCountOnDuty() + pdl.getForgotPunchCountOffDuty();
                    break;
                default:
                    result = 1;
                    break;
            }
        }
        return result;
    }

    private PunchMemberDTO convertPDLToPunchMemberDTO(PunchDayLog pdl) {
        PunchMemberDTO dto = new PunchMemberDTO();
        dto.setOrganizationId(pdl.getEnterpriseId());
        dto.setPunchOrganizationId(pdl.getPunchOrganizationId());
        dto.setDetailId(pdl.getDetailId());
        if(null != pdl.getDetailId()){
            OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(pdl.getDetailId());
            dto.setContractName(detail.getContactName());
        }
        dto.setUserId(pdl.getUserId());
        if(null != pdl.getUserId() && pdl.getUserId() > 0L){
            User u = this.userProvider.findUserById(pdl.getUserId());
            dto.setContactAvatar(contentServerService.parserUri(u.getAvatar(), EntityType.USER.getCode(),u.getId()));
        }
        dto.setDepartmentId(pdl.getDeptId());
        if(null != pdl.getDeptId()){
            Organization dpt = checkOrganization(pdl.getDeptId());
            dto.setDepartmentName(dpt.getName());
        }
        try{
            PunchRule pr = getPunchRuleByDetailId(UserContext.getCurrentNamespaceId(), PunchOwnerType.ORGANIZATION.getCode(), pdl.getEnterpriseId(), pdl.getDetailId());
            if(pr != null){
                dto.setRuleId(pr.getId());
            }
        }catch (Exception e ){
            LOGGER.error("get punch rule error ownerId" + pdl.getEnterpriseId() + " detailID" + pdl.getDetailId(), e);
        }
        return dto;
    }

    @Override
    public ListPunchStatusMembersResponse listMembersOfAPunchStatus(
            ListPunchStatusMembersCommand cmd) {
        checkUserStatisticPrivilege(cmd.getOrganizationId());

        ListPunchStatusMembersResponse response = new ListPunchStatusMembersResponse();

        PunchStatusStatisticsItemType itmeType = PunchStatusStatisticsItemType.fromCode(cmd.getPunchStatusStatisticsItemType());
        //如果查询日期是 null设为当天
        if (cmd.getQueryByDate() == null) {
            cmd.setQueryByDate(DateHelper.currentGMTTime().getTime());
        }
        List<PunchStatusStatisticsItemDTO> items = null;


        Integer pageOffset = cmd.getPageOffset() == null ? 1 : cmd.getPageOffset();
        int pageSize = getPageSize(configurationProvider, cmd.getPageSize());
        List<PunchMemberDTO> results = new ArrayList<>();
        //有月查询参数就按月查询,没有就按日查询 
        if (!StringUtils.isEmpty(cmd.getQueryByMonth())) {
            MonthlyPunchStatusStatisticsRecordMapper mapper = punchProvider.monthlyPunchStatusMemberCountsByDepartment(cmd.getOrganizationId(), cmd.getQueryByMonth(), findSubDepartmentIds(cmd.getDepartmentId()));
            items = mapper.parseToPunchStatusStatisticsItems(localeStringService, UserContext.current().getUser().getLocale());
            //按月查询查statistics表
            results = listMonthPunchMemberDTOs(cmd.getOrganizationId(), cmd.getDepartmentId(), cmd.getQueryByMonth(), itmeType, pageOffset, pageSize + 1);
        } else {
            if (dateSF.get().format(new Date(cmd.getQueryByDate())).equals(dateSF.get().format(DateHelper.currentGMTTime()))) {
                DailyPunchStatusStatisticsTodayRecordMapper mapper = punchProvider.dailyPunchStatusMemberCountsTodayByDepartment(cmd.getOrganizationId(), new java.sql.Date(cmd.getQueryByDate()), findSubDepartmentIds(cmd.getDepartmentId()));
                items = mapper.parseToPunchStatusStatisticsItems(localeStringService, UserContext.current().getUser().getLocale());
            } else {
                DailyPunchStatusStatisticsHistoryRecordMapper mapper = punchProvider.dailyPunchStatusMemberCountsHistoryByDepartment(cmd.getOrganizationId(), new java.sql.Date(cmd.getQueryByDate()), findSubDepartmentIds(cmd.getDepartmentId()));
                items = mapper.parseToPunchStatusStatisticsItems(localeStringService, UserContext.current().getUser().getLocale());
            }
            //按日查询查pdl表
            Date queryDate = new Date(cmd.getQueryByDate());
            results = listDailyPunchMemberDTOs(cmd.getOrganizationId(), cmd.getDepartmentId(), queryDate, itmeType, pageOffset, pageSize + 1);
        }
        response.setAllItems(items);
        if (CollectionUtils.isNotEmpty(items)) {
            for (PunchStatusStatisticsItemDTO dto : items) {
                if (itmeType == PunchStatusStatisticsItemType.fromCode(dto.getItemType())) {
                    response.setCurrentItem(dto);
                }
            }
        }

        if (null == results || results.size() == 0)
            return response;
        if (results.size() == pageSize + 1) {
            results.remove(pageSize);
            response.setNextPageOffset(pageOffset + 1);
        }
        response.setPunchMemberDTOS(results);
        return response;
    }

	@Override
	public ListPunchMembersResponse listMembersOfDepartment(ListPunchMembersCommand cmd){
        checkUserStatisticPrivilege(cmd.getOrganizationId());

        ListPunchMembersResponse response = new ListPunchMembersResponse();
        //如果查询日期是 null设为当天 由于现在每天早上刷新日报表,不需要当天做特殊处理了
        if (cmd.getQueryByDate() == null) {
            cmd.setQueryByDate(DateHelper.currentGMTTime().getTime());
        }
		Integer pageOffset = cmd.getPageOffset() == null ? 1 : cmd.getPageOffset(); 
        int pageSize = getPageSize(configurationProvider, cmd.getPageSize());
    	List<PunchMemberDTO> results = new ArrayList<>(); 
        //有月查询参数就按月查询,没有就按日查询 有日查询参数就按参数查询,没有就查当天
		if(!StringUtils.isEmpty(cmd.getQueryByMonth())){
			//按月查询查statistics表 
			results = listMonthPunchMemberDTOs(cmd.getOrganizationId(), cmd.getDepartmentId(), cmd.getQueryByMonth(), null, pageOffset, pageSize + 1);
			List<Long> deptIds = findSubDepartmentIds(cmd.getDepartmentId()); 
			Integer totalCount = punchProvider.countPunchSatisticsByItemTypeAndDeptIds(cmd.getOrganizationId(), deptIds, cmd.getQueryByMonth());
			response.setTotal(totalCount);
		}else{
			//按日查询查pdl表
			Date queryDate = new Date(cmd.getQueryByDate()); 
			results = listDailyPunchMemberDTOs(cmd.getOrganizationId(), cmd.getDepartmentId(), queryDate, null, pageOffset, pageSize + 1); 
			List<Long> deptIds = findSubDepartmentIds(cmd.getDepartmentId()); 
			Integer totalCount = punchProvider.countPunchDayLogsByItemTypeAndDeptIds(cmd.getOrganizationId(), deptIds, queryDate);
			response.setTotal(totalCount);
		}
		
		if (null == results || results.size() == 0)
            return response;
        if (results.size() == pageSize + 1) {
            results.remove(pageSize); 
            response.setNextPageOffset(pageOffset + 1);
        }
        response.setPunchMemberDTOS(results);
		return response;
	}

    private List<PunchMemberDTO> listMonthPunchMemberDTOs(Long organizationId, Long departmentId,
			String queryByMonth, PunchStatusStatisticsItemType itemType, Integer pageOffset, int pageSize) {
    	List<PunchMemberDTO> results = new ArrayList<>(); 
    	List<Long> deptIds = findSubDepartmentIds(departmentId); 
	    List<PunchStatistic> punchStatistics = punchProvider.listPunchSatisticsByItemTypeAndDeptIds(organizationId, deptIds, queryByMonth, itemType, pageOffset, pageSize);
	    if(punchStatistics != null && punchStatistics.size() > 0){
	    	for(PunchStatistic statistic : punchStatistics){
                PunchMemberDTO dto = convertPSToPunchMemberDTO(statistic);
	    		if(itemType != null){
                    dto.setStatisticsUnit(itemType.getUnit());
		        	switch(itemType){
                        case GO_OUT:
                            dto.setStatisticsCount(statistic.getGoOutPunchDayCount());
                            break;
                        case BELATE:
                            dto.setStatisticsCount(statistic.getBelateCount());
                            break;
		        		case LEAVE_EARLY:
		        			dto.setStatisticsCount(statistic.getLeaveEarlyCount());
		        			break;
		        		case NORMAL:
		        			dto.setStatisticsCount(1);
		        			break;
		        		case REST:
		        			dto.setStatisticsCount(statistic.getRestDayCount());
		        			break;
		        		case ABSENT:
		        			dto.setStatisticsCount(statistic.getAbsenceCount().intValue());
		        			break;
		        		case FORGOT_PUNCH:
                            dto.setStatisticsCount(statistic.getForgotPunchCountOnDuty() + statistic.getForgotPunchCountOffDuty());
		        			break;
	        			default:
	        				break;
		        	}
		        }
	    		results.add(dto);
	    	}
	    }
	    return results;
	}

	private List<Long> findSubDepartmentIds(Long departmentId) {
		List<Long> deptIds = new ArrayList<>();
        Organization org = this.checkOrganization(departmentId);
        List<Organization> orgs = findSubDepartments(org); 
        deptIds.add(org.getId());
        for (Organization o : orgs) {
            deptIds.add(o.getId());
        }
		return deptIds;
	}

    private PunchMemberDTO convertPSToPunchMemberDTO(PunchStatistic statistic) {
        PunchMemberDTO dto = new PunchMemberDTO();

        dto.setOrganizationId(statistic.getOwnerId());
        dto.setDetailId(statistic.getDetailId());
        if(null != statistic.getDetailId()){
            OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(statistic.getDetailId());
            dto.setContractName(detail.getContactName());
        }
        dto.setUserId(statistic.getUserId());
        if(null != statistic.getUserId() && statistic.getUserId() > 0L){
            User u = this.userProvider.findUserById(statistic.getUserId());
            dto.setContactAvatar(contentServerService.parserUri(u.getAvatar(), EntityType.USER.getCode(),u.getId()));
        }
        dto.setDepartmentId(statistic.getDeptId());
        if(null != statistic.getDeptId()){
            Organization dpt = checkOrganization(statistic.getDeptId());
            dto.setDepartmentName(dpt.getName());
        }
        try {
            PunchRule pr = getPunchRuleByDetailId(UserContext.getCurrentNamespaceId(), statistic.getOwnerType(), statistic.getOwnerId(), statistic.getDetailId());
            if (pr != null) {
                dto.setRuleId(pr.getId());
            }
        }catch (Exception e ){
            LOGGER.error("get punch rule error ownerId" + statistic.getOwnerId() + " detailID" + statistic.getDetailId(), e);
        }
        return dto;
    }

    private List<PunchMemberDTO> listTodayPunchMemberDTOs(Long ownerId, Long deptId, Integer pageOffset, Integer pageSize) {
    	List<PunchMemberDTO> results = new ArrayList<>(); 
        List<OrganizationMemberDetails> records = archivesService.queryArchivesEmployees(new ListingLocator(), ownerId, deptId, (locator, query) -> {
            //月底之后离职或者未离职
            query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.EMPLOYEE_STATUS.ne(EmployeeStatus.DISMISSAL.getCode())); 
            query.addOrderBy(Tables.EH_ORGANIZATION_MEMBER_DETAILS.ID.desc());
	        Integer offset = pageOffset == null ? 1 : (pageOffset - 1) * pageSize; 
	        query.addLimit(offset, pageSize);
            return query;
        }); 
        
        if(records != null && records.size() > 0){
	    	for(OrganizationMemberDetails detail : records){
	    		PunchMemberDTO dto = processMemberDetailToMemberDTO(detail);
	    		results.add(dto);
	    	}
	    }
        return results;
    }

    private PunchMemberDTO processMemberDetailToMemberDTO(OrganizationMemberDetails detail) {
        PunchMemberDTO dto = new PunchMemberDTO();
        dto.setOrganizationId(detail.getOrganizationId());
        dto.setDetailId(detail.getId());
        dto.setContractName(detail.getContactName());
        dto.setUserId(detail.getTargetId());
        if(null != detail.getTargetId() && detail.getTargetId() > 0L){
            User u = this.userProvider.findUserById(detail.getTargetId());
            dto.setContactAvatar(contentServerService.parserUri(u.getAvatar(),EntityType.USER.getCode(),u.getId()));
        }
        Long userDeptId = organizationService.getDepartmentByDetailIdAndOrgId(detail.getId(), detail.getOrganizationId());
        dto.setDepartmentId(userDeptId);
        if(null != userDeptId){
            Organization dpt = checkOrganization(userDeptId);
            dto.setDepartmentName(dpt.getName());
        }
        return dto;
    }

    public String processApprovalCategorieUrl(Long ownerId,Integer namespaceId, HttpServletRequest request){
    	String homeUrl = request.getHeader("Host");
        return "http://" + homeUrl + "/mobile/static/oa_punch/remaining_rule.html?ownerId=" + ownerId + "&namespaceId=" + namespaceId +"#sign_suffix";
    }
    
    @Override
	public ListPunchExceptionRequestMembersResponse listMembersOfAPunchExceptionRequest(
			ListPunchExceptionRequestMembersCommand cmd) {
        checkUserStatisticPrivilege(cmd.getOrganizationId());
        ListPunchExceptionRequestMembersResponse response = new ListPunchExceptionRequestMembersResponse();
        PunchExceptionRequestStatisticsItemType itemType = PunchExceptionRequestStatisticsItemType.fromCode(cmd.getPunchExceptionRequestStatisticsItemType());
        List<PunchExceptionRequestStatisticsItemDTO> items = null;
		 
        GeneralApprovalAttribute approvalAttribute = convertRequestItemTypeToApprovalAttribute(cmd.getPunchExceptionRequestStatisticsItemType());
        if(cmd.getQueryByDate() == null ){
            cmd.setQueryByDate(DateHelper.currentGMTTime().getTime());
        }
        Integer pageOffset = cmd.getPageOffset() == null ? 1 : cmd.getPageOffset();
        int pageSize = getPageSize(configurationProvider, cmd.getPageSize());
        List<PunchMemberDTO> results = new ArrayList<>();
        //有月查询参数就按月查询,没有就按日查询 有日查询参数就按参数查询,没有就查当天
        if(!StringUtils.isEmpty(cmd.getQueryByMonth())){
        	PunchExceptionRequestStatisticsRecordMapper mapper = punchProvider.monthlyPunchExceptionRequestMemberCountsByDepartment(cmd.getOrganizationId(), cmd.getQueryByMonth(), findSubDepartmentIds(cmd.getDepartmentId()));
			items = mapper.parseToPunchExceptionRequestStatisticsItems(localeStringService, UserContext.current().getUser().getLocale());
            results = listMonthExceptionPunchMemberDTOs(cmd.getOrganizationId(), cmd.getDepartmentId(), cmd.getQueryByMonth(), itemType, pageOffset, pageSize + 1);
        }else if(null != cmd.getQueryByDate()){
            Date queryDate = new Date(cmd.getQueryByDate()); 
            PunchExceptionRequestStatisticsRecordMapper mapper = punchProvider.dailyPunchExceptionRequestMemberCountsByDepartment(cmd.getOrganizationId(), new java.sql.Date(cmd.getQueryByDate()), findSubDepartmentIds(cmd.getDepartmentId()));
			items = mapper.parseToPunchExceptionRequestStatisticsItems(localeStringService, UserContext.current().getUser().getLocale());
            results = listDailyExceptionMemberDTOs(cmd.getOrganizationId(), cmd.getDepartmentId(), queryDate,
                   itemType , pageOffset, pageSize + 1);
        }else{
        	PunchExceptionRequestStatisticsRecordMapper mapper = punchProvider.dailyPunchExceptionRequestMemberCountsByDepartment(cmd.getOrganizationId(), new java.sql.Date(DateHelper.currentGMTTime().getTime()), findSubDepartmentIds(cmd.getDepartmentId()));
			items = mapper.parseToPunchExceptionRequestStatisticsItems(localeStringService, UserContext.current().getUser().getLocale());
            results = listTodayExceptionPunchMemberDTOs(cmd.getOrganizationId(), cmd.getDepartmentId(), DateHelper.currentGMTTime(), approvalAttribute, pageOffset, pageSize + 1);
        }
        response.setAllItems(items);
		if(CollectionUtils.isNotEmpty(items)){
			for(PunchExceptionRequestStatisticsItemDTO dto : items){
				if(itemType == PunchExceptionRequestStatisticsItemType.fromCode(dto.getItemType())){
					response.setCurrentItem(dto);
				}
			}
		}
        if (null == results || results.size() == 0)
            return response;
        if (results.size() == pageSize + 1) {
            results.remove(pageSize);
            response.setNextPageOffset(pageOffset + 1);
        }
        response.setPunchMemberDTOS(results);
		return response;
	}
    private Integer getPunchStatisticCountByItemType(PunchDayLog pdl, PunchExceptionRequestStatisticsItemType itemType){
        Integer result = 0;
        switch(itemType){
            case ASK_FOR_LEAVE:
                result = pdl.getAskForLeaveRequestCount();
                break;
            case GO_OUT:
                result = pdl.getGoOutRequestCount();
                break;
            case BUSINESS_TRIP:
                result = pdl.getBusinessTripRequestCount();
                break;
            case OVERTIME:
                result = pdl.getOvertimeRequestCount();
                break;
            case PUNCH_EXCEPTION:
                result = pdl.getPunchExceptionRequestCount();
                break;
            default:
                break;
        }
        return result;
    }
    private List<PunchMemberDTO> listDailyExceptionMemberDTOs(Long organizationId, Long departmentId, Date queryDate, PunchExceptionRequestStatisticsItemType itemType, Integer pageOffset, int pageSize) {
        List<PunchMemberDTO> results = new ArrayList<>();
        List<Long> deptIds = findSubDepartmentIds(departmentId);
        List<PunchDayLog> pdls = punchProvider.listPunchDayLogsByApprovalAttributeAndDeptIds(organizationId, deptIds,
                new java.sql.Date(queryDate.getTime()), itemType, pageOffset, pageSize);
        if(pdls != null && pdls.size() > 0){
            for(PunchDayLog pdl : pdls){
                PunchMemberDTO dto = convertPDLToPunchMemberDTO(pdl);
                dto.setStatisticsCount(getPunchStatisticCountByItemType(pdl, itemType));
                results.add(dto);
            }
        }
        return results;
    }

    private List<PunchMemberDTO> listTodayExceptionPunchMemberDTOs(Long organizationId, Long departmentId, Date queryDate, GeneralApprovalAttribute approvalAttribute, Integer pageOffset, int pageSize) {
        List<PunchMemberDTO> results = new ArrayList<>();
        List<OrganizationMemberDetails> records = punchProvider.listExceptionMembersByDate(organizationId, departmentId, new java.sql.Date(queryDate.getTime()), new java.sql.Date(queryDate.getTime()), approvalAttribute, pageOffset, pageSize);

        if(records != null && records.size() > 0){
            for(OrganizationMemberDetails detail : records){
                PunchMemberDTO dto = processMemberDetailToMemberDTO(detail);
                results.add(dto);
            }
        }
        return results;
    }

    private List<PunchMemberDTO> listMonthExceptionPunchMemberDTOs(Long organizationId, Long departmentId, String queryByMonth,
                                PunchExceptionRequestStatisticsItemType itemType, Integer pageOffset, int pageSize) {
        List<PunchMemberDTO> results = new ArrayList<>();
        List<Long> deptIds = findSubDepartmentIds(departmentId);
        List<PunchStatistic> punchStatistics = punchProvider.listPunchSatisticsByExceptionItemTypeAndDeptIds(organizationId, deptIds, queryByMonth, itemType, pageOffset, pageSize);
        if(punchStatistics != null && punchStatistics.size() > 0){
            for(PunchStatistic statistic : punchStatistics){
                PunchMemberDTO dto = convertPSToPunchMemberDTO(statistic);
                dto.setStatisticsCount(getPunchStatisticCountByItemType(statistic, itemType));
                results.add(dto);
            }
        }
        return results;
    }

    private Integer getPunchStatisticCountByItemType(PunchStatistic statistic, PunchExceptionRequestStatisticsItemType itemType) {
        Integer result = 0;
        switch(itemType){
            case ASK_FOR_LEAVE:
                result = statistic.getAskForLeaveRequestCount();
                break;
            case GO_OUT:
                result = statistic.getGoOutRequestCount();
                break;
            case BUSINESS_TRIP:
                result = statistic.getBusinessTripRequestCount();
                break;
            case OVERTIME:
                result = statistic.getOvertimeRequestCount();
                break;
            case PUNCH_EXCEPTION:
                result = statistic.getPunchExceptionRequestCount();
                break;
            default:
                break;
        }
        return result;
    }


    private GeneralApprovalAttribute convertRequestItemTypeToApprovalAttribute(Byte punchExceptionRequestStatisticsItemType) {
        GeneralApprovalAttribute approvalAttribute = null;
        switch(PunchExceptionRequestStatisticsItemType.fromCode(punchExceptionRequestStatisticsItemType)){
            case PUNCH_EXCEPTION:
                approvalAttribute = GeneralApprovalAttribute.ABNORMAL_PUNCH;
                break;
            case ASK_FOR_LEAVE:
                approvalAttribute = GeneralApprovalAttribute.ASK_FOR_LEAVE;
                break;
            case GO_OUT:
                approvalAttribute = GeneralApprovalAttribute.GO_OUT;
                break;
            case BUSINESS_TRIP:
                approvalAttribute = GeneralApprovalAttribute.BUSINESS_TRIP;
                break;
            case OVERTIME:
                approvalAttribute = GeneralApprovalAttribute.OVERTIME;
                break;
            default:
                break;
        }
        return approvalAttribute;
    }

    @Override
    public PunchMonthlyStatisticsByMemberResponse monthlyStatisticsByMember(PunchMonthlyStatisticsByMemberCommand cmd) {

        checkOrganization(cmd.getOrganizationId());
        cmd.setOrganizationId(getTopEnterpriseId(cmd.getOrganizationId()));
        if (cmd.getUserId() == null) {
            cmd.setUserId(UserContext.currentUserId());
        }
        if (!org.springframework.util.StringUtils.hasText(cmd.getStatisticsMonth())) {
            cmd.setStatisticsMonth(monthSF.get().format(new Date()));
        }
        PunchMonthReport report = punchMonthReportProvider.findPunchMonthReportByOwnerMonth(cmd.getOrganizationId(), cmd.getStatisticsMonth());
        if (report == null) {
            throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
                    PunchServiceErrorCode.ERROR_MONTH_STATISTICS_WAITTING_GENERATE, "no data");
        }
        OrganizationMemberDetails organizationMemberDetail = organizationProvider.findOrganizationMemberDetailsByTargetIdAndOrgId(cmd.getUserId(), cmd.getOrganizationId());
        MonthlyStatisticsByMemberRecordMapper record = punchProvider.monthlyStatisticsByMember(cmd.getOrganizationId(), cmd.getStatisticsMonth(), organizationMemberDetail.getId());
        PunchMonthlyStatisticsByMemberResponse response = new PunchMonthlyStatisticsByMemberResponse();
        response.setOrganizationId(cmd.getOrganizationId());
        response.setStatisticsMonth(cmd.getStatisticsMonth());
        response.setUserId(organizationMemberDetail.getTargetId());
        response.setDetailId(organizationMemberDetail.getId());
        response.setWorkDayCount(0);
        response.setRestDayCount(0);
        response.setLastUpdateTime(report.getUpdateTime() != null ? report.getUpdateTime().getTime() : report.getCreateTime().getTime());

        if (record == null) {
            return response;
        }

        response.setWorkDayCount(record.getWorkCount() != null ? record.getWorkCount() : 0);
        response.setRestDayCount(record.getRestDayCount() != null ? record.getRestDayCount() : 0);
        response.setPunchStatusStatisticsList(record.parseToPunchStatusStatisticsItems(localeStringService, UserContext.current().getUser().getLocale()));
        response.setExceptionRequestStatisticsList(record.parseToPunchExceptionRequestStatisticsItems(localeStringService, UserContext.current().getUser().getLocale()));
        return response;
    }

    @Override
    public PunchMonthlyStatisticsByDepartmentResponse monthlyStatisticsByDepartment(PunchMonthlyStatisticsByDepartmentCommand cmd) {
        Organization department = checkOrganization(cmd.getDepartmentId());
        cmd.setOrganizationId(getTopEnterpriseId(cmd.getOrganizationId()));
        if (!org.springframework.util.StringUtils.hasText(cmd.getStatisticsMonth())) {
            cmd.setStatisticsMonth(monthSF.get().format(new Date()));
        }
        PunchMonthReport report = punchMonthReportProvider.findPunchMonthReportByOwnerMonth(cmd.getOrganizationId(), cmd.getStatisticsMonth());
        if (report == null) {
            throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
                    PunchServiceErrorCode.ERROR_MONTH_STATISTICS_WAITTING_GENERATE, "no data");
        }
        List<Long> deptIds = listChildDepartmentsIncludeSelf(department);
        MonthlyStatisticsByDepartmentRecordMapper record = punchProvider.monthlyStatisticsByDepartment(cmd.getOrganizationId(), cmd.getStatisticsMonth(), deptIds);

        PunchMonthlyStatisticsByDepartmentResponse response = new PunchMonthlyStatisticsByDepartmentResponse();
        response.setStatisticsMonth(cmd.getStatisticsMonth());
        response.setDepartmentId(cmd.getDepartmentId());
        response.setLastUpdateTime(report.getUpdateTime() != null ? report.getUpdateTime().getTime() : report.getCreateTime().getTime());
        if (record == null) {
            return response;
        }
        response.setPunchStatusStatisticsList(record.parseToPunchStatusStatisticsItems(localeStringService, UserContext.current().getUser().getLocale()));
        response.setExceptionRequestStatisticsList(record.parseToPunchExceptionRequestStatisticsItems(localeStringService, UserContext.current().getUser().getLocale()));
        return response;
    }

    @Override
    public PunchDailyStatisticsByDepartmentResponse dailyStatisticsByDepartment(PunchDailyStatisticsByDepartmentCommand cmd) {
        Organization department = checkOrganization(cmd.getDepartmentId());
        cmd.setOrganizationId(getTopEnterpriseId(cmd.getOrganizationId()));
        if (cmd.getStatisticsDate() == null) {
            cmd.setStatisticsDate(System.currentTimeMillis());
        }
        List<Long> deptIds = listChildDepartmentsIncludeSelf(department);

        String locale = UserContext.current().getUser().getLocale();
        PunchDailyStatisticsByDepartmentResponse response = new PunchDailyStatisticsByDepartmentResponse();
        response.setStatisticsDate(cmd.getStatisticsDate());
        response.setDepartmentId(cmd.getDepartmentId());
        response.setRateOfAttendance(0);
        response.setRestMemberCount(new PunchStatusStatisticsItemDTO(
                localeStringService.getLocalizedString(
                        PunchStatisticsParser.PUNCH_STATUS_STATISTICS_ITEM_NAME_SCOPE, String.valueOf(PunchStatusStatisticsItemType.REST.getCode()), locale, PunchStatusStatisticsItemType.REST.toString()),
                PunchStatusStatisticsItemType.REST.getCode(), 0, PunchStatusStatisticsItemType.REST.getUnit())
        );
        response.setShouldArrivedMemberCount(new PunchStatusStatisticsItemDTO(
                localeStringService.getLocalizedString(
                        PunchStatisticsParser.PUNCH_STATUS_STATISTICS_ITEM_NAME_SCOPE, String.valueOf(PunchStatusStatisticsItemType.SHOULD_ARRIVE.getCode()), locale, PunchStatusStatisticsItemType.SHOULD_ARRIVE.toString()),
                PunchStatusStatisticsItemType.SHOULD_ARRIVE.getCode(), 0, PunchStatusStatisticsItemType.SHOULD_ARRIVE.getUnit())
        );
        response.setArrivedMemberCount(new PunchStatusStatisticsItemDTO(
                localeStringService.getLocalizedString(
                        PunchStatisticsParser.PUNCH_STATUS_STATISTICS_ITEM_NAME_SCOPE, String.valueOf(PunchStatusStatisticsItemType.ARRIVED.getCode()), locale, PunchStatusStatisticsItemType.ARRIVED.toString()),
                PunchStatusStatisticsItemType.ARRIVED.getCode(), 0, PunchStatusStatisticsItemType.ARRIVED.getUnit())
        );

        boolean isToday = dateSF.get().format(new Date()).equals(dateSF.get().format(new Date(cmd.getStatisticsDate())));
        DailyStatisticsByDepartmentBaseRecordMapper record = punchProvider.dailyStatisticsByDepartment(cmd.getOrganizationId(), new java.sql.Date(cmd.getStatisticsDate()), deptIds, isToday);
        if (record == null) {
            return response;
        }
        response.setRateOfAttendance(record.getRateOfAttendance() == null ? 0 : record.getRateOfAttendance());
        response.getRestMemberCount().setNum(record.getRestMemberCount() == null ? 0 : record.getRestMemberCount());
        response.getShouldArrivedMemberCount().setNum(record.getShouldArrivedMemberCount() == null ? 0 : record.getShouldArrivedMemberCount());
        response.getArrivedMemberCount().setNum(record.getArrivedMemberCount() == null ? 0 : record.getArrivedMemberCount());
        response.setPunchStatusStatisticsList(record.parseToPunchStatusStatisticsItems(localeStringService, locale));
        response.setExceptionRequestStatisticsList(record.parseToPunchExceptionRequestStatisticsItems(localeStringService, locale));
        return response;
    }

    @Override
    public ListPunchStatusItemDetailResponse listItemDetailsOfAPunchStatus(ListPunchStatusItemDetailCommand cmd) {
        ListPunchStatusItemDetailResponse response = new ListPunchStatusItemDetailResponse();
        Long userId = UserContext.currentUserId();
        if (null != cmd.getUserId()) {
            userId = cmd.getUserId();
        }
        PunchStatusStatisticsItemType itemType = PunchStatusStatisticsItemType.fromCode(cmd.getPunchStatusStatisticsItemType());
        java.sql.Date startDay = socialSecurityService.getTheFirstDate(cmd.getStatisticsMonth());
        java.sql.Date endDay = socialSecurityService.getTheLastDate(cmd.getStatisticsMonth());

        List<PunchDayLog> pdls = punchProvider.listPunchDayLogsByItemTypeAndUserId(cmd.getOrganizationId(), userId, startDay, endDay, itemType);
        if(null != pdls) {
            response.setDetails(pdls.stream().map(r -> {
                PunchStatusItemDetailDTO dto = new PunchStatusItemDetailDTO();
                dto.setPunchDate(r.getPunchDate().getTime());
                SimpleDateFormat sf = new SimpleDateFormat("dd日 EEE", Locale.SIMPLIFIED_CHINESE);
                dto.setDescription(sf.format(r.getPunchDate()) + "(" + getPunchStatisticCountByItemType(r, itemType) + itemType.getUnit() + ")");
                return dto;
            }).collect(Collectors.toList()));
        }
        return response;
    }

    @Override
    public ListPunchExceptionRequestItemDetailResponse listItemDetailsOfAPunchExceptionRequest(ListPunchExceptionRequestItemDetailCommand cmd) {
        ListPunchExceptionRequestItemDetailResponse response = new ListPunchExceptionRequestItemDetailResponse();
        Long userId = UserContext.currentUserId();
        if (null != cmd.getUserId()) {
            userId = cmd.getUserId();
        }
        GeneralApprovalAttribute approvalAttribute = convertRequestItemTypeToApprovalAttribute(cmd.getPunchExceptionRequestStatisticsItemType());
        java.sql.Date startDay = socialSecurityService.getTheFirstDate(cmd.getStatisticsMonth());
        java.sql.Date endDay = socialSecurityService.getTheLastDate(cmd.getStatisticsMonth());
        List<PunchExceptionRequest> exceptionRequests = punchProvider.listExceptionRequestsByItemTypeAndDate(userId,
                cmd.getOrganizationId(), startDay, endDay, approvalAttribute);
        if (CollectionUtils.isEmpty(exceptionRequests)) {
            return response;
        }
        response.setDetails(exceptionRequests.stream().map(r->{
            PunchExceptionRequestItemDetailDTO dto = getPunchExceptionRequestItemDetailDTO(approvalAttribute, r);
            return dto;
        }).collect(Collectors.toList()));
        return response;
    }

    private PunchExceptionRequestItemDetailDTO getPunchExceptionRequestItemDetailDTO(GeneralApprovalAttribute approvalAttribute, PunchExceptionRequest r) {
        PunchExceptionRequestItemDetailDTO dto = new PunchExceptionRequestItemDetailDTO();
        dto.setExceptionId(r.getId());
        dto.setFlowCaseId(r.getRequestId());
        dto.setWaitForApproval(com.everhomes.rest.approval.ApprovalStatus.AGREEMENT == com.everhomes.rest.approval.ApprovalStatus.fromCode(r.getStatus()) ? NormalFlag.NO.getCode() : NormalFlag.YES.getCode());
        String dayUnit = localeStringService.getLocalizedString(ApprovalServiceConstants.SCOPE, String.valueOf(ApprovalServiceConstants.TIME_UNIT_OF_DAY), PunchConstants.locale, "天");
        String hourUnit = localeStringService.getLocalizedString(ApprovalServiceConstants.SCOPE, String.valueOf(ApprovalServiceConstants.TIME_UNIT_OF_HOUR), PunchConstants.locale, "小时");
        String minuteUnit = localeStringService.getLocalizedString(ApprovalServiceConstants.SCOPE, String.valueOf(ApprovalServiceConstants.TIME_UNIT_OF_MINUTE), PunchConstants.locale, "分钟");
        String dateUnit = localeStringService.getLocalizedString("time.unit", "date", PunchConstants.locale, "日");
        
        Map<String, String> map = null;
        String result = null;
        switch(approvalAttribute){
            case ASK_FOR_LEAVE:
                ApprovalCategory approvalCategory = approvalCategoryProvider.findApprovalCategoryById(r.getCategoryId());
                if (approvalCategory != null) {
                    dto.setTitle(approvalCategory.getCategoryName());
                }

                map = getExceptionBeginEndTimeMap(r); 
                result = localeTemplateService.getLocaleTemplateString(PunchConstants.EXCEPTION_STATISTIC_SCOPE,
                        PunchConstants.ASK_FOR_LEAVE_TEMPLATE_CONTENT, PunchConstants.locale, map, "");
                dto.setDescription(result);
                break;
            case OVERTIME:
                if (r.getDurationDay() != null && r.getDurationDay().compareTo(BigDecimal.ZERO) > 0) {
                    dto.setTitle(r.getDurationDay().setScale(3, RoundingMode.UNNECESSARY) + dayUnit);
                } else {
                    dto.setTitle(PunchDayParseUtils.parseHourMinuteDisplayStringZeroWithUnit(r.getDurationMinute() * 60 * 1000, hourUnit, minuteUnit));
                }
                map = getExceptionBeginEndTimeMap(r);
                result = localeTemplateService.getLocaleTemplateString(PunchConstants.EXCEPTION_STATISTIC_SCOPE,
                        PunchConstants.GO_OUT_TEMPLATE_CONTENT, RentalNotificationTemplateCode.locale, map, "");
                dto.setDescription(result);
                break;
            case GO_OUT:
            case BUSINESS_TRIP:
                dto.setTitle(r.getDurationDay().setScale(3, RoundingMode.UNNECESSARY) + dayUnit);
                map = getExceptionBeginEndTimeMap(r);
                result = localeTemplateService.getLocaleTemplateString(PunchConstants.EXCEPTION_STATISTIC_SCOPE,
                        PunchConstants.GO_OUT_TEMPLATE_CONTENT, RentalNotificationTemplateCode.locale, map, "");
                dto.setDescription(result);
                break;
            case ABNORMAL_PUNCH:
            	SimpleDateFormat dayDF = new SimpleDateFormat("dd");
                SimpleDateFormat weekDF = new SimpleDateFormat("EEE", Locale.SIMPLIFIED_CHINESE);
                dto.setTitle(dayDF.format(r.getPunchDate()) + dateUnit + weekDF.format(r.getPunchDate()));
            	PunchLog pLog = punchProvider.findPunchLog(r.getEnterpriseId(), r.getUserId(), r.getPunchDate(), r.getPunchType(), r.getPunchIntervalNo());
            	if(null != pLog){
            		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
            		dto.setDescription(localeStringService.getLocalizedString(PunchConstants.PUNCH_PUNCHTYPE_SCOPE, String.valueOf(r.getPunchType()), PunchConstants.locale, "")
            				+ " " + ruleTimeToString(pLog.getRuleTime()));
            	}
                break;
            default:
                break;
        }
        return dto;
    }

	private String ruleTimeToString(Long ruleTime) {
		StringBuilder sb = new StringBuilder();
		long time = ruleTime;
        time = time / 1000;
        int hour = Integer.valueOf((time / 3600) + "");
        if (hour > 24){
        	hour = hour - 24;
        	sb.append(localeStringService.getLocalizedString(PunchConstants.PUNCH_TIME_SCOPE, PunchConstants.NEXT_DAY, PunchConstants.locale, "次日"));
        }
        if (hour < 10) {
            sb.append("0");
        }
        sb.append(hour);
        sb.append(":");
        time = time % 3600;
        int min = Integer.valueOf((time / 60) + ""); 
        if (min < 10) {
            sb.append("0");
        }
        sb.append(min);  
        return sb.toString(); 
	}

	private Map<String, String> getExceptionBeginEndTimeMap(PunchExceptionRequest r) {
		Map<String, String> map = new HashMap<String, String>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy");
		map.put("beginYear", df.format(r.getBeginTime()));
		map.put("endYear", df.format(r.getEndTime()));
		df = new SimpleDateFormat("MM");
		map.put("beginMonth", df.format(r.getBeginTime()));
		map.put("endMonth", df.format(r.getEndTime()));
		df = new SimpleDateFormat("dd");
		map.put("beginDate", df.format(r.getBeginTime()));
		map.put("endDate", df.format(r.getEndTime()));
		df = new SimpleDateFormat("HH:mm");
		map.put("beginTime", df.format(r.getBeginTime()));
		map.put("endTime", df.format(r.getEndTime()));
		return map;
	}


    @Override
    public String processUserPunchRuleInfoUrl(Long ownerId,Long punchDate, HttpServletRequest request){
        String homeUrl = request.getHeader("Host");
        return "http://" + homeUrl + "/mobile/static/oa_punch/punch_rule.html?ownerId=" + ownerId + "&punchDate=" + punchDate +"#sign_suffix";
    }
    @Override
    public GetUserPunchRuleInfoUrlResponse getUserPunchRuleInfoUrl(GetUserPunchRuleInfoUrlCommand cmd, HttpServletRequest request){
        PunchRule pr = getPunchRule(PunchOwnerType.ORGANIZATION.getCode(), cmd.getOwnerId(), UserContext.currentUserId());
        if(null == pr){
        	throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE,
					PunchServiceErrorCode.ERROR_ENTERPRISE_DIDNOT_SETTING,
					"have no punch rule");
        }
    	return new GetUserPunchRuleInfoUrlResponse(processUserPunchRuleInfoUrl(cmd.getOwnerId(),cmd.getPunchDate(), request));
    }
    
    @Override
    public GetUserPunchRuleInfoResponse getUserPunchRuleInfo(GetUserPunchRuleInfoCommand cmd){
 
        PunchRule pr = getPunchRule(PunchOwnerType.ORGANIZATION.getCode(), cmd.getOwnerId(), UserContext.currentUserId());

        GetUserPunchRuleInfoResponse response =  ConvertHelper.convert(pr, GetUserPunchRuleInfoResponse.class);
        response.setGroupName(pr.getName());
        //打卡时间
        response.setTimeRules(processPunchTimeRuleDTOs(pr.getPunchOrganizationId(), PunchRuleStatus.ACTIVE.getCode()));
        //打卡地点和WiFi
        response.setPunchGeoPoints(processPunchGeoPointDTOs(pr.getPunchOrganizationId()));
        response.setWifis(processPunchWiFiDTOs(pr.getPunchOrganizationId()));
        //加班时长
        response.setPunchOvertimeRules(processPunchOvertimeRuleDTOs(pr.getId(), PunchRuleStatus.ACTIVE.getCode()));
        PunchTimeRule ptr = getPunchTimeRuleWithPunchDayTypeByRuleIdAndDate(pr, new Date(cmd.getPunchDate()), UserContext.currentUserId());
        if (null != ptr) {
            if(ptr.getId().equals(0L)){
                response.setCurrentTimeRuleName("休息");
            }else {
                response.setCurrentTimeRuleName(ptr.getName());
            }
        }
		return response;
    	
    }
    private void checkUserStatisticPrivilege(Long orgId){
        CheckUserStatisticPrivilegeResponse resp = checkUserStatisticPrivilege(new CheckUserStatisticPrivilegeCommand(orgId));
        if (NormalFlag.fromCode(resp.getQueryPrivilege()) != NormalFlag.YES) {
            throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_CHECK_APP_PRIVILEGE,
                    "check app privilege error");
        }
    }
    @Override
    public CheckUserStatisticPrivilegeResponse checkUserStatisticPrivilege(CheckUserStatisticPrivilegeCommand cmd) {

        CheckUserStatisticPrivilegeResponse response = new CheckUserStatisticPrivilegeResponse();
        response.setAdminFlag(NormalFlag.NO.getCode());
        response.setQueryPrivilege(NormalFlag.NO.getCode());
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        Organization org = checkOrganization(cmd.getOrgId());
        try {
            if (resolver.checkSuperAdmin(UserContext.currentUserId(), cmd.getOrgId())
                    || resolver.checkOrganizationAdmin(UserContext.currentUserId(), cmd.getOrgId())
                    || resolver.checkModuleAppAdmin(UserContext.getCurrentNamespaceId(), cmd.getOrgId(), UserContext.currentUserId(), cmd.getAppId())) {
            	
            	response.setDeptName(org.getName());
                response.setAdminFlag(NormalFlag.YES.getCode());
                response.setQueryPrivilege(NormalFlag.YES.getCode());
                response.setHasSubDpts(checkHasSubDpt(org.getId()));
                
                return response;
            }

        } catch (Exception e) {
            LOGGER.error("there is a error when check org admin ", e);
        }
        OrganizationMember managerMember = organizationProvider.findMemberByType(UserContext.currentUserId(), org.getPath(), OrganizationGroupType.MANAGER.getCode());
        if (null != managerMember) {
            response.setQueryPrivilege(NormalFlag.YES.getCode());
            //部门管理员组要用parentId
            Organization manager = checkOrganization(managerMember.getOrganizationId());
            Organization dpt = checkOrganization(manager.getParentId());
            response.setDeptId(dpt.getId());
            response.setDeptName(dpt.getName());
            response.setHasSubDpts(checkHasSubDpt(dpt.getId()));
        }
        
        return response;
    }

    private Byte checkHasSubDpt(Long orgId) {
    	byte result = NormalFlag.YES.getCode();
    	List<Long> subDptIds = findSubDepartmentIds(orgId); 
    	//subDptIds会包含本身
        if(CollectionUtils.isEmpty(subDptIds) || subDptIds.size() <= 1){
        	result =  NormalFlag.NO.getCode();
        }
		return result;
	}

	private List<Long> listChildDepartmentsIncludeSelf(Organization parentDepartment) {
        List<Organization> children = findSubDepartments(parentDepartment);
        List<Long> deptIds = new ArrayList<>();
        deptIds.add(parentDepartment.getId());
        if (CollectionUtils.isEmpty(children)) {
            return deptIds;
        }
        children.forEach(d -> {
            deptIds.add(d.getId());
        });
        return deptIds;
    }

    @Override
    public void punchDayLogInitializeByMonth(String initMonth) throws ParseException {
        // 通过UniongroupMemberDetails来查询有在使用考勤功能的公司 ( 即没有使用考勤功能的不进行初始化 )
        List<Long> organizationIds = uniongroupConfigureProvider.distinctEnterpriseIdsFromUniongroupMemberDetails();
        if (CollectionUtils.isEmpty(organizationIds)) {
            return;
        }
        if (StringUtils.isBlank(initMonth)) {
            initMonth = monthSF.get().format(new Date());
        }
        final String finalInitMonth = initMonth;
        this.coordinationProvider.getNamedLock(CoordinationLocks.PUNCH_DAY_LOG_INIT_OPERATION.getCode()).enter(() -> {
            for (Long organizationId : organizationIds) {
                List<OrganizationMemberDetails> memberDetails = listMembers(organizationId, null, finalInitMonth, Integer.MAX_VALUE - 1, null, null);
                if (CollectionUtils.isEmpty(memberDetails)) {
                    continue;
                }
                for (OrganizationMemberDetails detail : memberDetails) {
                    Calendar monthBegin = Calendar.getInstance();
                    Calendar monthEnd = Calendar.getInstance();
                    monthBegin.setTime(monthSF.get().parse(finalInitMonth));
                    monthBegin.set(Calendar.DAY_OF_MONTH, 1);

                    if (!monthSF.get().format(monthEnd.getTime()).equals(finalInitMonth)) {
                        monthEnd.setTime(monthSF.get().parse(finalInitMonth));
                        monthEnd.set(Calendar.DAY_OF_MONTH, monthEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
                    }

                    while (true) {
                        Calendar punCalendar = Calendar.getInstance();
                        punCalendar.setTimeInMillis(monthBegin.getTimeInMillis());
                        try {
                            if (detail.getCheckInTime() != null && detail.getCheckInTime().getTime() <= punCalendar.getTimeInMillis()) {
                                punchDayLogRefresh(detail, organizationId, punCalendar);
                            }
                        } catch (Exception e) {
                            LOGGER.error("punchDayLogInitializeByMonth error,detailId = {},punchDate = {}", detail.getId(), punCalendar.getTime(), e);
                        }
                        monthBegin.add(Calendar.DAY_OF_MONTH, 1);
                        if (monthBegin.after(monthEnd)) {
                            break;
                        }
                    }
                }
            }
            refreshMonthReport(finalInitMonth);
            return null;
        });
    }

	@Override
	public PunchClockResponse thirdPartPunchClock(ThirdPartPunchClockCommand cmd) { 
		checkCompanyIdIsNull(cmd.getEnterpriseId());
        cmd.setEnterpriseId(getTopEnterpriseId(cmd.getEnterpriseId()));
		String punchTime = datetimeSF.get().format(new Date());
		PunchLog punchLog = ConvertHelper.convert(cmd, PunchLog.class);
		if(punchLog.getCreateType() == null){
			punchLog.setCreateType(CreateType.OTHER_THRID_PUNCH.getCode());
		}
		punchLog.setPunchType(PunchType.ON_DUTY.getCode());
		return createPunchLog(punchTime, ClockCode.SUCESS.getCode(), punchLog);
	}

    @Override
    public GoOutPunchLogDTO goOutPunchClock(GoOutPunchClockCommand cmd) {
        checkCompanyIdIsNull(cmd.getOrganizationId());
        cmd.setOrganizationId(getTopEnterpriseId(cmd.getOrganizationId()));
        PunchGoOutLog log = ConvertHelper.convert(cmd, PunchGoOutLog.class);
        log.setUserId(UserContext.currentUserId());
        log.setPunchTime(new java.sql.Timestamp(DateHelper.currentGMTTime().getTime()));
        log.setPunchDate(new java.sql.Date(DateHelper.currentGMTTime().getTime()));
        log.setStatus(PunchStatus.NORMAL.getCode());
        log.setNamespaceId(UserContext.getCurrentNamespaceId());
        OrganizationMemberDetails memberDetail = organizationProvider.findOrganizationMemberDetailsByTargetIdAndOrgId(log.getUserId(), log.getOrganizationId());
        if (null != memberDetail) {
            log.setDetailId(memberDetail.getId());
        }
        punchProvider.createPUnchGoOutLog(log);
        try {
            if (null != memberDetail) {
                PunchDayLog newPdl = new PunchDayLog();
                Calendar punCalendar = Calendar.getInstance();
                punCalendar.setTime(log.getPunchDate());
                PunchDayLog punchDayLog = punchProvider.getDayPunchLogByDateAndUserId(log.getUserId(),
                        log.getOrganizationId(), dateSF.get().format(punCalendar.getTime()));
                refreshPunchDayLog(memberDetail, punchDayLog, punCalendar, newPdl);
            }
        } catch (Exception e) {
        }

        return convertGoOutLogDTO(log);
    }

    private GoOutPunchLogDTO convertGoOutLogDTO(PunchGoOutLog log) {
        GoOutPunchLogDTO dto = ConvertHelper.convert(log, GoOutPunchLogDTO.class);
        dto.setPunchDate(log.getPunchDate() == null ? null : log.getPunchDate().getTime());
        dto.setPunchTime(log.getPunchTime() == null ? null : log.getPunchTime().getTime());
        dto.setImgUrl(contentServerService.parserUri(log.getImgUri()));
        return dto;
    }

    @Override
    public GoOutPunchLogDTO updateGoOutPunchLog(UpdateGoOutPunchLogCommand cmd) {
        PunchGoOutLog log = punchProvider.findPunchGoOutLogById(cmd.getId());
        if (null == log) {
            return null;
        }
        if (null == log.getDescription()) {
            log.setDescription(cmd.getDescription());
            punchProvider.updatePunchGoOutLog(log);
        }
        return convertGoOutLogDTO(log);
    }

	@Override
	public GoOutPunchLogDTO getGoOutPunchLog(GetGoOutPunchLogCommand cmd) {
		PunchGoOutLog log = punchProvider.findPunchGoOutLogById(cmd.getId());
		if (null == log) {
			return null;
		} 
		return convertGoOutLogDTO(log);
	}

}