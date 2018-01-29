//@formatter:off
package com.everhomes.purchase;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.purchase.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Wentian Wang on 2018/1/10.
 */

@RestController
@RequestMapping("/purchase")
public class PurchaseController extends ControllerBase{


    /**
     * <b>URL: /purchase/searchPurchases</b>
     * <p>采购管理列表</p>
     */
    @RequestMapping("searchPurchases")
    @RestReturn(value = SearchPurchasesResponse.class)
    private RestResponse searchPurchases(SearchPurchasesCommand cmd){
        RestResponse restResponse = new RestResponse();
        restResponse.setErrorCode(200);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }

    /**
     * <b>URL: /purchase/entryWarehouse</b>
     * <p>采购物品入库</p>
     */
    @RequestMapping("entryWarehouse")
    @RestReturn(value = String.class)
    private RestResponse entryWarehouse(EntryWarehouseCommand cmd){
        RestResponse restResponse = new RestResponse();
        restResponse.setErrorCode(200);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }

    /**
     * <b>URL: /purchase/createOrUpdatePurchaseOrder</b>
     * <p>新增或者修改一个采购单</p>
     */
    @RequestMapping("createOrUpdatePurchaseOrder")
    @RestReturn(value = String.class)
    private RestResponse createOrUpdatePurchaseOrder(CreateOrUpdatePurchaseOrderCommand cmd){
        RestResponse restResponse = new RestResponse();
        restResponse.setErrorCode(200);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }

    /**
     * <b>URL: /purchase/getPurchaseOrder</b>
     * <p>获得一个采购单的详情</p>
     */
    @RequestMapping("getPurchaseOrder")
    @RestReturn(value = GetPurchaseOrderDTO.class)
    private RestResponse getPurchaseOrder(GetPurchaseOrderCommand cmd){
        RestResponse restResponse = new RestResponse();
        restResponse.setErrorCode(200);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }

    /**
     * <b>URL: /purchase/deletePurchaseOrder</b>
     * <p>删除一个采购单</p>
     */
    @RequestMapping("deletePurchaseOrder")
    @RestReturn(value = String.class)
    private RestResponse deletePurchaseOrder(DeletePurchaseOrderCommand cmd){
        RestResponse restResponse = new RestResponse();
        restResponse.setErrorCode(200);
        restResponse.setErrorDescription("OK");
        return restResponse;
    }
}
