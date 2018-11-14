package com.everhomes.requisition;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.flow.*;
import com.everhomes.general_approval.GeneralApproval;
import com.everhomes.general_approval.GeneralApprovalProvider;
import com.everhomes.general_form.*;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.flow.*;
import com.everhomes.rest.general_approval.*;
import com.everhomes.rest.launchpad.ActionType;
import com.everhomes.rest.requisition.RequistionErrorCodes;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    private GeneralFormProvider generalFormProvider;
    @Autowired
    private GeneralApprovalProvider generalApprovalProvider;
    @Autowired
    private DbProvider dbProvider;
    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;


    @Override
    public PostGeneralFormDTO postGeneralFormVal(PostGeneralFormValCommand cmd) {
        userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), cmd.getCurrentOrganizationId(), PrivilegeConstants.REQUISITION_CREATE, ServiceModuleConstants.REQUISITION_MODULE, null, null, null, cmd.getOwnerId());
        PostGeneralFormDTO result = this.dbProvider.execute((TransactionStatus status) -> {
            Long referId;
            if(cmd.getRequisitionId() == null) {
                referId = generalFormService.saveGeneralForm(cmd);
            }else{
                referId = cmd.getRequisitionId();
            }
            GeneralApproval approval = generalApprovalProvider.getGeneralApprovalByNameAndRunning(
                    cmd.getNamespaceId(), cmd.getSourceId(), cmd.getOwnerId(), cmd.getOwnerType());


            //创建工作流
            Flow flow = flowService.getEnabledFlow(cmd.getNamespaceId(), FlowConstants.REQUISITION_MODULE
                    , "requisition",approval.getId(), "GENERAL_APPROVAL");
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
            createFlowCaseCommand.setServiceType("请示单申请");
            createFlowCaseCommand.setProjectId(cmd.getOwnerId());
            createFlowCaseCommand.setProjectType(cmd.getOwnerType());

            FlowCaseDetailDTOV2 flowCase = flowService.getFlowCaseDetailByRefer(cmd.getModuleId(), FlowUserType.NO_USER, UserContext.currentUserId(), createFlowCaseCommand.getReferType(), referId, false);
            if(flowCase != null){
                LOGGER.error("this requisition is being approval, please check . sourceId ={}", referId);
                throw RuntimeErrorException.errorWith(RequistionErrorCodes.SCOPE, RequistionErrorCodes.ERROR_FLOW_BEING_APPROVAL,
                        "this requisition is being approval, please check .");
            }

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
        userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), cmd.getCurrentOrganizationId(), PrivilegeConstants.REQUISITION_CREATE, ServiceModuleConstants.REQUISITION_MODULE, null, null, null, cmd.getOwnerId());

        PostGeneralFormValCommand deleteCmd = new PostGeneralFormValCommand();
        deleteCmd.setSourceId(cmd.getRequisitionId());
        deleteCmd.setModuleId(cmd.getSourceId());
        deleteCmd.setNamespaceId(cmd.getNamespaceId());
        generalFormService.deleteGeneralFormVal(deleteCmd);

        addGeneralFormValuesCommand cmd2 = new addGeneralFormValuesCommand();


        GeneralFormValRequest request = generalFormProvider.getGeneralFormValRequest(cmd.getRequisitionId());
        //GeneralApproval generalApproval =generalApprovalProvider.getGeneralApprovalByNameAndRunning(cmd.getNamespaceId(), cmd.getSourceId(), cmd.getOwnerId(), cmd.getOwnerType());
        String source_type = "EhGeneralFormValRequests";

        if(request != null) {
            cmd2.setGeneralFormId(request.getFormOriginId());
            cmd2.setGeneralFormVersion(request.getFormVersion());
            cmd2.setSourceId(cmd.getRequisitionId());
            cmd2.setSourceType(source_type);
            cmd2.setValues(cmd.getValues());
            generalFormService.addGeneralFormValues(cmd2);
        }else{
            LOGGER.error("getGeneralApprovalByNameAndRunning false: can not find running approval. namespaceId: " + cmd.getNamespaceId() + ", ownerType: "
                    + cmd.getOwnerType() + ", sourceType: " + cmd.getSourceType() + ", ownerId: " + cmd.getOwnerId() + ", sourceId: " + cmd.getSourceId());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_CLASS_NOT_FOUND,
                    "getGeneralApprovalByNameAndRunning false: can not find running approval.");
        }
        PostGeneralFormDTO dto = ConvertHelper.convert(cmd, PostGeneralFormDTO.class);
        return dto;
    }

    @Override
    public Long saveGeneralFormVal(PostGeneralFormValCommand cmd){
        userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), cmd.getCurrentOrganizationId(), PrivilegeConstants.REQUISITION_CREATE, ServiceModuleConstants.REQUISITION_MODULE, null, null, null, cmd.getOwnerId());
        return generalFormService.saveGeneralForm(cmd);
    }
    @Override
    public List<GeneralFormValDTO> getGeneralFormVal(GetGeneralFormValCommand cmd){
        userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), cmd.getCurrentOrganizationId(), PrivilegeConstants.REQUISITION_VIEW, ServiceModuleConstants.REQUISITION_MODULE, null, null, null, cmd.getOwnerId());
        return generalFormService.getGeneralFormVal(cmd);
    }

    @Override
    public Long deleteGeneralFormVal(PostGeneralFormValCommand cmd){
        userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), cmd.getCurrentOrganizationId(), PrivilegeConstants.REQUISITION_DELETE, ServiceModuleConstants.REQUISITION_MODULE, null, null, null, cmd.getOwnerId());
        FlowCaseDetailDTOV2 flowcase = flowService.getFlowCaseDetailByRefer(PrivilegeConstants.REQUISITION_MODULE, null, null, "requisitionId", cmd.getSourceId(), true);
        Long sourceId = this.dbProvider.execute((TransactionStatus status) -> {

            if (flowcase != null) {
                FlowCase flowCase = flowService.getFlowCaseById(flowcase.getId());
                FlowNodeDetailDTO node = flowService.getFlowNodeDetail(flowCase.getCurrentNodeId());
                String type = node.getNodeType();
                if (!type.equals(FlowNodeType.END.getCode())) {
                    FlowAutoStepDTO stepDTO = new FlowAutoStepDTO();
                    stepDTO.setFlowCaseId(flowCase.getId());
                    stepDTO.setFlowNodeId(flowCase.getCurrentNodeId());
                    stepDTO.setStepCount(flowCase.getStepCount());
                    stepDTO.setAutoStepType(FlowStepType.ABSORT_STEP.getCode());
                    stepDTO.setEventType(FlowEventType.STEP_MODULE.getCode());
                    stepDTO.setOperatorId(UserContext.currentUserId());
                    flowService.processAutoStep(stepDTO);
                }
                DeleteFlowCaseCommand cmd2 = new DeleteFlowCaseCommand();
                cmd2.setFlowCaseId(flowCase.getId());
                flowService.deleteFlowCase(cmd2);
                return generalFormService.deleteGeneralForm(cmd);
            }else{
                return generalFormService.deleteGeneralForm(cmd);
            }
        });
        return sourceId;
    }

    @Override
    public GeneralFormReminderDTO getGeneralFormReminder(GeneralFormReminderCommand cmd) {
        return null;
    }
}
