package com.everhomes.rest.equipment;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>logs: 任务操作记录列表 参考{@link com.everhomes.rest.equipment.EquipmentTaskLogsDTO}</li>
 *  <li>nextPageAnchor: 下一页的锚点，没有下一页则没有</li>
 * </ul>
 */
public class ListLogsByTaskIdResponse {
	@ItemType(EquipmentTaskLogsDTO.class)
	private List<EquipmentTaskLogsDTO> logs;
	
	private Long nextPageAnchor;
	
	public List<EquipmentTaskLogsDTO> getLogs() {
		return logs;
	}

	public void setLogs(List<EquipmentTaskLogsDTO> logs) {
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
