package com.everhomes.flow;

import com.everhomes.rest.flow.FlowEntityType;

import java.util.List;
import java.util.Map;

/**
 * <ul>
 * <li>APPLIER: 选择用户里的发起人</li>
 * <li>PREFIX_NODE_PROCESSOR: 选择用户里的上个节点执行人</li>
 * <li>CURRENT_NODE_PROCESSOR: 选择用户里的当前节点执行人</li>
 * <li>TARGET_NODE_PROCESSOR: 选择用户里的目标节点处理人</li>
 * <li>PREFIX_NODE_PROCESSORS: 选择用户的上一个目标节点所有处理人员</li>
 * <li>CURR_PROCESSORS: 选择用户的当前目标节点所有处理人员</li>
 * <li>SUPERVISOR: 选择用户里的督办人员</li>
 * </ul>
 * @author janson
 *
 */
public interface FlowVariableUserResolver {

	String APPLIER = "flow-variable-applier";
	String PREFIX_NODE_PROCESSOR = "flow-variable-prefix-node-processor";
	String CURRENT_NODE_PROCESSOR = "flow-variable-current-node-processor";
	String NEXT_NODE_PROCESSOR = "flow-variable-next-node-processor";
	String N_NODE_PROCESSOR = "flow-variable-n-node-processor";
	String SUPERVISOR = "flow-variable-supervisor";
	String TARGET_NODE_PROCESSOR = "flow-variable-target-node-processor";
	String PREFIX_NODE_PROCESSORS = "flow-variable-prefix-node-processors";
	String CURR_NODE_PROCESSORS = "flow-variable-curr-node-processors";
	String TARGET_NODE_TRANSFER = "flow-variable-target-node-transfer";

    String APPLIER_ORGANIZATION_MANAGER = "flow-variable-applier-organization-manager";// 发起人公司管理员
    String APPLIER_DEPARTMENT_MANAGER = "flow-variable-applier-department-manager";// 发起人部门经理

    List<Long> variableUserResolve(FlowCaseState ctx, Map<String, Long> processedEntities, FlowEntityType fromEntity,
                                   Long entityId, FlowUserSelection userSelection, int loopCnt);
}
