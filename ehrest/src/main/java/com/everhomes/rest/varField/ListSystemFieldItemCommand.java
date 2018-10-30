package com.everhomes.rest.varField;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>fieldId: 在系统里的字段id</li>
 * </ul>
 * Created by ying.xiong on 2017/9/22.
 */
public class ListSystemFieldItemCommand {

    private Long fieldId;

    private Long ownerId;

    private String ownerType;

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

    public Long getFieldId() {
        return fieldId;
    }

    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
