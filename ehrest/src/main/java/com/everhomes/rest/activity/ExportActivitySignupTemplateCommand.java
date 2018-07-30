// @formatter:off
package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间id</li>
 * <li>sourceType: 源类型</li>
 * <li>sourceId: 源Id</li>
 * </ul>
 */
public class ExportActivitySignupTemplateCommand {
    private Integer namespaceId;
    private String sourceType;
    private Long sourceId;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
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
