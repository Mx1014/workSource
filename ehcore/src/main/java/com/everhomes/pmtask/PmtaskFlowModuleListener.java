package com.everhomes.pmtask;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.everhomes.rest.pmtask.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowGraphEvent;
import com.everhomes.flow.FlowGraphNode;
import com.everhomes.flow.FlowModuleInfo;
import com.everhomes.flow.FlowModuleListener;
import com.everhomes.flow.FlowNode;
import com.everhomes.flow.FlowProvider;
import com.everhomes.flow.FlowService;
import com.everhomes.flow.FlowUserSelection;
import com.everhomes.flow.FlowUserSelectionProvider;
import com.everhomes.rest.flow.FlowCaseEntity;
import com.everhomes.rest.flow.FlowCaseEntityType;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.flow.FlowEntityType;
import com.everhomes.rest.flow.FlowModuleDTO;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.parking.ParkingErrorCode;
import com.everhomes.user.UserContext;
import com.everhomes.util.RuntimeErrorException;

@Component
public class PmtaskFlowModuleListener implements FlowModuleListener {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(PmtaskFlowModuleListener.class);
	@Autowired
	private FlowService flowService;
	@Autowired
	private FlowProvider flowProvider;
	@Autowired
	private PmTaskProvider pmTaskProvider;
	@Autowired
	private PmTaskService pmTaskService;
	@Autowired
	private PmTaskSearch pmTaskSearch;
	@Autowired
    private FlowUserSelectionProvider flowUserSelectionProvider;
	
	private Long moduleId = FlowConstants.PM_TASK_MODULE;
	@Autowired
    private ContentServerService contentServerService;
	
	@Override
	public FlowModuleInfo initModule() {
		FlowModuleInfo module = new FlowModuleInfo();
		FlowModuleDTO moduleDTO = flowService.getModuleById(moduleId);
		module.setModuleName(moduleDTO.getDisplayName());
		module.setModuleId(moduleId);
		return module;
	}

	@Override
	public void onFlowCaseStart(FlowCaseState ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFlowCaseAbsorted(FlowCaseState ctx) {
		
	}

	//状态改变之后
	@Override
	public void onFlowCaseStateChanged(FlowCaseState ctx) {
		//当前节点已经变成上一个节点
		FlowGraphNode currentNode = ctx.getPrefixNode();

		if (null == currentNode)
			return;

		FlowNode flowNode = currentNode.getFlowNode();
		FlowCase flowCase = ctx.getFlowCase();
		//业务的下一个节点是当前节点
		FlowNode nextNode = ctx.getCurrentNode().getFlowNode();

		String stepType = ctx.getStepType().getCode();
		String params = flowNode.getParams();

		if(StringUtils.isBlank(params)) {
			LOGGER.error("Invalid flowNode param.");
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_FLOW_NODE_PARAM,
					"Invalid flowNode param.");
		}

		JSONObject paramJson = JSONObject.parseObject(params);
		String nodeType = paramJson.getString("nodeType");

		Long flowId = flowNode.getFlowMainId();
		PmTask task = pmTaskProvider.findTaskById(flowCase.getReferId());
		Flow flow = flowProvider.findSnapshotFlow(flowCase.getFlowMainId(), flowCase.getFlowVersion());
		String tag1 = flow.getStringTag1();

		long now = System.currentTimeMillis();
		LOGGER.debug("update pmtask request, stepType={}, tag1={}, nodeType={}", stepType, tag1, nodeType);
		if(FlowStepType.APPROVE_STEP.getCode().equals(stepType)) {

			if ("ACCEPTING".equals(nodeType)) {
//				task.setStatus(PmTaskFlowStatus.ASSIGNING.getCode());
				task.setStatus(convertFlowStatus(nextNode.getParams()));
				pmTaskProvider.updateTask(task);

				//TODO: 同步数据到科技园
				Integer namespaceId = UserContext.getCurrentNamespaceId();
				if(namespaceId == 1000000) {

					List<PmTaskLog> logs = pmTaskProvider.listPmTaskLogs(task.getId(), PmTaskFlowStatus.ASSIGNING.getCode());
					if (null != logs && logs.size() != 0) {
						for (PmTaskLog r: logs) {
							if (null != r.getTargetId()) {
								synchronizedTaskToTechpark(task, r.getTargetId(), flow.getOrganizationId());
								break;
							}
						}
					}
				}
			}else if ("ASSIGNING".equals(nodeType)) {

				task.setStatus(convertFlowStatus(nextNode.getParams()));
				pmTaskProvider.updateTask(task);

			}else if ("PROCESSING".equals(nodeType)) {
				task.setStatus(convertFlowStatus(nextNode.getParams()));
				pmTaskProvider.updateTask(task);
			}
		}else if(FlowStepType.ABSORT_STEP.getCode().equals(stepType)) {

			task.setStatus(PmTaskFlowStatus.INACTIVE.getCode());
			pmTaskProvider.updateTask(task);
		}
		//elasticsearch更新
		pmTaskSearch.deleteById(task.getId());
		pmTaskSearch.feedDoc(task);
		
	}

	@Override
	public void onFlowCaseEnd(FlowCaseState ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFlowCaseActionFired(FlowCaseState ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String onFlowCaseBriefRender(FlowCase flowCase) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FlowCaseEntity> onFlowCaseDetailRender(FlowCase flowCase, FlowUserType flowUserType) {
		
		GetTaskDetailCommand cmd = new GetTaskDetailCommand();
		cmd.setId(flowCase.getReferId());
		cmd.setOwnerId(flowCase.getProjectId());
		cmd.setOwnerType(PmTaskOwnerType.COMMUNITY.getCode());
		PmTaskDTO dto = pmTaskService.getTaskDetail(cmd);

		flowCase.setCustomObject(JSONObject.toJSONString(dto));
		
		List<FlowCaseEntity> entities = new ArrayList<>();
		FlowCaseEntity e = new FlowCaseEntity();
		
		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.MULTI_LINE.getCode());
		e.setKey("服务内容");
		e.setValue(dto.getContent());
		entities.add(e);
		
		for(PmTaskAttachmentDTO s: dto.getAttachments()) {
			e = new FlowCaseEntity();
			e.setEntityType(FlowCaseEntityType.IMAGE.getCode());
			e.setKey("");
			e.setValue(s.getContentUrl());
			entities.add(e);
		}
		
		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.LIST.getCode());
		e.setKey("服务地点");
		e.setValue(dto.getAddress());
		entities.add(e);
		
		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.LIST.getCode());
		e.setKey("所属分类");
		e.setValue(dto.getCategoryName());
		entities.add(e);
		
		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.LIST.getCode());
		e.setKey("发起人");
		e.setValue(dto.getRequestorName());
		entities.add(e);
		
		e = new FlowCaseEntity();
		e.setEntityType(FlowCaseEntityType.LIST.getCode());
		e.setKey("联系电话");
		e.setValue(dto.getRequestorPhone());
		entities.add(e);
		
		return entities;
	}

	@Override
	public String onFlowVariableRender(FlowCaseState ctx, String variable) {
		// TODO Auto-generated method stub
		return null;
	}

	//fireButton 之前
	@Override
	public void onFlowButtonFired(FlowCaseState ctx) {

		FlowGraphNode currentNode = ctx.getCurrentNode();
		FlowNode flowNode = currentNode.getFlowNode();
		FlowCase flowCase = ctx.getFlowCase();

		String stepType = ctx.getStepType().getCode();
		String params = flowNode.getParams();

		if(StringUtils.isBlank(params)) {
			LOGGER.error("Invalid flowNode param.");
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_FLOW_NODE_PARAM,
					"Invalid flowNode param.");
		}

		JSONObject paramJson = JSONObject.parseObject(params);
		String nodeType = paramJson.getString("nodeType");

		LOGGER.debug("update pmtask request, stepType={}, nodeType={}", stepType, nodeType);
		if(FlowStepType.APPROVE_STEP.getCode().equals(stepType)) {
			if ("ASSIGNING".equals(nodeType)) {
				Integer namespaceId = UserContext.getCurrentNamespaceId();
				if(namespaceId == 1000000) {
					FlowGraphEvent evt = ctx.getCurrentEvent();
					if(evt != null && evt.getEntityId() != null
							&& FlowEntityType.FLOW_SELECTION.getCode().equals(evt.getFlowEntityType()) ) {

						PmTask task = pmTaskProvider.findTaskById(flowCase.getReferId());
						FlowUserSelection sel = flowUserSelectionProvider.getFlowUserSelectionById(evt.getEntityId());
						Long targetId = sel.getSourceIdA();

						PmTaskLog pmTaskLog = new PmTaskLog();
						pmTaskLog.setNamespaceId(task.getNamespaceId());
						pmTaskLog.setOperatorTime(new Timestamp(System.currentTimeMillis()));
						pmTaskLog.setOperatorUid(UserContext.current().getUser().getId());
						pmTaskLog.setOwnerId(task.getOwnerId());
						pmTaskLog.setOwnerType(task.getOwnerType());
						pmTaskLog.setStatus(task.getStatus());
						pmTaskLog.setTargetId(targetId);
						pmTaskLog.setTargetType(PmTaskTargetType.USER.getCode());
						pmTaskLog.setTaskId(task.getId());
						pmTaskProvider.createTaskLog(pmTaskLog);
					}
				}
			}
		}

	}

	private Byte convertFlowStatus(String params) {

		if(StringUtils.isBlank(params)) {
			LOGGER.error("Invalid flowNode param.");
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_FLOW_NODE_PARAM,
					"Invalid flowNode param.");
		}

		JSONObject paramJson = JSONObject.parseObject(params);
		String nodeType = paramJson.getString("nodeType");

		LOGGER.debug("pmtask flow nodeTppe: {}", nodeType);
		switch (nodeType) {
			case "ACCEPTING": return PmTaskFlowStatus.ACCEPTING.getCode();
			case "ASSIGNING": return PmTaskFlowStatus.ASSIGNING.getCode();
			case "PROCESSING": return PmTaskFlowStatus.PROCESSING.getCode();
			case "COMPLETED": return PmTaskFlowStatus.COMPLETED.getCode();
			default: return null;
		}
	}

	//同步数据到科技园
	private void synchronizedTaskToTechpark(PmTask task, Long targetId, Long organizationId) {
		UserContext context = UserContext.current();
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		if(namespaceId == 1000000) {
			TechparkSynchronizedServiceImpl handler = PlatformContext.getComponent("techparkSynchronizedServiceImpl");
			handler.pushToQueque(task.getId() + "," + targetId + "," + organizationId);
		}
	}
	
	@Override
	public void onFlowCreating(Flow flow) {
		

	}

	@Override
	public void onFlowCaseCreating(FlowCase flowCase) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFlowCaseCreated(FlowCase flowCase) {
		// TODO Auto-generated method stub
		
	}
}
