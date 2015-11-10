// @formatter:off
// generated at 2015-11-10 14:30:36
package com.everhomes.enterprise.admin;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.enterprise.ListEnterpriseContactResponse;

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
