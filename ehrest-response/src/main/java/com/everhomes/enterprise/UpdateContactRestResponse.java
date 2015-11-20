// @formatter:off
// generated at 2015-11-20 09:40:32
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
