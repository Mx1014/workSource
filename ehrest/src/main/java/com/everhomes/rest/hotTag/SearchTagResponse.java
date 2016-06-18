package com.everhomes.rest.hotTag;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageAnchor：分页的锚点，下一页开始取数据的位置</li>
 * <li>tags: tags信息，参考{@link com.everhomes.rest.hotTag.TagDTO}</li>
 * </ul>
 */
public class SearchTagResponse {
	
	@ItemType(TagDTO.class)
	private List<TagDTO> tags;
	
	private Long nextPageAnchor;
	
	public List<TagDTO> getTags() {
		return tags;
	}

	public void setTags(List<TagDTO> tags) {
		this.tags = tags;
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
