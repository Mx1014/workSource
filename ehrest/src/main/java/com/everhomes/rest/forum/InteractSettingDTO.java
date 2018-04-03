// @formatter:off
package com.everhomes.rest.forum;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: ID</li>
 *     <li>namespaceId: 域空间ID</li>
 *     <li>moduleType: 模块类型  参考 {@link ForumModuleType}</li>
 *     <li>categoryId: categoryId</li>
 *     <li>interactFlag: interactFlag</li>
 *     <li>createTime: 创建时间</li>
 * </ul>
 */
public class InteractSettingDTO {
    private Long id;

    private Integer namespaceId;

    private Byte moduleType;

    private Long categoryId;

    private Byte interactFlag;

    private Timestamp createTime;

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

    public Byte getModuleType() {
        return moduleType;
    }

    public void setModuleType(Byte moduleType) {
        this.moduleType = moduleType;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Byte getInteractFlag() {
        return interactFlag;
    }

    public void setInteractFlag(Byte interactFlag) {
        this.interactFlag = interactFlag;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
