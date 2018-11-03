// @formatter:off
package com.everhomes.flow;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.enterprise.EnterpriseContactService;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.flow.FlowEntityType;
import com.everhomes.rest.flow.FlowUserSelectionType;
import com.everhomes.rest.flow.FlowUserSourceType;
import com.everhomes.rest.organization.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class FlowUserSelectionServiceImpl implements FlowUserSelectionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowUserSelectionServiceImpl.class);

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private FlowVariableProvider flowVariableProvider;

    @Autowired
    private EnterpriseContactService enterpriseContactService;

    /**
     * <ul>此函数需要关注三个问题：
     * <li> 1. 变量引用，不能循环引用。 </li>
     * <li> 2. 不能过深的循环 </li>
     * <li> 3. 很多情况只需要求得部分值，不需要求得全部值 </li>
     * </ul>
     * @param ctx 当前工作流上下文
     * @param processedEntities 已经处理过的对象
     *
     */
    @Override
    public List<Long> resolveUserSelections(FlowCaseState ctx, Map<String, Long> processedEntities, FlowEntityType entityType,
                                            Long entityId, List<FlowUserSelection> selections, int loopCnt, int maxCount) {
        List<Long> users = new ArrayList<>();

        //判断是否调用层次过深，避免让服务器崩溃
        if (selections == null || loopCnt >= 5) {
            return users;
        }

        Flow flow = ctx.getFlowGraph().getFlow();
        Long orgId = flow.getOrganizationId();

        FlowCase flowCase = ctx.getFlowCase();

        String projectType = (flowCase.getProjectTypeA() != null && flowCase.getProjectTypeA().length() > 0)
                ? flowCase.getProjectTypeA() : flowCase.getProjectType();

        Long projectId = (flowCase.getProjectIdA() != null && flowCase.getProjectIdA() != 0)
                ? flowCase.getProjectIdA() : flowCase.getProjectId();

        for (FlowUserSelection sel : selections) {
            if (users.size() >= maxCount) {
                //为了加快处理的速度，有的情况不需要拿太多用户
                break;
            }

            if (sel.getId() != null) {
                //判断是否已经处理过，避免循环引用。如果直接是用户选择，则不需要判断
                String key = "sel:" + sel.getId();
                if (processedEntities.containsKey(key)) {
                    continue;
                }
                processedEntities.put(key, 1L);
            }

            // 用户
            if (FlowUserSourceType.SOURCE_USER.getCode().equals(sel.getSourceTypeA())) {
                users.add(sel.getSourceIdA());
            }
            // 岗位
            else if (FlowUserSelectionType.POSITION.getCode().equals(sel.getSelectType())) {
                //sourceA is position, sourceB is department
                Long organizationId = orgId;
                if (sel.getOrganizationId() != null && sel.getOrganizationId() != 0) {
                    organizationId = sel.getOrganizationId();
                }

                List<Long> userIds = null;
                FlowUserSourceType sourceTypeB = FlowUserSourceType.fromCode(sel.getSourceTypeB());
                switch (sourceTypeB) {
                    // 业务责任部门岗位
                    case SOURCE_DUTY_DEPARTMENT:// 旧版本数据
                    case SOURCE_BUSINESS_DEPARTMENT:
                        userIds = listUsersByBusinessDepartment(organizationId, flow.getModuleId(), sel.getSourceIdA(), projectType, projectId);
                        break;
                    // 具体部门岗位
                    case SOURCE_DEPARTMENT:
                        // 老的数据和新的数据不一样，需要按照时间来区分，以后把这个删掉
                        if (sel.getCreateTime().toLocalDateTime()
                                .isBefore(LocalDateTime.of(LocalDate.of(2018, 1, 18), LocalTime.MIN))) {
                            LOGGER.debug("Source Department invoke listUsersByJobPosition, " +
                                            "organizationId = {}, sourceIdB = {}, sourceIdA = {}",
                                    organizationId, sel.getSourceIdB(), sel.getSourceIdA());
                            userIds = listUsersByJobPosition(organizationId, sel.getSourceIdB(), sel.getSourceIdA());
                        } else {
                            LOGGER.debug("Source Department invoke listUsersByDepartmentJobPosition," +
                                            " organizationId = {}, sourceIdB = {}, sourceIdA = {}",
                                    organizationId, sel.getSourceIdB(), sel.getSourceIdA());
                            userIds = listUsersByDepartmentJobPosition(sel.getNamespaceId(), organizationId, sel.getSourceIdB(), sel.getSourceIdA());
                        }
                        break;
                    // 不限部门岗位
                    case SOURCE_UNLIMITED_DEPARTMENT:
                        userIds = listUsersByJobPosition(organizationId, organizationId, sel.getSourceIdA());
                        break;
                    // 旧版本的不限部门没有sourceTypeB
                    default:
                        userIds = listUsersByJobPosition(organizationId, organizationId, sel.getSourceIdA());
                        break;
                }

                if (userIds != null) {
                    users.addAll(userIds);
                }

                /*if (sel.getSourceIdB() != null) {
                    if (FlowUserSourceType.SOURCE_DUTY_DEPARTMENT.getCode().equals(sel.getSourceTypeB())) {
                        FlowCase flowCase = ctx.getFlowCase();
                        List<Long> tmp = flowUserSelectionService.findUsersByDudy(organizationId, flowCase.getModuleId(), flowCase.getProjectType(), flowCase.getProjectId(), sel.getSourceIdA());
                        users.addAll(tmp);
                        continue;
                    }

                    if (!sel.getSourceIdB().equals(0L)
                            && FlowUserSourceType.SOURCE_DEPARTMENT.getCode().equals(sel.getSourceTypeB())) {
                        departmentId = sel.getSourceIdB();
                    }
                }

                // LOGGER.error("position selId= " + sel.getId() + " positionId= " + sel.getSourceIdA() + " departmentId= " + departmentId);
                if (FlowUserSourceType.SOURCE_POSITION.getCode().equals(sel.getSourceTypeA())) {
                    List<Long> tmp = flowUserSelectionService.findUsersByJobPositionId(organizationId, sel.getSourceIdA(), departmentId);
                    if (tmp != null) {
                        users.addAll(tmp);
                    }
                } else {
                    LOGGER.error("resolvUser selId= " + sel.getId() + " position parse error!");
                }*/

            }
            // 经理
            else if (FlowUserSelectionType.MANAGER.getCode().equals(sel.getSelectType())) {
                Long organizationId = orgId;
                if (sel.getOrganizationId() != null && sel.getOrganizationId() != 0) {
                    organizationId = sel.getOrganizationId();
                }

                List<Long> userIds = null;
                FlowUserSourceType sourceTypeA = FlowUserSourceType.fromCode(sel.getSourceTypeA());
                switch (sourceTypeA) {
                    // 业务责任部门经理
                    case SOURCE_DUTY_MANAGER:// 旧版本数据
                    case SOURCE_BUSINESS_DEPARTMENT:
                        userIds = listModuleBusinessManagers(organizationId, flow.getModuleId(), projectType, projectId);
                        break;
                    // 具体部门经理
                    case SOURCE_DEPARTMENT:
                        userIds = listManagersByDepartment(organizationId, sel.getSourceIdA());
                        break;
                    // 不限部门经理
                    case SOURCE_UNLIMITED_DEPARTMENT:
                        userIds = listManagersByDepartment(organizationId, organizationId);
                        break;
                    // 旧版本的不限部门没有sourceTypeA
                    default:
                        userIds = listManagersByDepartment(organizationId, organizationId);
                        break;
                        // LOGGER.warn("Flow user selection manager sourceTypeA '{}' not found.", sel.getSourceTypeA());
                }

                if (userIds != null) {
                    users.addAll(userIds);
                }

                /*if (sel.getSourceTypeA() == null
                        || FlowUserSourceType.SOURCE_DEPARTMENT.getCode().equals(sel.getSourceTypeA())) {
                    if (null != sel.getSourceIdA() && !sel.getSourceIdA().equals(0L)) {
                        departmentId = sel.getSourceIdA();
                    }

                    List<Long> tmp = flowUserSelectionService.listManagersByDepartmentId(parentOrgId, departmentId, ctx.getFlowGraph().getFlow());
                    users.addAll(tmp);
                } else if (FlowUserSourceType.SOURCE_DUTY_MANAGER.getCode().equals(sel.getSourceTypeA())) {
                    List<Long> idList = flowUserSelectionService.listModuleBusinessManagers(departmentId, flow.getModuleId(), flow.getProjectType(), flow.getProjectId());
                    users.addAll(idList);
                } else {
                    LOGGER.error("resolvUser selId= " + sel.getId() + " manager parse error!");
                }*/
            }
            // 变量
            else if (FlowUserSelectionType.VARIABLE.getCode().equals(sel.getSelectType())) {
                if (sel.getSourceIdA() != null) {
                    FlowVariable variable = flowVariableProvider.getFlowVariableById(sel.getSourceIdA());
                    FlowVariableUserResolver ftr = PlatformContext.getComponent(variable.getScriptCls());
                    if (ftr != null) {
                        List<Long> tmp = ftr.variableUserResolve(ctx, processedEntities, entityType, entityId, sel, loopCnt + 1);
                        if (null != tmp) {
                            users.addAll(tmp);
                        }
                    }
                } else {
                    LOGGER.error("user params error selId= " + sel.getId() + " variable error!");
                }
            }
        }
        return users.stream().distinct().collect(Collectors.toList());
    }

    private List<Long> listUsersByDepartmentJobPosition(Integer namespaceId, Long organizationId, Long departmentId, Long departmentJobPosId) {
        ListOrganizationContactCommand cmd = new ListOrganizationContactCommand();
        cmd.setEnterpriseId(organizationId);
        cmd.setOrganizationId(departmentJobPosId);
        cmd.setNamespaceId(namespaceId);
        cmd.setPageSize(10000);
        ListOrganizationMemberCommandResponse response = enterpriseContactService.listOrganizationPersonnels(cmd);
        if (response != null && response.getMembers() != null) {
            return response.getMembers().stream()
                    .filter(r -> OrganizationMemberTargetType.fromCode(r.getTargetType()) == OrganizationMemberTargetType.USER)
                    .map(OrganizationMemberDTO::getTargetId).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private List<Long> listUsersByJobPosition(Long organizationId, Long departmentId, Long jobPositionId) {
        List<OrganizationMember> members;
        if (Objects.equals(organizationId, departmentId)) {
            members = organizationService.listOrganizationContactByJobPositionId(organizationId, jobPositionId);
        } else {
            List<Long> organizationIds = Collections.singletonList(departmentId);
            members = organizationService.listOrganizationContactByJobPositionId(organizationIds, jobPositionId);
        }
        return toOrganizationMemberIds(members);
    }

    private List<Long> toOrganizationMemberIds(List<OrganizationMember> members) {
        if (members != null) {
            return members.stream()
                    .filter(r -> OrganizationMemberTargetType.fromCode(r.getTargetType()) == OrganizationMemberTargetType.USER)
                    .map(OrganizationMember::getTargetId).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private List<Long> listManagersByDepartment(Long organizationId, Long departmentId) {
        List<OrganizationManagerDTO> dtoList;
        if (Objects.equals(organizationId, departmentId)) {
            ListOrganizationManagersCommand cmd = new ListOrganizationManagersCommand();
            cmd.setOrganizationId(organizationId);
            dtoList = organizationService.listOrganizationAllManagers(cmd);
        } else {
            ListOrganizationManagersCommand cmd = new ListOrganizationManagersCommand();
            cmd.setOrganizationId(departmentId);
            dtoList = organizationService.listOrganizationManagers(cmd);
        }
        return toOrganizationManagerIds(dtoList);
    }

    /**
     * 业务责任部门的经理
     */
    private List<Long> listModuleBusinessManagers(Long organizationId, Long moduleId, String ownerType, Long ownerId) {
        ListOrganizationByModuleIdCommand cmd = new ListOrganizationByModuleIdCommand();
        cmd.setOrganizationId(organizationId);
        cmd.setModuleId(moduleId);
        cmd.setOwnerType(ownerType);
        cmd.setOwnerId(ownerId);

        List<OrganizationManagerDTO> managers = organizationService.listModuleOrganizationManagers(cmd);

        return toOrganizationManagerIds(managers);
    }

    private List<Long> toOrganizationManagerIds(List<OrganizationManagerDTO> managers) {
        if (managers != null) {
            return managers.stream()
                    .filter(r -> OrganizationMemberTargetType.fromCode(r.getTargetType()) == OrganizationMemberTargetType.USER)
                    .map(OrganizationManagerDTO::getTargetId)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * 业务责任部门用户
     */
    private List<Long> listUsersByBusinessDepartment(Long organizationId, Long moduleId, Long jobPositionId, String ownerType, Long ownerId) {
        ListModuleOrganizationContactByJobPositionIdCommand cmd = new ListModuleOrganizationContactByJobPositionIdCommand();
        cmd.setOrganizationId(organizationId);
        cmd.setModuleId(moduleId);
        cmd.setJobPositionId(jobPositionId);
        cmd.setOwnerType(ownerType);
        cmd.setOwnerId(ownerId);

        List<OrganizationContactDTO> dtoList = organizationService.listModuleOrganizationContactByJobPositionId(cmd);

        List<Long> users = new ArrayList<>();
        if (dtoList == null) {
            return users;
        }
        users = dtoList.parallelStream()
                .filter(r -> OrganizationMemberTargetType.USER.getCode().equals(r.getTargetType()))
                .map(OrganizationContactDTO::getTargetId)
                .collect(Collectors.toList());
        return users;
    }
}
