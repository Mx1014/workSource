package com.everhomes.rest.forum;

import com.everhomes.rest.activity.ActivityDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>dtos: 论坛帖子列表 {@link PostDTO}</li>
 * </ul>
 */
public class ListTopicsByForumEntryIdResponse {

	private List<PostDTO> dtos;

	public List<PostDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<PostDTO> dtos) {
		this.dtos = dtos;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
