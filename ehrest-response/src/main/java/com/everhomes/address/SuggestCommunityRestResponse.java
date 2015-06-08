// @formatter:off
// generated at 2015-06-08 00:26:53
package com.everhomes.address;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.address.CommunitySummaryDTO;

public class SuggestCommunityRestResponse extends RestResponseBase {

    private CommunitySummaryDTO response;

    public SuggestCommunityRestResponse () {
    }

    public CommunitySummaryDTO getResponse() {
        return response;
    }

    public void setResponse(CommunitySummaryDTO response) {
        this.response = response;
    }
}
