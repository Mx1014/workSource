// @formatter:off
// generated at 2015-10-30 14:21:35
package com.everhomes.admin;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.admin.NamespaceDTO;

public class UpdateNamespaceRestResponse extends RestResponseBase {

    private NamespaceDTO response;

    public UpdateNamespaceRestResponse () {
    }

    public NamespaceDTO getResponse() {
        return response;
    }

    public void setResponse(NamespaceDTO response) {
        this.response = response;
    }
}
