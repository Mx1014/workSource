package com.everhomes.flow.vars;

import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowVariableTextResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 节点提醒的 上个节点处理人手机号
 * @author janson
 *
 */
@Component("flow-variable-text-remind-prefix-processors-phone")
public class FlowVarsTextRemindPrefixAllProcessorsPhone implements FlowVariableTextResolver {

    @Autowired
    private FlowVarsTextTrackerPrefixAllProcessorsPhone flowVarsTextTrackerPrefixAllProcessorsPhone;

    @Override
    public String variableTextRender(FlowCaseState ctx, String variable) {
        return flowVarsTextTrackerPrefixAllProcessorsPhone.variableTextRender(ctx, variable);
    }
}
