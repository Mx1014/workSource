//@formatter:off
package com.everhomes.requisition;

import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowService;
import com.everhomes.naming.NameMapper;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.flow.CreateFlowCaseCommand;
import com.everhomes.rest.flow.FlowCaseDetailDTOV2;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.organization.ListPMOrganizationsCommand;
import com.everhomes.rest.organization.ListPMOrganizationsResponse;
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
public class RequisitionServiceImpl implements RequisitionService {
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
        createFlowCaseCommand.setServiceType("requisition");
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
        checkAssetPriviledgeForPropertyOrg(cmd.getCommunityId(), PrivilegeConstants.REQUISITION_VIEW);
        ListRequisitionsResponse response = new ListRequisitionsResponse();
        Long pageAnchor = cmd.getPageAnchor();
        Integer pageSize = cmd.getPageSize();
        if(pageAnchor == null) pageAnchor = 0l;
        if(pageSize == null) pageSize = 20;
        List<ListRequisitionsDTO> result = requisitionProvider.listRequisitions(cmd.getNamespaceId(),cmd.getOwnerType()
                ,cmd.getOwnerId(),cmd.getCommunityId(),cmd.getTheme(),cmd.getTypeId(),pageAnchor,++pageSize);
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
}
