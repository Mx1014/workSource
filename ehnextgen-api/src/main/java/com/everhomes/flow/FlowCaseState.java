package com.everhomes.flow;

import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.user.UserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

public class FlowCaseState {

    private FlowCaseState parentState;
    private List<FlowCaseState> childStates;
    private FlowModuleInfo module;
    private FlowListenerManager listenerManager;
    private FlowCase flowCase;
    private UserInfo operator;
    private FlowGraph flowGraph;
    private FlowStepType stepType;
    private FlowGraphNode prefixNode;
    private FlowGraphNode currentNode;
    private FlowGraphNode nextNode;
    // private List<FlowGraphNode> currentNode;
    private FlowGraphEvent currentEvent;
    private FlowGraphWay currentWay;
    private FlowGraphLane currentLane;
    private Map<String, Object> extra;
    private List<FlowEventLog> logs;
    private List<FlowEventLog> updateLogs;
    private List<FlowTimeout> timeouts;
    private Stack<FlowCaseStateStackType> processStack;

    public FlowCaseState() {
        extra = new ConcurrentHashMap<>();
        logs = new ArrayList<>();
        timeouts = new ArrayList<>();
        updateLogs = new ArrayList<>();
        childStates = new ArrayList<>();
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

    public FlowGraphWay getCurrentWay() {
        return currentWay;
    }

    public void setCurrentWay(FlowGraphWay currentWay) {
        this.currentWay = currentWay;
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

    public FlowListenerManager getListenerManager() {
        return listenerManager;
    }

    public void setListenerManager(FlowListenerManager listenerManager) {
        this.listenerManager = listenerManager;
    }

    public List<FlowEventLog> getUpdateLogs() {
        return updateLogs;
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
}
