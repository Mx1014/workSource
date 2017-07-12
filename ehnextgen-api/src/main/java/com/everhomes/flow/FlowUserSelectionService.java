package com.everhomes.flow;

import com.everhomes.rest.flow.FlowOwnerType;

import java.util.List;

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
	 * @param departmentId 项目ID
	 * @return
	 */
	List<Long> findUsersByJobPositionId(Long parentOrgId, Long jobPositionId,
			Long departmentId);
	
	/**
	 * 查询某个部门下的所有经理
	 * @param parentOrgId
	 * @param departmentId
	 * @return
	 */
	List<Long> findManagersByDepartmentId(Long parentOrgId, Long departmentId, Flow flow);

    /**
     * 业务责任部门的经理
     */
    List<Long> findModuleDutyManagers(Long organizationId, Long moduleId, String projectType, Long projectId);

    List<Long> findUsersByDudy(Long parentOrgId, Long moduleId, String departmentType, Long departmentId, Long jobPositionId);
}
