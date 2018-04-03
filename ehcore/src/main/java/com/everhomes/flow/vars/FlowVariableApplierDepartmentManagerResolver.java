// @formatter:off
package com.everhomes.flow.vars;

import com.everhomes.flow.*;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.flow.FlowEntityType;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationManagerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component(FlowVariableUserResolver.APPLIER_DEPARTMENT_MANAGER)
public class FlowVariableApplierDepartmentManagerResolver implements FlowVariableUserResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowVariableApplierDepartmentManagerResolver.class);

    // @Autowired
    // private FlowUserSelectionProvider flowUserSelectionProvider;

    // @Autowired
    // private FlowService flowService;

    @Autowired
    private OrganizationService organizationService;

    @Override
    public List<Long> variableUserResolve(FlowCaseState ctx, Map<String, Long> processedEntities,
                                          FlowEntityType fromEntity, Long entityId,
                                          FlowUserSelection userSelection, int loopCnt) {
        FlowCase flowCase = ctx.getFlowCase();

        Long organizationId = flowCase.getApplierOrganizationId();
        // 判断flowCase里是否有申请人公司id
        if (organizationId == null) {
            List<OrganizationDTO> organizations = organizationService.listUserRelateOrganizations(
                    flowCase.getNamespaceId(), flowCase.getApplyUserId(), OrganizationGroupType.ENTERPRISE);
            // 如果当前用户只在一个公司里，则就用这个公司
            if (organizations != null && organizations.size() == 1) {
                organizationId = organizations.get(0).getId();
            }
        }

        if (organizationId != null) {
            List<String> groupTypes = new ArrayList<>();
            groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
            groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());

            List<OrganizationDTO> memberGroups = organizationService.getOrganizationMemberGroups(
                    groupTypes, flowCase.getApplyUserId(), organizationId);

            if (memberGroups != null) {
                List<Long> organizationIds = memberGroups.stream().map(OrganizationDTO::getId).collect(Collectors.toList());
                List<OrganizationManagerDTO> managers = organizationService.getOrganizationManagers(organizationIds);
                if (managers != null) {
                    return managers.stream().map(OrganizationManagerDTO::getTargetId).distinct().collect(Collectors.toList());
                }
            }
        }
        return new ArrayList<>();
    }
}