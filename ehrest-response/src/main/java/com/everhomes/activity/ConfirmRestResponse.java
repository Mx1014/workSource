// @formatter:off
// generated at 2015-06-01 21:15:04
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
