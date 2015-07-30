// @formatter:off
// generated at 2015-07-30 19:26:52
package com.everhomes.address;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.address.CommunitySummaryDTO;

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
