package com.everhomes.rest.general_approval;

/**
 * <ul>
 * <li>approvalId: 审批id</li>
 * <li>sourceType: 选择对象的类型 例：ORGANIZATION, MEMBERDETAIL 参考{@link com.everhomes.rest.uniongroup.UniongroupTargetType}</li>
 * <li>sourceId: 选择对象的id</li>
 * <li>sourceDescription: 选择对象的名称</li>
 * </ul>
 */
public class GeneralApprovalScopeMapDTO {

    private Long id;

    private Long approvalId;

    private String sourceType;

    private Long sourceId;

    private String sourceDescription;

    public GeneralApprovalScopeMapDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(Long approvalId) {
        this.approvalId = approvalId;
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

    public String getSourceDescription() {
        return sourceDescription;
    }

    public void setSourceDescription(String sourceDescription) {
        this.sourceDescription = sourceDescription;
    }
}
