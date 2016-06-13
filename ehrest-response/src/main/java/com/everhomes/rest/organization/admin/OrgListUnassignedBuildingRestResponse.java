// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.organization.admin;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.organization.pm.UnassignedBuildingDTO;

public class OrgListUnassignedBuildingRestResponse extends RestResponseBase {

    private List<UnassignedBuildingDTO> response;

    public OrgListUnassignedBuildingRestResponse () {
    }

    public List<UnassignedBuildingDTO> getResponse() {
        return response;
    }

    public void setResponse(List<UnassignedBuildingDTO> response) {
        this.response = response;
    }
}
