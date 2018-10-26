// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul> 添加访客授权。
 * <li>ownerId: 所属者id</li>
 * <li>ownerType: 所属者type：0园区 1企业</li>
 * <li>namespaceId: 域空间ID</li>
 * <li>path: 路径</li>
 * <li>name: 字段名</li>
 * <li>type: 字段类型：1文本 2单选</li>
 * <li>status: 状态：0失效 1必填 2非必填</li>
 * <li>itemName: 子项名称</li>
 * </ul>
 *
 */
public class CreateTempAuthCustomFieldCommand {

    private Long ownerId;
    private Byte ownerType;
    private Integer namespaceId;
    private String path;
    private String name;
//    private String value;
    private Byte type;
    private Byte status;
    private List<String> itemName;

    public List<String> getItemName() {
        return itemName;
    }

    public void setItemName(List<String> itemName) {
        this.itemName = itemName;
    }
    //    private List<CreateTempAuthCustomFieldCommand> list;
//
//    public List<CreateTempAuthCustomFieldCommand> getList() {
//        return list;
//    }
//
//    public void setList(List<CreateTempAuthCustomFieldCommand> list) {
//        this.list = list;
//    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Byte getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Byte ownerType) {
        this.ownerType = ownerType;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

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

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

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
