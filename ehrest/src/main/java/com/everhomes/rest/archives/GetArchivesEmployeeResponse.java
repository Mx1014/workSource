package com.everhomes.rest.archives;

import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>form: 档案详情表单(包含字段名、组名及值) 参考{@link com.everhomes.rest.general_approval.GeneralFormDTO}</li>
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
