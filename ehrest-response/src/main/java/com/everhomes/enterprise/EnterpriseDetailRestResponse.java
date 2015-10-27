// @formatter:off
// generated at 2015-10-27 15:08:14
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
