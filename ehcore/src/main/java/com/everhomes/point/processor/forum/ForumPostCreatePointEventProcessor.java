package com.everhomes.point.processor.forum;

import com.everhomes.bus.LocalEvent;
import com.everhomes.bus.SystemEvent;
import com.everhomes.forum.Post;
import com.everhomes.point.IPointEventProcessor;
import com.everhomes.point.PointEventLog;
import com.everhomes.point.PointRule;
import com.everhomes.point.PointSystem;
import com.everhomes.point.processor.GeneralPointEventProcessor;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.forum.ForumModuleType;
import com.everhomes.rest.forum.PostDTO;
import com.everhomes.util.StringHelper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 完善用户信息事件处理器
 * Created by xq.tian on 2017/12/7.
 */
@Component
public class ForumPostCreatePointEventProcessor extends GeneralPointEventProcessor implements IPointEventProcessor {

    @Override
    public String[] init() {
        return new String[]{SystemEvent.FORUM_POST_CREATE.getCode()};
    }

    @Override
    public List<PointRule> getPointRules(PointSystem pointSystem, LocalEvent localEvent, PointEventLog log) {
        String eventName = getEventName(localEvent, "");
        return super.getPointRules(eventName);
    }

    @Override
    protected String getEventName(LocalEvent localEvent, String subscriptionPath) {
        Long appId = null;
        Byte moduleType = null;

        Map<String, String> params = localEvent.getParams();
        String postJson = params.get("post");
        if (postJson != null) {
            Post post = (Post) StringHelper.fromJsonString(postJson, Post.class);
            appId = post.getEmbeddedAppId();
            moduleType = post.getModuleType();
        } else {
            String postDTOJson = params.get("postDTO");
            if (postDTOJson != null) {
                PostDTO post = (PostDTO) StringHelper.fromJsonString(postDTOJson, PostDTO.class);
                appId = post.getEmbeddedAppId();
                moduleType = post.getModuleType();
            }
        }

        String eventName;
        if (Objects.equals(appId, AppConstants.APPID_ACTIVITY)) {
            eventName = SystemEvent.ACTIVITY_ACTIVITY_CREATE.dft();
        } else if (Objects.equals(moduleType, ForumModuleType.FEEDBACK.getCode())){
            eventName = SystemEvent.FEEDBACK_FEEDBACK_CREATE.dft();
        } else {
            eventName = SystemEvent.FORUM_POST_CREATE.dft();
        }
        return eventName;
    }
}
