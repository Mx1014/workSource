// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.region;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.region.RegionDTO;

public class ListDescendantsRestResponse extends RestResponseBase {

    private List<RegionDTO> response;

    public ListDescendantsRestResponse () {
    }

    public List<RegionDTO> getResponse() {
        return response;
    }

    public void setResponse(List<RegionDTO> response) {
        this.response = response;
    }
}
