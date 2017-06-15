package com.everhomes.rest.general_approval;

/**
 * <ul> 获取表单值
 * <li>sourceType: 业务类型</li>
 * <li>sourceId: 业务ID</li>
 * <li>originFieldNameFlag: 字段原始名字是否返回</li>
 * </ul>
 */
public class GetGeneralFormValuesCommand {
    private String sourceType;
    private Long sourceId;

    private Byte originFieldNameFlag;

    public Byte getOriginFieldNameFlag() {
        return originFieldNameFlag;
    }

    public void setOriginFieldNameFlag(Byte originFieldNameFlag) {
        this.originFieldNameFlag = originFieldNameFlag;
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
}
