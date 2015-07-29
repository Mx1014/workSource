// @formatter:off
// generated at 2015-07-29 16:55:56
package com.everhomes.activity;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.activity.ActivityDTO;

public class CheckinRestResponse extends RestResponseBase {

    private ActivityDTO response;

    public CheckinRestResponse () {
    }

    public ActivityDTO getResponse() {
        return response;
    }

    public void setResponse(ActivityDTO response) {
        this.response = response;
    }
}
