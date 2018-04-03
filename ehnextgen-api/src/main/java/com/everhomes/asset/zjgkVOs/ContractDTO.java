//@formatter:off
package com.everhomes.asset.zjgkVOs;

import java.util.List;

/**
 * Created by Wentian Wang on 2017/9/8.
 */

public class ContractDTO {
    private String contractId;
    private String contractNum;
    private String lessee;
    private String settled;
    private String areaSize;
    private String layout;
    private String contractStartDate;
    private String contractExpireDate;
    private String rent;
    private String propertyFeeUnit;
    private String deposit;
    private List<CommunityAddressDTO> apartments;

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    public String getLessee() {
        return lessee;
    }

    public void setLessee(String lessee) {
        this.lessee = lessee;
    }

    public String getSettled() {
        return settled;
    }

    public void setSettled(String settled) {
        this.settled = settled;
    }

    public String getAreaSize() {
        return areaSize;
    }

    public void setAreaSize(String areaSize) {
        this.areaSize = areaSize;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getContractStartDate() {
        return contractStartDate;
    }

    public void setContractStartDate(String contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public String getContractExpireDate() {
        return contractExpireDate;
    }

    public void setContractExpireDate(String contractExpireDate) {
        this.contractExpireDate = contractExpireDate;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }

    public String getPropertyFeeUnit() {
        return propertyFeeUnit;
    }

    public void setPropertyFeeUnit(String propertyFeeUnit) {
        this.propertyFeeUnit = propertyFeeUnit;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public List<CommunityAddressDTO> getApartments() {
        return apartments;
    }

    public void setApartments(List<CommunityAddressDTO> apartments) {
        this.apartments = apartments;
    }
}
