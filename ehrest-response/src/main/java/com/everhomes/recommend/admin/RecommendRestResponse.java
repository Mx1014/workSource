// @formatter:off
// generated at 2015-11-03 16:20:53
package com.everhomes.recommend.admin;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.recommend.ListRecommendConfigResponse;

public class RecommendRestResponse extends RestResponseBase {

    private ListRecommendConfigResponse response;

    public RecommendRestResponse () {
    }

    public ListRecommendConfigResponse getResponse() {
        return response;
    }

    public void setResponse(ListRecommendConfigResponse response) {
        this.response = response;
    }
}
