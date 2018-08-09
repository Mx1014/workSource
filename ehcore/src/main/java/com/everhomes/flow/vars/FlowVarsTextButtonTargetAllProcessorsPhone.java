package com.everhomes.flow.vars;

import com.everhomes.flow.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 按钮文本的 本节点处理人姓名
 * @author janson
 *
 */
@Component("flow-variable-text-button-msg-target-processors-phone")
public class FlowVarsTextButtonTargetAllProcessorsPhone implements FlowVariableTextResolver {

    @Autowired
    FlowService flowService;

    @Autowired
    FlowEventLogProvider flowEventLogProvider;

    @Override
    public String variableTextRender(FlowCaseState ctx, String variable) {
        // if(ctx.getNextNode() == null) {
        // 	return null;
        // }

        Long maxStepCount = flowEventLogProvider.findMaxStepCountByNodeEnterLog(
                ctx.getFlowCase().getCurrentNodeId(), ctx.getFlowCase().getId());
        //stepCount 不加 1 的原因是，目标节点处理人是当前 stepCount 计算的 node_enter 的值
        List<FlowEventLog> logs = flowEventLogProvider.findCurrentNodeEnterLogs(ctx.getFlowCase().getCurrentNodeId()
                , ctx.getFlowCase().getId()
                , maxStepCount);

        List<Long> userIdList = logs.stream().map(FlowEventLog::getFlowUserId).collect(Collectors.toList());
        return displayText(flowService, ctx, userIdList, this::getPhone);
    }
}
