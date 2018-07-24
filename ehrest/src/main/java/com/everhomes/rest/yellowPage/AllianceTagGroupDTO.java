package com.everhomes.rest.yellowPage;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;


/**
 * <ul>
 * 标签组
 * <li>parentTag: 筛选父标签{@link com.everhomes.rest.yellowPage.AllianceTagDTO}</li>
 * <li>childTags: 筛选子标签列表(list) {@link com.everhomes.rest.yellowPage.AllianceTagDTO}</li>
 * </ul>
 */
public class AllianceTagGroupDTO {
	
	private AllianceTagDTO parentTag;

	@ItemType(AllianceTagDTO.class)
	private List<AllianceTagDTO> childTags;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public AllianceTagDTO getParentTag() {
		return parentTag;
	}

	public void setParentTag(AllianceTagDTO parentTag) {
		this.parentTag = parentTag;
	}

	public List<AllianceTagDTO> getChildTags() {
		return childTags;
	}

	public void setChildTags(List<AllianceTagDTO> childTags) {
		this.childTags = childTags;
	}
}
