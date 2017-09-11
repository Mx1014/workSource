package com.everhomes.rest.archives;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>formOriginId: 表单id(调用模板或没有表单时为0)</li>
 * </ul>
 */
public class GetArchivesFormCommand {

    private Long formOriginId;

    public GetArchivesFormCommand() {
    }

    public GetArchivesFormCommand(Long formOriginId) {
        this.formOriginId = formOriginId;
    }


    public Long getFormOriginId() {
        return formOriginId;
    }

    public void setFormOriginId(Long formOriginId) {
        this.formOriginId = formOriginId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
