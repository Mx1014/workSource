//@formatter:off
package com.everhomes.asset.zjgkVOs;

import java.util.List;

/**
 * Created by Wentian Wang on 2017/9/8.
 */

public class ContractBillsDTO {
    private String billID;
    private String billDate;
    private String feeName;
    private Byte payFlag;
    private String cutomerIdentifier;
    private String customerName;
    private List< CommunityAddressDTO> apartments;
    private String noticeTels;
    private String amountReceivable;
    private String amountReceived;
    private String amountOwed;

    public ContractBillsDTO() {
    }

    public String getBillID() {
        return billID;
    }

    public void setBillID(String billID) {
        this.billID = billID;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }

    public Byte getPayFlag() {
        return payFlag;
    }

    public void setPayFlag(Byte payFlag) {
        this.payFlag = payFlag;
    }

    public String getCutomerIdentifier() {
        return cutomerIdentifier;
    }

    public void setCutomerIdentifier(String cutomerIdentifier) {
        this.cutomerIdentifier = cutomerIdentifier;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<CommunityAddressDTO> getApartments() {
        return apartments;
    }

    public void setApartments(List<CommunityAddressDTO> apartments) {
        this.apartments = apartments;
    }

    public String getNoticeTels() {
        return noticeTels;
    }

    public void setNoticeTels(String noticeTels) {
        this.noticeTels = noticeTels;
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
}
