package com.everhomes.rest.payment_application;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 *     <li>applicationDTOs: 申请列表</li>
 *     <li>nextPageAnchor: 下一页锚点 没有则为空</li>
 * </ul>
 * Created by ying.xiong on 2018/2/1.
 */
public class ListPaymentApplicationByContractResponse {
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
}
