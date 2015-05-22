// @formatter:off
// generated at 2015-05-21 22:00:49
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
