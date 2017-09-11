//@formatter:off
package com.everhomes.asset;

/**
 * Created by Wentian Wang on 2017/8/22.
 */

public class exportPaymentBillsDetail {
    private String dateStr;
    private String billGroupName;
    private String targetName;
    private String noticeTel;
    private String amountReceivable;
    private String amountReceived;
    private String amountOwed;
    private String status;
    private String noticeTimes;
    private String contractNum;

    public String getDateStr() {
        return dateStr;
    }

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getBillGroupName() {
        return billGroupName;
    }

    public void setBillGroupName(String billGroupName) {
        this.billGroupName = billGroupName;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getNoticeTel() {
        return noticeTel;
    }

    public void setNoticeTel(String noticeTel) {
        this.noticeTel = noticeTel;
    }

    public String getAmountReceivable() {
        return amountReceivable;
    }

    public void setAmountReceivable(String amountReceivable) {
        this.amountReceivable = amountReceivable;
    }

    public String getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(String amountReceived) {
        this.amountReceived = amountReceived;
    }

    public String getAmountOwed() {
        return amountOwed;
    }

    public void setAmountOwed(String amountOwed) {
        this.amountOwed = amountOwed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNoticeTimes() {
        return noticeTimes;
    }

    public void setNoticeTimes(String noticeTimes) {
        this.noticeTimes = noticeTimes;
    }
}
