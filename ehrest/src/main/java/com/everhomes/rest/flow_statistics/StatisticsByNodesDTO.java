package com.everhomes.rest.flow_statistics;

import com.everhomes.util.StringHelper;

public class StatisticsByNodesDTO {
    private String nodeName ;//(节点名)
    private Long nodeId ;//(节点ID)
    private Integer nodeLevel ;//(节点level)
    private Integer handleTimes ;//(处理次数)
    private Double averageHandleCycle ;//(平均处理时长)

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public Integer getNodeLevel() {
        return nodeLevel;
    }

    public void setNodeLevel(Integer nodeLevel) {
        this.nodeLevel = nodeLevel;
    }

    public Integer getHandleTimes() {
        return handleTimes;
    }

    public void setHandleTimes(Integer handleTimes) {
        this.handleTimes = handleTimes;
    }

    public Double getAverageHandleCycle() {
        return averageHandleCycle;
    }

    public void setAverageHandleCycle(Double averageHandleCycle) {
        this.averageHandleCycle = averageHandleCycle;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
