// @formatter:off
package com.everhomes.entity;

import java.io.Serializable;

import com.everhomes.rest.entity.EntityProfileItemValueKind;
import com.everhomes.util.StringHelper;

public class EntityProfileItem implements Serializable {
    private static final long serialVersionUID = -4913875991618274544L;
    
    private java.lang.Long   id;
    private java.lang.Long   appId;
    private java.lang.Long   ownerId;
    private java.lang.String itemName;
    private java.lang.Byte   itemKind;
    private java.lang.String itemValue;
    private java.lang.String targetType;
    private java.lang.Long   targetId;
    private java.lang.Long   integralTag1;
    private java.lang.Long   integralTag2;
    private java.lang.Long   integralTag3;
    private java.lang.Long   integralTag4;
    private java.lang.Long   integralTag5;
    private java.lang.String stringTag1;
    private java.lang.String stringTag2;
    private java.lang.String stringTag3;
    private java.lang.String stringTag4;
    private java.lang.String stringTag5;
    
    private Class<?> ownerEntityPojoClass;
    private Class<?> itemPojoClass;

    public EntityProfileItem() {}

    public EntityProfileItem(
        java.lang.Long   id,
        java.lang.Long   appId,
        java.lang.Long   ownerId,
        java.lang.String itemName,
        java.lang.Byte   itemKind,
        java.lang.String itemValue,
        java.lang.String targetType,
        java.lang.Long   targetId,
        java.lang.Long   integralTag1,
        java.lang.Long   integralTag2,
        java.lang.Long   integralTag3,
        java.lang.Long   integralTag4,
        java.lang.Long   integralTag5,
        java.lang.String stringTag1,
        java.lang.String stringTag2,
        java.lang.String stringTag3,
        java.lang.String stringTag4,
        java.lang.String stringTag5
    ) {
        this.id = id;
        this.appId = appId;
        this.ownerId = ownerId;
        this.itemName = itemName;
        this.itemKind = itemKind;
        this.itemValue = itemValue;
        this.targetType = targetType;
        this.targetId = targetId;
        this.integralTag1 = integralTag1;
        this.integralTag2 = integralTag2;
        this.integralTag3 = integralTag3;
        this.integralTag4 = integralTag4;
        this.integralTag5 = integralTag5;
        this.stringTag1 = stringTag1;
        this.stringTag2 = stringTag2;
        this.stringTag3 = stringTag3;
        this.stringTag4 = stringTag4;
        this.stringTag5 = stringTag5;
    }
    
    public java.lang.Long getId() {
        return this.id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public java.lang.Long getAppId() {
        return this.appId;
    }

    public void setAppId(java.lang.Long appId) {
        this.appId = appId;
    }

    public java.lang.Long getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(java.lang.Long ownerId) {
        this.ownerId = ownerId;
    }

    public java.lang.String getItemName() {
        return this.itemName;
    }

    public void setItemName(java.lang.String itemName) {
        this.itemName = itemName;
    }

    public EntityProfileItemValueKind getItemKindEnum() {
        if(this.itemKind != null)
            return EntityProfileItemValueKind.fromCode(this.itemKind);
        return null;
    }
    
    public java.lang.Byte getItemKind() {
        return this.itemKind;
    }

    public void setItemKind(java.lang.Byte itemKind) {
        this.itemKind = itemKind;
    }
    
    public void setItemKindEnum(EntityProfileItemValueKind itemKind) {
        if(itemKind != null)
            this.itemKind = itemKind.getCode();
        else
            this.itemKind = null;
    }

    public java.lang.String getItemValue() {
        return this.itemValue;
    }

    public void setItemValue(java.lang.String itemValue) {
        this.itemValue = itemValue;
    }

    public java.lang.String getTargetType() {
        return this.targetType;
    }

    public void setTargetType(java.lang.String targetType) {
        this.targetType = targetType;
    }

    public java.lang.Long getTargetId() {
        return this.targetId;
    }

    public void setTargetId(java.lang.Long targetId) {
        this.targetId = targetId;
    }

    public java.lang.Long getIntegralTag1() {
        return this.integralTag1;
    }

    public void setIntegralTag1(java.lang.Long integralTag1) {
        this.integralTag1 = integralTag1;
    }

    public java.lang.Long getIntegralTag2() {
        return this.integralTag2;
    }

    public void setIntegralTag2(java.lang.Long integralTag2) {
        this.integralTag2 = integralTag2;
    }

    public java.lang.Long getIntegralTag3() {
        return this.integralTag3;
    }

    public void setIntegralTag3(java.lang.Long integralTag3) {
        this.integralTag3 = integralTag3;
    }

    public java.lang.Long getIntegralTag4() {
        return this.integralTag4;
    }

    public void setIntegralTag4(java.lang.Long integralTag4) {
        this.integralTag4 = integralTag4;
    }

    public java.lang.Long getIntegralTag5() {
        return this.integralTag5;
    }

    public void setIntegralTag5(java.lang.Long integralTag5) {
        this.integralTag5 = integralTag5;
    }

    public java.lang.String getStringTag1() {
        return this.stringTag1;
    }

    public void setStringTag1(java.lang.String stringTag1) {
        this.stringTag1 = stringTag1;
    }

    public java.lang.String getStringTag2() {
        return this.stringTag2;
    }

    public void setStringTag2(java.lang.String stringTag2) {
        this.stringTag2 = stringTag2;
    }

    public java.lang.String getStringTag3() {
        return this.stringTag3;
    }

    public void setStringTag3(java.lang.String stringTag3) {
        this.stringTag3 = stringTag3;
    }

    public java.lang.String getStringTag4() {
        return this.stringTag4;
    }

    public void setStringTag4(java.lang.String stringTag4) {
        this.stringTag4 = stringTag4;
    }

    public java.lang.String getStringTag5() {
        return this.stringTag5;
    }

    public void setStringTag5(java.lang.String stringTag5) {
        this.stringTag5 = stringTag5;
    }

    public Class<?> getOwnerEntityPojoClass() {
        return ownerEntityPojoClass;
    }

    public void setOwnerEntityPojoClass(Class<?> ownerEntityPojoClass) {
        this.ownerEntityPojoClass = ownerEntityPojoClass;
    }

    public Class<?> getItemPojoClass() {
        return itemPojoClass;
    }

    public void setItemPojoClass(Class<?> itemPojoClass) {
        this.itemPojoClass = itemPojoClass;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
