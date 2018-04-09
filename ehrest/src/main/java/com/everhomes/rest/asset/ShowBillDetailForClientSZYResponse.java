//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.util.List;

/**
 *<ul>
 * <li>amountReceivable:应收金额</li>
 * <li>amountOwed:待缴金额</li>
 * <li>datestr:账期</li>
 * <li>showBillDetailForClientSZYDTOList:账单列表，参考{@link com.everhomes.rest.asset.ShowBillDetailForClientSZYDTO}</li>
 *</ul>
 */
public class ShowBillDetailForClientSZYResponse {
    private BigDecimal amountReceivable;
    private BigDecimal amountOwed;
    private String datestr;
    @ItemType(ShowBillDetailForClientSZYDTO.class)
    private List<ShowBillDetailForClientSZYDTO> showBillDetailForClientSZYDTOList;
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public BigDecimal getAmountReceivable() {
        return amountReceivable;
    }

    public void setAmountReceivable(BigDecimal amountReceivable) {
        this.amountReceivable = amountReceivable;
    }

    public BigDecimal getAmountOwed() {
        return amountOwed;
    }

    public void setAmountOwed(BigDecimal amountOwed) {
        this.amountOwed = amountOwed;
    }

    public ShowBillDetailForClientSZYResponse() {

    }

    public String getDatestr() {
        return datestr;
    }

    public void setDatestr(String datestr) {
        this.datestr = datestr;
    }

	public List<ShowBillDetailForClientSZYDTO> getShowBillDetailForClientSZYDTOList() {
		return showBillDetailForClientSZYDTOList;
	}

	public void setShowBillDetailForClientSZYDTOList(
			List<ShowBillDetailForClientSZYDTO> showBillDetailForClientSZYDTOList) {
		this.showBillDetailForClientSZYDTOList = showBillDetailForClientSZYDTOList;
	}
}
