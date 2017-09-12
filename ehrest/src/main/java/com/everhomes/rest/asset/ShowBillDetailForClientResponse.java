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
 * <li>showBillDetailForClientDTOList:账单列表，参考{@link com.everhomes.rest.asset.ShowBillDetailForClientDTO}</li>
 *</ul>
 */
public class ShowBillDetailForClientResponse {
    private BigDecimal amountReceivable;
    private BigDecimal amountOwed;
    private String datestr;
    @ItemType(ShowBillDetailForClientDTO.class)
    private List<ShowBillDetailForClientDTO> showBillDetailForClientDTOList;
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

    public List<ShowBillDetailForClientDTO> getShowBillDetailForClientDTOList() {
        return showBillDetailForClientDTOList;
    }

    public void setShowBillDetailForClientDTOList(List<ShowBillDetailForClientDTO> showBillDetailForClientDTOList) {
        this.showBillDetailForClientDTOList = showBillDetailForClientDTOList;
    }

    public ShowBillDetailForClientResponse() {

    }

    public String getDatestr() {
        return datestr;
    }

    public void setDatestr(String datestr) {
        this.datestr = datestr;
    }
}
