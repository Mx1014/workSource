//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wentian Wang on 2017/8/22.
 */

public class PaymentExpectanciesResponse {
    @ItemType(PaymentExpectancyDTO.class)
    List<PaymentExpectancyDTO> list = new ArrayList<>();
    private Integer nextPageOffset;

    public List<PaymentExpectancyDTO> getList() {
        return list;
    }

    public void setList(List<PaymentExpectancyDTO> list) {
        this.list = list;
    }

    public Integer getNextPageOffset() {
        return nextPageOffset;
    }

    public void setNextPageOffset(Integer nextPageOffset) {
        this.nextPageOffset = nextPageOffset;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
