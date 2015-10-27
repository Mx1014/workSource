// @formatter:off
// generated at 2015-10-27 13:49:25
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
