package com.everhomes.rest.contract;

import com.everhomes.rest.customer.EnterpriseCustomerDTO;
import com.everhomes.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * <ul>
 *     <li>syncList: 返回错误结果集</li>
 *     <li>nextPageAnchor: 返回下页锚点</li>
 * </ul>
 */
public class ListSyncDataErrorMsgResponse {
    private List<SyncDataErrorDTO> results;
    private Long nextPageAnchor;

    public List<SyncDataErrorDTO> getResults() {
        return results;
    }

    public void setResults(List<SyncDataErrorDTO> results) {
        this.results = results;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
