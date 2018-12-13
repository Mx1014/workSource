package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>formOriginId: 表单 originId</li>
 *     <li>formVersion: 表单版本</li>
 * </ul>
 */
public class FlowFormRelationDataDirectRelation {

    @NotNull
    private Long formOriginId;
    @NotNull
    private Long formVersion;

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
