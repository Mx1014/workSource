// @formatter:off
// generated at 2015-06-01 21:15:04
package com.everhomes.activity;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.activity.ActivityListResponse;

public class ListRestResponse extends RestResponseBase {

    private ActivityListResponse response;

    public ListRestResponse () {
    }

    public ActivityListResponse getResponse() {
        return response;
    }

    public void setResponse(ActivityListResponse response) {
        this.response = response;
    }
}
