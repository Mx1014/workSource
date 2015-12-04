// @formatter:off
// generated at 2015-12-04 14:52:02
package com.everhomes.enterprise.admin;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.enterprise.EnterpriseDTO;

public class EnterpriseRestResponse extends RestResponseBase {

    private EnterpriseDTO response;

    public EnterpriseRestResponse () {
    }

    public EnterpriseDTO getResponse() {
        return response;
    }

    public void setResponse(EnterpriseDTO response) {
        this.response = response;
    }
}
