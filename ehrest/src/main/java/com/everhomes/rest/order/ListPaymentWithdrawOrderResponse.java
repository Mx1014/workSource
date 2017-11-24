package com.everhomes.rest.order;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>orders: 订单信息，{@link com.everhomes.rest.order.PaymentWithdrawOrderDTO}</li>
 *     <li>nextPageAnchor:  下一页锚点，如果没有下一页则为空</li>
 * </ul>
 */
public class ListPaymentWithdrawOrderResponse {
    @ItemType(PaymentWithdrawOrderDTO.class)
    private List<PaymentWithdrawOrderDTO> orders;
	
    private Long nextPageAnchor;

    public List<PaymentWithdrawOrderDTO> getOrders() {
        return orders;
    }

    public void setOrders(List<PaymentWithdrawOrderDTO> orders) {
        this.orders = orders;
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
