package com.everhomes.flow.vars;

import com.everhomes.flow.*;
import com.everhomes.rest.flow.FlowEntitySel;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.user.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 节点跟踪的 被转交人姓名
 * @author janson
 *
 */
@Component("flow-variable-text-tracker-target-transfer-name")
public class FlowVarsTextTrackerTargetTransferName implements FlowVariableTextResolver {

	@Autowired
	FlowService flowService;
	
	@Autowired
	FlowEventLogProvider flowEventLogProvider;
	
	// @Autowired
	// UserService userService;
	
	@Override
	public String variableTextRender(FlowCaseState ctx, String variable) {
		if(FlowStepType.TRANSFER_STEP.equals(ctx.getStepType())) {
			List<FlowEntitySel> sels = ctx.getCurrentEvent().getEntitySel();
			if(sels == null || sels.size() == 0) {
				return null;
			}
            UserInfo ui = flowService.getUserInfoInContext(ctx, sels.get(0).getEntityId());
			if(ui != null) {
				return ui.getNickName();
			}
		} else {
			//获取转交进入节点的日志
			FlowEventLog log = flowEventLogProvider.getLastNodeEnterStep(ctx.getFlowCase());
			if(log != null && FlowStepType.TRANSFER_STEP.getCode().equals(log.getButtonFiredStep())) {
                UserInfo ui = flowService.getUserInfoInContext(ctx, log.getFlowUserId());
				if(ui != null) {
					return ui.getNickName();
				}	
			}
		}
		return null;
	}
}
