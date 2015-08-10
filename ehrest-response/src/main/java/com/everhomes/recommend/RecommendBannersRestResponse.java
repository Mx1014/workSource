// @formatter:off
// generated at 2015-08-10 11:20:28
package com.everhomes.recommend;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.recommend.RecommendBannerListResponse;

public class RecommendBannersRestResponse extends RestResponseBase {

    private RecommendBannerListResponse response;

    public RecommendBannersRestResponse () {
    }

    public RecommendBannerListResponse getResponse() {
        return response;
    }

    public void setResponse(RecommendBannerListResponse response) {
        this.response = response;
    }
}
