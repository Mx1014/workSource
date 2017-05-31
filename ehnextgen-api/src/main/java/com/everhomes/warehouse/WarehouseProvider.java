package com.everhomes.warehouse;

import com.everhomes.listing.CrossShardListingLocator;

import java.util.List;

/**
 * Created by ying.xiong on 2017/5/10.
 */
public interface WarehouseProvider {

    void creatWarehouse(Warehouses warehouse);
    void updateWarehouse(Warehouses warehouse);
    Warehouses findWarehouse(Long id, String ownerType, Long ownerId);
    Warehouses findWarehouseByNumber(String warehouseNumber, String ownerType, Long ownerId);

    void creatWarehouseMaterialCategories(WarehouseMaterialCategories category);
    void updateWarehouseMaterialCategories(WarehouseMaterialCategories category);
    WarehouseMaterialCategories findWarehouseMaterialCategories(Long id, String ownerType, Long ownerId);
    WarehouseMaterialCategories findWarehouseMaterialCategoriesByNumber(String categoryNumber, String ownerType, Long ownerId);
    List<WarehouseMaterialCategories> listAllChildWarehouseMaterialCategories(String superiorPath, String ownerType, Long ownerId);

    void creatWarehouseMaterials(WarehouseMaterials materials);
    void updateWarehouseMaterials(WarehouseMaterials materials);
    WarehouseMaterials findWarehouseMaterials(Long id, String ownerType, Long ownerId);
    WarehouseMaterials findWarehouseMaterialsByNumber(String materialNumber, String ownerType, Long ownerId);
    List<WarehouseMaterials> listWarehouseMaterialsByCategory(String categoryPath, String ownerType, Long ownerId);

    void creatWarehouseStockLogs(WarehouseStockLogs log);
    WarehouseStockLogs findWarehouseStockLogs(Long id, String ownerType, Long ownerId);

    void creatWarehouseStock(WarehouseStocks stock);
    void updateWarehouseStock(WarehouseStocks stock);
    WarehouseStocks findWarehouseStocks(Long id, String ownerType, Long ownerId);
    WarehouseStocks findWarehouseStocksByWarehouseAndMaterial(Long warehouseId, Long materialId, String ownerType, Long ownerId);
    List<WarehouseStocks> listWarehouseStocks(Long warehouseId, String ownerType, Long ownerId);
    Long getWarehouseStockAmount(Long warehouseId, String ownerType, Long ownerId);
    Long getWarehouseStockAmountByMaterialId(Long materialId, String ownerType, Long ownerId);
    WarehouseUnits findWarehouseUnits(Long id, String ownerType, Long ownerId);
    WarehouseUnits findWarehouseUnitByName(String name, String ownerType, Long ownerId);

    List<Warehouses> listWarehouses(CrossShardListingLocator locator, Integer pageSize);
    List<WarehouseMaterialCategories> listWarehouseMaterialCategories(CrossShardListingLocator locator, Integer pageSize);
    List<WarehouseMaterials> listWarehouseMaterials(CrossShardListingLocator locator, Integer pageSize);
    List<WarehouseStocks> listWarehouseStocks(CrossShardListingLocator locator, Integer pageSize);
    List<WarehouseStockLogs> listWarehouseStockLogs(CrossShardListingLocator locator, Integer pageSize);
    List<WarehouseRequestMaterials> listWarehouseRequestMaterials(CrossShardListingLocator locator, Integer pageSize);

    void creatWarehouseUnit(WarehouseUnits unit);
    void updateWarehouseUnit(WarehouseUnits unit);
    List<WarehouseUnits> listWarehouseMaterialUnits(String ownerType, Long ownerId);

    void creatWarehouseRequest(WarehouseRequests request);
    WarehouseRequests findWarehouseRequests(Long id, String ownerType, Long ownerId);
    void updateWarehouseRequest(WarehouseRequests request);

    void creatWarehouseRequestMaterial(WarehouseRequestMaterials requestMaterial);
    void updateWarehouseRequestMaterial(WarehouseRequestMaterials requestMaterial);
    WarehouseRequestMaterials findWarehouseRequestMaterials(Long requestId, Long warehouseId, Long materialId);
    List<WarehouseRequestMaterials> listWarehouseRequestMaterials(Long requestId, String ownerType, Long ownerId);
    List<WarehouseRequestMaterials> listUnDeliveryWarehouseRequestMaterials(Long requestId, String ownerType, Long ownerId);
    List<WarehouseRequestMaterials> listWarehouseRequestMaterials(List<Long> ids, String ownerType, Long ownerId);

    List<WarehouseStocks> listMaterialStocks(Long materialId, String ownerType, Long ownerId);
}
