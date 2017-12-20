package com.everhomes.rest.point;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>id: 商品id</li>
 *     <li>systemId: systemId</li>
 *     <li>status: 状态 {@link com.everhomes.rest.point.PointCommonStatus}</li>
 *     <li>topStatus: 置顶 {@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 *     <li>number: number</li>
 *     <li>shopNumber: shopNumber</li>
 * </ul>
 */
public class UpdatePointGoodCommand {

    @NotNull
    private Long id;
    @NotNull
    private Long systemId;
    private Byte status;
    private Byte topStatus;
    private String number;
    private String shopNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getTopStatus() {
        return topStatus;
    }

    public void setTopStatus(Byte topStatus) {
        this.topStatus = topStatus;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getShopNumber() {
        return shopNumber;
    }

    public void setShopNumber(String shopNumber) {
        this.shopNumber = shopNumber;
    }

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
