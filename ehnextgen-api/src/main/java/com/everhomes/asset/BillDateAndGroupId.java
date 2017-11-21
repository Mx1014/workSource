//@formatter:off
package com.everhomes.asset;

/**
 * Created by Wentian Wang on 2017/11/9.
 */

public class BillDateAndGroupId {
    private String dateStr;
    private Long billGroupId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BillDateAndGroupId)) return false;

        BillDateAndGroupId that = (BillDateAndGroupId) o;

        if (getDateStr() != null ? !getDateStr().equals(that.getDateStr()) : that.getDateStr() != null) return false;
        return getBillGroupId() != null ? getBillGroupId().equals(that.getBillGroupId()) : that.getBillGroupId() == null;
    }

    @Override
    public int hashCode() {
        int result = getDateStr() != null ? getDateStr().hashCode() : 0;
        result = 31 * result + (getBillGroupId() != null ? getBillGroupId().hashCode() : 0);
        return result;
    }

    public String getDateStr() {

        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public Long getBillGroupId() {
        return billGroupId;
    }

    public void setBillGroupId(Long billGroupId) {
        this.billGroupId = billGroupId;
    }
}
