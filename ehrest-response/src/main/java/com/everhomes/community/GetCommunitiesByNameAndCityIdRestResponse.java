// @formatter:off
// generated at 2015-08-13 10:45:22
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
