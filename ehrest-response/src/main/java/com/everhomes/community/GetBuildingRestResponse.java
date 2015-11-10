// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.community;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.community.BuildingDTO;

public class GetBuildingRestResponse extends RestResponseBase {

    private List<BuildingDTO> response;

    public GetBuildingRestResponse () {
    }

    public List<BuildingDTO> getResponse() {
        return response;
    }

    public void setResponse(List<BuildingDTO> response) {
        this.response = response;
    }
}
