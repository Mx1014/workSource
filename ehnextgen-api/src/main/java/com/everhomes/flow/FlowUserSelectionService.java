package com.everhomes.flow;

import java.util.List;

import com.everhomes.organization.Organization;
import com.everhomes.rest.flow.FlowOwnerType;
import com.everhomes.rest.user.UserInfo;

public interface FlowUserSelectionService {
	/**
	 * 通过一个用户选择，获取所有的相关的用户
	 * @param userSelection
	 * @return
	 */
	List<UserInfo> findUsersBySelection(FlowUserSelection userSelection);
	
	
	/**
	 * 请求一个 Organization 下的当前用户或者所有用户
	 * @param orgId
	 * @param ownerType
	 * @param includeAll
	 * @return
	 */
	List<UserInfo> findUsersByOrganizationId(Long orgId, FlowOwnerType ownerType, Boolean includeAll);
	
	/**
	 * 查询某一个岗位以及某一个部门下的所有用户
	 * @param parentOrgId
	 * @param jobPositionId
	 * @param departmentId
	 * @return
	 */
	List<UserInfo> findUsersByJobPosition(Long parentOrgId, Long jobPositionId, Long departmentId);
	
	/**
	 * 查询某个部门下的所有经理
	 * @param parentOrgId
	 * @param departmentId
	 * @return
	 */
	List<UserInfo> findManagersByDepartmentId(Long parentOrgId, Long departmentId);
	
	/**
	 * 查询一个部门下的所有子部门
	 * @param parentOrgId
	 * @param includeAll
	 * @return
	 */
	List<Organization> listOrganizations(Long parentOrgId, Boolean includeAll);
	
	void listJobPositionsByOrganizationId(Long parentOrgId);
	
	/**
	 * 选择工作流的督办人员
	 * @param selection
	 * @return
	 */
	Long selectFlowSuperviser(Long flowId, FlowUserSelection selection);
	
	/**
	 * 某工作流节点处理人员
	 * @param flowNodeId
	 * @param selection
	 * @return
	 */
	Long selectFlowNodeProcessor(Long flowNodeId, FlowUserSelection selection);
	
	/**
	 * 某动作的处理用户
	 * @param actionId
	 * @param selection
	 * @return
	 */
	Long selectActionUser(Long actionId, FlowUserSelection selection);
}
