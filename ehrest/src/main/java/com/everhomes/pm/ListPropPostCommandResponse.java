package com.everhomes.pm;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.forum.PropertyPostDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageAnchor：分页的锚点，下一页开始取数据的位置</li>
 * <li>posts: 帖或评论信息，参考{@link com.everhomes.forum.PropertyPostDTO}</li>
 * </ul>
 */
public class ListPropPostCommandResponse {
    private Long nextPageAnchor;

    @ItemType(PropertyPostDTO.class)
    private List<PropertyPostDTO> posts;
    
    public ListPropPostCommandResponse() {
    }
    
    public ListPropPostCommandResponse(Long nextPageAnchor, List<PropertyPostDTO> posts) {
        this.nextPageAnchor = nextPageAnchor;
        this.posts = posts;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }
    
    public List<PropertyPostDTO> getPosts() {
        return posts;
    }

    public void setPosts(List<PropertyPostDTO> posts) {
        this.posts = posts;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
