// @formatter:off
// generated at 2015-07-30 15:54:51
package com.everhomes.activity;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.activity.ActivityListResponse;

public class FindActivityDetailsRestResponse extends RestResponseBase {

    private ActivityListResponse response;

    public FindActivityDetailsRestResponse () {
    }

    public ActivityListResponse getResponse() {
        return response;
    }

    public void setResponse(ActivityListResponse response) {
        this.response = response;
    }
}
