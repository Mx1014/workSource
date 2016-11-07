package com.everhomes.rest.quality;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>tasks: 参考 {@link com.everhomes.rest.quality.TaskCountDTO}</li>
 *  <li>nextPageAnchor: 下一页的锚点，没有下一页则没有</li>
 * </ul>
 */
public class CountTasksResponse {

	private List<TaskCountDTO> tasks;
	
	private Long nextPageAnchor;

	public List<TaskCountDTO> getTasks() {
		return tasks;
	}

	public void setTasks(List<TaskCountDTO> tasks) {
		this.tasks = tasks;
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
