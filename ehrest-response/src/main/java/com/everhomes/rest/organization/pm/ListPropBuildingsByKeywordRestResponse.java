// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.organization.pm;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.address.BuildingDTO;

public class ListPropBuildingsByKeywordRestResponse extends RestResponseBase {

    private List<BuildingDTO> response;

    public ListPropBuildingsByKeywordRestResponse () {
    }

    public List<BuildingDTO> getResponse() {
        return response;
    }

    public void setResponse(List<BuildingDTO> response) {
        this.response = response;
    }
}
