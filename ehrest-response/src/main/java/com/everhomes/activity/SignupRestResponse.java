// @formatter:off
// generated at 2015-06-08 00:26:53
package com.everhomes.activity;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.activity.ActivityDTO;

public class SignupRestResponse extends RestResponseBase {

    private ActivityDTO response;

    public SignupRestResponse () {
    }

    public ActivityDTO getResponse() {
        return response;
    }

    public void setResponse(ActivityDTO response) {
        this.response = response;
    }
}
