// @formatter:off
// generated at 2015-10-27 13:49:25
package com.everhomes.admin;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.admin.ServerDTO;

public class AddPersistServerRestResponse extends RestResponseBase {

    private ServerDTO response;

    public AddPersistServerRestResponse () {
    }

    public ServerDTO getResponse() {
        return response;
    }

    public void setResponse(ServerDTO response) {
        this.response = response;
    }
}
