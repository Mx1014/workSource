package com.everhomes.poll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.app.AppConstants;
import com.everhomes.forum.ForumEmbeddedHandler;
import com.everhomes.forum.Post;
import com.everhomes.util.StringHelper;

@Component(ForumEmbeddedHandler.FORUM_EMBEDED_OBJ_RESOLVER_PREFIX + AppConstants.APPID_POLL)
public class PollEmbeddedHandler implements ForumEmbeddedHandler {

    @Autowired
    private PollService pollService;

    @Override
    public String renderEmbeddedObjectSnapshot(Post post) {
        PollShowResultResponse result = pollService.showResult(post.getId());
        return StringHelper.toJsonString(result);
    }

    @Override
    public String renderEmbeddedObjectDetails(Post post) {
        PollShowResultResponse poll = pollService.showResult(post.getParentPostId());
        return StringHelper.toJsonString(poll);
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
