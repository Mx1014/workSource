//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * Created by Wentian Wang on 2017/9/28.
 */

/**
 * @author change by yangcx
 * @date 2018年5月19日----下午8:19:29
 */
/**
 *<ul>
* <li>nextPageAnchor:下页锚点</li>
 * <li>pageSize:显示数量</li>
* <li>list:交易明细的集合，参考{@link PaymentBillResp}</li>
 *</ul>
 */
public class ListPaymentBillResp {
    private Long nextPageAnchor;
    private Long pageSize;
    @ItemType(PaymentBillResp.class)
    private List<PaymentBillResp> list;
    private List<PaymentOrderBillDTO> paymentOrderBillDTOs;

    public ListPaymentBillResp() {

    }
    public ListPaymentBillResp(Long nextPageAnchor,Long pageSize) {
        this.nextPageAnchor = nextPageAnchor;
        this.pageSize = pageSize;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public List<PaymentBillResp> getList() {
        return list;
    }
    public void setList(List<PaymentBillResp> list) {
        this.list = list;
    }
	public List<PaymentOrderBillDTO> getPaymentOrderBillDTOs() {
		return paymentOrderBillDTOs;
	}
	public void setPaymentOrderBillDTOs(List<PaymentOrderBillDTO> paymentOrderBillDTOs) {
		this.paymentOrderBillDTOs = paymentOrderBillDTOs;
	}
}
