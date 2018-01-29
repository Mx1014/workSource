package com.everhomes.rest.payment_application;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>applicationDTOs: 申请列表</li>
 *     <li>nextPageAnchor: 下一页锚点 没有则为空</li>
 * </ul>
 * Created by ying.xiong on 2017/12/27.
 */
public class SearchPaymentApplicationResponse {

    private Long nextPageAnchor;
    @ItemType(PaymentApplicationDTO.class)
    private List<PaymentApplicationDTO> applicationDTOs;

    public List<PaymentApplicationDTO> getApplicationDTOs() {
        return applicationDTOs;
    }

    public void setApplicationDTOs(List<PaymentApplicationDTO> applicationDTOs) {
        this.applicationDTOs = applicationDTOs;
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
