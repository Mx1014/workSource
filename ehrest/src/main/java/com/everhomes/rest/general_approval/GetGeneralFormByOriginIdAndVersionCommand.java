package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>formOriginId: 表单原始ID</li>
 *     <li>formVersion: 表单版本</li>
 *     <li>sourceId: 资源ID</li>
 *     <li>sourceType: 资源类型</li>
 * </ul>
 * @author huqi
 */
public class GetGeneralFormByOriginIdAndVersionCommand {
    private Long formOriginId;
    private Long formVersion;
    private Long sourceId;
    private String sourceType;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
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
