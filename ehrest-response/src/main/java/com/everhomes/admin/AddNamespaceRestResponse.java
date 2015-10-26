// @formatter:off
// generated at 2015-10-26 15:50:45
package com.everhomes.admin;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.admin.NamespaceDTO;

public class AddNamespaceRestResponse extends RestResponseBase {

    private NamespaceDTO response;

    public AddNamespaceRestResponse () {
    }

    public NamespaceDTO getResponse() {
        return response;
    }

    public void setResponse(NamespaceDTO response) {
        this.response = response;
    }
}
