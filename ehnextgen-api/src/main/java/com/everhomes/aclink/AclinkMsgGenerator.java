package com.everhomes.aclink;

import java.util.List;

import com.everhomes.rest.aclink.AclinkWebSocketMessage;
import com.everhomes.rest.aclink.DoorMessage;

public interface AclinkMsgGenerator {

    List<DoorMessage> generateMessages(Long doorId);

    AclinkWebSocketMessage generateWebSocketMessage(Long doorId);

    AclinkWebSocketMessage generateTimeMessage(Long doorId);

	void invalidSyncTimer(Long doorId);

}
