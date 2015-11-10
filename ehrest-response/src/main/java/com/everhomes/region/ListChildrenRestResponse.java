// @formatter:off
// generated at 2015-11-10 11:23:24
package com.everhomes.region;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.region.RegionDTO;

public class ListChildrenRestResponse extends RestResponseBase {

    private List<RegionDTO> response;

    public ListChildrenRestResponse () {
    }

    public List<RegionDTO> getResponse() {
        return response;
    }

    public void setResponse(List<RegionDTO> response) {
        this.response = response;
    }
}
