// @formatter:off
// generated at 2015-08-13 10:45:22
package com.everhomes.admin;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.border.BorderDTO;

public class ListBorderRestResponse extends RestResponseBase {

    private List<BorderDTO> response;

    public ListBorderRestResponse () {
    }

    public List<BorderDTO> getResponse() {
        return response;
    }

    public void setResponse(List<BorderDTO> response) {
        this.response = response;
    }
}
