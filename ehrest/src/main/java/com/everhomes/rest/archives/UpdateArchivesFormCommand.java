package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.rest.general_approval.GeneralFormGroupDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>organizationId: 公司 id</li>
 * <li>formOriginId: 表单 id(根据独立的结果获取)</li>
 * <li>formFields: 字段 {@link com.everhomes.rest.general_approval.GeneralFormFieldDTO}</li>
 * <li>formGroups: 字段组 {@link com.everhomes.rest.general_approval.GeneralFormGroupDTO}</li>
 * </ul>
 */
public class UpdateArchivesFormCommand {

    private Long formOriginId;

    private Long organizationId;

    @ItemType(GeneralFormFieldDTO.class)
    List<GeneralFormFieldDTO> formFields;

    @ItemType(GeneralFormGroupDTO.class)
    List<GeneralFormGroupDTO> formGroups;

    public UpdateArchivesFormCommand() {
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getFormOriginId() {
        return formOriginId;
    }

    public void setFormOriginId(Long formOriginId) {
        this.formOriginId = formOriginId;
    }

    public List<GeneralFormFieldDTO> getFormFields() {
        return formFields;
    }

    public void setFormFields(List<GeneralFormFieldDTO> formFields) {
        this.formFields = formFields;
    }

    public List<GeneralFormGroupDTO> getFormGroups() {
        return formGroups;
    }

    public void setFormGroups(List<GeneralFormGroupDTO> formGroups) {
        this.formGroups = formGroups;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
