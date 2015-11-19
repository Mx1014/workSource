// @formatter:off
// generated at 2015-11-19 19:54:45
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
