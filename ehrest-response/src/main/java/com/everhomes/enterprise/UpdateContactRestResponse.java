// @formatter:off
// generated at 2015-11-19 19:54:45
package com.everhomes.enterprise;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.enterprise.EnterpriseContactDTO;

public class UpdateContactRestResponse extends RestResponseBase {

    private EnterpriseContactDTO response;

    public UpdateContactRestResponse () {
    }

    public EnterpriseContactDTO getResponse() {
        return response;
    }

    public void setResponse(EnterpriseContactDTO response) {
        this.response = response;
    }
}
