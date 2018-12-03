package com.everhomes.flow.vars;

import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowSubject;
import com.everhomes.flow.FlowVariableTextResolver;
import org.springframework.stereotype.Component;

/**
 * 用户在按钮触发时输入的内容
 */
@Component("flow-variable-text-button-msg-user-input-content")
public class FlowVarsTextButtonUserInputContent implements FlowVariableTextResolver {

	// @Autowired
	// private FlowService flowService;
	//
	// @Autowired
	// private FlowEventLogProvider flowEventLogProvider;
	
	@Override
	public String variableTextRender(FlowCaseState ctx, String variable) {
        FlowSubject subject = ctx.getCurrentEvent().getSubject();
        if (subject != null) {
            return subject.getContent();
        }
        return "";
    }
}
