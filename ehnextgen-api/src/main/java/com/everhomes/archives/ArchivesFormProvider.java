package com.everhomes.archives;

public interface ArchivesFormProvider {

    Long createArchivesForm(ArchivesForm form);

    void updateArchivesForm(ArchivesForm form);

    ArchivesForm findArchivesFormByOrgId(Integer namespaceId, Long orgId);

    ArchivesForm findArchivesDefaultForm(String ownerType);

    ArchivesFormGroup createArchivesFormGroup(ArchivesFormGroup group);

    ArchivesFormGroup findArchivesFormGroupByFormId(Integer namespaceId, Long formId);

    void updateArchivesFormGroup(ArchivesFormGroup group);

    void createArchivesFormVal(ArchivesFormVal val);

    void updateArchivesFormVal(ArchivesFormVal val);

    ArchivesFormVal findArchivesFormValBySourceIdAndName(Long formId, Long sourceId, String fieldName);

}
