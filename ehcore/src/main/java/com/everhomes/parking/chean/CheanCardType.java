package com.everhomes.parking.chean;

public class CheanCardType {

    private String TariffID;
    private String TariffName;
    private String CarTypeID;
    private String Charge;

    public String getTariffID() {
        return TariffID;
    }

    public void setTariffID(String tariffID) {
        TariffID = tariffID;
    }

    public String getTariffName() {
        return TariffName;
    }

    public void setTariffName(String tariffName) {
        TariffName = tariffName;
    }

    public String getCarTypeID() {
        return CarTypeID;
    }

    public void setCarTypeID(String carTypeID) {
        CarTypeID = carTypeID;
    }

    public String getCharge() {
        return Charge;
    }

    public void setCharge(String charge) {
        Charge = charge;
    }
}
