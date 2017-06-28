package com.everhomes.rest.parking;


import com.everhomes.util.StringHelper;

public class GetRechargeOrderResultResponse {
    private Integer code;
    private String description;
    private Byte rechargeType;

    private ParkingTempFeeDTO tempFee;

    private ParkingCardDTO card;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ParkingCardDTO getCard() {
        return card;
    }

    public void setCard(ParkingCardDTO card) {
        this.card = card;
    }

    public Byte getRechargeType() {
        return rechargeType;
    }

    public void setRechargeType(Byte rechargeType) {
        this.rechargeType = rechargeType;
    }

    public ParkingTempFeeDTO getTempFee() {
        return tempFee;
    }

    public void setTempFee(ParkingTempFeeDTO tempFee) {
        this.tempFee = tempFee;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
