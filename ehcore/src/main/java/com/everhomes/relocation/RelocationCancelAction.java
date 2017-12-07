package com.everhomes.relocation;


import com.everhomes.db.DbProvider;
import com.everhomes.flow.FlowAutoStepDTO;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseProvider;
import com.everhomes.flow.FlowService;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.relocation.RelocationRequestStatus;
import com.everhomes.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;


@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RelocationCancelAction implements Runnable{

	private static final Logger LOGGER = LoggerFactory.getLogger(RelocationCancelAction.class);

	@Autowired
	private RelocationProvider relocationProvider;
	@Autowired
	private FlowCaseProvider flowCaseProvider;
	@Autowired
	private FlowService flowService;
	@Autowired
	private DbProvider dbProvider;

	private Long relocationId;

	public RelocationCancelAction(String relocationId) {
		this.relocationId = Long.valueOf(relocationId);
	}
	
	@Override
	public void run() {

		RelocationRequest request = relocationProvider.findRelocationRequestById(relocationId);

		request.setStatus(RelocationRequestStatus.CANCELED.getCode());
		request.setCancelTime(new Timestamp(System.currentTimeMillis()));
		request.setCancelUid(User.SYSTEM_UID);

		FlowCase flowCase = flowCaseProvider.getFlowCaseById(request.getFlowCaseId());

		FlowAutoStepDTO stepDTO = new FlowAutoStepDTO();
		stepDTO.setFlowCaseId(flowCase.getId());
		stepDTO.setFlowMainId(flowCase.getFlowMainId());
		stepDTO.setFlowVersion(flowCase.getFlowVersion());
		stepDTO.setFlowNodeId(flowCase.getCurrentNodeId());
		stepDTO.setAutoStepType(FlowStepType.ABSORT_STEP.getCode());
		stepDTO.setStepCount(flowCase.getStepCount());

		dbProvider.execute(status -> {

			relocationProvider.updateRelocationRequest(request);
			flowService.processAutoStep(stepDTO);

			return null;
		});

	}

}
