// @formatter:off
// generated at 2015-11-10 11:23:24
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
