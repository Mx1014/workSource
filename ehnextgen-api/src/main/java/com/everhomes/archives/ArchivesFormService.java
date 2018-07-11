package com.everhomes.archives;

import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.rest.archives.ArchivesFormDTO;
import com.everhomes.rest.archives.GetArchivesFormCommand;
import com.everhomes.rest.archives.GetArchivesFormResponse;
import com.everhomes.rest.archives.UpdateArchivesFormCommand;
import com.everhomes.rest.general_approval.PostApprovalFormItem;

import java.util.List;

public interface ArchivesFormService {

    ArchivesFormDTO getArchivesFormByOrgId(Integer namespaceId, Long orgId);

    ArchivesFormDTO getArchivesDefaultForm();

    void updateArchivesForm(UpdateArchivesFormCommand cmd);

    GetArchivesFormResponse getArchivesForm(GetArchivesFormCommand cmd);

    void addArchivesDynamicValues(OrganizationMemberDetails employee, List<PostApprovalFormItem> dynamicItems);
}
