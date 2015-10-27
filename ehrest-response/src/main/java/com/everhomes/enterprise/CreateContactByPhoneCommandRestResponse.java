// @formatter:off
// generated at 2015-10-27 15:08:14
package com.everhomes.enterprise;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.enterprise.EnterpriseContactDTO;

public class CreateContactByPhoneCommandRestResponse extends RestResponseBase {

    private EnterpriseContactDTO response;

    public CreateContactByPhoneCommandRestResponse () {
    }

    public EnterpriseContactDTO getResponse() {
        return response;
    }

    public void setResponse(EnterpriseContactDTO response) {
        this.response = response;
    }
}
