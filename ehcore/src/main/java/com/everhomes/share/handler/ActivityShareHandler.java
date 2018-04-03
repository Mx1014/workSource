package com.everhomes.share.handler;

import com.everhomes.bus.LocalEventBus;
import com.everhomes.bus.LocalEventContext;
import com.everhomes.bus.SystemEvent;
import com.everhomes.rest.share.ActivityShareData;
import com.everhomes.rest.share.ShareType;
import com.everhomes.server.schema.tables.pojos.EhActivities;
import com.everhomes.share.ShareTypeHandler;
import com.everhomes.user.UserContext;
import com.everhomes.util.StringHelper;
import org.springframework.stereotype.Component;

/**
 * Created by xq.tian on 2017/12/18.
 */
@Component
public class ActivityShareHandler implements ShareTypeHandler {

    @Override
    public ShareType init() {
        return ShareType.ACTIVITY;
    }

    @Override
    public void execute(String shareData) {
        ActivityShareData data = (ActivityShareData) StringHelper.fromJsonString(shareData, ActivityShareData.class);

        LocalEventBus.publish(event -> {
            LocalEventContext context = new LocalEventContext();
            context.setUid(UserContext.currentUserId());
            context.setNamespaceId(UserContext.getCurrentNamespaceId());
            event.setContext(context);

            event.setEntityType(EhActivities.class.getSimpleName());
            event.setEntityId(data.getActivityId());
            event.setEventName(SystemEvent.ACTIVITY_ACTIVITY_SHARE.dft());
        });
    }
}
