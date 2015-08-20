// @formatter:off
// generated at 2015-08-20 18:05:54
package com.everhomes.forum;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.forum.ListPostCommandResponse;

public class SearchRestResponse extends RestResponseBase {

    private ListPostCommandResponse response;

    public SearchRestResponse () {
    }

    public ListPostCommandResponse getResponse() {
        return response;
    }

    public void setResponse(ListPostCommandResponse response) {
        this.response = response;
    }
}
