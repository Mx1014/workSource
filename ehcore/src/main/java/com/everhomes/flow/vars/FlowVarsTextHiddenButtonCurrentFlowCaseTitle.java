package com.everhomes.flow.vars;

import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowVariableTextResolver;
import org.springframework.stereotype.Component;

/**
 * flowCaseTitle
 * @author janson
 *
 */
@Component("text-hidden-button-msg-flow-case-title")
public class FlowVarsTextHiddenButtonCurrentFlowCaseTitle implements FlowVariableTextResolver {

	// @Autowired
	// FlowService flowService;
	
	// @Autowired
	// FlowEventLogProvider flowEventLogProvider;
	
	@Override
	public String variableTextRender(FlowCaseState ctx, String variable) {
        String title = ctx.getFlowCase().getTitle();
        if (title == null) {
            title = ctx.getModuleName();
        }
        return title;
	}
}
