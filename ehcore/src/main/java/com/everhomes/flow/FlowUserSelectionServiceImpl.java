package com.everhomes.flow;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.organization.Organization;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.user.UserInfo;

@Component
public class FlowUserSelectionServiceImpl implements FlowUserSelectionService {

	@Override
	public List<Long> findUsersByOrganizationId(Long orgId,
			FlowOwnerType ownerType, Boolean includeAll) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Long> findUsersByJobPositionId(Long parentOrgId,
			Long jobPositionId, Long departmentId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Long> findManagersByDepartmentId(Long parentOrgId,
			Long departmentId) {
		// TODO Auto-generated method stub
		return null;
	}
}
