// @formatter:off
// generated at 2015-08-14 09:54:22
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
