// @formatter:off
package com.everhomes.parking.clearance;

import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.entity.EntityType;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.parking.ParkingCommonStatus;
import com.everhomes.rest.parking.clearance.*;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.everhomes.util.RuntimeErrorException.errorWith;
import static org.bouncycastle.asn1.x500.style.RFC4519Style.o;

/**
 * Parking clearance service
 * Created by xq.tian on 2016/12/2.
 */
@Service
public class ParkingClearanceServiceImpl implements ParkingClearanceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParkingClearanceServiceImpl.class);
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private static final Long APPLY_PRIVILEGE_ID = 1L;// 申请车辆放行权限id
    private static final Long HAND_PRIVILEGE_ID = 1L;// 处理车辆放行权限id

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

    @Override
    public ParkingClearanceOperatorDTO createClearanceOperator(CreateClearanceOperatorCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        User user = this.findUserById(cmd.getUserId());
        ParkingClearanceOperator operator = new ParkingClearanceOperator();
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

    @Override
    public void deleteClearanceOperator(DeleteClearanceOperatorCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
    }

    @Override
    public ListClearanceOperatorResponse listClearanceOperator(ListClearanceOperatorCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        return null;
    }

    @Override
    public ParkingClearanceLogDTO createClearanceLog(CreateClearanceLogCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        return null;
    }

    @Override
    public SearchClearanceLogsResponse searchClearanceLog(SearchClearanceLogCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        return null;
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
        Long userId = UserContext.current().getUser().getId();
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
}
