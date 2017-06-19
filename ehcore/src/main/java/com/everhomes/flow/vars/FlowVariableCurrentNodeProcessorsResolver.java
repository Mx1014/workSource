package com.everhomes.flow.vars;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.everhomes.flow.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.flow.FlowEntityType;
import com.everhomes.rest.flow.FlowUserType;

/**
 * <ul>
 * <li>当前节点的所有处理人员</li>
 * </ul>
 * @author janson
 *
 */
@Component(FlowVariableUserResolver.CURR_NODE_PROCESSORS)
public class FlowVariableCurrentNodeProcessorsResolver implements FlowVariableUserResolver {
	private static final Logger LOGGER = LoggerFactory.getLogger(FlowVariableCurrentNodeProcessorResolver.class);

	@Autowired
    FlowUserSelectionProvider flowUserSelectionProvider;
	
	@Autowired
    FlowService flowService;
	
	@Override
	public List<Long> variableUserResolve(FlowCaseState ctx, Map<String, Long> processedEntities,
                                          FlowEntityType fromEntity, Long entityId,
                                          FlowUserSelection userSelection, int loopCnt) {
		if(ctx.getCurrentNode() != null && ctx.getFlowCase() != null) {
			FlowNode node = ctx.getCurrentNode().getFlowNode();
			List<FlowUserSelection> selections = flowUserSelectionProvider.findSelectionByBelong(node.getId()
					, FlowEntityType.FLOW_NODE.getCode(), FlowUserType.PROCESSOR.getCode(), ctx.getFlowCase().getFlowVersion());
			return flowService.resolvUserSelections(ctx, processedEntities, FlowEntityType.FLOW_NODE, node.getId(), selections, loopCnt);			
		}

		//empty results
		return new ArrayList<Long>();
	}
}
