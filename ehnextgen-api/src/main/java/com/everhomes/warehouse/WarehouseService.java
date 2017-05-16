package com.everhomes.warehouse;

import com.everhomes.rest.warehouse.*;

import java.util.List;

/**
 * Created by ying.xiong on 2017/5/10.
 */
public interface WarehouseService {

    WarehouseDTO updateWarehouse(UpdateWarehouseCommand cmd);
    void deleteWarehouse(DeleteWarehouseCommand cmd);
    WarehouseDTO findWarehouse(DeleteWarehouseCommand cmd);

    WarehouseMaterialCategoryDTO updateWarehouseMaterialCategory(UpdateWarehouseMaterialCategoryCommand cmd);
    void deleteWarehouseMaterialCategory(DeleteWarehouseMaterialCategoryCommand cmd);
    WarehouseMaterialCategoryDTO findWarehouseMaterialCategory(DeleteWarehouseMaterialCategoryCommand cmd);
    WarehouseMaterialCategoryDTO listWarehouseMaterialCategory(DeleteWarehouseMaterialCategoryCommand cmd);

    WarehouseMaterialDTO updateWarehouseMaterial(UpdateWarehouseMaterialCommand cmd);
    void deleteWarehouseMaterial(DeleteWarehouseMaterialCommand cmd);
    WarehouseMaterialDTO findWarehouseMaterial(DeleteWarehouseMaterialCommand cmd);
    void updateWarehouseStock(UpdateWarehouseStockCommand cmd);

    List<WarehouseMaterialUnitDTO> listWarehouseMaterialUnits(ListWarehouseMaterialUnitsCommand cmd);

    void updateWarehouseMaterialUnit(UpdateWarehouseMaterialUnitCommand cmd);
    WarehouseMaterialUnitDTO findWarehouseMaterialUnit(DeleteWarehouseMaterialUnitCommand cmd);

}
