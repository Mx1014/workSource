// @formatter:off
package com.everhomes.summary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.app.AppConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.forum.ForumEmbeddedHandler;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.Post;

@Component(TopicSummaryEmbeddedHandler.FORUM_EMBEDED_OBJ_RESOLVER_PREFIX + AppConstants.APPID_TOPIC_SUMMARY)
public class TopicSummaryEmbeddedHandler implements ForumEmbeddedHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(TopicSummaryEmbeddedHandler.class);
    
    @Autowired
    private ForumProvider forumProvider;
    @Autowired
    private ConfigurationProvider configurationProvider;

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
        Long embeddedObjId = post.getEmbeddedId();
        if(embeddedObjId != null) {
            Post orgPost = this.forumProvider.findPostById(embeddedObjId);
            if(orgPost != null) {
                orgPost.setForwardCount(orgPost.getForwardCount() + 1);
                this.forumProvider.updatePost(orgPost);
            } else {
                LOGGER.error("The original post is not found when forwarding, postId=" + post.getId() 
                    + ", embeddedObjId=" + embeddedObjId);
            }
        } else {
            LOGGER.error("The embeded object is null when forwarding, postId=" + post.getId() 
                + ", embeddedObjId=" + embeddedObjId);
        }
        return post;
    }

}
