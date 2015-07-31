package com.everhomes.organization;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.forum.PostDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>requests : 帖子列表,详见 {@link com.everhomes.forum.PostDTO}</li>
 *	<li>nextPageOffset : 下一页码</li>
 *</ul>
 *
 */
public class ListTopicsByTypeCommandResponse {
	
	@ItemType(PostDTO.class)
	List<PostDTO> requests;
	Long nextPageOffset;
	public List<PostDTO> getRequests() {
		return requests;
	}
	public void setRequests(List<PostDTO> requests) {
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
