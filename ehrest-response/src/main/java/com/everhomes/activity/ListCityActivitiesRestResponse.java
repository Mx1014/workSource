// @formatter:off
// generated at 2015-11-03 16:20:53
package com.everhomes.activity;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.activity.ListNearbyActivitiesResponse;

public class ListCityActivitiesRestResponse extends RestResponseBase {

    private ListNearbyActivitiesResponse response;

    public ListCityActivitiesRestResponse () {
    }

    public ListNearbyActivitiesResponse getResponse() {
        return response;
    }

    public void setResponse(ListNearbyActivitiesResponse response) {
        this.response = response;
    }
}
