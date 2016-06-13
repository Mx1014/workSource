// @formatter:off
package com.everhomes.summary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.forum.ForumEmbeddedHandler;
import com.everhomes.forum.Post;
import com.everhomes.group.Group;
import com.everhomes.group.GroupProvider;
import com.everhomes.rest.app.AppConstants;

@Component(GroupCardEmbeddedHandler.FORUM_EMBEDED_OBJ_RESOLVER_PREFIX + AppConstants.APPID_GROUP_CARD)
public class GroupCardEmbeddedHandler implements ForumEmbeddedHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupCardEmbeddedHandler.class);
    
    @Autowired
    private GroupProvider groupProvider;
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
            Group group = this.groupProvider.findGroupById(embeddedObjId);
            if(group != null) {
                group.setShareCount(group.getShareCount() + 1);
                this.groupProvider.updateGroup(group);
            } else {
                LOGGER.error("The group is not found when sharing, postId=" + post.getId() 
                    + ", embeddedObjId=" + embeddedObjId);
            }
        } else {
            LOGGER.error("The embeded object id is null when forwarding, postId=" + post.getId() 
                + ", embeddedObjId=" + embeddedObjId);
        }
        return post;
    }

}
