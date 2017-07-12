package com.everhomes.flow.vars;

import com.everhomes.flow.*;
import com.everhomes.rest.flow.FlowEntitySel;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 按钮文本的 本节点处理人姓名
 * @author janson
 *
 */
@Component("flow-variable-text-button-msg-target-transfer-name")
public class FlowVarsTextButtonTargetTransferName implements FlowVariableTextResolver {
	@Autowired
	FlowService flowService;
	
	@Autowired
	FlowEventLogProvider flowEventLogProvider;
	
	@Autowired
	UserService userService;
	
	@Override
	public String variableTextRender(FlowCaseState ctx, String variable) {
		if(FlowStepType.TRANSFER_STEP.equals(ctx.getStepType())) {
			List<FlowEntitySel> sels = ctx.getCurrentEvent().getEntitySel();
			if(sels == null || sels.size() == 0) {
				return null;
			}
			UserInfo ui = userService.getUserSnapshotInfoWithPhone(sels.get(0).getEntityId());
			if(ui != null) {
                flowService.fixupUserInfoInContext(ctx, ui);
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
