//@formatter:off
package com.everhomes.rest.pmkexing;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <ul>
 *     <li>billDate：账单日期</li>
 *     <li>billStatus：账单状态{@link com.everhomes.rest.pmkexing.PmKeXingBillStatus}</li>
 *     <li>receivableAmount: 应收合计</li>
 *     <li>unpaidAmount: 待缴费用</li>
 *     <li>items: 费用项列表 {@link com.everhomes.rest.pmkexing.PmKeXingBillItemDTO}</li>
 * </ul>
 */
public class PmKeXingBillDTO {

    private String billDate;
    private Byte billStatus;
    private BigDecimal receivableAmount = BigDecimal.ZERO;
    private BigDecimal unpaidAmount = BigDecimal.ZERO;
    @ItemType(PmKeXingBillItemDTO.class)
    private List<PmKeXingBillItemDTO> items = new ArrayList<>();

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public BigDecimal getReceivableAmount() {
        return receivableAmount;
    }

    public void setReceivableAmount(BigDecimal receivableAmount) {
        this.receivableAmount = receivableAmount;
    }

    public List<PmKeXingBillItemDTO> getItems() {
        return items;
    }

    public void setItems(List<PmKeXingBillItemDTO> items) {
        this.items = items;
    }

    public BigDecimal getUnpaidAmount() {
        return unpaidAmount;
    }

    public void setUnpaidAmount(BigDecimal unpaidAmount) {
        this.unpaidAmount = unpaidAmount;
    }

    public Byte getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(Byte billStatus) {
        this.billStatus = billStatus;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
