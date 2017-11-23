package com.everhomes.flow;

import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.user.UserInfo;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class FlowCaseState {

    private FlowCaseState parentState;
    private List<FlowCaseState> childStates;
    private FlowModuleInfo module;
    private FlowCase flowCase;
    private UserInfo operator;
    private FlowGraph flowGraph;
    private FlowStepType stepType;
    private FlowGraphNode prefixNode;
    private FlowGraphNode currentNode;
    private FlowGraphNode nextNode;
    private FlowGraphEvent currentEvent;
    private FlowGraphLane currentLane;
    private Map<String, Object> extra;
    private List<FlowEventLog> logs;
    private List<FlowEventLog> updateLogs;
    private List<FlowTimeout> timeouts;
    private List<FlowEvaluate> flowEvas;
    private Stack<FlowCaseStateStackType> processStack;

    private boolean continueFlag = true;

    public FlowCaseState() {
        extra = new ConcurrentHashMap<>();
        logs = new ArrayList<>();
        timeouts = new ArrayList<>();
        updateLogs = new ArrayList<>();
        childStates = new ArrayList<>();
        flowEvas = new ArrayList<>();
        processStack = new Stack<>();
    }

    public String getModuleName() {
        if (this.module != null) {
            return this.module.getModuleName();
        }

        return null;
    }

    public Long getModuleId() {
        if (this.module != null) {
            return this.module.getModuleId();
        }
        return null;
    }

    public boolean isContinueFlag() {
        return continueFlag;
    }

    public void setContinueFlag(boolean continueFlag) {
        this.continueFlag = continueFlag;
    }

    public FlowModuleInfo getModule() {
        return module;
    }

    public void setModule(FlowModuleInfo module) {
        this.module = module;
    }

    public FlowCase getFlowCase() {
        return flowCase;
    }

    public void setFlowCase(FlowCase flowCase) {
        this.flowCase = flowCase;
    }

    public FlowGraph getFlowGraph() {
        return flowGraph;
    }

    public void setFlowGraph(FlowGraph flowGraph) {
        this.flowGraph = flowGraph;
    }

    public FlowGraphNode getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(FlowGraphNode currentNode) {
        this.currentNode = currentNode;
    }

    public FlowGraphNode getPrefixNode() {
        return prefixNode;
    }

    public void setPrefixNode(FlowGraphNode prefixNode) {
        this.prefixNode = prefixNode;
    }

    public FlowGraphNode getNextNode() {
        return nextNode;
    }

    public void setNextNode(FlowGraphNode nextNode) {
        this.nextNode = nextNode;
    }

    public FlowCaseState getParentState() {
        return parentState;
    }

    public void setParentState(FlowCaseState parentState) {
        this.parentState = parentState;
    }

    public List<FlowCaseState> getChildStates() {
        return childStates;
    }

    public void setChildStates(List<FlowCaseState> childStates) {
        this.childStates = childStates;
    }

    public FlowGraphEvent getCurrentEvent() {
        return currentEvent;
    }

    public FlowGraphLane getCurrentLane() {
        return currentLane;
    }

    public void setCurrentLane(FlowGraphLane currentLane) {
        this.currentLane = currentLane;
    }

    public void setCurrentEvent(FlowGraphEvent currentEvent) {
        this.currentEvent = currentEvent;
    }

    public Map<String, Object> getExtra() {
        return extra;
    }

    public FlowStepType getStepType() {
        return stepType;
    }

    public void setStepType(FlowStepType stepType) {
        this.stepType = stepType;
    }

    public List<FlowEventLog> getLogs() {
        return logs;
    }

    public List<FlowTimeout> getTimeouts() {
        return timeouts;
    }

    public UserInfo getOperator() {
        return operator;
    }

    public void setOperator(UserInfo operator) {
        this.operator = operator;
    }

    public void addUpdateLogs(FlowEventLog updateLog) {
        this.updateLogs.add(updateLog);
    }

    public List<FlowEventLog> getUpdateLogs() {
        return updateLogs;
    }

    public List<FlowEvaluate> getFlowEvas() {
        return flowEvas;
    }

    public void setFlowEvas(List<FlowEvaluate> flowEvas) {
        this.flowEvas = flowEvas;
    }

    public void pushProcessType(FlowCaseStateStackType processType) {
        processStack.push(processType);
    }

    public FlowCaseStateStackType popProcessType() {
        return processStack.pop();
    }

    public FlowCaseStateStackType peekProcessType() {
        return processStack.peek();
    }

    public FlowCaseStateStackType firstProcessType() {
        return processStack.firstElement();
    }

    private void addNodeTrackLog(String content) {
        FlowEventLog log = new FlowEventLog();



        this.logs.add(log);
    }

    private List<FlowCase> getChildFlowCases(FlowCaseState parentState) {
        List<FlowCase> childStates = new ArrayList<>();
        childStates.add(parentState.getFlowCase());
        if (parentState.getChildStates() != null) {
            for (FlowCaseState childState : parentState.getChildStates()) {
                childStates.addAll(getChildFlowCases(childState));
            }
        }
        return childStates;
    }

    public List<FlowCase> getAllFlowCases() {
        FlowCaseState tempParentState = getGrantParentState();
        return getChildFlowCases(tempParentState);
    }

    public List<FlowCaseState> getAllFlowState() {
        FlowCaseState tempParentState = getGrantParentState();
        return getChildFlowState(tempParentState);
    }

    private List<FlowCaseState> getChildFlowState(FlowCaseState parentState) {
        List<FlowCaseState> states = new ArrayList<>();
        states.add(parentState);
        if (parentState.getChildStates() != null) {
            for (FlowCaseState flowCaseState : parentState.getChildStates()) {
                states.addAll(getChildFlowState(flowCaseState));
            }
        }
        return states;
    }

    public FlowCaseState getGrantParentState() {
        FlowCaseState tempParentState = this;
        while (tempParentState.getParentState() != null) {
            tempParentState = tempParentState.getParentState();
        }
        return tempParentState;
    }

    public List<FlowCase> getFlowCaseByBranch(FlowBranch branch) {
        return getAllFlowCases().stream().map(r -> {
            if (r.getStartNodeId().equals(branch.getOriginalNodeId())
                    && r.getEndNodeId().equals(branch.getConvergenceNodeId())) {
                return r;
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public void incrStepCount() {
        this.flowCase.incrStepCount();
    }

    public List<FlowCase> getSiblingFlowCase() {
        FlowCaseState parentState = this.getParentState();
        if (parentState != null) {
            return parentState.getChildStates().stream().map(FlowCaseState::getFlowCase).collect(Collectors.toList());
        }
        return null;
    }
}
