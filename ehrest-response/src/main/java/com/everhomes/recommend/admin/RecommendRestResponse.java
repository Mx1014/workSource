// @formatter:off
// generated at 2015-12-04 14:52:02
package com.everhomes.recommend.admin;

import com.everhomes.rest.recommend.ListRecommendConfigResponse;
import com.everhomes.rest.RestResponseBase;

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
