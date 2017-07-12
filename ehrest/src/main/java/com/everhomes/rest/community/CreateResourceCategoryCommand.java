// @formatter:off
package com.everhomes.rest.community;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * <ul>
 * <li>ownerType: 所属实体类型 公司机构：</li>
 * <li>ownerId: 所属实体id</li>
 * <li>name: 类型名称</li>
 * <li>parentId: 父级id</li>
 * </ul>
 */
public class CreateResourceCategoryCommand {

    private String ownerType;

    private Long ownerId;

    private String name;

    private Long parentId;

    private Byte type;

    public CreateResourceCategoryCommand() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
