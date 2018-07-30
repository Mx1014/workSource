package com.everhomes.requisition;

import com.everhomes.db.DbProvider;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowService;
import com.everhomes.general_form.GeneralFormModuleHandler;
import com.everhomes.general_form.GeneralFormService;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.general_approval.*;
import com.everhomes.rest.requisition.RequistionErrorCodes;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

@Component(GeneralFormModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX + "requisition")
public class RequisitionFormHandler implements GeneralFormModuleHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequisitionFormHandler.class);

    @Autowired
    RequisitionService requisitionService;
    @Autowired
    private FlowService flowService;
    @Autowired
    private GeneralFormService generalFormService;
    @Autowired
    private DbProvider dbProvider;

    @Override
    public PostGeneralFormDTO postGeneralFormVal(PostGeneralFormValCommand cmd) {
        PostGeneralFormDTO result = this.dbProvider.execute((TransactionStatus status) -> {
            Long referId = generalFormService.saveGeneralForm(cmd);

            //创建工作流
            Flow flow = flowService.getEnabledFlow(cmd.getNamespaceId(), FlowConstants.REQUISITION_MODULE
                    , FlowModuleType.NO_MODULE.getCode(),cmd.getOwnerId(), cmd.getOwnerType());
            if (null == flow) {
                LOGGER.error("Enable request flow not found, moduleId={}", FlowConstants.REQUISITION_MODULE);
                throw RuntimeErrorException.errorWith(RequistionErrorCodes.SCOPE, RequistionErrorCodes.ERROR_CREATE_FLOW_CASE,
                        "requistion flow case not found.");
            }
            CreateFlowCaseCommand createFlowCaseCommand = new CreateFlowCaseCommand();
            createFlowCaseCommand.setReferId(referId);
            createFlowCaseCommand.setReferType("requisitionId");
            createFlowCaseCommand.setCurrentOrganizationId(cmd.getCurrentOrganizationId());
            createFlowCaseCommand.setTitle("请示单申请");
            createFlowCaseCommand.setApplyUserId(UserContext.current().getUser().getId());
            createFlowCaseCommand.setFlowMainId(flow.getFlowMainId());
            createFlowCaseCommand.setFlowVersion(flow.getFlowVersion());
            createFlowCaseCommand.setServiceType("requisition");
            createFlowCaseCommand.setProjectId(cmd.getOwnerId());
            createFlowCaseCommand.setProjectType(cmd.getOwnerType());

            flowService.createFlowCase(createFlowCaseCommand);
            PostGeneralFormDTO dto = ConvertHelper.convert(cmd, PostGeneralFormDTO.class);
            return dto;
        });
        return result;
    }

    @Override
    public GeneralFormDTO getTemplateBySourceId(GetTemplateBySourceIdCommand cmd) {
        return null;
    }

    @Override
    public PostGeneralFormDTO updateGeneralFormVal(PostGeneralFormValCommand cmd) {
        return null;
    }

    @Override
    public GeneralFormReminderDTO getGeneralFormReminder(GeneralFormReminderCommand cmd) {
        return null;
    }
}
