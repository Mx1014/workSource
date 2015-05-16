package com.everhomes.poll;

import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.forum.ForumEmbeddedHandler;
import com.everhomes.forum.Post;
import com.everhomes.util.StringHelper;

public class PollEmbeddedHandler implements ForumEmbeddedHandler {

    @Autowired
    private PollService pollService;

    @Override
    public String renderEmbeddedObject(Post post) {
        PollShowResultResponse result = pollService.showResult(post.getId());
        return StringHelper.toJsonString(result);
    }

    @Override
    public Post preProcessEmbeddedObject(Post post) {
        return null;
    }

    @Override
    public Post postProcessEmbeddedObject(Post post) {
        PollPostCommand cmd = (PollPostCommand) StringHelper.fromJsonString(post.getEmbeddedJson(),
                PollPostCommand.class);
        pollService.createPoll(cmd, post.getId());
        return post;
    }

}
