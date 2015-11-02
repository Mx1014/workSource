// @formatter:off
// generated at 2015-10-30 14:21:35
package com.everhomes.enterprise;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.enterprise.EnterpriseDTO;

public class EnterpriseDetailRestResponse extends RestResponseBase {

    private EnterpriseDTO response;

    public EnterpriseDetailRestResponse () {
    }

    public EnterpriseDTO getResponse() {
        return response;
    }

    public void setResponse(EnterpriseDTO response) {
        this.response = response;
    }
}
