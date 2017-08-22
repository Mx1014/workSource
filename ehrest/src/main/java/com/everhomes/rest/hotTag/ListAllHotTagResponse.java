package com.everhomes.rest.hotTag;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageOffset：下一页页码</li>
 * <li>tags: tags信息，参考{@link TagDTO}</li>
 * </ul>
 */
public class ListAllHotTagResponse {
	
	@ItemType(TagDTO.class)
	private List<TagDTO> tags;
	
	private Integer nextPageOffset;
	
	public List<TagDTO> getTags() {
		return tags;
	}

	public void setTags(List<TagDTO> tags) {
		this.tags = tags;
	}

	public Integer getNextPageOffset() {
		return nextPageOffset;
	}

	public void setNextPageOffset(Integer nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
