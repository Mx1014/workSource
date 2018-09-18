package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

public class CMContractUnit {

    private String RentalID;

    private String UnitID;

    private String GFA;

    private String NFA;

    private String LFA;

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

    public String getUnitID() {
        return UnitID;
    }

    public void setUnitID(String unitID) {
        UnitID = unitID;
    }

    public String getGFA() {
        return GFA;
    }

    public void setGFA(String GFA) {
        this.GFA = GFA;
    }

    public String getNFA() {
        return NFA;
    }

    public void setNFA(String NFA) {
        this.NFA = NFA;
    }

    public String getLFA() {
        return LFA;
    }

    public void setLFA(String LFA) {
        this.LFA = LFA;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
