// @formatter:off
// generated at 2015-10-29 16:34:51
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
