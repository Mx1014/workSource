package com.everhomes.rest.archives;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>formOriginId: 表单id</li>
 * <li>formVersion: 表单版本</li>
 * </ul>
 */
public class ArchivesFromsDTO {

    private Long formOriginId;

    private Long formVersion;

    public ArchivesFromsDTO() {
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
