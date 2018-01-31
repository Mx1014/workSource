//@formatter:off
package com.everhomes.requisition;

import com.everhomes.rest.Requisition.CreateRequisitionCommand;
import com.everhomes.rest.Requisition.ListRequisitionsCommand;
import com.everhomes.rest.Requisition.ListRequisitionsResponse;

/**
 * Created by Wentian on 2018/1/20.
 */
public interface RequisitionService {
    void createRequisition(CreateRequisitionCommand cmd);

    ListRequisitionsResponse listRequisitions(ListRequisitionsCommand cmd);
}
