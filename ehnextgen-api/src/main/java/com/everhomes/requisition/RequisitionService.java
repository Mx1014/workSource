//@formatter:off
package com.everhomes.requisition;

import com.everhomes.rest.requisition.*;

import java.util.List;

/**
 * Created by Wentian on 2018/1/20.
 */
public interface RequisitionService {
    void createRequisition(CreateRequisitionCommand cmd);

    ListRequisitionsResponse listRequisitions(ListRequisitionsCommand cmd);

    GetRequisitionDetailResponse getRequisitionDetail(GetRequisitionDetailCommand cmd);

    List<ListRequisitionTypesDTO> listRequisitionTypes(ListRequisitionTypesCommand cmd);
}
