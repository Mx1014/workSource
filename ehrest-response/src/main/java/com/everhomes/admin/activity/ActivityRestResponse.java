// @formatter:off
// generated at 2015-11-10 11:23:24
package com.everhomes.admin.activity;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.activity.ListActivitiesReponse;

public class ActivityRestResponse extends RestResponseBase {

    private ListActivitiesReponse response;

    public ActivityRestResponse () {
    }

    public ListActivitiesReponse getResponse() {
        return response;
    }

    public void setResponse(ListActivitiesReponse response) {
        this.response = response;
    }
}
