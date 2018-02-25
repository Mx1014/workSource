//@formatter:off
package com.everhomes.purchase;

import com.everhomes.rest.purchase.*;

/**
 * Created by Wentian Wang on 2018/2/5.
 */

public interface PurchaseService {
    void CreateOrUpdatePurchaseOrderCommand(CreateOrUpdatePurchaseOrderCommand cmd);

    SearchPurchasesResponse searchPurchases(SearchPurchasesCommand cmd);

    void entryWarehouse(Long purchaseRequestId);

    GetPurchaseOrderDTO getPurchaseOrder(GetPurchaseOrderCommand cmd);

    void deletePurchaseOrder(Long purchaseRequestId);
}
