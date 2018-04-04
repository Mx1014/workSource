package com.everhomes.rest.equipment;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *  <li>tasks: 任务列表 参考{@link com.everhomes.rest.equipment.EquipmentTaskDTO}</li>
 *  <li>totayTasksCount  : 当前总任务数</li>
 *  <li>todayCompleteCount: 当前已完成 </li>
 *  <li>nextPageAnchor: 下一页的锚点，没有下一页则没有</li>
 * </ul>
 */
public class ListEquipmentTasksResponse {

	@ItemType(EquipmentTaskDTO.class)
	private List<EquipmentTaskDTO> tasks;

	private Long totayTasksCount;

	private Long todayCompleteCount;

	private Long nextPageAnchor;
	
	public List<EquipmentTaskDTO> getTasks() {
		return tasks;
	}

	public void setTasks(List<EquipmentTaskDTO> tasks) {
		this.tasks = tasks;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public Long getTotayTasksCount() {
		return totayTasksCount;
	}

	public void setTotayTasksCount(Long totayTasksCount) {
		this.totayTasksCount = totayTasksCount;
	}

	public Long getTodayCompleteCount() {
		return todayCompleteCount;
	}

	public void setTodayCompleteCount(Long todayCompleteCount) {
		this.todayCompleteCount = todayCompleteCount;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
