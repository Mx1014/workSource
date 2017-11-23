package com.everhomes.flow.vars;

import com.everhomes.flow.*;
import com.everhomes.rest.flow.FlowEntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 按钮里面的 目标节点处理人
 * @author janson
 *
 */
@Component("flow-variable-button-msg-target-processors")
public class FlowVarsButtonMsgTargetProcessors implements FlowVariableUserResolver {
	@Autowired
	FlowEventLogProvider flowEventLogProvider;
	
	@Override
	public List<Long> variableUserResolve(FlowCaseState ctx,
                                          Map<String, Long> processedEntities, FlowEntityType fromEntity,
                                          Long entityId, FlowUserSelection userSelection, int loopCnt) {

	    List<Long> users = new ArrayList<>();
        FlowGraphNode currentNode = ctx.getCurrentNode();
        List<FlowGraphLink> linksOut = currentNode.getLinksOut();

        FlowNode nextNode = null;
        if (linksOut.size() > 0) {
            nextNode = linksOut.get(0).getToNode(ctx, null).getFlowNode();
        }

        if(nextNode != null) {
            Long maxStepCount = flowEventLogProvider.findMaxStepCountByNodeEnterLog(
                    nextNode.getId(), ctx.getFlowCase().getId());

            //stepCount 不加 1 的原因是，目标节点处理人是当前 stepCount 计算的 node_enter 的值
            List<FlowEventLog> logs = flowEventLogProvider.findCurrentNodeEnterLogs(nextNode.getId()
					, ctx.getFlowCase().getId()
					, maxStepCount);
			if(logs != null && logs.size() > 0) {
				for(FlowEventLog log : logs) {
					if(log.getFlowUserId() != null && log.getFlowUserId() > 0) {
						users.add(log.getFlowUserId());	
					}
				}
			}	
		}
		return users;
	}

}
