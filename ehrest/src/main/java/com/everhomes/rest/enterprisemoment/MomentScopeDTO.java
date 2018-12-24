package com.everhomes.rest.enterprisemoment;

import com.everhomes.util.StringHelper;

/**
 *
 * <ul>
 * <li>sourceType: 可见范围类型{@link com.everhomes.rest.enterprisemoment.ScopeType}</li>
 * <li>sourceId: 可见id </li>
 * </ul>
 */
public class MomentScopeDTO {
    private String sourceType;
    private Long sourceId;
    private String sourceName;

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

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
