//@formatter:off
package com.everhomes.rest.Requisition;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * Created by Wentian Wang on 2018/1/20.
 */

public class ListRequisitionsResponse {
    private Long nextPageAnchor;
    private List<ListRequisitionsDTO> data;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public void setData(List<ListRequisitionsDTO> data) {
        this.data = data;
    }
}
