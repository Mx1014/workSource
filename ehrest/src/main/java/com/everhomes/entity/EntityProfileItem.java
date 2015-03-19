
package com.everhomes.entity;

import java.io.Serializable;

public class EntityProfileItem implements Serializable {
    private static final long serialVersionUID = -1391796484200435512L;
    
    private java.lang.String itemName;
    private java.lang.Byte   itemKind;
    private java.lang.String itemValue;
    private java.lang.String targetType;
    private java.lang.Long   targetId;

    public EntityProfileItem() {
    }

    public java.lang.String getItemName() {
        return itemName;
    }

    public void setItemName(java.lang.String itemName) {
        this.itemName = itemName;
    }

    public java.lang.Byte getItemKind() {
        return itemKind;
    }

    public void setItemKind(java.lang.Byte itemKind) {
        this.itemKind = itemKind;
    }

    public java.lang.String getItemValue() {
        return itemValue;
    }

    public void setItemValue(java.lang.String itemValue) {
        this.itemValue = itemValue;
    }

    public java.lang.String getTargetType() {
        return targetType;
    }

    public void setTargetType(java.lang.String targetType) {
        this.targetType = targetType;
    }

    public java.lang.Long getTargetId() {
        return targetId;
    }

    public void setTargetId(java.lang.Long targetId) {
        this.targetId = targetId;
    }
}

