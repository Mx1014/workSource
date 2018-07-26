package com.everhomes.relocation;


import com.everhomes.db.DbProvider;
import com.everhomes.flow.*;
import com.everhomes.rest.flow.FlowLogType;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.relocation.RelocationRequestStatus;
import com.everhomes.user.User;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import org.apache.poi.ss.formula.EvaluationName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


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
	@Autowired
	private FlowEventLogProvider flowEventLogProvider;
	@Autowired
	private UserProvider userProvider;

	private Long relocationId;

	public RelocationCancelAction(String relocationId) {
		this.relocationId = Long.valueOf(relocationId);
	}
	
	@Override
	public void run() {

		LOGGER.info("Prepared cancel timeout relocation, relocationId={}", relocationId);
		RelocationRequest request = relocationProvider.findRelocationRequestById(relocationId);
		//处理中的任务过期 自动取消
		if (null != request && request.getStatus() == RelocationRequestStatus.PROCESSING.getCode()) {

			request.setStatus(RelocationRequestStatus.CANCELED.getCode());
			request.setCancelTime(new Timestamp(System.currentTimeMillis()));
			request.setCancelUid(User.SYSTEM_UID);

			FlowCase flowCase = flowCaseProvider.getFlowCaseById(request.getFlowCaseId());

			User user = userProvider.findUserById(User.SYSTEM_UID);

			FlowAutoStepDTO stepDTO = new FlowAutoStepDTO();
			stepDTO.setFlowCaseId(flowCase.getId());
			stepDTO.setFlowMainId(flowCase.getFlowMainId());
			stepDTO.setFlowVersion(flowCase.getFlowVersion());
			stepDTO.setFlowNodeId(flowCase.getCurrentNodeId());
			stepDTO.setAutoStepType(FlowStepType.ABSORT_STEP.getCode());
			stepDTO.setStepCount(flowCase.getStepCount());
			stepDTO.setOperatorId(User.SYSTEM_UID);

			FlowEventLog log = new FlowEventLog();
			log.setId(flowEventLogProvider.getNextId());
			log.setFlowMainId(flowCase.getFlowMainId());
			log.setFlowVersion(flowCase.getFlowVersion());
			log.setNamespaceId(flowCase.getNamespaceId());
			log.setFlowNodeId(flowCase.getCurrentNodeId());
			log.setFlowCaseId(flowCase.getId());
			log.setStepCount(flowCase.getStepCount());
			log.setSubjectId(0L);
			log.setParentId(0L);
			log.setFlowUserId(user.getId());
			log.setFlowUserName(user.getNickName());
			log.setLogType(FlowLogType.NODE_TRACKER.getCode());
			log.setButtonFiredStep(FlowStepType.ABSORT_STEP.getCode());
			log.setTrackerApplier(1L); // 申请人可以看到此条log，为0则看不到
			log.setTrackerProcessor(1L);// 处理人可以看到此条log，为0则看不到
			log.setLogContent("物品放行申请已超时，系统自动取消");
			List<FlowEventLog> logList = new ArrayList<>(1);
			logList.add(log);
			stepDTO.setEventLogs(logList);

			dbProvider.execute(status -> {

				relocationProvider.updateRelocationRequest(request);
				flowService.processAutoStep(stepDTO);

				return null;
			});
			LOGGER.info("Cancel timeout relocation success, relocationId={}", relocationId);
		}
	}

}
