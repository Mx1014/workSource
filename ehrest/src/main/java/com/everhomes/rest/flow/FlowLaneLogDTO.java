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
 *     <li>isCurrentLane: 是否是当前泳道</li>
 *     <li>logs: 详细日志信息，目前仅有 logContent 有用 {@link com.everhomes.rest.flow.FlowEventLogDTO}</li>
 *     <li>needSelectNextNode: 下个节点需要处理人选择</li>
 *     <li>laneEnterTime: 进入泳道的时间</li>
 * </ul>
 */
public class FlowLaneLogDTO {

    private Long laneId;
    private Integer laneLevel;
    private String laneName;
    private Byte isCurrentLane;
    private Byte isRejectLane;
    private Byte needSelectNextNode;
    private Long laneEnterTime;

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

    public Byte getIsRejectLane() {
        return isRejectLane;
    }

    public void setIsRejectLane(Byte isRejectLane) {
        this.isRejectLane = isRejectLane;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
