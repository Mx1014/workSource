// @formatter:off
// generated at 2015-12-04 14:52:02
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
