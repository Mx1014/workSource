// @formatter:off
// generated at 2015-05-16 21:41:02
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
