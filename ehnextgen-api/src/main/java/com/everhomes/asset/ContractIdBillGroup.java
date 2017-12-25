//@formatter:off
package com.everhomes.asset;

/**
 * Created by Wentian Wang on 2017/12/6.
 */

public class ContractIdBillGroup {
    private Long contractId;
    private Long billGroupId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContractIdBillGroup)) return false;

        ContractIdBillGroup that = (ContractIdBillGroup) o;

        if (getContractId() != null ? !getContractId().equals(that.getContractId()) : that.getContractId() != null)
            return false;
        return getBillGroupId() != null ? getBillGroupId().equals(that.getBillGroupId()) : that.getBillGroupId() == null;
    }

    @Override
    public int hashCode() {
        int result = getContractId() != null ? getContractId().hashCode() : 0;
        result = 31 * result + (getBillGroupId() != null ? getBillGroupId().hashCode() : 0);
        return result;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Long getBillGroupId() {
        return billGroupId;
    }

    public void setBillGroupId(Long billGroupId) {
        this.billGroupId = billGroupId;
    }
}
