// @formatter:off
// generated at 2015-09-01 15:16:07
package com.everhomes.address;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.address.BuildingDTO;

public class ListBuildingsByKeywordRestResponse extends RestResponseBase {

    private List<BuildingDTO> response;

    public ListBuildingsByKeywordRestResponse () {
    }

    public List<BuildingDTO> getResponse() {
        return response;
    }

    public void setResponse(List<BuildingDTO> response) {
        this.response = response;
    }
}
