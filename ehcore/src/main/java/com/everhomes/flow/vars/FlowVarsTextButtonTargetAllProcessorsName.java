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
@Component("flow-variable-text-button-msg-target-processors-name")
public class FlowVarsTextButtonTargetAllProcessorsName implements FlowVariableTextResolver {

	@Autowired
	FlowService flowService;
	
	@Autowired
	FlowEventLogProvider flowEventLogProvider;
	
	@Override
	public String variableTextRender(FlowCaseState ctx, String variable) {
		if(ctx.getNextNode() == null) {
			return null;
		}

        //stepCount 不加 1 的原因是，目标节点处理人是当前 stepCount 计算的 node_enter 的值
        List<FlowEventLog> logs = flowEventLogProvider.findCurrentNodeEnterLogs(ctx.getNextNode().getFlowNode().getId()
				, ctx.getFlowCase().getId()
				, ctx.getFlowCase().getStepCount());

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
