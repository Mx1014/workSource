// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.community.admin;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.address.CommunityDTO;

public class CommunityGetNearbyCommunitiesByIdRestResponse extends RestResponseBase {

    private List<CommunityDTO> response;

    public CommunityGetNearbyCommunitiesByIdRestResponse () {
    }

    public List<CommunityDTO> getResponse() {
        return response;
    }

    public void setResponse(List<CommunityDTO> response) {
        this.response = response;
    }
}
