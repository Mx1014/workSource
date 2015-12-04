// @formatter:off
// generated at 2015-12-04 14:52:02
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
