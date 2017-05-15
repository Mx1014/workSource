package com.everhomes.warehouse;

import com.everhomes.rest.warehouse.*;

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

    WarehouseMaterialDTO updateWarehouseMaterial(UpdateWarehouseMaterialCommand cmd);
    void deleteWarehouseMaterial(DeleteWarehouseMaterialCommand cmd);
    WarehouseMaterialDTO findWarehouseMaterial(DeleteWarehouseMaterialCommand cmd);

}
