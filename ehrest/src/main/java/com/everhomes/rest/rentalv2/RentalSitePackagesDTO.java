package com.everhomes.rest.rentalv2;

import java.math.BigDecimal;
import java.util.List;

/**
 * <ul>
 * <li>id：id</li>
 * <li>name：套餐名</li>
 * <li>rentalSiteId：场所id</li>
 * <li>rentalType： time(0),halfday(1){@link com.everhomes.rest.rentalv2.RentalType} </li>
 * <li>priceType: 0 按时长定价 1 起步价模式</li>
 * <li>price：场所价格</li>
 * <li>initiatePrice: 起步后价格</li>
 * <li>	originalPrice：     	原价（如果不为null则price为打折价）	</li>
 * <li>halfsitePrice：半场价格</li>
 * <li>	halfsiteOriginalPrice：     	半场原价（如果不为null则price为打折价）	</li>
 * <li>orgMemberOriginalPrice: 原价-如果打折则有(企业内部价)</li>
 * <li>orgMemberPrice: 实际价格-打折则为折后价(企业内部价)</li>
 * <li>orgMemberInitiatePrice: 集团内部起步后价格</li>
 * <li>approvingUserOriginalPrice: 原价-如果打折则有（外部客户价）</li>
 * <li>approvingUserPrice: 实际价格-打折则为折后价（外部客户价）</li>
 * <li>approvingUserInitiatePrice: 外部客户起步后价格</li>
 * </ul>
 */

public class RentalSitePackagesDTO {
    private Long id;
    private String name;
    private Long rentalSiteId;
    private Byte rentalType;
    private Byte priceType;
    private java.math.BigDecimal price;
    private BigDecimal initiatePrice;
    private java.math.BigDecimal originalPrice;
    private java.math.BigDecimal halfsitePrice;
    private java.math.BigDecimal halfsiteOriginalPrice;
    private BigDecimal orgMemberOriginalPrice;
    private BigDecimal orgMemberPrice;
    private BigDecimal orgMemberInitiatePrice;

    private BigDecimal approvingUserOriginalPrice;
    private BigDecimal approvingUserPrice;
    private BigDecimal approvingUserInitiatePrice;
    private BigDecimal halfOrgMemberOriginalPrice;
    private BigDecimal halfOrgMemberPrice;
    private BigDecimal halfApprovingUserOriginalPrice;
    private BigDecimal halfApprovingUserPrice;
    //新版本的价格
    private List<RentalPriceClassificationDTO> priceRules;
    private Byte userPriceType;

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

    public Byte getPriceType() {
        return priceType;
    }

    public void setPriceType(Byte priceType) {
        this.priceType = priceType;
    }

    public BigDecimal getInitiatePrice() {
        return initiatePrice;
    }

    public void setInitiatePrice(BigDecimal initiatePrice) {
        this.initiatePrice = initiatePrice;
    }

    public BigDecimal getOrgMemberInitiatePrice() {
        return orgMemberInitiatePrice;
    }

    public void setOrgMemberInitiatePrice(BigDecimal orgMemberInitiatePrice) {
        this.orgMemberInitiatePrice = orgMemberInitiatePrice;
    }

    public BigDecimal getApprovingUserInitiatePrice() {
        return approvingUserInitiatePrice;
    }

    public void setApprovingUserInitiatePrice(BigDecimal approvingUserInitiatePrice) {
        this.approvingUserInitiatePrice = approvingUserInitiatePrice;
    }

    public Byte getUserPriceType() {
        return userPriceType;
    }

    public void setUserPriceType(Byte userPriceType) {
        this.userPriceType = userPriceType;
    }

    public List<RentalPriceClassificationDTO> getPriceRules() {
        return priceRules;
    }

    public void setPriceRules(List<RentalPriceClassificationDTO> priceRules) {
        this.priceRules = priceRules;
    }
}
