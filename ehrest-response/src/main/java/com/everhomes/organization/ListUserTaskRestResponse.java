// @formatter:off
// generated at 2015-12-04 14:52:03
package com.everhomes.organization;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.organization.ListTopicsByTypeCommandResponse;

public class ListUserTaskRestResponse extends RestResponseBase {

    private ListTopicsByTypeCommandResponse response;

    public ListUserTaskRestResponse () {
    }

    public ListTopicsByTypeCommandResponse getResponse() {
        return response;
    }

    public void setResponse(ListTopicsByTypeCommandResponse response) {
        this.response = response;
    }
}
