// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.address;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.address.CommunityDTO;

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
