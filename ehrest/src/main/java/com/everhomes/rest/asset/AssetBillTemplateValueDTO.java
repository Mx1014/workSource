package com.everhomes.rest.asset;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * <ul>
 *     <li>id: 模板字段id</li>
 *     <li>ownerId：账单所属物业公司id</li>
 *     <li>ownerType：账单所属物业公司类型</li>
 *     <li>targetId：账单所属园区id</li>
 *     <li>targetType：账单所属园区类型</li>
 *     <li>templateVersion: 版本号</li>
 *     <li>dtos: 账单字段列表 参考{@link com.everhomes.rest.asset.FieldValueDTO}</li>
 *     <li>periodAccountAmount: 总计应收</li>
 *     <li>unpaidPeriodAccountAmount: 待缴费用</li>
 * </ul>
 */
public class AssetBillTemplateValueDTO {

    private Long id;

    private Integer namespaceId;

    private Long ownerId;

    private String ownerType;

    private Long targetId;

    private String targetType;

    private Long tenantId;

    private String tenantType;

    private Long addressId;

    private Long templateVersion;

    private BigDecimal periodAccountAmount;

    private BigDecimal unpaidPeriodAccountAmount;

    @ItemType(FieldValueDTO.class)
    private List<FieldValueDTO> dtos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantType() {
        return tenantType;
    }

    public void setTenantType(String tenantType) {
        this.tenantType = tenantType;
    }

    public Long getTemplateVersion() {
        return templateVersion;
    }

    public void setTemplateVersion(Long templateVersion) {
        this.templateVersion = templateVersion;
    }

    public List<FieldValueDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<FieldValueDTO> dtos) {
        this.dtos = dtos;
    }

    public BigDecimal getPeriodAccountAmount() {
        return periodAccountAmount;
    }

    public void setPeriodAccountAmount(BigDecimal periodAccountAmount) {
        this.periodAccountAmount = periodAccountAmount;
    }

    public BigDecimal getUnpaidPeriodAccountAmount() {
        return unpaidPeriodAccountAmount;
    }

    public void setUnpaidPeriodAccountAmount(BigDecimal unpaidPeriodAccountAmount) {
        this.unpaidPeriodAccountAmount = unpaidPeriodAccountAmount;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
