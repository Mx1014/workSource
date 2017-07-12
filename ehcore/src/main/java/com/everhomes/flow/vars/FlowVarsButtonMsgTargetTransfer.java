package com.everhomes.flow.vars;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowEventLog;
import com.everhomes.flow.FlowEventLogProvider;
import com.everhomes.flow.FlowService;
import com.everhomes.flow.FlowUserSelection;
import com.everhomes.flow.FlowUserSelectionProvider;
import com.everhomes.flow.FlowVariableUserResolver;
import com.everhomes.rest.flow.FlowEntitySel;
import com.everhomes.rest.flow.FlowEntityType;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.user.User;

/**
 * 按钮里面的 被转交人
 * @author janson
 *
 */
@Component("flow-variable-button-msg-target-transfer")
public class FlowVarsButtonMsgTargetTransfer implements FlowVariableUserResolver {
	@Autowired
	FlowUserSelectionProvider flowUserSelectionProvider;
	
	@Autowired
	FlowService flowService;
	
	@Autowired
	FlowEventLogProvider flowEventLogProvider;
	
	@Override
	public List<Long> variableUserResolve(FlowCaseState ctx,
			Map<String, Long> processedEntities, FlowEntityType fromEntity,
			Long entityId, FlowUserSelection userSelection, int loopCnt) {
		Long uid = null;
		if(FlowStepType.TRANSFER_STEP.equals(ctx.getStepType())) {
			List<FlowEntitySel> sels = ctx.getCurrentEvent().getEntitySel();
			if(sels == null || sels.size() == 0) {
				return null;
			}
			uid = sels.get(0).getEntityId();
		} else {
			//获取转交进入节点的日志
			FlowEventLog log = flowEventLogProvider.getLastNodeEnterStep(ctx.getFlowCase());
			if(log != null && FlowStepType.TRANSFER_STEP.getCode().equals(log.getButtonFiredStep())) {
				uid = log.getFlowUserId();
			}
		}
		
		List<Long> rlts = new ArrayList<Long>();
		if(uid != null) {
			rlts.add(uid);
		}
		return rlts;
	}

}
