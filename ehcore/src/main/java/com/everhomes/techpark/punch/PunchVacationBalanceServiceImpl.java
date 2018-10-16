package com.everhomes.techpark.punch;

import com.everhomes.approval.ApprovalCategory;
import com.everhomes.approval.ApprovalServiceConstants;
import com.everhomes.archives.ArchivesService;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.filedownload.TaskService;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.organization.ExecuteImportTaskCallback;
import com.everhomes.organization.ImportFileService;
import com.everhomes.organization.ImportFileTask;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.common.OfficialActionData;
import com.everhomes.rest.common.Router;
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
import com.everhomes.rest.organization.ImportFileErrorType;
import com.everhomes.rest.organization.ImportFileResultLog;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.rest.organization.ImportFileTaskType;
import com.everhomes.rest.techpark.punch.NormalFlag;
import com.everhomes.rest.techpark.punch.PunchServiceErrorCode;
import com.everhomes.rest.techpark.punch.admin.BatchUpdateVacationBalancesCommand;
import com.everhomes.rest.techpark.punch.admin.ExportVacationBalancesCommand;
import com.everhomes.rest.techpark.punch.admin.ImportVacationBalancesCommand;
import com.everhomes.rest.techpark.punch.admin.ListVacationBalanceLogsCommand;
import com.everhomes.rest.techpark.punch.admin.ListVacationBalanceLogsResponse;
import com.everhomes.rest.techpark.punch.admin.ListVacationBalancesCommand;
import com.everhomes.rest.techpark.punch.admin.ListVacationBalancesResponse;
import com.everhomes.rest.techpark.punch.admin.UpdateVacationBalancesCommand;
import com.everhomes.rest.techpark.punch.admin.VacationBalanceDTO;
import com.everhomes.rest.techpark.punch.admin.VacationBalanceLogDTO;
import com.everhomes.server.schema.Tables;
import com.everhomes.sms.DateUtil;
import com.everhomes.socialSecurity.SocialSecurityService;
import com.everhomes.techpark.punch.utils.PunchDayParseUtils;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RouterBuilder;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.Tuple;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PunchVacationBalanceServiceImpl implements PunchVacationBalanceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PunchVacationBalanceServiceImpl.class);
    @Autowired
    private PunchVacationBalanceProvider punchVacationBalanceProvider;
    @Autowired
    private ImportFileService importFileService;
    @Autowired
    private CoordinationProvider coordinationProvider;
    @Autowired
    private LocaleTemplateService localeTemplateService;
    @Autowired
    private OrganizationProvider organizationProvider;
    @Autowired
    private PunchVacationBalanceLogProvider punchVacationBalanceLogProvider;
    @Autowired
    private DbProvider dbProvider;
    @Autowired
    private ConfigurationProvider configurationProvider;
    @Autowired
    private SocialSecurityService socialSecurityService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private LocaleStringService localeStringService;
    @Autowired
    private ArchivesService archivesService;
    @Autowired
    private MessagingService messagingService;


    private ThreadLocal<SimpleDateFormat> datetimeSF = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    @Override
    public void updateVacationBalancesAndSendMessage(UpdateVacationBalancesCommand cmd) {
        PunchVacationBalance punchVacationBalance = updateVacationBalances(cmd);
        String notificationContent = buildVacationBalanceChangedNotificationContent(cmd.getAnnualLeaveBalanceCorrection(), cmd.getOvertimeCompensationBalanceCorrection(),
                null, punchVacationBalance, null, ApprovalServiceConstants.VACATION_BALANCE_CHANGED_NOTIFICATION_CONTENT_BY_ADMIN);
        OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(cmd.getDetailId());
        sendMessageWhenVacationBalanceChanged(detail, notificationContent);
    }

    @Override
    public PunchVacationBalance updateVacationBalances(UpdateVacationBalancesCommand cmd) {
        Tuple<PunchVacationBalance, Boolean> tuple = this.coordinationProvider.getNamedLock(CoordinationLocks.VACATION_BALANCE_UPDATE.getCode() + cmd.getDetailId()).enter(() -> {
            PunchVacationBalance balance = punchVacationBalanceProvider.findPunchVacationBalanceByDetailId(cmd.getDetailId());
            OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(cmd.getDetailId());
            if (null == detail) {
                return null;
            }
            if (null == balance) {
                balance = newVacationBalance(detail, cmd.getOrganizationId());

            }
            if (cmd.getAnnualLeaveBalanceCorrection() == null) {
                cmd.setAnnualLeaveBalanceCorrection(0.0);
            }
            Double newALB = new BigDecimal(String.valueOf(balance.getAnnualLeaveBalance())).add(new BigDecimal(String.valueOf(cmd.getAnnualLeaveBalanceCorrection()))).doubleValue();
            if (newALB < 0) {
                if (NormalFlag.YES == NormalFlag.fromCode(cmd.getIsBatch())) {
                    newALB = 0.0;
                    cmd.setAnnualLeaveBalanceCorrection(-balance.getAnnualLeaveBalance());
                } else {
                    throw RuntimeErrorException.errorWith(
                            PunchServiceErrorCode.SCOPE, PunchServiceErrorCode.ERROR_ANNUAL_LEAVE_CORRECTION_TOO_SMALL, "年假余额不足");
                }
            }
            balance.setAnnualLeaveBalance(newALB);

            if (cmd.getOvertimeCompensationBalanceCorrection() == null) {
                cmd.setOvertimeCompensationBalanceCorrection(0.0);
            }
            Double newOCB = new BigDecimal(String.valueOf(balance.getOvertimeCompensationBalance())).add(new BigDecimal(String.valueOf(cmd.getOvertimeCompensationBalanceCorrection()))).doubleValue();
            if (newOCB < 0) {
                if (NormalFlag.YES == NormalFlag.fromCode(cmd.getIsBatch())) {
                    newOCB = 0.0;
                    cmd.setOvertimeCompensationBalanceCorrection(-balance.getOvertimeCompensationBalance());

                } else {
                    throw RuntimeErrorException.errorWith(
                            PunchServiceErrorCode.SCOPE, PunchServiceErrorCode.ERROR_OVERTIME_CORRECTION_TOO_SMALL, "调休余额不足");
                }
            }
            balance.setOvertimeCompensationBalance(newOCB);
            saveBalanceAndLog(balance, cmd.getDescription(), cmd.getAnnualLeaveBalanceCorrection(), cmd.getOvertimeCompensationBalanceCorrection(), UserContext.currentUserId());
            return balance;
        });
        return tuple.first();
    }

    private void saveBalanceAndLog(PunchVacationBalance balance, String description, Double annualLeaveBalanceCorrection,
                                   Double overtimeCompensationBalanceCorrection, Long userId) {
        PunchVacationBalanceLog log = ConvertHelper.convert(balance, PunchVacationBalanceLog.class);
        log.setDescription(description);
        log.setAnnualLeaveBalanceCorrection(annualLeaveBalanceCorrection);
        log.setOvertimeCompensationBalanceCorrection(overtimeCompensationBalanceCorrection);
        if (balance.getId() == null) {
            balance.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            balance.setCreatorUid(userId);
            balance.setUpdateTime(balance.getCreateTime());
            balance.setOperatorUid(balance.getCreatorUid());
            punchVacationBalanceProvider.createPunchVacationBalance(balance);
        } else {
            balance.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            balance.setOperatorUid(userId);
            punchVacationBalanceProvider.updatePunchVacationBalance(balance);
        }
        log.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        log.setCreatorUid(userId);
        punchVacationBalanceLogProvider.createPunchVacationBalanceLog(log);
    }

    private PunchVacationBalance newVacationBalance(OrganizationMemberDetails detail, Long organizationId) {
        PunchVacationBalance balance = new PunchVacationBalance();
        balance.setDetailId(detail.getId());
        balance.setNamespaceId(detail.getNamespaceId());
        balance.setOwnerId(organizationId);
        balance.setOwnerType("organization");
        balance.setUserId(detail.getTargetId());
        balance.setAnnualLeaveBalance(0.0);
        balance.setOvertimeCompensationBalance(0.0);
        balance.setAnnualLeaveHistoryCount(BigDecimal.ZERO);
        balance.setOvertimeCompensationHistoryCount(BigDecimal.ZERO);
        return balance;
    }

    @Override
    public void addVacationBalanceHistoryCount(Long userId, Long organizationId, BigDecimal approvalAnnualLeaveDays, BigDecimal approvalOvertimeDays) {
        OrganizationMember organizationMember = organizationProvider.findActiveOrganizationMemberByOrgIdAndUId(userId, organizationId);
        if (organizationMember == null) {
            return;
        }
        OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByDetailId(organizationMember.getDetailId());
        if (detail == null) {
            return;
        }
        PunchVacationBalance balance = punchVacationBalanceProvider.findPunchVacationBalanceByDetailId(detail.getId());
        if (balance == null) {
            balance = newVacationBalance(detail, organizationId);
        }
        balance.setAnnualLeaveHistoryCount(balance.getAnnualLeaveHistoryCount().add(approvalAnnualLeaveDays));
        balance.setOvertimeCompensationHistoryCount(balance.getOvertimeCompensationHistoryCount().add(approvalOvertimeDays));
        if (balance.getAnnualLeaveHistoryCount().compareTo(BigDecimal.ZERO) < 0) {
            balance.setAnnualLeaveHistoryCount(BigDecimal.ZERO);
        }
        if (balance.getOvertimeCompensationHistoryCount().compareTo(BigDecimal.ZERO) < 0) {
            balance.setOvertimeCompensationHistoryCount(BigDecimal.ZERO);
        }
        punchVacationBalanceProvider.updatePunchVacationBalance(balance);
    }

    @Override
    public void batchUpdateVacationBalances(BatchUpdateVacationBalancesCommand cmd) {
        if (CollectionUtils.isEmpty(cmd.getDetailIds())) {
            return;
        }
        this.dbProvider.execute((TransactionStatus status) -> {
            for (Long detailId : cmd.getDetailIds()) {
                OrganizationMemberDetails organizationMemberDetail = organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
                //因为每一次在update方法里面会更改cmd的值,所以这里在循环里面
                UpdateVacationBalancesCommand cmd1 = ConvertHelper.convert(cmd, UpdateVacationBalancesCommand.class);
                cmd1.setIsBatch(NormalFlag.YES.getCode());
                cmd1.setDetailId(detailId);
                PunchVacationBalance punchVacationBalance = updateVacationBalances(cmd1);
                String notificationContent = buildVacationBalanceChangedNotificationContent(cmd1.getAnnualLeaveBalanceCorrection(), cmd1.getOvertimeCompensationBalanceCorrection(),
                        null, punchVacationBalance, null, ApprovalServiceConstants.VACATION_BALANCE_CHANGED_NOTIFICATION_CONTENT_BY_ADMIN);
                sendMessageWhenVacationBalanceChanged(organizationMemberDetail, notificationContent);
            }
            return null;
        });
    }

    @Override
    public ListVacationBalancesResponse listVacationBalances(ListVacationBalancesCommand cmd) {
        ListVacationBalancesResponse response = new ListVacationBalancesResponse();
        if (cmd.getPageSize() == null)
            cmd.setPageSize(20);
        if (cmd.getPageOffset() == null) {
            cmd.setPageOffset(1);
        }
        // 如果选择的是顶层节点，则查询全公司
        Long queryDeptId = cmd.getDepartmentId();
        if (queryDeptId != null && cmd.getOrganizationId() != null && queryDeptId.equals(cmd.getOrganizationId())) {
            queryDeptId = null;
        }
        List<VacationBalanceDTO> result = listVacationBalanceDTOs(cmd.getOrganizationId(), cmd.getCheckInStartDay(),
                cmd.getCheckInEndDay(), queryDeptId, cmd.getKeyWords(), cmd.getPageSize(), cmd.getPageOffset());
        if (null == result || result.size() == 0) {
            return response;
        }
        Integer nextPageOffset = null;
        if (result != null && result.size() > cmd.getPageSize()) {
            result.remove(result.size() - 1);
            nextPageOffset = cmd.getPageOffset() + 1;
        }
        response.setNextPageOffset(nextPageOffset);
        response.setVacationBalances(result);
        return response;
    }

    @Override
    public ListVacationBalanceLogsResponse listVacationBalanceLogs(ListVacationBalanceLogsCommand cmd) {
        ListVacationBalanceLogsResponse response = new ListVacationBalanceLogsResponse();

        int pageSize = getPageSize(configurationProvider, cmd.getPageSize());

        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

        List<PunchVacationBalanceLog> results = punchVacationBalanceLogProvider.listPunchVacationBalanceLog(cmd.getDetailId(), locator, pageSize + 1);
        if (null == results)
            return response;
        Long nextPageAnchor = null;
        if (results != null && results.size() > pageSize) {
            results.remove(results.size() - 1);
            nextPageAnchor = results.get(results.size() - 1).getId();
        }
        response.setNextPageAnchor(nextPageAnchor);
        response.setVacationBalanceLogs(new ArrayList<>());

        String locale = UserContext.current().getUser() == null ? PunchConstants.locale : UserContext.current().getUser().getLocale();
        String dayUnit = localeStringService.getLocalizedString(ApprovalServiceConstants.SCOPE, String.valueOf(ApprovalServiceConstants.TIME_UNIT_OF_DAY), locale, "天");
        String hourUnit = localeStringService.getLocalizedString(ApprovalServiceConstants.SCOPE, String.valueOf(ApprovalServiceConstants.TIME_UNIT_OF_HOUR), locale, "小时");

        for (PunchVacationBalanceLog r : results) {
            VacationBalanceLogDTO dto = ConvertHelper.convert(r, VacationBalanceLogDTO.class);
            dto.setAnnualLeaveBalanceDisplay(PunchDayParseUtils.parseDayTimeDisplayStringZeroWithoutUnit(dto.getAnnualLeaveBalance(), dayUnit, hourUnit));
            dto.setAnnualLeaveBalanceCorrectionDisplay(PunchDayParseUtils.parseDayTimeDisplayStringZeroWithoutUnit(dto.getAnnualLeaveBalanceCorrection(), dayUnit, hourUnit));
            dto.setOvertimeCompensationBalanceDisplay(PunchDayParseUtils.parseDayTimeDisplayStringZeroWithoutUnit(dto.getOvertimeCompensationBalance(), dayUnit, hourUnit));
            dto.setOvertimeCompensationBalanceCorrectionDisplay(PunchDayParseUtils.parseDayTimeDisplayStringZeroWithoutUnit(dto.getOvertimeCompensationBalanceCorrection(), dayUnit, hourUnit));
            dto.setCreatorName(socialSecurityService.findNameByOwnerAndUser(cmd.getOrganizationId(), r.getCreatorUid()));
            dto.setCreateTime(r.getCreateTime().getTime());
            response.getVacationBalanceLogs().add(dto);
        }
        return response;
    }

    public static int getPageSize(ConfigurationProvider configProvider, Integer requestedPageSize) {
        if (requestedPageSize == null) {
            return configProvider.getIntValue("pagination.default.size", AppConstants.PAGINATION_DEFAULT_SIZE);
        }
        return requestedPageSize.intValue();
    }

    @Override
    public void exportVacationBalances(ExportVacationBalancesCommand cmd) {
        Map<String, Object> params = new HashMap();
        //如果是null的话会被传成“null”
        params.put("ownerId", cmd.getOrganizationId());
        params.put("reportType", "exportVacationBalances");
        String fileName = "";
        fileName = String.format("假期余额_%s.xlsx", DateUtil.dateToStr(DateHelper.currentGMTTime(), DateUtil.NO_SLASH)
        );
        taskService.createTask(fileName, TaskType.FILEDOWNLOAD.getCode(), PunchExportTaskHandler.class, params, TaskRepeatFlag.REPEAT.getCode(), new Date());
    }

    @Override
    public ImportFileTaskDTO importVacationBalances(MultipartFile[] files, ImportVacationBalancesCommand cmd) {
        ImportFileTask task = new ImportFileTask();
        List resultList = processorExcel(files[0]);
        task.setOwnerType("PUNCH_VACATION");
        task.setOwnerId(cmd.getOrganizationId());
        task.setType(ImportFileTaskType.PUNCH_VACATION.getCode());
        task.setCreatorUid(UserContext.currentUserId());
        Long userId = UserContext.currentUserId();
        importFileService.executeTask(new ExecuteImportTaskCallback() {
            @Override
            public ImportFileResponse importFile() {
                ImportFileResponse response = new ImportFileResponse();
                String fileLog = "";
                if (resultList.size() > 0) {
                    RowResult title = (RowResult) resultList.get(1);
                    Map<String, String> titleMap = title.getCells();
                    response.setTitle(titleMap);
                    fileLog = checkImportVacationTitle(titleMap, cmd.getOrganizationId());
                    if (!StringUtils.isEmpty(fileLog)) {
                        response.setFileLog(fileLog);
                        return response;
                    }
                    saveImportVacation(resultList, cmd.getOrganizationId(), fileLog, response, userId);
                } else {
                    response.setFileLog(ImportFileErrorType.TITLE_ERROR.getCode());
                }
                return response;
            }
        }, task);
        return ConvertHelper.convert(task, ImportFileTaskDTO.class);
    }

    @Override
    public OutputStream getVacationBalanceOutputStream(Long ownerId, Long taskId) {
        List<VacationBalanceDTO> balances = listVacationBalanceDTOs(ownerId, null, null, null, null, Integer.MAX_VALUE - 1, 1);
        taskService.updateTaskProcess(taskId, 54);
        Workbook wb = createVacationBook(balances, taskId);
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
    public void sendMessageWhenVacationBalanceChanged(OrganizationMemberDetails memberDetail, String content) {
        if (memberDetail == null || memberDetail.getTargetId() == null || memberDetail.getTargetId() <= 0 || StringUtils.isBlank(content)) {
            return;
        }
        String messageSubject = localeTemplateService.getLocaleTemplateString(ApprovalServiceConstants.SCOPE, ApprovalServiceConstants.VACATION_BALANCE_CHANGED_NOTIFICATION_TITLE, PunchConstants.locale, new HashMap<>(0), "假期余额变动");
        String homeUrl = configurationProvider.getValue(ConfigConstants.HOME_URL, "");
        String url = String.format("%s/mobile/static/oa_punch/remaining_rule.html?ownerId=%d&namespaceId=%d#sign_suffix", homeUrl, memberDetail.getOrganizationId(), memberDetail.getNamespaceId());

        MessageDTO message = new MessageDTO();
        message.setBodyType(MessageBodyType.TEXT.getCode());
        message.setBody(content);
        message.setMetaAppId(AppConstants.APPID_DEFAULT);
        message.setChannels(new MessageChannel(ChannelType.USER.getCode(), String.valueOf(memberDetail.getTargetId())));

        OfficialActionData data = new OfficialActionData();
        data.setUrl(url);
        Map<String, String> meta = new HashMap<>();
        meta.put(MessageMetaConstant.MESSAGE_SUBJECT, messageSubject);
        RouterMetaObject metaObject = new RouterMetaObject();
        metaObject.setUrl(RouterBuilder.build(Router.BROWSER_I, data, "假期余额"));
        meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.MESSAGE_ROUTER.getCode());
        meta.put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(metaObject));
        message.setMeta(meta);

        //  send the message
        messagingService.routeMessage(
                User.SYSTEM_USER_LOGIN,
                AppConstants.APPID_MESSAGING,
                ChannelType.USER.getCode(),
                String.valueOf(memberDetail.getTargetId()),
                message,
                MessagingConstants.MSG_FLAG_STORED.getCode()
        );
    }

    private List<VacationBalanceDTO> listVacationBalanceDTOs(Long organizationId, Long checkInStartDay, Long checkInEndDay, Long departmentId, String keyWords, Integer pageSize, Integer pageOffSet) {
        List<VacationBalanceDTO> results = new ArrayList<>();

        List<OrganizationMemberDetails> records = archivesService.queryArchivesEmployees(new ListingLocator(), organizationId, departmentId, (locator, query) -> {
            if (checkInStartDay != null) {
                query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.CHECK_IN_TIME.greaterOrEqual(new java.sql.Date(checkInStartDay)));
            }
            if (checkInEndDay != null) {
                query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.CHECK_IN_TIME.lessOrEqual(new java.sql.Date(checkInEndDay)));
            }
            if (keyWords != null) {
                query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.CONTACT_TOKEN.eq(keyWords).or(Tables.EH_ORGANIZATION_MEMBER_DETAILS.CONTACT_NAME.like("%" + keyWords + "%")));
            }
            query.addConditions(Tables.EH_ORGANIZATION_MEMBER_DETAILS.EMPLOYEE_STATUS.ne(EmployeeStatus.DISMISSAL.getCode()));

            int offset = ((pageOffSet == null ? 1 : pageOffSet) - 1) * (pageSize);
            query.addOrderBy(Tables.EH_ORGANIZATION_MEMBER_DETAILS.CHECK_IN_TIME.desc());
            query.addLimit(offset, pageSize + 1);
            return query;
        });


        List<PunchVacationBalance> punchVacationBalances = punchVacationBalanceProvider.findPunchVacationBalanceByDetailIds(selectDetailIds(records));
        Map<Long, PunchVacationBalance> punchVacationBalanceMap = convertToMapByDetailId(punchVacationBalances);

        String locale = UserContext.current().getUser() == null ? PunchConstants.locale : UserContext.current().getUser().getLocale();
        String dayUnit = localeStringService.getLocalizedString(ApprovalServiceConstants.SCOPE, String.valueOf(ApprovalServiceConstants.TIME_UNIT_OF_DAY), locale, "天");
        String hourUnit = localeStringService.getLocalizedString(ApprovalServiceConstants.SCOPE, String.valueOf(ApprovalServiceConstants.TIME_UNIT_OF_HOUR), locale, "小时");

        records.forEach(r -> {
            VacationBalanceDTO dto = new VacationBalanceDTO();
            dto.setContactToken(r.getContactToken());
            dto.setContactName(r.getContactName());
            dto.setDetailId(r.getId());
            if (null != r.getCheckInTime()) {
                dto.setCheckInTime(r.getCheckInTime().getTime());
            }
            dto.setEmployeeNo(r.getEmployeeNo());
            String depName = archivesService.convertToOrgNames(archivesService.getEmployeeDepartment(r.getId()));
            dto.setDepartName(depName);
            PunchVacationBalance balance = punchVacationBalanceMap.get(r.getId());
            if (null != balance) {
                dto.setAnnualLeaveBalance(balance.getAnnualLeaveBalance());
                dto.setAnnualLeaveBalanceDisplay(PunchDayParseUtils.parseDayTimeDisplayStringZeroWithoutUnit(balance.getAnnualLeaveBalance(), dayUnit, hourUnit));
                dto.setOvertimeCompensationBalance(balance.getOvertimeCompensationBalance());
                dto.setOvertimeCompensationBalanceDisplay(PunchDayParseUtils.parseDayTimeDisplayStringZeroWithoutUnit(balance.getOvertimeCompensationBalance(), dayUnit, hourUnit));
                dto.setAnnualLeaveHistoryCount(balance.getAnnualLeaveHistoryCount());
                dto.setAnnualLeaveHistoryCountDisplay(PunchDayParseUtils.parseDayTimeDisplayStringZeroWithoutUnit(balance.getAnnualLeaveHistoryCount().doubleValue(), dayUnit, hourUnit));
                dto.setOvertimeCompensationHistoryCount(balance.getOvertimeCompensationHistoryCount());
                dto.setOvertimeCompensationHistoryCountDisplay(PunchDayParseUtils.parseDayTimeDisplayStringZeroWithoutUnit(balance.getOvertimeCompensationHistoryCount().doubleValue(), dayUnit, hourUnit));
            } else {
                dto.setAnnualLeaveBalance(0.0);
                dto.setAnnualLeaveBalanceDisplay(PunchDayParseUtils.parseDayTimeDisplayStringZeroWithoutUnit(0D, dayUnit, hourUnit));
                dto.setOvertimeCompensationBalance(0.0);
                dto.setOvertimeCompensationBalanceDisplay(PunchDayParseUtils.parseDayTimeDisplayStringZeroWithoutUnit(0D, dayUnit, hourUnit));
                dto.setAnnualLeaveHistoryCount(BigDecimal.ZERO);
                dto.setAnnualLeaveHistoryCountDisplay(PunchDayParseUtils.parseDayTimeDisplayStringZeroWithoutUnit(0D, dayUnit, hourUnit));
                dto.setOvertimeCompensationHistoryCount(BigDecimal.ZERO);
                dto.setOvertimeCompensationHistoryCountDisplay(PunchDayParseUtils.parseDayTimeDisplayStringZeroWithoutUnit(0D, dayUnit, hourUnit));
            }

            results.add(dto);
        });
        return results;

    }

    private List<Long> selectDetailIds(List<OrganizationMemberDetails> details) {
        if (CollectionUtils.isEmpty(details)) {
            return Collections.emptyList();
        }
        List<Long> detailIds = new ArrayList<>();
        details.forEach(d -> {
            detailIds.add(d.getId());
        });
        return detailIds;
    }

    private Map<Long, PunchVacationBalance> convertToMapByDetailId(List<PunchVacationBalance> punchVacationBalances) {
        Map<Long, PunchVacationBalance> map = new HashMap<>();
        if (CollectionUtils.isEmpty(punchVacationBalances)) {
            return map;
        }
        punchVacationBalances.forEach(p -> {
            map.put(p.getDetailId(), p);
        });
        return map;
    }

    private Workbook createVacationBook(List<VacationBalanceDTO> balances, Long taskId) {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("假期余额");
        this.createVacationBookSheetHead(sheet);
        int num = 0;
        if (null == balances || balances.size() == 0)
            return wb;
        for (VacationBalanceDTO dto : balances) {
            setNewVacationBookRow(sheet, dto);
            if (++num % 100 == 0) {
                taskService.updateTaskProcess(taskId, 54 + (int) (num / (Double.valueOf(balances.size()) / 45.00)));
            }
        }
        return wb;
    }

    private void createVacationBookSheetHead(XSSFSheet sheet) {
        CellStyle style = sheet.getWorkbook().createCellStyle();
        style.setWrapText(true);

        Row row1 = sheet.createRow(sheet.getLastRowNum());
        row1.createCell(0).setCellValue("假期余额");
        Row row2 = sheet.createRow(sheet.getLastRowNum() + 1);
        row2.setHeightInPoints((2 * sheet.getDefaultRowHeightInPoints()));
        Cell cell = row2.createCell(0);
        cell.setCellStyle(style);
        cell.setCellValue("导出时间：" + datetimeSF.get().format(new Date()) + "\n注：为方便统计，已将假期余额的天数和小时数分开显示");


        // 合并单元格
        CellRangeAddress cra = new CellRangeAddress(0, 0, 0, 5); // 起始行, 终止行, 起始列, 终止列
        sheet.addMergedRegion(cra);
        cra = new CellRangeAddress(1, 1, 0, 5); // 起始行, 终止行, 起始列, 终止列
        sheet.addMergedRegion(cra);

        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        List<String> titles = Arrays.asList("手机", "姓名", "年假余额（天）", "年假余额（小时）", "调休余额（天）", "调休余额（小时）");
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.0"));
        int i = -1;
        for (String title : titles) {
            Cell c = row.createCell(++i);
            c.setCellStyle(style);
            c.setCellValue(title);
        }
        sheet.createFreezePane(2, 3, 3, 4);
    }

    private void setNewVacationBookRow(XSSFSheet sheet, VacationBalanceDTO dto) {
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        int i = -1;
        row.createCell(++i).setCellValue(dto.getContactToken());
        row.createCell(++i).setCellValue(dto.getContactName());
        row.createCell(++i).setCellValue(PunchDayParseUtils.calculateTimesOfHalfDay(dto.getAnnualLeaveBalance()));
        row.createCell(++i).setCellValue(PunchDayParseUtils.calculateTimesOfHalfHour(dto.getAnnualLeaveBalance()));
        row.createCell(++i).setCellValue(PunchDayParseUtils.calculateTimesOfHalfDay(dto.getOvertimeCompensationBalance()));
        row.createCell(++i).setCellValue(PunchDayParseUtils.calculateTimesOfHalfHour(dto.getOvertimeCompensationBalance()));
    }

    private void saveImportVacation(List resultList, Long organizationId, String fileLog, ImportFileResponse response, Long userId) {
        Long ownerId = getTopEnterpriseId(organizationId);
        response.setLogs(new ArrayList<>());
        int coverNum = 0;
        for (int i = 2; i < resultList.size(); i++) {
            RowResult r = (RowResult) resultList.get(i);
            ImportFileResultLog<Map<String, String>> log = new ImportFileResultLog<>(PunchServiceErrorCode.SCOPE);
            Map<String, String> data = new HashMap();
            for (Map.Entry<String, String> entry : ((Map<String, String>) response.getTitle()).entrySet()) {
                data.put(entry.getKey(), (r.getCells().get(entry.getKey()) == null) ? "" : r.getCells().get(entry.getKey()));
            }
            log.setData(data);
            if (null == r.getA()) {
                LOGGER.error("手机号不可以是空");
                log.setErrorLog("手机号不可以是空");
                log.setCode(PunchServiceErrorCode.ERROR_CHECK_CONTACT);
                log.setErrorDescription(log.getErrorLog());
                response.getLogs().add(log);
                continue;
            }

            if (null == r.getC() || null == r.getD() || r.getE() == null || r.getF() == null) {
                LOGGER.error("余额不可以是空");
                log.setErrorLog("余额不可以是空");
                log.setCode(PunchServiceErrorCode.ERROR_CHECK_CONTACT);
                log.setErrorDescription(log.getErrorLog());
                response.getLogs().add(log);
                continue;
            }

            String userContact = r.getA().trim();
            OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByOrganizationIdAndContactToken(ownerId, userContact);
            if (null == detail) {
                LOGGER.error("can not find organization member ,contact token is " + userContact);
                log.setErrorLog("找不到用户: 手机号" + userContact);
                log.setCode(PunchServiceErrorCode.ERROR_CHECK_CONTACT);
                log.setErrorDescription(log.getErrorLog());
                response.getLogs().add(log);
                continue;
            } else {
                Tuple<Integer, Boolean> tuple = this.coordinationProvider.getNamedLock(CoordinationLocks.VACATION_BALANCE_UPDATE.getCode() + detail.getId()).enter(() -> {
                    PunchVacationBalance balance = punchVacationBalanceProvider.findPunchVacationBalanceByDetailId(detail.getId());
                    int coverNum1 = 0;
                    if (null != balance) {
                        //有balance的就算覆盖
                        coverNum1++;
                    } else {
                        balance = newVacationBalance(detail, organizationId);
                    }

                    saveImportEmployeeSalary(balance, r, log, response, detail, userId);
                    return coverNum1;
                });
                coverNum += tuple.first();
            }
        }
        response.setTotalCount((long) (resultList.size() - 2));
        response.setFailCount((long) response.getLogs().size());
        response.setCoverCount((long) coverNum);
        LOGGER.debug("import resp" + response);
    }

    private void saveImportEmployeeSalary(PunchVacationBalance balance, RowResult r, ImportFileResultLog<Map<String, String>> log, ImportFileResponse response, OrganizationMemberDetails memberDetail, Long userId) {

        BigDecimal annalBalanceDayValue = convertVacationCellToDouble("年假余额（天）", response, log, r.getC().trim());
        if (annalBalanceDayValue == null) {
            return;
        }
        BigDecimal annalBalanceHourValue = convertVacationCellToDouble("年假余额（小时）", response, log, r.getD().trim());
        if (annalBalanceHourValue == null) {
            return;
        }

        BigDecimal overBalanceDayValue = convertVacationCellToDouble("调休余额（天）", response, log, r.getE().trim());
        if (overBalanceDayValue == null) {
            return;
        }
        BigDecimal overBalanceHourValue = convertVacationCellToDouble("调休余额（小时）", response, log, r.getF().trim());
        if (overBalanceHourValue == null) {
            return;
        }
        BigDecimal annalBalance = annalBalanceDayValue.add(annalBalanceHourValue.divide(new BigDecimal("8").setScale(4, BigDecimal.ROUND_FLOOR)));
        BigDecimal overBalance = overBalanceDayValue.add(overBalanceHourValue.divide(new BigDecimal("8").setScale(4, BigDecimal.ROUND_FLOOR)));
        Double annualLeaveBalanceCorrection = annalBalance.subtract(new BigDecimal(String.valueOf(balance.getAnnualLeaveBalance()))).setScale(4, BigDecimal.ROUND_DOWN).doubleValue();
        Double overtimeCompensationBalanceCorrection = overBalance.subtract(new BigDecimal(String.valueOf(balance.getOvertimeCompensationBalance())).setScale(4, BigDecimal.ROUND_DOWN)).doubleValue();
        balance.setAnnualLeaveBalance(annalBalance.doubleValue());
        balance.setOvertimeCompensationBalance(overBalance.doubleValue());
        saveBalanceAndLog(balance, "批量导入", annualLeaveBalanceCorrection, overtimeCompensationBalanceCorrection, userId);
        String notificationContent = buildVacationBalanceChangedNotificationContent(annualLeaveBalanceCorrection, overtimeCompensationBalanceCorrection, null, balance, null, ApprovalServiceConstants.VACATION_BALANCE_CHANGED_NOTIFICATION_CONTENT_BY_ADMIN);
        sendMessageWhenVacationBalanceChanged(memberDetail, notificationContent);
    }

    private BigDecimal convertVacationCellToDouble(String title, ImportFileResponse response,
                                                   ImportFileResultLog<Map<String, String>> log, String r) {

        BigDecimal value = BigDecimal.ZERO;
        try {
            if (org.springframework.util.StringUtils.hasText(r)) {
                // 只保留1位小数,并转化成0.5的整数倍，如0.8->0.5   0.8*10=8  0.5=(8-8%5)/10
                value = new BigDecimal(r).setScale(1, BigDecimal.ROUND_FLOOR);
                value = value.multiply(new BigDecimal("10"));
                int result = value.intValue() - value.intValue() % 5;
                value = new BigDecimal(String.valueOf(result)).divide(new BigDecimal("10")).setScale(1, BigDecimal.ROUND_FLOOR);
            }
            if (value.compareTo(BigDecimal.ZERO) < 0) {
                String errorString = title + "不可以是负数";
                LOGGER.error(errorString);
                log.setErrorLog(errorString);
                log.setCode(PunchServiceErrorCode.ERROR_IS_MINUS);
                log.setErrorDescription(log.getErrorLog());
                response.getLogs().add(log);
                return null;
            }
        } catch (Exception e) {
            String errorString = title + "不是数字";
            LOGGER.error(errorString);
            log.setErrorLog(errorString);
            log.setCode(PunchServiceErrorCode.ERROR_NOT_DOUBLE);
            log.setErrorDescription(log.getErrorLog());
            response.getLogs().add(log);
            return null;
        }
        return value;
    }

    private String checkImportVacationTitle(Map<String, String> titleMap, Long organizationId) {
        if (!titleMap.get("A").contains("手机")) {
            LOGGER.error("第一列不是手机而是" + titleMap.get("A"));
            return ImportFileErrorType.TITLE_ERROR.getCode();
        }
        if (!titleMap.get("B").contains("姓名")) {
            LOGGER.error("第2列不是姓名而是" + titleMap.get("B"));

            return ImportFileErrorType.TITLE_ERROR.getCode();
        }
        if (!titleMap.get("C").contains("年假余额（天）")) {
            LOGGER.error("第3列不是年假余额（天）而是" + titleMap.get("C"));

            return ImportFileErrorType.TITLE_ERROR.getCode();
        }
        if (!titleMap.get("D").contains("年假余额（小时）")) {
            LOGGER.error("第4列不是年假余额（小时）而是" + titleMap.get("D"));

            return ImportFileErrorType.TITLE_ERROR.getCode();
        }
        if (!titleMap.get("E").contains("调休余额（天）")) {
            LOGGER.error("第5列不是调休余额（天）而是" + titleMap.get("E"));

            return ImportFileErrorType.TITLE_ERROR.getCode();
        }
        if (!titleMap.get("F").contains("调休余额（小时）")) {
            LOGGER.error("第6列不是调休余额（小时）而是" + titleMap.get("F"));

            return ImportFileErrorType.TITLE_ERROR.getCode();
        }
        return null;
    }

    private List processorExcel(MultipartFile file) {
        try {
            List resultList = PropMrgOwnerHandler.processorExcel(file.getInputStream());
            if (resultList.isEmpty()) {
                LOGGER.error("File content is empty");
                throw RuntimeErrorException.errorWith(PunchServiceErrorCode.SCOPE, PunchServiceErrorCode.ERROR_FILE_IS_EMPTY,
                        "File content is empty");
            }
            return resultList;
        } catch (IOException e) {
            LOGGER.error("file process excel error ", e);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String buildVacationBalanceChangedNotificationContent(Double annualLeaveAdd, Double overtimeAdd, PunchExceptionRequest request, PunchVacationBalance balance, ApprovalCategory approvalCategory, Integer notificationCode) {
        /**
         *  1、request !=null && approvalCategory!=null 请假审批提交 或 年假/调休的审批被撤销/终止/删除  发送余额变更提醒
         *  2、否则 为 管理员调整假期余额
         */
        if ((annualLeaveAdd == null || annualLeaveAdd == 0) && (overtimeAdd == null || overtimeAdd == 0)) {
            // 余额没有变更
            return null;
        }
        if (notificationCode == null || balance == null) {
            return null;
        }
        String dayUnit = localeStringService.getLocalizedString(ApprovalServiceConstants.SCOPE, String.valueOf(ApprovalServiceConstants.TIME_UNIT_OF_DAY), PunchConstants.locale, "天");
        String hourUnit = localeStringService.getLocalizedString(ApprovalServiceConstants.SCOPE, String.valueOf(ApprovalServiceConstants.TIME_UNIT_OF_HOUR), PunchConstants.locale, "小时");

        Map<String, Object> model = new HashMap<>();
        model.put("annualLeaveAdd", annualLeaveAdd);
        model.put("annualLeaveAddShow", PunchDayParseUtils.parseDayTimeDisplayStringZeroWithoutUnit(Math.abs(annualLeaveAdd), dayUnit, hourUnit));
        model.put("overtimeAdd", overtimeAdd);
        model.put("overtimeAddShow", PunchDayParseUtils.parseDayTimeDisplayStringZeroWithoutUnit(Math.abs(overtimeAdd), dayUnit, hourUnit));
        model.put("annualLeaveBalance", PunchDayParseUtils.parseDayTimeDisplayStringZeroWithUnit(balance.getAnnualLeaveBalance(), dayUnit, hourUnit));
        model.put("overtimeCompensationBalance", PunchDayParseUtils.parseDayTimeDisplayStringZeroWithUnit(balance.getOvertimeCompensationBalance(), dayUnit, hourUnit));
        if (approvalCategory != null) {
            model.put("categoryName", approvalCategory.getCategoryName());
        }
        if (request != null) {
            String requestTime = localeTemplateService.getLocaleTemplateString(PunchConstants.EXCEPTION_STATISTIC_SCOPE,
                    PunchConstants.ASK_FOR_LEAVE_TEMPLATE_CONTENT, PunchConstants.locale, getExceptionBeginEndTimeMap(request), "");
            model.put("requestTime", requestTime);
        }

        return localeTemplateService.getLocaleTemplateString(ApprovalServiceConstants.SCOPE, notificationCode, PunchConstants.locale, model, "");
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

    private Long getTopEnterpriseId(Long organizationId) {
        Organization organization = organizationProvider.findOrganizationById(organizationId);
        if (organization.getParentId() == null)
            return organizationId;
        else {
            return Long.valueOf(organization.getPath().split("/")[1]);
        }
    }
}
