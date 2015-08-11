// @formatter:off
// generated at 2015-08-11 15:30:30
package com.everhomes.community;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.address.CommunityDTO;

public class GetCommunitiesByIdsRestResponse extends RestResponseBase {

    private List<CommunityDTO> response;

    public GetCommunitiesByIdsRestResponse () {
    }

    public List<CommunityDTO> getResponse() {
        return response;
    }

    public void setResponse(List<CommunityDTO> response) {
        this.response = response;
    }
}
