package com.everhomes.poll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.app.AppConstants;
import com.everhomes.forum.ForumEmbeddedHandler;
import com.everhomes.forum.Post;
import com.everhomes.server.schema.tables.pojos.EhPolls;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.StringHelper;

@Component(ForumEmbeddedHandler.FORUM_EMBEDED_OBJ_RESOLVER_PREFIX + AppConstants.APPID_POLL)
public class PollEmbeddedHandler implements ForumEmbeddedHandler {

    @Autowired
    private PollService pollService;
    
    @Autowired
    private ShardingProvider shardingProvider;

    @Override
    public String renderEmbeddedObjectSnapshot(Post post) {
//        PollShowResultResponse result = pollService.showResult(post.getId());
    	PollDTO result = pollService.showResultBrief(post.getId());
        if (result == null) {
            return null;
        }
        return StringHelper.toJsonString(result);
    }

    @Override
    public String renderEmbeddedObjectDetails(Post post) {
        PollShowResultResponse poll = pollService.showResult(post.getId());
        if (poll == null) {
            return null;
        }
        return StringHelper.toJsonString(poll);
    }

    @Override
    public Post preProcessEmbeddedObject(Post post) {
        Long id = shardingProvider.allocShardableContentId(EhPolls.class).second();
        post.setEmbeddedId(id);
        return post;
    }

    @Override
    public Post postProcessEmbeddedObject(Post post) {
        PollPostCommand cmd = (PollPostCommand) StringHelper.fromJsonString(post.getEmbeddedJson(),
                PollPostCommand.class);
        cmd.setId(post.getEmbeddedId());
        pollService.createPoll(cmd, post.getId());
        return post;
    }

}
