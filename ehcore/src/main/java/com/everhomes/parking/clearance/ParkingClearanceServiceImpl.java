// @formatter:off
package com.everhomes.parking.clearance;

import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.*;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.parking.ParkingCommonStatus;
import com.everhomes.rest.parking.ParkingLocalStringCode;
import com.everhomes.rest.parking.clearance.*;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.serviceModule.ServiceModule;
import com.everhomes.serviceModule.ServiceModuleProvider;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.everhomes.util.RuntimeErrorException.errorWith;

/**
 * Parking clearance service
 * Created by xq.tian on 2016/12/2.
 */
@Service
public class ParkingClearanceServiceImpl implements ParkingClearanceService, FlowModuleListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParkingClearanceServiceImpl.class);
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private static final Long MODULE_ID = 41500L;// 模块id
    // private static final String MODULE_NAME = "parkingClearance";// 模块名称

    private static final Long APPLY_PRIVILEGE_ID = 2L;// 处理车辆放行权限id
    private static final Long HAND_PRIVILEGE_ID = 2L;// 处理车辆放行权限id

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
    private ConfigurationProvider configurationProvider;

    @Autowired
    private LocaleStringService localeStringService;

    @Autowired
    private ServiceModuleProvider moduleProvider;

    @Autowired
    private LocaleTemplateService localeTemplateService;

    @Override
    public ParkingClearanceOperatorDTO createClearanceOperator(CreateClearanceOperatorCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        User user = this.findUserById(cmd.getUserId());
        ParkingClearanceOperator operator = new ParkingClearanceOperator();
        operator.setOrganizationId(cmd.getOrganizationId());
        operator.setCommunityId(cmd.getCommunityId());
        operator.setNamespaceId(currNamespaceId());
        operator.setParkingLotId(cmd.getParkingLotId());
        operator.setOperatorType(cmd.getOperatorType());
        operator.setOperatorId(user.getId());
        operator.setStatus(ParkingCommonStatus.ACTIVE.getCode());
        clearanceOperatorProvider.createClearanceOperator(operator);

        this.assignmentPrivileges(cmd);

        return toClearanceOperatorDTO(operator);
    }

    // 给用户添加权限
    // 申请放行权限或者处理权限
    private void assignmentPrivileges(CreateClearanceOperatorCommand cmd) {
        ParkingClearanceOperatorType operatorType = ParkingClearanceOperatorType.fromCode(cmd.getOperatorType());
        long privilegeId = (operatorType == ParkingClearanceOperatorType.APPLICANT) ? APPLY_PRIVILEGE_ID : HAND_PRIVILEGE_ID;
        rolePrivilegeService.assignmentPrivileges(EntityType.PARKING_LOT.getCode(), cmd.getParkingLotId(),
                EntityType.USER.getCode(), cmd.getUserId(), cmd.getOperatorType(), Collections.singletonList(privilegeId));
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
        return ConvertHelper.convert(log, ParkingClearanceLogDTO.class);
    }

    @Override
    public void deleteClearanceOperator(DeleteClearanceOperatorCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        clearanceOperatorProvider.deleteClearanceOperatorById(currNamespaceId(), cmd.getId());
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
        this.checkApplyClearanceAuthority(cmd);

        ParkingClearanceLog log = new ParkingClearanceLog();
        log.setStatus(ParkingClearanceLogStatus.PENDING.getCode());
        log.setParkingLotId(cmd.getParkingLotId());
        log.setCommunityId(cmd.getCommunityId());
        log.setApplicantId(currUserId());
        log.setClearanceTime(new Timestamp(cmd.getClearanceTime()));
        log.setRemarks(cmd.getRemarks());
        log.setPlateNumber(cmd.getPlateNumber());
        log.setApplyTime(Timestamp.from(Instant.now()));

        clearanceLogProvider.createClearanceLog(log);

        this.createFlowCase(log);
        return toClearanceLogDTO(log);
    }

    private void checkApplyClearanceAuthority(CreateClearanceLogCommand cmd) {
        // 校验当前用户是否有申请放行权限
        userPrivilegeMgr.checkUserAuthority(currUserId(), EntityType.PARKING_LOT.getCode(), cmd.getParkingLotId(),
                cmd.getOrganizationId(), APPLY_PRIVILEGE_ID);
    }

    private void createFlowCase(ParkingClearanceLog log) {
        Flow flow = flowService.getEnabledFlow(currNamespaceId(), MODULE_ID, null, log.getParkingLotId(), EntityType.PARKING_LOT.getCode());
        if (flow != null) {
            CreateFlowCaseCommand flowCaseCmd = new CreateFlowCaseCommand();
            flowCaseCmd.setApplyUserId(currUserId());
            flowCaseCmd.setFlowMainId(flow.getFlowMainId());
            flowCaseCmd.setFlowVersion(flow.getFlowVersion());
            flowCaseCmd.setReferId(log.getId());
            flowCaseCmd.setReferType(EntityType.PARKING_CLEARANCE_LOG.getCode());
            // flowCase摘要内容
            String contentLocaleString = localeStringService.getLocalizedString(ParkingLocalStringCode.SCOPE,
                    ParkingLocalStringCode.CLEARANCE_FLOW_CASE_BRIEF_CONTENT, currLocale(), "");
            flowCaseCmd.setContent(String.format(contentLocaleString, log.getPlateNumber(), log.getClearanceTime()));

            flowService.createFlowCase(flowCaseCmd);
        }
    }

    @Override
    public SearchClearanceLogsResponse searchClearanceLog(SearchClearanceLogCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        SearchClearanceLogsResponse response = new SearchClearanceLogsResponse();
        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        ParkingClearanceLogQueryObject qo = ConvertHelper.convert(cmd, ParkingClearanceLogQueryObject.class);
        qo.setPageSize(pageSize + 1);
        List<ParkingClearanceLog> logs = clearanceLogProvider.searchClearanceLog(qo);
        if (logs != null) {
            if (logs.size() > pageSize) {
                response.setNextPageAnchor(logs.get(logs.size() - 1).getCreateTime().getTime());
            }
            response.setLogs(logs.stream().limit(pageSize).map(this::toClearanceLogDTO).collect(Collectors.toList()));
        }
        return response;
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
        if (orgId == null) {
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
        }
    }

    // 参数校验方法
    // 可以校验带bean validation 注解的对象
    // 校验失败, 抛出异常, 异常信息附带参数值信息
    private void validate(Object o) {
        Set<ConstraintViolation<Object>> result = validator.validate(o);
        if (!result.isEmpty()) {
            result.stream().map(r -> r.getPropertyPath().toString() + " [ " + r.getInvalidValue() + " ]")
                    .reduce((i, a) -> i + ", " + a).ifPresent(r -> {
                LOGGER.error("Invalid parameter {}", r);
                throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid parameter %s", r);
            });
        }
    }

    @Override
    public FlowModuleInfo initModule() {
        FlowModuleInfo moduleInfo = new FlowModuleInfo();
        ServiceModule module = moduleProvider.findServiceModuleById(MODULE_ID);
        moduleInfo.setModuleName(module.getName());
        moduleInfo.setModuleId(MODULE_ID);
        return moduleInfo;
    }

    @Override
    public void onFlowCaseStart(FlowCaseState ctx) {

    }

    @Override
    public void onFlowCaseAbsorted(FlowCaseState ctx) {

    }

    @Override
    public void onFlowCaseStateChanged(FlowCaseState ctx) {

    }

    @Override
    public void onFlowCaseEnd(FlowCaseState ctx) {

    }

    @Override
    public void onFlowCaseActionFired(FlowCaseState ctx) {

    }

    @Override
    public String onFlowCaseBriefRender(FlowCase flowCase) {
        return flowCase.getContent();
    }

    @Override
    public List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase) {
        ParkingClearanceLog log = clearanceLogProvider.findById(flowCase.getReferId());
        User applicant = this.findUserById(log.getApplicantId());
        UserIdentifier userIdentifier = this.findUserIdentifierByUserId(applicant.getId());

        Map<String, Object> map = new HashMap<>();

        map.put("", applicant.getNickName());
        map.put("", userIdentifier.getIdentifierToken());
        map.put("", log.getPlateNumber());
        String dateStr = DateHelper.getDateDisplayString(TimeZone.getDefault(), log.getClearanceTime().getTime(), "yyyy-MM-dd HH:mm");
        map.put("", dateStr);
        map.put("", log.getRemarks());


        String detailJson = localeTemplateService.getLocaleTemplateString(ParkingLocalStringCode.SCOPE_TEMPLATE,
                ParkingLocalStringCode.CLEARANCE_FLOW_CASE_DETAIL_CONTENT, currLocale(), map, "");

        // return (FlowCaseEntityList)StringHelper.fromJsonString(detailJson, FlowCaseEntityList.class);
        // return entityList;
        return null;
    }

    @Override
    public String onFlowVariableRender(FlowCaseState ctx, String variable) {
        return null;
    }
}
