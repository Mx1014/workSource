// @formatter:off
// generated at 2015-11-20 09:40:32
package com.everhomes.community;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.community.BuildingDTO;

public class GetBuildingRestResponse extends RestResponseBase {

    private BuildingDTO response;

    public GetBuildingRestResponse () {
    }

    public BuildingDTO getResponse() {
        return response;
    }

    public void setResponse(BuildingDTO response) {
        this.response = response;
    }
}
