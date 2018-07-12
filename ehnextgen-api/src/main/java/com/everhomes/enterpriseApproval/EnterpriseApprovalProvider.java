package com.everhomes.enterpriseApproval;

import com.everhomes.general_approval.GeneralApproval;

import java.util.List;

public interface EnterpriseApprovalProvider {

    EnterpriseApprovalGroup findEnterpriseApprovalGroup(Long id);

    List<EnterpriseApprovalGroup> listEnterpriseApprovalGroups();

    GeneralApproval getGeneralApprovalByTemplateId(Integer namespaceId, Long moduleId, Long ownerId, String ownerType, Long templateId);

    Integer countGeneralApprovalInTemplateIds(Integer namespaceId, Long moduleId, Long ownerId, String ownerType, List<Long> templateIds);

    List<EnterpriseApprovalTemplate> listEnterpriseApprovalTemplateByModuleId(Long moduleId, boolean defaultFlag);

    List<Long> listGeneralApprovalIdsByGroupId(Integer namespaceId, Long moduleId, Long ownerId, String ownerType, Long groupId);

    GeneralApproval findEnterpriseApprovalByName(Integer namespaceId, Long moduleId, Long ownerId, String ownerType, String name, Long groupId);
}
