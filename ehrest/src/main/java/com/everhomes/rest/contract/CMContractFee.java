package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class CMContractFee {

    private String RentalID;

    private String BillItemName;

    private String StartDate;

    private String EndDate;

    private String CalculateMethod;

    private String DocumentAmt;

    private String ChargeAmt;

    private String TaxAmt;

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

    public String getBillItemName() {
        return BillItemName;
    }

    public void setBillItemName(String billItemName) {
        BillItemName = billItemName;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getCalculateMethod() {
        return CalculateMethod;
    }

    public void setCalculateMethod(String calculateMethod) {
        CalculateMethod = calculateMethod;
    }

    public String getDocumentAmt() {
        return DocumentAmt;
    }

    public void setDocumentAmt(String documentAmt) {
        DocumentAmt = documentAmt;
    }

    public String getChargeAmt() {
        return ChargeAmt;
    }

    public void setChargeAmt(String chargeAmt) {
        ChargeAmt = chargeAmt;
    }

    public String getTaxAmt() {
        return TaxAmt;
    }

    public void setTaxAmt(String taxAmt) {
        TaxAmt = taxAmt;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

