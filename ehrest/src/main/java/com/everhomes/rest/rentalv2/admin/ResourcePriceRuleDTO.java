package com.everhomes.rest.rentalv2.admin;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.rentalv2.RentalPriceClassificationDTO;
import com.everhomes.rest.rentalv2.RentalPriceClassificationTitleDTO;
import com.everhomes.rest.rentalv2.RentalV2ResourceType;
import com.everhomes.rest.rentalv2.RuleSourceType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>ownerType: ownerType {@link com.everhomes.rest.rentalv2.RentalOwnerType}</li>
 * <li>ownerId: 园区id</li>
 * <li>resourceTypeId: 图标id</li>
 * <li>resourceType: resourceType {@link RentalV2ResourceType}</li>
 * <li>sourceType: sourceType 默认规则：default_rule， 资源规则：resource_rule{@link RuleSourceType}</li>
 * <li>sourceId: 资源id，如果是默认规则，则不填</li>
 * <li>needPay: 是否需要支付 1：需要 0：不需要 {@link com.everhomes.rest.rentalv2.NormalFlag}</li>
 * <li>rentalTypes: 时间单元类型列表 {@link com.everhomes.rest.rentalv2.RentalType}</li>
 * <li>priceRules: 价格策略列表 {@link com.everhomes.rest.rentalv2.admin.PriceRuleDTO}</li>
 * <li>pricePackages: 套餐价格表{@link com.everhomes.rest.rentalv2.admin.PricePackageDTO}</li>
 * <li>vipLevels: 会员等级</li>
 * </ul>
 */
public class ResourcePriceRuleDTO {
    private String ownerType;

    private Long ownerId;

    private Long resourceTypeId;

    private String resourceType;

    private String sourceType;

    private Long sourceId;

    private Byte needPay;

    @ItemType(Byte.class)
    private List<Byte> rentalTypes;
    @ItemType(PriceRuleDTO.class)
    private List<PriceRuleDTO> priceRules;
    @ItemType(PricePackageDTO.class)
    private List<PricePackageDTO> pricePackages;
    private List<RentalPriceClassificationTitleDTO> classification;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getResourceTypeId() {
        return resourceTypeId;
    }

    public void setResourceTypeId(Long resourceTypeId) {
        this.resourceTypeId = resourceTypeId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public Byte getNeedPay() {
        return needPay;
    }

    public void setNeedPay(Byte needPay) {
        this.needPay = needPay;
    }

    public List<Byte> getRentalTypes() {
        return rentalTypes;
    }

    public void setRentalTypes(List<Byte> rentalTypes) {
        this.rentalTypes = rentalTypes;
    }

    public List<PriceRuleDTO> getPriceRules() {
        return priceRules;
    }

    public void setPriceRules(List<PriceRuleDTO> priceRules) {
        this.priceRules = priceRules;
    }

    public List<PricePackageDTO> getPricePackages() {
        return pricePackages;
    }

    public void setPricePackages(List<PricePackageDTO> pricePackages) {
        this.pricePackages = pricePackages;
    }

    public List<RentalPriceClassificationTitleDTO> getClassification() {
        return classification;
    }

    public void setClassification(List<RentalPriceClassificationTitleDTO> classification) {
        this.classification = classification;
    }
}
