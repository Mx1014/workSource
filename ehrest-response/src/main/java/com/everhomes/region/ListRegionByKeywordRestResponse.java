// @formatter:off
// generated at 2015-08-10 20:34:45
package com.everhomes.region;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.region.RegionDTO;

public class ListRegionByKeywordRestResponse extends RestResponseBase {

    private List<RegionDTO> response;

    public ListRegionByKeywordRestResponse () {
    }

    public List<RegionDTO> getResponse() {
        return response;
    }

    public void setResponse(List<RegionDTO> response) {
        this.response = response;
    }
}
