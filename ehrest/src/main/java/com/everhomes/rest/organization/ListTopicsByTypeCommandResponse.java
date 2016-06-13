package com.everhomes.rest.organization;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.forum.PostDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>requests : 帖子列表,详见 {@link com.everhomes.rest.organization.OrganizationTaskDTO2}</li>
 *	<li>nextPageOffset : 下一页码</li>
 *</ul>
 *
 */
public class ListTopicsByTypeCommandResponse {
	
	@ItemType(OrganizationTaskDTO2.class)
	List<OrganizationTaskDTO2> requests;
	Long nextPageOffset;
	
	public List<OrganizationTaskDTO2> getRequests() {
		return requests;
	}
	public void setRequests(List<OrganizationTaskDTO2> requests) {
		this.requests = requests;
	}
	public Long getNextPageOffset() {
		return nextPageOffset;
	}
	public void setNextPageOffset(Long nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	

}
