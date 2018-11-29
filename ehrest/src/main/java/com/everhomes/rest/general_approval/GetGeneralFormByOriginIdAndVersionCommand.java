package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>formOriginId: 表单原始ID</li>
 *     <li>formVersion: 表单版本</li>
 * </ul>
 * @author huqi
 */
public class GetGeneralFormByOriginIdAndVersionCommand {
    private Long formOriginId;
    private Long formVersion;
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
}
