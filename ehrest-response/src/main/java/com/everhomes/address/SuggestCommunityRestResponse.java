// @formatter:off
// generated at 2015-07-22 15:04:21
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
