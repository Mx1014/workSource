package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

public class CMInsurance {

    private String RentalID;

    private String InsuranceType;

    private String InsuranceMoney;

    private String ModifyDate;

    public String getModifyDate() {
        return ModifyDate;
    }

    public void setModifyDate(String modifyDate) {
        ModifyDate = modifyDate;
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

    public String getInsuranceMoney() {
        return InsuranceMoney;
    }

    public void setInsuranceMoney(String insuranceMoney) {
        InsuranceMoney = insuranceMoney;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
