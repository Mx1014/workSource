package com.everhomes.flow;

import java.util.List;

import com.everhomes.organization.Organization;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.user.UserInfo;

public interface FlowUserSelectionService {
	/**
	 * 请求一个 Organization 下的当前用户或者所有用户
	 * @param orgId
	 * @param ownerType
	 * @param includeAll
	 * @return
	 */
	List<Long> findUsersByOrganizationId(Long orgId, FlowOwnerType ownerType, Boolean includeAll);
	
	/**
	 * 查询某一个岗位以及某一个部门下的所有用户
	 * @param parentOrgId
	 * @param jobPositionId
	 * @param departmentId
	 * @return
	 */
	List<Long> findUsersByJobPositionId(Long parentOrgId, Long jobPositionId, Long departmentId);
	
	/**
	 * 查询某个部门下的所有经理
	 * @param parentOrgId
	 * @param departmentId
	 * @return
	 */
	List<Long> findManagersByDepartmentId(Long parentOrgId, Long departmentId, Flow flow);
}
