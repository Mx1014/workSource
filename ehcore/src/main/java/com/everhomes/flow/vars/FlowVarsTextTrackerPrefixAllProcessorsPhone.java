package com.everhomes.flow.vars;

import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowEventLogProvider;
import com.everhomes.flow.FlowService;
import com.everhomes.flow.FlowVariableTextResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 节点跟踪的 上一步处理人手机号
 * @author janson
 *
 */
@Component("flow-variable-text-tracker-prefix-processors-phone")
public class FlowVarsTextTrackerPrefixAllProcessorsPhone implements FlowVariableTextResolver {

    @Autowired
    FlowService flowService;

    @Autowired
    FlowEventLogProvider flowEventLogProvider;

    @Autowired
    FlowVarsUserRemindPrefixProcessors flowVarsUserRemindPrefixProcessors;

    @Override
    public String variableTextRender(FlowCaseState ctx, String variable) {
        List<Long> userIds = flowVarsUserRemindPrefixProcessors.variableUserResolve(
                ctx, null, null, null, null, 10);
        return displayText(flowService, ctx, userIds, this::getPhone);
    }
}
