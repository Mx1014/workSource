package com.everhomes.rest.rentalv2.admin;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * <ul>
 * <li>rentalType: 0按小时，1按半天，2按天，3按半天带晚上，4按月，参考{@link com.everhomes.rest.rentalv2.RentalType}</li>
 * <li>workdayPrice: 园区客户价格</li>
 * <li>orgMemberWorkdayPrice: 集团内部价格</li>
 * <li>approvingUserWorkdayPrice: 外部客户价格</li>
 * <li>discountType: 折扣类型，0不打折，1满减，2满天减，3比例折扣，参考{@link com.everhomes.rest.rentalv2.admin.DiscountType}</li>
 * <li>fullPrice: 满</li>
 * <li>cutPrice: 减</li>
 * <li>discountRatio: 折扣比例</li>
 * </ul>
 */
public class PricePackageDTO {
    private Long id;
    private String name;
    private Byte rentalType;
    private BigDecimal price;
    private BigDecimal orgMemberPrice;
    private BigDecimal approvingUserPrice;
    private Byte discountType;
    private BigDecimal fullPrice;
    private BigDecimal cutPrice;
    private Double discountRatio;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
