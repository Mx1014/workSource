// @formatter:off
// generated at 2015-11-19 19:54:45
package com.everhomes.recommend;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.recommend.RecommendUserResponse;

public class RecommendUsersRestResponse extends RestResponseBase {

    private RecommendUserResponse response;

    public RecommendUsersRestResponse () {
    }

    public RecommendUserResponse getResponse() {
        return response;
    }

    public void setResponse(RecommendUserResponse response) {
        this.response = response;
    }
}
