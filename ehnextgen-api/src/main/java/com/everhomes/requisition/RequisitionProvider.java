//@formatter:off
package com.everhomes.requisition;

import com.everhomes.rest.Requisition.ListRequisitionsDTO;

import java.util.List;

/**
 * Created by Wentian on 2018/1/20.
 */
public interface RequisitionProvider {
    void saveRequisition(Requisition req);

    List<ListRequisitionsDTO> listRequisitions(Integer namespaceId, String ownerType, Long ownerId, String theme, String typeId, Long pageAnchor, Integer integer);
}
