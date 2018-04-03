package com.everhomes.rest.flow;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>nextPageAnchor: nextPageAnchor</li>
 *     <li>logs: logs {@link com.everhomes.rest.flow.FlowOperateLogDTO}</li>
 * </ul>
 */
public class SearchFlowOperateLogResponse {

    private Long nextPageAnchor;

    @ItemType(FlowOperateLogDTO.class)
    private List<FlowOperateLogDTO> logs;

    public List<FlowOperateLogDTO> getLogs() {
        return logs;
    }

    public void setLogs(List<FlowOperateLogDTO> logs) {
        this.logs = logs;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
