// @formatter:off
package com.everhomes.summary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.forum.ForumEmbeddedHandler;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.Post;
import com.everhomes.rest.app.AppConstants;

@Component(TopicSummaryEmbeddedHandler.FORUM_EMBEDED_OBJ_RESOLVER_PREFIX + AppConstants.APPID_TOPIC_SUMMARY)
public class TopicSummaryEmbeddedHandler implements ForumEmbeddedHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(TopicSummaryEmbeddedHandler.class);
    
    @Autowired
    private ForumProvider forumProvider;
    @Autowired
    private ConfigurationProvider configurationProvider;
    
    @Autowired
    private CoordinationProvider coordinationProvider;

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
                try {
                    this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_POST.getCode()).enter(()-> {
                        orgPost.setForwardCount(orgPost.getForwardCount() + 1);
                        this.forumProvider.updatePost(orgPost);
                       return null;
                    });
                } catch(Exception e) {
                    LOGGER.error("Failed to update the forward count of post, topicId=" + embeddedObjId, e);
                }
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
