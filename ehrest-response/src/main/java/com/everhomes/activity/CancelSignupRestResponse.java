// @formatter:off
// generated at 2015-08-19 15:26:32
package com.everhomes.activity;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.activity.ActivityDTO;

public class CancelSignupRestResponse extends RestResponseBase {

    private ActivityDTO response;

    public CancelSignupRestResponse () {
    }

    public ActivityDTO getResponse() {
        return response;
    }

    public void setResponse(ActivityDTO response) {
        this.response = response;
    }
}
