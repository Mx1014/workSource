package com.everhomes.rest.ui.organization;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.forum.PostDTO;
/**
 * <ul>
 * <li>dtos : 任务贴列表，{@link com.everhomes.rest.forum.PostDTO}</li>
 * <li>nextPageAnchor : 下一页页码</li>
 * </ul>
 */
public class ListTaskPostsResponse {
	
    @ItemType(PostDTO.class)
    private List<PostDTO> dtos;

    private Long nextPageAnchor;

    public ListTaskPostsResponse() {
    }

    public ListTaskPostsResponse(List<PostDTO> dtos, Long nextPageAnchor) {
        super();
        this.dtos = dtos;
        this.nextPageAnchor = nextPageAnchor;
    }

	public List<PostDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<PostDTO> dtos) {
		this.dtos = dtos;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

    
}
