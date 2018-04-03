//@formatter:off
package com.everhomes.asset;

import java.util.List;

/**
 * Created by Wentian Wang on 2017/8/15.
 */

public class BillDTO_zj {
    private String dateStr;
    private String contractNum;
    private String totalNeedMoney;
    private String nextPageOffset;
    private List<BillDetailDTO_zj> details;

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    public String getTotalNeedMoney() {
        return totalNeedMoney;
    }

    public void setTotalNeedMoney(String totalNeedMoney) {
        this.totalNeedMoney = totalNeedMoney;
    }

    public String getNextPageOffset() {
        return nextPageOffset;
    }

    public void setNextPageOffset(String nextPageOffset) {
        this.nextPageOffset = nextPageOffset;
    }

    public List<BillDetailDTO_zj> getDetails() {
        return details;
    }

    public void setDetails(List<BillDetailDTO_zj> details) {
        this.details = details;
    }
}
