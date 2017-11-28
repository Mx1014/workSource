// @formatter:off
package com.everhomes.summary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.forum.ForumEmbeddedHandler;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.Post;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.forum.FreeStuffCommand;
import com.everhomes.util.StringHelper;

@Component(FreeStuffEmbeddedHandler.FORUM_EMBEDED_OBJ_RESOLVER_PREFIX + AppConstants.APPID_FREE_STUFF)
public class FreeStuffEmbeddedHandler implements ForumEmbeddedHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(FreeStuffEmbeddedHandler.class);
    
    @Autowired
    private ForumProvider forumProvider;
    
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
        String embeddedJson = post.getEmbeddedJson();
        try {
            if(embeddedJson != null && embeddedJson.trim().length() > 0) {
                FreeStuffCommand cmd = (FreeStuffCommand)StringHelper.fromJsonString(post.getEmbeddedJson(), FreeStuffCommand.class);
                cmd.setId(post.getId());
                post.setEmbeddedJson(StringHelper.toJsonString(cmd));
                this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_POST.getCode()).enter(()-> {
                    this.forumProvider.updatePost(post);
                   return null;
                });
            }
        } catch(Exception e) {
            LOGGER.error("Failed to processStat free stuff embedded json info, postId=" + post.getId() + ", json=" + embeddedJson, e);
        }
        return post;
    }

}
