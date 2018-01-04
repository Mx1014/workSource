package com.everhomes.share.handler;

import com.everhomes.bus.LocalEventBus;
import com.everhomes.bus.LocalEventContext;
import com.everhomes.bus.SystemEvent;
import com.everhomes.rest.share.PostShareData;
import com.everhomes.rest.share.ShareType;
import com.everhomes.server.schema.tables.pojos.EhForumPosts;
import com.everhomes.share.ShareTypeHandler;
import com.everhomes.user.UserContext;
import com.everhomes.util.StringHelper;
import org.springframework.stereotype.Component;

/**
 * Created by xq.tian on 2017/12/18.
 */
@Component
public class PostShareHandler implements ShareTypeHandler {

    @Override
    public ShareType init() {
        return ShareType.POST;
    }

    @Override
    public void execute(String shareData) {
        PostShareData data = (PostShareData) StringHelper.fromJsonString(shareData, PostShareData.class);

        LocalEventBus.publish(event -> {
            LocalEventContext context = new LocalEventContext();
            context.setUid(UserContext.currentUserId());
            context.setNamespaceId(UserContext.getCurrentNamespaceId());
            event.setContext(context);

            event.setEntityType(EhForumPosts.class.getSimpleName());
            event.setEntityId(data.getPostId());
            event.setEventName(SystemEvent.FORUM_POST_SHARE.dft());
        });
    }
}