// @formatter:off
// generated at 2015-07-09 01:51:43
package com.everhomes.organization.pm;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.forum.ListPostCommandResponse;

public class ListPmTopicsRestResponse extends RestResponseBase {

    private ListPostCommandResponse response;

    public ListPmTopicsRestResponse () {
    }

    public ListPostCommandResponse getResponse() {
        return response;
    }

    public void setResponse(ListPostCommandResponse response) {
        this.response = response;
    }
}
