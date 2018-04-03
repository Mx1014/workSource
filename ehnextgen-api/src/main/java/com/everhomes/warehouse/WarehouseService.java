package com.everhomes.warehouse;

import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.rest.user.admin.ImportDataResponse;
import com.everhomes.rest.warehouse.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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

//    ImportDataResponse importWarehouseMaterialCategories(ImportOwnerCommand cmd, MultipartFile mfile, Long userId);
//    ImportDataResponse importWarehouseMaterials(ImportOwnerCommand cmd, MultipartFile mfile, Long userId);
    ImportFileTaskDTO importWarehouseMaterialCategories(ImportOwnerCommand cmd, MultipartFile mfile, Long userId);
    ImportFileTaskDTO importWarehouseMaterials(ImportOwnerCommand cmd, MultipartFile mfile, Long userId);
    HttpServletResponse exportWarehouseStockLogs(SearchWarehouseStockLogsCommand cmd, HttpServletResponse response);

    void createRequest(CreateRequestCommand cmd);
    WarehouseRequestDetailsDTO findRequest(FindRequestCommand cmd);

    SearchRequestsResponse searchOneselfRequests(SearchOneselfRequestsCommand cmd);
    SearchRequestsResponse searchRequests(SearchRequestsCommand cmd);
}
