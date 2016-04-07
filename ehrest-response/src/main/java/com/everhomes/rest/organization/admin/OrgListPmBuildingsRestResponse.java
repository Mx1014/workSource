// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.organization.admin;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.organization.pm.PmBuildingDTO;

public class OrgListPmBuildingsRestResponse extends RestResponseBase {

    private List<PmBuildingDTO> response;

    public OrgListPmBuildingsRestResponse () {
    }

    public List<PmBuildingDTO> getResponse() {
        return response;
    }

    public void setResponse(List<PmBuildingDTO> response) {
        this.response = response;
    }
}
