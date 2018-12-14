// @formatter:off
package com.everhomes.flow.vars;

import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseState;
import com.everhomes.flow.FlowUserSelection;
import com.everhomes.flow.FlowVariableUserResolver;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.acl.ListServiceModuleAdministratorsCommand;
import com.everhomes.rest.common.ActivationFlag;
import com.everhomes.rest.flow.FlowEntityType;
import com.everhomes.rest.organization.OrganizationContactDTO;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component(FlowVariableUserResolver.APPLIER_ORGANIZATION_MANAGER)
public class FlowVariableApplierOrganizationManagerResolver implements FlowVariableUserResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowVariableApplierOrganizationManagerResolver.class);

    // @Autowired
    // private FlowUserSelectionProvider flowUserSelectionProvider;

    // @Autowired
    // private FlowService flowService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private RolePrivilegeService rolePrivilegeService;

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
            ListServiceModuleAdministratorsCommand cmd = new ListServiceModuleAdministratorsCommand();
            cmd.setOrganizationId(organizationId);
            cmd.setActivationFlag(ActivationFlag.YES.getCode());

            List<OrganizationContactDTO> administrators = rolePrivilegeService.listOrganizationSuperAdministrators(cmd);
            if (administrators != null) {
                return administrators.stream().map(OrganizationContactDTO::getTargetId).distinct().collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }
}
