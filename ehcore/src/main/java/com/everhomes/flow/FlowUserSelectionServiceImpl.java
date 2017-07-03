// @formatter:off
package com.everhomes.flow;

import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.organization.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FlowUserSelectionServiceImpl implements FlowUserSelectionService {

    @Autowired
    private OrganizationService organizationService;

    @Override
    public List<Long> findUsersByOrganizationId(Long orgId,
                                                FlowOwnerType ownerType, Boolean includeAll) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Long> findUsersByJobPositionId(Long parentOrgId, Long jobPositionId, Long departmentId) {
        // DepartmentId is also organizationId

		/*ListOrganizationContactByJobPositionIdCommand cmd = new ListOrganizationContactByJobPositionIdCommand();
        cmd.setJobPositionId(jobPositionId);
		cmd.setOrganizationId(departmentId);*/

        List<Long> organizationIds = Collections.singletonList(departmentId);
        List<OrganizationMember> members = organizationService.listOrganizationContactByJobPositionId(organizationIds, jobPositionId);

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

    @Override
    public List<Long> findManagersByDepartmentId(Long parentOrgId, Long departmentId, Flow flow) {
        List<OrganizationManagerDTO> dtos;
        if (departmentId.equals(parentOrgId)) {
            ListOrganizationManagersCommand cmd = new ListOrganizationManagersCommand();
            cmd.setOrganizationId(parentOrgId);
            cmd.setModuleId(flow.getModuleId());
            dtos = organizationService.listOrganizationAllManagers(cmd);
        } else {
            ListOrganizationManagersCommand cmd = new ListOrganizationManagersCommand();
            cmd.setOrganizationId(departmentId);
            dtos = organizationService.listOrganizationManagers(cmd);
        }
        return toOrganizationManagerIds(dtos);
    }

    @Override
    public List<Long> findModuleDutyManagers(Long organizationId, Long moduleId, String projectType, Long projectId) {
        ListOrganizationByModuleIdCommand cmd = new ListOrganizationByModuleIdCommand();
        cmd.setOrganizationId(organizationId);
        cmd.setModuleId(moduleId);
        cmd.setOwnerType(projectType);
        cmd.setOwnerId(projectId);

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

    @Override
    public List<Long> findUsersByDudy(Long parentOrgId, Long moduleId, String projectType, Long projectId, Long jobPositionId) {
        // ListOrganizationByModuleIdCommand cmd = new ListOrganizationByModuleIdCommand();
        // cmd.setModuleId(moduleId);
        // cmd.setOrganizationId(parentOrgId);
        // cmd.setOwnerType(projectType);
        // cmd.setOwnerId(projectId);

        ListModuleOrganizationContactByJobPositionIdCommand cmd1 = new ListModuleOrganizationContactByJobPositionIdCommand();
        cmd1.setJobPositionId(jobPositionId);
        cmd1.setModuleId(moduleId);
        cmd1.setOrganizationId(parentOrgId);
        cmd1.setOwnerType(projectType);
        cmd1.setOwnerId(projectId);

        List<OrganizationContactDTO> dtos = organizationService.listModuleOrganizationContactByJobPositionId(cmd1);
        List<Long> users = new ArrayList<>();
        if (dtos != null) {
            for (OrganizationContactDTO dto : dtos) {
                if (dto.getTargetType().equals(OrganizationMemberTargetType.USER.getCode())) {
                    users.add(dto.getTargetId());
                }
            }
        }

        return users;
    }
}
