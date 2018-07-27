package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul> 获取表单信息
 * <li>namespaceId：域空间id</li>
 * <li>ownerType：ownerType</li>
 * <li>ownerId：ownerId</li>
 * <li>sourceId: 资源ID</li>
 * <li>sourceType：资源类型</li>
 * <li>moduleId: 模块ID</li>
 * <li>moduleType：模块名称</li>
 * </ul>
 * @author janson
 *
 */
public class GetGeneralFormValCommand {

    private Integer namespaceId;
    private Long ownerId;

    private String ownerType;

    private String sourceType;
    private Long sourceId;
    private String moduleType;
    private Long moduleId;

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

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
