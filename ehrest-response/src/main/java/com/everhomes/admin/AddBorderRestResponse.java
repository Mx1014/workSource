// @formatter:off
// generated at 2015-07-22 15:04:21
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
