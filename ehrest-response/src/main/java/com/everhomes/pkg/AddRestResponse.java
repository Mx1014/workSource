// @formatter:off
// generated at 2015-10-27 15:48:23
package com.everhomes.pkg;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.pkg.ClientPackageFileDTO;

public class AddRestResponse extends RestResponseBase {

    private ClientPackageFileDTO response;

    public AddRestResponse () {
    }

    public ClientPackageFileDTO getResponse() {
        return response;
    }

    public void setResponse(ClientPackageFileDTO response) {
        this.response = response;
    }
}
