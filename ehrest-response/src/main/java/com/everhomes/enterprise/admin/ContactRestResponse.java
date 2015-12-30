// @formatter:off
// generated at 2015-12-04 14:52:02
package com.everhomes.enterprise.admin;

import com.everhomes.rest.enterprise.ListEnterpriseContactResponse;
import com.everhomes.rest.RestResponseBase;

public class ContactRestResponse extends RestResponseBase {

    private ListEnterpriseContactResponse response;

    public ContactRestResponse () {
    }

    public ListEnterpriseContactResponse getResponse() {
        return response;
    }

    public void setResponse(ListEnterpriseContactResponse response) {
        this.response = response;
    }
}
