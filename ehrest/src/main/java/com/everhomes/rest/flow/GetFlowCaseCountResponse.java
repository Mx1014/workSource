// @formatter:off
package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>count: 数量</li>
 * </ul>
 */
public class GetFlowCaseCountResponse {

    private Integer count;

    public GetFlowCaseCountResponse(Integer count) {
        this.count = count;
    }

    public GetFlowCaseCountResponse() {
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
