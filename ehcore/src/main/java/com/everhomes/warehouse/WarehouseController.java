package com.everhomes.warehouse;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.warehouse.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by ying.xiong on 2017/5/10.
 */
@RestDoc(value = "Warehouse Controller", site = "core")
@RestController
@RequestMapping("/warehouse")
public class WarehouseController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseController.class);

//    @Autowired
    private WarehouseService warehouseService;

//    @Autowired
//    private WarehouseSearcher warehouseSearcher;

    /**
     * <b>URL: /warehouse/updateWarehouse</b>
     * <p>创建或修改仓库</p>
     */
    @RequestMapping("updateWarehouse")
    @RestReturn(value = WarehouseDTO.class)
    public RestResponse updateWarehouse(UpdateWarehouseCommand cmd) {

//        WarehouseDTO warehouse = warehouseService.updateWarehouse(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /warehouse/deleteWarehouse</b>
     * <p>删除仓库</p>
     */
    @RequestMapping("deleteWarehouse")
    @RestReturn(value = String.class)
    public RestResponse deleteWarehouse(DeleteWarehouseCommand cmd) {

//        warehouseService.deleteWarehouse(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /warehouse/findWarehouse</b>
     * <p>根据id查询仓库</p>
     */
    @RequestMapping("findWarehouse")
    @RestReturn(value = WarehouseDTO.class)
    public RestResponse findWarehouse(DeleteWarehouseCommand cmd) {

//        WarehouseDTO warehouse = warehouseService.findWarehouse(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /warehouse/searchWarehouses</b>
     * <p>查看仓库</p>
     */
    @RequestMapping("searchWarehouses")
    @RestReturn(value = SearchWarehousesResponse.class)
    public RestResponse searchWarehouses(SearchWarehousesCommand cmd) {

//        SearchWarehousesResponse warehouses = warehouseSearcher.query(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /warehouse/updateWarehouseMaterialCategory</b>
     * <p>创建或修改物品分类</p>
     */
    @RequestMapping("updateWarehouseMaterialCategory")
    @RestReturn(value = WarehouseMaterialCategoryDTO.class)
    public RestResponse updateWarehouseMaterialCategory(UpdateWarehouseMaterialCategoryCommand cmd) {

//        WarehouseMaterialCategoryDTO category = warehouseService.updateWarehouseMaterialCategory(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /warehouse/deleteWarehouseMaterialCategory</b>
     * <p>删除物品分类</p>
     */
    @RequestMapping("deleteWarehouseMaterialCategory")
    @RestReturn(value = String.class)
    public RestResponse deleteWarehouseMaterialCategory(DeleteWarehouseMaterialCategoryCommand cmd) {

//        warehouseService.deleteWarehouseMaterialCategory(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /warehouse/findWarehouseMaterialCategory</b>
     * <p>根据id查询物品分类</p>
     */
    @RequestMapping("findWarehouseMaterialCategory")
    @RestReturn(value = WarehouseMaterialCategoryDTO.class)
    public RestResponse findWarehouseMaterialCategory(DeleteWarehouseMaterialCategoryCommand cmd) {

//        WarehouseMaterialCategoryDTO category = warehouseService.findWarehouseMaterialCategory(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /warehouse/searchWarehouseMaterialCategories</b>
     * <p>查看物品分类</p>
     */
    @RequestMapping("searchWarehouseMaterialCategories")
    @RestReturn(value = SearchWarehouseMaterialCategoriesResponse.class)
    public RestResponse searchWarehouseMaterialCategories(SearchWarehouseMaterialCategoriesCommand cmd) {

//        SearchWarehouseMaterialCategoriesResponse categories = warehouseMaterialCategorySearcher.query(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /warehouse/updateWarehouseMaterial</b>
     * <p>创建或修改物品</p>
     */
    @RequestMapping("updateWarehouseMaterial")
    @RestReturn(value = WarehouseMaterialDTO.class)
    public RestResponse updateWarehouseMaterial(UpdateWarehouseMaterialCommand cmd) {

//        WarehouseMaterialDTO material = warehouseService.updateWarehouseMaterial(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /warehouse/deleteWarehouseMaterial</b>
     * <p>删除物品</p>
     */
    @RequestMapping("deleteWarehouseMaterial")
    @RestReturn(value = String.class)
    public RestResponse deleteWarehouseMaterial(DeleteWarehouseMaterialCommand cmd) {

//        warehouseService.deleteWarehouseMaterial(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /warehouse/findWarehouseMaterial</b>
     * <p>根据id查询物品</p>
     */
    @RequestMapping("findWarehouseMaterial")
    @RestReturn(value = WarehouseMaterialCategoryDTO.class)
    public RestResponse findWarehouseMaterial(DeleteWarehouseMaterialCommand cmd) {

//        WarehouseMaterialDTO material = warehouseService.findWarehouseMaterial(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /warehouse/searchWarehouseMaterials</b>
     * <p>查看物品</p>
     */
    @RequestMapping("searchWarehouseMaterials")
    @RestReturn(value = SearchWarehouseMaterialsResponse.class)
    public RestResponse searchWarehouseMaterials(SearchWarehouseMaterialsCommand cmd) {

//        SearchWarehouseMaterialsResponse materials = warehouseMaterialSearcher.query(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /warehouse/updateWarehouseStock</b>
     * <p>入库/出库</p>
     */
    @RequestMapping("updateWarehouseStock")
    @RestReturn(value = String.class)
    public RestResponse updateWarehouseStock(UpdateWarehouseStockCommand cmd) {

//       warehouseService.updateWarehouseStock(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /warehouse/searchWarehouseStocks</b>
     * <p>查看库存</p>
     */
    @RequestMapping("searchWarehouseStocks")
    @RestReturn(value = SearchWarehouseStocksResponse.class)
    public RestResponse searchWarehouseStocks(SearchWarehouseStocksCommand cmd) {

//        SearchWarehouseStocksResponse stocks = warehouseStockSearcher.query(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /warehouse/searchWarehouseStockLogs</b>
     * <p>查看库存日志</p>
     */
    @RequestMapping("searchWarehouseStockLogs")
    @RestReturn(value = SearchWarehouseStockLogsResponse.class)
    public RestResponse searchWarehouseStockLogs(SearchWarehouseStockLogsCommand cmd) {

//        SearchWarehouseStockLogsResponse logs = warehouseStockLogSearcher.query(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /warehouse/createRequest</b>
     * <p>创建申请</p>
     */
    @RequestMapping("createRequest")
    @RestReturn(value = String.class)
    public RestResponse createRequest(CreateRequestCommand cmd) {

//        warehouseService.createRequest(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /warehouse/findRequest</b>
     * <p>根据id查询申请</p>
     */
    @RequestMapping("findRequest")
    @RestReturn(value = WarehouseRequestDetailsDTO.class)
    public RestResponse findRequest(FindRequestCommand cmd) {

//        WarehouseRequestDetailsDTO request = warehouseService.findRequest(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /warehouse/searchOneselfRequests</b>
     * <p>查看我的申请</p>
     */
    @RequestMapping("searchOneselfRequests")
    @RestReturn(value = SearchRequestsResponse.class)
    public RestResponse searchOneselfRequests(SearchOneselfRequestsCommand cmd) {

//        SearchRequestsResponse requests = warehouseService.searchOneselfRequests(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /warehouse/searchRequests</b>
     * <p>查看申请</p>
     */
    @RequestMapping("searchOneselfRequests")
    @RestReturn(value = SearchRequestsResponse.class)
    public RestResponse searchRequests(SearchRequestsCommand cmd) {

//        SearchRequestsResponse requests = warehouseService.SearchRequestsCommand(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /warehouse/updateWarehouseMaterialUnit</b>
     * <p>创建或修改物品单位</p>
     */
    @RequestMapping("updateWarehouseMaterialUnit")
    @RestReturn(value = String.class)
    public RestResponse updateWarehouseMaterialUnit(UpdateWarehouseMaterialUnitCommand cmd) {

//        warehouseService.updateWarehouseMaterialUnit(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /warehouse/deleteWarehouseMaterialUnit</b>
     * <p>删除物品单位</p>
     */
    @RequestMapping("deleteWarehouseMaterialUnit")
    @RestReturn(value = String.class)
    public RestResponse deleteWarehouseMaterialUnit(DeleteWarehouseMaterialUnitCommand cmd) {

//        warehouseService.deleteWarehouseMaterialUnit(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /warehouse/findWarehouseMaterialUnit</b>
     * <p>根据id查询物品单位</p>
     */
    @RequestMapping("findWarehouseMaterialUnit")
    @RestReturn(value = WarehouseMaterialUnitDTO.class)
    public RestResponse findWarehouseMaterialUnit(DeleteWarehouseMaterialUnitCommand cmd) {

//        WarehouseMaterialUnitDTO unit = warehouseService.findWarehouseMaterialUnit(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /warehouse/listWarehouseMaterialUnits</b>
     * <p>查看物品单位</p>
     */
    @RequestMapping("listWarehouseMaterialUnits")
    @RestReturn(value = WarehouseMaterialUnitDTO.class, collection = true)
    public RestResponse listWarehouseMaterialUnits(ListWarehouseMaterialUnitsCommand cmd) {

//        List<WarehouseMaterialUnitDTO> units = warehouseService.listWarehouseMaterialUnits(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
