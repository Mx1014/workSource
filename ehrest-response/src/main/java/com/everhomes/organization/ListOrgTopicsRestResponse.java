// @formatter:off
// generated at 2015-11-10 11:23:24
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
