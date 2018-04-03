package com.everhomes.rest.flow;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>nodes: 节点跟踪日志 {@link com.everhomes.rest.flow.FlowLaneLogDTO}</li>
 * </ul>
 */
public class FlowCaseTrackDTO {

    @ItemType(FlowLaneLogDTO.class)
    private List<FlowLaneLogDTO> lanes;

    public List<FlowLaneLogDTO> getLanes() {
        return lanes;
    }

    public void setLanes(List<FlowLaneLogDTO> lanes) {
        this.lanes = lanes;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
