// @formatter:off
// generated at 2015-10-27 13:49:25
package com.everhomes.admin;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.border.BorderDTO;

public class AddBorderRestResponse extends RestResponseBase {

    private BorderDTO response;

    public AddBorderRestResponse () {
    }

    public BorderDTO getResponse() {
        return response;
    }

    public void setResponse(BorderDTO response) {
        this.response = response;
    }
}
