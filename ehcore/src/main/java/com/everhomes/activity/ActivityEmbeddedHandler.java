// @formatter:off
package com.everhomes.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.app.AppConstants;
import com.everhomes.forum.ForumEmbeddedHandler;
import com.everhomes.forum.Post;
import com.everhomes.util.StringHelper;

@Component(ActivityEmbeddedHandler.FORUM_EMBEDED_OBJ_RESOLVER_PREFIX + AppConstants.APPID_ACTIVITY)
public class ActivityEmbeddedHandler implements ForumEmbeddedHandler {

    @Autowired
    private ActivityService activityService;


    @Override
    public String renderEmbeddedObjectSnapshot(Post post) {
        ActivityDTO result = activityService.findSnapshotByPostId(post.getId());
        return StringHelper.toJsonString(result);
    }

    @Override
    public String renderEmbeddedObjectDetails(Post post) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Post preProcessEmbeddedObject(Post post) {
        return null;
    }

    @Override
    public Post postProcessEmbeddedObject(Post post) {
        ActivityPostCommand cmd = (ActivityPostCommand) StringHelper.fromJsonString(post.getEmbeddedJson(),
                ActivityPostCommand.class);
        activityService.createPost(cmd, post.getId());
        return post;
    }

}
