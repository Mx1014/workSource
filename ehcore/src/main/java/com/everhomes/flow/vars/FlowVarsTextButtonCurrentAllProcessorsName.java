package com.everhomes.flow.vars;

import com.everhomes.flow.*;
import com.everhomes.rest.user.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 按钮文本的 本节点处理人姓名
 * @author janson
 *
 */
@Component("flow-variable-text-button-msg-curr-processors-name")
public class FlowVarsTextButtonCurrentAllProcessorsName implements FlowVariableTextResolver {

	@Autowired
	FlowService flowService;
	
	@Autowired
	FlowEventLogProvider flowEventLogProvider;
	
	@Override
	public String variableTextRender(FlowCaseState ctx, String variable) {

	    //stepCount-1 的原因是，当前节点处理人是上一个 stepCount 计算的 node_enter 的值
        long stepCount = ctx.getFlowCase().getStepCount() - 1L;
        // 如果下一个节点还是当前节点，说明stepCount也没变，所以不用减 1
        if (ctx.getCurrentNode() != null && ctx.getCurrentNode().equals(ctx.getNextNode())) {
            stepCount = ctx.getFlowCase().getStepCount();
        }

        List<FlowEventLog> logs = flowEventLogProvider.findCurrentNodeEnterLogs(
		        ctx.getCurrentNode().getFlowNode().getId(), ctx.getFlowCase().getId()
				, stepCount);
		String txt = "";
		int i = 0;
		
		if(logs != null && logs.size() > 0) {
			for(FlowEventLog log : logs) {
				if(log.getFlowUserId() != null && log.getFlowUserId() > 0) {
					UserInfo ui = flowService.getUserInfoInContext(ctx, log.getFlowUserId());
					if(ui != null) {
                        txt += ui.getNickName() + ", ";
						
						i++;
						if(i >= 3) {
							break;
						}	
					}
					
				}
			}
		}
		
		if(txt.length() > 2) {
			txt = txt.substring(0, txt.length()-2);
		}
		return txt;
	}

}
