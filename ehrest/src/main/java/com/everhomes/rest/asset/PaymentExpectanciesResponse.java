//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.ArrayList;
import java.util.List;
import com.everhomes.discover.ItemType;

/**
 * Created by Wentian Wang on 2017/8/22.
 */

/**
 *<ul>
 * <li>nextPageOffset:下一次offset</li>
 * <li>list:明细列表，参考{@link com.everhomes.rest.asset.PaymentExpectancyDTO}</li>
 *</ul>
 */
public class PaymentExpectanciesResponse {
    @ItemType(PaymentExpectancyDTO.class)
    List<PaymentExpectancyDTO>  list;
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
