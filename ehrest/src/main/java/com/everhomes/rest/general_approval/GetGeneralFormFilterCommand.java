package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;



/**
 * <ul>
 *     <li>namespaceId : namespaceId</li>
 *     <li>ownerId : 当前项目的id，communityId</li>
 *     <li>moduleId : 当前应用id</li>
 *     <li>formOriginId : 当前生效的表单Id</li>
 *     <li>formVersion : 当前生效的表单version</li>
 * </ul>
 */
public class GetGeneralFormFilterCommand {
    private Integer namespaceId;
    private Long ownerId;
    private Long moduleId;
    private Long formOriginId;
    private Long formVersion;

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

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Long getFormOriginId() {
        return formOriginId;
    }

    public void setFormOriginId(Long formOriginId) {
        this.formOriginId = formOriginId;
    }

    public Long getFormVersion() {
        return formVersion;
    }

    public void setFormVersion(Long formVersion) {
        this.formVersion = formVersion;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
