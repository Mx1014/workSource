//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 *<ul>
 * <li>billItemName:项目名称</li>
 * <li>amountReceivable:应收金额</li>
 * <li>addressName:楼栋门牌</li>
 * <li>description:描述</li>
 * <li>contractId:合同id</li>
 *</ul>
 */
public class ShowBillDetailForClientDTO {
    private String billItemName;
    private BigDecimal amountReceivable;
    private String addressName;
    private String description;
    private Long contractId;

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getBillItemName() {
        return billItemName;
    }

    public void setBillItemName(String billItemName) {
        this.billItemName = billItemName;
    }

    public BigDecimal getAmountReceivable() {
        return amountReceivable;
    }

    public void setAmountReceivable(BigDecimal amountReceivable) {
        this.amountReceivable = amountReceivable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ShowBillDetailForClientDTO() {

    }
}
