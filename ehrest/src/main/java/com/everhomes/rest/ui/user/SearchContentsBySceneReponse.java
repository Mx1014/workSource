package com.everhomes.rest.ui.user;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>dtos: 查询的帖子的结果列表，{@link com.everhomes.rest.ui.user.ContentBriefDTO}</li>
 * <li>nextPageAnchor: 下一页的锚点</li>
 *</ul>
 */
public class SearchContentsBySceneReponse {
	@ItemType(ContentBriefDTO.class)
	private List<ContentBriefDTO> dtos;
	
	private Long nextPageAnchor;

	public List<ContentBriefDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<ContentBriefDTO> dtos) {
		this.dtos = dtos;
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
