// @formatter:off
// generated at 2015-07-30 19:26:52
package com.everhomes.family;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.family.NeighborUserDTO;

public class ListNearbyNeighborUsersRestResponse extends RestResponseBase {

    private List<NeighborUserDTO> response;

    public ListNearbyNeighborUsersRestResponse () {
    }

    public List<NeighborUserDTO> getResponse() {
        return response;
    }

    public void setResponse(List<NeighborUserDTO> response) {
        this.response = response;
    }
}
