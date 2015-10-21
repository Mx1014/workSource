// @formatter:off
<<<<<<< HEAD
// generated at 2015-11-03 16:20:53
=======
// generated at 2015-10-21 17:44:17
>>>>>>> update ehrest-response 2015/10/21
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
