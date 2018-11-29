package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>flowUserType: 用户角色 {@link com.everhomes.rest.flow.FlowUserType}</li>
 *     <li>moduleId: 模块id</li>
 *     <li>organizationId: 公司 id</li>
 * </ul>
 */
public class ListFlowServiceTypesCommand {

    private String flowUserType;
    private Long moduleId;
    private Long organizationId;

    public String getFlowUserType() {
        return flowUserType;
    }

    public void setFlowUserType(String flowUserType) {
        this.flowUserType = flowUserType;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
