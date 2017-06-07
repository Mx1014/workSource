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
	OrganizationService organizationService;
	
	@Override
	public List<Long> findUsersByOrganizationId(Long orgId,
			FlowOwnerType ownerType, Boolean includeAll) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Long> findUsersByJobPositionId(Long parentOrgId,
			Long jobPositionId, Long departmentId) {
		// DepartmentId is also organizationId
		
		/*ListOrganizationContactByJobPositionIdCommand cmd = new ListOrganizationContactByJobPositionIdCommand();
		cmd.setJobPositionId(jobPositionId);
		cmd.setOrganizationId(departmentId);*/

        List<Long> organizationIds = Collections.singletonList(departmentId);
        List<OrganizationMember> members = organizationService.listOrganizationContactByJobPositionId(organizationIds, jobPositionId);

		if(members != null) {
            return members.stream().filter(r ->
                    OrganizationMemberTargetType.fromCode(r.getTargetType()) == OrganizationMemberTargetType.USER)
                    .map(OrganizationMember::getTargetId).collect(Collectors.toList());
        }
		return new ArrayList<>();
	}

	@Override
	public List<Long> findManagersByDepartmentId(Long parentOrgId,
			Long departmentId, Flow flow) {
		List<Long> users = new ArrayList<>();
		List<OrganizationManagerDTO> dtos;
		
		if(departmentId.equals(parentOrgId)) {
			ListOrganizationManagersCommand cmd = new ListOrganizationManagersCommand();
			cmd.setOrganizationId(parentOrgId);
			cmd.setModuleId(flow.getModuleId());
			dtos = organizationService.listOrganizationAllManagers(cmd);
		} else {
			ListOrganizationManagersCommand cmd = new ListOrganizationManagersCommand();
			cmd.setOrganizationId(departmentId);
			dtos = organizationService.listOrganizationManagers(cmd);			
		}
		
		if(dtos != null) {
			for(OrganizationManagerDTO dto: dtos) {
				if(dto.getTargetType().equals(OrganizationMemberTargetType.USER.getCode())) {
					users.add(dto.getTargetId());	
				}
			}
		}

		return users;
	}
	
	@Override
	public List<Long> findUsersByDudy(Long parentOrgId, Long moduleId, String departmentType, Long departmentId) {		
		ListOrganizationByModuleIdCommand cmd = new ListOrganizationByModuleIdCommand();
		cmd.setModuleId(moduleId);
		cmd.setOrganizationId(parentOrgId);
		cmd.setOwnerType(departmentType);
		cmd.setOwnerId(departmentId);
		
		List<OrganizationContactDTO> dtos = organizationService.listOrganizationsContactByModuleId(cmd);
		List<Long> users = new ArrayList<>();
		if(dtos != null) {
			for(OrganizationContactDTO dto : dtos) {
				if(dto.getTargetType().equals(OrganizationMemberTargetType.USER.getCode())) {
					users.add(dto.getTargetId());	
				}
			}
		}
		
		return users;
	}
}
