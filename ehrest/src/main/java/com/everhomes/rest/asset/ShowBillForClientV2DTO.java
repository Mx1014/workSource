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
 * <li>overAllAmountOwed:待缴金额总计</li>
 * <li>addressStr:包含的地址</li>
 * <li>contractId:合同id</li>
 *</ul>
 */
public class ShowBillForClientV2DTO {
    private String billGroupName;
    private String overAllAmountOwed;
    private Long billGroupId;
    private String addressStr;
    private String contractId;
    @ItemType(BillForClientV2.class)
    List<BillForClientV2> bills;

    public ShowBillForClientV2DTO(Long billGroupId, String contractId) {
        this.billGroupId = billGroupId;
        this.contractId = contractId;
    }


    public ShowBillForClientV2DTO(String billGroupName,String contractId) {
        this.billGroupName = billGroupName;
        this.contractId = contractId;
    }


    @Override

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShowBillForClientV2DTO)) return false;

        ShowBillForClientV2DTO that = (ShowBillForClientV2DTO) o;

        if (getBillGroupId() != null ? !getBillGroupId().equals(that.getBillGroupId()) : that.getBillGroupId() != null)
            return false;
        return getContractId() != null ? getContractId().equals(that.getContractId()) : that.getContractId() == null;
    }

    @Override
    public int hashCode() {
        int result = getBillGroupId() != null ? getBillGroupId().hashCode() : 0;
        result = 31 * result + (getContractId() != null ? getContractId().hashCode() : 0);
        return result;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getBillGroupName() {
        return billGroupName;
    }

    public void setBillGroupName(String billGroupName) {
        this.billGroupName = billGroupName;
    }

    public String getOverAllAmountOwed() {
        return overAllAmountOwed;
    }

    public void setOverAllAmountOwed(String overAllAmountOwed) {
        this.overAllAmountOwed = overAllAmountOwed;
    }

    public Long getBillGroupId() {
        return billGroupId;
    }

    public void setBillGroupId(Long billGroupId) {
        this.billGroupId = billGroupId;
    }

    public String getAddressStr() {
        return addressStr;
    }

    public void setAddressStr(String addressStr) {
        this.addressStr = addressStr;
    }

    public List<BillForClientV2> getBills() {
        return bills;
    }

    public void setBills(List<BillForClientV2> bills) {
        this.bills = bills;
    }
}
