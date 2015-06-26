package com.everhomes.user;

import java.util.List;

import com.everhomes.forum.PostDTO;
import com.everhomes.util.StringHelper;

public class ListUserTopicFavoriteResponse {
    private List<PostDTO> posts;

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
