package com.everhomes.rest.decoration;

import java.util.List;

public class GetDecorationFeeResponse {

    private List<DecorationFeeDTO> fee;
    private String address;

    public List<DecorationFeeDTO> getFee() {
        return fee;
    }

    public void setFee(List<DecorationFeeDTO> fee) {
        this.fee = fee;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
