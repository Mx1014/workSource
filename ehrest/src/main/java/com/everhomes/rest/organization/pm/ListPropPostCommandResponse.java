package com.everhomes.rest.organization.pm;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.forum.PropertyPostDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageOffset：下一页的页码（如果没有则为空）</li>
 * <li>posts: 物业帖或评论信息，参考{@link com.everhomes.rest.forum.PropertyPostDTO}</li>
 * </ul>
 */
public class ListPropPostCommandResponse {
	private Integer nextPageOffset;

    @ItemType(PropertyPostDTO.class)
    private List<PropertyPostDTO> posts;
    
    public ListPropPostCommandResponse() {
    }
    
    
    
    public ListPropPostCommandResponse(Integer nextPageOffset,
			List<PropertyPostDTO> posts) {
		super();
		this.nextPageOffset = nextPageOffset;
		this.posts = posts;
	}



	public List<PropertyPostDTO> getPosts() {
        return posts;
    }

    public void setPosts(List<PropertyPostDTO> posts) {
        this.posts = posts;
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
