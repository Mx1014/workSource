package com.everhomes.flow;

import com.everhomes.rest.flow.FlowNodeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlowGraph {

    private Flow flow;
    private List<FlowGraphNode> nodes;
    private List<FlowGraphLane> lanes;
    private List<FlowGraphBranch> branches;

    private Map<Long, FlowGraphNode> idToNode;
    private Map<Long, FlowGraphLane> idToLane;
    private Map<Integer, FlowGraphNode> levelToNode;
    private Map<Integer, FlowGraphLane> levelToLane;
    private Map<Long, FlowGraphButton> idToButton;
    private Map<Long, FlowGraphAction> idToAction;
    private Map<Long, FlowGraphBranch> originalNodeToBranch;
    private Map<Long, FlowGraphBranch> convergenceNodeToBranch;

    private List<FlowEvaluateItem> evaluateItems;

    private Long createTime;
    private FlowGraphNode endNode;
    private FlowGraphNode startNode;

    public FlowGraph() {
        nodes = new ArrayList<>();
        lanes = new ArrayList<>();
        branches = new ArrayList<>();
        evaluateItems = new ArrayList<>();
        idToNode = new HashMap<>();
        idToButton = new HashMap<>();
        idToAction = new HashMap<>();
        idToLane = new HashMap<>();
        originalNodeToBranch = new HashMap<>();
        convergenceNodeToBranch = new HashMap<>();
        levelToNode = new HashMap<>();
        levelToLane = new HashMap<>();
    }

    private void saveNodeIds(FlowGraphNode node) {
        for (FlowGraphButton btn : node.getApplierButtons()) {
            if (btn.getMessage() != null) {
                idToAction.put(btn.getMessage().getFlowAction().getId(), btn.getMessage());
            }
            if (btn.getSms() != null) {
                idToAction.put(btn.getSms().getFlowAction().getId(), btn.getSms());
            }
            if (btn.getScripts() != null) {
                for (FlowGraphAction action : btn.getScripts()) {
                    idToAction.put(action.getFlowAction().getId(), action);
                }
            }

            idToButton.put(btn.getFlowButton().getId(), btn);
        }

        for (FlowGraphButton btn : node.getProcessorButtons()) {
            idToButton.put(btn.getFlowButton().getId(), btn);
        }
    }

    public void saveIds() {
        for (FlowGraphNode node : nodes) {
            if (null != node.getMessageAction()) {
                idToAction.put(node.getMessageAction().getFlowAction().getId(), node.getMessageAction());
            }
            if (null != node.getSmsAction()) {
                idToAction.put(node.getSmsAction().getFlowAction().getId(), node.getSmsAction());
            }
            if (null != node.getTickMessageAction()) {
                idToAction.put(node.getTickMessageAction().getFlowAction().getId(), node.getTickMessageAction());
            }
            if (null != node.getTickSMSAction()) {
                idToAction.put(node.getTickSMSAction().getFlowAction().getId(), node.getTickSMSAction());
            }
            if (null != node.getTrackApproveEnter()) {
                idToAction.put(node.getTrackApproveEnter().getFlowAction().getId(), node.getTrackApproveEnter());
            }
            if (null != node.getTrackRejectEnter()) {
                idToAction.put(node.getTrackRejectEnter().getFlowAction().getId(), node.getTrackRejectEnter());
            }
            if (null != node.getTrackTransferLeave()) {
                idToAction.put(node.getTrackTransferLeave().getFlowAction().getId(), node.getTrackTransferLeave());
            }

            if (FlowNodeType.fromCode(node.getFlowNode().getNodeType()) == FlowNodeType.END) {
                this.endNode = node;
            }
            if (FlowNodeType.fromCode(node.getFlowNode().getNodeType()) == FlowNodeType.START) {
                this.startNode = node;
            }
            idToNode.put(node.getFlowNode().getId(), node);
            levelToNode.put(node.getFlowNode().getNodeLevel(), node);

            saveNodeIds(node);
        }

        for (FlowGraphLane lane : lanes) {
            idToLane.put(lane.getFlowLane().getId(), lane);
            levelToLane.put(lane.getFlowLane().getLaneLevel(), lane);
        }
        for (FlowGraphBranch branch : branches) {
            originalNodeToBranch.put(branch.getFlowBranch().getOriginalNodeId(), branch);
            convergenceNodeToBranch.put(branch.getFlowBranch().getConvergenceNodeId(), branch);
        }
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Flow getFlow() {
        return flow;
    }

    public void setFlow(Flow flow) {
        this.flow = flow;
    }

    public List<FlowGraphNode> getNodes() {
        return nodes;
    }

    public Map<Long, FlowGraphNode> getIdToNode() {
        return idToNode;
    }

    public Map<Long, FlowGraphButton> getIdToButton() {
        return idToButton;
    }

    public Map<Long, FlowGraphAction> getIdToAction() {
        return idToAction;
    }

    public FlowGraphNode getGraphNode(Long id) {
        return idToNode.get(id);
    }

    public FlowGraphNode getGraphNode(Integer nodeLevel) {
        return levelToNode.get(nodeLevel);
    }

    public List<FlowGraphBranch> getBranches() {
        return branches;
    }

    public FlowGraphBranch getBranchByOriginalNode(Long originalNodeId) {
        return originalNodeToBranch.get(originalNodeId);
    }

    public FlowGraphBranch getBranchByConvergenceNode(Long convergenceNodeId) {
        return convergenceNodeToBranch.get(convergenceNodeId);
    }

    public FlowGraphButton getGraphButton(Long id) {
        return idToButton.get(id);
    }

    public FlowGraphLane getGraphLane(Long id) {
        return idToLane.get(id);
    }

    public FlowGraphLane getGraphLane(Integer level) {
        return levelToLane.get(level);
    }

    public List<FlowGraphLane> getLanes() {
        return lanes;
    }

    public List<FlowEvaluateItem> getEvaluateItems() {
        return evaluateItems;
    }

    public void setEvaluateItems(List<FlowEvaluateItem> evaluateItems) {
        this.evaluateItems = evaluateItems;
    }

    public FlowGraphNode getEndNode() {
        return endNode;
    }

    public FlowGraphNode getStartNode() {
        return startNode;
    }
}
