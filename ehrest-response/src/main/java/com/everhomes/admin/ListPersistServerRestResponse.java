// @formatter:off
// generated at 2015-10-29 16:34:51
package com.everhomes.admin;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.admin.ServerDTO;

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
