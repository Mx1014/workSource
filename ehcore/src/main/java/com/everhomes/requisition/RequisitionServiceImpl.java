//@formatter:off
package com.everhomes.requisition;

import com.everhomes.app.AppService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseProvider;
import com.everhomes.flow.FlowService;
import com.everhomes.general_approval.GeneralApproval;
import com.everhomes.general_approval.GeneralApprovalProvider;
import com.everhomes.general_form.*;
import com.everhomes.naming.NameMapper;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.portal.PortalService;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowCaseDetailDTOV2;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.general_approval.GeneralApprovalDTO;
import com.everhomes.rest.general_approval.GeneralApprovalStatus;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.general_approval.GeneralFormValDTO;
import com.everhomes.rest.organization.ListPMOrganizationsCommand;
import com.everhomes.rest.organization.ListPMOrganizationsResponse;

import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;

import com.everhomes.rest.requisition.*;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.pojos.EhRequisitions;
import com.everhomes.supplier.SupplierHelper;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wentian Wang on 2018/1/20.
 */

@Service
public class RequisitionServiceImpl implements RequisitionService{
    private static final Logger LOGGER = LoggerFactory.getLogger(RequisitionServiceImpl.class);
    @Autowired
    private RequisitionProvider requisitionProvider;
    @Autowired
    private SequenceProvider sequenceProvider;
    @Autowired
    private DbProvider dbProvider;
    @Autowired
    private FlowService flowService;
    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private FlowCaseProvider flowCaseProvider;
    @Autowired
    private GeneralApprovalProvider generalApprovalProvider;
    @Autowired
    private GeneralFormProvider generalFormProvider;
    @Autowired
    private OrganizationProvider organizationProvider;
    @Autowired
    private PortalService portalService;

    @Override
    public void createRequisition(CreateRequisitionCommand cmd) {
        checkAssetPriviledgeForPropertyOrg(cmd.getCommunityId(),PrivilegeConstants.REQUISITION_CREATE);
        Requisition req = ConvertHelper.convert(cmd, Requisition.class);
        req.setCreateUid(UserContext.currentUserId());
        long nextReqId = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo
                (EhRequisitions.class));
        req.setId(nextReqId);
        req.setIdentity(SupplierHelper.getIdentity());
        req.setCreateUid(UserContext.currentUserId());
        req.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        //创建工作流
        Flow flow = flowService.getEnabledFlow(cmd.getNamespaceId(), FlowConstants.REQUISITION_MODULE
                , FlowModuleType.NO_MODULE.getCode(),cmd.getOwnerId(), cmd.getFlowOwnerType());
        if (null == flow) {
            LOGGER.error("Enable request flow not found, moduleId={}", FlowConstants.REQUISITION_MODULE);
            throw RuntimeErrorException.errorWith(RequistionErrorCodes.SCOPE, RequistionErrorCodes.ERROR_CREATE_FLOW_CASE,
                     "requistion flow case not found.");
        }
        CreateFlowCaseCommand createFlowCaseCommand = new CreateFlowCaseCommand();
        createFlowCaseCommand.setReferId(nextReqId);
        createFlowCaseCommand.setReferType("requisitionId");
        createFlowCaseCommand.setCurrentOrganizationId(req.getOwnerId());
        createFlowCaseCommand.setTitle("请示单申请");
        createFlowCaseCommand.setApplyUserId(req.getCreateUid());
        createFlowCaseCommand.setFlowMainId(flow.getFlowMainId());
        createFlowCaseCommand.setFlowVersion(flow.getFlowVersion());
//        createFlowCaseCommand.setReferId(req.getId());
//        createFlowCaseCommand.setReferType(EntityType.WAREHOUSE_REQUEST.getCode());
        createFlowCaseCommand.setServiceType("请示单申请");
        createFlowCaseCommand.setProjectId(req.getOwnerId());
        createFlowCaseCommand.setProjectType(req.getOwnerType());

        this.dbProvider.execute((TransactionStatus status) -> {
            requisitionProvider.saveRequisition(req);
            flowService.createFlowCase(createFlowCaseCommand);
            return null;
        });
    }

    @Override
    public ListRequisitionsResponse listRequisitions(ListRequisitionsCommand cmd) {
        //checkAssetPriviledgeForPropertyOrg(cmd.getCommunityId(), PrivilegeConstants.REQUISITION_VIEW);
        ListRequisitionsResponse response = new ListRequisitionsResponse();
        Long pageAnchor = cmd.getPageAnchor();
        Integer pageSize = cmd.getPageSize();
        if(pageAnchor == null) pageAnchor = 0l;
        if(pageSize == null) pageSize = 20;
        List<ListRequisitionsDTO> result = requisitionProvider.listRequisitions(cmd.getNamespaceId(),cmd.getOwnerType()
                ,cmd.getOwnerId(),cmd.getCommunityId(),cmd.getTheme(),cmd.getTypeId(),pageAnchor,++pageSize, cmd.getRequisitionStatus());
        if(result.size() > pageSize){
            result.remove(result.size()-1);
            response.setNextPageAnchor(pageSize + pageAnchor);
        }
        response.setList(result);
        return response;
    }


    @Override
    public GetRequisitionDetailResponse getRequisitionDetail(GetRequisitionDetailCommand cmd) {
        checkAssetPriviledgeForPropertyOrg(cmd.getCommunityId(),PrivilegeConstants.REQUISITION_VIEW);
        if(cmd.getRequisitionId() == null){
            FlowCase flowCase = flowCaseProvider.getFlowCaseById(cmd.getFlowCaseId());
            cmd.setRequisitionId(flowCase.getReferId());
        }
        Requisition requisition = requisitionProvider.findRequisitionById(cmd.getRequisitionId());
        GetRequisitionDetailResponse response = new GetRequisitionDetailResponse();
        response.setAmount(requisition.getAmount().toPlainString());
        response.setApplicantDepartment(requisition.getApplicantDepartment());
        response.setApplicantName(requisition.getApplicantName());
        response.setAttachmentUrl(requisition.getAttachmentUrl());
        response.setDescription(requisition.getDescription());
        response.setTheme(requisition.getTheme());
        response.setTypeId(requisition.getRequisitionTypeId());
        response.setFileName(requisition.getFileName());
        FlowCaseDetailDTOV2 flowcase = flowService.getFlowCaseDetailByRefer(PrivilegeConstants.REQUISITION_MODULE, null, null, "requisitionId", cmd.getRequisitionId(), true);
        if(flowcase!=null){
            response.setFlowCaseId(flowcase.getId());
        }
        return response;
    }

    @Override
    public List<ListRequisitionTypesDTO> listRequisitionTypes(ListRequisitionTypesCommand cmd) {
        List<ListRequisitionTypesDTO> result = new ArrayList<>();
        List<RequisitionType> list = requisitionProvider.listRequisitionTypes(cmd.getNamespaceId()
                ,cmd.getOwnerId(),cmd.getOwnerType());
        if(list.size() < 1){
            list = requisitionProvider.listRequisitionTypes(0
                    ,0l,"EhNamespaces");
        }
        for(RequisitionType type : list){
            ListRequisitionTypesDTO dto = new ListRequisitionTypesDTO();
            dto.setId(type.getId());
            dto.setName(type.getName());
            result.add(dto);
        }
        return result;
    }

    @Override
    public String getRequisitionNameById(Long requisitionId) {
        return requisitionProvider.getNameById(requisitionId);
    }

    @Override
    public ListRequisitionsResponse listRequisitionsForSecondParty(ListRequisitionsCommand cmd) {
        ListRequisitionsResponse response = new ListRequisitionsResponse();
        Long pageAnchor = cmd.getPageAnchor();
        Integer pageSize = cmd.getPageSize();
        if(pageAnchor == null) pageAnchor = 0l;
        if(pageSize == null) pageSize = 20;
        List<ListRequisitionsDTO> result = requisitionProvider.listRequisitions(cmd.getNamespaceId(),cmd.getOwnerType()
                ,cmd.getOwnerId(),cmd.getCommunityId(),cmd.getTheme(),cmd.getTypeId(),pageAnchor,++pageSize, cmd.getRequisitionStatus());
        if(result.size() > pageSize){
            result.remove(result.size()-1);
            response.setNextPageAnchor(pageSize + pageAnchor);
        }
        response.setList(result);
        return response;
    }

    private void checkAssetPriviledgeForPropertyOrg(Long communityId, Long priviledgeId) {
        ListPMOrganizationsCommand cmd = new ListPMOrganizationsCommand();
        cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
        ListPMOrganizationsResponse listPMOrganizationsResponse = organizationService.listPMOrganizations(cmd);
        Long currentOrgId = null;
        try{
            currentOrgId = listPMOrganizationsResponse.getDtos().get(0).getId();
        }catch (ArrayIndexOutOfBoundsException e){
        }
        userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), currentOrgId, priviledgeId, PrivilegeConstants.REQUISITION_MODULE, (byte)13, null, null, communityId);
    }


    //新增

    @Override
    public Long updateRequisitionApprovalActiveForm(UpdateRequisitionRunningFormCommand cmd){
        GeneralApproval approval = generalApprovalProvider.getGeneralApprovalById(cmd.getSourceId());
        if(cmd.getFormOriginId() != null && cmd.getFormOriginId() >= 0l && cmd.getFormVersion() >= 0 && cmd.getFormVersion() != null) {
            approval.setFormOriginId(cmd.getFormOriginId());
            approval.setFormVersion(cmd.getFormVersion());
            GeneralApproval obj = generalApprovalProvider.updateGeneralApproval(approval);
            Long formOriginId = obj.getFormOriginId();
            return formOriginId;
        }else {
            LOGGER.error("form origin id or form version param cannot null. form_origin_id : " + cmd.getFormOriginId() + ", form_version : " + cmd.getFormVersion());
            throw RuntimeErrorException.errorWith(RequistionErrorCodes.SCOPE,
                    RequistionErrorCodes.ERROR_FORM_PARAM, "form origin id or form version param cannot null");

        }
    }

    public String getUserContactNameByUserId(Long userId) {
        List<OrganizationMember> members = organizationProvider.listOrganizationMembersByUId(userId);
        if (members != null && members.size() > 0)
            return members.get(0).getContactName();
        return "";
    }


    @Override
    public void updateRequisitionApprovalActiveStatus(UpdateRequisitionActiveStatusCommond cmd){
        GeneralApproval approval = generalApprovalProvider.getGeneralApprovalById(cmd.getId());


        if(cmd.getStatus() != null) {
            approval.setStatus(cmd.getStatus());
            if(cmd.getStatus().equals(GeneralApprovalStatus.RUNNING.getCode())){
                GeneralApproval oldRunning = generalApprovalProvider.getGeneralApprovalByNameAndRunning(cmd.getNamespaceId(), cmd.getModuleId(), cmd.getOwnerId(), cmd.getOwnerType());
                if(oldRunning != null){
                    oldRunning.setStatus(GeneralApprovalStatus.INVALID.getCode());
                }
                this.dbProvider.execute((status) -> {
                    if(oldRunning != null){
                            generalApprovalProvider.updateGeneralApproval(oldRunning);
                    }

                    generalApprovalProvider.updateGeneralApproval(approval);
                    return null;
                });

            }else{
                generalApprovalProvider.updateGeneralApproval(approval);
            }
        }
    }

    @Override
    public GeneralFormDTO getRunningRequisitionForm(GetRunningRequisitionFormCommond cmd){
        GeneralApproval runningApproval = generalApprovalProvider.getGeneralApprovalByNameAndRunning(cmd.getNamespaceId(), cmd.getModuleId(), cmd.getOwnerId(), cmd.getOwnerType());
        if(runningApproval != null) {
            GeneralForm form = generalFormProvider.getGeneralFormById(runningApproval.getFormOriginId());
            return ConvertHelper.convert(form, GeneralFormDTO.class);
        }else{
            GeneralApproval getApproval = generalApprovalProvider.getGeneralApprovalByModuleId(cmd.getNamespaceId(), cmd.getModuleId(), cmd.getOwnerId(), cmd.getOwnerType());
            if(getApproval == null) {
                LOGGER.error("no approval has been created" + cmd.getNamespaceId() + ", moduleId : " + cmd.getModuleId() + ", ownerId: " + cmd.getOwnerType());
                throw RuntimeErrorException.errorWith(RequistionErrorCodes.SCOPE,
                        RequistionErrorCodes.ERROR_NO_ONE_APPROVAL, "no approval has been created");
            }else{
                LOGGER.error("no approval has been used" + cmd.getNamespaceId() + ", moduleId : " + cmd.getModuleId() + ", ownerId: " + cmd.getOwnerType());
                throw RuntimeErrorException.errorWith(RequistionErrorCodes.SCOPE,
                        RequistionErrorCodes.ERROR_NO_USED_APPROVAL, "no approval has been used");
            }
            /*GeneralFormTemplate request = generalFormProvider.getDefaultFieldsByModuleId(cmd.getModuleId(), cmd.getNamespaceId());
            return ConvertHelper.convert(request, GeneralFormDTO.class);*/

        }

    }

    @Override
    public GeneralFormDTO getSelectedRequisitionForm(GetSelectedRequisitionFormCommand cmd){
        if(cmd.getFormVersion() != null && cmd.getFormOriginId() != null)
            return ConvertHelper.convert(generalFormProvider.getActiveGeneralFormByOriginIdAndVersion(cmd.getFormOriginId(),cmd.getFormVersion()), GeneralFormDTO.class);
        else{
            LOGGER.error("the form Id can not find");
            throw RuntimeErrorException.errorWith(RequistionErrorCodes.SCOPE,
                    RequistionErrorCodes.ERROR_FORM_PARAM, "the form Id can not find");
        }
    }

    @Override
    public Long getRunningRequisitionFlow(GetRunningRequisitionFlowCommand cmd){
        Flow flow = flowService.getEnabledFlow(cmd.getNamespaceId(), cmd.getProjectType(), cmd.getProjectId(), cmd.getModuleId(), cmd.getModuleType(), cmd.getOwnerId(), cmd.getOwnerType());
        if(flow != null){
            if(flow.getOrganizationId().equals(cmd.getOrgId())){
                return flow.getId();
            }else{
                LOGGER.error("the flow Id can not find");
                throw RuntimeErrorException.errorWith(RequistionErrorCodes.SCOPE,
                        RequistionErrorCodes.ERROR_FORM_PARAM, "the flow Id can not find");
            }
        }else{
            LOGGER.error("the flow Id can not find");
            throw RuntimeErrorException.errorWith(RequistionErrorCodes.SCOPE,
                    RequistionErrorCodes.ERROR_FORM_PARAM, "the flow Id can not find");
        }
    }

    @Override
    public GeneralApprovalDTO getApprovalRunningForm(GetApprovalRunningFormCommond cmd){
        GeneralApproval approval = generalApprovalProvider.getGeneralApprovalById(cmd.getApprovalId());
        if(approval != null){
            return ConvertHelper.convert(approval,GeneralApprovalDTO.class);
        }else{
            LOGGER.error("the approval can not find");
            throw RuntimeErrorException.errorWith(RequistionErrorCodes.SCOPE,
                    RequistionErrorCodes.ERROR_FORM_PARAM, "the approval can not find");
        }
    }


    @Override
    public GetGeneralFormByCustomerIdResponse getGeneralFormByCustomerId(GetGeneralFormByCustomerIdCommand cmd){
        GeneralFormVal val;
        GetGeneralFormByCustomerIdResponse response = new GetGeneralFormByCustomerIdResponse();

        if(cmd.getCustomerId() != null && cmd.getNamespaceId() !=null && cmd.getCommunityId() != null){


            val = generalFormProvider.getGeneralFormValByCustomerId(cmd.getNamespaceId(), cmd.getCustomerId(), cmd.getModuleId(), cmd.getCommunityId());

            ListServiceModuleAppsCommand cmd2 = new ListServiceModuleAppsCommand();
            cmd2.setModuleId(cmd.getModuleId());
            cmd2.setNamespaceId(cmd.getNamespaceId());
            ListServiceModuleAppsResponse appsResponse = portalService.listServiceModuleApps(cmd2);
            if(val != null){
                response.setFormOriginId(val.getFormOriginId());
                response.setFormVersion(val.getFormVersion());
                response.setSourceId(val.getSourceId());
                response.setAppId(appsResponse.getServiceModuleApps().iterator().next().getOriginId());
                return response;
            }
            return null;



        }else{
            LOGGER.error("getGeneralFormVal false: param cannot be null. namespaceId: " + cmd.getNamespaceId() +  ", communityId: " + cmd.getCommunityId() + ", customerId: " + cmd.getCustomerId());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "namespaceId,communityId,customerId cannot be null.");
        }
    }

    private String getContractNameByUserId(Long userId, Long organizationId) {
        OrganizationMember member = organizationProvider.findOrganizationMemberByUIdAndOrgId(userId, organizationId);
        if (member != null) {
            return member.getContactName();
        }
        return UserContext.current().getUser().getNickName();
    }

}
