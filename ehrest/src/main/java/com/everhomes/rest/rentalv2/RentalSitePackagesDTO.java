package com.everhomes.rest.rentalv2;

import java.math.BigDecimal;

/**
 * <ul>
 * <li>id：id</li>
 * <li>name：套餐名</li>
 * <li>rentalSiteId：场所id</li>
 * <li>rentalType： time(0),halfday(1){@link com.everhomes.rest.rentalv2.RentalType} </li>
 * <li>price：场所价格</li>
 * <li>	originalPrice：     	原价（如果不为null则price为打折价）	</li>
 * <li>halfsitePrice：半场价格</li>
 * <li>	halfsiteOriginalPrice：     	半场原价（如果不为null则price为打折价）	</li>
 * <li>orgMemberOriginalPrice: 原价-如果打折则有(企业内部价)</li>
 * <li>orgMemberPrice: 实际价格-打折则为折后价(企业内部价)</li>
 * <li>approvingUserOriginalPrice: 原价-如果打折则有（外部客户价）</li>
 * <li>approvingUserPrice: 实际价格-打折则为折后价（外部客户价）</li>
 * </ul>
 */

public class RentalSitePackagesDTO {
    private Long id;
    private String name;
    private Long rentalSiteId;
    private Byte rentalType;
    private java.math.BigDecimal price;
    private java.math.BigDecimal originalPrice;
    private java.math.BigDecimal halfsitePrice;
    private java.math.BigDecimal halfsiteOriginalPrice;
    private BigDecimal orgMemberOriginalPrice;
    private BigDecimal orgMemberPrice;

    private BigDecimal approvingUserOriginalPrice;
    private BigDecimal approvingUserPrice;
    private BigDecimal halfOrgMemberOriginalPrice;
    private BigDecimal halfOrgMemberPrice;
    private BigDecimal halfApprovingUserOriginalPrice;
    private BigDecimal halfApprovingUserPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getRentalSiteId() {
        return rentalSiteId;
    }

    public void setRentalSiteId(Long rentalSiteId) {
        this.rentalSiteId = rentalSiteId;
    }

    public Byte getRentalType() {
        return rentalType;
    }

    public void setRentalType(Byte rentalType) {
        this.rentalType = rentalType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getHalfsitePrice() {
        return halfsitePrice;
    }

    public void setHalfsitePrice(BigDecimal halfsitePrice) {
        this.halfsitePrice = halfsitePrice;
    }

    public BigDecimal getHalfsiteOriginalPrice() {
        return halfsiteOriginalPrice;
    }

    public void setHalfsiteOriginalPrice(BigDecimal halfsiteOriginalPrice) {
        this.halfsiteOriginalPrice = halfsiteOriginalPrice;
    }

    public BigDecimal getOrgMemberOriginalPrice() {
        return orgMemberOriginalPrice;
    }

    public void setOrgMemberOriginalPrice(BigDecimal orgMemberOriginalPrice) {
        this.orgMemberOriginalPrice = orgMemberOriginalPrice;
    }

    public BigDecimal getOrgMemberPrice() {
        return orgMemberPrice;
    }

    public void setOrgMemberPrice(BigDecimal orgMemberPrice) {
        this.orgMemberPrice = orgMemberPrice;
    }

    public BigDecimal getApprovingUserOriginalPrice() {
        return approvingUserOriginalPrice;
    }

    public void setApprovingUserOriginalPrice(BigDecimal approvingUserOriginalPrice) {
        this.approvingUserOriginalPrice = approvingUserOriginalPrice;
    }

    public BigDecimal getApprovingUserPrice() {
        return approvingUserPrice;
    }

    public void setApprovingUserPrice(BigDecimal approvingUserPrice) {
        this.approvingUserPrice = approvingUserPrice;
    }

    public BigDecimal getHalfOrgMemberOriginalPrice() {
        return halfOrgMemberOriginalPrice;
    }

    public void setHalfOrgMemberOriginalPrice(BigDecimal halfOrgMemberOriginalPrice) {
        this.halfOrgMemberOriginalPrice = halfOrgMemberOriginalPrice;
    }

    public BigDecimal getHalfOrgMemberPrice() {
        return halfOrgMemberPrice;
    }

    public void setHalfOrgMemberPrice(BigDecimal halfOrgMemberPrice) {
        this.halfOrgMemberPrice = halfOrgMemberPrice;
    }

    public BigDecimal getHalfApprovingUserOriginalPrice() {
        return halfApprovingUserOriginalPrice;
    }

    public void setHalfApprovingUserOriginalPrice(BigDecimal halfApprovingUserOriginalPrice) {
        this.halfApprovingUserOriginalPrice = halfApprovingUserOriginalPrice;
    }

    public BigDecimal getHalfApprovingUserPrice() {
        return halfApprovingUserPrice;
    }

    public void setHalfApprovingUserPrice(BigDecimal halfApprovingUserPrice) {
        this.halfApprovingUserPrice = halfApprovingUserPrice;
    }
}
