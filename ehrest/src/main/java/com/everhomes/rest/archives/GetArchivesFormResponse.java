package com.everhomes.rest.archives;

import com.everhomes.rest.general_approval.GeneralFormDTO;

/**
 * <ul>
 * <li>form: 表单 {@link com.everhomes.rest.archives.ArchivesFromsDTO}</li>
 * </ul>
 */
public class GetArchivesFormResponse {

    private ArchivesFormDTO form;

    public GetArchivesFormResponse() {
    }

    public ArchivesFormDTO getForm() {
        return form;
    }

    public void setForm(ArchivesFormDTO form) {
        this.form = form;
    }
}
