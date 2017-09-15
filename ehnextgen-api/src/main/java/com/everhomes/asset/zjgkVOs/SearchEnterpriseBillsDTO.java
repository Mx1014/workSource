//@formatter:off
package com.everhomes.asset.zjgkVOs;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Wentian Wang on 2017/9/5.
 */

public class SearchEnterpriseBillsDTO {
    private String billID;
    private String billDate;
    private String feeName;
    private Byte payFlag;
    private String customerIdentifier;
    private String customerName;
    @ItemType(CommunityAddressDTO.class)
    private List<CommunityAddressDTO> apartments;
    private String noticeTels;
    private String amountReceivable;
    private String amountOwed;
    private String amountReceived;
    private String contractNum;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
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

    public String getCustomerIdentifier() {
        return customerIdentifier;
    }

    public void setCustomerIdentifier(String customerIdentifier) {
        this.customerIdentifier = customerIdentifier;
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

    public String getAmountOwed() {
        return amountOwed;
    }

    public void setAmountOwed(String amountOwed) {
        this.amountOwed = amountOwed;
    }

    public String getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(String amountReceived) {
        this.amountReceived = amountReceived;
    }
}
