// @formatter:off
package com.everhomes.rest.group;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.forum.PostDTO;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>posts: 帖子列表，参考{@link com.everhomes.rest.forum.PostDTO}</li>
 * </ul>
 */
public class ListUserGroupPostResponse {

	private Long nextPageAnchor;

	@ItemType(PostDTO.class)
	private List<PostDTO> posts;

	public ListUserGroupPostResponse() {

	}

	public ListUserGroupPostResponse(Long nextPageAnchor, List<PostDTO> posts) {
		super();
		this.nextPageAnchor = nextPageAnchor;
		this.posts = posts;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<PostDTO> getPosts() {
		return posts;
	}

	public void setPosts(List<PostDTO> posts) {
		this.posts = posts;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
