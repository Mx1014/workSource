// @formatter:off
// generated at 2015-11-03 16:20:53
package com.everhomes.activity;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.activity.ListActivitiesReponse;

public class ListRestResponse extends RestResponseBase {

    private ListActivitiesReponse response;

    public ListRestResponse () {
    }

    public ListActivitiesReponse getResponse() {
        return response;
    }

    public void setResponse(ListActivitiesReponse response) {
        this.response = response;
    }
}
