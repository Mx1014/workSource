package com.everhomes.rest.archives;

import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>form: 包含个人信息的表单 {@link com.everhomes.rest.general_approval.GeneralFormDTO}</li>
 * <li>others: (具体实现可能会加字段)</li>
 * </ul>
 */
public class GetArchivesEmployeeResponse {

    private GeneralFormDTO form;

    public GetArchivesEmployeeResponse() {
    }

    public GeneralFormDTO getForm() {
        return form;
    }

    public void setForm(GeneralFormDTO form) {
        this.form = form;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
