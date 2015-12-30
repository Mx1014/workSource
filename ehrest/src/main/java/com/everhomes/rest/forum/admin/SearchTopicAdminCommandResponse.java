package com.everhomes.rest.forum.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageAnchor：分页的锚点，下一页开始取数据的位置</li>
 * <li>posts: 帖或评论信息，参考{@link com.everhomes.rest.forum.PostDTO}</li>
 * </ul>
 */
public class SearchTopicAdminCommandResponse {
    private Long nextPageAnchor;

    @ItemType(PostAdminDTO.class)
    private List<PostAdminDTO> posts;
    
    private String keywords;
    
    public SearchTopicAdminCommandResponse() {
    }
    
    public SearchTopicAdminCommandResponse(Long nextPageAnchor, List<PostAdminDTO> posts) {
        this.nextPageAnchor = nextPageAnchor;
        this.posts = posts;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }
    
    public List<PostAdminDTO> getPosts() {
        return posts;
    }

    public void setPosts(List<PostAdminDTO> posts) {
        this.posts = posts;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
