// @formatter:off
// generated at 2015-10-27 15:08:14
package com.everhomes.organization;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.forum.ListPostCommandResponse;

public class ListOrgTopicsRestResponse extends RestResponseBase {

    private ListPostCommandResponse response;

    public ListOrgTopicsRestResponse () {
    }

    public ListPostCommandResponse getResponse() {
        return response;
    }

    public void setResponse(ListPostCommandResponse response) {
        this.response = response;
    }
}
