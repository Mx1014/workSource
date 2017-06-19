package com.everhomes.flow.vars;

import com.everhomes.flow.*;
import com.everhomes.rest.flow.FlowEntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 按钮里面的 本节点处理人
 * @author janson
 */
@Component("flow-variable-button-msg-curr-processors")
public class FlowVarsButtonMsgCurrentProcessors implements FlowVariableUserResolver {
    @Autowired
    FlowUserSelectionProvider flowUserSelectionProvider;

    @Autowired
    FlowService flowService;

    @Autowired
    FlowEventLogProvider flowEventLogProvider;

    @Override
    public List<Long> variableUserResolve(FlowCaseState ctx,
                                          Map<String, Long> processedEntities, FlowEntityType fromEntity,
                                          Long entityId, FlowUserSelection userSelection, int loopCnt) {

        //stepCount-1 的原因是，当前节点处理人是上一个 stepCount 计算的 node_enter 的值
        long stepCount = ctx.getFlowCase().getStepCount() - 1L;
        // 如果下一个节点还是当前节点，说明stepCount也没变，所以不用减 1
        if (ctx.getCurrentNode() != null && ctx.getCurrentNode().equals(ctx.getNextNode())) {
            stepCount = ctx.getFlowCase().getStepCount();
        }
        List<FlowEventLog> logs = flowEventLogProvider.findCurrentNodeEnterLogs(
                ctx.getCurrentNode().getFlowNode().getId(), ctx.getFlowCase().getId(), stepCount);
        List<Long> users = new ArrayList<>();
        if (logs != null && logs.size() > 0) {
            for (FlowEventLog log : logs) {
                if (log.getFlowUserId() != null && log.getFlowUserId() > 0) {
                    users.add(log.getFlowUserId());
                }
            }
        }
        return users;
    }
}