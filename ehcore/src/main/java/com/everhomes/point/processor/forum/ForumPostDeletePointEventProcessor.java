package com.everhomes.point.processor.forum;

import com.everhomes.bus.LocalEvent;
import com.everhomes.bus.SystemEvent;
import com.everhomes.forum.Post;
import com.everhomes.point.IPointEventProcessor;
import com.everhomes.point.processor.GeneralPointEventProcessor;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.forum.ForumModuleType;
import com.everhomes.rest.forum.PostDTO;
import com.everhomes.util.StringHelper;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Created by xq.tian on 2017/12/7.
 */
@Component
public class ForumPostDeletePointEventProcessor extends GeneralPointEventProcessor implements IPointEventProcessor {

    @Override
    public String[] init() {
        return new String[]{SystemEvent.FORUM_POST_DELETE.getCode()};
    }

    @Override
    protected String getEventName(LocalEvent localEvent, String subscriptionPath) {
        Long appId = null;
        Byte moduleType = null;
        boolean isComment = false;

        Post parentPost = (Post) StringHelper.fromJsonString(localEvent.getStringParam("parentPost"), Post.class);
        String postJson = localEvent.getStringParam("post");
        if (postJson != null) {
            Post post = (Post) StringHelper.fromJsonString(postJson, Post.class);
            isComment = post.getParentPostId() != null && post.getParentPostId() != 0;
            if (isComment && parentPost != null) {
                appId = parentPost.getEmbeddedAppId();
                moduleType = parentPost.getModuleType();
            } else {
                appId = post.getEmbeddedAppId();
                moduleType = post.getModuleType();
            }
            localEvent.setTargetUid(post.getCreatorUid());
        } else {
            String postDTOJson = localEvent.getStringParam("postDTO");
            if (postDTOJson != null) {
                PostDTO post = (PostDTO) StringHelper.fromJsonString(postDTOJson, PostDTO.class);
                isComment = post.getParentPostId() != null && post.getParentPostId() != 0;
                if (isComment && parentPost != null) {
                    appId = parentPost.getEmbeddedAppId();
                    moduleType = parentPost.getModuleType();
                } else {
                    appId = post.getEmbeddedAppId();
                    moduleType = post.getModuleType();
                }
            }
        }

        String eventName = "";
        if (Objects.equals(appId, AppConstants.APPID_ACTIVITY)) {
            if (isComment) {
                eventName = SystemEvent.ACTIVITY_COMMENT_DELETE.dft();
            } else {
                eventName = SystemEvent.ACTIVITY_ACTIVITY_DELETE.dft();
            }
        } else if (Objects.equals(moduleType, ForumModuleType.FEEDBACK.getCode())) {
            if (isComment) {
                // eventName = SystemEvent.FEEDBACK_COMMENT_DELETE.dft();
                eventName = SystemEvent.FORUM_COMMENT_DELETE.dft();
            } else {
                eventName = SystemEvent.FEEDBACK_FEEDBACK_DELETE.dft();
            }
        } else {
            if (isComment) {
                eventName = SystemEvent.FORUM_COMMENT_DELETE.dft();
            } else {
                eventName = SystemEvent.FORUM_POST_DELETE.dft();
            }
        }
        return eventName;
    }
}
