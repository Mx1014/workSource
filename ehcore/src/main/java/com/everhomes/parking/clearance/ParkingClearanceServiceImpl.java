// @formatter:off
package com.everhomes.parking.clearance;

import static com.everhomes.rest.parking.ParkingLocalStringCode.SCOPE_STRING_STATUS;
import static com.everhomes.rest.parking.clearance.ParkingClearanceConst.APPLY_PRIVILEGE_ID;
import static com.everhomes.rest.parking.clearance.ParkingClearanceConst.MODULE_ID;
import static com.everhomes.util.RuntimeErrorException.errorWith;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Size;
import javax.validation.metadata.ConstraintDescriptor;

import com.alibaba.fastjson.JSONArray;
import com.everhomes.parking.jinyi.JinyiClearance;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseProvider;
import com.everhomes.flow.FlowService;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.organization.OrganizationService;
import com.everhomes.parking.ParkingLot;
import com.everhomes.parking.ParkingProvider;
import com.everhomes.parking.ParkingVendorHandler;
import com.everhomes.parking.handler.JinyiParkingVendorHandler;
import com.everhomes.rest.activity.ActivityServiceErrorCode;
import com.everhomes.parking.jinyi.JinyiJsonEntity;
import com.everhomes.rest.energy.util.ParamErrorCodes;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowCaseStatus;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.launchpad.ActionType;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.parking.ParkingCommonStatus;
import com.everhomes.rest.parking.ParkingErrorCode;
import com.everhomes.rest.parking.ParkingLocalStringCode;
import com.everhomes.rest.parking.ParkingOwnerType;
import com.everhomes.rest.parking.clearance.CheckAuthorityCommand;
import com.everhomes.rest.parking.clearance.CheckAuthorityResponse;
import com.everhomes.rest.parking.clearance.CheckAuthorityStatus;
import com.everhomes.rest.parking.clearance.CreateClearanceLogCommand;
import com.everhomes.rest.parking.clearance.CreateClearanceOperatorCommand;
import com.everhomes.rest.parking.clearance.DeleteClearanceLogCommand;
import com.everhomes.rest.parking.clearance.DeleteClearanceOperatorCommand;
import com.everhomes.rest.parking.clearance.GetActualClearanceLogCommand;
import com.everhomes.rest.parking.clearance.ListClearanceOperatorCommand;
import com.everhomes.rest.parking.clearance.ListClearanceOperatorResponse;
import com.everhomes.rest.parking.clearance.ParkingActualClearanceLogDTO;
import com.everhomes.rest.parking.clearance.ParkingClearanceLogDTO;
import com.everhomes.rest.parking.clearance.ParkingClearanceLogStatus;
import com.everhomes.rest.parking.clearance.ParkingClearanceOperatorDTO;
import com.everhomes.rest.parking.clearance.ParkingClearanceOperatorType;
import com.everhomes.rest.parking.clearance.SearchClearanceLogCommand;
import com.everhomes.rest.parking.clearance.SearchClearanceLogsResponse;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.scheduler.RunningFlag;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.DateUtil;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.excel.ExcelUtils;


/**
 * Parking clearance service
 * Created by xq.tian on 2016/12/2.
 */
@Service
public class ParkingClearanceServiceImpl implements ParkingClearanceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParkingClearanceServiceImpl.class);
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    private ParkingClearanceOperatorProvider clearanceOperatorProvider;

    @Autowired
    private ParkingClearanceLogProvider clearanceLogProvider;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;

    @Autowired
    private FlowService flowService;

    @Autowired
    private FlowCaseProvider flowCaseProvider;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private LocaleStringService localeStringService;

    @Autowired
    private LocaleTemplateService localeTemplateService;

    @Autowired
    private ParkingProvider parkingProvider;

    @Autowired
    private CoordinationProvider coordinationProvider;

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ScheduleProvider scheduleProvider;

    @Override
    public void createClearanceOperator(CreateClearanceOperatorCommand cmd) {
    	if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)){
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 2090020900L, cmd.getAppId(), null,cmd.getCurrentProjectId());//车辆放行权限
		}

        if (null == cmd.getNamespaceId()) {
            cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
        }

        validate(cmd);

        if (cmd.getUserIds() != null && cmd.getUserIds().size() > 0) {
            int alreadyInsertUserCount = 0;
            for (Long userId : cmd.getUserIds()) {
                // 检查当前停车场里是否已经有当前用户了
                ParkingClearanceOperator operator = clearanceOperatorProvider.findByParkingLotIdAndUid(cmd.getParkingLotId(), userId, cmd.getOperatorType());
                if (operator != null) {
                    alreadyInsertUserCount++;
                    continue;
                }
                User user = this.findUserById(userId);
                ParkingClearanceOperator newOperator = new ParkingClearanceOperator();
                newOperator.setOrganizationId(cmd.getOrganizationId());
                newOperator.setCommunityId(cmd.getCommunityId());
                newOperator.setNamespaceId(cmd.getNamespaceId());
                newOperator.setParkingLotId(cmd.getParkingLotId());
                newOperator.setOperatorType(cmd.getOperatorType());
                newOperator.setOperatorId(user.getId());
                newOperator.setStatus(ParkingCommonStatus.ACTIVE.getCode());
                dbProvider.execute(status -> {
                    clearanceOperatorProvider.createClearanceOperator(newOperator);
                    return true;
                });
            }
            // 此次添加的用户都已经在数据库里了, 就给个提示, 测试要求的
            if (alreadyInsertUserCount == cmd.getUserIds().size()) {
                throw errorWith(ParkingErrorCode.SCOPE_CLEARANCE, ParkingErrorCode.ERROR_USER_ALREADY_IN_DATABASE, "All user is in database");
            }
        }
    }

    // 校验当前用户是否有申请放行权限
    private void checkApplicantAuthority(Long orgId, Long parkingLotId) {
        if (!userPrivilegeMgr.checkSuperAdmin(currUserId(), orgId) ||
                !checkApplyUser(parkingLotId, ParkingClearanceOperatorType.APPLICANT)) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                    "Insufficient privilege");
        }

    }

    private boolean checkApplyUser(Long parkingLotId, ParkingClearanceOperatorType operatorType) {
        ParkingClearanceOperator applicant = clearanceOperatorProvider
                .findByParkingLotIdAndUid(parkingLotId, currUserId(), operatorType.getCode());
        if (applicant == null) {
            return false;
        }
        return true;
    }

    private ParkingClearanceOperatorDTO toClearanceOperatorDTO(ParkingClearanceOperator operator) {
        ParkingClearanceOperatorDTO dto = new ParkingClearanceOperatorDTO();
        // 获取用户的部门
        List<OrganizationDTO> groups = organizationService.getOrganizationMemberGroups(
                OrganizationGroupType.DEPARTMENT, operator.getOperatorId(), operator.getOrganizationId());
        dto.setDepartment((groups != null && groups.size() > 0) ? groups.get(0).getName() : "");
        dto.setId(operator.getId());
        User user = this.findUserById(operator.getOperatorId());
        dto.setName(user.getNickName());
        dto.setIdentifierToken(this.findUserIdentifierByUserId(user.getId()).getIdentifierToken());
        return dto;
    }

    private ParkingClearanceLogDTO toClearanceLogDTO(ParkingClearanceLog log) {
        ParkingClearanceLogDTO dto = ConvertHelper.convert(log, ParkingClearanceLogDTO.class);
        String statusStr = localeStringService.getLocalizedString(SCOPE_STRING_STATUS, log.getStatus() + "", currLocale(), "");
        dto.setStatus(statusStr);
        dto.setIdentifierToken(this.findUserIdentifierByUserId(log.getOperatorId()).getIdentifierToken());
        dto.setApplicant(this.findUserById(log.getOperatorId()).getNickName());
        if (StringUtils.isBlank(log.getRemarks())) {
            String remarksNoneValue = localeStringService.getLocalizedString(ParkingLocalStringCode.SCOPE_STRING,
                    ParkingLocalStringCode.NONE_CODE, currLocale(), "");
            dto.setRemarks(remarksNoneValue);
        }
        FlowCase flowCase = flowCaseProvider.findFlowCaseByReferId(log.getId(), EntityType.PARKING_CLEARANCE_LOG.getCode(), MODULE_ID);
        if (flowCase != null) {
            dto.setFlowCaseId(flowCase.getId());
        }
        return dto;
    }

    @Scheduled(cron="0 1 0 * * ?")
    public void sychnLogs(){

        if(RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {

            this.coordinationProvider.getNamedLock(CoordinationLocks.PARKING_CLEARANCE_LOG_STATISTICS.getCode()).tryEnter(() -> {

                SearchClearanceLogCommand cmd = new SearchClearanceLogCommand();

                Calendar start = Calendar.getInstance();
                start.set(Calendar.HOUR_OF_DAY, 0);
                start.set(Calendar.MINUTE, 0);
                start.set(Calendar.SECOND, 0);
                start.set(Calendar.MILLISECOND, 0);
                cmd.setStartTime(start.getTimeInMillis());
                start.set(Calendar.HOUR_OF_DAY, 23);
                start.set(Calendar.MINUTE, 59);
                start.set(Calendar.SECOND, 59);
                start.set(Calendar.MILLISECOND, 999);
                cmd.setEndTime(start.getTimeInMillis());
//                qo.setStatus(ParkingClearanceLogStatus.COMPLETED.getCode());

                sychnLogs(cmd);
            });
        }
    }

    @Override
    public void sychnLogs(SearchClearanceLogCommand cmd) {
        ParkingClearanceLogQueryObject qo = new ParkingClearanceLogQueryObject();
        qo.setStartTime(cmd.getStartTime());
        qo.setEndTime(cmd.getEndTime());

        List<ParkingClearanceLog> logs = clearanceLogProvider.searchClearanceLog(qo);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        logs.forEach(r -> {
            ParkingLot parkingLot = parkingProvider.findParkingLotById(r.getParkingLotId());

            String vendorName = parkingLot.getVendorName();
            ParkingVendorHandler handler = getParkingVendorHandler(vendorName);
            List<ParkingActualClearanceLogDTO> result = handler.getTempCardLogs(r);

            if (null != result) {
//                List<ParkingActualClearanceLogDTO> result = actualLogs.stream().map(this::convertActualClearanceLogDTO).collect(Collectors.toList());
                Map<String, String> temp = new LinkedHashMap<>();
                result.forEach(a -> {
                    if (null != a.getEntryTime()) {
                        temp.put("进场时间", sdf.format(a.getEntryTime()));
                    }
                    if (null != a.getExitTime()) {
                        temp.put("出场时间", sdf.format(a.getExitTime()));
                    }
                });
                r.setLogJson(JSONArray.toJSONString(temp));
                clearanceLogProvider.updateClearanceLog(r);
            }
        });
    }

    @Override
    public void deleteClearanceOperator(DeleteClearanceOperatorCommand cmd) {
        validate(cmd);

        coordinationProvider.getNamedLock(CoordinationLocks.PARKING_CLEARANCE_OPERATOR.getCode() + cmd.getId()).tryEnter(() -> {
            ParkingClearanceOperator operator = clearanceOperatorProvider.findById(cmd.getId());
            if (operator != null) {
                boolean success = dbProvider.execute(status -> {

                    clearanceOperatorProvider.deleteClearanceOperator(operator);
                    return true;
                });
                if (!success) {
                    LOGGER.error("Delete parking clearance operator error, operator id = {}", cmd.getId());
                    throw errorWith(ParkingErrorCode.SCOPE_CLEARANCE, ParkingErrorCode.ERROR_DELETE_PARKING_CLEARANCE_OPERATOR,
                            "Delete parking clearance operator error");
                }
            } else {
                LOGGER.error("The operator is not exist, id = {}", cmd.getId());
            }
        });
    }

    @Override
    public ListClearanceOperatorResponse listClearanceOperator(ListClearanceOperatorCommand cmd) {

        if (null == cmd.getNamespaceId()) {
            cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
        }

        validate(cmd);
        ListClearanceOperatorResponse response = new ListClearanceOperatorResponse();

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        List<ParkingClearanceOperator> operators = clearanceOperatorProvider.listClearanceOperator(cmd.getNamespaceId(),
                cmd.getCommunityId(), cmd.getParkingLotId(), cmd.getOperatorType(), pageSize+1, cmd.getPageAnchor());
        if (operators != null) {
            if (operators.size() > pageSize) {
                response.setNextPageAnchor(operators.get(operators.size() - 1).getCreateTime().getTime());
            }
            response.setOperators(operators.stream().limit(pageSize).map(this::toClearanceOperatorDTO).collect(Collectors.toList()));
        }
        return response;
    }

    @Override
    public ParkingClearanceLogDTO createClearanceLog(CreateClearanceLogCommand cmd) {

        if (null == cmd.getNamespaceId()) {
            cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
        }

        this.validate(cmd);
        this.checkApplicantAuthority(cmd.getOrganizationId(), cmd.getParkingLotId());

        ParkingClearanceLog log = new ParkingClearanceLog();
        log.setNamespaceId(cmd.getNamespaceId());
        log.setStatus(ParkingClearanceLogStatus.PROCESSING.getCode());
        if(configurationProvider.getBooleanValue("parking.zijing.directcompleted",false)) {
            log.setStatus(ParkingClearanceLogStatus.COMPLETED.getCode());
        }
        log.setParkingLotId(cmd.getParkingLotId());
        log.setCommunityId(cmd.getCommunityId());
        log.setApplicantId(currUserId());
        log.setClearanceTime(new Timestamp(cmd.getClearanceTime()));
        log.setRemarks(cmd.getRemarks());
        log.setPlateNumber(cmd.getPlateNumber());
        log.setApplyTime(Timestamp.from(Instant.now()));
        log.setOperatorId(currUserId());

        boolean success = dbProvider.execute(status -> {
            long logId = clearanceLogProvider.createClearanceLog(log);
            log.setId(logId);
            this.createFlowCase(log, cmd.getOrganizationId(), cmd.getNamespaceId());

            notifyParkingLot(log);
            return true;
        });
        if (!success) {
            LOGGER.error("some error.");
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "some error.");
        }

        return toClearanceLogDTO(log);
    }

    private void notifyParkingLot(ParkingClearanceLog log) {
        //通知第三方，向停车系统申请放行资格
        ParkingLot parkingLot = parkingProvider.findParkingLotById(log.getParkingLotId());

        String vendorName = parkingLot.getVendorName();
        ParkingVendorHandler handler = getParkingVendorHandler(vendorName);
        String logToken = handler.applyTempCard(log);
        if (null == logToken) {
            LOGGER.error("some error.");
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "some error.");
        }
        log.setLogToken(logToken);
        clearanceLogProvider.updateClearanceLog(log);
    }

    private Long createFlowCase(ParkingClearanceLog log, Long organizationId, Integer namespaceId) {
        Flow flow = flowService.getEnabledFlow(namespaceId, MODULE_ID, null, log.getParkingLotId(), FlowOwnerType.PARKING.getCode());
        if (flow != null) {
            CreateFlowCaseCommand flowCaseCmd = new CreateFlowCaseCommand();
            flowCaseCmd.setApplyUserId(currUserId());
            flowCaseCmd.setFlowMainId(flow.getFlowMainId());
            flowCaseCmd.setFlowVersion(flow.getFlowVersion());
            flowCaseCmd.setReferId(log.getId());
            flowCaseCmd.setReferType(EntityType.PARKING_CLEARANCE_LOG.getCode());
            flowCaseCmd.setCurrentOrganizationId(organizationId);
            // flowCase摘要内容
            flowCaseCmd.setContent(this.getBriefContent(log));

            flowCaseCmd.setServiceType("车辆放行");
            FlowCase flowCase = flowService.createFlowCase(flowCaseCmd);
            return flowCase.getId();
        } else {
            LOGGER.error("There is no workflow enabled.");
            throw errorWith(ParkingErrorCode.SCOPE_CLEARANCE, ParkingErrorCode.ERROR_NO_WORK_FLOW_ENABLED, "There is no workflow enabled.");
        }
    }

    private String getBriefContent(ParkingClearanceLog log) {
        Map<String, String> map = new HashMap<>();
        map.put("plateNumber", log.getPlateNumber());
        String dateStr = DateHelper.getDateDisplayString(TimeZone.getDefault(), log.getClearanceTime().getTime(), "yyyy-MM-dd");
        map.put("clearanceTime", dateStr);
        return localeTemplateService.getLocaleTemplateString(ParkingLocalStringCode.SCOPE_TEMPLATE,
                ParkingLocalStringCode.CLEARANCE_FLOW_CASE_BRIEF_CONTENT, currLocale(), map, "");
    }

    @Override
    public SearchClearanceLogsResponse searchClearanceLog(SearchClearanceLogCommand cmd) {
    	if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)){
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 2090020900L, cmd.getAppId(), null,cmd.getCurrentProjectId());//车辆放行权限
		}

        if (null == cmd.getNamespaceId()) {
            cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
        }

        validate(cmd);
        SearchClearanceLogsResponse response = new SearchClearanceLogsResponse();
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        ParkingClearanceLogQueryObject qo = ConvertHelper.convert(cmd, ParkingClearanceLogQueryObject.class);
        qo.setPageSize(pageSize + 1);
        // 传来的参数是一天的起始时间,需要给他加到一天的结束时间
        if (qo.getEndTime() != null) {
            qo.setEndTime(Instant.ofEpochMilli(qo.getEndTime()).plus(1L, ChronoUnit.DAYS).toEpochMilli());
        }
        List<ParkingClearanceLog> logs = clearanceLogProvider.searchClearanceLog(qo);
        if (logs != null) {
            if (logs.size() > pageSize) {
                response.setNextPageAnchor(logs.get(logs.size() - 1).getCreateTime().getTime());
            }
            response.setLogs(logs.stream().limit(pageSize).map(this::toClearanceLogDTO).collect(Collectors.toList()));
        }
        return response;
    }

    @Override
	public void exportClearanceLog(SearchClearanceLogCommand cmd, HttpServletResponse response) {
    	if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)){
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 2090020900L, cmd.getAppId(), null,cmd.getCurrentProjectId());//车辆放行权限
		}
    	cmd.setPageAnchor(null);
    	cmd.setPageSize(10000);
    	SearchClearanceLogsResponse searchClearanceLogsResponse = searchClearanceLog(cmd);
    	List<ParkingClearanceLogDTO> logs = searchClearanceLogsResponse.getLogs();
    	if (logs != null && logs.size() > 0) {
    		logs.forEach(this::formatToExport);
    		String fileName = String.format("车辆放行记录_%s", DateUtil.dateToStr(new Date(), DateUtil.DATE_TIME_NO_SLASH));
			ExcelUtils excelUtils = new ExcelUtils(response, fileName, "车辆放行记录");
			List<String> propertyNames = new ArrayList<String>(Arrays.asList("applicant", "identifierToken", "applyTimeString", "plateNumber", "clearanceTimeString", "status", "remarks", "logJson"));
			List<String> titleNames = new ArrayList<String>(Arrays.asList("发起人", "手机号", "发起时间", "来访车辆", "预计来访日期", "任务状态", "备注", "实际来访记录"));
			List<Integer> titleSizes = new ArrayList<Integer>(Arrays.asList(20, 20, 20, 20, 20, 20, 20, 30, 30));
			excelUtils.setNeedSequenceColumn(true);
			excelUtils.writeExcel(propertyNames, titleNames, titleSizes, logs.stream().map(r->{
				if("{}".equals(r.getLogJson())){
					r.setLogJson("");
				}
				return r;
			}).collect(Collectors.toList()));
		}else {
			throw RuntimeErrorException.errorWith(ParkingLocalStringCode.SCOPE_STRING,
					Integer.parseInt(ParkingLocalStringCode.NO_DATA), "no data");
		}
	}

    private void formatToExport(ParkingClearanceLogDTO log) {
    	log.setApplyTimeString(DateUtil.dateToStr(log.getApplyTime(), DateUtil.DATE_TIME_LINE));
    	log.setClearanceTimeString(DateUtil.dateToStr(log.getClearanceTime(), DateUtil.YMR_SLASH));

    }

	@Override
    public CheckAuthorityResponse checkAuthority(CheckAuthorityCommand cmd) {
        validate(cmd);

        ActionType actionType = ActionType.fromCode(cmd.getActionType());

        ParkingClearanceOperatorType operatorType = ParkingClearanceOperatorType.APPLICANT;// 需要检查的operatorType
        long privilegeId = -1; // 需要检查的权限id
        String message = null;// 没有权限时的提示消息

        if (actionType == ActionType.PARKING_CLEARANCE) {
            operatorType = ParkingClearanceOperatorType.APPLICANT;
            privilegeId = APPLY_PRIVILEGE_ID;
            message = localeStringService.getLocalizedString(ParkingLocalStringCode.SCOPE_STRING,
                    ParkingLocalStringCode.INSUFFICIENT_PRIVILEGE_CLEARANCE_MESSAGE_CODE, currLocale(), "");
        } else if (actionType == ActionType.PARKING_CLEARANCE_TASK){

        }
        CheckAuthorityStatus status = CheckAuthorityStatus.FAILURE;
        List<ParkingLot> parkingLots = new ArrayList<>();
        if (cmd.getParkingLotId() != null) {
            ParkingLot parkingLot = parkingProvider.findParkingLotById(cmd.getParkingLotId());
            if (parkingLot != null) {
//                parkingLots.add(parkingLot);
            	checkApplicantAuthority(cmd.getOrganizationId(), parkingLot.getId());
                status = CheckAuthorityStatus.SUCCESS;
                message = null;
            }
        } else {
            parkingLots = parkingProvider.listParkingLots(ParkingOwnerType.COMMUNITY.getCode(), cmd.getCommunityId());
            if (privilegeId > 0 && parkingLots != null && parkingLots.size() > 0) {
                for (ParkingLot parkingLot : parkingLots) {
                    try {

                        checkApplicantAuthority(cmd.getOrganizationId(), parkingLot.getId());

                        status = CheckAuthorityStatus.SUCCESS;
                        message = null;
                        break;// 只要有一个停车场的权限就放行
                    } catch (RuntimeErrorException ree) {
                        // ignore
                    }
                }
            }
        }
        return new CheckAuthorityResponse(status.getCode(), message);
    }

    private Long currUserId() {
        return UserContext.current().getUser().getId();
    }

    private String currLocale() {
        return UserContext.current().getUser().getLocale();
    }

    private UserIdentifier findUserIdentifierByUserId(Long userId) {
        UserIdentifier identifier = userProvider.findClaimedIdentifierByOwnerAndType(userId, IdentifierType.MOBILE.getCode());
        if (identifier != null) {
            return identifier;
        }
        LOGGER.error("User identifier is not exist, userId = {}", userId);
        throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_USER_NOT_EXIST, "User identifier is not exist userId = %s", userId);
    }

    private User findUserById(Long userId) {
        User user = userProvider.findUserById(userId);
        if (user != null) {
            return user;
        }
        LOGGER.error("User is not exist, userId = {}", userId);
        throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_USER_NOT_EXIST, "User is not exist userId = %s", userId);
    }

    // 参数校验方法
    // 可以校验带bean validation 注解的对象
    // 校验失败, 抛出异常, 异常信息附带参数值信息
    private void validate(Object o) {
        Set<ConstraintViolation<Object>> result = validator.validate(o);

        for (ConstraintViolation<Object> v : result) {
            ConstraintDescriptor<?> constraintDescriptor = v.getConstraintDescriptor();
            String constraintAnnotationClassName = constraintDescriptor.getAnnotation().annotationType().getName();
            switch (constraintAnnotationClassName) {
                // 参数长度检查
                case "javax.validation.constraints.Size":
                    Size size = (Size) constraintDescriptor.getAnnotation();
                    int max = size.max();
                    if (max > 0) {
                        LOGGER.error("Parameter over length: [ {} ]", v.getPropertyPath());
                        throw errorWith(ParamErrorCodes.SCOPE, ParamErrorCodes.ERROR_OVER_LENGTH,
                                "Parameter over length: [ %s ]", v.getPropertyPath());
                    }
                    break;
                // 其他参数检查
                default:
                    LOGGER.error("Invalid parameter {} [ {} ]", v.getPropertyPath(), v.getInvalidValue());
                    throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                            "Invalid parameter %s [ %s ]", v.getPropertyPath(), v.getInvalidValue());
            }
        }

        /*if ("javax.validation.constraints.Size".equals(constraintAnnotationClassName)) {
            Size size = (Size) constraintDescriptor.getAnnotation();
            int max = size.max();
            if (max > 0) {
                LOGGER.error("Parameter over length: [ {} ]", v.getPropertyPath());
                throw errorWith(ParamErrorCodes.SCOPE, ParamErrorCodes.ERROR_OVER_LENGTH, "Parameter over length: [ %s ]", v.getPropertyPath());
            }
        }
        if (!result.isEmpty()) {
            result.stream().map(r -> r.getPropertyPath().toString() + " [ " + r.getInvalidValue() + " ]")
                    .reduce((i, a) -> i + ", " + a).ifPresent(r -> {
                LOGGER.error("Invalid parameter {}", r);
                throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid parameter %s", r);
            });
        }*/
    }

    @Override
    public void deleteClearanceLog(DeleteClearanceLogCommand cmd) {
        ParkingClearanceLog log = clearanceLogProvider.findById(cmd.getId());

        if (null == log) {
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid parameter");
        }
        dbProvider.execute(s -> {
            log.setStatus(ParkingClearanceLogStatus.INACTIVE.getCode());
            clearanceLogProvider.updateClearanceLog(log);

            FlowCase flowCase = flowCaseProvider.findFlowCaseByReferId(log.getId(), EntityType.PARKING_CLEARANCE_LOG.getCode(), MODULE_ID);
            if (null != flowCase) {
                flowCase.setStatus(FlowCaseStatus.INVALID.getCode());
                flowCaseProvider.updateFlowCase(flowCase);
            }

            return null;
        });

    }

    @Override
    public List<ParkingActualClearanceLogDTO> getActualClearanceLog(GetActualClearanceLogCommand cmd) {

//        List<ParkingActualClearanceLogDTO> result = new ArrayList<>();
        ParkingLot parkingLot = parkingProvider.findParkingLotById(cmd.getParkingLotId());

        ParkingClearanceLog log = clearanceLogProvider.findById(cmd.getId());
        String vendorName = parkingLot.getVendorName();
        ParkingVendorHandler handler = getParkingVendorHandler(vendorName);
        List<ParkingActualClearanceLogDTO> actualLogs = handler.getTempCardLogs(log);
//        if (null != actualLogs) {
//            result = actualLogs.stream().map(this::convertActualClearanceLogDTO).collect(Collectors.toList());
//        }
        if(actualLogs!=null && actualLogs.size()>0){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Map<String, String> temp = new LinkedHashMap<>();
            for (ParkingActualClearanceLogDTO actualLog : actualLogs) {
                if (null != actualLog.getEntryTime()) {
                    temp.put("进场时间", sdf.format(actualLog.getEntryTime()));
                }
                if (null != actualLog.getExitTime()) {
                    temp.put("出场时间", sdf.format(actualLog.getExitTime()));
                }
            }
            log.setLogJson(JSONArray.toJSONString(temp));
            clearanceLogProvider.updateClearanceLog(log);
        }
        return actualLogs;
    }


    private ParkingVendorHandler getParkingVendorHandler(String vendorName) {
        ParkingVendorHandler handler = null;

        if(vendorName != null && vendorName.length() > 0) {
            String handlerPrefix = ParkingVendorHandler.PARKING_VENDOR_PREFIX;
            handler = PlatformContext.getComponent(handlerPrefix + vendorName);
        }

        return handler;
    }
}
