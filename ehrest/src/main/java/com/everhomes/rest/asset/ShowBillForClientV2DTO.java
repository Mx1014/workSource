//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2017/11/15.
 */

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 *<ul>
 * <li>billGroupName:账单组名称</li>
 * <li>billGroupId:账单组id</li>
 * <li>billDuration:账期</li>
 * <li>overAllAmountReceivabled:应收金额总计</li>
 * <li>overAllAmountOwed:待缴金额总计</li>
 * <li>isPaymentAvailable:是否启用缴纳功能</li>
 * <li>isContractAvailable:是否启用查看合同</li>
 *</ul>
 */
public class ShowBillForClientV2DTO {
    private String billGroupName;
    private Long billGroupId;
    private String billDuration;
    private String overAllAmountReceivabled;
    private String overAllAmountOwed;
    private Byte isPaymentAvailable;
    private Byte isContractAvailable;

    public Long getBillGroupId() {
        return billGroupId;
    }

    public void setBillGroupId(Long billGroupId) {
        this.billGroupId = billGroupId;
    }

    public Byte getIsPaymentAvailable() {
        return isPaymentAvailable;
    }

    public void setIsPaymentAvailable(Byte isPaymentAvailable) {
        this.isPaymentAvailable = isPaymentAvailable;
    }

    public Byte getIsContractAvailable() {
        return isContractAvailable;
    }

    public void setIsContractAvailable(Byte isContractAvailable) {
        this.isContractAvailable = isContractAvailable;
    }

    public String getBillGroupName() {
        return billGroupName;
    }

    public void setBillGroupName(String billGroupName) {
        this.billGroupName = billGroupName;
    }

    public String getBillDuration() {
        return billDuration;
    }

    public void setBillDuration(String billDuration) {
        this.billDuration = billDuration;
    }

    public String getOverAllAmountReceivabled() {
        return overAllAmountReceivabled;
    }

    public void setOverAllAmountReceivabled(String overAllAmountReceivabled) {
        this.overAllAmountReceivabled = overAllAmountReceivabled;
    }

    public String getOverAllAmountOwed() {
        return overAllAmountOwed;
    }

    public void setOverAllAmountOwed(String overAllAmountOwed) {
        this.overAllAmountOwed = overAllAmountOwed;
    }
}
