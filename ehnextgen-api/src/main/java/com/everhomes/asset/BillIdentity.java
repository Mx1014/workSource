//@formatter:off
package com.everhomes.asset;

/**
 * Created by Wentian Wang on 2017/8/29.
 */

public class BillIdentity {
    private String dateStr;
    private Long billGroupId;
    private String contract;

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }



    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BillIdentity)) return false;

        BillIdentity that = (BillIdentity) o;

        if (getDateStr() != null ? !getDateStr().equals(that.getDateStr()) : that.getDateStr() != null) return false;
        if (getBillGroupId() != null ? !getBillGroupId().equals(that.getBillGroupId()) : that.getBillGroupId() != null)
            return false;
        return getContract() != null ? getContract().equals(that.getContract()) : that.getContract() == null;
    }

    @Override
    public int hashCode() {
        int result = getDateStr() != null ? getDateStr().hashCode() : 0;
        result = 31 * result + (getBillGroupId() != null ? getBillGroupId().hashCode() : 0);
        result = 31 * result + (getContract() != null ? getContract().hashCode() : 0);
        return result;
    }

    public Long getBillGroupId() {

        return billGroupId;
    }

    public void setBillGroupId(Long billGroupId) {
        this.billGroupId = billGroupId;
    }
}
