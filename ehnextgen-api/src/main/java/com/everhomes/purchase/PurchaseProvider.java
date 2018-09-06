//@formatter:off
package com.everhomes.purchase;

import com.everhomes.rest.purchase.GetWarehouseMaterialPurchaseHistoryDTO;
import com.everhomes.rest.purchase.SearchPurchasesDTO;
import com.everhomes.server.schema.tables.pojos.EhWarehousePurchaseItems;

import java.util.List;

/**
 * Created by Wentian Wang on 2018/2/5.
 */

public interface PurchaseProvider {
    void insertPurchaseOrder(PurchaseOrder order);

    void insertPurchaseItems(List<EhWarehousePurchaseItems> list);

    void changePurchaseOrderStatus2Target(byte code, Long referId);

    List<SearchPurchasesDTO> findPurchaseOrders(Long pageAnchor, Integer integer, Byte submissionStatus, Byte warehouseStatus, String applicant, Long ownerId, String ownerType, Integer namespaceId, Long communityId);

    PurchaseOrder getPurchaseOrderById(Long purchaseRequestId);

    List<PurchaseItem> getPurchaseItemsByOrderId(Long purchaseRequestId);

    void deleteOrderById(Long purchaseRequestId);

    void deleteOrderItemsByOrderId(Long purchaseRequestId);

    List<GetWarehouseMaterialPurchaseHistoryDTO> getWarehouseMaterialPurchaseHistory(Long communityId, Long ownerId, String ownerType, Long materialId);
}
