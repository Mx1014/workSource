// @formatter:off
// generated at 2015-09-08 16:00:43
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
