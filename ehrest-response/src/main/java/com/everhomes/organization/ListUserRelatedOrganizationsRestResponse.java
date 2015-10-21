// @formatter:off
<<<<<<< HEAD
// generated at 2015-11-03 16:20:54
=======
// generated at 2015-10-21 17:44:18
>>>>>>> update ehrest-response 2015/10/21
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
