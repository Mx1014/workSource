// @formatter:off
// generated at 2015-11-20 09:40:32
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
