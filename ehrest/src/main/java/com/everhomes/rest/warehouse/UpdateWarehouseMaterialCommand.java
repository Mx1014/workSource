package com.everhomes.rest.warehouse;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * <ul>
 *     <li>id: 物品id</li>
 *     <li>ownerType: 物品所属类型 eg：EhOrganizations</li>
 *     <li>ownerId: 物品所属类型id</li>
 *     <li>name: 物品名</li>
 *     <li>materialNumber: 物品编码</li>
 *     <li>categoryId: 物品分类</li>
 *     <li>brand: 品牌</li>
 *     <li>itemNo: 型号</li>
 *     <li>referencePrice: 参考单价</li>
 *     <li>unitId: 单位id</li>
 *     <li>specificationInformation: 规格信息</li>
 * </ul>
 * Created by ying.xiong on 2017/5/11.
 */
public class UpdateWarehouseMaterialCommand {
    private Long id;

    private String ownerType;

    private Long ownerId;

    private Long categoryId;

    private String name;

    private String materialNumber;

    private String brand;

    private String itemNo;

    private BigDecimal referencePrice;

    private Long unitId;

    private String specificationInformation;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getMaterialNumber() {
        return materialNumber;
    }

    public void setMaterialNumber(String materialNumber) {
        this.materialNumber = materialNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public BigDecimal getReferencePrice() {
        return referencePrice;
    }

    public void setReferencePrice(BigDecimal referencePrice) {
        this.referencePrice = referencePrice;
    }

    public String getSpecificationInformation() {
        return specificationInformation;
    }

    public void setSpecificationInformation(String specificationInformation) {
        this.specificationInformation = specificationInformation;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
