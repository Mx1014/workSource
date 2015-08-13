// @formatter:off
// generated at 2015-08-13 10:45:22
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
