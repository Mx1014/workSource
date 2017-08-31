package com.everhomes.rest.openapi.shenzhou;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by ying.xiong on 2017/8/30.
 */
public class ZJContractDetail {
    private String contracId;
    private String contractNum;
    private String lessee;
    private String settled;
    private Double areaSize;
    private String layout;
    private Timestamp contractStartDate;
    private Timestamp contractExpireDate;
    private BigDecimal rent;
    private BigDecimal propertyFeeUnit;
    private BigDecimal deposit;
    private List<CommunityAddressDTO> apartments;

    public List<CommunityAddressDTO> getApartments() {
        return apartments;
    }

    public void setApartments(List<CommunityAddressDTO> apartments) {
        this.apartments = apartments;
    }

    public Double getAreaSize() {
        return areaSize;
    }

    public void setAreaSize(Double areaSize) {
        this.areaSize = areaSize;
    }

    public String getContracId() {
        return contracId;
    }

    public void setContracId(String contracId) {
        this.contracId = contracId;
    }

    public Timestamp getContractExpireDate() {
        return contractExpireDate;
    }

    public void setContractExpireDate(Timestamp contractExpireDate) {
        this.contractExpireDate = contractExpireDate;
    }

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    public Timestamp getContractStartDate() {
        return contractStartDate;
    }

    public void setContractStartDate(Timestamp contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getLessee() {
        return lessee;
    }

    public void setLessee(String lessee) {
        this.lessee = lessee;
    }

    public BigDecimal getPropertyFeeUnit() {
        return propertyFeeUnit;
    }

    public void setPropertyFeeUnit(BigDecimal propertyFeeUnit) {
        this.propertyFeeUnit = propertyFeeUnit;
    }

    public BigDecimal getRent() {
        return rent;
    }

    public void setRent(BigDecimal rent) {
        this.rent = rent;
    }

    public String getSettled() {
        return settled;
    }

    public void setSettled(String settled) {
        this.settled = settled;
    }
}
