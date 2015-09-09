// @formatter:off
// generated at 2015-09-08 21:01:07
package com.everhomes.region;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.region.RegionDTO;

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
