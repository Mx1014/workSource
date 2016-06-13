// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.organization;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.organization.OrganizationSimpleDTO;

public class ListUserRelatedOrganizationsRestResponse extends RestResponseBase {

    private List<OrganizationSimpleDTO> response;

    public ListUserRelatedOrganizationsRestResponse () {
    }

    public List<OrganizationSimpleDTO> getResponse() {
        return response;
    }

    public void setResponse(List<OrganizationSimpleDTO> response) {
        this.response = response;
    }
}
