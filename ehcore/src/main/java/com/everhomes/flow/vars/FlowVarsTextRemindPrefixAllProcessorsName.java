package com.everhomes.flow.vars;

import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowService;
import com.everhomes.flow.FlowVariableTextResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 节点提醒的 上个节点处理人
 * @author janson
 *
 */
@Component("flow-variable-text-remind-prefix-processors-name")
public class FlowVarsTextRemindPrefixAllProcessorsName implements FlowVariableTextResolver {

    @Autowired
    FlowService flowService;

    @Autowired
    FlowVarsUserRemindPrefixProcessors flowVarsUserRemindPrefixProcessors;

    @Override
    public String variableTextRender(FlowCaseState ctx, String variable) {
        List<Long> userIds = flowVarsUserRemindPrefixProcessors.variableUserResolve(
                ctx, null, null, null, null, 10);
        return displayText(flowService, ctx, userIds, this::getNickName);
    }
}
