package com.everhomes.point.processor.forum;

import com.everhomes.bus.LocalEvent;
import com.everhomes.bus.SystemEvent;
import com.everhomes.point.IPointEventProcessor;
import com.everhomes.point.processor.GeneralPointEventProcessor;
import com.everhomes.rest.app.AppConstants;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Created by xq.tian on 2017/12/7.
 */
@Component
public class ForumCommentCreatePointEventProcessor extends GeneralPointEventProcessor implements IPointEventProcessor {

    @Override
    public String[] init() {
        return new String[]{SystemEvent.FORUM_COMMENT_CREATE.getCode()};
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
            eventName = SystemEvent.ACTIVITY_COMMENT_CREATE.dft();
        } else {
            eventName = SystemEvent.FORUM_COMMENT_CREATE.dft();
        }
        return eventName;
    }
}
