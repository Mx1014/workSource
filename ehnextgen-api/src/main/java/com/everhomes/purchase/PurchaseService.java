//@formatter:off
package com.everhomes.purchase;

import com.everhomes.rest.purchase.*;

import java.util.List;

/**
 * Created by Wentian Wang on 2018/2/5.
 */

public interface PurchaseService {
    void CreateOrUpdatePurchaseOrderCommand(CreateOrUpdatePurchaseOrderCommand cmd);

    SearchPurchasesResponse searchPurchases(SearchPurchasesCommand cmd);

    void entryWarehouse(Long purchaseRequestId,Long communityId);

    GetPurchaseOrderDTO getPurchaseOrder(GetPurchaseOrderCommand cmd);

    void deletePurchaseOrder(DeletePurchaseOrderCommand cmd);

    List<GetWarehouseMaterialPurchaseHistoryDTO> getWarehouseMaterialPurchaseHistory(GetWarehouseMaterialPurchaseHistoryCommand cmd);
}
