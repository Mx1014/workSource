// @formatter:off
// generated at 2015-11-03 16:20:53
// generated at 2015-10-21 17:44:17
// generated at 2015-10-26 15:50:45
// generated at 2015-10-27 13:49:25
// generated at 2015-10-27 15:48:23
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
