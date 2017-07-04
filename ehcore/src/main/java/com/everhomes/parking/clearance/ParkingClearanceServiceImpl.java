// @formatter:off
package com.everhomes.parking.clearance;

import com.everhomes.acl.RolePrivilegeService;
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
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.parking.ParkingLot;
import com.everhomes.parking.ParkingProvider;
import com.everhomes.rest.energy.util.ParamErrorCodes;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.launchpad.ActionType;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.parking.ParkingCommonStatus;
import com.everhomes.rest.parking.ParkingErrorCode;
import com.everhomes.rest.parking.ParkingLocalStringCode;
import com.everhomes.rest.parking.ParkingOwnerType;
import com.everhomes.rest.parking.clearance.*;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.DateUtil;
import com.everhomes.user.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.excel.ExcelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Size;
import javax.validation.metadata.ConstraintDescriptor;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.everhomes.rest.parking.ParkingLocalStringCode.SCOPE_STRING_STATUS;
import static com.everhomes.rest.parking.clearance.ParkingClearanceConst.*;
import static com.everhomes.util.RuntimeErrorException.errorWith;


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
    private OrganizationProvider organizationProvider;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private RolePrivilegeService rolePrivilegeService;

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

    @Override
    public void createClearanceOperator(CreateClearanceOperatorCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

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
                newOperator.setNamespaceId(currNamespaceId());
                newOperator.setParkingLotId(cmd.getParkingLotId());
                newOperator.setOperatorType(cmd.getOperatorType());
                newOperator.setOperatorId(user.getId());
                newOperator.setStatus(ParkingCommonStatus.ACTIVE.getCode());
                dbProvider.execute(status -> {
                    clearanceOperatorProvider.createClearanceOperator(newOperator);
                    this.addFlowProcessor(cmd, userId);
                    this.assignmentPrivileges(cmd.getOperatorType(), cmd.getParkingLotId(), userId);
                    return true;
                });
            }
            // 此次添加的用户都已经在数据库里了, 就给个提示, 测试要求的
            if (alreadyInsertUserCount == cmd.getUserIds().size()) {
                throw errorWith(ParkingErrorCode.SCOPE_CLEARANCE, ParkingErrorCode.ERROR_USER_ALREADY_IN_DATABASE, "All user is in database");
            }
        }
    }

    // 添加工作流处理人员
    private void addFlowProcessor(CreateClearanceOperatorCommand cmd, Long userId) {
        if (ParkingClearanceOperatorType.fromCode(cmd.getOperatorType()) == ParkingClearanceOperatorType.PROCESSOR) {
            Flow flow = flowService.getEnabledFlow(currNamespaceId(), MODULE_ID, null, cmd.getParkingLotId(), FlowOwnerType.PARKING.getCode());
            if (flow == null) {
                LOGGER.error("There is no workflow enabled.");
                throw errorWith(ParkingErrorCode.SCOPE_CLEARANCE, ParkingErrorCode.ERROR_NO_WORK_FLOW_ENABLED, "There is no workflow enabled.");
            }
            flowService.addSnapshotProcessUser(flow.getFlowMainId(), userId);
        }
    }

    // 校验当前用户是否有申请放行权限
    private void checkApplicantAuthority(CreateClearanceLogCommand cmd) {
//        userPrivilegeMgr.checkUserAuthority(
//                currUserId(),
//                EntityType.PARKING_LOT.getCode(),
//                cmd.getParkingLotId(),
//                cmd.getOrganizationId(),
//                APPLY_PRIVILEGE_ID
//        );
        // 上面的权限检查会放过超级管理员, 但是需求是不放过
        checkUserNotInOperatorList(cmd.getParkingLotId(), ParkingClearanceOperatorType.APPLICANT);
    }

    private void checkUserNotInOperatorList(Long parkingLotId, ParkingClearanceOperatorType operatorType) {
        ParkingClearanceOperator applicant = clearanceOperatorProvider
                .findByParkingLotIdAndUid(parkingLotId, currUserId(), operatorType.getCode());
        if (applicant == null) {
            LOGGER.error("Current user is not in operator list");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                    "Current user is not in operator list");
        }
    }

    // 给用户添加权限
    // 申请放行权限或者处理放行任务权限
    private void assignmentPrivileges(String operatorType,Long parkingLotId, Long userId) {
        rolePrivilegeService.assignmentPrivileges(EntityType.PARKING_LOT.getCode(),
                parkingLotId, EntityType.USER.getCode(), userId, operatorType, Collections.singletonList(this.getPrivilegeId(operatorType)));
    }

    private long getPrivilegeId(String operatorType) {
        return (ParkingClearanceOperatorType.fromCode(operatorType) == ParkingClearanceOperatorType.APPLICANT) ?
                APPLY_PRIVILEGE_ID : PROCESS_PRIVILEGE_ID;
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
        if (log.getRemarks() == null) {
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

    @Override
    public void deleteClearanceOperator(DeleteClearanceOperatorCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        coordinationProvider.getNamedLock(CoordinationLocks.PARKING_CLEARANCE_OPERATOR.getCode() + cmd.getId()).tryEnter(() -> {
            ParkingClearanceOperator operator = clearanceOperatorProvider.findById(cmd.getId());
            if (operator != null) {
                boolean success = dbProvider.execute(status -> {
                    long privilegeId = this.getPrivilegeId(operator.getOperatorType());
                    // 删除工作流处理人员
                    this.deleteFlowProcessor(operator);
                    // 删除权限
                    rolePrivilegeService.deleteAcls(
                            EntityType.PARKING_LOT.getCode(),//
                            operator.getParkingLotId(),//
                            EntityType.USER.getCode(),//
                            operator.getOperatorId(),//
                            new ArrayList<>(Collections.singletonList(privilegeId))
                    );
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

    private void deleteFlowProcessor(ParkingClearanceOperator operator) {
        if (ParkingClearanceOperatorType.fromCode(operator.getOperatorType()) == ParkingClearanceOperatorType.PROCESSOR) {
            Flow flow = flowService.getEnabledFlow(currNamespaceId(), MODULE_ID, null, operator.getParkingLotId(), FlowOwnerType.PARKING.getCode());
            // 删除当前工作流的处理人员
            if (flow != null) {
                flowService.deleteSnapshotProcessUser(flow.getFlowMainId(), operator.getOperatorId());
            }
        }
    }

    @Override
    public ListClearanceOperatorResponse listClearanceOperator(ListClearanceOperatorCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        ListClearanceOperatorResponse response = new ListClearanceOperatorResponse();

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        List<ParkingClearanceOperator> operators = clearanceOperatorProvider.listClearanceOperator(currNamespaceId(),
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
        this.validate(cmd);
        this.checkCurrentUserNotInOrg(cmd.getOrganizationId());
        this.checkApplicantAuthority(cmd);

        ParkingClearanceLog log = new ParkingClearanceLog();
        log.setNamespaceId(currNamespaceId());
        log.setStatus(ParkingClearanceLogStatus.PENDING.getCode());
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
            this.createFlowCase(log, cmd.getOrganizationId());
            return true;
        });
        if (!success) {
            LOGGER.error("some error.");
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "some error.");
        }
        return toClearanceLogDTO(log);
    }

    private Long createFlowCase(ParkingClearanceLog log, Long organizationId) {
        Flow flow = flowService.getEnabledFlow(currNamespaceId(), MODULE_ID, null, log.getParkingLotId(), FlowOwnerType.PARKING.getCode());
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
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
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
    	cmd.setPageAnchor(null);
    	cmd.setPageSize(10000);
    	SearchClearanceLogsResponse searchClearanceLogsResponse = searchClearanceLog(cmd);
    	List<ParkingClearanceLogDTO> logs = searchClearanceLogsResponse.getLogs();
    	if (logs != null && logs.size() > 0) {
    		logs.forEach(this::formatToExport);
    		String fileName = String.format("车辆放行记录_%s", DateUtil.dateToStr(new Date(), DateUtil.DATE_TIME_NO_SLASH));
			ExcelUtils excelUtils = new ExcelUtils(response, fileName, "车辆放行记录");
			List<String> propertyNames = new ArrayList<String>(Arrays.asList("applicant", "identifierToken", "applyTimeString", "plateNumber", "clearanceTimeString", "status", "remarks"));
			List<String> titleNames = new ArrayList<String>(Arrays.asList("发起人", "手机号", "发起时间", "来访车辆", "预计来访时间", "任务状态", "备注"));
			List<Integer> titleSizes = new ArrayList<Integer>(Arrays.asList(20, 20, 20, 20, 20, 20, 20, 30));
			excelUtils.setNeedSequenceColumn(true);
			excelUtils.writeExcel(propertyNames, titleNames, titleSizes, logs);
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
        // checkCurrentUserNotInOrg(cmd.getOrganizationId());

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
            privilegeId = PROCESS_PRIVILEGE_ID;
            operatorType = ParkingClearanceOperatorType.PROCESSOR;
            message = localeStringService.getLocalizedString(ParkingLocalStringCode.SCOPE_STRING,
                    ParkingLocalStringCode.INSUFFICIENT_PRIVILEGE_CLEARANCE_TASK_MESSAGE_CODE, currLocale(), "");
        }

        List<ParkingLot> parkingLots = new ArrayList<>();
        if (cmd.getParkingLotId() != null) {
            ParkingLot parkingLot = parkingProvider.findParkingLotById(cmd.getParkingLotId());
            if (parkingLot != null) {
                parkingLots.add(parkingLot);
            }
        } else {
            parkingLots = parkingProvider.listParkingLots(ParkingOwnerType.COMMUNITY.getCode(), cmd.getCommunityId());
        }

        CheckAuthorityStatus status = CheckAuthorityStatus.FAILURE;
        if (privilegeId > 0 && parkingLots != null && parkingLots.size() > 0) {
            for (ParkingLot parkingLot : parkingLots) {
                try {
                    userPrivilegeMgr.checkUserAuthority(currUserId(), EntityType.PARKING_LOT.getCode(), parkingLot.getId(),
                             cmd.getOrganizationId(), privilegeId);

                    // 上面的权限检查会放过超级管理员, 但是需求是不放过
                    checkUserNotInOperatorList(parkingLot.getId(), operatorType);

                    status = CheckAuthorityStatus.SUCCESS;
                    message = null;
                    break;// 只要有一个停车场的权限就放行
                } catch (RuntimeErrorException ree) {
                    // ignore
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

    private Integer currNamespaceId() {
        return UserContext.getCurrentNamespaceId();
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

    private void checkCurrentUserNotInOrg(Long orgId) {
        /*if (orgId == null) {
            LOGGER.error("Invalid parameter organizationId [ null ]");
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid parameter organizationId [ null ]");
        }
        Long userId = currUserId();
        OrganizationMember member = this.organizationProvider.findOrganizationMemberByOrgIdAndUId(userId, orgId);
        if (member == null) {
            LOGGER.error("User is not in the organization.");
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "User is not in the organization.");
        }*/
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
}
