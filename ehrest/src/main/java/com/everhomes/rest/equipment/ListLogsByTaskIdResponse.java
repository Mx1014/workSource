package com.everhomes.rest.equipment;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>logs: 任务操作记录列表 参考{@link com.everhomes.rest.equipment.EquipmentTaskLogs}</li>
 * <li>nextPageAnchor: 下一页的锚点，没有下一页则没有</li>
 * </ul>
 */
public class ListLogsByTaskIdResponse {
    @ItemType(EquipmentTaskLogs.class)
    private List<EquipmentTaskLogs> logs;

    private Long nextPageAnchor;

    public List<EquipmentTaskLogs> getLogs() {
        return logs;
    }

    public void setLogs(List<EquipmentTaskLogs> logs) {
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
