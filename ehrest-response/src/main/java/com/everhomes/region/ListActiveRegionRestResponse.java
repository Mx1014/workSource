// @formatter:off
// generated at 2015-10-30 14:21:35
package com.everhomes.region;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.region.RegionDTO;

public class ListActiveRegionRestResponse extends RestResponseBase {

    private List<RegionDTO> response;

    public ListActiveRegionRestResponse () {
    }

    public List<RegionDTO> getResponse() {
        return response;
    }

    public void setResponse(List<RegionDTO> response) {
        this.response = response;
    }
}
