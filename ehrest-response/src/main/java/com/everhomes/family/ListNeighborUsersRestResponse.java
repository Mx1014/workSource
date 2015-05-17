// @formatter:off
// generated at 2015-05-16 21:41:03
package com.everhomes.family;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.family.NeighborUserDTO;

public class ListNeighborUsersRestResponse extends RestResponseBase {

    private List<NeighborUserDTO> response;

    public ListNeighborUsersRestResponse () {
    }

    public List<NeighborUserDTO> getResponse() {
        return response;
    }

    public void setResponse(List<NeighborUserDTO> response) {
        this.response = response;
    }
}
