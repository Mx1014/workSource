// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.aclink;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.rest.aclink.AclinkWebSocketMessage;

public class SyncWebsocketMessagesRestResponse extends RestResponseBase {

    private AclinkWebSocketMessage response;

    public SyncWebsocketMessagesRestResponse () {
    }

    public AclinkWebSocketMessage getResponse() {
        return response;
    }

    public void setResponse(AclinkWebSocketMessage response) {
        this.response = response;
    }
}
