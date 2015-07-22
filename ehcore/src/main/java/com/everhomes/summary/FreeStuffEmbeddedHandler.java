// @formatter:off
package com.everhomes.summary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.app.AppConstants;
import com.everhomes.forum.ForumEmbeddedHandler;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.Post;

@Component(FreeStuffEmbeddedHandler.FORUM_EMBEDED_OBJ_RESOLVER_PREFIX + AppConstants.APPID_FREE_STUFF)
public class FreeStuffEmbeddedHandler implements ForumEmbeddedHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(FreeStuffEmbeddedHandler.class);
    
    @Autowired
    private ForumProvider forumProvider;

    @Override
    public String renderEmbeddedObjectSnapshot(Post post) {
        return post.getEmbeddedJson();
    }

    @Override
    public String renderEmbeddedObjectDetails(Post post) {
        return post.getEmbeddedJson();
    }

    @Override
    public Post preProcessEmbeddedObject(Post post) {
        return post;
    }

    @Override
    public Post postProcessEmbeddedObject(Post post) {
        return post;
    }

}
