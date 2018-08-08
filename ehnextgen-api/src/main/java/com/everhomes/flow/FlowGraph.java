package com.everhomes.flow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class FlowGraph implements Serializable {

    private Flow flow;
    private List<FlowGraphNode> nodes;
    private List<FlowGraphLane> lanes;
    private List<FlowGraphBranch> branches;

    transient private Map<Long, FlowGraphNode> idToNode;
    transient private Map<Long, FlowGraphLane> idToLane;
    transient private Map<Integer, FlowGraphNode> levelToNode;
    transient private Map<Integer, FlowGraphLane> levelToLane;
    transient private Map<Long, FlowGraphButton> idToButton;
    transient private Map<Long, FlowGraphAction> idToAction;
    transient private Map<Long, FlowGraphBranch> originalNodeToBranch;
    transient private Map<Long, List<FlowGraphBranch>> convergenceNodeToBranches;

    private List<FlowEvaluateItem> evaluateItems;

    private Long createTime;
    private FlowGraphNode endNode;
    private FlowGraphNode startNode;

    public FlowGraph() {
        initFields();
    }

    public void initFields() {
        ifNull(nodes, o -> nodes = new ArrayList<>());
        ifNull(lanes, o -> lanes = new ArrayList<>());
        ifNull(branches, o -> branches = new ArrayList<>());
        ifNull(evaluateItems, o -> evaluateItems = new ArrayList<>());

        ifNull(idToNode, o -> idToNode = new HashMap<>());
        ifNull(idToButton, o -> idToButton = new HashMap<>());
        ifNull(idToAction, o -> idToAction = new HashMap<>());
        ifNull(idToLane, o -> idToLane = new HashMap<>());
        ifNull(originalNodeToBranch, o -> originalNodeToBranch = new HashMap<>());
        ifNull(convergenceNodeToBranches, o -> convergenceNodeToBranches = new HashMap<>());
        ifNull(levelToNode, o -> levelToNode = new HashMap<>());
        ifNull(levelToLane, o -> levelToLane = new HashMap<>());
    }

    private <Type> void ifNull(Type o, Consumer<Type> con) {
        if (o == null) {
            con.accept(o);
        }
    }

    private void saveNodeIds(FlowGraphNode node) {
        for (FlowGraphButton btn : node.getApplierButtons()) {
            if (btn.getMessage() != null) {
                idToAction.put(btn.getMessage().getFlowAction().getId(), btn.getMessage());
            }
            if (btn.getSms() != null) {
                idToAction.put(btn.getSms().getFlowAction().getId(), btn.getSms());
            }
            if (btn.getScript() != null) {
                idToAction.put(btn.getScript().getFlowAction().getId(), btn.getScript());
            }

            idToButton.put(btn.getFlowButton().getId(), btn);
        }

        for (FlowGraphButton btn : node.getProcessorButtons()) {
            idToButton.put(btn.getFlowButton().getId(), btn);
        }
        for (FlowGraphButton btn : node.getSupervisorButtons()) {
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

            /*if (FlowNodeType.fromCode(node.getFlowNode().getNodeType()) == FlowNodeType.END
                    || node.getFlowNode().getNodeName().equals("END")) {
                this.endNode = node;
            }
            if (FlowNodeType.fromCode(node.getFlowNode().getNodeType()) == FlowNodeType.START
                    || node.getFlowNode().getNodeName().equals("START")) {
                this.startNode = node;
            }*/
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
            convergenceNodeToBranches.compute(branch.getFlowBranch().getConvergenceNodeId(), (k, v) -> {
                if (v == null) {
                    v = new ArrayList<>();
                }
                v.add(branch);
                return v;
            });
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

    public List<FlowGraphBranch> getBranchByConvNode(Long convergenceNodeId) {
        return convergenceNodeToBranches.get(convergenceNodeId);
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

    public void setEndNode(FlowGraphNode endNode) {
        this.endNode = endNode;
    }

    public void setStartNode(FlowGraphNode startNode) {
        this.startNode = startNode;
    }

    public FlowGraphBranch getBranchByOriginalAndConvNode(Long startNodeId, Long targetNodeId) {
        for (FlowGraphBranch branch : branches) {
            if (branch.getFlowBranch().getOriginalNodeId().equals(startNodeId)
                    && branch.getFlowBranch().getConvergenceNodeId().equals(targetNodeId)) {
                return branch;
            }
        }
        return null;
    }
}
