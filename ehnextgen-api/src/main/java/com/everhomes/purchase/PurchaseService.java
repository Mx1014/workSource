//@formatter:off
package com.everhomes.purchase;

import com.everhomes.rest.purchase.CreateOrUpdatePurchaseOrderCommand;
import com.everhomes.rest.purchase.SearchPurchasesCommand;
import com.everhomes.rest.purchase.SearchPurchasesResponse;

/**
 * Created by Wentian Wang on 2018/2/5.
 */

public interface PurchaseService {
    void CreateOrUpdatePurchaseOrderCommand(CreateOrUpdatePurchaseOrderCommand cmd);

    SearchPurchasesResponse searchPurchases(SearchPurchasesCommand cmd);

    void entryWarehouse(Long purchaseRequestId);
}
