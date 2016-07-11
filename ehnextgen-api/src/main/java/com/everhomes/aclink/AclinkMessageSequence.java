package com.everhomes.aclink;

import com.everhomes.rest.aclink.DoorMessage;

public interface AclinkMessageSequence {
    void pendingMessage(DoorMessage msg);

    void ackMessage(Long seq);
}
