// @formatter:off
// generated at 2015-07-05 23:21:21
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
