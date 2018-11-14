package com.everhomes.investmentAd;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowAutoStepDTO;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowService;
import com.everhomes.general_approval.GeneralApproval;
import com.everhomes.general_approval.GeneralApprovalProvider;
import com.everhomes.general_form.GeneralFormModuleHandler;
import com.everhomes.general_form.GeneralFormProvider;
import com.everhomes.general_form.GeneralFormService;
import com.everhomes.general_form.GeneralFormValRequest;
import com.everhomes.requisition.RequisitionFormHandler;
import com.everhomes.requisition.RequisitionService;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.DeleteFlowCaseCommand;
import com.everhomes.rest.flow.FlowCaseDetailDTOV2;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.flow.FlowEventType;
import com.everhomes.rest.flow.FlowNodeDetailDTO;
import com.everhomes.rest.flow.FlowNodeType;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.general_approval.GeneralFormReminderCommand;
import com.everhomes.rest.general_approval.GeneralFormReminderDTO;
import com.everhomes.rest.general_approval.GeneralFormValDTO;
import com.everhomes.rest.general_approval.GetGeneralFormValCommand;
import com.everhomes.rest.general_approval.GetTemplateBySourceIdCommand;
import com.everhomes.rest.general_approval.PostGeneralFormDTO;
import com.everhomes.rest.general_approval.PostGeneralFormValCommand;
import com.everhomes.rest.general_approval.addGeneralFormValuesCommand;
import com.everhomes.rest.requisition.RequistionErrorCodes;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

@Component(GeneralFormModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX + "investmentAd")
public class InvestmentAdFormHandler implements GeneralFormModuleHandler{

	private static final Logger LOGGER = LoggerFactory.getLogger(InvestmentAdFormHandler.class);

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
        //TODO 权限校验
        
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
            Flow flow = flowService.getEnabledFlow(cmd.getNamespaceId(), FlowConstants.INVESTMENT_AD_MODULE
                    , "investmentAd",approval.getId(), "GENERAL_APPROVAL");
            if (null == flow) {
                LOGGER.error("Enable request flow not found, moduleId={}", FlowConstants.INVESTMENT_AD_MODULE);
                throw RuntimeErrorException.errorWith(RequistionErrorCodes.SCOPE, RequistionErrorCodes.ERROR_CREATE_FLOW_CASE,
                        "investmentAd flow case not found.");
            }
            CreateFlowCaseCommand createFlowCaseCommand = new CreateFlowCaseCommand();
            createFlowCaseCommand.setReferId(referId);
            createFlowCaseCommand.setReferType("requisitionId");
            createFlowCaseCommand.setCurrentOrganizationId(cmd.getCurrentOrganizationId());
            createFlowCaseCommand.setTitle("房源招商申请提交");
            createFlowCaseCommand.setApplyUserId(UserContext.current().getUser().getId());
            createFlowCaseCommand.setFlowMainId(flow.getFlowMainId());
            createFlowCaseCommand.setFlowVersion(flow.getFlowVersion());
            createFlowCaseCommand.setServiceType("房源招商申请提交");
            createFlowCaseCommand.setProjectId(cmd.getOwnerId());
            createFlowCaseCommand.setProjectType(cmd.getOwnerType());

            flowService.createFlowCase(createFlowCaseCommand);
            PostGeneralFormDTO dto = ConvertHelper.convert(cmd, PostGeneralFormDTO.class);
            generalFormProvider.setInvestmentAdId(referId,cmd.getInvestmentAdId());
            
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
    	//TODO 权限校验
    	
        generalFormService.deleteGeneralFormVal(cmd);
        addGeneralFormValuesCommand cmd2 = new addGeneralFormValuesCommand();
        GeneralFormValRequest request = generalFormProvider.getGeneralFormValRequest(cmd.getRequisitionId());
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
    	//TODO 权限校验
    	return generalFormService.saveGeneralForm(cmd);
    }
    @Override
    public List<GeneralFormValDTO> getGeneralFormVal(GetGeneralFormValCommand cmd){
    	//TODO 权限校验
    	
    	return generalFormService.getGeneralFormVal(cmd);
    }

    @Override
    public Long deleteGeneralFormVal(PostGeneralFormValCommand cmd){
    	//TODO 权限校验
    	
    	FlowCaseDetailDTOV2 flowcase = flowService.getFlowCaseDetailByRefer(FlowConstants.INVESTMENT_AD_MODULE, null, null, "requisitionId", cmd.getSourceId(), true);
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
                return generalFormService.deleteGeneralFormVal(cmd);
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
