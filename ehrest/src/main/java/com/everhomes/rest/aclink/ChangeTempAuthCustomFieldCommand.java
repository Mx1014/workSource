// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul> 修改临时授权自定义字段。
 * <li>id: 字段id</li>
 * <li>name: 字段名</li>
 * <li>status: 状态：0删除 1生效必填 2生效非必填 3失效必填 4 失效非必填</li>
 * </ul>
 *
 */
public class ChangeTempAuthCustomFieldCommand {
    private Long id;
//    private Long ownerId;
//    private Byte ownerType;
//    private Integer namespaceId;
//    private String path;
    private String name;
//    private Byte type;
    private Byte status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public Long getOwnerId() {
//        return ownerId;
//    }
//
//    public void setOwnerId(Long ownerId) {
//        this.ownerId = ownerId;
//    }
//
//    public Byte getOwnerType() {
//        return ownerType;
//    }
//
//    public void setOwnerType(Byte ownerType) {
//        this.ownerType = ownerType;
//    }
//
//    public Integer getNamespaceId() {
//        return namespaceId;
//    }
//
//    public void setNamespaceId(Integer namespaceId) {
//        this.namespaceId = namespaceId;
//    }
//
//    public String getPath() {
//        return path;
//    }
//
//    public void setPath(String path) {
//        this.path = path;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public String getValue() {
//        return value;
//    }
//
//    public void setValue(String value) {
//        this.value = value;
//    }

//    public Byte getType() {
//        return type;
//    }
//
//    public void setType(Byte type) {
//        this.type = type;
//    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
