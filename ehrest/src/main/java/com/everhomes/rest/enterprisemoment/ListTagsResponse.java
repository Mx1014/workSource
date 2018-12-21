// @formatter:off
package com.everhomes.rest.enterprisemoment;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>tags: 标签列表 {@link com.everhomes.rest.enterprisemoment.MomentTagDTO}</li>
 * </ul>
 */
public class ListTagsResponse {
	private List<MomentTagDTO> tags;

	public List<MomentTagDTO> getTags() {
		return tags;
	}

	public void setTags(List<MomentTagDTO> tags) {
		this.tags = tags;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
