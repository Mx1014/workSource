// @formatter:off
// generated at 2015-08-18 15:16:38
package com.everhomes.organization;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.organization.OrganizationSimpleDTO;

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
