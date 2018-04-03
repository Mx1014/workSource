package com.everhomes.point.processor.forum;

import com.everhomes.bus.LocalEvent;
import com.everhomes.bus.SystemEvent;
import com.everhomes.point.*;
import com.everhomes.point.processor.GeneralPointEventProcessor;
import com.everhomes.rest.app.AppConstants;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Created by xq.tian on 2017/12/7.
 */
@Component
public class ForumPostLikePointEventProcessor extends GeneralPointEventProcessor implements IPointEventProcessor {

    @Override
    public String[] init() {
        return new String[]{SystemEvent.FORUM_POST_LIKE.getCode()};
    }

    @Override
    public PointEventProcessResult execute(LocalEvent localEvent, PointRule rule, PointSystem pointSystem, PointRuleCategory pointRuleCategory) {
        PointLog pointLog = pointLogProvider.findByRuleIdAndEntity(pointSystem.getNamespaceId(),
                pointSystem.getId(), localEvent.getContext().getUid(), rule.getId(), localEvent.getEntityType(), localEvent.getEntityId());
        if (pointLog != null) {
            return null;
        }
        return super.execute(localEvent, rule, pointSystem, pointRuleCategory);
    }

    @Override
    protected String getEventName(LocalEvent localEvent) {
        Long appId = null;

        String embeddedAppId = localEvent.getStringParam("embeddedAppId");
        if (embeddedAppId != null) {
            appId = Long.valueOf(embeddedAppId);
        }

        String eventName;
        if (Objects.equals(appId, AppConstants.APPID_ACTIVITY)) {
            eventName = SystemEvent.ACTIVITY_ACTIVITY_LIKE.dft();
        } else {
            eventName = SystemEvent.FORUM_POST_LIKE.dft();
        }
        return eventName;
    }
}
