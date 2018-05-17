package com.everhomes.rest.decoration;

import java.util.List;

/**
 * <ul>
 * <li>fee：List<DecorationFeeDTO> 参考{@link com.everhomes.rest.decoration.DecorationFeeDTO}</li>
 * <li>address</li>
 * </ul>
 */
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
