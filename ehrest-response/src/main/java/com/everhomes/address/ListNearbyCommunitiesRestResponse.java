// @formatter:off
// generated at 2015-09-01 15:16:07
package com.everhomes.address;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.address.CommunityDTO;

public class ListNearbyCommunitiesRestResponse extends RestResponseBase {

    private List<CommunityDTO> response;

    public ListNearbyCommunitiesRestResponse () {
    }

    public List<CommunityDTO> getResponse() {
        return response;
    }

    public void setResponse(List<CommunityDTO> response) {
        this.response = response;
    }
}
