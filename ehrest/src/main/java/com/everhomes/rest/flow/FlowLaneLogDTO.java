package com.everhomes.rest.flow;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * <ul>
 *     <li>laneId: 泳道id</li>
 *     <li>laneLevel: 泳道level</li>
 *     <li>laneName: 泳道名称</li>
 *     <li>currNodeParams: currNodeParams</li>
 *     <li>isCurrentLane: 是否是当前泳道</li>
 *     <li>isRejectLane: 当前节点是驳回节点</li>
 *     <li>isAbsortLane: 当前节点是终止节点</li>
 *     <li>needSelectNextNode: 下个节点需要处理人选择</li>
 *     <li>laneEnterTime: 进入泳道的时间</li>
 *     <li>currentFormOriginId: 当前节点的表单 id, 后期会废弃这个字段, 新的见 currentNode 的 formOriginId</li>
 *     <li>currentFormVersion: 当前节点配置的表单 version, 后期会废弃这个字段, 新的见 currentNode 的 formVersion</li>
 *     <li>currentNode: 当前节点的一些信息，把当前节点的信息用一个对象来封装，以后可以替代上面的一些和当前节点相关的独立的字段 {@link com.everhomes.rest.flow.FlowNodeLogDTO}</li>
 *     <li>logs: 详细日志信息，目前仅有 logContent 有用 {@link com.everhomes.rest.flow.FlowEventLogDTO}</li>
 * </ul>
 */
public class FlowLaneLogDTO {

    private Long laneId;
    private Integer laneLevel;
    private String laneName;
    private String currNodeParams;
    private Byte isCurrentLane;
    private Byte isRejectLane;
    private Byte isAbsortLane;
    private Byte needSelectNextNode;
    private Long laneEnterTime;

    private Long currentFormOriginId;
    private Long currentFormVersion;

    private FlowNodeLogDTO currentNode;

    @ItemType(FlowEventLogDTO.class)
    private List<FlowEventLogDTO> logs;

    public FlowLaneLogDTO() {
        logs = new ArrayList<>();
    }

    public Long getLaneId() {
        return laneId;
    }

    public void setLaneId(Long laneId) {
        this.laneId = laneId;
    }

    public Integer getLaneLevel() {
        return laneLevel;
    }

    public void setLaneLevel(Integer laneLevel) {
        this.laneLevel = laneLevel;
    }

    public String getLaneName() {
        return laneName;
    }

    public void setLaneName(String laneName) {
        this.laneName = laneName;
    }

    public Byte getIsCurrentLane() {
        return isCurrentLane;
    }

    public void setIsCurrentLane(Byte isCurrentLane) {
        this.isCurrentLane = isCurrentLane;
    }

    public List<FlowEventLogDTO> getLogs() {
        return logs;
    }

    public void setLogs(List<FlowEventLogDTO> logs) {
        this.logs = logs;
    }

    public Byte getNeedSelectNextNode() {
        return needSelectNextNode;
    }

    public void setNeedSelectNextNode(Byte needSelectNextNode) {
        this.needSelectNextNode = needSelectNextNode;
    }

    public Long getLaneEnterTime() {
        return laneEnterTime;
    }

    public void setLaneEnterTime(Long laneEnterTime) {
        this.laneEnterTime = laneEnterTime;
    }

    public String getCurrNodeParams() {
        return currNodeParams;
    }

    public void setCurrNodeParams(String currNodeParams) {
        this.currNodeParams = currNodeParams;
    }

    public Byte getIsRejectLane() {
        return isRejectLane;
    }

    public void setIsRejectLane(Byte isRejectLane) {
        this.isRejectLane = isRejectLane;
    }

    public Byte getIsAbsortLane() {
        return isAbsortLane;
    }

    public void setIsAbsortLane(Byte isAbsortLane) {
        this.isAbsortLane = isAbsortLane;
    }

    public Long getCurrentFormOriginId() {
        return currentFormOriginId;
    }

    public void setCurrentFormOriginId(Long currentFormOriginId) {
        this.currentFormOriginId = currentFormOriginId;
    }

    public Long getCurrentFormVersion() {
        return currentFormVersion;
    }

    public void setCurrentFormVersion(Long currentFormVersion) {
        this.currentFormVersion = currentFormVersion;
    }

    public FlowNodeLogDTO getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(FlowNodeLogDTO currentNode) {
        this.currentNode = currentNode;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
