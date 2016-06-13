// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.address;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.address.CommunitySummaryDTO;

public class ListSuggestedCommunitiesRestResponse extends RestResponseBase {

    private List<CommunitySummaryDTO> response;

    public ListSuggestedCommunitiesRestResponse () {
    }

    public List<CommunitySummaryDTO> getResponse() {
        return response;
    }

    public void setResponse(List<CommunitySummaryDTO> response) {
        this.response = response;
    }
}
