// @formatter:off
// generated at 2015-10-21 17:44:18
package com.everhomes.organization.pm;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.organization.pm.OrganizationOrderDTO;

public class CreatePmBillOrderRestResponse extends RestResponseBase {

    private OrganizationOrderDTO response;

    public CreatePmBillOrderRestResponse () {
    }

    public OrganizationOrderDTO getResponse() {
        return response;
    }

    public void setResponse(OrganizationOrderDTO response) {
        this.response = response;
    }
}
