// @formatter:off
// generated at 2015-11-19 19:54:45
package com.everhomes.organization.admin;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.organization.pm.UnassignedBuildingDTO;

public class OrgRestResponse extends RestResponseBase {

    private List<UnassignedBuildingDTO> response;

    public OrgRestResponse () {
    }

    public List<UnassignedBuildingDTO> getResponse() {
        return response;
    }

    public void setResponse(List<UnassignedBuildingDTO> response) {
        this.response = response;
    }
}
