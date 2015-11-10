// @formatter:off
// generated at 2015-11-10 14:30:36
package com.everhomes.enterprise;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.enterprise.EnterpriseContactDTO;

public class ListContactsByPhoneRestResponse extends RestResponseBase {

    private EnterpriseContactDTO response;

    public ListContactsByPhoneRestResponse () {
    }

    public EnterpriseContactDTO getResponse() {
        return response;
    }

    public void setResponse(EnterpriseContactDTO response) {
        this.response = response;
    }
}
