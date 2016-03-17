package com.everhomes.rest.quality;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>tasks: 参考com.everhomes.rest.quality.QualityInspectionTaskDTO</li>
 *  <li>groupUsers: 参考com.everhomes.rest.quality.GroupUserDTO</li>
 *  <li>pageAnchor: 下一页的锚点，没有下一页则没有</li>
 * </ul>
 */
public class ListQualityInspectionTasksResponse {
	
	@ItemType(QualityInspectionTaskDTO.class)
	private List<QualityInspectionTaskDTO> tasks;
	
	@ItemType(GroupUserDTO.class)
	private List<GroupUserDTO> groupUsers;
	
	private Long nextPageAnchor;

	public ListQualityInspectionTasksResponse(Long nextPageAnchor, 
			List<QualityInspectionTaskDTO> tasks, List<GroupUserDTO> groupUsers) {
        this.nextPageAnchor = nextPageAnchor;
        this.tasks = tasks;
        this.groupUsers = groupUsers;
    }
	
	public List<QualityInspectionTaskDTO> getTasks() {
		return tasks;
	}

	public void setTasks(List<QualityInspectionTaskDTO> tasks) {
		this.tasks = tasks;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
	
	public List<GroupUserDTO> getGroupUsers() {
		return groupUsers;
	}

	public void setGroupUsers(List<GroupUserDTO> groupUsers) {
		this.groupUsers = groupUsers;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
