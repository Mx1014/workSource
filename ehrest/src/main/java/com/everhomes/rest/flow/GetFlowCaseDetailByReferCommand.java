package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>moduleId: 模块id</li>
 *     <li>referId: referId</li>
 *     <li>referType: referType</li>
 *     <li>flowUserType: 以什么用户类型查看,默认是不区分用户类型 {@link com.everhomes.rest.flow.FlowUserType}</li>
 *     <li>needFlowButton: 是否需要显示按钮,默认是不需要按钮 {@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 * </ul>
 */
public class GetFlowCaseDetailByReferCommand {

    private Long moduleId;
    private Long referId;
    private String referType;
    private String flowUserType;
    private Byte needFlowButton;

    public Long getReferId() {
        return referId;
    }

    public void setReferId(Long referId) {
        this.referId = referId;
    }

    public String getReferType() {
        return referType;
    }

    public void setReferType(String referType) {
        this.referType = referType;
    }

    public Byte getNeedFlowButton() {
        return needFlowButton;
    }

    public void setNeedFlowButton(Byte needFlowButton) {
        this.needFlowButton = needFlowButton;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getFlowUserType() {
        return flowUserType;
    }

    public void setFlowUserType(String flowUserType) {
        this.flowUserType = flowUserType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
