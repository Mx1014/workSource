package com.everhomes.rest.decoration;

import java.math.BigDecimal;
import java.util.List;

/**
 * <ul>
 * <li>fee：List 参考{@link com.everhomes.rest.decoration.DecorationFeeDTO}</li>
 * <li>address</li>
 * </ul>
 */
public class GetDecorationFeeResponse {

    private List<DecorationFeeDTO> fee;
    private BigDecimal totalPrice;
    private String address;
    private Double longitude;
    private Double latitude;

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

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
