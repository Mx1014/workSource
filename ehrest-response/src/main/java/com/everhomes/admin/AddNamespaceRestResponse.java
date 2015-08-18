// @formatter:off
// generated at 2015-08-18 15:16:38
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
