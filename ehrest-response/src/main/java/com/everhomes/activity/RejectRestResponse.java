// @formatter:off
// generated at 2015-05-16 21:41:03
package com.everhomes.activity;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.activity.ActivityDTO;

public class RejectRestResponse extends RestResponseBase {

    private ActivityDTO response;

    public RejectRestResponse () {
    }

    public ActivityDTO getResponse() {
        return response;
    }

    public void setResponse(ActivityDTO response) {
        this.response = response;
    }
}
