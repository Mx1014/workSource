// @formatter:off
package com.everhomes.enterprisepaymentauth;

import com.everhomes.archives.ArchivesService;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.filedownload.TaskService;
import com.everhomes.gorder.sdk.order.GeneralOrderService;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.paySDK.PaySettings;
import com.everhomes.remind.RemindContants;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.common.OfficialActionData;
import com.everhomes.rest.common.Router;
import com.everhomes.rest.enterprisepaymentauth.BatchCreateOrUpdateEmployeePaymentLimitCommand;
import com.everhomes.rest.enterprisepaymentauth.CreateOrUpdateEnterprisePaymentSceneLimitCommand;
import com.everhomes.rest.enterprisepaymentauth.EmployeePaymentAuthDTO;
import com.everhomes.rest.enterprisepaymentauth.EmployeePaymentLimitChangeLogItemDTO;
import com.everhomes.rest.enterprisepaymentauth.EmployeePaymentLimitChangeLogsGroupDTO;
import com.everhomes.rest.enterprisepaymentauth.EmployeePaymentLogDTO;
import com.everhomes.rest.enterprisepaymentauth.EmployeePaymentSceneLimitDTO;
import com.everhomes.rest.enterprisepaymentauth.EmployeePaymentSceneLimitSimpleDTO;
import com.everhomes.rest.enterprisepaymentauth.ExportEnterprisePaymentPayLogsCommand;
import com.everhomes.rest.enterprisepaymentauth.FrozenStatus;
import com.everhomes.rest.enterprisepaymentauth.GetEmployeePaymentAuthDetailCommand;
import com.everhomes.rest.enterprisepaymentauth.GetEmployeePaymentAuthDetailResponse;
import com.everhomes.rest.enterprisepaymentauth.GetEmployeePaymentAuthStatusCommand;
import com.everhomes.rest.enterprisepaymentauth.GetEmployeePaymentAuthStatusResponse;
import com.everhomes.rest.enterprisepaymentauth.GetEnterpriseEmployeePaymentLimitDetailCommand;
import com.everhomes.rest.enterprisepaymentauth.GetEnterpriseEmployeePaymentLimitDetailResponse;
import com.everhomes.rest.enterprisepaymentauth.GetEnterprisePaymentAuthInfoCommand;
import com.everhomes.rest.enterprisepaymentauth.GetEnterprisePaymentAuthInfoResponse;
import com.everhomes.rest.enterprisepaymentauth.GetEnterprisePaymentSceneLimitInfoCommand;
import com.everhomes.rest.enterprisepaymentauth.GetEnterprisePaymentSceneLimitInfoResponse;
import com.everhomes.rest.enterprisepaymentauth.ListEmployeePaymentLimitChangeLogsCommand;
import com.everhomes.rest.enterprisepaymentauth.ListEmployeePaymentLimitChangeLogsResponse;
import com.everhomes.rest.enterprisepaymentauth.ListEmployeePaymentLogsCommand;
import com.everhomes.rest.enterprisepaymentauth.ListEmployeePaymentLogsResponse;
import com.everhomes.rest.enterprisepaymentauth.ListEnterpriseEmployeePaymentLimitChangeLogsCommand;
import com.everhomes.rest.enterprisepaymentauth.ListEnterpriseEmployeePaymentLimitChangeLogsResponse;
import com.everhomes.rest.enterprisepaymentauth.ListEnterprisePaymentAuthOfEmployeesCommand;
import com.everhomes.rest.enterprisepaymentauth.ListEnterprisePaymentAuthOfEmployeesResponse;
import com.everhomes.rest.enterprisepaymentauth.ListEnterprisePaymentAuthOperateLogsCommand;
import com.everhomes.rest.enterprisepaymentauth.ListEnterprisePaymentAuthOperateLogsResponse;
import com.everhomes.rest.enterprisepaymentauth.ListEnterprisePaymentPayLogsCommand;
import com.everhomes.rest.enterprisepaymentauth.ListEnterprisePaymentPayLogsResponse;
import com.everhomes.rest.enterprisepaymentauth.ListEnterprisePaymentSceneEmployeeLimitCommand;
import com.everhomes.rest.enterprisepaymentauth.ListEnterprisePaymentSceneEmployeeLimitResponse;
import com.everhomes.rest.enterprisepaymentauth.ListEnterprisePaymentScenesCommand;
import com.everhomes.rest.enterprisepaymentauth.ListEnterprisePaymentScenesResponse;
import com.everhomes.rest.enterprisepaymentauth.ListPaymentScenesResponse;
import com.everhomes.rest.enterprisepaymentauth.MessageUrlType;
import com.everhomes.rest.enterprisepaymentauth.NormalFlag;
import com.everhomes.rest.enterprisepaymentauth.OperateSourceType;
import com.everhomes.rest.enterprisepaymentauth.OperateType;
import com.everhomes.rest.enterprisepaymentauth.PaymentAuthCheckAndFrozenCommand;
import com.everhomes.rest.enterprisepaymentauth.PaymentAuthCheckAndFrozenResponse;
import com.everhomes.rest.enterprisepaymentauth.PaymentAuthFrozenConfirmCommand;
import com.everhomes.rest.enterprisepaymentauth.PaymentAuthOperateLogDTO;
import com.everhomes.rest.enterprisepaymentauth.PaymentAuthSceneDTO;
import com.everhomes.rest.enterprisepaymentauth.PaymentAuthUnFrozenCommand;
import com.everhomes.rest.enterprisepaymentauth.PaymentPayLogDTO;
import com.everhomes.rest.enterprisepaymentauth.SceneSimpleDTO;
import com.everhomes.rest.enterprisepaymentauth.TestPaymentCallbackCommand;
import com.everhomes.rest.filedownload.TaskRepeatFlag;
import com.everhomes.rest.filedownload.TaskType;
import com.everhomes.rest.messaging.ChannelType;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessageMetaConstant;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.messaging.MetaObjectType;
import com.everhomes.rest.messaging.RouterMetaObject;
import com.everhomes.rest.organization.EmployeeStatus;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.rest.promotion.order.GetPurchaseOrderByIdCommand;
import com.everhomes.rest.promotion.order.PurchaseOrderDTO;
import com.everhomes.rest.promotion.order.PurchaseOrderStatus;
import com.everhomes.rest.servicemoduleapp.ListServiceModuleAppsForEnterprisePayCommand;
import com.everhomes.rest.servicemoduleapp.ListServiceModuleAppsForEnterprisePayResponse;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.DateStatisticHelper;
import com.everhomes.util.ExecutorUtil;
import com.everhomes.util.PaginationHelper;
import com.everhomes.util.RouterBuilder;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SignatureHelper;
import com.everhomes.util.StringHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jooq.Condition;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
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
import java.util.stream.Collectors;

@Component
public class EnterprisePaymentAuthServiceImpl implements EnterprisePaymentAuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnterprisePaymentAuthServiceImpl.class);
    private static final int FETCH_SIZE = 2;

    @Autowired
    private TaskService taskService;
    @Autowired
    private LocaleTemplateService localeTemplateService;
    @Autowired
    private LocaleStringService localeStringService;
    @Autowired
    private EnterprisePaymentAuthEmployeePaySceneHistoryProvider enterprisePaymentAuthEmployeePaySceneHistoryProvider;
    @Autowired
    private EnterprisePaymentAuthPayLogProvider enterprisePaymentAuthPayLogProvider;
    @Autowired
    private EnterprisePaymentAuthScenePayHistoryProvider enterprisePaymentAuthScenePayHistoryProvider;
    @Autowired
    private EnterprisePaymentAuthEmployeePayHistoryProvider enterprisePaymentAuthEmployeePayHistoryProvider;
    @Autowired
    private EnterprisePaymentAuthSceneLimitProvider enterprisePaymentAuthSceneLimitProvider;
    @Autowired
    private EnterprisePaymentAuthOperateLogProvider enterprisePaymentAuthOperateLogProvider;
    @Autowired
    private EnterprisePaymentAuthEmployeeLimitProvider enterprisePaymentAuthEmployeeLimitProvider;
    @Autowired
    private EnterprisePaymentAuthEmployeeLimitDetailProvider enterprisePaymentAuthEmployeeLimitDetailProvider;
    @Autowired
    private EnterprisePaymentAuthEmployeeLimitChangeLogProvider enterprisePaymentAuthEmployeeLimitChangeLogProvider;
    @Autowired
    private EnterprisePaymentAuthFrozenRequestProvider enterprisePaymentAuthFrozenRequestProvider;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private ArchivesService archivesService;
    @Autowired
    private ServiceModuleAppService serviceModuleAppService;
    @Autowired
    private ConfigurationProvider configurationProvider;
    @Autowired
    private OrganizationProvider organizationProvider;
    @Autowired
    private ScheduleProvider scheduleProvider;
    @Autowired
    private CoordinationProvider coordinationProvider;
    @Autowired
    private DbProvider dbProvider;
    @Autowired
    private ContentServerService contentServerService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessagingService messagingService;
    @Autowired
    private GeneralOrderService orderService;

    private static final DateTimeFormatter MONTH_DF = DateTimeFormatter.ofPattern("yyyyMM");
    private ThreadLocal<SimpleDateFormat> monthSF = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMM");
        }
    };
    private ThreadLocal<SimpleDateFormat> DATE_TIME_SDF = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
    private static final BigDecimal BIG_DECIMAL_100 = new BigDecimal("100");


    @Override
    public GetEnterprisePaymentAuthInfoResponse getEnterprisePaymentAuthInfo(GetEnterprisePaymentAuthInfoCommand cmd) {
        GetEnterprisePaymentAuthInfoResponse response = enterprisePaymentAuthEmployeeLimitProvider.countEnterpriseAuthInfo(cmd.getOrganizationId());
        return response;
    }

    @Override
    public ListEnterprisePaymentAuthOfEmployeesResponse listEnterprisePaymentAuthOfEmployees(ListEnterprisePaymentAuthOfEmployeesCommand cmd) {
        ListEnterprisePaymentAuthOfEmployeesResponse response = new ListEnterprisePaymentAuthOfEmployeesResponse();
        Map<Long, String> sceneAppMap = getEnterprisePaymentSceneIdNameMap();
        int pageSize = cmd.getPageSize() == null ? 20 : cmd.getPageSize();
        int pageOffset = (cmd.getPageOffset() != null && cmd.getPageOffset() > 1) ? cmd.getPageOffset() : 1;
        int offset = (int) PaginationHelper.offsetFromPageOffset((long) pageOffset, pageSize);
        Condition condition = null;
        if(cmd.getPaymentSceneAppId() != null){
        	condition = DSL.exists(DSL.selectOne().from(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS)
        			.where(Tables.EH_ORGANIZATION_MEMBER_DETAILS.ID.eq(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.DETAIL_ID))
        			.and(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.PAYMENT_SCENE_APP_ID.eq(cmd.getPaymentSceneAppId()))
                	.and(Tables.EH_ENTERPRISE_PAYMENT_AUTH_EMPLOYEE_LIMIT_DETAILS.IS_DELETE.eq((byte) 0)));
        }
        List<OrganizationMemberDetails> members = organizationService.listMembers(cmd.getOrganizationId(), cmd.getDepartmentId(), pageSize + 1, offset, cmd.getKeyWords(), condition);
        if (members == null || members.size() == 0) {
            return response;
        }
        if (members.size() == pageSize + 1) {
            members.remove(members.size() - 1);
            response.setNextPageOffset(pageOffset + 1);
        }
        List<EnterprisePaymentAuthEmployeeLimit> employeeLimits = enterprisePaymentAuthEmployeeLimitProvider.listEnterprisePaymentAuthEmployeeLimit(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId());
        List<EnterprisePaymentAuthEmployeePayHistory> employeeHistories = enterprisePaymentAuthEmployeePayHistoryProvider
                .listEnterprisePaymentAuthEmployeePayHistory(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId(), MONTH_DF.format(LocalDateTime.now()));

        response.setEmployeeAuths(members.stream().map(r -> convertMember2EmployeePaymentAuthDTO(r, employeeLimits, employeeHistories, sceneAppMap)).collect(Collectors.toList()));
        return response;
    }

    private EmployeePaymentAuthDTO convertMember2EmployeePaymentAuthDTO(OrganizationMemberDetails detail,
                                                                        List<EnterprisePaymentAuthEmployeeLimit> employeeLimits, List<EnterprisePaymentAuthEmployeePayHistory> employeeHistories, Map<Long, String> sceneAppMap) {
        EnterprisePaymentAuthEmployeeLimit limit = findEmployeeLimit(employeeLimits, detail.getId());
        EnterprisePaymentAuthEmployeePayHistory history = findEmployeeHistory(employeeHistories, detail.getId());
        return convertMember2EmployeePaymentAuthDTO(detail, limit, history, sceneAppMap);
    }

    private EmployeePaymentAuthDTO convertMember2EmployeePaymentAuthDTO(OrganizationMemberDetails detail, EnterprisePaymentAuthEmployeeLimit limit, EnterprisePaymentAuthEmployeePayHistory history, Map<Long, String> sceneAppMap) {
        EmployeePaymentAuthDTO dto = convertMember2EmployeePaymentAuthDTO(detail);
        if (limit != null) {
            dto.setSceneString(processSceneListString(limit.getPaymentSceneList(), sceneAppMap));
        }
        dto.setLimitAmount(limit != null ? limit.getLimitAmount() : BigDecimal.ZERO);
        dto.setUsedAmount(history != null ? history.getUsedAmount() : BigDecimal.ZERO);
        dto.setCurrentMonthRemainAmount(dto.getLimitAmount().subtract(dto.getUsedAmount()).setScale(2, BigDecimal.ROUND_HALF_UP));
        if (dto.getCurrentMonthRemainAmount().compareTo(BigDecimal.ZERO) < 0) {
            dto.setCurrentMonthRemainAmount(BigDecimal.ZERO);
        }
        return dto;
    }

    private String processSceneListString(String paymentSceneList, Map<Long, String> sceneAppMap) {
        if(paymentSceneList == null){
            return null;
        }
        String[] sceneAppIds = paymentSceneList.split(",");
        String result = "";
        for(int i = 0 ; i<sceneAppIds.length ; i++) {
            Long appId = null;
            try{
                appId = Long.valueOf(sceneAppIds[i]);
            }catch (Exception e){
                LOGGER.error("appId can not parse to Long type id = " + sceneAppIds[i]);
            }
            String appName = sceneAppMap.get(appId);
            if(appName != null) {
                if (!result.equals("")) {
                    result = result + "、";
                }
                result = result + appName;
            }
        }
        return result;
    }

    private EmployeePaymentAuthDTO convertMember2EmployeePaymentAuthDTO(OrganizationMemberDetails detail) {
        EmployeePaymentAuthDTO dto = new EmployeePaymentAuthDTO();
        dto.setUserId(detail.getTargetId());
        dto.setDetailId(detail.getId());
        dto.setContactName(detail.getContactName());
        dto.setCurrentMonth(MONTH_DF.format(LocalDateTime.now()));

        Map<Long, String> dptMap = archivesService.getEmployeeDepartment(detail.getId());
        if (null != dptMap) {
            dto.setDepartmentId(dptMap.keySet().iterator().next());
            String depName = archivesService.convertToOrgNames(dptMap);
            dto.setDepartmentName(depName);
        }
        return dto;
    }

    private EnterprisePaymentAuthEmployeePayHistory findEmployeeHistory(List<EnterprisePaymentAuthEmployeePayHistory> employeeHistories, Long id) {
        if (CollectionUtils.isEmpty(employeeHistories)) {
            return null;
        }
        for (EnterprisePaymentAuthEmployeePayHistory history : employeeHistories) {
            if (history.getDetailId().equals(id)) {
                return history;
            }
        }
        return null;
    }

    private EnterprisePaymentAuthEmployeeLimit findEmployeeLimit(List<EnterprisePaymentAuthEmployeeLimit> employeeLimits, Long id) {
        if (CollectionUtils.isEmpty(employeeLimits)) {
            return null;
        }
        for (EnterprisePaymentAuthEmployeeLimit limit : employeeLimits) {
            if (limit.getDetailId().equals(id)) {
                return limit;
            }
        }
        return null;
    }

    @Override
    public ListEnterprisePaymentScenesResponse listEnterprisePaymentScenes(ListEnterprisePaymentScenesCommand cmd) {
        ListServiceModuleAppsForEnterprisePayCommand cmd1 = new ListServiceModuleAppsForEnterprisePayCommand();
        cmd1.setEnableEnterprisePayFlag(TrueOrFalseFlag.TRUE.getCode());
        cmd1.setNamespaceId(UserContext.getCurrentNamespaceId());
        Map<Long, String> map = getEnterprisePaymentSceneIdNameMap();
        List<Map.Entry<Long, String>> sortedList = sortMapByValue(map);
        if (CollectionUtils.isEmpty(sortedList)) {
            return new ListEnterprisePaymentScenesResponse(0, 0, null);
        }
        int totalSceneCount = sortedList.size();
        int authSceneCount = 0;
        List<PaymentAuthSceneDTO> sceneAtuhs = new ArrayList<>();

        for (Map.Entry<Long, String> entry : sortedList) {
            sceneAtuhs.add(convertPaymentAuthSceneDTO(entry, cmd.getOrganizationId()));
        }
        for (PaymentAuthSceneDTO dto : sceneAtuhs) {
            if (dto.getAuthEmployeeCount() > 0) {
                authSceneCount++;
            }
        }
        return new ListEnterprisePaymentScenesResponse(totalSceneCount, authSceneCount, sceneAtuhs);
    }

    private PaymentAuthSceneDTO convertPaymentAuthSceneDTO(Map.Entry<Long, String> entry, Long organizationId) {
        PaymentAuthSceneDTO dto = new PaymentAuthSceneDTO();
        dto.setSceneAppId(entry.getKey());
        dto.setSceneAppName(entry.getValue());
        EnterprisePaymentAuthSceneLimit sceneLimit = enterprisePaymentAuthSceneLimitProvider.findEnterprisePaymentAuthSceneLimitByAppId(organizationId, UserContext.getCurrentNamespaceId(), dto.getSceneAppId());
        dto.setLimitAmount(sceneLimit == null ? null : sceneLimit.getLimitAmount());
        EnterprisePaymentAuthScenePayHistory sceneHistory = enterprisePaymentAuthScenePayHistoryProvider.findEnterprisePaymentAuthScenePayHistoryByAppId(organizationId, UserContext.getCurrentNamespaceId(), dto.getSceneAppId(), MONTH_DF.format(LocalDateTime.now()));
        dto.setUsedAmount(sceneHistory == null ? BigDecimal.ZERO : sceneHistory.getUsedAmount());
        dto.setCurrentMonthRemainAmount(dto.getLimitAmount() == null ? null : dto.getLimitAmount().subtract(dto.getUsedAmount()));
        if (dto.getCurrentMonthRemainAmount() != null && dto.getCurrentMonthRemainAmount().compareTo(BigDecimal.ZERO) < 0) {
            dto.setCurrentMonthRemainAmount(BigDecimal.ZERO);
        }
        dto.setAuthEmployeeCount(enterprisePaymentAuthEmployeeLimitDetailProvider.countSceneEmployeeCount(organizationId, UserContext.getCurrentNamespaceId(), dto.getSceneAppId()));
        return dto;
    }


    @Override
    public GetEnterprisePaymentSceneLimitInfoResponse getEnterprisePaymentSceneLimitInfo(GetEnterprisePaymentSceneLimitInfoCommand cmd) {
        Map<Long, String> appMap = getEnterprisePaymentSceneIdNameMap();
        GetEnterprisePaymentSceneLimitInfoResponse response = new GetEnterprisePaymentSceneLimitInfoResponse();
        response.setSceneAppId(cmd.getSceneAppId());
        response.setSceneAppName(appMap.get(cmd.getSceneAppId()));
        EnterprisePaymentAuthSceneLimit sceneLimit = enterprisePaymentAuthSceneLimitProvider.findEnterprisePaymentAuthSceneLimitByAppId(
                cmd.getOrganizationId(), UserContext.getCurrentNamespaceId(), cmd.getSceneAppId());
        response.setLimitAmount(sceneLimit == null ? null : sceneLimit.getLimitAmount());
        response.setHistoricalPayCount(sceneLimit != null ? sceneLimit.getHistoricalPayCount() : null);
        response.setHistoricalTotalPayAmount(sceneLimit != null ? sceneLimit.getHistoricalTotalPayAmount() : null);
        EnterprisePaymentAuthScenePayHistory sceneHistory = enterprisePaymentAuthScenePayHistoryProvider.findEnterprisePaymentAuthScenePayHistoryByAppId(
                cmd.getOrganizationId(), UserContext.getCurrentNamespaceId(), cmd.getSceneAppId(), MONTH_DF.format(LocalDateTime.now()));
        response.setUsedAmount(sceneHistory == null ? BigDecimal.ZERO : sceneHistory.getUsedAmount());
        response.setCurrentMonthRemainAmount(response.getLimitAmount() == null ? null : response.getLimitAmount().subtract(response.getUsedAmount()));
        if (response.getCurrentMonthRemainAmount() != null && response.getCurrentMonthRemainAmount().compareTo(BigDecimal.ZERO) < 0) {
            response.setCurrentMonthRemainAmount(BigDecimal.ZERO);
        }
        response.setTotalEmployeeAuthCount(enterprisePaymentAuthEmployeeLimitDetailProvider.countSceneEmployeeCount(cmd.getOrganizationId(), UserContext.getCurrentNamespaceId(), cmd.getSceneAppId()));

        return response;
    }

    private EmployeePaymentAuthDTO convertMember2EmployeePaymentAuthDTO(OrganizationMemberDetails detail, EnterprisePaymentAuthEmployeeLimitDetail r) {
        EmployeePaymentAuthDTO dto = convertMember2EmployeePaymentAuthDTO(detail);
        dto.setLimitAmount(r.getLimitAmount() == null ? null : r.getLimitAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
        return dto;
    }

    @Override
    public ListEnterprisePaymentSceneEmployeeLimitResponse listPaymentSceneEmployeeLimit(ListEnterprisePaymentSceneEmployeeLimitCommand cmd) {
        ListEnterprisePaymentSceneEmployeeLimitResponse response = new ListEnterprisePaymentSceneEmployeeLimitResponse();
        int pageSize = cmd.getPageSize() == null ? 20 : cmd.getPageSize();
        int pageOffset = (cmd.getPageOffset() != null && cmd.getPageOffset() > 1) ? cmd.getPageOffset() : 1;
        int offset = (int) PaginationHelper.offsetFromPageOffset((long) pageOffset, pageSize);
        List<EnterprisePaymentAuthEmployeeLimitDetail> employees = enterprisePaymentAuthEmployeeLimitDetailProvider
                .listEnterprisePaymentAuthEmployeeLimitDetailByScene(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId(), cmd.getSceneAppId(),
                        pageSize + 1, offset);
        if (employees == null || employees.size() == 0) {
            return response;
        }
        if (employees.size() == pageSize + 1) {
            employees.remove(employees.size() - 1);
            response.setNextPageOffset(pageOffset + 1);
        }
        if (CollectionUtils.isNotEmpty(employees)) {
            response.setEmployeeAuths(employees.stream().map(r -> {
                OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(r.getDetailId());
                return convertMember2EmployeePaymentAuthDTO(detail, r);
            }).collect(Collectors.toList()));
        }
        return response;
    }

    @Override
    public void createOrUpdatePaymentSceneLimit(CreateOrUpdateEnterprisePaymentSceneLimitCommand cmd) {
        this.coordinationProvider.getNamedLock(CoordinationLocks.ENTERPRISE_PAYMENT_AUTH_FROZEN.getCode() + "-" + cmd.getOrganizationId() + "-" + cmd.getSceneAppId()).enter(() -> {
            EnterprisePaymentAuthSceneLimit limit = enterprisePaymentAuthSceneLimitProvider.findEnterprisePaymentAuthSceneLimitByAppId(cmd.getOrganizationId(), UserContext.getCurrentNamespaceId(), cmd.getSceneAppId());
            if (limit == null) {
                limit = initEnterprisePaymentAuthSceneLimit(UserContext.getCurrentNamespaceId(), cmd.getSceneAppId(), cmd.getOrganizationId());
            }
            limit.setLimitAmount(cmd.getLimitAmount());
            saveEnterprisePaymentAuthSceneLimit(limit);
            return null;
        });
    }

    private void initEnterprisePaymentAuthSceneLimitIfNotExist(Integer namespaceId, Long organizationId, Long appId) {
        EnterprisePaymentAuthSceneLimit limit = enterprisePaymentAuthSceneLimitProvider.findEnterprisePaymentAuthSceneLimitByAppId(organizationId, namespaceId, appId);
        if (limit == null) {
            limit = initEnterprisePaymentAuthSceneLimit(namespaceId, appId, organizationId);
            enterprisePaymentAuthSceneLimitProvider.createEnterprisePaymentAuthSceneLimit(limit);
        }
    }

    private void saveEnterprisePaymentAuthSceneLimit(EnterprisePaymentAuthSceneLimit limit) {
        if (limit.getId() == null) {
            enterprisePaymentAuthSceneLimitProvider.createEnterprisePaymentAuthSceneLimit(limit);
        } else {
            enterprisePaymentAuthSceneLimitProvider.updateEnterprisePaymentAuthSceneLimit(limit);
        }

    }

    private EnterprisePaymentAuthSceneLimit initEnterprisePaymentAuthSceneLimit(Integer namespaceId, Long sceneAppId, Long organizationId) {
        EnterprisePaymentAuthSceneLimit limit = new EnterprisePaymentAuthSceneLimit();
        limit.setNamespaceId(namespaceId);
        limit.setOrganizationId(organizationId);
        limit.setPaymentSceneAppId(sceneAppId);
        limit.setIsDelete(NormalFlag.NO.getCode());

        return limit;
    }

    @Override
    public void batchCreateOrUpdateEmployeePaymentLimit(BatchCreateOrUpdateEmployeePaymentLimitCommand cmd, HttpServletRequest request) {
        if (CollectionUtils.isEmpty(cmd.getEmployees())) {
            return;
        }
        StringBuilder employeeListSB = new StringBuilder();
        List<EnterprisePaymentAuthEmployeeLimitChangeLog> changeLogList = new ArrayList<>();
        Set<Long> receiveUserIds = new HashSet<>();
        Map<Long, String> sceneAppMap = getEnterprisePaymentSceneIdNameMap();
        this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_ENTERPRISE_PAYMENT_AUTHS.getCode() + cmd.getOrganizationId()).enter(() -> {
	        dbProvider.execute(db->{
		        //不删除的场景
		        cmd.getEmployees().forEach(r -> {
		            List<EnterprisePaymentAuthEmployeeLimitDetail> createLimitDetails = new ArrayList<>();
		            if (employeeListSB.length() > 0) {
		                employeeListSB.append("、");
		            }
		            OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(r.getDetailId());
		            if (detail != null) {
		                List<Long> deleteAppIds = new ArrayList<Long>();
		                List<EnterprisePaymentAuthEmployeeLimitDetail> originLimitDetails = enterprisePaymentAuthEmployeeLimitDetailProvider.listEnterprisePaymentAuthEmployeeLimitDetail(detail.getNamespaceId(), cmd.getOrganizationId(), r.getDetailId());
		
		                //设置员工每月额度
		                employeeListSB.append(detail.getContactName());
		                EnterprisePaymentAuthEmployeeLimit employeeLimit = enterprisePaymentAuthEmployeeLimitProvider
		                        .findEnterprisePaymentAuthEmployeeLimitByDetailId(detail.getNamespaceId(), cmd.getOrganizationId(), r.getDetailId());
		                EnterprisePaymentAuthEmployeeLimitChangeLog changeLog = initChangeLog(detail);
		                changeLog.setOperateSourceType(OperateSourceType.LIMIT_AMOUNT.getCode());
		                changeLog.setOperateSourceId(0L);
		                changeLog.setOperateSourceName(localeStringService.getLocalizedString(EnterprisePaymentAuthConstants.SOURCE_SCOPE,
		                        EnterprisePaymentAuthConstants.SOURCE_LIMIT, EnterprisePaymentAuthConstants.LOCALE, "每月额度"));
		                if (employeeLimit == null) {
		                    changeLog.setOperateType(OperateType.ADD.getCode());
		                    changeLog.setChangedAmount(cmd.getEmployeeMonthLimitAmount());
		                    employeeLimit = initEnterprisePaymentAuthEmployeeLimit(detail);
		                }
		                processChangeLogAmount(changeLog, cmd.getEmployeeMonthLimitAmount(), employeeLimit.getLimitAmount());
		                employeeLimit.setLimitAmount(cmd.getEmployeeMonthLimitAmount());
		                employeeLimit.setPaymentSceneList("");
		                //设置员工每个场景的额度
		                if (null != cmd.getEmployeePaymentSceneLimits()) {
		                    for (EmployeePaymentSceneLimitSimpleDTO scene : cmd.getEmployeePaymentSceneLimits()) {
		                        deleteOriginLimitDetailByScene(originLimitDetails, scene.getSceneAppId());
		                        employeeLimit.setPaymentSceneList((employeeLimit.getPaymentSceneList().length() < 1 ? employeeLimit.getPaymentSceneList() : employeeLimit.getPaymentSceneList() + ",")
		                                + scene.getSceneAppId());
		                        EnterprisePaymentAuthEmployeeLimitDetail limitDetail = enterprisePaymentAuthEmployeeLimitDetailProvider
		                                .findEnterprisePaymentAuthEmployeeLimitDetailByDetailId(detail.getNamespaceId(), cmd.getOrganizationId(), r.getDetailId(), scene.getSceneAppId());
		                        EnterprisePaymentAuthEmployeeLimitChangeLog sceneChangeLog = initChangeLog(detail);
		                        sceneChangeLog.setOperateSourceType(OperateSourceType.PAYMENT_SCENE.getCode());
		                        sceneChangeLog.setOperateSourceName(scene.getSceneAppName());
		                        sceneChangeLog.setOperateSourceId(scene.getSceneAppId());
		                        if (limitDetail == null) {
		                            sceneChangeLog.setOperateType(OperateType.ADD.getCode());
		                            sceneChangeLog.setChangedAmount(scene.getLimitAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
		                            limitDetail = new EnterprisePaymentAuthEmployeeLimitDetail();
		                            processEnterprisePaymentAuthEmployeeLimitDetail(detail, limitDetail);
		                            limitDetail.setPaymentSceneAppId(scene.getSceneAppId());
		                        }
		                        limitDetail.setIsDelete((byte) 0);
		                        processChangeLogAmount(sceneChangeLog, scene.getLimitAmount().setScale(2, BigDecimal.ROUND_HALF_UP), limitDetail.getLimitAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
		                        if (sceneChangeLog.getOperateType() != null) {
		                            receiveUserIds.add(detail.getTargetId());
		                            changeLogList.add(sceneChangeLog);
		                            limitDetail.setLimitAmount(scene.getLimitAmount());
		                            createLimitDetails.add(limitDetail);
		                        }
		                    }
		                }
		
		                if (changeLog.getOperateType() != null) {
		                    receiveUserIds.add(detail.getTargetId());
		                    changeLogList.add(changeLog);
		                }
		                //无论有没有变更都更新一下employeeLimit (可能增减了scene)
		                saveEnterprisePaymentAuthEmployeeLimit(employeeLimit);
		                //要删除的limitdetail
		                if (CollectionUtils.isNotEmpty(originLimitDetails)) {
		                	
		                    for (EnterprisePaymentAuthEmployeeLimitDetail limitDetail : originLimitDetails) {
		                    	receiveUserIds.add(limitDetail.getUserId());
		                        EnterprisePaymentAuthEmployeeLimitChangeLog sceneChangeLog = initChangeLog(detail);
		                        sceneChangeLog.setOperateSourceType(OperateSourceType.PAYMENT_SCENE.getCode());
		                        sceneChangeLog.setOperateSourceName(sceneAppMap.get(limitDetail.getPaymentSceneAppId()));
		                        sceneChangeLog.setOperateSourceId(limitDetail.getPaymentSceneAppId());
		                        sceneChangeLog.setOperateType(OperateType.DELETE.getCode());
		                        changeLogList.add(sceneChangeLog);
		                        deleteAppIds.add(limitDetail.getPaymentSceneAppId());
		                    }
		                    enterprisePaymentAuthEmployeeLimitDetailProvider.deleteEnterprisePaymentAuthEmployeeLimitDetailsByDetailIdAndAppId(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId(), r.getDetailId(), deleteAppIds);
		                }
		            }
		
		            //删除场景授权再添加
		            if (CollectionUtils.isNotEmpty(createLimitDetails)) {
		                createLimitDetails.forEach(this::saveEnterprisePaymentAuthEmployeeLimitDetail);
		            }
		        });
		        //批量保存员工授权记录
		        enterprisePaymentAuthEmployeeLimitChangeLogProvider.batchCreateEnterprisePaymentAuthEmployeeLimitChangeLog(changeLogList);
		
		        //保存管理员授权日志
		        saveOperateLog(cmd, employeeListSB);
	
		        return null;});
            return null;
        });
        
        //发消息给有变更的用户
        if (!CollectionUtils.isEmpty(receiveUserIds)) {
            OrganizationMemberDetails operator = organizationProvider.findOrganizationMemberDetailsByTargetId(UserContext.currentUserId(), cmd.getOrganizationId());
            String homeUrl = request.getHeader("Host");
            for (Long uId : receiveUserIds) {
            	Integer namespaceId = UserContext.getCurrentNamespaceId();
            	ExecutorUtil.submit(() -> {
            		sendUpdateLimitMsg(operator == null ? "" : operator.getContactName(), uId, homeUrl, namespaceId, cmd.getOrganizationId());
            	});
            }
        }
    }

    private void deleteOriginLimitDetailByScene(List<EnterprisePaymentAuthEmployeeLimitDetail> originLimitDetails, Long sceneAppId) {
        if (CollectionUtils.isNotEmpty(originLimitDetails)) {
            for (EnterprisePaymentAuthEmployeeLimitDetail limitDetail : originLimitDetails) {
                if (limitDetail.getPaymentSceneAppId().equals(sceneAppId)) {
                    originLimitDetails.remove(limitDetail);
                    return;
                }
            }
        }
    }

    private void sendUpdateLimitMsg(String operatorName, Long receiveUserId, String homeUrl, Integer namespaceId, Long organizationId) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("operator", operatorName);
        String content = localeTemplateService.getLocaleTemplateString(EnterprisePaymentAuthConstants.MSG_SCOPE, EnterprisePaymentAuthConstants.UPDATE_LIMIT, EnterprisePaymentAuthConstants.LOCALE,
                map, operatorName + "调整了你的企业支付额度，点击查看。");
        sendMessage(receiveUserId, content, MessageUrlType.PAYMENT_AUTH_LIMIT, homeUrl, namespaceId, organizationId);
    }

    private void sendMsgAfterPaySuccess(Long payDateTime, BigDecimal payAmount, Long appId, String homeUrl, OrganizationMemberDetails memberDetail) {
        Map<Long, String> appMap = getEnterprisePaymentSceneIdNameMap(memberDetail.getNamespaceId());
        if (!appMap.containsKey(appId)) {
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("payTime", DATE_TIME_SDF.get().format(new Date(payDateTime)));
        map.put("payAmount", payAmount.doubleValue());
        map.put("sceneName", appMap.get(appId));
        String content = localeTemplateService.getLocaleTemplateString(EnterprisePaymentAuthConstants.MSG_SCOPE, EnterprisePaymentAuthConstants.PAYMENT_AUTH_USED_MESSAGE, EnterprisePaymentAuthConstants.LOCALE, map, "");
        sendMessage(memberDetail.getTargetId(), content, MessageUrlType.PAYMENT_AUTH_LIMIT, homeUrl, memberDetail.getNamespaceId(), memberDetail.getOrganizationId());
    }

    private void sendMessage(Long receiveUserId, String content, MessageUrlType urlType, String homeUrl, Integer namespaceId, Long organizationId) {
        MessageDTO message = new MessageDTO();
        message.setBodyType(MessageBodyType.TEXT.getCode());
        message.setBody(content.toString());
        message.setMetaAppId(AppConstants.APPID_DEFAULT);
        message.setChannels(new MessageChannel(ChannelType.USER.getCode(), String.valueOf(receiveUserId)));
        String subjectName = localeStringService.getLocalizedString(EnterprisePaymentAuthConstants.ENTERPRISE_PAYMENT_AUTH_SCOPE,
                EnterprisePaymentAuthConstants.MSG_SUBJECT_1, RemindContants.LOCALE, "企业支付额度");
        Map<String, String> meta = new HashMap<>();
        if (urlType != null) {
            String url = null;
            switch (urlType) {
                case PAYMENT_AUTH_LIMIT:
                    url = "http://" + homeUrl + "/enterprise-payment-auth/build/index.html?organizationId=" + organizationId + "&namespaceId=" + namespaceId + "#/home#sign_suffix";
                    break;
                default:
                    break;
            }
            RouterMetaObject metaObject = new RouterMetaObject();
            OfficialActionData data = new OfficialActionData();
            data.setUrl(url);
            metaObject.setUrl(RouterBuilder.build(Router.BROWSER_I, data, subjectName));
            meta.put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(metaObject));
        }
        meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.MESSAGE_ROUTER.getCode());
        meta.put(MessageMetaConstant.MESSAGE_SUBJECT, subjectName);

        message.setMeta(meta);
        //  send the message
        messagingService.routeMessage(
                User.SYSTEM_USER_LOGIN,
                AppConstants.APPID_MESSAGING,
                ChannelType.USER.getCode(),
                String.valueOf(receiveUserId),
                message,
                MessagingConstants.MSG_FLAG_STORED_PUSH.getCode()
        );
    }

    private void saveOperateLog(BatchCreateOrUpdateEmployeePaymentLimitCommand cmd,
                                StringBuilder employeeListSB) {
        EnterprisePaymentAuthOperateLog operateLog = new EnterprisePaymentAuthOperateLog();
        operateLog.setNamespaceId(UserContext.getCurrentNamespaceId());
        operateLog.setOperatorUid(UserContext.currentUserId());
        operateLog.setOrganizationId(cmd.getOrganizationId());
        String sceneString = "";
        if (CollectionUtils.isEmpty(cmd.getEmployeePaymentSceneLimits())) {
            sceneString += localeStringService.getLocalizedString(EnterprisePaymentAuthConstants.ENTERPRISE_PAYMENT_AUTH_SCOPE,
                    EnterprisePaymentAuthConstants.NO_SCENE, EnterprisePaymentAuthConstants.LOCALE, "无");
        } else {
            if (sceneString.length() > 1) {
                sceneString += "、";
            }
            for (int i = 0; i < cmd.getEmployeePaymentSceneLimits().size() ; i++ ) {
                EmployeePaymentSceneLimitSimpleDTO dto = cmd.getEmployeePaymentSceneLimits().get(i);
                Map<String, String> map = new HashMap<String, String>();
                map.put("sceneName", dto.getSceneAppName());
                map.put("limitAmount", String.valueOf(dto.getLimitAmount().setScale(2, BigDecimal.ROUND_HALF_UP)));
                String result = localeTemplateService.getLocaleTemplateString(EnterprisePaymentAuthConstants.OPERATE_LOG_SCOPE,
                        EnterprisePaymentAuthConstants.OPERATE_LOG_SCENE, EnterprisePaymentAuthConstants.LOCALE, map, "");
                sceneString += result + ((i < cmd.getEmployeePaymentSceneLimits().size() - 1) ? "、" : "。");
            }
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("employeeCount", String.valueOf(cmd.getEmployees().size()));
        map.put("employeeName", employeeListSB.toString());
        map.put("limitAmount", String.valueOf(cmd.getEmployeeMonthLimitAmount().setScale(2, BigDecimal.ROUND_HALF_UP)));
        map.put("sceneString", sceneString);
        String logString = localeTemplateService.getLocaleTemplateString(EnterprisePaymentAuthConstants.OPERATE_LOG_SCOPE,
                EnterprisePaymentAuthConstants.OPERATE_LOG_MAIN, EnterprisePaymentAuthConstants.LOCALE, map, "");
        operateLog.setLog(logString);
        operateLog.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        enterprisePaymentAuthOperateLogProvider.createEnterprisePaymentAuthOperateLog(operateLog);
    }

    private void saveEnterprisePaymentAuthEmployeeLimitDetail(EnterprisePaymentAuthEmployeeLimitDetail limitDetail) {
        if (limitDetail.getId() == null) {
            enterprisePaymentAuthEmployeeLimitDetailProvider.createEnterprisePaymentAuthEmployeeLimitDetail(limitDetail);
        } else {
            enterprisePaymentAuthEmployeeLimitDetailProvider.updateEnterprisePaymentAuthEmployeeLimitDetail(limitDetail);
        }

    }

    private void processChangeLogAmount(EnterprisePaymentAuthEmployeeLimitChangeLog changeLog, BigDecimal currentAmount, BigDecimal originAmount) {
        if (currentAmount != null) {
            if (currentAmount.compareTo(originAmount) > 0) {
                if (changeLog.getOperateType() == null) changeLog.setOperateType(OperateType.CHANGE.getCode());
                changeLog.setChangedAmount(currentAmount.subtract(originAmount));
            } else if (currentAmount.compareTo(originAmount) < 0) {
                if (changeLog.getOperateType() == null) changeLog.setOperateType(OperateType.CHANGE.getCode());
                changeLog.setChangedAmount(currentAmount.subtract(originAmount));
            }
        }
        changeLog.setCurrentAmount(currentAmount);
    }

    private void processEnterprisePaymentAuthEmployeeLimitDetail(OrganizationMemberDetails detail, EnterprisePaymentAuthEmployeeLimitDetail limitDetail) {
        limitDetail.setNamespaceId(detail.getNamespaceId());
        limitDetail.setOrganizationId(detail.getOrganizationId());
        limitDetail.setUserId(detail.getTargetId());
        limitDetail.setDetailId(detail.getId());
        limitDetail.setLimitAmount(BigDecimal.ZERO);
        limitDetail.setHistoricalTotalPayAmount(BigDecimal.ZERO);
        limitDetail.setHistoricalPayCount(0);
    }

    private EnterprisePaymentAuthEmployeeLimitChangeLog initChangeLog(OrganizationMemberDetails detail) {
        EnterprisePaymentAuthEmployeeLimitChangeLog log = new EnterprisePaymentAuthEmployeeLimitChangeLog();
        log.setNamespaceId(detail.getNamespaceId());
        log.setDetailId(detail.getId());
        log.setOrganizationId(detail.getOrganizationId());
        log.setUserId(detail.getTargetId());
        return log;
    }

    private void saveEnterprisePaymentAuthEmployeeLimit(EnterprisePaymentAuthEmployeeLimit employeeLimit) {
        if (employeeLimit.getId() == null) {
            enterprisePaymentAuthEmployeeLimitProvider.createEnterprisePaymentAuthEmployeeLimit(employeeLimit);
        } else {
            enterprisePaymentAuthEmployeeLimitProvider.updateEnterprisePaymentAuthEmployeeLimit(employeeLimit);
        }
    }

    private EnterprisePaymentAuthEmployeeLimit initEnterprisePaymentAuthEmployeeLimit(OrganizationMemberDetails detail) {
        EnterprisePaymentAuthEmployeeLimit employeeLimit = new EnterprisePaymentAuthEmployeeLimit();
        employeeLimit.setDetailId(detail.getId());
        employeeLimit.setUserId(detail.getTargetId());
        employeeLimit.setOrganizationId(detail.getOrganizationId());
        employeeLimit.setLimitAmount(BigDecimal.ZERO);
        employeeLimit.setNamespaceId(detail.getNamespaceId());
        employeeLimit.setHistoricalPayCount(0);
        employeeLimit.setHistoricalTotalPayAmount(BigDecimal.ZERO);
        employeeLimit.setCreatorUid(UserContext.currentUserId());
        employeeLimit.setOperatorUid(UserContext.currentUserId());
        return employeeLimit;
    }

    @Override
    public GetEnterpriseEmployeePaymentLimitDetailResponse getEmployeePaymentLimitDetail(GetEnterpriseEmployeePaymentLimitDetailCommand cmd) {
        GetEnterpriseEmployeePaymentLimitDetailResponse response = new GetEnterpriseEmployeePaymentLimitDetailResponse();
        OrganizationMemberDetails employee = organizationProvider.findOrganizationMemberDetailsByDetailId(cmd.getDetailId());
        response.setDetailId(employee.getId());
        response.setUserId(employee.getTargetId());
        response.setContactName(employee.getContactName());

        Map<Long, String> dptMap = archivesService.getEmployeeDepartment(employee.getId());
        if (null != dptMap) {
            response.setDepartmentId(dptMap.keySet().iterator().next());
            String depName = archivesService.convertToOrgNames(dptMap);
            response.setDepartmentName(depName);
        }
        response.setContactAvatar(contentServerService.parserUri(employee.getAvatar()));
        EnterprisePaymentAuthEmployeeLimit limit = enterprisePaymentAuthEmployeeLimitProvider
                .findEnterprisePaymentAuthEmployeeLimitByDetailId(employee.getNamespaceId(), employee.getOrganizationId(), employee.getId());
        if (limit != null) {
            response.setLimitAmount(limit.getLimitAmount());
            response.setHistoricalTotalPayAmount(limit.getHistoricalTotalPayAmount());
            response.setHistoricalPayCount(limit.getHistoricalPayCount());
            //本月
            EnterprisePaymentAuthEmployeePayHistory history = enterprisePaymentAuthEmployeePayHistoryProvider.findEnterprisePaymentAuthEmployeePayHistoryByDetailId(
                    employee.getNamespaceId(), employee.getOrganizationId(), employee.getId(), MONTH_DF.format(LocalDateTime.now()));
            response.setCurrentMonthRemainAmount(response.getLimitAmount() == null ? null : response.getLimitAmount().subtract((history == null || history.getUsedAmount() == null) ? BigDecimal.ZERO : history.getUsedAmount()));
            if (response.getCurrentMonthRemainAmount().compareTo(BigDecimal.ZERO) < 0) {
                response.setCurrentMonthRemainAmount(BigDecimal.ZERO);
            }
        }
        //场景
        response.setSceneAuths(new ArrayList<>());
        Map<Long, String> appMap = getEnterprisePaymentSceneIdNameMap();
        if (appMap.keySet().size() == 0) {
            return response;
        }
        List<Map.Entry<Long, String>> sortedList = sortMapByValue(appMap);
        for (Map.Entry<Long, String> entry : sortedList) {
            PaymentAuthSceneDTO dto = new PaymentAuthSceneDTO();
            dto.setSceneAppId(entry.getKey());
            dto.setSceneAppName(entry.getValue());
            EnterprisePaymentAuthEmployeeLimitDetail limitDetail = enterprisePaymentAuthEmployeeLimitDetailProvider.findEnterprisePaymentAuthEmployeeLimitDetailByDetailId(
                    employee.getNamespaceId(), employee.getOrganizationId(), employee.getId(), entry.getKey());
            if (limitDetail != null) {
                dto.setLimitAmount(limitDetail.getLimitAmount());

                EnterprisePaymentAuthEmployeePaySceneHistory sceneHistory = enterprisePaymentAuthEmployeePaySceneHistoryProvider.findEnterprisePaymentAuthEmployeePaySceneHistoryByDetailId(
                        employee.getNamespaceId(), employee.getOrganizationId(), employee.getId(), entry.getKey(), MONTH_DF.format(LocalDateTime.now()));
                dto.setUsedAmount((sceneHistory == null || sceneHistory.getUsedAmount() == null) ? BigDecimal.ZERO : sceneHistory.getUsedAmount());
                dto.setCurrentMonthRemainAmount(dto.getLimitAmount().subtract(dto.getUsedAmount()));
                if (dto.getCurrentMonthRemainAmount().compareTo(BigDecimal.ZERO) < 0) {
                    dto.setCurrentMonthRemainAmount(BigDecimal.ZERO);
                }
                response.getSceneAuths().add(dto);
            }
        }
        return response;
    }

    @Override
    public ListEnterpriseEmployeePaymentLimitChangeLogsResponse listEmployeePaymentLimitChangeLogs(ListEnterpriseEmployeePaymentLimitChangeLogsCommand cmd) {
        ListEnterpriseEmployeePaymentLimitChangeLogsResponse response = new ListEnterpriseEmployeePaymentLimitChangeLogsResponse();
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        int pageOffset = (cmd.getPageOffset() != null && cmd.getPageOffset() > 1) ? cmd.getPageOffset() : 1;
        int offset = (int) PaginationHelper.offsetFromPageOffset((long) pageOffset, pageSize);

        OrganizationMemberDetails memberDetail = organizationProvider.findOrganizationMemberDetailsByDetailId(cmd.getDetailId());
        List<EmployeePaymentLimitChangeLogsGroupDTO> groupDTOS = enterprisePaymentAuthEmployeeLimitChangeLogProvider.listEmployeePaymentLimitChangeLogsGroups(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId(), memberDetail.getId(), offset, pageSize + 1);
        Integer nextPageOffset = null;
        if (groupDTOS != null && groupDTOS.size() > pageSize) {
            nextPageOffset = pageOffset + 1;
            groupDTOS.remove(pageSize);
        }
        if (CollectionUtils.isEmpty(groupDTOS)) {
            return response;
        }
        List<EnterprisePaymentAuthEmployeeLimitChangeLog> logDetails = enterprisePaymentAuthEmployeeLimitChangeLogProvider.listEnterprisePaymentAuthEmployeeLimitChangeLog(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId(), memberDetail.getId(), getOperateNoList(groupDTOS));
        if (CollectionUtils.isEmpty(logDetails)) {
            return response;
        }
        response.setNextPageOffset(nextPageOffset);
        Map<Long, List<EmployeePaymentLimitChangeLogItemDTO>> groupMap = buildEmployeePaymentLimitChangeLogItemGroupByOperateNo(logDetails);
        Map<Long, String> userNameMap = new HashMap<>();
        response.setPaymentEmployeeChangeLogs(groupDTOS.stream().map(r -> {
            PaymentAuthOperateLogDTO dto = new PaymentAuthOperateLogDTO();
            dto.setOperatorUid(r.getOperatorUid());
            setOperatorName(userNameMap, r, cmd.getOrganizationId());
            dto.setOperatorName(r.getOperatorName());
            dto.setOperateTime(r.getOperateTime());
            StringBuilder operateLog = processChangeLog(groupMap, r);
            dto.setOperateLog(operateLog.toString());
            return dto;
        }).collect(Collectors.toList()));

        return response;
    }

    private StringBuilder processChangeLog(Map<Long, List<EmployeePaymentLimitChangeLogItemDTO>> groupMap, EmployeePaymentLimitChangeLogsGroupDTO r) {
        StringBuilder operateLog = new StringBuilder();
        List<EmployeePaymentLimitChangeLogItemDTO> changeLogList = groupMap.get(r.getOperateNo());
        changeLogList = sortChangeLogItemList(changeLogList);
        for (EmployeePaymentLimitChangeLogItemDTO changeLog : changeLogList) {
            if (operateLog.length() > 1) {
                operateLog.append("\n");
            }
            String result = "";
            if (OperateType.DELETE == OperateType.fromCode(changeLog.getOperateType())) {
                result = changeLog.getSourceName() + ":" + " 不再支持";
            }else {
                String changeAmount = "";
                if (changeLog.getChangedAmount().compareTo(BigDecimal.ZERO) >= 0) {
                    changeAmount += localeStringService.getLocalizedString(EnterprisePaymentAuthConstants.ENTERPRISE_PAYMENT_AUTH_SCOPE,
                            EnterprisePaymentAuthConstants.INCREASE, EnterprisePaymentAuthConstants.LOCALE, "增加");
                } else {
                    changeAmount += localeStringService.getLocalizedString(EnterprisePaymentAuthConstants.ENTERPRISE_PAYMENT_AUTH_SCOPE,
                            EnterprisePaymentAuthConstants.DECREASE, EnterprisePaymentAuthConstants.LOCALE, "减少");
                }
                changeAmount += changeLog.getChangedAmount().abs().toString();
                Map<String, String> map = new HashMap<String, String>();
                map.put("sceneName", changeLog.getSourceName() == null ? "" : changeLog.getSourceName());
                map.put("changeAmount", changeAmount);
                map.put("limitAmount", changeLog.getCurrentLimitAmount().toString());
                result = localeTemplateService.getLocaleTemplateString(EnterprisePaymentAuthConstants.OPERATE_LOG_SCOPE,
                        EnterprisePaymentAuthConstants.CHANGE_LOG, EnterprisePaymentAuthConstants.LOCALE, map, "");
            }

            operateLog.append(result);
        }
        return operateLog;
    }

    @Override
    public ListEnterprisePaymentAuthOperateLogsResponse listPaymentAuthOperateLogs(ListEnterprisePaymentAuthOperateLogsCommand cmd) {
        ListEnterprisePaymentAuthOperateLogsResponse response = new ListEnterprisePaymentAuthOperateLogsResponse();
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        int pageOffset = (cmd.getPageOffset() != null && cmd.getPageOffset() > 1) ? cmd.getPageOffset() : 1;
        int offset = (int) PaginationHelper.offsetFromPageOffset((long) pageOffset, pageSize);
        List<EnterprisePaymentAuthOperateLog> operateLogs = enterprisePaymentAuthOperateLogProvider
                .listEnterprisePaymentAuthOperateLogs(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId(), pageSize + 1, offset);
        Integer nextPageOffset = null;
        if (operateLogs != null && operateLogs.size() > pageSize) {
            nextPageOffset = pageOffset + 1;
            operateLogs.remove(pageSize);
        }
        response.setNextPageOffset(nextPageOffset);
        if (CollectionUtils.isEmpty(operateLogs)) {
            return response;
        }
        response.setPaymentAuthOperateLogs(operateLogs.stream().map(this::convertOperateLog).collect(Collectors.toList()));
        return response;
    }

    private PaymentAuthOperateLogDTO convertOperateLog(EnterprisePaymentAuthOperateLog log) {
        PaymentAuthOperateLogDTO dto = new PaymentAuthOperateLogDTO();
        dto.setOperatorUid(log.getOperatorUid());
        dto.setOperateTime(log.getOperateTime().getTime());
        OrganizationMemberDetails operator = organizationProvider.findOrganizationMemberDetailsByTargetId(log.getOperatorUid(), log.getOrganizationId());
        if (null != operator) {
            dto.setOperatorName(operator.getContactName());
        }
        dto.setOperateLog(log.getLog());
        return dto;

    }

    @Override
    public ListEnterprisePaymentPayLogsResponse listEnterprisePayLogs(ListEnterprisePaymentPayLogsCommand cmd) {
        ListEnterprisePaymentPayLogsResponse response = new ListEnterprisePaymentPayLogsResponse();
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        int pageOffset = (cmd.getPageOffset() != null && cmd.getPageOffset() > 1) ? cmd.getPageOffset() : 1;
        int offset = (int) PaginationHelper.offsetFromPageOffset((long) pageOffset, pageSize);
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        if (null != cmd.getNamespaceId()) {
            namespaceId = cmd.getNamespaceId();
        }
        Long merchantOrderId = null;
        try {
            if (StringUtils.hasText(cmd.getOrderNo())) {
                merchantOrderId = Long.parseLong(cmd.getOrderNo());
            }
        } catch (Exception e) {
            return response;
        }
        Timestamp startTimestamp = null;
        if (cmd.getPaymentStartDate() != null) {
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTimeInMillis(cmd.getPaymentStartDate());
            startCalendar = DateStatisticHelper.getDateBeginCalendar(startCalendar);
            startTimestamp = new Timestamp(startCalendar.getTimeInMillis());
        }
        Timestamp endTimestamp = null;
        if (cmd.getPaymentEndDate() != null) {
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTimeInMillis(cmd.getPaymentEndDate());
            endCalendar = DateStatisticHelper.getDateBeginCalendar(endCalendar);
            endCalendar.add(Calendar.DAY_OF_MONTH, 1);
            endTimestamp = new Timestamp(endCalendar.getTimeInMillis());
        }
        List<Long> detialIds = null;
        if (cmd.getKeyWords() != null) {
            detialIds = organizationService.listMemberDetailIds(cmd.getOrganizationId(), cmd.getOrganizationId(), Integer.MAX_VALUE - 1, 1, cmd.getKeyWords());

        }
        List<EnterprisePaymentAuthPayLog> payLogs = enterprisePaymentAuthPayLogProvider.listEnterprisePaymentAuthPayLogs(namespaceId, cmd.getOrganizationId(), cmd.getPaymentSceneAppId(), startTimestamp,
                endTimestamp, detialIds, merchantOrderId, offset, pageSize + 1);
        Integer nextPageOffset = null;
        if (payLogs != null && payLogs.size() > pageSize) {
            nextPageOffset = pageOffset + 1;
            payLogs.remove(pageSize);
        }
        if (CollectionUtils.isEmpty(payLogs)) {
            return response;
        }
        response.setNextPageOffset(nextPageOffset);
        Map<Long, String> appMap = getEnterprisePaymentSceneIdNameMap();
        Map<Long, OrganizationMemberDetails> memberDetailsMap = new HashMap<>();
        response.setPayLogs(payLogs.stream().map(r -> convertPayLogDTO(r, appMap, memberDetailsMap)).collect(Collectors.toList()));

        return response;
    }

    private PaymentPayLogDTO convertPayLogDTO(EnterprisePaymentAuthPayLog r, Map<Long, String> appMap, Map<Long, OrganizationMemberDetails> memberDetailsMap) {
        PaymentPayLogDTO dto = new PaymentPayLogDTO();
        dto.setOrderNo(String.valueOf(r.getMerchantOrderId()));
        dto.setUserId(r.getUserId());
        dto.setPayAmount(r.getPayAmmount());
        dto.setPayTime(r.getPayTime().getTime());
        dto.setPaymentSceneAppId(r.getPaymentSceneAppId());
        dto.setPaymentSceneAppName(appMap.get(r.getPaymentSceneAppId()));
        OrganizationMemberDetails detail = getOrganizationMemberDetailsByTargetId(r.getOrganizationId(), r.getUserId(), memberDetailsMap);
        if (null != detail) {
            dto.setContactToken(detail.getContactToken());
            dto.setContactName(detail.getContactName());
        }
        return dto;
    }

    private OrganizationMemberDetails getOrganizationMemberDetailsByTargetId(Long organizationId, Long userId, Map<Long, OrganizationMemberDetails> memberDetailsMap) {
        OrganizationMemberDetails result = memberDetailsMap.size() > 0 ? memberDetailsMap.get(userId) : null;
        if (result == null) {
            result = organizationProvider.findOrganizationMemberDetailsByTargetId(userId, organizationId);
            memberDetailsMap.put(userId, result);
        }

        return result;
    }

    @Override
    public void exportEnterprisePayLogs(ExportEnterprisePaymentPayLogsCommand cmd) {
        Map<String, Object> params = new HashMap<>();
        params.put("organizationId", cmd.getOrganizationId());
        params.put("paymentStartDate", cmd.getPaymentStartDate());
        params.put("paymentEndDate", cmd.getPaymentEndDate());
        params.put("paymentSceneAppId", cmd.getPaymentSceneAppId());
        params.put("keyWords", cmd.getKeyWords());
        params.put("orderNo", cmd.getOrderNo());
        params.put("namespaceId", UserContext.getCurrentNamespaceId());
        params.put("reportType", "exportEnterprisePayLogs");

        String fileName = localeStringService.getLocalizedString(EnterprisePaymentAuthConstants.SOURCE_SCOPE,
                EnterprisePaymentAuthConstants.EXCEL_HEAD1, EnterprisePaymentAuthConstants.LOCALE,
                "企业支付记录") + ".xlsx";
        taskService.createTask(fileName, TaskType.FILEDOWNLOAD.getCode(), EnterprisePaymentAuthExportTaskHandler.class, params, TaskRepeatFlag.REPEAT.getCode(), new Date());
    }

    private void createEnterprisePayLogsData(Row row,
                                             PaymentPayLogDTO dto, SimpleDateFormat dateTimeSF) {
        int i = -1;
        row.createCell(++i).setCellValue(dto.getOrderNo());
        row.createCell(++i).setCellValue(dto.getContactName());
        row.createCell(++i).setCellValue(dto.getContactToken());
        row.createCell(++i).setCellValue(dto.getPaymentSceneAppName());
        row.createCell(++i).setCellValue(dateTimeSF.format(new Date(dto.getPayTime())));
        row.createCell(++i).setCellValue(dto.getPayAmount().toString());
    }

    @Override
    public OutputStream getEnterprisePayLogsOutputStream(Integer namespaceId, Long organizationId, Long paymentSceneAppId,
                                                         Long paymentStartDate, Long paymentEndDate, String keyWords, String orderNo, Long taskId) {
        //  title
        String titleString = localeStringService.getLocalizedString(EnterprisePaymentAuthConstants.SOURCE_SCOPE,
                EnterprisePaymentAuthConstants.EXCEL_TITLE, EnterprisePaymentAuthConstants.LOCALE,
                "订单编号;使用人;使用人手机;支付场景;支付时间;支付金额");
        List<String> title = Arrays.asList(titleString.split(";"));
        XSSFWorkbook workbook = new XSSFWorkbook();

        String EXCEL_HEAD1 = localeStringService.getLocalizedString(EnterprisePaymentAuthConstants.SOURCE_SCOPE,
                EnterprisePaymentAuthConstants.EXCEL_HEAD1, EnterprisePaymentAuthConstants.LOCALE,
                "企业支付记录");
        Sheet sheet = workbook.createSheet(EXCEL_HEAD1);
        //  1.head
        Row headRow = sheet.createRow(0);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, title.size() - 1));
        headRow.setHeight((short) (50 * 20));
        createExcelHead(workbook, headRow, EXCEL_HEAD1);

        String excelHead2 = localeStringService.getLocalizedString(EnterprisePaymentAuthConstants.SOURCE_SCOPE,
                EnterprisePaymentAuthConstants.EXCEL_HEAD2, EnterprisePaymentAuthConstants.LOCALE,
                "支付时间:");
        SimpleDateFormat dateSF = new SimpleDateFormat("yyyy-MM-dd");
        if (paymentStartDate != null) {
            excelHead2 += dateSF.format(new Date(paymentStartDate));
            excelHead2 += " ~ ";
            if (paymentEndDate != null) {
                excelHead2 += dateSF.format(new Date(paymentEndDate));
            }
        }
        Row headRow2 = sheet.createRow(1);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, title.size() - 1));
        headRow.setHeight((short) (50 * 20));
        createExcelHead(workbook, headRow2, excelHead2);
        //  2.title
        Row titleRow = sheet.createRow(2);
        createExcelTitle(workbook, sheet, titleRow, title);

        taskService.updateTaskProcess(taskId, 10);
        //  data
        setEnterprisePayLogsByPage(namespaceId, organizationId, paymentSceneAppId, paymentStartDate, paymentEndDate, keyWords, orderNo, taskId, sheet);
        taskService.updateTaskProcess(taskId, 99);
        return writeExcel(workbook);
    }

    private void setEnterprisePayLogsByPage(Integer namespaceId, Long organizationId, Long paymentSceneAppId, Long paymentStartDate, Long paymentEndDate, String keyWords, String orderNo, Long taskId, Sheet sheet) {
        ListEnterprisePaymentPayLogsCommand cmd = new ListEnterprisePaymentPayLogsCommand();
        cmd.setKeyWords(keyWords);
        cmd.setOrderNo(orderNo);
        cmd.setOrganizationId(organizationId);
        cmd.setPageOffset(1);
        cmd.setPageSize(100);
        cmd.setPaymentStartDate(paymentStartDate);
        cmd.setPaymentEndDate(paymentEndDate);
        cmd.setNamespaceId(namespaceId);
        cmd.setPaymentSceneAppId(paymentSceneAppId);
        ListEnterprisePaymentPayLogsResponse resp = listEnterprisePayLogs(cmd);
        int setedCount = 0;
        boolean process = resp != null && CollectionUtils.isNotEmpty(resp.getPayLogs());
        while (process) {
            setEnterprisePayLogsListData(resp.getPayLogs(), sheet);
            if (taskId != null && (++setedCount % 20 == 0)) {
                taskService.updateTaskProcess(taskId, 10 + Math.min(89, setedCount / 20));
            }
            if (resp.getNextPageOffset() != null && resp.getNextPageOffset() > cmd.getPageOffset()) {
                cmd.setPageOffset(resp.getNextPageOffset());
                resp = listEnterprisePayLogs(cmd);
                process = resp != null && CollectionUtils.isNotEmpty(resp.getPayLogs());
            } else {
                process = false;
            }
        }
    }

    private void setEnterprisePayLogsListData(List<PaymentPayLogDTO> payLogs, Sheet sheet) {
        //  3.data
        if (CollectionUtils.isNotEmpty(payLogs)) {
            SimpleDateFormat dateTimeSF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (PaymentPayLogDTO payLog : payLogs) {
                Row dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
                createEnterprisePayLogsData(dataRow, payLog, dateTimeSF);
            }
        }
    }


    private XSSFCellStyle commonTitleStyle(XSSFWorkbook workbook) {
        XSSFCellStyle titleStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setBold(true);
        font.setFontName("微软雅黑");
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setFont(font);
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        return titleStyle;
    }

    private void createExcelTitle(XSSFWorkbook workbook, Sheet sheet, Row titleRow, List<String> title) {
        XSSFCellStyle commonStyle = commonTitleStyle(workbook);
        for (int i = 0; i < title.size(); i++) {
            Cell cell = titleRow.createCell(i);
            sheet.setColumnWidth(i, 18 * 256);
            cell.setCellStyle(commonStyle);
            cell.setCellValue(title.get(i));
        }
    }

    private void createExcelHead(XSSFWorkbook workbook, Row headRow, String head) {
        XSSFCellStyle headStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        // font.setBold(true);
        font.setFontName("微软雅黑");
        headStyle.setFont(font);
        headStyle.setAlignment(HorizontalAlignment.CENTER);
        headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex()); //  find it in the IndexedColors
        headStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headStyle.setWrapText(true);

        Cell cell = headRow.createCell(0);
        cell.setCellStyle(headStyle);
        cell.setCellValue(head);
    }

    private ByteArrayOutputStream writeExcel(XSSFWorkbook workbook) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            workbook.write(out);
        } catch (Exception e) {
            LOGGER.error("export error, e = {}", e);
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                LOGGER.error("export error, e = {}", e);
            }
        }
        return out;
    }


    @Override
    public GetEmployeePaymentAuthDetailResponse getEmployeePaymentAuthDetail(GetEmployeePaymentAuthDetailCommand cmd) {
        Long userId = cmd.getUserId() != null ? cmd.getUserId() : UserContext.currentUserId();
        OrganizationMemberDetails memberDetail = organizationProvider.findOrganizationMemberDetailsByTargetIdAndOrgId(userId, cmd.getOrganizationId());

        GetEmployeePaymentAuthDetailResponse response = new GetEmployeePaymentAuthDetailResponse();
        response.setUserId(userId);
        response.setDetailId(memberDetail.getId());
        response.setContactName(memberDetail.getContactName());
        response.setPaymentSceneLimitDTOS(new ArrayList<>());

        EnterprisePaymentAuthEmployeeLimit employeeLimit = enterprisePaymentAuthEmployeeLimitProvider.findEnterprisePaymentAuthEmployeeLimitByDetailId(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId(), memberDetail.getId());
        if (employeeLimit == null) {
            return response;
        }
        String month = MONTH_DF.format(LocalDateTime.now());
        response.setLimitAmount(employeeLimit.getLimitAmount() != null ? employeeLimit.getLimitAmount() : BigDecimal.ZERO);
        response.setCurrentMonthRemainAmount(response.getLimitAmount());
        EnterprisePaymentAuthEmployeePayHistory usedHistory = enterprisePaymentAuthEmployeePayHistoryProvider.findEnterprisePaymentAuthEmployeePayHistoryByDetailId(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId(), memberDetail.getId(), month);
        if (usedHistory != null && usedHistory.getUsedAmount() != null) {
            response.setUsedAmount(usedHistory.getUsedAmount());
            BigDecimal remainAmount = response.getLimitAmount().subtract(response.getUsedAmount());
            if (remainAmount.compareTo(BigDecimal.ZERO) < 0) {
                remainAmount = BigDecimal.ZERO;
            }
            response.setCurrentMonthRemainAmount(remainAmount);
        }

        List<EnterprisePaymentAuthEmployeeLimitDetail> limitDetails = enterprisePaymentAuthEmployeeLimitDetailProvider.listEnterprisePaymentAuthEmployeeLimitDetail(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId(), memberDetail.getId());
        if (CollectionUtils.isEmpty(limitDetails)) {
            return response;
        }

        response.setPaymentAuthSceneCount(limitDetails.size());

        Map<Long, String> paymentSceneIdNameMap = getEnterprisePaymentSceneIdNameMap();
        Map<String, EnterprisePaymentAuthEmployeePaySceneHistory> historyMap = getEnterprisePaymentAuthEmployeePaySceneHistoryMap(cmd.getOrganizationId(), memberDetail.getId(), month);

        List<EmployeePaymentSceneLimitDTO> paymentSceneLimitDTOS = new ArrayList<>();
        for (EnterprisePaymentAuthEmployeeLimitDetail limitDetail : limitDetails) {
            EmployeePaymentSceneLimitDTO limitDTO = new EmployeePaymentSceneLimitDTO();
            limitDTO.setDetailId(limitDetail.getDetailId());
            limitDTO.setUserId(limitDetail.getUserId());
            limitDTO.setSceneAppId(limitDetail.getPaymentSceneAppId());
            limitDTO.setSceneAppName(paymentSceneIdNameMap.get(limitDetail.getPaymentSceneAppId()));
            limitDTO.setLimitAmount(limitDetail.getLimitAmount() != null ? limitDetail.getLimitAmount() : BigDecimal.ZERO);
            limitDTO.setCurrentMonthRemainAmount(limitDTO.getLimitAmount());
            limitDTO.setUsedAmount(BigDecimal.ZERO);
            EnterprisePaymentAuthEmployeePaySceneHistory history = historyMap.get(String.format("%d-%d", limitDetail.getDetailId(), limitDetail.getPaymentSceneAppId()));
            if (history != null && history.getUsedAmount() != null) {
                limitDTO.setUsedAmount(history.getUsedAmount());
                BigDecimal remainAmount = limitDTO.getLimitAmount().subtract(limitDTO.getUsedAmount());
                if (remainAmount.compareTo(BigDecimal.ZERO) < 0) {
                    remainAmount = BigDecimal.ZERO;
                }
                limitDTO.setCurrentMonthRemainAmount(remainAmount);
            }
            paymentSceneLimitDTOS.add(limitDTO);
        }
        Comparator<EmployeePaymentSceneLimitDTO> comparatorByAppName = (o1, o2) -> Collator.getInstance(Locale.CHINA).compare(o1.getSceneAppName(), o2.getSceneAppName());
        Collections.sort(paymentSceneLimitDTOS, comparatorByAppName);
        response.setPaymentSceneLimitDTOS(paymentSceneLimitDTOS);
        return response;
    }

    private Map<String, EnterprisePaymentAuthEmployeePaySceneHistory> getEnterprisePaymentAuthEmployeePaySceneHistoryMap(Long organizationId, Long detailId, String payMonth) {
        List<EnterprisePaymentAuthEmployeePaySceneHistory> histories = enterprisePaymentAuthEmployeePaySceneHistoryProvider.listEnterprisePaymentAuthEmployeePaySceneHistoryByDetailId(UserContext.getCurrentNamespaceId(), organizationId, detailId, payMonth);

        Map<String, EnterprisePaymentAuthEmployeePaySceneHistory> map = new HashMap<>();
        if (CollectionUtils.isEmpty(histories)) {
            return map;
        }
        for (EnterprisePaymentAuthEmployeePaySceneHistory history : histories) {
            map.put(String.format("%d-%d", history.getDetailId(), history.getPaymentSceneAppId()), history);
        }
        return map;
    }

    @Override
    public ListEmployeePaymentLogsResponse listEmployeePaymentLogs(ListEmployeePaymentLogsCommand cmd) {
        Long userId = cmd.getUserId() != null ? cmd.getUserId() : UserContext.currentUserId();
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        int pageOffset = (cmd.getPageOffset() != null && cmd.getPageOffset() > 1) ? cmd.getPageOffset() : 1;
        int offset = (int) PaginationHelper.offsetFromPageOffset((long) pageOffset, pageSize);

        OrganizationMemberDetails memberDetail = organizationProvider.findOrganizationMemberDetailsByTargetIdAndOrgId(userId, cmd.getOrganizationId());
        List<EnterprisePaymentAuthPayLog> logs = enterprisePaymentAuthPayLogProvider.listEnterprisePaymentAuthPayLogByDetailId(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId(), memberDetail.getId(), offset, pageSize + 1);
        Integer nextPageOffset = null;
        if (logs != null && logs.size() > pageSize) {
            nextPageOffset = pageOffset + 1;
            logs.remove(pageSize);
        }
        ListEmployeePaymentLogsResponse response = new ListEmployeePaymentLogsResponse();
        response.setNextPageOffset(nextPageOffset);
        response.setEmployeePaymentLogDTOS(new ArrayList<>());
        if (CollectionUtils.isEmpty(logs)) {
            return response;
        }
        Map<Long, String> paymentSceneIdNameMap = getEnterprisePaymentSceneIdNameMap();
        for (EnterprisePaymentAuthPayLog log : logs) {
            EmployeePaymentLogDTO dto = new EmployeePaymentLogDTO();
            dto.setPaymentSceneAppId(log.getPaymentSceneAppId());
            dto.setPaymentSceneAppName(paymentSceneIdNameMap.get(log.getPaymentSceneAppId()));
            dto.setPayAmount(log.getPayAmmount());
            dto.setOrderNo(String.valueOf(log.getMerchantOrderId()));
            dto.setPayTime(log.getPayTime().getTime());
            response.getEmployeePaymentLogDTOS().add(dto);
        }
        return response;
    }

    @Override
    public ListEmployeePaymentLimitChangeLogsResponse listEmployeePaymentLimitChangeLogs(ListEmployeePaymentLimitChangeLogsCommand cmd) {
        Long userId = cmd.getUserId() != null ? cmd.getUserId() : UserContext.currentUserId();
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        int pageOffset = (cmd.getPageOffset() != null && cmd.getPageOffset() > 1) ? cmd.getPageOffset() : 1;
        int offset = (int) PaginationHelper.offsetFromPageOffset((long) pageOffset, pageSize);

        OrganizationMemberDetails memberDetail = organizationProvider.findOrganizationMemberDetailsByTargetIdAndOrgId(userId, cmd.getOrganizationId());
        List<EmployeePaymentLimitChangeLogsGroupDTO> groupDTOS = enterprisePaymentAuthEmployeeLimitChangeLogProvider.listEmployeePaymentLimitChangeLogsGroups(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId(), memberDetail.getId(), offset, pageSize + 1);
        Integer nextPageOffset = null;
        if (groupDTOS != null && groupDTOS.size() > pageSize) {
            nextPageOffset = pageOffset + 1;
            groupDTOS.remove(pageSize);
        }
        ListEmployeePaymentLimitChangeLogsResponse response = new ListEmployeePaymentLimitChangeLogsResponse();
        if (CollectionUtils.isEmpty(groupDTOS)) {
            return response;
        }
        List<EnterprisePaymentAuthEmployeeLimitChangeLog> logDetails = enterprisePaymentAuthEmployeeLimitChangeLogProvider.listEnterprisePaymentAuthEmployeeLimitChangeLog(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId(), memberDetail.getId(), getOperateNoList(groupDTOS));
        if (CollectionUtils.isEmpty(logDetails)) {
            return response;
        }

        response.setNextPageOffset(nextPageOffset);
        Map<Long, List<EmployeePaymentLimitChangeLogItemDTO>> groupMap = buildEmployeePaymentLimitChangeLogItemGroupByOperateNo(logDetails);
        Map<Long, String> userNameMap = new HashMap<>();
        for (EmployeePaymentLimitChangeLogsGroupDTO groupDTO : groupDTOS) {
            setOperatorName(userNameMap, groupDTO, cmd.getOrganizationId());

            List<EmployeePaymentLimitChangeLogItemDTO> groupItems = groupMap.get(groupDTO.getOperateNo());
            groupItems = sortChangeLogItemList(groupItems);
            if (!CollectionUtils.isEmpty(groupItems)) {
                groupDTO.setLogItemDTOS(groupItems);
            }
        }
        response.setLogsGroupDTOS(groupDTOS);
        return response;
    }


    private void setOperatorName(Map<Long, String> userNameMap, EmployeePaymentLimitChangeLogsGroupDTO groupDTO, Long organizationId) {
        if (groupDTO.getOperatorUid() == null) {
            groupDTO.setOperatorName("");
        }
        if (userNameMap.containsKey(groupDTO.getOperatorUid())) {
            groupDTO.setOperatorName(userNameMap.get(groupDTO.getOperatorUid()));
            return;
        }

        OrganizationMemberDetails operator = organizationProvider.findOrganizationMemberDetailsByTargetIdAndOrgId(groupDTO.getOperatorUid(), organizationId);
        if (operator != null) {
            userNameMap.put(operator.getTargetId(), operator.getContactName());
            groupDTO.setOperatorName(operator.getContactName());
            return;
        }
        UserInfo userInfo = userService.getUserInfo(groupDTO.getOperatorUid());
        if (userInfo != null) {
            userNameMap.put(userInfo.getId(), userInfo.getNickName());
            groupDTO.setOperatorName(userInfo.getNickName());
            return;
        }
        userNameMap.put(groupDTO.getOperatorUid(), "");
        groupDTO.setOperatorName("");
    }

    private List<Long> getOperateNoList(List<EmployeePaymentLimitChangeLogsGroupDTO> groupDTOS) {
        if (CollectionUtils.isEmpty(groupDTOS)) {
            return new ArrayList<>();
        }
        List<Long> noList = new ArrayList<>();
        for (EmployeePaymentLimitChangeLogsGroupDTO group : groupDTOS) {
            noList.add(group.getOperateNo());
        }
        return noList;
    }

    private Map<Long, List<EmployeePaymentLimitChangeLogItemDTO>> buildEmployeePaymentLimitChangeLogItemGroupByOperateNo(List<EnterprisePaymentAuthEmployeeLimitChangeLog> logDetails) {
        if (CollectionUtils.isEmpty(logDetails)) {
            return new HashMap<>();
        }

        Map<Long, List<EmployeePaymentLimitChangeLogItemDTO>> map = new HashMap<>();
        for (EnterprisePaymentAuthEmployeeLimitChangeLog log : logDetails) {
            List<EmployeePaymentLimitChangeLogItemDTO> group = map.get(log.getOperateNo());
            if (group == null) {
                group = new ArrayList<>();
                map.put(log.getOperateNo(), group);
            }
            EmployeePaymentLimitChangeLogItemDTO dto = new EmployeePaymentLimitChangeLogItemDTO();
            dto.setSourceName(log.getOperateSourceName());
            dto.setOperateType(log.getOperateType());
            dto.setChangedAmount(log.getChangedAmount());
            dto.setCurrentLimitAmount(log.getCurrentAmount());
            group.add(dto);
        }
        return map;
    }

    private Map<Long, String> getEnterprisePaymentSceneIdNameMap(Integer namespaceId) {
        ListServiceModuleAppsForEnterprisePayCommand cmd = new ListServiceModuleAppsForEnterprisePayCommand();
        cmd.setEnableEnterprisePayFlag(TrueOrFalseFlag.TRUE.getCode());
        cmd.setNamespaceId(namespaceId);
        ListServiceModuleAppsForEnterprisePayResponse response = serviceModuleAppService.listServiceModuleAppsForEnterprisePay(cmd);

        Map<Long, String> map = new HashMap<>();
        if (response == null || CollectionUtils.isEmpty(response.getApps())) {
            return map;
        }
        for (ServiceModuleAppDTO dto : response.getApps()) {
            //应用对应模块可能找不到
            if (dto.getName() != null) {
                map.put(dto.getOriginId(), dto.getName());
            }
        }
        return map;
    }

    private Map<Long, String> getEnterprisePaymentSceneIdNameMap() {
        return getEnterprisePaymentSceneIdNameMap(UserContext.getCurrentNamespaceId());
    }

    private List<Map.Entry<Long, String>>  sortMapByValue(Map<Long, String> map) {
        //两项及以上才排序
        if (map.keySet().size() > 1) {
            // 升序比较器
            Comparator<Map.Entry<Long, String>> valueComparator = (o1, o2) -> Collator.getInstance(Locale.CHINA).compare(o1.getValue(), o2.getValue());
            // map转换成list进行排序
            List<Map.Entry<Long, String>> list = new ArrayList<>(map.entrySet());
            // 排序
            Collections.sort(list, valueComparator);
            // 默认情况下，TreeMap对key进行升序排序
            return list;
        }
        return new ArrayList<>(map.entrySet());

    }

    private List<EmployeePaymentLimitChangeLogItemDTO> sortChangeLogItemList(List<EmployeePaymentLimitChangeLogItemDTO> groupItems) {
        if (CollectionUtils.isNotEmpty(groupItems) && groupItems.size() > 1) {
            String topicString = localeStringService.getLocalizedString(EnterprisePaymentAuthConstants.SOURCE_SCOPE,
                    EnterprisePaymentAuthConstants.SOURCE_LIMIT, EnterprisePaymentAuthConstants.LOCALE, "每月额度");
            Comparator<EmployeePaymentLimitChangeLogItemDTO> valueComparator = (o1, o2) -> {
            	//比较越小,排在越前面,所以让每月额度排最小
                if (o1.getSourceName().equals(topicString)) {
                    return -1;
                } else if (o2.getSourceName().equals(topicString)) {
                    return 1;
                } else {
                    return Collator.getInstance(Locale.CHINA).compare(o1.getSourceName(), o2.getSourceName());
                }
            };
            Collections.sort(groupItems, valueComparator);
        }
        return groupItems;
    }
    /**
     * 每月1号0点自动删除离职员工的额度配置
     */
    @Override
    @Scheduled(cron = "1 0 0,12 */1 * ?")
    public void autoDeleteEmployeePaymentLimit() {
        if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
            this.coordinationProvider.getNamedLock(CoordinationLocks.AUTO_DELETE_EMPLOYEE_PAYMENT_AUTH_LIMIT_OPERATE.getCode()).enter(() -> {
                String month = MONTH_DF.format(LocalDateTime.now());
                dbProvider.execute(transactionStatus -> {
                    enterprisePaymentAuthEmployeeLimitProvider.autoDeleteDismissEmployeePaymentAuthLimit(month);
                    enterprisePaymentAuthEmployeeLimitDetailProvider.autoDeleteDismissEmployeePaymentAuthLimitDetail(month);
                    return null;
                });
                return null;
            });
        }
    }

    @Override
    @Scheduled(cron = "1 0 0,2,4 * * ?")
    public void checkPaymentStatusScheduled() {
        if (RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
            coordinationProvider.getNamedLock(CoordinationLocks.CHECK_ENTERPRISE_PAYMENT_STATUS.getCode()).tryEnter(() -> {
                LOGGER.info("checkPaymentStatusScheduled begin");
                int loopCount = 0;
                // 取前一天0点到当前1小时前
                Timestamp betweenFrom = new Timestamp(DateStatisticHelper.getStartDateOfLastNDays(new Date(), 1, false).getTime());
                Timestamp betweenTo = new Timestamp(System.currentTimeMillis() - 60 * 60 * 1000);

                List<EnterprisePaymentAuthFrozenRequest> requests = enterprisePaymentAuthFrozenRequestProvider.findEnterprisePaymentAuthRequestsByFrozened(FETCH_SIZE, betweenFrom, betweenTo);
                boolean isProcess = !org.springframework.util.CollectionUtils.isEmpty(requests);
                GetPurchaseOrderByIdCommand getPurchaseOrderByIdCommand = new GetPurchaseOrderByIdCommand();
                PaymentAuthFrozenConfirmCommand confirmCommand = new PaymentAuthFrozenConfirmCommand();
                PaymentAuthUnFrozenCommand unFrozenCommand = new PaymentAuthUnFrozenCommand();
                int count = 0;
                while (isProcess) {
                    loopCount++;
                    count += requests.size();
                    requests.forEach(frozenRequest -> {
                        try {
                            getPurchaseOrderByIdCommand.setMerchantOrderId(frozenRequest.getMerchantOrderId());
                            PurchaseOrderDTO order = orderService.getPurchaseOrderById(getPurchaseOrderByIdCommand).getResponse();
                            if (order != null) {
                                if (PurchaseOrderStatus.COMPLETED.getCode() == order.getOrderStatus() || (PurchaseOrderStatus.NEW_CREATED.getCode() == order.getOrderStatus() && 29 == order.getPaymentType())) {
                                    confirmCommand.setNamespaceId(frozenRequest.getNamespaceId());
                                    confirmCommand.setOrganizationId(frozenRequest.getOrganizationId());
                                    confirmCommand.setUserId(frozenRequest.getUserId());
                                    confirmCommand.setMerchantOrderId(frozenRequest.getMerchantOrderId());
                                    confirmCommand.setPaymentType(order.getPaymentType());
                                    confirmCommand.setPayDateTime(order.getPaymentTime() != null ? order.getPaymentTime().getTime() : order.getCreateTime().getTime());
                                    confirmCommand.setPayAmount(frozenRequest.getFrozenAmmount().multiply(BIG_DECIMAL_100).longValue());
                                    confirmCommand.setAppId(frozenRequest.getPaymentSceneAppId());
                                    confirmCommand.setBizOrderNum(order.getBusinessOrderNumber());
                                    paymentAuthFrozenConfirm(confirmCommand, false, null);
                                } else {
                                    unFrozenCommand.setNamespaceId(frozenRequest.getNamespaceId());
                                    unFrozenCommand.setOrganizationId(frozenRequest.getOrganizationId());
                                    unFrozenCommand.setUserId(frozenRequest.getUserId());
                                    unFrozenCommand.setMerchantOrderId(frozenRequest.getMerchantOrderId());
                                    unFrozenCommand.setPaymentType(order.getPaymentType());
                                    unFrozenCommand.setPayAmount(frozenRequest.getFrozenAmmount().multiply(BIG_DECIMAL_100).longValue());
                                    unFrozenCommand.setAppId(frozenRequest.getPaymentSceneAppId());
                                    unFrozenCommand.setBizOrderNum(order.getBusinessOrderNumber());
                                    unFrozenCommand.setRefundFlag((byte) 0);
                                    paymentAuthUnFrozen(unFrozenCommand, false);
                                }
                            }
                        } catch (Exception e) {
                            // 忽略单个错误，以免影响到其它订单状态的确认
                            LOGGER.error("checkPaymentStatusScheduled error,merchantOrderId = {}", frozenRequest.getMerchantOrderId(), e);
                        }
                    });
                    // 当查询到无结果,或者循环次数超过100次（避免死循环）,就退出循环
                    requests = enterprisePaymentAuthFrozenRequestProvider.findEnterprisePaymentAuthRequestsByFrozened(FETCH_SIZE, betweenFrom, betweenTo);
                    if (org.springframework.util.CollectionUtils.isEmpty(requests) || loopCount > 100) {
                        isProcess = false;
                    }
                }
                LOGGER.info("checkPaymentStatusScheduled end, count = {}", count);
            });
        }
    }

    @Override
    public GetEmployeePaymentAuthStatusResponse getPaymentAuthStatus(GetEmployeePaymentAuthStatusCommand cmd) {
        if (cmd.getNamespaceId() == null || cmd.getOrganizationId() == null || cmd.getAppId() == null || cmd.getUserId() == null || cmd.getPayAmount() == null || cmd.getMerchantOrderId() == null) {
            throw RuntimeErrorException.errorWith(EnterprisePaymentAuthConstants.ERROR_SCOPE, EnterprisePaymentAuthConstants.ERROR_INVALID_PARAMETER, EnterprisePaymentAuthConstants.LOCALE, "ERROR_INVALID_PARAMETER");
        }
        verifySignature(cmd,cmd.getSignature());
        OrganizationMemberDetails memberDetail = getAndCheckOrganizationMemberDetails(cmd.getUserId(), cmd.getOrganizationId());
        EnterprisePaymentAuthEmployeeLimitDetail limitDetail = enterprisePaymentAuthEmployeeLimitDetailProvider.findEnterprisePaymentAuthEmployeeLimitDetailByDetailId(cmd.getNamespaceId(), cmd.getOrganizationId(), memberDetail.getId(), cmd.getAppId());
        if (limitDetail == null || limitDetail.getLimitAmount() == null || limitDetail.getLimitAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return new GetEmployeePaymentAuthStatusResponse();
        }
        String payMonth = MONTH_DF.format(LocalDateTime.now());
        EnterprisePaymentAuthFrozenRequest frozenRequest = enterprisePaymentAuthFrozenRequestProvider.findEnterprisePaymentAuthRequestByOrderId(cmd.getNamespaceId(), cmd.getOrganizationId(), cmd.getUserId(), cmd.getAppId(), cmd.getMerchantOrderId());
        BigDecimal frozenAmountBySameOrder = BigDecimal.ZERO;
        if (frozenRequest != null && FrozenStatus.FROZEN == FrozenStatus.fromCode(frozenRequest.getStatus()) && monthSF.get().format(frozenRequest.getCreateTime()).equals(payMonth)) {
            frozenAmountBySameOrder = frozenRequest.getFrozenAmmount();
        }
        BigDecimal remainAmountThisPaymentScene = limitDetail.getLimitAmount();
        EnterprisePaymentAuthEmployeePaySceneHistory history = enterprisePaymentAuthEmployeePaySceneHistoryProvider.findEnterprisePaymentAuthEmployeePaySceneHistoryByDetailId(cmd.getNamespaceId(), cmd.getOrganizationId(), memberDetail.getId(), cmd.getAppId(), payMonth);
        if (history != null && history.getUsedAmount() != null) {
            remainAmountThisPaymentScene = limitDetail.getLimitAmount().subtract(history.getUsedAmount()).add(frozenAmountBySameOrder);
        }
        if (remainAmountThisPaymentScene.compareTo(BigDecimal.ZERO) <= 0) {
            return new GetEmployeePaymentAuthStatusResponse();
        }
        EnterprisePaymentAuthEmployeeLimit limitOfMonth = enterprisePaymentAuthEmployeeLimitProvider.findEnterprisePaymentAuthEmployeeLimitByDetailId(cmd.getNamespaceId(), cmd.getOrganizationId(), memberDetail.getId());
        if (limitOfMonth == null || limitOfMonth.getLimitAmount() == null || limitOfMonth.getLimitAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return new GetEmployeePaymentAuthStatusResponse();
        }
        BigDecimal remainAmountThisMonth = limitOfMonth.getLimitAmount();
        EnterprisePaymentAuthEmployeePayHistory employeePayHistory = enterprisePaymentAuthEmployeePayHistoryProvider.findEnterprisePaymentAuthEmployeePayHistoryByDetailId(cmd.getNamespaceId(), cmd.getOrganizationId(), memberDetail.getId(), payMonth);
        if (employeePayHistory != null && employeePayHistory.getUsedAmount() != null) {
            remainAmountThisMonth = limitOfMonth.getLimitAmount().subtract(employeePayHistory.getUsedAmount()).add(frozenAmountBySameOrder);
        }
        // 实际可用余额
        Long actRemainAmount = Math.min(remainAmountThisMonth.multiply(BIG_DECIMAL_100).longValue(), remainAmountThisPaymentScene.multiply(BIG_DECIMAL_100).longValue());

        GetEmployeePaymentAuthStatusResponse response = new GetEmployeePaymentAuthStatusResponse();
        // 余额为负值 则设置成0
        response.setRemainAmount(Math.max(0, actRemainAmount));
        // 可用余额>= 支付金额  则authFlag = 1 ,并设置token
        response.setAuthFlag(response.getRemainAmount() >= cmd.getPayAmount() ? NormalFlag.YES.getCode() : NormalFlag.NO.getCode());

        return response;
    }

    private OrganizationMemberDetails getAndCheckOrganizationMemberDetails(Long userId, Long organizationId) {
        OrganizationMemberDetails memberDetail = organizationProvider.findOrganizationMemberDetailsByTargetIdAndOrgId(userId, organizationId);
        if (memberDetail == null) {
            LOGGER.error("OrganizationMemberDetails no found , userId = {} organizationId = {}", userId, organizationId);
            throw RuntimeErrorException.errorWith(EnterprisePaymentAuthConstants.ERROR_SCOPE, EnterprisePaymentAuthConstants.ERROR_EMPLOYEE_NO_FOUND, EnterprisePaymentAuthConstants.LOCALE, "ERROR_EMPLOYEE_NO_FOUND");
        }
        if (EmployeeStatus.DISMISSAL == EmployeeStatus.fromCode(memberDetail.getEmployeeStatus())) {
            LOGGER.error("OrganizationMemberDetails DISMISSAL , userId = {} organizationId = {}", userId, organizationId);
            throw RuntimeErrorException.errorWith(EnterprisePaymentAuthConstants.ERROR_SCOPE, EnterprisePaymentAuthConstants.ERROR_EMPLOYEE_DISMISSAL, EnterprisePaymentAuthConstants.LOCALE, "ERROR_EMPLOYEE_DISMISSAL");
        }
        return memberDetail;
    }

    @Override
    public PaymentAuthCheckAndFrozenResponse checkAndPaymentFrozen(PaymentAuthCheckAndFrozenCommand cmd) {
        // STEP-1 : 参数校验
        if (cmd.getPayAmount() == null || cmd.getMerchantOrderId() == null || cmd.getNamespaceId() == null || cmd.getOrganizationId() == null || cmd.getAppId() == null || cmd.getUserId() == null) {
            throw RuntimeErrorException.errorWith(EnterprisePaymentAuthConstants.ERROR_SCOPE, EnterprisePaymentAuthConstants.ERROR_INVALID_PARAMETER, EnterprisePaymentAuthConstants.LOCALE, "ERROR_INVALID_PARAMETER");
        }
        verifySignature(cmd,cmd.getSignature());
        OrganizationMemberDetails memberDetail = getAndCheckOrganizationMemberDetails(cmd.getUserId(), cmd.getOrganizationId());

        return this.coordinationProvider.getNamedLock(CoordinationLocks.ENTERPRISE_PAYMENT_AUTH_PAY_AMOUNT_LOCK.getCode() + "-" + cmd.getOrganizationId() + "-" + cmd.getAppId()).enter(() -> {
            // STEP-2 : 确认该订单是否存在冻结记录，且金额是否一致，是则直接返回结果
            EnterprisePaymentAuthFrozenRequest existFrozenRequest = getAndValidExistFrozen(cmd, memberDetail);
            if (existFrozenRequest != null) {
                return new PaymentAuthCheckAndFrozenResponse(NormalFlag.YES.getCode(), null);
            }
            // STEP-3 : 查询鉴权
            EnterprisePaymentAuthEmployeeLimitDetail limitDetail = enterprisePaymentAuthEmployeeLimitDetailProvider.findEnterprisePaymentAuthEmployeeLimitDetailByDetailId(cmd.getNamespaceId(), cmd.getOrganizationId(), memberDetail.getId(), cmd.getAppId());
            if (limitDetail == null || limitDetail.getLimitAmount() == null || limitDetail.getLimitAmount().compareTo(BigDecimal.ZERO) <= 0) {
                return new PaymentAuthCheckAndFrozenResponse(NormalFlag.NO.getCode(), EnterprisePaymentAuthConstants.ENTERPRISE_PAYMENT_UN_AUTH_CODE);
            }
            String currentMonth = MONTH_DF.format(LocalDateTime.now());
            EnterprisePaymentAuthEmployeePaySceneHistory history = enterprisePaymentAuthEmployeePaySceneHistoryProvider.findEnterprisePaymentAuthEmployeePaySceneHistoryByDetailId(cmd.getNamespaceId(), cmd.getOrganizationId(), memberDetail.getId(), cmd.getAppId(), currentMonth);
            BigDecimal remainAmount = limitDetail.getLimitAmount();
            if (history != null && history.getUsedAmount() != null) {
                remainAmount = limitDetail.getLimitAmount().subtract(history.getUsedAmount());
            }
            if (remainAmount.multiply(BIG_DECIMAL_100).longValue() < cmd.getPayAmount()) {
                return new PaymentAuthCheckAndFrozenResponse(NormalFlag.NO.getCode(), EnterprisePaymentAuthConstants.ENTERPRISE_PAYMENT_AMOUNT_EXCEEDS_LIMIT);
            }
            EnterprisePaymentAuthEmployeeLimit employeeLimit = enterprisePaymentAuthEmployeeLimitProvider.findEnterprisePaymentAuthEmployeeLimitByDetailId(cmd.getNamespaceId(), cmd.getOrganizationId(), memberDetail.getId());
            if (employeeLimit == null || employeeLimit.getLimitAmount() == null || employeeLimit.getLimitAmount().compareTo(BigDecimal.ZERO) <= 0) {
                return new PaymentAuthCheckAndFrozenResponse(NormalFlag.NO.getCode(), EnterprisePaymentAuthConstants.ENTERPRISE_PAYMENT_UN_AUTH_CODE);
            }
            remainAmount = employeeLimit.getLimitAmount();
            EnterprisePaymentAuthEmployeePayHistory employeePayHistory = enterprisePaymentAuthEmployeePayHistoryProvider.findEnterprisePaymentAuthEmployeePayHistoryByDetailId(cmd.getNamespaceId(), cmd.getOrganizationId(), memberDetail.getId(), currentMonth);
            if (employeePayHistory != null && employeePayHistory.getUsedAmount() != null) {
                remainAmount = employeeLimit.getLimitAmount().subtract(employeePayHistory.getUsedAmount());
            }
            if (remainAmount.multiply(BIG_DECIMAL_100).longValue() < cmd.getPayAmount()) {
                return new PaymentAuthCheckAndFrozenResponse(NormalFlag.NO.getCode(), EnterprisePaymentAuthConstants.ENTERPRISE_PAYMENT_EMPLOYEE_TOTAL_EXCEEDS_LIMIT);
            }
            EnterprisePaymentAuthSceneLimit enterprisePaymentAuthSceneLimit = enterprisePaymentAuthSceneLimitProvider.findEnterprisePaymentAuthSceneLimitByAppId(cmd.getOrganizationId(), cmd.getNamespaceId(), cmd.getAppId());
            // enterprisePaymentAuthSceneLimit==null || enterprisePaymentAuthSceneLimit.getLimitAmount() == null 表示不限额
            if (enterprisePaymentAuthSceneLimit != null && enterprisePaymentAuthSceneLimit.getLimitAmount() != null && enterprisePaymentAuthSceneLimit.getLimitAmount().compareTo(BigDecimal.ZERO) <= 0) {
                return new PaymentAuthCheckAndFrozenResponse(NormalFlag.NO.getCode(), EnterprisePaymentAuthConstants.ENTERPRISE_PAYMENT_ORGANIZATION_TOTAL_EXCEEDS_LIMIT);
            }

            EnterprisePaymentAuthScenePayHistory enterprisePaymentAuthScenePayHistory = enterprisePaymentAuthScenePayHistoryProvider.findEnterprisePaymentAuthScenePayHistoryByAppId(cmd.getOrganizationId(), cmd.getNamespaceId(), cmd.getAppId(), currentMonth);
            // enterprisePaymentAuthSceneLimit.getLimitAmount() == null 表示不限额
            if (enterprisePaymentAuthSceneLimit != null && enterprisePaymentAuthSceneLimit.getLimitAmount() != null) {
                remainAmount = enterprisePaymentAuthSceneLimit.getLimitAmount();
                if (enterprisePaymentAuthScenePayHistory != null && enterprisePaymentAuthScenePayHistory.getUsedAmount() != null) {
                    remainAmount = enterprisePaymentAuthSceneLimit.getLimitAmount().subtract(enterprisePaymentAuthScenePayHistory.getUsedAmount());
                }
                if (remainAmount.multiply(BIG_DECIMAL_100).longValue() < cmd.getPayAmount()) {
                    return new PaymentAuthCheckAndFrozenResponse(NormalFlag.NO.getCode(), EnterprisePaymentAuthConstants.ENTERPRISE_PAYMENT_ORGANIZATION_TOTAL_EXCEEDS_LIMIT);
                }
            }

            // STEP-4 : 如果额度富余，则锁定额度
            dbProvider.execute(transactionStatus -> {
                BigDecimal payAmount = BigDecimal.valueOf(cmd.getPayAmount()).divide(BIG_DECIMAL_100);
                createOrUpdateEmployeePaySceneHistory(limitDetail, history, payAmount, currentMonth);
                createOrUpdateEmployeePayHistory(employeeLimit, employeePayHistory, payAmount, currentMonth);
                createOrUpdateEnterprisePaymentAuthScenePayHistory(limitDetail, enterprisePaymentAuthScenePayHistory, payAmount, currentMonth);
                createEnterprisePaymentAuthRequest(cmd, memberDetail);
                return null;
            });

            return new PaymentAuthCheckAndFrozenResponse(NormalFlag.YES.getCode(), null);
        }).first();
    }

    private EnterprisePaymentAuthFrozenRequest getAndValidExistFrozen(PaymentAuthCheckAndFrozenCommand cmd, OrganizationMemberDetails memberDetail) {
        EnterprisePaymentAuthFrozenRequest frozenRequest = enterprisePaymentAuthFrozenRequestProvider.findEnterprisePaymentAuthRequestByOrderId(cmd.getNamespaceId(), cmd.getOrganizationId(), cmd.getUserId(), cmd.getAppId(), cmd.getMerchantOrderId());
        if (frozenRequest == null) {
            return null;
        }
        if (FrozenStatus.FROZEN_CONFIRM == FrozenStatus.fromCode(frozenRequest.getStatus())) {
            LOGGER.error("pay status error,cmd = {}", StringHelper.toJsonString(cmd));
            throw RuntimeErrorException.errorWith(EnterprisePaymentAuthConstants.ERROR_SCOPE, EnterprisePaymentAuthConstants.ERROR_INVALID_PARAMETER, "ERROR_INVALID_PARAMETER");
        }
        if (frozenRequest.getFrozenAmmount().compareTo(BigDecimal.valueOf(cmd.getPayAmount()).divide(BIG_DECIMAL_100)) != 0) {
            BigDecimal frozenAmount = frozenRequest.getFrozenAmmount();
            String frozenMonth = monthSF.get().format(frozenRequest.getCreateTime());
            enterprisePaymentAuthFrozenRequestProvider.deleteEnterprisePaymentAuthRequest(frozenRequest);
            enterprisePaymentAuthScenePayHistoryProvider.decrUsedAmountAndPayCount(cmd.getNamespaceId(), cmd.getOrganizationId(), cmd.getAppId(), frozenAmount, frozenMonth);
            enterprisePaymentAuthEmployeePayHistoryProvider.decrUsedAmountAndPayCount(cmd.getNamespaceId(), cmd.getOrganizationId(), memberDetail.getId(), frozenAmount, frozenMonth);
            enterprisePaymentAuthEmployeePaySceneHistoryProvider.decrUsedAmountAndPayCount(cmd.getNamespaceId(), cmd.getOrganizationId(), memberDetail.getId(), cmd.getAppId(), frozenAmount, frozenMonth);
            return null;
        }
        return frozenRequest;
    }

    private void createEnterprisePaymentAuthRequest(PaymentAuthCheckAndFrozenCommand cmd, OrganizationMemberDetails memberDetail) {
        EnterprisePaymentAuthFrozenRequest frozenRequest = new EnterprisePaymentAuthFrozenRequest();
        frozenRequest.setNamespaceId(cmd.getNamespaceId());
        frozenRequest.setOrganizationId(cmd.getOrganizationId());
        frozenRequest.setUserId(cmd.getUserId());
        frozenRequest.setDetailId(memberDetail.getId());
        frozenRequest.setPaymentSceneAppId(cmd.getAppId());
        frozenRequest.setFrozenAmmount(BigDecimal.valueOf(cmd.getPayAmount()).divide(BIG_DECIMAL_100));
        frozenRequest.setMerchantOrderId(cmd.getMerchantOrderId());
        frozenRequest.setStatus(FrozenStatus.FROZEN.getCode());
        enterprisePaymentAuthFrozenRequestProvider.createEnterprisePaymentAuthRequest(frozenRequest);
    }

    private void createOrUpdateEnterprisePaymentAuthScenePayHistory(EnterprisePaymentAuthEmployeeLimitDetail limitDetail, EnterprisePaymentAuthScenePayHistory enterprisePaymentAuthScenePayHistory, BigDecimal payAmount, String currentMonth) {
        if (enterprisePaymentAuthScenePayHistory == null) {
            enterprisePaymentAuthScenePayHistory = new EnterprisePaymentAuthScenePayHistory();
            enterprisePaymentAuthScenePayHistory.setNamespaceId(limitDetail.getNamespaceId());
            enterprisePaymentAuthScenePayHistory.setOrganizationId(limitDetail.getOrganizationId());
            enterprisePaymentAuthScenePayHistory.setPaymentSceneAppId(limitDetail.getPaymentSceneAppId());
            enterprisePaymentAuthScenePayHistory.setUsedAmount(payAmount);
            enterprisePaymentAuthScenePayHistory.setPayMonth(currentMonth);
            enterprisePaymentAuthScenePayHistory.setPayCount(1);
            enterprisePaymentAuthScenePayHistory.setCreatorUid(0L);
            enterprisePaymentAuthScenePayHistoryProvider.createEnterprisePaymentAuthScenePayHistory(enterprisePaymentAuthScenePayHistory);
        } else {
            enterprisePaymentAuthScenePayHistory.setUsedAmount(enterprisePaymentAuthScenePayHistory.getUsedAmount().add(payAmount));
            enterprisePaymentAuthScenePayHistory.setPayCount(enterprisePaymentAuthScenePayHistory.getPayCount() + 1);
            enterprisePaymentAuthScenePayHistory.setOperatorUid(0L);
            enterprisePaymentAuthScenePayHistoryProvider.updateEnterprisePaymentAuthScenePayHistory(enterprisePaymentAuthScenePayHistory);
        }
    }

    private void createOrUpdateEmployeePayHistory(EnterprisePaymentAuthEmployeeLimit employeeLimit, EnterprisePaymentAuthEmployeePayHistory employeePayHistory, BigDecimal payAmount, String currentMonth) {
        if (employeePayHistory == null) {
            employeePayHistory = new EnterprisePaymentAuthEmployeePayHistory();
            employeePayHistory.setNamespaceId(employeeLimit.getNamespaceId());
            employeePayHistory.setOrganizationId(employeeLimit.getOrganizationId());
            employeePayHistory.setUserId(employeeLimit.getUserId());
            employeePayHistory.setDetailId(employeeLimit.getDetailId());
            employeePayHistory.setUsedAmount(payAmount);
            employeePayHistory.setPayMonth(currentMonth);
            employeePayHistory.setPayCount(1);
            employeePayHistory.setCreatorUid(employeePayHistory.getUserId());
            enterprisePaymentAuthEmployeePayHistoryProvider.createEnterprisePaymentAuthEmployeePayHistory(employeePayHistory);
        } else {
            employeePayHistory.setUsedAmount(employeePayHistory.getUsedAmount().add(payAmount));
            employeePayHistory.setPayCount(employeePayHistory.getPayCount() + 1);
            employeePayHistory.setOperatorUid(employeePayHistory.getUserId());
            enterprisePaymentAuthEmployeePayHistoryProvider.updateEnterprisePaymentAuthEmployeePayHistory(employeePayHistory);
        }
    }

    private void createOrUpdateEmployeePaySceneHistory(EnterprisePaymentAuthEmployeeLimitDetail limitDetail, EnterprisePaymentAuthEmployeePaySceneHistory history, BigDecimal payAmount, String currentMonth) {
        if (history == null) {
            history = new EnterprisePaymentAuthEmployeePaySceneHistory();
            history.setNamespaceId(limitDetail.getNamespaceId());
            history.setOrganizationId(limitDetail.getOrganizationId());
            history.setUserId(limitDetail.getUserId());
            history.setDetailId(limitDetail.getDetailId());
            history.setPaymentSceneAppId(limitDetail.getPaymentSceneAppId());
            history.setUsedAmount(payAmount);
            history.setPayMonth(currentMonth);
            history.setPayCount(1);
            history.setCreatorUid(history.getUserId());
            enterprisePaymentAuthEmployeePaySceneHistoryProvider.createEnterprisePaymentAuthEmployeePaySceneHistory(history);
        } else {
            history.setUsedAmount(history.getUsedAmount().add(payAmount));
            history.setPayCount(history.getPayCount() + 1);
            history.setOperatorUid(history.getUserId());
            enterprisePaymentAuthEmployeePaySceneHistoryProvider.updateEnterprisePaymentAuthEmployeePaySceneHistory(history);
        }
    }

    @Override
    public ListPaymentScenesResponse listPaymentScenes() {
        ListPaymentScenesResponse response = new ListPaymentScenesResponse();
        response.setScenes(new ArrayList<>());
        Map<Long, String> map = getEnterprisePaymentSceneIdNameMap();
        List<Map.Entry<Long, String>> sortedList = sortMapByValue(map);
        if (CollectionUtils.isNotEmpty(sortedList)) {
            for (Map.Entry<Long, String> entry : sortedList) {
                SceneSimpleDTO dto = new SceneSimpleDTO();
                dto.setSceneAppId(entry.getKey());
                dto.setSceneAppName(entry.getValue());
                response.getScenes().add(dto);
            }
        }
        return response;
    }

    @Override
    public void paymentAuthUnFrozen(PaymentAuthUnFrozenCommand cmd, boolean verifySignature) {
        if (cmd.getNamespaceId() == null || cmd.getOrganizationId() == null || cmd.getAppId() == null || cmd.getUserId() == null || cmd.getPayAmount() == null || cmd.getMerchantOrderId() == null || cmd.getPaymentType() == null) {
            throw RuntimeErrorException.errorWith(EnterprisePaymentAuthConstants.ERROR_SCOPE, EnterprisePaymentAuthConstants.ERROR_INVALID_PARAMETER, EnterprisePaymentAuthConstants.LOCALE, "ERROR_INVALID_PARAMETER");
        }
        if (verifySignature) {
            verifySignature(cmd, cmd.getSignature());
        }
        OrganizationMemberDetails memberDetail = getAndCheckOrganizationMemberDetails(cmd.getUserId(), cmd.getOrganizationId());
        this.coordinationProvider.getNamedLock(CoordinationLocks.ENTERPRISE_PAYMENT_AUTH_FROZEN.getCode() + "-" + cmd.getOrganizationId() + "-" + cmd.getAppId()).enter(() -> {
            EnterprisePaymentAuthFrozenRequest frozenRequest = enterprisePaymentAuthFrozenRequestProvider.findEnterprisePaymentAuthRequestByOrderId(cmd.getNamespaceId(), cmd.getOrganizationId(), cmd.getUserId(), cmd.getAppId(), cmd.getMerchantOrderId());
            if (frozenRequest == null || FrozenStatus.UN_FROZEN == FrozenStatus.fromCode(frozenRequest.getStatus())) {
                LOGGER.error("no frozen request found , cmd = {}", StringHelper.toJsonString(cmd));
                return null;
            }
            frozenRequest.setStatus(FrozenStatus.UN_FROZEN.getCode());
            dbProvider.execute(transactionStatus -> {
                BigDecimal frozenAmount = BigDecimal.valueOf(cmd.getPayAmount()).divide(BIG_DECIMAL_100);
                String frozenMonth = monthSF.get().format(frozenRequest.getCreateTime());
                if (NormalFlag.YES == NormalFlag.fromCode(cmd.getRefundFlag())) {
                    EnterprisePaymentAuthPayLog refundLog = new EnterprisePaymentAuthPayLog(cmd.getNamespaceId(), cmd.getOrganizationId(), memberDetail.getTargetId(), memberDetail.getId(), cmd.getAppId());
                    refundLog.setPayAmmount(frozenAmount.multiply(BigDecimal.valueOf(-1)));
                    refundLog.setMerchantOrderId(cmd.getMerchantOrderId());
                    refundLog.setBizOrderNum(cmd.getBizOrderNum());
                    refundLog.setPaymentType(cmd.getPaymentType().byteValue());
                    refundLog.setIsRefund(NormalFlag.YES.getCode());
                    refundLog.setPayTime(new Timestamp(new Date().getTime()));
                    enterprisePaymentAuthPayLogProvider.createEnterprisePaymentAuthPayLog(refundLog);
                    enterprisePaymentAuthSceneLimitProvider.incrHistoricalTotalPayAmountAndPayCount(cmd.getNamespaceId(), cmd.getOrganizationId(), cmd.getAppId(), frozenAmount.multiply(BigDecimal.valueOf(-1)), -1);
                    enterprisePaymentAuthEmployeeLimitProvider.incrHistoricalTotalPayAmountAndPayCount(cmd.getNamespaceId(), cmd.getOrganizationId(), memberDetail.getId(), frozenAmount.multiply(BigDecimal.valueOf(-1)), -1);
                    enterprisePaymentAuthEmployeeLimitDetailProvider.incrHistoricalTotalPayAmountAndPayCount(cmd.getNamespaceId(), cmd.getOrganizationId(), memberDetail.getId(), cmd.getAppId(), frozenAmount.multiply(BigDecimal.valueOf(-1)), -1);
                }
                enterprisePaymentAuthFrozenRequestProvider.updateEnterprisePaymentAuthRequest(frozenRequest);
                enterprisePaymentAuthScenePayHistoryProvider.decrUsedAmountAndPayCount(cmd.getNamespaceId(), cmd.getOrganizationId(), cmd.getAppId(), frozenAmount, frozenMonth);
                enterprisePaymentAuthEmployeePayHistoryProvider.decrUsedAmountAndPayCount(cmd.getNamespaceId(), cmd.getOrganizationId(), memberDetail.getId(), frozenAmount, frozenMonth);
                enterprisePaymentAuthEmployeePaySceneHistoryProvider.decrUsedAmountAndPayCount(cmd.getNamespaceId(), cmd.getOrganizationId(), memberDetail.getId(), cmd.getAppId(), frozenAmount, frozenMonth);
                return null;
            });
            return null;
        });
    }

    @Override
    public void paymentAuthFrozenConfirm(PaymentAuthFrozenConfirmCommand cmd, boolean verifySignature, HttpServletRequest request) {
        if (cmd.getNamespaceId() == null || cmd.getOrganizationId() == null || cmd.getAppId() == null || cmd.getUserId() == null || cmd.getPayAmount() == null || cmd.getMerchantOrderId() == null || cmd.getPayDateTime() == null || cmd.getPaymentType() == null) {
            throw RuntimeErrorException.errorWith(EnterprisePaymentAuthConstants.ERROR_SCOPE, EnterprisePaymentAuthConstants.ERROR_INVALID_PARAMETER, EnterprisePaymentAuthConstants.LOCALE, "ERROR_INVALID_PARAMETER");
        }
        if (verifySignature) {
            verifySignature(cmd, cmd.getSignature());
        }
        OrganizationMemberDetails memberDetail = getAndCheckOrganizationMemberDetails(cmd.getUserId(), cmd.getOrganizationId());
        BigDecimal payAmount = BigDecimal.valueOf(cmd.getPayAmount()).divide(BIG_DECIMAL_100);
        this.coordinationProvider.getNamedLock(CoordinationLocks.ENTERPRISE_PAYMENT_AUTH_FROZEN.getCode() + "-" + cmd.getOrganizationId() + "-" + cmd.getAppId()).enter(() -> {
            EnterprisePaymentAuthFrozenRequest frozenRequest = enterprisePaymentAuthFrozenRequestProvider.findEnterprisePaymentAuthRequestByOrderId(cmd.getNamespaceId(), cmd.getOrganizationId(), cmd.getUserId(), cmd.getAppId(), cmd.getMerchantOrderId());
            if (frozenRequest == null || FrozenStatus.FROZEN != FrozenStatus.fromCode(frozenRequest.getStatus())) {
                LOGGER.error("no frozen request found , cmd = {}", StringHelper.toJsonString(cmd));
                return null;
            }
            frozenRequest.setStatus(FrozenStatus.FROZEN_CONFIRM.getCode());

            EnterprisePaymentAuthPayLog payLog = new EnterprisePaymentAuthPayLog(cmd.getNamespaceId(), cmd.getOrganizationId(), memberDetail.getTargetId(), memberDetail.getId(), cmd.getAppId());
            payLog.setPayAmmount(payAmount);
            payLog.setMerchantOrderId(cmd.getMerchantOrderId());
            payLog.setBizOrderNum(cmd.getBizOrderNum());
            payLog.setPaymentType(cmd.getPaymentType().byteValue());
            payLog.setIsRefund(NormalFlag.NO.getCode());
            payLog.setPayTime(new Timestamp(cmd.getPayDateTime()));
            dbProvider.execute(transactionStatus -> {
                enterprisePaymentAuthPayLogProvider.createEnterprisePaymentAuthPayLog(payLog);
                enterprisePaymentAuthFrozenRequestProvider.updateEnterprisePaymentAuthRequest(frozenRequest);
                initEnterprisePaymentAuthSceneLimitIfNotExist(cmd.getNamespaceId(), cmd.getOrganizationId(), cmd.getAppId());
                enterprisePaymentAuthSceneLimitProvider.incrHistoricalTotalPayAmountAndPayCount(cmd.getNamespaceId(), cmd.getOrganizationId(), cmd.getAppId(), payLog.getPayAmmount(), 1);
                enterprisePaymentAuthEmployeeLimitProvider.incrHistoricalTotalPayAmountAndPayCount(cmd.getNamespaceId(), cmd.getOrganizationId(), memberDetail.getId(), payLog.getPayAmmount(), 1);
                enterprisePaymentAuthEmployeeLimitDetailProvider.incrHistoricalTotalPayAmountAndPayCount(cmd.getNamespaceId(), cmd.getOrganizationId(), memberDetail.getId(), cmd.getAppId(), payLog.getPayAmmount(), 1);
                return null;
            });
            return null;
        });
        if (request == null) {
            return;
        }
        String homeUrl = request.getHeader("Host");
        ExecutorUtil.submit(() -> {
            sendMsgAfterPaySuccess(cmd.getPayDateTime(), payAmount, cmd.getAppId(), homeUrl, memberDetail);
        });
    }

    @Override
    public void testPaymentCallback(TestPaymentCallbackCommand cmd, HttpServletRequest request) {
        GetEmployeePaymentAuthStatusCommand remainStatusCommand = ConvertHelper.convert(cmd, GetEmployeePaymentAuthStatusCommand.class);
        remainStatusCommand.setAppId(cmd.getPaymentSceneAppId());
        remainStatusCommand.setMerchantOrderId(System.currentTimeMillis());
        remainStatusCommand.setAppkey(PaySettings.getAppKey());
        Map<String, String> remainStatusCommandParams = new HashMap<>();
        StringHelper.toStringMap(null, remainStatusCommand, remainStatusCommandParams);
        remainStatusCommand.setSignature(SignatureHelper.computeSignature(remainStatusCommandParams, PaySettings.getSecretKey()));
        GetEmployeePaymentAuthStatusResponse remainStatusResponse = getPaymentAuthStatus(remainStatusCommand);
        if (NormalFlag.YES != NormalFlag.fromCode(remainStatusResponse.getAuthFlag())) {
            throw RuntimeErrorException.errorWith(EnterprisePaymentAuthConstants.ERROR_SCOPE, EnterprisePaymentAuthConstants.ERROR_PAYMENT_AUTH_LESS, EnterprisePaymentAuthConstants.LOCALE, "ERROR_PAYMENT_AUTH_LESS");
        }
        PaymentAuthCheckAndFrozenCommand checkCmd = ConvertHelper.convert(cmd, PaymentAuthCheckAndFrozenCommand.class);
        checkCmd.setSignature(null);
        checkCmd.setAppId(cmd.getPaymentSceneAppId());
        checkCmd.setMerchantOrderId(remainStatusCommand.getMerchantOrderId());
        checkCmd.setAppkey(PaySettings.getAppKey());
        Map<String, String> checkCmdParams = new HashMap<>();
        StringHelper.toStringMap(null, checkCmd, checkCmdParams);
        checkCmd.setSignature(SignatureHelper.computeSignature(checkCmdParams, PaySettings.getSecretKey()));
        PaymentAuthCheckAndFrozenResponse checkResponse = checkAndPaymentFrozen(checkCmd);
        if (NormalFlag.YES != NormalFlag.fromCode(checkResponse.getAuthFlag())) {
            throw RuntimeErrorException.errorWith(EnterprisePaymentAuthConstants.ERROR_SCOPE, EnterprisePaymentAuthConstants.ERROR_PAYMENT_AUTH_LESS, EnterprisePaymentAuthConstants.LOCALE, "ERROR_PAYMENT_AUTH_LESS");
        }

        if (NormalFlag.YES == NormalFlag.fromCode(cmd.getPayStatus())) {
            PaymentAuthFrozenConfirmCommand confirmCommand = ConvertHelper.convert(checkCmd, PaymentAuthFrozenConfirmCommand.class);
            confirmCommand.setSignature(null);
            confirmCommand.setPaymentType(1);
            confirmCommand.setBizOrderNum(String.valueOf(System.currentTimeMillis()));
            confirmCommand.setPayDateTime(System.currentTimeMillis());
            confirmCommand.setAppkey(PaySettings.getAppKey());
            Map<String, String> confirmCommandParams = new HashMap<>();
            StringHelper.toStringMap(null, confirmCommand, confirmCommandParams);
            confirmCommand.setSignature(SignatureHelper.computeSignature(confirmCommandParams, PaySettings.getSecretKey()));
            paymentAuthFrozenConfirm(confirmCommand, true, request);
        } else {
            PaymentAuthUnFrozenCommand unFrozenCommand = ConvertHelper.convert(checkCmd, PaymentAuthUnFrozenCommand.class);
            unFrozenCommand.setSignature(null);
            unFrozenCommand.setPaymentType(1);
            unFrozenCommand.setBizOrderNum(String.valueOf(System.currentTimeMillis()));
            unFrozenCommand.setAppkey(PaySettings.getAppKey());
            Map<String, String> unFrozenCommandParams = new HashMap<>();
            StringHelper.toStringMap(null, unFrozenCommand, unFrozenCommandParams);
            unFrozenCommand.setSignature(SignatureHelper.computeSignature(unFrozenCommandParams, PaySettings.getSecretKey()));
            paymentAuthUnFrozen(unFrozenCommand, true);
        }
    }

    private void verifySignature(Object cmd, String signature) {
        Map<String, String> params = new HashMap();
        StringHelper.toStringMap(null, cmd, params);
        params.remove("signature");
        boolean verify = SignatureHelper.verifySignature(params, PaySettings.getSecretKey(), signature);
        if (!verify) {
            LOGGER.error("cmd = [" + StringHelper.toJsonString(cmd) + "] is Forbidden");
            throw RuntimeErrorException.errorWith(EnterprisePaymentAuthConstants.ERROR_SCOPE, EnterprisePaymentAuthConstants.ERROR_SIGNATURE_WRONG, EnterprisePaymentAuthConstants.LOCALE, "Forbidden");
        }
    }
}