// @formatter:off
// generated at 2015-07-22 15:04:21
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
