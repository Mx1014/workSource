// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.organization.admin;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.organization.PmManagementCommunityDTO;

public class OrgListPmManagementComunitesRestResponse extends RestResponseBase {

    private List<PmManagementCommunityDTO> response;

    public OrgListPmManagementComunitesRestResponse () {
    }

    public List<PmManagementCommunityDTO> getResponse() {
        return response;
    }

    public void setResponse(List<PmManagementCommunityDTO> response) {
        this.response = response;
    }
}
