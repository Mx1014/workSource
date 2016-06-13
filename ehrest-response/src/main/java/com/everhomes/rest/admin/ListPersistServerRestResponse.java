// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.admin;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.admin.ServerDTO;

public class ListPersistServerRestResponse extends RestResponseBase {

    private List<ServerDTO> response;

    public ListPersistServerRestResponse () {
    }

    public List<ServerDTO> getResponse() {
        return response;
    }

    public void setResponse(List<ServerDTO> response) {
        this.response = response;
    }
}
