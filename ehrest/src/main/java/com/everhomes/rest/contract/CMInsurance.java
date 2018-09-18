package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

public class CMInsurance {

    private String RentalID;

    private String InsuranceType;

    private BigDecimal InsuranceMoney;

    private String Modifydate;

    public String getModifydate() {
        return Modifydate;
    }

    public void setModifydate(String modifydate) {
        Modifydate = modifydate;
    }

    public String getRentalID() {
        return RentalID;
    }

    public void setRentalID(String rentalID) {
        RentalID = rentalID;
    }

    public String getInsuranceType() {
        return InsuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        InsuranceType = insuranceType;
    }

    public BigDecimal getInsuranceMoney() {
        return InsuranceMoney;
    }

    public void setInsuranceMoney(BigDecimal insuranceMoney) {
        InsuranceMoney = insuranceMoney;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
