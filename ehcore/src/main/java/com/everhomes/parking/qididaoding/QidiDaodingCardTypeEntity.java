// @formatter:off
package com.everhomes.parking.qididaoding;

import java.math.BigDecimal;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/4/17 14:51
 */
public class QidiDaodingCardTypeEntity {
    private String parkingId;
    private String id;
    private String typeName;
    private Integer monthCount;
    private BigDecimal unitPrice;
    private Integer monthType;

    public String getParkingId() {
        return parkingId;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getMonthCount() {
        return monthCount;
    }

    public void setMonthCount(Integer monthCount) {
        this.monthCount = monthCount;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getMonthType() {
        return monthType;
    }

    public void setMonthType(Integer monthType) {
        this.monthType = monthType;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
