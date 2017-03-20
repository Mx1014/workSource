package com.everhomes.flow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlowGraph {
	private Flow flow;
	private List<FlowGraphNode> nodes;

	private Map<Long, FlowGraphNode> idToNode;
	private Map<Long, FlowGraphButton> idToButton;
	private Map<Long, FlowGraphAction> idToAction;
	private Long createTime;
	
	public FlowGraph() {
		nodes = new ArrayList<FlowGraphNode>();
		idToNode = new HashMap<Long, FlowGraphNode>();
		idToButton = new HashMap<Long, FlowGraphButton>();
		idToAction = new HashMap<Long, FlowGraphAction>();
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
	public FlowGraphButton getGraphButton(Long id) {
		return idToButton.get(id);
	}
	
	private void saveNodeIds(FlowGraphNode node) {
		for(FlowGraphButton btn : node.getApplierButtons()) {
			if(btn.getMessage() != null) {
				idToAction.put(btn.getMessage().getFlowAction().getId(), btn.getMessage());
			}
			if(btn.getSms() != null) {
				idToAction.put(btn.getSms().getFlowAction().getId(), btn.getSms());
			}
			if(btn.getScripts() != null) {
				for(FlowGraphAction action : btn.getScripts()) {
					idToAction.put(action.getFlowAction().getId(), action);
				}	
			}
			
			idToButton.put(btn.getFlowButton().getId(), btn);
		}
		
		for(FlowGraphButton btn : node.getProcessorButtons()) {
			idToButton.put(btn.getFlowButton().getId(), btn);
		}
	}
	
	public void saveIds() {
		for(FlowGraphNode node : nodes) {
			if(null != node.getMessageAction()) {
				idToAction.put(node.getMessageAction().getFlowAction().getId(), node.getMessageAction());	
			}
			if(null != node.getSmsAction()) {
				idToAction.put(node.getSmsAction().getFlowAction().getId(), node.getSmsAction());
			}
			if(null != node.getTickMessageAction()) {
				idToAction.put(node.getTickMessageAction().getFlowAction().getId(), node.getTickMessageAction());
			}
			if(null != node.getTickSMSAction()) {
				idToAction.put(node.getTickSMSAction().getFlowAction().getId(), node.getTickSMSAction());
			}
			if(null != node.getTrackApproveEnter()) {
				idToAction.put(node.getTrackApproveEnter().getFlowAction().getId(), node.getTrackApproveEnter());
			}
			if(null != node.getTrackRejectEnter()) {
				idToAction.put(node.getTrackRejectEnter().getFlowAction().getId(), node.getTrackRejectEnter());
			}
			if(null != node.getTrackTransferLeave()) {
				idToAction.put(node.getTrackTransferLeave().getFlowAction().getId(), node.getTrackTransferLeave());
			}
			idToNode.put(node.getFlowNode().getId(), node);
			
			saveNodeIds(node);
		}
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
}
