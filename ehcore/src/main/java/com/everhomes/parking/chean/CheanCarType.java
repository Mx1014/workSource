package com.everhomes.parking.chean;

public class CheanCarType {

    private String CarTypeID;
    private String CarTypeName;
    private String DefaultCharge;
    private String TimeLtd;
    private String StoredLtd;

    public String getCarTypeID() {
        return CarTypeID;
    }

    public void setCarTypeID(String carTypeID) {
        CarTypeID = carTypeID;
    }

    public String getCarTypeName() {
        return CarTypeName;
    }

    public void setCarTypeName(String carTypeName) {
        CarTypeName = carTypeName;
    }

    public String getDefaultCharge() {
        return DefaultCharge;
    }

    public void setDefaultCharge(String defaultCharge) {
        DefaultCharge = defaultCharge;
    }

    public String getTimeLtd() {
        return TimeLtd;
    }

    public void setTimeLtd(String timeLtd) {
        TimeLtd = timeLtd;
    }

    public String getStoredLtd() {
        return StoredLtd;
    }

    public void setStoredLtd(String storedLtd) {
        StoredLtd = storedLtd;
    }
}
