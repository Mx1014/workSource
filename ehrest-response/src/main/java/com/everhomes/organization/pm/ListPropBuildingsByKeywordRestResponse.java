// @formatter:off
// generated at 2015-11-20 09:40:32
package com.everhomes.organization.pm;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.address.BuildingDTO;

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
