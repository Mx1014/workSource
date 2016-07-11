package com.everhomes.search;

import java.util.List;

import com.everhomes.forum.Post;
import com.everhomes.rest.forum.ListPostCommandResponse;
import com.everhomes.rest.forum.SearchByMultiForumAndCmntyCommand;
import com.everhomes.rest.forum.SearchTopicCommand;
import com.everhomes.rest.ui.forum.SearchTopicBySceneCommand;

public interface PostSearcher {
    void deleteById(Long id);
    void bulkUpdate(List<Post> posts);
    void feedDoc(Post post);
    void syncFromDb();
    ListPostCommandResponse query(SearchTopicCommand cmd);
    ListPostCommandResponse query(QueryMaker filter);
    ListPostCommandResponse queryByScene(SearchTopicBySceneCommand cmd);
    ListPostCommandResponse queryByMultiForumAndCmnty(SearchByMultiForumAndCmntyCommand cmd);
}
