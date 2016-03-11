// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.organization.admin;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.organization.OrganizationMemberDTO;

public class OrgListOrganizationPersonnelsRestResponse extends RestResponseBase {

    private List<OrganizationMemberDTO> response;

    public OrgListOrganizationPersonnelsRestResponse () {
    }

    public List<OrganizationMemberDTO> getResponse() {
        return response;
    }

    public void setResponse(List<OrganizationMemberDTO> response) {
        this.response = response;
    }
}
