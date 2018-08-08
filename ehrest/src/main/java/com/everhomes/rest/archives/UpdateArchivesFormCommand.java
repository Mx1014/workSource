package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.general_approval.GeneralFormFieldDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>namespaceId: 域空间 id</li>
 * <li>organizationId: 公司 id</li>
 * <li>formFields: 表单字段 {@link com.everhomes.rest.general_approval.GeneralFormFieldDTO}</li>
 * <li>formGroups: 表单字段组 {@link com.everhomes.rest.archives.ArchivesFormGroupDTO}</li>
 * </ul>
 */
public class UpdateArchivesFormCommand {

    private Integer namespaceId;

    private Long organizationId;

    @ItemType(GeneralFormFieldDTO.class)
    private List<GeneralFormFieldDTO> formFields;

    @ItemType(ArchivesFormGroupDTO.class)
    private List<ArchivesFormGroupDTO> formGroups;

    public UpdateArchivesFormCommand() {
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public List<GeneralFormFieldDTO> getFormFields() {
        return formFields;
    }

    public void setFormFields(List<GeneralFormFieldDTO> formFields) {
        this.formFields = formFields;
    }

    public List<ArchivesFormGroupDTO> getFormGroups() {
        return formGroups;
    }

    public void setFormGroups(List<ArchivesFormGroupDTO> formGroups) {
        this.formGroups = formGroups;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
