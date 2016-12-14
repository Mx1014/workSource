package com.everhomes.flow;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.organization.Organization;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.user.UserInfo;

@Component
public class FlowUserSelectionServiceImpl implements FlowUserSelectionService {

	@Autowired
	private FlowUserSelectionProvider flowUserSelectionProvider;

	@Override
	public List<UserInfo> findUsersBySelection(FlowUserSelection userSelection) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserInfo> findUsersByOrganizationId(Long orgId,
			FlowOwnerType ownerType, Boolean includeAll) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserInfo> findUsersByJobPosition(Long parentOrgId,
			Long jobPositionId, Long departmentId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserInfo> findManagersByDepartmentId(Long parentOrgId,
			Long departmentId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Organization> listOrganizations(Long parentOrgId,
			Boolean includeAll) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void listJobPositionsByOrganizationId(Long parentOrgId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Long selectFlowSuperviser(Long flowId, FlowUserSelection selection) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long selectFlowNodeProcessor(Long flowNodeId,
			FlowUserSelection selection) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long selectActionUser(Long actionId, FlowUserSelection selection) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
