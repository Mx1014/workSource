package com.everhomes.flow;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.flow.FlowEntitySel;
import com.everhomes.rest.flow.FlowEntityType;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.user.UserService;

@Component(FlowVariableTextResolver.TRANSFER_TARGET_PHONE)
public class FlowVariableTransferTargetPhoneResolver implements FlowVariableTextResolver {
	@Autowired
	FlowService flowService;
	
	@Autowired
	FlowEventLogProvider flowEventLogProvider;
	
	@Autowired
	UserService userService;
	
	@Override
	public String variableTextRender(FlowCaseState ctx, String variable) {
		FlowNode node = null;
		if(ctx.getStepType() != FlowStepType.TRANSFER_STEP) {
			return null;
		}
		if(ctx.getCurrentEvent() != null) {
			List<FlowEntitySel> sels = ctx.getCurrentEvent().getEntitySel();
			if(sels == null || sels.size() == 0) {
				return null;
			}
			UserInfo ui = userService.getUserSnapshotInfoWithPhone(sels.get(0).getEntityId());
			if(ui != null) {
				return ui.getNickName();
			}
		}
		if(node == null) {
			//获取转交进入节点的日志
			FlowEventLog log = flowEventLogProvider.getLastNodeEnterStep(ctx.getFlowCase());
			if(log != null) {
				UserInfo ui = userService.getUserSnapshotInfoWithPhone(log.getFlowUserId());
				if(ui != null && ui.getPhones() != null && ui.getPhones().size() > 0) {
					return ui.getPhones().get(0);
				}	
			}
		}
		
		return null;
	}

}
