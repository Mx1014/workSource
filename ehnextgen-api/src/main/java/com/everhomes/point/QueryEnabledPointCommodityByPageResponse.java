package com.everhomes.point;

import com.everhomes.util.StringHelper;

public class QueryEnabledPointCommodityByPageResponse {

    private PointCommodityList body;

    public PointCommodityList getBody() {
        return body;
    }

    public void setBody(PointCommodityList body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
