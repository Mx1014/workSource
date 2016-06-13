// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.admin;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.border.BorderDTO;

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
