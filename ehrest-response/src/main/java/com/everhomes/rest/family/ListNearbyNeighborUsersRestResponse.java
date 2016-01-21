// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.family;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.family.NeighborUserDTO;

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
