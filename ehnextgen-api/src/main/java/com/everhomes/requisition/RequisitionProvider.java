//@formatter:off
package com.everhomes.requisition;

import com.everhomes.general_approval.GeneralApproval;
import com.everhomes.rest.requisition.ListRequisitionsDTO;

import java.util.List;

/**
 * Created by Wentian on 2018/1/20.
 */
public interface RequisitionProvider {
    void saveRequisition(Requisition req);

    List<ListRequisitionsDTO> listRequisitions(Integer namespaceId, String ownerType, Long ownerId, Long communityId,
                                               String theme
            , Long typeId, Long pageAnchor, Integer integer, Byte requisitionStatus);

    Requisition findRequisitionById(Long requisitionId);

    List<RequisitionType> listRequisitionTypes(Integer namespaceId, Long ownerId, String ownerType);

    void changeRequisitionStatus2Target(Byte target, Long referId);

    String getNameById(Long requisitionId);

    /**
     * 根据域空间和moudleId获取正在运行的审批
     * @param moduleId
     * @param namespaceId
     * @return
     */
    GeneralApproval getRunnintEApproval(Long moduleId, Integer namespaceId, Long ownerId);
}
