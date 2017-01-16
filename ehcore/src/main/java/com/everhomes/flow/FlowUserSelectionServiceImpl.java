package com.everhomes.flow;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.organization.ListOrganizationContactByJobPositionIdCommand;
import com.everhomes.rest.organization.ListOrganizationManagersCommand;
import com.everhomes.rest.organization.OrganizationContactDTO;
import com.everhomes.rest.organization.OrganizationManagerDTO;
import com.everhomes.rest.organization.OrganizationMemberTargetType;
import com.everhomes.rest.user.UserInfo;

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
		
		ListOrganizationContactByJobPositionIdCommand cmd = new ListOrganizationContactByJobPositionIdCommand();
		cmd.setJobPositionId(jobPositionId);
		cmd.setOrganizationId(departmentId);
		
		List<OrganizationContactDTO> dtos = organizationService.listOrganizationContactByJobPositionId(cmd);
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
}
