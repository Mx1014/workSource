// @formatter:off
// generated at 2015-07-09 01:51:43
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
