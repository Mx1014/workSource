// @formatter:off
package com.everhomes.point;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.messaging.MessagingService;

public class PointMessageAction extends PointAction {

    private MessagingService messagingService;

    private Long targetUid;

    public PointMessageAction() {
        messagingService = PlatformContext.getComponent(MessagingService.class);
    }

    public void doAction() {

    }
}