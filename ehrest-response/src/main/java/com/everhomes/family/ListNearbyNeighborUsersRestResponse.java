// @formatter:off
// generated at 2015-09-18 18:44:17
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
