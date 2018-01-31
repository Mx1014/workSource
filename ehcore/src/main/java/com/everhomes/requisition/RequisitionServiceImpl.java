//@formatter:off
package com.everhomes.requisition;

import com.everhomes.naming.NameMapper;
import com.everhomes.rest.Requisition.*;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.pojos.EhRequisitions;
import com.everhomes.supplier.SupplierHelper;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wentian Wang on 2018/1/20.
 */

@Service
public class RequisitionServiceImpl implements RequisitionService {
    @Autowired
    private RequisitionProvider requisitionProvider;
    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public void createRequisition(CreateRequisitionCommand cmd) {
        Requisition req = ConvertHelper.convert(cmd, Requisition.class);
        req.setCreateUid(UserContext.currentUserId());
        req.setId(this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhRequisitions.class)));
        req.setIdentity(SupplierHelper.getIdentity());
        req.setCreateUid(UserContext.currentUserId());
        req.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        requisitionProvider.saveRequisition(req);

    }

    @Override
    public ListRequisitionsResponse listRequisitions(ListRequisitionsCommand cmd) {
        ListRequisitionsResponse response = new ListRequisitionsResponse();
        Long pageAnchor = cmd.getPageAnchor();
        Integer pageSize = cmd.getPageSize();
        if(pageAnchor == null) pageAnchor = 0l;
        if(pageSize == null) pageSize = 20;
        List<ListRequisitionsDTO> result = requisitionProvider.listRequisitions(cmd.getNamespaceId(),cmd.getOwnerType()
                ,cmd.getOwnerId(),cmd.getTheme(),cmd.getTypeId(),pageAnchor,++pageSize);
        if(result.size() > pageSize){
            result.remove(result.size()-1);
            response.setNextPageAnchor(pageSize + pageAnchor);
        }
        response.setList(result);
        return response;
    }

    @Override
    public GetRequisitionDetailResponse getRequisitionDetail(GetRequisitionDetailCommand cmd) {
        Requisition requisition = requisitionProvider.findRequisitionById(cmd.getRequisitionId());
        GetRequisitionDetailResponse response = new GetRequisitionDetailResponse();
        response.setAmount(requisition.getAmount().toPlainString());
        response.setApplicantDepartment(requisition.getApplicantDepartment());
        response.setApplicantName(requisition.getApplicantName());
        response.setAttachmentUrl(requisition.getAttachmentUrl());
        response.setDescription(requisition.getDescription());
        response.setTheme(requisition.getTheme());
        response.setTypeId(requisition.getRequisitionTypeId());
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

}
