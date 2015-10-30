// @formatter:off
// generated at 2015-10-30 14:21:35
package com.everhomes.community;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.address.CommunityDTO;

public class GetCommunitiesByNameAndCityIdRestResponse extends RestResponseBase {

    private List<CommunityDTO> response;

    public GetCommunitiesByNameAndCityIdRestResponse () {
    }

    public List<CommunityDTO> getResponse() {
        return response;
    }

    public void setResponse(List<CommunityDTO> response) {
        this.response = response;
    }
}
