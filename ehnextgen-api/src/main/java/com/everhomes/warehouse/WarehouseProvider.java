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
    void deleteWarehouseMaterialCategories(Long id);

    void creatWarehouseMaterials(WarehouseMaterials materials);
    void updateWarehouseMaterials(WarehouseMaterials materials);
    WarehouseMaterials findWarehouseMaterials(Long id, String ownerType, Long ownerId);
    WarehouseMaterials findWarehouseMaterialsByNumber(String materialNumber, String ownerType, Long ownerId);
    List<WarehouseMaterials> listWarehouseMaterialsByCategory(Long categoryId, String ownerType, Long ownerId);
    void deleteWarehouseMaterials(Long id);

    List<WarehouseStocks> listWarehouseStocks(Long warehouseId, String ownerType, Long ownerId);
    Long getWarehouseStockAmount(Long warehouseId, String ownerType, Long ownerId);
    Long getWarehouseStockAmountByMaterialId(Long materialId, String ownerType, Long ownerId);
    WarehouseUnits findWarehouseUnits(Long id, String ownerType, Long ownerId);

    List<Warehouses> listWarehouses(CrossShardListingLocator locator, Integer pageSize);
    List<WarehouseMaterialCategories> listWarehouseMaterialCategories(CrossShardListingLocator locator, Integer pageSize);
    List<WarehouseMaterials> listWarehouseMaterials(CrossShardListingLocator locator, Integer pageSize);
}
