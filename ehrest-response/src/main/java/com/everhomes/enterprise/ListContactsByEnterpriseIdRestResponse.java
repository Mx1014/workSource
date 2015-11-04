// @formatter:off
// generated at 2015-11-03 16:20:53
package com.everhomes.enterprise;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.enterprise.ListEnterpriseContactResponse;

public class ListContactsByEnterpriseIdRestResponse extends RestResponseBase {

    private ListEnterpriseContactResponse response;

    public ListContactsByEnterpriseIdRestResponse () {
    }

    public ListEnterpriseContactResponse getResponse() {
        return response;
    }

    public void setResponse(ListEnterpriseContactResponse response) {
        this.response = response;
    }
}
