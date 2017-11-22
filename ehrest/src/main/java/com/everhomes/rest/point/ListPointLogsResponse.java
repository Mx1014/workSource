package com.everhomes.rest.point;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>nextPageAnchor: nextPageAnchor</li>
 *     <li>logs: 积分日志列表 {@link com.everhomes.rest.point.PointLogDTO}</li>
 * </ul>
 */
public class ListPointLogsResponse {

    private Long nextPageAnchor;

    @ItemType(PointLogDTO.class)
    private List<PointLogDTO> logs;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<PointLogDTO> getLogs() {
        return logs;
    }

    public void setLogs(List<PointLogDTO> logs) {
        this.logs = logs;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
