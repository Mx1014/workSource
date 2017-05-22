package com.everhomes.rest.general_form;

/**
 * <ul> 获取表单值
 * <li>sourceType: 业务类型</li>
 * <li>sourceId: 业务ID</li>
 * </ul>
 */
public class GetGeneralFormValuesCommand {
    private String sourceType;
    private Long sourceId;

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
}
