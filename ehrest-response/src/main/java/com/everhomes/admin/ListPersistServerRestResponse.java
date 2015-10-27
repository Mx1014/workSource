// @formatter:off
// generated at 2015-11-03 16:20:53
// generated at 2015-10-21 17:44:17
// generated at 2015-10-26 15:50:45
// generated at 2015-10-27 13:49:25
// generated at 2015-10-27 15:48:23
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
