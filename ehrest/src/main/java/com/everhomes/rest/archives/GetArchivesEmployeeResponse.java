package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.general_approval.PostApprovalFormItem;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>form: form表单 {@link com.everhomes.rest.general_approval.PostApprovalFormItem}</li>
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
