package com.everhomes.rest.archives;

import com.everhomes.rest.general_approval.GeneralFormDTO;

/**
 * <ul>
 * <li>form: 表单对象 {@link com.everhomes.rest.general_approval.GeneralFormDTO}</li>
 * </ul>
 */
public class GetArchivesFormResponse {

    private GeneralFormDTO form;

    public GetArchivesFormResponse() {
    }

    public GeneralFormDTO getForm() {
        return form;
    }

    public void setForm(GeneralFormDTO form) {
        this.form = form;
    }
}
