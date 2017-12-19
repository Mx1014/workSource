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
import com.everhomes.rest.user.FeedbackTargetType;
import com.everhomes.rest.user.FeedbackVerifyType;
import com.everhomes.user.Feedback;
import com.everhomes.util.StringHelper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * Created by xq.tian on 2017/12/7.
 */
@Component
public class ForumPostReportPointEventProcessor extends GeneralPointEventProcessor implements IPointEventProcessor {

    @Override
    public String[] init() {
        return new String[]{SystemEvent.FORUM_POST_REPORT.getCode()};
    }

    @Override
    public List<PointRule> getPointRules(PointSystem pointSystem, LocalEvent localEvent, PointEventLog log) {
        String eventName = getEventName(localEvent, null);
        return super.getPointRules(eventName);
    }

    @Override
    protected String getEventName(LocalEvent localEvent, String subscriptionPath) {
        Long appId = null;

        String postJson = localEvent.getParam("post");
        String parentPostJson = localEvent.getParam("parentPost");

        if (parentPostJson != null) {
            Post parentJson = (Post) StringHelper.fromJsonString(parentPostJson, Post.class);
            appId = parentJson.getEmbeddedAppId();
        } else if (postJson != null) {
            Post post = (Post) StringHelper.fromJsonString(postJson, Post.class);
            appId = post.getEmbeddedAppId();
        }

        String feedbackJson = localEvent.getParam("feedback");
        if (feedbackJson == null) {
            return "";
        }

        Feedback feedback = (Feedback) StringHelper.fromJsonString(feedbackJson, Feedback.class);
        FeedbackTargetType targetType = FeedbackTargetType.fromStatus(feedback.getTargetType());
        FeedbackVerifyType verifyType = FeedbackVerifyType.fromStatus(feedback.getVerifyType());

        String eventName = "";
        if (verifyType == FeedbackVerifyType.TRUE && targetType == FeedbackTargetType.POST) {
            if (Objects.equals(appId, AppConstants.APPID_ACTIVITY)) {
                eventName = SystemEvent.ACTIVITY_ACTIVITY_REPORT.dft();
            } else {
                eventName = SystemEvent.FORUM_POST_REPORT.dft();
            }
        }
        return eventName;
    }
}
