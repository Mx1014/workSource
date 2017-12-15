package com.everhomes.point.processor;

import com.everhomes.bus.LocalEvent;
import com.everhomes.bus.SystemEvent;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.Post;
import com.everhomes.point.*;
import com.everhomes.rest.category.CategoryConstants;
import com.everhomes.user.User;
import com.everhomes.user.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 完善用户信息事件处理器
 * Created by xq.tian on 2017/12/7.
 */
// @Component
public class ForumPointEventProcessor implements PointEventProcessor {

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private GeneralPointEventProcessor generalProcessor;

    @Autowired
    private ForumProvider forumProvider;

    @Override
    public String[] init() {
        long[] categories = {
                CategoryConstants.CATEGORY_ID_TOPIC,
                CategoryConstants.CATEGORY_ID_TOPIC_COMMON,
                CategoryConstants.CATEGORY_ID_NOTICE,
                CategoryConstants.CATEGORY_ID_TOPIC_ACTIVITY,
                CategoryConstants.CATEGORY_ID_TOPIC_POLLING,
        };
        String[] events = new String[categories.length];
        for (int i = 0; i < categories.length; i++) {
            events[i] = SystemEvent.FORUM_POST_CREATE.suffix(categories[i]);
        }
        return events;
    }

    @Override
    public PointEventProcessResult execute(LocalEvent localEvent, PointRule rule, PointSystem pointSystem, PointRuleCategory pointRuleCategory) {
        if (isValidEvent(localEvent)) {
            return generalProcessor.execute(localEvent, rule, pointSystem, pointRuleCategory);
        }
        return null;
    }

    private boolean isValidEvent(LocalEvent localEvent) {
        User user = userProvider.findUserById(localEvent.getContext().getUid());
        return isValid(user.getAvatar()) && isValid(user.getBirthday()) && isValid(user.getStatusLine()) && isValid(user.getOccupation());
    }

    private boolean isValid(Object o) {
        return o != null && o.toString().trim().length() > 0;
    }

    @Override
    public List<PointResultAction> getResultActions(List<PointAction> pointActions, LocalEvent localEvent,
                                                    PointRule rule, PointSystem pointSystem, PointRuleCategory category) {
        return generalProcessor.getResultActions(pointActions, localEvent, rule, pointSystem, category);
    }

    @Override
    public List<PointRule> getPointRules(PointSystem pointSystem, LocalEvent localEvent, PointEventLog log) {
        Post post = forumProvider.findPostById(localEvent.getEntityId());

        Long cateId = post.getContentCategory();

        if (cateId == null) {
            return new ArrayList<>();
        }

        Long appId = post.getEmbeddedAppId();
        if (appId == null) {
            appId = 0L;
        }
        return generalProcessor.getPointRules(pointSystem, localEvent, log);
    }
}
