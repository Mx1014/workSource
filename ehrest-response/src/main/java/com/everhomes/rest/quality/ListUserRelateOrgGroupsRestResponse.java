// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.quality;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.organization.OrganizationDTO;

public class ListUserRelateOrgGroupsRestResponse extends RestResponseBase {

    private List<OrganizationDTO> response;

    public ListUserRelateOrgGroupsRestResponse () {
    }

    public List<OrganizationDTO> getResponse() {
        return response;
    }

    public void setResponse(List<OrganizationDTO> response) {
        this.response = response;
    }
}
