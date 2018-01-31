//@formatter:off
package com.everhomes.requisition;

import com.everhomes.naming.NameMapper;
import com.everhomes.rest.Requisition.CreateRequisitionCommand;
import com.everhomes.rest.Requisition.ListRequisitionsCommand;
import com.everhomes.rest.Requisition.ListRequisitionsDTO;
import com.everhomes.rest.Requisition.ListRequisitionsResponse;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.pojos.EhRequisitions;
import com.everhomes.supplier.SupplierHelper;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        response.setData(result);
        return response;
    }
}
