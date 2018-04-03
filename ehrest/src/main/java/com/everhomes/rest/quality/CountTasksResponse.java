package com.everhomes.rest.quality;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>tasks: 参考 {@link com.everhomes.rest.quality.TaskCountDTO}</li>
 *  <li>offset: 下一页的偏移值</li>
 * </ul>
 */
public class CountTasksResponse {

	@ItemType(TaskCountDTO.class)
	private List<TaskCountDTO> tasks;
	
	private Integer offset;

	public List<TaskCountDTO> getTasks() {
		return tasks;
	}

	public void setTasks(List<TaskCountDTO> tasks) {
		this.tasks = tasks;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
