package com.everhomes.rest.rentalv2.admin;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * <ul>
 * <li>rentalType: 0按小时，1按半天，2按天，3按半天带晚上，4按月，参考{@link com.everhomes.rest.rentalv2.RentalType}</li>
 * <li>price: 园区客户价格</li>
 * <li>originalPrice: 原价-如果打折则有（园区客户）</li>
 * <li>orgMemberPrice: 集团内部价格</li>
 * <li>orgMemberOriginalPrice: 原价-如果打折则有(企业内部价)</li>
 * <li>approvingUserPrice: 外部客户价格</li>
 * <li>approvingUserOriginalPrice: 原价-如果打折则有（外部客户价）</li>
 * <li>discountType: 折扣类型，0不打折，1满减，2满天减，3比例折扣，参考{@link com.everhomes.rest.rentalv2.admin.DiscountType}</li>
 * <li>fullPrice: 满</li>
 * <li>cutPrice: 减</li>
 * <li>discountRatio: 折扣比例</li>
 * <li>orgMemberDiscountType: 集团内部折扣类型，0不打折，1满减，2满天减，3比例折扣，参考{@link com.everhomes.rest.rentalv2.admin.DiscountType}</li>
 * <li>orgMemberFullPrice: 集团内部满</li>
 * <li>orgMemberCutPrice: 集团内部减</li>
 * <li>orgMemberDiscountRatio: 集团内部折扣比例</li>
 * <li>approvingUserDiscountType: 外部客户折扣类型，0不打折，1满减，2满天减，3比例折扣，参考{@link com.everhomes.rest.rentalv2.admin.DiscountType}</li>
 * <li>approvingUserFullPrice: 外部客户满</li>
 * <li>approvingUserCutPrice: 外部客户减</li>
 * <li>approvingUserDiscountRatio: 外部客户折扣比例</li>
 * </ul>
 */
public class PricePackageDTO {
    private Long id;
    private String name;
    private Byte rentalType;
    private BigDecimal price;
    private java.math.BigDecimal originalPrice;
    private BigDecimal orgMemberPrice;
    private BigDecimal orgMemberOriginalPrice;
    private BigDecimal approvingUserPrice;
    private BigDecimal approvingUserOriginalPrice;
    private Byte discountType;
    private BigDecimal fullPrice;
    private BigDecimal cutPrice;
    private Double discountRatio;
    private Byte  orgMemberDiscountType;
    private BigDecimal  orgMemberFullPrice;
    private BigDecimal  orgMemberCutPrice;
    private Double  orgMemberDiscountRatio;
    private Byte approvingUserDiscountType;
    private BigDecimal approvingUserFullPrice;
    private BigDecimal approvingUserCutPrice;
    private Double approvingUserDiscountRatio;
    private Long cellBeginId;
    private Long cellEndId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCellBeginId() {
        return cellBeginId;
    }

    public void setCellBeginId(Long cellBeginId) {
        this.cellBeginId = cellBeginId;
    }

    public Long getCellEndId() {
        return cellEndId;
    }

    public void setCellEndId(Long cellEndId) {
        this.cellEndId = cellEndId;
    }

    public Byte getRentalType() {
        return rentalType;
    }

    public void setRentalType(Byte rentalType) {
        this.rentalType = rentalType;
    }

    public Byte getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Byte discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getFullPrice() {
        return fullPrice;
    }

    public void setFullPrice(BigDecimal fullPrice) {
        this.fullPrice = fullPrice;
    }

    public BigDecimal getCutPrice() {
        return cutPrice;
    }

    public void setCutPrice(BigDecimal cutPrice) {
        this.cutPrice = cutPrice;
    }

    public Double getDiscountRatio() {
        return discountRatio;
    }

    public void setDiscountRatio(Double discountRatio) {
        this.discountRatio = discountRatio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getOrgMemberPrice() {
        return orgMemberPrice;
    }

    public void setOrgMemberPrice(BigDecimal orgMemberPrice) {
        this.orgMemberPrice = orgMemberPrice;
    }

    public BigDecimal getApprovingUserPrice() {
        return approvingUserPrice;
    }

    public void setApprovingUserPrice(BigDecimal approvingUserPrice) {
        this.approvingUserPrice = approvingUserPrice;
    }

    public Byte getOrgMemberDiscountType() {
        return orgMemberDiscountType;
    }

    public void setOrgMemberDiscountType(Byte orgMemberDiscountType) {
        this.orgMemberDiscountType = orgMemberDiscountType;
    }

    public BigDecimal getOrgMemberFullPrice() {
        return orgMemberFullPrice;
    }

    public void setOrgMemberFullPrice(BigDecimal orgMemberFullPrice) {
        this.orgMemberFullPrice = orgMemberFullPrice;
    }

    public BigDecimal getOrgMemberCutPrice() {
        return orgMemberCutPrice;
    }

    public void setOrgMemberCutPrice(BigDecimal orgMemberCutPrice) {
        this.orgMemberCutPrice = orgMemberCutPrice;
    }

    public Double getOrgMemberDiscountRatio() {
        return orgMemberDiscountRatio;
    }

    public void setOrgMemberDiscountRatio(Double orgMemberDiscountRatio) {
        this.orgMemberDiscountRatio = orgMemberDiscountRatio;
    }

    public Byte getApprovingUserDiscountType() {
        return approvingUserDiscountType;
    }

    public void setApprovingUserDiscountType(Byte approvingUserDiscountType) {
        this.approvingUserDiscountType = approvingUserDiscountType;
    }

    public BigDecimal getApprovingUserFullPrice() {
        return approvingUserFullPrice;
    }

    public void setApprovingUserFullPrice(BigDecimal approvingUserFullPrice) {
        this.approvingUserFullPrice = approvingUserFullPrice;
    }

    public BigDecimal getApprovingUserCutPrice() {
        return approvingUserCutPrice;
    }

    public void setApprovingUserCutPrice(BigDecimal approvingUserCutPrice) {
        this.approvingUserCutPrice = approvingUserCutPrice;
    }

    public Double getApprovingUserDiscountRatio() {
        return approvingUserDiscountRatio;
    }

    public void setApprovingUserDiscountRatio(Double approvingUserDiscountRatio) {
        this.approvingUserDiscountRatio = approvingUserDiscountRatio;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getOrgMemberOriginalPrice() {
        return orgMemberOriginalPrice;
    }

    public void setOrgMemberOriginalPrice(BigDecimal orgMemberOriginalPrice) {
        this.orgMemberOriginalPrice = orgMemberOriginalPrice;
    }

    public BigDecimal getApprovingUserOriginalPrice() {
        return approvingUserOriginalPrice;
    }

    public void setApprovingUserOriginalPrice(BigDecimal approvingUserOriginalPrice) {
        this.approvingUserOriginalPrice = approvingUserOriginalPrice;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
