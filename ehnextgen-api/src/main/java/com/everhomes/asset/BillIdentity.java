//@formatter:off
package com.everhomes.asset;

/**
 * Created by Wentian Wang on 2017/8/29.
 */

public class BillIdentity {
    private String dateStr;
    private Long billGroupId;

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BillIdentity)) return false;

        BillIdentity identity = (BillIdentity) o;

        if (!getDateStr().equals(identity.getDateStr())) return false;
        return getBillGroupId().equals(identity.getBillGroupId());
    }

    @Override
    public int hashCode() {
        int result = getDateStr().hashCode();
        result = 31 * result + getBillGroupId().hashCode();
        return result;
    }

    public Long getBillGroupId() {

        return billGroupId;
    }

    public void setBillGroupId(Long billGroupId) {
        this.billGroupId = billGroupId;
    }
}
