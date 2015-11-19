// @formatter:off
// generated at 2015-11-19 19:54:44
package com.everhomes.activity;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.activity.ActivityDTO;

public class ConfirmRestResponse extends RestResponseBase {

    private ActivityDTO response;

    public ConfirmRestResponse () {
    }

    public ActivityDTO getResponse() {
        return response;
    }

    public void setResponse(ActivityDTO response) {
        this.response = response;
    }
}
