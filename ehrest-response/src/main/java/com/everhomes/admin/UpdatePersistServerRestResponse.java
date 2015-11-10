// @formatter:off
// generated at 2015-11-10 11:23:24
package com.everhomes.admin;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.admin.ServerDTO;

public class UpdatePersistServerRestResponse extends RestResponseBase {

    private ServerDTO response;

    public UpdatePersistServerRestResponse () {
    }

    public ServerDTO getResponse() {
        return response;
    }

    public void setResponse(ServerDTO response) {
        this.response = response;
    }
}
