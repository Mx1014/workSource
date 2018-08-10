package com.everhomes.flow;

import com.everhomes.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xq.tian on 2018/2/1.
 */
public class FlowCaseTree {

    private FlowCase flowCase;
    private List<FlowCaseTree> nodes;

    public FlowCaseTree() {
        this(null);
    }

    public FlowCaseTree(FlowCase flowCase) {
        this.flowCase = flowCase;
        this.nodes = new ArrayList<>();
    }

    public FlowCase getFlowCase() {
        return flowCase;
    }

    public void setFlowCase(FlowCase flowCase) {
        this.flowCase = flowCase;
    }

    /**
     * 获取所有的叶子节点列表，也就是正在执行的任务列表
     */
    public List<FlowCaseTree> getLeafNodes() {
        List<FlowCaseTree> leafNodes = new ArrayList<>();
        if (this.nodes != null && this.nodes.size() > 0) {
            for (FlowCaseTree child : this.nodes) {
                leafNodes.addAll(child.getLeafNodes());
            }
        } else {
            leafNodes.add(this);
        }
        return leafNodes;
    }

    public List<FlowCaseTree> getNodes() {
        return nodes;
    }

    public void setNodes(List<FlowCaseTree> nodes) {
        this.nodes = nodes;
    }

    public void addChild(FlowCaseTree treeDTO) {
        if (this.nodes == null) {
            this.nodes = new ArrayList<>();
        }
        this.nodes.add(treeDTO);
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
