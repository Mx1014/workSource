//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * @author created by yangcx
 * @date 2018年5月23日----上午11:08:33
 */
/**
 *<ul>
* <li>nextPageAnchor:下页锚点</li>
 * <li>pageSize:显示数量</li>
* <li>list:交易明细的集合，参考{@link PaymentBillRespForEnt}</li>
 *</ul>
 */
public class ListPaymentBillRespForEnt {
    private Long nextPageAnchor;
    private Long pageSize;
    @ItemType(PaymentBillResp.class)
    private List<PaymentBillRespForEnt> list;

    public ListPaymentBillRespForEnt() {

    }
    public ListPaymentBillRespForEnt(Long nextPageAnchor,Long pageSize) {
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
	public List<PaymentBillRespForEnt> getList() {
		return list;
	}
	public void setList(List<PaymentBillRespForEnt> list) {
		this.list = list;
	}
}
