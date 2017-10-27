package com.everhomes.flow.event;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.flow.*;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.user.User;
import com.everhomes.user.UserService;
import com.everhomes.util.RuntimeErrorException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlowGraphAutoStepEvent extends AbstractFlowGraphEvent {

    private FlowAutoStepDTO stepDTO;
    private Long firedUserId;
    private FlowSubject subject;

	private FlowEventLogProvider flowEventLogProvider;
	private UserService userService;
	private FlowService flowService;
    private FlowStateProcessor flowStateProcessor;

	public FlowGraphAutoStepEvent(FlowAutoStepDTO o) {
		firedUserId = User.SYSTEM_UID;
		if(o.getOperatorId() != null) {
			firedUserId = o.getOperatorId();
		}
        this.stepDTO = o;
        flowEventLogProvider = PlatformContext.getComponent(FlowEventLogProvider.class);
		userService = PlatformContext.getComponent(UserService.class);
		flowService = PlatformContext.getComponent(FlowService.class);
        flowStateProcessor = PlatformContext.getComponent(FlowStateProcessor.class);
	}
	@Override
	public FlowUserType getUserType() {
		return FlowUserType.PROCESSOR;
	}

	@Override
	public FlowEventType getEventType() {
		return FlowEventType.fromCode(stepDTO.getEventType());
	}

	public void setFiredUserId(Long firedUserId) {
		this.firedUserId = firedUserId;
	}

	@Override
	public Long getFiredUserId() {
		return this.firedUserId;
	}

	@Override
	public Long getFiredButtonId() {
		return null;
	}
	
	@Override
	public List<FlowEntitySel> getEntitySel() {
		return new ArrayList<>();
	}
	
	@Override
	public void fire(FlowCaseState ctx) {
        FlowGraph flowGraph = ctx.getFlowGraph();

        FlowEventLog log;
        FlowEventLog tracker = null;

        FlowStepType stepType = ctx.getStepType();
        FlowCase flowCase = ctx.getFlowCase();

        //currentNode state change to next step
        FlowGraphNode currentNode = ctx.getCurrentNode();
        FlowGraphNode next = null;

        FlowGraphLane currentLane = ctx.getCurrentLane();

        UserInfo applier = userService.getUserSnapshotInfo(flowCase.getApplyUserId());

        Map<String, Object> templateMap = new HashMap<>();
        templateMap.put("nodeName", currentNode.getFlowNode().getNodeName());
        templateMap.put("laneName", currentLane.getFlowLane().getDisplayName());
        templateMap.put("applierName", applier.getNickName());

		switch(stepType) {
		case NO_STEP:
			break;
		case APPROVE_STEP:
			/*next = null;
	    	FlowButton flowBtn = flowButtonProvider.findFlowButtonByStepType(stepDTO.getFlowNodeId()
	    			, stepDTO.getFlowVersion(), stepType.getCode(), FlowUserType.PROCESSOR.getCode());
			
			if(!flowBtn.getGotoNodeId().equals(0L)) {
				next = ctx.getFlowGraph().getGraphNode(flowBtn.getGotoNodeId());
			}
			if(next == null) {
				//get next level
				next = ctx.getFlowGraph().getNodes().get(currentNode.getFlowNode().getNodeLevel()+1);
			}*/

            next = currentNode.getLinksOut().get(0).getToNode(ctx, this);

            boolean isConditionNode = FlowNodeType.fromCode(next.getFlowNode().getNodeType()) == FlowNodeType.CONDITION_FRONT;
            // 条件节点
            if (isConditionNode) {
                next.stepEnter(ctx, currentNode);
                flowCase.setCurrentNodeId(next.getFlowNode().getId());
                flowCase.setCurrentLaneId(next.getFlowNode().getFlowLaneId());
            } else {
                ctx.setNextNode(next);
            }
			
			tracker = new FlowEventLog();
			tracker.setLogContent(flowService.getStepMessageTemplate(stepType, next.getExpectStatus(), ctx.getCurrentEvent(), templateMap));
			tracker.setStepCount(ctx.getFlowCase().getStepCount());
			if(next.getExpectStatus() == FlowCaseStatus.FINISHED && subject == null) {
				//显示任务跟踪语句
				subject = new FlowSubject();
			}
			
			flowCase.setStepCount(flowCase.getStepCount() + 1L);
			break;
		case REJECT_STEP:
			if(currentNode.getFlowNode().getNodeLevel() < 1) {
				throw RuntimeErrorException.errorWith(FlowServiceErrorCode.SCOPE, FlowServiceErrorCode.ERROR_FLOW_STEP_ERROR, "flow node step error");
			}

			/*tracker = new FlowEventLog();
			tracker.setLogContent(flowService.getFireButtonTemplate(stepType, templateMap));
			tracker.setStepCount(ctx.getFlowCase().getStepCount());

			next = ctx.getFlowGraph().getNodes().get(currentNode.getFlowNode().getNodeLevel()-1);
			ctx.setNextNode(next);
			if(subject == null) {
				subject = new FlowSubject();
			}

			flowCase.setRejectNodeId(currentNode.getFlowNode().getId());
			flowCase.setRejectCount(flowCase.getRejectCount() + 1);
			flowCase.setStepCount(flowCase.getStepCount() + 1L);*/

            tracker = new FlowEventLog();
            tracker.setLogContent(flowService.getFireButtonTemplate(stepType, templateMap));
            tracker.setStepCount(ctx.getFlowCase().getStepCount());

            flowStateProcessor.rejectToNode(ctx, 0, currentNode);

            boolean notFindNextNode = ctx.getAllFlowState().stream().allMatch(r -> r.getNextNode() == null);
            if (notFindNextNode) {
                throw RuntimeErrorException.errorWith("", 1, "reject node not found");
            }

            if (subject == null) {
                subject = new FlowSubject();
            }

            flowCase.setRejectNodeId(currentNode.getFlowNode().getId());
            flowCase.setRejectCount(flowCase.getRejectCount() + 1);
            flowCase.setStepCount(flowCase.getStepCount() + 1);

			break;
		case END_STEP:
		case ABSORT_STEP:
            tracker = new FlowEventLog();
            if(ctx.getOperator() != null) {
                templateMap.put("applierName", ctx.getOperator().getNickName());
            }

            next = flowGraph.getEndNode();

            tracker.setLogContent(flowService.getStepMessageTemplate(stepType, next.getExpectStatus(), ctx.getCurrentEvent(), templateMap));
            tracker.setStepCount(ctx.getFlowCase().getStepCount());
            if (subject == null) {
                //显示任务跟踪语句
                subject = new FlowSubject();
            }

            for (FlowCaseState flowCaseState : ctx.getAllFlowState()) {
                flowCaseState.setNextNode(next);
                flowCaseState.setStepType(stepType);
                flowCaseState.getFlowCase().setStepCount(flowCaseState.getFlowCase().getStepCount() + 1);
            }

			break;
		default:
			break;
		}

		if(tracker != null && subject != null) {
			tracker.setId(flowEventLogProvider.getNextId());
			tracker.setFlowMainId(ctx.getFlowGraph().getFlow().getFlowMainId());
			tracker.setFlowVersion(ctx.getFlowGraph().getFlow().getFlowVersion());
			tracker.setNamespaceId(ctx.getFlowGraph().getFlow().getNamespaceId());
			tracker.setFlowNodeId(ctx.getCurrentNode().getFlowNode().getId());
			tracker.setParentId(0L);
			tracker.setFlowCaseId(ctx.getFlowCase().getId());
			tracker.setFlowUserId(ctx.getOperator().getId());
			tracker.setFlowUserName(ctx.getOperator().getNickName());
			if(subject.getContent() != null && !subject.getContent().isEmpty()) {
				tracker.setSubjectId(subject.getId());	
			} else {
				tracker.setSubjectId(0L);// BUG #5431
			}
			
			tracker.setLogType(FlowLogType.NODE_TRACKER.getCode());
			
			tracker.setButtonFiredStep(stepType.getCode());
			tracker.setTrackerApplier(1L);
			tracker.setTrackerProcessor(1L);
			ctx.getLogs().add(tracker);
		}
		
		log = new FlowEventLog();
		log.setId(flowEventLogProvider.getNextId());
		log.setFlowMainId(ctx.getFlowGraph().getFlow().getFlowMainId());
		log.setFlowVersion(ctx.getFlowGraph().getFlow().getFlowVersion());
		log.setNamespaceId(ctx.getFlowGraph().getFlow().getNamespaceId());
        if (next != null) {
            log.setFlowNodeId(next.getFlowNode().getId());
        }
		log.setParentId(0L);
		log.setFlowCaseId(ctx.getFlowCase().getId());
		log.setFlowUserId(ctx.getOperator().getId());
		log.setLogType(FlowLogType.AUTO_STEP.getCode());
		log.setButtonFiredStep(stepType.getCode());
		log.setButtonFiredFromNode(currentNode.getFlowNode().getId());
		ctx.getLogs().add(log);	//added but not save to database now.
	}

	@Override
	public FlowSubject getSubject() {
		return subject;
	}

    public void setSubject(FlowSubject subject) {
        this.subject = subject;
    }
}
