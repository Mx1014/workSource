//@formatter:off
package com.everhomes.rest.requisition;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * Created by Wentian Wang on 2018/1/20.
 */

public class ListRequisitionsResponse {
    private Long nextPageAnchor;
    private List<ListRequisitionsDTO> list;//这里如果命名为data，spring会序列化此项形成json，SerializationFeature.FAIL_ON_EMPTY_BEANS为true时会导致报无法写出json错误

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<ListRequisitionsDTO> getList() {
        return list;
    }

    public void setList(List<ListRequisitionsDTO> list) {
        this.list = list;
    }
}
