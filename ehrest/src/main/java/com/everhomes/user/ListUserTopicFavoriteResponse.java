package com.everhomes.user;

import java.util.List;

import com.everhomes.forum.PostDTO;
import com.everhomes.util.StringHelper;

public class ListUserTopicFavoriteResponse {
    private Long uid;
    private List<PostDTO> posts;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
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
