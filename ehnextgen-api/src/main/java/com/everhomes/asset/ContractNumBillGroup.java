//@formatter:off
package com.everhomes.asset;

/**
 * Created by Wentian Wang on 2017/12/6.
 */

public class ContractNumBillGroup {
    private String contractNum;
    private Long billGroupId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContractNumBillGroup)) return false;

        ContractNumBillGroup that = (ContractNumBillGroup) o;

        if (getContractNum() != null ? !getContractNum().equals(that.getContractNum()) : that.getContractNum() != null)
            return false;
        return getBillGroupId() != null ? getBillGroupId().equals(that.getBillGroupId()) : that.getBillGroupId() == null;
    }

    @Override
    public int hashCode() {
        int result = getContractNum() != null ? getContractNum().hashCode() : 0;
        result = 31 * result + (getBillGroupId() != null ? getBillGroupId().hashCode() : 0);
        return result;
    }

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    public Long getBillGroupId() {
        return billGroupId;
    }

    public void setBillGroupId(Long billGroupId) {
        this.billGroupId = billGroupId;
    }
}
