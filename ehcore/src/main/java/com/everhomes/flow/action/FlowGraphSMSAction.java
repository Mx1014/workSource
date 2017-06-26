package com.everhomes.flow.action;

import com.everhomes.flow.*;
import com.everhomes.rest.flow.FlowEntityType;
import com.everhomes.rest.flow.FlowStatusType;
import com.everhomes.rest.flow.FlowTimeoutMessageDTO;
import com.everhomes.rest.flow.FlowTimeoutType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;

public class FlowGraphSMSAction extends FlowGraphAction {
	private static final Logger LOGGER = LoggerFactory.getLogger(FlowGraphSMSAction.class);
	
	private Long timeoutAtTick;
	private Long remindTick;
	private Long remindCount;
	
	public FlowGraphSMSAction() {
	}
	
	public FlowGraphSMSAction(Long timeoutAtTick, Long remindTick, Long remindCount) {
		this.timeoutAtTick = timeoutAtTick;
		this.remindCount = remindCount;
		this.remindTick = remindTick;
	}
	
	public Long getTimeoutAtTick() {
		return timeoutAtTick;
	}

	public void setTimeoutAtTick(Long timeoutAtTick) {
		this.timeoutAtTick = timeoutAtTick;
	}
	
	@Override
	public void fireAction(FlowCaseState ctx, FlowGraphEvent event)
			throws FlowStepErrorException {
		FlowGraphNode curr = ctx.getCurrentNode();
		FlowTimeout ft = new FlowTimeout();
		ft.setBelongEntity(FlowEntityType.FLOW_ACTION.getCode());
		ft.setBelongTo(this.getFlowAction().getId());
		ft.setTimeoutType(FlowTimeoutType.SMS_TIMEOUT.getCode());
		ft.setStatus(FlowStatusType.VALID.getCode());
		
		FlowTimeoutMessageDTO dto = new FlowTimeoutMessageDTO();
		dto.setFlowCaseId(ctx.getFlowCase().getId());
		dto.setFlowMainId(ctx.getFlowCase().getFlowMainId());
		dto.setFlowVersion(ctx.getFlowCase().getFlowVersion());
		dto.setStepCount(ctx.getFlowCase().getStepCount());
		dto.setFlowNodeId(curr.getFlowNode().getId());
		dto.setTimeoutAtTick(timeoutAtTick);
		dto.setRemindTick(remindTick);
		dto.setRemindCount(remindCount);

		if(ctx.getOperator() != null) {
			dto.setOperatorId(ctx.getOperator().getId());
		}
		if(ctx.getNextNode() != null) {
			dto.setFlowTargetId(ctx.getNextNode().getFlowNode().getId());	
		}
		
		ft.setJson(dto.toString());
		
		if(timeoutAtTick == null) {
			timeoutAtTick = 0l;
		}
//		Long timeoutTick = DateHelper.currentGMTTime().getTime() + timeoutAtTick * 60*1000l;
		Long timeoutTick = System.currentTimeMillis() + timeoutAtTick * 60 * 1000l;
		ft.setTimeoutTick(new Timestamp(timeoutTick));
		
		ctx.getTimeouts().add(ft);
	}

}
