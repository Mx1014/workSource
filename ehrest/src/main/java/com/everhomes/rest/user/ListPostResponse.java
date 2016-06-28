package com.everhomes.rest.user;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.forum.PostDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>postDtos: 帖子列表 参考{@link com.everhomes.rest.forum.PostDTO}</li>
 * <li>nextPageAnchor: 下页锚点</li>
 * </ul>
 */
public class ListPostResponse {
	@ItemType(PostDTO.class)
	private List<PostDTO> postDtos;

	private Long nextPageAnchor;

    public ListPostResponse() {
    }

	public List<PostDTO> getPostDtos() {
		return postDtos;
	}

	public void setPostDtos(List<PostDTO> postDtos) {
		this.postDtos = postDtos;
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
