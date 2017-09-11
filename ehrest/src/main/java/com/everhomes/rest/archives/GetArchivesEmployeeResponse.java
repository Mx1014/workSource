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

/*    @ItemType(PostApprovalFormItem.class)
    List<PostApprovalFormItem> form;*/

    public GetArchivesEmployeeResponse() {
    }

/*
    public List<PostApprovalFormItem> getForm() {
        return form;
    }

    public void setForm(List<PostApprovalFormItem> form) {
        this.form = form;
    }
*/

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
