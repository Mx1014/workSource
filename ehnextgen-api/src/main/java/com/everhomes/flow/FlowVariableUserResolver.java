package com.everhomes.flow;

import java.util.List;
import java.util.Map;

import com.everhomes.rest.flow.FlowEntityType;

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
	public static final String APPLIER = "flow-variable-applier";
	public static final String PREFIX_NODE_PROCESSOR = "flow-variable-prefix-node-processor";
	public static final String CURRENT_NODE_PROCESSOR = "flow-variable-current-node-processor";
	public static final String NEXT_NODE_PROCESSOR = "flow-variable-next-node-processor";
	public static final String N_NODE_PROCESSOR = "flow-variable-n-node-processor";
	public static final String SUPERVISOR = "flow-variable-supervisor";
	public static final String TARGET_NODE_PROCESSOR = "flow-variable-target-node-processor";
	public static final String PREFIX_NODE_PROCESSORS = "flow-variable-prefix-node-processors";
	public static final String CURR_NODE_PROCESSORS = "flow-variable-curr-node-processors";
	public static final String TARGET_NODE_TRANSFER = "flow-variable-target-node-transfer";
	
	List<Long> variableUserResolve(FlowCaseState ctx, Map<String, Long> processedEntities, FlowEntityType fromEntity, Long entityId, FlowUserSelection userSelection, int loopCnt);
}
